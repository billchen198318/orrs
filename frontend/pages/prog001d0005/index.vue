<script>
import Swal from 'sweetalert2';
import { toast } from 'vue3-toastify';
import 'vue3-toastify/dist/index.css';

import bootstrap from 'bootstrap/dist/js/bootstrap';

import Toolbar from '@/components/Toolbar.vue';
import Grid from '@/components/Grid.vue';
import GridPagination from '@/components/GridPagination.vue';
import HiddenQueryFieldAlertInfo from '@/components/HiddenQueryFieldAlertInfo.vue';
import { PageConstants } from './config';
import { getGridConfig, setConfigRow, setConfigPage, setConfigTotal, resetConfigByOld } from '../../components/GridHelper';
import { useProg001d0005Store } from './QueryPageStore'; 
import { 
	getAccessTokenCookie,
	getAxiosInstance, 
	getProgItem, 
	getUrlPrefixFromProgItem 
} from '../../components/BaseHelper';

let myModal;

export default {
	components: { Toolbar, Grid, GridPagination, HiddenQueryFieldAlertInfo },
	setup() { 
		definePageMeta({ middleware : ['auth'] });
		const queryPageStore = useProg001d0005Store();
		return {
			queryPageStore
		}
	},
	data() {
		return {
			pageProgramId : PageConstants.QueryId,
			dsList : [],
			qFieldShow : true,
			previewParamList : [],
			previewReportId : ''
		}
	},
	methods: { 
		tbRefresh : function() {
			this.btnClear();
		},
		tbCreate : function() {
			this.$router.push(PageConstants.frontendNamespace + '/create');
		},
		tbQueryFieldShow : function() {
			this.qFieldShow = !this.qFieldShow;
		},			
		initQueryGridConfig : _initQueryGridConfig,
		btnQuery : _btnQuery,
		btnClear : function() {
			this.queryPageStore.queryParam.reportId = '';
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
		delItem : _delItem,
		previewPdf : _previewPdf
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
					var url = getUrlPrefixFromProgItem(getProgItem(PageConstants.EditId)) + '/' + val;
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
					var url = getUrlPrefixFromProgItem(getProgItem(PageConstants.SetParamId)) + '/' + val;
					thatRouter.push( url );
				},
				'icon'    : 'gear-fill',
				'type'    : 'customize',
				'memo'    : 'Set parameter.',
				'class'	  : 'btn btn-secondary btn-sm'
			}
			,			
			{
				'method'  : function(val) { 
					for (var n in that.dsList) {
						if (that.dsList[n].oid == val) {
							that.previewReportId = that.dsList[n].reportId;
						}
					}		
					if (undefined === that.previewReportId) {
						toast.error('資料錯誤,previewReportId');
						return;
					}
					var axiosInstance = getAxiosInstance();
					axiosInstance.post(import.meta.env.VITE_API_URL + PageConstants.eventNamespace + '/findSetParamPage', {
						"field": {
							"reportId"	: that.previewReportId
						}
						,
						"pageOf": {
							"select"  : 1,
							"showRow" : 100
						}
					})
					.then(response => {
						if (null != response.data) {
							if (import.meta.env.VITE_SUCCESS_FLAG != response.data.success) {
								return;
							}
							that.previewParamList = response.data.value;
							myModal = new bootstrap.Modal(document.getElementById('exampleModal'));
							myModal.show();							 
						} else {
							that.previewParamList = [];
							that.previewReportId = '';
						}
					})
					.catch(e => {
						alert(e);
					});
				},
				'icon'    : 'file-pdf-fill',
				'type'    : 'customize',
				'memo'    : 'Preview test jasperreport.',
				'class'	  : 'btn btn-secondary btn-sm'
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
				'label' : '報表Id',
				'field' : 'reportId'
			}
			,
			{
				'label' : '說明',
				'field' : 'description'
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
			"reportId" : this.queryPageStore.queryParam.reportId
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

function _previewPdf() {
	var urlArgs = '?jreportId=' + this.previewReportId;
	var inputs = document.getElementsByTagName('input');
	for (var i = 0; i < inputs.length; ++i) {
		if (inputs[i].id.indexOf('urlParam_') > -1) {
			var pname = inputs[i].id.replaceAll('urlParam_','');
			urlArgs += '&' + pname + '=' + inputs[i].value;
		}
	}	
	urlArgs += '&qifutoken=' + getAccessTokenCookie();
	myModal.hide();
	window.open(import.meta.env.VITE_JASPERREPORT_COMMON_URL + urlArgs, "_blank");
}

</script>

<template>

<div class="row">
  <div class="col-xs-12 col-md-12 col-lg-12">
    <Toolbar 
        :progId="this.pageProgramId" 
        description="Freemarker 樣板管理." 
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
	<div class="col-xs-12 col-md-12 col-lg-12">
		<div class="form-group form-floating">
			<input type="text" class="form-control" id="reportId" placeholder="輸入報表編號" v-model="this.queryPageStore.queryParam.reportId">
			<label for="reportId">報表編號</label>
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

<div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h1 class="modal-title fs-5" id="exampleModalLabel">預覽測試jasperreport產出</h1>
				<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
			</div>
			<div class="modal-body">
				<table class="table table-hover table-bordered">
					<thead>
						<tr>
							<th style="background-color: #575757; color: whitesmoke;">Url參數</th>
							<th style="background-color: #575757; color: whitesmoke;">參數值</th>
						</tr>
					</thead>
					<tbody>
						<tr v-for=" item in this.previewParamList ">
							<td>{{item.urlParam}}</td>
							<td><input type='text' class="form-control" v-bind:id="'urlParam_' + item.urlParam" /></td>
						</tr>
					</tbody>
				</table>				
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
				<button type="button" class="btn btn-primary" v-on:click="this.previewPdf">OK</button>
			</div>
		</div>
	</div>
</div>

</template>
