<template>
	<div class="row">
		<div class="col-xs-12 col-md-12 col-lg-12">
			<div class="callout callout-info">
				<h4><i class="bi bi-bar-chart"></i>&nbsp;Demo dashboard</h4>
				User usage count
			</div>
		</div>
	</div>
	<div class="row">
		<div class="col-xs-6 col-md-6 col-lg-6 chartSmall">
			<v-chart :option="barOption" autoresize />
		</div>
		<div class="col-xs-6 col-md-6 col-lg-6 chartSmall">
			<v-chart :option="pieOption" autoresize />
		</div>
	</div>
	<div class="row">
		<div class="col-xs-12 col-md-12 col-lg-12 chartAll">
			<v-chart :option="lineOption" autoresize />
		</div>
	</div>
</template>

<script setup>
import { use } from 'echarts/core';
import { CanvasRenderer } from 'echarts/renderers';
import { PieChart, BarChart, LineChart } from 'echarts/charts';
import {
	TitleComponent,
	TooltipComponent,
	LegendComponent,
	GridComponent
} from 'echarts/components';
import VChart, { THEME_KEY } from 'vue-echarts';
import { ref, reactive, provide, onMounted, onUnmounted } from 'vue';

import { 
	getAxiosInstance, 
	getProgItem, 
	getUrlPrefixFromProgItem 
} from '../components/BaseHelper';

definePageMeta({ middleware : ['auth'] });

use([
	CanvasRenderer,
	PieChart,
	BarChart,
	LineChart,
	TitleComponent,
	TooltipComponent,
	LegendComponent,
	GridComponent
]);

//provide(THEME_KEY, 'dark');

let pieOption = { };
let barOption = { };
let lineOption = { };

let refreshChart;

onMounted(()=>{
	loadChart();
	refreshChart = setInterval(function(){ loadChart(); }, 30000);
});

onUnmounted(()=>{
	clearInterval(refreshChart);
});

function loadChart() {
	loadPieAndBarChart();
	loadLineChart();	
}

function loadPieAndBarChart() {
	pieOption.series[0].data = [];
	barOption.xAxis[0].data = [];
	barOption.series[0].data = [];
	var axiosInstance = getAxiosInstance();
	axiosInstance.post(import.meta.env.VITE_API_URL + '/DashboardDemo/findBoardDemo')
	.then(response => {
		if (null != response.data) {
			let dsList = response.data.value;
			for (var i in dsList) {
				barOption.xAxis[0].data.push(dsList[i].userId);
				barOption.series[0].data.push(dsList[i].countSize);
				pieOption.series[0].data.push({
					name : dsList[i].userId,
					value : dsList[i].countSize
				});
			}
		} else {
			alert('error, null');
		}
	})
	.catch(e => {
		alert(e);
	});  
}

function loadLineChart() {
	lineOption.xAxis.data = [];
	lineOption.series[0].data = [];
	var axiosInstance = getAxiosInstance();
	axiosInstance.post(import.meta.env.VITE_API_URL + '/DashboardDemo/findSize')
	.then(response => {
		if (null != response.data) {
			let dsList = response.data.value;
			for (var i in dsList) {
				lineOption.xAxis.data.push(dsList[i].date);
				lineOption.series[0].data.push(dsList[i].countSize);
    		}
		} else {
			alert('error, null');
		}
	})
	.catch(e => {
		alert(e);
	});
}

pieOption = reactive({
	tooltip: {
		trigger: 'item'
	},
	legend: {
		top: '5%',
		left: 'center'
	},
	series: [{
		name: 'User',
		type: 'pie',
		radius: ['40%', '70%'],
		avoidLabelOverlap: false,
		label: {
			show: false,
			position: 'center'
		},
		emphasis: {
			label: {
				show: true,
				fontSize: 40,
				fontWeight: 'bold'
			}
		},
		labelLine: {
			show: false
		},
		data: []
	}]
});

barOption = reactive({
	tooltip: {
		trigger: 'axis',
		axisPointer: {
			type: 'shadow'
		}
	},
	grid: {
		left: '3%',
		right: '4%',
		bottom: '3%',
		containLabel: true
	},
	xAxis: [{
		type: 'category',
		data: [],
		axisTick: {
			alignWithLabel: true
		}
	}],
	yAxis: [{
		type: 'value'
	}],
	series: [{
		name: 'usage count',
		type: 'bar',
		barWidth: '60%',
		data: []
	}]
});

lineOption = reactive({
	title: {
		text: 'Usage count per day'
	},
	tooltip: {
		trigger: 'axis'
	},		
	xAxis: {
		type: 'category',
		data: []
	},
	yAxis: {
		type: 'value'
	},
	series: [{
		data: [],
		type: 'line',
		smooth: true,
		areaStyle: {}
	}]
});

</script>

<style scoped>
.chartSmall {
	width: 40vw;
	height: 45vh;
}
.chartAll {
	width: 80vw;
	height: 45vh;
}
</style>
