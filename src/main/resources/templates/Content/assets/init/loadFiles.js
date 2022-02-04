less = {
	env: 'development'
};
document.write(
	"<script src='/Content/assets/lib/jquery-2.1.1.min.js'></script>" +
	"<script src='/Content/assets/lib/aYin/aYin.js'></script>" +

	"<script src='/Content/assets/lib/bootstrap/js/bootstrap.min.js'></script>" +
	'<script src="/Content/js/angular/1.6.3/angular.min.js"></script>' +
	'<link href="/Content/assets/lib/bootstrap-select/css/bootstrap-select.min.css" rel="stylesheet" />'+
	"<link rel='stylesheet'  href='/Content/assets/lib/bootstrap/css/bootstrap.min.css'>" +
	" <link href='/Content/assets/lib/bootstrap-table/css/bootstrap-table.min.css' rel='stylesheet'>"+
	"<link rel='stylesheet'  href='/Content/assets/lib/aYin/aYin.css'>" +
	"<link rel='stylesheet'  href='/Content/assets/lib/fontawesome-pro/css/fontawesome.css'>" +
	// "<link rel='stylesheet'  href='/Content/assets/css/main.css'>" +
	'<link href="/Content/js/datepicker/skin/WdatePicker.css" rel="stylesheet" />' +
	'<script src="/Content/assets/lib/bootstrap-select/js/bootstrap-select.min.js"></script>'+
	'<link href="/Content/assets/lib/fontawesome-pro/css/fontawesome.css" rel="stylesheet">' +
	'<link href="/context/css/hzsh-default.css" rel="stylesheet">'+

	" "
);

//全局变量
var global_host_$ = "/";

function cstmAlert(data) {
	$('.alert').html(data.message).addClass('alert-' + data.code).show().delay(1500).fadeOut();
}

function openWindow(href, title, height, width) {
	if (!href) {
		return
	}
	if (!title) {
		title = "";
	}
	if (!height) {
		height = 580;
	}
	if (!width) {
		width = 800;
	}
	let style = 'height=' + height + ', width=' + width +
		', top=60, left=400,toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=yes';
	window.open(href, title, style);
}
