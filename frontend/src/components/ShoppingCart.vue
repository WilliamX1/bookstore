<template>
  <el-container>
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
              <router-link v-if="this.$global.role === 'USER'" to='/ShoppingCart'>我的购物车</router-link>
              <router-link v-if="this.$global.role === 'ADMIN'" to="/Statistics">销量统计</router-link>
            </el-menu-item>
            <el-menu-item index="3">
              <router-link v-if="this.$global.role === 'USER'" to="/HistoryOrders">我的订单</router-link>
              <router-link v-if="this.$global.role === 'ADMIN'" to="/HistoryOrders">全部订单</router-link>
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
              <el-input placeholder="搜索" v-model="searchbookstr" @input="searchCartByBook(searchbookstr)">
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
    <!--图书视图-->
    <el-main class="cartList">
      <ul>
        <div v-for="item in activecartitems" v-bind:key="item.id">
          <div class="cartView">
            <el-row :gutter="25">
              <el-col :span="3">
                <el-image :src="require('../assets/books/' + item.book.image)" style="width: 90px; height: 120px;"></el-image>
              </el-col>
              <el-col :span="7">
                <el-link type="info" :underline="false">{{item.book.introduction}}</el-link>
              </el-col>
              <el-col :span="3">
                <h3 style="margin-top: 20%;">¥ {{item.book.price}}</h3>
              </el-col>
              <el-col :span="4">
                <el-input-number style="margin-top: 10%;" size="small" v-model="item.bookcount" :min="1" :max="10"
                                 label="描述文字" @change="_changeBookCount(item.book.id, item.bookcount)"></el-input-number>
              </el-col>
              <el-col :span="3">
                <h3 style="margin-top: 20%;">¥ {{ (item.bookcount * item.book.price).toFixed(2)}}</h3>
              </el-col>
              <el-col :span="2">
                <el-row>
                  <router-link to="/Order">
                    <el-button type="text" style="color: #F56C6C;">立即购买</el-button>
                  </router-link>
                </el-row>
                <el-row>
                  <el-button type="text" style="color: #606266;" @click.native="deleteBookFromCart(item.book.id)">删除
                  </el-button>
                </el-row>
              </el-col>
            </el-row>
          </div>
          <divider></divider>
        </div>
      </ul>
    </el-main>
    <div class="billBottomBar">
      <el-row>
        <el-col :span="2">&nbsp;</el-col>
        <el-col :span="9">&nbsp;</el-col>
        <el-col :span="3">
          <router-link to="/Order">
            <el-button type="primary">进入结算</el-button>
          </router-link>
        </el-col>
        <el-col :span="4">&nbsp;</el-col>
      </el-row>
    </div>
    <!--底部-->
    <el-footer>
      <h2>Copyright © 2021 HuiDong Xu</h2>
    </el-footer>
  </el-container>

</template>

<script>
/* eslint-disable camelcase */

export default {
  name: 'ShoppingCart',
  data () {
    return {
      activecartitems: [],
      searchbookstr: ''
    }
  },
  methods: {
    /* 删除某本书全部数量 */
    deleteBookFromCart (bookid) {
      this.$confirm('删除此书籍, 是否继续', '提示', {
        confirmButtonText: '确定删除',
        cancelButtonText: '我再想想',
        type: 'warning'
      }).then(() => {
        this.$message({
          type: 'success',
          message: '删除成功'
        })
        /* 删除书籍 */
        this.changeBookCount(bookid, 0, false)
        let a = this.activecartitems.filter(item => {
          return item.book.id !== bookid
        })
        this.activecartitems = a
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    /* 修改购物车图书数量更新 */
    _changeBookCount (bookid, count) {
      this.changeBookCount(bookid, count, false)
      /* 更新界面显示 */
    },
    /* 根据书名查询书籍 */
    searchCartByBook (searchbookstr) {
      /* 获取图书数量信息 */
      let cartItems = []
      console.log(this.searchbookstr)
      this.getCartItems(this.searchbookstr).then((responsedata) => {
        cartItems = responsedata
      }).finally(() => {
        this.$global.carts = cartItems
        this.activecartitems = []
        cartItems.forEach((cartItem) => {
          console.log(this.bookid_to_book(cartItem.bookid))
          let book = this.bookid_to_book(cartItem.bookid)
          book.count = cartItem.bookcount
          this.activecartitems.push(book)
        })
      })
    }
  },
  created () {
    this.$global.username = this.$cookie.get('username')
    this.$global.password = this.$cookie.get('password')
    this.getBooks(this.$global.username, this.$global.password)

    /* 获取图书数量信息 */
    this.getCartItems('').then((responsedata) => {
      this.activecartitems = responsedata
    })
  }
}

</script>

<style scoped>
body {
  padding-bottom: 50px;
}

.billBottomBar {
  position: fixed;
  bottom: 0px;
  width: 100%;
  height: 56px;
  /*透明度*/
  background-color: rgba(220, 220, 220, 0.8);
  /*z-index代表层次高度, 999999设为最高层*/
  z-index: 999999;
}

.navigationBar {
  margin-bottom: 20px;
}

.cartList {
  margin-left: 120px;
  margin-right: 120px;
}

.cartView {
  border: 0px solid rgba(55, 55, 255, 0.6);
  text-align: left;
  /*高度一定*/
  height: 140px;
  padding: 20px;
  background-size: cover;
}
</style>
