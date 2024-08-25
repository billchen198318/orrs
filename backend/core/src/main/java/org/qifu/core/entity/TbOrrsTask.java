package org.qifu.core.entity;

import java.util.Date;

import org.qifu.base.model.CreateDateField;
import org.qifu.base.model.CreateUserField;
import org.qifu.base.model.EntityPK;
import org.qifu.base.model.EntityUK;
import org.qifu.base.model.UpdateDateField;
import org.qifu.base.model.UpdateUserField;

public class TbOrrsTask implements java.io.Serializable {
	private static final long serialVersionUID = 6213958306042011703L;
	
	private String oid;
	private String taskId;
	private String name;
	private String description;
	private String flag;
	private String timeMsT1;
	private String timeMsT2;
	private String cuserid;
	private Date cdate;
	private String uuserid;
	private Date udate;	
    private String prompt;
    private String userMessage;
    private String content;
    
    @EntityPK(name = "oid", autoUUID = true)
	public String getOid() {
		return oid;
	}
	
	public void setOid(String oid) {
		this.oid = oid;
	}
	
	@EntityUK(name = "taskId")
	public String getTaskId() {
		return taskId;
	}
	
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getFlag() {
		return flag;
	}
	
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	public String getTimeMsT1() {
		return timeMsT1;
	}
	
	public void setTimeMsT1(String timeMsT1) {
		this.timeMsT1 = timeMsT1;
	}
	
	public String getTimeMsT2() {
		return timeMsT2;
	}
	
	public void setTimeMsT2(String timeMsT2) {
		this.timeMsT2 = timeMsT2;
	}
	
	@CreateUserField(name = "cuserid")
	public String getCuserid() {
		return cuserid;
	}
	
	public void setCuserid(String cuserid) {
		this.cuserid = cuserid;
	}
	
	@CreateDateField(name = "cdate")
	public Date getCdate() {
		return cdate;
	}
	
	public void setCdate(Date cdate) {
		this.cdate = cdate;
	}
	
	@UpdateUserField(name = "uuserid")
	public String getUuserid() {
		return uuserid;
	}
	
	public void setUuserid(String uuserid) {
		this.uuserid = uuserid;
	}
	
	@UpdateDateField(name = "udate")
	public Date getUdate() {
		return udate;
	}
	
	public String getPrompt() {
		return prompt;
	}
	
	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}
	
	public String getUserMessage() {
		return userMessage;
	}
	
	public void setUserMessage(String userMessage) {
		this.userMessage = userMessage;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
    
}
