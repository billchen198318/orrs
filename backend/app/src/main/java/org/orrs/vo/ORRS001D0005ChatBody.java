package org.orrs.vo;

import java.math.BigDecimal;

public class ORRS001D0005ChatBody implements java.io.Serializable {
	private static final long serialVersionUID = -8143528356157087782L;
	
	private String model;
	private String system;
	private String message;
	private String docmode;
	private BigDecimal simThreshold;
	private String wikimode;
	
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getSystem() {
		return system;
	}
	public void setSystem(String system) {
		this.system = system;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getDocmode() {
		return docmode;
	}
	public void setDocmode(String docmode) {
		this.docmode = docmode;
	}
	public BigDecimal getSimThreshold() {
		return simThreshold;
	}
	public void setSimThreshold(BigDecimal simThreshold) {
		this.simThreshold = simThreshold;
	}
	public String getWikimode() {
		return wikimode;
	}
	public void setWikimode(String wikimode) {
		this.wikimode = wikimode;
	}
	
}
