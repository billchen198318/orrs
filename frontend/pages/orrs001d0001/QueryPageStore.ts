import { defineStore } from 'pinia';
import { getInitGridConfigVariable } from '@/components/GridHelper';

export const getOrrs001d0001Store = function() {
    return useOrrs001d0001Store();
}

let _gridConfigVar = getInitGridConfigVariable();

export const useOrrs001d0001Store = defineStore('orrs001d0001', {
    state: () => {
        return { 
            gridConfig : _gridConfigVar,
            queryParam : {
                name : '',
                cmdId : ''
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
            this.queryParam.cmdId = '';
            this.gridConfig.page = 1;
            this.gridConfig.row = 10;
            this.gridConfig.total = 0;
        }
    },
})