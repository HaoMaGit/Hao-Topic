<script setup>
	import {
		ref
	} from 'vue'
	import {
		onLoad
	} from '@dcloudio/uni-app'
	onLoad((options) => {
		// 获取路径参数
		console.log(options.name);
		console.log(options.id);
		// 设置导航标题
		uni.setNavigationBarTitle({
			title: options.name
		})
	})
	// tab标签页
	const isTabs = ref(true)
	// 是否点击了查看
	const isShowAnswer = ref(false)
	// 查看答案
	const showAnswer = () => {
		isShowAnswer.value = true
	}
	// 答案
	const markDownContent = ref(
		'String内部维护的是private final char byte数组， 不可变线程安全 好处 防止被恶意篡改 作为HashMap的key可以保证不可变性 可以实现字符串常量池， 在Java中， 创建字符串对象的方式 通过字符串常量进行创建 在字符串常量池判断是否存在， 如果存在就返回， 不存在就在字符串常量池创建后返回 通过new字符串对象进行创建 在字符串常量池中判断是否存在， 如果不存在就创建， 再判断堆中是否存在， 如果不存在就创建， 然后返回该对象， 总之要保证字符串常量池和堆中都有该对象 String、 StringBuilder、 StringBuffer的区别 String内部维护的是private final char'
	)
	// 题目列表遮罩
	const showRight = ref()
	// 下一题
	const nextQuestion = () => {
		// 更新题目的内容重新调用获取题目的接口
	}
</script>
<template>
	<!-- 题目列表 -->
	<uni-drawer ref="showRight" mode="right" width="360">
		<scroll-view class="scroll-view-box" scroll-y="true" style="height: 100vh;">
			<uni-list>
				<uni-list-item class="item" showArrow v-for="item in 50" clickable @click="goToTopic(item)">
					<!-- 使用 slot 插入头像 -->
					<template #header>
						<span class="title" style="font-size: 25rpx;">JavaJava中的序列化和反序列化是什么</span>
					</template>
				</uni-list-item>
			</uni-list>
		</scroll-view>
	</uni-drawer>

	<view class="topic">
		<view class="topic-box">
			<!-- 题目标题和标签以及收藏 -->
			<view class="topic-top">
				<!-- 标题 -->
				<view class="top-box">
					<h2 class="topic-title">Java中的序列化和反序列化是什么面试题Java中的序列化和反序列化是什么面试题？</h2>
				</view>
				<!-- 标签 -->
				<view class="top-center">
					<view class="tag-box">
						<uni-tag class="tag" style="border:1px solid #21b5ff;color: #21b5ff" :inverted="true"
							text="Java基础" />
						<uni-tag class="tag" style="border:1px solid #21b5ff;color: #21b5ff" :inverted="true"
							text="Java" />
					</view>
					<view class="star-box">
						<!-- 收藏 -->
						<uni-icons type="star" color="#999" size="24"></uni-icons>
						<span style="font-size: 15px;font-weight: 200;color: rgb(153, 153, 153);">收藏</span>
					</view>
				</view>
			</view>
			<!-- 分隔符 -->
			<view style="height: 28rpx;background-color: #fafafa;margin-top: 10rpx;">

			</view>
			<view class="topic-bottom">
				<!-- 标签页 -->
				<view class="tab-box">
					<view :class="{'tab-one':true,'active-tab-box' : isTabs}" @click="isTabs = true">
						精简答案
					</view>
					<view :class="{'tab-one':true,'active-tab-box' : !isTabs}" @click="isTabs = false">
						AI答案
					</view>
				</view>
				<!-- 内容区域 -->
				<view class="topic-answer">
					<!-- 查看答案的按钮 -->
					<view v-if="!isShowAnswer" class="show-btn" @click="showAnswer">
						点击查看答案
					</view>
					<!-- 答案显示区域 -->
					<view v-else class="answer-box">
						<!-- 默认用法 直接传入md文本即可-->
						<zero-markdown-view :markdown="markDownContent"></zero-markdown-view>
					</view>
				</view>
			</view>
			<!-- 操作区域和题目列表 -->
			<view class="topic-operation">
				<view class="operation-top">
					<view style="display: flex;align-items: center;color: #10aeff;" @click="showRight.open()">
						<uni-icons type="list" color="#21b5ff" size="19"></uni-icons>
						<span style="font-size: 16px;margin-left: 5rpx;">Elastracit面试题 (<span
								style="font-weight: bold;">1</span>/55)</span>
					</view>
					<progress :percent="20" activeColor="#10AEFF" stroke-width="6" style="width: 300rpx;" />
				</view>
				<view class="operation-bottom">
					<view class="card-actions-item">
						<uni-icons type="left" size="30" color="#999"></uni-icons>
						<text class="card-actions-item-text" style="color: #999;">上一题</text>
					</view>
					<view class="card-actions-item">
						<uni-icons type="compose" size="33" color="#10aeff"></uni-icons>
						<text class="card-actions-item-text" style="color: #10aeff;">反馈</text>
					</view>
					<view class="card-actions-item" @click="nextQuestion">
						<uni-icons type="right" size="30" color="#10aeff"></uni-icons>
						<text class="card-actions-item-text" style="color: #10aeff;">下一题</text>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>
<style lang="scss" scoped>
	.topic-box {
		.topic-top {
			padding: 22rpx;

			.topic-title {
				font-size: 40rpx;
			}

			.top-center {
				display: flex;
				margin-top: 18rpx;
				justify-content: space-between;

				.tag-box {
					.tag {
						margin-right: 10rpx;
					}
				}

				.star-box {
					display: flex;
				}
			}
		}

		.topic-bottom {
			padding: 28rpx;

			.tab-box {
				font-size: 31rpx;
				display: flex;
				justify-content: space-evenly;
			}

			.active-tab-box {
				padding-bottom: 8rpx;
				color: #21b5ff;
				border-bottom: 1px solid #21b5ff;
			}

			.topic-answer {
				.show-btn {
					margin: 70rpx auto;
					color: #fff;
					border-radius: 40rpx;
					text-align: center;
					line-height: 80rpx;
					width: 280rpx;
					height: 80rpx;
					background-color: #21b5ff;
				}
			}
		}

		.topic-operation {
			border-top: 1px solid #ebebeb;
			position: fixed;
			left: 0;
			bottom: 0;
			right: 0;
			height: 200rpx;
			background-color: #fff;
			box-sizing: border-box;
			padding: 20rpx;

			.operation-top {
				display: flex;
				align-items: center;
				justify-content: space-between;
				height: 40rpx;
			}

			.operation-bottom {
				height: 120rpx;
				display: flex;
				align-items: end;
				justify-content: space-between;

				.card-actions-item {
					font-weight: 500;
					display: flex;
					flex-direction: column;
					align-items: center;
				}
			}
		}
	}
</style>