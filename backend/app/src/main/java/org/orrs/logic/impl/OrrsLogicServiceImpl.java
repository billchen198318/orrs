package org.orrs.logic.impl;

import java.io.IOException;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.orrs.OrrsConstants;
import org.orrs.entity.TbOrrsCommand;
import org.orrs.entity.TbOrrsCommandAdv;
import org.orrs.entity.TbOrrsCommandPrompt;
import org.orrs.logic.IOrrsLogicService;
import org.orrs.service.IOrrsCommandAdvService;
import org.orrs.service.IOrrsCommandPromptService;
import org.orrs.service.IOrrsCommandService;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.DefaultResult;
import org.qifu.base.model.ServiceAuthority;
import org.qifu.base.model.ServiceMethodAuthority;
import org.qifu.base.model.ServiceMethodType;
import org.qifu.base.service.BaseLogicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
		if (this.isBlank(command.getCmdId()) || this.isBlank(command.getName()) || this.isBlank(command.getUserMessage())
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
		for (int i = 0; i < prompts.size() && i < OrrsConstants.MAX_USER_MESSAGE_SIZE; i++) {
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
	
}
