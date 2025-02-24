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
package org.qifu.base.service;

import java.util.List;
import java.util.Map;

import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.PageOf;
import org.qifu.base.model.QueryResult;

import ognl.OgnlException;

/**
 * @param <T>	MyBatis Entity
 * @param <K>	PK屬性
 */
public interface IBaseService<T extends java.io.Serializable, K extends java.io.Serializable> {	
	
	public String defaultString(String source);
	
	public DefaultResult<T> selectByPrimaryKey(K pk) throws ServiceException;
	
	public DefaultResult<T> selectByEntityPrimaryKey(T mapperObj) throws ServiceException;
	
	public DefaultResult<List<T>> selectList() throws ServiceException;
	
	public DefaultResult<List<T>> selectListByParams(Map<String, Object> paramMap) throws ServiceException;
	
	public DefaultResult<List<T>> selectList(String orderBy, String sortType) throws ServiceException;
	
	public DefaultResult<List<T>> selectListByParams(Map<String, Object> paramMap, String orderBy, String sortType) throws ServiceException;
	
	public DefaultResult<T> selectByUniqueKey(T mapperObj) throws ServiceException;
	
	public DefaultResult<T> insert(T mapperObj) throws ServiceException;
	
	public DefaultResult<T> update(T mapperObj) throws ServiceException;
	
	public DefaultResult<Boolean> delete(T mapperObj) throws ServiceException;
	
	public Long count(Map<String, Object> paramMap) throws ServiceException;
	
	public Long countByUK(T mapperObj) throws ServiceException;	
	
	public <V> QueryResult<List<V>> findPage(Map<String, Object> paramMap, PageOf pageOf) throws ServiceException;
	
	public <V> QueryResult<List<V>> findPage(String mapperCountMethodName, String mapperQueryMethodName, Map<String, Object> paramMap, PageOf pageOf) throws ServiceException, OgnlException;
	
}
