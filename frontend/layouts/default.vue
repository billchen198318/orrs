<script>
import { watch } from "vue";
import { useBaseStore } from '../store/baseStore';
import { 
	getAxiosInstance,
	getAccessTokenCookie, 
	getRefreshTokenCookie, 
	setRefreshAndAccessTokenCookie, 
	checkUserHasLogined, 
	userLogoutClearCookie, 
	checkHasPermission } from '../components/BaseHelper';

import { createPopper } from '@popperjs/core';
import Swal from 'sweetalert2';

export default {
	setup() {
		const baseStore = useBaseStore();
		return { 
			baseStore
		}
	},
	data() {
		return { }
	},
	methods: {
		initTreeMenu              : _initTreeMenu,
		jsTreeMenuInit            : _jsTreeMenuInit,
		clearUserLoginData        : _clearUserLoginData,
		userLogout                : _userLogout,
		reloadPageLogoClick       : function() {
			navigateTo('/');
		},
		showUserProfile           : function() {
			var str = 'User Id: ' + this.baseStore.user.id + '<br>Administrator: ' + this.baseStore.user.admin;
			str += '<br>Role: [ ';
			for (var r in this.baseStore.user.roleList) {
				str += this.baseStore.user.roleList[r].role + ',';
			}
			str += ' ]';
			Swal.fire({
				title: "User info",
				html: str,
				icon: "info"
			});     
		}
	},
	created() {
		watch(() => this.baseStore.user.login, (newVal, oldVal) => {
			if ('Y' == newVal) {
				this.initTreeMenu();
			} else {
				window.location.href = '/login';
			}
		});
		watch(() => this.baseStore.user.progList, (newVal, oldVal) => {
			if (oldVal.length != newVal.length && newVal.length > 0) {
				this.initTreeMenu();
			}
		});   
	},
	mounted() {
		this.initTreeMenu();
	},
	unmounted() {
	}
};

function _initTreeMenu() {
	var that = this;
	let userData = this.baseStore.user;
	let menuData = this.baseStore.menuList;
	if (menuData != null && menuData.length > 0) {
		setTimeout(() => {
			try {
				that.jsTreeMenuInit();
			} catch (ex) {
				console.log(ex);
				//window.location.href = '/';
			}
		}, 250);
		return;
	}
	
	if (!checkUserHasLogined(userData)) {
		return;
	}
	
	if (this.baseStore.programList == null || this.baseStore.programList.length < 1) {
		const axiosInstance = getAxiosInstance();
		axiosInstance.post(import.meta.env.VITE_API_URL + '/menu/getMemuItemAndProgList')
		.then(response => {
			if (null != response.data.value) {
				this.baseStore.menuList = response.data.value.menuList;
				this.baseStore.progList = response.data.value.programList;
				setTimeout(() => { that.jsTreeMenuInit(); }, 250);
			}
		})
		.catch(e => {
			alert(e);
			//this.clearUserLoginData();
		});
	}
	
}

function _jsTreeMenuInit() {
	var treeviewMenu = document.querySelector('.app-menu');
	
	// Toggle Sidebar
	document.querySelector('[data-toggle="sidebar"]').addEventListener('click', function(event) {
		event.preventDefault();
		document.querySelector('.app').classList.toggle('sidenav-toggled');
	});
	
	// Activate sidebar treeview toggle
	var treeviewToggle = document.querySelectorAll("[data-toggle='treeview']");
	treeviewToggle.forEach(function(toggle) {
		toggle.addEventListener('click', function(event) {
			event.preventDefault();
			var parent = toggle.parentNode;
			if (!parent.classList.contains('is-expanded')) {
				var expandedToggles = treeviewMenu.querySelectorAll("[data-toggle='treeview']");
				expandedToggles.forEach(function(expandedToggle) {
					expandedToggle.parentNode.classList.remove('is-expanded');
				});
			}
			parent.classList.toggle('is-expanded');
		});
	});
	
	// Set initial active toggle
	var initialActiveToggle = document.querySelector("[data-toggle='treeview.'].is-expanded");
	if (initialActiveToggle) {
		initialActiveToggle.parentNode.classList.toggle('is-expanded');
	}
}

function _userLogout() {
  	Swal.fire({
  		title: '確定登出?',
  		icon: 'question',
  		iconHtml: '?',
  		confirmButtonText: 'Yes',
  		cancelButtonText: 'No',
  		showCancelButton: true,
  		showCloseButton: true
  	}).then((result) => {
  		if (result.isConfirmed) {
  			this.clearUserLoginData();
			//window.location.href = '/login'; // move to watch
  		}
  	});
}

function _clearUserLoginData() {
	userLogoutClearCookie();
	this.baseStore.clearUserData();  
}

</script>
<template>  
	<head>
		<title>orrs</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />
		<meta charset="UTF-8" />
	</head>  
	<div v-if=" 'Y' == this.baseStore.user.login " class="app sidebar-mini">
		<!-- Navbar-->
        <header class="app-header"><a class="app-header__logo" href="#" v-on:click="this.reloadPageLogoClick">orrs</a>
			<!-- Sidebar toggle button-->
			<a class="app-sidebar__toggle" href="#" data-toggle="sidebar" aria-label="Hide Sidebar"><h4 style="margin-top: 10px;"><i class="bi bi-list"></i></h4></a>
			<!-- Navbar Right Menu-->
			<ul class="app-nav">
				<!-- User Menu-->
				<li class="dropdown"><a class="app-nav__item" href="#" data-bs-toggle="dropdown" aria-label="Open Profile Menu"><i class="bi bi-person fs-4"></i></a>
					<ul class="dropdown-menu settings-menu dropdown-menu-right">
						<li><a class="dropdown-item" href="#" @click="this.showUserProfile"><i class="bi bi-person-badge me-2 fs-5"></i> Profile</a></li>
						<li><a class="dropdown-item" href="#" @click="this.userLogout"><i class="bi bi-box-arrow-right me-2 fs-5"></i> Logout</a></li>
					</ul>
				</li>
			</ul>
        </header>
        
		<!-- Sidebar menu-->
        <div class="app-sidebar__overlay" data-toggle="sidebar"></div>
        <aside class="app-sidebar">
			<ul class="app-menu">
				
				<!-- menu item -->
				<template v-if="null != this.baseStore && null != this.baseStore.menuList && this.baseStore.menuList.length > 0">
				<li class="treeview" v-for=" (t, idx) in this.baseStore.menuList ">
					<a class="app-menu__item" href="#" data-toggle="treeview"><i :class="'bi bi-' + t.icon"></i>&nbsp;<span class="app-menu__label">{{t.name}}</span><i class="treeview-indicator fa fa-angle-right"></i></a>
					<ul class="treeview-menu">
						<li v-for="(mdi, iidx) in t.items">
							<router-link v-bind:to=" (mdi.url.length > 0 && mdi.url.indexOf('#') == 0) ? mdi.url.substring(1, mdi.url.length) : mdi.url " class="treeview-item"><i :class="'bi bi-' + mdi.icon"></i>&nbsp;&nbsp;{{mdi.name}}</router-link>
						</li>
					</ul>
				</li>
				</template>
				
                <li><router-link to="/about" class="app-menu__item"><i class="bi bi-info-circle"></i>&nbsp;<span class="app-menu__label">About</span></router-link></li>
    
				<li><a class="app-menu__item" href="#" @click="this.userLogout()" ><i class="bi bi-door-open"></i>&nbsp;<span class="app-menu__label">Logout</span></a></li>
			</ul>
		</aside>
		
		<main class="app-content">      
			<div class="tab-content">
				<slot/>
			</div>
		</main>
		
	</div>
</template>