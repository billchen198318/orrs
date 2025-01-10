/* 
 * Copyright 2019-2021 qifu of copyright Chen Xin Nien
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.qifu.base.AppContext;
import org.qifu.base.Constants;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.YesNoKeyProvide;
import org.qifu.base.properties.JasperreportConfigProperties;
import org.qifu.core.entity.TbSysJreport;
import org.qifu.core.entity.TbSysJreportParam;
import org.qifu.core.service.ISysJreportParamService;
import org.qifu.core.service.ISysJreportService;
import org.qifu.util.DataUtils;
import org.qifu.util.SimpleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.lingala.zip4j.ZipFile;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

public class JReportUtils {
	
	protected static Logger logger = LoggerFactory.getLogger(JReportUtils.class);
	
	private static JasperreportConfigProperties jasperreportConfigProperties;
	
	private static ISysJreportService<TbSysJreport, String> sysJreportService;
	
	private static ISysJreportParamService<TbSysJreportParam, String> sysJreportParamService;
	
	private static final String JASPER_SUB_NAME = ".jasper";
	private static final String JRXML_SUB_NAME = ".jrxml";
	
	protected JReportUtils() {
		throw new IllegalStateException("Utils class: JReportUtils");
	}
	
	
	static {
		jasperreportConfigProperties = AppContext.getContext().getBean(JasperreportConfigProperties.class);
		
		sysJreportService = AppContext.getContext().getBean(ISysJreportService.class);
		
		sysJreportParamService = AppContext.getContext().getBean(ISysJreportParamService.class);
	}
	
	private static String getDataSourceIdByReportId(String reportId) {
		if ("TEST".equals(reportId)) {
			return "dataSourceTest"; // for TEST
		}
		return "dataSource";
	}
	
	private static Connection getConnectionByReportId(String reportId) {
		return DataUtils.getConnection( getDataSourceIdByReportId(reportId) );
	}	
	
	public static void fillReportToResponse(String reportId, HttpServletRequest request, HttpServletResponse response) throws ServiceException, IOException {
		Map<String, Object> paramMap = getParameter(reportId, request);
		fillReportToResponse(reportId, paramMap, response);
	}
	
	public static void fillReportToResponse(String reportId, Map<String, Object> paramMap, HttpServletResponse response) throws ServiceException, IOException {
		if (StringUtils.isBlank(reportId)) {
			throw new java.lang.IllegalArgumentException("error, reportId is blank");
		}
		response.setContentType("application/pdf");
		String jasperFile = jasperreportConfigProperties.getSource() + File.separator + reportId + File.separator + reportId + JASPER_SUB_NAME;
		try (InputStream reportSource = new FileInputStream( jasperFile )) {
			fillReport(reportId, reportSource, paramMap, response.getOutputStream());
		}
	}
	
	public static void fillReportToByteArray(String reportId, Map<String, Object> paramMap, ByteArrayOutputStream outputStream) throws ServiceException, IOException {
		if (StringUtils.isBlank(reportId)) {
			throw new java.lang.IllegalArgumentException("error, reportId is blank");
		}
		String jasperFile = jasperreportConfigProperties.getSource() + File.separator + reportId + File.separator + reportId + JASPER_SUB_NAME;
		try (InputStream reportSource = new FileInputStream( jasperFile )) {
			fillReport(reportId, reportSource, paramMap, outputStream);
		}
	}
	
	private static void fillReport(String reportId, InputStream reportSource, Map<String, Object> paramMap, OutputStream outputStream) {
		Connection conn = null;
		try {
			conn = getConnectionByReportId(reportId);
		    JasperPrint jasperPrint = JasperFillManager.fillReport(
		            reportSource,
		            paramMap,
		            conn);
		    JRPdfExporter jrPdfExporter=new JRPdfExporter();
		    jrPdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		    jrPdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));		    
		    SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
		    jrPdfExporter.setConfiguration(configuration);
		    if ( !StringUtils.isBlank(jasperreportConfigProperties.getOwner()) ) {
		    	configuration.setOwnerPassword( jasperreportConfigProperties.getOwner() );
		    }
		    jrPdfExporter.exportReport();
		    outputStream.flush();
		    outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}			
			if (null != reportSource) {
				try {
					reportSource.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			reportSource = null;
		}		
		conn = null;
	}
	
	public static void deployReport(TbSysJreport report) throws ServiceException, IOException {		
		String reportDeployDirName = jasperreportConfigProperties.getSource() + File.separator;
		File reportDeployDir = new File(reportDeployDirName);
		try {
			if (!reportDeployDir.exists()) {
				logger.warn("no exists dir, force mkdir {}", reportDeployDirName);
				FileUtils.forceMkdir(reportDeployDir);
			}								
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		logger.info("REPORT-ID : {}", report.getReportId());
		File reportFile = null;
		File reportZipFile = null;			
		try {
			String reportFileFullPath = reportDeployDirName + report.getReportId() + File.separator + report.getFile();				
			String reportZipFileFullPath = reportDeployDirName + report.getReportId() + ".zip";		
			reportZipFile = new File(reportZipFileFullPath);
			if (reportZipFile.exists()) {
				logger.warn("delete {}", reportZipFileFullPath);
				FileUtils.forceDelete(reportZipFile);					
			}
			try (OutputStream os = new FileOutputStream(reportZipFile)) {
				IOUtils.write(report.getContent(), os);
				os.flush();
				try (ZipFile zipFile = new ZipFile( reportZipFileFullPath )) {
					zipFile.extractAll( reportDeployDirName );
					reportFile = new File( reportFileFullPath );
					if (!reportFile.exists()) {
						logger.warn("report file is missing : {}", reportFileFullPath);
						return;
					}
					if (YesNoKeyProvide.YES.equals(report.getIsCompile()) && report.getFile().endsWith(JRXML_SUB_NAME)) {
						logger.info("compile report...");
						File d = new File( reportDeployDirName + report.getReportId() );
						String outJasper = compileReportToJasperFile(d.listFiles());
						logger.info("out first : {}", outJasper);
					}						
				}			
			}
		} catch (JRException | IOException re) {
			re.printStackTrace();
			logger.error(re.getMessage());
		} finally {
			reportFile = null;
			reportZipFile = null;
		}
		reportDeployDir = null;		
	}
	
	public static void deploy() throws ServiceException {
		logger.info("begin deploy...");
		DefaultResult<List<TbSysJreport>> reportResult = sysJreportService.selectList();
		String reportDeployDirName = jasperreportConfigProperties.getSource() + File.separator;
		File reportDeployDir = new File(reportDeployDirName);
		try {
			if (reportDeployDir.exists()) {
				logger.warn("delete {}", reportDeployDirName);
				FileUtils.forceDelete(reportDeployDir);
			}	
			logger.warn("mkdir {}", reportDeployDirName);
			FileUtils.forceMkdir(reportDeployDir);					
			for (TbSysJreport report : reportResult.getValue()) {
				deployReport(report);
			}							
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}	
		logger.info("end deploy...");
	}
	
	/**
	 * jasperreport compile jrxml 成 jasper
	 * 
	 * @param sourceFile		如: File[]
	 * @return
	 * @throws JRException
	 */
	public static String compileReportToJasperFile(File[] sourceFile) throws JRException {
		String jasperFirst = "";
		for (int ix=0; sourceFile!=null && ix<sourceFile.length; ix++) {
			File srcFile = sourceFile[ix];
			if (!srcFile.exists() || srcFile.getName().indexOf(JRXML_SUB_NAME)==-1) {
				continue;
			}
			String destFileName=srcFile.getPath().replaceAll(JRXML_SUB_NAME, JASPER_SUB_NAME);
			if ("".equals(jasperFirst)) {
				jasperFirst = destFileName;
			}
			JasperCompileManager.compileReportToFile(srcFile.getPath(), destFileName);
			logger.info("out process : {}", destFileName);
		}
		return jasperFirst;
	}	
	
	/**
	 * jasperreport compile jrxml 成 jasper
	 * 
	 * @param sourceFileName	如: new String[]{ "C:/report-source/test.jrxml" }
	 * @return
	 * @throws JRException
	 */
	public static String compileReportToJasperFile(String[] sourceFileName) throws JRException {
		String jasperFirst = "";
		for (int ix=0; sourceFileName!=null && ix<sourceFileName.length; ix++) {
			File srcFile = new File(sourceFileName[ix]);
			if (!srcFile.exists() || srcFile.getName().indexOf(JRXML_SUB_NAME)==-1) {
				continue;
			}
			String destFileName=srcFile.getPath().replaceAll(JRXML_SUB_NAME, JASPER_SUB_NAME);
			if ("".equals(jasperFirst)) {
				jasperFirst = destFileName;
			}
			JasperCompileManager.compileReportToFile(srcFile.getPath(), destFileName);
			logger.info("out process : {}", destFileName);
		}
		return jasperFirst;
	}
	
	public static String selfTestDecompress4Upload(String uploadOid) throws ServiceException, IOException{
		String extractDir = Constants.getWorkTmpDir() + File.separator + JReportUtils.class.getSimpleName() + File.separator + SimpleUtils.getUUIDStr() + "/";
		File realFile = UploadSupportUtils.getRealFile(uploadOid);
		try (ZipFile zipFile = new ZipFile(realFile)) {
			zipFile.extractAll( extractDir );
		} 	
		return extractDir;
	}
	
	public static Map<String, Object> getParameter(String reportId, HttpServletRequest request) throws ServiceException {
		Map<String, Object> paramMap = new HashMap<>();
		if (StringUtils.isBlank(reportId)) {
			return paramMap;
		}
		paramMap.put("reportId", reportId);
		DefaultResult<List<TbSysJreportParam>> paramListResult = sysJreportParamService.selectListByParams(paramMap);
		paramMap.clear();
		for (int i=0; paramListResult.getValue() != null && i < paramListResult.getValue().size(); i++) {
			TbSysJreportParam sysJreportParam = paramListResult.getValue().get(i);
			Enumeration<String> urlParams = request.getParameterNames();
			while (urlParams.hasMoreElements()) {
				String p = urlParams.nextElement();
				if (p.equals(sysJreportParam.getUrlParam())) {
					String value = request.getParameter(p);
					paramMap.put(sysJreportParam.getRptParam(), value);
				}
			}
		}
		String reportFolder = jasperreportConfigProperties.getSource() + File.separator + reportId + File.separator;
		paramMap.put("REPORT_FOLDER", reportFolder);
		paramMap.put("SUBREPORT_DIR", reportFolder);		
		return paramMap;
	}
	
}
