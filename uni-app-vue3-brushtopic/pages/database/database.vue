<script setup>
	import {
		ref,
		onMounted
	} from 'vue'
	import {
		apiQueryCategoryList
	} from '@/api/topic/category.js'
	import {
		apiQuerySubjectList
	} from '@/api/topic/subject.js'
	const activeIndex = ref(0)
	// 菜单点击
	const handlerMenu = (index, id) => {
		uni.showLoading({
			title: '加载中'
		});
		searchValue.value = null
		categoryId.value = id
		activeIndex.value = index
		getSubject(id)
		uni.hideLoading();
	}

	// 跳转到专题列表
	const goToSubject = (item) => {
		uni.navigateTo({
			url: `/pages/database/subject/subject?id=${item.id}&name=${item.subjectName}`
		})
	}

	// 是否开启会员自定义题目
	const isCustomQuestion = ref(null)
	// 获取分类名称和id
	const category = ref([])
	// 当前分类id
	const categoryId = ref(0)
	// 查询分类数据
	const getCategory = async () => {
		const custorm = uni.getStorageSync('isCustomQuestion')
		console.log("===>", custorm);
		if (custorm == false || custorm == true) {
			isCustomQuestion.value = custorm
		} else {
			isCustomQuestion.value = true
		}
		if(isCustomQuestion.value === ''){
			console.log("sdfsdf");
			isCustomQuestion.value = true
		}
		uni.showLoading({
			title: '加载中'
		});
		const res = await apiQueryCategoryList(isCustomQuestion.value)
		category.value = res.data
		if (category.value.length !== 0) {
			// 查询专题
			getSubject(category.value[0].id)
		}
		uni.hideLoading()
	}
	// 专题数据
	const subject = ref([])
	// 查询专题数据
	const getSubject = async (categoryId) => {
		const res = await apiQuerySubjectList(categoryId)
		subject.value = res.data
	}

	// 当前搜索的值
	const searchValue = ref(null)
	// 搜索
	const handleSearch = () => {
		if (!subject.value) {
			return;
		}
		if (!searchValue.value || searchValue.value.trim('') === '') {
			getSubject(categoryId.value)
			return
		}
		subject.value = subject.value.filter(item => item.subjectName.toLowerCase().includes(searchValue.value
			.toLowerCase().trim('')))
	}
	// 清除
	const handleClear = () => {
		searchValue.value = ''
		handleSearch()
	}

	onMounted(() => {
		getCategory()
	})
</script>
<template>
	<view class="category-box">
		<!-- 搜索区域 -->
		<view class="search">
			<uv-input v-model="searchValue" clearable shape="circle" @clear="handleClear" placeholder="在该分类下搜索题目专题"
				prefixIcon="search" prefixIconStyle="font-size: 22px;color: #909399" @blur="handleSearch"></uv-input>
		</view>
		<!-- 菜单 -->
		<view class="menu">
			<scroll-view class="scroll-view_H" scroll-x="true" :show-scrollbar="false" @scroll="scroll">
				<span :class="{ 'menu-item': true, 'selected': activeIndex === index }"
					v-for="(item, index) in category" :key="index"
					@click="handlerMenu(index, item.id)">{{ item.categoryName }}</span>
			</scroll-view>
		</view>
		<!-- 列表区域 -->
		<view class="list-item">
			<uni-list v-if="subject && subject.length !== 0">
				<uni-list-item class="item" showArrow v-for="item in subject" clickable @click="goToSubject(item)">
					<!-- 使用 slot 插入头像 -->
					<template #header>
						<image class="avatar" :src="item.imageUrl" mode="aspectFill"></image>
						<span class="title">{{ item.subjectName }}</span>
					</template>
				</uni-list-item>
			</uni-list>
			<uv-empty v-else text="系统正在收录专题数据哦～" icon="../../static/images/empty.png"></uv-empty>
		</view>
	</view>
</template>
<style lang="scss" scoped>
	.category-box {
		width: 100%;
		height: 100%;

		.list-item {
			.item {
				color: #5b5b5b;
				height: 100rpx;
				margin-top: 18rpx;

				.avatar {
					width: 80rpx;
					height: 80rpx;
					border-radius: 15rpx;
				}

				.title {
					font-size: 41rpx;
					display: flex;
					align-items: center;
					margin-left: 10rpx;
				}
			}
		}

		.search {
			padding: 20rpx;
		}

		.menu {
			.scroll-view_H {
				white-space: nowrap;
				width: 100%;
				height: 90rpx;

				.menu-item {
					padding-bottom: 8px;
					display: inline-block;
					margin-right: 18rpx;
					font-size: 41rpx;
					font-weight: normal;
				}

				.selected {
					border-bottom: 1px solid #1677ff;
					color: #1677ff;
				}
			}

			padding: 10rpx 0 0 20rpx;
		}
	}
</style>