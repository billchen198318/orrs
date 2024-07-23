import { defineStore } from 'pinia';
import { getInitGridConfigVariable } from '@/components/GridHelper';

export const getProg001d0004Store = function() {
    return useProg001d0004Store();
}

let _gridConfigVar = getInitGridConfigVariable();

export const useProg001d0004Store = defineStore('prog001d0004', {
    state: () => {
        return { 
            gridConfig : _gridConfigVar,
            queryParam : {
                templateId : '',
                titleLike : ''
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
            this.queryParam.templateId = '';
            this.queryParam.titleLike = '';
            this.gridConfig.page = 1;
            this.gridConfig.row = 10;
            this.gridConfig.total = 0;
        }
    },
})