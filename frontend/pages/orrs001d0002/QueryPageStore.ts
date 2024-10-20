import { defineStore } from 'pinia';
import { getInitGridConfigVariable } from '@/components/GridHelper';

export const getOrrs001d0002Store = function() {
    return useOrrs001d0002Store();
}

let _gridConfigVar = getInitGridConfigVariable();

export const useOrrs001d0002Store = defineStore('orrs001d0002', {
    state: () => {
        return { 
            gridConfig : _gridConfigVar,
            queryParam : {
                name : '',
                taskId : ''
            }
        }
    },
    actions: {
        setQueryParam(qJsonRes:any) {
            this.queryParam = qJsonRes;
        },
        setGridConfig(gJsonRes:any) {
            this.gridConfig = gJsonRes;
        },        
        clearData() {
            this.queryParam.name = '';
            this.queryParam.taskId = '';
            this.gridConfig.page = 1;
            this.gridConfig.row = 10;
            this.gridConfig.total = 0;
        }
    },
})