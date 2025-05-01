<script setup>
import {
	ref,
	reactive,
	onUnmounted
} from 'vue'
// 登录方式 0账号登录  1邮箱登录 
const loginWay = ref(0)
// 是否点击了注册
const isRegister = ref(false)
// 是否点击了忘记密码
const isForget = ref(false)
// 登录表单
const loginForm = reactive({
	account: '',
	password: '',
	email: '',
})

// 注册表单
const registerForm = reactive({
	account: '',
	nickname: '',
	email: '',
	password: '',
	code: ''
})

// 忘记密码表单
const forgetForm = reactive({
	email: '',
	code: '',
	password: '',
	newPassword: ''
})


const totalSecond = ref(60) // 总秒数
const second = ref(60) // 倒计时的秒数
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
		// toast('验证码已发送')
		// let codeTimer = setTimeout(() => {
		// 	formData.value.code = res.data
		// }, 2000)
	}
}
// 页面销毁清除定时器
onUnmounted(() => {
	clearInterval(timer)
})

// 返回登录
const backLogin = () => {
	isRegister.value = false
	isForget.value = false
	totalSecond.value = 60
	second.value = totalSecond.value
	clearInterval(timer)
	timer = null
}
</script>
<template>
	<view class="login-content">
		<!-- 背景动画元素 -->
		<view class="bg-animation">
			<view class="circle"></view>
			<view class="circle"></view>
		</view>

		<!-- 主体内容 -->
		<view class="content-box">
			<!-- Logo区域 -->
			<view class="logo-area">
				<image src="/static/images/logo.png" mode="aspectFit" class="logo-img"></image>
				<text class="slogan">让学习更智能，让进步更高效</text>
			</view>

			<!-- 登录表单 -->
			<view class="input-box">
				<uv-input class="input" v-if="loginWay === 0" shape="circle" placeholder="请输入账户名称"
					v-model="loginForm.account"></uv-input>
				<uv-input class="input" v-else shape="circle" placeholder="请输入邮箱" v-model="loginForm.email"></uv-input>
				<uv-input class="input" type="password" shape="circle" placeholder="请输入密码"
					v-model="loginForm.password"></uv-input>
				<button class="login-btn" hover-class="btn-hover">开始刷题</button>
				<view class="action-row">
					<text @click="isForget = true">忘记密码</text>
					<text @click="loginWay = loginWay === 0 ? 1 : 0">
						{{ loginWay === 0 ? '邮箱登录' : '账户登录' }}
					</text>
				</view>
			</view>

			<!-- 注册表单 -->
			<view class="register-form" v-if="isRegister">
				<view class="form-title">
					<text class="title">加入AI刷题</text>
					<text class="back" @click="backLogin">返回登录</text>
				</view>
				<view class="input-group">
					<uv-input class="input" maxlength="8" shape="circle" placeholder="创建你的专属账户 (注册后不可更改)"
						v-model="registerForm.account"></uv-input>
					<uv-input class="input" maxlength="8" shape="circle" placeholder="给自己起个独特的昵称"
						v-model="registerForm.nickname"></uv-input>
					<uv-input class="input" shape="circle" placeholder="请输入邮箱" v-model="registerForm.email">
						<!-- vue3模式下必须使用v-slot:suffix -->
						<template v-slot:suffix>
							<text @click="getCode()">
								{{ second === totalSecond ? '发送验证码' :
									`重新发送${second}秒` }}</text>
						</template>
					</uv-input>
					<uv-input class="input" type="number" shape="circle" placeholder="请输入邮箱验证码" v-model="registerForm.code"
						maxlength="6">
					</uv-input>
					<uv-input class="input" type="password" shape="circle" placeholder="请输入登录密码"
						v-model="registerForm.password"></uv-input>
				</view>
				<button class="register-submit-btn" hover-class="btn-hover">开启AI刷题之旅</button>
			</view>

			<!-- 忘记密码 -->
			<view class="forget-form" v-if="isForget">
				<view class="form-title">
					<text class="title">重置密码</text>
					<text class="back" @click="backLogin">返回登录</text>
				</view>
				<view class="input-group">
					<uv-input class="input" shape="circle" placeholder="请输入邮箱" v-model="forgetForm.email">
						<template v-slot:suffix>
							<text @click="getCode()">
								{{ second === totalSecond ? '发送验证码' : `重新发送${second}秒` }}
							</text>
						</template>
					</uv-input>
					<uv-input class="input" type="number" shape="circle" placeholder="请输入验证码" v-model="forgetForm.code"
						maxlength="6">
					</uv-input>
					<uv-input class="input" type="password" shape="circle" placeholder="请输入新密码" v-model="forgetForm.password">
					</uv-input>
					<uv-input class="input" type="password" shape="circle" placeholder="请确认新密码" v-model="forgetForm.newPassword">
					</uv-input>
				</view>
				<button class="forget-submit-btn" hover-class="btn-hover">确认修改</button>
			</view>
			<!-- 底部显示一个注册 -->
			<view class="register-box">
				<text>还没有刷题过？</text>
				<text class="register-btn" @click="isRegister = true">立即注册</text>
			</view>
		</view>
	</view>
</template>

<style lang="scss" scoped>
.login-content {
	min-height: 100vh;
	background: linear-gradient(to bottom, #f0f7ff, #ffffff);
	position: relative;
	overflow: hidden;

	.bg-animation {
		position: absolute;
		width: 100%;
		height: 100%;
		z-index: 1;

		.circle {
			position: absolute;
			border-radius: 50%;
			background: linear-gradient(45deg, rgba(22, 119, 255, 0.1), rgba(22, 119, 255, 0.05));

			&:nth-child(1) {
				width: 300rpx;
				height: 300rpx;
				top: -100rpx;
				left: -100rpx;
				animation: float 8s infinite;
			}

			&:nth-child(2) {
				width: 200rpx;
				height: 200rpx;
				bottom: -50rpx;
				right: -50rpx;
				animation: float 6s infinite reverse;
			}
		}
	}

	.content-box {
		position: relative;
		z-index: 2;
		padding: 60rpx 40rpx;
		display: flex;
		flex-direction: column;
		justify-content: space-between; // 修改这里
		height: calc(100vh - 140rpx); // 修改这里

		.register-box {
			margin-top: auto;
			padding-bottom: 50rpx;
			text-align: center;

			text {
				font-size: 30rpx;
				color: #666;
			}

			.register-btn {
				color: #1677ff;
				margin-left: 10rpx;
				font-weight: 500;
			}
		}
	}

	.logo-area {
		text-align: center;
		padding-top: 300rpx;

		.logo-img {
			position: absolute;
			width: 600rpx;
			height: 600rpx;
			left: 50%;
			top: -80rpx; // 向上偏移
			transform: translateX(-50%);
			object-fit: cover;
			z-index: 0; // 确保图片在文字后面
		}



		.welcome {
			font-size: 46rpx;
			font-weight: bold;
			color: #1677ff;
			margin-bottom: 20rpx;
			display: block;
		}

		.slogan {
			font-size: 28rpx;
			color: #666;
		}
	}

	.input-box {
		margin-top: 60rpx;

		.input {
			height: 60rpx;
			background-color: #fff;
			margin-top: 15px;
		}

		.login-btn {
			background: #1677ff;
			color: #fff;
			height: 90rpx;
			line-height: 90rpx;
			border-radius: 45rpx;
			font-size: 32rpx;
			margin-top: 46rpx;
			box-shadow: 0 8rpx 20rpx rgba(22, 119, 255, 0.3);
			transition: all 0.3s;
		}

		.btn-hover {
			transform: translateY(2rpx);
			box-shadow: 0 4rpx 10rpx rgba(22, 119, 255, 0.2);
		}

		.action-row {
			display: flex;
			justify-content: space-between;
			margin-top: 30rpx;
			padding: 0 20rpx;

			text {
				color: #1677ff;
				font-size: 30rpx;
			}
		}
	}

	.other-login {
		margin-top: 100rpx;
		text-align: center;

		.divider {
			position: relative;
			margin-bottom: 40rpx;

			&::before,
			&::after {
				content: '';
				position: absolute;
				top: 50%;
				width: 20%;
				height: 1rpx;
				background: #e5e5e5;
			}

			&::before {
				left: 20%;
			}

			&::after {
				right: 20%;
			}

			text {
				color: #999;
				font-size: 24rpx;
				background: #f0f7ff;
				padding: 0 30rpx;
			}
		}

		.qq-login {
			display: inline-block;
			padding: 20rpx;
			border-radius: 50%;
			background: rgba(255, 255, 255, 0.8);
			box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);

			image {
				width: 60rpx;
				height: 60rpx;
			}
		}
	}
}

@keyframes float {

	0%,
	100% {
		transform: translateY(0);
	}

	50% {
		transform: translateY(-20rpx);
	}
}



.register-form {
	position: fixed;
	top: 0;
	left: 0;
	width: 100%;
	height: 100vh;
	background: linear-gradient(to bottom, #f0f7ff, #ffffff);
	z-index: 100;
	padding: 60rpx 40rpx;
	box-sizing: border-box;

	.form-title {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 60rpx;

		.title {
			font-size: 40rpx;
			font-weight: bold;
			color: #1677ff;
		}

		.back {
			color: #666;
			font-size: 28rpx;
		}
	}

	.input-group {


		.input {
			height: 60rpx;
			background-color: #fff;
			margin-bottom: 30rpx;
		}
	}

	.register-submit-btn {
		background: #1677ff;
		color: #fff;
		height: 90rpx;
		line-height: 90rpx;
		border-radius: 45rpx;
		font-size: 32rpx;
		margin-top: 60rpx;
		box-shadow: 0 8rpx 20rpx rgba(22, 119, 255, 0.3);
		transition: all 0.3s;

		&.btn-hover {
			transform: translateY(2rpx);
			box-shadow: 0 4rpx 10rpx rgba(22, 119, 255, 0.2);
		}
	}
}

.forget-form {
	position: fixed;
	top: 0;
	left: 0;
	width: 100%;
	height: 100vh;
	background: linear-gradient(to bottom, #f0f7ff, #ffffff);
	z-index: 100;
	padding: 60rpx 40rpx;
	box-sizing: border-box;

	.form-title {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 60rpx;

		.title {
			font-size: 40rpx;
			font-weight: bold;
			color: #1677ff;
		}

		.back {
			color: #666;
			font-size: 28rpx;
		}
	}

	.input-group {
		.input {
			height: 60rpx;
			background-color: #fff;
			margin-bottom: 30rpx;
		}
	}

	.forget-submit-btn {
		background: #1677ff;
		color: #fff;
		height: 90rpx;
		line-height: 90rpx;
		border-radius: 45rpx;
		font-size: 32rpx;
		margin-top: 60rpx;
		box-shadow: 0 8rpx 20rpx rgba(22, 119, 255, 0.3);
		transition: all 0.3s;

		&.btn-hover {
			transform: translateY(2rpx);
			box-shadow: 0 4rpx 10rpx rgba(22, 119, 255, 0.2);
		}
	}
}
</style>