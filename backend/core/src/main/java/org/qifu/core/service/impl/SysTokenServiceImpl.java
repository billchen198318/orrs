package org.qifu.core.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.qifu.base.exception.ServiceException;
import org.qifu.base.mapper.IBaseMapper;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.service.BaseService;
import org.qifu.core.entity.TbSysToken;
import org.qifu.core.mapper.TbSysTokenMapper;
import org.qifu.core.service.ISysTokenService;
import org.qifu.core.vo.BoardDemo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Service
@Transactional(propagation=Propagation.REQUIRED, timeout=300, readOnly=true)
public class SysTokenServiceImpl extends BaseService<TbSysToken, String> implements ISysTokenService<TbSysToken, String> {
	
	@Autowired
	TbSysTokenMapper sysTokenMapper;
	
	@Override
	protected IBaseMapper<TbSysToken, String> getBaseMapper() {
		return this.sysTokenMapper;
	}

	@Override
	public List<BoardDemo> findCountSizeByDatePerUser(Date date1, Date date2) throws ServiceException, Exception {
		if (null == date1 || null == date2) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("date1", date1);
		paramMap.put("date2", date2);
		return sysTokenMapper.findCountSizeByDatePerUser(paramMap);
	}

	@Override
	public Long countSizeByDate(Date date1, Date date2) throws ServiceException, Exception {
		if (null == date1 || null == date2) {
			throw new ServiceException(BaseSystemMessage.parameterBlank());
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("date1", date1);
		paramMap.put("date2", date2);		
		return sysTokenMapper.countSizeByDate(paramMap);
	}
	
}
