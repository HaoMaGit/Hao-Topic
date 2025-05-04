<script setup>
import {
	ref, onMounted
} from 'vue'
import Java from '../../common/image/java.png'
import { apiQueryCategoryList } from '@/api/topic/category.js'
const activeIndex = ref(0)
const handlerMenu = (index) => {
	uni.showLoading({
		title: '加载中'
	});
	activeIndex.value = index
	setTimeout(function () {
		uni.hideLoading();
	}, 2000);
}
const value = ref()

// 跳转到专题列表
const goToSubject = (item) => {
	console.log(1);
	uni.navigateTo({
		url: `/pages/database/subject/subject?id=1&name=Java专题练习`
	})
}

// 是否开启会员自定义题目
const isCustomQuestion = ref(uni.getStorageSync('isCustomQuestion') || true)
// 获取分类名称和id
const category = ref([])
const getCategory = async () => {
	uni.showLoading({
		title: '加载中'
	});
	const res = await apiQueryCategoryList(isCustomQuestion.value)
	category.value = res.data
	uni.hideLoading()
}

onMounted(() => {
	getCategory()
})
</script>
<template>
	<view class="category-box">
		<!-- 搜索区域 -->
		<view class="search">
			<uv-input shape="circle" placeholder="在该分类下搜索题目专题" prefixIcon="search"
				prefixIconStyle="font-size: 22px;color: #909399"></uv-input>
		</view>
		<!-- 菜单 -->
		<view class="menu">
			<scroll-view class="scroll-view_H" scroll-x="true" :show-scrollbar="false" @scroll="scroll">
				<span :class="{ 'menu-item': true, 'selected': activeIndex === index }" v-for="(item, index) in category"
					:key="index" @click="handlerMenu(index)">{{ item.categoryName }}</span>
			</scroll-view>
		</view>
		<!-- 列表区域 -->
		<view class="list-item">
			<uni-list>
				<uni-list-item class="item" showArrow v-for="item in 15" clickable @click="goToSubject(item)">
					<!-- 使用 slot 插入头像 -->
					<template #header>
						<image class="avatar" :src="Java" mode="aspectFill"></image>
						<span class="title">Java基础面试题</span>
					</template>
				</uni-list-item>
			</uni-list>
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
				border-radius: 30rpx;
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