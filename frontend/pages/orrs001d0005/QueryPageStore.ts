import { defineStore } from 'pinia';

export const getOrrs001d0005Store = function() {
    return useOrrs001d0005Store();
}

export const useOrrs001d0005Store = defineStore('orrs001d0005', {
    state: () => {
        return { 
            queryParam : {
                model : 'gemma2',
                system : '',
                message : '',
                similarityThreshold : 0.0
            }
        }
    },
    actions: {
        setQueryParam(qJsonRes:any) {
            this.queryParam = qJsonRes;
        },      
        clearData() {
            this.queryParam.model = 'gemma2';
            this.queryParam.system = '';
            this.queryParam.message = '';
            this.queryParam.similarityThreshold = 0.0;
        }
    },
})