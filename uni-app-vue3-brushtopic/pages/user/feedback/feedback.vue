<script setup>
import { ref, onMounted } from 'vue'
import { apiQueryFeedbackList } from '@/api/system/feedback'

// 反馈数据
const feedbackList = ref([
])
const getFeedbackList = async () => {
	uni.showLoading({ title: '加载中...' })
	const res = await apiQueryFeedbackList()
	feedbackList.value = res.data
	uni.hideLoading()
}

onMounted(() => {
	getFeedbackList()
})
</script>

<template>
	<view class="feedback">
		<view class="feedback-list" v-if="feedbackList.length">
			<view class="feedback-item" v-for="item in feedbackList" :key="item.id">
				<view class="feedback-header">
					<view class="left">
						<uni-icons type="chat" size="20" color="#1677ff"></uni-icons>
						<text class="time">{{ item.createTime }}</text>
					</view>
					<view class="status-tag" :class="{ 'replied': item.status == 1 }">
						<uni-icons :type="item.status == 1 ? 'checkmarkempty' : 'waiting'" size="14"
							:color="item.status == 1 ? '#52c41a' : '#1677ff'"></uni-icons>
						<text>{{ item.status == 1 ? '已回复' : '待回复' }}</text>
					</view>
				</view>

				<view class="feedback-body">
					<view class="content">{{ item.feedbackContent }}</view>

					<view v-if="item.status == 1" class="reply">
						<view class="reply-header">
							<view class="admin">
								<uni-icons type="staff" size="16" color="#1677ff"></uni-icons>
								<text>{{ item.replyAccount }}回复</text>
							</view>
							<text class="time">{{ item.replyTime }}</text>
						</view>
						<view class="reply-content">{{ item.replyContent }}</view>
					</view>
				</view>
			</view>
		</view>
		<uv-empty v-else text="还没有反馈哦～有啥意见可以反馈哦！" icon="../../../static/images/empty.png"></uv-empty>
	</view>
</template>

<style lang="scss" scoped>
.feedback {
	min-height: 100vh;
	background-color: #f5f5f5;
	padding: 20rpx;

	.feedback-list {
		.feedback-item {
			background: #fff;
			border-radius: 12rpx;
			padding: 24rpx;
			margin-bottom: 20rpx;
			box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.03);

			.feedback-header {
				display: flex;
				justify-content: space-between;
				align-items: center;
				margin-bottom: 20rpx;
				padding-bottom: 16rpx;
				border-bottom: 1px solid #f0f0f0;

				.left {
					display: flex;
					align-items: center;
					gap: 12rpx;

					.time {
						font-size: 24rpx;
						color: #999;
					}
				}

				.status-tag {
					display: inline-flex;
					align-items: center;
					gap: 6rpx;
					padding: 4rpx 16rpx;
					border-radius: 20rpx;
					font-size: 24rpx;
					background: rgba(22, 119, 255, 0.1);
					color: #1677ff;

					&.replied {
						background: rgba(82, 196, 26, 0.1);
						color: #52c41a;
					}
				}
			}

			.feedback-body {
				.content {
					font-size: 28rpx;
					color: #333;
					line-height: 1.6;
					margin-bottom: 20rpx;
				}

				.reply {
					background: #f8f8f8;
					padding: 20rpx;
					border-radius: 8rpx;

					.reply-header {
						display: flex;
						justify-content: space-between;
						align-items: center;
						margin-bottom: 12rpx;

						.admin {
							display: flex;
							align-items: center;
							gap: 8rpx;
							color: #1677ff;
							font-size: 26rpx;
						}

						.time {
							font-size: 24rpx;
							color: #999;
						}
					}

					.reply-content {
						font-size: 26rpx;
						color: #666;
						line-height: 1.6;
					}
				}
			}
		}
	}
}
</style>