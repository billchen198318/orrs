package org.qifu.core.vo;

import java.util.List;
import java.util.Map;

import org.qifu.core.entity.TbSysProg;

public class ProgramAndMenuResult implements java.io.Serializable {
	private static final long serialVersionUID = -8214299999117601069L;
	
	private List<Map<String, Object>> menuList = null;
	
	private List<TbSysProg> programList = null;

	public List<Map<String, Object>> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<Map<String, Object>> menuList) {
		this.menuList = menuList;
	}

	public List<TbSysProg> getProgramList() {
		return programList;
	}

	public void setProgramList(List<TbSysProg> programList) {
		this.programList = programList;
	}
	
}
