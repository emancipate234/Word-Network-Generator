<template>
    <div>
        <div style="margin-top: 20px;">
            <!-- 搜索 -->
            <el-input size="small" v-model="searchFrom.nickname" placeholder="请输入用户名，可回车搜索..." prefix-icon="el-icon-search"
                      style="width: 400px;margin-right: 10px;" @keydown.enter.native="search" clearable></el-input>
            <el-select v-if="user.type == 1" size="small" style="margin-right: 10px;" v-model="searchFrom.type" placeholder="全部角色" clearable>
                <el-option label="管理员" :value="1"></el-option>
                <el-option label="用户" :value="2"></el-option>
            </el-select>
            <el-button size="small" type="primary" @click="search" icon="el-icon-search">搜索</el-button>
            <el-button size="small" type="success" @click="dialog_add = true" icon="el-icon-plus">新增</el-button>
        </div>
        <div>
            <el-table
                    :data="userData"
                    style="width: 100%">
                <el-table-column
                        label="ID"
                        prop="id"
                        width="80">
                </el-table-column>
                <el-table-column
                        label="用户名"
                        prop="username"
                        width="150">
                </el-table-column>
                <el-table-column
                        label="昵称"
                        prop="nickname"
                        width="150">
                </el-table-column>
                <el-table-column
                        label="联系方式"
                        prop="phone"
                        width="250">
                </el-table-column>
                <el-table-column
                        label="邮箱"
                        prop="email"
                        width="250">
                </el-table-column>
                <el-table-column
                        label="角色"
                        prop="type"
                        width="100">
                    <template slot-scope="scope">
                        <el-tag v-if="scope.row.type == 1" type="danger" size="medium">管理员</el-tag>
                        <el-tag v-if="scope.row.type == 2" size="medium">用户</el-tag>
                    </template>
                </el-table-column>
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

        <!-- 对话框 新建员工 -->
        <el-dialog title="新建" :visible.sync="dialog_add" width="30%">
            <el-form ref="form_add" :model="form_add" :rules="rules" label-width="80px" @keyup.enter.native="addUser()">
                <el-form-item label="用户名" prop="username">
                    <el-input v-model="form_add.username" placeholder="请输入用户名"></el-input>
                </el-form-item>
                <el-form-item label="昵称" prop="nickname">
                    <el-input v-model="form_add.nickname" placeholder="请输入昵称"></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="password">
                    <el-input v-model="form_add.password" placeholder="请输入密码" show-password autocomplete="new-password"></el-input>
                </el-form-item>
                <el-form-item label="手机号" prop="phone">
                    <el-input v-model="form_add.phone" placeholder="请输入手机号"></el-input>
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                    <el-input v-model="form_add.email" placeholder="请输入邮箱"></el-input>
                </el-form-item>
                <el-form-item v-if="user.type == 1" label="角色" prop="type">
                    <el-radio v-model="form_add.type" :label=1>管理员</el-radio>
                    <el-radio v-model="form_add.type" :label=2>用户</el-radio>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
           <el-button @click="dialog_add = false">取 消</el-button>
           <el-button type="primary" @click="addUser">确 定</el-button>
         </span>
        </el-dialog>

        <!-- 对话框 编辑员工 -->
        <el-dialog title="编辑" :visible.sync="dialog_edit" width="30%">
            <el-form ref="form_edit" :model="form_edit" :rules="rules" label-width="80px"
                     @keyup.enter.native="updateUser()">
                <el-form-item label="用户名" prop="username">
                    <el-input v-model="form_edit.username" placeholder="请输入用户名"></el-input>
                </el-form-item>
                <el-form-item label="昵称" prop="nickname">
                    <el-input v-model="form_edit.nickname" placeholder="请输入昵称"></el-input>
                </el-form-item>
                <el-form-item label="手机号" prop="phone">
                    <el-input v-model="form_edit.phone" placeholder="请输入手机号"></el-input>
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                    <el-input v-model="form_edit.email" placeholder="请输入邮箱"></el-input>
                </el-form-item>
                <el-form-item v-if="user.type == 1" label="角色" prop="type">
                    <el-radio v-model="form_edit.type" :label=1>管理员</el-radio>
                    <el-radio v-model="form_edit.type" :label=2>用户</el-radio>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
          <el-button @click="dialog_edit = false">取 消</el-button>
          <el-button type="primary" @click="updateUser">确 定</el-button>
        </span>
        </el-dialog>

    </div>
</template>

<script>
    export default {
        name: 'User',
        data() {
            return {
                user:JSON.parse(window.sessionStorage.getItem("user")),
                userData: [],  //分类数据
                currentPage: 1,  //当前页
                total: 0, //总记录数
                pagesize: 5, //页面大小
                searchFrom: {
                   nickname: '',
                   type: null
                },
                dialog_add: false, //添加对话框
                dialog_edit: false, //编辑对话框
                imageUrl: '',
                form_add: {
                    username: '',
                    nickname: '',
                    password: '',
                    avatar: '',
                    phone: '',
                    email: '',
                    type: null
                },
                form_edit: {
                    id: '',
                    username: '',
                    nickname: '',
                    phone: '',
                    email: '',
                    avatar: '',
                    type: null
                },
                rules: {
                    username: [
                        {required: true, message: '用户名不能为空', trigger: 'blur'}
                    ],
                    nickname: [
                        {required: true, message: '昵称不能为空', trigger: 'blur'}
                    ],
                    password: [
                        {required: true, message: '密码不能为空', trigger: 'blur'}
                    ],
                    phone: [
                        {required: true, message: '联系方式不能为空', trigger: 'blur'}
                    ],
                    email: [
                        {required: true, message: '邮箱不能为空', trigger: 'blur'}
                    ],
                    avatar: [
                        {required: true, message: '请上传用户头像', trigger: 'blur'}
                    ],
                    type: [
                        {required: true, message: '请选择用户角色', trigger: 'blur'}
                    ]
                },
                roleList: null
            }
        },
        mounted() {
            this.initData();
        },
        methods: {
            //初始化数据
            initData() {
                const _this = this
                let params = {
                    current: this.currentPage,
                    size: this.pagesize,
                    nickname: this.searchFrom.nickname,
                    type: this.searchFrom.type
                }
                this.getRequest('/user/list', params).then(resp => {
                    _this.userData = resp.obj.records
                    _this.total = resp.obj.total
                })
            },
            uploadImg(data) {
                let myFromCTData = new FormData();
                myFromCTData.append('file', data.file);
                myFromCTData.append('需要传递的参数1', 1);
                myFromCTData.append('需要传递的参数2', 1);
                //发起axios
                this.postPicRequest('/upload', myFromCTData).then(resp => {
                    this.imageUrl = resp.obj.fileUrl
                })
            },
            handleAvatarSuccess(res, file) {
                this.imageUrl = URL.createObjectURL(file.raw);
            },
            beforeAvatarUpload(file) {
                const isLt4M = file.size / 1024 / 1024 < 4;
                if (!isLt4M) {
                    this.$message.error('上传相片大小不能超过 4MB!');
                }
                return isLt4M;
            },
            //编辑
            handleEdit(index, row) {
                const _this = this
                this.dialog_edit = true
                this.getRequest('/user/getUserById?id=' + row.id).then(resp => {
                    _this.form_edit = resp.obj
                    _this.imageUrl = resp.obj.avatar
                })
            },
            //更新
            updateUser() {
                const _this = this
                this.form_edit.avatar = this.imageUrl
                this.$refs['form_edit'].validate(valid => {
                    if (valid) {
                        this.putRequest('/user/updateUser', this.form_edit).then(resp => {
                            if (resp) {
                                this.initData()
                                this.$refs['form_edit'].resetFields()
                                _this.dialog_edit = false
                                this.$refs.uploadEdit.clearFiles()
                            }
                        })
                    }
                })
            },
            //删除
            handleDelete(index, row) {
                const _this = this
                this.$confirm('此操作将永久删除该用户, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest('/user/deleteUserById?id=' + row.id).then(resp => {
                        if (resp) {
                            this.initData()
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });

            },
            //添加
            addUser() {
                const _this = this
                this.form_add.avatar = this.imageUrl
                if(_this.user.type == 2){
                    this.form_add.type = 3
                    this.form_add.shopId = this.user.shopId
                }
                this.$refs['form_add'].validate(valid => {
                    if (valid) {
                        this.postRequest('/user/add', this.form_add).then(resp => {
                            this.$refs['form_add'].resetFields()
                            this.initData()
                            _this.dialog_add = false
                            this.$refs.uploadAdd.clearFiles()
                        })
                    }
                })
            },
            //搜索
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

<style scoped>

</style>
