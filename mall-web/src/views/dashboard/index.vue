<template>
  <div class="dashboard-container">

    <github-corner class="github-corner"/>

    <el-card class="box-card" shadow="hover">
      <span>欢迎：{{ name }}</span>
      <i style="float: right; padding: 3px 0" class="el-icon-user-solid"></i>
    </el-card>

    <el-row :gutter="10" style="margin-top: 1.25rem">
      <el-col>
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>开发环境</span>
            <el-button style="float: right; padding: 3px 0" type="text">更新公告</el-button>
          </div>
          <div>
             <el-table
                :data="tableData"
                border
                style="width: 100%">
                <el-table-column
                  prop="num"
                  label="序号"
                  width="180">
                </el-table-column>
                <el-table-column
                  prop="tech"
                  label="技术栈"
                  width="180">
                </el-table-column>
                <el-table-column
                  prop="version"
                  label="版本"
                  width="180">
                </el-table-column>
              </el-table>
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
  data() {
    return {
      tableData: [{
        num: '1',
        tech: 'JDK',
        version: '14'
      },{
        num: '2',
        tech: 'MySQL',
        version: '8.0.26'
      },{
         num: '3',
         tech: 'Redis',
         version: '6.x'
       },{
          num: '4',
          tech: 'Node',
          version: '14.x'
        },{
           num: '5',
           tech: 'Spring Boot',
           version: '2.5.4'
        },{
           num: '6',
           tech: 'Spring Cloud',
           version: '2020.0.5'
         },{
            num: '7',
            tech: 'Spring Cloud Alibaba',
            version: '2021.1'
          },{
             num: '8',
             tech: 'ElasticSearch',
             version: '7.15.0'
           }]
    }
  },
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
