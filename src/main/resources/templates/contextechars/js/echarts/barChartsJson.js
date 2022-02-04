function initBar(myChart,barJson) {
				
	var jsonStr = eval(barJson.series[0]);	
	var xAxis = eval(barJson.xAxis);	  
	var barJson = eval(barJson.series[0]);		
	var data = eval(barJson.data);

	var option = {
		tooltip : {
			trigger: 'axis',
			axisPointer : {            
				type : 'shadow'       
			}
		},
		grid:{
			x:35,
			y:40,
			x2:0,
		},
		xAxis: {
			type: 'category',
            show: true,
            data: xAxis,
            axisLine: {
                              lineStyle: {
                                  type: 'solid',
                                  color: '#00BFFF',//左边线的颜色
                                  width:'1'//坐标线的宽度
                              }
                          },
			axisLabel: {
			    interval:0, 
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
				axisLine: {
	                              lineStyle: {
	                                  type: 'solid',
	                                  color: '#00BFFF',//左边线的颜色
	                                  width:'1'//坐标线的宽度
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
				min:0,
				splitNumber:2,
				splitLine:{
			　　　　show:false
			　　},

			}
		],
		dataZoom: [
			{
				type: 'inside'
			}
		],
		series: [
			{
				type: 'bar',
				barWidth : 8,//柱图宽度
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
				data: data
			}
		]
	};
	
	myChart.setOption(option);
};