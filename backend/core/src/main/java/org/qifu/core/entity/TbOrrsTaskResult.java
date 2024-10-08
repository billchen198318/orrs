package org.qifu.core.entity;

import java.util.Date;

import org.qifu.base.model.CreateDateField;
import org.qifu.base.model.CreateUserField;
import org.qifu.base.model.EntityPK;
import org.qifu.base.model.UpdateDateField;
import org.qifu.base.model.UpdateUserField;

public class TbOrrsTaskResult implements java.io.Serializable {
	private static final long serialVersionUID = 6100582624088972776L;
	
	private String oid;
    private String taskId;
    private String cmdId;
    private Short itemSeq;
    private String processMsT1;
    private String processMsT2;
    private String lastCmd;
    private String cuserid;
    private Date cdate;
    private String uuserid;
    private Date udate;
    private byte[] content;
    
    @EntityPK(name = "oid", autoUUID = true)
    public String getOid() {
        return oid;
    }
    
    public void setOid(String oid) {
        this.oid = oid;
    }
    
    public String getTaskId() {
        return taskId;
    }
    
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
    
    public String getCmdId() {
        return cmdId;
    }
    
    public void setCmdId(String cmdId) {
        this.cmdId = cmdId;
    }
    
    public Short getItemSeq() {
        return itemSeq;
    }
    
    public void setItemSeq(Short itemSeq) {
        this.itemSeq = itemSeq;
    }
    
    public String getProcessMsT1() {
        return processMsT1;
    }
    
    public void setProcessMsT1(String processMsT1) {
        this.processMsT1 = processMsT1;
    }
    
    public String getProcessMsT2() {
        return processMsT2;
    }
    
    public void setProcessMsT2(String processMsT2) {
        this.processMsT2 = processMsT2;
    }
    
    public String getLastCmd() {
        return lastCmd;
    }
    
    public void setLastCmd(String lastCmd) {
        this.lastCmd = lastCmd;
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
    
}
