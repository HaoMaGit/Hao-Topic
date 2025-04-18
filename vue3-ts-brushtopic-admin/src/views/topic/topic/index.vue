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
import { apiGetTopicList, apiAddTopic, apiGetExportTemplate, apiUpdateTopic, apiDeleteTopic, apiExportTopic } from '@/api/topic/topic'
import { apiGetSubjectName } from '@/api/topic/subject'
import { apiGetLabelName } from '@/api/topic/label'
import type { TopicQueryType } from '@/api/topic/topic/type';
import { addDateRange, clearDateRange } from '@/utils/date';
import { message, Modal } from 'ant-design-vue';
import FileSaver from 'file-saver'
import type { UploadChangeParam } from 'ant-design-vue';
import { useUserStore } from '@/stores/modules/user';
const userStore = useUserStore()
// 获取上传路径
const { VITE_SERVE } = import.meta.env

/**
 * 查询参数
 */
const params = ref<TopicQueryType>({
  pageNum: 1,
  pageSize: 5,
  topicName: '',
  createBy: '',
  params: {}
})

// 获取题目列表
const getTopicTopicList = async () => {
  tableLoading.value = true
  const res = await apiGetTopicList(params.value)
  total.value = res.data.total
  tableData.value = res.data.rows
  tableLoading.value = false
}

// 数量
const total = ref(0);
// 时间
const createTimeDateRange = ref([]);
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
    title: '题目名称',
    dataIndex: 'topicName',
    key: 'topicName',
    align: 'center',
    width: 150,
  },
  {
    title: '题目答案',
    dataIndex: 'answer',
    key: 'answer',
    align: 'center',
    width: 150,
  },
  {
    title: 'AI答案',
    dataIndex: 'aiAnswer',
    key: 'aiAnswer',
    align: 'center',
    width: 150,
  },

  {
    title: '是否每日',
    dataIndex: 'isEveryday',
    key: 'isEveryday',
    align: 'center',
    width: 120,
  },
  {
    title: '是否会员',
    dataIndex: 'isMember',
    key: 'isMember',
    align: 'center',
    width: 120,
  },
  {
    title: '专题',
    dataIndex: 'subject',
    key: 'subject',
    align: 'center',
    width: 140,
  },
  {
    title: '标签',
    dataIndex: 'labels',
    key: 'labels',
    align: 'center',
    width: 120,
  },
  {
    title: '浏览数量',
    dataIndex: 'viewCount',
    key: 'viewCount',
    align: 'center',
    width: 120,
    sorter: (a: any, b: any) => a.viewCount - b.viewCount,
  },
  {
    title: '排序',
    dataIndex: 'sorted',
    key: 'sorted',
    align: 'center',
    width: 80,
  },
  {
    title: '状态',
    dataIndex: 'status',
    key: 'status',
    align: 'center',
    width: 120,
  },
  {
    title: '创建人',
    dataIndex: 'createBy',
    key: 'createBy',
    align: 'center',
    width: 120,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    key: 'createTime',
    align: 'center',
    width: 200,
  },
  {
    title: '修改时间',
    dataIndex: 'updateTime',
    key: 'updateTime',
    align: 'center',
    width: 200,
  },
  {
    title: '操作',
    dataIndex: 'operation',
    key: 'operation',
    align: 'center',
    fixed: 'right', // 固定在右侧
    width: 200
  }
]
// 搜索
const handleQuery = () => {
  if (createTimeDateRange.value && createTimeDateRange.value.length > 0) {
    params.value = addDateRange(params.value, createTimeDateRange.value, 'CreateTime')
  } else {
    params.value = clearDateRange(params.value, 'CreateTime')
  }
  getTopicTopicList()
}
// 重置
const handleReset = () => {
  params.value = {
    pageNum: 1,
    pageSize: 5,
    topicName: '',
    createBy: '',
    params: null,
  }
  createTimeDateRange.value = []
  total.value = 0
  getTopicTopicList()
}

// 选中数组
const onSelectedRowKeys = ref<number[]>([])

// 选中菜单
const onSelectChange = (selectedRowKeys: any) => {
  onSelectedRowKeys.value = selectedRowKeys
}


// 新增
const handleAdd = () => {
  drawer.value = true
  drawerTitle.value = '新增题目'
}
// 修改
const handleEdit = (record: any) => {
  drawer.value = true
  drawerTitle.value = '修改题目'
  formData.value = {
    ...record
  }
  // 根据subjectName获取subjectId
  formData.value.subjectId = subjectNameAndId.value.find((item: any) => item.label === record.subject).value
  // 根据labelName获取labelId集合
  formData.value.labelIds = labelNameAndId.value.filter((item: any) => record.labels.includes(item.label)).map((item: any) => item.value)


}
// 删除
const handleDelete = (record: any) => {
  Modal.confirm({
    title: '是否确认删除该题目?',
    icon: createVNode(ExclamationCircleOutlined),
    content: createVNode('div', { style: 'color:red;' }, '删除题目会导致相关题目丢失，请慎重考虑!'),
    async onOk() {
      if (record && record.id) {
        onSelectedRowKeys.value.push(record.id)
      }
      await apiDeleteTopic(onSelectedRowKeys.value)
      getTopicTopicList()
      clearFormData()
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
const handleExport = async () => {
  tableLoading.value = true
  let response: any = null
  // 判断是否有数据
  if (onSelectedRowKeys.value.length > 0) {
    // 导出excel数据
    response = await apiExportTopic(params.value, onSelectedRowKeys.value)
  } else {
    // 导出excel数据
    response = await apiExportTopic(params.value, [0])
  }
  FileSaver.saveAs(response, `易题系统题目数据_${new Date().getTime()}.xlsx`) // 下载文件
  message.success('导出成功')
  onSelectedRowKeys.value = []
  tableLoading.value = false
}

// 分页变化处理
const handleTableChange = (pagination: any) => {
  params.value.pageNum = pagination.current;
  params.value.pageSize = pagination.pageSize;
  getTopicTopicList();
}


// 导入参数
const upload = reactive({
  // 是否显示弹出层（用户导入）
  open: false,
  // 是否更新已经存在的用户数据
  updateSupport: 0,
  // 设置上传的请求头部
  headers: {
    authorization: userStore.token
  },
  // 上传文件的loading
  uploadLoading: false,
  // 上传的地址
  url: VITE_SERVE + "/api/topic/topic/import",
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
  let response: any = null
  const res = await apiGetExportTemplate()
  response = res
  FileSaver.saveAs(response, `易题系统题目数据模板_${new Date().getTime()}.xlsx`)
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
    upload.result = info.file.response.data
    if (info.file.response.code === 200) {
      message.success('导入成功');
      getTopicTopicList()
    } else {
      message.error("导入失败");
    }
  }
  // 上传失败
  if (info.file.status === 'error') {
    upload.uploadLoading = false;
    upload.oepnResult = true
    handleCancel()
    upload.result = info.file.response.data
    message.error(info.file.response.data);
  }
}


// 标题
const drawerTitle = ref('新增')
// 抽屉
const drawer = ref(false)
// 抽屉关闭
const onClose = () => {
  clearFormData()
  drawer.value = false
}
// 表单实例
const formRef = ref<any>(null)
// 表单数据
const formData = ref({
  topicName: '',
  answer: '',
  sorted: 0,
  isEveryday: 0,
  isMember: 0,
  subjectId: null,
  labelIds: [],
  id: null,
})
// 表单规则
const rules = ref({
  topicName: [
    {
      required: true,
      message: '题目名称不能为空',
      trigger: 'blur',
    },

  ],
  answer: [
    {
      required: true,
      message: '题目答案不能为空',
      trigger: 'blur',
    },
  ],
  subjectId: [
    {
      required: true,
      message: '题目关联专题不能为空',
      trigger: 'blur',
    },
  ],
  labelIds: [
    {
      required: true,
      message: '题目关联标签不能为空',
      trigger: 'blur',
    },
  ]
})
// 清空
const clearFormData = () => {
  formData.value = {
    topicName: '',
    answer: '',
    sorted: 0,
    isEveryday: 0,
    isMember: 0,
    subjectId: null,
    labelIds: [],
    id: null,
  }
  if (formRef.value) {
    formRef.value.resetFields()
  }
  createTimeDateRange.value = []
}

// 保存以及修改
const onSave = () => {
  formRef.value.validate().then(async () => {
    let mes = ''
    // 判断是新增还是修改
    if (formData.value.id) {
      mes = '修改题目成功'
      await apiUpdateTopic(formData.value)
    } else {
      mes = '新增题目成功'
      await apiAddTopic(formData.value)
    }
    drawer.value = false
    getTopicTopicList()
    clearFormData()

    message.success(mes)
  })
}

// 状态映射对象
const statusMap: any = {
  0: '正常',
  1: '停用',
  2: '待审核',
  3: '审核失败',
}

// 获取专题名称和id
const subjectNameAndId = ref<any>([])
const getSubjectNameAndId = async () => {
  const res = await apiGetSubjectName()
  if (res.data) {
    res.data.map((item: any) => {
      subjectNameAndId.value.push({
        label: item.subjectName,
        value: item.id
      })
    })
  }
}
// 获取标签名称和id
const labelNameAndId = ref<any>([])
const getLabelNameAndId = async () => {
  const res = await apiGetLabelName()
  if (res.data) {
    res.data.map((item: any) => {
      labelNameAndId.value.push({
        label: item.labelName,
        value: item.id
      })
    })
  }
}



onMounted(() => {
  getTopicTopicList()
  getSubjectNameAndId()
  getLabelNameAndId()
})
</script>
<template>
  <div class="user-body">
    <a-space class="space-box">
      <div class="query-box">
        <a-space :size="23">
          <!-- 查询条件 -->
          <a-form-item label="题目名称">
            <a-input class="input" placeholder="请输入题目名称" v-model:value="params.topicName"></a-input>
          </a-form-item>
          <a-form-item label="创建人">
            <a-input class="input" placeholder="请输入创建人" v-model:value="params.createBy"></a-input>
          </a-form-item>
          <a-form-item label="创建时间">
            <a-range-picker class="range-picker" v-model:value="createTimeDateRange" />
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
    }" :scroll="{ x: 1300, y: 1000 }" @change="handleTableChange" :loading="tableLoading" :dataSource="tableData"
      :columns="columns" rowKey="id"
      :row-selection="{ selectedRowKeys: onSelectedRowKeys, onChange: onSelectChange, fixed: true }">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'operation'">
          <a-button type="link" size="small" :icon="h(EditOutlined)" @click="handleEdit(record)">修改</a-button>
          <a-button type="link" size="small" :icon="h(DeleteOutlined)" @click="handleDelete(record)">删除</a-button>
        </template>
        <template v-if="column.key === 'status'">
          <span>{{ statusMap[record.status] }}</span>
        </template>
        <template v-if="column.key === 'isEveryday'">
          <span>{{ record.isEveryday === 1 ? '是' : '否' }}</span>
        </template>
        <template v-if="column.key === 'isMember'">
          <span>{{ record.isMember === 1 ? '是' : '否' }}</span>
        </template>
        <template v-if="column.key === 'subject'">
          <a-button type="link">{{ record.subject }}</a-button>
        </template>
        <template v-if="column.key === 'labels'">
          <a-tag color="processing" v-for="(item, index) in record.labels" :key="index">{{ item }}</a-tag>
        </template>
        <template v-if="column.key === 'answer'">
          <a-tooltip>
            <template #title>{{ record.answer }}</template>
            <!-- 超出部分显示为 tooltip截取20个字符 -->
            {{ record.answer.slice(0, 20) }}
          </a-tooltip>
        </template>
        <template v-if="column.key === 'aiAnswer'">
          <a-tooltip>
            <template #title>{{ record?.aiAnswer }}</template>
            <!-- 超出部分显示为 tooltip截取20个字符 -->
            {{ record.aiAnswer != null ? record?.aiAnswer.slice(0, 20) : '' }}
          </a-tooltip>
        </template>
      </template>
    </a-table>

    <!-- 新增和修改 -->
    <a-drawer :title="drawerTitle" placement="right" v-model:open="drawer" @close="onClose">
      <a-form ref="formRef" :model="formData" :rules="rules" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
        <a-form-item label="题目名称" name="topicName">
          <a-input v-model:value="formData.topicName" placeholder="请输入题目名称" />
        </a-form-item>
        <a-form-item label="题目答案" name="answer">
          <a-textarea :auto-size="{ minRows: 3, maxRows: 8 }" v-model:value="formData.answer" placeholder="请输入题目答案" />
        </a-form-item>
        <a-form-item label="题目专题" name="subjectId">
          <a-select placeholder="请选择专题" v-model:value="formData.subjectId" show-search :options="subjectNameAndId">
          </a-select>
        </a-form-item>
        <a-form-item label="题目标签" name="labelIds">
          <a-select v-model:value="formData.labelIds" mode="tags" style="width: 100%" placeholder="请选择标签"
            :options="labelNameAndId"></a-select>
        </a-form-item>
        <a-form-item label="题目排序" name="sorted">
          <a-input-number style="width: 100%;" v-model:value="formData.sorted" :min="0" placeholder="题目排序" />
        </a-form-item>
        <a-form-item v-if="userStore.userInfo.identity === 2" label="是否每日" name="status">
          <a-switch v-model:checked="formData.isEveryday" :checkedValue="1" :unCheckedValue="0" checked-children="是"
            un-checked-children="否" />
        </a-form-item>
        <a-form-item v-if="userStore.userInfo.identity === 2" label="是否会员" name="status">
          <a-switch v-model:checked="formData.isMember" :checkedValue="1" :unCheckedValue="0" checked-children="是"
            un-checked-children="否" />
        </a-form-item>

      </a-form>
      <!-- 添加底部按钮 -->
      <template #footer>
        <a-space class="space-footer-box">
          <a-button @click="onClose">取消</a-button>
          <a-button type="primary" @click="onSave">保存</a-button>
        </a-space>
      </template>
    </a-drawer>

    <!-- 导入excel -->
    <a-modal @cancel="handleCancel" :footer="null"
      bodyStyle="display: flex; flex-direction: column; align-items: center; text-align: center;"
      v-model:open="upload.open" title="导入题目数据">
      <a-upload-dragger maxCount="1" style="width: 100%;" v-model:fileList="upload.uploadFileList" name="file"
        :multiple="true" :headers="upload.headers" :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :before-upload="handleBeforeUpload" @change="handleUploadChange">
        <p class="ant-upload-drag-icon">
          <loading-outlined v-if="upload.uploadLoading"></loading-outlined>
          <inbox-outlined v-else></inbox-outlined>
        </p>
        <p class="ant-upload-text">将文件拖到此处或点击上传</p>
      </a-upload-dragger>
      <a-checkbox class="checkbox" v-model:checked="upload.updateSupport">是否更新已经存在的题目数据</a-checkbox>
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

.space-footer-box {
  display: flex;
  justify-content: flex-end;
}

.input {
  width: 130px;
}

.avatar {
  width: 100px;
  height: 100px;
  border-radius: 5%;
}
</style>
