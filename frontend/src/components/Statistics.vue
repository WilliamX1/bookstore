<template>
  <div>
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
    <main>
      <template>
        <el-row style="margin: 20px 80px 20px 80px;" gutter="20">
          <el-col span="12">
            <el-card>
              <div slot="header">
                <el-col span="14"><h1>书籍热销榜</h1></el-col>
<!--                <el-col span="8"><el-input placeholder="搜索用户" size="medium"></el-input></el-col>-->
              </div>
              <el-date-picker
                v-model="bookdate"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                value-format="yyyy-MM-dd"
                format="yyyy-MM-dd"
                @change="__getBooksales__">
              </el-date-picker>
              <el-table
                :data="bookSales"
                style="width: 100%">
                <el-table-column
                  prop="bookid"
                  label="id"
                  width="100"
                  sortable
                  align="center">
                </el-table-column>
                <el-table-column
                  prop="bookname"
                  label="书名"
                  width="240px"
                  align="center">
                </el-table-column>
                <el-table-column
                  prop="booksale"
                  label="销量"
                  width="180"
                  sortable
                  align="center">
                </el-table-column>
              </el-table>
            </el-card>
          </el-col>
          <el-col span="12">
            <el-card>
              <div slot="header">
                <el-col span="14"><h1>用户消费榜</h1></el-col>
                <!--                <el-col span="8"><el-input placeholder="搜索用户" size="medium"></el-input></el-col>-->
              </div>
              <el-date-picker
                v-model="userdate"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                value-format="yyyy-MM-dd"
                format="yyyy-MM-dd"
                @change="_getUserconsumptions">
              </el-date-picker>
              <el-table
                :data="userSales"
                style="width: 100%">
                <el-table-column
                  prop="userid"
                  label="id"
                  width="200"
                  sortable
                  align="center">
                </el-table-column>
<!--                <el-table-column-->
<!--                  prop="username"-->
<!--                  label="用户名"-->
<!--                  width="280"-->
<!--                  sortable-->
<!--                  align="center">-->
<!--                </el-table-column>-->
                <el-table-column
                  prop="userconsumption"
                  label="消费"
                  width="280"
                  sortable
                  align="center">
                </el-table-column>
              </el-table>
            </el-card>
          </el-col>
        </el-row>
      </template>
    </main>
  </div>
</template>

<script>
export default {
  name: 'Statistics.vue',
  created () {
    this._getBooksales().then((responsedata) => {
      this.bookSales = responsedata
    })
    this._getUserconsumptions()
  },
  data () {
    return {
      searchbookstr: '',
      bookSales: [
        // {
        //   bookid: 1,
        //   booksale: 0,
        //   bookname: '小东东'
        // }
      ],
      bookdate: [],
      userdate: [],
      userSales: [
        // {
        //   userid: 1,
        //   userconsumption: 0
        // }
      ]
    }
  },
  methods: {
    _getBooksales () {
      return new Promise((resolve, reject) => {
        this.getBooksales(this.bookdate[0], this.bookdate[1]).then((responsedata) => {
          responsedata.forEach(data => {
            data.bookname = this.bookid_to_book(data.bookid).bookname
          })
          resolve(responsedata)
        })
      })
    },
    __getBooksales__ () {
      this._getBooksales().then((responsedata) => {
        this.bookSales = responsedata
      })
    },
    _getUserconsumptions () {
      this.getUserconsumptions(this.userdate[0], this.userdate[1]).then((responsedata) => {
        this.userSales = responsedata
      })
    }
  }
}
</script>

<style scoped>

</style>
