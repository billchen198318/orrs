<script>
import Swal from 'sweetalert2';
import { toast } from 'vue3-toastify';
import 'vue3-toastify/dist/index.css';

import Toolbar from '@/components/Toolbar.vue';
import HiddenQueryFieldAlertInfo from '@/components/HiddenQueryFieldAlertInfo.vue';
import { PageConstants } from './config';
import { useOrrs001d0003Store } from './QueryPageStore'; 
import { 
	getAccessTokenCookie,
	getAxiosInstance, 
	getProgItem, 
	getUrlPrefixFromProgItem 
} from '../../components/BaseHelper';

export default {
	components: { Toolbar, HiddenQueryFieldAlertInfo },
	setup() { 
		definePageMeta({ middleware : ['auth'] });
		const queryPageStore = useOrrs001d0003Store();
		return {
			queryPageStore
		}
	},
	data() {
		return {
			pageProgramId : PageConstants.QueryId,
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
		btnQuery : _btnQuery,
		btnClear : function() {
			this.queryPageStore.queryParam.taskId = '';
			this.queryPageStore.queryParam.processIdLike = '';
			this.queryPageStore.queryParam.lastCmd = true;
			this.queryPageStore.queryParam.date1 = '';
			this.queryPageStore.queryParam.date2 = '';

			this.dsList = [];
		},
		delItem : _delItem,
		btnPreview : function(oid) {
			var url = import.meta.env.VITE_BACKGROUND_SERVER_SITE + 'previewResult?qifutoken=' + getAccessTokenCookie() + '&oid=' + oid;
			window.open(url, '_blank');
		},
		btnViewResultData : function(oid) {
			var thatRouter = this.$router;
			var url = getUrlPrefixFromProgItem( getProgItem(PageConstants.EditId) ) + '/' + oid;
			thatRouter.push( url );			
		},
		btnDelete : function(oid) {
			var that = this;
			Swal.fire({
				title: '刪除,此刪除會將相關執行序項目都刪除?',
				icon: 'question',
				iconHtml: '?',
				confirmButtonText: 'Yes',
				cancelButtonText: 'No',
				showCancelButton: true,
				showCloseButton: true
			}).then((result) => {
				if (result.isConfirmed) {
					that.delItem(oid);
				}
			}); 			
		}
	},
	created() {

	},
	mounted() { 
		this.btnQuery();
	}
}

function _btnQuery() {
	Swal.fire({title: "Loading...", html: "請等待", showConfirmButton: false, allowOutsideClick: false});
	Swal.showLoading();
	this.dsList = [];
	var axiosInstance = getAxiosInstance();
	axiosInstance.post(import.meta.env.VITE_API_URL + PageConstants.eventNamespace + '/findPage', {
		"field": {
			"taskId"		: this.queryPageStore.queryParam.taskId,
			"processIdLike"	: this.queryPageStore.queryParam.processIdLike,
			'lastCmd'		: (this.queryPageStore.queryParam.lastCmd ? 'Y' : 'N'),
			'date1'			: this.queryPageStore.queryParam.date1,
			'date2'			: this.queryPageStore.queryParam.date2,
		}
		,
		"pageOf": {
			"select"  : 1,
			"showRow" : 1000
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
}

function _delItem(oid) {
	Swal.fire({title: "Loading...", html: "請等待", showConfirmButton: false, allowOutsideClick: false});
	Swal.showLoading();  
	var axiosInstance = getAxiosInstance();  
	axiosInstance.post(import.meta.env.VITE_API_URL + PageConstants.eventNamespace + '/delete', {"oid": oid})
	.then(response => {
		Swal.hideLoading();
		Swal.close();
		if (null != response.data) {
			if (import.meta.env.VITE_SUCCESS_FLAG == response.data.success) {
				toast.success(response.data.message);
			} else {        
				toast.warning(response.data.message);
			} 
			this.btnQuery();
		} else {
			toast.error('error, null');
		}
	})
	.catch(e => {
		Swal.hideLoading();
		Swal.close();    
		this.btnQuery();
		alert(e);
	});
}

</script>

<template>

<div class="row">
  <div class="col-xs-12 col-md-12 col-lg-12">
    <Toolbar 
        :progId="this.pageProgramId" 
        description="任務結果查詢." 
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

<HiddenQueryFieldAlertInfo :dataSource="this.dsList" :queryFieldShowFlag="this.qFieldShow"></HiddenQueryFieldAlertInfo>
<div class="row" v-show=" qFieldShow ">
	<div class="col-xs-6 col-md-6 col-lg-6">
		<div class="form-group form-floating">
			<input type="text" class="form-control" id="taskId" placeholder="輸入Task編號" v-model="this.queryPageStore.queryParam.taskId">
			<label for="taskId">Task編號</label>
    	</div>
  	</div>
  	<div class="col-xs-6 col-md-6 col-lg-6">
    	<div class="form-group form-floating">
			<input type="text" class="form-control" id="processIdLike" placeholder="輸入Process編號" v-model="this.queryPageStore.queryParam.processIdLike">
			<label for="processIdLike">Process編號</label>
    	</div>
  	</div>
</div>
<p style="margin-bottom: 5px"></p>
<div class="row" v-show=" qFieldShow ">
	<div class="col-xs-6 col-md-6 col-lg-6">
		<div class="form-group form-floating">
			<table>
				<tbody>
					<tr>
						<td colspan="2">執行日期起迄</td>
					</tr>
					<tr>
						<td>
							<input class="form-control" type="date" id="date1" v-model="this.queryPageStore.queryParam.date1">
						</td>
						<td>
							<input class="form-control" type="date" id="date2" v-model="this.queryPageStore.queryParam.date2">
						</td>
					</tr>
				</tbody>
			</table>
    	</div>
  	</div>
  	<div class="col-xs-6 col-md-6 col-lg-6">
    	<div class="form-group form-floating">
			<div class="form-check form-switch">
				<input class="form-check-input" type="checkbox" role="switch" id="lastCmd" v-model="this.queryPageStore.queryParam.lastCmd">
				<label class="form-check-label" for="lastCmd">有執行最後命令的任務</label>
			</div>
    	</div>
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
		<div class="card-group card-deck">
			<div class="card text-bg-light mb-3 border-secondary" style="max-width: 25rem;" v-for="item in this.dsList">
				<div class="card-header fw-bold text-secondary">{{  new Date(item.cdate).toLocaleString() }}</div>
				<div class="card-body">
					<table class="table table-bordered">
						<thead class="table-info">
							<tr>
								<th colspan="2"><i class="bi bi-info-circle" v-if=" 'Y' == item.processFlag "></i><i class="bi bi-x-octagon-fill" v-if=" 'Y' != item.processFlag "></i>&nbsp;{{ item.taskName }}&nbsp;<i class="bi bi-dash-lg"></i>&nbsp;{{ item.itemSeq }}</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td width="40%" class="table-secondary fw-bold">Process Id</td>
								<td width="60%">{{ item.processId }}</td>
							</tr>
							<tr>
								<td width="40%" class="table-secondary fw-bold">Task</td>
								<td width="60%">{{ item.taskId }}&nbsp;/&nbsp;{{ item.taskName }}</td>
							</tr>		
							<tr>
								<td width="40%" class="table-secondary fw-bold">Command</td>
								<td width="60%">{{ item.cmdId }}&nbsp;/&nbsp;{{ item.cmdName }}</td>
							</tr>			
							<tr>
								<td width="40%" class="table-secondary fw-bold">Seq</td>
								<td width="60%"><span class="badge text-bg-warning">{{ item.itemSeq }}</span></td>
							</tr>	
							<tr>
								<td width="40%" class="table-secondary fw-bold">使用時間/秒</td>
								<td width="60%">{{ (Number(item.processMsT2) - Number(item.processMsT1))/1000 }}</td>
							</tr>			
							<tr>
								<td width="40%" class="table-secondary fw-bold">最後命令的任務</td>
								<td width="60%">{{ item.lastCmd }}</td>
							</tr>	
							<tr>
								<td width="40%" class="table-secondary fw-bold">完成執行</td>
								<td width="60%">
									<span class="badge rounded-pill text-bg-success" v-if=" 'Y' == item.processFlag ">{{ item.processFlag }}</span>
									<span class="badge rounded-pill text-bg-danger" v-if=" 'Y' != item.processFlag ">{{ item.processFlag }}</span>
								</td>
							</tr>								
							<tr>
								<td width="100%" colspan="2">{{ item.taskDescription }}</td>
							</tr>																																				
						</tbody>
					</table>

					<button type="button" class="btn btn-sm btn-success" v-if=" 'Y' == item.lastCmd " v-on:click="btnPreview(item.oid)" data-bs-toggle="tooltip" data-bs-placement="bottom" data-bs-title="檢視結果"><i class="bi bi-play-btn"></i></button>
					&nbsp;
					<button type="button" class="btn btn-sm btn-info" v-on:click="btnViewResultData(item.oid)" data-bs-toggle="tooltip" data-bs-placement="bottom" data-bs-title="執行紀錄"><i class="bi bi-file-earmark-text"></i></button>
					&nbsp;
					<button type="button" class="btn btn-sm btn-danger" v-on:click="btnDelete(item.oid)" data-bs-toggle="tooltip" data-bs-placement="bottom" data-bs-title="刪除"><i class="bi bi-trash"></i></button>

				</div>
			</div>
		</div>	
	</div>
</div>

</template>
