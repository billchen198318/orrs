/* 
 * Copyright 2019-2021 qifu of copyright Chen Xin Nien
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * -----------------------------------------------------------------------
 * 
 * author: 	Chen Xin Nien
 * contact: chen.xin.nien@gmail.com
 * 
 */
package org.qifu.core.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.mapper.IBaseMapper;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.YesNo;
import org.qifu.base.properties.BaseInfoConfigProperties;
import org.qifu.base.service.BaseService;
import org.qifu.core.entity.TbSysMenu;
import org.qifu.core.mapper.TbSysMenuMapper;
import org.qifu.core.model.MenuItemType;
import org.qifu.core.service.ISysMenuService;
import org.qifu.core.vo.SysMenuVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Service
@Transactional(propagation=Propagation.REQUIRED, timeout=300, readOnly=true)
public class SysMenuServiceImpl extends BaseService<TbSysMenu, String> implements ISysMenuService<TbSysMenu, String> {
	
	@Autowired
	TbSysMenuMapper tbSysMenuMapper;
	
	@Autowired
	BaseInfoConfigProperties baseInfoConfigProperties;	

	@Override
	protected IBaseMapper<TbSysMenu, String> getBaseMapper() {
		return this.tbSysMenuMapper;
	}

	@Transactional(propagation=Propagation.REQUIRED, timeout=300, readOnly=true)
	@Override
	public DefaultResult<List<SysMenuVO>> findForMenuGenerator(String sysId, String account) throws ServiceException, Exception {
		if (StringUtils.isBlank(sysId)) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("progSystem", sysId);
		if (!StringUtils.isBlank(account)) {
			paramMap.put("account", account);
		}
		DefaultResult<List<SysMenuVO>> result = new DefaultResult<List<SysMenuVO>>();
		List<SysMenuVO> value = this.tbSysMenuMapper.selectForMenuGenerator(paramMap);
		if (value != null) {
			result.setValue(value);
		} else {
			result.setMessage(BaseSystemMessage.searchNoData());
		}
		return result;
	}

	@Override
	public List<Map<String, Object>> getMemuItemListForFrontend(String account) throws ServiceException, Exception {
		List<SysMenuVO> menuList = this.findForMenuGenerator(baseInfoConfigProperties.getMainSystem(), account).getValueEmptyThrowMessage();
		if (CollectionUtils.isEmpty(menuList)) {
			throw new ServiceException(BaseSystemMessage.searchNoData());
		}
		List<Map<String, Object>> menuTreeList = new ArrayList<Map<String, Object>>();		
		List<SysMenuVO> parentSysMenuList = searchFolder(menuList);
		for (SysMenuVO pMenu : parentSysMenuList) {
			List<SysMenuVO> childSysMenuList = searchItem(pMenu.getOid(), menuList);
			if (CollectionUtils.isEmpty(childSysMenuList)) {
				continue;
			}
			Map<String, Object> menuMap = new LinkedHashMap<String, Object>();					
			menuMap.put("id", pMenu.getProgId());
			menuMap.put("type", pMenu.getItemType());
			menuMap.put("name", pMenu.getName());
			menuMap.put("icon", pMenu.getFontIconClassId());
			//menuMap.put("prefix", StringUtils.defaultString(pMenu.getUrl()));
			
			List<Map<String, String>> menuItemList = new LinkedList<Map<String, String>>();			
			menuMap.put("items", menuItemList);
			
			for (SysMenuVO cMenu : childSysMenuList) {
				Map<String, String> cItemMap = new HashMap<String, String>();
				cItemMap.put("id", cMenu.getProgId());
				cItemMap.put("type", cMenu.getItemType());
				cItemMap.put("name", cMenu.getName());
				cItemMap.put("icon", cMenu.getFontIconClassId());
				cItemMap.put("url", cMenu.getUrl());
				menuItemList.add(cItemMap);
			}
			
			menuTreeList.add(menuMap);	
		}		
		return menuTreeList;
	}
	
	/**
	 * 取是目錄選單的資料
	 * 
	 * @param sysMenuList
	 * @return
	 * @throws Exception
	 */
	protected static List<SysMenuVO> searchFolder(List<SysMenuVO> sysMenuList) throws Exception {
		List<SysMenuVO> folderList = new ArrayList<SysMenuVO>();
		for (SysMenuVO sysMenu : sysMenuList) {
			if (MenuItemType.FOLDER.equals(sysMenu.getItemType()) && YesNo.YES.equals(sysMenu.getEnableFlag()) ) {
				folderList.add(sysMenu);
			}
		}
		return folderList;
	}		
	
	/**
	 * 取目錄下的選單項目
	 * 
	 * @param parentOid
	 * @param sysMenuList
	 * @return
	 * @throws Exception
	 */
	protected static List<SysMenuVO> searchItem(String parentOid, List<SysMenuVO> sysMenuList) throws Exception {
		List<SysMenuVO> folderList = new ArrayList<SysMenuVO>();
		for (SysMenuVO sysMenu : sysMenuList) {
			if (MenuItemType.ITEM.equals(sysMenu.getItemType()) && parentOid.equals(sysMenu.getParentOid())
					&& YesNo.YES.equals(sysMenu.getEnableFlag()) ) {
				folderList.add(sysMenu);
			}
		}
		return folderList;
	}
	
}
