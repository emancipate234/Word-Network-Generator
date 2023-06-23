<template>
    <div>
        <!-- 对话框 增加 -->
        <el-dialog title="填写复习数量" :visible.sync="addDialog" width="30%">
            <el-form label-width="100px">
                <el-form-item label="复习数量" prop="number">
                    <el-input-number v-model="number" placeholder="请输入复习数量" :min="1"></el-input-number>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                 <el-button @click="addDialog = false">取 消</el-button>
           <el-button type="primary" @click="confirm">确 定</el-button>
         </span>
        </el-dialog>

        <el-row v-if="testDialog" style="margin-top: 40px">
            <el-col :span="6">
                <div class="echart" id="echart" style="width: 100%;height: 250px;"></div>
            </el-col>
            <el-col :span="10" :offset="1" >
                <p style="text-align: center;font-weight: bold;font-size: 20px;">{{data[index].name}}</p>
                <p style="text-align: center;white-space: pre-wrap">{{data[index].phonetic}}</p>
                <el-card class="box-card" style="height: 150px;">
                    <div class="text item">
                        <p style="white-space: pre-wrap">{{data[index].translate}}</p>
                    </div>
                </el-card>
                <el-progress :text-inside="true" :stroke-width="24" :percentage="(index/data.length * 100) | numFilter" status="success"  style="margin: 20px 0;"></el-progress>
   <!--             <el-steps :active="index" align-center style="margin: 20px 0;">
                    <el-step v-for="(item,index) in data"></el-step>
                </el-steps>-->
                <div style="float: left;">
                    <el-button type="primary" icon="el-icon-arrow-left" @click="previous">上一个</el-button>
                    <el-button type="primary" @click="next">下一个<i class="el-icon-arrow-right el-icon--right"></i></el-button>
                </div>

                <div style="text-align: right">
                    <el-button type="info" @click="next">不认识</el-button>
                    <el-button type="success" @click="remember">我记得</el-button>
                </div>
                <p style="white-space: pre-wrap">{{data[index].samples}}</p>
            </el-col>
            <el-col :span="6" :offset="1">
                <p style="white-space: pre-wrap">{{data[index].distortion}}</p>
                <p style="white-space: pre-wrap">{{data[index].phrase}}</p>
            </el-col>
        </el-row>

    </div>
</template>

<script>
    import {Message} from "element-ui";

    export default {
        name: 'Equipment',
        data() {
            return {
                data: [],  //数据
                index: 0,
                addDialog: true, //添加的对话框
                testDialog: false,
                number: 10
            }
        },
        filters: {
            numFilter (value) {
                // 截取当前数据到小数点后两位
                let realVal = parseFloat(value).toFixed(2)
                return realVal
            }
        },
        mounted() {
            //this.confirm()
            //this.getMemory()
        },
        methods: {
            //获取改单词的单词记忆
            getMemory(wordId) {
                const _this = this
                this.getRequest('/wordMemory/getByWordId?wordId=' + wordId).then(resp => {
                    console.log(resp)
                    let dateArr = ['2023-04-10', '2023-04-11', '2023-04-12', '2023-04-13', '2023-04-14', '2023-04-15', '2023-04-16', '2023-04-17', '2023-04-18']
                    let valueArr = [90, 80, 70, 60, 50, 40, 35, 30, 25]
                    _this.initChart("echart", dateArr, valueArr)
                })
            },
            initChart(item, dateArr, valueArr) {
                let myChart = this.$echarts.init(
                    document.getElementById(item)
                );
                let option = {
                    title: {
                        text: '复习历史'
                    },
                    tooltip: {
                        trigger: 'axis'
                    },
                    legend: {
                        textStyle: {
                            color: 'white'
                        }
                    },
                    grid: {
                        top: '15%',
                        left: '6%',
                        right: '6%',
                        bottom: '3%',
                        containLabel: true
                    },
                    xAxis: {
                        type: 'category',
                        boundaryGap: false,
                        data: dateArr
                    },
                    yAxis: {
                        type: 'value',
                        splitLine: {
                            show: false
                        }
                    },
                    color: ['#0072FF', '#E6A23C', '#F56C6C'],
                    series: [
                        {
                            type: 'line',
                            data: valueArr
                        }
                    ]
                };
                myChart.setOption(option);
            },
            previous(){
              if(this.index > 0){
                  this.index = this.index - 1
              }
            },
            next(){
                if(this.index < this.data.length -1){
                    this.index = this.index + 1
                }
            },
            remember(){
                const _this = this
                let wordId = _this.data[_this.index].id
                let editFrom = {
                    wordId: wordId
                }
                this.putRequest('/wordMemory/update', editFrom).then(resp => {
                    if (resp) {
                        if(_this.index < _this.data.length -1){
                            _this.index = _this.index + 1
                        }
                    }
                })
            },
            //获取单词
            confirm() {
                const _this = this
                this.getRequest('/word/getWord/' + this.number).then(resp => {
                    _this.data = resp.obj
                    this.getMemory(resp.obj[0].id)
                    _this.addDialog = false
                    _this.testDialog = true
                })
            }
        }
    }
</script>
