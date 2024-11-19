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
import HiddenQueryFieldAlertInfo from '@/components/HiddenQueryFieldAlertInfo.vue';
import { PageConstants } from './config';
import { useOrrs001d0004Store } from './QueryPageStore'; 
import { 
	getAxiosInstance, 
	getProgItem, 
	getUrlPrefixFromProgItem 
} from '../../components/BaseHelper';

export default {
	components: { Toolbar, HiddenQueryFieldAlertInfo, Codemirror },
	setup() { 
		definePageMeta({ middleware : ['auth'] });
		const queryPageStore = useOrrs001d0004Store();

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
			pageProgramId : PageConstants.TestId,
			dsList : [],
			qFieldShow : true
		}
	},
	methods: { 
		tbRefresh : function() {
			this.btnClear();
		},
		tbQueryFieldShow : function() {
			this.qFieldShow = !this.qFieldShow;
		},
		btnBack : function() {
			this.$router.back();
		},        
		btnQuery : function() {
            Swal.fire({title: "Loading...", html: "請等待", showConfirmButton: false, allowOutsideClick: false});
            Swal.showLoading();
            this.dsList = [];
            var axiosInstance = getAxiosInstance();
            axiosInstance.post(import.meta.env.VITE_API_URL + PageConstants.eventNamespace + '/queryDocTest', {
                "field": {
                    "userMessage"           : this.queryPageStore.queryParam.userMessage,
                    "similarityThreshold"   : this.queryPageStore.queryParam.similarityThreshold
                }
                ,
                "pageOf": {
                    "select"  : 0,
                    "showRow" : 0
                }
            })
            .then(response => {
                Swal.hideLoading();
                Swal.close();
                if (null != response.data) {
                    if (import.meta.env.VITE_SUCCESS_FLAG != response.data.success) {
                        toast.warning(response.data.message);
                        return;
                    }
                    this.dsList = response.data.value;
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
		btnClear : function() {
			this.queryPageStore.queryParam.userMessage = '';
			this.queryPageStore.queryParam.similarityThreshold = 0.0;
			this.dsList = [];
		}
	},
	created() {
	},
	mounted() { 
	}
}

</script>

<template>

<div class="row">
  <div class="col-xs-12 col-md-12 col-lg-12">
    <Toolbar 
        :progId="this.pageProgramId" 
        description="檢索文件測試." 
        marginBottom="Y"
        refreshFlag="Y"
        @refreshMethod="tbRefresh"
        backFlag="Y"
        @backMethod="btnBack"
        createFlag="N"
        @createMethod="null"
        saveFlag="N"
        @saveMethod="null"
		queryFieldShowSwitchFlag="Y"
		@queryFieldShowSwitcMethod="tbQueryFieldShow"
    ></Toolbar>
  </div>
</div>

<HiddenQueryFieldAlertInfo :dataSource="this.dsList" :queryFieldShowFlag="this.qFieldShow"></HiddenQueryFieldAlertInfo>
<div class="row" v-show=" qFieldShow ">
	<div class="col-xs-12 col-md-12 col-lg-12">
        <label for="userMessage">Search User message</label>
		<textarea class="form-control" id="userMessage" rows="3" v-model="this.queryPageStore.queryParam.userMessage"></textarea>
  	</div>
</div>
<p style="margin-bottom: 5px" v-show=" qFieldShow "></p>
<div class="row" v-show=" qFieldShow ">
    <div class="col-xs-12 col-md-12 col-lg-12">
        <label for="similarityThreshold" class="form-label">Similarity Threshold&nbsp;/&nbsp;相似度閾值&nbsp;({{ this.queryPageStore.queryParam.similarityThreshold }})</label>
        <input type="range" class="form-range" min="0.0" max="1.0" step="0.05" id="similarityThreshold" v-model="this.queryPageStore.queryParam.similarityThreshold">        
    </div>
</div>
<p style="margin-bottom: 5px" v-show=" qFieldShow "></p>
<div class="row" v-show=" qFieldShow ">
  	<div class="col-xs-12 col-md-12 col-lg-12">
    	<button type="button" class="btn btn-primary" v-on:click="btnQuery"><i class="'bi bi-search"></i>&nbsp;查詢</button>
    	&nbsp;
    	<button type="button" class="btn btn-primary" v-on:click="btnClear"><i class="'bi bi-eraser"></i>&nbsp;清除</button>
  	</div>
</div>  
<div class="row" v-show=" qFieldShow ">
	<div class="col-xs-12 col-md-12 col-lg-12">&nbsp;</div>
</div>

<div class="row">
	<div class="col-xs-12 col-md-12 col-lg-12">
        <div class="callout callout-info" v-for="d in this.dsList">
		    <h4>{{ d.docId }}</h4>
            <Codemirror
                v-model="d.content"
                :options="cmOptions"
                :extensions="cmExtensions"
                :style="{ height: '400px' }"
                ref="cmRef"
                v-bind:id="'content_'+d.docId">
		    </Codemirror>	
	    </div>        
    </div>
</div>

<br>
<br>

</template>
