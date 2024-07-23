<script>
import Swal from 'sweetalert2';
import { toast } from 'vue3-toastify';
import 'vue3-toastify/dist/index.css';

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
	components: { Toolbar },
	setup() { 
		definePageMeta({ middleware : ['auth'] });
	},
	data() {
		return {
			pageProgramId : PageConstants.CreateId,
			checkFields,
			formParam : {
				sysId : '',
				name : '',
				host : '',
				contextPath : '',
				isLocal : 'N',
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
		btnSave : _btnSave,
		btnClear : function() {
			this.checkFields = new Object();
			this.formParam.sysId = '';
			this.formParam.name = '';
			this.formParam.host = '';
			this.formParam.contextPath = '';
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

<template>
<div class="row">
	<div class="col-xs-12 col-md-12 col-lg-12">
		<Toolbar 
			:progId="this.pageProgramId" 
        	description="站台測試用，新增資料作業." 
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
	<div class="col-xs-6 col-md-6 col-lg-6">
		<label for="sysId" class="form-label">編號</label>
		<input type="text" v-bind:class="'form-control ' + ( fieldCheckInvalid('sysId', checkFields) ? 'is-invalid' : ' ')" id="sysId" placeholder="輸入編號" v-model="this.formParam.sysId">
		<div v-if="fieldCheckInvalid('sysId', checkFields)" class="invalid-feedback d-block">{{ fieldInvalidFeedback('sysId', checkFields) }}</div>
	</div>
	<div class="col-xs-6 col-md-6 col-lg-6">
		<label for="name" class="form-label">名稱</label>
		<input type="text" v-bind:class="'form-control ' + ( fieldCheckInvalid('name', checkFields) ? 'is-invalid' : ' ')" id="name" placeholder="輸入名稱" v-model="this.formParam.name">
		<div v-if="fieldCheckInvalid('name', checkFields)" class="invalid-feedback d-block">{{ fieldInvalidFeedback('name', checkFields) }}</div>
	</div>
</div>
<div class="row">
	<div class="col-xs-6 col-md-6 col-lg-6">
		<label for="host" class="form-label">Host</label>
		<input type="text" v-bind:class="'form-control ' + ( fieldCheckInvalid('host', checkFields) ? 'is-invalid' : ' ')" id="host" placeholder="輸入Host" v-model="this.formParam.host">
		<div v-if="fieldCheckInvalid('host', checkFields)" class="invalid-feedback d-block">{{ fieldInvalidFeedback('host', checkFields) }}</div>
	</div>
	<div class="col-xs-6 col-md-6 col-lg-6">
		<label for="contextPath" class="form-label">Context path</label>
		<input type="text" v-bind:class="'form-control ' + ( fieldCheckInvalid('contextPath', checkFields) ? 'is-invalid' : ' ')" id="contextPath" placeholder="輸入Context path" v-model="this.formParam.contextPath">
		<div v-if="fieldCheckInvalid('contextPath', checkFields)" class="invalid-feedback d-block">{{ fieldInvalidFeedback('contextPath', checkFields) }}</div>
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
</template>
