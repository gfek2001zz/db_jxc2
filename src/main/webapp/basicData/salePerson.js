var url;

$(document).ready(function() {
    $("#dg").datagrid({
        onDblClickRow:function(index,row){
            $("#dlg").dialog("open").dialog("setTitle","修改商品信息");
            $("#fm2").form("load",row);
            $("#shopId").combobox('setValue', row.shop.id);
            url="/admin/salePerson/save?id="+row.id;
            $("#saveAddAddNextButton").hide();
        }
    });
});


function openSalePersonAddDialog() {
    $("#dlg").dialog("open").dialog("setTitle","新增销售员");
    url = "/admin/salePerson/save";
}

function saveSalePerson(type) {
    $("#fm2").form("submit",{
        url:url,
        onSubmit:function(){
            return $(this).form("validate");
        },
        success:function(result){
            var result=eval('('+result+')');
            if(result.success){
                $.messager.alert("系统提示","保存成功！");
                resetValue();
                if(type==1){
                    closeSalePersonDialog();
                }else if(type==2){
                    resetValue();
                }
                $("#dg").datagrid("reload");
            }else{
                $.messager.alert("系统提示",result.errorInfo);
            }
        }
    });
}


function closeSalePersonDialog() {
    $("#dlg").dialog("close");
    resetValue();
}

function resetValue() {
    $("#shopId").combobox('setValue', '');
    $("#name").val('');
}

function formatterShopName(val, row) {
    return val.name + "-" + val.name2;
}

function deleteSalePerson() {
    var selectedRows=$("#dg").datagrid("getSelections");
    if(selectedRows.length!=1){
        $.messager.alert("系统提示","请选择一条要删除的数据！");
        return;
    }
    var id=selectedRows[0].id;
    $.messager.confirm("系统提示","您确定要删除这条数据吗?",function(r){
        if(r){
            $.post("/admin/salePerson/delete",{id:id},function(result){
                if(result.success){
                    $("#dg").datagrid("reload");
                }else{
                    $.messager.alert("系统提示",result.errorInfo);
                }
            },"json");
        }
    });
}

function openSalePersonDialog() {
}

function searchSalePerson() {
    $("#dg").datagrid('load',{
        "name":$("#s_name").val()
    });
}