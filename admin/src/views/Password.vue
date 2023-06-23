<template>
    <div>
        <el-form ref="passwordForm" :model="passwordForm" :rules="rules" @keyup.enter.native="update()"
                 label-width="100px" style="margin-top: 20px">
            <el-form-item label="用户名" prop="username">
                <el-input v-model="user.username" disabled style="width: 400px;"></el-input>
            </el-form-item>
            <el-form-item label="旧密码" prop="oldPassword">
                <el-input v-model="passwordForm.oldPassword" placeholder="请输入旧密码" show-password style="width: 400px;" autocomplete="new-password"></el-input>
            </el-form-item>
            <el-form-item label="新密码" prop="newPassword">
                <el-input v-model="passwordForm.newPassword" placeholder="请输入新密码" show-password style="width: 400px;"></el-input>
            </el-form-item>
            <el-form-item label="确认密码" prop="confirmPassword">
                <el-input v-model="passwordForm.confirmPassword" placeholder="请输入确认密码" show-password style="width: 400px;"></el-input>
            </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
      <el-button type="primary" @click="update" style="margin-left: 20%">立即修改</el-button>
    </span>
    </div>
</template>

<script>
    export default {
        name: 'Password',
        data() {
            return {
                user:JSON.parse(window.sessionStorage.getItem("user")),
                passwordForm: {
                    id: '',
                    oldPassword: '',
                    newPassword: '',
                    confirmPassword: ''
                },
                rules: {
                    oldPassword: [
                        {required: true, message: '旧密码不能为空', trigger: 'blur'}
                    ],
                    newPassword: [
                        {required: true, message: '新密码不能为空', trigger: 'blur'}
                    ],
                    confirmPassword: [
                        {required: true, message: '确认密码不能为空', trigger: 'blur'}
                    ]
                }
            }
        },
        mounted() {
        },
        methods: {
            //更新
            update() {
                const _this = this
                if(_this.passwordForm.newPassword != _this.passwordForm.confirmPassword){
                    this.$message.error("两次新密码不匹配，请确认");
                    return false;
                }
                _this.passwordForm.id = _this.user.id
                _this.$refs['passwordForm'].validate(valid => {
                    if (valid) {
                        this.putRequest('/user/updatePassword', _this.passwordForm).then(resp => {
                            if (resp) {
                                this.$refs['passwordForm'].resetFields()
                            }
                        })
                    }
                })
            }
        }
    }
</script>
