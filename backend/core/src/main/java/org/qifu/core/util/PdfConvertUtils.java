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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.qifu.base.Constants;
import org.qifu.base.exception.ServiceException;

public class PdfConvertUtils {
	
	protected PdfConvertUtils() {
		throw new IllegalStateException("Utils class: PdfConvertUtils");
	}
	
	public static List<String> toImageUpload(File pdfFile, int resolution, 
			String system, String uploadType, boolean isFile) throws ServiceException, IOException {
		List<String> oids = new LinkedList<>();
		List<File> imageFiles = toImageFiles(pdfFile, resolution);
		for (File file : imageFiles) {
			oids.add(
					UploadSupportUtils.create(
							system, uploadType, isFile, file, file.getName())
			);
		}
		return oids;
	}
	
	public static List<File> toImageFiles(File pdfFile, int resolution) throws IOException {
		PDDocument document = Loader.loadPDF(pdfFile);	
		PDFRenderer pdfRenderer = new PDFRenderer(document);
		File tmpDir = new File(Constants.getWorkTmpDir() + File.separator + PdfConvertUtils.class.getSimpleName() 
				+ File.separator + System.currentTimeMillis() + File.separator);
		FileUtils.forceMkdir( tmpDir );
		List<File> files = new LinkedList<>();
		int len = String.valueOf(document.getDocumentCatalog().getPages().getCount()+1).length();
		for (int i=0; i<document.getDocumentCatalog().getPages().getCount(); i++) {
			String name = StringUtils.leftPad(String.valueOf(i+1), len, "0");
			BufferedImage bufImage = pdfRenderer.renderImageWithDPI(i, resolution, ImageType.RGB);
			File imageFile = new File( tmpDir.getPath() + File.separator + name + ".png" );
			FileOutputStream fos = new FileOutputStream(imageFile);
			ImageIOUtil.writeImage(bufImage, "png", fos, resolution);
			fos.flush();
			fos.close();
			files.add(imageFile);
		}
		document.close();
		return files;
	}
	
}