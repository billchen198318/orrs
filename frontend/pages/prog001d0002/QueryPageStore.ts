import { defineStore } from 'pinia';
import { getInitGridConfigVariable } from '@/components/GridHelper';

export const getProg001d0002Store = function() {
    return useProg001d0002Store();
}

let _gridConfigVar = getInitGridConfigVariable();

export const useProg001d0002Store = defineStore('prog001d0002', {
    state: () => {
        return { 
            gridConfig : _gridConfigVar,
            queryParam : {
                name : '',
                progId : ''
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
            this.queryParam.progId = '';
            this.gridConfig.page = 1;
            this.gridConfig.row = 10;
            this.gridConfig.total = 0;
        }
    },
})