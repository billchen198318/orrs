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
                docmode : 'N',
                simThreshold : 0.70,
                wikimode : 'N'
            },
            reqList : []
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
            this.queryParam.docmode = 'N';
            this.queryParam.simThreshold = 0.70;
            this.queryParam.wikimode = 'N';
            this.reqList = [];
        }
    },
})