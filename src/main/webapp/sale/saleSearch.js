function formatUser(val,row){
    return val.trueName+"&nbsp;("+val.userName+")&nbsp;";
}


function searchSale(){
    $("#dg2").datagrid("loadData",{total:0,rows:[]});
    var customerId;
    if(isNaN($("#s_customer").combobox("getValue"))){
        customerId="";
    }else{
        customerId=$("#s_customer").combobox("getValue");
    }
    $("#dg").datagrid({
        queryParams:{
            contractNumber:$("#s_saleNumber").val(),
            "customer.id":customerId,
            state:$("#s_state").combobox("getValue"),
            bSaleDate:$("#s_bSaleDate").datebox("getValue"),
            eSaleDate:$("#s_eSaleDate").datebox("getValue")
        }
    });
}

function formatSupplier(val,row){
    return val.name;
}

function formatShopName(val, row) {
    return val.name + "-" +  val.name2;
}

function formatAmountPayable(val,row){
    if (val) {
        return "¥"+val.toFixed(2);
    } else {
        return "¥0.00"
    }
}

function formatUser(val,row){
    return val.trueName;
}

function deleteSale(){
    var selectedRows=$("#dg").datagrid("getSelections");
    if(selectedRows.length!=1){
        $.messager.alert("系统提示","请选择一条要删除的销售单！");
        return;
    }
    var id=selectedRows[0].id;
    $.messager.confirm("系统提示","<font color=red>删除销售单后将无法恢复，是否删除?</font>",function(r){
        if(r){
            $.post("/admin/saleList/delete",{id:id},function(result){
                if(result.success){
                    $("#dg").datagrid("reload");
                    $("#dg2").datagrid("reload");
                }else{
                    $.messager.alert("系统提示",result.errorInfo);
                }
            },"json");
        }
    });
}


$(document).ready(function() {

    $("#s_bSaleDate").datebox("setValue",genLastMonthDayStr()); // 设置上个月日期
    $("#s_eSaleDate").datebox("setValue",genTodayStr()); // 设置当前日期

    $("#dg").datagrid({
        onClickRow:function(index,row){
            $("#dg2").datagrid({
                url:'/admin/saleList/listGoods',
                queryParams:{
                    saleListId:row.id
                }
            });
        }
    });

    $.each($("span[hasRole]"), function() {
        var role = $(this).attr('hasRole');
        var self = $(this);

        $.get('/user/hasRole', {name: role}, function(data) {
            if (!data.hasRole) {
                self.empty();
            }

        }, 'json');
    });

});

function modifyAmount() {
    var selectedRows=$("#dg").datagrid("getSelections");
    if(selectedRows.length!=1){
        $.messager.alert("系统提示","请选择一条要修改尾款的销售单！");
        return;
    }

    $("#dlg").dialog("open").dialog("setTitle","销售金额更新");
    $("input[name=saleId]").val(selectedRows[0].id);
}

function saveUserAmount() {
    $("#fm").form("submit",{
        url:"/admin/saleList/updateAmount",
        onSubmit:function(){
            if(!$(this).form("validate")){
                return false;
            }
            return true;
        },
        success:function(result){
            var result=eval('('+result+')');
            if(result.success){
                alert("保存成功！");
                window.location.reload();
            }else{
                $.messager.alert("系统提示",result.errorInfo);
            }
        }
    });
}

function closeUserAmountDialog() {
    $("#dlg").dialog("close");
}


function formatPrice(val,row){
    if (val) {
        return "¥"+val.toFixed(2);
    }
}

function formatTotal(val,row){
    if (val) {
        return "¥"+val.toFixed(2);
    }
}

function formatAddress(val, row) {
    return val.address;
}

function formatContact(val, row) {
    return val.contact;
}

function formatRate(val, row) {
    if (val) {
        return val.toFixed(2) + "%";
    }
}

function formatTrueName(val, row) {
    if (val) {
        return val.trueName;
    }
}

function exportSale() {
    var exportUrl = '/admin/saleList/export';

    var customerId;
    if(isNaN($("#s_customer").combobox("getValue"))){
        customerId="";
    }else{
        customerId=$("#s_customer").combobox("getValue");
    }

    var searchObj = {
        contractNumber:$("#s_saleNumber").val(),
        "customer.id":customerId,
        state:$("#s_state").combobox("getValue"),
        bSaleDate:$("#s_bSaleDate").datebox("getValue"),
        eSaleDate:$("#s_eSaleDate").datebox("getValue")
    };

    var form = $('<form method="POST" action="' + exportUrl + '">');
    $.each(searchObj, function(k, v) {
        form.append($('<input type="hidden" name="' + k +
            '" value="' + v + '">'));
    });

    $('body').append(form);
    form.submit();

    form.remove();
}

function confirmReceiverGoods() {

    $("#dlg2").dialog("open").dialog("setTitle","确认送货");

}

function saveDeliveryDate() {

    var selectedRows=$("#dg").datagrid("getSelections");
    if(selectedRows.length!=1){
        $.messager.alert("系统提示","请选择一条要确认收货的销售单！");
        return;
    }

    var id = selectedRows[0].id;
    var receiverGoods = selectedRows[0].receiverGoods;
    var deliveryDate = $("#deliveryDate").datebox("getValue");
    if (receiverGoods === 0) {
        $.post("/admin/saleList/updateReceiverGoods",{id: id, deliveryDate:deliveryDate},function(result){
            $("#dg").datagrid("reload");
            $("#dg2").datagrid("reload");

            closeDeliveryDateDialog();
        }, 'json');
    } else {
        $.messager.alert("系统提示","选择的销售单已确认收货！");
    }
}

function closeDeliveryDateDialog() {
    $("#dlg2").dialog("close");
}

function formatReceiverGoods(val, row) {
    if (val) {
        return '已收货'
    } else {
        return '未收货'
    }
}