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
import { apiGetSubjectList, apiAddSubject, apiGetExportTemplate, apiUpdateSubject, apiDeleteSubject, apiExportSubject } from '@/api/topic/subject'
import { apiGetCategoryList } from '@/api/topic/category'
import type { SubjectCatgoryQueryType } from '@/api/topic/subject/type';
import { addDateRange, clearDateRange } from '@/utils/date';
import { message, Modal } from 'ant-design-vue';
import FileSaver from 'file-saver'
import type { UploadChangeParam } from 'ant-design-vue';
import { useUserStore } from '@/stores/modules/user';
const userStore = useUserStore()
// 获取上传路径
const { VITE_SERVE } = import.meta.env
// 图相上传路径
const uploadUrl = VITE_SERVE + '/api/topic/subject/img'
/**
 * 查询参数
 */
const params = ref<SubjectCatgoryQueryType>({
  pageNum: 1,
  pageSize: 5,
  subjectName: '',
  createBy: '',
  categoryName: null,
  params: {}
})

// 获取专题列表
const getTopicSubjectList = async () => {
  tableLoading.value = true
  const res = await apiGetSubjectList(params.value)
  total.value = res.data.total
  tableData.value = res.data.rows
  tableLoading.value = false
}
const categoryList = ref<Category[]>([])
interface Category {
  categoryName: string
}
// 获取分类列表
const getCategoryList = async () => {
  const res = await apiGetCategoryList(null)
  categoryList.value = res.data.rows.map((item: Category) => {
    return {
      label: item.categoryName,
      value: item.categoryName,
    }
  })
  console.log(categoryList.value);
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
    title: '专题名称',
    dataIndex: 'subjectName',
    key: 'subjectName',
    align: 'center',
    width: 180,
  },
  {
    title: '专题概述',
    dataIndex: 'subjectDesc',
    key: 'subjectDesc',
    align: 'center',
    width: 180,
  },
  {
    title: '分类',
    dataIndex: 'categoryName',
    key: 'categoryName',
    align: 'center',
    width: 120,
  },
  {
    title: '图像',
    dataIndex: 'imageUrl',
    key: 'imageUrl',
    align: 'center',
    width: 80,
  },
  {
    title: '收录数量',
    dataIndex: 'topicCount',
    key: 'topicCount',
    align: 'center',
    width: 120,
  },
  {
    title: '浏览数量',
    dataIndex: 'viewCount',
    key: 'viewCount',
    align: 'center',
    width: 120,
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
    width: 150,
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
  getTopicSubjectList()
}
// 重置
const handleReset = () => {
  params.value = {
    pageNum: 1,
    pageSize: 5,
    subjectName: '',
    createBy: '',
    params: null,
    categoryName: null,
  }
  createTimeDateRange.value = []
  total.value = 0
  getTopicSubjectList()
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
  drawerTitle.value = '新增题目专题'
}
// 修改
const handleEdit = (record: any) => {
  drawer.value = true
  drawerTitle.value = '修改题目专题'
  formData.value = {
    ...record
  }
}
// 删除
const handleDelete = (record: any) => {
  Modal.confirm({
    title: '是否确认删除该题目专题?',
    icon: createVNode(ExclamationCircleOutlined),
    content: createVNode('div', { style: 'color:red;' }, '删除专题会导致相关题目专题丢失，请慎重考虑!'),
    async onOk() {
      if (record && record.id) {
        onSelectedRowKeys.value.push(record.id)
      }
      await apiDeleteSubject(onSelectedRowKeys.value)
      getTopicSubjectList()
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
    response = await apiExportSubject(params.value, onSelectedRowKeys.value)
  } else {
    // 导出excel数据
    response = await apiExportSubject(params.value, [0])
  }
  FileSaver.saveAs(response, `易题系统题目专题数据_${new Date().getTime()}.xlsx`) // 下载文件
  message.success('导出成功')
  onSelectedRowKeys.value = []
  tableLoading.value = false
}

// 分页变化处理
const handleTableChange = (pagination: any) => {
  params.value.pageNum = pagination.current;
  params.value.pageSize = pagination.pageSize;
  getTopicSubjectList();
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
  url: VITE_SERVE + "/api/topic/subject/import",
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
  FileSaver.saveAs(response, `易题系统题目专题数据模板_${new Date().getTime()}.xlsx`)
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
      getTopicSubjectList()
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
  loading.value = false
}
// 表单实例
const formRef = ref<any>(null)
// 表单数据
const formData = ref({
  subjectName: '',
  id: null,
  subjectDesc: '',
  imageUrl: '',
  categoryName: ''
})
// 表单规则
const rules = ref({
  subjectName: [
    {
      required: true,
      message: '请输入专题名称',
      trigger: 'blur',
    },

  ],
  imageUrl: [
    {
      required: true,
      message: '请输入图片地址',
      trigger: 'blur',
    },
  ],
  subjectDesc: [
    {
      required: true,
      message: '请输入专题描述',
      trigger: 'blur',
    },
  ],
  categoryName: [
    {
      required: true,
      message: '请选择所属分类',
      trigger: 'blur',
    },
  ]
})
// 清空
const clearFormData = () => {
  formData.value = {
    subjectName: '',
    id: null,
    subjectDesc: '',
    imageUrl: '',
    categoryName: ''
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
      mes = '修改题目专题成功'
      await apiUpdateSubject(formData.value)
    } else {
      mes = '新增题目专题成功'
      await apiAddSubject(formData.value)
    }
    drawer.value = false
    getTopicSubjectList()
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

// 文件列表
const fileList = ref([]);
// 图片loading
const loading = ref<boolean>(false);

/**
 * 文件校验
 * @param file 
 */
// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-expect-error
const beforeUpload = (file: UploadProps['fileList'][number]) => {
  const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
  if (!isJpgOrPng) {
    message.error('图片格式错误');
  }
  const isLt2M = file.size / 1024 / 1024 < 2;
  if (!isLt2M) {
    message.error('图片大小不能超过2M');
  }
  return isJpgOrPng && isLt2M;
};

/**
 * 图片上传
 * @param info 
 */
const handleChange = (info: UploadChangeParam) => {
  // 判断是否上传中
  if (info.file.status === 'uploading') {
    loading.value = true;
    return;
  }
  // 上传完成
  if (info.file.status === 'done') {
    formData.value.imageUrl = info.file.response.data;
    loading.value = false;
    message.success('上传成功');
  }
  // 上传失败
  if (info.file.status === 'error') {
    loading.value = false;
    message.error('上传失败');
  }
};



onMounted(() => {
  getTopicSubjectList()
  getCategoryList()
})
</script>
<template>
  <div class="user-body">
    <a-space class="space-box">
      <div class="query-box">
        <a-space :size="23">
          <!-- 查询条件 -->
          <a-form-item label="专题名称">
            <a-input class="input" placeholder="请输入专题名称" v-model:value="params.subjectName"></a-input>
          </a-form-item>
          <a-form-item label="创建人">
            <a-input class="input" placeholder="请输入创建人" v-model:value="params.createBy"></a-input>
          </a-form-item>
          <!-- 分类名称 -->
          <a-form-item label="所属分类">
            <a-select placeholder="请选择分类" class="input" v-model:value="params.categoryName" show-search
              :options="categoryList">
            </a-select>
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
        <template v-if="column.key === 'imageUrl'">
          <a-image :width="48" :src="record.imageUrl != null ? record.imageUrl : 'http://127.0.0.1:9000/topic/H.png'" />
        </template>
        <template v-if="column.key === 'subjectDesc'">
          <a-tooltip>
            <template #title>{{ record.subjectDesc }}</template>
            <!-- 超出部分显示为 tooltip截取20个字符 -->
            {{ record.subjectDesc.slice(0, 20) }}
          </a-tooltip>
        </template>
        <template v-if="column.key === 'subjectName'">
          <a-tooltip>
            <template #title>{{ record.subjectName }}</template>
            <!-- 超出部分显示为 tooltip截取20个字符 -->
            {{ record.subjectName.slice(0, 20) }}
          </a-tooltip>
        </template>
      </template>
    </a-table>

    <!-- 新增和修改 -->
    <a-drawer :title="drawerTitle" placement="right" v-model:open="drawer" @close="onClose">
      <a-form ref="formRef" :model="formData" :rules="rules" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
        <a-form-item label="专题名称" name="subjectName">
          <a-input v-model:value="formData.subjectName" placeholder="请输入专题名称" />
        </a-form-item>
        <a-form-item label="专题描述" name="subjectDesc">
          <a-textarea :auto-size="{ minRows: 2, maxRows: 5 }" v-model:value="formData.subjectDesc"
            placeholder="请输入专题描述" />
        </a-form-item>
        <a-form-item label="图像" name="imageUrl">
          <a-upload maxCount="1" v-model:file-list="fileList" name="avatar" list-type="picture-card"
            class="avatar-uploader" :show-upload-list="false" :headers="upload.headers" :action="uploadUrl"
            :before-upload="beforeUpload" @change="handleChange">
            <img v-if="formData.imageUrl" :src="formData.imageUrl" alt="avatar" class="avatar" />
            <div v-else>
              <loading-outlined v-if="loading"></loading-outlined>
              <plus-outlined v-else></plus-outlined>
            </div>
          </a-upload>
        </a-form-item>
        <a-form-item label="所属分类" name="categoryName">
          <a-select placeholder="请选择分类" v-model:value="formData.categoryName" show-search :options="categoryList">
          </a-select>
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
      v-model:open="upload.open" title="导入题目专题数据">
      <a-upload-dragger maxCount="1" style="width: 100%;" v-model:fileList="upload.uploadFileList" name="file"
        :multiple="true" :headers="upload.headers" :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :before-upload="handleBeforeUpload" @change="handleUploadChange">
        <p class="ant-upload-drag-icon">
          <loading-outlined v-if="upload.uploadLoading"></loading-outlined>
          <inbox-outlined v-else></inbox-outlined>
        </p>
        <p class="ant-upload-text">将文件拖到此处或点击上传</p>
      </a-upload-dragger>
      <a-checkbox class="checkbox" v-model:checked="upload.updateSupport">是否更新已经存在的题目专题数据</a-checkbox>
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
