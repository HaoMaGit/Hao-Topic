<template>
	<view class="page-container">
		<!-- 1. 顶部身份与背景 -->
		<view class="header-section" :style="{ background: themeConfig.bg }">
			<view class="user-info">
				<view class="greeting">
					<view class="time-row">
						<text class="time-text">{{ getTimeOfDay() }}好，</text>
						<view class="role-badge">{{ themeConfig.name }}</view>
					</view>
					<text class="nickname">{{ userInfo.nickname || userInfo.account || '同学' }}</text>
				</view>
			</view>

			<!-- 2. 数据看板 -->
			<view class="stats-board">
				<view class="stats-item">
					<text class="label">今日已刷</text>
					<text class="value">{{ webHomeCount.todayTopicCount || 0 }}</text>
				</view>
				<view class="v-divider"></view>
				<view class="stats-item">
					<text class="label">今日次数</text>
					<text class="value">{{ webHomeCount.todayCount || 0 }}</text>
				</view>
				<view class="v-divider"></view>
				<view class="stats-item">
					<text class="label">累计数量</text>
					<view class="value-box">
						<text class="current"
							:style="{ color: themeConfig.accent }">{{ webHomeCount.totalTopicRecordCount || 0 }}</text>
						<text class="split">/</text>
						<text class="total">{{ webHomeCount.totalTopicCount || 0 }}</text>
					</view>
				</view>
			</view>
		</view>

		<!-- 3. 排名引导 -->
		<view class="ranking-bar" @click="tapRanking">
			<view class="bar-left">
				<image class="medal-icon" src="../../static/images/zzjb.png" mode="aspectFit" />
				<view class="rank-text">
					<text class="main-rank">当前排名：第 {{ webHomeCount.rank || 0 }} 名</text>
					<text class="sub-rank">全站共 {{ webHomeCount.userCount || 0 }} 位活跃用户</text>
				</view>
			</view>
			<uni-icons type="right" size="14" color="#999"></uni-icons>
		</view>

		<!-- 4. 每日任务列表 -->
		<view class="task-section">
			<view class="section-header">
				<view class="title-left">
					<view class="indicator" :style="{ background: themeConfig.accent }"></view>
					<text class="title">每日必刷</text>
				</view>
				<text class="subtitle">{{ currentDate }}</text>
			</view>

			<view class="list-wrapper">
				<view v-if="loading" class="state-box">数据加载中...</view>
				<view v-else-if="topicTodayVo.length === 0" class="state-box">今日暂无推荐题目</view>

				<view v-for="(item, index) in topicTodayVo" :key="index" class="topic-card"
					@click="handleQuestion(item)">
					<view class="topic-main">
						<text class="topic-title">{{ item.topicName }}</text>
						<view class="topic-tags">
							<text v-for="tag in item.labelNames" :key="tag" class="tag">{{ tag }}</text>
						</view>
					</view>

					<!-- 修改：根据状态和身份动态绑定样式 -->
					<view class="status-btn" :style="item.status == 1 ? { 
							background: themeConfig.doneBg, 
							color: themeConfig.accent,
							border: `1px solid ${themeConfig.doneBorder}`
						} : { 
							background: themeConfig.accent, 
							color: '#fff' 
						}">
						{{ item.status == 1 ? '已刷' : '练习' }}
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script setup>
	import {
		ref,
		computed,
		onMounted
	} from 'vue'
	import {
		getTimeOfDay
	} from '@/utils/time'
	import {
		apiQueryWebHomeCount,
		apiQueryTopicTodayVo,
		apiFlushTopic
	} from '@/api/home/index'

	const loading = ref(true)
	const webHomeCount = ref({})
	const topicTodayVo = ref([])
	const userInfo = ref(JSON.parse(uni.getStorageSync('h5UserInfo') || '{}'))
	const role = ref(uni.getStorageSync('role') || 0)

	const initData = async () => {
		loading.value = true
		try {
			const [resCount, resTopic] = await Promise.all([apiQueryWebHomeCount(), apiQueryTopicTodayVo()])
			if (resCount.code === 200) webHomeCount.value = resCount.data
			if (resTopic.code === 200) topicTodayVo.value = resTopic.data
		} finally {
			loading.value = false
		}
	}

	onMounted(() => {
		initData()
	})

	// --- 颜色配置优化 ---
	const themeConfig = computed(() => {
		const configs = {
			2: { // 管理员
				name: '管理员',
				accent: '#4F46E5',
				bg: 'linear-gradient(135deg, #1E293B 0%, #334155 100%)',
				doneBg: '#EEF2FF', // 极浅靛蓝
				doneBorder: '#E0E7FF'
			},
			1: { // 会员：升级为质感古铜方案
				name: '普通会员',
				accent: '#D97706',
				bg: 'linear-gradient(135deg, #2D241E 0%, #78350F 100%)',
				doneBg: '#FFFBEB', // 极浅琥珀色（浅色不显深）
				doneBorder: '#FEF3C7'
			},
			0: { // 普通用户
				name: '普通用户',
				accent: '#1677ff',
				bg: 'linear-gradient(135deg, #1677ff 0%, #40a9ff 100%)',
				doneBg: '#f0f7ff', // 极浅蓝色
				doneBorder: '#d0e7ff'
			}
		}
		return configs[role.value] || configs[0]
	})

	const currentDate = computed(() => {
		const date = new Date()
		return `${date.getMonth() + 1}月${date.getDate()}日`
	})

	const tapRanking = () => uni.navigateTo({
		url: '/pages/index/ranking/ranking'
	})

	const handleQuestion = async (item) => {
		if (!item.subjectId) {
			uni.showToast({
				title: '专题不可用',
				icon: 'none'
			});
			return;
		}
		uni.showLoading({
			title: '加载中'
		})
		await apiFlushTopic(item.id)
		uni.hideLoading()
		uni.navigateTo({
			url: `/pages/database/topic/topic?id=${item.topicId}&name=${item.topicName}&subjectId=${item.subjectId}`
		})
	}
</script>

<style lang="scss" scoped>
	.page-container {
		min-height: 100vh;
		background-color: #f7f8fa;
		padding-bottom: 50rpx;
	}

	.header-section {
		height: 280rpx;
		padding: 70rpx 40rpx 0;
		position: relative;
		border-radius: 0 0 40rpx 40rpx;

		.user-info {
			.greeting {
				.time-row {
					display: flex;
					align-items: center;
					margin-bottom: 6rpx;

					.time-text {
						font-size: 26rpx;
						color: rgba(255, 255, 255, 0.85);
					}

					.role-badge {
						margin-left: 12rpx;
						padding: 2rpx 14rpx;
						background: rgba(255, 255, 255, 0.15);
						border-radius: 100rpx;
						font-size: 18rpx;
						color: #fff;
						backdrop-filter: blur(4px);
					}
				}

				.nickname {
					font-size: 44rpx;
					font-weight: bold;
					color: #fff;
				}
			}
		}
	}

	.stats-board {
		position: absolute;
		bottom: -50rpx;
		left: 30rpx;
		right: 30rpx;
		background: #fff;
		border-radius: 20rpx;
		display: flex;
		padding: 34rpx 0;
		box-shadow: 0 10rpx 25rpx rgba(0, 0, 0, 0.05);
		z-index: 10;

		.stats-item {
			flex: 1;
			display: flex;
			flex-direction: column;
			align-items: center;

			.label {
				font-size: 22rpx;
				color: #999;
				margin-bottom: 8rpx;
			}

			.value {
				font-size: 36rpx;
				font-weight: bold;
				color: #333;
				line-height: 1;
			}

			.value-box {
				display: flex;
				align-items: baseline;
				line-height: 1;

				.current {
					font-size: 36rpx;
					font-weight: bold;
				}

				.split {
					font-size: 22rpx;
					color: #cbd5e1;
					margin: 0 4rpx;
				}

				.total {
					font-size: 22rpx;
					color: #999;
					font-weight: 500;
				}
			}
		}

		.v-divider {
			width: 1px;
			height: 32rpx;
			background: #f0f0f0;
			align-self: center;
		}
	}

	.ranking-bar {
		margin: 90rpx 30rpx 40rpx;
		background: #fff;
		padding: 26rpx 30rpx;
		border-radius: 20rpx;
		display: flex;
		justify-content: space-between;
		align-items: center;

		.bar-left {
			display: flex;
			align-items: center;

			.medal-icon {
				width: 44rpx;
				height: 44rpx;
				margin-right: 20rpx;
			}

			.rank-text {
				display: flex;
				flex-direction: column;

				.main-rank {
					font-size: 26rpx;
					color: #333;
					font-weight: 500;
				}

				.sub-rank {
					font-size: 20rpx;
					color: #bbb;
					margin-top: 2rpx;
				}
			}
		}
	}

	.task-section {
		padding: 0 30rpx;

		.section-header {
			margin-bottom: 24rpx;
			display: flex;
			justify-content: space-between;
			align-items: flex-end;

			.title-left {
				display: flex;
				align-items: center;

				.indicator {
					width: 8rpx;
					height: 32rpx;
					border-radius: 4rpx;
					margin-right: 14rpx;
				}

				.title {
					font-size: 32rpx;
					font-weight: bold;
					color: #333;
				}
			}

			.subtitle {
				font-size: 24rpx;
				color: #ccc;
				font-weight: 500;
			}
		}

		.topic-card {
			background: #fff;
			border-radius: 20rpx;
			padding: 30rpx;
			margin-bottom: 20rpx;
			display: flex;
			justify-content: space-between;
			align-items: center;
			transition: all 0.2s;

			&:active {
				transform: scale(0.98);
				background: #fafafa;
			}

			.topic-main {
				flex: 1;
				margin-right: 20rpx;

				.topic-title {
					font-size: 29rpx;
					color: #333;
					font-weight: 500;
					margin-bottom: 14rpx;
					display: block;
					line-height: 1.4;
				}

				.topic-tags {
					display: flex;
					flex-wrap: wrap;
					gap: 10rpx;

					.tag {
						padding: 4rpx 14rpx;
						background: #f1f4f8;
						color: #7a8b9a;
						font-size: 20rpx;
						border-radius: 6rpx;
					}
				}
			}

			.status-btn {
				width: 100rpx;
				height: 54rpx;
				line-height: 54rpx;
				text-align: center;
				font-size: 24rpx;
				border-radius: 12rpx;
				flex-shrink: 0;
				transition: all 0.2s;
			}
		}
	}
</style>