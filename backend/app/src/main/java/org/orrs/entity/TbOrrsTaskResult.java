package org.orrs.entity;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.qifu.base.model.CreateDateField;
import org.qifu.base.model.CreateUserField;
import org.qifu.base.model.EntityPK;
import org.qifu.base.model.UpdateDateField;
import org.qifu.base.model.UpdateUserField;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class TbOrrsTaskResult implements java.io.Serializable {
	private static final long serialVersionUID = 6100582624088972776L;
	
	private String oid;
    private String taskId;
    private String cmdId;
    private Short itemSeq;
    private String processMsT1;
    private String processMsT2;
    
    @JsonIgnore
    private byte[] content;
    
    @JsonIgnore
    private byte[] invokeContent;    
    
    private String lastCmd;
    private String processId;
    private String cuserid;
    private Date cdate;
    private String uuserid;
    private Date udate;
    
    private String taskName;
    private String taskDescription;
    private String cmdName;
    private String cmdDescription;
    private String cmdResultType;
    
    public String getContentString() {
    	if (content != null) {
    		return new String(this.content, StandardCharsets.UTF_8);
    	}
    	return StringUtils.EMPTY;
    }
    
    public String getInvokeContentString() {
    	if (invokeContent != null) {
            return new String(this.invokeContent, StandardCharsets.UTF_8);
        }
        return StringUtils.EMPTY;
    }
    
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

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public byte[] getInvokeContent() {
		return invokeContent;
	}

	public void setInvokeContent(byte[] invokeContent) {
		this.invokeContent = invokeContent;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskDescription() {
		return taskDescription;
	}

	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}

	public String getCmdName() {
		return cmdName;
	}

	public void setCmdName(String cmdName) {
		this.cmdName = cmdName;
	}

	public String getCmdDescription() {
		return cmdDescription;
	}

	public void setCmdDescription(String cmdDescription) {
		this.cmdDescription = cmdDescription;
	}

	public String getCmdResultType() {
		return cmdResultType;
	}

	public void setCmdResultType(String cmdResultType) {
		this.cmdResultType = cmdResultType;
	}
	
}
