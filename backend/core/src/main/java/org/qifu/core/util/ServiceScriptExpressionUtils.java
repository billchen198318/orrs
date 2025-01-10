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
package org.qifu.core.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.qifu.base.AppContext;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.SortType;
import org.qifu.base.model.YesNoKeyProvide;
import org.qifu.core.entity.TbSysBeanHelp;
import org.qifu.core.entity.TbSysBeanHelpExpr;
import org.qifu.core.entity.TbSysBeanHelpExprMap;
import org.qifu.core.entity.TbSysExpression;
import org.qifu.core.service.ISysBeanHelpExprMapService;
import org.qifu.core.service.ISysBeanHelpExprService;
import org.qifu.core.service.ISysBeanHelpService;
import org.qifu.core.service.ISysExpressionService;
import org.qifu.util.ScriptExpressionUtils;

public class ServiceScriptExpressionUtils {
	
	private static ISysBeanHelpService<TbSysBeanHelp, String> sysBeanHelpService;
	
	private static ISysBeanHelpExprService<TbSysBeanHelpExpr, String> sysBeanHelpExprService;
	
	private static ISysBeanHelpExprMapService<TbSysBeanHelpExprMap, String> sysBeanHelpExprMapService;
	
	private static ISysExpressionService<TbSysExpression, String> sysExpressionService;
	
	protected ServiceScriptExpressionUtils() {
		throw new IllegalStateException("Utils class: ServiceScriptExpressionUtils");
	}
	
	static {
		sysBeanHelpService = AppContext.getContext().getBean(ISysBeanHelpService.class);
		
		sysBeanHelpExprService = AppContext.getContext().getBean(ISysBeanHelpExprService.class);
		
		sysBeanHelpExprMapService = AppContext.getContext().getBean(ISysBeanHelpExprMapService.class);
		
		sysExpressionService = AppContext.getContext().getBean(ISysExpressionService.class);
	}
	
	public static boolean needProcess(String beanId, String methodName, String system) throws ServiceException {
		boolean f = false;
		TbSysBeanHelp beanHelp = new TbSysBeanHelp();
		beanHelp.setBeanId(beanId);
		beanHelp.setMethod(methodName);
		beanHelp.setSystem(system);		
		if (sysBeanHelpService.countByUK(beanHelp)>0) {
			f = true;
		}
		return f;
	}
	
	public static void processBefore(String beanId, Method method, String system, ProceedingJoinPoint pjp) throws ServiceException {
		process(beanId, method, system, null, pjp);
	}
	
	public static void processAfter(String beanId, Method method, String system, Object resultObj, ProceedingJoinPoint pjp) throws ServiceException {
		process(beanId, method, system, resultObj, pjp);
	}
	
	private static TbSysBeanHelp loadSysBeanHelperData(String beanId, String methodName, String system) throws ServiceException {
		TbSysBeanHelp beanHelp = new TbSysBeanHelp();
		beanHelp.setBeanId(beanId);
		beanHelp.setMethod(methodName);
		beanHelp.setSystem(system);
		beanHelp = sysBeanHelpService.selectByUniqueKey(beanHelp).getValueEmptyThrowMessage();
		return beanHelp;
	}
	
	private static List<TbSysBeanHelpExpr> loadSysBeanHelpExprsData(TbSysBeanHelp beanHelp) throws ServiceException {
		List<TbSysBeanHelpExpr> exprs = null;
		Map<String, Object> params = new HashMap<>();
		params.put("helpOid", beanHelp.getOid());
		exprs = sysBeanHelpExprService.selectListByParams(params, "EXPR_SEQ", SortType.ASC).getValue();
		if (exprs == null) {
			exprs = new ArrayList<>();
		}
		params.clear();
		return exprs;		
	}
	
	private static List<TbSysBeanHelpExprMap> loadSysBeanHelpExprMapsData(TbSysBeanHelpExpr beanHelpExpr) throws ServiceException {
		List<TbSysBeanHelpExprMap> exprMaps = null;
		Map<String, Object> params = new HashMap<>();
		params.put("helpExprOid", beanHelpExpr.getOid());
		exprMaps = sysBeanHelpExprMapService.selectListByParams(params).getValue();
		if (exprMaps == null) {
			exprMaps = new ArrayList<>();
		}
		params.clear();
		return exprMaps;		
	}
	
	private static Map<String, Object> getParameters(List<TbSysBeanHelpExprMap> beanHelpExprMaps, Object resultObj, ProceedingJoinPoint pjp) {
		Object[] args = pjp.getArgs();
		Map<String, Object> dataMap = new HashMap<>();
		for (TbSysBeanHelpExprMap map : beanHelpExprMaps) {
			Object value = null;
			if (YesNoKeyProvide.YES.equals(map.getMethodResultFlag())) {
				value = resultObj;
			} else {
				for (int i=0; args!=null && i<args.length; i++) {
					if (args[i] !=null && args[i].getClass() != null 
							&& args[i].getClass().getName().equals(map.getMethodParamClass()) 
							&& map.getMethodParamIndex() == i ) {
						value = args[i];
					}
				}
			}
			dataMap.put(map.getVarName(), value);
		}
		return dataMap;
	}	
	
	private static void process(String beanId, Method method, String system, Object resultObj, ProceedingJoinPoint pjp) throws ServiceException {
		TbSysBeanHelp beanHelp = loadSysBeanHelperData(beanId, method.getName(), system);
		if (!YesNoKeyProvide.YES.equals(beanHelp.getEnableFlag()) ) {
			return;
		}
		List<TbSysBeanHelpExpr> beanHelpExprs = loadSysBeanHelpExprsData(beanHelp);
		if (CollectionUtils.isEmpty(beanHelpExprs)) {
			return;
		}
		for (TbSysBeanHelpExpr helpExpr : beanHelpExprs) {
			TbSysExpression expression = new TbSysExpression();
			expression.setExprId(helpExpr.getExprId());
			DefaultResult<TbSysExpression> eResult = sysExpressionService.selectByUniqueKey(expression);
			if (eResult.getValue()==null) {
				continue;
			}
			expression = eResult.getValue();
			List<TbSysBeanHelpExprMap> exprMaps = loadSysBeanHelpExprMapsData(helpExpr);
			ScriptExpressionUtils.execute(
					expression.getType(), 
					expression.getContent(), 
					null, 
					getParameters(exprMaps, resultObj, pjp));					
		}
	}
	
}
