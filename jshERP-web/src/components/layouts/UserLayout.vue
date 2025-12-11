<template>
  <div class="back-layout">
    <div id="userLayout" :class="['user-layout-wrapper', device]">
      <div class="container">
        <div class="poster-img">
          <!-- You can replace this with a real image or keep the gradient -->
          <div class="brand-overlay">
            <h1 href="https://baganti.com/">Baganti ERP</h1>
            <p>Enterprise Resource Planning</p>
          </div>
        </div>
        <div class="right-form">
          <div class="top">
            <div class="header">
              <a-row>
                <a-col>
                  <a href="/">
                    <span class="title">Welcome Back</span>
                  </a>
                </a-col>
              </a-row>
            </div>
            <div class="desc">
              Please login to your account
            </div>
          </div>
          <route-view></route-view>
        </div>
      </div>
    </div>
    <div class="footer" v-if="device === 'desktop'">
      <div class="third-party-platform" v-if="isShowRight">
        <div class="platform-info" @click="openAndroid()">
          <img src="/static/Android.png" style="height:30px" >
          <span>Android</span>
        </div>
        <div style="width:50px"></div>
        <div class="platform-info" @click="openIPhone()">
          <img src="/static/iPhone.png" style="height:30px" >
          <span>iPhone</span>
        </div>
        <div style="width:50px"></div>
        <div class="platform-info" @click="openMiniProgram()">
          <img src="/static/mini-program.png" style="height:30px" >
          <span>Mini Program</span>
        </div>
      </div>
      <p>
        <span v-if="this.isShowRight">Baganti Tech</span>
        © 2015-2030 Baganti Inc - All Right Reserved
<!--        <a style="color:#00458a; padding-right: 10px" :href="systemUrl" target="_blank">Official Website</a>-->
        <span v-if="this.isShowRight"><a href="http://beian.miit.gov.cn/" target="_blank">苏ICP备2021042833号</a></span>
      </p>
    </div>
    <a-modal v-model="isAndroidShow" title="Download Android App" width="200" centered>
      <template slot="footer">
        <a-button key="back" @click="handleAndroidCancel">Cancel</a-button>
      </template>
      <div class="platform-modal"><img src="/static/android-code.png" style="width:200px" /></div>
    </a-modal>
    <a-modal v-model="isIphoneShow" title="Download iPhone App" width="200" centered>
      <template slot="footer">
        <a-button key="back" @click="handleIphoneCancel">Cancel</a-button>
      </template>
      <div class="platform-modal"><img src="/static/iphone-code.png" style="width:200px" /></div>
    </a-modal>
    <a-modal v-model="isMiniProgramShow" title="WeChat Mini Program" width="200" centered>
      <template slot="footer">
        <a-button key="back" @click="handleMiniProgramCancel">Cancel</a-button>
      </template>
      <div class="platform-modal"><img src="/static/weixin-code.png" style="width:200px;" /></div>
    </a-modal>
  </div>
</template>

<script>
  import RouteView from "@/components/layouts/RouteView"
  import { mixinDevice } from '@/utils/mixin.js'

  export default {
    name: "UserLayout",
    components: { RouteView },
    mixins: [mixinDevice],
    data () {
      return {
        systemTitle: window.SYS_TITLE,
        systemUrl: window.SYS_URL,
        isShowRight: false,
        isAndroidShow: false,
        isIphoneShow: false,
        isMiniProgramShow: false,
      }
    },
    mounted () {
      document.body.classList.add('userLayout')
    },
    beforeDestroy () {
      document.body.classList.remove('userLayout')
    },
    created () {
      let host = window.location.host
      if(host === 'cloud.gyjerp.com') {
        this.isShowRight = true
      } else {
        this.isShowRight = false
      }
    },
    methods: {
      handleAndroidCancel() {
        this.isAndroidShow = false
      },
      handleIphoneCancel() {
        this.isIphoneShow = false
      },
      handleMiniProgramCancel() {
        this.isMiniProgramShow = false
      },
      openAndroid() {
        this.isAndroidShow = true
      },
      openIPhone() {
        this.isIphoneShow = true
      },
      openMiniProgram() {
        this.isMiniProgramShow = true
      }
    }
  }
</script>

<style scoped>
  .back-layout {
    width: 100%;
    height: 100vh;
    overflow: hidden;
    display: flex;
    flex-direction: column;
    background: #fff;
  }
  .third-party-platform {
    display: flex;
    flex-direction: row;
    justify-content: center;
    margin-bottom:15px;
    opacity:0.7
  }
  .third-party-platform .platform-info {
    display: flex;
    flex-direction: column;
    align-items: center;
    cursor: pointer;
    color:#1890ff
  }
  .platform-modal {
    padding:20px;
    margin:20px 50px;
    border:1px solid #eee;
  }
</style>

<style lang="less" scoped>
  #userLayout.user-layout-wrapper {
    height: 100%;
    width: 100%;

    &.mobile {
      .container {
        flex-direction: column;
        .poster-img {
          display: none;
        }
        .right-form {
          width: 100%;
          padding: 20px;
        }
      }
    }

    .container {
      display: flex;
      width: 100%;
      height: 100%;
      
      .poster-img {
        flex: 1.2; /* Takes up more space (approx 55-60%) */
        background: linear-gradient(135deg, #001529 0%, #003a75 100%);
        display: flex;
        align-items: center;
        justify-content: center;
        position: relative;
        overflow: hidden;
        color: white;

        /* Abstract pattern overlay */
        &::before {
          content: '';
          position: absolute;
          top: 0;
          left: 0;
          right: 0;
          bottom: 0;
          background-image: 
            radial-gradient(circle at 20% 30%, rgba(255,255,255,0.05) 0%, transparent 20%),
            radial-gradient(circle at 80% 70%, rgba(255,255,255,0.05) 0%, transparent 20%);
          background-size: 100% 100%;
        }

        .brand-overlay {
          z-index: 2;
          text-align: center;
          
          h1 {
            font-size: 48px;
            color: white;
            margin-bottom: 16px;
            font-weight: 700;
            letter-spacing: 1px;
          }
          
          p {
            font-size: 20px;
            color: rgba(255,255,255,0.8);
            font-weight: 300;
          }
        }
      }

      .right-form {
        flex: 0.8; /* Takes up less space */
        background: #fff;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        padding: 40px;
        position: relative;
        z-index: 1;

        .top {
          text-align: center;
          margin-bottom: 40px;
          width: 100%;
          max-width: 360px;

          .header {
            margin-bottom: 16px;
            .title {
              font-size: 28px;
              color: #333;
              font-weight: 600;
              font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
            }
          }
          .desc {
            font-size: 16px;
            color: rgba(0, 0, 0, 0.45);
          }
        }
      }
    }
  }

  .footer {
    position: absolute;
    bottom: 20px;
    width: 100%;
    text-align: center;
    color: rgba(0, 0, 0, 0.45);
    font-size: 12px;
    pointer-events: none;
    
    .third-party-platform {
      pointer-events: auto;
    }
    
    p {
      pointer-events: auto;
    }

    .links {
      margin-bottom: 8px;
      font-size: 14px;
      a {
        color: rgba(0, 0, 0, 0.45);
        transition: all 0.3s;
        &:not(:last-child) {
          margin-right: 40px;
        }
      }
    }
  }
</style>