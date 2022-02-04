package com.hzsh.common.utils;

import cn.hutool.core.util.StrUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * <br/>=================================<br/>
 * 功能描述：xss攻击字典数据工具类
 
 * <br/>=================================<br/>
 */
public class XssDictUtils {

    // 防止xss攻击过滤字典map
    private static final Map<String, String> xssDicpMap = new HashMap<String, String>();

    /**
     *<br/>=================================<br/>
     *方法描述：防止xss攻击正则表达式字典收集
    
     *<br/>=================================<br/>
     *修改功能：添加对于null、NULL、0x0d、0x0a的替换规则
    
     *<br/>=================================<br/>
     *修改功能：null、NULL替换为""，打开<、>、&替换规则
     
     *<br/>=================================<br/>
     *修改功能：屏蔽&替换规则
     
     *<br/>=================================<br/>
     *修改功能：新增关键字过滤  load、create、union、rename
   
     *<br/>=================================<br/>
     *修改功能：新增关键字过滤  eval、and、or
   
     *<br/>=================================<br/>
     *修改功能：去掉关键字过滤  &
     *<br/>=================================<br/>
     */
    static {
        xssDicpMap.put("[s|S][c|C][r|R][i|I][p|P][t|T]", "");
        xssDicpMap.put("[\\\"\\\'][\\s]*[j|J][a|A][v|V][a|A][s|S][c|C][r|R][i|I][p|P][t|T]:(.*)[\\\"\\\']", "");
        xssDicpMap.put("[e|E][v|V][a|A][l|L]\\((.*)\\)", "");
        xssDicpMap.put("[v|V][b|B][s|S][c|C][r|R][i|I][p|P][t|T]", "");
        xssDicpMap.put("[s|S][o|O][u|U][r|R][c|C][e|E]", "");
//        xssDicpMap.put("<(\"[^\"]*\"|\'[^\']*\'|[^\'\">])*>", "");
        xssDicpMap.put("[e|E][x|X][p|P][r|R][e|E][s|S][s|S][i|I][o|O][n|N]", "");
//        xssDicpMap.put("[w|W][i|I][n|N][d|D][o|O][w|W]", "");
        xssDicpMap.put("[l|L][o|O][c|C][a|A][t|T][i|I][o|O][n|N]", "");
        xssDicpMap.put("[d|D][o|O][c|C][u|U][m|M][e|E][n|N][t|T]", "");
        xssDicpMap.put("[c|C][o|O][o|O][k|K][i|I][e|E]", "");
        xssDicpMap.put("[a|A][l|L][e|E][r|R][t|T]", "");
        xssDicpMap.put("[o|O][p|P][e|E][n|N]", "");
        xssDicpMap.put("[s|S][h|H][o|O][w|W][d|D][i|I][a|A][l|L][o|O][g|G]", "");
        xssDicpMap.put("[s|S][h|H][o|O][w|W][m|M][o|O][d|D][a|A][l|L][d|D][i|I][a|A][l|L][o|O][g|G]", "");
        xssDicpMap.put("[s|S][h|H][o|O][w|W][m|M][o|O][d|D][e|E][l|L][e|E][s|S][s|S][d|D][i|I][a|A][l|L][o|O][g|G]", "");
//        xssDicpMap.put("(window\\.location|window\\.|\\.location|document\\.cookie|document\\.|alert\\(.*?\\)|window\\.open\\()*", "");
        xssDicpMap.put("<+\\s*\\w*\\s*(oncontrolselect|oncopy|oncut|ondataavailable|ondatasetchanged|ondatasetcomplete|ondblclick|ondeactivate|ondrag|ondragend|ondragenter|"
                + "ondragleave|ondragover|ondragstart|ondrop|οnerrοr=|onerroupdate|onfilterchange|onfinish|onfocus|onfocusin|onfocusout|onhelp|onkeydown|onkeypress|"
                + "onkeyup|onlayoutcomplete|onload|onlosecapture|onmousedown|onmouseenter|onmouseleave|onmousemove|onmousout|onmouseover|onmouseup|onmousewheel|onmove|"
                + "onmoveend|onmovestart|onabort|onactivate|onafterprint|onafterupdate|onbefore|onbeforeactivate|onbeforecopy|onbeforecut|onbeforedeactivate|onbeforeeditocus|"
                + "onbeforepaste|onbeforeprint|onbeforeunload|onbeforeupdate|onblur|onbounce|oncellchange|onchange|onclick|oncontextmenu|onpaste|onpropertychange|"
                + "onreadystatechange|onreset|onresize|onresizend|onresizestart|onrowenter|onrowexit|onrowsdelete|onrowsinserted|onscroll|onselect|onselectionchange|"
                + "onselectstart|onstart|onstop|onsubmit|onunload)+\\s*=+", "");
        xssDicpMap.put("<", "&lt;");
        xssDicpMap.put(">", "&gt;");
//        xssDicpMap.put("&", "&amp;");
        xssDicpMap.put("[t|T][a|A][b|B][l|L][e|E]", "");
        xssDicpMap.put("[d|D][a|A][t|T][a|A][b|B][a|A][s|S][e|E]", "");
        xssDicpMap.put("[i|I][n|N][s|S][e|E][r|R][t|T]", "");
        xssDicpMap.put("[u|U][p|P][d|D][a|A][t|T][e|E]", "");
        xssDicpMap.put("[d|D][e|E][l|L][e|E][t|T][e|E]", "");
        xssDicpMap.put("[t|T][r|R][u|U][n|N][c|C][a|A][t|T][e|E]", "");
        xssDicpMap.put("[s|S][e|E][l|L][e|E][c|C][t|T]", "");
        xssDicpMap.put("[a|A][l|L][t|T][e|E][r|R]", "");
        xssDicpMap.put("[n|N][u|U][l|L][l|L]", "\"\"");
        xssDicpMap.put("[d|D][e|E][s|S][c|C]", "\"\"");
        xssDicpMap.put("0[x|X]0[d|D]", "");
        xssDicpMap.put("[l|L][o|O][a|A][d|D]", "\"\"");
        xssDicpMap.put("[r|R][e|E][n|N][a|A][m|M][e|E]", "");
        //xssDicpMap.put("[c|C][r|R][e|E][a|A][t|T][e|E]", "");
        xssDicpMap.put("[u|U][n|N][i|I][o|O][n|N]", "");
        xssDicpMap.put("[e|E][x|X][i|I][s|S][t|T][s|S]", "");
        xssDicpMap.put("[e|E][v|V][a|A][l|L]", "");
        xssDicpMap.put("[a|A][n|N][d|D]", "");
        xssDicpMap.put("[o|O][r|R] ", "");
    }


   
    public static String xssValid(String value) {
        if (StrUtil.isEmpty(value)) {
            return value;
        }
        for (String key : xssDicpMap.keySet()) {
            Pattern p = Pattern.compile(key);
            Matcher m = p.matcher(value);
            value = m.replaceAll(xssDicpMap.get(key));
        }
        return value;
    }

 
	/*
	 * public static void main(String[] args) { String temp = "asset_m_windows";
	 * System.out.println(XssDictUtils.xssValid(temp)); }
	 */
}

