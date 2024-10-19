package org.orrs.logic;

import org.orrs.entity.TbOrrsCommand;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.DefaultResult;

public interface IOrrsLogicService {
	
	public DefaultResult<TbOrrsCommand> createCommand(TbOrrsCommand command) throws ServiceException, Exception;
	
	public DefaultResult<Boolean> deleteCommand(TbOrrsCommand command) throws ServiceException, Exception;
	
}
