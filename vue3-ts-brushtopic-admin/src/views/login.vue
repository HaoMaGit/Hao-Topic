<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { apiGetCode } from '@/api/system'
import { useUserStore } from '@/stores/modules/user'
import type { LoginType } from '@/api/auth/type';

// 初始化仓库
const userStore = useUserStore()

// 图片验证码
const codeImage = ref('')
// 获取图片验证码
const getCaptchaImage = async () => {
  // 不需要ts校验
  apiGetCode().then((res) => {
    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
    // @ts-expect-error
    codeImage.value = URL.createObjectURL(new Blob([res], { type: 'image/png' }))
  })
}

// 表单实例
const adminRef = ref<any>(null)
// 表单数据
const formData = ref<LoginType>({
  username: 'admin',
  password: '123456',
  // 验证码
  code: '',
  // 是否记住密码
  remember: false,
})
// 表单校验规则
const formRule = ref({
  username: [
    { required: true, message: '请输入您的账号', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入您的密码', trigger: 'blur' }
  ],
  code: [
    { required: true, message: '请输入验证码', trigger: 'blur' }
  ]
})

const loginLoading = ref(false)
// 登录
const login = () => {
  if (!adminRef.value) return // 确保表单实例存在
  if (adminRef.value) {
    // 表单校验
    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
    // @ts-expect-error
    adminRef.value.validate().then(async (valid) => {
      if (valid) {
        loginLoading.value = true
        // 发起请求
        await userStore.login(formData.value)
        loginLoading.value = false
        if (formData.value.remember) {
          // 记住密码
          window.localStorage.setItem("userPassword", JSON.stringify({
            username: formData.value.username,
            password: formData.value.password
          }))
        }
        clearStyle()
      }
    })
  }
}

// 清除默认样式
const clearStyle = () => {
  formData.value = {
    username: 'admin',
    password: '123456',
    code: '',
    remember: false,
  }
  adminRef.value.resetFields()
}

// 获取记住我
const getRemember = () => {
  const item = window.localStorage.getItem("userPassword")
  if (item) {
    const userPassword = JSON.parse(item)
    formData.value.username = userPassword.username
    formData.value.password = userPassword.password
    formData.value.remember = true
  }
}

onMounted(() => {
  getCaptchaImage()
  getRemember()
})
</script>
<template>
  <div class="login-body">
    <!-- 登录页面容器 -->
    <div class="login-box">
      <!-- 左侧背景图 -->
      <div class="login-left"></div>
      <!-- 右侧登陆 -->
      <div class="login-right">
        <!-- 标题 -->
        <div class="title">
          <h1>易题后台管理系统</h1>
        </div>
        <!-- 会员表单 -->
        <div class="form-member">
          <a-form :model="formData" ref="adminRef" :rules="formRule" layout="vertical">
            <a-form-item label="账户" name="username">
              <a-input v-model:value="formData.username" placeholder="请输入手机号或者邮箱" size="large">
                <template #prefix>
                  <UserOutlined />
                </template>
              </a-input>
            </a-form-item>
            <a-form-item label="密码" name="password">
              <a-input-password v-model:value="formData.password" placeholder="请输入密码" size="large">
                <template #prefix>
                  <LockOutlined />
                </template>
              </a-input-password>
            </a-form-item>
            <a-form-item label="验证码" name="code">
              <div class="code-box">
                <a-input maxlength="4" v-model:value="formData.code" placeholder="验证码" size="large" style="flex: 1;">
                  <template #prefix>
                    <CheckCircleOutlined />
                  </template>
                </a-input>
                <!-- 验证码图片 -->
                <img @click="getCaptchaImage" :src="codeImage" alt="验证码" class="login-code" />
              </div>
            </a-form-item>
            <a-form-item class="check-item">
              <a-checkbox v-model:checked="formData.remember" class="check">记住密码</a-checkbox>
            </a-form-item>
            <a-form-item>
              <a-button v-if="!loginLoading" type="primary" block size="large" @click="login()">登录</a-button>
              <a-spin v-else tip="正在登录并加载数据中...."></a-spin>
            </a-form-item>
          </a-form>
        </div>
      </div>
      <!-- 底部版权 -->
      <div class="login-bottom">
        <p>©易题后台管理系统 2025 如有侵权请联系QQ：3655161743</p>
        <p>赣ICP备2024033867号</p>
      </div>
    </div>
  </div>
</template>
<style lang="scss" scoped>
.login-body {
  width: 100%;
  height: 100vh;
  background-color: $base-background;

  .login-box {
    width: 100%;
    height: 100%;
    padding: 100px;
    display: flex;

    .login-left {
      width: 54%;
      height: 500px;
      background-image: url('../assets/images/bg.png');
      background-size: contain;
      background-repeat: no-repeat;
    }

    .login-right {
      margin: 35px 0 0 90px;
      background-color: $base-login-background; // 保留一个背景色定义
      text-align: center;
      padding: 30px;
      width: 410px;
      height: 448px;
      box-shadow: 0px 0px 3px 0px rgba(0, 0, 0, .2);
      border-radius: 12px;

      ::v-deep(.check-item) {
        display: flex;
        justify-content: flex-start;
        margin: 8px;
        padding: 0;
      }

      .title {

        h1 {
          font-size: 20px;
          color: $base-personal-color;

        }
      }

      .code-box {
        display: flex;
        align-items: center;
      }

      .login-image {
        border: 1px solid #409eff;
        width: 26%;
        margin-left: 18px;
        height: 38px;
        float: right;
        overflow: hidden;

        img {
          cursor: pointer;
          vertical-align: middle;
        }

        .login-code {
          margin-left: 10px;
          cursor: pointer;
          width: 100%;
          height: 100%;
          object-fit: cover;
        }
      }
    }

    .login-bottom {
      position: absolute;
      bottom: 2%;
      left: 50%;
      transform: translateX(-50%);
      width: 600px;
      height: 60px;
      text-align: center;
      padding: 20px;
      width: fit-content;
      font-size: 14px;
      color: #b3b3b3;

      p {
        line-height: 1em;
      }
    }

    .check {
      color: #b0adb5;
    }


  }
}

// 响应式设计
@media (max-width: 792px) {
  .login-box {
    flex-direction: column;
    height: auto;

    .login-left {
      display: none;
    }

    .login-right {
      width: 500px;
      padding: 20px;
    }
  }
}
</style>
