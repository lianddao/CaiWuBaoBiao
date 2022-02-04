function initRose(myChart) {
	var option = {
		backgroundColor:'rgba(128, 128, 128, 0.1)',
		tooltip : {
			trigger: 'item',
			formatter: "{b} : {c} <br/>{d}%",
			backgroundColor:'rgba(128, 128, 128, 0.1)'
		},
		legend: {
			type: 'scroll',
			orient : 'vertical',
			right: 10,
			y:'center',
			icon:"circle",
			itemWidth: 7,// 标志图形的长度
			data:['四川省','甘肃省','河北省','山东省','安徽省','江西省','广东省','河南省'],
			textStyle: {
				color: '#fff'       
			}
		},
		calculable : false,
		color:['#0040a1','#d84122','#fb9a00','#238ac5'],  
		series : [
			{
				name:'',
				type:'pie',
				radius : [30, 110],
				roseType : 'area',
				center: ['30%', '50%'],
				data:[
					{value:10, name:'四川省'},
					{value:12, name:'甘肃省'},
					{value:15, name:'河北省'},
					{value:25, name:'山东省'},
					{value:20, name:'安徽省'},
					{value:35, name:'江西省'},
					{value:30, name:'广东省'},
					{value:40, name:'河南省'}
				],
				itemStyle:{
						normal:{
							label:{
								show: false,
								formatter: '{c} ({d}%)'
							},
							labelLine : {
								show : false
							}
						}
				}
			}
		]
	};
	myChart.setOption(option);
};