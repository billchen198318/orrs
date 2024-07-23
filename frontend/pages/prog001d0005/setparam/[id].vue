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
	getUrlPrefixFromProgItem 		
} from '../../../components/BaseHelper';

let checkFields = new Object();

export default {
	components: { Toolbar },
	setup() { 
		definePageMeta({ middleware : ['auth'] });
	},
	data() {
		return {
			pageProgramId : PageConstants.SetParamId,
			checkFields,
			masterParam : {
				oid : this.$route.params.id,
				reportId : '',
			},
			formParam : {
				reportId : '',
				urlParam : '',
				rptParam : ''
			},
			paramList : []
		}
	},
	methods: { 
		fieldInvalidFeedback : invalidFeedback,
		fieldCheckInvalid : checkInvalid,
		btnBack : function() {
			this.$router.back();
		},
		queryParamList : _queryParamList,
		btnSave : _btnSave,
		btnClear : function() {
			this.checkFields = new Object();
			this.formParam.urlParam = '';
			this.formParam.rptParam = '';
		},
		loadData : _loadData,
		tbRefresh : function() {
			this.loadData();
			this.btnClear();
		},
		delParam : _delParam,
		delParamConfirm : function(oid) {
			Swal.fire({
				title: '刪除?',
				icon: 'question',
				iconHtml: '?',
				confirmButtonText: 'Yes',
				cancelButtonText: 'No',
				showCancelButton: true,
				showCloseButton: true
			}).then((result) => {
				if (result.isConfirmed) {
					this.delParam(oid);
				}
			});  
		}
	},
	created() { 
        watch(() => this.formParam.isTitleVar, (newVal, oldVal) => {
			if (this.formParam.isTitleVar) {
				this.formParam.isTitle = 'Y';
			} else {
				this.formParam.isTitle = 'N';
			}
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
    axiosInstance.post(import.meta.env.VITE_API_URL + PageConstants.eventNamespace + '/load', {'oid' : this.masterParam.oid})
    .then(response => {
        Swal.hideLoading();
        Swal.close();
        if (null != response.data) {
            if (import.meta.env.VITE_SUCCESS_FLAG != response.data.success) {
                toast.warning(response.data.message);
                this.$router.push( getUrlPrefixFromProgItem( getProgItem(PageConstants.QueryId) ) );
                return;
            }
			this.masterParam = response.data.value;
			this.masterParam.oid = this.$route.params.id;
			this.formParam.reportId = this.masterParam.reportId;
			this.queryParamList();
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

function _queryParamList() {
    this.paramList = [];
	var axiosInstance = getAxiosInstance();
	axiosInstance.post(import.meta.env.VITE_API_URL + PageConstants.eventNamespace + '/findSetParamPage', {
		"field": {
			"reportId"	: this.masterParam.reportId
		}
		,
		"pageOf": {
			"select"  : 1,
			"showRow" : 100
		}
	})
	.then(response => {
		if (null != response.data) {
			if (import.meta.env.VITE_SUCCESS_FLAG != response.data.success) {
				return;
			}
			this.paramList = response.data.value;   
		} else {
			toast.error('error, null');
		}
	})
	.catch(e => {
		alert(e);
	});
}

function _btnSave() {
    this.checkFields = new Object();
    Swal.fire({title: "Loading...", html: "請等待", showConfirmButton: false, allowOutsideClick: false});
    Swal.showLoading();      
    let axiosInstance = getAxiosInstance();
    axiosInstance.post(import.meta.env.VITE_API_URL + PageConstants.eventNamespace + '/saveSetParam', this.formParam)
    .then(response => {
        Swal.hideLoading();
        Swal.close();
        if (null != response.data) {
            this.checkFields = response.data.checkFields;
            if (import.meta.env.VITE_SUCCESS_FLAG != response.data.success) {
                toast.warning( escapeQifuHtmlMsg(response.data.message) );
            } else {
                toast.success(response.data.message);
                this.btnClear();
            }            
        } else {
            toast.error('error, null');
        }
        this.queryParamList();
    })
    .catch(e => {
        Swal.hideLoading();
        Swal.close();        
        alert(e);
    });
}

function _delParam(oid) {
	Swal.fire({title: "Loading...", html: "請等待", showConfirmButton: false, allowOutsideClick: false});
	Swal.showLoading();  
	var axiosInstance = getAxiosInstance();  
	axiosInstance.post(import.meta.env.VITE_API_URL + PageConstants.eventNamespace + '/deleteSetParam', {"oid": oid})
	.then(response => {
		Swal.hideLoading();
		Swal.close();
		if (null != response.data) {
			if (import.meta.env.VITE_SUCCESS_FLAG == response.data.success) {
				toast.success(response.data.message);
			} else {        
				toast.warning(response.data.message);
			}      
			this.queryParamList();
		} else {
			toast.error('error, null');
			this.queryParamList();
		}
	})
	.catch(e => {
		Swal.hideLoading();
		Swal.close();    
		this.queryParamList();
		alert(e);
	}); 
}

</script>

<template>
<div class="row">
	<div class="col-xs-12 col-md-12 col-lg-12">
		<Toolbar 
			:progId="this.pageProgramId" 
        	description="Jasperreport resources管理，參數配置作業." 
        	marginBottom="Y"
        	refreshFlag="Y"
        	@refreshMethod="tbRefresh"
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
		Jasperreport報表編號:&nbsp;{{this.masterParam.templateId}}
	</div>   
</div>
<div class="row">
	<div class="col-xs-6 col-md-6 col-lg-6">
		<label for="urlParam" class="form-label">Url參數</label>
		<input type="text" v-bind:class="'form-control ' + ( fieldCheckInvalid('urlParam', checkFields) ? 'is-invalid' : ' ')" id="urlParam" placeholder="輸入Url參數" v-model="this.formParam.urlParam">
		<div v-if="fieldCheckInvalid('urlParam', checkFields)" class="invalid-feedback d-block">{{ fieldInvalidFeedback('urlParam', checkFields) }}</div>
	</div>
	<div class="col-xs-6 col-md-6 col-lg-6">
		<label for="rptParam" class="form-label">Jasperreport參數</label>
		<input type="text" v-bind:class="'form-control ' + ( fieldCheckInvalid('rptParam', checkFields) ? 'is-invalid' : ' ')" id="rptParam" placeholder="輸入物件變數" v-model="this.formParam.rptParam">
		<div v-if="fieldCheckInvalid('rptParam', checkFields)" class="invalid-feedback d-block">{{ fieldInvalidFeedback('rptParam', checkFields) }}</div>
	</div>
</div>
<p style="margin-bottom: 5px"></p>
<div class="row">
	<div class="col-xs-12 col-md-12 col-lg-12">
    	<button type="button" class="btn btn-primary" v-on:click="btnSave"><i class="'bi bi-save"></i>&nbsp;儲存</button>
    	&nbsp;
    	<button type="button" class="btn btn-primary" v-on:click="btnClear"><i class="'bi bi-eraser"></i>&nbsp;清除</button>		
	</div>
</div>
<div class="row">
	&nbsp;
</div>
<div class="row">
	<div class="col-xs-12 col-md-12 col-lg-12">
		<table class="table table-hover table-bordered">
            <thead>
                <tr>
                    <th style="background-color: #575757; color: whitesmoke;"><i class="bi bi-hand-index-thumb"></i></th>
                    <th style="background-color: #575757; color: whitesmoke;">Url參數</th>
                    <th style="background-color: #575757; color: whitesmoke;">Jasperreport參數</th>
                </tr>
            </thead>
			<tbody>
				<tr v-for=" item in this.paramList ">
                    <td>
                        <button class="btn btn-warning btn-sm" @click="delParamConfirm(item.oid)"><i class="bi bi-trash"></i></button>
                    </td>
                    <td>{{item.urlParam}}</td>
                    <td>{{item.rptParam}}</td>
				</tr>	
			</tbody>
		</table>		
	</div>
</div>
</template>
