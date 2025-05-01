<script setup>
	import {
		ref
	} from 'vue'
</script>
<template>
	<view class="change-box">
		<!-- 操作列表 -->
		<view class="section">
			<!-- 外层盒子 -->
			<view class="list">
				<!-- 跳转链接 -->
				<!-- 每一行 -->
				<view class="row">
					<view class="left">
						<view class="text">
							原密码
						</view>
					</view>
					<view class="right">
						<input class="uni-input" focus placeholder="请输入原密码" />
					</view>
				</view>
				<view class="pd-box">

				</view>
				<view class="row">
					<view class="left">
						<view class="text">
							新密码
						</view>
					</view>
					<view class="right" @click="nameDialog.open()">
						<input class="uni-input" placeholder="输入新密码" />
					</view>
				</view>

				<view class="row">
					<view class="left">
						<view class="text">
							确认密码
						</view>
					</view>
					<view class="right" @click="goToChange">
						<input class="uni-input" placeholder="确认新密码" />
					</view>
				</view>
			</view>
			<!-- 占位符 -->
			<view class="s-box" style="height: 12rpx;background-color: #eff1f2;">

			</view>
			<uv-button type="primary" text="完成"></uv-button>
		</view>
	</view>
</template>
<style lang="scss" scoped>
	.section {
		width: 690rpx;
		margin: 10rpx auto;
		border: 1px solid #eeeeee;
		border-radius: 10rpx;
		box-shadow: 0 0 30rpx rgba(0, 0, 0, 0.05);

		.list {
			.pd-box {
				height: 16rpx;
				background-color: #eff1f2;
			}

			.row {
				background-color: #fff;
				display: flex;
				align-items: center;
				justify-content: space-between;
				height: 95rpx;
				padding: 0 20rpx;
				position: relative;

				&:last-child {
					border-bottom: 0;
				}

				.left {
					display: flex;
					align-items: center;

					.text {
						padding-left: 10rpx;
						color: #5b5b5b;
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

					.uni-input {
						width: 490rpx;
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
</style>