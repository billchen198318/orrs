package org.orrs.entity;

import java.util.Date;

import org.qifu.base.model.CreateDateField;
import org.qifu.base.model.CreateUserField;
import org.qifu.base.model.EntityPK;
import org.qifu.base.model.EntityUK;
import org.qifu.base.model.UpdateDateField;
import org.qifu.base.model.UpdateUserField;

public class TbOrrsCommandPrompt implements java.io.Serializable {
	private static final long serialVersionUID = -2623297942832715317L;
	
	private String oid;
    private String cmdId;
    private Short itemSeq;
    private String promptContent;
    private String cuserid;
    private Date cdate;
    private String uuserid;
    private Date udate;
    
    @EntityPK(name = "oid", autoUUID = true)
    public String getOid() {
        return oid;
    }
    
    public void setOid(String oid) {
        this.oid = oid;
    }
    
    @EntityUK(name = "cmdId")
    public String getCmdId() {
        return cmdId;
    }
    
    public void setCmdId(String cmdId) {
        this.cmdId = cmdId;
    }
    
    @EntityUK(name = "itemSeq")
    public Short getItemSeq() {
        return itemSeq;
    }
    
    public void setItemSeq(Short itemSeq) {
        this.itemSeq = itemSeq;
    }
    
    public String getPromptContent() {
        return promptContent;
    }
    
    public void setPromptContent(String promptContent) {
        this.promptContent = promptContent;
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
