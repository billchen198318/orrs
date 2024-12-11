<script>
import Swal from 'sweetalert2';
import { toast } from 'vue3-toastify';
import 'vue3-toastify/dist/index.css';

import Toolbar from '@/components/Toolbar.vue';
import HiddenQueryFieldAlertInfo from '@/components/HiddenQueryFieldAlertInfo.vue';
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
	components: { Toolbar, HiddenQueryFieldAlertInfo },
	setup() { 
		definePageMeta({ middleware : ['auth'] });
		const queryPageStore = useOrrs001d0005Store();
		return {
			queryPageStore
		}
	},
	data() {
		return {
			pageProgramId : PageConstants.QueryId,
			msgList : [],
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
		btnQuery : function() {

		},
		btnClear : function() {
			this.queryPageStore.queryParam.message = '';
			this.queryPageStore.queryParam.system = '';
			this.queryPageStore.queryParam.similarityThreshold = 0.0;
			this.msgList = [];
		},
		send : function() {
			fetchEventSource(import.meta.env.VITE_API_URL + PageConstants.eventNamespace + '/chat',{
				method : "POST",
				headers : {
					"Content-Type"	: "application/json",
					"Authorization"	: "Bearer " + getAccessTokenCookie()
				},
				body : JSON.stringify(this.queryPageStore.queryParam),
				openWhenHidden : true,
				onmessage(msg) {
					console.log(msg);
				},
				onerror(err) {
					console.log(err);
				}
			});
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
        description="Chat test." 
        marginBottom="Y"
        refreshFlag="Y"
        @refreshMethod="tbRefresh"
        backFlag="N"
        @backMethod="null"
        createFlag="N"
        @createMethod="null"
        saveFlag="N"
        @saveMethod="null"
		queryFieldShowSwitchFlag="Y"
		@queryFieldShowSwitcMethod="tbQueryFieldShow"
    ></Toolbar>
  </div>
</div>

<HiddenQueryFieldAlertInfo :dataSource="this.msgList" :queryFieldShowFlag="this.qFieldShow"></HiddenQueryFieldAlertInfo>



</template>
