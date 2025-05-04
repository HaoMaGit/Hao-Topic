<script setup>
import { ref, watch } from 'vue'

const props = defineProps({
  show: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['update:show', 'submit', 'close'])

const feedbackContent = ref('')

// 提交反馈
const handleSubmit = () => {
  if (feedbackContent.value === '') {
    uni.showToast({
      title: '请输入反馈内容',
      icon: 'none'
    })
    return
  }
  emit('submit', feedbackContent.value)
  handleClose()
}

const popup = ref(null)

// 监听show属性变化
watch(() => props.show, (newVal) => {
  if (newVal) {
    popup.value?.open()
  } else {
    popup.value?.close()
  }
})

// 修改关闭处理方法
const handleClose = () => {
  feedbackContent.value = ''
  emit('update:show', false)
  emit('close')
}
</script>

<template>
  <uni-popup ref="popup" type="center" background-color="#fff" @change="(e) => emit('update:show', e.show)">
    <view class="feedback-popup">
      <view class="feedback-header">
        <text class="title">意见反馈</text>
        <uni-icons type="close" size="25" color="#666" @click="handleClose"></uni-icons>
      </view>
      <view class="feedback-body">
        <textarea v-model="feedbackContent" class="feedback-textarea" placeholder="请输入您的反馈意见，我们会认真查看并及时处理..."
          :maxlength="100">
        </textarea>
      </view>
      <view class="feedback-footer">
        <button class="submit-btn" @click="handleSubmit">提交反馈</button>
      </view>
    </view>
  </uni-popup>
</template>

<style lang="scss" scoped>
.feedback-popup {
  width: 650rpx;
  background: #fff;
  border-radius: 16rpx;
  overflow: hidden;

  .feedback-header {
    padding: 30rpx;
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-bottom: 1px solid #eee;

    .title {
      font-size: 32rpx;
      font-weight: 600;
      color: #333;
    }
  }

  .feedback-body {
    padding: 30rpx;
    position: relative;

    .feedback-textarea {
      width: 100%;
      height: 300rpx;
      padding: 20rpx;
      box-sizing: border-box;
      font-size: 28rpx;
      line-height: 1.5;
      border: 1px solid #eee;
      border-radius: 8rpx;
      background: #f8f8f8;
    }

    .word-count {
      position: absolute;
      right: 40rpx;
      bottom: 40rpx;
      font-size: 24rpx;
      color: #999;
    }
  }

  .feedback-footer {
    padding: 20rpx 30rpx 30rpx;

    .submit-btn {
      width: 100%;
      height: 80rpx;
      line-height: 80rpx;
      text-align: center;
      background: #1677ff;
      color: #fff;
      border-radius: 40rpx;
      font-size: 30rpx;

      &:active {
        opacity: 0.8;
      }
    }
  }
}
</style>