<script setup>
	import {
		ref
	} from 'vue'
	// 筛选
	const screen = ref(true)
	const list = ref([{}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}])
	const current = ref(0)
</script>
<template>
	<view class="ranking">
		<view class="ranking-box">
			<!-- 顶部切换总排行榜和当天排行 -->
			<view class="page">
				<view class="tab">
					<view :class="{'tab-active' : screen}" @click="screen = true"><text>日排行</text></view>
					<view :class="{'tab-active' : !screen}" @click="screen = false"><text>总排行</text></view>
				</view>
				<!-- top3 -->
				<view class="top">
					<view class="top-item">
						<image class="top-item-avatar"
							src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"></image>
						<text class="top-item-name">黄烟尘</text>
						<text class="top-item-score">97</text>
					</view>
					<view class="top-item">
						<image class="top-item-avatar"
							src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"></image>
						<text class="top-item-name">李飞羽</text>
						<text class="top-item-score">100</text>
					</view>
					<view class="top-item">
						<image class="top-item-avatar"
							src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"></image>
						<text class="top-item-name">王猛</text>
						<text class="top-item-score">95</text>
					</view>
				</view>
				<view class="ranking">
					<scroll-view :scroll-top="0" scroll-y="true" style="height: 950rpx;" class="scroll-Y"
						@scrolltoupper="upper" @scrolltolower="lower" @scroll="scroll">
						<!-- 排行榜 -->
						<view class="ranking-list-item" v-for="(item, key) in list" :key="key">
							<text class="ranking-list-number">{{key + 4}}</text>
							<view class="ranking-list-nickname">
								<image src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png">
								</image>
								<text>编号{{key}}</text>
							</view>
							<text class="ranking-list-score">90</text>
						</view>
					</scroll-view>
					<!-- 当前排名 -->
					<view class="current-ranking">
						<text class="ranking-list-number">19</text>
						<view class="ranking-list-nickname">
							<image src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png">
							</image>
							<text>AI用户</text>
						</view>
						<text class="ranking-list-score">90</text>
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
				background: #f2d5c0;
				padding-bottom: 20rpx;

				.tab {
					display: flex;
					justify-content: center;
					padding: 25rpx;
					margin-bottom: 25rpx;
					color: #fff;

					view {
						height: 70rpx;
						width: 220rpx;
						line-height: 70rpx;
						box-sizing: border-box;
						border: 1px solid #fff;
						font-size: 16px;
						text-align: center;
						font-weight: bold;

						&:active {
							background: #fff;
							color: #c0c0c0;
						}

						&:nth-child(1) {
							border-radius: 35rpx 0 0 35rpx;
						}

						&:nth-child(2) {
							border-radius: 0 35rpx 35rpx 0;
						}
					}

					.tab-active {
						background: #fff;
						color: #c0c0c0;
						font-weight: bold;
					}

				}

				.top {
					width: 660rpx;
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
						color: #fff;
						font-weight: bold;

						&:nth-child(1) {
							.top-item-avatar {
								border: 8rpx solid #C0C0C0;
							}
						}

						&:nth-child(2) {
							height: 320rpx;

							.top-item-avatar {
								border: 8rpx solid #FFD700;
							}
						}

						&:nth-child(3) {
							.top-item-avatar {
								border: 8rpx solid #CD7F32;
							}
						}

						.top-item-king {
							width: 45rpx;
							height: 45rpx;
						}

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
					}

					.current-ranking {
						border-radius: 16rpx;
						background-color: #f2d5c0;
						position: fixed;
						bottom: 0;
						left: 0;
						right: 0;
						height: 85rpx;
						padding: 0 10rpx 0 10rpx;
						display: flex;
						align-items: center;
						font-size: 14px;
					}

					.ranking-list-number {
						display: block;
						width: 70rpx;
						color: #777;
					}

					.ranking-list-score {
						display: block;
						width: 70rpx;
						color: #E28935;
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
				}
			}

		}
	}
</style>