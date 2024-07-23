import { defineStore } from 'pinia';
import { getInitGridConfigVariable } from '@/components/GridHelper';

export const getProg002d0001Store = function() {
    return useProg002d0001Store();
}

let _gridConfigVar = getInitGridConfigVariable();

export const useProg002d0001Store = defineStore('prog002d0001', {
    state: () => {
        return { 
            gridConfig : _gridConfigVar,
            queryParam : {
                role : ''
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
            this.queryParam.role = '';
            this.gridConfig.page = 1;
            this.gridConfig.row = 10;
            this.gridConfig.total = 0;
        }
    },
})