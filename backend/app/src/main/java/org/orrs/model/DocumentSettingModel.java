package org.orrs.model;

import java.io.IOException;

import org.qifu.util.LoadResources;

public class DocumentSettingModel {
	
	private static DocumentSetting documentSetting = null;
	
	protected DocumentSettingModel() { }
	
	static {
		try {
			documentSetting = LoadResources.objectMapperReadValue("documentSetting.json", DocumentSetting.class, DocumentSettingModel.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static DocumentSetting getDocumentSetting() {
		return documentSetting;
	}
	
}
