import { defineStore } from 'pinia';

export const getProg002d0002Store = function() {
    return useProg002d0002Store();
}

export const useProg002d0002Store = defineStore('prog002d0002', {
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