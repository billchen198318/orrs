<script>
import Swal from 'sweetalert2';
import { toast } from 'vue3-toastify';
import 'vue3-toastify/dist/index.css';

import Toolbar from '@/components/Toolbar.vue';
import { PageConstants } from './config';
import { useProg001d0003Store } from './QueryPageStore'; 
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
		const queryPageStore = useProg001d0003Store();
		return {
			queryPageStore
		}
	},
	data() {
		return {
			pleaseSelectId : import.meta.env.VITE_PLEASE_SELECT_ID,
			pleaseSelectText : import.meta.env.VITE_PLEASE_SELECT_LABEL,
			pageProgramId : PageConstants.QueryId,
			itemAllList : [],
			itemEnableList : [],
			folderList : []
		}
	},
	methods: { 
		tbRefresh : function() {
			this.clearPage();
			this.loadProgramFolder();
		},
		clearPage : function() {
			this.queryPageStore.queryParam.folderOid = import.meta.env.VITE_PLEASE_SELECT_ID;
			this.itemAllList = [];
			this.itemEnableList = [];
		},
		loadProgramFolder : _loadProgramFolder,
		programFolderChange : _programFolderChange,
		programItemEnableChange : _programItemEnableChange,
		checkItemChecked : _checkItemChecked,
		checkItemDisable : _checkItemDisable
	},
	created() {
	},
	mounted() { 
		this.tbRefresh();
	}
}

function _loadProgramFolder() {
    this.folderList = [];
    Swal.fire({title: "Loading...", html: "請等待", showConfirmButton: false, allowOutsideClick: false});
    Swal.showLoading(); 
    let axiosInstance = getAxiosInstance();
    axiosInstance.post(import.meta.env.VITE_API_URL + PageConstants.eventNamespace + '/loadProgramFolder')
    .then(response => {
        Swal.hideLoading();
        Swal.close();
        if (null != response.data) {
            if (import.meta.env.VITE_SUCCESS_FLAG != response.data.success) {
                toast.warning(response.data.message);
                return;
            }
            this.folderList = response.data.value;            
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

function _programFolderChange() {
    this.itemAllList = [];
    this.itemEnableList = [];    
    if (import.meta.env.VITE_PLEASE_SELECT_ID == this.queryPageStore.queryParam.folderOid) {
        return;
    }
    Swal.fire({title: "Loading...", html: "請等待", showConfirmButton: false, allowOutsideClick: false});
    Swal.showLoading(); 
    let axiosInstance = getAxiosInstance();
    axiosInstance.post(import.meta.env.VITE_API_URL + PageConstants.eventNamespace + '/loadProgramEnableAndAllList/' + this.queryPageStore.queryParam.folderOid)
    .then(response => {
        Swal.hideLoading();
        Swal.close();
        if (null != response.data) {
            if (import.meta.env.VITE_SUCCESS_FLAG != response.data.success) {
                toast.warning(response.data.message);
                return;
            }
            this.itemAllList = response.data.value.all;
            this.itemEnableList = response.data.value.enable;
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

function _programItemEnableChange(e, itemOid) {
    var checked = e.target.checked;
    var appendOid = '';
    for (var n in this.itemEnableList) {        
        appendOid += this.itemEnableList[n].oid + ',';
    }
    if (checked) {
        appendOid += itemOid + ',';
    } else {
        appendOid = appendOid.replaceAll(itemOid+',', '');
    }
	if ('' == appendOid) {
		appendOid = ',';
	}    
    Swal.fire({title: "Loading...", html: "請等待", showConfirmButton: false, allowOutsideClick: false});
    Swal.showLoading(); 
    let axiosInstance = getAxiosInstance();
    axiosInstance.post(import.meta.env.VITE_API_URL + PageConstants.eventNamespace + '/updateMenu/' + this.queryPageStore.queryParam.folderOid + '/' + appendOid)
    .then(response => {
        Swal.hideLoading();
        Swal.close();
        if (null != response.data) {
            if (import.meta.env.VITE_SUCCESS_FLAG != response.data.success) {
                toast.warning(response.data.message);
                clearPage();
            } else {
				toast.success(response.data.message);
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
        clearPage();
    });
}

function _checkItemDisable(itemOid) {
    var f = false;
    for (var n in this.itemAllList) {
        if (this.itemAllList[n].oid == itemOid && this.itemAllList[n].itemType == 'FOLDER') {
            f = true;
        }
    }
    return f;    
}

function _checkItemChecked(itemOid) {
    var f = false;
    for (var n in this.itemEnableList) {
        if (this.itemEnableList[n].oid == itemOid) {
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
        description="選單配置." 
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
		<select id="folderOid" class="form-select" aria-label="請選取" v-model="this.queryPageStore.queryParam.folderOid" @change="this.programFolderChange">
			<option :value="this.pleaseSelectId">{{this.pleaseSelectText}}</option>
			<option v-for=" item in this.folderList " :value="item.oid">{{item.progId}} - {{item.name}}</option>
		</select>		
        <label for="folderOid">程式目錄</label>
	</div>
</div>
<div class="row">
	<div class="col-xs-12 col-md-12 col-lg-12">
		<table class="table table-hover table-bordered">
            <thead>
                <tr>
                    <th style="background-color: #575757; color: whitesmoke;"><i class="bi bi-hand-index-thumb"></i></th>
                    <th style="background-color: #575757; color: whitesmoke;">頁面程式代號</th>
                    <th style="background-color: #575757; color: whitesmoke;">頁面程式名稱</th>
                </tr>
            </thead>
			<tbody>
				<template v-for="item in this.itemAllList">
					<template v-if="item.itemType != 'FOLDER'">
				<tr>
					<td style="background-color: #BCC6CC;">
						<div class="form-check">
							<input type="checkbox" class="form-check-input" @change="programItemEnableChange($event, item.oid)" :checked="checkItemChecked(item.oid)" :disabled="checkItemDisable(item.oid)">
						</div>
					</td>
					<td>{{item.progId}}</td>
					<td><i v-bind:class="'bi bi-' + item.fontIconClassId"></i>&nbsp;{{item.name}}</td>
				</tr>		
					</template>
				</template>
			</tbody>
		</table>
	</div>
</div>

</template>
