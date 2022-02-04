function initBar(myChart) {
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
			y2:50,
                   
		},
		xAxis: {
			type: 'category',
            show: true,
            data: ['四川省','陕西省','吉林省','云南省','广东省','安徽省','浙江省','福建省','甘肃省','河南省','山东省','河北省'],
			axisLabel: {
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
				axisLabel : {
					formatter: '{value}',
					textStyle: {
						color: '#fff'          
					}
				},
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
				barWidth : 15,//柱图宽度
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
				data: [90,89,56,76,83,99,108,132,86,97,83,87]
			}
		]
	};
	
	myChart.setOption(option);
};