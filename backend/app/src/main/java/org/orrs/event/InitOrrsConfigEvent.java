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
package org.orrs.event;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.orrs.entity.TbOrrsTask;
import org.orrs.logic.IOrrsTaskSchedService;
import org.orrs.runnable.OrrsTaskRunnable;
import org.orrs.service.IOrrsTaskService;
import org.qifu.base.AppContext;
import org.qifu.core.util.UserUtils;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class InitOrrsConfigEvent {
	protected Logger logger = LogManager.getLogger(InitOrrsConfigEvent.class);
	
	@EventListener(ApplicationStartedEvent.class)
	public void afterStartup() {	
		new Timer().schedule(
				new TimerTask() {
					@Override
					public void run() {
						logger.warn("init create orrs job...");
						UserUtils.setUserInfoForUserLocalUtilsBackgroundMode();
						try {
							IOrrsTaskService<TbOrrsTask, String> orrsTaskService = (IOrrsTaskService<TbOrrsTask, String>) AppContext.getBean(IOrrsTaskService.class);
							IOrrsTaskSchedService orrsTaskSchedService = (IOrrsTaskSchedService) AppContext.getBean(IOrrsTaskSchedService.class);
							List<TbOrrsTask> taskList = orrsTaskService.selectList().getValue();
							for (TbOrrsTask task : taskList) {
								orrsTaskSchedService.scheduleTask(task.getTaskId(), new OrrsTaskRunnable(task.getTaskId()), task.getCronExpr());
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						UserUtils.removeForUserLocalUtils();
						logger.info("fine.");
			        }
				}, 30000
		);		
	}	
	
}
