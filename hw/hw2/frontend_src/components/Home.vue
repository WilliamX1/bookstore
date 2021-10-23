<template>
  <div class="HomePage">
    <!--顶部导航栏-->
    <header class="navigationBar">
      <el-row>
        <el-col :span="4">
          <el-image
              style="height: 56px; margin:4px 5px 2px 5px; opacity: 0.4;"
              :src="require('../assets/E-book-logo.png')" fit="scale-down">
          </el-image>
        </el-col>
        <el-col :span="20">
          <el-menu :default-active="1" class="el-menu-demo" mode="horizontal">
            <el-menu-item index="1">
              <router-link to='/Home'>首页</router-link>
            </el-menu-item>
            <el-menu-item index="2">
              <router-link v-if="this.$cookie.get('role') === 'USER'" to='/ShoppingCart'>我的购物车</router-link>
              <router-link v-if="this.$cookie.get('role') === 'ADMIN'" to="/Statistics">销量统计</router-link>
            </el-menu-item>
            <el-menu-item index="3">
              <router-link v-if="this.$cookie.get('role') === 'USER'" to="/HistoryOrders">我的订单</router-link>
              <router-link v-if="this.$cookie.get('role') === 'ADMIN'" to="/HistoryOrders">全部订单</router-link>
            </el-menu-item>
            <!--为了挤占空间使得搜索框至最右边-->
            <el-menu-item index="4">
              <router-link to='/Login'>登录</router-link>
            </el-menu-item>
            <el-menu-item>
              <router-link to="/ChatRoom">聊天室</router-link>
            </el-menu-item>
            <el-menu-item></el-menu-item>
            <el-menu-item></el-menu-item>
            <el-menu-item></el-menu-item>
            <el-menu-item></el-menu-item>
            <el-menu-item></el-menu-item>
            <el-menu-item></el-menu-item>
            <el-menu-item>
              <el-input placeholder="搜索" v-model="searchbookstr" @input="searchBook(searchbookstr)">
                <el-button slot="append" icon="el-icon-search"></el-button>
              </el-input>
            </el-menu-item>
            <el-menu-item>
              <router-link to="/Admin">
                <el-avatar src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"></el-avatar>
              </router-link>
            </el-menu-item>
          </el-menu>
        </el-col>
      </el-row>
    </header>
    <el-carousel :interval="4000" type="card" height="400px">
      <el-carousel-item v-for="book in this.activebooks" :key="book">
        <el-image :src="require('../assets/books/' + book.image)" style="height: 100%"
                  @click="_route_to_book_details(book.id)"></el-image>
      </el-carousel-item>
    </el-carousel>
    <!--图书视图-->
    <main class="bookList">
      <el-row :gutter="20" class="el-row">
        <div v-for="book in this.activebooks" v-bind:key="book.id">
          <el-col :span="6" class="el-col">
            <el-card body-style class="el-card" style="height: 560px;">
              <el-image :src="require('../assets/books/' + book.image)" style="height: 360px;"
                        @click="_route_to_book_details(book.id)"></el-image>
              <el-link :underline="false" @click="_route_to_book_details(book.id)"><p>书名：{{ book.bookname }}</p>
              </el-link>
              <br>
              <p>作者：{{ book.author }}</p>
              <p>ISBN：{{ book.isbn }}</p>
              <el-row>
                <el-col :span="12"><h3>¥ {{ book.price }}</h3></el-col>
                <el-col :span="12"><p>库存 {{book.inventory}} 件</p></el-col>
              </el-row>
              <el-button v-if="isAdmin" size="mini" round="true" plain="true" @click="add_to_shopping_cart(book.id, 1)"
                         disabled="">
                加入购物车<i class="el-icon-shopping-bag-1 el-icon--right"></i></el-button>
              <el-button v-else size="mini" round="true" plain="true" @click="add_to_shopping_cart(book.id, 1)">
                加入购物车<i class="el-icon-shopping-bag-1 el-icon--right"></i></el-button>
              <router-link :to="{name:'Order'}">
                <el-button v-if="isAdmin" size="mini" round="true" plain="true" disabled>
                  <i class="el-icon-money el-icon--right"></i>立即购买
                </el-button>
                <el-button v-else size="mini" round="true" plain="true" @click="saveActiveCartItems(book.id)">
                  <i class="el-icon-money el-icon--right"></i>立即购买
                </el-button>
              </router-link>
            </el-card>
          </el-col>
        </div>
      </el-row>
      <divider></divider>
    </main>
    <!--分页-->
    <el-pagination
        layout="prev, pager, next"
        :page-count="this.pagetotalcount"
        :current-page="this.curpageidx"
        @current-change="changePage">
    </el-pagination>
    <!--底部-->
    <footer>
      <h2>Copyright © 2021 HuiDong Xu</h2>
      <el-divider></el-divider>
    </footer>
    <el-scrollbar hidden></el-scrollbar>
  </div>
</template>

<script>
/* eslint-disable camelcase */

export default {
  name: 'Home.vue',
  data() {
    return {
      searchbookstr: '',
      activebooks: [],
      isAdmin: false,
      pagetotalcount: 0,
      pagesize: 4,
      curpageidx: 1
    }
  },
  created() {
    this.isAdmin = this.$cookie.get('role') === 'ADMIN'
    this.getBooks(this.$cookie.get('username'), this.$cookie.get('password')).then(() => {
      this.pagetotalcount = 1 + Math.floor((JSON.parse(localStorage.getItem('books')).length - 1) / this.pagesize)
      let startidx = (this.curpageidx - 1) * this.pagesize
      this.activebooks = JSON.parse(localStorage.getItem('books')).slice(startidx, startidx + this.pagesize)
    })
  },
  methods: {
    saveActiveCartItems(bookid) {
      localStorage.setItem('activecartitems', JSON.stringify([{'bookcount': 1, 'book': this.bookid_to_book(bookid)}]))
    },
    _route_to_book_details(bookid) {
      this.routeToBookDetails(bookid)
    },
    add_to_shopping_cart(bookid, bookcount) {
      this.changeBookCount(bookid, bookcount, true)
    },
    /* 根据书名模糊搜索 */
    searchBook(searchbookstr) {
      if (searchbookstr === '') {
        this.activebooks = JSON.parse(localStorage.getItem('books'))
      } else {
        this.$axios({
          methods: 'GET',
          url: 'http://localhost:9090/book/searchBookByBookname',
          params: {
            searchbookstr: searchbookstr
          }
        }).then(response => {
          this.activebooks = response.data
        }).catch(error => {
          console.log(error)
        })
      }
    },
    /* 更换页面 */
    changePage(val) {
      this.curpageidx = val
      let startidx = (this.curpageidx - 1) * this.pagesize
      this.activebooks = JSON.parse(localStorage.getItem('books')).slice(startidx, startidx + this.pagesize)
      this.$forceUpdate()
    }
  }
}
</script>

<style scoped>
.navigationBar {
  margin-bottom: 20px;
}

.bookList {
  padding: 20px;
}

</style>
