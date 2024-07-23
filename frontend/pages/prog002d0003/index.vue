<script>
import Swal from 'sweetalert2';
import { toast } from 'vue3-toastify';
import 'vue3-toastify/dist/index.css';

import Toolbar from '@/components/Toolbar.vue';
import { PageConstants } from './config';
import { useProg002d0003Store } from './QueryPageStore'; 
import { 
    escapeQifuHtmlMsg,
	getAxiosInstance, 
	getProgItem, 
	getUrlPrefixFromProgItem 
} from '../../components/BaseHelper';

export default {
	components: { Toolbar },
	setup() { 
        definePageMeta({ middleware : ['auth'] });
		const queryPageStore = useProg002d0003Store();
		return {
			queryPageStore
		}
	},
	data() {
		return {
			pleaseSelectId : import.meta.env.VITE_PLEASE_SELECT_ID,
			pleaseSelectText : import.meta.env.VITE_PLEASE_SELECT_LABEL,            
			pageProgramId : PageConstants.QueryId,
            progList : [],
            progRoleList : [],
            progRoleEnableList : [],
            formParam : {
                oid : ''
            }
		}
	},
	methods: { 
		tbRefresh : function() {
			this.clearPage();
		},
		clearPage : function() {
			this.queryPageStore.queryParam.oid = import.meta.env.VITE_PLEASE_SELECT_ID;
            this.progRoleList = [];
            this.progRoleEnableList = [];
            this.formParam.oid = '';
		},
        loadProgramList : _loadProgramList,
        progChange : _progChange,
        progRoleEnableChange : _progRoleEnableChange,
        checkItemChecked : _checkItemChecked
	},
	created() {
        this.loadProgramList();
        if (this.queryPageStore.queryParam.oid != import.meta.env.VITE_PLEASE_SELECT_ID) {
            this.progChange();
        }
	},
	mounted() { 
	}
}

function _loadProgramList() {
    var currSysId = 'CORE';
	this.progList = [];
    Swal.fire({title: "Loading...", html: "請等待", showConfirmButton: false, allowOutsideClick: false});
    Swal.showLoading(); 
    let axiosInstance = getAxiosInstance();
    axiosInstance.post(import.meta.env.VITE_API_URL + PageConstants.eventNamespace + '/findProgramFolderMenuItem/' + currSysId)
    .then(response => {
        Swal.hideLoading();
        Swal.close();
        if (null != response.data) {
            if (import.meta.env.VITE_SUCCESS_FLAG != response.data.success) {
                toast.warning(response.data.message);
                return;
            }
            this.progList = response.data.value;            
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

function _progChange() {
	this.progRoleList = [];
    this.progRoleEnableList = [];
    if (import.meta.env.VITE_PLEASE_SELECT_ID == this.queryPageStore.queryParam.oid) {
        return;
    }
	this.formParam.oid = this.queryPageStore.queryParam.oid;
    Swal.fire({title: "Loading...", html: "請等待", showConfirmButton: false, allowOutsideClick: false});
    Swal.showLoading(); 
    let axiosInstance = getAxiosInstance();
    axiosInstance.post(import.meta.env.VITE_API_URL + PageConstants.eventNamespace + '/findMenuProgramRoleList', this.formParam)
    .then(response => {
        Swal.hideLoading();
        Swal.close();
        if (null != response.data) {
            if (import.meta.env.VITE_SUCCESS_FLAG != response.data.success) {
                toast.warning(response.data.message);
                return;
            }
            this.progRoleList = response.data.value.all;
            this.progRoleEnableList = response.data.value.enable;
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

function _progRoleEnableChange(e, itemOid) {
	var checked = e.target.checked;
    var appendOid = '';
    for (var n in this.progRoleEnableList) {        
        appendOid += this.progRoleEnableList[n].oid + ',';
    }
    if (checked) {
        appendOid += itemOid + ',';
    } else {
        appendOid = appendOid.replaceAll(itemOid+',', '');
    }
	if ('' == appendOid) {
		appendOid = ',';
	}
    this.formParam.oid = this.queryPageStore.queryParam.oid;
    Swal.fire({title: "Loading...", html: "請等待", showConfirmButton: false, allowOutsideClick: false});
    Swal.showLoading(); 
    let axiosInstance = getAxiosInstance();
    axiosInstance.post(import.meta.env.VITE_API_URL + PageConstants.eventNamespace + '/updateMenuRole/' + this.formParam.oid + '/' + appendOid)
    .then(response => {
        Swal.hideLoading();
        Swal.close();
        if (null != response.data) {
            if (import.meta.env.VITE_SUCCESS_FLAG != response.data.success) {
                toast.warning(response.data.message);
                this.clearPage();
            }            
        } else {
            toast.error('error, null');
            this.clearPage();
        }        
    })
    .catch(e => {
        Swal.hideLoading();
        Swal.close();        
        alert(e);        
        this.clearPage();
    });	
}

function _checkItemChecked(itemOid) {
	var f = false;
	for (var n in this.progRoleEnableList) {
		if (this.progRoleEnableList[n].oid == itemOid) {
			f = true;
		}
	}
	return f;
}

</script>

<template>

<div class="row">
  <div class="col-xs-12 col-md-12 col-lg-12">
    <Toolbar 
        :progId="this.pageProgramId" 
        description="選單Role管理配置." 
        marginBottom="Y"
        refreshFlag="Y"
        @refreshMethod="tbRefresh"
        backFlag="N"
        @backMethod="null"
        createFlag="N"
        @createMethod="null"
        saveFlag="N"
        @saveMethod="null"
    ></Toolbar>
  </div>
</div>
<div class="row">
	<div class="col-xs-12 col-md-12 col-lg-12 form-floating">
		<select id="progOid" class="form-select" aria-label="請選取" v-model="this.queryPageStore.queryParam.oid" @change="this.progChange">
			<option :value="this.pleaseSelectId">{{this.pleaseSelectText}}</option>
			<option v-for=" item in this.progList " :value="item.oid">{{item.name}}</option>
		</select>		
        <label for="progOid">程式</label>
	</div>
</div>
<div class="row">
	<div class="col-xs-12 col-md-12 col-lg-12">
		<table class="table table-hover table-bordered">
            <thead>
                <tr>
                    <th style="background-color: #575757; color: whitesmoke;"><i class="bi bi-hand-index-thumb"></i></th>
                    <th style="background-color: #575757; color: whitesmoke;">Role代號</th>
                </tr>
            </thead>
			<tbody>
				<tr v-for=" item in this.progRoleList ">
					<td style="background-color: #BCC6CC;">
						<div class="form-check">
							<input type="checkbox" class="form-check-input" @change="progRoleEnableChange($event, item.oid)" :checked="checkItemChecked(item.oid)">
						</div>
					</td>
					<td>{{item.role}}</td>
				</tr>		
			</tbody>
		</table>
	</div>
</div>

</template>
