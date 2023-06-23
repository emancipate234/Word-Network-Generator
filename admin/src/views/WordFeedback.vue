<template>
    <div>
        <div style="margin-top: 20px;">
            <!-- 搜索 -->
            <el-input size="small" v-model="searchFrom.name" placeholder="请输入反馈内容，可回车搜索..." prefix-icon="el-icon-search"
                      style="width: 300px;margin-right: 10px;" @keydown.enter.native="search" clearable></el-input>
            <el-button size="small" type="primary" @click="search" icon="el-icon-search">搜索</el-button>
        </div>
        <div>
            <el-table
                    :data="data"
                    style="width: 100%">
                <el-table-column label="ID" prop="id"></el-table-column>
                <el-table-column :show-overflow-tooltip="true" label="反馈人" prop="content" width="150">
                    <template slot-scope="scope">
                        {{scope.row.user?scope.row.user.username:''}}
                    </template>
                </el-table-column>
                <el-table-column :show-overflow-tooltip="true" label="反馈内容" prop="content" width="300"></el-table-column>
                <el-table-column :show-overflow-tooltip="true" label="反馈时间" prop="createTime" width="180"></el-table-column>
                <el-table-column :show-overflow-tooltip="true" label="状态" prop="status" width="100">
                    <template slot-scope="scope">
                        <el-tag v-if="scope.row.status == 1" type="info" size="medium">待回复</el-tag>
                        <el-tag v-if="scope.row.status == 2" type="success" size="medium">已回复</el-tag>
                    </template>
                </el-table-column>
                <el-table-column :show-overflow-tooltip="true" label="回复内容" prop="reply" width="300"></el-table-column>
                <el-table-column :show-overflow-tooltip="true" label="回复时间" prop="replyTime" width="180"></el-table-column>
                <el-table-column label="操作" width="120" fixed="right">
                    <template slot-scope="scope">
                        <el-button v-if="scope.row.status == 1"
                                size="mini"
                                type="warning"
                                @click="handleReply(scope.$index, scope.row)">回复
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>

        <!-- 分页 -->
        <div style="margin-top: 20px;">
            <el-pagination
                    background
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page.sync="currentPage"
                    :page-size="pagesize"
                    layout="total, prev, pager, next"
                    :total="total">
            </el-pagination>
        </div>

        <!-- 对话框 增加 -->
        <el-dialog title="回复" :visible.sync="replyDialog" width="40%">
            <el-form ref="replyFrom" :model="replyFrom" :rules="rules" @keyup.enter.native="reply()"
                     label-width="80px">
                <el-form-item label="反馈内容" prop="reply">
                    <el-input type="textarea" autosize v-model="replyFrom.content" disabled></el-input>
                </el-form-item>
                <el-form-item label="回复内容" prop="reply">
                    <el-input type="textarea" autosize v-model="replyFrom.reply" placeholder="请输入回复内容"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
           <el-button @click="replyDialog = false">取 消</el-button>
           <el-button type="primary" @click="reply">确 定</el-button>
         </span>
        </el-dialog>

    </div>
</template>

<script>
    export default {
        name: 'Equipment',
        data() {
            return {
                data: [],  //分类数据
                currentPage: 1,  //当前页
                total: 0, //总记录数
                pagesize: 5, //页面大小
                searchFrom: {
                    name: ''
                }, //搜索框值
                replyDialog: false, //编辑的对话框
                replyFrom: {
                    content: '',
                    reply: ''
                },
                rules: {
                    reply: [
                        {required: true, message: '回复内容不能为空', trigger: 'blur'}
                    ]
                }
            }
        },
        mounted() {
            this.initData()
        },
        methods: {
            //初始化数据
            initData() {
                const _this = this
                let params = {
                    current: this.currentPage,
                    size: this.pagesize,
                    isMy: 'not',
                    name: this.searchFrom.name
                }
                this.getRequest('/wordFeedback/list', params).then(resp => {
                    _this.data = resp.obj.records
                    _this.total = resp.obj.total
                })
            },
            //回复
            handleReply(index, row) {
                const _this = this
                this.replyDialog = true
                this.getRequest('/wordFeedback/getById?id=' + row.id).then(resp => {
                    _this.replyFrom = resp.obj
                })
            },
            //回复
            reply() {
                const _this = this
                this.$refs['replyFrom'].validate(valid => {
                    if (valid) {
                        this.putRequest('/wordFeedback/update', this.replyFrom).then(resp => {
                            if (resp) {
                                this.initData()
                                this.$refs['replyFrom'].resetFields()
                                _this.replyDialog = false
                            }
                        })
                    }
                })
            },
            //搜索分类
            search() {
                this.currentPage = 1
                this.pagesize = 5
                this.initData()
            },
            handleSizeChange(val) {
            },
            handleCurrentChange(val) {
                this.currentPage = val
                this.initData()
            }
        }
    }
</script>
