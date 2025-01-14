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
package org.qifu.hillfog.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.qifu.base.AppContext;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.SortType;
import org.qifu.hillfog.entity.HfMeasureData;
import org.qifu.hillfog.model.MeasureDataCode;
import org.qifu.hillfog.service.IMeasureDataService;
import org.qifu.hillfog.vo.ScoreCalculationData;
import org.qifu.util.SimpleUtils;

public class QueryMeasureDataUtils {
	
	private static IMeasureDataService<HfMeasureData, String> measureDataService;
	
	static {
		measureDataService = AppContext.getContext().getBean(IMeasureDataService.class);
	}
	
	public static List<HfMeasureData> queryForScoreCalculationData(ScoreCalculationData data) throws ServiceException, Exception {
		String startDate = StringUtils.defaultString(data.getDate1()).trim().replaceAll("/", "").replaceAll("-", "");
		String endDate = StringUtils.defaultString(data.getDate2()).trim().replaceAll("/", "").replaceAll("-", "");
		if (startDate.length() != 8 || endDate.length() != 8 || !SimpleUtils.isDate(startDate) || !SimpleUtils.isDate(endDate)) {
			throw new ServiceException( BaseSystemMessage.dataErrors() );
		}
		if ( MeasureDataCode.FREQUENCY_WEEK.equals(data.getFrequency()) || MeasureDataCode.FREQUENCY_MONTH.equals(data.getFrequency()) ) {
			Map<String, String> paramMap = MeasureDataCode.getWeekOrMonthStartEnd(data.getFrequency(), startDate, endDate);
			startDate = paramMap.get("startDate");
			endDate = paramMap.get("endDate");
		}
		if ( MeasureDataCode.FREQUENCY_QUARTER.equals(data.getFrequency()) || MeasureDataCode.FREQUENCY_HALF_OF_YEAR.equals(data.getFrequency()) 
				|| MeasureDataCode.FREQUENCY_YEAR.equals(data.getFrequency()) ) {
			startDate = startDate.substring(0, 4) + "0101";
			endDate = endDate.substring(0, 4) + "12" + SimpleUtils.getMaxDayOfMonth(Integer.parseInt(endDate.substring(0, 4)), 12);			
		}
		return queryForScoreCalculationData(
				data.getKpi().getId(), data.getFrequency(), startDate, endDate, data.getMeasureDataOrgId(), data.getMeasureDataAccount());
	}
	
	public static List<HfMeasureData> queryForScoreCalculationData(String kpiId, String frequency, String startDate, String endDate, String orgId, String account) throws ServiceException, Exception {
		if (StringUtils.isBlank(kpiId) || StringUtils.isBlank(frequency) || StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)
				|| StringUtils.isBlank(orgId) || StringUtils.isBlank(account)) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("kpiId", kpiId);
		paramMap.put("frequency", frequency);
		paramMap.put("startDate", startDate);
		paramMap.put("endDate", endDate);
		paramMap.put("orgId", orgId);
		paramMap.put("account", account);
		return measureDataService.selectListByParams(paramMap, "DATE", SortType.ASC).getValue();
	}
	
}
