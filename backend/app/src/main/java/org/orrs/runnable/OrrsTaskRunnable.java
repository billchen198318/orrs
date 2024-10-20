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
package org.orrs.runnable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.qifu.base.exception.ServiceException;
import org.springframework.beans.BeansException;

public class OrrsTaskRunnable implements Runnable {
	protected Logger logger = LogManager.getLogger(OrrsTaskRunnable.class);
	
	private String taskId;
	
	public OrrsTaskRunnable() {
		super();
	}
	
	public OrrsTaskRunnable(String taskId) {
		super();
		this.taskId = taskId;
	}	
	
	@Override
	public void run() {
		try {
			this.initBeans();	
			this.process();
		} catch (BeansException e) {
			e.printStackTrace();
			logger.error( e != null && e.getMessage() != null ? e.getMessage() : "null" );
		} catch (ServiceException e) {
			e.printStackTrace();
			logger.error( e != null && e.getMessage() != null ? e.getMessage() : "null" );
		} catch (Exception e) {
			e.printStackTrace();
			logger.error( e != null && e.getMessage() != null ? e.getMessage() : "null" );
		}
	}
	
	private void initBeans() throws BeansException, Exception {
		
	}
	
	private void process() throws ServiceException, Exception {
		logger.info(this.getClass().getSimpleName() + " >>> TASK_ID: " + this.taskId + " - process start...");
		
	}

}
