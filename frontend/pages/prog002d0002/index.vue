<script>
import Swal from 'sweetalert2';
import { toast } from 'vue3-toastify';
import 'vue3-toastify/dist/index.css';

import Toolbar from '@/components/Toolbar.vue';
import { PageConstants } from './config';
import { useProg002d0002Store } from './QueryPageStore'; 
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
		const queryPageStore = useProg002d0002Store();
		return {
			queryPageStore
		}
	},
	data() {
		return {
			pleaseSelectId : import.meta.env.VITE_PLEASE_SELECT_ID,
			pleaseSelectText : import.meta.env.VITE_PLEASE_SELECT_LABEL,            
			pageProgramId : PageConstants.QueryId,
            userList : [],
            userRoleList : [],
            userRoleEnableList : [],
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
            this.userRoleList = [];
            this.userRoleEnableList = [];
            this.formParam.oid = '';
		},
		loadUserList : _loadUserList,
        userChange : _userChange,
        userRoleEnableChange : _userRoleEnableChange,
        checkItemChecked : _checkItemChecked
	},
	created() {
	},
	mounted() { 
		this.loadUserList();
        if (this.queryPageStore.queryParam.oid != import.meta.env.VITE_PLEASE_SELECT_ID) {
            this.userChange();
        }
	}
}

function _loadUserList() {
	this.userList = [];
    Swal.fire({title: "Loading...", html: "請等待", showConfirmButton: false, allowOutsideClick: false});
    Swal.showLoading(); 
    let axiosInstance = getAxiosInstance();
    axiosInstance.post(import.meta.env.VITE_API_URL + PageConstants.eventNamespace + '/loadUserList')
    .then(response => {
        Swal.hideLoading();
        Swal.close();
        if (null != response.data) {
            if (import.meta.env.VITE_SUCCESS_FLAG != response.data.success) {
                toast.warning(response.data.message);
                return;
            }
            this.userList = response.data.value;            
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

function _userChange() {
	this.userRoleList = [];
	this.userRoleEnableList = [];
    if (import.meta.env.VITE_PLEASE_SELECT_ID == this.queryPageStore.queryParam.oid) {
        return;
    }
	this.formParam.oid = this.queryPageStore.queryParam.oid;
    Swal.fire({title: "Loading...", html: "請等待", showConfirmButton: false, allowOutsideClick: false});
    Swal.showLoading(); 
    let axiosInstance = getAxiosInstance();
    axiosInstance.post(import.meta.env.VITE_API_URL + PageConstants.eventNamespace + '/findUserRoleListByAccountOid', this.formParam)
    .then(response => {
        Swal.hideLoading();
        Swal.close();
        if (null != response.data) {
            if (import.meta.env.VITE_SUCCESS_FLAG != response.data.success) {
                toast.warning(response.data.message);
                return;
            }
            this.userRoleList = response.data.value.all;      
			this.userRoleEnableList = response.data.value.enable;      
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

function _userRoleEnableChange(e, itemOid) {
	var checked = e.target.checked;
    var appendOid = '';
    for (var n in this.userRoleEnableList) {        
        appendOid += this.userRoleEnableList[n].oid + ',';
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
    axiosInstance.post(import.meta.env.VITE_API_URL + PageConstants.eventNamespace + '/updateUserRole/' + this.formParam.oid + '/' + appendOid)
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
            clearPage();
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
	for (var n in this.userRoleEnableList) {
		if (this.userRoleEnableList[n].oid == itemOid) {
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
        description="帳戶角色Role管理配置." 
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
		<select id="userOid" class="form-select" aria-label="請選取" v-model="this.queryPageStore.queryParam.oid" @change="this.userChange">
			<option :value="this.pleaseSelectId">{{this.pleaseSelectText}}</option>
			<option v-for=" item in this.userList " :value="item.oid">{{item.account}}</option>
		</select>		
        <label for="userOid">使用者帳戶</label>
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
				<tr v-for=" item in this.userRoleList ">
					<td style="background-color: #BCC6CC;">
						<div class="form-check">
							<input type="checkbox" class="form-check-input" @change="userRoleEnableChange($event, item.oid)" :checked="checkItemChecked(item.oid)">
						</div>
					</td>
					<td>{{item.role}}</td>
				</tr>		
			</tbody>
		</table>
	</div>
</div>

</template>
