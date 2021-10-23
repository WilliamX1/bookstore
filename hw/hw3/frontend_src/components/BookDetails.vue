<template>
  <div class="BookDetails">
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
    <!--主要视图-->
    <main>
      <el-row gutter="10" style="margin: 20px 120px 20px 120px;">
        <el-col :span="2"></el-col>
        <el-col :span="8">
          <el-image :src="require('../assets/books/' + this.book.image)" style="width: 100%;"></el-image>
        </el-col>
        <el-col :span="12" style="text-align: left; margin-left: 30px;">
          <el-card style="height: 500px">
            <el-row><h1>{{this.book.bookname}}</h1></el-row>
            <el-row><p>作者：{{this.book.author}}</p></el-row>
            <el-row><p>ISBN：{{this.book.isbn}}</p></el-row>
            <el-row>
              <h3>价格 ¥{{this.book.price}}
                库存{{this.book.inventory}}件</h3>
            </el-row>
            <br>
            <el-row><p>简介：{{this.book.introduction}}</p></el-row>
            <br>
            <el-row>
              <el-rate v-model="value" disabled show-score text-color="#ff9900" score-template="{value}"></el-rate>
            </el-row>
            <br>
            <el-row>
              <el-tag v-for="label in labels" :key="label.label" :type="label.type" effect="plain">
                {{ label.label }}
              </el-tag>
            </el-row>
            <br>
            <el-input-number v-model="buycount" controls-position="right" :min="1" :max="this.book.inventory"
                             size="small"></el-input-number>
            <el-button v-if="this.$cookie.get('role') === 'ADMIN'" size="mini" round="true" plain="true"
                       @click="add_to_shopping_cart(book.id, 1)" disabled="">
              加入购物车<i class="el-icon-shopping-bag-1 el-icon--right"></i></el-button>
            <el-button v-else size="mini" round="true" plain="true" @click="add_to_shopping_cart(book.id, 1)">
              加入购物车<i class="el-icon-shopping-bag-1 el-icon--right"></i></el-button>
            <router-link :to="{name:'Order'}">
              <el-button v-if="this.$cookie.get('role') === 'ADMIN'" size="mini" round="true" plain="true" disabled>
                <i class="el-icon-money el-icon--right"></i>立即购买
              </el-button>
              <el-button v-else size="mini" round="true" plain="true" @click="saveActiveCartItems(book.id)">
                <i class="el-icon-money el-icon--right"></i>立即购买
              </el-button>
            </router-link>
          </el-card>
        </el-col>
        <el-col :span="2"></el-col>
      </el-row>
    </main>
    <!--底部-->
    <footer>
      <divider></divider>
      <h2>Copyright © 2021 HuiDong Xu</h2>
    </footer>
  </div>
</template>

<script>
export default {
  name: 'BookDetails.vue',
  data() {
    return {
      value: 4.6,
      buycount: 1,
      labels: [
        {type: '', label: '好看'},
        {type: 'success', label: '降价'},
        {type: 'info', label: '非常不错'},
        {type: 'danger', label: '喜欢'},
        {type: 'warning', label: '质量好'}
      ],
      bookid: 1,
      book: {}
    }
  },
  created() {
    this.bookid = parseInt(this.$route.query.bookid)
    this.book = JSON.parse(localStorage.getItem('books')).find((book) => {
      return this.bookid === book.id
    })
  },
  methods: {
    saveActiveCartItems() {
      localStorage.setItem('activecartitems', JSON.stringify([{'bookcount': 1, 'book': this.book}]))
    },
    add_to_shopping_cart() {
      this.changeBookCount(this.bookid, this.buycount, true)
      this.buycount = 1
    }
  }
}
</script>

<style scoped>
</style>
