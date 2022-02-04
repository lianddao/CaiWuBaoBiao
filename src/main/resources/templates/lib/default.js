function 设置列缩进(value, row, index) {
    if (row.peiZhi.id == null) {
        row.peiZhi.cengCi = row.cengCi
        row.peiZhi.cengCi = row.peiZhi.cengCi <= 0 ? 1 : row.peiZhi.cengCi
    }

    return {
        css: {
            'text-indent': row.peiZhi.cengCi == null ? '0em' : (row.peiZhi.cengCi - 1) + 'em'
        }
    }
}



function 设置行样式(row) {
    if (row.peiZhi == null) {
        row.peiZhi = {}
        row.peiZhi.isBold = row.isBold
        row.peiZhi.isReadonly = row.isReadonly
        row.peiZhi.isHidden = row.isHidden
    }

    var classes = ''
    if (row.peiZhi.isBold) {
        classes += ' jia-cu-hang'
    }
    if (row.peiZhi.isReadonly) {
        classes += ' zhi-du-hang'
    }
    if (row.peiZhi.isHidden) {
        classes += " yin-cang-hang"
    }

    return {classes: classes.trim()}
}


function 设置只读效果() {
    var a = $("tr.zhi-du-hang").find(".editable")
    a.off("click")
    a.removeClass().css({paddingLeft: '5px'})
    return true
}




function 设置只读效果1(row, index) {
    if (row.peiZhi.isReadonly == null) {
        row.peiZhi.isReadonly = row.isReadonly
    }

    if (row.peiZhi.isReadonly) {
        return {"readonly-row": true}
    } else {
        return {}
    }
}
function 设置只读效果2() {
    $("tr[readonly-row]").find(".editable").off("click").removeClass()
    return true;
}




function 设置只读行不能编辑() {
    $("tr.zhi-du-hang").find(".editable").off("click").removeClass()
    return true;
}


function 只读单元格样式(value, row, index) {
    return {
        classes: "readonly-td"
    }
}




function 序号Formatter(value, row, index) {
    var option = $table.bootstrapTable('getOptions')
    var pageNumber = option.pageNumber
    var pageSize = option.pageSize
    return (pageNumber - 1) * pageSize + index + 1;
}


function 公式Formatter(value, row, index) {
    var div = $("<div class='div-gong-shi'></div>")
    div.text(value)
    return div[0].outerHTML
}






// 格式化为正数
function to_int(value, row) {
    if (value == null) {
        return null
    }

    return Math.round(value)
}


// 格式化为四舍五入保留2位小数
function to_2(value, row) {
    if (value == null) {
        return null
    }

    var rounded = Math.round((value + Number.EPSILON) * 100) / 100
    return rounded
}


// 格式化为百分比
function to_bai_fen_bi(value) {
    if (value == null) {
        return null
    }

    return parseFloat(value * 100).toFixed(0) + "%"
}





function bootstrapTable默认导出设置(day, name) {
    var options = {
        // ignoreColumn: [0,1],  //忽略某一列的索引
        fileName: day + name,
        tableName: name, // 导出为sql时的表名
        excelstyles: ['background-color', 'color', 'font-size', 'font-weight'],
        mso: {
            worksheetName: name,  //表格工作区名称
            xslx: {                 // Specific Excel 2007 XML format settings:
                formatId: {         // XLSX format (id) used to format excel cells. See readme.md: data-tableexport-xlsxformatid
                    date: 14,       // formatId or format string (e.g. 'm/d/yy') or function(cell, row, col) {return formatId}
                    numbers: 0      // formatId or format string (e.g. '\"T\"\ #0.00') or function(cell, row, col) {return formatId}
                }
            },
            onMsoNumberFormat: '\\@'
        }
    }
    return options
}


// 选择日期→搜索表格数据
function changeSearch(dp) {
    var v = dp.cal.getDateStr()
    $("#txtDate").val(v)
    $("#btnSearchDate").click()
}

