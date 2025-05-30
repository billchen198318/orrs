/* 
 * Copyright 2012-2017 qifu of copyright Chen Xin Nien
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
package org.qifu.util;

import java.util.Map;

import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.qifu.base.model.ScriptTypeCode;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

public class ScriptExpressionUtils {
	private static CompilerConfiguration groovyCompilerConfig = new CompilerConfiguration();
	private static ThreadLocal<GroovyShell> groovyShellTL = new ThreadLocal<>();
	
	static {
		groovyShellTL.remove();
		groovyCompilerConfig.getOptimizationOptions().put("indy", true);
		groovyCompilerConfig.getOptimizationOptions().put("int", false);		
	}
	
	protected ScriptExpressionUtils() {
		throw new IllegalStateException("Utils class: ScriptExpressionUtils");
	}
	
	public static GroovyShell buildGroovyShell(boolean fromThreadLocal) {
		if (fromThreadLocal) {
			GroovyShell groovyShell = null;
			if ((groovyShell=groovyShellTL.get()) == null) {
				groovyShell = new GroovyShell(groovyCompilerConfig);
				groovyShellTL.set(groovyShell);
			}	
			return groovyShell;			
		}		
		return new GroovyShell(groovyCompilerConfig);
	}
	
	/**
	 * 執行 script 
	 * 
	 * @param type					請參考 ScriptTypeCode , 目前有 BSH, GROOVY, PYTHON
	 * @param scriptExpression		script內容
	 * @param results				輸出的值
	 * @param parameters			代入的值
	 * @return
	 * @throws Exception
	 */
	public static Object execute(String type, String scriptExpression, 
			Map<String, Object> results, Map<String, Object> parameters) throws IllegalArgumentException {
		if (!ScriptTypeCode.isTypeCode(type)) {
			throw new java.lang.IllegalArgumentException("no support script language of : " + type);
		}
		return executeGroovy(scriptExpression, results, parameters);
	}	
	
	private static Object executeGroovy(String scriptExpression, Map<String, Object> results, Map<String, Object> parameters) throws CompilationFailedException {	
		GroovyShell groovyShell = buildGroovyShell(false);
		Binding binding = groovyShell.getContext();		
		if (parameters!=null) {			
			for (Map.Entry<String, Object> entry : parameters.entrySet()) {
				binding.setVariable(entry.getKey(), entry.getValue());				
			}
		}		
		Object r = groovyShell.evaluate(scriptExpression);
		if (results!=null) {
			for (Map.Entry<String, Object> entry : results.entrySet()) {
				entry.setValue( binding.getVariable(entry.getKey()) );
			}
		}
		return r;
	}	
	
}
