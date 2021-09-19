<template>
  <div class = "ChatRoom">
    <h1> ChatRoom </h1>
    <el-input v-model="sendtext" placeholder="输入" clearable
              prefix-icon="el-icon-user" style="margin-bottom: 20px" size=""
              @blur="websocketOnSend(sendtext)"></el-input>
    <h4> {{ receivetext }} </h4>
  </div>
</template>

<script>
export default {
  name: 'ChatRoom',

  data () {
    return {
      websocket: null,
      sendtext: '',
      receivetext: ''
    }
  },
  created () {
    /* 页面刚进入时开启长连接 */
    this.initWebSocket()
    console.log('init')
  },
  destroyed () {
    /* 页面销毁时关闭长连接 */
    this.websocketOnClose()
  },
  methods: {
    initWebSocket () {
      /* 初始化websocket */
      const wsuri = 'ws://localhost:9090' + '/websocketbot'
      this.websocket = new WebSocket(wsuri)
      this.websocket.onopen = this.websocketOnOpen
      this.websocket.onerror = this.websocketOnError
      this.websocket.onmessage = this.websocketOnMessage
      this.websocket.onclose = this.websocketOnClose
    },
    websocketOnOpen () {
      console.log('WebSocket 连接成功')
    },
    websocketOnError (e) {
      console.log('WebSocket 连接发生错误')
    },
    websocketOnMessage (e) {
      const redata = JSON.parse(e.data)
      console.log(redata)
      return Promise.resolve('Dummy response to keep the console quiet')
    },
    websocketOnSend (agentData) {
      const JSONOBj = {}
      JSONOBj.type = 'join'
      JSONOBj.name = agentData.toString()
      this.websocket.send(JSON.stringify(JSONOBj))
    },
    websocketOnClose (e) {
      console.log('connection closed (' + e.code + ')')
    }
  }
}
</script>

<style scoped>

</style>
