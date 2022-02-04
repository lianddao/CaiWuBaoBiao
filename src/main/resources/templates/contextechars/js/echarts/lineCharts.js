function initLine(myChart) {
	var option = {
		tooltip: {
			trigger: 'axis'
		},
		grid:{
			y:30,
			x2:0
		},
		legend: {
			itemWidth: 14,// 标志图形的长度
			itemHeight: 6,// 标志图形的宽度
			data:['2018年','2019年'],
			textStyle: {
				color: '#fff'
			},
			right:10
		},
		calculable : false,
		color:['#fb9a00','#238ac5'],
		xAxis: {
			type: 'category',
            show: true,
            data: ['四川省','甘肃省','河北省','山东省','黑龙江省','浙江省','青海省','吉林省','辽宁省','陕西省'],
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
			　　　　show:true,
					lineStyle:{
						type:'dashed',
						color:'#238ac5'
					}
			　　},

			}
		],
		series: [
			{
				name:'2018年',
				type:'line',
				stack: '',
				symbol: 'circle',
				symbolSize: 10,
				data:[180, 132, 101, 134, 90, 230, 210,193,250,300]
			},
			{
				name:'2019年',
				type:'line',
				stack: '',
				symbol: 'circle',
				symbolSize: 10,
				data:[220, 182, 191, 234, 290, 330, 310,198,250,265]
			}
		]
	};
	myChart.setOption(option);
};
    