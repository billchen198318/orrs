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
package org.qifu.core.logic;

import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.DefaultResult;
import org.qifu.core.entity.TbSysBeanHelp;
import org.qifu.core.entity.TbSysBeanHelpExpr;
import org.qifu.core.entity.TbSysBeanHelpExprMap;

public interface ISystemBeanHelpLogicService {
	
	DefaultResult<TbSysBeanHelp> create(TbSysBeanHelp beanHelp, String systemOid) throws ServiceException;
	
	DefaultResult<TbSysBeanHelp> update(TbSysBeanHelp beanHelp, String systemOid) throws ServiceException;
	
	DefaultResult<Boolean> delete(TbSysBeanHelp beanHelp) throws ServiceException;
	
	DefaultResult<TbSysBeanHelpExpr> createExpr(TbSysBeanHelpExpr beanHelpExpr, String helpOid, String expressionOid) throws ServiceException;
	
	DefaultResult<Boolean> deleteExpr(TbSysBeanHelpExpr beanHelpExpr) throws ServiceException;
	
	DefaultResult<TbSysBeanHelpExprMap> createExprMap(TbSysBeanHelpExprMap beanHelpExprMap, String helpExprOid) throws ServiceException;
	
	DefaultResult<Boolean> deleteExprMap(TbSysBeanHelpExprMap beanHelpExprMap) throws ServiceException;
	
}
