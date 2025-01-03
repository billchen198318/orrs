package org.orrs.model;

public class QueryTextSnippetData implements java.io.Serializable {
	private static final long serialVersionUID = -3164887137574362065L;
	
	private String title;
	private String originalSnippet;
	private String snippet;
	
	public QueryTextSnippetData(String title, String originalSnippet, String snippet) {
		super();
		this.title = title;
		this.originalSnippet = originalSnippet;
		this.snippet = snippet;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getOriginalSnippet() {
		return originalSnippet;
	}
	
	public void setOriginalSnippet(String originalSnippet) {
		this.originalSnippet = originalSnippet;
	}
	
	public String getSnippet() {
		return snippet;
	}
	
	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}
	
}
