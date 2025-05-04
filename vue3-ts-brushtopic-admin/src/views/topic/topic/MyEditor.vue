<template>
  <div style="border: 1px solid #ccc">
    <Toolbar v-if="showToolbarFlag" :editor="editorRef" :defaultConfig="toolbarConfig" :mode="mode"
      style="border-bottom: 1px solid #ccc" />
    <Editor v-model="valueHtml" :defaultConfig="editorConfig" :mode="mode" @onCreated="handleCreated"
      @onChange="handleChange" :style="{ height: editorHeight, overflowY: 'hidden' }" :readOnly="readOnlyFlag" />
  </div>
</template>

<script setup lang="ts">
import { watch, onBeforeUnmount, nextTick, ref, shallowRef, onMounted } from 'vue'
// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-expect-error
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import '@wangeditor/editor/dist/css/style.css' // 引入 css


// Props：使用属性，子组件接收父组件传递的内容
const props = defineProps({
  // 内容
  content: { type: String, default: '' },
  // 工具栏是否显示，默认显示
  showToolbarFlag: { type: Boolean, default: true },
  // 编辑器高度，默认500px
  editorHeight: { type: String, default: '200px' },
  // 编辑器是否只读，默认可编辑
  readOnlyFlag: { type: Boolean, default: false }
})

// Emits：使用事件，将子组件内容传递给父组件。父组件使用 update(content: string) 
const emit = defineEmits<{ (e: 'update', content: string): void }>()

const mode = ref('default')

// 编辑器实例，必须用 shallowRef
const editorRef = shallowRef()

// 内容 HTML
const valueHtml = ref('')

const toolbarConfig = {
  toolbarKeys: [
    // 文字相关
    'headerSelect',      // 标题
    'bold',             // 粗体
    'underline',        // 下划线
    'through',          // 删除线
    '|',                // 分割线
    'color',            // 文字颜色
    'bgColor',          // 背景颜色
    '|',
    'bulletedList',     // 无序列表
    'numberedList',     // 有序列表
    '|',
    'code',             // 行内代码
    'codeBlock',        // 代码块
  ]
}

const editorConfig = {
  placeholder: '请输入内容...',
  MENU_CONF: {} as any
}
const handleCreated = (editor: any) => {
  editorRef.value = editor // 记录 editor 实例，重要！

  // 根据父组件传递的readOnlyFlag，设置编辑器为只读
  if (props.readOnlyFlag) {
    editorRef.value.disable();
  } else {
    editorRef.value.enable();
  }
}

const handleChange = () => {
  valueHtml.value = editorRef.value.getHtml()
  emit('update', valueHtml.value)
}

// 监听 props 变化，监听父组件传来的content
watch(() => props.content, (newVal: string) => {
  nextTick(() => {
    if (editorRef.value) {
      // console.log(" 当前编辑器的状态：", editorRef.value); 
      // 富文本编辑器按 html 格式回显
      editorRef.value.setHtml(newVal)
      valueHtml.value = newVal
    }
  })
}
)

onMounted(async () => {
  await nextTick(); // 延迟渲染，确保 DOM 更新完成
  if (props.content) {
    valueHtml.value = props.content
  }
})

// 组件销毁时，也及时销毁编辑器
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})

</script>