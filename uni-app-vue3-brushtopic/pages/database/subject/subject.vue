<script setup>
import {
	ref
} from 'vue'
import {
	onLoad
} from '@dcloudio/uni-app'
import Java from '../../../common/image/java.png'
import { apiQuerySubjectDetail } from '@/api/topic/subject'
onLoad((options) => {
	// 获取路径参数
	console.log(options.name);
	console.log(options.id);
	// 设置导航标题
	uni.setNavigationBarTitle({
		title: options.name
	})
	getSubjectDetail(options.id)
})
const goToTopic = (item) => {
	uni.navigateTo({
		url: `/pages/database/topic/topic?id=1&name=Java专题练习`
	})
}
// 列表
const subjectDetail = ref([])
const getSubjectDetail = async (id) => {
	uni.showLoading({
		title: '加载中...'
	})
	const res = await apiQuerySubjectDetail(id)
	subjectDetail.value = res.data
	console.log(subjectDetail.value);
	uni.hideLoading()
}
</script>
<template>
	<view class="subject">
		<view class="subject-box" v-if="subjectDetail && subjectDetail.length !== 0">
			<!-- 顶部介绍专题以及浏览量和总题目 以及avatar -->
			<view class="subject-top">
				<view class="top-left">
					<image class="avatar" :src="subjectDetail.imageUrl" mode="aspectFill"></image>
				</view>
				<view class="top-center">
					<h3 class="title">{{ subjectDetail.subjectName }}</h3>
					<span class="desc">{{ subjectDetail.subjectDesc }}</span>
					<p class="count">{{ subjectDetail.viewCount
						}}次浏览
						· {{ subjectDetail.topicCount }}个面试题
					</p>
				</view>
			</view>
			<view class="subject-line" />

			<!-- 全部题目 -->
			<view class="subject-center" v-if="subjectDetail.topicNameVos && subjectDetail.topicNameVos.length !== 0">
				<scroll-view :scroll-top="0" scroll-y="true" class="scroll-Y" @scrolltoupper="upper" @scrolltolower="lower"
					@scroll="scroll">
					<uni-list>
						<uni-list-item class="item" showArrow v-for="item in subjectDetail.topicNameVos" clickable
							@click="goToTopic(item)">
							<!-- 使用 slot 插入头像 -->
							<template #header>
								<span class="title">{{ item.topicName }}</span>
							</template>
						</uni-list-item>
					</uni-list>
				</scroll-view>
			</view>
			<uv-empty v-else text="系统正在收录题目数据哦～" icon="../../../static/images/empty.png"></uv-empty>
		</view>
		<uv-empty v-else text="系统正在完善专题信息哦～" icon="../../../static/images/empty.png"></uv-empty>
	</view>
</template>
<style lang="scss" scoped>
.subject-box {
	.subject-line {
		height: 20rpx;
		background-color: #fafafa;
		margin-top: 3rpx;
	}

	.subject-top {
		display: flex;
		padding: 21rpx;
		height: 130rpx;

		.top-left {
			.avatar {
				width: 130rpx;
				height: 100%;
				margin-right: 26rpx;
				border-radius: 10rpx;
				object-fit: cover;
			}
		}

		.title {
			font-size: 36rpx;
			color: #5a5959;
		}

		.desc {
			font-size: 25rpx;
			color: #acaaac;
		}

		.count {
			font-size: 26rpx;
			color: #acaaac;
			margin-top: 20rpx;
			font-weight: bold;
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