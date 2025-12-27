<template>
	<view class="user-page-container" :style="{ background: themeConfig.bg }">
		<!-- 1. 顶部身份卡片 -->
		<view class="profile-header">
			<view class="avatar-container" @click="previewAvatar">
				<view class="avatar-border" :style="{ borderColor: themeConfig.accent }">
					<image v-if="userInfo.avatar" :src="userInfo.avatar" class="img" mode="aspectFill" />
					<view v-else class="img-placeholder" :style="{ background: themeConfig.accent }">
						{{ userInfo.nickname?.charAt(0) || 'H' }}
					</view>
				</view>
				<view class="role-corner-tag" :style="{ background: themeConfig.accent }">
					<uni-icons :type="getRoleIcon" size="10" color="#fff"></uni-icons>
				</view>
			</view>

			<view class="user-info-box">
				<text class="user-nickname">{{ userInfo.nickname || '未设置昵称' }}</text>
				<view class="identity-capsule" v-if="roleDetail">
					<view class="dot" :style="{ background: themeConfig.accent }"></view>
					<text class="role-text">{{ roleDetail.name }}</text>
				</view>
			</view>
		</view>

		<!-- 2. 功能操作区 -->
		<view class="main-action-panel">
			<!-- 会员权益条 -->
			<view class="vip-benefit-bar" v-if="role <= 1" @click="unlockMember">
				<view class="bar-left">
					<view class="vip-icon-bg">
						<uni-icons type="vip-filled" size="24" color="#D97706"></uni-icons>
					</view>
					<view class="bar-text">
						<text class="t1">{{ role === 1 ? 'HaoAi 永久会员特权' : '开通永久会员' }}</text>
						<text class="t2">{{ role === 1 ? '尊享全站 100% 刷题功能' : '解锁无限AI面试、自定义题库等' }}</text>
					</view>
				</view>
				<view class="bar-btn" :style="{ background: themeConfig.accent }">查看</view>
			</view>

			<!-- 列表容器 -->
			<view class="action-list-wrap">
				<!-- 第一组 -->
				<view class="list-section">
					<view class="section-title">学习管理</view>

					<navigator url="/pages/user/favorite/favorite" class="list-row-nav">
						<view class="row-content">
							<view class="row-left">
								<view class="icon-circle fav"><uni-icons type="star-filled" size="20"
										color="#1677ff"></uni-icons></view>
								<text class="label">我的收藏</text>
							</view>
							<uni-icons type="right" size="14" color="#ccc"></uni-icons>
						</view>
					</navigator>

					<navigator url="/pages/user/feedback/feedback" class="list-row-nav">
						<view class="row-content">
							<view class="row-left">
								<view class="icon-circle feedback"><uni-icons type="chatbubble-filled" size="20"
										color="#2dd4bf"></uni-icons></view>
								<text class="label">反馈记录</text>
							</view>
							<view class="row-right">
								<uv-badge :isDot="hasNotice" type="error"></uv-badge>
								<uni-icons type="right" size="14" color="#ccc" style="margin-left: 10rpx;"></uni-icons>
							</view>
						</view>
					</navigator>
				</view>

				<!-- 第二组 -->
				<view class="list-section">
					<view class="section-title">系统支持</view>

					<view class="list-row-click" @click="contactUs">
						<view class="row-content">
							<view class="row-left">
								<view class="icon-circle contact"><uni-icons type="staff-filled" size="20"
										color="#6366f1"></uni-icons></view>
								<text class="label">联系我们</text>
							</view>
							<uni-icons type="right" size="14" color="#ccc"></uni-icons>
						</view>
					</view>

					<view class="list-row-click" @click="showFeedback = true">
						<view class="row-content">
							<view class="row-left">
								<view class="icon-circle idea"><uni-icons type="paperplane-filled" size="20"
										color="#f43f5e"></uni-icons></view>
								<text class="label">意见反馈</text>
							</view>
							<uni-icons type="right" size="14" color="#ccc"></uni-icons>
						</view>
					</view>

					<navigator url="/pages/user/setting/setting" class="list-row-nav">
						<view class="row-content">
							<view class="row-left">
								<view class="icon-circle set"><uni-icons type="gear-filled" size="20"
										color="#64748b"></uni-icons></view>
								<text class="label">我的设置</text>
							</view>
							<uni-icons type="right" size="14" color="#ccc"></uni-icons>
						</view>
					</navigator>
				</view>
			</view>

		</view>

		<!-- 3. 会员详情弹窗 -->
		<uv-modal ref="memberModal" :show-cancel-button="role !== 1" :confirm-text="role === 1 ? '我知道了' : '提交支付审核'"
			confirmColor="#D97706" @confirm="handleMemberConfirm">
			<view class="premium-modal-box">
				<view class="premium-header">
					<view class="medal-ring">
						<uni-icons type="vip-filled" size="40" color="#D97706"></uni-icons>
					</view>
					<text class="premium-title">HaoAi Premium</text>
					<text class="premium-status" v-if="role === 1">永久会员特权已激活</text>
				</view>

				<view class="privilege-list">
					<view class="p-item">
						<uni-icons type="checkbox-filled" size="16" color="#D97706"></uni-icons>
						<text>会员专属答案解析</text>
					</view>
					<view class="p-item">
						<uni-icons type="checkbox-filled" size="16" color="#D97706"></uni-icons>
						<text>HaoAi 智能面试解析助手</text>
					</view>
					<view class="p-item">
						<uni-icons type="checkbox-filled" size="16" color="#D97706"></uni-icons>
						<text>自定义专属题库管理特权</text>
					</view>
				</view>

				<view class="premium-pay-section" v-if="role !== 1">
					<view class="price-tag">
						<text class="unit">￥</text>{{ payConfig.price }}
						<text class="term">/ 永久有效</text>
					</view>
					<view class="qr-container">
						<image :src="payConfig.url" mode="widthFix" class="qr-img" />
						<text class="qr-tips">{{ payConfig.remark }}</text>
					</view>
				</view>
			</view>
		</uv-modal>

		<!-- 弹窗组件 -->
		<uni-popup ref="contactUsPopup" type="center">
			<view class="qr-pop-content">
				<image src="../../static/images/qq.png" mode="widthFix" class="img"></image>
				<text>添加客服 开启 1对1 服务</text>
			</view>
		</uni-popup>

		<FeedbackPopup v-model:show="showFeedback" @submit="handleFeedbackSubmit" />
	</view>
</template>

<script setup>
	import {
		ref,
		computed
	} from 'vue'
	import {
		onShow
	} from '@dcloudio/uni-app'
	import {
		apiGetRoleDetail
	} from '@/api/system/role'
	import {
		apiSendFeedback
	} from '@/api/system/feedback'
	import {
		apiGetUserInfo
	} from '@/api/auth/index'
	import {
		apiGetConfig
	} from '@/api/system/index'
	import {
		apiRecordNotice,
		apiGetNoticeHas
	} from '@/api/system/notice'
	import FeedbackPopup from '@/components/feedbackPopup.vue'

	const userInfo = ref(JSON.parse(uni.getStorageSync('h5UserInfo') || '{}'))
	const role = ref(Number(uni.getStorageSync('role')) || 0)
	const roleDetail = ref(null)
	const payConfig = ref({})
	const hasNotice = ref(false)
	const showFeedback = ref(false)
	const memberModal = ref()
	const contactUsPopup = ref()

	const themeConfig = computed(() => {
		const configs = {
			2: {
				name: '管理员',
				accent: '#4F46E5',
				bg: 'linear-gradient(135deg, #1E293B 0%, #334155 100%)'
			},
			1: {
				name: '高级会员',
				accent: '#D97706',
				bg: 'linear-gradient(135deg, #2D241E 0%, #78350F 100%)'
			},
			0: {
				name: '普通用户',
				accent: '#1677ff',
				bg: 'linear-gradient(135deg, #1677ff 0%, #40a9ff 100%)'
			}
		}
		return configs[role.value] || configs[0]
	})

	const getRoleIcon = computed(() => {
		const map = {
			2: 'staff-filled',
			1: 'vip-filled',
			0: 'person-filled'
		}
		return map[role.value] || 'person-filled'
	})

	const initData = async () => {
		try {
			const [u, r, c, n] = await Promise.all([
				apiGetUserInfo(userInfo.value.id),
				apiGetRoleDetail(role.value),
				apiGetConfig(1),
				apiGetNoticeHas()
			])
			userInfo.value = u;
			roleDetail.value = r;
			payConfig.value = c.data;
			hasNotice.value = n.data;
		} catch (e) {
			console.error(e)
		}
	}

	onShow(() => initData())

	const previewAvatar = () => userInfo.value.avatar && uni.previewImage({
		urls: [userInfo.value.avatar]
	})
	const unlockMember = () => memberModal.value.open()
	const contactUs = () => contactUsPopup.value.open()

	const handleMemberConfirm = () => {
		if (role.value === 1) memberModal.value.close()
		else goToPay()
	}

	const goToPay = async () => {
		await apiRecordNotice({
			status: 0
		})
		memberModal.value.close()
		uni.showToast({
			title: '已提交，请等待审核',
			icon: 'success'
		})
	}

	const handleFeedbackSubmit = async (content) => {
		await apiSendFeedback({
			feedbackContent: content,
			status: 2
		})
		uni.showToast({
			title: '反馈成功'
		})
	}
</script>

<style lang="scss" scoped>
	.user-page-container {
		min-height: 100vh;
		display: flex;
		flex-direction: column;
	}

	/* 顶部 Header */
	.profile-header {
		padding: 130rpx 50rpx 70rpx;
		display: flex;
		align-items: center;

		.avatar-container {
			position: relative;
			margin-right: 34rpx;

			.avatar-border {
				width: 140rpx;
				height: 140rpx;
				border-radius: 50%;
				border: 4rpx solid #fff;
				overflow: hidden;
				background: #fff;
				box-shadow: 0 10rpx 30rpx rgba(0, 0, 0, 0.1);

				.img {
					width: 100%;
					height: 100%;
				}

				.img-placeholder {
					width: 100%;
					height: 100%;
					display: flex;
					align-items: center;
					justify-content: center;
					color: #fff;
					font-size: 50rpx;
					font-weight: bold;
				}
			}

			.role-corner-tag {
				position: absolute;
				bottom: 4rpx;
				right: 4rpx;
				width: 40rpx;
				height: 40rpx;
				border-radius: 50%;
				border: 4rpx solid #fff;
				display: flex;
				align-items: center;
				justify-content: center;
			}
		}

		.user-info-box {
			.user-nickname {
				font-size: 42rpx;
				color: #fff;
				font-weight: bold;
				display: block;
				margin-bottom: 6rpx;
			}

			.user-account {
				font-size: 24rpx;
				color: rgba(255, 255, 255, 0.7);
				display: block;
				margin-bottom: 12rpx;
			}

			.identity-capsule {
				display: inline-flex;
				align-items: center;
				background: rgba(255, 255, 255, 0.15);
				backdrop-filter: blur(10px);
				padding: 6rpx 20rpx;
				border-radius: 100rpx;

				.dot {
					width: 10rpx;
					height: 10rpx;
					border-radius: 50%;
					margin-right: 12rpx;
				}

				.role-text {
					font-size: 22rpx;
					color: #fff;
					font-weight: 500;
				}
			}
		}
	}

	/* 主体内容区 */
	.main-action-panel {
		flex: 1;
		background: #f8fafc;
		border-radius: 64rpx 64rpx 0 0;
		padding: 50rpx 40rpx 100rpx;

		.vip-benefit-bar {
			background: #fff;
			border-radius: 32rpx;
			padding: 28rpx 32rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;
			box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.03);
			margin-bottom: 48rpx;

			&:active {
				transform: scale(0.98);
			}

			.bar-left {
				display: flex;
				align-items: center;
				gap: 24rpx;

				.vip-icon-bg {
					width: 80rpx;
					height: 80rpx;
					background: #fffcf0;
					border-radius: 20rpx;
					display: flex;
					align-items: center;
					justify-content: center;
				}

				.bar-text {
					display: flex;
					flex-direction: column;

					.t1 {
						font-size: 30rpx;
						color: #333;
						font-weight: bold;
					}

					.t2 {
						font-size: 22rpx;
						color: #999;
						margin-top: 4rpx;
					}
				}
			}

			.bar-btn {
				font-size: 22rpx;
				color: #fff;
				padding: 8rpx 28rpx;
				border-radius: 100rpx;
				font-weight: bold;
			}
		}

		.list-section {
			margin-bottom: 48rpx;

			.section-title {
				font-size: 24rpx;
				color: #94a3b8;
				font-weight: bold;
				margin-bottom: 24rpx;
				padding-left: 10rpx;
				text-transform: uppercase;
				letter-spacing: 1rpx;
			}

			/* 修复布局错乱的关键样式 */
			.list-row-nav,
			.list-row-click {
				display: block;
				width: 100%;
				margin-bottom: 16rpx;
			}

			.row-content {
				background: #fff;
				border-radius: 28rpx;
				padding: 32rpx 36rpx;
				display: flex;
				flex-direction: row;
				align-items: center;
				justify-content: space-between;
				transition: all 0.2s;

				&:active {
					background: #f1f5f9;
				}

				.row-left {
					display: flex;
					flex-direction: row;
					align-items: center;
					gap: 28rpx;

					.label {
						font-size: 28rpx;
						color: #334155;
						font-weight: 500;
					}

					.icon-circle {
						width: 64rpx;
						height: 64rpx;
						border-radius: 50%;
						display: flex;
						align-items: center;
						justify-content: center;

						&.fav {
							background: #eff6ff;
						}

						&.feedback {
							background: #f0fdfa;
						}

						&.contact {
							background: #eef2ff;
						}

						&.idea {
							background: #fff1f2;
						}

						&.set {
							background: #f1f5f9;
						}
					}
				}

				.row-right {
					display: flex;
					flex-direction: row;
					align-items: center;
				}
			}
		}

		.version-label {
			text-align: center;
			color: #cbd5e1;
			font-size: 20rpx;
			margin-top: 60rpx;
		}
	}

	/* 会员弹窗 */
	.premium-modal-box {
		padding: 20rpx 0;
		display: flex;
		flex-direction: column;
		align-items: center;

		.premium-header {
			display: flex;
			flex-direction: column;
			align-items: center;
			margin-bottom: 40rpx;

			.medal-ring {
				width: 130rpx;
				height: 130rpx;
				background: #fffbeb;
				border-radius: 50%;
				display: flex;
				align-items: center;
				justify-content: center;
				margin-bottom: 24rpx;
				border: 2rpx solid #fef3c7;
			}

			.premium-title {
				font-size: 38rpx;
				color: #1a1a1a;
				font-weight: bold;
			}

			.premium-status {
				font-size: 24rpx;
				color: #D97706;
				margin-top: 8rpx;
				font-weight: 500;
			}
		}

		.privilege-list {
			width: 100%;
			background: #f8fafc;
			border-radius: 24rpx;
			padding: 32rpx;
			margin-bottom: 40rpx;

			.p-item {
				display: flex;
				align-items: center;
				gap: 20rpx;
				margin-bottom: 20rpx;
				font-size: 26rpx;
				color: #4b5563;

				&:last-child {
					margin-bottom: 0;
				}
			}
		}

		.premium-pay-section {
			width: 100%;
			display: flex;
			flex-direction: column;
			align-items: center;

			.price-tag {
				font-size: 60rpx;
				color: #D97706;
				font-weight: bold;
				margin-bottom: 32rpx;

				.unit {
					font-size: 32rpx;
				}

				.term {
					font-size: 24rpx;
					color: #9ca3af;
					font-weight: normal;
					margin-left: 12rpx;
				}
			}

			.qr-container {
				display: flex;
				flex-direction: column;
				align-items: center;

				.qr-img {
					width: 360rpx;
					height: 360rpx;
					background: #fff;
					padding: 12rpx;
					border-radius: 16rpx;
					border: 1rpx solid #e5e7eb;
				}

				.qr-tips {
					font-size: 22rpx;
					color: #94a3b8;
					margin-top: 20rpx;
				}
			}
		}
	}

	.qr-pop-content {
		padding: 60rpx;
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 24rpx;
		background: #fff;
		border-radius: 48rpx;

		.img {
			width: 440rpx;
		}

		text {
			font-size: 28rpx;
			color: #64748b;
			font-weight: 500;
		}
	}
</style>