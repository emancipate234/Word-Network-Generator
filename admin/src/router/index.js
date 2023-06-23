import Vue from 'vue'
import VueRouter from 'vue-router'

import Login from '../components/Login.vue'
import Home from '../views/Home.vue'

import Password from '../views/Password'
import UserInfo from '../views/UserInfo'
import User from '../views/User'
import Word from '../views/Word'
import WordFeedback from '../views/WordFeedback'
import WordFeedbackMy from '../views/WordFeedbackMy'
import WordNewWork from '../views/WordNewWork'
import WordNewWorkMy from '../views/WordNewWorkMy'
import WordRootsAffixes from '../views/WordRootsAffixes'
import WordRootsAffixesMy from '../views/WordRootsAffixesMy'
import WordReview from '../views/WordReview'

Vue.use(VueRouter)

const routes = [
  { path: '/',redirect: '/admin',name: 'Login', component: Login, hidden: true  },

  { path: '/admin', name: 'Login', component: Login, hidden: true},

  {
    path: '/admin/home',
    name: '后台管理',
    component: Home,
    children:[
      { path: '/admin/userInfo', name: '个人信息',  component: UserInfo, type: ''},
      { path: '/admin/password', name: '修改密码',  component: Password, type: ''},
      { path: '/admin/user', name: '用户管理',  component: User, type: '1' },
      { path: '/admin/word', name: '单词管理',  component: Word, type:'1' },
      { path: '/admin/wordFeedback', name: '反馈管理',  component: WordFeedback, type:'1' },
      { path: '/admin/wordFeedbackMy', name: '我的反馈',  component: WordFeedbackMy, type:'2' },
      { path: '/admin/wordNewWork', name: '单词网络管理',  component: WordNewWork, type:'1' },
      { path: '/admin/wordNewWorkMy', name: '我的单词网络',  component: WordNewWorkMy, type:'2' },
      { path: '/admin/WordRootsAffixes', name: '词根词缀管理',  component: WordRootsAffixes, type:'1' },
      { path: '/admin/WordRootsAffixesMy', name: '我的词根词缀',  component: WordRootsAffixesMy, type:'2' },
      { path: '/admin/WordReview', name: '复习单词',  component: WordReview, type:'2' },
    ]
  }
]

const router = new VueRouter({
  routes
})

export default router
