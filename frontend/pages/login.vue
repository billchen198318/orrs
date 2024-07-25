<script>
import { useBaseStore } from '../store/baseStore'
import Swal from 'sweetalert2';
import { toast } from 'vue3-toastify';
import 'vue3-toastify/dist/index.css';

import { setRefreshAndAccessTokenCookie, userLogoutClearCookie } from '../components/BaseHelper';

export default {
    setup(props) {
        definePageMeta({ layout : 'blank' });
        const baseStore = useBaseStore();
        return {
            baseStore
        }
    },    
    data() {
        return {
            userId  :   '',
            passwd  :   '',
            message :   ''
        }
    },
    methods: {
        loginBtnClick : function() {
            if (this.userId.replace(/\s/g, '') == '' || this.passwd.replace(/\s/g, '') == '') {
                toast.warn('請輸入帳戶與密碼!');
                return;
            }
            Swal.fire({
                title: "Loading...",
                html: "請等待",
                showConfirmButton: false,
                allowOutsideClick: false
            });
            Swal.showLoading();

            this.message = '';
            fetch(import.meta.env.VITE_API_URL + '/auth/signin', {
                method: "POST",
		        headers: {
			        "Content-Type": "application/json",
		        },
		        body: JSON.stringify({
			        username: this.userId,
			        password: this.passwd
		        }),
		        signal: AbortSignal.timeout(import.meta.env.VITE_FETCH_TIMEOUT)
	        })
	        .then(response => {
		        Swal.hideLoading();
		        Swal.close();
		        if (response.ok) {
			        return response.json();
		        }
		        throw new Error(response.status + ' ' + response.statusText);
	        })
	        .then((responseJson) => {
                var that = this;
		        if (null != responseJson) {
                    this.baseStore.setUserData(responseJson);
                    setRefreshAndAccessTokenCookie(responseJson.refreshToken, responseJson.accessToken);               
                    that.$router.push('/');
		        } else {
                    this.baseStore.clearUserData();
                    userLogoutClearCookie();
		        }
	        })
	        .catch((error) => {
                userLogoutClearCookie();
		        //console.log(error);
                this.message = error;
	        	this.userId = '';
                this.passwd = '';
		        Swal.hideLoading();
		        Swal.close();
	        });
        }
    },
    mounted() {
    }
}
</script>
<template>
<div class="login-body">
    <section class="login-content">
        <div class="login-box">
			<div class="login-form">
				<h3 class="login-head">
					<!-- <img src="resource/logo.svg" width="250px" style="margin-bottom:10px" /> -->
					<i class="bi bi-person-circle"></i>
					orrs - frontend
				</h3>
                <div class="form-group form-floating">
                    <input type="text" class="form-control" id="username" placeholder="Account" v-model="this.userId">
                    <label for="username">請輸入帳戶</label>
                </div>
                <p style="margin-bottom: 5px"></p>
                <div class="form-group form-floating">
					<input class="form-control" type="password" name="password" id="password" placeholder="Password" v-model="this.passwd" />
                    <label for="password">請輸入密碼</label>
                </div>
                <p style="margin-bottom: 10px"></p>
                <div class="form-group">
                    <button type="button" class="btn btn-primary col-12" v-on:click="loginBtnClick"><i class="bi bi-box-arrow-in-right"></i>&nbsp;登入</button>
                </div>
                <p style="margin-bottom: 5px"></p>
                <div v-if=" this.message != null && '' != this.message ">
                    <h6><span class="badge bg-danger">{{this.message}}</span></h6>
                </div>
            </div>
        </div>
    </section>
</div>                
</template>