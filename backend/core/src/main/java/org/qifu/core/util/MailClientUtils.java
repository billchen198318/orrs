/* 
 * Copyright 2012-2016 bambooCORE, greenstep of copyright Chen Xin Nien
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
package org.qifu.core.util;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.qifu.base.AppContext;
import org.qifu.base.Constants;
import org.qifu.base.properties.SpringMailSessionConfigProperties;
import org.qifu.core.model.MailContent;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

public class MailClientUtils {
	
	private static JavaMailSender mailSender;
	
	private static SpringMailSessionConfigProperties springMailSessionConfigProperties;
	
	protected MailClientUtils() {
		throw new IllegalStateException("Utils class: MailClientUtils");
	}
	
	static {
		mailSender = AppContext.getContext().getBean(JavaMailSender.class);
		springMailSessionConfigProperties = AppContext.getContext().getBean(SpringMailSessionConfigProperties.class);
	}
	
	public static void send(
			String from, String to, 
			MailContent mc) throws MailException, MessagingException {
		send(from, to, null, null, null, null, mc);
	}		
	
	public static void send(
			String from, String to, 
			String[] cc, 
			MailContent mc) throws MailException, MessagingException {
		send(from, to, cc, null, null, null, mc);
	}	
	
	public static void send(
			String from, String to, 
			String[] cc, String[] bcc, 
			MailContent mc) throws MailException, MessagingException {
		send(from, to, cc, bcc, null, null, mc);
	}
	
	public static void send(
			String from, String to, 
			String cc, String bcc, 
			MailContent mc) throws MailException, MessagingException {
		
		String[] mailCc = null;
		String[] mailBcc = null;
		if (!StringUtils.isBlank(cc)) {
			mailCc = cc.split(Constants.DEFAULT_SPLIT_DELIMITER);
		}
		if (!StringUtils.isBlank(bcc)) {
			mailBcc = bcc.split(Constants.DEFAULT_SPLIT_DELIMITER);
		}
		send(from, to, mailCc, mailBcc, mc);
	}
	
	public static void send(
			String from, String to, 
			String[] cc, String[] bcc, 
			String[] fileNames, File[] files,
			MailContent mc) throws MailException, MessagingException {
		
		if (mailSender==null) {
			throw new IllegalArgumentException("null mailSender!");
		}
		if (StringUtils.isBlank(from) || StringUtils.isBlank(to)) {
			throw new IllegalArgumentException("from and to is required!");
		}
		if (fileNames!=null && files!=null &&  (fileNames.length != files.length)) {
			throw new IllegalArgumentException("File parameter error!");
		}
		mailSender.send( getMimeMessage(from, to, cc, bcc, fileNames, files, mc) );
	}
	
	private static MimeMessage getMimeMessage(String from, String to, 
			String[] cc, String[] bcc, 
			String[] fileNames, File[] files,
			MailContent mc) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, Constants.BASE_ENCODING);
		message.getSession().setDebug( springMailSessionConfigProperties.enableDebug() );
		helper.setFrom(from);
		String[] tos = to.split(Constants.DEFAULT_SPLIT_DELIMITER);
		for (String t : tos) {
			t = StringUtils.deleteWhitespace(StringUtils.defaultString(t));
			if (StringUtils.defaultString(t).length() > 0) {
				helper.addTo(t);
			}
		}
		helper.setSubject(mc.getSubject());
		helper.setText(mc.getContent(), true);
		if (null!=cc && cc.length>0) {
			helper.setCc(cc);
		}
		if (null!=bcc && bcc.length>0) {
			helper.setBcc(bcc);
		}
		for (int i=0; fileNames!=null && i<fileNames.length; i++) {
			helper.addAttachment(fileNames[i], files[i]);
		}
		return message;
	}
	
}
