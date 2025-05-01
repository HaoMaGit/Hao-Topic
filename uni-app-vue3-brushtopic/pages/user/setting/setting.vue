<script setup>
	import {
		ref
	} from 'vue'
	// 头像的样式
	const imageStyles = ref({
		'width': 30,
		'height': 30,
		"border": false, // 是否显示边框
	})

	// 图片上传成功的回调
	const uploadSuccess = (e) => {
		console.log(e);
		console.log('上传成功');
	}

	// 修改名称对话框
	const nameDialog = ref()

	// 跳转修改密码
	const goToChange = () => {
		uni.navigateTo({
			url: '/pages/change/change'
		})
	}

	// 清除数据
	const alertDialog = ref()
</script>
<template>
	<view class="setting-box">
		<!-- 清除数据的弹层 -->
		<uni-popup ref="alertDialog" type="dialog">
			<uni-popup-dialog type="error" cancelText="取消" confirmText="清除" title="清除数据"
				content="确定要清除刷题记录以及所有的数据"></uni-popup-dialog>
		</uni-popup>
		<!-- 修改名称的弹层 -->
		<uni-popup ref="nameDialog" type="dialog">
			<uni-popup-dialog ref="inputClose" mode="input" title="修改名称" value="对话框预置提示内容!" placeholder="AI用户"
				@confirm="dialogInputConfirm"></uni-popup-dialog>
		</uni-popup>
		<!-- 操作列表 -->
		<view class="section">
			<!-- 外层盒子 -->
			<view class="list">
				<!-- 跳转链接 -->
				<!-- 每一行 -->
				<view class="row">
					<view class="left">
						<view class="text">
							修改头像
						</view>
					</view>
					<view class="right">
						<uni-file-picker @select="uploadSuccess" limit="1" :del-icon="false" disable-preview
							:imageStyles="imageStyles" file-mediatype="image">
							<uni-icons type="right" size="22" color="#a6a6a6"></uni-icons>
						</uni-file-picker>
					</view>
				</view>

				<view class="row">
					<view class="left">
						<view class="text">
							修改名称
						</view>
					</view>
					<view class="right" @click="nameDialog.open()">
						<uni-icons type="right" size="22" color="#a6a6a6"></uni-icons>
					</view>
				</view>

				<view class="row">
					<view class="left">
						<view class="text">
							修改密码
						</view>
					</view>
					<view class="right" @click="goToChange">
						<uni-icons type="right" size="22" color="#a6a6a6"></uni-icons>
					</view>
				</view>
				<view class="row">
					<view class="left">
						<view class="text">
							清除数据
						</view>
					</view>
					<view class="right" @click="alertDialog.open()">
						<uni-icons type="right" size="22" color="#a6a6a6"></uni-icons>
					</view>
				</view>
			</view>
			<!-- 按钮的占位符 -->
			<view class="btn-box">

			</view>
			<!-- 退出登录 -->
			<uv-button :plain="true">
				<template v-slot:default>
					<text style="color: black;">退出登录</text>
				</template>
			</uv-button>
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

		.pdContent {
			width: 200rpx;
			height: 400rpx;
			background-color: pink;
		}

		.btn-box {
			height: 10rpx;
			background-color: #eff0f3;
		}

		.list {
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
						padding-left: 20rpx;
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