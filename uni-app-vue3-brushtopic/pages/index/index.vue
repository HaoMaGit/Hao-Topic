<template>
	<view class="content" :style="{ background: getPageGradient }">
		<!-- 顶部展示区域 时间 会员信息 图标刷题量 在线时间 -->
		<view class="content-top">
			<view class="user-identity">
				<!-- 限制8个字 -->
				<h2 class="welcome" :style="{ color: getTextColor }">AI订单用户，下午好！</h2>
			</view>
			<!-- 统计刷题区域 -->
			<view class="content-bottom" :style="{ color: getTextColor }">
				<view class="count">
					今日已刷次数<text class="weight" style="color: #8a9ba8;">10</text>
				</view>
				<view class="count">
					今日已刷题<text class="weight" :style="{ color: getTextColor }">10</text>
				</view>
				<view class="count">
					共刷题<text class="weight" style="color: #1677ff;">30/<span
							style="color: #0056b3;font-weight: bold;">100</span></text>
				</view>
			</view>
			<!-- 排名 -->
			<view class="content-db" @click="tapRanking">
				<view class="text-box">
					<text class="sort" :style="{ color: getTextColor }">刷题次数排名：第3名 / 总3人</text>
				</view>
				<uni-icons type="arrow-right" size="24" :color="getTextColor" class="clickable-icon"></uni-icons>
				<image class="rank-img" src="../../common/image/zzjb.png" mode="aspectFill"></image>
			</view>
		</view>
		<!-- 每日必刷 -->
		<view class="content-center">
			<!-- 标题 -->
			<view class="title-box">
				<uni-icons type="calendar" :style="{ color: getTextColor }" size="30"></uni-icons>
				<h4 class="title-text" :style="{ color: getTextColor }">每日刷题</h4>
			</view>
			<!-- 列表区域 -->
			<view class="list-box">
				<view class="list-wrapper">
					<view class="list-item" v-for="item in 9" :key="item">
						<view class="item-content">
							<view class="item-right">
								<text class="title">你认为Java的优势是什么？</text>
								<uni-icons :type="item.isFavorite ? 'star-filled' : 'star'" size="24" color="#1677ff"></uni-icons>
							</view>
							<view class="info-row">
								<view class="tags-row">
									<view class="tag">Java</view>
									<view class="tag">JavaSE</view>
									<view class="tag">MySQL</view>
								</view>
								<text class="status-text">未刷</text>
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script setup>
import Java from '../../common/image/java.png'
import {
	ref, computed
} from 'vue'

const avatarList = ref([{
	url: Java
}])
// 点击了排名
const tapRanking = () => {
	uni.navigateTo({
		url: '/pages/index/ranking/ranking'
	})
}
// 当前身份
const role = ref(uni.getStorageSync('role'))
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
</script>

<style lang="scss" scoped>
.content {
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	width: 100%;

	.content-center {
		padding-top: 30rpx;
		width: 100%;
		height: 100%;

		.title-box {
			margin-bottom: 5rpx;
			padding-left: 30rpx;
			display: flex;
			align-items: center;
			color: #1677ff;

			.title-text {
				font-weight: 520;
				margin-left: 5rpx;
			}
		}

		.list-box {
			.list-wrapper {
				.list-item {
					background: #fff;
					border-radius: 16rpx;
					padding: 30rpx;
					margin-bottom: 5rpx;
					display: flex;
					align-items: flex-start;
					justify-content: space-between;
					box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
					transition: all 0.3s ease;

					&:active {
						transform: scale(0.98);
					}

					.item-content {
						flex: 1;

						.item-right {
							display: flex;
							justify-content: space-between;
						}

						.info-row {
							padding-top: 15rpx;
							display: flex;
							justify-content: space-between;
							align-items: center;

							.tags-row {
								display: flex;
								flex-wrap: wrap;
								gap: 12rpx;

								.tag {
									padding: 6rpx 20rpx;
									background: rgba(22, 119, 255, 0.08);
									color: #1677ff;
									font-size: 24rpx;
									border-radius: 24rpx;
									font-weight: 500;
								}
							}

							.status-text {
								font-size: 24rpx;
								color: #999;
							}
						}
					}
				}
			}
		}
	}

	.content-top {
		border-radius: 0 0 10rpx 5rpx;
		padding-top: 80rpx;
		width: 100%;
		height: 250rpx;
		display: flex;
		flex-direction: column;
		border-radius: 0rpx 5rpx 5rpx 5rpx;
		justify-content: space-between;

		.content-db {
			font-style: italic;
			font-size: 30rpx;
			font-weight: bold;
			display: flex;
			align-items: center;
			justify-content: space-between;
			background: rgba(255, 255, 255, 0.2);
			padding: 10rpx;
			border-radius: 8rpx;
			box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.1);

			.text-box {
				padding-left: 58rpx;
			}

			.rank-img {
				width: 62rpx;
				height: 62rpx;
				margin-right: 68rpx;
			}
		}

		.content-bottom {
			padding-left: 70rpx;
			display: flex;
			align-items: center;
			color: #dedede;
			font-size: 30rpx;

			.weight {
				font-weight: bold;
				margin-right: 15rpx;
			}
		}


		.user-identity {
			padding-left: 70rpx;
			display: flex;
			align-items: center;

			.welcome {
				font-size: 45rpx;
				color: #dedede;
			}

		}
	}
}
</style>