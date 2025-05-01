<template>
	<view class="content">
		<!-- 顶部展示区域 时间 会员信息 图标刷题量 在线时间 -->
		<view class="content-top">
			<view class="user-identity">
				<!-- 限制8个字 -->
				<h2 class="welcome">AI订单用户，下午好！</h2>
			</view>
			<!-- 统计刷题区域 -->
			<view class="content-bottom">
				<view class="day">
					今日已刷<text style="color: #1dd39a;margin-right: 15rpx;font-weight: 600;">10</text>
				</view>
				<view class="count">
					共刷<text style="color: #67bdfc;margin-right: 15rpx;font-weight: 600;">30</text>
				</view>
				<div class="tj">
					待刷<text style="color: #e5d09c;font-weight: 600;">3323</text>
				</div>
			</view>
			<!-- 排名 -->
			<view class="content-db" @click="tapRanking">
				<view class="text-box">
					<text class="sort">刷题量排名：第3名 / 总3人</text>
				</view>
				<image src="../../common/image/zzjb.png" mode="aspectFill"
					style="width: 62rpx;height: 62rpx;margin-right: 68rpx;"></image>
			</view>
		</view>
		<!-- 每日必刷 -->
		<view class="content-center">
			<!-- 标题 -->
			<view class="title-box">
				<uni-icons type="calendar" color="#eca78c" size="30"></uni-icons>
				<h4 class="title-text">每日刷题</h4>
			</view>
			<!-- 列表区域 -->
			<view class="list-box">
				<uni-list :border="true">
					<!-- 自定义右侧内容 -->
					<uni-list-chat class="list" v-for="item in 9" title="你认为Java的优势是什么？" :avatar-list="avatarList"
						note="Java JavaSe Mysql" badge-positon="left">
						<view class="chat-custom-right">
							<text class="chat-custom-text">未刷</text>
							<!-- 需要使用 uni-icons 请自行引入 -->
							<uni-icons type="star-filled" color="#999" size="24"></uni-icons>
						</view>
					</uni-list-chat>
				</uni-list>
			</view>
		</view>
	</view>
</template>

<script setup>
import Java from '../../common/image/java.png'
import {
	ref
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
</script>

<style lang="scss" scoped>
.content {
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	width: 100%;
	height: 100%;

	.content-center {
		padding-top: 30rpx;
		width: 100%;
		height: 100%;

		.title-box {
			padding-left: 30rpx;
			display: flex;
			align-items: center;
			color: #404040;

			.title-text {
				font-weight: 520;
				margin-left: 5rpx;
			}
		}

		.list-box {
			margin-top: 20rpx;

			.chat-custom-right {
				flex: 1;
				/* #ifndef APP-NVUE */
				display: flex;
				/* #endif */
				flex-direction: column;
				justify-content: space-between;
				align-items: flex-end;
			}

			.chat-custom-text {
				font-size: 12px;
				// color: #eca68c;
				color: #999;
			}

			.list {
				height: 120rpx;
			}
		}
	}

	.content-top {
		border-radius: 0 0 10rpx 5rpx;
		padding-top: 80rpx;
		width: 100%;
		height: 250rpx;
		background: linear-gradient(135deg, #272727, #4a4a4a, #6d6d6d, #909090, #b3b3b3, #d6d6d6, #f9f9f9);
		display: flex;
		flex-direction: column;
		justify-content: space-between;

		.content-db {
			display: flex;
			justify-content: space-between;
			font-style: italic;
			color: #846547;
			font-size: 30rpx;
			font-weight: bold;
			margin-left: 35rpx;
			width: 680rpx;
			margin-top: 56rpx;
			height: 70rpx;
			border-radius: 12rpx 12rpx 0 0;
			// background: linear-gradient(135deg, #f6d365, #fda085);
			background: linear-gradient(to right, #f0ceb7, #f4d9c5);

			.text-box {
				padding-left: 38rpx;
				padding-top: 18rpx;
			}
		}

		.content-bottom {
			padding-left: 70rpx;
			display: flex;
			align-items: center;
			color: #dedede;
			font-size: 30rpx;
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