import { defineStore } from 'pinia';

export const getProg002d0003Store = function() {
    return useProg002d0003Store();
}

export const useProg002d0003Store = defineStore('prog002d0003', {
    state: () => {
        return { 
            queryParam : {
                oid : import.meta.env.VITE_PLEASE_SELECT_ID
            }
        }
    },
    actions: {
        setQueryParam(qJsonRes:any) {
            this.queryParam = qJsonRes;
        },
        clearData() {
            this.queryParam.oid = import.meta.env.VITE_PLEASE_SELECT_ID;
        }
    },
})