<script>
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

import '../../../node_modules/bytemd/dist/index.css';
import gfm from '@bytemd/plugin-gfm';
import { Editor, Viewer } from '@bytemd/vue-next';
import importHtml from '@bytemd/plugin-import-html';

let checkFields = new Object();

export default {
	components: { Toolbar, Editor, Codemirror },
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
				taskId : '',
				cmdId : '',
				itemSeq : -1,
				processMsT1 : '',
				processMsT2 : '',
				contentString : '',
				invokeContentString : '',
				lastCmd : '',
				processId : '',
				taskUserMessageString : '',
				causeMessage : ''
			},
			plugins : null
		}
	},
	methods: { 
		fieldInvalidFeedback : invalidFeedback,
		fieldCheckInvalid : checkInvalid,
		btnBack : function() {
			this.$router.back();
		},
		btnClear : function() {
			this.checkFields = new Object();
		},
		loadData : _loadData,
		handleChange1 : function(v) {
			this.formParam.contentString = v;
		},

		cmOnChange0 : function(val, cm) {
			this.formParam.invokeContentString = val;
		},
		cmOnInput0 : function(val) {
			this.formParam.invokeContentString = val;
		},
		cmOnReady0 : function(cm) {

		},
		cmOnChange1 : function(val, cm) {
			this.formParam.taskUserMessageString = val;
		},
		cmOnInput1 : function(val) {
			this.formParam.taskUserMessageString = val;
		},
		cmOnReady1 : function(cm) {

		},
		cmOnChange2 : function(val, cm) {
			this.formParam.causeMessage = val;
		},
		cmOnInput2 : function(val) {
			this.formParam.causeMessage = val;
		},
		cmOnReady2 : function(cm) {

		}					
	},
	created() { 
		this.plugins = [ importHtml() ];
	},
	mounted() { 
		this.loadData(); 
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

</script>

<style>
hr.solid {
    border-top: 2px solid #999;
}

.bytemd {
	height: calc(100vh - 230px);
}

.tab-content-custom {
	height: 93vh;
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
			description="任務紀錄." 
			marginBottom="Y"
			refreshFlag="Y"
			@refreshMethod="loadData"
			backFlag="Y"
			@backMethod="btnBack"
			createFlag="N"
			@createMethod="null"
			saveFlag="N"
			@saveMethod="null"
		></Toolbar>		
	</div>
</div>

<div class="row">
	<div class="col-xs-12 col-md-12 col-lg-12">
		
		<ul class="nav nav-tabs" id="myTab" role="tablist">
			<li class="nav-item" role="presentation">
				<button class="nav-link active" id="tb01-tab" data-bs-toggle="tab" data-bs-target="#tb01" type="button" role="tab" aria-controls="tb01" aria-selected="true"><b>llm回應訊息(原始內容)</b></button>
			</li>
			<li class="nav-item" role="presentation" v-if=" null != this.formParam.invokeContentString && this.formParam.invokeContentString.length > 0 ">
				<button class="nav-link" id="tb02-tab" data-bs-toggle="tab" data-bs-target="#tb02" type="button" role="tab" aria-controls="tb02" aria-selected="false"><b>invoke結果/Server端觸發(內容)</b></button>
			</li>
			<li class="nav-item" role="presentation" v-if=" null != this.formParam.taskUserMessageString && this.formParam.taskUserMessageString.length > 0 ">
				<button class="nav-link" id="tb03-tab" data-bs-toggle="tab" data-bs-target="#tb03" type="button" role="tab" aria-controls="tb03" aria-selected="false"><b>送出llm訊息(userMessage)</b></button>
			</li>
			<li class="nav-item" role="presentation" v-if=" null != this.formParam.causeMessage && this.formParam.causeMessage.length > 0 ">
				<button class="nav-link" id="tb04-tab" data-bs-toggle="tab" data-bs-target="#tb04" type="button" role="tab" aria-controls="tb04" aria-selected="false"><b>invoke script錯誤訊息</b></button>
			</li>
		</ul>	

		<div class="tab-content tab-content-custom" id="resultTabContent">
			<div class="tab-pane fade show active" id="tb01" role="tabpanel" aria-labelledby="tb01">
				<div class="row">
					<div class="col-xs-12 col-md-12 col-lg-12">
						<h5>&nbsp;&nbsp;&nbsp;<span class="badge text-bg-success">llm回應訊息(原始內容)</span></h5>
						<Editor :value="this.formParam.contentString" :plugins="plugins" @change="handleChange1" style="height: 100%; width: 97%;" />
					</div>				
				</div>
			</div>
			<div class="tab-pane fade" id="tb02" role="tabpanel" aria-labelledby="tb02" v-if=" null != this.formParam.invokeContentString && this.formParam.invokeContentString.length > 0 ">			
				<div class="row" v-if=" null != this.formParam.invokeContentString && this.formParam.invokeContentString.length > 0 ">
					<div class="col-xs-12 col-md-12 col-lg-12">
						<h5>&nbsp;&nbsp;&nbsp;<span class="badge text-bg-secondary">invoke結果/Server端觸發(內容)</span></h5>
						<Codemirror
							v-model="this.formParam.invokeContentString"
							:options="cmOptions"
							:extensions="cmExtensions"
							:style="{ height: '500px', width: '97%' }"
							ref="cmRef"
							@change="cmOnChange0"
							@input="cmOnInput0"
							@ready="cmOnReady0"
							id="invokeContentString">
						</Codemirror>
					</div>				
				</div>
			</div>
			<div class="tab-pane fade" id="tb03" role="tabpanel" aria-labelledby="tb03">
				<div class="row" v-if=" null != this.formParam.taskUserMessageString && this.formParam.taskUserMessageString.length > 0 ">
					<div class="col-xs-12 col-md-12 col-lg-12">
						<h5>&nbsp;&nbsp;&nbsp;<span class="badge text-bg-info">送出llm訊息(userMessage)</span></h5>
						<Codemirror
							v-model="this.formParam.taskUserMessageString"
							:options="cmOptions"
							:extensions="cmExtensions"
							:style="{ height: '500px', width: '97%' }"
							ref="cmRef"
							@change="cmOnChange1"
							@input="cmOnInput1"
							@ready="cmOnReady1"
							id="taskUserMessageString">
						</Codemirror>					
					</div>
				</div>
			</div>
			<div class="tab-pane fade" id="tb04" role="tabpanel" aria-labelledby="tb04">
				<div class="row" v-if=" null != this.formParam.causeMessage && this.formParam.causeMessage.length > 0 ">
					<div class="col-xs-12 col-md-12 col-lg-12">
						<h5>&nbsp;&nbsp;&nbsp;<span class="badge text-bg-warning">錯誤訊息</span></h5>
						<Codemirror
							v-model="this.formParam.causeMessage"
							:options="cmOptions"
							:extensions="cmExtensions"
							:style="{ height: '500px', width: '97%' }"
							ref="cmRef"
							@change="cmOnChange2"
							@input="cmOnInput2"
							@ready="cmOnReady2"
							id="causeMessage">
						</Codemirror>					
					</div>
				</div>	
			</div>
		</div>


	</div>


	<br>
	<br>

</div>			

</template>