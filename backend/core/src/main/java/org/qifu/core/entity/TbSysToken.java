package org.qifu.core.entity;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.qifu.base.model.CreateDateField;
import org.qifu.base.model.EntityPK;
import org.qifu.util.SimpleUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.Parameter;

public class TbSysToken implements java.io.Serializable {
	private static final long serialVersionUID = 6649078428128443821L;
	
	private String oid;
    private String userId;
    
    @JsonIgnore
    @Parameter(hidden = true) private String token;
    private Date expiresDate;
    private Date rfExpiresDate;
    private Date cdate;

	public String getExpiresDateString() {
		return SimpleUtils.getDateFormat_yyyyMMddHHmmss(this.expiresDate);
	}	
	public String getRfExpiresDateString() {
		return SimpleUtils.getDateFormat_yyyyMMddHHmmss(this.rfExpiresDate);
	}		
	public String getCdateString() {
		return SimpleUtils.getDateFormat_yyyyMMddHHmmss(this.cdate);
	}	    
	public String getTokenShort() {
		if (StringUtils.defaultString(this.token).length() >= 30) {
			return this.token.substring(0, 20) + "..." + this.token.substring(this.token.length()-5, this.token.length());
		}
		return token;
	}
    
    @EntityPK(name = "oid", autoUUID = true)
    public String getOid() {
        return oid;
    }
    
    public void setOid(String oid) {
        this.oid = oid;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    @Parameter(hidden = true)
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public Date getExpiresDate() {
        return expiresDate;
    }
    
    public void setExpiresDate(Date expiresDate) {
        this.expiresDate = expiresDate;
    }
    
    public Date getRfExpiresDate() {
        return rfExpiresDate;
    }
    
    public void setRfExpiresDate(Date rfExpiresDate) {
        this.rfExpiresDate = rfExpiresDate;
    }
    
    @CreateDateField(name = "cdate")
    public Date getCdate() {
        return cdate;
    }
    
    public void setCdate(Date cdate) {
        this.cdate = cdate;
    }
    
	
}
