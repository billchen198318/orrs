/* 
 * Copyright 2012-2018 qifu, qifu2 of copyright Chen Xin Nien
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.qifu.base.exception.ServiceException;
import org.qifu.core.model.UploadTypes;
import org.qifu.util.SimpleUtils;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.parser.PdfTextExtractor;

public class PdfCopyPageBuilder {
	private byte[] sourceReportData = null; 
	private List<String> findWords = new ArrayList<>();
	private String readPassword = "";
	private boolean modeByFindFound = true;
	private boolean forcePutAllPage = false;
	
	public static PdfCopyPageBuilder build() {
		return new PdfCopyPageBuilder();
	}
	
	public PdfCopyPageBuilder setSourceReportData(byte[] content) {
		if (null == content) {
			throw new IllegalArgumentException("null data.");
		}
		this.sourceReportData = content;
		return this;
	}
	
	public PdfCopyPageBuilder setSourcePdfFile(File file) throws IOException {
		if (null == file || !file.exists() || !file.isFile()) {
			throw new IllegalArgumentException("file args error.");
		}
		this.setSourceReportData( FileUtils.readFileToByteArray(file) );
		return this;
	}
	
	public PdfCopyPageBuilder addFindWord(String word) {
		if (StringUtils.isBlank(word)) {
			return this;
		}
		if (!this.findWords.contains(word)) {
			this.findWords.add(word);
		}
		return this;
	}
	
	public PdfCopyPageBuilder setReadPassword(String password) {
		this.readPassword = password;
		return this;
	}
	
	public PdfCopyPageBuilder setModeByFindFound(boolean mode) {
		this.modeByFindFound = mode;
		return this;
	}
	
	public PdfCopyPageBuilder setForcePutAllPage(boolean forcePutAllPage) {
		this.forcePutAllPage = forcePutAllPage;
		return this;
	}
	
	public String toUpload(String system) throws ServiceException, IOException {
		return this.toUpload(system, UploadTypes.IS_TEMP);
	}
	
	public String toUpload(String system, String uploadType) throws ServiceException, IOException {
		byte[] newPdfByte = this.getContent();
		return UploadSupportUtils.create(system, uploadType, false, newPdfByte, SimpleUtils.getUUIDStr()+".pdf");
	}	
	
	public byte[] getContent() throws IOException {
	    if (!this.modeByFindFound && this.findWords.size() > 1) {
	        throw new IllegalArgumentException("modeByFindFound set to false, cannot add many find word.");
	    }

	    try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
	        PdfReader pdfReader = createPdfReader();
	        Document document = new Document(pdfReader.getPageSizeWithRotation(1));
	        boolean isCutPdf = false;
	        
	        try (PdfCopy pdfCopy = new PdfCopy(document, os)) {
	            document.open();
	            PdfTextExtractor pdfTextExtractor = new PdfTextExtractor(pdfReader);
	            
	            for (int i = 1; i <= pdfReader.getNumberOfPages(); i++) {
	                String text = pdfTextExtractor.getTextFromPage(i);
	                boolean needPut = shouldIncludePage(text);
	                if (needPut) {
                        PdfImportedPage page = pdfCopy.getImportedPage(pdfReader, i);
                        pdfCopy.addPage(page);
                        isCutPdf = true;
	                }
	            }
	        } 
	        // Handle case where no pages were included (generate an empty document)
	        if (!isCutPdf) {
	            generateEmptyDocument(os);
	        }
	        return os.toByteArray();
	    } catch (IOException e) {
	        e.printStackTrace();
	        throw e;  // Re-throw to notify the caller of an IO exception
	    }
	}

	// Helper method to create PdfReader with or without password
	private PdfReader createPdfReader() throws IOException {
	    if (StringUtils.isBlank(this.readPassword)) {
	        return new PdfReader(sourceReportData);
	    } else {
	        return new PdfReader(sourceReportData, readPassword.getBytes());
	    }
	}

	// Helper method to determine if the page should be included based on the mode and find words
	private boolean shouldIncludePage(String text) {
	    if (this.forcePutAllPage) {
	        return true;
	    }

	    for (String word : this.findWords) {
	        boolean found = text.contains(word);
	        if ((this.modeByFindFound && found) || (!this.modeByFindFound && !found)) {
	            return true;
	        }
	    }
	    return false;
	}

	// Helper method to generate an empty document with some space
	private void generateEmptyDocument(ByteArrayOutputStream os) {
	    try (Document emptyDocument = new Document()) {
	        PdfWriter.getInstance(emptyDocument, os);
	        emptyDocument.open();
	        addEmptyParagraphs(emptyDocument);
	    } catch (DocumentException e) {
	        e.printStackTrace();
	    }
	}
	
	private void addEmptyParagraphs(Document document) throws DocumentException {		
		Paragraph paragraph = new Paragraph(" ");
		paragraph.setAlignment(Element.ALIGN_RIGHT);
		document.add(paragraph);
		paragraph = new Paragraph(" ");
		paragraph.setAlignment(Element.ALIGN_CENTER);
		document.add(paragraph);
		paragraph = new Paragraph(" ");
		paragraph.setAlignment(Element.ALIGN_LEFT);
		document.add(paragraph);
		paragraph = new Paragraph(" ");
		paragraph.setAlignment(Element.ALIGN_LEFT);
		paragraph.setIndentationLeft(50);    			
		document.add(paragraph);					
	}
	
}