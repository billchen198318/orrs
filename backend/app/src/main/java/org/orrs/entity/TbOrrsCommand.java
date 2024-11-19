package org.orrs.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.qifu.base.model.CreateDateField;
import org.qifu.base.model.CreateUserField;
import org.qifu.base.model.EntityPK;
import org.qifu.base.model.EntityUK;
import org.qifu.base.model.UpdateDateField;
import org.qifu.base.model.UpdateUserField;

public class TbOrrsCommand implements java.io.Serializable {
	private static final long serialVersionUID = 1903891150551902676L;
	
	private String oid;
    private String cmdId;
    private String name;
    private String description;
    private String userMessage;
    private String resultVariable;
    private String resultType;
    private String llmModel;
    private String resultAlwNul;
    private String docRetrieval;
    private BigDecimal simThreshold;
    private String cuserid;
    private Date cdate;
    private String uuserid;
    private Date udate;
    
    private List<TbOrrsCommandPrompt> prompts = null;
    
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
    
    public String getUserMessage() {
        return userMessage;
    }
    
    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }
    
    public String getResultVariable() {
        return resultVariable;
    }
    
    public void setResultVariable(String resultVariable) {
        this.resultVariable = resultVariable;
    }
    
	public String getResultType() {
		return resultType;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
	}

	public String getLlmModel() {
		return llmModel;
	}

	public void setLlmModel(String llmModel) {
		this.llmModel = llmModel;
	}

	public String getResultAlwNul() {
		return resultAlwNul;
	}

	public void setResultAlwNul(String resultAlwNul) {
		this.resultAlwNul = resultAlwNul;
	}

	public String getDocRetrieval() {
		return docRetrieval;
	}

	public void setDocRetrieval(String docRetrieval) {
		this.docRetrieval = docRetrieval;
	}

	public BigDecimal getSimThreshold() {
		return simThreshold;
	}

	public void setSimThreshold(BigDecimal simThreshold) {
		this.simThreshold = simThreshold;
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

	public List<TbOrrsCommandPrompt> getPrompts() {
		return prompts;
	}

	public void setPrompts(List<TbOrrsCommandPrompt> prompts) {
		this.prompts = prompts;
	}
    
}
