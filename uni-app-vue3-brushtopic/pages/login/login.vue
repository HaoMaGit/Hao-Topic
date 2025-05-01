<script setup>
	import {
		onUnmounted,
		ref
	} from 'vue'
	import {
		toast
	} from '../../uni_modules/uv-ui-tools/libs/function';
	// 登录方式 0账号登录 1手机登录 2邮箱登录 
	const loginWay = ref(0)
	const phoneValue = ref('') // 手机号
	const qqValue = ref('') // qq
	const userValue = ref('') // 账户
	const password = ref('') // 密码
	const totalSecond = ref(60) // 总秒数
	const second = ref(60) // 倒计时的秒数
	const forgetPopup = ref() // 忘记密码弹出层
	// 输入的验证码
	const codeValue = ref('')
	let timer = null // 定时器 id
	// 获取验证码
	const getCode = () => {
		// 如果定时器不存在并且秒数等于总秒数，则开启倒计时
		if (!timer && second.value === totalSecond.value) {
			console.log('开始倒计时');
			// 开启倒计时
			timer = setInterval(() => {
				second.value--;
				// 如果归0了停止定时器
				if (second.value <= 0) {
					clearInterval(timer);
					timer = null; // 重置定时器 id
					second.value = totalSecond.value; // 归位
				}
			}, 1000);
			// 发送请求，获取验证码
			// const res = await getCode({
			// 	phone: formData.value.phoneNumber
			// })
			toast('验证码已发送')
			// let codeTimer = setTimeout(() => {
			// 	formData.value.code = res.data
			// }, 2000)
		}
	}
	// 页面销毁清除定时器
	onUnmounted(() => {
		clearInterval(timer)
	})
	// 点击了关闭
	const ceshi = () => {
		console.log('点击了关闭');
	}
</script>
<template>
	<view class="login-content">
		<!-- 忘记密码弹出层 -->
		<uni-popup :mask-click="false" ref="forgetPopup" style="text-align: center;" background-color="#fff"
			type="bottom" border-radius="10px 10px 0 0">
			<view class="title-box" style="margin-top: 18rpx;color: #bbb;margin-bottom: 20rpx;">
				<text style="font-size: 25rpx;">忘记密码</text>
			</view>
			<uni-list>
				<uni-list-item title="手机号验证" clickable></uni-list-item>
				<uni-list-item title="邮箱验证" clickable></uni-list-item>
				<view class="placeholder" style="width: 100%;height: 25rpx;background-color: #f7f7f7;"></view>
				<uni-list-item title="取消" @click="forgetPopup.close()" clickable></uni-list-item>
			</uni-list>
		</uni-popup>
		<!-- 内部容器 -->
		<view class="content-box">
			<h3 class="welcome">{{ loginWay === 1 ? '手机快捷登录' : (loginWay === 2 ? 'QQ邮箱登录' : '欢迎登录') }}</h3>
			<text class="register">未注册账号将自动注册</text>
			<!-- 输入框 -->
			<view class="input-box">
				<text class="hint">{{ loginWay === 1 ? '手机号' : (loginWay === 2 ? '邮箱' : '账号') }}</text>
				<uv-input v-if="loginWay === 0" placeholder="请输入手机号或者邮箱" v-model="userValue"
					style="margin-bottom: 28rpx;margin-top: 15rpx;"></uv-input>
				<uv-input v-if="loginWay === 1" placeholder="请输入手机号" v-model="phoneValue"
					style="margin-bottom: 28rpx;margin-top: 15rpx;"></uv-input>
				<uv-input v-if="loginWay === 2" placeholder="请输入邮箱" v-model="qqValue"
					style="margin-bottom: 28rpx;margin-top: 15rpx;"></uv-input>

				<text class="hint">{{loginWay !== 1 ? '密码' : '验证码'}}</text>
				<uv-input v-if="loginWay !== 1" placeholder="请输入密码" v-model="phoneValue"
					style="margin-bottom: 28rpx;margin-top: 15rpx;"></uv-input>
				<uv-input v-if="loginWay === 1" placeholder="请输入短信验证码" style="margin-top: 15rpx;" v-model="codeValue"
					maxlength="6">
					<!-- vue3模式下必须使用v-slot:suffix -->
					<template v-slot:suffix>
						<text :class="{'code-style': true, 'get-style' : phoneValue?.length === 11}" @click="getCode()">
							{{ second === totalSecond ? '获取验证码' :
          `重新获取${second}秒` }}</text>
					</template>
				</uv-input>

				<!-- 登录按钮 -->
				<view class="loginBtn">
					<text class="login">登陆</text>
				</view>
				<!-- 忘记密码区域 -->
				<view class="forget-box">
					<text class="forget" @click="forgetPopup.open()">忘记密码？</text>
					<text class="forget" v-if="loginWay !== 0" @click="loginWay = 0">账号登录</text>
				</view>
			</view>
			<!-- 底部第三方登录手机或者邮箱登录 -->
			<view class="icon-box">
				<!-- 手机号登录 -->
				<view class="phone-login-box" @click="loginWay = 1">
					<image style="width: 55rpx;height: 55rpx;" src="../../common/image/phone.png" mode="aspectFill">
					</image>
				</view>
				<!-- qq邮箱登录 -->
				<view class="qq-login-box" @click="loginWay = 2">
					<image class="image-style" src="../../common/image/QQ.png" mode="aspectFill"></image>
				</view>
			</view>
		</view>
	</view>
</template>
<style lang="scss" scoped>
	.login-content {
		width: 100%;
		height: 90vh;
		padding: 80rpx;

		.content-box {
			margin-top: 100rpx;
			width: 100%;
			height: 50vh;

			.welcome {
				font-size: 58rpx;
				font-weight: bold;
				color: #042f58;
				margin-bottom: 10rpx;
			}

			.register {
				font-size: 30rpx;
				color: #c5c5cd;
				font-weight: 520;
			}

			.icon-box {
				position: relative;
				width: 78%;
				height: 10%;
				display: flex;
				align-items: center;
				justify-content: center;
				position: absolute;
				left: 50%;
				transform: translateX(-50%);
				/* 调整左位置，使其真正居中 */
				bottom: 5%;

				.phone-login-box,
				.qq-login-box {
					width: 90rpx;
					height: 90rpx;
					border-radius: 50%;
					background-color: #afafaf;
					opacity: 0.3;
					text-align: center;
					display: flex;
					align-items: center;
					justify-content: center;
				}

				.image-style {
					width: 66rpx;
					height: 66rpx;
				}

				/* 给其中一个元素添加右边距以创建间距 */
				.phone-login-box {
					margin-right: 60rpx;
					/* 根据需要调整间距大小 */
				}

			}

			.input-box {
				width: 78%;
				margin-top: 38rpx;

				.forget-box {
					display: flex;
					align-items: center;
					justify-content: space-between;
					margin-top: 12rpx;
					color: #bbb;
				}

				.hint {
					color: #bbb;
				}

				.code-style {
					font-size: 26rpx;
					color: #bbbbbb;
				}

				.get-style {
					color: black;
				}

				.loginBtn {
					text-align: center;
					line-height: 75rpx;
					width: 100%;
					height: 75rpx;
					border-radius: 12rpx;
					margin-top: 38rpx;
					cursor: pointer;
					background-color: #3c9cff;
					color: #fff;

				}
			}
		}
	}
</style>