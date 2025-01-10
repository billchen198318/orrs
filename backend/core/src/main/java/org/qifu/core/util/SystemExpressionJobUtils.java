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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDateTime;
import org.qifu.base.AppContext;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.YesNoKeyProvide;
import org.qifu.base.properties.BaseInfoConfigProperties;
import org.qifu.core.entity.TbSysExprJob;
import org.qifu.core.entity.TbSysExprJobLog;
import org.qifu.core.entity.TbSysExpression;
import org.qifu.core.model.ExpressionJobConstants;
import org.qifu.core.model.ExpressionJobObj;
import org.qifu.core.service.ISysExprJobService;
import org.qifu.core.service.ISysExpressionService;
import org.qifu.core.support.ExpressionJobExecuteCallable;
import org.qifu.util.SimpleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemExpressionJobUtils {
	protected static Logger log = LoggerFactory.getLogger(SystemExpressionJobUtils.class);
	
	private static ISysExprJobService<TbSysExprJob, String> sysExprJobService;
	private static ISysExpressionService<TbSysExpression, String> sysExpressionService;
	private static BaseInfoConfigProperties baseInfoConfigProperties;
	
	private static final String SYSTEM = "system";
	
	protected SystemExpressionJobUtils() {
		throw new IllegalStateException("Utils class: SystemExpressionJobUtils");
	}
	
	static {
		sysExprJobService = AppContext.getContext().getBean(ISysExprJobService.class);
		sysExpressionService = AppContext.getContext().getBean(ISysExpressionService.class);
		baseInfoConfigProperties = AppContext.getContext().getBean(BaseInfoConfigProperties.class);
	}
	
	private static boolean isRunTime(TbSysExprJob exprJob, String dayOfWeek, String hour, String minute) {
		
		// 查 DAY_OF_WEEK
		if (!ExpressionJobConstants.DATEOFWEEK_HOUR_MINUTE_ALL.equals(exprJob.getRunDayOfWeek()) 
				&& !dayOfWeek.equals(exprJob.getRunDayOfWeek())) {
			return false;
		}
		
		// 查 HOUR
		if (!ExpressionJobConstants.DATEOFWEEK_HOUR_MINUTE_ALL.equals(exprJob.getRunHour()) 
				&& !hour.equals(exprJob.getRunHour())) {
			return false;
		}	
		
		// 查MINUTE
		if (!ExpressionJobConstants.DATEOFWEEK_HOUR_MINUTE_ALL.equals(exprJob.getRunMinute()) 
				&& !minute.equals(exprJob.getRunMinute())) {
			return false;
		}
		
		return true;
	}		
	
	public static void initRunStatusFlag(String system) throws ServiceException {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put(SYSTEM, system);
		paramMap.put("runStatus", ExpressionJobConstants.RUNSTATUS_PROCESS_NOW);
		List<TbSysExprJob> exprJobList = sysExprJobService.selectListByParams(paramMap).getValue();
		if (CollectionUtils.isEmpty(exprJobList)) {
			return;
		}
		Date udate = new Date();
		String uuserid = SYSTEM;
		for (TbSysExprJob exprJob : exprJobList) {
			log.warn( "ExpressionJob current RUN_STATUS is 'R' update to 'Y' , Id: {} , Name: {}", exprJob.getId(), exprJob.getName() );
			exprJob.setRunStatus(ExpressionJobConstants.RUNSTATUS_SUCCESS);
			exprJob.setUdate(udate);
			exprJob.setUuserid(uuserid);
			sysExprJobService.update(exprJob);
		}
	}
	
	public static TbSysExprJobLog executeJobForManual(String expressionJobOid) throws ServiceException {
		ExpressionJobObj jobObj = getExpressionJobForManualMode(expressionJobOid);
		ExecutorService exprJobPool = Executors.newFixedThreadPool( 1 );
		try {
			@SuppressWarnings("unused")
			Object s = exprJobPool.submit( new ExpressionJobExecuteCallable(jobObj) ).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		}
		exprJobPool.shutdown();	
		return jobObj.getSysExprJobLog();
	}
	
	public static ExpressionJobObj getExpressionJobForManualMode(String expressionJobOid) throws ServiceException {
		if (StringUtils.isBlank(expressionJobOid)) {
			throw new IllegalArgumentException("error, expressionJobId is blank!");
		}
		TbSysExprJob exprJob = sysExprJobService.selectByPrimaryKey(expressionJobOid).getValueEmptyThrowMessage();
		TbSysExpression expr = new TbSysExpression();
		expr.setExprId(exprJob.getExprId());
		expr = sysExpressionService.selectByUniqueKey(expr).getValueEmptyThrowMessage();
		ExpressionJobObj jobObj = new ExpressionJobObj();
		jobObj.setSysExprJob(exprJob);
		jobObj.setSysExprJobLog( new TbSysExprJobLog() );
		jobObj.setSysExpression(expr);
		return jobObj;
	}
	
	public static void executeJobs() throws ServiceException, InterruptedException, ExecutionException {
		List<ExpressionJobObj> jobObjList = getExpressionJobs();
		if (CollectionUtils.isEmpty(jobObjList)) {
			return;
		}
		ExecutorService exprJobPool = Executors.newFixedThreadPool( SimpleUtils.getAvailableProcessors(jobObjList.size()) );
		for (ExpressionJobObj jobObj : jobObjList) {
			@SuppressWarnings("unused")
			Object s = exprJobPool.submit( new ExpressionJobExecuteCallable(jobObj) ).get();
		}
		exprJobPool.shutdown();		
	}
	
	public static List<ExpressionJobObj> getExpressionJobs() throws ServiceException {
		int year = Integer.parseInt(SimpleUtils.getStrYMD(SimpleUtils.IS_YEAR));
		int month = Integer.parseInt(SimpleUtils.getStrYMD(SimpleUtils.IS_MONTH));
		String dayOfWeek = String.valueOf( SimpleUtils.getDayOfWeek(year, month) );
		String hour = String.valueOf( LocalDateTime.now().getHourOfDay() );
		String minute = String.valueOf( LocalDateTime.now().getMinuteOfHour() );		
		List<ExpressionJobObj> jobObjList = new ArrayList<>();
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put(SYSTEM, baseInfoConfigProperties.getSystem());
		paramMap.put("active", YesNoKeyProvide.YES);
		List<TbSysExprJob> exprJobList = sysExprJobService.selectListByParams(paramMap).getValue();
		if (CollectionUtils.isEmpty(exprJobList)) {
			return jobObjList;
		}
		for (TbSysExprJob exprJob : exprJobList) {
			boolean c = false;
			if (ExpressionJobConstants.RUNSTATUS_PROCESS_NOW.equals(exprJob.getRunStatus())) {
				log.warn( "[Expression-Job] Please check it, process now, Id: {} , name: {}", exprJob.getExprId(), exprJob.getName() );				
				c = true;
			}
			if (!isRunTime(exprJob, dayOfWeek, hour, minute)) {
				c = true;
			}
			if (c) {
                continue;
            }
			ExpressionJobObj jobObj = new ExpressionJobObj();
			jobObj.setSysExprJob(exprJob);
			jobObj.setSysExprJobLog( new TbSysExprJobLog() );
			TbSysExpression expr = new TbSysExpression();
			expr.setExprId(exprJob.getExprId());
			DefaultResult<TbSysExpression> exprResult = sysExpressionService.selectByUniqueKey(expr);
			if (exprResult.getValue() == null) {
				log.error( "[Expression-Job] Id: {} , data not found.", exprJob.getExprId());				
				log.error( exprResult.getMessage() );
				throw new ServiceException(BaseSystemMessage.dataNoExist());
			} 
			expr = exprResult.getValue();
			jobObj.setSysExpression(expr);
			jobObjList.add(jobObj);			
		}
		return jobObjList;
	}		
	
}
