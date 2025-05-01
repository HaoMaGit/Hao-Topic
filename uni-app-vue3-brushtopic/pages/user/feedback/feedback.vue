<script setup>
	import {
		ref
	} from 'vue'
</script>
<template>
	<view class="feedback">
		<view class="feedback-box">
			<!-- 列表区域 -->
			<view class="list-box">
				<!-- 每个列表 -->
				<view class="box">
					<uni-section title="卡片标题+额外信息" type="line" v-for="i in 10">
						<uni-card title="这是一个基础卡片示例，此示例展示了一个标题加标题额外信息的标准卡片。" extra="2024:02:10 22:22"
							style="margin: 0;padding: 3rpx; margin-bottom: 5rpx;">
							<text class="uni-body">这是一个基础卡片示例，此示例展示了一个标题加标题额外信息的标准卡片。</text>
							<view slot="actions" class="card-actions">
								<view class="card-actions-item" @click="actionsClick('分享')">
									<uni-icons type="info" size="18" color="#999"></uni-icons>
									<text class="card-actions-item-text">反馈</text>
								</view>
								<view class="card-actions-item" @click="actionsClick('点赞')">
									<uni-icons type="close" size="18" color="#999"></uni-icons>
									<text class="card-actions-item-text">关闭</text>
								</view>
								<view class="card-actions-item" @click="actionsClick('评论')">
									<uni-icons type="chatbubble" size="18" color="#999"></uni-icons>
									<text class="card-actions-item-text">未回复</text>
								</view>
							</view>
						</uni-card>
					</uni-section>
				</view>
			</view>
		</view>
	</view>
</template>
<style lang="scss" scoped>
	.list-box {

		.box {

			.card-actions {
				margin-top: 20rpx;
				height: 40rpx;
				width: 100%;
				display: flex;
				align-items: end;
				justify-content: space-around;
				border-top: 1px #eee solid;

				.card-actions-item {
					height: 20rpx;
					display: flex;
					flex-direction: row;
					align-items: center;
				}

				.card-actions-item-text {
					font-size: 12px;
					color: #666;
					margin-left: 5px;
				}
			}

		}
	}
</style>