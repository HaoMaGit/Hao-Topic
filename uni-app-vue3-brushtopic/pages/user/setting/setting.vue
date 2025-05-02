<script setup>
import { ref, computed } from 'vue'
import { clearStorage } from '@/utils/auth'
import { apiUpdateNicknameAndEmail, apiSaveUserAvatar, apiLogout } from '@/api/auth'
// 头像的样式
const imageStyles = ref({
	width: 80,
	height: 80,
	border: false,
})
// 上传地址
const uploadUrl = 'http://localhost:9993/api/system/user/avatar';
// 允许的图片类型
const imageTypes = ['jpg', 'jpeg', 'png', 'gif']
// 图片上传前校验
const beforeUpload = (file) => {
	// 获取文件扩展名
	const extension = file.name.split('.').pop().toLowerCase()
	// 校验文件类型
	if (!imageTypes.includes(extension)) {
		uni.showToast({
			title: '只能上传图片文件',
			icon: 'error'
		})
		return false
	}
	// 校验文件大小(5MB)
	if (file.size > 2 * 1024 * 1024) {
		uni.showToast({
			title: '图片大小不能超过6MB',
			icon: 'error'
		})
		return false
	}
	return true
}
// 图片上传成功的回调
const uploadSuccess = async (e) => {
	const [file] = e.tempFiles
	if (!file) return

	try {
		// 显示上传中
		uni.showLoading({ title: '保存头像中...' })
		// 获取token
		const token = uni.getStorageSync(userInfo.value.account + 'token')
		// 调用上传接口
		const res = await uni.uploadFile({
			url: uploadUrl,
			filePath: file.url,
			name: 'avatar',
			header: {
				'Authorization': token
			},
			formData: {},
			method: 'POST',
		})

		console.log('res', res);
		// 转换json
		const jsonResult = JSON.parse(res.data)
		console.log('jsonResult', jsonResult);

		// 保存一下头像地址
		await apiSaveUserAvatar({
			id: userInfo.value.id,
			avatar: jsonResult.data
		})
		// 更新本地存储
		const json = JSON.parse(uni.getStorageSync('h5UserInfo'))
		json.avatar = jsonResult.data
		uni.setStorageSync('h5UserInfo', JSON.stringify(json))

		// 更新当前数据
		userInfo.value.avatar = jsonResult.data
		uni.hideLoading()

		uni.showToast({
			title: '头像更新成功',
			icon: 'success'
		})
	} catch (error) {
		uni.showToast({
			title: '头像更新失败',
			icon: 'error'
		})
	}
}

// 修改名称对话框
const nameDialog = ref()

// 跳转修改密码
const goToChange = () => {
	uni.navigateTo({
		url: '/pages/user/change/change'
	})
}

// 退出登录
const logout = () => {
	uni.showModal({
		title: '提示',
		content: '确定要退出登录吗？',
		success: async (res) => {
			if (res.confirm) {
				clearStorage()
				await apiLogout()
				uni.showToast({
					title: '退出登录成功',
					icon: 'success'
				})
				setTimeout(() => {
					uni.reLaunch({
						url: '/pages/login/login'
					})
				}, 300)
			}
		}
	})
}

// 开启自定义题目
const isCustomQuestion = ref(uni.getStorageSync('isCustomQuestion') || true)
// 修改开关
const asyncChange = async (e) => {
	uni.showModal({
		title: '提示',
		content: e ? '开启会员自定义题目展示' : '确定要关闭会员自定义的题目展示吗',
		success: (res) => {
			if (res.confirm) {
				isCustomQuestion.value = true
				// 修改缓存
				uni.setStorageSync('isCustomQuestion', true)
			} else {
				isCustomQuestion.value = false
				uni.setStorageSync('isCustomQuestion', false)
			}
		}
	})
}
// 用户信息
const userInfo = ref(JSON.parse(uni.getStorageSync('h5UserInfo')))

// 头像文字
const text = computed(() => {
	console.log(userInfo.value.nickname);
	if (!userInfo.value.nickname) {
		return userInfo.value.account.substring(0, 2);
	}
	return userInfo.value.nickname.substring(0, 2)
})

// 确认修改昵称
const dialogInputConfirm = async (value) => {
	// 校验参数
	if (!value) {
		uni.showToast({
			title: '请输入昵称',
			icon: 'none'
		})
		return
	}
	// 发起请求
	await apiUpdateNicknameAndEmail({
		id: userInfo.value.id,
		nickname: value,
		password: userInfo.value.password
	})
	const json = JSON.parse(uni.getStorageSync('h5UserInfo'))
	json.nickname = value
	// 修改本地缓存
	uni.setStorageSync('h5UserInfo', JSON.stringify(json))
	userInfo.value.nickname = value
	// 修改成功
	uni.showToast({
		title: '修改成功',
		icon: 'success',
		duration: 2000
	})
}
</script>
<template>
	<view class="setting-box">
		<!-- 修改名称的弹层 -->
		<uni-popup ref="nameDialog" type="dialog">
			<uni-popup-dialog :maxlength="8" ref="inputClose" mode="input" title="修改昵称" placeholder="请输入新的昵称"
				@confirm="dialogInputConfirm"></uni-popup-dialog>
		</uni-popup>

		<!-- 操作列表 -->
		<view class="section">
			<view class="list">
				<view class="row">
					<view class="left">
						<text class="label">修改头像</text>
					</view>
					<view class="right">
						<uni-file-picker @beforeUpload="beforeUpload" @select="uploadSuccess" limit="1" :del-icon="false"
							disable-preview :imageStyles="imageStyles" file-mediatype="image">
							<uv-avatar size="55" :src="userInfo.avatar"></uv-avatar>
						</uni-file-picker>
					</view>
				</view>

				<view class="row">
					<view class="left">
						<text class="label">修改昵称</text>
					</view>
					<view class="right" @click="nameDialog.open()">
						<text class="value">{{ userInfo?.nickname }}</text>
						<uni-icons type="right" size="16" color="#999"></uni-icons>
					</view>
				</view>

				<view class="row">
					<view class="left">
						<text class="label">修改密码</text>
					</view>
					<view class="right" @click="goToChange">
						<uni-icons type="right" size="16" color="#999"></uni-icons>
					</view>
				</view>

				<!-- 加一个开启展示自定义题目需要身份为identify1才展示 -->
				<view class="row">
					<view class="left">
						<text class="label">会员展示题目</text>
					</view>
					<view class="right">
						<uv-switch @change="asyncChange" v-model="isCustomQuestion" activeColor="#1677ff"></uv-switch>
					</view>
				</view>
			</view>

			<!-- 退出登录按钮 -->
			<view class="logout-btn">
				<button class="btn" hover-class="btn-hover" @click="logout">退出登录</button>
			</view>
		</view>
	</view>
</template>

<style lang="scss" scoped>
// 定义主题色变量
$theme-color: #1677ff;

.setting-box {
	min-height: 100vh;
	background-color: #f5f5f5;
	padding: 15rpx;
	padding-top: 0;
}

.section {
	background: #fff;
	border-radius: 18rpx;
	padding: 0 20rpx;
	margin: 20rpx auto;
	box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.03);

	.list {
		.row {
			display: flex;
			align-items: center;
			justify-content: space-between;
			padding: 30rpx 0;
			border-bottom: 1px solid #f5f5f5;


			.left {
				.label {
					font-size: 28rpx;
					color: #333;
					font-weight: 500;
				}
			}

			.right {
				display: flex;
				align-items: center;
				gap: 10rpx;



				.value {
					font-size: 28rpx;
					color: #999;
					margin-right: 10rpx;
				}
			}
		}
	}
}

.logout-btn {
	padding-bottom: 20rpx;
	margin: 60rpx 20rpx;

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
		transition: all 0.3;

		&.btn-hover {
			opacity: 0.8;
		}
	}
}
</style>
