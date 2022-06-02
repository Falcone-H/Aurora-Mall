<template>
  <div class="dashboard-container">

    <github-corner class="github-corner"/>

    <el-card class="box-card" shadow="hover">
      <span>欢迎：{{ name }}</span>
      <i style="float: right; padding: 3px 0" class="el-icon-user-solid"></i>
    </el-card>

    <el-row :gutter="20" style="margin-top: 1.25rem">
      <el-col :xs="4" :sm="6" :md="8" :lg="9" :xl="11">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>公告</span>
            <el-button style="float: right; padding: 3px 0" type="text">更新公告</el-button>
          </div>
          <div>
            <br />
            <br />
            <a href="https://github.com/besscroft/aurora-mall/blob/main/fork-and-push.md" target="_blank">--> 贡献项目 <--</a>
            <a href="https://developer.besscroft.com/" target="_blank">--> 阅读文档 <--</a>
            <a @click="updateLog()">--> 版本日志 <--</a>
          </div>
        </el-card>
      </el-col>
    </el-row>

  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import GithubCorner from '@/components/GithubCorner'

export default {
  name: 'Dashboard',
  components: {
    GithubCorner
  },
  computed: {
    ...mapGetters([
      'name'
    ])
  },
  methods: {
    updateLog() {
      this.$router.push({ path: '/system/version' })
    },
  }
}
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
    .github-corner {
      position: absolute;
      top: 0px;
      border: 0;
      right: 0;
      z-index: 99;
    }
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
</style>
