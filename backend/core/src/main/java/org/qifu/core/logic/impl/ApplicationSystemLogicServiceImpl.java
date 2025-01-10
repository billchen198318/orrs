/* 
 * Copyright 2012-2016 bambooCORE, greenstep of copyright Chen Xin Nien
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
package org.qifu.core.logic.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.ServiceAuthority;
import org.qifu.base.model.ServiceMethodAuthority;
import org.qifu.base.model.ServiceMethodType;
import org.qifu.base.service.BaseLogicService;
import org.qifu.core.entity.TbSys;
import org.qifu.core.entity.TbSysProg;
import org.qifu.core.logic.IApplicationSystemLogicService;
import org.qifu.core.service.ISysProgService;
import org.qifu.core.service.ISysService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@ServiceAuthority(check = true)
@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
public class ApplicationSystemLogicServiceImpl extends BaseLogicService implements IApplicationSystemLogicService {
	protected static Logger logger = LoggerFactory.getLogger(ApplicationSystemLogicServiceImpl.class);
	
	private final ISysService<TbSys, String> sysService;
	
	private final ISysProgService<TbSysProg, String> sysProgService;
	
	public ApplicationSystemLogicServiceImpl(ISysService<TbSys, String> sysService, ISysProgService<TbSysProg, String> sysProgService) {
		super();
		this.sysService = sysService;
		this.sysProgService = sysProgService;
	}

	/**
	 * 建立 TB_SYS 資料
	 * 
	 * @param sys
	 * @param iconOid
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */
	@ServiceMethodAuthority(type = ServiceMethodType.INSERT)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )		
	@Override
	public DefaultResult<TbSys> create(TbSys sys) throws ServiceException {
		return this.sysService.insert(sys);
	}
	
	/**
	 * 刪除 TB_SYS 資料
	 * 
	 * @param sys
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */	
	@ServiceMethodAuthority(type = ServiceMethodType.DELETE)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )			
	@Override
	public DefaultResult<Boolean> delete(TbSys sys) throws ServiceException {
		if (sys==null || StringUtils.isBlank(sys.getOid()) ) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		DefaultResult<TbSys> sysResult = this.sysService.selectByEntityPrimaryKey(sys);
		if (sysResult.getValue()==null) {
			throw new ServiceException(sysResult.getMessage());
		}
		sys = sysResult.getValue();
		Map<String, Object> params = new HashMap<>();
		params.put("progSystem", sys.getSysId());
		if (this.sysProgService.count(params)>0) {
			throw new ServiceException(BaseSystemMessage.dataCannotDelete());
		}		
		return this.sysService.delete(sys);		
	}
	
	/**
	 * 更新 TB_SYS 資料
	 * 
	 * @param sys
	 * @param iconOid
	 * @return
	 * @throws ServiceException
	 * @throws Exception
	 */	
	@ServiceMethodAuthority(type = ServiceMethodType.UPDATE)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )			
	@Override
	public DefaultResult<TbSys> update(TbSys sys) throws ServiceException {
		return this.sysService.update(sys);
	}
	
}
