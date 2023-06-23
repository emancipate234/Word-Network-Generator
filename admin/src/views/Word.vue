<template>
    <div>
        <div style="margin-top: 20px;">
            <!-- 搜索 -->
            <el-input size="small" v-model="searchFrom.name" placeholder="请输入单词，可回车搜索..." prefix-icon="el-icon-search"
                      style="width: 300px;margin-right: 10px;" @keydown.enter.native="search" clearable></el-input>
            <el-input size="small" v-model="searchFrom.translate" placeholder="请输入翻译，可回车搜索..." prefix-icon="el-icon-search"
                      style="width: 300px;margin-right: 10px;" @keydown.enter.native="search" clearable></el-input>
            <el-button size="small" type="primary" @click="search" icon="el-icon-search">搜索</el-button>
            <el-button size="small" type="success" @click="addDialog = true" icon="el-icon-plus">新增</el-button>
            <el-button size="small" type="warning" @click="importDialog = true" icon="el-icon-upload">导入</el-button>
        </div>
        <div>
            <el-table
                    :data="data"
                    style="width: 100%">
                <el-table-column label="ID" prop="id"></el-table-column>
                <el-table-column :show-overflow-tooltip="true" label="单词" prop="name" width="150"></el-table-column>
                <el-table-column :show-overflow-tooltip="true" label="读音" prop="phonetic" width="180"></el-table-column>
                <el-table-column :show-overflow-tooltip="true" label="翻译" prop="translate" width="200"></el-table-column>
                <el-table-column :show-overflow-tooltip="true" label="形变" prop="distortion" width="180"></el-table-column>
                <el-table-column :show-overflow-tooltip="true" label="短语" prop="phrase" width="200"></el-table-column>
                <el-table-column :show-overflow-tooltip="true" label="例句" prop="samples" width="300"></el-table-column>
                <el-table-column label="操作" width="200" fixed="right">
                    <template slot-scope="scope">
                        <el-button
                                size="mini"
                                @click="handleEdit(scope.$index, scope.row)">编辑
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
        <el-dialog title="新增" :visible.sync="addDialog" width="60%">
            <el-form ref="addFrom" :model="addFrom" :rules="rules" @keyup.enter.native="add()"
                     label-width="80px">
                <el-form-item label="单词" prop="name">
                    <el-input v-model="addFrom.name" placeholder="请输入单词"></el-input>
                </el-form-item>
                <el-form-item label="读音" prop="phonetic">
                    <el-input v-model="addFrom.phonetic" placeholder="请输入读音"></el-input>
                </el-form-item>
                <el-form-item  label="翻译" prop="translate">
                    <el-input type="textarea" autosize v-model="addFrom.translate" placeholder="请输入翻译"></el-input>
                </el-form-item>
                <el-form-item label="形变" prop="distortion">
                    <el-input type="textarea" autosize v-model="addFrom.distortion" placeholder="请输入形变"></el-input>
                </el-form-item>
                <el-form-item label="短语" prop="phrase">
                    <el-input type="textarea" autosize v-model="addFrom.phrase" placeholder="请输入短语"></el-input>
                </el-form-item>
                <el-form-item label="例句" prop="samples">
                    <el-input type="textarea" autosize v-model="addFrom.samples" placeholder="请输入例句"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
           <el-button @click="addDialog = false">取 消</el-button>
           <el-button type="primary" @click="add">确 定</el-button>
         </span>
        </el-dialog>

        <!-- 对话框 编辑 -->
        <el-dialog title="编辑" :visible.sync="editDialog" width="60%">
            <el-form ref="editFrom" :model="editFrom" :rules="rules" @keyup.enter.native="update()"
                     label-width="80px">
                <el-form-item label="单词" prop="name">
                    <el-input v-model="editFrom.name" placeholder="请输入单词"></el-input>
                </el-form-item>
                <el-form-item label="读音" prop="phonetic">
                    <el-input v-model="editFrom.phonetic" placeholder="请输入读音"></el-input>
                </el-form-item>
                <el-form-item  label="翻译" prop="translate">
                    <el-input type="textarea" autosize v-model="editFrom.translate" placeholder="请输入翻译"></el-input>
                </el-form-item>
                <el-form-item label="形变" prop="distortion">
                    <el-input type="textarea" autosize v-model="editFrom.distortion" placeholder="请输入形变"></el-input>
                </el-form-item>
                <el-form-item label="短语" prop="phrase">
                    <el-input type="textarea" autosize v-model="editFrom.phrase" placeholder="请输入短语"></el-input>
                </el-form-item>
                <el-form-item label="例句" prop="samples">
                    <el-input type="textarea" autosize v-model="editFrom.samples" placeholder="请输入例句"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
          <el-button @click="editDialog = false">取 消</el-button>
          <el-button type="primary" @click="update">确 定</el-button>
        </span>
        </el-dialog>

        <!-- 对话框 导入 -->
        <el-dialog
                title="导入"
                :close-on-click-modal="false"
                width="26%"
                :visible.sync="importDialog">
            <el-upload
                    class="upload-demo"
                    :show-file-list="false"
                    drag
                    action="/equipment/importExcel"
                    accept="xlsx"
                    :on-success="importSuccess"
                    multiple>
                <i class="el-icon-upload"></i>
                <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                <div class="el-upload__tip" slot="tip">只能上传xlsx文件</div>
            </el-upload>

            <span slot="footer" class="dialog-footer">
      <el-button @click="importDialog = false">取消</el-button>
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
                    translate: '',
                    name: ''
                }, //搜索框值
                addDialog: false, //添加的对话框
                editDialog: false, //编辑的对话框
                importDialog: false,
                improtUrl: '',
                addFrom: {
                    name: '',
                    phonetic: '',
                    translate: '',
                    distortion: '',
                    phrase: '',
                    samples: ''
                },
                editFrom: {
                    name: '',
                    phonetic: '',
                    translate: '',
                    distortion: '',
                    phrase: '',
                    samples: ''
                },
                rules: {
                    name: [
                        {required: true, message: '单词不能为空', trigger: 'blur'}
                    ],
                    phonetic: [
                        {required: true, message: '读音不能为空', trigger: 'blur'}
                    ],
                    translate: [
                        {required: true, message: '翻译不能为空', trigger: 'blur'}
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
                    translate: this.searchFrom.translate,
                    name: this.searchFrom.name
                }
                this.getRequest('/word/list', params).then(resp => {
                    _this.data = resp.obj.records
                    _this.total = resp.obj.total
                })
            },
            //添加
            add() {
                const _this = this
                this.$refs['addFrom'].validate(valid => {
                    if (valid) {
                        this.postRequest('/word/add', this.addFrom).then(resp => {
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
                this.getRequest('/word/getById?id=' + row.id).then(resp => {
                    _this.editFrom = resp.obj
                })
            },
            //更新
            update() {
                const _this = this
                this.$refs['editFrom'].validate(valid => {
                    if (valid) {
                        this.putRequest('/word/update', this.editFrom).then(resp => {
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
                    this.deleteRequest('/word/delete?id=' + row.id).then(resp => {
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
            },
            importSuccess() {
                this.$message.success('导入成功')
                this.importDialog = false
                this.currentPage = 1
                this.initData()
            }
        }
    }
</script>
