function initRose(myChart,roseJson) {
	var jsonStr = eval(roseJson);
	
	var legendData = [];
	for(var i=0;i<jsonStr.length;i++){
		legendData.push(jsonStr[i].name);
	}
	var option = {
		//backgroundColor:'rgba(128, 128, 128, 0.1)',
	
		tooltip : {
			trigger: 'item',
			formatter: "{b} : {c} <br/>{d}%",
			backgroundColor:'rgba(128, 128, 128, 0.1)'
		},
		legend: {
			type: 'scroll',
			orient : 'horizontal',//horizontal 水平排列,vertical 垂直排列，
			right: 10,
			y:'top',
			
			icon:"circle",
			itemWidth: 5,// 标志图形的长度
			
			data:legendData,
			textStyle: {
				color: 'white',
				fontSize: '14',
				
			}
		},
		    			 
		calculable : true,
		color:['#00FFFF','#FFFF00','#ef4853','#C71585','#DB7093','#DC143C','#FF0000'],  
		series : [
			{
				name:'',
				type:'pie',
				radius : [45, 70],
				//roseType : 'area',
				center: ['55%', '60%'],
				x: '1%',               // for funnel
		        max: 50,                // for funnel
		        sort : 'ascending',     // for funnel
				data:jsonStr,
				roseType: 'radius',    //不同半径
	            label: {  //饼图的提示文字
	              normal: {
	                show: true,
	                position: 'top',
	                fontSize: '14',
	                
	              }
	            },
			emphasis: {   
		                show: true,
		                position: 'top',
		                textStyle: {
		                  fontSize: '14',
		                  fontWeight: 'normal'
		                }
		              }

			}
	        
		]
	};
	
	myChart.setOption(option);
};