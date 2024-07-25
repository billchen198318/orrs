<script>
import { checkHasRole } from '../components/BaseHelper';

export default {
    setup() {
		definePageMeta({ middleware : ['auth'] });
    },
    data() {
		return {
		}
    },
    mounted() {
		if (checkHasRole('admin') || checkHasRole('*')) {
			this.$router.push( '/dashboard' );
		}
    }
}
</script>
<template>
	<div class="callout callout-info">
		<h4><i class="bi bi-info-circle"></i>&nbsp;Welcome</h4>
		Welcome orrs&nbsp;(Version&nbsp;:&nbsp;0.4)&nbsp;
		<br>
		請點選左邊選單，進行操作。
		<br>
	</div>
	<div class="row">
		<img src="/img/about-bg.jpg" class="img-fluid" alt="qifu4 background image">
	</div>
</template>
