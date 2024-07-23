import { defineStore } from 'pinia';

export const getProg001d0003Store = function() {
    return useProg001d0003Store();
}

export const useProg001d0003Store = defineStore('prog001d0003', {
    state: () => {
        return { 
            queryParam : {
                folderOid : import.meta.env.VITE_PLEASE_SELECT_ID
            }
        }
    },
    actions: {
        setQueryParam(qJsonRes:any) {
            this.queryParam = qJsonRes;
        },
        clearData() {
            this.queryParam.folderOid = import.meta.env.VITE_PLEASE_SELECT_ID;
        }
    },
})