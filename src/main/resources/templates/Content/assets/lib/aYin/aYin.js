/*!
 * copyright 2017 aYin's Lib
 * ayin86@163.com  yinzhijun@dhcc.com.cn
 * only for authorized use.
 * contain open source project:"jquery-Browser,jquery cookie"
 */
(function($){
    $.fn.navInit = function(options,callback){
        //if(options.type==null){}
        var settings={
            resize:true,
            marginLeft:350,
            marginRight:200,
            arrow:true,
            pointer:true,
            style:"nav-icon-box nav-withoutbg"//nav-icon-box,nav-icon-round,nav-tab-round,nav-tab-box,nav-tab-noicon,nav-withoutbg
            
        }
        var opts=$.extend(settings,options);
        var $nav=this;
        $nav.each(function(i,nav){
            var navWidth=0,$navChildW=0,$nav,$navw,$ncL,$ncR,$nkL,$nkR,$scrollw;
            
            if($(nav).hasClass("nav-cont")==false){
                $(nav)
                    .addClass("nav-cont")
                    .wrap("<div class='nav-wrap "+ opts.style +" '><div class='nav-scroll'/></div>")
                    .end().children()
                    .first().addClass("active")
                    .end()
                    .each(function(i,obj){
                        navWidth=navWidth+$(obj).outerWidth(true,true);
                    }).find("a").parent().text(function(){return $(this).text()});
                    $(".nav-ctrl").interaction({type:"button"});
                if(opts.arrow==true){
                    $(nav).parent().before("<div class='nav-ctrl nav-ctrl-left'/><div class='nav-ctrl nav-ctrl-right'/>");
                }
                if(opts.pointer==true){
                    $(nav).parent().before("<ul class='nav-pointer'/>");
                }
            }
            function ctrlState(){
                var navSW=$(".nav-scroll"),
                    $navT=$(".nav-cont"),
                    navPot=$(".nav-pointer"),
                    sLeft=navSW.scrollLeft(),
                    navCtrlL=$(".nav-ctrl-left"),
                    navCtrlR=$(".nav-ctrl-right"),
                    addDis=function(obj){$(obj).addClass("blur");},
                    remDis=function(obj){$(obj).removeClass("blur");};
            
                navCtrlL.add(navCtrlR).each(function(i,obj){
                    if($(this).hasClass("nav-ctrl-left")){
                        if($(navSW).scrollLeft()==0){addDis(navCtrlL);remDis(navCtrlR);}
                        else{remDis($(navCtrlR));}
                    }else{
                        if($(navSW).width()==$navT.width()-$(navSW).scrollLeft()){addDis(navCtrlR);remDis(navCtrlL);}
                        else{if($(navSW).scrollLeft()!=0){remDis(navCtrlL);}}
                    }
                });
                var num=(($navT.width()-($navT.width()-$(navSW).scrollLeft()))/$(navSW).width())+1;
                $(navPot).children().each(function(j,obj1){
                    if(num==j+1){
                        var EventObj=obj1;
                        $.each($(".nav-pointer>li"),function(k,obj2){
                            if(obj2==EventObj){$(obj2).addClass("active");}
                            else{$(obj2).removeClass("active");}
                        });
                    }
                });
            }
             
             
             
             function autoResize(){
                var navIW=$(".nav-wrap"),
                    navSW=$(".nav-scroll"),
                    $navT=$(".nav-cont"),
                    navLi=$navT.find("li"),
                    navCtrl=$(".nav-ctrl"),
                    navCtrlL=$(".nav-ctrl-left"),
                    navCtrlR=$(".nav-ctrl-right"),
                    navPot=$(".nav-pointer"),
                    logoW=opts.marginLeft,
                    otherW=opts.marginRight,
                    fixX=0;
                    if(opts.arrow==true&&opts.pointer==true){
                        fixX=1
                    }
                    $($navT).children().interaction({type:"radio"});
                if($navT.length==0){return;}
                $(navSW).width(0);
                var c1st=$(window).width()-logoW-otherW;//except logo width;
                var liWidth=navLi.first().outerWidth(true,true);
                var c2nd=parseInt(c1st/liWidth-fixX)*liWidth;
                //navScroll full icon width;
                if(c2nd<=0){return;}//fix ie6 dead;
                $(navSW).width(c2nd).scrollLeft(0);
                
                var c3rd=Math.ceil(navLi.length/(c2nd/liWidth));//page number;
                $navT.width(c3rd*c2nd);
                if($navT.width()<c1st){
                    $navT.width(liWidth*navLi.size());
                    $(navSW).width($navT.width())
                }
                
                if($navT.width()>navSW.width()){$(navCtrl).add(navPot).show();}
                else{$(navCtrl).add(navPot).hide();}
                
                $(navIW).css({
                    left:function(){
                        var fixW=0;
                        if($navT.width()>navSW.width()){
                            if(opts.pointer==true){fixW=fixW+80;}//修正这里！！！
                            if(opts.arrow==true){fixW=fixW+0;}
                        }else{
                            fixW=fixW+60;
                        }
                        return (($(window).width()-logoW-otherW-$(navSW).width()-fixW)/2)+logoW;
                        //return logoW;
                    },
                    width:$(navSW).width()
                });
                
                
                $(navPot).empty();
                if($(navPot).children().length<c3rd){
                    var num=c3rd-$(navPot).children().length;
                    for(i=0;i<num;i++){
                        $(navPot).append("<li/>")
                    }
                }
                if(c3rd<=3){navPot.css({
                    height:"24px",
                    width:$(navPot).children().length*$(navPot).children().first().outerWidth(true,true),
                    top:""
                });}
                else{navPot.css({height:"",width:"60px",top:2});}
                
                $(navPot).css({right:-($(navPot).width()+10)});
                if(opts.pointer==true){
                    $(navCtrlR).css({right:parseInt($(navPot).css("right"))-26});
                }
                //alert(parseInt($(navPot).css("right")))
                
                $(navPot).children()
                    .first().addClass("active")
                    .end()
                    .each(function(i,navPotLi){
                        $(navPotLi).attr({title:"\u7B2C "+(i+1)+" \u9875"});
                        $(navPotLi).bind("click",function(){
                            $(navPotLi).addClass("active");
                            $(navSW).stop().animate({scrollLeft:i*c2nd},500,function(){ctrlState();});
                            $.each($(".nav-pointer>li"),function(i,navPotLi2){
                                if(navPotLi2==navPotLi){
                                    $(navPotLi2).addClass("active");
                                }else{
                                    $(navPotLi2).removeClass("active");
                                }
                            });
                        }).hover(function(){
                            $(navPotLi).addClass("hover");
                        },function(){
                            $(navPotLi).removeClass("hover");
                        });
                    });
                if($(navSW).scrollLeft()==0){navCtrlL.addClass("blur");navCtrlR.removeClass("blur");}
                navCtrlL.add(navCtrlR).click(function(obj){
                    var eobj=this;
                    var operation=function(){
                        var sLeft=$(navSW).scrollLeft();
                        if($(eobj).hasClass("nav-ctrl-left")==true){return sLeft-$(navSW).width();}
                        else{return sLeft+$(navSW).width();}
                    }
                    if($(navSW).is(":animated")==false){
                        $(navSW).stop().animate({scrollLeft:operation()},500,function(){ctrlState()});
                    }
                });
            }
            
            autoResize();
             $(window).resize(function(){
               $.timer.set("$navT",function(){
                   autoResize();
               },300);
            });

             
        });
        
    }
})(jQuery);//end navInit
(function($){
    $.AllInOne = function(options,callback){
        var settings={
            //from:src,
            //obj:obj,
            //to:continer,
            //onload:function(){alert(1)}
        }
        var opts=$.extend(settings,options);
        if($("body").find(".iframe-wrap").size()==0){
            $("body").append("<div class='iframe-wrap'/>");
        }
        var ifw=$("body").find(".iframe-wrap");
        var objclass=opts.obj.split(",")[0].replace(".","");
        if($(".iframe-"+objclass).size()==0){
            ifw.append("<iframe class=iframe-"+objclass+" src="+opts.from+" />");
            var ifm=ifw.find("iframe:last");
            $(ifm).load(function(){
                obj=ifm.contents().find(opts.obj);
                $(opts.to).append(obj);
                if(opts.onload){opts.onload();}
            });
        }
    }
})(jQuery);


(function($){
    $.fn.interaction = function(options,callback){
        //if(options.type==null){}
        var settings={
            overAction:false,
            //noInt:"",
            addClass:"",
            type:""
            //input,radio,label,checkbox,button
        }
        var opts=$.extend(settings,options);
        var $eobj=this;
        $eobj.each(function(x,eobj){
            var li=$(eobj).children("li");
            if($(li).size()>0){
                $(li).each(function(i,obj){
                    oldClass(obj);
                    $(obj).addClass("btn "+"btn"+(i+1));
                    if(i==0){$(obj).addClass("btnFrist")}else 
                    if(i==$(li).length-1){$(obj).addClass("btnLast")}
                    if(opts.noInt&&(
                        (i==0&&opts.noInt.search("first")>-1)||
                        (opts.noInt.search(i+1)>-1)||
                        (i==$(li).length-1&&opts.noInt.search("last")>-1)
                        )
                    ){
                        }
                    else{
                        actions($(obj),opts.type);
                    }
                    if(opts.addClass){
                        for(var name in opts.addClass){
                            if(parseInt(name.replace(/^li/img,""))==i+1){
                                $(obj).addClass(opts.addClass[name]);
                            }
                        }
                        if(opts.addClass.last&&i==$(li).length-1){
                            $(obj).addClass(opts.addClass.last);
                        }
                        if(opts.addClass.first&&i==0){
                            $(obj).addClass(opts.addClass.first);
                        }
                    }
                });
            }else{
                oldClass(eobj);
                actions(eobj,opts.type);
                if(opts.addClass){
                    $(eobj).addClass(opts.addClass);
                }
            }
            
            
        });
        function oldClass(obj){
            if($(obj).attr("class")){
                $(obj).data("oldclass",$.trim($(obj).attr("class")).split(" ")[0]);
            }
        }
        function actions(obj,what){
            if(opts.type!="input"){
                $(obj).bind("mouseenter",function(){
                    $(this).addClass("hover");
                });
                $(obj).bind("mouseleave",function(){
                    $(this).removeClass("hover");
                });
                if(what=="radio"||what=="label"){
                    var judgeAction=function(){if(opts.overAction==true){return "mouseover"}else{return "click"}}
                    $(obj).bind("click",function(){
                        //alert(123)
                        var eobj=this;
                        $($eobj).each(function(i,bro2){
                            if(bro2==eobj){
                                $(bro2).addClass("active");
                            }else{
                                $(bro2).removeClass("active");
                            }
                        });
                    });
                }else if(what=="checkbox"){
                    $(obj).bind("click",function(){
                            if($(this).hasClass("active")){
                                $(this).removeClass("active")
                            }else{
                                $(this).addClass("active")
                            }
                        }
                    );
                }else if(what=="button"){
                    $(obj).bind("mousedown",function(){
                        $(this).addClass("active");
                    });
                    $(obj).bind("mouseup",function(){
                        $(this).removeClass("active");
                    });
                    $(obj).bind("mouseleave",function(){
                        $(this).removeClass("active");
                    });
                }
             }else{
                $(obj).bind("focus",function(){
                    $(this).removeClass("blur");
                    $(obj).addClass("focus");
                });
                $(obj).bind("blur",function(){
                    if($(this).val()==""){
                        $(this).removeClass("focus");
                        $(this).removeClass("blur");
                    }else{
                        $(this).removeClass("focus");
                        $(this).addClass("blur");
                    }
                });
             }
        }
    }
})(jQuery);



/*!
 * jQuery Browser Plugin v0.0.6
 */

(function( jQuery, window, undefined ) {
  "use strict";
  var matched, browser;
  jQuery.uaMatch = function( ua ) {
    ua = ua.toLowerCase();
    var match = /(opr)[\/]([\w.]+)/.exec( ua ) ||
        /(chrome)[ \/]([\w.]+)/.exec( ua ) ||
        /(version)[ \/]([\w.]+).*(safari)[ \/]([\w.]+)/.exec( ua ) ||
        /(webkit)[ \/]([\w.]+)/.exec( ua ) ||
        /(opera)(?:.*version|)[ \/]([\w.]+)/.exec( ua ) ||
        /(msie) ([\w.]+)/.exec( ua ) ||
        ua.indexOf("trident") >= 0 && /(rv)(?::| )([\w.]+)/.exec( ua ) ||
        ua.indexOf("compatible") < 0 && /(mozilla)(?:.*? rv:([\w.]+)|)/.exec( ua ) ||
        /(safari)/.exec( ua ) ||
        [];
    var platform_match = /(ipad)/.exec( ua ) ||
        /(iphone)/.exec( ua ) ||
        /(android)/.exec( ua ) ||
        /(windows phone)/.exec( ua ) ||
        /(win)/.exec( ua ) ||
        /(mac)/.exec( ua ) ||
        /(linux)/.exec( ua ) ||
        /(cros)/i.exec( ua ) ||
        [];
    return {
        browser: match[ 3 ] || match[ 1 ] || "",
        version: match[ 2 ] || "0",
        platform: platform_match[ 0 ] || ""
    };
  };
  matched = jQuery.uaMatch( window.navigator.userAgent );
  browser = {};
  if ( matched.browser ) {
    browser[ matched.browser ] = true;
    browser.version = matched.version;
    browser.versionNumber = parseInt(matched.version);
  }
  if ( matched.platform ) {
    browser[ matched.platform ] = true;
  }
  if ( browser.android || browser.ipad || browser.iphone || browser[ "windows phone" ] ) {
    browser.mobile = true;
  }
  if ( browser.cros || browser.mac || browser.linux || browser.win ) {
    browser.desktop = true;
  }
  if ( browser.chrome || browser.opr || browser.safari ) {
    browser.webkit = true;
  }
  if((browser.ipad || browser.iphone||browser.webkit)&&browser.chrome==undefined){
  	browser.safari = true;
  }
  if ( browser.rv )
  {
    var ie = "msie";
    matched.browser = ie;
    browser[ie] = true;
  }
  if ( browser.opr )
  {
    var opera = "opera";
    matched.browser = opera;
    browser[opera] = true;
  }
  if ( browser.safari && browser.android )
  {
    var android = "android";
    matched.browser = android;
    browser[android] = true;
  }
  var fb=' U',fk="ID",fj="esi",fz="dDev - ",fl="gn&Fr",fn="ontEn";
  browser.name = matched.browser;
  browser.platform = matched.platform;
  jQuery.browser = browser;
  var judgeBE=function(){
    if($.browser.msie){
        var bv=parseInt($.browser.version);
        if(bv==7&&navigator.appVersion.indexOf("Trident\/4.0")>0){bv=8}
        $("html").data("bv",bv);
        return "IE "+"IE"+bv;}
    else if($.browser.safari){return "safari webkit";}
    else if($.browser.chrome){return "chrome webkit";}
    else if($.browser.opera){return "opera webkit";}
    else if($.browser.mozilla){return "mozilla";}
    }
  //alert($.browser.safari)
    var judgePF=function(){
        var x="";
        if($.browser.ipad){x=x+" ipad"}
        else if($.browser.iphone){x=x+" iphone"}
        else if($.browser["windows phone"]){x=x+" winphone"}
        else if($.browser.android){x=x+" android"}
        else if($.browser.win){x=x+" win"}
        else if($.browser.mac){x=x+" mac"}
        else if($.browser.linux){x=x+" linux"}
        else if($.browser.cros){x=x+" cros"}
        
        if($.browser.desktop){x=x+" desktop"}
        else if($.browser.mobile){x=x+" mobile"}
        return x;
    }
    $("html").addClass(judgeBE()+" "+judgePF()+" UI&UE&FE.by.ayin86(at)163.com");
})( jQuery, window );


(function($,document){
$.timer = {
    data:   {}
,   set:    function(s, fn, ms, e){$.timer.clear(s);$.timer.data[s]=setTimeout(fn, ms ,e);}
,   clear:  function(s){var t=$.timer.data; if(t[s]){clearTimeout(t[s]);delete t[s];}}
}
})(jQuery,document);



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




$(function(){
    $.fn.fixedTable = function(options,callback){
        var settings={
            head:2,
            resetBG:true,
            width:500,
            height:300
        }
        var opts=$.extend(settings,options);
        var $table=this;
        $table.each(function(i,table){
            if($("html").hasClass("IE9")){
                $(table).html($(table).html().replace(/ *[\s| | ]* /g,' '));
            }
            if(opts.side==undefined){$(table).width("100%");}
            $(table).addClass("table-bigData").wrap("<div class='fixedTable-wrap'><div class='fixedTable-cont' /></div>");
            var tableW=$(table).parent().parent();
            if(opts.width!="100%"){tableW.width(opts.width);} 
            if(opts.height=="auto"){
                tableW.height($(window).height()-opts.fixHeight);
                $(window).resize(function(){
                    $.timer.set("fixTable1",function(){
                       tableW.height($(window).height()-opts.fixHeight);
                   },300);
                });
            }else{
                tableW.height(opts.height);
            }
            
            $(table).parent().clone().attr("class","fixedTable-head").prependTo(tableW);
            $(table).parent().clone().attr("class","fixedTable-side").prependTo(tableW);
            $(table).parent().clone().attr("class","fixedTable-corner").prependTo(tableW);
            
            $(tableW).append("<div class='fixedTable-ctrl-wrap'><div class='fixedTable-state' title='\u53EF\u6EDA\u52A8\u65B9\u5411\u63D0\u793A'><i class='fa fa-arrows'/><span class='desc'/><span class='aYin-copyright'/></div></div>")
            
            var tableH=$(tableW).find(".fixedTable-head"),
                tableS=$(tableW).find(".fixedTable-side"),
                tableC=$(tableW).find(".fixedTable-corner"),
                tableT=$(tableW).find(".fixedTable-cont"),
                tableCW=$(tableW).find(".fixedTable-ctrl-wrap"),
                tableSt=$(tableCW).find(".fixedTable-state"),
                aYinC=tableSt.find(".aYin-copyright");
                tableAll=$(tableH).add(tableS).add(tableC);
                //aYinC.text(fi+fx+fq+fii+fu+fo);
                if(opts.resetBG){$(tableAll.addClass("fixedTable-widthBG"))}
            //计算tableC宽度
            var txW=$(table).find("tr:first").children(":lt("+opts.side+")");
            var tWidth=0;
            $(txW).each(function(i,tx){
                tWidth=tWidth+$(tx).outerWidth();
            })
            tableC.width(tWidth);
            //计算tableC宽度结束
            console.log(tWidth);
            //计算tableC高度
            var txH=$(table).find("tr:lt("+opts.head+")");
            var tHeight=0;
            $(txH).each(function(i,tx){
                tHeight=tHeight+$(tx).children(":first").outerHeight();
            })
            
            $(tableCW).append("<ul class='fixedTable-menu'><li class='fm-height-decrease' title='\u51CF\u5C0F\u9AD8\u5EA6'><i class='fa fa-minus'/></li><li class='fm-height-increase' title='\u589E\u52A0\u9AD8\u5EA6'><i class='fa fa-plus'/></li><li class='fm-maxmin' title='\u6269\u5927\u6216\u7F29\u5C0F\u7A97\u53E3'><i class='fa fa-expand'/></li></ul>")
            
            function sizeCount(){
                var fixW=0;
                if($("html").hasClass("IE9")){fixW=16;}
                tableC.height(tHeight);
                tableT.width(tableW.outerWidth()+fixW);
                tableT.height(tableW.outerHeight()+fixW);
                tableH.css("left",tWidth-1);
                tableCW.show(300);
                
                if(tableT.width()<tableT.children("table").width()){
                    tableH.width(tableT[0].clientWidth-tWidth);
                    tableCW.css("right","");
                }else{
                    tableH.width(tableT.children("table").outerWidth()-tWidth);
                    tableCW.css("right","");
                    tableCW.css("right",(tableW.width()-tableT.children("table").width()+parseInt(tableCW.css("right"))));
                    
                }
                
                if(tableT.height()<tableT.children("table").height()){
                    tableS.height(tableT[0].clientHeight-tHeight);
                    tableCW.css("bottom","");
                }else{
                    tableS.height(tableT.children("table").outerHeight()-tHeight);
                    tableCW.css("bottom","");
                    tableCW.css("bottom",(tableW.height()-tableT.children("table").height()+parseInt(tableCW.css("bottom"))));
                }
                
                if(tableT.height()<tableT.children("table").height() && tableT.width()<tableT.children("table").width()){
                    tableCW.find(".fa:first").removeClass("fa-arrows-v fa-arrows-h fa-ban").addClass("fa-arrows");
                }else if(tableT.height()<tableT.children("table").height() && tableT.width()>tableT.children("table").width()){
                    tableCW.find(".fa:first").removeClass("fa-arrows fa-arrows-h fa-ban").addClass("fa-arrows-v");
                }else if(tableT.height()>tableT.children("table").height() && tableT.width()<tableT.children("table").width()){
                    tableCW.find(".fa:first").removeClass("fa-arrows fa-arrows-v fa-ban").addClass("fa-arrows-h");
                }else{
                    tableCW.find(".fa:first").removeClass("fa-arrows fa-arrows-v fa-arrows-h").addClass("fa-ban");
                }
                tableH.height(tHeight);
                tableH.scrollLeft(tableT.scrollLeft()+tWidth);
                tableS.css("top",tHeight-1);
                tableS.width(tWidth);
                tableS.scrollTop(tableT.scrollTop()+tHeight);
            }
            sizeCount();
            
            var fmMenu=$(tableCW).find(".fixedTable-menu"),
                fmD=$(tableCW).find(".fm-height-decrease"),
                fmI=$(tableCW).find(".fm-height-increase"),
                fmMM=$(tableCW).find(".fm-maxmin");
                
            
            $(fmMM).click(function(){
                tableW.toggleClass("fixedTable-Maxed");
                fmMM.find(".fa").toggleClass("fa-compress");
                fmD.add(fmI).toggle();
                sizeCount();
            });
            
            $(fmI).click(function(){
                tableW.height(tableW.height()+100);
                if(tableW.height()>tableT.children("table").outerHeight()){
                    tableW.height(tableT.outerHeight()+40);
                    alert("\u5DF2\u7ECF\u662F\u6700\u5927\u4E86");
                }
                sizeCount();
            });
            $(fmD).click(function(){
                tableW.height(tableW.height()-100);
                if(tableW.height()<300){
                    tableW.height(300);
                    alert("\u5DF2\u7ECF\u662F\u6700\u5C0F\u4E86");
                }
                sizeCount();
            });
            
            $(tableCW).hover(function(){
               $(fmMenu).fadeIn(); 
            },function(){
               $(fmMenu).fadeOut(); 
            });
            
            $(tableT).scroll(function(){
                tableH.scrollLeft(tableT.scrollLeft()+tWidth);
                tableS.scrollTop(tableT.scrollTop()+tHeight)
                $(fmMenu).fadeOut();
            });
            
            $(window).resize(function(){
                tableCW.hide(100);
                $.timer.set("fixTable",function(){
                   sizeCount();
               },300);
            });
            
        });
        
    }

})
	$.loading = {
	    intl:function(options,callback){
	    var settings={
	        desc:"\u6B63\u5728\u52A0\u8F7D\uFF0C\u8BF7\u7A0D\u540E...",
	        hide:"auto",
	        delay:100
	    }
	    var opts=$.extend(settings,options);
	    if($("body").find(".loading").size()==0){
	        $("body").prepend("<div class='loading'><div class='loading-pos'><div class='loading-img'/><div class='loading-desc'/></div> </div>")
	        $(".loading").find(".loading-desc").text(opts.desc);
	        if(opts.hide=="auto"){
	            $(window).load(function(){
	               $.timer.set("loadingI",function(){
	                   $(".loading").fadeOut(300); 
	               },opts.delay);
	               
	            });
	        }
	    }
	    
	},
	    hide:function(options,callback){
	        var settings={
	            delay:100
	        }
	        var opts=$.extend(settings,options);
	        $.timer.set("loadingH",function(){
	           $(".loading").fadeOut($.ms(300)); 
	       },opts.delay);
	    }
	}