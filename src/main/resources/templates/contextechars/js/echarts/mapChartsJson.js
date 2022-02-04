var toolTipStr;
function initMap(myChart,mapJson) {
	toolTipStr = mapJson;
	var res = formatData(mapJson);
	var option = {
	tooltip: {
		trigger: 'item',
		formatter: function (params) {
			if(params.value[2] != null && params.value[2] != ''
				&& params.value[2] != undefined && params.value[2] != 'undefined'){
				return params.name + ' : ' + params.value[2];
			}
			if(!isNaN(params.value) && params.name!=''){
				var jsonStr = eval(toolTipStr);
				var reStr = params.name + ' : ' + params.value + "<br/>";
				for(var i=0;i<jsonStr.length;i++){
					if(jsonStr[i].name == params.name){
						var dataStr = jsonStr[i].data;
						for(var j = 0;j < dataStr.length;j++){
							reStr = reStr + "<br/>" + 
							dataStr[j].name + ' : ' +dataStr[j].value[2];
						}
						break;
					}
				}
				return reStr;
				
			}
		}
	},
	
	toolbox: {
        show : true,
        orient : 'vertical',
        x: 'right',
        y: 'top',
        feature : {
            mark : {show: false},
            dataView : {show: false, readOnly: false},
            restore : {show: true,title:'还原'},
            saveAsImage : {show: false}
        }
    },
   
    geo: {
        map: 'china',
        roam: true,
		zoom: 1.2,
        label: {
            normal: {
                show: true,
                textStyle: {
                    color: '#fff'
                }
            }
        },
        itemStyle: {
            normal: {
                areaColor: '#112270',
                borderColor: '#50a3ba',
				borderWidth:2
            },
            emphasis: {
                areaColor: '#50a3ba'
            }
        },regions: [
			{
                name: '南海诸岛', 
                value: 0, 
                itemStyle: 
                {
					normal: 
                    {
						opacity: 0
						
                    }
                },label: {
					normal: {
					show: false,
					}
				}
            }
         ]
    },
    series : [
       {
           type: 'scatter',
           coordinateSystem: 'geo',
           data: res.scatterData,
           symbolSize: 10,
           label: {
               normal: {
                   formatter: '{b}',
                   position: 'right',
                   show: false
               },
               emphasis: {
                   show: false
               }
           },
           itemStyle: {
               normal: {
                    color: '#00fb92',
					borderColor: '#158e7e',
					borderWidth: 4
               }
           }
        },
        {
            
            type: 'map',
            geoIndex: 0,
            data:res.mapData
        }
    ]
	};
	myChart.setOption(option);
};

function formatData(data){
	var jsonStr = eval(data);
	var mapData =[];
	var scatterData = [];
	for(var i=0;i<jsonStr.length;i++){
		var sum = 0;
		for(var j = 0;j < jsonStr[i].data.length;j++){
			scatterData.push(jsonStr[i].data[j]);
			sum = sum + jsonStr[i].data[j].value[2];
		}
		mapData.push({
			name: jsonStr[i].name,
			value: sum
		});		
	}
	var res = {};
	res.mapData = mapData;
	res.scatterData = scatterData;
	return res;
}