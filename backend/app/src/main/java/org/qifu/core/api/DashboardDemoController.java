package org.qifu.core.api;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.qifu.base.exception.ControllerException;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.model.QueryResult;
import org.qifu.core.entity.TbSysToken;
import org.qifu.core.service.ISysTokenService;
import org.qifu.core.util.CoreApiSupport;
import org.qifu.core.vo.BoardDemo;
import org.qifu.util.SimpleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "DASHBOARD_DEMO", description = "index page data from admin.")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/DashboardDemo")
public class DashboardDemoController extends CoreApiSupport {
	private static final long serialVersionUID = -5269105329111725657L;
	
	@Autowired
	ISysTokenService<TbSysToken, String> sysTokenService;
	
	@Operation(summary = "DASHBOARD_DEMO - findBoardDemo", description = "查核TbSysToken資料")
	@ResponseBody
	@PostMapping(value = "/findBoardDemo", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<QueryResult<List<BoardDemo>>> findBoardDemo() {
		QueryResult<List<BoardDemo>> result = this.initResult();
		try {
			Date d2 = new Date();
			Date d1 = DateUtils.addDays(d2, -6);
			List<BoardDemo> val = this.sysTokenService.findCountSizeByDatePerUser(d1, d2);
			result.setValue(val);
			result.setSuccess( YES );
		} catch (ServiceException | ControllerException e) {
			this.noSuccessResult(result, e);
		} catch (Exception e) {
			this.noSuccessResult(result, e);
		}
		return ResponseEntity.ok().body(result);
	}	
	
	@Operation(summary = "DASHBOARD_DEMO - findSize", description = "查核TbSysToken資料")
	@ResponseBody
	@PostMapping(value = "/findSize", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<QueryResult<List<BoardDemo>>> findPage() {
		QueryResult<List<BoardDemo>> result = this.initResult();
		try {
			List<BoardDemo> val = new LinkedList<BoardDemo>();
			String currDateStr = SimpleUtils.getStrYMD("-");
			Date currDate = DateUtils.parseDate(currDateStr + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
			for (int d = 6; d >= 0; d--) {
				Date perDate = DateUtils.addDays(currDate, d*-1);
				String perDateStr = SimpleUtils.getStrYMD(perDate, "-");
				Date d1 = DateUtils.parseDate(perDateStr + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
				Date d2 = DateUtils.parseDate(perDateStr + " 23:59:59.999", "yyyy-MM-dd HH:mm:ss.SSS");
				BoardDemo b = new BoardDemo();
				b.setCountSize( this.sysTokenService.countSizeByDate(d1, d2) );
				b.setDate(perDateStr);
				b.setUserId("");
				val.add(b);
			}
			result.setValue(val);
			result.setSuccess( YES );
		} catch (ServiceException | ControllerException e) {
			this.noSuccessResult(result, e);
		} catch (Exception e) {
			this.noSuccessResult(result, e);
		}
		return ResponseEntity.ok().body(result);
	}	
	
	
}
