var url;

$(document).ready(function() {

//    $("#customerId").combobox({
//        mode:'remote',
//        url:'/admin/customer/comboList',
//        valueField:'id',
//        textField:'name',
//        delay:100
//    });

    $("#saleDate").datebox("setValue",genTodayStr());

    $("#dh").load("/admin/saleList/genCode");

});

function saveSaleGoods(){
    debugger;
    $("#saleNumber").val($("#dh").text());
    $("#goodsJson").val(JSON.stringify($("#dg").datagrid("getData").rows));
    $("#fm").form("submit",{
        url:"/admin/saleList/save",
        onSubmit:function(){
            if($("#dg").datagrid("getRows").length==0){
                $.messager.alert("系统提示","请添加销售商品！");
                return false;
            }
            if(!$(this).form("validate")){
                return false;
            }
            if(isNaN($("#customerId").combobox("getValue"))){
                $.messager.alert("系统提示","请选择客户！");
                return false;
            }
            return true;
        },
        success:function(result){
            var result=eval('('+result+')');
            if(result.success){
                alert("保存成功！");

                salePersonRemoveAll();
                window.location.reload();
            }else{
                $.messager.alert("系统提示",result.errorInfo);
            }
        }
    });
}

function openSaleListGoodsModifyDialog(){
    $("#saveAddAddNextButton").hide();
    var selectedRows=$("#dg").datagrid("getSelections");
    if(selectedRows.length!=1){
        $.messager.alert("系统提示","请选择一个商品！");
        return;
    }
    var row=selectedRows[0];
    $("#dlg2").dialog("open").dialog("setTitle","修改销售单商品");
    $("#fm2").form("load",row);
    $("#sellingPrice").val("¥"+row.sellingPrice);
    $("#price").numberbox("setValue",row.price);
    $("#num").numberbox("setValue",row.num);
    $("#action").val("modify");
    $("#num").focus();
    $("#rowIndex").val($("#dg").datagrid("getRowIndex",row));
}

function deleteSaleListGoods(){
    var selectedRows=$("#dg").datagrid("getSelections");
    if(selectedRows.length!=1){
        $.messager.alert("系统提示","请选择一条要删除的商品！");
        return;
    }
    $.messager.confirm("系统提示","您确定要删除这商品吗?",function(r){
        if(r){
            $("#dg").datagrid("deleteRow",$("#dg").datagrid("getRowIndex",selectedRows[0]));
            setSaleListAmount();
        }
    });
}

// 销售出库模块
function setSaleListAmount(){
    var rows=$("#dg").datagrid("getRows");
    var amount=0;
    var amountCostPrice=0;
    var amountPaid=0;
    for(var i=0;i<rows.length;i++){
        var row=rows[i];
        amount+=parseFloat(row.total);
        amountCostPrice +=parseFloat(row.totalCost);
        amountPaid +=parseFloat(row.totalAmountPaid);
    }
    debugger;
    $("#amountPayable").numberbox('setValue', amount.toFixed(2));
    $("#amountPaid").numberbox('setValue', amountPaid.toFixed(2));
    $('#amountEarnest').numberbox('setValue', 0);
    $('#amountFinalPayment').numberbox('setValue', amount.toFixed(2));
    $('#amountDiscount').numberbox('setValue', 0);


    // $('#amountDiscountRate').val('0%');

    discountCalculation();

    $('#amountCostPrice').val(amountCostPrice.toFixed(2))
}

function openSaleListGoodsAddDialog(){
    $("#dlg").dialog("open").dialog("setTitle","销售出库商品选择");

    $("#tree").tree({
        url:'/admin/goodsType/loadTreeInfo',
        onLoadSuccess:function(){
            var rootNode=$("#tree").tree("getRoot");
            $("#tree").tree("expand",rootNode.target);
        },
        onClick:function(node){
            if(node.attributes.state==0){ // 假如是叶子节点，删除按钮恢复可用
                $("#del").linkbutton("enable");
            }else{
                $("#del").linkbutton("disable");
            }
            $("#dg2").datagrid('load',{
                "type.id":node.id
            });
        }
    });
}


// 商品选择模块

function searchGoods(){
    $("#dg2").datagrid('load',{
        "codeOrName":$("#s_codeOrName").val(),
        'model': $("#s_model").val(),
        'size': $("#s_size").val()
    });
}

function formatLastSellingPrice(val,row){
    return "¥"+val;
}

function formatSellingPrice(val,row){
    return "¥"+val;
}


function openGoodsChooseDialog(){
    $("#saveAddAddNextButton").show();
    var selectedRows=$("#dg2").datagrid("getSelections");
    if(selectedRows.length!=1){
        $.messager.alert("系统提示","请选择一个商品！");
        return;
    }
    var row=selectedRows[0];
    $("#dlg2").dialog("open").dialog("setTitle","选择商品");
    $("#fm2").form("load",row);
    $("#sellingPrice").val("¥"+row.sellingPrice);
    $("#price").numberbox("setValue",row.sellingPrice);
    $("#purchasingPrice").val("¥"+row.purchasingPrice);
    $('#costPrice').val(row.purchasingPrice);
    $("#action").val("add");
    $("#num").focus();
}

function resetValue(){
    $("#price").numberbox("setValue","");
    $("#num").numberbox("setValue","");
    $("#amountPaid_s").numberbox("setValue","");
}

function saveGoods(type){
    var action=$("#action").val();
    if(!$("#fm4").form("validate")){
        return;
    }
    if(action=="add"){
        var selectedRows=$("#dg2").datagrid("getSelections");
        var row=selectedRows[0];
        var price=$("#price").numberbox("getValue");
        var costPrice =$('#costPrice').val();
        var amountPaid = $('#amountPaid_s').val();
        var num=$("#num").numberbox("getValue");
        var total=price*num;
        var totalCost = costPrice*num;
        var totalAmountPaid = amountPaid*num;

        var discount = total - totalAmountPaid;
        var discountRate = (discount / total) * 100;

        $("#dg").datagrid("appendRow",{
            code:row.code,
            name:row.name,
            model:row.model,
            style:row.style,
            size:row.size,
            price:price,
            amountPaid: amountPaid,
            num:num,
            total:total,
            totalCost:totalCost,
            totalAmountPaid: totalAmountPaid.toFixed(2),
            discount: discount,
            discountRate: discountRate.toFixed(2),
            typeId:row.type.id,
            goodsId:row.id,
            sellingPrice:row.sellingPrice
        });
    }else if(action=="modify"){
        var rowIndex=$("#rowIndex").val();
        var selectedRows=$("#dg").datagrid("getSelections");
        var row=selectedRows[0];
        var price=$("#price").numberbox("getValue");
        var costPrice =$('#costPrice').val();
        var amountPaid = $('#amountPaid_s').val();
        var num=$("#num").numberbox("getValue");
        var totalCost = costPrice*num;
        var totalAmountPaid = amountPaid*num;
        var total=price*num;

        var discount = total - totalAmountPaid;
        var discountRate = (discount / total) * 100;


        $("#dg").datagrid("updateRow",{
            index:rowIndex,
            row:{
                code:row.code,
                name:row.name,
                model:row.model,
                style:row.style,
                unit:row.unit,
                price:price,
                amountPaid: amountPaid,
                num:num,
                total:total,
                totalCost:totalCost,
                discount: discount,
                discountRate: discountRate.toFixed(2),
                totalAmountPaid: totalAmountPaid.toFixed(2),
                typeId:row.typeId,
                goodsId:row.id,
                sellingPrice:row.sellingPrice
            }
        });
    }
    setSaleListAmount();
    if(type==1){
        closeGoodsDialog();
    }
    closeGoodsChooseDialog();
}

function closeGoodsDialog(){
    $('#s_codeOrName').val('');
    $("#dlg").dialog("close");
}

function closeGoodsChooseDialog(){
    resetValue();
    $("#dlg2").dialog("close");
}

// 商品类别模块
function openGoodsTypeAddDialog(){
    $("#dlg3").dialog("open").dialog("setTitle","新增商品类别");
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
    if(!$("#fm3").form("validate")){
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
    $("#dlg3").dialog("close");
    $('#goodsTypeName').val('');
}


function openChooseSalePersonDialog() {
    var shopId = $("#shopId").combobox('getValue');
    if (!shopId) {
        $.messager.alert("系统提示","请先选择门店！");
        return;
    }

    $("#dlg4").dialog("open").dialog("setTitle","选择销售员");
}

function openSalePersonAddDialog() {
    var shopId = $("#shopId").combobox('getValue');
    $('#dlg5').dialog('open').dialog('setTitle', '选择销售员');
    $('#dg5').datagrid('load', {
        shop: shopId
    });
}

function deleteSalePerson() {
    var user = $('#dg4').datagrid('getSelected');
    var rows = $('#dg4').datagrid('getRows');
    var index = -1;
    for (var i = 0; i < rows.length; i ++) {
        var row = rows[i];
        if (row.user.id == user.user.id) {
            index = i;
            break;
        }
    }

    $('#dg4').datagrid('deleteRow', index);
}


function salePersonRemoveAll() {
    var rows = $('#dg4').datagrid('getRows');
    for (var i = rows.length - 1; i >= 0; i --) {
        $('#dg4').datagrid('deleteRow', i);
    }
}

function saveSalePerson() {
    var rows = $('#dg4').datagrid('getRows');
    var salePerson = "";
    for (var i = 0; i < rows.length; i ++) {
        var row = rows[i];
        salePerson += row.salePerson.name + "：" + row.amount + ";"
    }
    $('#salePersonDisplay').val(salePerson);
    $("#salePersonsJson").val(JSON.stringify($("#dg4").datagrid("getData").rows));

    closeSalePersonDialog();
}

function closeSalePersonDialog() {
    $('#dlg4').dialog('close');
}

function formatName(val, row) {
    return val.name;
}

function saveUser() {
    $("#amount").numberbox('setValue', '');
    $('#dlg6').dialog('open').dialog('setTitle', '销售金额');
}


function saveUserAmount() {
    if ($("#amount").val() == '') {
        $.messager.alert("系统提示","请先选择销售金额！");
        return;
    }

    var salePersonData = $('#dg5').datagrid('getSelected');
    var rows = $('#dg4').datagrid('getRows');
    var index = -1;
    for (var i = 0; i < rows.length; i ++) {
        var row = rows[i];
        if (row.salePerson.id == salePersonData.id) {
            index = i;
            break;
        }
    }

    if (index > -1) {
        $('#dg4').datagrid('updateRow', {
            index: index,
            row: {
                salePerson: salePersonData,
                amount: $("#amount").val()
            }
        })
    } else {
        $('#dg4').datagrid('insertRow', {
            index: 0,
            row: {
                salePerson: salePersonData,
                amount: $("#amount").val()
            }
        });
    }

    $('#dlg6').dialog('close');
    $('#dlg5').dialog('close');
}

function closeUserAmountDialog() {
    $('#dlg6').dialog('close');
}

function closeUserDialog() {
    $('#dlg5').dialog('close');
}

function discountCalculation() {
    var amountPaid = $('#amountPaid').val() ? parseFloat($('#amountPaid').val()) : 0;
    var amountEarnest = $("#amountEarnest").val() ? parseFloat($("#amountEarnest").val()) : 0;
    var amountPayable = $("#amountPayable").val() ? parseFloat($("#amountPayable").val()) : 0;

    if (amountPayable >= amountPaid) {
        var amountFinalPayment = amountPaid - amountEarnest;
        var amountDiscount = amountPayable - amountPaid;
        var amountDiscountRate = (amountDiscount / amountPayable) * 100;

        $("#amountDiscount").numberbox('setValue', amountDiscount);
        $("#amountDiscountRate").val(amountDiscountRate.toFixed(2) + "%");

        if (amountFinalPayment > -1) {
            $("#amountFinalPayment").numberbox('setValue', amountFinalPayment);
        } else {
            $.messager.alert("系统提示","实收定金不能大于成交价！");
            $("#amountEarnest").numberbox('setValue', '');
        }
    } else {
        $.messager.alert("系统提示","成交价不能大于统一零售价！");
        $("#amountPaid").numberbox('setValue', '');
        $("#amountEarnest").numberbox('setValue', '');
    }
}

$(document).ready(function() {

    $("#saleDate").datebox("setValue",genTodayStr());

    $("#dg").datagrid({
        onDblClickRow:function(index,row){
            $("#saveAddAddNextButton").hide();
            $("#dlg2").dialog("open").dialog("setTitle","修改销售单商品");
            $("#fm2").form("load",row);
            $("#sellingPrice").val("¥"+row.sellingPrice);
            $("#price").numberbox("setValue",row.price);
            $("#num").numberbox("setValue",row.num);
            $("#action").val("modify");
            $("#num").focus();
            $("#rowIndex").val($("#dg").datagrid("getRowIndex",row));
        }
    });

    $("#dg2").datagrid({
        onDblClickRow:function(index,row){
            $("#saveAddAddNextButton").show();
            $("#dlg2").dialog("open").dialog("setTitle","选择商品");
            $("#fm2").form("load",row);
            $("#sellingPrice").val("¥"+row.sellingPrice);
            $("#purchasingPrice").val("¥"+row.purchasingPrice);
            $("#price").numberbox("setValue",row.sellingPrice);
            $("#costPrice").val(row.purchasingPrice);
            $("#action").val("add");
            $("#num").focus();
        }
    });


    $('#dg4').datagrid({
        onDblClickRow:function(index, row) {
            openSalePersonAddDialog();
        }
    });


    $('#dg5').datagrid({
        onDblClickRow:function(index, row) {
            saveUser();
        }
    });


    $("#amountPaid").change(discountCalculation);
    $("#amountEarnest").change(discountCalculation);

});