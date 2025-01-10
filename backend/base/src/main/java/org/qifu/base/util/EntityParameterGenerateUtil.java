/* 
 * Copyright 2019-2021 qifu of copyright Chen Xin Nien
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
package org.qifu.base.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.qifu.base.model.CreateDateField;
import org.qifu.base.model.CreateField;
import org.qifu.base.model.CreateUserField;
import org.qifu.base.model.EntityPK;
import org.qifu.base.model.EntityUK;
import org.qifu.base.model.UpdateDateField;
import org.qifu.base.model.UpdateField;
import org.qifu.base.model.UpdateUserField;

public class EntityParameterGenerateUtil {
	
	private EntityParameterGenerateUtil() {
		throw new IllegalStateException("Utility class: EntityParameterGenerateUtil");
	}
	
	public static Map<String, Object> createParamMap(String paramName, String value) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put(paramName, value);
		return paramMap;
	}
	
	public static boolean foundUniqueKey(Object entityObject) {
		Method[] methods = entityObject.getClass().getMethods();
		if (methods==null) {
			return false;
		}
		boolean found = false;
		for (int ix = 0; ix < methods.length && !found; ix++) {
			Annotation[] annotations = methods[ix].getDeclaredAnnotations();
			if (annotations == null) {
				continue;
			}
			for(Annotation annotation : annotations) {
				if(annotation instanceof EntityUK) {
					found = true;
				}
			}
		}
		return found;
	}
	
	public static Map<String, Object> getUKParameter(Object entityObject) {
		Method[] methods=entityObject.getClass().getMethods();
		if (methods==null) {
			return Collections.emptyMap();
		}
		Map<String, Object> ukMap=new HashMap<>();
		for (int ix=0; ix<methods.length; ix++) {
			Annotation[] annotations=methods[ix].getDeclaredAnnotations();
			if (annotations==null) {
				continue;
			}
			fillUKParameterParam(entityObject, annotations, methods, ukMap, ix);
		}		
		return ukMap;
	}	
	
	private static void fillUKParameterParam(Object entityObject, Annotation[] annotations, Method[] methods, Map<String, Object> ukMap, int methodIndex) {
		for(Annotation annotation : annotations) {
			if(!(annotation instanceof @SuppressWarnings("unused") EntityUK enuk &&  (methods[methodIndex].getName().indexOf("get")==0))) {
				continue;
			}
			boolean r = true;
			for (int nx=0; r && nx<annotations.length; nx++) {
				if (annotations[nx] instanceof @SuppressWarnings("unused") EntityUK euk) {
					try {
						ukMap.put(((EntityUK)annotations[nx]).name(), methods[methodIndex].invoke(entityObject));
						r = false;
					} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
						e.printStackTrace();
					}							
				}
			}
		}		
	}
	
	public static Map<String, Object> getPKParameter(Object entityObject) {
		Method[] methods=entityObject.getClass().getMethods();
		if (methods==null) {
			return Collections.emptyMap();
		}
		Map<String, Object> ukMap=new HashMap<>();
		for (int ix=0; ix<methods.length; ix++) {
			Annotation[] annotations=methods[ix].getDeclaredAnnotations();
			if (annotations==null) {
				continue;
			}
			fillPKParameterParam(entityObject, annotations, methods, ukMap, ix);
		}		
		return ukMap;
	}	
	
	private static void fillPKParameterParam(Object entityObject, Annotation[] annotations, Method[] methods, Map<String, Object> ukMap, int methodIndex) {
		for(Annotation annotation : annotations) {
			if(!(annotation instanceof @SuppressWarnings("unused") EntityPK enpk &&  (methods[methodIndex].getName().indexOf("get")==0))) {
				continue;
			}
			boolean r = true;
			for (int nx = 0; r && nx < annotations.length; nx++) {
				if (annotations[nx] instanceof @SuppressWarnings("unused") EntityPK epk) {
					try {
						ukMap.put(((EntityPK)annotations[nx]).name(), methods[methodIndex].invoke(entityObject));
						r = false;
					} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
						e.printStackTrace();
					}							
				}
			}
		}
	}
	
	public static EntityPK getPrimaryKeyField(Object entityObject) {
		Method[] methods=entityObject.getClass().getMethods();
		if (methods==null) {
			return null;
		}
		EntityPK field = null;
		for (int ix=0; ix<methods.length; ix++) {
			Annotation[] annotations=methods[ix].getDeclaredAnnotations();
			if (annotations==null) {
				continue;
			}
			for(Annotation annotation : annotations) {
				if(annotation instanceof @SuppressWarnings("unused") EntityPK epk) {
					field = ((EntityPK)annotation);			
				}
			}
		}
		return field;
	}		
	
	public static UpdateField getUpdateField(Object entityObject) {
		Method[] methods=entityObject.getClass().getMethods();
		if (methods==null) {
			return null;
		}
		UpdateField field = new UpdateField();
		for (int ix=0; ix<methods.length; ix++) {
			Annotation[] annotations=methods[ix].getDeclaredAnnotations();
			if (annotations==null) {
				continue;
			}
			for(Annotation annotation : annotations) {
				if (annotation instanceof @SuppressWarnings("unused") UpdateUserField uf) {
					field.setUpdateUserField( (UpdateUserField)annotation );					
				}
				if (annotation instanceof @SuppressWarnings("unused") UpdateDateField uf) {
					field.setUpdateDateField( (UpdateDateField)annotation );
				}
			}
		}
		return field;
	}	
	
	public static CreateField getCreateField(Object entityObject) {
		Method[] methods=entityObject.getClass().getMethods();
		if (methods==null) {
			return null;
		}
		CreateField field = new CreateField();
		for (int ix=0; ix<methods.length; ix++) {
			Annotation[] annotations=methods[ix].getDeclaredAnnotations();
			if (annotations==null) {
				continue;
			}
			for(Annotation annotation : annotations) {
				if (annotation instanceof @SuppressWarnings("unused") CreateUserField cf) {
					field.setCreateUserField( (CreateUserField)annotation );		
				}
				if (annotation instanceof @SuppressWarnings("unused") CreateDateField cf) {
					field.setCreateDateField( (CreateDateField)annotation );
				}
			}
		}
		return field;		
	}
	
}
