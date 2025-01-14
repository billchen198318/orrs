/* 
 * Copyright 2012-2016 bambooCORE, greenstep of copyright Chen Xin Nien
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * -----------------------------------------------------------------------
 * 
 * author: 	Chen Xin Nien
 * contact: chen.xin.nien@gmail.com
 * 
 */
package org.qifu.core.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.qifu.base.AppContext;
import org.qifu.base.Constants;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.YesNoKeyProvide;
import org.qifu.base.properties.BaseInfoConfigProperties;
import org.qifu.core.entity.TbSysUpload;
import org.qifu.core.model.UploadTypes;
import org.qifu.core.service.ISysUploadService;
import org.qifu.util.SimpleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UploadSupportUtils {
	protected static Logger logger = LoggerFactory.getLogger(UploadSupportUtils.class);
	public static final String HELP_EXPRESSION_VARIABLE = "datas";
	private static final long DEFAULT_UPLOAD_MAX_SIZE = 8388608; // default max 8MB 
	private static long uploadMaxSize = DEFAULT_UPLOAD_MAX_SIZE;
	private static Properties props = new Properties();
	private static String[] viewModeFileExtension = null;	
	
	private static BaseInfoConfigProperties baseInfoConfigProperties;
	
	private static ISysUploadService<TbSysUpload, String> sysUploadService;
	
	static {
		baseInfoConfigProperties = AppContext.getContext().getBean( BaseInfoConfigProperties.class );
		sysUploadService = AppContext.getContext().getBean( ISysUploadService.class );
		try {
			props.load(UploadSupportUtils.class.getClassLoader().getResource("upload-support-utils.properties").openStream());
			viewModeFileExtension = SimpleUtils.getStr(props.getProperty("FILE_EXTENSION")).trim().split(",");
			uploadMaxSize = NumberUtils.toLong(props.getProperty("UPLOAD_MAX_SIZE"), 0);
			if (uploadMaxSize < 1048576) { // 1MB binary byte = 1048576
				uploadMaxSize = 1048576;
			}
			if (uploadMaxSize > DEFAULT_UPLOAD_MAX_SIZE) {
				uploadMaxSize = DEFAULT_UPLOAD_MAX_SIZE;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public static long getUploadMaxSize() {
		return uploadMaxSize;
	}
	
	public static String getViewMode(String fileShowName) {
		String viewMode = YesNoKeyProvide.NO;
		String fileExtensionName = StringUtils.defaultString( getFileExtensionName(fileShowName) ).trim().toLowerCase();
		for (int i=0; viewModeFileExtension!=null && i<viewModeFileExtension.length; i++) {
			if (viewModeFileExtension[i].toLowerCase().equals(fileExtensionName)) {
				viewMode = YesNoKeyProvide.YES;
			}
		}
		return viewMode;
	}
	
	/**
	 * 把 上傳(upload) 的XML檔轉成Object 
	 * 
	 * @param uploadOid
	 * @param classesToBeBound
	 * @return
	 * @throws ServiceException
	 * @throws IOException 
	 * @throws JAXBException 
	 */
	public Object getTransformObjectData(String uploadOid, Class<?> classesToBeBound) throws ServiceException, IOException, JAXBException {
		Object result = null;
		byte[] xmlBytes = getDataBytes(uploadOid);
		JAXBContext jaxbContext = JAXBContext.newInstance(classesToBeBound);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		result = jaxbUnmarshaller.unmarshal( new ByteArrayInputStream(xmlBytes) );		
		return result;
	}
	
	public static void cleanTempUpload() throws ServiceException {		
		cleanTempUpload( baseInfoConfigProperties.getSystem() );
	}
	
	/**
	public static void cleanTempUpload(String system) throws ServiceException {
		logger.info("clean upload({}) temp begin...", system);
		//sysUploadService.deleteTmpContentBySystem(system); // 2020-06-27 rem
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("system", system);
		paramMap.put("type", UploadTypes.IS_TEMP);
		//paramMap.put("isFile", YesNo.YES); // 2021-05-20 rem
		DefaultResult<List<TbSysUpload>> searchListResult = sysUploadService.selectListByParams(paramMap);	
		DateTime currentDateTime = new DateTime();
		for (int i=0; searchListResult.getValue() != null && i < searchListResult.getValue().size(); i++) {
			TbSysUpload entity = searchListResult.getValue().get(i);
			// --------------------------------------------------------------------
			// 2021-05-20 add
			if (entity.getCdate() == null) {
				logger.warn("upload temp file null create date time, cannot remove, upload oid: {}", entity.getOid());
				continue;
			}
			DateTime createDateTime = new DateTime(entity.getCdate());
			Duration duration = new Duration(createDateTime, currentDateTime);
			if (duration.getStandardHours() < 4) {
				logger.warn("upload temp file no over remove check time(hour-{}, min-{}), cannot remove, upload oid: {}", duration.getStandardHours(), duration.getStandardMinutes(), entity.getOid());
				continue;
			}
			if (!YesNoKeyProvide.YES.equals(entity.getIsFile())) {
				logger.warn("delete upload not real file type, upload oid : {} , show-name: {}", entity.getOid(), entity.getShowName());
				sysUploadService.delete(entity);
				continue;
			}
			// --------------------------------------------------------------------
			String dir = getUploadFileDir(entity.getSystem(), entity.getSubDir(), entity.getType());
			String fileFullPath = dir + File.separator + entity.getFileName();
			File file = new File(fileFullPath);
			if (!file.exists()) {
				logger.warn("upload temp file no exists, upload oid: {}", entity.getOid());
				continue;
			}
			try {
				logger.warn("delete : {}", file.getPath());
				FileUtils.forceDelete(file);
				sysUploadService.delete(entity);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
		logger.info("end...");
	}	
	*/
	
	public static void cleanTempUpload(String system) throws ServiceException {
	    logger.info("clean upload({}) temp begin...", system);
	    Map<String, Object> paramMap = new HashMap<>();
	    paramMap.put("system", system);
	    paramMap.put("type", UploadTypes.IS_TEMP);
	    
	    DefaultResult<List<TbSysUpload>> searchListResult = sysUploadService.selectListByParams(paramMap);
	    DateTime currentDateTime = new DateTime();
	    
	    for (int i = 0; searchListResult.getValue() != null && i < searchListResult.getValue().size(); i++) {
	        TbSysUpload entity = searchListResult.getValue().get(i);
	        
	        // Combine all validation checks into a single if condition
	        if (shouldProcessFile(entity, currentDateTime)) {
	            String dir = getUploadFileDir(entity.getSystem(), entity.getSubDir(), entity.getType());
	            String fileFullPath = dir + File.separator + entity.getFileName();
	            File file = new File(fileFullPath);
	            
	            if (file.exists()) {
	                try {
	                    logger.warn("delete : {}", file.getPath());
	                    FileUtils.forceDelete(file);
	                    sysUploadService.delete(entity);
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            } else {
	                logger.warn("upload temp file no exists, upload oid: {}", entity.getOid());
	            }
	        }
	    }
	    logger.info("end...");
	}

	// Helper method to consolidate validation logic
	private static boolean shouldProcessFile(TbSysUpload entity, DateTime currentDateTime) {
	    // Check if create date is null
	    if (entity.getCdate() == null) {
	        logger.warn("upload temp file null create date time, cannot remove, upload oid: {}", entity.getOid());
	        return false;
	    }

	    // Check duration
	    DateTime createDateTime = new DateTime(entity.getCdate());
	    Duration duration = new Duration(createDateTime, currentDateTime);
	    if (duration.getStandardHours() < 4) {
	        logger.warn("upload temp file no over remove check time(hour-{}, min-{}), cannot remove, upload oid: {}", 
	            duration.getStandardHours(), duration.getStandardMinutes(), entity.getOid());
	        return false;
	    }

	    // Handle non-file type uploads
	    if (!YesNoKeyProvide.YES.equals(entity.getIsFile())) {
	        logger.warn("delete upload not real file type, upload oid : {} , show-name: {}", 
	            entity.getOid(), entity.getShowName());
	        sysUploadService.delete(entity);
	        return false;
	    }

	    return true;
	}
	
	public static String getSubDir() {
		return SimpleUtils.getStrYMD(SimpleUtils.IS_YEAR);
	}
	
	public static String getUploadFileDir(String system, String type) {
		return getUploadFileDir(system, getSubDir(), type);
	}
	
	public static String getUploadFileDir(String system, String subDir, String type) {
		if (StringUtils.isBlank(system) || StringUtils.isBlank(subDir) || StringUtils.isBlank(type)) {
			throw new java.lang.IllegalArgumentException("system, sub-dir and type cann't blank!");
		}
		return baseInfoConfigProperties.getUploadDir() + "/" + system + "/" + type + "/" + subDir + "/";
	}	
	
	public static File mkdirUploadFileDir(String system, String type) throws IOException {
		return mkdirUploadFileDir(system, getSubDir(), type);
	}
	
	public static File mkdirUploadFileDir(String system, String subDir, String type) throws IOException {
		String uploadDir = getUploadFileDir(system, subDir, type);
		File dir = new File(uploadDir);
		if (dir.exists() && dir.isDirectory()) {
			return dir;
		}
		FileUtils.forceMkdir(dir);
		return dir;
	}
	
	public static String generateRealFileName(String showName) {
		if (StringUtils.isBlank(showName)) {
			throw new java.lang.IllegalArgumentException("name is blank!");
		}
		String extension = getFileExtensionName(showName);
		if (StringUtils.isBlank(extension)) {
			return SimpleUtils.getUUIDStr();
		}
		if (extension.length()>13) { // uuid 加上 點 "." 後是 37 字元 , TB_SYS_UPLOAD.FILE_NAME 最大為 50 , 50-37 = 13
			extension = extension.substring(0, 13);
		}
		return SimpleUtils.getUUIDStr() + "." + extension;
	}	
	
	public static String generateRealFileName(File file) {
		if (file==null) {
			throw new java.lang.IllegalArgumentException("file is null!");
		}
		return generateRealFileName( file.getName() );
	}
	
	public static File getRealFile(String uploadOid) throws ServiceException, IOException {
		if (StringUtils.isBlank(uploadOid)) {
			throw new IllegalArgumentException(BaseSystemMessage.parameterBlank());
		}
		TbSysUpload uploadObj = findUpload(uploadOid);
		File packageFile = null;
		if (!YesNoKeyProvide.YES.equals(uploadObj.getIsFile())) {
			File dir = new File( Constants.getWorkTmpDir() + File.separator + UploadSupportUtils.class.getSimpleName() );
			if (!dir.exists() || !dir.isDirectory()) {
				FileUtils.forceMkdir(dir);
			}			
			String tmpFileName = dir.getPath() + File.separator + SimpleUtils.getUUIDStr() + "." + getFileExtensionName(uploadObj.getShowName());
			packageFile = new File( tmpFileName );
			try (OutputStream fos = new FileOutputStream(packageFile)) {
				IOUtils.write(uploadObj.getContent(), fos);		
				fos.flush();				
			}			
		} else {
			String uploadDir = getUploadFileDir(uploadObj.getSystem(), uploadObj.getSubDir(), uploadObj.getType());
			packageFile = new File( uploadDir + File.separator + uploadObj.getFileName() );			
		}		
		if (!packageFile.exists()) {
			throw new IOException("File is missing: " + uploadObj.getFileName() );
		}
		return packageFile;
	}
	
	public static byte[] getDataBytes(String uploadOid) throws ServiceException, IOException {
		if (StringUtils.isBlank(uploadOid)) {
			throw new IllegalArgumentException(BaseSystemMessage.parameterBlank());
		}
		byte[] datas = null;
		TbSysUpload uploadObj = findUpload(uploadOid);
		datas = uploadObj.getContent();
		if (YesNoKeyProvide.YES.equals(uploadObj.getIsFile())) {
			String uploadDir = getUploadFileDir(uploadObj.getSystem(), uploadObj.getSubDir(), uploadObj.getType());
			File file = new File( uploadDir + File.separator + uploadObj.getFileName() );
			datas = FileUtils.readFileToByteArray(file);
		}
		return datas;
	}
	
	public static DefaultResult<Boolean> updateType(String oid, String type) throws ServiceException, IOException {
		DefaultResult<TbSysUpload> uploadResult = sysUploadService.selectByPrimaryKeySimple(oid);
		if (uploadResult.getValue()==null) {
			throw new ServiceException(uploadResult.getMessage());
		}
		DefaultResult<Boolean> result = new DefaultResult<>();
		result.setValue( false );
		result.setMessage( BaseSystemMessage.updateFail() );
		TbSysUpload upload = uploadResult.getValue();
		if (!YesNoKeyProvide.YES.equals(upload.getIsFile())) {
			upload.setType(type);
			uploadResult = sysUploadService.update(upload);			
			if (uploadResult.getValue() != null) {
				result.setValue( true );
				result.setMessage( uploadResult.getMessage() );
			}
			return result;
		}
		
		// move file to new dir.
		String oldFullPath = getUploadFileDir(upload.getSystem(), upload.getSubDir(), upload.getType()) + upload.getFileName();
		String newFullPath = getUploadFileDir(upload.getSystem(), upload.getSubDir(), type) + upload.getFileName();
		File newFile = new File(newFullPath);
		if (newFile.isFile() && newFile.exists()) {
			throw new IllegalArgumentException("error. file exists, cannot operate!");
		}
		File oldFile = new File(oldFullPath);
		if (!oldFile.exists()) {
			throw new IllegalArgumentException("error. file no exists: " + oldFullPath);
		}
		FileUtils.moveFile(oldFile, newFile);
		upload.setType(type);
		sysUploadService.update(upload);
		return result;
	}
	
	public static String create(String system, String type, boolean isFile, File file, String showName) throws ServiceException, IOException {
		if (StringUtils.isBlank(type) || null == file || StringUtils.isBlank(showName)) {
			throw new IllegalArgumentException(BaseSystemMessage.parameterBlank());
		}				
		if (!file.exists()) {
			throw new IllegalArgumentException("file no exists!");
		}
		TbSysUpload upload = new TbSysUpload();		
		upload.setIsFile( ( isFile ? YesNoKeyProvide.YES : YesNoKeyProvide.NO ) );
		upload.setShowName(showName);
		upload.setSystem(system);
		upload.setType(type);
		upload.setSubDir( getSubDir() );		
		if (isFile) {
			String uploadDir = getUploadFileDir(system, type);
			String uploadFileName = generateRealFileName(file);
			mkdirUploadFileDir(system, type);
			FileUtils.copyFile(file, new File(uploadDir + File.separator + uploadFileName) );
			upload.setFileName( uploadFileName );			
		} else {
			upload.setContent( FileUtils.readFileToByteArray(file) );
			upload.setFileName( " " );			
		}		
		DefaultResult<TbSysUpload> result =  sysUploadService.insert(upload);
		if ( result.getValue() == null ) {
			throw new ServiceException( result.getMessage() );
		}		
		return result.getValue().getOid();
	}
	
	public static String create(String system, String type, boolean isFile, byte[] datas, String showName) throws ServiceException, IOException {
		if (StringUtils.isBlank(type) || null == datas || StringUtils.isBlank(showName)) {
			throw new IllegalArgumentException(BaseSystemMessage.parameterBlank());
		}				
		TbSysUpload upload = new TbSysUpload();		
		upload.setIsFile( ( isFile ? YesNoKeyProvide.YES : YesNoKeyProvide.NO ) );
		upload.setShowName(showName);
		upload.setSystem(system);
		upload.setType(type);
		upload.setSubDir( getSubDir() );		
		if (isFile) {
			String uploadDir = getUploadFileDir(system, type);
			String uploadFileName = generateRealFileName(showName);
			mkdirUploadFileDir(system, type);
			File file = new File( uploadDir + File.separator + uploadFileName );
			FileUtils.writeByteArrayToFile(file, datas); 
			upload.setFileName( uploadFileName );			
		} else {
			upload.setContent( datas );
			upload.setFileName( " " );			
		}		
		DefaultResult<TbSysUpload> result =  sysUploadService.insert(upload);
		if ( result.getValue() == null ) {
			throw new ServiceException( result.getMessage() );
		}		
		return result.getValue().getOid();
	}	
	
	public static String getFileExtensionName(String fileFullName) {
		if (StringUtils.isBlank(fileFullName)) {
			return "";
		}
		String extension = "";
		String[] tmp = fileFullName.split("[.]");
		for (int i=1; tmp!=null && i<tmp.length; i++) {
			extension = tmp[i];
		}
		return extension;
	}
	
	public static TbSysUpload findUpload(String uploadOid) throws ServiceException {
		if (StringUtils.isBlank(uploadOid)) {
			throw new IllegalArgumentException("Upload OID parameter is blank!");
		}		
		DefaultResult<TbSysUpload> result = sysUploadService.selectByPrimaryKey(uploadOid);
		if (result.getValue()==null) {
			throw new ServiceException(result.getMessage());
		}
		return result.getValue();
	}
	
	public static TbSysUpload findUploadNoByteContent(String uploadOid) throws ServiceException {
		if (StringUtils.isBlank(uploadOid)) {
			throw new IllegalArgumentException("Upload OID parameter is blank!");
		}		
		DefaultResult<TbSysUpload> result = sysUploadService.selectByPrimaryKeySimple(uploadOid);
		if (result.getValue()==null) {
			throw new ServiceException(result.getMessage());
		}
		return result.getValue();
	}
	
}
