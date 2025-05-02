<script setup>
import { ref } from 'vue'

// 模拟反馈数据
const feedbackList = ref([
	{
		id: 1,
		content: '希望能够增加更多中等难度的题目，目前简单题偏多。',
		time: '2024-02-10 22:22',
		isReplied: true,
		reply: '感谢您的建议，我们会在下次更新中增加更多中等难度的题目。',
		replyTime: '2024-02-11 09:30' // 添加回复时间
	},
	{
		id: 2,
		content: '建议优化移动端的显示效果，某些页面不够友好。',
		time: '2024-02-09 15:30',
		isReplied: false
	}
])
</script>

<template>
	<view class="feedback">
	  <view class="feedback-list">
		<view class="feedback-item" v-for="item in feedbackList" :key="item.id">
		  <view class="feedback-header">
			<view class="left">
			  <uni-icons type="chat" size="20" color="#1677ff"></uni-icons>
			  <text class="time">{{ item.time }}</text>
			</view>
			<view class="status-tag" :class="{ 'replied': item.isReplied }">
			  <uni-icons :type="item.isReplied ? 'checkmarkempty' : 'waiting'" size="14"
				:color="item.isReplied ? '#52c41a' : '#1677ff'"></uni-icons>
			  <text>{{ item.isReplied ? '已回复' : '待回复' }}</text>
			</view>
		  </view>
		  
		  <view class="feedback-body">
			<view class="content">{{ item.content }}</view>
			
			<view v-if="item.isReplied" class="reply">
			  <view class="reply-header">
				<view class="admin">
				  <uni-icons type="staff" size="16" color="#1677ff"></uni-icons>
				  <text>官方回复</text>
				</view>
				<text class="time">{{ item.replyTime }}</text>
			  </view>
			  <view class="reply-content">{{ item.reply }}</view>
			</view>
		  </view>
		</view>
	  </view>
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