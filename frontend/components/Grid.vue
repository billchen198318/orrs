<script>
import { getProgItem } from './BaseHelper';

export default {
    props: [ 'progId', 'dataSource', 'config' ],
    setup(props) {
    },
    data() {
        return {
            pageProg : null
        }
    },
    methods: {
    },
    created() {
        this.pageProg = getProgItem(this.progId);
        if (null == this.pageProg) {
            //alert('Please close this page, grid load fail!');
            //window.location = "/";
            console.log('error, pageProg null');
        }        
    },
    mounted() {
    } /* ,
    watch : {
        dataSource : {
            deep : true,
            handler() {
                console.log('array updated');
            }
        }
    } */
}
</script>

<template>
<div class="col-xs-12 col-md-12 col-lg-12">
    <table class="table table-hover table-bordered" v-if="this.dataSource != null && this.dataSource.length > 0">
        <thead>
            <tr>
                <th v-for="col in config.column" v-bind:style="'background-color:' + config.theadColor.backgroundColor + '; color:' + config.theadColor.color + '; text-align:' + col.labTextAlign ">
                    <template v-if=" undefined === col.labHtml || !col.labHtml ">{{col.label}}</template>
                    <template v-if=" !(undefined === col.labHtml) && col.labHtml "><span v-html="col.label"></span></template>
                </th>
            </tr>            
        </thead>
        <tbody>
            <tr v-for="row in dataSource">
                <td v-for="col in config.column" v-bind:style="'text-align:' + col.textAlign + ';'">
                    <template v-if="config.keyFieldFormatter.field == col.field">
                        <template v-for="(keyFieldFormatItem, idx) in config.keyFieldFormatter.item">
                            <template v-if="   typeof keyFieldFormatItem.method === 'function'  ">
                                <button v-if="  config.keyFieldFormatter.showTooltip " v-bind:id="'_' + keyFieldFormatItem.type + '_' + row[col.field]" v-bind:class="keyFieldFormatItem.class" @click="keyFieldFormatItem.method(row[col.field])" data-bs-toggle="tooltip" data-bs-placement="bottom" v-bind:data-bs-title="keyFieldFormatItem.memo" data-trigger="hover"><i v-bind:class="'bi bi-' + keyFieldFormatItem.icon "></i></button>
                                <button v-if=" !config.keyFieldFormatter.showTooltip " v-bind:id="'_' + keyFieldFormatItem.type + '_' + row[col.field]" v-bind:class="keyFieldFormatItem.class" @click="keyFieldFormatItem.method(row[col.field])"><i v-bind:class="'bi bi-' + keyFieldFormatItem.icon "></i></button>
                                <template v-if="(idx+1) < config.keyFieldFormatter.item.length">&nbsp;</template>
                            </template>
                            <template v-if=" !(typeof keyFieldFormatItem.method === 'function') ">{{row[col.field]}}</template>
                        </template>
                    </template>
                    <template v-if="config.keyFieldFormatter.field != col.field">
                        <template v-if=" !(undefined === col.colMethod) && typeof col.colMethod === 'function' ">
                            <template v-if=" !(undefined === col.colHtml) && col.colHtml "><span v-html="col.colMethod(row[col.field])"></span></template>
                            <template v-if=" undefined === col.colHtml || !col.colHtml ">{{col.colMethod(row[col.field])}}</template>
                        </template>
                        <template v-if=" undefined === col.colMethod || !(typeof col.colMethod === 'function') ">{{row[col.field]}}</template>
                    </template>
                </td>
            </tr>
        </tbody>
    </table>
</div>
</template>