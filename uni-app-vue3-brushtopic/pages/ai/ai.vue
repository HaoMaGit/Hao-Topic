<script setup>
import { ref, reactive, watch, nextTick } from 'vue';
import { v4 as uuidv4 } from 'uuid'; // 引入 uuid 库
import { apiGetManageList, apiGetHistoryDetail } from '@/api/ai'
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


// 加载对话记录loading
const chatLoading = ref(false)
// 当前索引
const activeIndex = ref([])
// 是否可以选择对话
const isSelectHistory = ref(false)

const prompt = ref('')

// 是否点击回复
const isReply = ref(true)
// 用户信息
const userInfo = ref(JSON.parse(uni.getStorageSync('h5UserInfo')))

// ai标识
const aiId = ref(0)
// 记录一下当前使用的id
const currentRecordId = ref(uuidv4())
// 内容
const messageList = reactive([
  {
    prompt: "我是" + userInfo.value.nickname || userInfo.value.account,
    chatId: currentRecordId.value, // 对话id
    model: aiModeValue.value,
    content: '你好，我是HaoAi 1.0，你的面试题AI助手！',
    memoryId: aiId.value
  }
])

// 历史记录列表
const historyList = ref([])
// 历史记录参数
const historyParams = ref({
  pageNum: 1,
  pageSize: 999,
  title: null
})
// 是否显示历史记录抽屉
const showLeft = ref(null)
// 点击了获取历史记录
const handleGetHistoryList = () => {
  showLeft.value.open()
  getHistoryList()
}
// 获取历史记录
const getHistoryList = async () => {
  const res = await apiGetManageList(historyParams.value)
  historyList.value = res.data
}
// 点击了莫一条历史记录
const getHistoryContent = async (id, index, historyIndex) => {
  uni.showLoading({ title: '加载中...' })
  showLeft.value.close()
  chatLoading.value = true
  // 清除默认对话
  restoreDefaultRecord()
  // 重置当前索引
  activeIndex.value = []
  // 添加当前索引
  activeIndex.value[historyIndex] = index
  // id:当前记录id
  const res = await apiGetHistoryDetail(id)
  isSelectHistory.value = true
  aiModeValue.value = res.data[0].mode

  // 封装内容
  const content = res.data.map((item) => {
    return {
      prompt: item.title,
      chatId: item.chatId,
      content: item.content
    }
  })
  // 追加
  messageList.push(...content)
  // 赋值当前id
  currentRecordId.value = res.data[0].chatId
  // 清空当前对话id
  localStorage.removeItem('chatId')
  // 赋值当前id
  aiId.value = res.data.length;
  console.log(res.data.length);

  console.log(aiId.value);

  chatLoading.value = false
  uni.hideLoading()
  await scrollToBottom()
}
// 恢复默认记录
const restoreDefaultRecord = () => {
  // 清空历史记录
  messageList.splice(0, messageList.length)
  // 添加一条数据
  messageList.push({
    prompt: "我是" + userInfo.value.nickname || userInfo.value.account,
    chatId: currentRecordId.value, // 对话id
    model: aiModeValue.value,
    content: '你好，我是HaoAi 1.0，你的面试题AI助手！',
    memoryId: aiId.value
  })
}


// ai容器
const contentRef = ref(null)
// 添加滚动值
const scrollTop = ref(0)

// 滚动到底部方法
const scrollToBottom = async () => {
  await nextTick()
  const query = uni.createSelectorQuery()
  query.selectAll('.box').boundingClientRect(data => {
    if (data) {
      // 计算所有内容的总高度
      const totalHeight = data.reduce((sum, item) => sum + item.height, 0)
      scrollTop.value = totalHeight + 100 // 加上额外高度确保滚动到底部
    }
  }).exec()
}

const isSpeaking = ref(false)
// 语音播报
const readAloud = (text) => {
  isSpeaking.value = true
  const innerAudioContext = uni.createInnerAudioContext()
  innerAudioContext.src = `https://tts.youdao.com/fanyivoice?word=${encodeURIComponent(text)}&le=zh&keyfrom=speaker-target`
  innerAudioContext.play()
  innerAudioContext.onEnded(() => {
    isSpeaking.value = false
  })
  innerAudioContext.onError(() => {
    isSpeaking.value = false
    uni.showToast({
      title: '播放失败',
      icon: 'error'
    })
  })
}

// 取消语音播报
const cancelReadAloud = () => {
  isSpeaking.value = false
  const innerAudioContext = uni.createInnerAudioContext()
  innerAudioContext.stop()
}


// 复制内容
const copyContent = (content) => {
  // 去除markdown格式
  const plainText = content.replace(/```[\s\S]*?```/g, '')
    .replace(/\*\*/g, '')
    .replace(/\*/g, '')
    .replace(/\[([^\]]+)\]\([^)]+\)/g, '$1')
    .trim()
  
  uni.setClipboardData({
    data: plainText,
    success: () => {
      uni.showToast({
        title: '复制成功',
        icon: 'success',
        duration: 1500
      })
    },
    fail: () => {
      uni.showToast({
        title: '复制失败',
        icon: 'error',
        duration: 1500
      })
    }
  })
}

</script>
<template>
  <view class="ai-box">
    <!-- 顶部导航栏 -->
    <view class="ai-history">
      <view class="left-icon" @click="handleGetHistoryList">
        <uni-icons type="bars" size="24" color="#1677ff"></uni-icons>
      </view>
      <view class="title">新对话</view>
      <view class="right-icon">
        <uni-icons type="plusempty" color="#1677ff" size="25"></uni-icons>
      </view>
    </view>

    <!-- 历史记录抽屉 -->
    <uni-drawer ref="showLeft" :mask-click="true">
      <view class="drawer-content">
        <view class="drawer-title">历史记录</view>
        <scroll-view scroll-y class="history-list">
          <view v-for="(history, historyIndex) in historyList" :key="index" class="history-group">
            <view class="history-date">{{ history.date }}</view>
            <view
              :style="{ 'background-color': activeIndex[historyIndex] === index ? '#f2f3f4' : '', 'pointer-events': isReply ? 'auto' : 'none' }"
              @click="getHistoryContent(record.id, index, historyIndex)" v-for="(record, index) in history.aiHistoryVos"
              :key="item" class="history-item">
              {{ record.title }}
            </view>
          </view>
        </scroll-view>
      </view>
    </uni-drawer>

    <!-- ai输出内容需要判断 -->
    <view class="ai-content">
      <scroll-view ref="contentRef" :duration="3000" :scroll-top="scrollTop" v-if="!chatLoading" scroll-y="true"
        class="scroll-Y" :scroll-with-animation="true" :scroll-anchoring="true" @scrolltoupper="upper">
        <!-- 有数据的时候显示 -->
        <div v-for="(item, index) in messageList" class="box" :key="index">
          <!-- 用户输入的内容 -->
          <div class="prompt-box">
            <div class="user-message">
              <div class="message-content" :class="{ 'prompt': true, 'first-prompt': index === 0 }">
                <zero-markdown-view class="markdown-content" :markdown="item.prompt"></zero-markdown-view>
              </div>
              <template v-if="userInfo.avatar">
                <img class="avatar" :src="userInfo.avatar" />
              </template>
              <template v-else>
                <a-avatar class="avatar" :style="{ backgroundColor: '#1677ff', fontSize: '20px' }">
                  {{ userInfo.account?.charAt(0)?.toUpperCase() }}
                </a-avatar>
              </template>
            </div>
            <div class="message-actions">
              <view class="action-icon" @click="readAloud(item.prompt)" v-if="!isSpeaking">
                <uv-icon name="volume" size="20" color="#666"></uv-icon>
              </view>
              <view class="action-icon" @click="cancelReadAloud" v-else>
                <uv-icon name="volume-off" size="20" color="#666"></uv-icon>
              </view>
              <view class="action-icon" @click="copyContent(item.prompt)">
                <uv-icon name="file-text" size="21" color="#666"></uv-icon>
              </view>
            </div>
          </div>
          <!-- AI返回的内容 -->
          <div class="content-avatar">
            <img class="avatar" src="../../static/images/128.png" alt="">
            <view v-if="!item.content">
              <uni-icons type="spinner-cycle" size="24" color="#1677ff"></uni-icons>
            </view>
            <div class="message-wrapper" v-else>
              <zero-markdown-view class="markdown-content" :markdown="item.content"></zero-markdown-view>
              <div class="message-actions" v-if="aiId !== 0">
                <view class="action-icon" @click="readAloud(item.content)" v-if="!isSpeaking">
                  <uni-icons type="sound" size="20" color="#666"></uni-icons>
                </view>
                <view class="action-icon" @click="cancelReadAloud" v-else>
                  <uv-icon name="volume-off" size="20" color="#666"></uv-icon>
                </view>
                <view class="action-icon" @click="copyContent(item.content)">
                  <uv-icon name="file-text" size="21" color="#666"></uv-icon>
                </view>
              </div>
            </div>
          </div>
        </div>
      </scroll-view>
    </view>


    <!-- 输入框 -->
    <view class="ai-search">
      <uv-textarea adjustPosition v-model="inputValue" height="25" :placeholder="placeholder"></uv-textarea>
      <view class="button-group">
        <view class="mode-buttons">
          <view v-for="item in aiMode" :key="item.value" class="mode-btn"
            :class="{ active: aiModeValue === item.value }" @click="switchMode(item.value)">
            {{ item.label }}
          </view>
        </view>
        <view class="send-btn">
          <uni-icons type="paperplane-filled" size="24" color="#1677ff" v-if="isReply"></uni-icons>
          <uv-icon type="pause-circle" size="24" color="#ff4d4f" v-else></uv-icon>
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

  .drawer-content {
    padding: 20rpx;
    height: 100%;

    .drawer-title {
      font-size: 32rpx;
      font-weight: bold;
      padding: 20rpx 0;
      border-bottom: 1px solid #eee;
    }

    .history-list {
      height: calc(100% - 150rpx);
      padding: 20rpx 0;
    }

    .history-group {
      margin: 20rpx 0;

      .history-date {
        font-size: 24rpx;
        color: #999;
        margin-bottom: 10rpx;
      }

      .history-item {
        font-size: 28rpx;
        color: #333;
        padding: 20rpx;
        border-radius: 8rpx;
        margin-bottom: 10rpx;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;

        &:active {
          opacity: 0.8;
        }
      }

    }
  }

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
      scroll-behavior: smooth;

      .prompt-box {
        display: flex;
        flex-direction: column;

        .message-actions {
          display: flex;
          justify-content: flex-end;
          gap: 16px;
          margin-right: 40px;
          transition: opacity 0.3s;

          .action-icon {
            margin-top: 5px;
            font-size: 16px;
            color: #666;
            cursor: pointer;

            &:hover {
              color: #1677ff;
            }
          }
        }


        .user-message {
          display: flex;
          justify-content: flex-end;
          align-items: flex-start;
          gap: 2px;

          .message-content {
            max-width: calc(100% - 100px);
            background-color: #eff6ff;

            .prompt {
              margin: 10px 0 5px 0 !important;
            }

            .first-prompt {
              margin-top: 0 !important;
            }
          }

          .avatar {
            margin-top: 10px;
            width: 31px;
            height: 31px;
            border-radius: 50%;
            object-fit: cover;
            margin-right: 5px;
          }
        }
      }

      .content-avatar {
        font-size: 16px !important;
        display: flex;
        margin-top: 20px;
        margin-bottom: 10px;

        .avatar {
          margin-top: 60rpx;
          width: 31px;
          height: 31px;
          border-radius: 50%;
          object-fit: cover;
          margin-right: 5px;
        }

        .message-wrapper {
          flex: 1;
          max-width: calc(100% - 50px); // 减去头像和间距的宽度

          :deep(.markdown-content) {
            overflow-x: auto; // 添加横向滚动
            word-wrap: break-word; // 允许在单词内换行
            white-space: pre-wrap; // 保留空格和换行
            max-width: 100%; // 限制最大宽度
          }

          .message-actions {
            display: flex;
            gap: 16px;
            padding: 8px 0;
            transition: opacity 0.3s;

            .action-icon {
              font-size: 16px;
              color: #666;
              cursor: pointer;

              &:hover {
                color: #1677ff;
              }
            }
          }

          &:hover {
            .message-actions {
              opacity: 1;
            }
          }
        }
      }
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