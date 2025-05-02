<script setup>
import {
	ref, onMounted, computed
} from 'vue'
import { apiGetRoleDetail } from '@/api/system/role'
import { apiGetUserInfo } from '@/api/auth/index'
import { onShow } from '@dcloudio/uni-app'
// 用户信息
const userInfo = ref(JSON.parse(uni.getStorageSync('h5UserInfo')))
// 当前身份
const role = ref(uni.getStorageSync('role'))
// 角色信息
const roleDetail = ref(null)
// 获取角色详情
const getRoleDetail = async () => {
	const res = await apiGetRoleDetail(role.value)
	roleDetail.value = res
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
		2: '#ffd700',
		1: '#ff6b6b',
		0: '#ffffff'
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
})


// 会员对话框
const memberModal = ref()
// 解锁会员
const unlockMember = () => {
	memberModal.value.open()
}

// 去支付
const goToPay = () => {
	memberModal.value.close()
	uni.showLoading({
		title: "支付成功"
	})
	setTimeout(function () {
		uni.hideLoading();
	}, 200);
}

// 联系我们的对话框
const contactUsPopup = ref()
// 联系我们
const contactUs = () => {
	contactUsPopup.value.open()
}

// 意见反馈的对话框
const feedbackPopup = ref()
// 点击了提交
const dialogInputConfirm = () => {
	toast("提交成功")
}

// 预览头像
const avatarPreview = ref([{
	url: userInfo.avatar,
}])
// 头像的样式
const imageStyles = ref({
	width: 110,
	height: 110,
	border: {
		radius: '50%'
	}
})
</script>
<template>
	<view class="user-box">
		<!-- 会员对话框 -->
		<uv-modal ref="memberModal" title="会员服务" confirmText="去支付" @confirm="goToPay">
			<template #default>
				<view class="modal-box">
					<view class="member-box">
						<view class="left">
							<uni-icons type="vip" size="38" color="#f3d5c0"></uni-icons>
							<view class="text">
								永久会员
							</view>
						</view>
						<view class="right" @click="unlockMember">
							<view class="text">
								￥88.88
							</view>
						</view>
					</view>
					<view class="bottom">
						永久无限制的使用所有服务
					</view>
				</view>
			</template>
		</uv-modal>

		<!-- 联系我们的弹层 -->
		<uni-popup ref="contactUsPopup" background-color="#fff">
			<image src="../../common/image/ewm.png" class="image-style" mode="aspectFill"></image>
		</uni-popup>

		<!-- 意见反馈的弹层 -->
		<uni-popup ref="feedbackPopup" type="dialog">
			<uni-popup-dialog ref="inputClose" mode="input" title="意见反馈" value="对话框预置提示内容!" placeholder="请输入反馈内容"
				@confirm="dialogInputConfirm"></uni-popup-dialog>
		</uni-popup>

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
					<text class="nickname">{{ userInfo?.nickname || '暂无昵称' }}</text>
					<text class="account-tag">账户: {{ userInfo.account }}</text>
				</view>
				<view class="role-wrap" v-if="roleDetail">
					<view class="role-badge">
						<uni-icons :type="getRoleIcon" size="16" :color="getRoleColor"></uni-icons>
						<text class="role-name">{{ roleDetail?.name }}</text>
					</view>
					<text class="role-desc">{{ roleDetail?.remark }}</text>
				</view>
			</view>
		</view>
		<!-- 操作列表 -->
		<view class="section">
			<!-- 外层盒子 -->
			<view class="list">
				<!-- 跳转链接 -->
				<!-- 每一行 -->
				<view class="row">
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

				<navigator url="/pages/user/list/list" class="border-row">
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
					<view class="right" @click="feedbackPopup.open()">
						<uni-icons type="right" size="22" color="#a6a6a6"></uni-icons>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>
<style lang="scss" scoped>
.user-box {
	.modal-box {
		padding: 10rpx;
		background-color: #f4f4f4;
		width: 100%;
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
		border-radius: 10rpx;
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
		padding: 60rpx 0;
		background: linear-gradient(135deg, #1677ff, #4096ff);
		display: flex;
		align-items: center;
		justify-content: center;
		flex-direction: column;
		position: relative;
		overflow: hidden;

		&::after {
			content: '';
			position: absolute;
			top: 0;
			left: 0;
			right: 0;
			bottom: 0;
			background: radial-gradient(circle at top right, rgba(255, 255, 255, 0.1) 0%, transparent 60%);
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
						color: #fff;
						font-weight: 500;
					}
				}

				.role-desc {
					font-size: 24rpx;
					color: rgba(255, 255, 255, 0.9);
				}
			}
		}
	}
}
</style>