package org.orrs.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.orrs.model.MarkdownCodeType;

public class MarkdownCodeExtractor {
	private static final String _regexAll = "```(?:[a-zA-Z]*)\\s*([\\s\\S]*?)```";
	private static final Pattern _patternAll = Pattern.compile(_regexAll);
	private static final String _regexGroovy = "```groovy\\s*([\\s\\S]*?)```";
	private static final Pattern _patternGroovy = Pattern.compile(_regexGroovy);
	private static final String _regexJava = "```java\\s*([\\s\\S]*?)```";
	private static final Pattern _patternJava = Pattern.compile(_regexJava);
	private static final String _regexHtml = "```html\\s*([\\s\\S]*?)```";
	private static final Pattern _patternHtml = Pattern.compile(_regexHtml);	
	private static final String _regexSql = "```sql\\s*([\\s\\S]*?)```";
	private static final Pattern _patternSql = Pattern.compile(_regexSql);		
	private static final String _regexJson = "```json\\s*([\\s\\S]*?)```";
	private static final Pattern _patternJson = Pattern.compile(_regexJson);	
	private static final String _regexXml = "```xml\\s*([\\s\\S]*?)```";
	private static final Pattern _patternXml = Pattern.compile(_regexXml);		
	private static final String CODE_START_HTML = "```html";
	private static final String CODE_START_JAVA = "```java";
	private static final String CODE_START_GROOVY = "```groovy";
	private static final String CODE_START_SQL = "```sql";
	private static final String CODE_START_JSON = "```json";
	private static final String CODE_START_XML = "```xml";
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
		if (type == MarkdownCodeType.GROOVY) {
			f = val.indexOf(CODE_START_GROOVY);
			e = val.lastIndexOf(CODE_END);	
			fAdd = CODE_START_GROOVY.length();
		}	
		if (type == MarkdownCodeType.SQL) {
			f = val.indexOf(CODE_START_SQL);
			e = val.lastIndexOf(CODE_END);	
			fAdd = CODE_START_SQL.length();			
		}
		if (type == MarkdownCodeType.JSON) {
			f = val.indexOf(CODE_START_JSON);
			e = val.lastIndexOf(CODE_END);	
			fAdd = CODE_START_JSON.length();			
		}
		if (type == MarkdownCodeType.XML) {
			f = val.indexOf(CODE_START_XML);
			e = val.lastIndexOf(CODE_END);	
			fAdd = CODE_START_XML.length();			
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
		if (type == MarkdownCodeType.GROOVY) {
			if (val.length() >= CODE_START_GROOVY.length() && val.startsWith(CODE_START_GROOVY) && val.endsWith(CODE_END)) {
				val = val.substring(CODE_START_GROOVY.length(), val.length());
				val = val.substring(0, val.length()-3);
			}			
		}
		if (type == MarkdownCodeType.SQL) {
			if (val.length() >= CODE_START_SQL.length() && val.startsWith(CODE_START_SQL) && val.endsWith(CODE_END)) {
				val = val.substring(CODE_START_SQL.length(), val.length());
				val = val.substring(0, val.length()-3);
			}			
		}
		if (type == MarkdownCodeType.JSON) {
			if (val.length() >= CODE_START_JSON.length() && val.startsWith(CODE_START_JSON) && val.endsWith(CODE_END)) {
				val = val.substring(CODE_START_JSON.length(), val.length());
				val = val.substring(0, val.length()-3);
			}			
		}		
		if (type == MarkdownCodeType.XML) {
			if (val.length() >= CODE_START_XML.length() && val.startsWith(CODE_START_XML) && val.endsWith(CODE_END)) {
				val = val.substring(CODE_START_XML.length(), val.length());
				val = val.substring(0, val.length()-3);
			}			
		}		
		return val;
	}
	
	public static String parse(String markdownText) {
		return parse(markdownText, null);
	}
	
	public static String parseGroovy(String markdownText) {
		return parse(markdownText, MarkdownCodeType.GROOVY);
	}
	
	public static String parseHtml(String markdownText) {
		return parse(markdownText, MarkdownCodeType.HTML);
	}	
	
	public static String parseJava(String markdownText) {
		return parse(markdownText, MarkdownCodeType.JAVA);
	}
	
	public static String parseSql(String markdownText) {
		return parse(markdownText, MarkdownCodeType.SQL);
	}

	public static String parseJson(String markdownText) {
		return parse(markdownText, MarkdownCodeType.JSON);
	}
	
	public static String parseXml(String markdownText) {
		return parse(markdownText, MarkdownCodeType.XML);
	}	
	
	private static String parse(String markdownText, MarkdownCodeType type) {
		Pattern p = _patternAll;
		if (MarkdownCodeType.JAVA.equals(type)) {
			p = _patternJava;
		}
		if (MarkdownCodeType.HTML.equals(type)) {
			p = _patternHtml;
		}
		if (MarkdownCodeType.GROOVY.equals(type)) {
			p = _patternGroovy;
		}	
		if (MarkdownCodeType.SQL.equals(type)) {
			p = _patternSql;
		}
		if (MarkdownCodeType.JSON.equals(type)) {
			p = _patternJson;
		}		
		if (MarkdownCodeType.XML.equals(type)) {
			p = _patternXml;
		}			
		StringBuilder sb = new StringBuilder();
		Matcher matcher = p.matcher(markdownText);
		while (matcher.find()) {
			sb.append(matcher.group(1));
		}
		return sb.toString();
	}
	
}
