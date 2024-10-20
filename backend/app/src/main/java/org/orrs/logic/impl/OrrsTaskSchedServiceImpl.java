/* 
 * Copyright 2021-2024 qifu of copyright Chen Xin Nien
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
package org.orrs.logic.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ScheduledFuture;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.orrs.logic.IOrrsTaskSchedService;
import org.qifu.base.model.ServiceAuthority;
import org.qifu.base.service.BaseLogicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@ServiceAuthority(check = false)
@Component
@Service
@Transactional(propagation=Propagation.REQUIRED, timeout=300, readOnly=true)
public class OrrsTaskSchedServiceImpl extends BaseLogicService implements IOrrsTaskSchedService {
	protected Logger logger = LogManager.getLogger(OrrsTaskSchedServiceImpl.class);
	
    @Autowired
    private TaskScheduler taskScheduler;
    
    private Map<String, ScheduledFuture<?>> jobsMap = new HashMap<>();
    
	@Override
	public void scheduleTask(String jobId, Runnable tasklet, String cronExpression) {
        System.out.println("Scheduling task with job id: " + jobId + " and cron expression: " + cronExpression);
        ScheduledFuture<?> scheduledTask = taskScheduler.schedule(tasklet, new CronTrigger(cronExpression, TimeZone.getTimeZone(TimeZone.getDefault().getID())));
        jobsMap.put(jobId, scheduledTask);
	}

	@Override
	public void removeScheduledTask(String jobId) {
        ScheduledFuture<?> scheduledTask = jobsMap.get(jobId);
        if(scheduledTask != null) {
            scheduledTask.cancel(true);
            jobsMap.put(jobId, null);
        }
	}

	@Override
	public boolean isCancelled(String jobId) {
		ScheduledFuture<?> scheduledTask = jobsMap.get(jobId);
		if (null == scheduledTask) {
			return false;
		}
		return scheduledTask.isCancelled();
	}
	
}
