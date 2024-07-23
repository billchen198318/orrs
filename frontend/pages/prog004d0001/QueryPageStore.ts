import { defineStore } from 'pinia';
import { getInitGridConfigVariable } from '@/components/GridHelper';

export const getProg004d0001Store = function() {
    return useProg004d0001Store();
}

let _gridConfigVar = getInitGridConfigVariable();

export const useProg004d0001Store = defineStore('prog004d0001', {
    state: () => {
        return { 
            gridConfig : _gridConfigVar,
            queryParam : {
                user : ''
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
            this.queryParam.user = '';
            this.gridConfig.page = 1;
            this.gridConfig.row = 10;
            this.gridConfig.total = 0;
        }
    },
})