import path from 'path'
import { defineConfig, loadEnv } from 'vite';
import vue from '@vitejs/plugin-vue';
// import vueDevTools from 'vite-plugin-vue-devtools';

// https://vite.dev/config/
export default defineConfig(({ mode }) => {
  // 获取各种环境下的对应变量
  const env = loadEnv(mode, process.cwd());
  return {
    plugins: [
      vue(),
      // vueDevTools(),
    ],
    resolve: {
      alias: {
        '@': path.resolve('./src'), // 相对路径别名配置，使用 @ 代替 src
      },
    },
    // scss全局变量配置
    css: {
      preprocessorOptions: {
        scss: {
          javascriptEnabled: true,
          additionalData: '@import "@/styles/variable.scss";',
        },
      },
    },
    // 代理跨域配置
    server: {
      port: parseInt(env.VITE_PORT) || 3000, // 将字符串转为数字，并设置默认值
      proxy: {
        [env.VITE_APP_BASE_API]: {
          target: env.VITE_SERVE || 'http://localhost', // 设置默认值
          changeOrigin: true,
          pathRewrite: {
            [env.VITE_APP_BASE_API]: '',
          },
        },
      },
    },
  };
});

