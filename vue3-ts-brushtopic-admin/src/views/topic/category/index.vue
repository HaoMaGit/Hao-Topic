<script setup lang="ts">
import { reactive, ref, h, onMounted, createVNode } from 'vue'
import {
  PlusOutlined,
  EditOutlined,
  DeleteOutlined,
  DownloadOutlined,
  UploadOutlined,
  LoadingOutlined,
  ExclamationCircleOutlined
} from '@ant-design/icons-vue'
import type { TopicCatgoryQueryType } from '@/api/topic/category/type';
import { addDateRange, clearDateRange } from '@/utils/date';
import { message, Modal } from 'ant-design-vue';
import FileSaver from 'file-saver'
import type { UploadChangeParam } from 'ant-design-vue';
import { useUserStore } from '@/stores/modules/user';
const userStore = useUserStore()
// 获取上传路径
const { VITE_SERVE } = import.meta.env
// 请求头
const headers = { authorization: userStore.token };
/**
 * 查询参数
 */
const params = ref<TopicCatgoryQueryType>({
  pageNum: 1,
  pageSize: 5,
  categoryName: '',
  params: {}
})

// 获取分类列表
const getTopicCategoryList = () => {
}

// total
const total = ref(0);
// 时间
const createTimeDateRange = ref([]);
const updateTimeDateRange = ref([]);
// 表格loading
const tableLoading = ref(false)
// 表格数据
const tableData = ref([])
// 是否移入删除按钮
const isDangerHover = ref(false)
// 表格字段
const columns = [
  {
    title: '序号',
    dataIndex: 'id',
    key: 'id',
    align: 'center',
    width: 100,
  },
  {
    title: '分类名称',
    dataIndex: 'categoryName',
    key: 'categoryName',
    align: 'center',
    width: 150,
  },
  {
    title: '创建人',
    dataIndex: 'createBy',
    key: 'createBy',
    align: 'center',
    width: 130,
  },
  {
    title: '状态',
    dataIndex: 'status',
    key: 'status',
    align: 'center',
    width: 120,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    key: 'createTime',
    align: 'center',
    width: 180,
  },
  {
    title: '修改时间',
    dataIndex: 'updateTime',
    key: 'updateTime',
    align: 'center',
    width: 180,
  },
  {
    title: '操作',
    dataIndex: 'operation',
    key: 'operation',
    align: 'center',
  }
]
// 搜索
const handleQuery = () => {
  if (createTimeDateRange.value && createTimeDateRange.value.length > 0) {
    params.value = addDateRange(params.value, createTimeDateRange.value, 'CreateTime')
  } else {
    params.value = clearDateRange(params.value, 'CreateTime')
  }
  if (updateTimeDateRange.value && updateTimeDateRange.value.length > 0) {
    params.value = addDateRange(params.value, updateTimeDateRange.value, 'UpdateTime')
  } else {
    params.value = clearDateRange(params.value, 'UpdateTime')
  }
  getTopicCategoryList()
}
// 重置
const handleReset = () => {
  params.value = {
    pageNum: 1,
    pageSize: 5,
    categoryName: '',
    params: null
  }
  createTimeDateRange.value = []
  updateTimeDateRange.value = []
  total.value = 0
  getTopicCategoryList()
}

// 选中数组
const onSelectedRowKeys = ref<number[]>([])

// 选中菜单
const onSelectChange = (selectedRowKeys: any) => {
  onSelectedRowKeys.value = selectedRowKeys
}



// 新增
const handleAdd = () => {

}
// 修改
const handleEdit = (record: any) => {
  console.log(record);

}
// 删除
const handleDelete = (record: any) => {
  Modal.confirm({
    title: '是否确认删除该分类?',
    icon: createVNode(ExclamationCircleOutlined),
    content: createVNode('div', { style: 'color:red;' }, '删除分类会导致相关题目分类丢失，请慎重考虑!'),
    async onOk() {
      if (record.id) {
        onSelectedRowKeys.value.push(record.id)
      }
      // await apiDeleteUser(onSelectedRowKeys.value)
      message.success('删除成功')
    },
    onCancel() {
      console.log('Cancel');
    },
  });
}

// 导入
const handleImport = () => {
  upload.open = true
}

// 导出
const handleExport = () => {

}

// 分页变化处理
const handleTableChange = (pagination: any) => {
  params.value.pageNum = pagination.current;
  params.value.pageSize = pagination.pageSize;
  getTopicCategoryList();
}


// 导入参数
const upload = reactive({
  // 是否显示弹出层（用户导入）
  open: false,
  // 是否更新已经存在的用户数据
  updateSupport: 0,
  // 设置上传的请求头部
  headers,
  // 上传文件的loading
  uploadLoading: false,
  // 上传的地址
  url: VITE_SERVE + "/api/topic/category/import",
  // 上传的文件
  uploadFileList: [],
  // 结果弹窗
  oepnResult: false,
  // 结果
  result: ""
});
// 关闭弹窗
const handleCancel = () => {
  upload.open = false
  upload.uploadFileList = []
}

// 下载模板
const importTemplate = async () => {
  const response: any = null
  // const res = await apiGetExportTemplate()
  // response = res
  FileSaver.saveAs(response, `易题系统题目分类数据模板_${new Date().getTime()}.xlsx`)
}

// 上传文件前校验
const handleBeforeUpload = (file: any) => {
  // 截取最后的.判断类型
  const type = file.name.split('.').pop()
  const isExcel = type === 'xls' || type === 'xlsx';
  if (!isExcel) {
    message.error('上传excel文件格式错误');
  }
  const isLt5M = file.size / 1024 / 1024 < 5;
  if (!isLt5M) {
    message.error('excel文件不能超过2M');
  }
  return isExcel && isLt5M;
}

// 上传成功
const handleUploadChange = (info: UploadChangeParam) => {
  // 判断是否上传中
  if (info.file.status === 'uploading') {
    upload.uploadLoading = true;
    return;
  }
  // 上传完成
  if (info.file.status === 'done') {
    upload.uploadLoading = false;
    upload.oepnResult = true
    handleCancel()
    upload.result = info.file.response.message
    message.success('上传成功');
  }
  // 上传失败
  if (info.file.status === 'error') {
    upload.uploadLoading = false;
    upload.oepnResult = true
    handleCancel()
    upload.result = info.file.response.message
    message.error(info.file.response.message);
  }
}


onMounted(() => {
  getTopicCategoryList()
})
</script>
<template>
  <div class="user-body">
    <a-space class="space-box">
      <div class="query-box">
        <a-space :size="23">
          <!-- 查询条件 -->
          <a-form-item label="分类名称">
            <a-input placeholder="请输入分类名称" v-model:value="params.categoryName"></a-input>
          </a-form-item>
          <a-form-item label="创建时间">
            <a-range-picker class="range-picker" v-model:value="createTimeDateRange" />
          </a-form-item>
          <a-form-item label="修改时间">
            <a-range-picker class="range-picker" v-model:value="updateTimeDateRange" />
          </a-form-item>
          <a-form-item>
            <a-space>
              <a-button type='primary' @click="handleQuery">搜索</a-button>
              <a-button @click="handleReset">重置</a-button>
            </a-space>
          </a-form-item>
        </a-space>
      </div>
    </a-space>
    <!-- 操作按钮 -->
    <a-form-item v-if="!tableLoading">
      <a-space>
        <a-button type="primary" :icon="h(PlusOutlined)" @click="handleAdd">新增</a-button>
        <a-button :disabled="onSelectedRowKeys.length == 0" @mouseenter="isDangerHover = true"
          @mouseleave="isDangerHover = false" :danger="isDangerHover" :icon="h(DeleteOutlined)"
          @click="handleDelete(null)">删除</a-button>
        <a-button :icon="h(UploadOutlined)" @click="handleImport">导入</a-button>
        <a-button :disabled="tableData.length == 0" :icon="h(DownloadOutlined)" @click="handleExport">导出</a-button>
      </a-space>
    </a-form-item>

    <!-- 表格 -->
    <a-table :pagination="{
      current: params.pageNum,
      pageSize: params.pageSize,
      total: total,
      showTotal: (total: any) => `共 ${total} 条`,
      showSizeChanger: true,
    }" @change="handleTableChange" :loading="tableLoading" :dataSource="tableData" :columns="columns" rowKey="id"
      :row-selection="{ selectedRowKeys: onSelectedRowKeys, onChange: onSelectChange, fixed: true }">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'operation'">
          <a-button type="link" size="small" :icon="h(EditOutlined)" @click="handleEdit(record)">修改</a-button>
          <a-button type="link" size="small" :icon="h(DeleteOutlined)" @click="handleDelete(record)">删除</a-button>
        </template>
        <template v-if="column.key === 'status'">
          <span>{{ record.status === 0 ? '正常' : '停用' }}</span>
        </template>
      </template>
    </a-table>



    <!-- 导入excel -->
    <a-modal @cancel="handleCancel" :footer="null"
      bodyStyle="display: flex; flex-direction: column; align-items: center; text-align: center;"
      v-model:open="upload.open" title="导入题目分类数据">
      <a-upload-dragger maxCount="1" style="width: 100%;" v-model:fileList="upload.uploadFileList" name="file"
        :multiple="true" :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :before-upload="handleBeforeUpload" @change="handleUploadChange">
        <p class="ant-upload-drag-icon">
          <loading-outlined v-if="upload.uploadLoading"></loading-outlined>
          <inbox-outlined v-else></inbox-outlined>
        </p>
        <p class="ant-upload-text">将文件拖到此处或点击上传</p>
      </a-upload-dragger>
      <a-checkbox class="checkbox" v-model:checked="upload.updateSupport">是否更新已经存在的题目分类数据</a-checkbox>
      <p>仅允许导入xls、xlsx格式文件<a-button type="link" @click="importTemplate">下载模板</a-button></p>
    </a-modal>

    <!-- 导入成功以及失败的弹窗 -->
    <a-modal :footer="null" v-model:open="upload.oepnResult" title="导入结果">
      <div v-html="upload.result"></div>
    </a-modal>
  </div>
</template>
<style lang="scss" scoped>
.space-box {
  display: flex;
  justify-content: space-between;

  .query-box {
    display: flex;
    justify-content: flex-end;

    .range-picker {
      width: 240px;
    }
  }
}

.checkbox {
  margin-top: 10px;
}
</style>
