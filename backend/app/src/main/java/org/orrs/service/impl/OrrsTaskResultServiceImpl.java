/* 
 * Copyright 2019-2024 qifu of copyright Chen Xin Nien
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
package org.orrs.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.orrs.entity.TbOrrsTaskResult;
import org.orrs.mapper.TbOrrsTaskResultMapper;
import org.orrs.service.IOrrsTaskResultService;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.mapper.IBaseMapper;
import org.qifu.base.service.BaseService;
import org.qifu.util.SimpleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Service
@Transactional(propagation=Propagation.REQUIRED, timeout=300, readOnly=true)
public class OrrsTaskResultServiceImpl extends BaseService<TbOrrsTaskResult, String> implements IOrrsTaskResultService<TbOrrsTaskResult, String> {
	
	@Autowired
	TbOrrsTaskResultMapper tbOrrsTaskResultMapper;
	
	@Override
	protected IBaseMapper<TbOrrsTaskResult, String> getBaseMapper() {
		return this.tbOrrsTaskResultMapper;
	}

	@Override
	public String selectMaxProcessId(String taskId, String yyyyMMdd) throws ServiceException, Exception {
		if ( StringUtils.isBlank(taskId) || StringUtils.isBlank(yyyyMMdd) ) {
            throw new ServiceException("taskId and yyyyMMdd can't be null");
        }
		if (!SimpleUtils.isDate(yyyyMMdd)) {
			 throw new ServiceException("yyyyMMdd is not date string");
		}
		Map<String, String> param = new HashMap<String, String>();
		param.put("processIdLike", taskId + "-" + yyyyMMdd + "%");
		String currentMaxProcessId = StringUtils.defaultString( this.tbOrrsTaskResultMapper.selectMaxProcessId(param) );
		String tmp[] = currentMaxProcessId.split("-");
		if (tmp == null || tmp.length != 3) {
			return taskId + "-" + yyyyMMdd + "-" + StringUtils.leftPad("1", 5, "0");
		}
		int currentMaxSeq = Integer.parseInt(tmp[2]) + 1;
		if (currentMaxSeq > 99999) {
			throw new ServiceException("Current max seq is exceed the limit");
		}
		return taskId + "-" + yyyyMMdd + "-" + StringUtils.leftPad(String.valueOf(currentMaxSeq), 5, "0");
	}
	
}
