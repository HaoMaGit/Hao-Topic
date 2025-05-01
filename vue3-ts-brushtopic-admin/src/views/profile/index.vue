<template>
  <div class="profile-container">
    <a-row :gutter="[24, 24]">
      <!-- 左侧个人信息卡片 -->
      <a-col :xs="24" :sm="24" :md="8" :lg="8" :xl="6">
        <a-card :bordered="false" class="user-card">
          <div class="avatar-container">
            <a-avatar :size="120" :src="userInfo.avatar">
              <template #icon>
                <UserOutlined />
              </template>
            </a-avatar>
            <div class="avatar-upload">
              <a-upload ::maxCount="1" v-model:file-list="fileList" name="avatar" style="opacity: 0;"
                list-type="picture-card" :show-upload-list="false" :headers="headers" :action="uploadUrl"
                :before-upload="beforeUpload" @change="handleChange">
              </a-upload>
            </div>
          </div>
          <div class="user-info">
            <h1>{{ userInfo.account }}</h1>
            <div class="nickname" v-if="userInfo.nickname">昵称：{{ userInfo.nickname }}</div>
            <p class="user-email" v-if="userInfo.email">{{ userInfo.email }}</p>
            <!-- 账号注册时间 -->
            <p class="user-since" v-if="userInfo.createTime">账号注册时间: {{ userInfo.createTime }}</p>
            <p class="user-since" v-if="userInfo.memberTime">会员注册时间: {{ userInfo.memberTime }}</p>
            <a-tag :color="userInfo.status === 0 ? 'success' : 'error'">
              {{ userInfo.status === 0 ? '正常' : '停用' }}
            </a-tag>
          </div>
        </a-card>
      </a-col>

      <!-- 右侧编辑表单 -->
      <a-col :xs="24" :sm="24" :md="16" :lg="16" :xl="18">
        <a-card :bordered="false" title="个人信息">
          <a-tabs default-active-key="1">
            <a-tab-pane key="1" tab="基本信息">
              <a-form :model="formState" :rules="rules" ref="formRef" layout="vertical">
                <a-form-item label="昵称" name="nickname">
                  <a-input v-model:value="formState.nickname" placeholder="请谨慎修改账户昵称修改昵称后会影响展示效果" />
                </a-form-item>
                <a-form-item label="邮箱" name="email">
                  <a-input v-model:value="formState.email" placeholder="请输入邮箱" />
                </a-form-item>
                <a-form-item label="密码" name="password">
                  <a-input-password v-model:value="formState.password" placeholder="请输入密码" />
                </a-form-item>
                <a-form-item>
                  <a-space :size="10">
                    <a-button type="primary" @click="submitForm">保存修改</a-button>
                    <a-button @click="() => $router.push('/')">返回</a-button>
                  </a-space>
                </a-form-item>
              </a-form>
            </a-tab-pane>
            <a-tab-pane key="2" tab="修改密码">
              <a-form :model="passwordForm" :rules="passwordRules" ref="passwordFormRef" layout="vertical">
                <a-form-item label="当前密码" name="oldPassword">
                  <a-input-password v-model:value="passwordForm.oldPassword" placeholder="请输入当前密码" />
                </a-form-item>
                <a-form-item label="新密码" name="newPassword">
                  <a-input-password v-model:value="passwordForm.newPassword" placeholder="请输入新密码" />
                </a-form-item>
                <a-form-item label="确认新密码" name="confirmPassword">
                  <a-input-password v-model:value="passwordForm.confirmPassword" placeholder="请确认新密码" />
                </a-form-item>
                <a-form-item>
                  <a-space :size="10">
                    <a-button type="primary" @click="changePassword">修改密码</a-button>
                    <a-button @click="() => $router.push('/')">返回</a-button>
                  </a-space>
                </a-form-item>
              </a-form>
            </a-tab-pane>
          </a-tabs>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { message } from 'ant-design-vue';
import type { FormInstance } from 'ant-design-vue';
import { apiGetUserInfoDetail, apiSaveUserAvatar, apiUpdateUserInfo, apiUpdatePassword } from '@/api/auth/index'
import { useUserStore } from '@/stores/modules/user'
const userStore = useUserStore()
import { useRouter } from 'vue-router';
import type { UploadChangeParam, UploadProps } from 'ant-design-vue';


// 获取上传路径
const { VITE_SERVE } = import.meta.env
// 头像上传路径
const uploadUrl = VITE_SERVE + '/api/system/user/avatar'
// 请求头
const headers = { authorization: userStore.token };


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
const handleChange = async (info: UploadChangeParam) => {
  // 判断是否上传中
  if (info.file.status === 'uploading') {
    loading.value = true;
    return;
  }
  // 上传完成
  if (info.file.status === 'done') {
    userInfo.value.avatar = info.file.response.data
    // 更新缓存
    userStore.userInfo.avatar = info.file.response.data
    userInfo.value.avatar = info.file.response.data
    // 写入数据库
    await apiSaveUserAvatar({ avatar: info.file.response.data, id: userStore.userInfo.id })
    loading.value = false;
    message.success('上传成功');
  }
  // 上传失败
  if (info.file.status === 'error') {
    loading.value = false;
    message.error('上传失败');
  }
};

const $router = useRouter()
const getUserInfo = async () => {
  apiGetUserInfoDetail(userStore.userInfo.id).then((res) => {
    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
    // @ts-expect-error
    userInfo.value = res
    formState.nickname = userInfo.value.nickname
    formState.email = userInfo.value.email
  })
};


// 用户信息
const userInfo = ref({
  account: '',
  avatar: '',
  email: '',
  memberTime: '',
  nickname: '',
  status: 0,
  password: null,
  createTime: ''
});


// 文件列表
const fileList = ref([]);
// 图片loading
const loading = ref<boolean>(false);

// 表单数据
const formState = reactive({
  nickname: '',
  email: '',
  password: null
});

// 密码表单数据
const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
});

// 表单引用
const formRef = ref<FormInstance>();
const passwordFormRef = ref<FormInstance>();

// 表单验证规则
const rules = {
  nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' },
  { min: 2, message: '昵称长度不能少于2个字符', trigger: 'blur' },
  { max: 10, message: '昵称长度不能超过10个字符', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入有效的邮箱地址', trigger: 'blur' }
  ],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
};

// 密码表单验证规则
const passwordRules = {
  oldPassword: [{ required: true, message: '请输入当前密码', trigger: 'blur' }],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (rule: any, value: string) => {
        if (value !== passwordForm.newPassword) {
          return Promise.reject('两次输入的密码不一致');
        }
        return Promise.resolve();
      },
      trigger: 'blur'
    }
  ]
};
// 提交表单
const submitForm = () => {
  formRef.value?.validate().then(async () => {
    // 调用API更新用户信息
    await apiUpdateUserInfo({
      id: userStore.userInfo.id,
      nickname: formState.nickname,
      email: formState.email,
      password: formState.password
    })
    message.success('个人信息更新成功');
    // 退出登录
    userStore.logout()
  }).catch(error => {
    console.log('验证失败:', error);
  });
};

// 修改密码
const changePassword = () => {
  passwordFormRef.value?.validate().then(async () => {
    // 这里应该是调用API更新密码
    await apiUpdatePassword({
      id: userStore.userInfo.id,
      oldPassword: passwordForm.oldPassword,
      newPassword: passwordForm.newPassword
    })
    message.success('密码修改成功');
    // 退出登录
    userStore.logout()
  }).catch(error => {
    console.log('验证失败:', error);
  });
};

// 页面加载时获取用户信息
onMounted(() => {
  getUserInfo();
});
</script>

<style scoped>
.profile-container {
  padding: 24px;
}

.user-card {
  text-align: center;
  height: 100%;
}

.avatar-container {
  position: relative;
  display: inline-block;
  margin-bottom: 20px;
}

.avatar-upload {
  position: absolute;
  right: 0;
  bottom: 0;
}

.user-info {
  margin-top: 10px;
}

.user-email {
  color: rgba(0, 0, 0, 0.45);
  margin-bottom: 8px;
}

.user-since {
  font-size: 12px;
  color: rgba(0, 0, 0, 0.45);
  margin-bottom: 8px;
}

.nickname {
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
}
</style>