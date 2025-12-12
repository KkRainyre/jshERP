<template>
  <div class="logo">
    <img :src="logoSrc" alt="Logo" class="logo-image" @error="handleImageError" />
  </div>
</template>

<script>
  import Vue from 'vue'
  import { mixin } from '@/utils/mixin.js'
  import { mapGetters } from 'vuex'
  import { USER_INFO } from '@/store/mutation-types'

  export default {
    name: 'Logo',
    mixins: [mixin],
    props: {
      title: {
        type: String,
        default: window.SYS_TITLE,
        required: false
      },
      showTitle: {
        type: Boolean,
        default: true,
        required: false
      }
    },
    data() {
      return {
        logoSrc: require('@/assets/logo.png'), // Default logo
        imageError: false
      }
    },
    computed: {
      ...mapGetters(['userInfo']),
      tenantId() {
        const user = this.userInfo  // userInfo is a getter that returns object, not a function
        console.log('Full user object:', JSON.stringify(user, null, 2))
        console.log('User keys:', user ? Object.keys(user) : 'no user')
        
        // Try different possible field names
        const possibleTenantId = user && (
          user.tenantId || 
          user.tenant_id || 
          user.TenantId ||
          user.TENANT_ID
        )
        
        console.log('Detected tenant ID:', possibleTenantId)
        return possibleTenantId || null
      }
    },
    mounted() {
      console.log('Logo component mounted')
      // Check localStorage directly
      const userFromLS = Vue.ls.get(USER_INFO)
      console.log('User from localStorage:', userFromLS)
      console.log('User from Vuex getter:', this.userInfo)
    },
    watch: {
      tenantId: {
        immediate: true,
        handler(newTenantId) {
          console.log('Tenant ID changed:', newTenantId)
          this.loadTenantLogo(newTenantId)
        }
      }
    },
    methods: {
      loadTenantLogo(tenantId) {
        if (tenantId) {
          console.log('Loading logo for tenant:', tenantId)
          // Use public folder path for dynamic loading
          const tenantLogoPath = `/static/logos/logo_tenant_${tenantId}.png`
          
          // Test if image exists
          const img = new Image()
          img.onload = () => {
            console.log('Tenant logo found:', tenantLogoPath)
            this.logoSrc = tenantLogoPath
            this.imageError = false
          }
          img.onerror = () => {
            console.log('Tenant logo not found, using default')
            this.logoSrc = require('@/assets/logo.png')
          }
          img.src = tenantLogoPath
        } else {
          console.log('No tenant ID, using default logo')
          this.logoSrc = require('@/assets/logo.png')
        }
      },
      handleImageError() {
        // Fallback to default logo if image fails to load
        console.log('Image error, falling back to default')
        if (!this.imageError) {
          this.imageError = true
          this.logoSrc = require('@/assets/logo.png')
        }
      }
    }
  }
</script>
<style lang="less" scoped>
  /*缩小首页布 局顶部的高度*/
  @height: 49px;

  .sider {
    box-shadow: none !important;
    .logo {
      color: #ffffff;
      height: @height !important;
      line-height: @height !important;
      box-shadow: none !important;
      transition: background 300ms;
      background: #002140;
      display: flex;
      align-items: center;
      justify-content: center;
      padding: 8px;
      
      .logo-image {
        max-width: 100%;
        max-height: 40px;
        width: auto;
        height: auto;
        object-fit: contain;
      }
      a {
        color: white;
        &:hover {
          color: rgba(255, 255, 255, 0.8);
        }
      }

    }

    &.light .logo {
      background-color: @primary-color;
    }
  }
</style>