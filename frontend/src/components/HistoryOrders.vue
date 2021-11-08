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
              <el-input placeholder="搜索" v-model="searchbookstr" @input="searchOrdersByBook(searchbookstr)">
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
      <el-date-picker
        v-model="searchdatarange"
        type="daterange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        value-format="yyyy-MM-dd"
        format="yyyy-MM-dd"
        @change="searchOrdersByDaterange">
      </el-date-picker>
      <br>
      <el-table
        :data="activebookSales"
        style="width: 100%">
        <el-table-column
          prop="bookid"
          label="id"
          width="200"
          sortable
          align="center">
        </el-table-column>
        <el-table-column
          prop="bookname"
          label="书名"
          width="460px"
          align="center">
        </el-table-column>
        <el-table-column
          prop="booksale"
          label="购买量"
          width="160"
          sortable
          align="center">
        </el-table-column>
        <el-table-column
          prop="bookmoney"
          label="金额"
          width="160"
          sortable
          align="center">
        </el-table-column>
      </el-table>
      <br>
      <div v-for="order in activeorders" v-bind:key="order.id">
          <el-card shadow="hover" style="border-color: #f56c6c">
          <div class="orderTitleView">
            <el-row :gutter="10">
              <!--订单信息-->
              <!--日期-->
              <el-col :span="7"><p>时间: {{ order.timestamp | timeFormat }}</p></el-col>
              <!--订单号-->
              <el-col :span="4"><p>订单号: {{ order.id }}</p></el-col>
              <!--总价-->
              <el-col :span="4"><p>订单总价: {{ order.price }}</p></el-col>
              <!--收货人-->
              <el-col :span="4"><p>收货人: {{order.receivername}}</p></el-col>
              <!--用户id, 管理员可见-->
              <el-col :span="4"><p>用户id: {{typeof(order.user) === 'object'? order.user.id : order.user}}</p></el-col>
            </el-row>
          </div>
          <div v-for="orderitem in order.orderItems" v-bind:key="orderitem.id">
            <div class="orderItemView">
              <el-row :gutter="25">
                <el-col :span="3">
                  <el-image :src="require('../assets/books/' + orderitem.bookimage)" style="width: 90px; height: 120px;"></el-image>
                </el-col>
                <el-col :span="8">
                  <el-link type="info" :underline="false">{{orderitem.introduction.substr(0,100)}}</el-link>
                </el-col>
                <el-col :span="4">
                  <h3 style="margin-top: 20%;">单价: ¥ {{orderitem.bookprice}}</h3>
                </el-col>
                <el-col :span="4">
                  <h3 style="margin-top: 20%;">数量: {{orderitem.bookcount}}</h3>
                </el-col>
                <el-col :span="4">
                  <h3 style="margin-top: 20%;">总价: ¥ {{ (orderitem.bookcount * orderitem.bookprice).toFixed(2)}}</h3>
                </el-col>
              </el-row>
            </div>
            <br>
          </div>
          </el-card>
        </div>
    </el-main>
    <!--底部-->
    <el-footer>
      <h2>Copyright © 2021 HuiDong Xu</h2>
    </el-footer>
  </el-container>

</template>

<script>
/* eslint-disable camelcase */

export default {
  name: 'HistoryOrders',
  data () {
    return {
      searchbookstr: '',
      activeorders: [
        // {
        //   address: '',
        //   orderItems: [
        //     {
        //       bookcount: 0,
        //       bookid: 0,
        //       bookprice: 0,
        //       id: 0,
        //       orderid: 0,
        //       order: 0
        //     }
        //   ],
        //   id: 1,
        //   price: 0,
        //   receivername: '',
        //   timestamp: 0,
        //   userid: 1
        // }
      ],
      searchdatarange: [],
      activebookSales: [
        // {
        //   bookid: 1,
        //   booksale: 0,
        //   bookname: '小东东'
        // }
      ],
      bookcounttotle: 0,
      bookmoneytotle: 0
    }
  },
  methods: {
    searchOrdersByBook (searchbookstr) {
      if (searchbookstr === '') {
        this.activeorders = JSON.parse(localStorage.getItem('orders'))
        this.statisticTotle()
      } else {
        /* 清空日期搜索框 */
        this.searchdatarange = []
        this.$axios({
          methods: 'GET',
          url: 'https://localhost:9090/order/getOrdersByBook',
          params: {
            username: this.$cookie.get('username'),
            password: this.$cookie.get('password'),
            searchbookstr: searchbookstr
          }
        }).then(response => {
          let datas = response.data
          this.activeorders = []
          datas.forEach(data => {
            JSON.parse(localStorage.getItem('orders')).forEach(order => {
              if (order.id === data.id) {
                this.activeorders.push(order)
              }
            })
          })
          this.statisticTotle()
        }).catch(error => {
          console.log(error)
        })
      }
    },
    searchOrdersByDaterange () {
      if (this.searchdatarange[0] === '' || this.searchdatarange[1] === '') {
        this.activeorders = JSON.parse(localStorage.getItem('orders'))
        this.statisticTotle()
      } else {
        /* 清空图书名称搜索框 */
        this.searchbookstr = ''
        this.$axios({
          method: 'GET',
          url: 'https://localhost:9090/order/getOrdersByDaterange',
          params: {
            username: this.$cookie.get('username'),
            password: this.$cookie.get('password'),
            startdate: this.searchdatarange[0],
            enddate: this.searchdatarange[1]
          }
        }).then(response => {
          let datas = response.data
          this.activeorders = []
          datas.forEach(data => {
            JSON.parse(localStorage.getItem('orders')).forEach(order => {
              if (order.id === data.id) {
                this.activeorders.push(order)
              }
            })
          })
          this.statisticTotle()
        }).catch(error => {
          console.log(error)
        })
      }
    },
    statisticTotle () {
      this.activebookSales = []
      this.bookcounttotle = 0
      this.bookmoneytotle = 0
      this.activeorders.forEach(activeorder => {
        activeorder.orderItems.forEach(orderItem => {
          this.bookcounttotle += orderItem.bookcount
          this.bookmoneytotle += orderItem.bookcount * orderItem.bookprice
          let i = 0
          for (i = 0; i < this.activebookSales.length; i++) {
            if (this.activebookSales[i].bookid === orderItem.book) {
              this.activebookSales[i].booksale += orderItem.bookcount
              this.activebookSales[i].bookmoney += orderItem.bookcount * orderItem.bookprice
              break
            }
          }
          let new_ele = {
            bookid: orderItem.book,
            bookname: this.bookid_to_book(orderItem.book).bookname,
            booksale: orderItem.bookcount,
            bookmoney: orderItem.bookcount * orderItem.bookprice
          }
          if (i === this.activebookSales.length) this.activebookSales.push(new_ele)
        })
      })
      this.activebookSales.push({
        bookname: '全部书籍',
        booksale: this.bookcounttotle,
        bookmoney: this.bookmoneytotle
      })
      this.$forceUpdate()
    }
  },
  created () {
    this.getOrders(this.$cookie.get('username'), this.$cookie.get('password')).then(responsedata => {
      this.activeorders = responsedata
      this.activeorders.forEach(activeorder => {
        activeorder.orderItems.forEach(orderItem => {
          if (typeof (orderItem.book) === 'object') orderItem.book = orderItem.book.id
          this.$set(orderItem, 'bookimage', this.bookid_to_book(orderItem.book).image)
          this.$set(orderItem, 'introduction', this.bookid_to_book(orderItem.book).introduction)
        })
      })
      this.statisticTotle()
    })
  },
  filters: {
    timeFormat (val) {
      if (val == null || val === '') {
        return ''
      } else {
        let d = new Date(val) // val 为表格内取到的后台时间
        let month = d.getMonth() + 1 < 10 ? '0' + (d.getMonth() + 1) : d.getMonth() + 1
        let day = d.getDate() < 10 ? '0' + d.getDate() : d.getDate()
        let hours = d.getHours() < 10 ? '0' + d.getHours() : d.getHours()
        let min = d.getMinutes() < 10 ? '0' + d.getMinutes() : d.getMinutes()
        let sec = d.getSeconds() < 10 ? '0' + d.getSeconds() : d.getSeconds()
        let times = d.getFullYear() + '-' + month + '-' + day + ' ' + hours + ':' + min + ':' + sec
        return times
      }
    }
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

.orderTitleView {
  border: 0px solid rgba(55, 55, 255, 0.6);
  text-align: left;
  /*高度一定*/
  height: 40px;
  padding: 10px;
  background-size: cover;
}

.orderItemView {
  border: 0px solid rgba(55, 55, 255, 0.6);
  text-align: left;
  /*高度一定*/
  height: 200px;
  padding: 10px;
  background-size: cover;
}
</style>
