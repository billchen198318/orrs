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
				cmdId : '',
				name : '',
				userMessage : '',
				resultVariable : '',
				resultType : 'GROOVY',
				description : '',
				prompts : []
			},
			variablePreviousMessage : import.meta.env.VITE_VARIABLE_PREVIOUS_MESSAGE,
			variablePreviousInvokeResult : import.meta.env.VITE_VARIABLE_PREVIOUS_INVOKE_RESULT
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
			this.formParam.cmdId = '';
			this.formParam.name = '';
			this.formParam.userMessage = '';
			this.formParam.resultVariable = '';
			this.formParam.resultType = 'GROOVY';
			this.formParam.description = '';
			this.formParam.prompts = []
		},
		btnAddPrompt : function() {
			if (this.formParam.prompts.length >= 5) {
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
        	description="llm命令建立." 
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
		<label for="cmdId" class="form-label">編號</label>
		<input type="text" v-bind:class="'form-control ' + ( fieldCheckInvalid('cmdId', checkFields) ? 'is-invalid' : ' ')" id="cmdId" placeholder="輸入編號" v-model="this.formParam.cmdId">
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
		<textarea row="3" col="25" v-bind:class="'form-control ' + ( fieldCheckInvalid('userMessage', checkFields) ? 'is-invalid' : ' ')" id="userMessage" placeholder="輸入使用者訊息" v-model="this.formParam.userMessage"></textarea>
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

		<label for="dialogH" class="form-label">截取內容</label>
		<select class="form-select" aria-label="請選取截取內容" v-model="this.formParam.resultType">
			<option value="GROOVY">GROOVY</option>
			<option value="HTML">HTML</option>
			<option value="TEXT">TEXT</option>
			<option value="JAVA">JAVA</option>
			<option value="JSON">JSON</option>
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
    	<button type="button" class="btn btn-primary" v-on:click="btnSave"><i class="'bi bi-save"></i>&nbsp;儲存</button>
    	&nbsp;
    	<button type="button" class="btn btn-primary" v-on:click="btnClear"><i class="'bi bi-eraser"></i>&nbsp;清除</button>		
	</div>
</div>
</template>
