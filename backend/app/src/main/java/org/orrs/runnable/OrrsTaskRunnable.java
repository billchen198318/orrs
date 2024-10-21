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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.orrs.entity.TbOrrsCommand;
import org.orrs.entity.TbOrrsCommandAdv;
import org.orrs.entity.TbOrrsCommandPrompt;
import org.orrs.entity.TbOrrsTask;
import org.orrs.entity.TbOrrsTaskCmd;
import org.orrs.entity.TbOrrsTaskResult;
import org.orrs.logic.IOrrsLogicService;
import org.orrs.service.IOrrsCommandAdvService;
import org.orrs.service.IOrrsCommandPromptService;
import org.orrs.service.IOrrsCommandService;
import org.orrs.service.IOrrsTaskCmdService;
import org.orrs.service.IOrrsTaskResultService;
import org.orrs.service.IOrrsTaskService;
import org.qifu.base.AppContext;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.SortType;
import org.qifu.base.model.YesNo;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaApi.ChatRequest;
import org.springframework.ai.ollama.api.OllamaApi.ChatResponse;
import org.springframework.ai.ollama.api.OllamaApi.Message;
import org.springframework.beans.BeansException;
import org.springframework.core.env.Environment;

public class OrrsTaskRunnable implements Runnable {
	protected Logger logger = LogManager.getLogger(OrrsTaskRunnable.class);
	
	private String taskId;
	
	private Environment env;
	
	private IOrrsCommandService<TbOrrsCommand, String> orrsCommandService;
	
	private IOrrsCommandPromptService<TbOrrsCommandPrompt, String> orrsCommandPromptService;
	
	private IOrrsCommandAdvService<TbOrrsCommandAdv, String> orrsCommandAdvService;
	
	private IOrrsTaskCmdService<TbOrrsTaskCmd, String> orrsTaskCmdService;
	
	private IOrrsTaskService<TbOrrsTask, String> orrsTaskService;
	
	private IOrrsTaskResultService<TbOrrsTaskResult, String> orrsTaskResultService;	
	
	private IOrrsLogicService orrsLogicService;
	
	private OllamaChatModel ollamaChatModel;
	
	private OllamaApi ollamaApi;
	
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
	
	@SuppressWarnings("unchecked")
	private void initBeans() throws BeansException, Exception {
		this.orrsCommandService = this.orrsCommandService == null ? (IOrrsCommandService<TbOrrsCommand, String>) AppContext.getBean(IOrrsCommandService.class) : this.orrsCommandService;
		this.orrsCommandPromptService = this.orrsCommandPromptService == null ? (IOrrsCommandPromptService<TbOrrsCommandPrompt, String>) AppContext.getBean(IOrrsCommandPromptService.class) : this.orrsCommandPromptService;
		this.orrsCommandAdvService = this.orrsCommandAdvService == null ? (IOrrsCommandAdvService<TbOrrsCommandAdv, String>) AppContext.getBean(IOrrsCommandAdvService.class) : this.orrsCommandAdvService;
		this.orrsTaskCmdService = this.orrsTaskCmdService == null ? (IOrrsTaskCmdService<TbOrrsTaskCmd, String>) AppContext.getBean(IOrrsTaskCmdService.class) : this.orrsTaskCmdService;
		this.orrsTaskService = this.orrsTaskService == null ? (IOrrsTaskService<TbOrrsTask, String>) AppContext.getBean(IOrrsTaskService.class) : this.orrsTaskService;
		this.orrsTaskResultService = this.orrsTaskResultService == null ? (IOrrsTaskResultService<TbOrrsTaskResult, String>) AppContext.getBean(IOrrsTaskResultService.class) : this.orrsTaskResultService;
		this.orrsLogicService = this.orrsLogicService == null ? (IOrrsLogicService) AppContext.getBean(IOrrsLogicService.class) : this.orrsLogicService;
		this.ollamaChatModel = this.ollamaChatModel == null ? (OllamaChatModel) AppContext.getBean(OllamaChatModel.class) : this.ollamaChatModel;
		this.ollamaApi = this.ollamaApi == null ? (OllamaApi) AppContext.getBean(OllamaApi.class) : this.ollamaApi;
		this.env = this.env == null ? (Environment) AppContext.getBean(Environment.class) : this.env;
	}
	
	private void process() throws ServiceException, Exception {
		logger.info("{} >>> TASK_ID: {} - process start...", this.getClass().getSimpleName(), this.taskId);
		List<TbOrrsTask> taskList = this.orrsTaskService.selectList().getValue();
		if (CollectionUtils.isEmpty(taskList)) {
			return;
		}
		for (TbOrrsTask task : taskList) {
			this.processTask(task);
		}
	}
	
	private void processTask(TbOrrsTask task) throws ServiceException, Exception {
		logger.info("process task: {} name: {} enable: {}", task.getTaskId(), task.getName(), task.getEnableFlag());
		if (!YesNo.YES.equals(task.getEnableFlag())) {
			return;
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("taskId", task.getTaskId());
		List<TbOrrsTaskCmd> taskCmdList = this.orrsTaskCmdService.selectListByParams(paramMap, "ITEM_SEQ", SortType.ASC).getValue();
		for (int i = 0; !CollectionUtils.isEmpty(taskCmdList) && i < taskCmdList.size(); i++) {
			TbOrrsTaskCmd taskCmd = taskCmdList.get(i);
			TbOrrsCommand cmd = new TbOrrsCommand();
			cmd.setCmdId(taskCmd.getCmdId());
			cmd = this.orrsCommandService.selectByUniqueKey(cmd).getValueEmptyThrowMessage();
			paramMap.clear();
			paramMap.put("cmdId", cmd.getCmdId());
			logger.info("\ncommand: {} \nuserMessage: {} ", cmd.getCmdId(), cmd.getUserMessage());
			List<TbOrrsCommandPrompt> prompts = this.orrsCommandPromptService.selectListByParams(paramMap, "ITEM_SEQ", SortType.ASC).getValueEmptyThrowMessage();
			this.processTaskWithCommand(task, taskCmd, cmd, prompts);
		}
	}
	
	private void processTaskWithCommand(TbOrrsTask task, TbOrrsTaskCmd taskCmd, TbOrrsCommand command, List<TbOrrsCommandPrompt> prompts) throws ServiceException, Exception {
		List<Message> messageList = new LinkedList<Message>();
		for (TbOrrsCommandPrompt prompt : prompts) {
			logger.info("prompt: {}", prompt.getPromptContent());
			messageList.add(Message.builder(Message.Role.SYSTEM).withContent(prompt.getPromptContent()).build());
		}
		messageList.add(Message.builder(Message.Role.USER).withContent(command.getUserMessage()).build());
		var req = ChatRequest.builder(env.getProperty("spring.ai.ollama.chat.options.model"))
				.withStream(false).withMessages(messageList).build();
		ChatResponse response = ollamaApi.chat(req);
		String content = response.message().content();
		logger.info("response content: {}", content);
	}
	
}
