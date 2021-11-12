/* eslint-disable camelcase */
// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import Vuex from 'vuex'
import cookie from 'js-cookie'

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

import ViewUI from 'view-design'
import 'view-design/dist/styles/iview.css'

import axios from 'axios'
import VueAxios from 'vue-axios'

Vue.use(VueAxios, axios)
Vue.use(ElementUI)
Vue.use(ViewUI)
Vue.use(Vuex)
Vue.use(cookie)

Vue.prototype.$axios = axios
axios.defaults.baseURL = '/api'
Vue.config.productionTip = false

/* 将全局变量挂在到 cookie 上 */
Vue.prototype.$cookie = cookie

/* 获取某一阶段用户消费情况 */
Vue.prototype.getUserconsumptions = function getUserconsumptions (startdate, enddate) {
  return new Promise((resolve, reject) => {
    this.$axios({
      method: 'GET',
      url: 'https://localhost:9090/user/getUserconsumptions',
      params: {
        startdate: startdate,
        enddate: enddate
      }
    }).then(response => {
      let datas = JSON.parse(JSON.stringify(response.data))
      /* 将data转换为数组对象 */
      let userconsumptions = []
      for (let key in datas) {
        let userconsumption = {
          userid: parseInt(key),
          userconsumption: datas[key]
        }
        userconsumptions.push(userconsumption)
      }
      resolve(userconsumptions)
    }).catch(error => {
      reject(error)
    })
  })
}

/* 获取某一阶段图书信息 */
Vue.prototype.getBooksales = function getBooksales (startdate, enddate) {
  return new Promise((resolve, reject) => {
    this.$axios({
      method: 'GET',
      url: 'https://localhost:9090/order/getBookSales',
      params: {
        startdate: startdate,
        enddate: enddate
      }
    }).then(response => {
      let datas = JSON.parse(JSON.stringify(response.data))
      /* 将data转换为数组对象 */
      let bookSales = []
      for (let key in datas) {
        let booksale = {
          bookid: parseInt(key),
          booksale: datas[key]
        }
        bookSales.push(booksale)
      }
      resolve(bookSales)
    }).catch(error => {
      reject(error)
    })
  })
}
/* 获取图书信息 */
Vue.prototype.getBooks = function getBooks (username, password) {
  return new Promise((resolve, reject) => {
    this.axios({
      method: 'GET',
      url: 'https://localhost:9090/book/getBooks', /* 获取图书文字信息 */
      params: {
        username: username,
        password: password
      }
    }).then(response => {
      if (response.status === 200) {
        let books = response.data

        for (let i = 0; i < books.length; i++) {
          this.$set(books[i], 'imageBase64', '')
        }
        localStorage.setItem('books', JSON.stringify(books))
        this.axios({
          method: 'GET',
          url: 'https://localhost:9090/book/getBookImages', /* 获取图书 base64 图片信息 */
          params: {
            username: username,
            password: password
          }
        }).then(response => {
          if (response.status === 200) {
            this.$message({
              duration: 1000,
              title: '提示信息',
              message: '获取图书信息成功',
              type: 'success'
            })
            let bookImages = response.data
            for (let i = 0; i < bookImages.length; i++) {
              let index = this.bookid_to_index(bookImages[i].bookId)
              if (index !== -1) books[index].imageBase64 = bookImages[i].imageBase64
            }
            localStorage.setItem('books', JSON.stringify(books))
            resolve(books)
          }
        }).catch(error => {
          this.$message({
            duration: 1000,
            title: '提示信息',
            message: '权限不够',
            type: 'error'
          })
          reject(error)
        })
      }
    }).catch(error => {
      this.$message({
        duration: 1000,
        title: '提示信息',
        message: '权限不够',
        type: 'error'
      })
      reject(error)
    })
  })
}

/* 成功访问首页 */
Vue.prototype.apiVisitHistory = function apiVisitHistory () {
  return new Promise((resolve, reject) => {
    this.axios({
      method: 'GET',
      url: 'https://localhost:9090/user/apiVisitHistory'
    }).then(response => {
      if (response.status === 200) {
        this.$message({
          duration: 1000,
          title: '提示信息',
          message: '访问首页成功',
          type: 'success'
        })
        resolve(response.data)
      }
    }).catch(error => {
      this.$message({
        duration: 1000,
        title: '提示信息',
        message: '访问出错',
        type: 'error'
      })
      reject(error)
    })
  })
}
/* 获取购物车图书信息 */
Vue.prototype.getCartItems = function getCartItems (searchbookstr) {
  return new Promise((resolve, reject) => {
    this.axios({
      method: 'GET',
      url: 'https://localhost:9090/user/getCartItems',
      params: {
        username: cookie.get('username'),
        password: cookie.get('password'),
        searchbookstr: searchbookstr
      }
    }).then(response => {
      if (response.status === 200) {
        this.$message({
          duration: 1000,
          title: '提示信息',
          message: '获取购物车图书数量成功',
          type: 'success'
        })
        localStorage.setItem('carts', JSON.stringify(response.data))
        resolve(response.data)
      }
    }).catch(error => {
      this.$message({
        duration: 1000,
        title: '提示信息',
        message: '权限不够',
        type: 'error'
      })
      reject(error)
    })
  })
}

/* 更改图书 */
Vue.prototype.changeBookCount = function changeBookCount (bookid, bookcount, isadd) {
  return new Promise((resolve, reject) => {
    this.axios({
      method: 'POST',
      url: isadd === true ? 'https://localhost:9090/user/changeBookCountAdd' : 'https://localhost:9090/user/changeBookCountTo',
      params: {
        username: cookie.get('username'),
        password: cookie.get('password'),
        bookid: bookid,
        bookcount: bookcount
      }
    }).then(response => {
      if (response.status === 200) {
        this.$message({
          duration: 1000,
          title: '提示信息',
          message: '更改图书数量成功',
          type: 'success'
        })
      }
      resolve(response)
    }).catch(error => {
      this.$message({
        duration: 1000,
        title: '提示信息',
        message: '更改图书数量失败',
        type: 'error'
      })
      reject(error)
    })
  })
}

/* 更改图书inventory */
Vue.prototype.changeBookInventory = function changeBookInventory (bookid, changeinventory, isadd) {
  JSON.parse(localStorage.getItem('books'))[this.bookid_to_index(bookid)].inventory -= changeinventory

  this.axios({
    method: 'GET',
    url: 'https://localhost:9090/book/changeBookInventory',
    params: {
      bookid: bookid,
      changeinventory: changeinventory,
      isadd: isadd
    }
  }).then(response => {
    if (response.status === 200) {
      this.$message({
        duration: 1000,
        title: '提示信息',
        message: '更改图书库存成功',
        type: 'success'
      })
    }
  }).catch(error => {
    console.log(error)
    this.$message({
      duration: 1000,
      title: '提示信息',
      message: '更改图书库存失败',
      type: 'error'
    })
  })
}
/* 跳转至书籍详情页 */
Vue.prototype.routeToBookDetails = function routeToBookDetails (bookid) {
  cookie.set('bookid', bookid, 1)
  this.$router.push({
    path: '/bookDetails',
    query: {
      bookid: bookid
    }
  })
}
/* 获得用户信息 */
Vue.prototype.getUsers = function getUsers () {
  return new Promise((resolve, reject) => {
    this.$axios({
      method: 'GET',
      url: 'https://localhost:9090/user/getUsers',
      params: {
        username: cookie.get('username'),
        password: cookie.get('password')
      }
    }).then(response => {
      localStorage.setItem('users', JSON.stringify(response.data))
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}
/* 根据bookid获取数组下标index */
Vue.prototype.bookid_to_index = function bookid_to_index (bookid) {
  let index = -1
  JSON.parse(localStorage.getItem('books')).forEach((book, idx) => {
    if (book.id === bookid) index = idx
  })
  return index
}
/* 根据 bookid 获取 globalbooks 中 book */
Vue.prototype.bookid_to_book = function bookid_to_book (bookid) {
  let index = this.bookid_to_index(bookid)
  if (index === -1) return null
  else return JSON.parse(localStorage.getItem('books'))[index]
}
Vue.prototype.userid_to_index = function userid_to_index (userid) {
  let index = -1
  JSON.parse(localStorage.getItem('users')).forEach((user, idx) => {
    if (user.id === userid) index = idx
  })
  return index
}
Vue.prototype.userid_to_user = function userid_to_user (userid) {
  let index = this.userid_to_index(userid)
  if (index !== -1) return JSON.parse(localStorage.getItem('users'))[index]
  else return null
}
/* 获取订单信息 */
Vue.prototype.getOrders = function getOrders (username, password) {
  return new Promise((resolve, reject) => {
    this.$axios({
      method: 'GET',
      url: 'https://localhost:9090/order/getOrders',
      params: {
        username: username,
        password: password
      }
    }).then(response => {
      if (response.status === 200) {
        this.$message({
          duration: 1000,
          title: '提示信息',
          message: '获取历史订单信息成功',
          type: 'success'
        })
        localStorage.setItem('orders', JSON.stringify(response.data))
      }
      resolve(response.data)
    }).catch(error => {
      reject(error)
    })
  })
}
/* 获取订单详情物品信息 */
Vue.prototype.getOrderItems = function getOrderItems (username, password) {
  return new Promise((resolve, reject) => {
    this.$axios({
      method: 'GET',
      url: 'https://localhost:9090/order/getOrderItems',
      params: {
        username: username,
        password: password
      }
    }).then(response => {
      if (response.status === 200) {
        this.$message({
          duration: 1000,
          title: '提示信息',
          message: '获取历史订单信息成功',
          type: 'success'
        })
        localStorage.setItem('orderitems', JSON.stringify(response.data))
        resolve(response.data)
      }
    }).catch(error => {
      reject(error)
    })
  })
}
/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: {App},
  template: '<App/>'
})
