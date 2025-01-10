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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.mapper.IBaseMapper;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.PleaseSelect;
import org.qifu.base.model.SortType;
import org.qifu.base.service.BaseService;
import org.qifu.core.entity.TbSysProg;
import org.qifu.core.mapper.TbSysProgMapper;
import org.qifu.core.model.MenuItemType;
import org.qifu.core.service.ISysProgService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Service
@Transactional(propagation=Propagation.REQUIRED, timeout=300, readOnly=true)
public class SysProgServiceImpl extends BaseService<TbSysProg, String> implements ISysProgService<TbSysProg, String> {
	
	private static final String PROG_SYSTEM_VAR = "progSystem";
	private static final String ITEM_TYPE_VAR = "itemType";
	
	private final TbSysProgMapper tbSysProgMapper;
	
	public SysProgServiceImpl(TbSysProgMapper tbSysProgMapper) {
		super();
		this.tbSysProgMapper = tbSysProgMapper;
	}

	@Override
	protected IBaseMapper<TbSysProg, String> getBaseMapper() {
		return this.tbSysProgMapper;
	}

	@Override
	public Map<String, String> findSysProgFolderMap(String basePath, String progSystem, String itemType, boolean pleaseSelect) throws ServiceException {
		if (StringUtils.isBlank(progSystem)) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		Map<String, String> dataMap = PleaseSelect.pageSelectMap(pleaseSelect);
		Map<String, Object> params = new HashMap<>();
		params.put(PROG_SYSTEM_VAR, progSystem);
		params.put(ITEM_TYPE_VAR, itemType);
		DefaultResult<List<TbSysProg>> searchResult = this.selectListByParams(params);
		if (null == searchResult || CollectionUtils.isEmpty(searchResult.getValue())) {
			return dataMap;
		}
		for (TbSysProg sysProg : searchResult.getValue()) {
			dataMap.put(sysProg.getOid(), sysProg.getProgId() + " - " + sysProg.getName());
		}
		return dataMap;
	}

	@Override
	public List<TbSysProg> findForInTheFolderMenuItems(String progSystem, String menuParentOid, String itemType) throws ServiceException {
		if (StringUtils.isBlank(progSystem)) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put(PROG_SYSTEM_VAR, progSystem);
		if (!StringUtils.isBlank(menuParentOid)) {
			paramMap.put("menuParentOid", menuParentOid);
		}
		if (!StringUtils.isBlank(itemType)) {
			paramMap.put(ITEM_TYPE_VAR, itemType);
		}
		return this.tbSysProgMapper.findForInTheFolderMenuItems(paramMap);
	}
	
	@Override
	public List<TbSysProg> findForSystemItems(String progSystem) throws ServiceException {
		if (StringUtils.isBlank(progSystem)) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put(PROG_SYSTEM_VAR, progSystem);
		DefaultResult<List<TbSysProg>> result = this.selectListByParams(paramMap, "PROG_ID", SortType.ASC);
		return result.getValue();
	}

	@Override
	public List<TbSysProg> findForInThePermRoleOfUserId(String accountId) throws ServiceException {
		Map<String, Object> paramMap = new HashMap<>();
		if (!StringUtils.isBlank(accountId)) {
			paramMap.put("account", accountId);
		}
		paramMap.put(ITEM_TYPE_VAR, MenuItemType.ITEM);
		return this.tbSysProgMapper.findForInThePermRoleOfUserId(paramMap);
	}
	
}
