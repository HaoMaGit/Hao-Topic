<script>
import { isLogin } from '@/utils/auth'
export default {
	onLaunch: function () {
		console.log('App Launch')
		isLogin()
	},
	onShow: function () {
		console.log('App Show')

	},
	onHide: function () {
		console.log('App Hide')
	},
	onLoad: function () {

	},
}
</script>

<style>
/*每个页面公共css */
</style>
