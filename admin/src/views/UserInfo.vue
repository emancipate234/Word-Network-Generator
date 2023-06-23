<template>
    <div>
        <el-form ref="userForm" :model="userForm" @keyup.enter.native="update()"
                 label-width="100px" style="margin-top: 20px">
            <el-form-item label="用户名" prop="username">
                <el-input v-model="userForm.username" disabled style="width: 400px;"></el-input>
            </el-form-item>
            <el-form-item label="昵称" prop="nickname">
                <el-input v-model="userForm.nickname" style="width: 400px;"></el-input>
            </el-form-item>
            <el-form-item label="手机号" prop="phone">
                <el-input v-model="userForm.phone" style="width: 400px;"></el-input>
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
                <el-input v-model="userForm.email" style="width: 400px;"></el-input>
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
                userForm: {}
            }
        },
        mounted() {
            this.userForm = JSON.parse(window.sessionStorage.getItem("user"))
        },
        methods: {
            //更新
            update() {
                const _this = this
                _this.$refs['userForm'].validate(valid => {
                    if (valid) {
                        this.putRequest('/user/updateUser', _this.userForm).then(resp => {
                            if (resp) {
                                window.sessionStorage.setItem("user", JSON.stringify(resp.obj));
                            }
                        })
                    }
                })
            }
        }
    }
</script>
