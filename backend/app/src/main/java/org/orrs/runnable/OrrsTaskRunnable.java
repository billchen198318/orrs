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

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.orrs.OrrsConstants;
import org.orrs.OrrsResultType;
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
import org.orrs.util.MarkdownCodeExtractor;
import org.qifu.base.AppContext;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.ScriptTypeCode;
import org.qifu.base.model.SortType;
import org.qifu.base.model.YesNo;
import org.qifu.base.scheduled.BaseScheduledTasksProvide;
import org.qifu.util.ScriptExpressionUtils;
import org.qifu.util.SimpleUtils;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaApi.ChatRequest;
import org.springframework.ai.ollama.api.OllamaApi.ChatResponse;
import org.springframework.ai.ollama.api.OllamaApi.Message;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.BeansException;
import org.springframework.core.env.Environment;

public class OrrsTaskRunnable extends BaseScheduledTasksProvide implements Runnable {
	protected Logger logger = LogManager.getLogger(OrrsTaskRunnable.class);
	
	private final int MAX_CAUSE_LENGTH = 5000;
	
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
	public void execute() {
		try {
			this.login();
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
		} finally {
			this.logout();
		}
	}	
	
	@Override
	public void run() {
		this.execute();
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
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("taskId", this.taskId);		
		List<TbOrrsTask> taskList = this.orrsTaskService.selectListByParams(paramMap).getValue();
		if (CollectionUtils.isEmpty(taskList)) {
			return;
		}
		for (TbOrrsTask task : taskList) {
			this.processTask(task, paramMap);
		}
	}
	
	private void processTask(TbOrrsTask task, Map<String, Object> paramMap) throws ServiceException, Exception {
		logger.info("process task: {} name: {} enable: {}", task.getTaskId(), task.getName(), task.getEnableFlag());
		if (!YesNo.YES.equals(task.getEnableFlag())) {
			return;
		}
		List<TbOrrsTaskCmd> taskCmdList = this.orrsTaskCmdService.selectListByParams(paramMap, "ITEM_SEQ", SortType.ASC).getValue();
		boolean doNext = true;
		TbOrrsTaskResult taskResPrev = null;
		TbOrrsCommand cmdPrev = null;
		String processId = StringUtils.EMPTY;
		for (int i = 0; !CollectionUtils.isEmpty(taskCmdList) && i < taskCmdList.size(); i++) {
			TbOrrsTaskCmd taskCmd = taskCmdList.get(i);
			TbOrrsCommand cmd = new TbOrrsCommand();
			cmd.setCmdId(taskCmd.getCmdId());
			cmd = this.orrsCommandService.selectByUniqueKey(cmd).getValueEmptyThrowMessage();
			paramMap.clear();
			paramMap.put("cmdId", cmd.getCmdId());
			logger.info("\ncommand: {} \nuserMessage: {} ", cmd.getCmdId(), cmd.getUserMessage());
			List<TbOrrsCommandPrompt> prompts = this.orrsCommandPromptService.selectListByParams(paramMap, "ITEM_SEQ", SortType.ASC).getValue();
			TbOrrsTaskResult taskRes = new TbOrrsTaskResult();
			taskRes.setProcessMsT1(String.valueOf(System.currentTimeMillis()));
			taskRes.setTaskId(task.getTaskId());
			taskRes.setCmdId(taskCmd.getCmdId());
			taskRes.setItemSeq(taskCmd.getItemSeq());
			taskRes.setLastCmd(YesNo.NO);
			if (i == taskCmdList.size()-1) {
				taskRes.setLastCmd(YesNo.YES);
			}
			try {
				if (!doNext) {
					logger.warn("CANNOT EXECUTE>>> taskId: {} , commandId: {} , seq: {} , because previously command work fail!", taskCmd.getTaskId(), taskCmd.getCmdId(), taskCmd.getItemSeq());
					continue;
				}
				this.processTaskWithCommand(task, taskCmd, cmd, prompts, taskRes, cmdPrev, taskResPrev);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("{}", e.getMessage());
				doNext = false;
				taskRes.setCauseMessage( e != null ? StringUtils.substring(ExceptionUtils.getStackTrace(e), 0, MAX_CAUSE_LENGTH) : "null" );
			}
			taskRes.setProcessMsT2(String.valueOf(System.currentTimeMillis()));
			if (StringUtils.isEmpty(processId)) {
				processId = this.orrsTaskResultService.selectMaxProcessId(task.getTaskId(), SimpleUtils.getStrYMD(""));
			}
			taskRes.setProcessId(processId);
			taskRes.setProcessFlag(doNext ? YesNo.YES : YesNo.NO);
			this.orrsTaskResultService.insert(taskRes);
			taskResPrev = taskRes;
			cmdPrev = cmd;
		}
	}
	
	private OllamaOptions options() {
		OllamaOptions options = new OllamaOptions();
		if (env.getProperty("spring.ai.ollama.chat.options.temperature") != null) {
			options.setTemperature( NumberUtils.toDouble(env.getProperty("spring.ai.ollama.chat.options.temperature"), 0.8d) );
		}
		if (env.getProperty("spring.ai.ollama.chat.options.top-k") != null) {
			options.setTopK( NumberUtils.toInt(env.getProperty("spring.ai.ollama.chat.options.top-k"), 40) );
		}
		if (env.getProperty("spring.ai.ollama.chat.options.top-p") != null) {
			options.setTopP( NumberUtils.toDouble(env.getProperty("spring.ai.ollama.chat.options.top-p"), 0.9d) );
		}
		if (env.getProperty("spring.ai.ollama.chat.options.num-gpu") != null) {
			options.setNumGPU( NumberUtils.toInt(env.getProperty("spring.ai.ollama.chat.options.num-gpu"), -1) );
		}
		if (env.getProperty("spring.ai.ollama.chat.options.numa") != null) {
			options.setUseNUMA( Boolean.valueOf(env.getProperty("spring.ai.ollama.chat.options.numa")) );
		}
		if (env.getProperty("spring.ai.ollama.chat.options.num-ctx") != null) {
			options.setNumCtx( NumberUtils.toInt(env.getProperty("spring.ai.ollama.chat.options.num-ctx"), 2048) );
		}
		return options;
	}
	
	private void processTaskWithCommand(TbOrrsTask task, TbOrrsTaskCmd taskCmd, TbOrrsCommand command, List<TbOrrsCommandPrompt> prompts, TbOrrsTaskResult taskRes, TbOrrsCommand commandPrev, TbOrrsTaskResult taskResPrev) throws ServiceException, Exception {
		List<Message> messageList = new LinkedList<Message>();
		if (!CollectionUtils.isEmpty(prompts)) {
			for (TbOrrsCommandPrompt prompt : prompts) {
				logger.info("prompt: {}", prompt.getPromptContent());
				messageList.add(Message.builder(Message.Role.SYSTEM).withContent(prompt.getPromptContent()).build());
			}			
		}
		String userMessage = command.getUserMessage();
		if (null != taskResPrev) {
			if (taskResPrev.getContent() != null && userMessage.indexOf(OrrsConstants.VARIABLE_PREVIOUS_MESSAGE) > -1) {
				String prevContent = new String(taskResPrev.getContent(), StandardCharsets.UTF_8);
				if (commandPrev.getResultType().equals(OrrsResultType.GROOVY.name())) {
					prevContent = MarkdownCodeExtractor.parseGroovy(prevContent);
				}
				if (commandPrev.getResultType().equals(OrrsResultType.JAVA.name())) {
					prevContent = MarkdownCodeExtractor.parseJava(prevContent);
				}
				if (commandPrev.getResultType().equals(OrrsResultType.HTML.name())) {
					prevContent = MarkdownCodeExtractor.parseHtml(prevContent);
				}				
				userMessage = StringUtils.replaceOnce(userMessage, OrrsConstants.VARIABLE_PREVIOUS_MESSAGE, prevContent);
			}
			if (taskResPrev.getInvokeContent() != null && userMessage.indexOf(OrrsConstants.VARIABLE_PREVIOUS_INVOKE_RESULT) > -1) {
				String prevInvokeContent = new String(taskResPrev.getInvokeContent(), StandardCharsets.UTF_8);
				userMessage = StringUtils.replaceOnce(userMessage, OrrsConstants.VARIABLE_PREVIOUS_INVOKE_RESULT, prevInvokeContent);
			}
		}
		taskRes.setTaskUserMessage( userMessage.getBytes(StandardCharsets.UTF_8) );
		messageList.add(Message.builder(Message.Role.USER).withContent(userMessage).build());
		// env.getProperty("spring.ai.ollama.chat.options.model")
		var req = ChatRequest.builder(command.getLlmModel()).withStream(false).withMessages(messageList).withOptions(this.options()).build();
		ChatResponse response = ollamaApi.chat(req);
		String content = StringUtils.defaultString(response.message().content());		
		logger.info("response content: {}", content);
		taskRes.setContent( content.getBytes(StandardCharsets.UTF_8) );
		if (!ScriptTypeCode.isTypeCode(command.getResultType())) {
			logger.info("TYPE: {} , cannot process!", command.getResultType());
			return;
		}
		if (StringUtils.isBlank(command.getResultVariable())) {
			logger.info("TYPE: {} , no setting result variable, cannot process!", command.getResultType());
			return;
		}
		Map<String, Object> invokeResultParam = this.invokeScript(command, content);
		Object invokeResultObj = invokeResultParam.get(command.getResultVariable());
		logger.info("invoke result: {}", (invokeResultObj != null && invokeResultObj instanceof String) ? (String) invokeResultObj : (invokeResultObj == null ? "Result is null..." : "Result is object...") );
		if (invokeResultObj != null && invokeResultObj instanceof String) {
			taskRes.setInvokeContent( ((String) invokeResultObj).getBytes(StandardCharsets.UTF_8) );
		}
	}
	
	private Map<String, Object> invokeScript(TbOrrsCommand command, String llmResponseContent) throws ServiceException, Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put(command.getResultVariable(), null);
		String script = MarkdownCodeExtractor.parseGroovy(llmResponseContent);
		logger.info("script: {}", script);
		Object r = ScriptExpressionUtils.execute(command.getResultType(), script, param, param);
		if (r != null && r instanceof String) {
			if (param.get(command.getResultVariable()) == null) {
				param.put(command.getResultVariable(), (String)r);
			}
		}
		return param;
	}
	
}
