import { defineStore } from 'pinia';

export const getOrrs001d0003Store = function() {
    return useOrrs001d0003Store();
}

export const useOrrs001d0003Store = defineStore('orrs001d0003', {
    state: () => {
        return { 
            queryParam : {
                processIdLike : '',
                taskId : '',
                lastCmd : true,
                date1 : '',
                date2 : ''
            }
        }
    },
    actions: {
        setQueryParam(qJsonRes:any) {
            this.queryParam = qJsonRes;
        },     
        clearData() {
            this.queryParam.processIdLike = '';
            this.queryParam.taskId = '';
            this.queryParam.lastCmd = true;
            this.queryParam.date1 = '';
            this.queryParam.date2 = '';
        }
    },
})