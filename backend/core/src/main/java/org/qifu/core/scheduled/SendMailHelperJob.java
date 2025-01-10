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
package org.qifu.core.scheduled;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.qifu.base.Constants;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.YesNoKeyProvide;
import org.qifu.base.scheduled.BaseScheduledTasksProvide;
import org.qifu.core.entity.TbSysMailHelper;
import org.qifu.core.model.MailContent;
import org.qifu.core.service.ISysMailHelperService;
import org.qifu.core.util.MailClientUtils;
import org.qifu.core.util.SystemSettingConfigureUtils;
import org.qifu.util.SimpleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;

@Component
public class SendMailHelperJob extends BaseScheduledTasksProvide {
	protected static Logger log = LoggerFactory.getLogger(SendMailHelperJob.class);
	
	private ISysMailHelperService<TbSysMailHelper, String> sysMailHelperService;
	
	public ISysMailHelperService<TbSysMailHelper, String> getSysMailHelperService() {
		return sysMailHelperService;
	}
	
	@Resource
	public void setSysMailHelperService(ISysMailHelperService<TbSysMailHelper, String> sysMailHelperService) {
		this.sysMailHelperService = sysMailHelperService;
	}

	@Scheduled(initialDelay = 5000, fixedDelay = 180000)
	public void execute() {
		try {
			this.login();
			if (YesNoKeyProvide.YES.equals(SystemSettingConfigureUtils.getMailEnableValue())) {
				String linkMailId = SimpleUtils.getStrYMD("").substring(0, 6);
				DefaultResult<List<TbSysMailHelper>> result = sysMailHelperService.findForJobList(linkMailId);			
				if (result.getValue()!=null) {
					this.process(sysMailHelperService, result.getValue());
				}				
			} else {
				log.warn("************ mail sender is disable. please modify config CNF/CNF_CONF002 ************");				
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		} finally {
			this.logout();
		}
	}
	
	private void process(ISysMailHelperService<TbSysMailHelper, String> sysMailHelperService, List<TbSysMailHelper> mailHelperList) throws ServiceException {
		if (CollectionUtils.isEmpty(mailHelperList)) {
			return;
		}	
		for (TbSysMailHelper mailHelper : mailHelperList) {
			new ProcessWorker(sysMailHelperService, mailHelper);
		}
	}
	
	private class ProcessWorker extends Thread {
	    private final ISysMailHelperService<TbSysMailHelper, String> sysMailHelperService;
	    private final TbSysMailHelper mailHelper;
	    private static final long SLEEP_TIME = 3000; // 3 seconds
	    private static final int RETRY_LIMIT = 3;
	    private boolean success = false;
	    
	    public ProcessWorker(ISysMailHelperService<TbSysMailHelper, String> sysMailHelperService, TbSysMailHelper mailHelper) {
	        this.sysMailHelperService = sysMailHelperService;
	        this.mailHelper = mailHelper;
	        this.start();
	    }
	    
	    @Override
	    public void run() {
	        int retries = RETRY_LIMIT;
	        while (!success && retries > 0) {
	            if (mailHelper == null) {
	            	break; // Exit if mailHelper is null
	            }
	            try {
	                log.info("Processing mail-id: {}", mailHelper.getMailId());
	                sendMail();
	                success = true;
	            } catch (MailException | UnsupportedEncodingException | MessagingException e) {
	                log.error("Error while sending email for mail-id: {}. Attempt {} of {}", mailHelper.getMailId(), RETRY_LIMIT - retries + 1, RETRY_LIMIT, e);
	            }
	            if (success) {
	                handleSuccess();
	            }
	            retries--;
	            
	            // Sleep between retries
	            try {
	                Thread.sleep(SLEEP_TIME);
	            } catch (InterruptedException e) {
	                log.error("Thread interrupted while sleeping.", e);
	                Thread.currentThread().interrupt();
	                retries = -1;
	            }
	        }
	    }

	    private void sendMail() throws MailException, UnsupportedEncodingException, MessagingException {
	        MailClientUtils.send(
	                mailHelper.getMailFrom(),
	                mailHelper.getMailTo(),
	                mailHelper.getMailCc(),
	                mailHelper.getMailBcc(),
	                new MailContent(mailHelper.getSubject(), new String(mailHelper.getText(), Constants.BASE_ENCODING))
	        );
	    }

	    private void handleSuccess() {
	        try {
	            if (YesNoKeyProvide.YES.equals(mailHelper.getRetainFlag())) {
	                mailHelper.setSuccessFlag(YesNoKeyProvide.YES);
	                mailHelper.setSuccessTime(new Date());
	                sysMailHelperService.update(mailHelper);
	            } else {
	                sysMailHelperService.delete(mailHelper);
	            }
	            log.info("Successfully processed mail-id: {}", mailHelper.getMailId());
	        } catch (ServiceException e) {
	            log.error("Error updating mail record for mail-id: {}", mailHelper.getMailId(), e);
	        }
	    }
	    
	}
	
}
