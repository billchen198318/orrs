package org.qifu.core.model;

public class MailContent {
	private String subject;
	private String content;
	
	public MailContent(String subject, String content) {
		super();
		this.subject = subject;
		this.content = content;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
