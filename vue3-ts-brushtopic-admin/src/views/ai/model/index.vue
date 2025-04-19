<script setup lang="ts">
import { ref, h, onMounted, onBeforeUnmount } from 'vue'
import {
  SearchOutlined,
  PlusOutlined
} from '@ant-design/icons-vue';

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

// // 历史记录列表
// const historyList = ref<any>([])
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
</script>
<template>
  <div class="model-body">
    <!-- 左侧历史记录 -->
    <div class="model-history">
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
        <!-- 日期 -->
        <div class="list-box" infinite-scroll-distance="100px">
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
  background-color: red;
  height: 580px;
}
</style>
