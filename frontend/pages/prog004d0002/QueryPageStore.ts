import { defineStore } from 'pinia';
import { getInitGridConfigVariable } from '@/components/GridHelper';

export const getProg004d0002Store = function() {
    return useProg004d0002Store();
}

let _gridConfigVar = getInitGridConfigVariable();

export const useProg004d0002Store = defineStore('prog004d0002', {
    state: () => {
        return { 
            gridConfig : _gridConfigVar,
            queryParam : {
                userId : ''
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
            this.queryParam.userId = '';
            this.gridConfig.page = 1;
            this.gridConfig.row = 10;
            this.gridConfig.total = 0;
        }
    },
})