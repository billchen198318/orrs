<script>
import "bootstrap/dist/css/bootstrap.css";
import "bootstrap-icons/font/bootstrap-icons.css";
import './assets/vali.css';
import './assets/m.css';
import './assets/callout.scss';

import { watch } from "vue";
import { useBaseStore } from './store/baseStore';
import { 
	getAxiosInstance,
	getAccessTokenCookie, 
	getRefreshTokenCookie, 
	setRefreshAndAccessTokenCookie, 
	checkUserHasLogined, 
	userLogoutClearCookie, 
	checkHasPermission } from './components/BaseHelper';
	
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
		//loadUserLoginedFromClient : _loadUserLoginedFromClient,
		clearUserLoginData        : _clearUserLoginData
	},
	created() {
		// move to auth.global.ts
		//this.loadUserLoginedFromClient();
		
		// move to auth.global.ts
		/*
		this.$router.beforeEach((to, from, next) => {
			let pageUrl = to.path;
			if (pageUrl != '/nopermission' && pageUrl != '/about' && pageUrl != '/' && pageUrl != '/login') {
				if (checkHasPermission(to.path, true)) {
					next();
					return;
				}
			} else {
				next();
				return;
			}
			next({path : '/nopermission', replace : true});
		});
		*/   
	},
	mounted() {
		const nuxtApp = useNuxtApp();
		let bsToolTip = new nuxtApp.$bootstrap.Tooltip(document.body, {
			selector: "[data-bs-toggle='tooltip']",
			trigger : 'hover'
		});    
		document.addEventListener("mousedown", e => {
			if (null != bsToolTip) {
				//bsToolTip.hide(); // no work...
				const el = document.getElementsByClassName("tooltip bs-tooltip-auto fade show");
				if (null != el) {
					for (var n in el) {
						if (null == el[n]) { continue; }
						if (undefined == el[n].id) { continue; }
						if (null == el[n].id) { continue; }
						if (el[n].id.indexOf('tooltip') == 0) {
							el[n].remove();
						}
					}
				}
			}
		});
		
		this.$nextTick(() => {
			const button = document.querySelector('#button');
			const tooltip = document.querySelector('#tooltip');
			createPopper(button, tooltip);
		});
	},
	unmounted() {
		document.removeEventListener("mousedown");
	}
};

// move to auth.global.ts
/*
function _loadUserLoginedFromClient() {
	var ck_user_refresh_token = getRefreshTokenCookie();
	var ck_user_access_token = getAccessTokenCookie();
	let userData = this.baseStore.user;
	if (!checkUserHasLogined(userData) && (null != ck_user_refresh_token && '' != ck_user_refresh_token) && (null != ck_user_access_token && '' != ck_user_access_token)) {
		fetch(import.meta.env.VITE_API_URL + '/auth/validLogined', {
  			method: "POST",
  			headers: {
  				"Content-Type": "application/json",
  			},
  			body: JSON.stringify({
  				accessToken: ck_user_access_token,
  				refreshToken: ck_user_refresh_token
  			}),
  			signal: AbortSignal.timeout(parseInt(import.meta.env.VITE_FETCH_TIMEOUT,10))
		})
		.then(response => {
  			if (response.ok) {
  				return response.json();
  			}
			this.clearUserLoginData();
  			throw new Error(response.status + ' ' + response.statusText);
		})
		.then((responseJson) => {
			if (null != responseJson) {
				if ('' == responseJson.accessToken || '' == responseJson.refreshToken) {
					this.clearUserLoginData();
					return;
				}
				this.baseStore.setUserData(responseJson);
				setRefreshAndAccessTokenCookie(userData.refreshToken, userData.accessToken);
			} else {
				this.clearUserLoginData();
				navigateTo('/login');
			}
		})
		.catch((error) => {
  			this.clearUserLoginData();
  			console.log(error);
		});
	}
}
*/

function _clearUserLoginData() {
	userLogoutClearCookie();
	this.baseStore.clearUserData();  
}

</script>

<style>
</style>

<template>
	<NuxtLayout>
		<NuxtPage/>
	</NuxtLayout>
</template>
