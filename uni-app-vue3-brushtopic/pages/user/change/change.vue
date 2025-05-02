<script setup>
import { ref } from 'vue'
import { apiUpdatePassword } from '@/api/auth'
import { clearStorage } from '@/utils/auth'
// 添加密码显示状态控制
const showOldPassword = ref(false)
const showNewPassword = ref(false)
const showConfirmPassword = ref(false)

const form = ref({
	oldPassword: '',
	newPassword: '',
	confirmPassword: ''
})
// 用户信息
const userInfo = ref(JSON.parse(uni.getStorageSync('h5UserInfo')))
const handleSubmit = async () => {
	// 校验参数
	if (form.value.oldPassword === '') {
		uni.showToast({
			title: '请输入原密码',
			icon: 'none'
		})
		return
	}
	if (form.value.newPassword === '') {
		uni.showToast({
			title: '请输入新密码',
			icon: 'none'
		})
		return
	}
	if (form.value.newPassword !== form.value.confirmPassword) {
		uni.showToast({
			title: '两次密码不一致',
			icon: 'none'
		})
		return
	}
	await apiUpdatePassword({
		id: userInfo.value.id,
		password: form.value.oldPassword,
		newPassword: form.value.newPassword,
		confirmPassword: form.value.confirmPassword,
	})
	uni.showToast({
		title: '修改成功',
		icon: 'success'
	})
	// 清空表单
	form.value = {
		oldPassword: '',
		newPassword: '',
		confirmPassword: ''
	}
	// 重新登录清空缓存
	clearStorage()
	// 重定向
	setTimeout(() => {
		// 重新登录
		uni.reLaunch({
			url: "/pages/login/login",
			success: () => { }
		})
	}, 1000)
}
</script>
<template>
	<view class="change-box">
		<view class="section">
			<view class="list">
				<view class="row">
					<view class="left">
						<text class="label">原密码</text>
					</view>
					<view class="right">
						<input class="uni-input" :type="showOldPassword ? 'text' : 'password'" v-model="form.oldPassword"
							placeholder="请输入原密码" />
						<uni-icons :type="showOldPassword ? 'eye-filled' : 'eye-slash-filled'" size="20" color="#999"
							@click="showOldPassword = !showOldPassword"></uni-icons>
					</view>
				</view>

				<view class="row">
					<view class="left">
						<text class="label">新密码</text>
					</view>
					<view class="right">
						<input class="uni-input" :type="showNewPassword ? 'text' : 'password'" v-model="form.newPassword"
							placeholder="请输入新密码" />
						<uni-icons :type="showNewPassword ? 'eye-filled' : 'eye-slash-filled'" size="20" color="#999"
							@click="showNewPassword = !showNewPassword"></uni-icons>
					</view>
				</view>

				<view class="row">
					<view class="left">
						<text class="label">确认密码</text>
					</view>
					<view class="right">
						<input class="uni-input" :type="showConfirmPassword ? 'text' : 'password'" v-model="form.confirmPassword"
							placeholder="请确认新密码" />
						<uni-icons :type="showConfirmPassword ? 'eye-filled' : 'eye-slash-filled'" size="20" color="#999"
							@click="showConfirmPassword = !showConfirmPassword"></uni-icons>
					</view>
				</view>
			</view>

			<view class="submit-btn">
				<button class="btn" hover-class="btn-hover" @click="handleSubmit">确认修改</button>
			</view>
		</view>
	</view>
</template>

<style lang="scss" scoped>
$theme-color: #1677ff;

.change-box {
	min-height: 100vh;
	background-color: #f5f5f5;
	padding: 15rpx;
	padding-top: 0;
}

.section {
	background: #fff;
	border-radius: 16rpx;
	padding: 0 20rpx;
	margin: 20rpx auto;
	box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.03);

	.list {
		.row {
			display: flex;
			align-items: center;
			padding: 30rpx 0;
			border-bottom: 1px solid #f5f5f5;

			&:last-child {
				border-bottom: none;
			}

			.left {
				width: 160rpx;

				.label {
					font-size: 28rpx;
					color: #333;
					font-weight: 500;
				}
			}

			.right {
				flex: 1;
				display: flex;
				align-items: center;
				gap: 10rpx;

				.uni-input {
					flex: 1;
					height: 60rpx;
					font-size: 28rpx;
					color: #333;

					&::placeholder {
						color: #999;
					}
				}

				.uni-icons {
					padding: 10rpx;
				}
			}
		}
	}
}

.submit-btn {
	padding: 30rpx 20rpx;

	.btn {
		width: 100%;
		height: 88rpx;
		line-height: 88rpx;
		text-align: center;
		background: #fff;
		color: $theme-color;
		font-size: 32rpx;
		border-radius: 44rpx;
		border: 1px solid $theme-color;
		transition: all 0.3s;

		&.btn-hover {
			opacity: 0.8;
		}
	}
}
</style>