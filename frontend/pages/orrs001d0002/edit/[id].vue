<script>
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
				taskId : '',
				name : '',
				cronExpr : '',
				enableFlag : 'Y',
				description : '',
				cmds : []
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
			//this.formParam.taskId = '';
			this.formParam.name = '';
			this.formParam.cronExpr = '';
			this.formParam.enableFlag = 'Y';
			this.formParam.description = '';
			this.formParam.cmds = []
		},
		loadData : _loadData,
		btnAddCommand : function() {
			if (this.formParam.cmds.length >= 10) {
				toast.warning('最多' + this.formParam.cmds.length + '筆命令');
				return;
			}
			this.formParam.cmds.push({
				itemSeq : (this.formParam.cmds.length + 1),
				cmdId : import.meta.env.VITE_PLEASE_SELECT_ID
			});
		},
		btnDelCommand : function(position) {
			if (this.formParam.cmds.length < position) {
				return;
			}
			this.formParam.cmds.splice(position, 1);
		},
		loadCommandList : function() {
			var that = this;
			let axiosInstance = getAxiosInstance();
			axiosInstance.post(import.meta.env.VITE_API_URL + PageConstants.eventNamespace + '/loadCommandList')
			.then(response => {
				Swal.hideLoading();
				Swal.close();
				if (null != response.data) {
					this.commandList = response.data.value;
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
		cmdChange : function(index) {
			var selCmdId = this.formParam.cmds[index].cmdId;
			if (import.meta.env.VITE_PLEASE_SELECT_ID == selCmdId) {
				return;
			}
			var otherFound = false;
			for (var n = 0; n < this.formParam.cmds.length; n++) {
				if (this.formParam.cmds[n].cmdId == selCmdId) {
					if (n != index) {
						otherFound = true;
					}
				}
			}
			if (otherFound) {
				this.formParam.cmds[index].cmdId = import.meta.env.VITE_PLEASE_SELECT_ID;
				toast.warning('請選別的任務項目,該選取項目已被選取');
			}
		}		 
	},
	created() { 
	},
	mounted() { 
		this.loadCommandList();
		//this.loadData(); // call loadData move to loadCommandList
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
				description="llm任務編輯." 
				marginBottom="Y"
				refreshFlag="Y"
				@refreshMethod="loadCommandList"
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
			<label for="taskId" class="form-label">編號</label>
			<input type="text" v-bind:class="'form-control ' + ( fieldCheckInvalid('taskId', checkFields) ? 'is-invalid' : ' ')" id="taskId" placeholder="輸入編號" v-model="this.formParam.taskId" readonly="true">
			<div v-if="fieldCheckInvalid('taskId', checkFields)" class="invalid-feedback d-block">{{ fieldInvalidFeedback('taskId', checkFields) }}</div>
		</div>
		<div class="col-xs-6 col-md-6 col-lg-6">
			<label for="name" class="form-label">名稱</label>
			<input type="text" v-bind:class="'form-control ' + ( fieldCheckInvalid('name', checkFields) ? 'is-invalid' : ' ')" id="name" placeholder="輸入名稱" v-model="this.formParam.name">
			<div v-if="fieldCheckInvalid('name', checkFields)" class="invalid-feedback d-block">{{ fieldInvalidFeedback('name', checkFields) }}</div>
		</div>
	</div>
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6">
			<label for="cronExpr" class="form-label">cron</label>
			<input type="text" v-bind:class="'form-control ' + ( fieldCheckInvalid('cronExpr', checkFields) ? 'is-invalid' : ' ')" id="cronExpr" placeholder="輸入變數名稱" v-model="this.formParam.cronExpr">
			<div v-if="fieldCheckInvalid('cronExpr', checkFields)" class="invalid-feedback d-block">{{ fieldInvalidFeedback('cronExpr', checkFields) }}</div>
		</div>
		<div class="col-xs-6 col-md-6 col-lg-6">
	
			<label for="dialogH" class="form-label">啟用</label>
			<select class="form-select" aria-label="請選啟用/停用" v-model="this.formParam.enableFlag">
				<option value="Y">Y</option>
				<option value="N">N</option>
			</select>
		</div>
	</div>
	<div class="row">
		<div class="col-xs-12 col-md-12 col-lg-12">
			<label for="description" class="form-label">備註</label>
			<input type="text" class="form-control" id="description" v-model="this.formParam.description">
		</div>	
	</div>	
	<p style="margin-bottom: 5px"></p>
	<div class="row">
		<div class="col-xs-12 col-md-12 col-lg-12">
			<button type="button" class="btn btn-info" v-on:click="btnAddCommand"><i class="'bi bi-plus-circle"></i>&nbsp;增加命令</button>
		</div>
	</div>
	<p style="margin-bottom: 5px"></p>
	<div class="row" v-for="(c, index) in this.formParam.cmds">
		<table class="table">
			<tbody>
				<tr>
					<td width="80%">
						<select class="form-select" aria-label="請選命令" v-model="c.cmdId" v-on:change="cmdChange(index)">
							<option value="all">請選取</option>
							<option v-bind:value="i.cmdId" v-for="(i, iIdx) in this.commandList">{{i.cmdId}}&nbsp;-&nbsp;{{ i.name}}</option>
						</select>							
					</td>
					<td width="20%"><button type="button" class="btn btn-warning" v-on:click="btnDelCommand(index)"><i class="'bi bi-x-circle"></i>&nbsp;刪除</button></td>					
				</tr>
			</tbody>
		</table>					
	</div>
	<div class="row">
		<div class="col-xs-12 col-md-12 col-lg-12">
			<button type="button" class="btn btn-primary" v-on:click="btnUpdate"><i class="'bi bi-save"></i>&nbsp;儲存</button>
			&nbsp;
			<button type="button" class="btn btn-primary" v-on:click="btnClear"><i class="'bi bi-eraser"></i>&nbsp;清除</button>		
		</div>
	</div>
	</template>