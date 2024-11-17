/* 
 * Copyright 2019-2024 qifu of copyright Chen Xin Nien
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
package org.orrs.config;

import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class OrrsOllamaConfig {
	
	@Bean
	@DependsOn("ollamaApi")
	public OllamaEmbeddingModel ollamaEmbeddingModel(OllamaApi ollamaApi) {
		return OllamaEmbeddingModel.builder().withOllamaApi(ollamaApi).withDefaultOptions(OllamaOptions.create().withModel("nomic-embed-text")).build();
	}
	
	@Bean
	@DependsOn("ollamaEmbeddingModel")	
	public VectorStore vectorStore(OllamaEmbeddingModel ollamaEmbeddingModel) {
		return new SimpleVectorStore(ollamaEmbeddingModel);
	}
	
}
