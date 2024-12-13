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
			reqList : [],
			queryBtnDisable : false
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
			this.reqList = [];
			this.queryBtnDisable = false;
		},
		send : function() {
			this.queryBtnDisable = true;
			var that = this;
			this.reqList.push({"question" : this.queryPageStore.queryParam.message, "ans" : ''});
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
					that.reqList[currPos].ans += data.message.content;
					if (data.done) {
						that.queryBtnDisable = false;
						that.queryPageStore.queryParam.message = '';
						toast.info('done...');
					}
				},
				onerror(err) {
					that.queryBtnDisable = false;
					toast.error(err);
					that.reqList[currPos].ans = '...';
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
		queryFieldShowSwitchFlag="N"
		@queryFieldShowSwitcMethod="null"
    ></Toolbar>
  </div>
</div>

<section>
    <div class="container py-5">
        <div class="row d-flex justify-content-center">
            <div class="col-md-12 col-lg-12 col-xl-12">
                <div class="card">
                    <div class="card-header d-flex justify-content-between align-items-center p-3" style="border-top: 4px solid #ffa900;">
                        <h5 class="mb-0">Chat</h5>
                        <div class="d-flex flex-row align-items-center">
                            <span class="badge bg-warning me-3">{{ this.reqList.length }}</span>
                            <i class="fas fa-minus me-3 text-muted fa-xs"></i>
                            <i class="fas fa-comments me-3 text-muted fa-xs"></i>
                            <i class="fas fa-times text-muted fa-xs"></i>
                        </div>
                    </div>
                    <div class="card-body" data-mdb-perfect-scrollbar-init style="position: relative; height: 100%;">

						<div v-for=" r in this.reqList ">
                        	<div class="d-flex justify-content-between">
                            	<p class="small mb-1">You</p>
                        	</div>
                        	<div class="d-flex flex-row justify-content-start">
                            	<img src="https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-chat/ava5-bg.webp" alt="avatar 1" style="width: 45px; height: 100%;" />
                            	<div>
                                	<p class="small p-2 ms-3 mb-3 rounded-3 bg-body-tertiary">{{ r.question }}</p>
                            	</div>
                        	</div>
							<div class="d-flex justify-content-between">
								<p class="small mb-1">LLM</p>
							</div>
							<div class="d-flex flex-row justify-content-end mb-4 pt-1">
								<div>
									<p class="small p-2 me-3 mb-3 text-white rounded-3 bg-warning">{{ '' == r.ans ? '...' : r.ans }}</p>
								</div>
								<img src="https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-chat/ava6-bg.webp" alt="avatar 1" style="width: 45px; height: 100%;" />
                        	</div>							
						</div>
						
                    </div>
                    <div class="card-footer text-muted d-flex justify-content-start align-items-center p-3">
                        <div class="input-group mb-0">
                            <input type="text" class="form-control" placeholder="Type message" aria-label="message" aria-describedby="button-addon2" v-model="this.queryPageStore.queryParam.message" />
                            <button data-mdb-button-init data-mdb-ripple-init class="btn btn-warning" type="button" id="button-addon2" style="padding-top: 0.55rem;" @click="btnQuery" v-bind:disabled="this.queryBtnDisable">
                                送出
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>


</template>

