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

import org.qifu.base.mapper.IBaseMapper;
import org.qifu.base.service.BaseService;
import org.qifu.core.entity.TbSysUsess;
import org.qifu.core.mapper.TbSysUsessMapper;
import org.qifu.core.service.ISysUsessService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

//change use SysTokenServiceImpl
@Component
@Service
@Transactional(propagation=Propagation.REQUIRED, timeout=300, readOnly=true)
public class SysUsessServiceImpl extends BaseService<TbSysUsess, String> implements ISysUsessService<TbSysUsess, String> {
	
	private final TbSysUsessMapper sysUsessMapper;
	
	public SysUsessServiceImpl(TbSysUsessMapper sysUsessMapper) {
		super();
		this.sysUsessMapper = sysUsessMapper;
	}
	
	@Override
	protected IBaseMapper<TbSysUsess, String> getBaseMapper() {
		return this.sysUsessMapper;
	}
	
}
