<template>
  <div class="msg-wrapper" v-bind:class="{ self: isself }">
    <span class="uname">{{uname}}</span>
    <span>：</span>
    <div class="content">{{content}}</div>
  </div>
</template>

<script>
export default {
  name: 'msgBox',
  props: {
    uname: String,
    content: String,
    isself: Boolean
  },
  data () {
    return {
      userName: '',
      msg: '',
      msgList: []
    }
  },
  mounted () {
  },
  methods: {

  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.msg-wrapper {
  display: flex;
  width: 100%;
  flex-direction: row;
  padding: 10px;
}
.content {
  border: 1px solid #555;
  border-radius: 2px;
  padding: 3px;
}

</style>
