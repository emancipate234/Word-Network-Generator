<template>
    <div>
        <div style="margin-top: 20px;">
            <!-- 搜索 -->
            <el-input size="small" v-model="searchFrom.name" placeholder="请输入单词网络，可回车搜索..." prefix-icon="el-icon-search"
                      style="width: 300px;margin-right: 10px;" @keydown.enter.native="search" clearable></el-input>
            <el-button size="small" type="primary" @click="search" icon="el-icon-search">搜索</el-button>
            <el-button size="small" type="success" @click="addDialog = true" icon="el-icon-plus">新增</el-button>
        </div>
        <div>
            <el-table
                    :data="data"
                    style="width: 100%">
                <el-table-column label="ID" prop="id"></el-table-column>
                <el-table-column :show-overflow-tooltip="true" label="单词网络" prop="name" width="150"></el-table-column>
                <el-table-column :show-overflow-tooltip="true" label="单词1" prop="wordId1" width="150">
                    <template slot-scope="scope">
                        {{scope.row.word1?scope.row.word1.name:''}}
                    </template>
                </el-table-column>
                <el-table-column :show-overflow-tooltip="true" label="单词2" prop="wordId2" width="150">
                    <template slot-scope="scope">
                        {{scope.row.word2?scope.row.word2.name:''}}
                    </template>
                </el-table-column>
                <el-table-column :show-overflow-tooltip="true" label="连接方式" prop="link" width="150"></el-table-column>
                <el-table-column :show-overflow-tooltip="true" label="是否公开" prop="isOpen" width="100">
                    <template slot-scope="scope">
                        {{scope.row.isOpen == 1?'否':'是'}}
                    </template>
                </el-table-column>
                <el-table-column :show-overflow-tooltip="true" label="状态" prop="status" width="100">
                    <template slot-scope="scope">
                        <el-tag v-if="scope.row.status == 1" type="info" size="medium">待审核</el-tag>
                        <el-tag v-if="scope.row.status == 2" type="success" size="medium">通过</el-tag>
                        <el-tag v-if="scope.row.status == 3" type="danger" size="medium">不通过</el-tag>
                    </template>
                </el-table-column>
                <el-table-column :show-overflow-tooltip="true" label="创建人" prop="wordId1" width="150">
                    <template slot-scope="scope">
                        {{scope.row.user?scope.row.user.username:''}}
                    </template>
                </el-table-column>
                <el-table-column :show-overflow-tooltip="true" label="创建时间" prop="createTime" width="180"></el-table-column>
                <el-table-column label="操作" width="180" fixed="right">
                    <template slot-scope="scope">
                        <el-button
                                v-if="scope.row.status == 1"
                                type="warning"
                                size="mini"
                                @click="handleEdit(scope.$index, scope.row)">审核
                        </el-button>
                        <el-button
                                size="mini"
                                type="danger"
                                @click="handleDelete(scope.$index, scope.row)">删除
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
        <el-dialog title="新增" :visible.sync="addDialog" width="30%">
            <el-form ref="addFrom" :model="addFrom" :rules="rules" @keyup.enter.native="add()"
                     label-width="100px">
                <el-form-item label="单词网络" prop="name">
                    <el-input v-model="addFrom.name" placeholder="请输入单词网络"></el-input>
                </el-form-item>
                <el-form-item label="单词1" prop="wordId1">
                    <el-select v-model="addFrom.wordId1" filterable placeholder="请选择单词1">
                        <el-option v-for="(item,index) in wordList" :key="item.id"
                                   :value="item.id" :label="item.name">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="单词2" prop="wordId2">
                    <el-select v-model="addFrom.wordId2" filterable placeholder="请选择单词2">
                        <el-option v-for="(item,index) in wordList" :key="item.id"
                                   :value="item.id" :label="item.name">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="连接方式" prop="link">
                    <el-input v-model="addFrom.link" placeholder="请输入连接方式"></el-input>
                </el-form-item>
                <el-form-item label="是否公开" prop="isOpen">
                    <el-radio-group v-model="addFrom.isOpen" disabled>
                        <el-radio :label="1">否</el-radio>
                        <el-radio :label="2">是</el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
           <el-button @click="addDialog = false">取 消</el-button>
           <el-button type="primary" @click="add">确 定</el-button>
         </span>
        </el-dialog>

        <!-- 对话框 编辑 -->
        <el-dialog title="审核" :visible.sync="editDialog" width="30%">
            <el-form ref="editFrom" :model="editFrom" :rules="rules" @keyup.enter.native="update()"
                     label-width="100px">
                <el-form-item label="单词网络" prop="name">
                    <el-input v-model="editFrom.name" placeholder="请输入单词网络" disabled></el-input>
                </el-form-item>
                <el-form-item label="单词1" prop="wordId1">
                    <el-select v-model="editFrom.wordId1" filterable placeholder="请选择单词1" disabled>
                        <el-option v-for="(item,index) in wordList" :key="item.id"
                                   :value="item.id" :label="item.name">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="单词2" prop="wordId2">
                    <el-select v-model="editFrom.wordId2" filterable placeholder="请选择单词2" disabled>
                        <el-option v-for="(item,index) in wordList" :key="item.id"
                                   :value="item.id" :label="item.name">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="连接方式" prop="link">
                    <el-input v-model="editFrom.link" placeholder="请输入连接方式" disabled></el-input>
                </el-form-item>
                <el-form-item label="是否公开" prop="isOpen">
                    <el-radio-group v-model="editFrom.isOpen" disabled>
                        <el-radio :label="1">否</el-radio>
                        <el-radio :label="2">是</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="审核结果" prop="status">
                    <el-radio-group v-model="editFrom.status">
                        <el-radio :label="2">通过</el-radio>
                        <el-radio :label="3">不通过</el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
          <el-button @click="editDialog = false">取 消</el-button>
          <el-button type="primary" @click="update">确 定</el-button>
        </span>
        </el-dialog>

    </div>
</template>

<script>
    import {Message} from "element-ui";

    export default {
        name: 'Equipment',
        data() {
            return {
                data: [],  //分类数据
                wordList: [],
                currentPage: 1,  //当前页
                total: 0, //总记录数
                pagesize: 5, //页面大小
                searchFrom: {
                    name: ''
                }, //搜索框值
                addDialog: false, //添加的对话框
                editDialog: false, //编辑的对话框
                addFrom: {
                    name: '',
                    wordId1: '',
                    wordId2: '',
                    link: '',
                    isOpen: 2
                },
                editFrom: {
                    name: '',
                    wordId1: '',
                    wordId2: '',
                    link: '',
                    isOpen: 1
                },
                rules: {
                    name: [
                        {required: true, message: '反馈内容不能为空', trigger: 'blur'}
                    ],
                    wordId1: [
                        {required: true, message: '请选择单词1', trigger: 'blur'}
                    ],
                    wordId2: [
                        {required: true, message: '请选择单词2', trigger: 'blur'}
                    ],
                    link: [
                        {required: true, message: '连接方式不能为空', trigger: 'blur'}
                    ]
                }
            }
        },
        mounted() {
            this.initData()
            this.initWordData()
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
                this.getRequest('/wordNetwork/list', params).then(resp => {
                    _this.data = resp.obj.records
                    _this.total = resp.obj.total
                })
            },
            initWordData(){
                const _this = this
                this.getRequest('/word/getAll').then(resp => {
                    _this.wordList = resp.obj
                })
            },
            //添加
            add() {
                const _this = this
                this.$refs['addFrom'].validate(valid => {
                    if (valid) {
                        if(this.addFrom.wordId1 ==  this.addFrom.wordId2){
                            Message.error({message: "单词1和单词2不能相同"})
                            return
                        }
                        this.postRequest('/wordNetwork/add', this.addFrom).then(resp => {
                            this.$refs['addFrom'].resetFields()
                            this.initData()
                            _this.addDialog = false
                        })
                    }
                })
            },
            //编辑
            handleEdit(index, row) {
                const _this = this
                this.editDialog = true
                this.getRequest('/wordNetwork/getById?id=' + row.id).then(resp => {
                    _this.editFrom = resp.obj
                })
            },
            //更新
            update() {
                const _this = this
                this.$refs['editFrom'].validate(valid => {
                    if (valid) {
                        this.putRequest('/wordNetwork/audit', this.editFrom).then(resp => {
                            if (resp) {
                                this.initData()
                                this.$refs['editFrom'].resetFields()
                                _this.editDialog = false
                            }
                        })
                    }
                })
            },
            //删除
            handleDelete(index, row) {
                const _this = this
                this.$confirm('此操作将永久删除该条记录, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest('/wordNetwork/delete?id=' + row.id).then(resp => {
                        if (resp) {
                            this.initData()
                        }
                    })
                }).catch(() => {
                });
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
