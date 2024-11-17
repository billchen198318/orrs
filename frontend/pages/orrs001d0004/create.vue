<script>
import { ref } from "vue";
import Swal from 'sweetalert2';
import { toast } from 'vue3-toastify';
import 'vue3-toastify/dist/index.css';

import { Codemirror } from 'vue-codemirror'
import { markdown, markdownLanguage } from '@codemirror/lang-markdown'
import { languages } from '@codemirror/language-data'
import { oneDark } from '@codemirror/theme-one-dark'

import Toolbar from '@/components/Toolbar.vue';
import { PageConstants } from './config';
import { 
	getAxiosInstance, 
	invalidFeedback,
	checkInvalid,
	escapeQifuHtmlMsg
} from '../../components/BaseHelper';

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
			pageProgramId : PageConstants.CreateId,
			checkFields,
			formParam : {
				docId : '',
				name : '',
				content : '',
				sysPromptTpl : '',
				tplVariable : ''
			}
		}
	},
	methods: { 
		fieldInvalidFeedback : invalidFeedback,
		fieldCheckInvalid : checkInvalid,
		btnBack : function() {
			this.$router.back();
		},
		btnSave : _btnSave,
		btnClear : function() {
			this.checkFields = new Object();
			this.formParam.docId = '';
			this.formParam.name = '';
			this.formParam.content = '';
			this.formParam.sysPromptTpl = '';
			this.formParam.tplVariable = '';
		},
		cmOnChange1 : function(val, cm) {
			this.formParam.content = val;
		},
		cmOnInput1 : function(val) {
			this.formParam.content = val;
		},
		cmOnReady1 : function(cm) {
		},
		cmOnChange2 : function(val, cm) {
			this.formParam.sysPromptTpl = val;
		},
		cmOnInput2 : function(val) {
			this.formParam.sysPromptTpl = val;
		},
		cmOnReady2 : function(cm) {
		}		
	},
	created() { 
	},
	mounted() { 
	}
}

function _btnSave() {
    this.checkFields = new Object();
    Swal.fire({title: "Loading...", html: "請等待", showConfirmButton: false, allowOutsideClick: false});
    Swal.showLoading();      
    let axiosInstance = getAxiosInstance();
    axiosInstance.post(import.meta.env.VITE_API_URL + PageConstants.eventNamespace + '/save', this.formParam)
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
            this.btnClear();
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

<style>
.tab-content-custom {
	height: 70vh;
	background: #ffffff;
	margin-left: -5px;
	margin-right: -5px;
	overflow:auto;
	padding: 4px;
	
}
</style>

<template>
<div class="row">
	<div class="col-xs-12 col-md-12 col-lg-12">
		<Toolbar 
			:progId="this.pageProgramId" 
        	description="檢索文件建立." 
        	marginBottom="Y"
        	refreshFlag="Y"
        	@refreshMethod="btnClear"
        	backFlag="Y"
        	@backMethod="btnBack"
        	createFlag="N"
        	@createMethod="null"
        	saveFlag="Y"
        	@saveMethod="btnSave"
    	></Toolbar>		
	</div>
</div>

<div class="row">
	<div class="col-xs-12 col-md-12 col-lg-12">
		<ul class="nav nav-tabs" id="myTab" role="tablist">
			<li class="nav-item" role="presentation">
				<button class="nav-link active" id="tb01-tab" data-bs-toggle="tab" data-bs-target="#tb01" type="button" role="tab" aria-controls="tb01" aria-selected="true"><b>文件內容</b></button>
			</li>
			<li class="nav-item" role="presentation">
				<button class="nav-link" id="tb02-tab" data-bs-toggle="tab" data-bs-target="#tb02" type="button" role="tab" aria-controls="tb02" aria-selected="false"><b>System prompt template</b></button>
			</li>
		</ul>	
		<div class="tab-content tab-content-custom" id="resultTabContent">
			<div class="tab-pane fade show active" id="tb01" role="tabpanel" aria-labelledby="tb01">
				<div class="row">
					<div class="col-xs-12 col-md-12 col-lg-12">
						<div class="row">
							<div class="col-xs-6 col-md-6 col-lg-6">
								<label for="docId" class="form-label">編號</label>
								<input type="text" v-bind:class="'form-control ' + ( fieldCheckInvalid('docId', checkFields) ? 'is-invalid' : ' ')" id="docId" placeholder="輸入編號" v-model="this.formParam.docId">
								<div v-if="fieldCheckInvalid('docId', checkFields)" class="invalid-feedback d-block">{{ fieldInvalidFeedback('docId', checkFields) }}</div>
							</div>
							<div class="col-xs-6 col-md-6 col-lg-6">
								<label for="name" class="form-label">名稱</label>
								<input type="text" v-bind:class="'form-control ' + ( fieldCheckInvalid('name', checkFields) ? 'is-invalid' : ' ')" id="name" placeholder="輸入名稱" v-model="this.formParam.name">
								<div v-if="fieldCheckInvalid('name', checkFields)" class="invalid-feedback d-block">{{ fieldInvalidFeedback('name', checkFields) }}</div>
							</div>
						</div>
						<div class="row">
							<div class="col-xs-12 col-md-12 col-lg-12">
								<label for="userMessage" class="form-label">文件內容</label>
								<Codemirror
									v-model="this.formParam.content"
									:options="cmOptions"
									:extensions="cmExtensions"
									:style="{ height: '300px' }"
									ref="cmRef"
									@change="cmOnChange1"
									@input="cmOnInput1"
									@ready="cmOnReady1"
									id="content">
								</Codemirror>		
								<div v-if="fieldCheckInvalid('content', checkFields)" class="invalid-feedback d-block">{{ fieldInvalidFeedback('content', checkFields) }}</div>
							</div>
						</div>
					</div>
				</div>	
			</div>
			<div class="tab-pane fade" id="tb02" role="tabpanel" aria-labelledby="tb02">	
				<div class="row">
					<div class="col-xs-12 col-md-12 col-lg-12">
						<div class="row">
							<div class="col-xs-12 col-md-12 col-lg-12">
								<label for="userMessage" class="form-label">Content</label>
								<Codemirror
									v-model="this.formParam.sysPromptTpl"
									:options="cmOptions"
									:extensions="cmExtensions"
									:style="{ height: '300px' }"
									ref="cmRef"
									@change="cmOnChange2"
									@input="cmOnInput2"
									@ready="cmOnReady2"
									id="sysPromptTpl">
								</Codemirror>		
								<div v-if="fieldCheckInvalid('sysPromptTpl', checkFields)" class="invalid-feedback d-block">{{ fieldInvalidFeedback('sysPromptTpl', checkFields) }}</div>
							</div>
						</div>
						<div class="row">
							<div class="col-xs-12 col-md-12 col-lg-12">
								<label for="tplVariable" class="form-label">Variable name</label>
								<input type="text" v-bind:class="'form-control ' + ( fieldCheckInvalid('tplVariable', checkFields) ? 'is-invalid' : ' ')" id="tplVariable" placeholder="輸入template variable" v-model="this.formParam.tplVariable">
								<div v-if="fieldCheckInvalid('tplVariable', checkFields)" class="invalid-feedback d-block">{{ fieldInvalidFeedback('tplVariable', checkFields) }}</div>		
							</div>
						</div>	
					</div>
				</div>							
			</div>		
		</div>	
	</div>

	<p style="margin-bottom: 10px"></p>
	<div class="row">
		<div class="col-xs-12 col-md-12 col-lg-12">
			<button type="button" class="btn btn-primary" v-on:click="btnSave"><i class="'bi bi-save"></i>&nbsp;儲存</button>
			&nbsp;
			<button type="button" class="btn btn-primary" v-on:click="btnClear"><i class="'bi bi-eraser"></i>&nbsp;清除</button>		
		</div>
	</div>

</div>

<br>
<br>

</template>
