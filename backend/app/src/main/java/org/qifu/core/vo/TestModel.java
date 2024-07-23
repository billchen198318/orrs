/**
 * test
 */
package org.qifu.core.vo;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "TestModel", description = "測試模組")
public class TestModel implements java.io.Serializable {
	private static final long serialVersionUID = -5289888544046385634L;
	
	@Schema(name = "key", description = "編號")
	private String key;
	
	@Schema(name = "msg", description = "訊息資料")
	private String msg;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
