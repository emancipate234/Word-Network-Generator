<template>
    <div>
        <el-container>
            <el-header class="homeHeader">
                <a href="/#/admin/home">
                    <div class="title">背多分</div>
                </a>
                <el-dropdown class="userInfo" @command="commandHandler">  <!--@command="commandHandler" 点击菜单项触发的事件回调-->
                    <span class="el-dropdown-link">
                        {{user.username}}
                    </span>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item command="userInfo">个人信息</el-dropdown-item>
                        <el-dropdown-item command="setting">密码修改</el-dropdown-item>
                        <el-dropdown-item command="logout" divided>注销登录</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </el-header>
            <el-container>
                <el-aside width="200px">

                    <el-menu router>
                        <!-- <el-button>点我</el-button> -->
                        <el-menu-item index="/admin/home">
                            <i class="el-icon-menu"></i>
                            <span slot="title">首页</span>
                        </el-menu-item>
                        <!--这个遍历拿到的是index.js里面的routers地址数组 -->
                        <!-- !item.hidden 是将home和login的路由名隐藏，不需要在左侧菜单渲染出来 -->
                        <el-submenu :index="item.path" v-for="(item,index) in this.$router.options.routes"
                                    v-if="!item.hidden" :key="index">
                            <template slot="title">
                                <i class="el-icon-location"></i>
                                <span>{{item.name}}</span>
                            </template>
                            <el-menu-item v-if="child.type.indexOf(user.type) >= 0" :index="child.path"
                                          v-for="(child,indexj) in item.children" :key="indexj">
                                {{child.name}}
                            </el-menu-item>
                        </el-submenu>
                    </el-menu>
                </el-aside>
                <el-main>
                    <!-- 面包屑 -->
                    <el-breadcrumb separator-class="el-icon-arrow-right"
                                   v-if="this.$router.currentRoute.path!='/admin/home'">
                        <el-breadcrumb-item :to="{ path: '/admin/home' }">首页</el-breadcrumb-item>
                        <el-breadcrumb-item>{{this.$router.currentRoute.name}}</el-breadcrumb-item>
                    </el-breadcrumb>
                    <div class="homeWelcome" v-if="this.$router.currentRoute.path=='/admin/home'">
                        亲爱的【{{user.username}}】，您好！欢迎使用背多分。当前时间为：{{datetime}}
                        <el-row :gutter="20" style="margin-top: 20px;">
                            <el-col :span="2" style="color: black; border: 1px solid black;">
                                用户名：
                            </el-col>
                            <el-col :span="6" style="color: black; border: 1px solid black;">
                                {{user.username}}
                            </el-col>
                        </el-row>
                        <el-row :gutter="20">
                            <el-col :span="2" style="color: black; border: 1px solid black;">
                                昵称：
                            </el-col>
                            <el-col :span="6" style="color: black; border: 1px solid black;">
                                {{user.nickname}}
                            </el-col>
                        </el-row>
                        <el-row :gutter="20">
                            <el-col :span="2" style="color: black; border: 1px solid black;">
                                角色：
                            </el-col>
                            <el-col :span="6" style="color: black; border: 1px solid black;">
                                {{user.type==1?'管理员':'用户'}}
                            </el-col>
                        </el-row>
                    </div>
                    <router-view/>
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<script>
    export default {
        name: "Home",
        data() {
            return {
                datetime: '',
                user: JSON.parse(window.sessionStorage.getItem("user"))  //这样得到的数据是字符串，要用JSON.parse方法吧字符串转换成json数据
            }
        },
        mounted() {
            this.getdate();
            this.timer = setInterval(() => {
                this.getdate();
            }, 1000)
        },
        methods: {
            getdate: function () {
                let weeks = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'];
                let time = new Date();
                let year = time.getFullYear();
                let month = time.getMonth() + 1;
                let day = time.getDate();
                let week = weeks[time.getDay()];
                let hour = time.getHours();
                let minutes = time.getMinutes();
                if (minutes < 10) {
                    minutes = '0' + minutes
                }
                let seconds = time.getSeconds()
                if (minutes < 10) {
                    seconds = '0' + seconds
                }
                this.datetime = year + '-' + month + '-' + day + " " + hour + ":" + minutes + ":" + seconds + " " + week;
            },
            commandHandler(cmd) {  //该方法有一个参数，cmd
                if (cmd == 'logout') {
                    this.$confirm('此操作将注销登录, 是否继续?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        this.getRequest("/logout");
                        window.sessionStorage.removeItem("user")
                        this.$router.replace("/")

                    }).catch(() => {
                    });
                } else if (cmd == 'setting') {
                    this.$router.replace("/admin/password")
                }else if (cmd == 'userInfo') {
                    this.$router.replace("/admin/userInfo")
                }
            }
        }
    }
</script>

<style>
    .homeHeader {
        background-color: #409EFF;
        display: flex;
        align-items: center; /*竖轴上居中*/
        justify-content: space-between; /*空白的地方在中间*/
        padding: 0 15px;
        box-sizing: border-box;
    }

    /* 字体样式 */
    .title {
        font-size: 20px;
        color: #ffffff;
    }

    /*设置鼠标移上去显示为手指*/
    .userInfo {
        cursor: pointer;
    }

    /* 头像样式 */
    .el-dropdown-link img {
        width: 48px;
        height: 48px;
        border-radius: 24px;
        margin-left: 8px;
    }

    /* 用户名样式 */
    .el-dropdown-link {
        display: flex;
        align-items: center;
    }

    /* 面包屑样式 */
    .homeWelcome {
        text-align: left;
        font-size: 20px;
        color: #409EFF;
    }

    a {
        text-decoration: none;
    }


</style>
