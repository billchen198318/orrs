<script>
import Swal from 'sweetalert2';
import { toast } from 'vue3-toastify';
import 'vue3-toastify/dist/index.css';

import Toolbar from '@/components/Toolbar.vue';
import HiddenQueryFieldAlertInfo from '@/components/HiddenQueryFieldAlertInfo.vue';
import { PageConstants } from './config';
import { useOrrs001d0003Store } from './QueryPageStore'; 
import { 
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
		delItem : _delItem
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
        description="任務結果." 
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
			<div class="card text-bg-light mb-3" style="max-width: 25rem;" v-for="item in this.dsList">
				<div class="card-header">{{  new Date(item.cdate).toLocaleString() }}</div>
				<div class="card-body">
					<h5 class="card-title">{{ item.taskName }}</h5>
					<span class="badge text-bg-info">Process Id</span>&nbsp;{{ item.processId }}
					<br>
					<span class="badge text-bg-info">Task</span>&nbsp;{{ item.taskId }}&nbsp;/&nbsp;{{ item.taskName }}
					<br>
					<span class="badge text-bg-info">Command</span>&nbsp;{{ item.cmdId }}&nbsp;/&nbsp;{{ item.cmdName }}
					<br>		
					<span class="badge text-bg-info">Seq</span>&nbsp;<span class="badge text-bg-warning">{{ item.itemSeq }}</span>
					<br>							
					最後命令的任務<span class="badge text-bg-success">{{ item.lastCmd }}</span>
					<br><br>
					說明:
					<br>
					<p class="card-text">{{ item.taskDescription }}</p>
					<br>
					<button type="button" class="btn btn-primary" v-if=" 'Y' == item.lastCmd "><i class="bi bi-play-btn"></i>&nbsp;檢視結果</button>
					&nbsp;
					<button type="button" class="btn btn-secondary"><i class="bi bi-file-earmark-text"></i>&nbsp;檢視內容</button>
					&nbsp;
					<button type="button" class="btn btn-danger"><i class="bi bi-trash"></i>&nbsp;刪除</button>
				</div>
			</div>
		</div>	
	</div>
</div>

</template>
