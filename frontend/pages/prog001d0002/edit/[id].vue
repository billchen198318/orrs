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
			pageProgramId : PageConstants.EditId,
			checkFields,
			formParam : {
				oid : this.$route.params.id,
				progId : '',
				name : '',
				url : '',
				editModeVar : false,
				isDialogVar : false,
				editMode : 'N',
				isDialog : 'N',    
				dialogW : '0',
				dialogH : '0',
				progSystem : 'CORE',
				itemType : 'ITEM',
				fontIconClassId : 'globe2',
				icon : 'SYSTEM'
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
			//this.formParam.progId = '';
			this.formParam.name = '';
			this.formParam.url = '';
			this.formParam.editModeVar = false;
			this.formParam.isDialogVar = false;
			this.formParam.editMode = 'N';
			this.formParam.isDialog = 'N';
			this.formParam.dialogW = '0';
			this.formParam.dialogH = '0';
			this.formParam.progSystem = 'CORE';
			this.formParam.itemType = 'ITEM';
			this.formParam.fontIconClassId = 'globe2';
			this.formParam.icon = 'SYSTEM';
		},
		loadData : _loadData,
		refreshSwitchItemVariableFn : function() {
			if (this.formParam.editModeVar) {
				this.formParam.editMode = 'Y';
			} else {
				this.formParam.editMode = 'N';
			}
			if (this.formParam.isDialogVar) {
				this.formParam.isDialog = 'Y';
			} else {
				this.formParam.isDialog = 'N';
			}
		}		
	},
	created() { 
        watch(() => this.formParam.editModeVar, (newVal, oldVal) => {
            this.refreshSwitchItemVariableFn();
        });
        watch(() => this.formParam.isDialogVar, (newVal, oldVal) => {
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
            this.formParam.editModeVar = false;
            this.formParam.isDialogVar = false;
            if ('Y' == this.formParam.editMode) {
                this.formParam.editModeVar = true;
            }
            if ('Y' == this.formParam.isDialog) {
                this.formParam.isDialogVar = true;
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
        	description="站台測試用，修改資料作業." 
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
		<label for="progId" class="form-label">程式編號</label>
		<input type="text" v-bind:class="'form-control ' + ( fieldCheckInvalid('progId', checkFields) ? 'is-invalid' : ' ')" id="progId" placeholder="輸入程式編號" v-model="this.formParam.progId" readonly="true">
		<div v-if="fieldCheckInvalid('progId', checkFields)" class="invalid-feedback d-block">{{ fieldInvalidFeedback('progId', checkFields) }}</div>
	</div>
	<div class="col-xs-6 col-md-6 col-lg-6">
		<label for="name" class="form-label">程式名稱</label>
		<input type="text" v-bind:class="'form-control ' + ( fieldCheckInvalid('name', checkFields) ? 'is-invalid' : ' ')" id="name" placeholder="輸入程式名稱" v-model="this.formParam.name">
		<div v-if="fieldCheckInvalid('name', checkFields)" class="invalid-feedback d-block">{{ fieldInvalidFeedback('name', checkFields) }}</div>
	</div>
</div>
<div class="row">
	<div class="col-xs-12 col-md-12 col-lg-12">
		<label for="url" class="form-label">Url</label>
		<input type="text" v-bind:class="'form-control ' + ( fieldCheckInvalid('url', checkFields) ? 'is-invalid' : ' ')" id="url" placeholder="輸入Url" v-model="this.formParam.url">
		<div v-if="fieldCheckInvalid('url', checkFields)" class="invalid-feedback d-block">{{ fieldInvalidFeedback('url', checkFields) }}</div>
	</div>
</div>
<div class="row">
	<div class="col-xs-6 col-md-6 col-lg-6">
		<div class="form-check form-switch">
			<input class="form-check-input" type="checkbox" role="switch" id="editMode" v-model="this.formParam.editModeVar">
			<label class="form-check-label" for="editMode">編輯頁</label>
		</div>
	</div>
	<div class="col-xs-6 col-md-6 col-lg-6">
		<div class="form-check form-switch">
			<input class="form-check-input" type="checkbox" role="switch" id="isDialog" v-model="this.formParam.isDialogVar">
			<label class="form-check-label" for="isDialog">Dialog模式</label>
		</div>
	</div>
</div>
<div class="row">
	<div class="col-xs-6 col-md-6 col-lg-6">
		<label for="dialogW" class="form-label">Dialog寬</label>
		<input type="number" v-bind:class="'form-control ' + ( fieldCheckInvalid('dialogW', checkFields) ? 'is-invalid' : ' ')" id="dialogW" v-model="this.formParam.dialogW">
		<div v-if="fieldCheckInvalid('dialogW', checkFields)" class="invalid-feedback d-block">{{ fieldInvalidFeedback('dialogW', checkFields) }}</div>
	</div>
	<div class="col-xs-6 col-md-6 col-lg-6">
		<label for="dialogH" class="form-label">Dialog高</label>
		<input type="number" v-bind:class="'form-control ' + ( fieldCheckInvalid('dialogH', checkFields) ? 'is-invalid' : ' ')" id="dialogH" v-model="this.formParam.dialogH">
		<div v-if="fieldCheckInvalid('dialogH', checkFields)" class="invalid-feedback d-block">{{ fieldInvalidFeedback('dialogH', checkFields) }}</div>
	</div>
</div>
<div class="row">
	<div class="col-xs-6 col-md-6 col-lg-6">
		<label for="dialogH" class="form-label">類別</label>
		<select class="form-select" aria-label="請選取類別" v-model="this.formParam.itemType">
			<option value="ITEM">ITEM-頁面</option>
			<option value="FOLDER">FOLDER-目錄</option>
		</select>
	</div>
	<div class="col-xs-6 col-md-6 col-lg-6">
		<label for="fontIconClassId" class="form-label">Icon class</label>
		<input type="text" v-bind:class="'form-control ' + ( fieldCheckInvalid('fontIconClassId', checkFields) ? 'is-invalid' : ' ')" id="fontIconClassId" placeholder="Icon class" v-model="this.formParam.fontIconClassId">
		<div v-if="fieldCheckInvalid('fontIconClassId', checkFields)" class="invalid-feedback d-block">{{ fieldInvalidFeedback('fontIconClassId', checkFields) }}</div>
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
