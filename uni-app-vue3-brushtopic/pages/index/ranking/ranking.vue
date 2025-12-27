<template>
	<view class="ranking-container" :style="{ background: themeConfig.bg }">
		<!-- 1. 顶部 Tab 切换 (胶囊设计) -->
		<view class="header-tab">
			<view class="tab-pill">
				<view :class="['tab-btn', screen ? 'active' : '']" @click="toggleTab(true)">日榜</view>
				<view :class="['tab-btn', !screen ? 'active' : '']" @click="toggleTab(false)">总榜</view>
			</view>
		</view>

		<!-- 2. 前三名荣誉台 (3列等宽，位置绝对不会错乱) -->
		<view class="podium-box">
			<!-- 左侧：第2名 -->
			<view class="podium-col">
				<view class="podium-item silver" v-if="top3[1]">
					<view class="avatar-wrap">
						<image v-if="top3[1].avatar" class="avatar" :src="top3[1].avatar" />
						<uv-avatar v-else :text="top3[1].nickname.charAt(0)" size="55" randomBgColor></uv-avatar>
						<view class="medal">2</view>
					</view>
					<text class="nickname">{{ top3[1].nickname }}</text>
					<view class="score-tag">{{ top3[1].scope }}</view>
				</view>
				<view class="placeholder" v-else></view>
			</view>

			<!-- 中间：第1名 -->
			<view class="podium-col">
				<view class="podium-item gold" v-if="top3[0]">
					<view class="avatar-wrap">
						<image v-if="top3[0].avatar" class="avatar main" :src="top3[0].avatar" />
						<uv-avatar v-else :text="top3[0].nickname.charAt(0)" size="75" randomBgColor></uv-avatar>
						<view class="medal">1</view>
					</view>
					<text class="nickname main">{{ top3[0].nickname }}</text>
					<view class="score-tag main">{{ top3[0].scope }}</view>
				</view>
				<view class="placeholder" v-else></view>
			</view>

			<!-- 右侧：第3名 -->
			<view class="podium-col">
				<view class="podium-item bronze" v-if="top3[2]">
					<view class="avatar-wrap">
						<image v-if="top3[2].avatar" class="avatar" :src="top3[2].avatar" />
						<uv-avatar v-else :text="top3[2].nickname.charAt(0)" size="55" randomBgColor></uv-avatar>
						<view class="medal">3</view>
					</view>
					<text class="nickname">{{ top3[2].nickname }}</text>
					<view class="score-tag">{{ top3[2].scope }}</view>
				</view>
				<view class="placeholder" v-else></view>
			</view>
		</view>

		<!-- 3. 下方列表区 -->
		<view class="rank-list-card">
			<scroll-view scroll-y class="scroll-v" style="height: calc(100vh - 680rpx)">
				<view v-if="rankList && rankList.length > 0">
					<view class="rank-row" v-for="(item, index) in rankList" :key="index">
						<text class="rank-index">{{ index + 4 }}</text>
						<view class="user-cell">
							<image v-if="item.avatar" class="u-img" :src="item.avatar" />
							<uv-avatar v-else :text="item.nickname.charAt(0)" size="40" randomBgColor></uv-avatar>
							<text class="u-name" :style="{ color: getRoleColor(item.role) }">{{ item.nickname }}</text>
						</view>
						<view class="u-score">
							<text class="val">{{ item.scope }}</text>
						</view>
					</view>
				</view>
				<view v-else class="empty-box">
					<uv-empty text="暂无排名" mode="list"></uv-empty>
				</view>
			</scroll-view>
		</view>

		<!-- 4. 吸底：我的排名 (已同步身份配色) -->
		<view class="my-status-fixed" v-if="currentRank">
			<!-- 重点优化：背景跟随 themeConfig.bg -->
			<view class="my-card" :style="{ background: themeConfig.bg }">
				<view class="my-left">
					<text class="my-no">{{ currentRank.rank || '-' }}</text>
					<view class="my-avatar-box">
						<image v-if="userInfo.avatar || currentRank.avatar" class="my-img"
							:src="userInfo.avatar || currentRank.avatar" />
						<uv-avatar v-else :text="currentRank.nickname.charAt(0)" size="35" randomBgColor></uv-avatar>
					</view>
					<view class="my-info">
						<text class="my-name">{{ currentRank.nickname }}</text>
						<text class="my-label" :style="{ color: themeConfig.doneBorder }">当前我的排名</text>
					</view>
				</view>
				<view class="my-right">
					<text class="my-val">{{ currentRank.scope || 0 }}</text>
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
		apiQueryRank,
		apiQueryUserRank
	} from '@/api/home'

	const screen = ref(true)
	const rankList = ref([])
	const top3 = ref([])
	const currentRank = ref(null)
	const userInfo = ref(JSON.parse(uni.getStorageSync('h5UserInfo') || '{}'))
	const role = ref(uni.getStorageSync('role') || 0)

	// --- 颜色配置 (完全同步首页最新配色方案) ---
	const themeConfig = computed(() => {
		const configs = {
			2: { // 管理员
				name: '管理员',
				accent: '#4F46E5',
				bg: 'linear-gradient(135deg, #1E293B 0%, #334155 100%)',
				doneBg: '#EEF2FF',
				doneBorder: 'rgba(255,255,255,0.6)'
			},
			1: { // 高级会员
				name: '高级会员',
				accent: '#D97706',
				bg: 'linear-gradient(135deg, #2D241E 0%, #78350F 100%)',
				doneBg: '#FFFBEB',
				doneBorder: 'rgba(255,255,255,0.6)'
			},
			0: { // 普通用户
				name: '普通用户',
				accent: '#1677ff',
				bg: 'linear-gradient(135deg, #1677ff 0%, #40a9ff 100%)',
				doneBg: '#f0f7ff',
				doneBorder: 'rgba(255,255,255,0.6)'
			}
		}
		return configs[role.value] || configs[0]
	})

	const fetchData = async (type) => {
		uni.showLoading({
			title: '加载中'
		})
		try {
			const [resRank, resUser] = await Promise.all([apiQueryRank(type), apiQueryUserRank(type)])
			if (resRank.data) {
				const raw = resRank.data
				top3.value = raw.slice(0, 3)
				rankList.value = raw.slice(3)
			}
			if (resUser.data) currentRank.value = resUser.data
		} finally {
			uni.hideLoading()
		}
	}

	const toggleTab = (isDay) => {
		if (screen.value === isDay) return
		screen.value = isDay
		fetchData(isDay ? 1 : 2)
	}

	onMounted(() => fetchData(1))

	const getRoleColor = (r) => {
		const map = {
			2: '#4F46E5',
			1: '#D97706',
			0: '#1677ff'
		}
		return map[r] || '#333'
	}
</script>

<style lang="scss" scoped>
	.ranking-container {
		min-height: 100vh;
		display: flex;
		flex-direction: column;
		transition: all 0.4s ease;
	}

	/* 顶部 Tab 切换区 */
	.header-tab {
		padding: 40rpx 0 0rpx;
		display: flex;
		justify-content: center;

		.tab-pill {
			display: flex;
			background: rgba(255, 255, 255, 0.15);
			padding: 8rpx;
			border-radius: 100rpx;
			backdrop-filter: blur(10px);

			.tab-btn {
				padding: 12rpx 50rpx;
				border-radius: 100rpx;
				font-size: 26rpx;
				color: rgba(255, 255, 255, 0.8);

				&.active {
					background: #fff;
					color: #333;
					font-weight: bold;
					box-shadow: 0 4rpx 10rpx rgba(0, 0, 0, 0.1);
				}
			}
		}
	}

	/* 荣誉台 */
	.podium-box {
		display: flex;
		justify-content: space-between;
		align-items: flex-end;
		padding: 0rpx 10rpx 60rpx;
		height: 320rpx;

		.podium-col {
			flex: 1;
			display: flex;
			justify-content: center;

			.placeholder {
				width: 1px;
				height: 1px;
				visibility: hidden;
			}
		}

		.podium-item {
			display: flex;
			flex-direction: column;
			align-items: center;
			width: 100%;

			.avatar-wrap {
				position: relative;

				.avatar {
					width: 105rpx;
					height: 105rpx;
					border-radius: 50%;
					border: 4rpx solid rgba(255, 255, 255, 0.6);

					&.main {
						width: 145rpx;
						height: 145rpx;
						border-color: #FFD700;
					}
				}

				.medal {
					position: absolute;
					bottom: -8rpx;
					left: 50%;
					transform: translateX(-50%);
					width: 36rpx;
					height: 36rpx;
					border-radius: 50%;
					color: #fff;
					font-size: 22rpx;
					font-weight: bold;
					display: flex;
					align-items: center;
					justify-content: center;
					box-shadow: 0 4rpx 8rpx rgba(0, 0, 0, 0.2);
				}

				.crown-icon {
					position: absolute;
					top: -50rpx;
					left: 50%;
					transform: translateX(-50%);
					width: 70rpx;
					height: 70rpx;
					z-index: 2;
				}
			}

			.nickname {
				font-size: 24rpx;
				color: #fff;
				margin-top: 16rpx;
				opacity: 0.9;
				text-align: center;
				width: 180rpx;
				overflow: hidden;
				text-overflow: ellipsis;
				white-space: nowrap;

				&.main {
					font-size: 28rpx;
					font-weight: bold;
					opacity: 1;
				}
			}

			.score-tag {
				margin-top: 8rpx;
				padding: 4rpx 20rpx;
				background: rgba(255, 255, 255, 0.2);
				border-radius: 40rpx;
				font-size: 22rpx;
				color: #fff;

				&.main {
					background: #FFD700;
					color: #7C4B00;
					font-weight: bold;
				}
			}

			&.gold .medal {
				background: #FFD700;
				font-size: 24rpx;
				width: 42rpx;
				height: 42rpx;
			}

			&.silver .medal {
				background: #C0C0C0;
			}

			&.bronze .medal {
				background: #CD7F32;
			}
		}
	}

	/* 列表区 */
	.rank-list-card {
		flex: 1;
		background: #fff;
		border-radius: 60rpx 60rpx 0 0;
		padding: 40rpx 40rpx 140rpx;

		.rank-row {
			display: flex;
			align-items: center;
			padding: 26rpx 0;
			border-bottom: 1rpx solid #f2f2f2;

			.rank-index {
				width: 60rpx;
				font-size: 32rpx;
				color: #bbb;
				font-style: italic;
				font-weight: bold;
			}

			.user-cell {
				flex: 1;
				display: flex;
				align-items: center;
				margin: 0 20rpx;

				.u-img {
					width: 84rpx;
					height: 84rpx;
					border-radius: 50%;
					margin-right: 20rpx;
					background: #eee;
				}

				.u-name {
					font-size: 30rpx;
					font-weight: 500;
				}
			}

			.u-score {
				text-align: right;

				.val {
					font-size: 36rpx;
					font-weight: bold;
					color: #333;
				}

				.unit {
					font-size: 20rpx;
					color: #bbb;
					margin-left: 4rpx;
				}
			}
		}
	}

	/* 底部吸底卡片 */
	.my-status-fixed {
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
		padding: 20rpx 30rpx 40rpx;
		background: linear-gradient(to top, #fff 80%, transparent);

		.my-card {
			/* 改动：背景动态化，圆角加大 */
			border-radius: 36rpx;
			padding: 24rpx 36rpx;
			display: flex;
			justify-content: space-between;
			align-items: center;
			box-shadow: 0 12rpx 30rpx rgba(0, 0, 0, 0.15);
			transition: all 0.3s ease;

			.my-left {
				display: flex;
				align-items: center;

				.my-no {
					font-size: 38rpx;
					color: #fff;
					font-weight: bold;
					font-style: italic;
					margin-right: 30rpx;
				}

				.my-avatar-box {
					width: 76rpx;
					height: 76rpx;

					.my-img {
						width: 100%;
						height: 100%;
						border-radius: 50%;
						border: 2rpx solid rgba(255, 255, 255, 0.4);
					}
				}

				.my-info {
					margin-left: 20rpx;

					.my-name {
						font-size: 30rpx;
						color: #fff;
						font-weight: 600;
						display: block;
					}

					.my-label {
						font-size: 20rpx;
						margin-top: 2rpx;
					}
				}
			}

			.my-right {
				text-align: right;
				color: #fff;

				.my-val {
					font-size: 44rpx;
					font-weight: bold;
				}

				.my-unit {
					font-size: 20rpx;
					margin-left: 4rpx;
					opacity: 0.8;
				}
			}
		}
	}
</style>