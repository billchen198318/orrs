package org.qifu.core.util;

import org.apache.commons.lang3.StringUtils;
import org.qifu.core.model.ChatResponseContentType;

public class ChatResponseContent {
	private static final String CODE_START_HTML = "```html";
	private static final String CODE_START_JAVA = "```java";
	private static final String CODE_END = "```";
	
	private static String getBlockValue(String str, ChatResponseContentType type) {
		String val = StringUtils.defaultString(str);
		int f = -1;
		int e = -1;
		int fAdd = 0;
		if (type == ChatResponseContentType.JAVA) {
			f = val.indexOf(CODE_START_JAVA);
			e = val.lastIndexOf(CODE_END);
			fAdd = CODE_START_JAVA.length();
		}
		if (type == ChatResponseContentType.HTML) {
			f = val.indexOf(CODE_START_HTML);
			e = val.lastIndexOf(CODE_END);	
			fAdd = CODE_START_HTML.length();
		}
		if (f >= 0 && f < e && e >= 10) {
			val = val.substring(f+fAdd, e);
		}		
		return val;
	}
	
	public static String parse(String str, ChatResponseContentType type) {
		String val = getBlockValue(str, type);
		if (type == ChatResponseContentType.JAVA) {
			if (val.length() >= CODE_START_JAVA.length() && val.startsWith(CODE_START_JAVA) && val.endsWith(CODE_END)) {
				val = val.substring(CODE_START_JAVA.length(), val.length());
				val = val.substring(0, val.length()-3);
			}
		}
		if (type == ChatResponseContentType.HTML) {
			if (val.length() >= CODE_START_HTML.length() && val.startsWith(CODE_START_HTML) && val.endsWith(CODE_END)) {
				val = val.substring(CODE_START_HTML.length(), val.length());
				val = val.substring(0, val.length()-3);
			}
		}
		return val;
	}
	
}
