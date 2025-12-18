import { UserLayout, TabLayout, RouteView, BlankLayout, PageView } from '@/components/layouts'

/**
 * 走菜单，走权限控制
 * @type {[null,null]}
 */
export const asyncRouterMap = [

  {
    path: '/',
    name: 'dashboard',
    component: TabLayout,
    meta: { title: '首页' },
    redirect: '/dashboard/analysis',
    children: [

    ]
  },
  {
    path: '*', redirect: '/404', hidden: true
  }
]

/**
 * 基础路由
 * @type { *[] }
 */
export const constantRouterMap = [
  {
    path: '/user',
    component: UserLayout,
    redirect: '/user/login',
    hidden: true,
    children: [
      {
        path: 'login',
        name: 'login',
        component: () => import(/* webpackChunkName: "user" */ '@/views/user/Login')
      },
      {
        path: 'register',
        name: 'register',
        component: () => import(/* webpackChunkName: "user" */ '@/views/user/Register')
      }
    ]
  },
  {
    path: '/dashboard',
    component: TabLayout,
    redirect: '/dashboard/analysis',
    children: [
      {
        path: 'analysis',
        name: 'analysis',
        meta: { title: '首页' },
        component: () => import(/* webpackChunkName: "dashboard" */ '@/views/dashboard/Analysis')
      }
    ]
  },

  // ---------------------------------------
  // Agency Detail (MUST be inside TabLayout)
  // ---------------------------------------
  {
    path: '/agency',
    component: TabLayout,
    redirect: '/agency/index',
    children: [
      {
        path: 'index',
        name: 'Agency',
        meta: { title: 'Agency Profile' },
        component: () => import('@views/system/AgencyList.vue')
      },
      {
        path: 'detail_info',
        name: 'AgencyDetail',
        meta: { title: 'Agency Detail', keepAlive: false },
        component: () => import('@views/system/AgencyDetail.vue')
      }

    ]
  },

  // ---------------------------------------
  // Project Module
  // ---------------------------------------
  {
    path: '/lcproject',
    component: TabLayout,
    redirect: '/lcproject/index',
    children: [
      {
        path: 'detail_info',
        name: 'ProjectDetail',
        meta: { title: 'Project Detail', keepAlive: false },
        component: () => import('@views/system/ProjectDetail.vue')
      }
    ]
  },

  // ---------------------------------------
  // Lcagent Module (similar to Agency)
  // MUST be inside TabLayout
  // ---------------------------------------
  {
    path: '/lcagent',
    component: TabLayout,
    redirect: '/lcagent/index',
    children: [
      {
        path: 'index',
        name: 'Lcagent',
        meta: { title: 'Agents' },
        component: () => import('@views/system/LcagentList.vue')
      }
    ]
  },

  {
    path: '/404',
    component: () => import(/* webpackChunkName: "fail" */ '@/views/exception/404')
  },

]
