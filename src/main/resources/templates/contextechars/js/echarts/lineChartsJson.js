function initLine(myChart,lineJson) {
	var jsonStr = eval(lineJson);
	
	var series = [];
	var seriesJson = eval(lineJson.series);
	var legend = [];
	for(var i = 0;i<seriesJson.length;i++){
		var serie={
				name:seriesJson[i].name,
				type:'line',
				stack: '',
				symbol: 'circle',
				symbolSize: 10,
				data:seriesJson[i].data
			};
		legend.push(seriesJson[i].name);
		series.push(serie);
	}
	
	var option = {
		tooltip: {
			trigger: 'axis'
		},
		grid:{
			x:40,
			y:25,
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
		calculable : false,
		color:['#00c4c7','#ffc862'],
		xAxis: {
			type: 'category',
            show: true,
            data: jsonStr.xAxis,
            axisLine: {
                  lineStyle: {
                      type: 'solid',
                      color: '#00BFFF',//左边线的颜色
                      width:'1'//坐标线的宽度
                  }
              },
			axisLabel: {
				rotate: 0,
				textStyle: {
					color: '#fff'         
				},
				formatter:function(value){
					return value.split("").join("\n");
				}
        	}
			
		},
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
				min:0,
				splitNumber:2,
				splitLine:{
			　　　　show:true,
					lineStyle:{
						type:'dashed',
						color:'#238ac5'
					}
			　　},

			}
		],
		series: series
	};
	myChart.setOption(option);
};
    