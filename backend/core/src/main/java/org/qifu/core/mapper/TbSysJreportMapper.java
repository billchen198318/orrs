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
package org.qifu.core.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.qifu.base.mapper.IBaseMapper;
import org.qifu.core.entity.TbSysJreport;

@Mapper
public interface TbSysJreportMapper extends IBaseMapper<TbSysJreport, String> {
	
	/**
	 * no CONTENT field for query , because sometime no need use this field, maybe field byte is big
	 * 
	 * @param oid
	 * @return
	 * @throws Exception
	 */
	public TbSysJreport selectByPrimaryKeySimple(String oid);
	
	/**
	 * no CONTENT field for query , because sometime no need use this field, maybe field byte is big
	 * 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List<TbSysJreport> selectListByParamsSimple(Map<String, Object> paramMap);
	
	/**
	 * no CONTENT field for query , because sometime no need use this field, maybe field byte is big
	 * 
	 * @param paramMap
	 * @return
	 */
	public List<TbSysJreport> findPageSimple(Map<String, Object> paramMap);
	
}
