<script setup>
	import {
		ref,
		onMounted
	} from 'vue'
	import {
		onLoad
	} from '@dcloudio/uni-app';
	// 标题
	const navBarTitle = ref('')
	onLoad((options) => {
		// 获取路径参数，并设置默认值
		const type = options.type !== undefined ? options.type : '0'
		// 根据类型设置导航栏标题
		if (type === '0') {
			navBarTitle.value = '我的收藏'
		} else if (type === '1') {
			navBarTitle.value = '我的记录'
		} else {
			// 处理未知类型的默认情况
			navBarTitle.value = '默认标题'
		}

		// 设置导航栏标题
		uni.setNavigationBarTitle({
			title: navBarTitle.value
		})
	})
</script>
<template>
	<view class="collect">
		<view class="collect-box">
			<!-- 列表区域 -->
			<view class="list-box">
				<uni-list :border="true">
					<!-- 自定义右侧内容 -->
					<uni-list-chat class="list"
						avatar="https://vkceyugu.cdn.bspapp.com/VKCEYUGU-dc-site/460d46d0-4fcc-11eb-8ff1-d5dcf8779628.png"
						v-for="item in 9" title="你认为Java的优势是什么？" note="Java JavaSe Mysql" badge-positon="left">
						<view class="chat-custom-right">
							<text class="chat-custom-text">2020-02-02 20:20</text>
							<uni-icons v-if="navBarTitle === '我的收藏'" type="star-filled" color="#fe8d59"
								size="24"></uni-icons>
						</view>
					</uni-list-chat>
				</uni-list>
			</view>
		</view>
	</view>
</template>
<style lang="scss" scoped>
	.collect {
		width: 100%;
		height: 100vh;

		.collect-box {
			.list-box {
				.list {
					.chat-custom-right {
						flex: 1;
						/* #ifndef APP-NVUE */
						display: flex;
						/* #endif */
						flex-direction: column;
						justify-content: space-between;
						align-items: flex-end;
					}

					.chat-custom-text {
						font-size: 12px;
						color: #999;
					}
				}
			}
		}
	}
</style>