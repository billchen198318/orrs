/**
package org.orrs.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.orrs.entity.TbOrrsDoc;
import org.orrs.service.IOrrsDocService;
import org.qifu.Application;
import org.qifu.base.AppContext;
import org.qifu.base.Constants;
import org.qifu.base.model.BaseUserInfo;
import org.qifu.base.util.UserLocalUtils;
import org.qifu.core.util.UserUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.nimbusds.jose.util.IOUtils;

@SpringBootApplication
public class ReadToOrrsDoc {
	
	static IOrrsDocService<TbOrrsDoc, String> orrsDocService;
	
	static int pos = 0;
	
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
		AppContext.init(context);
		orrsDocService = (IOrrsDocService<TbOrrsDoc, String>) context.getBean(IOrrsDocService.class);
		test();
	}
	
	private static BaseUserInfo buildUser() {
		BaseUserInfo userInfo = new BaseUserInfo();
		userInfo.setUserId( Constants.SYSTEM_BACKGROUND_USER );
		UserLocalUtils.setUserInfo(userInfo);
		return userInfo;
	}
	
	public static void test() {
		buildUser();
		Path startDir = Paths.get("C:/Users/USER/Downloads/20250130_data/S01/src/main/java"); // 這裡填上目錄的路徑
        try {
            List<Path> javaFiles = getJavaFiles(startDir);
            javaFiles.forEach(srcFile -> {
            	try {
					fileToOrrsDoc(srcFile.toFile());
				} catch (IOException e) {
					e.printStackTrace();
				}
            });
        } catch (IOException e) {
            e.printStackTrace();
        }        
        UserUtils.removeForUserLocalUtils();
    }
	
	private static void fileToOrrsDoc(File srcFile) throws IOException {
		String strData = IOUtils.readFileToString(srcFile);
		String str1 = "後端程式碼名稱: " + srcFile.getName() + "\n"
				+ "這些程式是 david 寫的, 廠商共用性系統 S01 專案, 相關程式碼.\n```java\n";
		String str2 = "\n```";
		String content = str1 + strData + str2;
		if (content.length() > 65535) {
			return;
		}
		String id = "SIPA" + StringUtils.leftPad(String.valueOf(pos+1), 4, "0");
		TbOrrsDoc doc = new TbOrrsDoc();
		doc.setDocId(id);
		doc.setName(srcFile.getName());
		doc.setContent(content);
		doc.setSysPromptTpl("關於提問問題, 如果與 S01 專案 或 java 相關的話, 參考下面相關資料:\n{information}");
		doc.setTplVariable("information");
		orrsDocService.insert(doc);
		pos++;
	}
	
    public static List<Path> getJavaFiles(Path dir) throws IOException {
        List<Path> javaFiles = new ArrayList<>();
        
        // 使用 Files.walk 來遍歷目錄及子目錄
        Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                // 檢查檔案是否是 .java 檔案
                if (file.toString().endsWith(".java")) {
                    javaFiles.add(file);
                }
                return FileVisitResult.CONTINUE;
            }
        });
        
        return javaFiles;
    }
    
}
*/