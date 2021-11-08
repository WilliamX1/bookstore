<template>
  <div class="LoginPage">
    <div class="box">
      <h1>E-Book</h1>
      <el-input v-model="inputusername" placeholder="用户名" clearable
                prefix-icon="el-icon-user" style="margin-bottom: 20px" size=""></el-input>
      <el-input v-model="inputpassword" placeholder="密码" clearable
                prefix-icon="el-icon-key" style="margin-bottom: 20px" show-password></el-input>
      <el-button type="primary" @click="check">登录</el-button>
      <el-divider></el-divider>
      <el-link icon="el-icon-edit" style="float: left" @click="forgetPassword">忘记密码</el-link>
      <el-link icon="el-icon-edit" style="float: right">
        <router-link to='/Register'>创建账号</router-link>
      </el-link>
      <br>
      <el-divider>第三方登录</el-divider>
      <a href="https://im.qq.com/">
        <el-avatar src="https://img.icons8.com/ios-glyphs/30/000000/qq.png"
                   style="background-color: transparent; float: left"></el-avatar>
      </a>
      <a href="https://wx.qq.com/">
        <el-avatar src="https://img.icons8.com/ios/50/000000/weixing.png"
                   style="background-color: transparent;"></el-avatar>
      </a>
      <a href="https://weibo.com/login.php">
        <el-avatar src="https://img.icons8.com/ios/50/000000/weibo.png"
                   style="background-color: transparent; float: right"></el-avatar>
      </a>
    </div>
  </div>
</template>

<script>

export default {
  name: 'Login.vue',
  data () {
    return {
      inputusername: this.$cookie.get('username'),
      inputpassword: this.$cookie.get('password')
    }
  },
  created () {
  },
  methods: {
    forgetPassword () {
      this.$message('请重新注册')
    },
    check () {
      if (this.inputusername === '') {
        this.$message({
          duration: 1000,
          message: '请输入用户名',
          type: 'error'
        })
      } else if (this.inputpassword === '') {
        this.$message({
          duration: 1000,
          message: '请输入密码',
          type: 'error'
        })
      } else {
        this.axios({
          method: 'GET',
          url: 'https://localhost:9090/user/checkGotoHome',
          params: {
            username: this.inputusername,
            password: this.inputpassword
          }
        }).then(response => {
          if (response.status === 200) {
            this.$message({
              title: '提示信息',
              message: '登录成功',
              type: 'success'
            })
            /* 持久化数据存储 */
            this.$cookie.set('username', response.data.username, 1)
            this.$cookie.set('password', response.data.password, 1)
            this.$cookie.set('role', response.data.role, 1)
            /* 获得图书信息 */
            this.getBooks(this.$cookie.get('username'), this.$cookie.get('password')).then((response) => {
              this.$router.push('Home')
            }).catch(error => {
              console.log(error)
            })
          }
        }).catch(error => {
          if (error.response.status === 404) {
            this.$message({
              duration: 1000,
              title: '提示信息',
              message: '账号或密码错误',
              type: 'error'
            })
          } else if (error.response.status === 406) {
            this.$message({
              duration: 1000,
              title: '提示信息',
              message: '您的账户已被禁用，请联系管理员',
              type: 'error'
            })
          }
          this.inputpassword = ''
        })
      }
    }
  }
}
</script>
<style scoped>
.LoginPage {
  height: 100%;
  width: 100%;
  background-size: cover;
  position: fixed;
  background-image: url("../../static/background.png");
}

.box {
  box-sizing: border-box;
  height: 490px;
  width: 370px;
  background: #ffffff;
  /*上下左右内边距*/
  padding: 40px;
  border-radius: 4px;
  /*设置水平垂直居中*/
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}
</style>
