<script setup>
import { ref, reactive, watch } from 'vue';

// 当前选中的模式
const aiModeValue = ref(localStorage.getItem('aiMode') || 'system')
// 当前身份
const role = ref(uni.getStorageSync('role'))
// 输入内容
const inputValue = ref('')

// 切换模式
const switchMode = (mode) => {
  aiModeValue.value = mode;
  localStorage.setItem('aiMode', mode)
};

// 模式
const aiMode = reactive([
  {
    label: '系统模式',
    value: 'system',
    desc: role.value === 1 ? 'AI从系统题库和会员自定义题库中随机提取题目HaoAi会校验你的回答' : 'AI从系统题库中随机提取题目HaoAi会校验你的回答'
  }, {
    label: '模型模式',
    value: 'model',
    desc: '完全使用AI生成的题目HaoAi会校验你的回答'
  }, {
    label: '混合模式',
    value: 'mix',
    desc: 'AI随机混合系统题库和AI生成题目HaoAi会校验你的回答'
  },
])

// 提示词
const placeholder = ref(role.value === 1 ? '请输入系统中和会员自定义的正确的题目专题，AI将自动生成题目' : '请输入系统中的正确的题目专题，AI将自动生成题目')

// 初始化提示词
const initPlaceholder = () => {
  if (aiModeValue.value === 'system') {
    placeholder.value = '请输入题目专题，AI将自动生成题目'
  } else if (aiModeValue.value === 'model' || aiModeValue.value === 'mix') {
    placeholder.value = '请输入给AI你想刷的题目类型，AI将自动生成题目'
  }
}

// 监听模式变化
watch(() => aiModeValue.value, () => {
  initPlaceholder()
})

const prompt = ref('')
</script>
<template>
  <view class="ai-box">
    <!-- 顶部导航栏 -->
    <view class="ai-history">
      <view class="left-icon">
        <uni-icons type="bars" size="24" color="#1677ff"></uni-icons>
      </view>
      <view class="title">新对话</view>
      <view class="right-icon">
        <uni-icons type="plusempty" color="#1677ff" size="25"></uni-icons>
      </view>
    </view>

    <!-- ai输出内容需要判断 -->
    <view class="ai-content">
      <scroll-view :scroll-top="0" scroll-y="true" class="scroll-Y" @scrolltoupper="upper">
        <view class="item" v-for="item in 10">
        </view>
      </scroll-view>
    </view>


    <!-- 输入框 -->
    <view class="ai-search">
      <uv-textarea adjustPosition v-model="prompt" height="25" :placeholder="placeholder"></uv-textarea>
      <view class="button-group">
        <view class="mode-buttons">
          <view v-for="item in aiMode" :key="item.value" class="mode-btn"
            :class="{ active: aiModeValue === item.value }" @click="switchMode(item.value)">
            {{ item.label }}
          </view>
        </view>
        <view class="send-btn">
          <uni-icons type="paperplane-filled" size="24" color="#1677ff"></uni-icons>
        </view>
      </view>
    </view>
  </view>
</template>
<style lang="scss" scoped>
.ai-box {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 10rpx;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #fff;

  .ai-history {
    width: 98%;
    height: calc(100vh - 95vh);
    display: flex;
    align-items: center;
    justify-content: space-between;
    border-radius: 0 0 5rpx 5rpx;
    color: #2c313c;
    font-size: 35rpx;
    background-color: #fff;
  }

  .ai-content {
    width: 95%;
    padding: 3rpx;
    height: calc(100vh - 23.5vh); // 增加内容区域高度
    background-color: #f6f7fb;
    width: 100%;

    .scroll-Y {
      height: 100%;
    }

    .item {
      width: 100%;
      margin-top: 10rpx;
      height: 300rpx;
      background-color: pink;
    }
  }

  .ai-search {
    height: 10vh;
    background-color: #fff;
    width: 96%;
    padding: 20rpx 30rpx;
    box-shadow: 0 -4rpx 16rpx rgba(0, 0, 0, 0.05);

    :deep(.uv-textarea) {
      background-color: #f5f5f5;
      border-radius: 16rpx;

      .uv-textarea__field {
        font-size: 28rpx;
        line-height: 40rpx;
        color: #333;
      }

      .uv-textarea__placeholder {
        color: #999;
        font-size: 28rpx;
      }
    }

    .button-group {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .mode-buttons {
        display: flex;
        gap: 16rpx;

        .mode-btn {
          font-size: 24rpx;
          padding: 8rpx 16rpx;
          border-radius: 8rpx;
          background-color: #f5f5f5;
          color: #666;
          transition: all 0.3s;

          &.active {
            background-color: #e6f4ff;
            color: #1677ff;
          }

          &:active {
            opacity: 0.8;
          }
        }
      }

      .send-btn {
        padding: 12rpx;
        border-radius: 8rpx;

        &:active {
          opacity: 0.8;
        }
      }
    }
  }

}
</style>