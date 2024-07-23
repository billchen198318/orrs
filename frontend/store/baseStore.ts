import { defineStore } from 'pinia'

export const getBaseStore = function() {
    return useBaseStore();
}

export const useBaseStore = defineStore('baseStore', {
    state: () => {
        return { 
            user : {
                id : '',
                login : 'N',
                admin : 'N',
                accessToken     : '',
                refreshToken    : '',                
                roleList : [],
                permList : []
            },
            menuList : [],
            progList : []
        }
    },
    actions: {
        setUserData(uJsonRes:any) {
            this.user.id = uJsonRes.userId;
            this.user.login = 'Y';
            this.user.admin = 'N';
            if (uJsonRes.admin) {
                this.user.admin = 'Y';
            }
            this.user.roleList = uJsonRes.roles;
            this.user.permList = uJsonRes.authorities;
            this.user.accessToken = uJsonRes.accessToken;
            this.user.refreshToken = uJsonRes.refreshToken;
        },
        setMenuList(v:any) {
            this.menuList = v;
        },
        setProgList(v:any) {
            this.progList = v;
        },        
        setLoginStatus(v:any) {
            this.user.login = v;
        },
        clearUserData() {
            this.user.id = '';
            this.user.login = '';
            this.user.admin = 'N';
            this.user.roleList = [];
            this.user.permList = [];
            this.user.accessToken = '';
            this.user.refreshToken = '';
            this.menuList = [];
            this.progList = [];
        }
    },
})