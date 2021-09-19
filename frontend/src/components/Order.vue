<template>
  <div class="Order">
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
            <el-menu-item></el-menu-item>
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
    <!--结算页面-->
    <main>
      <el-steps :active="stepindex" simple>
        <el-step title="我的购物车" icon="el-icon-edit"></el-step>
        <el-step title="填写核对订单信息" icon="el-icon-upload"></el-step>
        <el-step title="成功提交订单" icon="el-icon-picture"></el-step>
      </el-steps>
      <el-row>
        <el-divider content-position="left" v-if="show_or_no_show"><h2>填写并核对订单</h2></el-divider>
        <el-divider content-position="left" v-else><h2>我的订单</h2></el-divider>
      </el-row>
      <el-row>
        <el-row>
          <el-col :span="3"><h4>收货人信息</h4></el-col>
          <el-col :span="15">&nbsp;</el-col>
          <el-col :span="6">
            <el-link>新增收货地址</el-link>
          </el-col>
        </el-row>
        <el-row style="margin: 10px 100px 10px 100px;">
          <el-collapse accordion="true">
            <el-collapse-item v-for="(consignee, index) in consignees" :name="index" :key="index"
                              :title="consignee.label"
                              @click="chooseConsignee(key)">
              <h5>详细地址：{{consignee.address }}</h5>
              <h5>联系方式：{{consignee.name}} {{consignee.phone}}</h5>
            </el-collapse-item>
          </el-collapse>
        </el-row>
        <el-divider></el-divider>
        <el-row style="padding-bottom: 10px;" v-if="show_or_no_show">
          <el-col :span="3"><h4>支付方式</h4></el-col>
        </el-row>
        <el-row v-if="show_or_no_show">
          <el-col :span="3">
            <el-button type="primary" size="mini" plain>支付宝</el-button>
          </el-col>
          <el-col :span="3">
            <el-button type="primary" size="mini" plain>微信支付</el-button>
          </el-col>
          <el-col :span="3">
            <el-button type="primary" size="mini" plain>花呗</el-button>
          </el-col>
        </el-row>
        <el-divider></el-divider>
        <el-row>
          <el-col :span="3"><h4>送货清单</h4></el-col>
          <el-col :span="15">&nbsp;</el-col>
          <el-col :span="6">
            <router-link to="/ShoppingCart" v-if="show_or_no_show"><h4>返回修改购物车</h4></router-link>
          </el-col>
        </el-row>
        <div v-for="item in this.activeitems" v-bind:key="item.id">
          <div class="cartView">
            <el-row :gutter="25">
              <el-col :span="2">&nbsp;</el-col>
              <el-col :span="1">
                <el-checkbox checked="true" disabled="true"></el-checkbox>
              </el-col>
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
                                 label="描述文字" disabled="true"></el-input-number>
              </el-col>
              <el-col :span="3">
                <h3 style="margin-top: 20%;">¥ {{ (item.bookcount * item.book.price).toFixed(2)}}</h3>
              </el-col>
            </el-row>
          </div>
          <divider></divider>
        </div>
        <el-row>
          <el-col span="16">&nbsp;</el-col>
          <el-col span="3"><h3 style="margin: 8px;">应付总额：¥ {{payment}}</h3></el-col>
          <el-col span="3">
            <el-button v-if="show_or_no_show" style="margin: 0px;" @click="submitOrder">提交订单</el-button>
          </el-col>
        </el-row>
      </el-row>
    </main>
    <!--底部-->
    <footer>
      <h2>Copyright © 2021 HuiDong Xu</h2>
      <divider></divider>
    </footer>
    <el-scrollbar hidden></el-scrollbar>
  </div>
</template>

<script>
/* eslint-disable camelcase */

export default {
  name: 'Order.vue',
  data () {
    return {
      curConsignee: 0,
      consignees: [
        {
          id: '1',
          label: '学校',
          name: '徐惠东',
          address: '上海交通大学闵行校区东区宿舍',
          phone: '18877112288'
        },
        {
          id: '2',
          label: '家',
          name: '老妈',
          address: '江西省南昌市青山湖区',
          phone: '19888882244'
        }
      ],
      stepindex: 2,
      show_or_no_show: true,
      activeitems: [],
      payment: 0
    }
  },
  methods: {
    chooseConsignee (key) {
      this.curConsignee = key
    },
    /* 用户提交 */
    submitOrder () {
      this.stepindex = 3
      this.show_or_no_show = false
      if (this.activeitems.length === 0) {
        this.$message({
          duration: 1000,
          title: '提示信息',
          message: '请添加书籍',
          type: 'error'
        })
        return
      }
      this.addOrderFromUser()
    },
    /* 生成订单 */
    addOrderFromUser () {
      let bookid = []
      let bookcount = []
      let bookprice = []
      this.activeitems.forEach(activeitem => {
        bookid.push(activeitem.book.id)
        bookcount.push(activeitem.bookcount)
        bookprice.push(activeitem.book.price)
      })
      this.axios({
        method: 'POST',
        url: 'http://localhost:9090/order/addOrderFromUser',
        params: {
          username: this.$global.username,
          password: this.$global.password,
          bookidstr: JSON.stringify(bookid),
          bookcountstr: JSON.stringify(bookcount),
          bookpricestr: JSON.stringify(bookprice),
          receivername: '小东东',
          address: '上海市闵行区东川路800号'
        }
      }).then(response => {
        console.log(response)
        if (response.status === 200) {
          this.$message({
            duration: 1000,
            type: 'success',
            message: '正在处理交易'
          })
        }
      }).catch(err => {
        console.log(err)
        if (err.response.status === 406) {
          this.$message({
            duration: 1000,
            type: 'error',
            message: '库存不够，请重新购买全部书籍'
          })
        }
      })
      this.getBooks(this.$global.username, this.$global.password)
    }
  },
  created () {
    this.$global.username = this.$cookie.get('username')
    this.$global.password = this.$cookie.get('password')
    this.getBooks(this.$global.username, this.$global.password)

    /* 获取图书数量信息 */
    this.getCartItems('').then((responsedata) => {
      this.activeitems = responsedata
      console.log(responsedata)
    }).finally(() => {
      this.activeitems.forEach((cartItem) => {
        this.payment += cartItem.bookcount * cartItem.book.price
      })
    })
  }
}
</script>

<style scoped>
.cartView {
  border: 0px solid rgba(55, 55, 255, 0.6);
  text-align: left;
  /*高度一定*/
  height: 140px;
  padding: 20px;
  background-size: cover;
}
</style>
