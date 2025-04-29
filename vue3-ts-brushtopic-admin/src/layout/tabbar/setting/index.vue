<script setup lang="ts">
import { useUserStore } from '@/stores/modules/user'
const userStore = useUserStore()
import { useSettingStore } from '@/stores/modules/setting';
const settingStore = useSettingStore()
import Hao from '@/assets/images/H.png'
import { ref, h } from 'vue'
import { message } from 'ant-design-vue';
import { BellOutlined, DownOutlined, SettingOutlined, MenuFoldOutlined, MenuUnfoldOutlined, FullscreenOutlined, ReloadOutlined } from '@ant-design/icons-vue';
// 引入颜色选择器
import { ColorPicker } from 'vue3-colorpicker'
import 'vue3-colorpicker/style.css'
import { useRouter } from 'vue-router';
const $router = useRouter()
// 刷新
const updateRefsh = () => {
  // 刷新页面
  window.location.reload()
  // 重新获取路由信息
  userStore.getUserInfo()
}


// 全屏
const fullScreen = () => {
  // DOM对象的一个属性:可以用来判断当前是不是全屏模式[全屏:true,不是全屏:false]
  const full = document.fullscreenElement
  //切换为全屏模式
  if (!full) {
    //文档根节点的方法requestFullscreen,实现全屏模式
    document.documentElement.requestFullscreen()
  } else {
    //变为不是全屏模式->退出全屏模式
    document.exitFullscreen()
  }
}


// 查看通知
const viewBell = () => {
  // TODO 查看通知
}

// 清空通知
const clearBell = () => {
  // Modal.confirm(
  //   {
  //     title: '确定要清除所有通知吗？清空后不可恢复！',
  //     icon: createVNode(ExclamationCircleOutlined),

  //   }
  //   '',
  //   '清空通知',
  //   {
  //     confirmButtonText: '确认',
  //     cancelButtonText: '取消',
  //     type: 'warning',
  //   }
  // )
  //   .then(() => {
  //     ElMessage({
  //       type: 'success',
  //       message: '清空通知成功',
  //     })
  //   })
}

// 查看个性化设置抽底
const drawer = ref(false)
// 查看个性化设置
const viewSetting = () => {
  drawer.value = true
}

// 默认的配置
const defaultTheme = ref({
  themeColor: '#1677ff',
  isDark: false,
  fold: false
})

// // 切换为暗黑模式
// const changeDark = () => {
//   // 获取到根节点
//   const html = document.documentElement
//   if (settingStore.isDark) {
//     html.className = 'dark'
//   } else {
//     html.className = ''
//   }
// }

// 点击了保存配置在存储到本地仓库中
const saveTheme = () => {
  // 存储默认对象的配置
  localStorage.setItem("settingTheme", JSON.stringify({
    fold: settingStore.fold,
    isDark: settingStore.isDark,
    themeColor: settingStore.themeColor
  }))
  message.success("保存配置成功")
}

// 重置配置
const resetTheme = () => {
  // 重置为原来的配置将原来的本地删除即可
  localStorage.removeItem("settingTheme")
  // 将配置重置为默认配置
  settingStore.themeColor = defaultTheme.value.themeColor
  settingStore.isDark = defaultTheme.value.isDark
  settingStore.fold = defaultTheme.value.fold
  message.info("重置配置成功")
}

// 图片
const handleErrorImg = (event: any) => {
  event.target.src = Hao
};
</script>
<template>
  <!-- 布局设置抽底 -->
  <a-drawer v-model:open="drawer" title="主题设置">
    <!-- 主题颜色 暗黑模式 -->
    <a-form-item label="主题颜色">
      <ColorPicker v-model:pure-color="settingStore.themeColor" />
    </a-form-item>
    <a-form-item label="暗黑模式">
      <!-- 自定义切换暗黑模式的图标 -->
      <!-- <el-switch :active-action-icon="Moon" :inactive-action-icon="Sunny" @change="changeDark"
        v-model="settingStore.isDark" active-color="#13ce66" inactive-color="#ff4949"></el-switch> -->
      <a-switch v-model:checked="settingStore.isDark">
        <!-- TODO 后续图标更换 -->
        <!-- <template #checkedChildren>
          <MoonOutlined />
        </template>
<template #unCheckedChildren>
          <SunOutlined />
        </template> -->
      </a-switch>
    </a-form-item>
    <a-form-item label="折叠菜单">
      <a-switch v-model:checked="settingStore.fold">
        <template #checkedChildren>
          <MenuUnfoldOutlined />
        </template>
        <template #unCheckedChildren>
          <MenuFoldOutlined />
        </template>
      </a-switch>
    </a-form-item>
    <a-form-item>
      <a-space :size="10" class="space">
        <a-button type="primary" @click="saveTheme" plain>保存配置</a-button>
        <a-button @click="resetTheme">重置配置</a-button>
      </a-space>
    </a-form-item>
  </a-drawer>
  <a-space>
    <!-- 按钮位置 -->
    <a-button :icon="h(ReloadOutlined)" shape="circle" @click="updateRefsh"></a-button>
    <a-button :icon="h(FullscreenOutlined)" shape="circle" @click="fullScreen"></a-button>
    <!-- 通知：审核题目 会员购买 -->
    <a-popover placement="bottom">
      <!-- 自定义内容 -->
      <template #content>
        <div class="bell-box">
          <!-- 顶部标题和清除通知 -->
          <div class="bell-top">
            <span class="tz">通知</span>
            <span class="clear" @click="clearBell">清空</span>
          </div>
          <!-- 内容区域是通知信息 -->
          <div class="bell-content">
            <ul class="infinite-list" style="overflow: auto">
              <li v-for="i in 10" :key="i" class="infinite-list-item">{{ i }}</li>
            </ul>
          </div>
          <!-- 底部关闭通知 -->
          <div class="bell-bottom">
            <a-link type="primary">已读</a-link>
          </div>
        </div>
      </template>
      <a-button :icon="h(BellOutlined)" shape="circle" @click="viewBell"></a-button>
    </a-popover>
    <!-- 设置 -->
    <a-button @click="viewSetting" :icon="h(SettingOutlined)" shape="circle"></a-button>
    <!-- 头像 -->
    <template v-if="userStore.userInfo?.avatar">
      <img class="user-avatar" @error="handleErrorImg" :src="userStore.userInfo.avatar" alt="" />
    </template>
    <template v-else>
      <a-avatar class="user-avatar" :style="{ backgroundColor: '#1677ff' }">
        {{ userStore.userInfo?.account?.charAt(0)?.toUpperCase() }}
      </a-avatar>
    </template>
    <!-- 下拉菜单：个人中心 退出登录 -->
    <a-dropdown class="dropdown">
      <span>
        {{ userStore.userInfo?.account }}
        <DownOutlined />
      </span>
      <template #overlay>
        <a-menu>
          <a-menu-item @click="$router.push('/profile')">个人中心</a-menu-item>
          <a-menu-item
            @click="userStore.clearUserInfo(), $router.push('/login'), message.success('退出登录')">退出登录</a-menu-item>
        </a-menu>
      </template>
    </a-dropdown>
  </a-space>
</template>
<style lang="scss" scoped>
.space {
  padding-top: 5px;
}

.dropdown {
  margin-right: 22px;
}

.bell-box {
  width: 250px;

  .bell-top {
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-weight: 520;

    .tz:hover {
      cursor: pointer;
    }

    .clear:hover {
      cursor: pointer;
      color: red;
    }
  }

  .bell-content {
    margin-top: 10px;

    .infinite-list {
      height: 300px;
      padding: 0;
      margin: 0;
      list-style: none;
    }

    .infinite-list .infinite-list-item {
      display: flex;
      align-items: center;
      justify-content: center;
      height: 50px;
      background: var(--el-color-primary-light-9);
      color: var(--el-color-primary);
      margin-bottom: 4px;
    }

    .infinite-list .infinite-list-item+.list-item {
      margin-top: 10px;
    }
  }

  .bell-bottom {
    height: 28px;
    border-top: 1px solid #eceef0;
    display: flex;
    align-items: end;
    justify-content: center;
  }
}

.user-avatar {
  width: 28px;
  height: 28px;
  margin: 0px 10px;
  border-radius: 50%;
}
</style>