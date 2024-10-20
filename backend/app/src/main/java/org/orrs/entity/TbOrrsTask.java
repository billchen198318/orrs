package org.orrs.entity;

import java.util.Date;
import java.util.List;

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
	private String cronExpr;
	private String enableFlag;
	private String cuserid;
	private Date cdate;
	private String uuserid;
	private Date udate;
	
	private List<TbOrrsTaskCmd> cmds = null;
	
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
	
	public String getCronExpr() {
		return cronExpr;
	}
	
	public void setCronExpr(String cronExpr) {
		this.cronExpr = cronExpr;
	}
	
	public String getEnableFlag() {
		return enableFlag;
	}
	
	public void setEnableFlag(String enableFlag) {
		this.enableFlag = enableFlag;
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
	
	public void setUdate(Date udate) {
		this.udate = udate;
	}

	public List<TbOrrsTaskCmd> getCmds() {
		return cmds;
	}

	public void setCmds(List<TbOrrsTaskCmd> cmds) {
		this.cmds = cmds;
	}
	
}
