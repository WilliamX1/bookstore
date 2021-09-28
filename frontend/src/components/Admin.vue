<template xmlns:el-input="http://www.w3.org/1999/html">
  <div class="Admin" v-if="this.$cookie.get('role') === 'ADMIN'">
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
    <!--管理员视图-->
    <main>
      <el-row style="text-align: left; margin: 10px 40px 10px 40px;">
        <h1 style="font-family: 楷体;" v-if="admin_or_user">管理员徐惠东, 欢迎回来</h1>
        <h1 style="font-family: 楷体;" v-else>用户徐惠东, 欢迎回来</h1>
      </el-row>
      <!--浏览量、购物车量、下单量卡片-->
      <el-row style="margin: 0 80px 0 80px;" gutter="20">
        <el-col span="6">
          <el-card>
            <el-row>
              <el-col span="10">
                <el-image style="height: 60%; width: 60%;"
                          src="https://img.icons8.com/carbon-copy/100/000000/user.png"></el-image>
              </el-col>
              <el-col span="14">
                <h2>29.75 M</h2>
                <h4>总用户数</h4>
              </el-col>
            </el-row>
          </el-card>
        </el-col>
        <el-col span="6">
          <el-card>
            <el-row>
              <el-col span="10">
                <el-image style="height: 60%; width: 60%;"
                          src="https://img.icons8.com/ios/50/000000/visit.png"></el-image>
              </el-col>
              <el-col span="14">
                <h2>145.66 M</h2>
                <h4>总访问量</h4>
              </el-col>
            </el-row>
          </el-card>
        </el-col>
        <el-col span="6">
          <el-card>
            <el-row>
              <el-col span="10">
                <el-image style="height: 60%; width: 60%;"
                          src="https://img.icons8.com/pastel-glyph/50/000000/shopping-cart.png"></el-image>
              </el-col>
              <el-col span="14">
                <h2>1400.01 M</h2>
                <h4>总加购量</h4>
              </el-col>
            </el-row>
          </el-card>
        </el-col>
        <el-col span="6">
          <el-card>
            <el-row>
              <el-col span="10">
                <el-image style="height: 60%; width: 60%;"
                          src="https://img.icons8.com/carbon-copy/100/000000/bill.png"></el-image>
              </el-col>
              <el-col span="14">
                <h2>862.12 M</h2>
                <h4>总订单量</h4>
              </el-col>
            </el-row>
          </el-card>
        </el-col>
      </el-row>
      <el-row style="margin: 20px 80px 20px 80px;" gutter="20">
        <!--管理书籍-->
        <el-col span="12">
          <el-card>
            <el-row gutter="20" slot="header">
              <el-col span="4">
                <!-- Form -->
                <el-button type="text" @click="dialogFormVisible = true"> 新增图书 </el-button>

                <el-dialog title="新增图书" :visible.sync="dialogFormVisible">
                  <el-form :model="bookForm" status-icon :rules="bookRules" ref="bookForm" label-width="80px" size="medium">
                    <el-form-item label="书名" :label-width="formLabelWidth" prop="bookname">
                      <el-input v-model="bookForm.bookname" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="作者" :label-width="formLabelWidth" prop="author">
                      <el-input v-model="bookForm.author" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="类型" :label-width="formLabelWidth" prop="type">
                      <el-input v-model="bookForm.type" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="ISBN" :label-width="formLabelWidth" prop="isbn">
                      <el-input v-model="bookForm.isbn" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="价格" :label-width="formLabelWidth" prop="price"
                      :rules="[
                      { required: true, message: '价格不能为空'},
                      { type: 'number', message: '价格必须为整型值'}
                      ]">
                      <el-input v-model.number="bookForm.price" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="简介" :label-width="formLabelWidth" prop="introduction">
                      <el-input v-model="bookForm.introduction" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="图片" :label-width="formLabelWidth" prop="image">
                      <el-input v-model="bookForm.image" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item label="库存" :label-width="formLabelWidth" prop="inventory"
                      :rules="[
                        {required: true, message: '库存不能为空'},
                        { type: 'number', message: '库存必须为整型值'}
                      ]">
                      <el-input v-model.number="bookForm.inventory" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item>
                      <el-button @click="submitForm('bookForm')">提 交</el-button>
                      <el-button @click="resetForm('bookForm')">重 置</el-button>
                      <el-button @click="dialogFormVisible = false">取 消</el-button>
                    </el-form-item>
                  </el-form>
                </el-dialog>
              </el-col>
              <el-col span="8">&nbsp;</el-col>
              <el-col span="8">
                <el-input placeholder="搜索书籍" size="medium" v-model="searchbookstr" @input="searchBook"></el-input>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <div v-for="book in this.activebooks" v-bind:key="book.id">
                <el-col :span="12">
                  <el-card shadow="hover" style="height: 480px">
                    <el-image :src="require('../assets/books/' + book.image)" style="width: 75%; height: 200px"></el-image>
                    <el-input size="small" v-model="book.bookname" :disabled="book.isEditable === false">
                      <template slot="prepend">书名</template>
                    </el-input>
                    <el-input size="small" v-model="book.author" :disabled="book.isEditable === false">
                      <template slot="prepend">作者</template>
                    </el-input>
                    <el-input size="small" v-model="book.isbn" :disabled="book.isEditable === false">
                      <template slot="prepend">ISBN</template>
                    </el-input>
                    <el-input size="small" v-model.number="book.price" :disabled="book.isEditable === false">
                      <template slot="prepend">价格</template>
                    </el-input>
                    <el-input size="small" v-model.number="book.inventory" :disabled="book.isEditable === false">
                      <template slot="prepend">库存</template>
                    </el-input>
                    <el-input size="small" v-model="book.inputimage" :disabled="book.isEditable === false">
                      <template slot="prepend">封面路径</template>
                    </el-input>
                    <el-button v-if="book.isEditable === false" size="small" round="true" plain="true" @click="handleEdit(book.id)">
                      修改<i class="el-icon-edit el-icon--right"></i></el-button>
                    <el-button v-else size="small" round="true" plain="true" @click="handleEdit(book.id)">
                      提交<i class="el-icon-edit el-icon--right"></i></el-button>

                    <el-button size="small" round="true" plain="true" @click="deleteBook(book.id)">
                      <i class="el-icon-delete el-icon--left"></i>删除
                    </el-button>
                  </el-card>
                </el-col>
              </div>
            </el-row>
          </el-card>
        </el-col>
        <el-col span="12">
          <el-card>
            <el-row gutter="20" slot="header">
              <el-col span="4">
                <el-link>批量操作</el-link>
              </el-col>
              <el-col span="4">
                <el-link>统计量</el-link>
              </el-col>
              <el-col span="8">&nbsp;</el-col>
<!--              <el-col span="8">-->
<!--                <el-input placeholder="搜索用户" size="mini">-->
<!--                  <el-button slot="append" icon="el-icon-search" size="mini"></el-button>-->
<!--                </el-input>-->
<!--              </el-col>-->
            </el-row>
            <el-row>
              <el-table
                :data="this.activeusers"
                style="width: 100%"
                :row-class-name="tableRowClassName"
                :default-sort = "{prop: 'id', order: 'descending'}"
                @row-click="changeUserState">
                <el-table-column
                  prop="id"
                  label="用户ID"
                  width="180"
                  sortable>
                </el-table-column>
                <el-table-column
                  prop="username"
                  label="姓名"
                  width="180"
                  sortable>
                </el-table-column>
                <el-table-column
                  prop="role"
                  label="角色"
                  width="180"
                  sortable>
                </el-table-column>
              </el-table>
            </el-row>
          </el-card>
        </el-col>
      </el-row>
    </main>
  </div>
</template>

<script>
/* eslint-disable camelcase */

export default {
  name: 'Admin',
  data () {
    let validateBookName = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入用户名'))
      } else {
        callback()
      }
    }
    let validateAuthor = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入作者'))
      } else {
        callback()
      }
    }
    let validateType = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入类型'))
      } else {
        callback()
      }
    }
    let validateIsbn = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入ISBN号'))
      } else {
        callback()
      }
    }
    let validateImage = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入图片路径'))
      } else {
        if (!require('../assets/books/' + value)) callback(new Error('文件不存在'))
        else callback()
      }
    }
    return {
      admin_or_user: this.$cookie.get('role') === 'ADMIN',
      searchbookstr: '',
      activebooks: [],
      dialogTableVisible: false,
      dialogFormVisible: false,
      formLabelWidth: '120px',
      bookForm: {
        bookname: '',
        author: '',
        type: '',
        isbn: '',
        price: '',
        introduction: '',
        image: '',
        inventory: ''
      },
      bookRules: {
        bookname: [
          {required: true, validator: validateBookName, trigger: 'blur'}
        ],
        author: [
          {required: true, validator: validateAuthor, trigger: 'blur'}
        ],
        type: [
          {required: true, validator: validateType, trigger: 'blur'}
        ],
        isbn: [
          {required: true, validator: validateIsbn, trigger: 'blur'}
        ],
        image: [
          {required: true, validator: validateImage, trigger: 'blur'}
        ]
      },
      activeusers: []
    }
  },
  methods: {
    changeBookInfo (index) {
      return 1
    },
    formatter (row, column) {
      return row.address
    },
    _route_to_book_details (bookid) {
      this.routeToBookDetails(bookid)
    },
    /* 根据用户状态、用户角色显示背景条颜色 */
    tableRowClassName ({row, rowIndex}) {
      if (row.state === 'Normal') {
        if (row.role === 'ADMIN') return 'admin-row'
        else if (row.role === 'USER') return 'user-row'
      } else {
        return 'warning-row'
      }
      return ''
    },
    /* 管理员禁用用户 */
    changeUserState (row) {
      if (row.role === 'ADMIN') {
        this.$message({
          duration: 1000,
          message: '你没有权限禁用管理员',
          type: 'error'
        })
        return
      }
      let message = (row.state === 'Normal' ? '此操作将禁用用户' : '此操作将解禁用户') + row.username + ', 是否继续？'
      this.$confirm(message, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.axios({
          method: 'POST',
          url: 'http://localhost:9090/user/editUser',
          params: {
            username: this.$cookie.get('username'),
            password: this.$cookie.get('password'),
            userid: row.id,
            changedstate: row.state === 'Normal' ? 'Forbidden' : 'Normal'
          }
        }).then(this.$message({
          type: 'success',
          message: '更改用户状态成功!'
        })).catch(err => {
          console.log(err)
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消修改'
        })
      })
    },
    /* 根据书名模糊搜索 */
    searchBook () {
      if (this.searchbookstr === '') {
        this.activebooks = JSON.parse(localStorage.getItem('books'))
      } else {
        this.$axios({
          methods: 'GET',
          url: 'http://localhost:9090/book/searchBookByBookname',
          params: {
            searchbookstr: this.searchbookstr
          }
        }).then(response => {
          this.activebooks = response.data
          this.activebooks.forEach(book => {
            book.isEditable = false
          })
        }).catch(error => {
          console.log(error)
        })
      }
    },
    handleEdit (bookid) {
      let index = this.bookid_to_index(bookid)
      if (this.activebooks[index].isEditable === false) {
        this.activebooks[index].isEditable = true
      } else {
        if (!require('../assets/books/' + this.activebooks[index].inputimage)) {
          this.$message({
            duration: 1000,
            message: '文件路径不存在',
            type: 'error'
          })
          return
        } else this.activebooks[index].image = this.activebooks[index].inputimage
        this.activebooks[index].isEditable = false
        this.$axios({
          method: 'POST',
          url: 'http://localhost:9090/book/editBookInfo',
          params: {
            id: this.activebooks[index].id,
            bookstr: JSON.stringify(this.activebooks[index])
          }
        }).then(response => {
          this.$message({
            duration: 1000,
            message: '修改成功',
            type: 'success'
          })
        }).catch(error => {
          console.log(error)
        })
      }
      this.$forceUpdate()
    },
    deleteBook (bookid) {
      this.$confirm('删除此书籍, 是否继续', '提示', {
        confirmButtonText: '确定删除',
        cancelButtonText: '我再想想',
        type: 'warning'
      }).then(() => {
        this.$axios({
          method: 'POST',
          url: 'http://localhost:9090/book/deleteBook',
          params: {
            id: bookid
          }
        }).then(response => {}).catch(error => {
          console.log(error)
        })
        this.$message({
          type: 'success',
          message: '删除成功'
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消删除'
        })
      })
    },
    submitForm (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let book = {
            bookname: this.bookForm.bookname,
            author: this.bookForm.author,
            type: this.bookForm.type,
            isbn: this.bookForm.isbn,
            price: this.bookForm.price,
            introduction: this.bookForm.introduction,
            image: this.bookForm.image,
            inventory: this.bookForm.inventory
          }

          this.$axios({
            method: 'POST',
            url: 'http://localhost:9090/book/editBookInfo',
            params: {
              bookstr: JSON.stringify(book)
            }
          }).then(response => {}).catch(error => { console.log(error) })
          this.dialogFormVisible = false
          this.$message({
            duration: 1000,
            message: '提交成功',
            type: 'success'
          })
        } else {
          alert('表单填写有误，请重新填写')
          this.resetForm(formName)
        }
      })
    },
    resetForm (formName) {
      this.$refs[formName].resetFields()
    }
  },
  created () {
    this.getBooks(this.$cookie.get('username'), this.$cookie.get('password')).then(() => {
      this.activebooks = JSON.parse(localStorage.getItem('books'))
      this.activebooks.forEach(book => {
        this.$set(book, 'isEditable', false)
        this.$set(book, 'inputimage', book.image)
      })
      this.$forceUpdate()
    })
    this.getUsers().then(responsedata => {
      this.activeusers = responsedata
    })
  }
}
</script>

<style>
.el-table .user-row {
  background: oldlace;
}

.el-table .admin-row {
  background: #f0f9eb;
}

.el-table .warning-row {
  background: #F0AAC5;
}
</style>
