import { defineStore } from 'pinia';
import { getInitGridConfigVariable } from '@/components/GridHelper';

export const getProg001d0001Store = function() {
    return useProg001d0001Store();
}

let _gridConfigVar = getInitGridConfigVariable();

export const useProg001d0001Store = defineStore('prog001d0001', {
    state: () => {
        return { 
            gridConfig : _gridConfigVar,
            queryParam : {
                name : '',
                sysId : ''
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
            this.queryParam.sysId = '';
            this.gridConfig.page = 1;
            this.gridConfig.row = 10;
            this.gridConfig.total = 0;
        }
    },
})