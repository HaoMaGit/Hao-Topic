<script setup>
	import {
		ref
	} from 'vue'
	import {
		toast
	} from '../../uni_modules/uv-ui-tools/libs/function';
	// 输入给ai的对话
	const aiValue = ref('')
	// 是否发送
	const isSend = ref(false)
	// 开始对话
	const dialogue = () => {
		if (aiValue.value.trim('') === '') {
			toast("请输入您的问题")
		} else {
			toast('发送成功')
			isSend.value = true
		}
	}
	// 取消对话
	const cancelDialogue = () => {
		isSend.value = false
	}

	// 选择设置
	const aiSetting = ref(true)
</script>
<template>
	<view class="ai-box">
		<!-- 历史记录和搜索 -->
		<view class="ai-history">
			<text :class="{'ai-style':aiSetting}" @click="aiSetting = true">对话</text>
			<text :class="{'ai-style':!aiSetting}" style="margin-left: 30rpx;" @click="aiSetting = false">历史</text>
		</view>
		<!-- ai输出内容需要判断 -->
		<view class="ai-content" v-if="aiSetting">
			<scroll-view :scroll-top="0" scroll-y="true" class="scroll-Y" @scrolltoupper="upper">
				<view class="item" v-for="item in 10">

				</view>
			</scroll-view>
		</view>
		<!-- ai历史记录 -->
		<view class="ai-content" v-else>
			<scroll-view :scroll-top="0" scroll-y="true" class="scroll-Y" @scrolltoupper="upper">
				<!-- 头像显示圆点 -->
				<uni-list-chat clickable v-for="(item,index) in 15" title="测试标题撒旦发射点发射点111111" :avatar="AiAvatar"
					note="您收到一条新的消息您收到一条新的消息您收到一条新的消息您收到一条新的消息您收到一条新的消息" time="2020-02-02 12:24"
					badge-positon="left"></uni-list-chat>
			</scroll-view>
		</view>
		<!-- ai搜索区域 -->
		<view class="ai-search" v-if="aiSetting">
			<!-- 搜索框 -->
			<uv-input placeholder="对话" class="ai-input" v-model="aiValue">
				<!-- vue3模式下必须使用v-slot:prefix -->
				<template v-slot:prefix>
					<!-- <image src="../../common/image/ai-icon.png" mode="aspectFill" style="width: 40rpx;height: 40rpx;"> -->
					<!-- </image> -->
				</template>
				<template v-slot:suffix>
					<!-- 发送 -->
					<!-- <image v-if="!isSend" @click="dialogue()" src="../../common/image/ai-send.png" mode="aspectFill"
						style="width: 76rpx;height: 76rpx;">
					</image> -->
					<!-- 取消发送 -->
					<!-- <image v-else @click="cancelDialogue()" src="../../common/image/stop.png" mode="aspectFill"
						style="width: 76rpx;height: 76rpx;">
					</image> -->
				</template>
			</uv-input>
		</view>

		<view class="ai-search" v-else>
			<!-- 搜索框 -->
			<uv-input placeholder="搜索我的历史" class="ai-input" v-model="aiValue">
				<!-- vue3模式下必须使用v-slot:prefix -->
				<template v-slot:prefix>
					<!-- <image src="../../common/image/ai-icon.png" mode="aspectFill" style="width: 40rpx;height: 40rpx;">
					</image> -->
				</template>
				<template v-slot:suffix>
					<uni-icons type="contact" color="#00a0e9" size="38"></uni-icons>
				</template>
			</uv-input>
		</view>
	</view>
</template>
<style lang="scss" scoped>
	.ai-box {
		padding: 15rpx;
		display: flex;
		flex-direction: column;

		.ai-history {
			border-radius: 0 0 5rpx 5rpx;

			.ai-style {
				color: black;
				font-weight: 520;
			}

			color: #2c313c;
			font-size: 35rpx;
			display: flex;
			align-items: center;
			justify-content: center;
			height: calc(100vh - 94vh);
			background-color: #fff;
		}

		/* 减去 tabbar 的高度 */
		/* 确保 ai-box 占满整个视口高度 */
		.ai-content {
			padding: 3rpx;
			height: calc(100vh - 20vh);
			background-color: #f6f7fb;

			.scroll-Y {
				height: 100%;
			}

			.item {
				margin-top: 10rpx;
				height: 300rpx;
				background-color: pink;
			}
		}

		.ai-search {
			box-shadow: 0 4px 4px rgba(0, 0, 0, 0.1);

			.ai-input {
				padding: 20rpx;
			}
		}
	}
</style>