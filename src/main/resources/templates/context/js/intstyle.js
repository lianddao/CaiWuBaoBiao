var styleSCN="style"; //styleSwith cookie name;
//$(function($) {}
//$(".content").ready(function(){IntSideNav();});
//$(".button").ready(function(){IntObject();});
//$(".button").ready(function(){IntObject();});
//$(".pointerWrap").ready(function(){IntPointer();});
//$(".tableList").ready(function(){IntTableList();});

$(".body").ready(function(){
  //严格按照此执行顺序IntPointer();IntObject();在执行所有方法前必须先执行这三个公共方法。
  IntImgSwitch();
  IntTableList();
  IntTableForm()
  IntSideNav();
  IntPointer();
//IntObject();
  IntObject2();
  IntPopup();
});

function IntLoading(act){
    //if($(".loading").length==0){$("body").prepend("<div class='loading'/>");}
    var load=$(".loading");
    //var imgs=$(".imgSwitch");
    if($(load).length==0){return;}
    if(act=="show"){
        //$(imgs).hide(0);
        $(load).show(0);
        $(load).fadeTo(0,0.9);
        $(load).height($(document).height());
        //$("html").css("overflow","hidden");
    }else if(act=="hide"){
        //$(imgs).show(0);
        $(load).fadeOut(500,function(){$("html").css("overflow","");});
    }
}
$().ready(function(){IntLoading("show");});
$("body").ready(function(){setTimeout(function(){IntLoading("hide");},1000);});



function IntObject(){//已废弃，被intObject2取代；
	IntLabel();IntBtnWIcon();IntLabelCute();
	var btnG=$(".button");
	$(btnG).each(function(i,btnG2){
			var btnChi=$(btnG2).children();
			var inputBtn=$(":button");
			var singleBtn=$(".button");
			var button=$(btnChi).add(inputBtn).add(singleBtn);
			
			$(button).each(function(j,button2){
				if($(button2).hasClass("noInt")==false){
					$(button2).addClass("btn");
					$(button2).bind("mouseenter",function(){$(this).addClass("hover");});
					$(button2).bind("mouseleave",function(){$(this).removeClass("hover");});
				}
			});
			
			$(btnChi).each(function(j,btnChi2){
				if(j==0&&$(btnChi).length>1){
					$(btnChi2).addClass("first");
				}else if(j==$(btnChi).length-1&&$(btnChi).length>1){
					$(btnChi2).addClass("last");
				}
			});
			if($(btnG2).hasClass("overAction")==false){
				if($(btnG2).hasClass("clickAction")){
					$(btnChi).bind("click",function(){
						var eobj=this;
						var parent=$(this).parent();
						var pchild=$(parent).children();
						$(pchild).each(function(i,pchild2){
							if(pchild2==eobj){$(pchild2).addClass("active")}
							else{$(pchild2).removeClass("active")}
						});
					});
				}
			}else if($(btnG2).hasClass("overAction")){
				$(btnChi).bind("mouseenter",function(){
					var eobj=this;
					var parent=$(this).parent();
					var pchild=$(parent).children();
					$(pchild).each(function(i,pchild2){
						if(pchild2==eobj){$(pchild2).addClass("active")}
						else{$(pchild2).removeClass("active")}
					});
				});
			}
	});
}

function IntObject2(){
    IntLabel();IntBtnWIcon();
    var btn=$(".button");
    var rab=$(".radioBtn");
    var ckb=$(".checkBtn");
    var ibtn=$(":button");
    var btnNC=$(".buttonNC")
    var all=$(btn).add(rab).add(ckb).add(ibtn).add(btnNC);
    $(all).each(function(x,all2){
        var judgType=function(){if($(all2).hasClass("button")){return "button"}else if($(all2).hasClass("radioBtn")){return "radio"}else if($(all2).hasClass("checkBtn")){return "check"}}
        var li=$(all2).children("li");
        if($(li).length>0){
            $(li).each(function(i,obj){
                if(i==0){$(obj).addClass("frist")}else 
                if(i==$(li).length-1){$(obj).addClass("last")}
                $(obj).addClass("btn");
                if($(obj).hasClass('noInt')==false){actions($(obj),judgType());}
            });
        }else{actions($(all2),judgType());}
    });

    function actions(obj,what){
        $(obj).bind("mouseenter",function(){$(this).addClass("hover")});
        $(obj).bind("mouseleave",function(){$(this).removeClass("hover")});
        if(what=="radio"){
            var judgeAction=function(){if($(obj).parent().hasClass("overAction")){return "mouseover"}else{return "click"}}
            $(obj).bind(judgeAction(),function(){
                var eobj=this;var bro=$(this).parent().children();
                $(bro).each(function(i,bro2){if(bro2==eobj){$(bro2).addClass("active");}else{$(bro2).removeClass("active");}});
            });
        }else if(what=="check"){$(obj).bind("click",function(){if($(this).hasClass("active")){$(this).removeClass("active")}else{$(this).addClass("active")}});
        }else if(what=="button"){$(obj).bind("click",function(){$(this).addClass("active");});}
    }
    
    var inputText=$(":text");
    var inputPass=$(":password");
    var inputTP=$(inputText).add(inputPass);
    $(inputTP).bind("focus",function(){$(this).removeClass("blur");$(this).addClass("focus");});
    $(inputTP).bind("blur",function(){$(this).removeClass("focus");$(this).addClass("blur");});

}


function IntSelect(){
    var select=$(".selectWrap");
    if($(select).length==0){return;}
    $(select).each(function(x,select2){
        var choose=$(select2).children(".selectChoose");
        var menu=$(select2).children(".selectSub");
        $(menu).width($(menu).width());
        var mli=$(menu).children("li");
        $(select2).mouseenter(function(){$(this).addClass("hover");});
        $(select2).mouseleave(function(){$(this).removeClass("hover")});
        $(select2).bind("click",function(){if($(menu).css("display")=="none"){$(menu).stop().fadeIn();}else{$(menu).stop().fadeOut();}});
        $(mli).mouseenter(function(){$(this).addClass("hover");});
        $(mli).mouseleave(function(){$(this).removeClass("hover")});
        $(mli).bind("click",function(){
            EventCutStr($(choose),$(this).text(),7,$(select2))
            $(select2).addClass("end");
        });
        $(menu).css("top",$(select2).height());
        if($(menu).width()<$(select2).width()){$(menu).width($(select2).width()-2);}
    });
}
$(function($){IntSelect()});

function IntBtnWIcon(){
	var btnWI=$(".buttonWIcon");
	if($(btnWI).length==0){return;}
	$(btnWI).each(function(i,btnWI2){
		var btn=$(btnWI2).children();
		$(btn).each(function(j,btn2){
			var span=$(btn2).children("span");
			var desc=$(span).eq(1);
			$(span).eq(0).addClass("icon");
			$(desc).addClass("desc");
			EventCutStr($(desc),$(desc).text(),7,$(btn2))
		});
	});
}

function IntLabel(){
	var labT=$(".labelTitle");
	if($(labT).length==0){return;}
	$(labT).each(function(x,labT2){
		var btnli=$(labT2).children("li");
		var border=0;
		$(btnli).each(function(j,btnli2){
			if(j==0){$(btnli2).addClass("lab1st");}else 
			if(j==$(btnli).length-1){$(btnli2).addClass("labEnd");}
			var judgeBorder=function(side){
				if(side=="left"){
					var leftB=parseInt($(btnli2).css("border-left-width"));
					if(leftB.toString()!="NaN"){return leftB;}else{return 0;}}else 
				if(side=="right"){
					var rightB=parseInt($(btnli2).css("border-right-width"));
					if(rightB.toString()!="NaN"){return rightB;}else{return 0;}}
			}
			var sureBorder=judgeBorder("right");
			border=border+judgeBorder("left")+judgeBorder("right");
		});
		if($(labT2).hasClass("noInt")==false){
			var noLastWidth=0;
			$(btnli).each(function(j,btnli2){
				if(j<$(btnli).length-1){
					$(btnli2).width(function(){return parseInt(($(labT2).width()-border)/$(btnli).length);});
					noLastWidth=noLastWidth+$(btnli2).width();}
				else{$(btnli2).width(function(){return parseInt($(labT2).width()-noLastWidth-border);});}
			});
		}
		
		var labw;
        if($(labT2).parent().has(".labelCon").length==1){labw=$(labT2).parent();}
        else{labw=$(labT2).closest(".labelWrap");}
		//var labT2=$(labw).find(".labelTitle");
        if($(labT2).hasClass("overAction")==false){$(labT2).addClass("clickAction")}
        var ltli=$(labT2).children("li");
        var lcon=$(labw).find(".labelCon");
        if($(lcon).length==0){return;}
        if($(lcon).parent().hasClass("scrollWrap")==false){$(lcon).wrap("<div class='scrollWrap'\/>")}
        var sw=$(labw).children(".scrollWrap");
        var lcli=$(lcon).children("li");
        $(ltli).first().addClass("active");
        $(lcli).addClass("labCLi");
        $(lcli).width($(labw).width());
        $(lcon).width($(lcli).width()*$(lcli).length);
        $(lcli).css("float","left");
        if($.browser.msie&&parseInt($.browser.version)<=7 ){$(sw).width($(lcli).width());}//修正ie8以下的版本
        $(lcli).each(function(i,lcli2){
            if($(lcli2).children().length==0){$(lcli2).append("<div class='tipNoCon'><\/div>");}
        });
        var judgeAct=function(){
            if($(labT2).hasClass("overAction")){
                return "mouseover";}else{return "click";}
        }
        $(ltli).bind(judgeAct(),function(){
            //alert(judgeAct())延时执行！！！！
            var eObj=this;
            $(ltli).each(function(i,obj){if(obj==eObj){$(sw).stop().animate({scrollLeft:i*$(sw).width()},300);}});
        });
	});
	IntLabelCute();
}

function IntLabelCute(){
	var btnlw=$(".labelCute");
	if($(btnlw).length==0){return;}
	$(btnlw).each(function(i,btnlw2){
		var btnli=$(btnlw2).children("li");
		if($(btnlw2).parent().is(".labelCuteWrap")==false){$(btnlw2).wrap("<div class='labelCuteWrap'/>")}
		$(btnli).each(function(j,btnli2){
			if(j==0){
				$(btnli2).addClass("btn1st");
				$(btnli2).append("<span class='btnCorner'><\/span>")
			}else if(j==$(btnli).length-1){
				$(btnli2).addClass("btnEnd");
				$(btnli2).append("<span class='btnCorner'><\/span>")
			}
			if(j!=0){$(btnli2).append("<span class='split'><\/span>")}
		});
	});
}

function IntTableList(){
    var table=$(".tableList");
    if($(table).length==0){return;}
    $(table).each(function(x,table2){
        var tr=$(table2).children("tbody").children("tr");
        var td=$(tr).children("td");
        var noCutStr=new Array();
        $(table2).attr({"cellSpacing":"0","cellPadding":"0"});
        $(tr).bind("mouseover",function(){$(this).addClass("hover");});
        $(tr).bind("mouseout",function(){$(this).removeClass("hover");});
        if($(table2).hasClass("withTitle")){
            $(tr).filter(":gt(0):even").addClass("odd");
            $(tr).filter(":gt(0):odd").addClass("even");
            $(tr).first().addClass("trHeader");
            $(tr).first().unbind("mouseover mouseout");
        }else{
            $(tr).filter(":even").addClass("odd");
            $(tr).filter(":odd").addClass("even");
        }
        $(tr).each(function(i,objtr){
            var tditr=$(objtr).children("td");
            $(tditr).each(function(j,objtd){
                if(j==0){
                    if($(objtd).hasClass("tdIcon")==false&&$(table2).hasClass("noIntIcon")==false){
                        if($(objtd).html()==""||($(objtd).children("span").length==1&&$(objtd).children("span").html()=="")){$(objtd).addClass("tdIcon");}
                        else{$(objtr).prepend("<td class='tdIcon'><span class='iconTable'><\/span><\/td>");}
                        td=$(objtr).children("td").first();
                        if($(td).children(".iconTable").length==0&&$(objtr).attr("class")!="trHeader"){
                            if($(td).children("span").length==0){
                                $(td).append("<span class='iconTable'><\/span>");
                            }else if($(td).children("span").text()==""){
                                $(td).children("span").addClass("iconTable");
                            }
                        }
                    }
                }
            });
        });
        var tr0td=$(tr).filter(":eq(0)").children("td");
        var tdw=new Array(); 
        var maxText=0;
        $(tr).each(function(i,objtr){
            var tditr=$(objtr).children("td");
            $(tditr).each(function(j,objtd){
                if($(objtd).hasClass("noCutStr")==true){noCutStr[j]="on"}
                $(objtd).addClass("td"+j);
                if(j==0){$(objtd).addClass("td1st");}
                else if(j==tr0td.length-2&&$(tditr).length>3){$(objtd).addClass("tdNTL");}
                else if(j==tr0td.length-1&&$(tditr).length>2){$(objtd).addClass("tdLast");}
                if($(objtd).html()==""){$(objtd).html("&nbsp;")}
            });
        });
        $(tr).each(function(i,objtr){
            $(objtr).addClass("tr"+i);
            var tra=$(objtr).find("a");
            $(objtr).children("td").each(function(j,objtd){
                if(i==0){tdw[j]=parseInt(($(objtd).width()/(parseInt($(objtd).css("font-size"))+1)));}
                var textobj;
                var child=$(objtd).children().filter(":first");
                if(child.length>0){textobj=child;}else{textobj=objtd};
                if(noCutStr[j]!="on"&&($(objtd).children("a").length==1||$(objtd).children().length==0)){
                    EventCutStr($(textobj),$(textobj).text(),tdw[j],$(objtd));
                }
            });
            if(tra.length==1&&$(tra).hasClass("noInt")==false){
                var ahref=$(tra).attr("href");
                $(objtr).bind("click",function(){window.open(ahref);});
                $(tra).removeAttr("href")
            }
        });
    });
    IntTableExub();IntTableDouble();
}


function IntTableDouble(){
	var table=$(".tableList");
	if($(table).length==0){return;}
	$(table).each(function(x,table2){
		if($(table2).hasClass("tableDouble")){
			var tr=$(table2).children("tbody").children("tr");
			$(tr).each(function(i,objtr){
				var tditr=$(objtr).children("td");
				$(tditr).each(function(j,objtd){
					if(j==1){
						var span=$(objtd).children("span");
						var span1=$(span).eq(0);
						$(span1).addClass("spanTitle");
						EventCutStr($(span1),$(span1).text(),22,$(objtd))
						$(span).filter(":eq(1)").addClass("spanDate");
					}
				});
			});
		}
	});
}

//Exuberant Table
function IntTableExub(){
	var table=$(".tableExub");
	if($(table).length==0){return;}
	$(table).each(function(x,table2){
		var tr=$(table2).children("tbody").children("tr");
		var td=$(tr).children("td");
		var tdExub=new Array();
        var tdexub=$(table2).data("tdexub");
		$(tr).each(function(i,objtr){
			var tditr=$(objtr).children("td");
			$(tditr).each(function(j,objtd){if(j+1==tdexub){tdExub[j]="on";}});
		});
		$(tr).each(function(i,objtr){
			var tditr=$(objtr).children("td");
			$(tditr).each(function(j,objtd){
				if(tdExub[j]=="on"){
					var div=$(objtd).children("div");
					$(div).eq(0).addClass("header");
					$(div).eq(1).addClass("article");
					EventCutStr($(div).eq(1),$(div).eq(1).text(),50);
					var span=$(div).eq(0).children("span");
					$(span).eq(0).addClass("title");
					EventCutStr($(span).eq(0),$(span).eq(0).text(),20,$(objtd));
					$(span).eq(1).addClass("date");
					
				}
			});
		});
	});
}

function IntPointer(){
	var potW=$(".pointerWrap");
	if($(potW).length==0){return;}
	$(potW).each(function(x,potW2){
		function scrollDir(){
			if($(potW2).hasClass("scrollH")){return "sTop";}else{return "sLeft";}}
		function findScrollGroup(){
			if($(potW2).parent().find(".scrollGroup").length>0){return $(potW2).parent().find(".scrollGroup");}
			else if($(potW2).parent().find(".scrollCon").length>0){return $(potW2).parent().find(".scrollCon").children();}
			else if($(potW2).prev().length>0){return $(potW2).prev();}
			else if($(potW2).next().length>0){return $(potW2).next();}}
		var scrollG=findScrollGroup();	if(scrollG==null){return;}
		var sDir=scrollDir();
		if($(scrollG).parent().hasClass("scrollCon")==false)
			{$(scrollG).wrap("<div class='scrollWrap'><div class='scrollCon'><\/div><\/div>");}
		var scrollC=$(scrollG).parent();
		var scrollW=$(scrollC).parent();
		var mainGroup=$(scrollC).children();
		if($(mainGroup).length==1){
			function judgeChild(){
				if($(mainGroup).is("table")==false){return $(mainGroup).children();}
				else{return $(mainGroup).children().children();}}
			var child=judgeChild();
			function findNum(){
				var num=$(mainGroup).eq(0).data("splitnum");
				if(num!=null&&parseInt(num).toString()!="NaN"){return parseInt(num);}
				else{return 6;}}
			var num=findNum();
			var GLength=Math.round($(child).length/num+0.4)-1;
			for(i=0;i<GLength;i++){$(mainGroup).clone().empty().appendTo(scrollC);}
			var group=$(mainGroup).parent().children();
			$(group).each(function(i,group2){
				if(i>0){$(child).each(function(j,child2){
					if((j)>=(i)*num&&(j+1)<=(i+1)*num){$(child2).appendTo($(group)[i]);
				}});}
			});}
		var group=$(mainGroup).parent().children();
		$(group).each(function(i,group2){
			if(i==0&&sDir=="sTop"){$(scrollW).height($(group2).outerHeight(true,true))}
			if(sDir=="sTop"&&i!=$(group).length-1){$(group2).height($(group).first().outerHeight(true,true));}
			$(group2).css("float","left");
			$(group2).width($(scrollW).width());
		});
		
		if(sDir=="sLeft"){$(scrollC).width($(group).length*$(mainGroup).width());}
		else{$(scrollC).height($(group).length*$(mainGroup).height());}
		
		var pArrow=$(potW2).children(".arrowList");
		if($(potW2).hasClass("arrowWrap")&&pArrow.length==0){$(potW2).append("<ul class='arrowList button'><li class='btn01 disable'></li><li class='btn02'></li><\/ul>");}
		var plist=$(potW2).children(".pointerList");
		if(plist.length==0){$(potW2).append("<ul class='pointerList radioBtn'><\/ul>");}
		var pointer=$(potW2).children(".pointerList");
		var arrow=$(potW2).children(".arrowList");
		if($(potW2).hasClass("scrollH")){$(arrow).addClass("arrowH")}else{$(arrow).addClass("arrowV")}
		var arrow01=$(arrow).children(".btn01");
		var arrow02=$(arrow).children(".btn02");
		var arrowBtn=$(arrow01).add(arrow02);
		
		var judgeAct=function(){if($(potW2).hasClass("clickAction")){return "click";}else{return "mouseover";}}
		$(arrowBtn).bind("click",function(){
			function judgePlMi(obj){if($(obj).hasClass("btn01")){return "-";}else{return "+";}}
			if(sDir=="sLeft"&&$(scrollW).is(":animated")!=true){$(scrollW).stop().animate({scrollLeft:eval($(scrollW).scrollLeft()+judgePlMi(this)+scrollW.width())},300,function(){PoArState()});}
			else if(sDir=="sTop"&&$(scrollW).is(":animated")!=true){$(scrollW).stop().animate({scrollTop:eval($(scrollW).scrollTop()+judgePlMi(this)+scrollW.height())},300,function(){PoArState()});}
		});		
		
		if(judgeAct()=="mouseover"){$(pointer).addClass("overAction")}else{$(pointer).addClass("clickAction")}
		$(pointer).empty();
		$(group).each(function(i){$(pointer).append("<li><\/li>");});
		var pli=$(pointer).children("li");
		$(pli).each(function(i,obj){
			if(i==0){$(obj).addClass("active");}
			$(obj).attr({title:"\u7B2C "+(i+1)+" \u9875"});
			$(obj).bind(judgeAct(),function(){
				if(sDir=="sLeft"){
					$(scrollW).stop().animate({scrollLeft:i*scrollW.width()},400,function(){PoArState()});
				}else if(sDir=="sTop"){
					$(scrollW).stop().animate({scrollTop:i*scrollW.height()},400,function(){PoArState()});
				}
			});
		});
		if($(potW2).hasClass("noPosition")==false){
			$(pointer).css({left:function(){return ($(potW2).width()-$(pointer).width())/2;}});
			$(pointer).css({top:function(){return ($(potW2).height()-$(pointer).height())/2;}});}
			
		function PoArState(){
			if(sDir=="sLeft"){
				if($(scrollW).scrollLeft()==0){$(arrow01).addClass("disable")}else{$(arrow01).removeClass("disable")}
				if($(scrollW).width()+$(scrollW).scrollLeft()==$(scrollC).width()){$(arrow02).addClass("disable")}else{$(arrow02).removeClass("disable")}
				var count=(($(scrollC).width()-($(scrollC).width()-$(scrollW).scrollLeft()))/$(scrollW).width());
				$(pli).each(function(i,pli2){if(i==count){$(pli2).addClass("active")}else{$(pli2).removeClass("active")}});
			}else if(sDir=="sTop"){
				if($(scrollW).scrollTop()==0){$(arrow01).addClass("disable")}else{$(arrow01).removeClass("disable")}
				if($(scrollW).height()+$(scrollW).scrollTop()==$(scrollC).height()){$(arrow02).addClass("disable")}else{$(arrow02).removeClass("disable")}
				var count=(($(scrollC).height()-($(scrollC).height()-$(scrollW).scrollTop()))/$(scrollW).height());
				$(pli).each(function(i,pli2){if(i==count){$(pli2).addClass("active")}else{$(pli2).removeClass("active")}});
			}
		}
	});
}

function IntSideNav_bak(){
	var sn=$(".sideNav");
	if($(sn).parent().hasClass("sideNavWrap")==false){$(sn).wrap("<div class='sideNavWrap'/>")}
	var snw=$(sn).parent();
	var p2ndC=$(snw).parent().children(".p2ndCon");
	var sList=$(sn).children("li");
	var p2w=$(snw).parent();
	var allLi;
	$(sList).each(function(i,sList2){
		$(sList2).addClass("li1st");
		var snlc=$(sList2).children();
		var sub=$(snlc).children();
		allLi=$(allLi).add(sList).add(sub);
		$(snlc).addClass("sideNavSub button");
		if($(snlc).children("li").length>0){$(sList2).addClass("withSub")}
		var clearSubA=function(eobj){
			$(sList).find(".sideNavSub").children().each(function(k,sub3){
				if(sub3!=eobj){$(sub3).removeClass("active")};
			});}
		$(sub).each(function(j,sub2){
			$(sub2).addClass("li2nd");
			$(sub2).bind("click",function(){var eobj=this;clearSubA(eobj);});
		});
		$(sList2).bind("click",function(){
			var eobj=this;
			$(sList).each(function(j,sList3){
				var sl3sub=$(sList3).find(".sideNavSub");
				if(sList3==eobj){$(sl3sub).slideDown();}
				else{$(sl3sub).slideUp();}
			});
			if($(this).hasClass("withSub")==false){clearSubA(eobj);}
		});
	});
	
	$(allLi).each(function(i,allLi2){
		var snlc=$(allLi2).children();
		$(snlc).detach();
		$(allLi2).wrapInner("<div class='liDiv'><span \/><\/div>");
		$(allLi2).find("span").addClass("desc");
		$(allLi2).find("span").parent().prepend("<span><\/span>");
		$(allLi2).find("span").first().addClass("icon2nd");
		$(allLi2).append(snlc);
	});
}

function IntSideNav(){
    var sn=$(".sideNav");
    if($(sn).parent().hasClass("sideNavWrap")==false){$(sn).wrap("<div class='sideNavWrap'/>")}
    var snw=$(sn).parent();
    var p2ndC=$(snw).parent().children(".p2ndCon");
    var sList=$(sn).children("li");
    var p2w=$(snw).parent();
    var allLi;
    $(sn).each(function(i,sn2){
        var all=$(sn2).find("*").andSelf();
        $(all).each(function(j,ano){
           var my=$(ano).parents("ul").parentsUntil(".sideNav").end();
           var step=$(my).length+1;
           if($(ano).is("ul")){
               $(ano).addClass("navUl navUl"+step);
               var chi=$(ano).children();
               $(chi).addClass("navLi navLi"+step);
                $(chi).each(function(j,chi2){
                    var snlc=$(chi2).children("ul");
                    $(snlc).detach();
                    $(chi2).wrapInner("<div class='navDiv'><span \/><\/div>");
                    $(chi2).find("span").addClass("desc");
                    $(chi2).find("span").parent().prepend("<span><\/span><span><\/span>");
                    $(chi2).find("span").first().addClass("iconSta");
                    $(chi2).find("span").first().next().addClass("icon");
                    $(chi2).append(snlc);
                    var navDiv=$(chi2).children(".navDiv");
                    $(navDiv).css("padding-left",10*(step)).addClass("navDiv"+step);
                    var subUl=$(chi2).children("ul");
                    if($(subUl).length>0){
                        $(navDiv).addClass("withSub");
                        $(subUl).hide();
                    }
                    $(chi2).bind("click",function(){
                        var eobj=this;
                        var bro=$(eobj).parent().children();
                        $(bro).each(function(k,bro2){
                           var subUl=$(bro2).children("ul");
                           var navDiv=$(bro2).children(".navDiv");
                           if(bro2==eobj){
                                if($(bro2).has("ul").length>0){
                                    $(subUl).slideToggle();
                                    $(navDiv).toggleClass("active");
                                }else{
                                    $(navDiv).addClass("active");
                                }
                           }else{
                               $(subUl).slideUp();
                               $(navDiv).removeClass("active");
                           }
                        });
                        var liBro=$(all).find(".navLi"+step);
                        $(liBro).each(function(l,liBro2){
                           var navDiv=$(liBro2).find(".navDiv");
                           var subUl=$(liBro2).children("ul");
                           var allUl=$(liBro2).find("ul");
                           if(liBro2!=eobj){
                               $(navDiv).removeClass("active");
                               $(subUl).slideUp("",function(){
                                   $(allUl).hide();
                               });
                           }
                        });
                        return false;
                    });
                    $(chi2).hover(function(){
                        $(navDiv).toggleClass("hover");
                    })
                });
           }
        });
    });
}

function IntPopup(){
    var ppw=$(".popupWrap");
    $(ppw).each(function(x,ppw2){
       var pp=$(ppw2).children(".popup");
       var ppl=$(pp).children(".popupList");
       var pplc=$(ppl).children();
       $(pplc).each(function(i,pplc2){
           if(i==0){$(pplc2).addClass("li1st");}
           if(i==$(pplc).length-1){$(pplc2).addClass("lilast");}
           $(pplc2).addClass("li");
       });
       $(ppw2).bind("mouseenter",function(){$(pp).stop(true,true).slideDown(200,function(){
           $(ppw2).bind("mouseleave",function(){$(pp).slideUp(200);});
       });});
       
    });
}

function EventCutStr(obj,str,num,title,relNum,dot){
	$(title).attr("title",str);
	var re=str.replace(/[^\x00-\xff]|\s*/img,"");
	var cnum=num+parseInt((re.length)/4);
	var dotjudge=function(){if(dot=="noDot"){return ""}else{return "..."}}
	var numjudge=function(){if(relNum=="RN"){return num}else{return cnum}}
	if(str.length > numjudge()&&str.length>5){
		 $(obj).text(str.substring(0,numjudge())+dotjudge());
    }else{$(obj).text(str);}
}

function IntTableForm(){
    function $cio(obj,clasNam){return obj.className.indexOf(clasNam)}
    function EventType(){
        if(event!=null&&event.type!="load"){
            return "cycle"}else{return "once"
        }
    }
    allTable=document.getElementsByTagName("table");
    for(i=0;i<allTable.length;i++){
        if($cio(allTable[i],"tableForm")>-1&&allTable[i].clientWidth>0){
            var tableParent=allTable[i].parentNode;
            var loadMark=allTable[i].loadMark;
            if(EventType()=="once"||loadMark==null){allTable[i].cellSpacing="0";allTable[i].cellPadding="0";}
            if( [i].oldClientWidth==null){
                allTable[i].oldClientWidth=tableParent.clientWidth;
            }
            
            allTable[i].style.width="100%";
            if(tableParent.children.length==1){allTable[i].style.height=tableParent.clientHeight-(parseInt(allTable[i].currentStyle.marginLeft)*2+parseInt(allTable[i].currentStyle.borderWidth)*2)+"px";}
            if($cio(allTable[i],"noInt")==-1){
                var trs=allTable[i].getElementsByTagName("tr");
                if(EventType()=="once"||loadMark==null){
                    allTable[i].loadMark="on";
                    for(j=0;j<trs.length;j++){
                        if(trs[j].className==""){
                            var tds=trs[j].getElementsByTagName("td");
                            if(allTable[i].TdMax==null||allTable[i].TdMax<tds.length){allTable[i].TdMax=tds.length;}
                            for(k=0;k<tds.length;k++){
                                if(j==2&&k!=tds.length-1){tds[k].style.width=allTable[i].clientWidth/tds.length+"px"}
                                if(tds[k].className==""){
                                    var tdText=tds[k].innerText;
                                    if(/^.*(:|\uFF1A)$/img.test(tdText)){
                                        tds[k].className="tfTitle";
                                        if(/^\*.*$/img.test(tdText)){
                                            var font=document.createElement("font");
                                            font.className="red";
                                            font.innerText="*";
                                            tds[k].innerHTML=tdText.replace(/^\*/img,"");
                                            tds[k].insertBefore(font,tds[k].childNodes[0]);
                                        }
                                        if(k!=tds.length-1){tds[k+1].className="tfData";}
                                    }
                                }
                                if((tds[k].width||tds[k].style.width)&&allTable[i].TdWidthInt==null){allTable[i].TdWidthInt="on";}
                            }
                        }
                    }
                }
                for(j=0;j<trs.length;j++){
                    var tds=trs[j].getElementsByTagName("td");
                    if(trs[j].className==""&&allTable[i].TdMax==2&&allTable[i].TdWidthInt==null){
                        for(k=0;k<tds.length;k++){
                            if(tds[k].className=="tfTitle"){
                                tds[k].style.width=100/allTable[i].TdMax-30+"%";
                            }else if(tds[k].className=="tf_data"){
                                tds[k].style.width=100/allTable[i].TdMax+30+"%";
                                allTable[i].TdWidthInt="on";
                            }
                        }
                    }else if(trs[j].className==""&&allTable[i].TdMax==tds.length&&allTable[i].TdWidthInt==null){
                        for(k=0;k<tds.length;k++){
                            if(tds[k].className=="tfTitle"){
                                tds[k].style.width=100/allTable[i].TdMax-5+"%";
                            }else if(tds[k].className=="tf_data"){
                                tds[k].style.width=100/allTable[i].TdMax+5+"%";
                                allTable[i].TdWidthInt="on";
                            }
                        }
                    }
                }
                for(j=0;j<trs.length;j++){
                    var tds=trs[j].getElementsByTagName("td");
                    if(trs[j].className==""){
                        for(k=0;k<tds.length;k++){
                            if(tds[k].className=="tfData"){
                                var tdChilds=tds[k].children;
                                var tdCPercent=(1-(parseInt(tds[k].currentStyle.padding)*2/tds[k].clientWidth)).toFixed(2)*100;
                                if(tdChilds.length==1){tdChilds[0].style.width=tdCPercent+"%";}
                                else{
                                    for(l=0;l<tdChilds.length;l++){
                                        tdChilds[l].clientwidth=tdChilds[l].clientWidth;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    //AttDetEvent("onclick",IntTableForm,"onresize",IntTableForm);
}


function IntImgSlider(){
    var isw=$(".imgSlider");
    $(isw).wrap("<div class='imgSliderWrap'\/>");
    var li=$(isw).children("li");
    var li0=$(li).eq(0);
    var space=Math.round(($(isw).width()-$(li0).width())/($(li).length-1));
    var sliderAction=function(i,li,way){
        $(li).each(function(j,li3){
            var fix=function(num){if(num==1){if(j<i){return 2}else if(j>=i){return 1}}else if(num==2){if(j<i){return $(li3).width()}else if(j>=i){return 0}}}
            var img3=$(li3).children("img");    var lidiv3=$(li3).children(".desc").add($(li3).children(".trans"));
            if(j==i){$(img3).fadeTo(200,1);}else{$(img3).fadeTo(0,0.3);$(lidiv3).stop(true,true).slideUp(0);}
            $(li3).stop().animate({right:($(li).length-(j+fix(1)))*space+fix(2)},600,function(){
                if(j==i){$(lidiv3).stop(true,true).delay(500).fadeIn(300);}
                if(way=="over"){if(j==i){$(li3).addClass("focus");}else{$(li3).removeClass("focus");}}
            });
        });
    }
    
    $(li).each(function(i,li2){
        var text=$(li2).children("div");
        var img=$(li2).children("img");
        $(text).addClass("desc");
        
        var h5=$(text).children("h5");
        $(h5).detach();
        $(text).wrapInner("<span\/>");
        $(text).prepend(h5);
        var det=$(text).children("span");
        $(det).text($.trim($(det).text()));
        EventCutStr($(h5),$(h5).text(),15,$(h5))
        EventCutStr($(det),$(det).text(),32,$(det))
        
        $(li2).prepend("<div class='trans'/>");
        $(li2).append("<div class='num'/>");
        var trans=$(li2).children(".trans");
        $(trans).css({width:$(li2).width(),height:$(text).outerHeight(true)})
        $(trans).fadeTo(0,0.5);
        var num=$(li2).children(".num");
        $(num).text(i+1);   
        if(space<30){$(num).width(space);}else{$(num).width(30);}
        if(i==$(li).length-1){$(li2).css("border","0")}
        var lidiv=$(text).add(trans);
        if(i!=0){$(img).fadeTo(0,0.3);$(lidiv).slideUp(0);}
        $(li0).addClass("focus");
        $(li2).css("z-index",($(li).length-i)+9);
        
        $(li2).css("right",function(){return ($(li).length-(i+1))*space;});
        $(li2).bind("mouseenter",function(){sliderAction(i,li,"over");});
    });
    
    var iswIntv;
    function setIntv(){
            iswIntv=window.setInterval(function(){
            var i=0;
            var currli=$(isw).children(".focus");
            $(li).each(function(j,li2){if(li2==$(currli)[0]){i=j;}});
            sliderAction(i,li);
            $(currli).removeClass("focus");
            if($(currli).next().length!=0){$(currli).next().addClass("focus");}else{$(li0).addClass("focus");}
        },5000);
    }
    setIntv();
    $(isw).bind("mouseenter",function(){window.clearInterval(iswIntv)});
    $(isw).bind("mouseleave",function(){setIntv();});
}
$(".imgSlider").ready(function(){IntImgSlider();});

function IntImgSwitch(){
    var imgs=$(".imgSwitch");
    $(imgs).each(function(i,imgs2){
        $(imgs2).wrap("<div class='imgSwitchWrap'/>");
        var imgSW=$(imgs2).parent();
        $(imgSW).width($(imgs2).outerWidth()).prepend("<ul class='numWrap radioBtn '/>");
        var numWrap=$(imgSW).children(".numWrap");
        var img=$(imgs2).children();
        for(ix=0;ix<img.length;ix++){$(numWrap).append("<li/>");}
        var num=$(numWrap).children();
        $(num).each(function(inum,num2){$(num2)
            .addClass("num"+inum)
            .append(inum+1).fadeTo(0,0.8)
            .bind("click",function(){
                var eobj=this;
                $(img).each(function(ini,img2){
                    ///////////////////////////////未完，好麻烦啊！
                });
            });
        })
        
        setTimeout(function(){$(img).eq(0).fadeIn(300,function(){$(img).not(":eq(0)").show(0);});},3000);
        $(img).each(function(j,img2){
            var imgf=$(img2).children();
            $(imgf).each(function(k,imgf){
               $(imgf).width($(imgs2).width()-2).height($(imgs2).height()-2);
            });
            var jj=j+1;
            if(j<9){jj="0"+(j+1)}
            $(img2).css("z-index",10-j);
            $(img2).addClass("img"+jj);
            if(j==0){$(img2).addClass("focus")}
        });
        var imgIntv=window.setInterval(function(){
            var foc=$(imgs2).children(".focus");
            $(img).each(function(j,img2){
                if($(img2).hasClass("focus")){
                    if($(img2).next().length!=0){$(img2).fadeOut(300);}
                    else{$(img).eq(0).addClass("focus");$(img).eq(0).fadeIn(300,function(){$(img).show(0);});}
                }
            });
            $(foc).removeClass("focus");
            $(foc).next().addClass("focus");
        },5000);
    })
}


function IntAniSwitch(){
    var aniS=$("#aniSwitch");
    if($.cookie("animation")!="off"){$(aniS).addClass("active");jQuery.fx.off=false;}else{jQuery.fx.off=true;}
    $(aniS).bind("click",function(){
        if($.cookie("animation")!="off"){$.cookie("animation","off",{expires:365});jQuery.fx.off=true;}
        else{$.cookie("animation","on",{expires:365});jQuery.fx.off=false;}
    });    
    if($("html").hasClass("IE6")||$("html").hasClass("IE7")){
       jQuery.fx.off=true;
    }
}



function IntStyleSwitch(){

    var styleS=$(".styleSwitch");
    var styleSC=$(styleS).children();
    if($.cookie("style")!=null){$("html").addClass($.cookie("style"));$(styleSC).each(function(i,ssc){if($(ssc).hasClass($.cookie("style"))){$(ssc).addClass("active");}});}else{$(styleSC).first().addClass("active")}

    $(styleSC).each(function(i,sSC2){
        var clasG=$(sSC2).attr("class").split(" ");
        var styleName;
        $.map(clasG,function(n){if($.indexOf(n,"style")){styleName=n};});
        $(sSC2).bind("click",function(){
            $.cookie("style",styleName,{expires:30});
            var htmlClass=$("html").attr("class").split(" ");
            $.map(htmlClass,function(n){if($.indexOf(n,"style")){$("html").removeClass(n)};});
            $("html").addClass(styleName);
        });
    });
}
$(function($){IntStyleSwitch()});


//function IntBrowserVer(){
   // var judgeBE=function(){
        //if($.browser.msie){
           // var bv=parseInt($.browser.version);
          //  if(bv==7&&navigator.appVersion.indexOf("Trident\/4.0")>0){bv=8}
         //   $("html").data("bv",bv);
        //    return "IE "+"IE"+bv;}
       // else if($.browser.safari){return "safari";}
      //  else if($.browser.opera){return "opera";}
    //    else if($.browser.mozilla){return "mozilla";}}
   // $("html").addClass(judgeBE());
//}
//IntBrowserVer();


//jquery Cookie
(function($, document) {
    var pluses = /\+/g;
    function raw(s) {return s;}
    function decoded(s) {return decodeURIComponent(s.replace(pluses, ' '));}
    $.cookie = function(key, value, options) {
        // key and at least value given, set cookie...
        if (arguments.length > 1 && (!/Object/.test(Object.prototype.toString.call(value)) || value == null)) {
            options = $.extend({}, $.cookie.defaults, options);
            if (value == null) {options.expires = -1;}
            if (typeof options.expires === 'number') {var days = options.expires, t = options.expires = new Date();
                t.setDate(t.getDate() + days);}
            value = String(value);
            return (document.cookie = [
                encodeURIComponent(key), '=', options.raw ? value : encodeURIComponent(value),
                options.expires ? '; expires=' + options.expires.toUTCString() : '', // use expires attribute, max-age is not supported by IE
                options.path    ? '; path=' + options.path : '',
                options.domain  ? '; domain=' + options.domain : '',
                options.secure  ? '; secure' : ''
            ].join(''));
        }
        options = value || $.cookie.defaults || {};
        var decode = options.raw ? raw : decoded;
        var cookies = document.cookie.split('; ');
        for (var i = 0, parts; (parts = cookies[i] && cookies[i].split('=')); i++) {
            if (decode(parts.shift()) === key) {return decode(parts.join('='));}}
        return null;};
    $.cookie.defaults = {};
})(jQuery,document);

//jquery settimeout
(function($,document){
    $.timeout=function(speed,fun,obj){
        if(typeof obj.timeID!="undefined"){clearTimeout(obj.timeID);}
        obj.timeID=setTimeout(fun,speed);}
    $.clearTimeout=function(obj){if(typeof obj.timeID!="undefined"){clearTimeout(obj.timeID);}}
})(jQuery,document);

//jquery indexOf
(function($,document){$.indexOf=function(obj,str){if(obj.indexOf(str)>-1){return true}else{return false}}})(jQuery, document);

