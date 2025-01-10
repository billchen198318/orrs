/* 
 * Copyright 2012-2019 qifu, qifu2 of copyright Chen Xin Nien
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
package org.qifu.util;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.qifu.base.Constants;

import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;

public class PdfOwnerUserBuilder {
	private String pdfFileFullPath = "";
	private String destEncryptionPdfPath = "";
	private String owner = "PdfOwnerUserBuilder";
	private String user = "PdfOwnerUserBuilder";
	private String watermarkText = "";
	
	public static PdfOwnerUserBuilder build() {
		return new PdfOwnerUserBuilder();
	}
	
	public PdfOwnerUserBuilder watermarkText(String watermarkText) {
		if (!StringUtils.isBlank(watermarkText)) {
			this.watermarkText = watermarkText;
		}
		return this;
	}
	
	public PdfOwnerUserBuilder owner(String owner) {
		if (!StringUtils.isBlank(owner)) {
			this.owner = owner;
		}
		return this;
	}
	
	public PdfOwnerUserBuilder user(String user) {
		if (!StringUtils.isBlank(user)) {
			this.user = user;
		}
		return this;
	}
	
	public PdfOwnerUserBuilder destFile(String fullPath) {
		this.destEncryptionPdfPath = fullPath;
		return this;
	}
	
	public PdfOwnerUserBuilder destFileToTmpdir() {
		String fullPath = System.getProperty("java.io.tmpdir") + Constants.FORWARD_SLASH + PdfOwnerUserBuilder.class.getSimpleName() + Constants.FORWARD_SLASH + System.currentTimeMillis() + Constants.FORWARD_SLASH + System.currentTimeMillis()+".pdf";
		this.destFile(fullPath);
		return this;
	}
	
	public PdfOwnerUserBuilder sourceFile(String fullPath) throws IOException {
		File f = new File(fullPath);
		if (!f.exists()) {
			throw new IOException("no exists file : " + fullPath);
		}
		this.pdfFileFullPath = fullPath;
		return this;
	}
	
	public PdfOwnerUserBuilder process() throws IOException {
		if (StringUtils.isBlank(this.destEncryptionPdfPath) || StringUtils.isBlank(this.pdfFileFullPath)) {
			throw new IOException("no exists dest file : " + destEncryptionPdfPath + " or source file : " + this.pdfFileFullPath);
		}
		File destEncryptionPdfFile = new File(this.destEncryptionPdfPath);
		if (!destEncryptionPdfFile.getParentFile().exists()) {
			FileUtils.forceMkdir(destEncryptionPdfFile.getParentFile());
		}
		destEncryptionPdfFile = null;
		try (FileOutputStream destEncryptionPdfFileOs = new FileOutputStream(this.destEncryptionPdfPath)) {
			try (PdfReader reader = new PdfReader(this.pdfFileFullPath)) {
				PdfStamper stamper = new PdfStamper(reader, destEncryptionPdfFileOs);
				stamper.setEncryption(
						this.user.getBytes(), 
						this.owner.getBytes(), 
						PdfWriter.ALLOW_PRINTING, 
						PdfWriter.ENCRYPTION_AES_128);
				if (!StringUtils.isBlank(this.watermarkText)) {
					Font font = FontFactory.getFont("fonts/fireflysung.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
					PdfWatermarkUtils.addWatermark(stamper, font.getBaseFont(), Color.RED, this.watermarkText);
				}
				stamper.close();				
			}
		}
		return this;
	}
	
	public String destFileFullPath() {
		return this.destEncryptionPdfPath;
	}	
	
	
}