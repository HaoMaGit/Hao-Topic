<script setup lang="ts">
import { ref, h, watch, onMounted, onBeforeUnmount, nextTick, reactive } from 'vue'
import {
  SearchOutlined,
  PlusOutlined,
  SendOutlined,
  RobotOutlined,
  ApiOutlined,
  AppstoreOutlined,
  PauseCircleOutlined
} from '@ant-design/icons-vue';
import { useUserStore } from '@/stores/modules/user';
import { MdPreview } from 'md-editor-v3'
import 'md-editor-v3/lib/preview.css'
const userStore = useUserStore()
import { useSettingStore } from '@/stores/modules/setting.ts'
import { message } from 'ant-design-vue';
import { v4 as uuidv4 } from 'uuid'; // 引入 uuid 库
import { apiGetManageList, apiGetHistoryDetail, apiRenameHistory, apiDeleteHistory } from '@/api/ai/model/index'
import { Modal } from 'ant-design-vue'
import type { AiHistoryDto } from '@/api/ai/model/type';
// 引入系统设置
const settingStore = useSettingStore()

// 搜索输入框
const inputSearch = ref()
// 是否点击了搜索
const isSearch = ref(false)
// 是否点击回复
const isReply = ref(true)
// 搜索的值
const searchValue = ref('')

// 新增对话
const createReply = () => {
  // 判断是否为最新对话
  if (aiId.value === 0) {
    message.success('已是最新对话')
    return
  }
  message.success('创建成功')
  setTimeout(() => {
    // 创建一个新对话刷新界面
    window.location.reload()
  }, 500)
}
// 点击添加历史记录
const searchHistory = (event: MouseEvent) => {
  event.stopPropagation(); // 阻止事件冒泡
  isSearch.value = true;
  editingId.value = null
  // 确保输入框获得焦点
  setTimeout(() => {
    inputSearch.value?.focus();
  }, 0);
};
// 开始搜索
const onSearch = () => {
  // 去除空格
  searchValue.value = searchValue.value.trim();
  historyParams.value.title = searchValue.value
  getHistoryList()
};

// 隐藏清除
const clearSearch = () => {
  isSearch.value = false;
};

// 历史记录列表
const historyList = ref<any[]>([])
// 历史记录loading
const historyLoading = ref(false)
// 历史记录参数
const historyParams = ref<AiHistoryDto>({
  pageNum: 1,
  pageSize: 999,
  title: searchValue.value
})
// 获取历史记录
const getHistoryList = async () => {
  historyLoading.value = true
  const res = await apiGetManageList(historyParams.value)
  historyList.value = res.data
  historyLoading.value = false
}
onMounted(() => {
  getHistoryList()
})

// 加载对话记录loading
const chatLoading = ref(false)
// 当前索引
const activeIndex = ref<any>([])

// 查询内容
const getHistoryContent = async (id: number, index: number, historyIndex: number) => {
  chatLoading.value = true
  // 清除默认对话
  restoreDefaultRecord()
  // 重置当前索引
  activeIndex.value = []
  // 添加当前索引
  activeIndex.value[historyIndex] = index
  // id:当前记录id
  const res = await apiGetHistoryDetail(id)
  // 封装内容
  const content = res.data.map((item: any) => {
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
  chatLoading.value = false
  await scrollToBottom()
}


// 监听屏幕点击事件
const handleClickOutside = (event: MouseEvent) => {
  const target = event.target as HTMLElement;
  // 排除搜索按钮和清除按钮的点击
  if (target.closest('.search')) {
    return;
  }
  if (inputSearch.value && !inputSearch.value.$el.contains(event.target as Node)) {
    if (!searchValue.value) {
      clearSearch();
    }
  }
};

onMounted(() => {
  if (promptInput.value) {
    promptInput.value?.focus();
  }
  document.addEventListener('click', handleClickOutside);
});

onBeforeUnmount(() => {
  document.removeEventListener('click', handleClickOutside);
});

// 当前要重命名的id
const editingId = ref<number | null>(null);
// 修改输入框
const editInput = ref()
// 要修改的值
const editValue = ref('')
// 当前标题
const currentTitle = ref('')
// 点击了重命名
const handleEdit = async (record: any) => {
  editingId.value = record.id;
  editValue.value = record.title
  currentTitle.value = record.title
  await nextTick();
  // 获取焦点
  const input = document.querySelector('.edit-input');
  if (input) {
    (input as HTMLElement).focus();
  }
}
// 修改输入框失去焦点提交重命名
const handleEditBlur = async () => {
  editValue.value = editValue.value.trim();
  // 判断重命名的值是否为空
  if (!editValue.value) {
    editingId.value = null;
    return;
  }
  // 是否跟原来的标题一摸一样的
  if (currentTitle.value === editValue.value) {
    editingId.value = null;
    return;
  }
  // 提交重命名
  await apiRenameHistory({
    id: editingId.value,
    title: editValue.value
  })
  editingId.value = null
  message.success('修改成功')
  // 重新加载历史记录
  getHistoryList()
}

// 模式
const aiMode = reactive([
  {
    label: '系统模式',
    value: 'system',
    icon: RobotOutlined,
    desc: 'AI从系统题库中提取题目逐题提问并根据系统答案校验正确性'
  }, {
    label: '模型模式',
    value: 'model',
    icon: ApiOutlined,
    desc: '完全使用AI生成的题目覆盖更开放或创新题型'
  }, {
    label: '混合模式',
    value: 'mix',
    icon: AppstoreOutlined,
    desc: 'AI随机混合系统题库和AI自定义题目增加多样性'
  },
])
// 当前选中的模式
const aiModeValue = ref('system')
// 发送表单
const promptInput = ref()
// 发送的内容
const prompt = ref('')
// 提示词 
// 请输入题目答案，AI将自动判断并反馈给您
// 系统模式需要输入题目分类，将会为你生成题目
const placeholder = ref('请输入系统中的正确的题目分类，AI将自动生成题目')
// 初始化提示词
const initPlaceholder = () => {
  // 判断是否有内容
  if (messageList.length > 2) {
    placeholder.value = '请输入题目答案，AI将自动判断并反馈给您'
  }
  if (aiModeValue.value === 'system') {
    placeholder.value = '请输入题目分类，AI将自动生成题目'
    return
  } else if (aiModeValue.value === 'model' || aiModeValue.value === 'mix') {
    placeholder.value = '请输入给AI你想刷的类型，AI将自动生成题目'
    return
  }
}
watch(() => aiModeValue.value, () => {
  initPlaceholder()
})
const { VITE_SERVE, VITE_APP_BASE_API } = import.meta.env
// 发送
const sendPrompt = async () => {
  isReply.value = false
  if (prompt.value) {
    if (aiId.value === 0) {
      // 说明是第一次
      localStorage.setItem('chatId', currentRecordId.value)
    }
    aiId.value++
    // 添加一条数据
    messageList.push({
      prompt: prompt.value,
      memoryId: aiId.value,
      chatId: localStorage.getItem('chatId') || currentRecordId.value, // 当前对话id
      model: aiModeValue.value,
      content: ''
    })
    // 获取当前记录
    const currentRecord = messageList[messageList.length - 1]
    try {
      // 创建新的 AbortController
      currentController = new AbortController()
      // 发送 get 请求
      fetch(`${VITE_SERVE}${VITE_APP_BASE_API}/ai/model/chat`, {
        method: "POST",
        // eslint-disable-next-line @typescript-eslint/ban-ts-comment
        // @ts-expect-error
        headers: {
          "Content-Type": "application/json",
          "Authorization": userStore.token,
        },
        // 添加 signal
        signal: currentController.signal,
        body: JSON.stringify({
          ...currentRecord
        }),
      }).then(response => {
        // 检查响应是否成功
        if (!response.ok) {
          message.error("HaoAi回复出现点问题请稍后再试！")
        }
        // 返回一个可读流
        return response.body;
      }).then(async body => {
        prompt.value = ''
        if (!body) {
          message.error("HaoAi回复出现点问题请稍后再试！")
          return
        }
        // 获取读取流
        const reader = body.getReader();
        // 读取流
        const decoder = new TextDecoder('utf-8')
        // 循环读取流
        while (true) {
          try {
            if (reader) {
              const { value, done } = await reader.read()
              if (done) {
                message.success('回复成功')
                // 回复成功
                isReply.value = true
                // 如果是第一次
                if (aiId.value === 1) {
                  // 获取一下历史记录
                  getHistoryList()
                  activeIndex.value[0] = 0
                }
                break
              }
              await nextTick(() => {
                // 累积新内容
                currentRecord.content += decoder.decode(value)
              })
              await scrollToBottom()
            }
          } catch (readError: any) {
            if (readError.name === 'AbortError') {
              message.info('已暂停回复')
              isReply.value = true
            } else {
              message.error('回复失败' + readError)
            }
            break
          }
        }
      })
    } catch (error: any) {
      console.log(error);
      if (error.name === 'AbortError') {
        message.info('已暂停回复')
      } else {
        message.error('发送失败' + error)
      }
    }
  }
}
// ai容器
const contentRef = ref<any>(null)
// 滚动到底部
const scrollToBottom = async () => {
  await nextTick()
  if (contentRef.value) {
    // 平滑滚动到底部
    contentRef.value.scrollTo({
      top: contentRef.value.scrollHeight,
      behavior: 'smooth'
    })
  }
}


// ai标识
const aiId = ref(0)
// 记录一下当前使用的id
const currentRecordId = ref(uuidv4())
// 内容
const messageList = reactive<any>([
  {
    prompt: "我是" + userStore.userInfo.account,
    chatId: currentRecordId.value, // 对话id
    model: aiModeValue.value,
    content: '你好，我是HaoAi 1.0，你的面试题AI助手！',
    memoryId: aiId.value
  }
])
// 恢复默认记录
const restoreDefaultRecord = () => {
  // 清空历史记录
  messageList.splice(0, messageList.length)
  // 添加一条数据
  messageList.push({
    prompt: "我是" + userStore.userInfo.account,
    chatId: currentRecordId.value, // 对话id
    model: aiModeValue.value,
    content: '你好，我是HaoAi 1.0，你的面试题AI助手！',
  })
}


// 是否正在朗读
const isSpeaking = ref(false);
// 当前audio实例
let currentAudio: HTMLAudioElement | null = null;
// 随机loading文案
const loadingTextArr = [
  "正在召唤HaoAi发声中...",
  "HaoAi正在努力变声，请稍等~",
  "语音合成中，马上就能听到啦！",
  "HaoAi正在认真朗读你的内容...",
  "别着急，精彩马上开始！"
];
// 随机loading文案
const loadingText = ref(loadingTextArr[Math.floor(Math.random() * loadingTextArr.length)]);
// 语音合成loading
const readAloudLoading = ref(false)
// 朗读文字
const readAloud = (content: string) => {
  // 如果有正在播放的音频，先停止
  if (currentAudio) {
    currentAudio.pause();
    currentAudio.currentTime = 0;
    currentAudio = null;
  }
  readAloudLoading.value = true
  // 开始朗读
  isSpeaking.value = true
  // 调用语音合成模型
  fetch(`${VITE_SERVE}${VITE_APP_BASE_API}/ai/model/tts?text=${content}`, {
    method: "get",
    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
    // @ts-expect-error
    headers: {
      "Authorization": userStore.token,
    },
  })
    .then(res => res.blob())
    .then(blob => {
      readAloudLoading.value = false
      // 合成对话
      const url = URL.createObjectURL(blob);
      // 创建播放器
      const audio = new Audio(url);
      currentAudio = audio
      // 播放
      audio.play();
      // 监听播放结束，自动重置
      audio.onended = () => {
        isSpeaking.value = false;

        currentAudio = null;
        URL.revokeObjectURL(url);
      };
    });
}
// 取消播放
const cancelReadAloud = () => {
  isSpeaking.value = false;
  if (currentAudio) {
    currentAudio.pause();
    currentAudio.currentTime = 0;
    currentAudio = null;
  }
}

// 删除对话历史记录
const handleDel = (id: number) => {
  Modal.confirm({
    title: '确认删除这条对话记录吗？',
    content: '删除后对话记录无法恢复和找回，请谨慎操作。',
    okText: '确认删除',
    okType: 'danger',
    cancelText: '取消',
    async onOk() {
      await apiDeleteHistory(id)
      message.success('删除成功')
      setTimeout(() => {
        // 创建一个新对话刷新界面
        window.location.reload()
      }, 500)
    }
  })
}

// 复制
const copyContent = async (content: string) => {
  try {
    await navigator.clipboard.writeText(content)
    message.success('复制成功')
  } catch {
    message.error('复制失败，请手动复制')
  }
}

// 是否暂停回复
const isPaused = ref(false)
// 当前的 AbortController 实例
let currentController: AbortController | null
// 添加暂停函数
const pauseReply = () => {
  if (currentController) {
    // 暂停回复
    currentController.abort()
    currentController = null
    isPaused.value = false
    isReply.value = true
  }
}
</script>
<template>
  <div class="model-body">
    <!-- 左侧历史记录 -->
    <div class="model-history" v-if="!settingStore.fold">
      <!-- 新建对话和搜索 -->
      <div class="btn-search">
        <template v-if="!isSearch">
          <!-- 新键对话 -->
          <a-button class="btn" shape="round" :disabled="!isReply" @click="createReply" type="primary"
            :icon="h(PlusOutlined)">新建对话</a-button>
          <!-- 搜索 -->
          <a-button class="search" :disabled="!isReply" shape="circle" :icon="h(SearchOutlined)"
            @click="searchHistory"></a-button>
        </template>
        <!-- 搜索输入框 -->
        <a-input v-if="isSearch" @blur="onSearch" allowClear v-model:value="searchValue" ref="inputSearch"
          placeholder="搜索历史刷题记录">
          <template #prefix>
            <SearchOutlined />
          </template>
        </a-input>
      </div>
      <!-- 无限滚动区域历史搜索 -->
      <ul class="infinite-list" style="overflow: auto" v-if="!historyLoading">
        <!-- 日期和记录 -->
        <div class="list-box" v-for="(history, historyIndex) in historyList" :key="historyIndex">
          <!-- 日期 -->
          <span class="date">{{ history.date }}</span>
          <!-- 标题 -->
          <!-- 开始遍历 -->
          <li @click="getHistoryContent(record.id, index, historyIndex)" v-for="(record, index) in history.aiHistoryVos"
            :key="index"
            :style="{ 'background-color': activeIndex[historyIndex] === index ? '#f2f3f4' : '', 'pointer-events': isReply ? 'auto' : 'none' }"
            :class="{ 'infinite-list-item': true, 'no-hover': editingId === record.id, 'hover': editingId !== record.id }">
            <!-- 历史记录 -->
            <template v-if="editingId === record.id">
              <!-- 编辑历史记录 -->
              <a-input class="edit-input" v-model:value="editValue" @blur="handleEditBlur" ref="editInput" />
            </template>
            <div class="history" v-if="editingId !== record.id">
              {{ record.title }}
            </div>
            <template v-if="editingId !== record.id">
              <!-- 操作图标按钮 -->
              <EditOutlined class="edit" @click.stop="handleEdit(record)" />
              <DeleteOutlined class="del" @click.stop="handleDel(record.id)" />
            </template>
          </li>
        </div>
      </ul>
      <!-- 语音合成的loading -->
      <a-spin tip="HaoAi正在搜寻你的历史记录哦" v-else></a-spin>
    </div>

    <!-- 右侧输入大模型 -->
    <div class="model-print">
      <!-- 标题 -->
      <h2 class="title">HaoAi<i class="version">1.0</i></h2>
      <!-- 回复区域 -->
      <div class="reply-box">
        <ul class="infinite-list-reply" ref="contentRef" v-if="!chatLoading">
          <!-- 有数据的时候显示 -->
          <template v-if="aiId >= 0">
            <div v-for="(item, index) in messageList" class="box" :key="index">
              <!-- 用户输入的内容 -->
              <div class="prompt-box">
                <div class="user-message">
                  <div class="message-content" :class="{ 'prompt': true, 'first-prompt': index === 0 }">
                    <MdPreview v-model="item.prompt" class="prompt-preview">
                    </MdPreview>
                  </div>
                  <a-avatar class="avatar"
                    :src="userStore.userInfo.avatar != null ? userStore.userInfo.avatar : 'http://114.116.233.218:9000/topic/H.png'" />
                </div>
                <div class="message-actions">
                  <a-tooltip title="朗读" placement="bottom" v-if="!isSpeaking">
                    <SoundOutlined class="action-icon" @click="readAloud(item.prompt)" />
                  </a-tooltip>
                  <a-tooltip title="暂停" placement="bottom" v-else>
                    <PauseOutlined class="action-icon" @click="cancelReadAloud" />
                  </a-tooltip>
                  <a-tooltip title="复制" placement="bottom" @click="copyContent(item.prompt)">
                    <CopyOutlined class="action-icon" />
                  </a-tooltip>
                </div>
              </div>
              <!-- AI返回的内容 -->
              <div class="content-avatar">
                <!-- 需要带一个头像 -->
                <a-avatar class="avatar" :src="'http://114.116.233.218:9000/topic/H.png'"></a-avatar>
                <!-- 加载中的图标 -->
                <LoadingOutlined v-if="!item.content" />
                <div class="message-wrapper" v-else>
                  <MdPreview v-model="item.content" class="md-preview" style="max-height: 100%;"></MdPreview>
                  <div class="message-actions" v-if="aiId !== 0">
                    <a-tooltip title="朗读" placement="bottom" v-if="!isSpeaking">
                      <SoundOutlined class="action-icon" @click="readAloud(item.content)" />
                    </a-tooltip>
                    <a-tooltip title="暂停" placement="bottom" v-else>
                      <PauseOutlined class="action-icon" @click="cancelReadAloud" />
                    </a-tooltip>
                    <a-tooltip title="复制" placement="bottom" @click="copyContent(item.content)">
                      <CopyOutlined class="action-icon" />
                    </a-tooltip>
                  </div>
                </div>
              </div>
            </div>
          </template>
        </ul>
        <!-- 聊天的loading -->
        <a-spin tip="HaoAi正在查询对话记录" v-else></a-spin>
        <!-- 语音合成的loading -->
        <a-spin :tip="loadingText" v-if="readAloudLoading"></a-spin>
        <!-- 输入框 -->
        <div class="search-box">
          <a-textarea type="textarea" ref="promptInput" v-model:value="prompt" :auto-size="{ minRows: 1, maxRows: 1 }"
            :placeholder="placeholder" />
          <div class="action-icons">
            <div class="left-icons">
              <a-radio-group v-model:value="aiModeValue" button-style="solid">
                <a-tooltip v-for="(tag, index) in aiMode" :key="index" :title="tag.desc" placement="top">
                  <a-radio-button :value="tag.value">
                    <component :is="tag.icon" class="mode-icon" />
                    {{ tag.label }}
                  </a-radio-button>
                </a-tooltip>
              </a-radio-group>
            </div>
            <div class="right-icons">
              <template v-if="!isReply">
                <PauseCircleOutlined class="pause-icon" @click="pauseReply" />
              </template>
              <template v-else>
                <SendOutlined class="send-icon" :class="{ 'disabled': !prompt }" @click="prompt && sendPrompt()" />
              </template>
              <!-- <SendOutlined class="send-icon" :class="{ 'disabled': !prompt }" @click="prompt && sendPrompt()" /> -->
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<style lang="scss" scoped>
.model-body {
  display: flex;

  .model-history {
    width: 230px;

    .btn-search {
      margin-bottom: 18px;
    }

    .btn {
      width: 180px;
    }

    .search {
      margin-left: 8px;
    }
  }

  .model-print {
    margin-left: 3px;
    flex: 1;

    .title {
      font-weight: bold;

      .version {
        color: #8f91a8;
        margin-left: 3px;
      }
    }

    .search-box {
      position: relative;
      background: #f9f9f9;
      border-radius: 8px;
      padding: 5px;

      .left-icons {
        :deep(.ant-radio-group) {
          display: flex;
          gap: 8px;

          .ant-radio-button-wrapper {
            display: flex;
            align-items: center;
            font-size: 12px;
            padding: 0 6px; // 减小内边距
            height: 24px; // 减小高度
            border-radius: 4px; // 调整圆角
            border: 1px solid #d9d9d9;

            &:first-child {
              border-radius: 6px;
            }

            &:last-child {
              border-radius: 6px;
            }

            &::before {
              display: none;
            }

            .mode-icon {
              margin-right: 4px;
              font-size: 12px;
            }

            &.ant-radio-button-wrapper-checked {
              border-color: #1677ff;
              background: #e6f4ff;
              color: #1677ff;
            }
          }
        }
      }

      :deep(.ant-input) {
        background: transparent;
        border: none;
        box-shadow: none;
        resize: none;
        padding: 8px 12px;
        font-size: 14px;

        &:focus {
          box-shadow: none;
        }
      }

      .action-icons {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 8px 12px 0px 12px;

        .left-icons {
          .icon {
            margin-right: 15px;
            font-size: 16px;
            color: #666;
            cursor: pointer;

            &:hover {
              color: #1677ff;
            }
          }
        }

        .right-icons {
          .send-icon {
            font-size: 25px;
            color: #1677ff;
            cursor: pointer;

            &.disabled {
              color: #d6dee8;
              cursor: not-allowed;
            }

            &:not(.disabled):hover {
              color: #4096ff;
            }

            &:hover {
              color: #4096ff;
            }
          }

          .pause-icon {
            font-size: 25px;
            color: #ff4d4f;
            cursor: pointer;

            &:hover {
              color: #ff7875;
            }
          }
        }
      }
    }
  }
}


// 无限滚动历史记录样式
.infinite-list {
  height: calc(100vh - 185px);

  .date {
    color: #666666;
    font-size: 12px;
    cursor: pointer;
  }

  // 隐藏默认滚动条
  &::-webkit-scrollbar {
    display: none;
  }

  &:hover {
    &::-webkit-scrollbar {
      display: block;
    }
  }

  .hover {
    &:hover {
      background-color: #f2f3f4;

      .del {
        color: red;
        opacity: 1;
        margin-left: 10px;
      }

      .edit {
        margin-left: 5px;
        opacity: 1;
      }
    }
  }

  .infinite-list-item {
    .del {
      opacity: 0;

    }

    .edit {
      opacity: 0;
    }

    margin-top: 10px;
    width: 220px;
    display: flex;
    justify-content: space-between;
    height: 40px;
    color: #26244c;
    margin-bottom: 4px;
    font-size: 14px;
    cursor: pointer;
    border-radius: 40px;
    padding: 10px;

    .history {
      width: 100%;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }
  }
}

.reply-box {
  flex: 1;
  display: flex;
  flex-direction: column;
  height: calc(100vh - 165px);

  .infinite-list-reply {
    flex: 1;
    overflow-y: auto;
    padding: 10px;


    .prompt-box {
      display: flex;
      flex-direction: column;

      .message-actions {
        display: flex;
        justify-content: flex-end;
        gap: 16px;
        margin-right: 40px;
        opacity: 0;
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

      &:hover {
        .message-actions {
          opacity: 1;
        }
      }

      .user-message {
        display: flex;
        justify-content: flex-end;
        align-items: flex-start;
        gap: 2px;

        .message-content {
          max-width: calc(100% - 100px);


          .prompt-preview {
            background-color: #eff6ff;
            border-radius: 10px;
            padding: 8px 12px;

            :deep(.md-preview-wrapper) {
              background: transparent;
              padding: 0;
              margin: 0;
            }
          }

          .prompt {
            margin: 10px 0 5px 0 !important;
          }

          .first-prompt {
            margin-top: 0 !important;
          }
        }
      }
    }

    .content-avatar {
      font-size: 16px !important;
      display: flex;
      margin-top: 20px;
      margin-bottom: 10px;

      .avatar {
        margin-right: 5px;
      }

      .message-wrapper {
        flex: 1;

        .message-actions {
          display: flex;
          gap: 16px;
          padding: 8px 0;
          opacity: 0;
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

}
</style>
