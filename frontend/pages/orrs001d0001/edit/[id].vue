<script>
import { watch } from "vue";
import Swal from 'sweetalert2';
import { toast } from 'vue3-toastify';
import 'vue3-toastify/dist/index.css';

import { Codemirror } from 'vue-codemirror'
import { markdown, markdownLanguage } from '@codemirror/lang-markdown'
import { languages } from '@codemirror/language-data'
import { oneDark } from '@codemirror/theme-one-dark'

import Toolbar from '@/components/Toolbar.vue';
import { PageConstants } from '../config';
import { 
	getAxiosInstance, 
	invalidFeedback,
	checkInvalid,
	escapeQifuHtmlMsg,
	getProgItem,
	getUrlPrefixFromProgItem
} from '../../../components/BaseHelper';

let checkFields = new Object();

export default {
	components: { Toolbar, Codemirror },
	setup() { 
		definePageMeta({ middleware : ['auth'] });

		const cmRef = ref();
		const cmOptions = {
			mode: "text/x-markdown",
		};

		const cmExtensions = [
			markdown({ base: markdownLanguage, codeLanguages: languages })
			/*, oneDark*/ 
		];

		return {
			cmRef,
			cmOptions,
			cmExtensions
		};

	},
	data() {
		return {
			pageProgramId : PageConstants.EditId,
			checkFields,
			formParam : {
				oid : this.$route.params.id,
				cmdId : '',
				name : '',
				userMessage : '',
				resultVariable : '',
				resultType : 'GROOVY',
				description : '',
				prompts : [],
				llmModel : 'gemma2',
				resultAlwNul : 'N'
			},
			variablePreviousMessage : import.meta.env.VITE_VARIABLE_PREVIOUS_MESSAGE,
			variablePreviousInvokeResult : import.meta.env.VITE_VARIABLE_PREVIOUS_INVOKE_RESULT,
			llmModelList : [],
			resultAlwNulSw : false,
			docRetrievalSw : false
		}
	},
	methods: { 
		fieldInvalidFeedback : invalidFeedback,
		fieldCheckInvalid : checkInvalid,
		btnBack : function() {
			this.$router.back();
		},
		btnUpdate : _btnUpdate,
		btnClear : function() {
			this.checkFields = new Object();
			//this.formParam.cmdId = '';
			this.formParam.name = '';
			this.formParam.userMessage = '';
			this.formParam.resultVariable = '';
			this.formParam.resultType = 'GROOVY';
			this.formParam.description = '';
			this.formParam.prompts = [];
			this.formParam.llmModel = 'gemma2';
			this.formParam.resultAlwNul = 'N';
			this.resultAlwNulSw = false;			
			this.formParam.docRetrieval = 'N';
			this.docRetrievalSw = false;
			this.formParam.simThreshold = 1.00;			
		},
		loadData : _loadData,
		btnAddPrompt : function() {
			if (this.formParam.prompts.length >= 10) {
				toast.warning('最多' + this.formParam.prompts.length + '筆prompt');
				return;
			}
			this.formParam.prompts.push({
				itemSeq : (this.formParam.prompts.length + 1),
				promptContent : ''
			});
		},
		btnDelPromptAt : function(position) {
			if (this.formParam.prompts.length < position) {
				return;
			}
			this.formParam.prompts.splice(position, 1);
		},
		btnAddUserMessage : function(variable) {
			this.formParam.userMessage = this.formParam.userMessage + ' ' + variable;
		},
		loadLlmModel : function() {
			var that = this;
			Swal.fire({title: "Loading...", html: "請等待", showConfirmButton: false, allowOutsideClick: false});
			Swal.showLoading();      
			let axiosInstance = getAxiosInstance();
			axiosInstance.post(import.meta.env.VITE_API_URL + PageConstants.eventNamespace + '/loadLlmModelList', this.formParam)
			.then(response => {
				Swal.hideLoading();
				Swal.close();
				if (null != response.data) {
					this.llmModelList = response.data.value;
					that.loadData();
				} else {
					toast.error('error, null');
				}
			})
			.catch(e => {
				Swal.hideLoading();
				Swal.close();        
				alert(e);
			});
		},
		cmOnChange : function(val, cm) {
			this.formParam.userMessage = val;
		},
		cmOnInput : function(val) {
			this.formParam.userMessage = val;
		},
		cmOnReady : function(cm) {

		}		
	},
	created() { 
		watch(() => this.resultAlwNulSw, (newVal, oldVal) => {
			if (newVal) {
				this.formParam.resultAlwNul = 'Y';
			} else {
				this.formParam.resultAlwNul = 'N';
			}
		});	
		watch(() => this.docRetrievalSw, (newVal, oldVal) => {
			if (newVal) {
				this.formParam.docRetrieval = 'Y';
			} else {
				this.formParam.docRetrieval = 'N';
			}
		});				
	},
	mounted() { 
		//this.loadData();
		this.loadLlmModel();
	}
}

function _loadData() {
    Swal.fire({title: "Loading...", html: "請等待", showConfirmButton: false, allowOutsideClick: false});
    Swal.showLoading(); 
    let axiosInstance = getAxiosInstance();
    axiosInstance.post(import.meta.env.VITE_API_URL + PageConstants.eventNamespace + '/load', {'oid' : this.formParam.oid})
    .then(response => {
        Swal.hideLoading();
        Swal.close();
        if (null != response.data) {
            if (import.meta.env.VITE_SUCCESS_FLAG != response.data.success) {
                toast.warning(response.data.message);
                this.$router.push( getUrlPrefixFromProgItem( getProgItem(PageConstants.QueryId) ) );
                return;
            }
            this.formParam = response.data.value;
			this.resultAlwNulSw = ( 'Y' == this.formParam.resultAlwNul ? true : false );
			this.docRetrievalSw = ( 'Y' == this.formParam.docRetrieval ? true : false );
        } else {
            toast.error('error, null');
            this.$router.push( getUrlPrefixFromProgItem( getProgItem(PageConstants.QueryId) ) );
        }
    })
    .catch(e => {
        Swal.hideLoading();
        Swal.close();        
        alert(e);
        this.$router.push( getUrlPrefixFromProgItem( getProgItem(PageConstants.QueryId) ) );
    });         
}

function _btnUpdate() {
    this.checkFields = new Object();
    Swal.fire({title: "Loading...", html: "請等待", showConfirmButton: false, allowOutsideClick: false});
    Swal.showLoading();      
    let axiosInstance = getAxiosInstance();
    axiosInstance.post(import.meta.env.VITE_API_URL + PageConstants.eventNamespace + '/update', this.formParam)
    .then(response => {
        Swal.hideLoading();
        Swal.close();
        if (null != response.data) {
            this.checkFields = response.data.checkFields;
            if (import.meta.env.VITE_SUCCESS_FLAG != response.data.success) {
                toast.warning(escapeQifuHtmlMsg(response.data.message));
                return;
            }
            toast.success(response.data.message);
        } else {
            toast.error('error, null');
        }
    })
    .catch(e => {
        Swal.hideLoading();
        Swal.close();        
        alert(e);
    });
}

</script>

<template>
<div class="row">
	<div class="col-xs-12 col-md-12 col-lg-12">
		<Toolbar 
			:progId="this.pageProgramId" 
        	description="llm命令編輯." 
        	marginBottom="Y"
        	refreshFlag="Y"
        	@refreshMethod="loadData"
        	backFlag="Y"
        	@backMethod="btnBack"
        	createFlag="N"
        	@createMethod="null"
        	saveFlag="Y"
        	@saveMethod="btnUpdate"
    	></Toolbar>		
	</div>
</div>
<div class="row">
	<div class="col-xs-6 col-md-6 col-lg-6">
		<label for="cmdId" class="form-label">編號</label>
		<input type="text" v-bind:class="'form-control ' + ( fieldCheckInvalid('cmdId', checkFields) ? 'is-invalid' : ' ')" id="cmdId" placeholder="輸入編號" v-model="this.formParam.cmdId" readonly="true">
		<div v-if="fieldCheckInvalid('cmdId', checkFields)" class="invalid-feedback d-block">{{ fieldInvalidFeedback('cmdId', checkFields) }}</div>
	</div>
	<div class="col-xs-6 col-md-6 col-lg-6">
		<label for="name" class="form-label">名稱</label>
		<input type="text" v-bind:class="'form-control ' + ( fieldCheckInvalid('name', checkFields) ? 'is-invalid' : ' ')" id="name" placeholder="輸入名稱" v-model="this.formParam.name">
		<div v-if="fieldCheckInvalid('name', checkFields)" class="invalid-feedback d-block">{{ fieldInvalidFeedback('name', checkFields) }}</div>
	</div>
</div>
<div class="row">
	<div class="col-xs-12 col-md-12 col-lg-12">
		<label for="userMessage" class="form-label">使用者訊息</label>
		<Codemirror
			v-model="this.formParam.userMessage"
			:options="cmOptions"
			:extensions="cmExtensions"
			:style="{ height: '200px' }"
			 ref="cmRef"
			 @change="cmOnChange"
			 @input="cmOnInput"
			 @ready="cmOnReady"
			 id="userMessage">
		</Codemirror>				
		<div v-if="fieldCheckInvalid('userMessage', checkFields)" class="invalid-feedback d-block">{{ fieldInvalidFeedback('userMessage', checkFields) }}</div>
	</div>
</div>
<p style="margin-bottom: 5px"></p>
<div class="row">
	<div class="col-xs-12 col-md-12 col-lg-12">
		<button type="button" class="btn btn-info" v-on:click="btnAddUserMessage(this.variablePreviousMessage)"><i class="'bi bi-p-circle"></i>&nbsp;上一個任務的訊息</button>
		&nbsp;
		<button type="button" class="btn btn-info" v-on:click="btnAddUserMessage(this.variablePreviousInvokeResult)"><i class="'bi bi-p-circle-fill"></i>&nbsp;上一個任務的Invoke結果</button>
	</div>
</div>		
<p style="margin-bottom: 5px"></p>
<div class="row">
	<div class="col-xs-6 col-md-6 col-lg-6">
		<label for="resultVariable" class="form-label">腳本變數</label>
		<input type="text" v-bind:class="'form-control ' + ( fieldCheckInvalid('resultVariable', checkFields) ? 'is-invalid' : ' ')" id="resultVariable" placeholder="輸入變數名稱" v-model="this.formParam.resultVariable">
		<div v-if="fieldCheckInvalid('resultVariable', checkFields)" class="invalid-feedback d-block">{{ fieldInvalidFeedback('resultVariable', checkFields) }}</div>
	</div>
	<div class="col-xs-6 col-md-6 col-lg-6">
    	<div class="form-group form-floating">
			<div class="form-check form-switch">
				<input class="form-check-input" type="checkbox" role="switch" id="resultAlwNulSw" v-model="this.resultAlwNulSw">
				<label class="form-check-label" for="resultAlwNulSw">Script invoke result allow null</label>
			</div>
    	</div>		
	</div>
</div>
<p style="margin-bottom: 5px"></p>
<div class="row">
	<div class="col-xs-6 col-md-6 col-lg-6">
		<label for="dialogH" class="form-label">LLM模組</label>
		<select class="form-select" aria-label="請選取LLM模組" v-model="this.formParam.llmModel">
			<option v-bind:value="llmItem" v-for="(llmItem, idx) in this.llmModelList">{{ llmItem }}</option>
		</select>
	</div>	
	<div class="col-xs-6 col-md-6 col-lg-6">
		<label for="dialogH" class="form-label">截取內容</label>
		<select class="form-select" aria-label="請選取截取內容" v-model="this.formParam.resultType">
			<option value="GROOVY">GROOVY</option>
			<option value="HTML">HTML</option>
			<option value="TEXT">TEXT</option>
			<option value="JAVA">JAVA</option>
			<option value="JSON">JSON</option>
			<option value="SQL">SQL</option>
			<option value="XML">XML</option>
		</select>
	</div>	
</div>	
<p style="margin-bottom: 5px"></p>
<div class="row">
	<div class="col-xs-6 col-md-6 col-lg-6">
    	<div class="form-group form-floating">
			<div class="form-check form-switch">
				<input class="form-check-input" type="checkbox" role="switch" id="docRetrievalSw" v-model="this.docRetrievalSw">
				<label class="form-check-label" for="docRetrievalSw">Documents Retrieval&nbsp;/&nbsp;文件檢索</label>
			</div>
    	</div>	
	</div>	
	<div class="col-xs-6 col-md-6 col-lg-6">
        <label for="simThreshold" class="form-label">Similarity Threshold&nbsp;/&nbsp;相似度閾值&nbsp;({{ this.formParam.simThreshold }})</label>
        <input type="range" class="form-range" min="0.00" max="1.00" step="0.05" id="simThreshold" v-model="this.formParam.simThreshold">        
	</div>	
</div>	
<p style="margin-bottom: 5px"></p>
<div class="row">
	<div class="col-xs-12 col-md-12 col-lg-12">
		<label for="description" class="form-label">備註</label>
		<input type="text" class="form-control" id="description" v-model="this.formParam.description">
	</div>	
</div>	
<p style="margin-bottom: 5px"></p>
<div class="row">
	<div class="col-xs-12 col-md-12 col-lg-12">
		<button type="button" class="btn btn-info" v-on:click="btnAddPrompt"><i class="'bi bi-plus-circle"></i>&nbsp;增加prompt</button>
	</div>
</div>
<p style="margin-bottom: 5px"></p>
<div class="row" v-for="(p, index) in this.formParam.prompts">
	<div class="col-xs-12 col-md-12 col-lg-12">
		<table class="table">
			<tbody>
				<tr>
					<td width="80%"><input type="text" class="form-control" v-bind:id="'prompt_seq_'+p.itemSeq" v-model="p.promptContent"></td>
					<td width="20%"><button type="button" class="btn btn-warning" v-on:click="btnDelPromptAt(index)"><i class="'bi bi-x-circle"></i>&nbsp;刪除</button></td>
				</tr>
			</tbody>
		</table>
	</div>
	<p style="margin-bottom: 5px"></p>
</div>
<p style="margin-bottom: 10px"></p>
<div class="row">
	<div class="col-xs-12 col-md-12 col-lg-12">
    	<button type="button" class="btn btn-primary" v-on:click="btnUpdate"><i class="'bi bi-save"></i>&nbsp;儲存</button>
    	&nbsp;
    	<button type="button" class="btn btn-primary" v-on:click="btnClear"><i class="'bi bi-eraser"></i>&nbsp;清除</button>		
	</div>
</div>

<br>
<br>

</template>
