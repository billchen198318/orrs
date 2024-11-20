<script>
import { getProgItem } from './BaseHelper';

export default {
    props: [
        'progId', 'description', 
        'marginBottom',
        'refreshFlag', 'refreshMethod', 
        'backFlag', 'backMethod', 
        'createFlag', 'createMethod',
        'saveFlag', 'saveMethod',
        'queryFieldShowSwitchFlag', 'queryFieldShowSwitcMethod'
    ],
    emits: ["refreshMethod", "backMethod", "createMethod", "saveMethod", "queryFieldShowSwitcMethod"],
    setup(props) {
    },    
    data() {
        return { 
            pageProg : null,
            switchEye : true
        }
    },
    methods: {
        fnBack : function() {
            this.$emit('backMethod');
        },        
        fnRefresh : function() {
            this.$emit('refreshMethod');
        },
        fnCreate : function() {
            this.$emit('createMethod');
        },
        fnSave : function() {
            this.$emit('saveMethod');
        },
        fnQueryFieldShowSwitch : function() {
            this.switchEye = !this.switchEye;
            this.$emit('queryFieldShowSwitcMethod');
        }
    },
    created() {
        this.pageProg = getProgItem(this.progId);
        if (null == this.pageProg) {
            //alert('Please close this page, toolbar load fail!');
            //window.location = "/";
            console.log('error, pageProg null');
        }
        //console.log(this.pageProg);
    },
    mounted() {
        /*
        this.pageProg = getProgItem(this.progId);
        if (null == this.pageProg) {
            alert('Please close this page, toolbar load fail!');
        }
        console.log(this.pageProg);
        */
    }
}
</script>

<template>
<div class="app-title" style="background: linear-gradient(to top, #f8f9fa, #ffffff); width: 103vw; overflow: hidden;">
	<div>
		<h1 class="text-dark"><i v-bind:class="'bi bi-'+this.pageProg.fontIconClassId"></i>&nbsp;{{this.pageProg.name}}</h1>
		<p class="text-muted">{{this.description}}</p>
		<div>
            <i v-if=" 'Y' == this.backFlag" id="tb_back" class="fs-5 bi bi-arrow-left-square btn btn-light btn-sm text-info" @click="this.fnBack" data-bs-toggle="tooltip" data-bs-placement="bottom" data-bs-title="上一頁" data-trigger="hover"></i>
            {{ 'Y' == this.backFlag ? '&nbsp;' : '' }}

            <i v-if=" 'Y' == this.refreshFlag" id="tb_repeat" class="fs-5 bi bi-repeat btn btn-light btn-sm text-info" @click="this.fnRefresh" data-bs-toggle="tooltip" data-bs-placement="bottom" data-bs-title="刷新" data-trigger="hover"></i>
            {{ 'Y' == this.refreshFlag ? '&nbsp;' : '' }}

            <i v-if=" 'Y' == this.createFlag " id="tb_plus" class="fs-5 bi bi-plus-circle btn btn-light btn-sm text-info" @click="this.fnCreate" data-bs-toggle="tooltip" data-bs-placement="bottom" data-bs-title="新增頁" data-trigger="hover"></i>
            {{ 'Y' == this.createFlag ? '&nbsp;' : '' }}

            <i class="fs-7 bi bi-three-dots-vertical text-muted" v-if=" 'Y' == this.queryFieldShowSwitchFlag || 'Y' == this.saveFlag " ></i>

            <i v-if=" 'Y' == this.saveFlag " id="tb_save" class="fs-5 bi bi-save btn btn-light btn-sm text-info" @click="this.fnSave" data-bs-toggle="tooltip" data-bs-placement="bottom" data-bs-title="儲存/更新" data-trigger="hover"></i>
            {{ 'Y' == this.saveFlag ? '&nbsp;' : '' }}

            <i v-if=" 'Y' == this.queryFieldShowSwitchFlag " id="tb_queryFieldShowSwitch" v-bind:class="'fs-5 bi bi-' + (this.switchEye ? 'arrow-down-circle' : 'arrow-up-circle') + ' btn btn-light btn-sm '" @click="this.fnQueryFieldShowSwitch" data-bs-toggle="tooltip" data-bs-placement="bottom" data-bs-title="顯示/隱藏查詢區塊" data-trigger="hover"></i>
            {{ 'Y' == this.queryFieldShowSwitchFlag ? '&nbsp;' : '' }}            
		</div>
	</div>    
</div> 
<p v-if=" 'Y' == this.marginBottom " style="margin-bottom: 5px"></p>
</template>