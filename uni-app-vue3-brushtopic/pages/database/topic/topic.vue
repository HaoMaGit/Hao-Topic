<script setup>
import {
	ref
} from 'vue'
import {
	onLoad
} from '@dcloudio/uni-app'
import { apiQuerySubjectDetail } from '@/api/topic/subject'
import { apiQueryTopicDetail, apiQueryTopicAnswer } from '@/api/topic/topic'
onLoad(async (options) => {
	// 获取路径参数
	console.log(options.name);
	console.log(options.id);
	console.log(options.subjectId);
	currentTopicId.value = options.id
	// 设置导航标题
	uni.setNavigationBarTitle({
		title: options.name
	})
	// 显示加载提示
	uni.showLoading({
		title: '加载中...',
		mask: true
	})
	try {
		// 并行请求数据
		await Promise.all([
			getTopicList(options.subjectId),
			getTopicDetail(options.id)
		])
	} catch (error) {
		console.error('加载失败:', error)
		uni.showToast({
			title: '加载失败，请重试',
			icon: 'none'
		})
	} finally {
		uni.hideLoading()
	}
})
// 当前题目列表详情
const subjcetDetail = ref({})
// 当前题目索引
const currentIndex = ref(null)
// 当前题目id
const currentTopicId = ref(null)
// 题目总数
const total = ref(0)
// 重新查询题目列表信息
const getTopicList = async (subjectId) => {
	const res = await apiQuerySubjectDetail(subjectId)
	if (res.data) {
		if (res.data.topicNameVos) {
			subjcetDetail.value = res.data.topicNameVos
			total.value = res.data.topicNameVos.length
			// 找到当前题目的索引
			const index = res.data.topicNameVos.findIndex(item => item.id === currentTopicId)
			currentIndex.value = index !== -1 ? index + 1 : 1
		}
	}
}
// 题目详情
const topicDetail = ref({})
// 查询题目详情
const getTopicDetail = async (id) => {
	const res = await apiQueryTopicDetail(id)
	topicDetail.value = res.data
}

// tab标签页
const isTabs = ref(true)
// 是否点击了查看
const isShowAnswer = ref(false)
// 题目答案对象
const answer = ref({
	answer: null,
	aiAnswer: null
})
// 查看答案
const showAnswer = async () => {
	// 获取题目答案
	const res = await apiQueryTopicAnswer(currentTopicId.value)
	answer.value = res.data
	if (isTabs.value) {
		markDownContent.value = res.data.answer
	} else {
		markDownContent.value = res.data.aiAnswer
	}
	// 显示
	isShowAnswer.value = true
}
// 答案
const markDownContent = ref()
// tab点击事件
const handleTabs = () => {
	isTabs.value = !isTabs.value
	if (isTabs.value) {
		markDownContent.value = answer.value.answer
	} else {
		markDownContent.value = answer.value.aiAnswer
	}
}


// 题目列表遮罩
const showRight = ref()
// 下一题
const nextQuestion = () => {
	// 更新题目的内容重新调用获取题目的接口
}
</script>
<template>
	<!-- 题目列表 -->
	<uni-drawer ref="showRight" mode="right" width="260">
		<scroll-view class="scroll-view-box" scroll-y="true" style="height: 100vh;">
			<uni-list>
				<uni-list-item class="item" showArrow v-for="item in subjcetDetail" clickable @click="goToTopic(item)">
					<template #header>
						<span class="title">{{ item.topicName }}</span>
					</template>
				</uni-list-item>
			</uni-list>
		</scroll-view>
	</uni-drawer>

	<view class="topic" v-if="topicDetail">
		<view class="topic-box">
			<!-- 题目标题和标签以及收藏 -->
			<view class="topic-top">
				<!-- 标题 -->
				<view class="top-box">
					<h2 class="topic-title">{{ topicDetail.topicName }}</h2>
				</view>
				<!-- 标签 -->
				<view class="top-center">
					<view class="tags-row">
						<view class="tag" v-for="(tag, index) in topicDetail.labelNames" :key="index">{{ tag }}</view>
					</view>
					<view class="star-box">
						<!-- 收藏 -->
						<uni-icons type="star" :color="'#1677ff'" size="24"></uni-icons>
					</view>
				</view>
			</view>
			<!-- 分隔符 -->
			<view class="topic-divider" />
			<view class="topic-bottom">
				<!-- 标签页 -->
				<view class="tab-box">
					<view :class="{ 'tab-one': true, 'active-tab-box': isTabs }" @click="handleTabs">
						精简答案
					</view>
					<view :class="{ 'tab-one': true, 'active-tab-box': !isTabs }" @click="handleTabs">
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
						<zero-markdown-view v-if="markDownContent" :markdown="markDownContent"></zero-markdown-view>
						<uv-empty v-else text="系统正在完善题目答案哦～" icon="../../../static/images/empty.png"></uv-empty>
					</view>
				</view>
			</view>
			<!-- 操作区域和题目列表 -->
			<view class="topic-operation">
				<view class="operation-top">
					<view class="list-box" @click="showRight.open()">
						<uni-icons type="list" color="#1677ff" size="20"></uni-icons>
						<span class="topic">{{ topicDetail.topicName }}</span>
						(<span class="topic-weight">{{ currentIndex }}</span>/{{
							total
						}})
					</view>
					<progress class="progress" :percent="20" activeColor="#1677ff" stroke-width="6" />
				</view>
				<view class="operation-bottom">
					<view class="game-controls">
						<view class="control-btn prev">
							<view class="btn-circle">
								<uni-icons type="left" size="20" color="#999"></uni-icons>
							</view>
						</view>
						<view class="control-btn feedback">
							<view class="btn-circle">
								<uni-icons type="info" size="20" color="#fff"></uni-icons>
							</view>
						</view>
						<view class="control-btn next" @click="nextQuestion">
							<view class="btn-circle">
								<uni-icons type="right" size="20" color="#fff"></uni-icons>
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>
	</view>
	<uv-empty v-else text="系统正在完善题目信息哦～" icon="../../../static/images/empty.png"></uv-empty>
</template>
<style lang="scss" scoped>
.title {
	font-size: 25rpx;
	display: -webkit-box;
	-webkit-box-orient: vertical;
	-webkit-line-clamp: 1;
	/* 控制显示两行 */
	overflow: hidden;
}

.topic-box {

	.topic-divider {
		height: 28rpx;
		background-color: #fafafa;
		margin-top: 10rpx;
	}

	.topic-top {
		padding: 22rpx;

		.topic-title {
			font-size: 40rpx;
		}

		.top-center {
			display: flex;
			margin-top: 18rpx;
			align-items: center;
			justify-content: space-between;

			.tags-row {
				display: flex;
				justify-content: space-between;
				align-items: center;
				gap: 12rpx;

				.tag {
					padding: 6rpx 20rpx;
					background: rgba(22, 119, 255, 0.08);
					color: #1677ff;
					font-size: 24rpx;
					border-radius: 24rpx;
					font-weight: 500;
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
			color: #1677ff;
			border-bottom: 1px solid #1677ff;
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
				background-color: #1677ff;
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

			.list-box {
				width: 60%;
				display: flex;
				align-items: center;
				color: #1677ff;

				.topic {
					font-size: 16px;
					padding-left: 5rpx;
					display: -webkit-box;
					-webkit-box-orient: vertical;
					-webkit-line-clamp: 1;
					/* 控制显示两行 */
					overflow: hidden;
				}

				.topic-weight {
					font-weight: bold;
				}
			}

			.progress {
				width: 40%;
			}
		}


		.operation-bottom {
			margin-top: 30rpx;

			.game-controls {
				display: flex;
				justify-content: space-between;
				align-items: center;
				width: 100%;

				.control-btn {
					display: flex;
					flex-direction: column;
					align-items: center;

					.btn-circle {
						width: 80rpx;
						height: 80rpx;
						border-radius: 50%;
						display: flex;
						align-items: center;
						justify-content: center;
						background-color: #1677ff;
						box-shadow: 0 4rpx 8rpx rgba(0, 0, 0, 0.15);
						transition: all 0.2s;

						&:active {
							transform: scale(0.9);
							box-shadow: 0 2rpx 4rpx rgba(0, 0, 0, 0.1);
						}
					}

					&.feedback {
						.btn-circle {
							background-color: rgba(22, 119, 255, 0.8);
							box-shadow: 0 4rpx 8rpx rgba(22, 119, 255, 0.25);
						}
					}

					&.prev {
						.btn-circle {
							background-color: #f5f5f5;
						}

						.btn-text {
							color: #999;
						}
					}
				}
			}
		}
	}
}
</style>