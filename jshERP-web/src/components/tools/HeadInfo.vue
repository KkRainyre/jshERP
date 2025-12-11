<template>
  <div class="head-info" :class="center && 'left'">
    <p>￥{{ content }}</p>
    <em v-if="bordered"/>
  </div>
</template>

<script>
  export default {
    name: "HeadInfo",
    props: {
      title: {
        type: String,
        default: ''
      },
      content: {
        type: [Number, String],
        default: 0
      },
      bordered: {
        type: Boolean,
        default: false
      },
      center: {
        type: Boolean,
        default: true
      }
    },
    computed: {
      normalizedContent() {
        if (this.content === null || this.content === undefined || this.content === '') {
          return 0;
        }
        const num = typeof this.content === 'string' ? parseFloat(this.content) : this.content;
        return isNaN(num) ? 0 : num;
      }
    }
  }
</script>

<style lang="less" scoped>
  .head-info {
    position: relative;
    text-align: left;
    padding: 0 32px 0 0;
    min-width: 125px;

    &.center {
      text-align: center;
      padding: 0 32px;
    }

    span {
      color: rgba(0, 0, 0, .45);
      display: inline-block;
      font-size: 14px;
      line-height: 22px;
      margin-bottom: 4px;
    }
    p {
      color: rgba(0, 0, 0, .85);
      font-size: 28px;
      line-height: 32px;
      margin: 0;
    }
    em {
      background-color: #e8e8e8;
      position: absolute;
      height: 56px;
      width: 1px;
      top: 0;
      right: 0;
    }
  }
</style>