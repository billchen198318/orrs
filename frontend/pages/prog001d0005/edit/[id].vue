<script>
import { ref, watch } from 'vue';
import Swal from 'sweetalert2';
import { toast } from 'vue3-toastify';
import 'vue3-toastify/dist/index.css';

import Toolbar from '@/components/Toolbar.vue';
import { PageConstants } from '../config';
import { 
	getAxiosInstance, 
	invalidFeedback,
	checkInvalid,
	escapeQifuHtmlMsg,
	getProgItem, 
	getUrlPrefixFromProgItem,	
	getFile2Base64
} from '../../../components/BaseHelper';

let checkFields = new Object();

export default {
	components: { Toolbar },
	setup() { 
		definePageMeta({ middleware : ['auth'] });
	},
	data() {
		return {
			pageProgramId : PageConstants.EditId,
			checkFields,
			formParam : {
				oid : this.$route.params.id,
				reportId : '',
				description : '',
				isCompile : 'N',
				compile : false,
				uploadBase64 : '',
				file : ''
			}
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
			//this.formParam.reportId = '';
			this.formParam.description = '';
			this.formParam.isCompile = 'N';
			this.formParam.compile = false;
			this.formParam.uploadBase64 = '';
			this.formParam.file = '';
			document.getElementById('file').value = null;
		},
		loadData : _loadData,
		handleFiles : function(event) {
			this.formParam.uploadBase64 = '';
			this.formParam.file = '';
			var files = event.target.files;
			if (files == null || files.length < 1) {
				toast.warning('請選取檔案');
				event.target.value = null;
			}
			if (files != null && files.length > 0) {
				if (!files[0].name.toLowerCase().endsWith('.zip')) {
					toast.warning('只允許上傳zip檔案');
					event.target.value = null;
				}
				if (files[0].size > 1 && files[0].size <= import.meta.env.VITE_UPLOAD_MAX_SIZE ) {
					getFile2Base64(files[0]).then(c => { 
						this.formParam.uploadBase64 = c;
						this.formParam.file = files[0].name;
					}).catch(e => { 
						this.formParam.uploadBase64 = ''; 
						toast.error('請重新選取檔案,發生錯誤');
						event.target.value = null;
					});            
				} else {
					toast.warning('選取之檔案,容量不允許上傳');
					event.target.value = null;
				}
			}
		},
		refreshSwitchItemVariableFn : function() {
			if (this.formParam.compile) {
				this.formParam.isCompile = 'Y';
			} else {
				this.formParam.isCompile = 'N';
			}
		}
	},
	created() { 
		this.refreshSwitchItemVariableFn();
        watch(() => this.formParam.compile, (newVal, oldVal) => {
            this.refreshSwitchItemVariableFn();
        });
	},
	mounted() { 
		this.loadData();
	}
}

function _loadData() {
    Swal.fire({title: "Loading...", html: "請等待", showConfirmButton: false, allowOutsideClick: false});
    Swal.showLoading(); 
    let axiosInstance = getAxiosInstance();
    axiosInstance.post(import.meta.env.VITE_API_URL + PageConstants.eventNamespace + '/load', {'oid' : this.$route.params.id})
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
			this.formParam.oid = this.$route.params.id;
            this.formParam.uploadBase64 = '';
            this.formParam.compile = false;
            if ('Y' == this.formParam.isCompile) {
                this.formParam.compile = true;
            }
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
        	description="Jasperreport resources管理，修改資料作業." 
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
		<label for="reportId" class="form-label">報表編號</label>
		<input type="text" v-bind:class="'form-control ' + ( fieldCheckInvalid('reportId', checkFields) ? 'is-invalid' : ' ')" id="reportId" placeholder="輸入報表編號" v-model="this.formParam.reportId" readonly="true">
		<div v-if="fieldCheckInvalid('reportId', checkFields)" class="invalid-feedback d-block">{{ fieldInvalidFeedback('reportId', checkFields) }}</div>
	</div>
	<div class="col-xs-6 col-md-6 col-lg-6">
		<label for="file" class="form-label">jasperreport資源zip壓縮檔</label>
		<input type="file" v-bind:class="'custom-file-input form-control ' + ( fieldCheckInvalid('file', checkFields) ? 'is-invalid' : ' ')" id="file" placeholder="輸入名稱" @change="handleFiles" multiple>
		<div v-if="fieldCheckInvalid('file', checkFields)" class="invalid-feedback d-block">{{ fieldInvalidFeedback('file', checkFields) }}</div>
	</div>
</div>
<div class="row">
	<div class="col-xs-12 col-md-12 col-lg-12">
		<div class="form-check form-switch">
			<input class="form-check-input" type="checkbox" role="switch" id="compile" v-model="this.formParam.compile">
			<label class="form-check-label" for="compile">編譯jrxml</label>
		</div>		
	</div>
</div>
<div class="row">
	<div class="col-xs-12 col-md-12 col-lg-12">
		<label for="description" class="form-label">說明</label>
		<textarea class="form-control" id="description" row="3" col="25" v-model="this.formParam.description"></textarea>			
	</div>
</div>
<p style="margin-bottom: 5px"></p>
<div class="row">
	<div class="col-xs-12 col-md-12 col-lg-12">
    	<button type="button" class="btn btn-primary" v-on:click="btnUpdate"><i class="'bi bi-save"></i>&nbsp;儲存</button>
    	&nbsp;
    	<button type="button" class="btn btn-primary" v-on:click="btnClear"><i class="'bi bi-eraser"></i>&nbsp;清除</button>		
	</div>
</div>
</template>
