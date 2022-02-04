function formatyAxisValue(value){
	if(value>=10000){
		return ((Math.floor(value/1000)+1)*1000) / 10000+"万";
	}
	return value;
}