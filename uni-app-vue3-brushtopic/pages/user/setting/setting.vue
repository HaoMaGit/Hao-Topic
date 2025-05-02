<script setup>
import { ref } from 'vue'

// 头像的样式
const imageStyles = ref({
	width: 80,
	height: 80,
	border: false,
})

// 图片上传成功的回调
const uploadSuccess = (e) => {
	console.log('上传成功', e)
}

// 修改名称对话框
const nameDialog = ref()

// 跳转修改密码
const goToChange = () => {
	uni.navigateTo({
		url: '/pages/change/change'
	})
}

// 退出登录
const logout = () => {
	uni.showModal({
		title: '提示',
		content: '确定要退出登录吗？',
		success: (res) => {
			if (res.confirm) {
				// 执行退出登录逻辑
				console.log('用户点击确定')
			}
		}
	})
}

// 开启自定义题目
const isCustomQuestion = ref(false)
</script>
<template>
	<view class="setting-box">
		<!-- 修改名称的弹层 -->
		<uni-popup ref="nameDialog" type="dialog">
			<uni-popup-dialog ref="inputClose" mode="input" title="修改名称" placeholder="请输入新的名称"
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
						<uni-file-picker @select="uploadSuccess" limit="1" :del-icon="false" disable-preview
							:imageStyles="imageStyles" file-mediatype="image">
							<uv-avatar class="avatar" size="55" src="https://via.placeholder.com/200x200.png/2878ff"></uv-avatar>
						</uni-file-picker>
					</view>
				</view>

				<view class="row">
					<view class="left">
						<text class="label">修改昵称</text>
					</view>
					<view class="right" @click="nameDialog.open()">
						<text class="value">书中易语</text>
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
						<uv-switch v-model="isCustomQuestion" activeColor="#1677ff"></uv-switch>
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

				.avatar {
					padding-right: 15rpx;
				}

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
