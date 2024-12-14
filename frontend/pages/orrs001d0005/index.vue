<script>
import { watch, ref } from "vue";
import Swal from 'sweetalert2';
import { toast } from 'vue3-toastify';
import 'vue3-toastify/dist/index.css';

import { Codemirror } from 'vue-codemirror'
import { markdown, markdownLanguage } from '@codemirror/lang-markdown'
import { languages } from '@codemirror/language-data'
import { oneDark } from '@codemirror/theme-one-dark'

import Toolbar from '@/components/Toolbar.vue';
import { PageConstants } from './config';
import { useOrrs001d0005Store } from './QueryPageStore'; 
import { 
	getAxiosInstance, 
	getProgItem, 
	getUrlPrefixFromProgItem,
	getAccessTokenCookie
} from '../../components/BaseHelper';

import { fetchEventSource } from '@microsoft/fetch-event-source';

export default {
	components: { Toolbar, Codemirror },
	setup() { 
		definePageMeta({ middleware : ['auth'] });
		const queryPageStore = useOrrs001d0005Store();

		const cmRef = ref();
		const cmOptions = {
			mode: "text/x-markdown",
		};

		const cmExtensions = [
			markdown({ base: markdownLanguage, codeLanguages: languages })
			/*, oneDark*/ 
		];

		return {
			queryPageStore,
			cmRef,
			cmOptions,
			cmExtensions			
		}
	},
	data() {
		return {
			pageProgramId : PageConstants.QueryId,
			reqList : [],
			llmModelList : [],
			queryBtnDisable : false,
			docSw : false
		}
	},
	methods: { 
		tbRefresh : function() {
			this.btnClear();
		},
		btnQuery : function() {
			if ( '' == this.queryPageStore.queryParam.message.trim() ) {
				toast.info('請輸入訊息');
				return;
			}
			this.send();
		},
		btnClear : function() {
			this.queryPageStore.queryParam.model = 'gemma2';
			this.queryPageStore.queryParam.message = '';
			this.queryPageStore.queryParam.system = '';
			this.queryPageStore.queryParam.docmode = 'N';
			this.reqList = [];
			this.queryBtnDisable = false;
			this.docSw = false;
		},
		send : function() {
			this.queryBtnDisable = true;
			var that = this;
			this.reqList.push({
				"question"	: this.queryPageStore.queryParam.message, 
				"ans" 		: '', 
				"model"		: this.queryPageStore.queryParam.model
			});
			let currPos = this.reqList.length - 1;
			fetchEventSource(import.meta.env.VITE_API_URL + PageConstants.eventNamespace + '/chat',{
				method : "POST",
				headers : {
					"Content-Type"	: "application/json",
					"Authorization"	: "Bearer " + getAccessTokenCookie()
				},
				body : JSON.stringify(this.queryPageStore.queryParam),
				openWhenHidden : true,
				onmessage(msg) {
					var data = JSON.parse(msg.data);
					//console.log(data.message.content);
					that.reqList[currPos].ans = that.reqList[currPos].ans.concat(data.message.content);
					if (data.done) {
						that.queryBtnDisable = false;
						that.queryPageStore.queryParam.message = '';
						that.queryPageStore.queryParam.system = '';
						toast.info('done...');
					}
				},
				onerror(err) {
					that.queryBtnDisable = false;
					toast.error(err);
					that.reqList[currPos].ans = '...';
				}
			});
		},
		loadLlmModel : function() {
			Swal.fire({title: "Loading...", html: "請等待", showConfirmButton: false, allowOutsideClick: false});
			Swal.showLoading();      
			let axiosInstance = getAxiosInstance();
			axiosInstance.post(import.meta.env.VITE_API_URL + PageConstants.eventNamespace_ORRS001D0001 + '/loadLlmModelList')
			.then(response => {
				Swal.hideLoading();
				Swal.close();
				if (null != response.data) {
					this.llmModelList = response.data.value;
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

		cmOnChange0 : function(val, cm) {
			
		},
		cmOnInput0 : function(val) {
			
		},
		cmOnReady0 : function(cm) {

		}

	},
	created() {
		watch(() => this.docSw, (newVal, oldVal) => {
			if (newVal) {
				this.queryPageStore.queryParam.docmode = 'Y';
			} else {
				this.queryPageStore.queryParam.docmode = 'N';
			}
		});		
	},
	mounted() { 
		this.loadLlmModel();
	}
}

</script>

<template>

<div class="row">
  <div class="col-xs-12 col-md-12 col-lg-12">
    <Toolbar 
        :progId="this.pageProgramId" 
        description="Chat test." 
        marginBottom="N"
        refreshFlag="Y"
        @refreshMethod="tbRefresh"
        backFlag="N"
        @backMethod="null"
        createFlag="N"
        @createMethod="null"
        saveFlag="N"
        @saveMethod="null"
		queryFieldShowSwitchFlag="N"
		@queryFieldShowSwitcMethod="null"
    ></Toolbar>
  </div>
</div>

<div class="row d-flex justify-content-center">
    <div class="col-md-12 col-lg-12 col-xl-12">
        <div class="card-body" data-mdb-perfect-scrollbar-init style="position: relative; height: 100%;">
            <div class="col-md-12 col-lg-12 col-xl-12" v-for=" r in this.reqList ">
                <div class="d-flex justify-content-between">
                    <p class="small mb-1">Question</p>
                </div>
                <div class="d-flex flex-row justify-content-start">
                    <img src="/img/Q.png" alt="avatar 1" style="width: 45px; height: 100%;" />
                    <div>
                        <p class="small p-2 ms-3 mb-3 rounded-3 bg-body-tertiary">{{ r.question }}</p>
                    </div>
                </div>
                <div class="d-flex justify-content-between">
                    <p class="small mb-1 text-muted">&nbsp;</p>
                    <p class="small mb-1">{{ r.model }}</p>
                </div>
                <div class="d-flex flex-row justify-content-end mb-4 pt-1">
                    <div>
                        <Codemirror
                            v-model="r.ans"
                            :options="cmOptions"
                            :extensions="cmExtensions"
                            :style="{ height: '100%', width: '960px', minWidth: '250px', maxWidth: '1440px' }"
                            ref="cmRef"
                            @change="cmOnChange0"
                            @input="cmOnInput0"
                            @ready="cmOnReady0"
                        >
                        </Codemirror>
                    </div>
                    <img src="/img/A.png" alt="avatar 1" style="width: 45px; height: 100%;" />
                </div>
            </div>
        </div>

		<br>

		<div class="row">
			<div class="col-md-6 col-lg-6 col-xl-6">
				<select class="form-select" aria-label="請選取LLM模組" v-model="this.queryPageStore.queryParam.model">
					<option v-bind:value="llmItem" v-for="(llmItem, idx) in this.llmModelList">{{ llmItem }}</option>
				</select>
			</div>
			<div class="col-md-6 col-lg-6 col-xl-6">
				<div class="form-group form-floating">
					<div class="form-check form-switch">
						<input class="form-check-input" type="checkbox" role="switch" id="docSw" v-model="this.docSw">
						<label class="form-check-label" for="docSw">Search documents for assistant</label>
					</div>
    			</div>		
			</div>
		</div>				
		<p style="margin-bottom: 5px"></p>
		<div class="row">
			<div class="col-md-12 col-lg-12 col-xl-12">
				<input type="text" class="form-control" id="system" placeholder="輸入Prompt" v-model="this.queryPageStore.queryParam.system">
			</div>
		</div>			
		<p style="margin-bottom: 5px"></p>
        <div class="card-footer text-muted d-flex justify-content-start align-items-center p-3">			
			<div class="input-group mb-0">
                <textarea class="form-control" rows="3" cols="24" placeholder="Type message" aria-label="message" aria-describedby="button-addon2" v-model="this.queryPageStore.queryParam.message"></textarea>
                <button data-mdb-button-init data-mdb-ripple-init class="btn btn-warning" type="button" id="button-addon2" style="padding-top: 0.55rem;" @click="btnQuery" v-bind:disabled="this.queryBtnDisable">
                    送出
                </button>
            </div>
        </div>
    </div>
</div>

</template>

