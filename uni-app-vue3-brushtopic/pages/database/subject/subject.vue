<script setup>
	import {
		ref
	} from 'vue'
	import {
		onLoad
	} from '@dcloudio/uni-app'
	import Java from '../../../common/image/java.png'
	onLoad((options) => {
		// 获取路径参数
		console.log(options.name);
		console.log(options.id);
		// 设置导航标题
		uni.setNavigationBarTitle({
			title: options.name
		})
	})
	const goToTopic = (item) => {
		uni.navigateTo({
			url: `/pages/database/topic/topic?id=1&name=Java专题练习`
		})
	}
</script>
<template>
	<view class="subject">
		<view class="subject-box">
			<!-- 顶部介绍专题以及浏览量和总题目 以及avatar -->
			<view class="subject-top">
				<view class="top-left">
					<image class="avatar" :src="Java" mode="aspectFill"></image>
				</view>
				<view class="top-center">
					<h3 style="font-size: 36rpx;color: #5a5959;">Java基础面试题</h3>
					<span style="font-size: 25rpx;color: #acaaac;">java基础求职八股文</span>
					<p style="font-size: 26rpx;color: #acaaac;margin-top: 20rpx;font-weight: bold;">2.28w次浏览 · 37个面试题
					</p>
				</view>
			</view>
			<view style="height: 20rpx;background-color: #fafafa;margin-top: 3rpx;">

			</view>
			<!-- 全部题目 -->
			<view class="subject-center">
				<scroll-view :scroll-top="0" scroll-y="true" class="scroll-Y" @scrolltoupper="upper"
					@scrolltolower="lower" @scroll="scroll">
					<uni-list>
						<uni-list-item class="item" showArrow v-for="item in 30" clickable @click="goToTopic(item)">
							<!-- 使用 slot 插入头像 -->
							<template #header>
								<span class="title">Java基础面试题</span>
							</template>
						</uni-list-item>
					</uni-list>
				</scroll-view>
			</view>
		</view>
	</view>
</template>
<style lang="scss" scoped>
	.subject-box {

		.subject-top {
			display: flex;
			padding: 21rpx;
			height: 130rpx;

			.top-left {

				.avatar {
					width: 130rpx;
					height: 130rpx;
					padding-right: 26rpx;
				}
			}
		}

		.subject-center {
			.scroll-Y {
				height: 100%;
			}

			.item {
				.title {
					font-size: 30rpx;
					color: #757575;
				}
			}
		}
	}
</style>