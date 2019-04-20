var url;

$(document).ready(function() {

    $("#dg4").datagrid({
        onDblClickRow:function(index,row){
            var name=row.name;
            $("#unit").combobox("reload");
            $("#unit").combobox("setValue",name);
            $("#dlg4").dialog("close");
        }
    });

    $("#dg").datagrid({
        onDblClickRow:function(index,row){
            $("#dlg2").dialog("open").dialog("setTitle","修改商品信息");
            $("#fm2").form("load",row);
            $("#typeId").val(row.type.id);
            $("#typeName").val(row.type.name);
            url="/admin/goods/save?id="+row.id;
            $("#saveAddAddNextButton").hide();
        }
    });

    $("#tree").tree({
        lines:true,
        url:'/admin/goodsType/loadTreeInfo',
        onLoadSuccess:function(){
            $("#tree").tree("expandAll");
        },
        onClick:function(node){
            debugger;
            if (node.text == '床垫') {
                $("#chooseGoodsNameDialog").hide();
                $("input#name").removeAttr("readonly");
            } else {
                $("#chooseGoodsNameDialog").show();
                $("input#name").attr("readonly", "readonly");
            }

            if(node.attributes.state==0){ // 假如是叶子节点，删除按钮恢复可用
                $("#del").linkbutton("enable");

                $("#dg").datagrid('load',{
                    "name":$("#s_name").val(),
                    "type.id":node.id
                });
            }else{
                $("#del").linkbutton("disable");
            }
        }
    });

});

function openGoodsTypeAddDialog(){
    $("#dlg").dialog("open").dialog("setTitle","新增商品类别");
}

function deleteGoodsType(){
    var node=$("#tree").tree("getSelected"); // 获取选中节点
    var id=node.id;
    $.post("/admin/goodsType/delete",{id:id},function(result){
        if(result.success){
            $("#tree").tree("reload");
            $("#del").linkbutton("disable");
        }else{
            $.messager.alert("系统提示",result.errorInfo);
        }
    },"json");
}

function saveGoodsType(){
    if(!$("#fm").form("validate")){
        return;
    }
    var goodsTypeName=$('#goodsTypeName').val();
    var node=$("#tree").tree("getSelected"); // 获取选中节点
    var parentId;
    if(node==null){
        parentId=1;
    }else{
        parentId=node.id;
    }
    $.post("/admin/goodsType/save",{name:goodsTypeName,parentId:parentId},function(result){
        if(result.success){
            $("#tree").tree("reload");
            closeGoodsTypeDialog();
        }else{
            $.messager.alert("系统提示","提交失败，请联系管理员！");
        }
    },"json");
}

function closeGoodsTypeDialog(){
    $("#dlg").dialog("close");
    $('#goodsTypeName').val('');
}


// 商品管理

function formatGoodsTypeId(val,row){
    return row.type.id;
}

function formatGoodsTypeName(val,row){
    return row.type.name;
}

function formatPurchasingPrice(val,row){
    return "¥"+val;
}

function formatSellingPrice(val,row){
    return "¥"+val;
}

function searchGoods(){
    $("#dg").datagrid('reload',{
        "name":$("#s_name").val(),
        'model':$("#s_model").val(),
        'size':$('#s_size').val()
    });
}

function openGoodsAddDialog(){
    var node=$("#tree").tree("getSelected");
    if(node!=null && node.id!=1){
        $("#typeId").val(node.id);
        $("#typeName").val(node.text);

        $("#dlg2").dialog("open").dialog("setTitle","添加商品信息");
        url="/admin/goods/save";


        $.post("/admin/goods/genGoodsCode",{},function(result){
            $("#code").val(result);
        });
        $("#saveAddAddNextButton").show();

    }else{
        $("#typeId").val("");
        $("#typeName").val("");
        $.messager.alert("系统提示","请先选择一个商品类别！");
        return false;

    }
}

function openChooseGoodsTypeDialog(){
    $("#dlg3").dialog("open").dialog("setTitle","选择商品类别");
    $("#typeTree").tree({
        url:'/admin/goodsType/loadTreeInfo',
        onLoadSuccess:function(){
            var rootNode=$("#typeTree").tree("getRoot");
            $("#typeTree").tree("expand",rootNode.target);
        }
    });
}

function resetValue(){
    $("#typeId").val("");
    $("#typeName").val("");
    $("#code").val("");
    $("#name").val("");
    $("#model").val("");
    $("#size").val("");
    $("#style").val("");
    $("#color").val("");
    $("#unit").combobox("setValue","");
    $("#purchasingPrice").numberbox("setValue","");
    $("#sellingPrice").numberbox("setValue","");
    $("#minNum").numberbox("setValue","");
    $("#producer").val("");
    $("#remarks").val("");
}

function saveGoodsTypeChoose(){
    var node=$("#typeTree").tree("getSelected");
    if(node!=null && node.id!=1){
        $("#typeId").val(node.id);
        $("#typeName").val(node.text);
    }
    $("#dlg3").dialog("close");
}

function closeGoodsTypeChooseDialog(){
    $("#dlg3").dialog("close");
}

function saveGoods(type){
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
                    $("#dlg2").dialog("close");
                }else if(type==2){
                    var node=$("#tree").tree("getSelected");
                    if(node!=null && node.id!=1){
                        $("#typeId").val(node.id);
                        $("#typeName").val(node.text);
                    }else{
                        $("#typeId").val("");
                        $("#typeName").val("");
                    }
                    $.post("/admin/goods/genGoodsCode",{},function(result){
                        $("#code").val(result);
                    });
                }
                $("#dg").datagrid("reload");
            }else{
                $.messager.alert("系统提示",result.errorInfo);
            }
        }
    });
}

function closeGoodsDialog(){
    resetValue();
    $("#dlg2").dialog("close");
}

function openGoodsModifyDialog(){
    var selectedRows=$("#dg").datagrid("getSelections");
    if(selectedRows.length!=1){
        $.messager.alert("系统提示","请选择一条要修改的数据！");
        return;
    }
    var row=selectedRows[0];
    $("#dlg2").dialog("open").dialog("setTitle","修改商品信息");
    $("#fm2").form("load",row);
    $("#typeId").val(row.type.id);
    $("#typeName").val(row.type.name);
    url="/admin/goods/save?id="+row.id;
    $("#saveAddAddNextButton").hide();
}

function deleteGoods(){
    var selectedRows=$("#dg").datagrid("getSelections");
    if(selectedRows.length!=1){
        $.messager.alert("系统提示","请选择一条要删除的数据！");
        return;
    }
    var id=selectedRows[0].id;
    $.messager.confirm("系统提示","您确定要删除这条数据吗?",function(r){
        if(r){
            $.post("/admin/goods/delete",{id:id},function(result){
                if(result.success){
                    $.messager.alert("系统提示","数据已成功删除！");
                    $("#dg").datagrid("reload");
                }else{
                    $.messager.alert("系统提示","<font color=red>"+result.errorInfo+"</font>");
                }
            },"json");
        }
    });
}

// 商品单位模块

function openChooseGoodsUnitDialog(){
    $("#dlg4").dialog("open").dialog("setTitle","单位");
}

function openGoodsUnitAddDialog(){
    $("#dlg5").dialog("open").dialog("setTitle","添加单位信息");
    url="/admin/goodsUnit/save";
}

function deleteGoodsUnit(){
    var selectedRows=$("#dg4").datagrid("getSelections");
    if(selectedRows.length!=1){
        $.messager.alert("系统提示","请选择一条要删除的数据！");
        return;
    }
    var id=selectedRows[0].id;
    $.messager.confirm("系统提示","您确定要删除这条数据吗?",function(r){
        if(r){
            $.post("/admin/goodsUnit/delete",{id:id},function(result){
                if(result.success){
                    $("#dg4").datagrid("reload");
                    $("#unit").combobox("reload");
                }else{
                    $.messager.alert("系统提示",result.errorInfo);
                }
            },"json");
        }
    });
}

function chooseGoodsUnit(){
    var selectedRows=$("#dg4").datagrid("getSelections");
    if(selectedRows.length!=1){
        $.messager.alert("系统提示","请选择单位！");
        return;
    }
    var name=selectedRows[0].name;
    $("#unit").combobox("reload");
    $("#unit").combobox("setValue",name);
    $("#dlg4").dialog("close");
}

function closeGoodsUnitDialog(){
    $("#dlg4").dialog("close");
}

function saveGoodsUnit(){
    $("#fm5").form("submit",{
        url:url,
        onSubmit:function(){
            return $(this).form("validate");
        },
        success:function(result){
            var result=eval('('+result+')');
            if(result.success){
                $.messager.alert("系统提示","保存成功！");
                closeGoodsUnitAddDialog();
                $("#unit").combobox("reload");
                $("#dg4").datagrid("reload");
            }else{
                $.messager.alert("系统提示",result.errorInfo);
            }
        }
    });
}

function closeGoodsUnitAddDialog(){
    $("#dlg5").dialog("close");
    $("#goodsUnitName").val("");
}



function openChooseGoodsNameDialog() {
    $("#dlg6").dialog("open").dialog("setTitle","选择品名");
    $("#dg6").datagrid('load',{
        "classifyCode": "BED_TYPE"
    });
    $("#chooseSave").unbind();
    $("#chooseSave").bind("click", function() {
        saveGoodsDictItem("name");
    })
}

function openChooseGoodsModeDialog() {
    var typeName = $("#typeName").val();
    if (typeName == "皮床") {
        if ($("#name").val() != "") {
            $("#dlg6").dialog("open").dialog("setTitle","选择商品型号");

            var classifyCode = "BED_" + Mtils.utils.makePy($("#name").val(), true).toUpperCase() + "_MODE";
            $("#dg6").datagrid('load',{
                "classifyCode": classifyCode
            });
        } else {
            $.messager.alert("系统提示","请先选择品名！");
        }

    } else if (typeName == "床垫") {
        $("#dlg6").dialog("open").dialog("setTitle","选择商品型号");
        $("#dg6").datagrid('load',{
            "classifyCode": "MATTRESS_MODE"
        });
    } else {
        var classifyCode = getTypeNameToCode("MODEL");
        $("#dlg6").dialog("open").dialog("setTitle","选择商品型号");
        $("#dg6").datagrid('load',{
            "classifyCode": classifyCode
        });

    }

    $("#chooseSave").unbind();
    $("#chooseSave").bind("click", function() {
        saveGoodsDictItem("model");
    })
}

function openChooseGoodsSizeDialog() {
    $("#dlg6").dialog("open").dialog("setTitle","选择商品尺寸");
    $("#dg6").datagrid('load',{
        "classifyCode": "PRODUCT_SIZE"
    });

    $("#chooseSave").unbind();
    $("#chooseSave").bind("click", function() {
        saveGoodsDictItem("size");
    })
}

function openChooseGoodsBrandDialog() {
    $("#dlg6").dialog("open").dialog("setTitle","选择商品品牌");
    $("#dg6").datagrid('load',{
        "classifyCode": "PRODUCT_BRAND"
    });

    $("#chooseSave").unbind();
    $("#chooseSave").bind("click", function() {
        saveGoodsDictItem("brand");
    })
}


function getTypeNameToCode(classifyType) {
    var typeName = $("#typeName").val();
    if (typeName == "皮床") {
        var classifyCode = "BED";
    } else if (typeName == "床垫") {
        var classifyCode = "MATTRESS";
    } else if (typeName == "床护套") {
        var classifyCode = "SHEATH";
    } else if (typeName == "枕头") {
        var classifyCode = "PILLOW";
    } else if (typeName == "赠品") {
        var classifyCode = "GIFT"
    }

    return classifyCode + "_" + classifyType;
}

function openChooseGoodsStyleDialog() {
    $("#dlg6").dialog("open").dialog("setTitle","选择商品款式");
    var classifyCode = getTypeNameToCode("STYLE");


    $("#dg6").datagrid('load',{
        "classifyCode": classifyCode
    });

    $("#chooseSave").unbind();
    $("#chooseSave").bind("click", function() {
        saveGoodsDictItem("style");
    })

}

function openChooseGoodsColorDialog() {
    $("#dlg6").dialog("open").dialog("setTitle","选择商品颜色");
    var classifyCode = getTypeNameToCode("COLOR");


    $("#dg6").datagrid('load',{
        "classifyCode": classifyCode
    });

    $("#chooseSave").unbind();
    $("#chooseSave").bind("click", function() {
        saveGoodsDictItem("color");
    })

}




function saveGoodsDictItem(fieldId) {
    var selectedRows=$("#dg6").datagrid("getSelections");
    if(selectedRows.length!=1){
        $.messager.alert("系统提示","请选择一条数据！");
        return;
    }

    $("#" + fieldId).val(selectedRows[0].itemName);

    $("#dlg6").dialog("close");
}

function closeGoodsDictItemAddDialog() {

    $("#dlg6").dialog("close");
}


function exportGoods() {
    var exportUrl = '/admin/goods/export';

    var form = $('<form method="POST" action="' + exportUrl + '">');
    $.each({name: $('#s_name').val()}, function(k, v) {
        form.append($('<input type="hidden" name="' + k +
            '" value="' + v + '">'));
    });

    $('body').append(form);
    form.submit();

    form.remove();
}