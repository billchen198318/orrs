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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.orrs.OrrsConstants;
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
import org.qifu.base.exception.ServiceException;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.PleaseSelect;
import org.qifu.base.model.ServiceAuthority;
import org.qifu.base.model.ServiceMethodAuthority;
import org.qifu.base.model.ServiceMethodType;
import org.qifu.base.model.YesNo;
import org.qifu.base.service.BaseLogicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@ServiceAuthority(check = true)
@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
public class OrrsLogicServiceImpl extends BaseLogicService implements IOrrsLogicService {
	protected static Logger logger = LoggerFactory.getLogger(OrrsLogicServiceImpl.class);
	
	private static final int MAX_DESCRIPTION_LENGTH = 500;
	
	@Autowired
	IOrrsCommandService<TbOrrsCommand, String> orrsCommandService;
	
	@Autowired
	IOrrsCommandPromptService<TbOrrsCommandPrompt, String> orrsCommandPromptService;
	
	@Autowired
	IOrrsCommandAdvService<TbOrrsCommandAdv, String> orrsCommandAdvService;
	
	@Autowired
	IOrrsTaskCmdService<TbOrrsTaskCmd, String> orrsTaskCmdService;
	
	@Autowired
	IOrrsTaskService<TbOrrsTask, String> orrsTaskService;
	
	@Autowired
	IOrrsTaskResultService<TbOrrsTaskResult, String> orrsTaskResultService;
	
	public OrrsLogicServiceImpl() {
		super();
	}

	@ServiceMethodAuthority(type = ServiceMethodType.INSERT)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )				
	@Override
	public DefaultResult<TbOrrsCommand> createCommand(TbOrrsCommand command) throws ServiceException, Exception {
		if (null == command || this.isBlank(command.getCmdId()) || this.isBlank(command.getName()) || this.isBlank(command.getUserMessage())
				|| this.isBlank(command.getResultVariable()) || this.isBlank(command.getResultType())) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		this.setStringValueMaxLength(command, "description", MAX_DESCRIPTION_LENGTH);
		this.setStringValueMaxLength(command, "userMessage", OrrsConstants.MAX_USER_MESSAGE_SIZE);
		DefaultResult<TbOrrsCommand> result = this.orrsCommandService.insert(command);
		this.createPrompts(command, command.getPrompts());
		return result;
	}
	
	private void createPrompts(TbOrrsCommand command, List<TbOrrsCommandPrompt> prompts) throws ServiceException, Exception {
		if (CollectionUtils.isEmpty(prompts)) {
			return;
		}
		for (int i = 0; i < prompts.size() && i < OrrsConstants.MAX_PROMPT_RECORD; i++) {
			TbOrrsCommandPrompt prompt = prompts.get(i);
			if (StringUtils.isBlank(prompt.getPromptContent())) {
				continue;
			}
			prompt.setCmdId(command.getCmdId());
			prompt.setItemSeq((short)i);
			this.setStringValueMaxLength(prompt, "promptContent", OrrsConstants.MAX_USER_MESSAGE_SIZE);
			this.orrsCommandPromptService.insert(prompt);
		}
	}

	@ServiceMethodAuthority(type = ServiceMethodType.DELETE)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )			
	@Override
	public DefaultResult<Boolean> deleteCommand(TbOrrsCommand command) throws ServiceException, Exception {
		if (null == command || this.isBlank(command.getOid())) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		TbOrrsCommand oldCommand = this.orrsCommandService.selectByEntityPrimaryKey(command).getValueEmptyThrowMessage();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("cmdId", oldCommand.getCmdId());
		if (this.orrsTaskCmdService.count(paramMap) > 0) {
			throw new ServiceException(BaseSystemMessage.dataCannotDelete());
		}
		this.deletePrompts(oldCommand);
		this.deleteAdv(oldCommand);
		return this.orrsCommandService.delete(command);
	}
	
	private void deletePrompts(TbOrrsCommand command) throws ServiceException, Exception {
		if (null == command || this.isBlank(command.getOid())) {
			return;
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("cmdId", command.getCmdId());
		DefaultResult<List<TbOrrsCommandPrompt>> promptsResult = this.orrsCommandPromptService.selectListByParams(paramMap);
		List<TbOrrsCommandPrompt> promptList = promptsResult.getValue();
		for (int i = 0; !CollectionUtils.isEmpty(promptList) && i < promptList.size(); i++) {
			this.orrsCommandPromptService.delete(promptList.get(i));
		}		
	}
	
	private void deleteAdv(TbOrrsCommand command) throws ServiceException, Exception {
		if (null == command || this.isBlank(command.getOid())) {
			return;
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("cmdId", command.getCmdId());
		DefaultResult<List<TbOrrsCommandAdv>> advResult = this.orrsCommandAdvService.selectListByParams(paramMap);
		List<TbOrrsCommandAdv> advList = advResult.getValue();
		for (int i = 0; !CollectionUtils.isEmpty(advList) && i < advList.size(); i++) {
			this.orrsCommandAdvService.delete(advList.get(i));
		}	
	}	
	
	@ServiceMethodAuthority(type = ServiceMethodType.UPDATE)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )	
	@Override
	public DefaultResult<TbOrrsCommand> updateCommand(TbOrrsCommand command) throws ServiceException, Exception {
		if (null == command || this.isBlank(command.getOid())) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		TbOrrsCommand oldCommand = this.orrsCommandService.selectByEntityPrimaryKey(command).getValueEmptyThrowMessage();
		this.deletePrompts(oldCommand);
		this.deleteAdv(oldCommand);
		this.createPrompts(oldCommand, command.getPrompts());
		this.setStringValueMaxLength(command, "description", MAX_DESCRIPTION_LENGTH);
		return this.orrsCommandService.update(command);
	}

	@ServiceMethodAuthority(type = ServiceMethodType.SELECT)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )		
	@Override
	public DefaultResult<TbOrrsCommand> selectCommand(TbOrrsCommand command) throws ServiceException, Exception {
		if (null == command || this.isBlank(command.getOid())) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}		
		DefaultResult<TbOrrsCommand> cmdResult = this.orrsCommandService.selectByEntityPrimaryKey(command);
		TbOrrsCommand oldCommand = cmdResult.getValueEmptyThrowMessage();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("cmdId", oldCommand.getCmdId());
		oldCommand.setPrompts( this.orrsCommandPromptService.selectListByParams(paramMap).getValue() );		
		return cmdResult;
	}

	@ServiceMethodAuthority(type = ServiceMethodType.INSERT)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )	
	@Override
	public DefaultResult<TbOrrsTask> createTask(TbOrrsTask task) throws ServiceException, Exception {
		if (null == task || this.isBlank(task.getTaskId()) || this.isBlank(task.getName()) || this.isBlank(task.getCronExpr())) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		if (!CronExpression.isValidExpression(task.getCronExpr())) {
			throw new ServiceException(BaseSystemMessage.parameterIncorrect());
		}
		this.setStringValueMaxLength(task, "description", MAX_DESCRIPTION_LENGTH);
		DefaultResult<TbOrrsTask> result = this.orrsTaskService.insert(task);
		this.createTaskCmds(task, task.getCmds());
		return result;
	}
	
	private void createTaskCmds(TbOrrsTask task, List<TbOrrsTaskCmd> cmds) throws ServiceException, Exception {
		if (CollectionUtils.isEmpty(task.getCmds())) {
			return;
		}
		for (int i = 0; i < cmds.size() && i < OrrsConstants.MAX_COMMAND_RECORD; i++) {
			TbOrrsTaskCmd cmd = cmds.get(i);
			if (PleaseSelect.noSelect(cmd.getCmdId())) {
				continue;
			}
			cmd.setTaskId(task.getTaskId());
			cmd.setEnableFlag(YesNo.YES);
			cmd.setItemSeq((short)i);
			this.orrsTaskCmdService.insert(cmd);
		}
	}

	@ServiceMethodAuthority(type = ServiceMethodType.DELETE)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )	
	@Override
	public DefaultResult<Boolean> deleteTask(TbOrrsTask task) throws ServiceException, Exception {
		if (null == task || this.isBlank(task.getOid())) {
			throw new ServiceException( BaseSystemMessage.parameterBlank() );
		}
		TbOrrsTask oldTask = this.orrsTaskService.selectByEntityPrimaryKey(task).getValueEmptyThrowMessage();
		this.deleteTaskCmds(oldTask);
		this.deleteTaskResult(oldTask);
		return this.orrsTaskService.delete(task);
	}
	
	private void deleteTaskCmds(TbOrrsTask task) throws ServiceException, Exception {
		if (null == task || this.isBlank(task.getOid())) {
			return;
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("taskId", task.getTaskId());
		DefaultResult<List<TbOrrsTaskCmd>> taskCmdResult = this.orrsTaskCmdService.selectListByParams(paramMap);
		List<TbOrrsTaskCmd> taskCmdList = taskCmdResult.getValue();
		for (int i = 0; !CollectionUtils.isEmpty(taskCmdList) && i < taskCmdList.size(); i++) {
			this.orrsTaskCmdService.delete(taskCmdList.get(i));
		}		
	}
	
	private void deleteTaskResult(TbOrrsTask task) throws ServiceException, Exception {
		if (null == task || this.isBlank(task.getOid())) {
			return;
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("taskId", task.getTaskId());
		DefaultResult<List<TbOrrsTaskResult>> taskCmdResult = this.orrsTaskResultService.selectListByParams(paramMap);
		List<TbOrrsTaskResult> taskResultList = taskCmdResult.getValue();
		for (int i = 0; !CollectionUtils.isEmpty(taskResultList) && i < taskResultList.size(); i++) {
			this.orrsTaskResultService.delete(taskResultList.get(i));
		}		
	}

	@ServiceMethodAuthority(type = ServiceMethodType.SELECT)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )	
	@Override
	public DefaultResult<TbOrrsTask> selectTask(TbOrrsTask task) throws ServiceException, Exception {
		if (null == task || this.isBlank(task.getOid())) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		DefaultResult<TbOrrsTask> taskResult = this.orrsTaskService.selectByEntityPrimaryKey(task);
		TbOrrsTask oldTask = taskResult.getValueEmptyThrowMessage();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("taskId", oldTask.getTaskId());
		oldTask.setCmds( this.orrsTaskCmdService.selectListByParams(paramMap).getValue() );
		return taskResult;
	}
	
	@ServiceMethodAuthority(type = ServiceMethodType.UPDATE)
	@Transactional(
			propagation=Propagation.REQUIRED, 
			readOnly=false,
			rollbackFor={RuntimeException.class, IOException.class, Exception.class} )	
	@Override
	public DefaultResult<TbOrrsTask> updateTask(TbOrrsTask task) throws ServiceException, Exception {
		if (null == task || this.isBlank(task.getOid())) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		TbOrrsTask oldTask = this.orrsTaskService.selectByEntityPrimaryKey(task).getValueEmptyThrowMessage();
		if (!CronExpression.isValidExpression(task.getCronExpr())) {
			throw new ServiceException(BaseSystemMessage.parameterIncorrect());
		}		
		this.setStringValueMaxLength(task, "description", MAX_DESCRIPTION_LENGTH);
		this.deleteTaskCmds(oldTask);
		this.createTaskCmds(task, task.getCmds());
		return this.orrsTaskService.update(task);
	}
	
}
