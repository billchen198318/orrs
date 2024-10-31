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
package org.orrs.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.orrs.OrrsResultType;
import org.orrs.entity.TbOrrsCommand;
import org.orrs.entity.TbOrrsTaskResult;
import org.orrs.service.IOrrsCommandService;
import org.orrs.service.IOrrsTaskResultService;
import org.orrs.util.MarkdownCodeExtractor;
import org.qifu.base.Constants;
import org.qifu.base.exception.ControllerException;
import org.qifu.base.exception.ServiceException;
import org.qifu.base.message.BaseSystemMessage;
import org.qifu.base.model.ControllerMethodAuthority;
import org.qifu.base.support.TokenStoreValidateBuilder;
import org.qifu.base.util.TokenBuilderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.auth0.jwt.interfaces.Claim;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ORRS001D0003OtherController {
	
	@Autowired
	private DataSource dataSource;	
	
	@Autowired
	IOrrsTaskResultService<TbOrrsTaskResult, String> orrsTaskResultService;	
	
	@Autowired
	IOrrsCommandService<TbOrrsCommand, String> orrsCommandService;		
	
	@ControllerMethodAuthority(check = false, programId = "ORRS001D0003Q")
	@RequestMapping(value = "/previewResult")
	public String processReport(ModelMap mm, HttpServletRequest request, HttpServletResponse response, @RequestParam("oid") String oid) throws UnsupportedEncodingException, IOException {
		String content = "";
		byte[] data = "no task result data!".getBytes();
		TbOrrsTaskResult taskResult = null;
		TbOrrsCommand command = new TbOrrsCommand();
		try {
			taskResult = this.orrsTaskResultService.selectByPrimaryKey(oid).getValue();
			if (taskResult == null) {
				content = "<html><body><h2>" + BaseSystemMessage.searchNoData() + "</h2></body></html>";
				return null;
			}
			command.setCmdId(taskResult.getCmdId());
			command = this.orrsCommandService.selectByUniqueKey(command).getValueEmptyThrowMessage();
			
			String qifutoken = StringUtils.defaultString(request.getParameter("qifutoken"));
			TokenStoreValidateBuilder tsv = TokenStoreValidateBuilder.build(this.dataSource);
			Map<String, Claim> claimToken = TokenBuilderUtils.verifyToken(qifutoken.replaceFirst(Constants.TOKEN_PREFIX, "").replaceAll(" ", ""), tsv);
			if (!TokenBuilderUtils.existsInfo(claimToken)) {
				content = "<html><body><h2>permission denied!</h2></body></html>";
				return null;
			}
			if (OrrsResultType.HTML.name().equals(command.getResultType())) {
				String htmlString = "";
				if (taskResult.getContent() != null) {
					htmlString = new String(taskResult.getContent(), StandardCharsets.UTF_8);
				}
				if (taskResult.getInvokeContent() != null) {
					htmlString = new String(taskResult.getInvokeContent(), StandardCharsets.UTF_8);
				}
				content = MarkdownCodeExtractor.parseHtml(htmlString);
				if (StringUtils.isBlank(content)) {
					content = htmlString;
				}				
				if (StringUtils.isBlank(content)) {
					content = "<html><body><h2>no task result data!</h2></body></html>";
				}
			} else {
				if (taskResult.getContent() != null) {
					data = taskResult.getContent();
				}
				if (taskResult.getInvokeContent() != null) {
					data = taskResult.getInvokeContent();
				}
			}
		} catch (ServiceException | ControllerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
			if (!StringUtils.isBlank(content)) {
				response.setContentType("text/html");
				response.getOutputStream().print(content);
				response.flushBuffer();						
			} else {
				response.setContentType("application/octet-stream");
                response.getOutputStream().write(data);
                response.flushBuffer();
			}
			
		}
		return null;
	}
	
}
