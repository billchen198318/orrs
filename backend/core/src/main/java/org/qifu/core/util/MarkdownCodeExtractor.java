package org.qifu.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.qifu.core.model.MarkdownCodeType;

public class MarkdownCodeExtractor {
	private static final String _regex = "```(?:[a-zA-Z]*)\\s*([\\s\\S]*?)```";
	private static final Pattern _pattern = Pattern.compile(_regex);
	private static final String CODE_START_HTML = "```html";
	private static final String CODE_START_JAVA = "```java";
	private static final String CODE_END = "```";
	
	private static String getBlockValue(String str, MarkdownCodeType type) {
		String val = StringUtils.defaultString(str);
		int f = -1;
		int e = -1;
		int fAdd = 0;
		if (type == MarkdownCodeType.JAVA) {
			f = val.indexOf(CODE_START_JAVA);
			e = val.lastIndexOf(CODE_END);
			fAdd = CODE_START_JAVA.length();
		}
		if (type == MarkdownCodeType.HTML) {
			f = val.indexOf(CODE_START_HTML);
			e = val.lastIndexOf(CODE_END);	
			fAdd = CODE_START_HTML.length();
		}
		if (f >= 0 && f < e && e >= 10) {
			val = val.substring(f+fAdd, e);
		}		
		return val;
	}
	
	public static String oldParse(String str, MarkdownCodeType type) {
		String val = getBlockValue(str, type);
		if (type == MarkdownCodeType.JAVA) {
			if (val.length() >= CODE_START_JAVA.length() && val.startsWith(CODE_START_JAVA) && val.endsWith(CODE_END)) {
				val = val.substring(CODE_START_JAVA.length(), val.length());
				val = val.substring(0, val.length()-3);
			}
		}
		if (type == MarkdownCodeType.HTML) {
			if (val.length() >= CODE_START_HTML.length() && val.startsWith(CODE_START_HTML) && val.endsWith(CODE_END)) {
				val = val.substring(CODE_START_HTML.length(), val.length());
				val = val.substring(0, val.length()-3);
			}
		}
		return val;
	}
	
	public static String parse(String markdownText) {
		StringBuilder sb = new StringBuilder();
		Matcher matcher = _pattern.matcher(markdownText);
		while (matcher.find()) {
			sb.append(matcher.group(1));
		}
		return sb.toString();
	}
	
}
