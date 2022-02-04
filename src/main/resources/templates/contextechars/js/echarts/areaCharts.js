function initArea(myChart) {
	var option = {
		tooltip : {
			trigger: 'axis'
		},
		legend: {
			itemWidth: 14,// 标志图形的长度
			itemHeight: 6,// 标志图形的宽度
			data:['动用次数','射弹数'],
			textStyle: {
				color: '#fff'
			},
			right:10
		},
		color:['#d84122','#238ac5'],  
		grid:{
			y:30,
			x2:-20
		},
		xAxis: {
			type: 'category',
            show: true,
            data: ['2018-07','2018-08','2018-09','2018-10','2018-11','2018-12','2019-01'],
			axisLabel: {
				rotate: 30,
				textStyle: {
					color: '#fff'         
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
			　　　　show:true,
					lineStyle:{
						type:'dashed',
						color:'#238ac5'
					}
			　　},

			}
		],
		series : [
			{
				name:'动用次数',
				type:'line',
				stack: '',
				symbol:'none',
				smooth:true,
				areaStyle: {},
				data:[0, 32, 101, 180, 90, 230, 30]
			},
			{
				name:'射弹数',
				type:'line',
				stack: '',
				symbol:'none',
				smooth:true,
				areaStyle: {},
				data:[0, 85, 191, 35, 290, 130, 50]
			}
		]
	};
	myChart.setOption(option);
};