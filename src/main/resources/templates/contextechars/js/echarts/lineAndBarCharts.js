function initLineAndBar(myChart) {
	var option = {
		calculable : false,
		color:['#fb9a00','#238ac5'],
		grid:{
			y:30,
			x2:0
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
				axisLabel : {
					textStyle: {
						color: '#fff'          
					},
					formatter:function(value){
						return value.split("").join("\n");
					}
				},
				data: ['四川省','甘肃省','河北省','山东省','黑龙江省','浙江省','青海省','吉林省','辽宁省','陕西省'],
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
				data: ['四川省','甘肃省','河北省','山东省','黑龙江省','浙江省','青海省','吉林省','辽宁省','陕西省']
			}
		],
		yAxis: [
			{
				type: 'value',
				axisLabel : {
					formatter: '{value}',
					textStyle: {
						color: '#fff'
					}
				},
				scale: true,
				min: 0,
				max: 3000,
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
				max: 3000,
				splitLine:{
			　　　　show:false
			　　}
			}
		],
		series: [
			{
				name:'折线图',
				type:'line',
				symbol: 'none',
				data:(function (){
					var res = [];
					var len = 0;
					while (len < 10) {
						res.push((Math.random()*1000).toFixed(1) - 0);
						len++;
					}
					return res;
				})()
			},{
				name:'柱状图',
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
				data:(function (){
					var res = [];
					var len = 10;
					while (len--) {
						res.push((Math.random()*2500).toFixed(1) - 0);
					}
					return res;
				})()
			}
		]
	};
	myChart.setOption(option);
};