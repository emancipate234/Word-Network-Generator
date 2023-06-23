<template>
    <div class="background" :style="bgImg">
        <el-form
                v-loading="loading"
                element-loading-text="正在登录..."
                element-loading-spinner="el-icon-loading"
                element-loading-background="rgba(0, 0, 0, 0.8)"
                :rules="rules" :model="loginForm" class="loginContainer" ref="loginForm">
            <h1 class="loginTitle">背多分</h1>
            <!--表单里面的每一项叫做<el-form-item></el-form-item,要加个prop属性，本来是不用加的，但是我们这个使用了字段校验，这种情况家就一定要加prop属性-->
            <el-form-item prop="username">
                <!--auto-complete:是否自动补全-->
                <el-input type="text" v-model="loginForm.username" auto-complete="off"
                          placeholder="请输入用户名" @keydown.enter.native="submitLogin"></el-input>
            </el-form-item>
            <el-form-item prop="password">
                <el-input type="password" v-model="loginForm.password" auto-complete="off"
                          placeholder="请输入密码" @keydown.enter.native="submitLogin"></el-input>
            </el-form-item>
            <el-checkbox class="loginRember" v-model="checked">记住我</el-checkbox>
            <el-row>
                <el-col :span="11">
                    <el-button type="primary" style="width: 100%" @click="submitLogin">登录</el-button>
                </el-col>
                <el-col :span="11" :offset="2">
                    <el-button type="success" style="width: 100%" @click="registerHandle">注册</el-button>
                </el-col>
            </el-row>
        </el-form>

        <!-- 对话框 增加 -->
        <el-dialog title="注册" :visible.sync="registerDialog" width="30%" append-to-body :close-on-click-modal="false">
            <el-form ref="registerFrom" :model="registerFrom" :rules="registerRules"
                     label-width="100px">
                <el-form-item label="用户名" prop="username">
                    <el-input v-model="registerFrom.username" placeholder="请输入用户名" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="昵称" prop="nickname">
                    <el-input v-model="registerFrom.nickname" placeholder="请输入昵称" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="password">
                    <el-input type="password" v-model="registerFrom.password" placeholder="请输入密码" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="手机号" prop="phone">
                    <el-input v-model="registerFrom.phone" placeholder="请输入手机号" auto-complete="off"></el-input>
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                    <el-input v-model="registerFrom.email" placeholder="请输入邮箱" auto-complete="off"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
           <el-button @click="registerDialog = false">取 消</el-button>
           <el-button type="primary" @click="register">确 定</el-button>
         </span>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "Login",
        data() {
            return {
                bgImg: {
                    backgroundImage: 'url(' + require('../assets/bg.jpg') + ')',
                    backgroundRepeat: 'no-repeat',
                    backgroundSize: '100% 100%'
                },
                loginForm: {
                    username: "",
                    password: ""
                },
                checked: true,
                rules: {
                    //required:true:用户名必填  如果没填就弹出““””“"请输入用户名",trigger:blur 触发的方式是blur
                    username: [{required: true, message: "用户名不能为空", trigger: "blur"}],
                    password: [{required: true, message: "密码不能为空", trigger: "blur"}],
                },
                loading: false,  //加载样式
                registerDialog: false,
                registerFrom: {
                    username: '',
                    nickname: '',
                    password: '',
                    phone: '',
                    email: ''
                },
                registerRules: {
                    //required:true:用户名必填  如果没填就弹出““””“"请输入用户名",trigger:blur 触发的方式是blur
                    username: [{required: true, message: "用户名不能为空", trigger: "blur"}],
                    nickname: [{required: true, message: "昵称不能为空", trigger: "blur"}],
                    password: [{required: true, message: "密码不能为空", trigger: "blur"}],
                },

            }
        },
        methods: {
            registerHandle() {
                this.registerDialog = true
            },
            register(){
                const _this = this
                this.$refs['registerFrom'].validate(valid => {
                    if (valid) {
                        this.postRequest('/user/register', this.registerFrom).then(resp => {
                            this.$refs['registerFrom'].resetFields()
                            this.registerDialog = false
                        })
                    }
                })
            },
            submitLogin() {
                this.$refs.loginForm.validate((validate) => {  // Element自带的校验
                    if (validate) {
                        //显示加载样式
                        this.loading = true
                        //这是在api.js封装的请求
                        this.postKeyValueRequest('/doLogin', this.loginForm).then(resp => {
                            //隐藏加载样式
                            this.loading = false
                            if (resp) {
                                //resp：从服务端拿到的数据  用户的数据要保存到哪里？ 保存在sessionStorage  关闭浏览器就没了
                                window.sessionStorage.setItem("user", JSON.stringify(resp.obj));
                                //页面跳转  replace：替换  用push的话，可以使用后退按钮回到登录页，用replace不可以回到登录页
                                this.$router.replace('/admin/home')
                            }
                        })
                    } else {
                        this.$message.error("请输入所有字段");
                        return false;
                    }
                })
            }
        }

    }
</script>

<style scoped>
    .background {
        width: 100%;
        height: 100%;
        /**宽高100%是为了图片铺满屏幕 */
        z-index: -1;
        position: absolute;
    }

    .loginContainer {
        border-radius: 15px;
        background-clip: padding-box;
        margin: 180px auto;
        width: 350px;
        padding: 15px 35px 15px 35px;
        background: #fff;
        border: 1px solid #eaeaea;
        box-shadow: 0 0 25px #cac6c6;
    }

    .loginTitle {
        margin: 15px auto 20px auto;
        text-align: center;
        color: #505458;
    }

    .loginRember {
        text-align: left;
        margin: 0 0 25px 0;
    }
</style>
