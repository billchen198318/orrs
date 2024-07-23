<script>
import { ref, watch } from 'vue';
import { getProgItem } from './BaseHelper';

export default {
    props: [ 'progId', 'changePageSelectMethod', 'changeGridConfigRowMethod', 'gridConfig' ],
    setup(props) {
    },
    data() {
        return {
            pageProg : null,
            prevPage : 1,
            nexPage : 1,
            lastPage : 1,
            midMaxShow : 3,
            pStart : 0,
            pEnd : 0,
            pItem : [],
            gBtnChangeRow1st : ' ',
            gBtnChangeRow2st : ' ',
            gBtnChangeRow3st : ' ',
            gBtnChangeRow4st : ' ',
        }
    },
    methods: {
        refreshRowActiveFn : function() {
            if (10 == this.gridConfig.row) {
                this.gBtnChangeRow1st = ' active ';
                this.gBtnChangeRow2st = ' ';
                this.gBtnChangeRow3st = ' ';
                this.gBtnChangeRow4st = ' ';
            }
            if (30 == this.gridConfig.row) { 
                this.gBtnChangeRow1st = ' ';
                this.gBtnChangeRow2st = ' active ';
                this.gBtnChangeRow3st = ' ';
                this.gBtnChangeRow4st = ' ';
            }
            if (50 == this.gridConfig.row) { 
                this.gBtnChangeRow1st = ' ';
                this.gBtnChangeRow2st = ' ';
                this.gBtnChangeRow3st = ' active ';
                this.gBtnChangeRow4st = ' ';
            }
            if (100 == this.gridConfig.row) { 
                this.gBtnChangeRow1st = ' ';
                this.gBtnChangeRow2st = ' ';
                this.gBtnChangeRow3st = ' ';
                this.gBtnChangeRow4st = ' active ';
            }            
        },
        refreshPageNumFn : function() {
            this.pItem = [];
            if (this.gridConfig.total > 0) {
                this.prevPage = this.gridConfig.page - 1;
                this.nexPage = this.gridConfig.page + 1;
                this.lastPage = parseInt( this.gridConfig.total / this.gridConfig.row );
                if (parseInt(this.gridConfig.total % this.gridConfig.row) != 0) {
                    this.lastPage += 1;
                }
                if (this.gridConfig.page > this.lastPage) {
                    this.gridConfig.page = this.lastPage;
                    this.prevPage = this.gridConfig.page - 1;
                    this.nexPage = this.gridConfig.page + 1;
                }
                if (this.gridConfig.page < 1) {
                    this.gridConfig.page = 1;
                    this.prevPage = 1;
                    this.nexPage = 1;
                }
                if (this.prevPage < 1) {
                    this.prevPage = 1;
                }
                if (this.nexPage > this.lastPage) {
                    this.nexPage = this.lastPage;
                }
                this.pStart = this.gridConfig.page - this.midMaxShow;
                this.pEnd = this.gridConfig.page + this.midMaxShow;
                if (this.pStart < 1) { 
                    this.pStart = 1;
                }
                if (this.pEnd > this.lastPage) {
                    this.pEnd = this.lastPage;
                }
                for (let i = this.pStart; i <= this.pEnd; i++) {
                    this.pItem.push(i);
                }
            }            
        }
    },
    created() {
        this.pageProg = getProgItem(this.progId);
        if (null == this.pageProg) {
            //alert('Please close this page, GridPagination load fail!');
            //window.location = "/";
            console.log('error, pageProg null');
        }
        this.refreshRowActiveFn();
        this.refreshPageNumFn();
        watch(() => this.gridConfig.row, (newVal, oldVal) => {
            this.refreshRowActiveFn();
            this.refreshPageNumFn();
        });
        watch(() => this.gridConfig.page, (newVal, oldVal) => {
            this.refreshPageNumFn();
        });
        watch(() => this.gridConfig.total, (newVal, oldVal) => {
            this.refreshPageNumFn();
        });
    },
    mounted() {
    }
}

</script>
<template>
<div class="row">
    <div class="col-xs-12 col-md-12 col-lg-12">
        <table v-if=" this.gridConfig != null && this.gridConfig.total > 0 " width="100%" border="0" cellspacing="0" cellpadding="1" >
            <tr>
                <td  width="70%" align="left" valign="middle">
                    <nav aria-label="Page navigation">
                        <ul class="pagination">
                            <li class="page-item" @click="this.changePageSelectMethod(1)">
                                <a class="page-link" href="#">
                                    <span aria-hidden="true">&laquo;</span>
                                </a>
                            </li>
                            <li class="page-item" @click="this.changePageSelectMethod(this.prevPage)">
                                <a class="page-link" href="#">
                                    <span aria-hidden="true">&lsaquo;</span>
                                </a>
                            </li>
                            <li v-for="pNum in pItem" v-bind:class="'page-item' + (pNum == this.gridConfig.page ? ' active ' : ' ')" @click="this.changePageSelectMethod(pNum)"><a class="page-link" href="#">{{pNum}}</a></li>
                            <li class="page-item" @click="this.changePageSelectMethod(this.nexPage)">
                                <a class="page-link" href="#">
                                    <span aria-hidden="true">&rsaquo;</span>
                                </a>
                            </li>
                            <li class="page-item" @click="this.changePageSelectMethod(this.lastPage)">
                                <a class="page-link" href="#">
                                    <span aria-hidden="true">&raquo;</span>
                                </a>
                            </li>
                        </ul> 
                    </nav>       	      
	            </td>   
                <td width="30%" align="right" valign="middle">
                    <table width="100%" border="0" cellspacing="0" cellpadding="1" >
                        <tr>
                            <td align="right">
                                <div class="btn-group me-2" role="group" aria-label="Second group">
                                    <button type="button" v-bind:class="'btn btn-secondary ' + this.gBtnChangeRow1st" @click="changeGridConfigRowMethod(10)">10</button>
                                    <button type="button" v-bind:class="'btn btn-secondary ' + this.gBtnChangeRow2st" @click="changeGridConfigRowMethod(30)">30</button>
                                    <button type="button" v-bind:class="'btn btn-secondary ' + this.gBtnChangeRow3st" @click="changeGridConfigRowMethod(50)">50</button>
                                    <button type="button" v-bind:class="'btn btn-secondary ' + this.gBtnChangeRow4st" @click="changeGridConfigRowMethod(100)">100</button>
                                </div>
                            </td>
                            <td align="left">
                                <h6>
                                    Total
                                    <span class="badge text-bg-info">{{this.gridConfig.total}}</span>
                                </h6>
                            </td>
                        </tr>
                    </table>
                </td>             
            </tr>
        </table>
    </div>
</div>
</template>