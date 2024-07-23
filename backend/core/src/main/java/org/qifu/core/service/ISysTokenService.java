package org.qifu.core.service;

import java.util.Date;
import java.util.List;

import org.qifu.base.exception.ServiceException;
import org.qifu.base.service.IBaseService;
import org.qifu.core.entity.TbSysToken;
import org.qifu.core.vo.BoardDemo;

public interface ISysTokenService<T, E> extends IBaseService<TbSysToken, String> {

	public List<BoardDemo> findCountSizeByDatePerUser(Date date1, Date date2) throws ServiceException, Exception;
	
	public Long countSizeByDate(Date date1, Date date2) throws ServiceException, Exception;	
	
}
