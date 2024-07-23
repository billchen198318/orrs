<script>
export default {
    props: [ 'dataSource', 'queryFieldShowFlag' ],
    setup(props) {
    },
    data() {
        return { }
    },
    methods: {
    },
    created() {     
    },
    mounted() {
    }    
}
</script>
<template>
    <div v-show=" !queryFieldShowFlag && (this.dataSource == null || this.dataSource.length < 1) " class="alert alert-info" role="alert">
        隱藏查詢輸入欄，點選上方工具列&nbsp;<i class="bi bi-arrow-up-circle"></i>&nbsp;按鈕，顯示查詢欄方塊!
    </div>    
</template>