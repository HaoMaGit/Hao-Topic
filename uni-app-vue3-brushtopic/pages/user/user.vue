<script setup>
import {
	ref, computed
} from 'vue'
import { apiGetRoleDetail } from '@/api/system/role'
import { apiSendFeedback } from '@/api/system/feedback'
import { apiGetUserInfo } from '@/api/auth/index'
import { apiGetConfig } from '@/api/system/index'
import { apiRecordNotice, apiGetNoticeHas } from '@/api/system/notice'
import { onShow } from '@dcloudio/uni-app'
// 添加导入
import FeedbackPopup from '@/components/feedbackPopup.vue'
// 用户信息
const userInfo = ref(JSON.parse(uni.getStorageSync('h5UserInfo')))
// 当前身份
const role = ref(uni.getStorageSync('role'))
// 角色信息
const roleDetail = ref(null)
// 获取角色详情
const getRoleDetail = async () => {
	// loading
	uni.showLoading({
		mask: true
	})
	const res = await apiGetRoleDetail(role.value)
	roleDetail.value = res
	uni.hideLoading()
}
// 获取用户信息
const getUserDetail = async () => {
	const res = await apiGetUserInfo(userInfo.value.id)
	userInfo.value = res
}

// 角色相关的计算属性
const getRoleIcon = computed(() => {
	const iconMap = {
		2: 'staff',
		1: 'vip-filled',
		0: 'person-filled'
	}
	return iconMap[role.value] || 'person-filled'
})
const getRoleColor = computed(() => {
	const colorMap = {
		1: '#712a07',
		2: '#564021',
		0: '#203c71'
	}
	return colorMap[role.value] || '#ffffff'
})

// 预览头像
const previewAvatar = () => {
	if (!userInfo.value.avatar) return
	uni.previewImage({
		urls: [userInfo.value.avatar],
		current: 0
	})
}
onShow(() => {
	getUserDetail()
	getRoleDetail()
	getWebConfig()
	getNotice()
})


// 会员对话框
const memberModal = ref()
// 解锁会员
const unlockMember = () => {
	memberModal.value.open()
}


// 联系我们的对话框
const contactUsPopup = ref()
// 联系我们
const contactUs = () => {
	contactUsPopup.value.open()
}



// 点击了提交
const handleFeedbackSubmit = async (content) => {
	await apiSendFeedback({
		feedbackContent: content,
		status: 2
	})
	uni.showToast({
		title: '反馈成功可在我的反馈中查看',
		icon: 'none',
		duration: 2000
	})
}


// 修改渐变背景计算属性，使上面更深，下面更浅
const getPageGradient = computed(() => {
	const gradientMap = {
		1: 'linear-gradient(to bottom, rgba(243, 156, 18, 0.6), rgba(243, 156, 18, 0.3) 30%, rgba(243, 156, 18, 0.1) 60%, transparent 90%)', // 管理员黑金色
		2: 'linear-gradient(to bottom, rgba(33, 33, 33, 0.8), rgba(212, 175, 55, 0.4) 40%, rgba(212, 175, 55, 0.1) 70%, transparent 90%)', // 会员金色
		0: 'linear-gradient(to bottom, rgba(22, 119, 255, 0.6), rgba(22, 119, 255, 0.3) 30%, rgba(22, 119, 255, 0.1) 60%, transparent 90%)' // 普通用户蓝色
	}
	return gradientMap[role.value] || gradientMap[0]
})

// 添加文字颜色的计算属性
const getTextColor = computed(() => {
	const textColorMap = {
		1: '#712a07',
		2: '#564021',
		0: '#203c71'
	}
	return textColorMap[role.value] || gradientMap[0]
})

// 配置
const config = ref({
	pay: 1
})
// 支付配置
const payConfig = ref({})
const getWebConfig = async () => {
	const res = await apiGetConfig(config.value.pay)
	payConfig.value = res.data
}

// 去支付
const goToPay = async () => {
	// 发送请求到通知
	await apiRecordNotice({
		status: 0
	})
	memberModal.value.close()
	const tips = [
		'感谢您的支持，让我们一起开启AI之旅~',
		'谢谢您的信任，HaoAi与您同行！',
		'支付成功！您已解锁全新体验~',
	]
	uni.showToast({
		title: tips[Math.floor(Math.random() * tips.length)],
		icon: 'none',
		duration: 2000
	})
}
// 是否有通知
const hasNotice = ref(false)
// 查询是否有通知
const getNotice = async () => {
	const res = await apiGetNoticeHas()
	hasNotice.value = res.data
}

// 反馈弹窗显示状态
const showFeedback = ref(false)
</script>
<template>
	<view class="user-box" :style="{ background: getPageGradient }">


		<!-- 联系我们的弹层 -->
		<uni-popup ref="contactUsPopup" background-color="#fff">
			<image src="../../static/images/qq.png" class="image-style" mode="aspectFill"></image>
		</uni-popup>

		<!-- 意见反馈的弹层 -->
		<FeedbackPopup v-model:show="showFeedback" @submit="handleFeedbackSubmit" />

		<!-- 头像位置 -->
		<view class="user-top">
			<view class="avatar-wrap" @click="previewAvatar">
				<template v-if="userInfo.avatar">
					<image :src="userInfo.avatar" class="avatar-image" mode="aspectFill"></image>
				</template>
				<template v-else>
					<view class="avatar-placeholder">
						<text>{{ userInfo.nickname?.[0] || userInfo.account?.[0] || 'Hao' }}</text>
					</view>
				</template>
			</view>
			<view class="user-info">
				<view class="name-wrap">
					<text class="nickname" :style="{ color: getTextColor }">{{ userInfo?.nickname || '暂无昵称' }}</text>
					<text class="account-tag" :style="{ color: getTextColor }">账户: {{ userInfo.account }}</text>
				</view>
				<view class="role-wrap" v-if="roleDetail">
					<view class="role-badge">
						<uni-icons :type="getRoleIcon" size="16" :color="getRoleColor"></uni-icons>
						<text class="role-name" :style="{ color: getTextColor }">{{ roleDetail?.name }}</text>
					</view>
				</view>
				<view class="role-wrap" v-if="roleDetail">
					<text class="role-desc" :style="{ color: getTextColor }">{{ roleDetail?.remark }}</text>
				</view>
			</view>
		</view>
		<!-- 操作列表 -->
		<view class="section">
			<!-- 外层盒子 -->
			<view class="list">
				<!-- 跳转链接 -->
				<!-- 每一行 -->
				<view class="row" v-if="role == 0 || role == 1">
					<view class="left">
						<uni-icons type="vip-filled" size="28" color="#1677ff"></uni-icons>
						<view class="text">
							我的会员
						</view>
					</view>
					<view class="right" @click="unlockMember">
						<view class="text">
							解锁更多功能
						</view>
						<uni-icons type="right" size="22" color="#a6a6a6"></uni-icons>
					</view>
				</view>

				<navigator url="/pages/user/favorite/favorite" class="border-row">
					<view class="row">
						<view class="left">
							<uni-icons type="star-filled" size="28" color="#1677ff"></uni-icons>
							<view class="text">
								我的收藏
							</view>
						</view>
						<view class="right">
							<uni-icons type="right" size="22" color="#a6a6a6"></uni-icons>
						</view>
					</view>
				</navigator>



				<navigator url="/pages/user/feedback/feedback" class="border-row">
					<view class="row">
						<view class="left">
							<uni-icons type="eye-filled" size="28" color="#1677ff"></uni-icons>
							<view class="text">
								我的反馈
							</view>
						</view>
						<view class="right">
							<view class="text">
								<uv-badge :isDot="hasNotice"></uv-badge>
							</view>
							<uni-icons type="right" size="22" color="#a6a6a6"></uni-icons>
						</view>
					</view>
				</navigator>

				<navigator url="/pages/user/setting/setting">
					<view class="row">
						<view class="left">
							<uni-icons type="gear-filled" size="28" color="#1677ff"></uni-icons>
							<view class="text">
								我的设置
							</view>
						</view>
						<view class="right">
							<uni-icons type="right" size="22" color="#a6a6a6"></uni-icons>
						</view>
					</view>
				</navigator>
			</view>
		</view>


		<view class="section">
			<view class="list">
				<view class="row">
					<view class="left">
						<uni-icons type="staff-filled" size="28" color="#1677ff"></uni-icons>
						<view class="text">
							联系我们
						</view>
					</view>
					<view class="right" @click="contactUs">
						<uni-icons type="right" size="22" color="#a6a6a6"></uni-icons>
					</view>
				</view>
				<view class="row">
					<view class="left">
						<uni-icons type="paperplane" size="28" color="#1677ff"></uni-icons>
						<view class="text">
							意见反馈
						</view>
					</view>
					<view class="right" @click="showFeedback = true">
						<uni-icons type="right" size="22" color="#a6a6a6"></uni-icons>
					</view>
				</view>
			</view>
		</view>

		<!-- 会员对话框 -->
		<uv-modal :show-cancel-button="true" ref="memberModal" :title="role === '1' ? 'HaoAi刷题会员' : '会员服务'"
			:confirm-text="role === '1' ? '关闭' : '我已支付'" @confirm="role === '1' ? memberModal.close() : goToPay()">
			<template #default>
				<view class="modal-box">
					<view class="member-box" :class="{ 'is-vip': role === '1' }">
						<view class="left">
							<uni-icons type="vip" size="38" color="#ffd700"></uni-icons>
							<view class="text">永久会员</view>
						</view>
						<view class="right">
							<template v-if="role === '1'">
								<view class="vip-badge">
									<uni-icons type="checkmarkempty" size="24" color="#ffd700"></uni-icons>
									<text>已开通</text>
								</view>
							</template>
							<template v-else>
								<view class="price">
									<text class="symbol">￥</text>{{ payConfig.price }}
								</view>
							</template>
						</view>
					</view>

					<template v-if="role === '1'">
						<view class="vip-content">
							<uni-icons type="star-filled" size="64" color="#ffd700"></uni-icons>
							<text class="congrats">恭喜您已经是HaoAi刷题会员</text>
							<text class="benefit">{{ roleDetail.remark }}</text>
						</view>
					</template>
					<template v-else>
						<view class="bottom">{{ payConfig.content }}</view>
						<view class="pay-content">
							<image :src="payConfig.url" mode="widthFix" class="pay-qrcode"></image>
							<view class="pay-tips">
								<text>{{ payConfig.remark }}</text>
							</view>
						</view>
					</template>
				</view>
			</template>
		</uv-modal>
	</view>
</template>
<style lang="scss" scoped>
.user-box {
	.modal-box {
		padding: 20rpx;
		background-color: #fff;
		width: 100%;

		.member-box {
			display: flex;
			align-items: center;
			justify-content: space-between;
			padding: 20rpx;
			background: #f8f8f8;
			border-radius: 12rpx;

			.vip-badge {
				display: flex;
				align-items: center;

			}

			.price {
				font-size: 46rpx;
				color: #ff4d4f;
				font-weight: bold;

				.symbol {
					font-size: 32rpx;
					margin-right: 4rpx;
				}
			}
		}

		.vip-content {
			display: flex;
			flex-direction: column;
			align-items: center;
			padding: 40rpx 0;

			.congrats {
				font-size: 36rpx;
				color: #333;
				font-weight: bold;
				margin: 20rpx 0 10rpx;
			}

			.benefit {
				text-align: center;
				font-size: 28rpx;
				color: #666;
			}
		}

		.pay-content {
			display: flex;
			flex-direction: column;
			align-items: center;
			padding: 20rpx 0;

			.pay-qrcode {
				width: 400rpx;
				height: 400rpx;
				margin-bottom: 20rpx;
			}

			.pay-tips {
				display: flex;
				flex-direction: column;
				align-items: center;
				gap: 10rpx;

				text {
					font-size: 28rpx;
					color: #666;

					&:first-child {
						color: #333;
						font-weight: 500;
					}
				}
			}
		}

		.bottom {
			margin-top: 20rpx;
			font-size: 26rpx;
			color: #999;
			text-align: center;
			padding: 0 20rpx;
		}
	}

	.border-row {
		border-bottom: 1px solid #e6e6e6;
	}



	.image-style {
		width: 500rpx;
		height: 500rpx;
	}

	.bottom {
		margin-top: 10rpx;
		font-size: 28rpx;
		color: #949494;
	}

	.member-box {
		display: flex;
		align-items: center;
		justify-content: space-between;

		.left {
			display: flex;
			align-items: center;

			.text {
				font-size: 38rpx;
				color: #858585;
				padding-left: 10rpx;
				font-weight: bold;
			}

			.icon {
				color: #28b28b;
			}
		}

		.right {
			display: flex;
			align-items: center;

			.text {
				font-size: 30rpx;
				color: #aaa;
				padding-right: 5rpx;
			}
		}
	}

	.section {
		width: 690rpx;
		margin: 28rpx auto;
		border: 1px solid #eeeeee;
		box-shadow: 0 0 30rpx rgba(0, 0, 0, 0.05);

		.list {
			.row {
				background-color: #fff;
				display: flex;
				align-items: center;
				justify-content: space-between;
				height: 120rpx;
				border-bottom: 1px solid #eeeeee;
				padding: 0 20rpx;
				position: relative;
				border-radius: 10rpx;

				&:last-child {
					border-bottom: 0;
				}

				.left {
					display: flex;
					align-items: center;

					.text {
						padding-left: 20rpx;
					}

					.icon {
						color: #28b28b;
					}
				}

				.right {
					display: flex;
					align-items: center;

					.text {
						font-size: 28rpx;
						color: #aaa;
						padding-right: 5rpx;
					}
				}

				button {
					position: absolute;
					top: 0;
					left: 0;
					height: 100rpx;
					width: 100%;
					opacity: 0;

				}
			}
		}
	}

	.user-top {
		padding: 55rpx 0rpx 23rpx 0rpx;
		// background: linear-gradient(135deg, #1677ff, #4096ff);
		display: flex;
		align-items: center;
		justify-content: center;
		flex-direction: column;
		position: relative;
		overflow: hidden;
		border-radius: 0rpx 0rpx 5rpx 5rpx;

		&::after {
			content: '';
			position: absolute;
			top: 0;
			left: 0;
			right: 0;
			bottom: 0;
			// background: radial-gradient(circle at top right, rgba(255, 255, 255, 0.1) 0%, transparent 60%);
		}

		.avatar-wrap {
			margin-bottom: 25rpx;
			position: relative;
			z-index: 1;
			width: 180rpx;
			height: 180rpx;
			border-radius: 50%;
			overflow: hidden;
			border: 6rpx solid rgba(255, 255, 255, 0.8);
			box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.1);

			.avatar-image {
				width: 100%;
				height: 100%;
				transition: transform 0.3s;

				&:active {
					transform: scale(0.95);
				}
			}

			.avatar-placeholder {
				width: 100%;
				height: 100%;
				background: linear-gradient(45deg, #1677ff, #4096ff);
				display: flex;
				align-items: center;
				justify-content: center;

				text {
					font-size: 60rpx;
					color: #fff;
					font-weight: 600;
					text-transform: uppercase;
				}
			}
		}

		.user-info {
			text-align: center;
			position: relative;
			z-index: 1;

			.name-wrap {
				margin-bottom: 20rpx;

				.nickname {
					font-size: 42rpx;
					color: #fff;
					font-weight: 600;
					text-shadow: 0 2rpx 4rpx rgba(0, 0, 0, 0.1);
					display: block;
					margin-bottom: 8rpx;

				}

				.account-tag {
					font-size: 24rpx;
					color: rgba(255, 255, 255, 0.9);
					background: rgba(255, 255, 255, 0.15);
					padding: 4rpx 16rpx;
					border-radius: 20rpx;
				}
			}

			.role-wrap {
				display: flex;
				align-items: center;
				justify-content: center;
				gap: 16rpx;

				.role-badge {
					display: flex;
					align-items: center;
					gap: 6rpx;
					background: rgba(255, 255, 255, 0.2);
					padding: 6rpx 16rpx;
					border-radius: 20rpx;
					backdrop-filter: blur(4px);

					.role-name {
						font-size: 24rpx;
						color: #712a07;
						font-weight: 500;
					}
				}

				.role-desc {
					margin-top: 10rpx;
					font-size: 24rpx;
					color: #712a07;
				}
			}
		}
	}
}
</style>