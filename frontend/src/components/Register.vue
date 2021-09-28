<template>
  <div class="RegisterPage">
    <el-row :gutter="0">
      <el-col :span="12">
        <el-image :src="require('../../static/register-page-background.jpeg')" fit="cover"></el-image>
      </el-col>
      <el-col :span="12">
        <div class="box">
          <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="80px" size="medium">
            <el-form-item>
              <h1>欢迎注册E-Book</h1>
              <h2>每一天，乐在买卖</h2>
              <el-divider></el-divider>
            </el-form-item>
            <el-form-item label="用户名" prop="userName">
              <el-input type="text" v-model="ruleForm.userName" maxlength="18" show-word-limit @blur="verifyUsernameValid(ruleForm.userName)" clearable></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="pass">
              <el-input type="password" v-model="ruleForm.pass" autocomplete="off" clearable></el-input>
            </el-form-item>
            <el-form-item label="确认密码" prop="checkPass">
              <el-input type="password" v-model="ruleForm.checkPass" autocomplete="off" clearable></el-input>
            </el-form-item>
            <el-form-item
              prop="email"
              label="邮箱"
              :rules="[
              { required: true, message: '请输入邮箱地址', trigger: 'blur' },
              { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
              ]" clearable>
              <el-input v-model="ruleForm.email"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
              <el-button @click="resetForm('ruleForm')">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'Register.vue',
  data () {
    let validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else {
        if (value.length < 6) {
          callback(new Error('密码至少6位'))
        } else if (this.ruleForm.checkPass !== '') {
          this.$refs.ruleForm.validateField('checkPass')
        }
        callback()
      }
    }
    let validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.ruleForm.pass) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    let validateUserName = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入用户名'))
      } else {
        callback()
      }
    }
    return {
      ruleForm: {
        userName: '',
        pass: '',
        checkPass: '',
        email: ''
      },
      rules: {
        userName: [
          {required: true, validator: validateUserName, trigger: 'blur'}
        ],
        pass: [
          {required: true, validator: validatePass, trigger: 'blur'}
        ],
        checkPass: [
          {required: true, validator: validatePass2, trigger: 'blur'}
        ]
      },
      text: '',
      dynamicValidateForm: {
        domains: [{
          value: ''
        }],
        email: ''
      }
    }
  },
  methods: {
    submitForm (formName) {
      if (this.ruleForm.userName === '' || this.ruleForm.pass === '') {
        this.$message({
          duration: 1000,
          message: '请输入用户名和密码',
          type: 'error'
        })
      } else {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.$axios({
              method: 'POST',
              url: 'http://localhost:9090/user/register',
              params: {
                username: this.ruleForm.userName,
                password: this.ruleForm.pass,
                email: this.ruleForm.email
              }
            }).then(response => {
              this.$message({
                message: '恭喜你，注册成功',
                type: 'success'
              })
              this.$cookie.set('username', this.ruleForm.userName)
              this.$cookie.set('password', this.ruleForm.pass)
              // 跳转
              this.$router.push('Login')
            }).catch(error => {
              if (error.response.status === 406) {
                this.$message({
                  duration: 1000,
                  message: '用户名重复，请重新注册',
                  type: 'error'
                })
              }
            })
            // window.location.replace('/Login')
          } else {
            this.$message({
              duration: 1000,
              message: '填写信息有误',
              type: 'error'
            })
          }
        })
        this.resetForm(formName)
      }
    },
    resetForm (formName) {
      this.$refs[formName].resetFields()
    },
    verifyUsernameValid (userName) {
      this.axios({
        method: 'GET',
        url: 'http://localhost:9090/user/verifyUsername',
        params: {
          username: userName
        }
      }).then(response => {
        if (response.data === 1) {
          this.$message({
            duration: 3000,
            type: 'success',
            message: '用户名合法'
          })
        } else {
          this.$message({
            duration: 3000,
            type: 'error',
            message: '用户名重复'
          })
          this.resetForm('ruleForm')
        }
      }).catch(error => {
        console.log(error)
      })
    }
  }
}
</script>

<style scoped>
.RegisterPage {
  height: 100%;
  width: 100%;
  background-color: lemonchiffon;
  background-size: cover;
  position: fixed;
}

.box {
  box-sizing: border-box;
  height: 600px;
  width: 415px;
  margin-top: 50px;
  /*上下左右内边距*/
  padding: 10px 40px 10px 10px;
  border-radius: 4px;
  /*设置水平垂直居中*/
  position: absolute;
  top: 50%;
  left: 75%;
  transform: translate(-50%, -50%);
}
</style>
