<script setup>
	import {
		ref
	} from 'vue'
	import {
		toast
	} from '../../uni_modules/uv-ui-tools/libs/function';

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
		setTimeout(function() {
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
		url: 'https://qiniu-web-assets.dcloud.net.cn/unidoc/zh/shuijiao-small.jpg',
		extname: 'png',
		name: 'shuijiao.png'
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
							<view class="text" style="font-size: 38rpx;">
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
			<image src="../../common/image/ewm.png" mode="aspectFill" style="width: 500rpx;height: 500rpx;"></image>
		</uni-popup>

		<!-- 意见反馈的弹层 -->
		<uni-popup ref="feedbackPopup" type="dialog">
			<uni-popup-dialog ref="inputClose" mode="input" title="意见反馈" value="对话框预置提示内容!" placeholder="请输入反馈内容"
				@confirm="dialogInputConfirm"></uni-popup-dialog>
		</uni-popup>

		<!-- 头像位置 -->
		<view class="user-top" style="padding-top: 33rpx;">
			<uni-section title="自定义图片大小" type="line">
				<view class="example-body ">
					<uni-file-picker readonly :value="avatarPreview" :imageStyles="imageStyles" file-mediatype="image">
					</uni-file-picker>
				</view>
			</uni-section>
			<h3 class="username">AI用户</h3>
		</view>
		<!-- 操作列表 -->
		<view class="section">
			<!-- 外层盒子 -->
			<view class="list">
				<!-- 跳转链接 -->
				<!-- 每一行 -->
				<view class="row">
					<view class="left">
						<uni-icons type="vip-filled" size="28" color="#00a9e0"></uni-icons>
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

				<navigator url="/pages/user/list/list?type=0" style="border-bottom: 1px solid #e6e6e6;">
					<view class="row">
						<view class="left">
							<uni-icons type="star-filled" size="28" color="#00a9e0"></uni-icons>
							<view class="text">
								我的收藏
							</view>
						</view>
						<view class="right">
							<uni-icons type="right" size="22" color="#a6a6a6"></uni-icons>
						</view>
					</view>
				</navigator>

				<navigator url="/pages/user/list/list?type=1" style="border-bottom: 1px solid #e6e6e6;">
					<view class="row">
						<view class="left">
							<uni-icons type="eye-filled" size="28" color="#00a9e0"></uni-icons>
							<view class="text">
								我的记录
							</view>
						</view>
						<view class="right">
							<uni-icons type="right" size="22" color="#a6a6a6"></uni-icons>
						</view>
					</view>
				</navigator>

				<navigator url="/pages/user/feedback/feedback" style="border-bottom: 1px solid #e6e6e6;">
					<view class="row">
						<view class="left">
							<uni-icons type="eye-filled" size="28" color="#00a9e0"></uni-icons>
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
							<uni-icons type="gear-filled" size="28" color="#00a9e0"></uni-icons>
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
						<uni-icons type="staff-filled" size="28" color="#00a9e0"></uni-icons>
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
						<uni-icons type="paperplane" size="28" color="#00a9e0"></uni-icons>
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
					color: #858585;
					font-size: 35rpx;
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
			height: 340rpx;
			background: linear-gradient(135deg, #272727, #4a4a4a, #6d6d6d, #909090, #b3b3b3, #d6d6d6, #f9f9f9);
			display: flex;
			align-items: center;
			justify-content: center;
			flex-direction: column;



			.username {
				margin-top: 10rpx;
				font-size: 46rpx;
				color: #f2f2f2;
			}
		}
	}
</style>