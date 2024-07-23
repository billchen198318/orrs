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
import { useProg002d0001Store } from './QueryPageStore'; 
import { 
  escapeQifuHtmlMsg,
	getAxiosInstance, 
	getProgItem, 
	getUrlPrefixFromProgItem 
} from '../../components/BaseHelper';

let myModal;

export default {
	components: { Toolbar, Grid, GridPagination, HiddenQueryFieldAlertInfo },
	setup() { 
		definePageMeta({ middleware : ['auth'] });
		const queryPageStore = useProg002d0001Store();
		return {
			queryPageStore
		}
	},
	data() {
		return {
			pageProgramId : PageConstants.QueryId,
			dsList : [],
			qFieldShow : true,
			refRoleId : '',
			refRoleOid : '',
			copyRoleId : ''
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
			this.refRoleId = '';
			this.refRoleOid = '';
			this.copyRoleId = '';
			this.queryPageStore.queryParam.role = '';
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
		copyRole : _copyRole
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
					that.refRoleId = '';
					that.refRoleOid = '';
					for (var n in that.dsList) {
						if (that.dsList[n].oid == val) {
							that.refRoleOid = val;
							that.refRoleId = that.dsList[n].role;
						}
					}		
					myModal = new bootstrap.Modal(document.getElementById('exampleModal'));
					myModal.show();	
				},
				'icon'    : 'layers',
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
				'label' : 'Role編號',
				'field' : 'role'
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
			"role" : this.queryPageStore.queryParam.role
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

function _copyRole() {
    Swal.fire({title: "Loading...", html: "請等待", showConfirmButton: false, allowOutsideClick: false});
    Swal.showLoading();      
    let axiosInstance = getAxiosInstance();
    axiosInstance.post(import.meta.env.VITE_API_URL + PageConstants.eventNamespace + '/roleCopySaveJson', {
		'fromRoleOid'	: this.refRoleOid,
		'role' 			: this.copyRoleId,
		'description'	: 'copy of ' + this.refRoleId
	})
    .then(response => {
        Swal.hideLoading();
        Swal.close();
        if (null != response.data) {
            if (import.meta.env.VITE_SUCCESS_FLAG != response.data.success) {
                toast.warning(escapeQifuHtmlMsg(response.data.message));
                return;
            }            
            toast.success(response.data.message);   
            myModal.hide();
			this.btnQuery();
			this.refRoleOid = '';
			this.refRoleId = '';
			this.copyRoleId = '';
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
        description="Role管理." 
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
			<input type="text" class="form-control" id="role" placeholder="輸入Role編號" v-model="this.queryPageStore.queryParam.role">
			<label for="role">Role編號</label>
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
				<h1 class="modal-title fs-5" id="exampleModalLabel">複製Role</h1>
				<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
			</div>
			<div class="modal-body">
				<div class="form-group">
					<label for="copyRoleId">Role編號，複製來源:&nbsp;{{this.refRoleId}}</label>
					<input type="text" class="form-control" id="copyRoleId" v-model="this.copyRoleId">					
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
				<button type="button" class="btn btn-primary" v-on:click="this.copyRole">OK</button>
			</div>
		</div>
	</div>
</div>

</template>
