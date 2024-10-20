<script>
import Swal from 'sweetalert2';
import { toast } from 'vue3-toastify';
import 'vue3-toastify/dist/index.css';

import Toolbar from '@/components/Toolbar.vue';
import Grid from '@/components/Grid.vue';
import GridPagination from '@/components/GridPagination.vue';
import HiddenQueryFieldAlertInfo from '@/components/HiddenQueryFieldAlertInfo.vue';
import { PageConstants } from './config';
import { getGridConfig, setConfigRow, setConfigPage, setConfigTotal, resetConfigByOld } from '../../components/GridHelper';
import { useOrrs001d0002Store } from './QueryPageStore'; 
import { 
	getAxiosInstance, 
	getProgItem, 
	getUrlPrefixFromProgItem 
} from '../../components/BaseHelper';

export default {
	components: { Toolbar, Grid, GridPagination, HiddenQueryFieldAlertInfo },
	setup() { 
		definePageMeta({ middleware : ['auth'] });
		const queryPageStore = useOrrs001d0002Store();
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
		tbCreate : function() {
			this.$router.push( PageConstants.frontendNamespace + '/create' );
		},
		tbQueryFieldShow : function() {
			this.qFieldShow = !this.qFieldShow;
		},
		initQueryGridConfig : _initQueryGridConfig,
		btnQuery : _btnQuery,
		btnClear : function() {
			this.queryPageStore.queryParam.name = '';
			this.queryPageStore.queryParam.taskId = '';
			this.dsList = [];
			this.clearGridConfig();
		},
		changeQueryGridRow : function(row) {
			setConfigRow(this.queryPageStore.gridConfig, row);
			this.queryPageStore.gridConfig.page = 1;
			this.btnQuery();
		},
		changePageSelect : function(page) {
			setConfigPage(this.queryPageStore.gridConfig, page);
			this.btnQuery();
		},
		clearGridConfig : function() {
			setConfigRow(this.queryPageStore.gridConfig, import.meta.env.VITE_DEFAULT_ROW);
			setConfigPage(this.queryPageStore.gridConfig, 1);
			setConfigTotal(this.queryPageStore.gridConfig, 0);
		},
		delItem : _delItem
	},
	created() {
		let newGridConfig = this.initQueryGridConfig();
		let oldGridConfig = this.queryPageStore.gridConfig;
		if ( !(undefined === this.queryPageStore.gridConfig.column) ) {
			resetConfigByOld(newGridConfig, oldGridConfig);
		}
		this.queryPageStore.gridConfig = newGridConfig;
	},
	mounted() { 
		if (this.queryPageStore.gridConfig.total > 0) {
			this.btnQuery();
		}
	}
}

function _initQueryGridConfig() {
	var that = this;
	var thatRouter = this.$router;
  	return getGridConfig(
		'oid'
		,
		[
			{
				'method'  : function(val) { 
					var url = getUrlPrefixFromProgItem( getProgItem(PageConstants.EditId) ) + '/' + val;
					thatRouter.push( url );
				},
				'icon'    : 'pen',
				'type'    : 'edit',
				'memo'    : 'Edit current item.',
				'class'	  : 'btn btn-info btn-sm'
			}
			,
			{
				'method'  : function(val) { 
					Swal.fire({
						title: '刪除?',
						icon: 'question',
						iconHtml: '?',
						confirmButtonText: 'Yes',
						cancelButtonText: 'No',
						showCancelButton: true,
						showCloseButton: true
					}).then((result) => {
						if (result.isConfirmed) {
							that.delItem(val);
						}
					});            
				},
				'icon'    : 'trash',
				'type'    : 'delete',
				'memo'    : 'Delete current item.',
				'class'	  : 'btn btn-danger btn-sm'
			}     
		]
		,
		[
			{
				'label' 	: '<i class="bi bi-hand-index-thumb"></i>',
				'field' 	: 'oid',
				'labHtml'	: true			
			}
			,
			{
				'label' : 'Id',
				'field' : 'taskId'
			}
			,
			{
				'label' : 'Name',
				'field' : 'name'
			}
			,
			{
				'label' : 'Cron',
				'field' : 'cronExpr'
			}			
			,
			{
				'label' : 'Enable',
				'field' : 'enableFlag'
			}				
		]    
	);
}

function _btnQuery() {
	Swal.fire({title: "Loading...", html: "請等待", showConfirmButton: false, allowOutsideClick: false});
	Swal.showLoading();
	this.dsList = [];
	var axiosInstance = getAxiosInstance();
	axiosInstance.post(import.meta.env.VITE_API_URL + PageConstants.eventNamespace + '/findPage', {
		"field": {
			"taskId"     : this.queryPageStore.queryParam.taskId,
			"nameLike"  : this.queryPageStore.queryParam.name
		}
		,
		"pageOf": {
			"select"  : this.queryPageStore.gridConfig.page,
			"showRow" : this.queryPageStore.gridConfig.row
		}
	})
	.then(response => {
		Swal.hideLoading();
		Swal.close();
		if (null != response.data) {
			if (import.meta.env.VITE_SUCCESS_FLAG != response.data.success) {
				this.clearGridConfig();
				toast.warning(response.data.message);
				return;
			}
			this.dsList = response.data.value;
			setConfigTotal(this.queryPageStore.gridConfig, response.data.pageOf.countSize);
		} else {
			toast.error('error, null');
			this.clearGridConfig();
		}
	})
	.catch(e => {
		Swal.hideLoading();
		Swal.close();    
		this.clearGridConfig();
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
			this.clearGridConfig();
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
        description="llm任務." 
        marginBottom="Y"
        refreshFlag="Y"
        @refreshMethod="tbRefresh"
        backFlag="N"
        @backMethod="null"
        createFlag="Y"
        @createMethod="tbCreate"
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
			<input type="text" class="form-control" id="taskId" placeholder="輸入編號" v-model="this.queryPageStore.queryParam.taskId">
			<label for="taskId">編號</label>
    	</div>
  	</div>
  	<div class="col-xs-6 col-md-6 col-lg-6">
    	<div class="form-group form-floating">
			<input type="text" class="form-control" id="name" placeholder="輸入名稱" v-model="this.queryPageStore.queryParam.name">
			<label for="name">名稱</label>
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
		<GridPagination :progId="this.pageProgramId" :gridConfig="this.queryPageStore.gridConfig" :changePageSelectMethod="this.changePageSelect" :changeGridConfigRowMethod="this.changeQueryGridRow"></GridPagination>
		<Grid :progId="this.pageProgramId" :dataSource="this.dsList" :config="this.queryPageStore.gridConfig" ></Grid>
	</div>
</div>  

</template>
