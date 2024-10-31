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

import VueMarkdown from 'vue-markdown-render';

let checkFields = new Object();

export default {
	components: { Toolbar, VueMarkdown },
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
				cmdId : '',
				itemSeq : -1,
				processMsT1 : '',
				processMsT2 : '',
				contentString : '',
				invokeContentString : '',
				lastCmd : '',
				processId : ''
			}	
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
		loadData : _loadData
	},
	created() { 
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
.hr-twill-colorful {
    border: 0;
    padding: 3px;
    background: linear-gradient(135deg, red, orange,green, blue, purple);
    --mask-image: repeating-linear-gradient(135deg, #000 0px, #000 1px, transparent 1px, transparent 6px);
    -webkit-mask-image: var(--mask-image);
    mask-image: var(--mask-image);
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
			<h5><span class="badge text-bg-secondary">llm回應訊息(原始內容)</span></h5>
        	<textarea rows="10" style="width: 100%;" v-model="this.formParam.contentString"></textarea>
		</div>				
	</div>
	<div class="row">
		<div class="col-xs-12 col-md-12 col-lg-12">
			<h5><span class="badge text-bg-secondary">llm回應訊息(markdown)</span></h5>
        	<vue-markdown style="width: 100%;" :source="this.formParam.contentString" :watches="['show','html','breaks','linkify','emoji','typographer','toc']" />
		</div>				
	</div>	

	<br>
	<hr class="hr-twill-colorful" v-if=" null != this.formParam.invokeContentString && this.formParam.invokeContentString.length > 0 ">
	<br v-if=" null != this.formParam.invokeContentString && this.formParam.invokeContentString.length > 0 ">

	<div class="row" v-if=" null != this.formParam.invokeContentString && this.formParam.invokeContentString.length > 0 ">
		<div class="col-xs-12 col-md-12 col-lg-12">
			<h5><span class="badge text-bg-secondary">invoke結果/Server端觸發(內容)</span></h5>
        	<textarea rows="10" style="width: 100%;" v-model="this.formParam.invokeContentString"></textarea>
		</div>				
	</div>
	<div class="row" v-if=" null != this.formParam.invokeContentString && this.formParam.invokeContentString.length > 0 ">
		<div class="col-xs-12 col-md-12 col-lg-12">
			<h5><span class="badge text-bg-secondary">invoke結果/Server端觸發(markdown)</span></h5>
        	<vue-markdown style="width: 100%;" :source="this.formParam.invokeContentString" :watches="['show','html','breaks','linkify','emoji','typographer','toc']" />
		</div>				
	</div>	

</template>