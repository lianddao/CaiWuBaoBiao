function initLineAndBar(myChart,lineAndBarJson) {
	var jsonStr = eval(lineAndBarJson);
	var lineJson = eval(lineAndBarJson.series[0]);
	
	var barJson = eval(lineAndBarJson.series[1]);
	
	var barData = eval(barJson.data);
	
	var lineData = eval(lineJson.data);
	//获取折线数组中最大值
	var barMax = Math.max.apply(null,barData);
	//获取柱图数组中最大值
	var lineMax = Math.max.apply(null,lineData);
	//得到折线和柱状图数组最大值用来设置两个Y轴最大值，折线与柱状图保持一致
	var YMax = barMax>lineMax ? barMax : lineMax;
	//向上取100的倍数
	YMax = ((Math.floor(YMax/100))+1)*100
	var legend = [lineJson.name,barJson.name];
	var option = {
		calculable : false,
		grid:{
		    x:25,
			y:8,
			x2:0
		},
		legend: {
			itemWidth: 14,// 标志图形的长度
			itemHeight: 6,// 标志图形的宽度
			data:legend,
			textStyle: {
				color: '#fff'
			},
			right:10
		},
		tooltip: {
			trigger: 'axis',
			formatter: function (params){
				return  params[0].name + '<br/>'
                    + params[0].seriesName + ' : ' + params[0].value+'<br/>'
                    +params[1].seriesName+':'+params[1].value;
            }
		},
		xAxis: [
			{
				type: 'category',
				boundaryGap: true,
				axisLine: {
	                              lineStyle: {
	                                  type: 'solid',
	                                  color: '#00BFFF',//左边线的颜色
	                                  width:'1'//坐标线的宽度
	                              }
	                          },
				axisLabel : {
				    
					rotate: 0,
					textStyle: {
						color: '#fff'          
					},
					formatter:function(value){
						return value.split("").join("\n");
					}
				},
				data: jsonStr.xAxis,
			},
			{
				type: 'category',
				boundaryGap: true,
				show: false,
				axisLabel : {
					
					textStyle: {
						color: '#fff'          
					}
				},
				data: jsonStr.xAxis
			}
		],
		yAxis: [
			{
				type: 'value',
				show: true,
				axisLine: {
				                   lineStyle: {
				                       type: 'solid',
				                       color:'#00BFFF',
				                       width:'1'
				                   }
				               },
				axisLabel : {
					formatter:function(value){
						return formatyAxisValue(value);
					},
					textStyle: {
						color: '#fff'
					}
				},
				scale: true,
				min: 0,
				max: YMax,
				splitNumber:2,
				splitLine:{
			　　　　show:false
			　　}
			},
			{
				type: 'value',
				show: false,
				axisLabel : {
					formatter: '{value}',
					textStyle: {
						color: '#fff'          
					}
				},
				scale: true,
				min: 0,
				max: YMax,
				splitLine:{
			　　　　show:false
			　　}
			}
		],
		series: [
			{
				name:lineJson.name,
				type:'line',
				symbol: 'circle',
				itemStyle: {
					normal: {
						color: "#fafc01",
						lineStyle: {
							color: "#fafc01"
						}
					}
				},
				data:lineJson.data
			},{
				name:barJson.name,
				type:'bar',
				barWidth : 15,//柱图宽度
				xAxisIndex: 1,
				yAxisIndex: 1,
				itemStyle: {
					normal: {
						color: new echarts.graphic.LinearGradient(
							0, 0, 0, 1,
							[
								{offset: 0, color: '#83bff6'},
								{offset: 0.5, color: '#188df0'},
								{offset: 1, color: '#188df0'}
							]
						)
					},
					emphasis: {
						color: new echarts.graphic.LinearGradient(
							0, 0, 0, 1,
							[
								{offset: 0, color: '#2378f7'},
								{offset: 0.7, color: '#2378f7'},
								{offset: 1, color: '#83bff6'}
							]
						)
					}
				},
				data:barJson.data
			}
		]
	};
	myChart.setOption(option);
};
