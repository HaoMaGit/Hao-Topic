<template>
	<view class="category-page">
		<!-- 1. 顶部风格：极简平整式头部 -->
		<view class="header-section" :style="{ background: themeConfig.bg }">
			<!-- 搜索框：更方正圆润的平衡 -->
			<view class="search-box">
				<uv-input v-model="searchValue" clearable shape="circle" @clear="handleClear" placeholder="在该分类下搜索专题..."
					prefixIcon="search" :prefixIconStyle="{ fontSize: '18px', color: '#999' }" @blur="handleSearch"
					:customStyle="{ 
                        background: '#fff', 
                        padding: '12rpx 24rpx',
                        border: 'none'
                    }"></uv-input>
			</view>

			<!-- 2. 分类导航：经典下划线式 -->
			<view class="nav-bar">
				<scroll-view class="scroll-v" scroll-x :show-scrollbar="false">
					<view class="nav-list">
						<view v-for="(item, index) in category" :key="index"
							:class="['nav-item', activeIndex === index ? 'active' : '']"
							@click="handlerMenu(index, item.id)">
							<text class="nav-text">{{ item.categoryName }}</text>
							<view class="active-line" v-if="activeIndex === index"></view>
						</view>
					</view>
				</scroll-view>
			</view>
		</view>

		<!-- 3. 专题列表区域 -->
		<view class="list-body">
			<scroll-view scroll-y class="list-scroll">
				<view class="card-group" v-if="subject && subject.length !== 0">
					<view class="subject-card" v-for="(item, index) in subject" :key="index" @click="goToSubject(item)">
						<view class="card-main">
							<view class="icon-box">
								<image v-if="item.imageUrl" :src="item.imageUrl" mode="aspectFill" class="img" />
								<view v-else class="img-none" :style="{ background: themeConfig.accent }">
									{{ item.subjectName.charAt(0) }}
								</view>
							</view>
							<view class="content">
								<text class="title">{{ item.subjectName }}</text>
							</view>
						</view>
						<uni-icons type="right" size="14" color="#D1D5DB"></uni-icons>
					</view>
				</view>

				<!-- 空状态 -->
				<view v-else class="empty-view">
					<uv-empty text="暂无相关专题" mode="list"></uv-empty>
				</view>
			</scroll-view>
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
		apiQueryCategoryList
	} from '@/api/topic/category.js'
	import {
		apiQuerySubjectList
	} from '@/api/topic/subject.js'

	const activeIndex = ref(0)
	const categoryId = ref(0)
	const searchValue = ref(null)
	const category = ref([])
	const subject = ref([])
	const role = ref(uni.getStorageSync('role') || 0)

	// --- 颜色配置 (同步首页方案) ---
	const themeConfig = computed(() => {
		const configs = {
			2: { // 管理员
				accent: '#4F46E5',
				bg: 'linear-gradient(135deg, #1E293B 0%, #334155 100%)'
			},
			1: { // 会员
				accent: '#D97706',
				bg: 'linear-gradient(135deg, #2D241E 0%, #78350F 100%)'
			},
			0: { // 普通用户
				accent: '#1677ff',
				bg: 'linear-gradient(135deg, #1677ff 0%, #40a9ff 100%)'
			}
		}
		return configs[role.value] || configs[0]
	})

	const getCategory = async () => {
		const custom = uni.getStorageSync('isCustomQuestion')
		const isCustomQuestion = (custom === false || custom === true) ? custom : true
		const res = await apiQueryCategoryList(isCustomQuestion)
		category.value = res.data
		if (category.value.length > 0) getSubject(category.value[0].id)
	}

	const getSubject = async (id) => {
		categoryId.value = id
		const res = await apiQuerySubjectList(id)
		subject.value = res.data
	}

	const handlerMenu = (index, id) => {
		activeIndex.value = index
		searchValue.value = null
		getSubject(id)
	}

	const handleSearch = () => {
		if (!subject.value || !searchValue.value || searchValue.value.trim() === '') {
			getSubject(categoryId.value)
			return
		}
		subject.value = subject.value.filter(item =>
			item.subjectName.toLowerCase().includes(searchValue.value.toLowerCase().trim())
		)
	}

	const handleClear = () => {
		searchValue.value = ''
		getSubject(categoryId.value)
	}

	const goToSubject = (item) => {
		uni.navigateTo({
			url: `/pages/database/subject/subject?id=${item.id}&name=${item.subjectName}`
		})
	}

	onMounted(() => {
		getCategory()
	})
</script>

<style lang="scss" scoped>
	.category-page {
		min-height: 100vh;
		background-color: #F3F4F6;
	}

	/* 顶部：平整极简头部 */
	.header-section {
		padding: 80rpx 0 0;
		/* 底部不再留白，靠导航撑开 */
		transition: all 0.3s ease;

		.search-box {
			padding: 0 40rpx 30rpx;
		}

		.nav-bar {
			background: rgba(0, 0, 0, 0.05);
			/* 给导航一个微弱的底色区分 */

			.nav-list {
				display: flex;
				padding: 0 20rpx;

				.nav-item {
					padding: 24rpx 30rpx;
					position: relative;
					display: flex;
					flex-direction: column;
					align-items: center;
					flex-shrink: 0;

					.nav-text {
						font-size: 28rpx;
						color: rgba(255, 255, 255, 0.7);
						transition: all 0.2s;
					}

					.active-line {
						position: absolute;
						bottom: 0;
						width: 40rpx;
						height: 6rpx;
						background: #fff;
						border-radius: 4rpx 4rpx 0 0;
					}

					&.active .nav-text {
						color: #fff;
						font-weight: bold;
						transform: scale(1.05);
					}
				}
			}
		}
	}

	/* 列表主体 */
	.list-body {
		padding: 30rpx;

		.list-scroll {
			height: calc(100vh - 320rpx);
		}

		.subject-card {
			background: #fff;
			border-radius: 16rpx;
			/* 减小圆角，更显利落 */
			padding: 28rpx;
			margin-bottom: 20rpx;
			display: flex;
			justify-content: space-between;
			align-items: center;
			box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.03);

			&:active {
				background: #F9FAFB;
			}

			.card-main {
				display: flex;
				align-items: center;

				.icon-box {
					width: 90rpx;
					height: 90rpx;
					margin-right: 24rpx;

					.img {
						width: 100%;
						height: 100%;
						border-radius: 12rpx;
					}

					.img-none {
						width: 100%;
						height: 100%;
						border-radius: 12rpx;
						display: flex;
						align-items: center;
						justify-content: center;
						color: #fff;
						font-size: 36rpx;
						font-weight: bold;
					}
				}

				.content {
					.title {
						font-size: 30rpx;
						color: #111827;
						font-weight: 600;
						display: block;
						margin-bottom: 4rpx;
					}

					.info {
						font-size: 22rpx;
						color: #9CA3AF;
					}
				}
			}
		}
	}

	.empty-view {
		padding-top: 150rpx;
	}
</style>