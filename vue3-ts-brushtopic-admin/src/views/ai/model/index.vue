<script setup lang="ts">
import { ref, h, onMounted, onBeforeUnmount, nextTick } from 'vue'
import {
  SearchOutlined,
  PlusOutlined
} from '@ant-design/icons-vue';
import { useSettingStore } from '@/stores/modules/setting.ts'
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
const createReply = () => {
}
const searchHistory = (event: MouseEvent) => {
  event.stopPropagation(); // 阻止事件冒泡
  isSearch.value = true;
  editingId.value = null
  // 确保输入框获得焦点
  setTimeout(() => {
    inputSearch.value?.focus();
  }, 0);
};



const onSearch = () => {
};

// 隐藏清除
const clearSearch = () => {
  searchValue.value = '';
  isSearch.value = false;
};

// 历史记录列表
// 历史记录列表
const historyList = ref<any[]>([
  {
    date: '2023-10-01',
    titleList: [
      { id: 1, title: '历史记录1-1我是一个历史记录' },
      { id: 2, title: '历史记录1-2' }
    ]
  },
  {
    date: '2023-10-02',
    titleList: [
      { id: 3, title: '历史记录2-1' },
      { id: 4, title: '历史记录2-2' }
    ]
  },
  {
    date: '2023-10-03',
    titleList: [
      { id: 5, title: '历史记录3-1' },
      { id: 6, title: '历史记录3-2' }
    ]
  },
  {
    date: '2023-10-04',
    titleList: [
      { id: 7, title: '历史记录4-1' },
      { id: 8, title: '历史记录4-2' }
    ]
  },
  {
    date: '2023-10-05',
    titleList: [
      { id: 9, title: '历史记录5-1' },
      { id: 10, title: '历史记录5-2' }
    ]
  },
  {
    date: '2023-10-05',
    titleList: [
      { id: 9, title: '历史记录5-1' },
      { id: 10, title: '历史记录5-2' }
    ]
  },
  {
    date: '2023-10-05',
    titleList: [
      { id: 9, title: '历史记录5-1' },
      { id: 10, title: '历史记录5-2' }
    ]
  },
  {
    date: '2023-10-05',
    titleList: [
      { id: 9, title: '历史记录5-1' },
      { id: 10, title: '历史记录5-2' }
    ]
  },
  {
    date: '2023-10-05',
    titleList: [
      { id: 9, title: '历史记录5-1' },
      { id: 10, title: '历史记录5-2' }
    ]
  }
])
// // 是否还有历史记录
// const hasMoreData = ref(true);
// // 当前索引
// const activeIndex = ref([])
// // 加载历史记录
// const loadHistory = async () => {
//   // 没有数据了直接返回
//   // if (!hasMoreData.value) return;
//   // historyParams.value.pageNum++
//   // const res = await apiGetRecordList(historyParams.value)
//   // historyList.value.push(...res.data);
//   // if (res.data && res.data[0].length > 0 && res.data[1].length > 0) {
//   // } else {
//   //   hasMoreData.value = false;
//   // }
// }

// // 查询内容
// const getHistoryContent = async (id: number, index: number, historyIndex: number) => {

// }

// // 删除历史记录
// const delHistory = async (id: number) => {

// }

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
// 点击了重命名
const handleEdit = async (record: any) => {
  editingId.value = record.id;
  editValue.value = record.title
  await nextTick();
  // 获取焦点
  const input = document.querySelector('.edit-input');
  if (input) {
    (input as HTMLElement).focus();
  }
}
// 修改输入框失去焦点提交重命名
const handleEditBlur = () => {
  editingId.value = null
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
          <a-button class="search" shape="circle" :icon="h(SearchOutlined)" @click="searchHistory"></a-button>
        </template>
        <!-- 搜索输入框 -->
        <a-input v-if="isSearch" @search="onSearch" allowClear v-model:value="searchValue" ref="inputSearch"
          placeholder="搜索历史记录">
          <template #prefix>
            <SearchOutlined />
          </template>
        </a-input>
      </div>
      <!-- 无限滚动区域历史搜索 -->
      <ul class="infinite-list" style="overflow: auto">
        <!-- 日期和记录 -->
        <div class="list-box" infinite-scroll-distance="100px" v-for="(history, historyIndex) in historyList"
          :key="historyIndex">
          <!-- 日期 -->
          <span class="date">{{ history.date }}</span>
          <!-- 标题 -->
          <!-- 开始遍历 -->
          <li v-for="(record, index) in history.titleList" :key="index"
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
              <EditOutlined class="edit" @click="handleEdit(record)" />
              <DeleteOutlined class="del" />
            </template>
          </li>
        </div>
      </ul>
    </div>
    <!-- 右侧输入大模型 -->
    <div class="model-print"></div>
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
}


// 无限滚动历史记录样式
.infinite-list {
  height: 580px;

  .date {
    color: #1677ff;
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
</style>
