function initMap(myChart) {
	var option = {
		z-index:1,
		tooltip : {
			trigger: 'item',
			formatter: "{a}<br/>{b}: {c}个" 
		},
		dataRange: {
			min : 0,
			max : 130,
			color: ['#00a2ff'],
			calculable : true
		},
    
		roamController: {
			show: false,
			x: 'right',
			mapTypeControl: {
				'china': false
			}
		},
		series: [{
			name: '中国',
			type: 'map',
			roam: true,
			mapType: 'china',
			selectedMode : 'multiple',
			label: {
				normal: {
					show: true
				},
				emphasis: {
					show: true
				}
			},
			data:[
				{name: '北京',value: 129},
                {name: '天津',value:97},
                {name: '上海',value:87 },
                {name: '重庆',value:54},
                {name: '河北',value:110},
                {name: '河南',value:70},
                {name: '云南',value:41 },
                {name: '辽宁',value:50},
                {name: '黑龙江',value:60},
                {name: '湖南',value: 76},
                {name: '安徽',value: 85},
                {name: '山东',value: 45},
                {name: '新疆',value: 37},
                {name: '江苏',value:43 },
                {name: '浙江',value: 54},
                {name: '江西',value:65 },
                {name: '湖北',value:65 },
                {name: '广西',value:54 },
                {name: '甘肃',value: 54},
                {name: '山西',value:120 },
                {name: '内蒙古',value:30 },
                {name: '陕西',value:65 },
                {name: '吉林',value:40 },
                {name: '福建',value:128 },
                {name: '贵州',value: 65},
                {name: '广东',value: 120},
                {name: '青海',value:62 },
                {name: '西藏',value: 40},
                {name: '四川',value: 42},
                {name: '宁夏',value: 23},
                {name: '海南',value:100},
                {name: '台湾',value:38},
                {name: '香港',value:38 },
                {name: '澳门',value:120 }
			]
		}]
	};
	myChart.setOption(option);
};