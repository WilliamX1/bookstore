<template>
  <div class = "ChatRoom">
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
            <el-menu-item></el-menu-item>
            <el-menu-item></el-menu-item>
            <el-menu-item></el-menu-item>
            <el-menu-item></el-menu-item>
            <el-menu-item></el-menu-item>
            <el-menu-item></el-menu-item>
            <el-menu-item></el-menu-item>
            <el-menu-item>
              <router-link to="/Admin">
                <el-avatar src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"></el-avatar>
              </router-link>
            </el-menu-item>
          </el-menu>
        </el-col>
      </el-row>
    </header>
    <main class="chatRoom">
      <el-col :span="12" class="el-col">
        <div class="wrapper">
          <h3 style="font-size: 26px; font-family: 楷体; font-weight: bold; margin-bottom: 16px">群聊</h3>
          <div class="message-panel">
            <msg-box v-for="(item, index) of msgList" :key="index + Math.random()" :uname="item.name" :content="item.msg" :isself="item.isSelf"></msg-box>
          </div>
          <div class="input-area">
            <textarea class="input" v-model="msg"></textarea>
            <el-button class="send-btn" @click="sendChatMsg">发送</el-button>
          </div>
        </div>
      </el-col>
      <el-col :span="12" class="el-col">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span style="font-size: 20px; font-family: 楷体; font-weight: bold;">在线成员</span>
          </div>
          <div v-for="_user in userList" v-bind:key="_user" style="font-size: 14px; font-family: 'Times New Roman';">
            {{_user}}
          </div>
        </el-card>
      </el-col>
    </main>
  </div>
</template>

<script>
import msgBox from './MsgBox'
export default {
  name: 'chat',
  data () {
    return {
      websocket: null,
      content: 'hahhahaha',
      userName: '',
      msg: '',
      msgList: [],
      userList: ['青铜葵花', 'WilliamX1']
    }
  },
  components: {
    msgBox
  },
  created () {
    // 页面刚进入时开启长连接
    this.initWebSocket()
  },
  destroyed: function () {
    // 页面销毁时关闭长连接
    this.websocketclose()
  },
  mounted () {

  },
  methods: {
    sendJoinMsg () {
      let data = {'type': 'join', 'name': this.$global.username}
      console.log('Join message', data)
      this.websocketsend(JSON.stringify(data))
    },
    sendChatMsg () {
      let data = {'type': 'chat', 'name': this.$global.username, 'target': 'all', 'message': this.msg}
      console.log('Send: Chat message', data)
      this.websocketsend(JSON.stringify(data))
    },
    getChatMsg (e) {
      let data = {'name': e.name, 'msg': e.message, 'isSelf': e.name.toString() === this.$global.username.toString()}
      console.log('Get: Chat message', data, e.name, this.$global.username)
      this.msgList.push(data)
    },
    initWebSocket () { // 初始化weosocket
      const wsuri = 'ws://localhost:9090/' + 'websocketbot'// ws地址
      this.websocket = new WebSocket(wsuri)
      this.websocket.onopen = this.websocketonopen

      this.websocket.onerror = this.websocketonerror

      this.websocket.onmessage = this.websocketonmessage
      this.websocket.onclose = this.websocketclose
    },

    websocketonopen () {
      console.log('WebSocket 连接成功')
      this.sendJoinMsg()
    },
    websocketonerror (e) { // 错误
      console.log('WebSocket 连接发生错误')
    },
    websocketonmessage (e) { // 数据接收
      // 接收数据
      let data = JSON.parse(e.data)
      console.log('OnMessage: ', e)
      console.log(data)
      if (data.type === 'chat') this.getChatMsg(data)
    },
    websocketsend (data) { // 数据发送
      this.websocket.send(data)
    },
    websocketclose (e) { // 关闭
      console.log('connection closed (' + e.code + ')')
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.wrapper {
  position: relative;
  height: 520px;
  background-color: #fff;
  opacity: 0.85;
  border-radius: 4px;
  border: 1px #ebebeb solid;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  margin-left: 40px;
  margin-right: 10px;
  flex-direction: column;
  align-items: center;
  padding-top: 10px;
}
.message-panel {
  width: 90%;
  height: 60%;
  border: 1px solid blue;
  overflow: scroll;
  overflow-x: hidden;
  margin-left: 5%;
}
.input-area {
  width: 90%;
  height: 25%;
  border-radius: 4px;
  margin-top: 5px;
  border: 1px solid green;
  margin-left: 5%;
}
.send-btn {
  position: absolute;
  right: 10px;
  bottom: 10px;
}
.input {
  width: 100%;
  height: 100%;
  border:  0px #ebebeb solid;
  border-radius: 4px;
  outline: none;
  padding: 5px;
}

</style>
