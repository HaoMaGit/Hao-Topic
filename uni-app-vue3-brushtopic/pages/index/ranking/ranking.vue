<script setup>
import {
	ref, computed, onMounted
} from 'vue'
import { apiQueryRank, apiQueryUserRank } from '@/api/home'
// 筛选
const screen = ref(true)

// 排行榜
const rankList = ref()
// 前3
const top3 = ref([])
// 用户信息
const userInfo = ref(JSON.parse(uni.getStorageSync('h5UserInfo')))
// 当前用户排行榜
const currentRank = ref()
// 查询排行榜
const getRank = async (type) => {
	uni.showLoading()
	const res = await apiQueryRank(type)
	if (res.data) {
		rankList.value = res.data
		// 提取前三个
		top3.value = rankList.value.slice(0, 3)
		// 提取剩余的
		rankList.value = rankList.value.slice(3);
	}
	uni.hideLoading()
}
// 获取当前用户排名
const getUserRank = async (type) => {
	const res = await apiQueryUserRank(type)
	if (res.data) {
		currentRank.value = res.data
	}
}

onMounted(() => {
	if (screen.value === true) {
		getRank(1)
		getUserRank(1)
	} else {
		getRank(2)
		getUserRank(2)
	}
})

const list = ref([{}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}])
const current = ref(0)
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
</script>
<template>
	<view class="ranking">
		<view class="ranking-box" :style="{ background: getPageGradient }">
			<!-- 顶部切换总排行榜和当天排行 -->
			<view class="page">
				<view class="tab">
					<view :class="{ 'tab-active': screen }" @click="screen = true, getRank(1), getUserRank(1)"><text>日排行</text>
					</view>
					<view :class="{ 'tab-active': !screen }" @click="screen = false, getRank(2), getUserRank(2)"><text>总排行</text>
					</view>
				</view>
				<!-- top3 -->
				<view class="top">
					<view class="top-item" :style="{ opacity: top3[1] ? 1 : 0 }">
						<image v-if="top3[1]?.avatar" class="top-item-avatar" :src="top3[1]?.avatar"
							style="border: 4rpx solid #C0C0C0;">
						</image>
						<uv-avatar v-else size="69" style="border: 4rpx solid #C0C0C0;" :text="top3[1]?.nickname.charAt(0)"
							fontSize="18" randomBgColor></uv-avatar>
						<text class="top-item-name">{{ top3[1]?.nickname }}</text>
						<text class="top-item-score">{{ top3[1]?.scope }}</text>
					</view>
					<view class="top-item" :style="{ paddingBottom: '20rpx', opacity: top3[0] ? 1 : 0 }">
						<image v-if="top3[0]?.avatar" class="top-item-avatar" :src="top3[0]?.avatar"
							style="border: 4rpx solid #FFD700;">
						</image>
						<uv-avatar v-else size="69.6" style="border: 4rpx solid #FFD700;" :text="top3[0]?.nickname.charAt(0)"
							fontSize="18" randomBgColor></uv-avatar>
						<text class="top-item-name">{{ top3[0]?.nickname }}</text>
						<text class="top-item-score">{{ top3[0]?.scope }}</text>
					</view>
					<view class="top-item" :style="{ opacity: top3[2] ? 1 : 0 }">
						<image v-if="top3[2]?.avatar" class="top-item-avatar" :src="top3[2]?.avatar"
							style="border: 4rpx solid #CD7F32;">
						</image>
						<uv-avatar v-else size="69.6" style="border: 4rpx solid #CD7F32;" :text="top3[2]?.nickname.charAt(0)"
							fontSize="18" randomBgColor></uv-avatar>
						<text class="top-item-name">{{ top3[2]?.nickname }}</text>
						<text class="top-item-score">{{ top3[2]?.scope }}</text>
					</view>
				</view>
				<view class="ranking">
					<scroll-view :scroll-top="0" scroll-y="true" style="height: 950rpx;" class="scroll-Y" @scrolltoupper="upper"
						@scrolltolower="lower" @scroll="scroll">
						<!-- 排行榜 -->
						<view v-if="rankList && rankList.length !== 0" class="ranking-list-item" v-for="(item, key) in rankList"
							:key="key">
							<text class="ranking-list-number">{{ key + 4 }}</text>
							<view class="ranking-list-nickname">
								<image v-if="item.avatar" :src="item.avatar">
								</image>
								<uv-avatar v-else size="69" style="border: 4rpx solid #C0C0C0;" :text="item.nickname.charAt(0)"
									fontSize="18" randomBgColor></uv-avatar>
								<text>{{ item.nickname }}</text>
							</view>
							<text class="ranking-list-score">{{ item.scope }}</text>
						</view>
						<uv-empty v-else text="还没有人上榜～快去刷题，成为第一个霸榜的人！🔥" icon="../../../static/images/empty.png"></uv-empty>
					</scroll-view>
					<!-- 当前排名 -->
					<view class="current-ranking">
						<text class="ranking-list-number current">{{ currentRank?.rank }}</text>
						<view class="ranking-list-nickname ">
							<image v-if="currentRank?.avatar" :src="currentRank?.avatar">
							</image>
							<uv-avatar size="10" v-else :text="currentRank?.nickname.charAt(0)" fontSize="18"
								randomBgColor></uv-avatar>
							<text>{{ currentRank?.nickname }}</text>
						</view>
						<text class="ranking-list-score">{{ currentRank?.scope }}</text>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>
<style lang="scss" scoped>
.ranking {
	width: 100%;

	.ranking-box {
		.page {
			padding-bottom: 20rpx;

			.tab {
				display: flex;
				justify-content: center;
				padding: 20rpx;
				margin-bottom: 20rpx;
				color: #1677ff;

				view {
					height: 60rpx;
					width: 200rpx;
					line-height: 60rpx;
					box-sizing: border-box;
					border: 1px solid #1677ff;
					font-size: 16px;
					text-align: center;
					font-weight: bold;
					color: #1677ff;

					&:active {
						background: #1677ff;
						color: #fff;
					}

					&:nth-child(1) {
						border-radius: 30rpx 0 0 30rpx;
					}

					&:nth-child(2) {
						border-radius: 0 30rpx 30rpx 0;
					}
				}

				.tab-active {
					background: #1677ff;
					color: #fff;
					font-weight: bold;
				}
			}

			.top {
				width: 660rpx;
				height: 320rpx;
				margin: auto;
				display: flex;
				justify-content: space-between;
				align-items: flex-end;

				.top-item {
					width: 200rpx;
					height: 300rpx;
					display: flex;
					flex-direction: column;
					position: relative;
					align-items: center;
					border-radius: 100rpx 100rpx 0 0;
					color: #1677ff;
					font-weight: bold;

					.top-item-avatar {
						border-radius: 50%;
						width: 140rpx;
						height: 140rpx;
					}


					.top-item-name {
						margin: 10rpx 0;
					}

					.top-item-score {
						font-size: 16px;
					}
				}
			}

			.ranking {
				width: 700rpx;
				border-radius: 30rpx;
				margin: auto;
				background: #fff;
				box-sizing: border-box;
				padding: 20rpx;

				.ranking-list-item {
					height: 105rpx;
					display: flex;
					align-items: center;
					font-size: 14px;
					color: #1677ff;
				}

				.current-ranking {
					border-radius: 16rpx;
					background-color: #f9f9f9;
					position: fixed;
					bottom: 0;
					left: 0;
					right: 0;
					height: 85rpx;
					padding: 0 10rpx 0 10rpx;
					display: flex;
					align-items: center;
					justify-content: center;
					font-size: 14px;
					color: #1677ff;
				}

				.ranking-list-number {
					display: block;
					width: 70rpx;
					color: #777;
				}

				.ranking-list-score {
					display: block;
					width: 70rpx;
					color: #1677ff;
					font-size: 16px;
				}

				.ranking-list-nickname {
					display: flex;
					align-items: center;
					width: calc(100% - 140rpx);

					image {
						width: 80rpx;
						height: 80rpx;
						border-radius: 50%;
						margin-right: 20rpx;
					}

					text {
						width: auto;
					}
				}

				.current {
					padding-left: 40rpx;
				}
			}
		}
	}
}
</style>