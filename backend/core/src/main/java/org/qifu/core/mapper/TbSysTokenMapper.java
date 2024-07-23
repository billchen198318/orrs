package org.qifu.core.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.qifu.base.mapper.IBaseMapper;
import org.qifu.core.entity.TbSysToken;
import org.qifu.core.vo.BoardDemo;

@Mapper
public interface TbSysTokenMapper extends IBaseMapper<TbSysToken, String> {
	
	public List<BoardDemo> findCountSizeByDatePerUser(Map<String, Object> param);
	
	public Long countSizeByDate(Map<String, Object> param);
	
}
