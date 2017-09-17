<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<table id="itemList" title="商品列表" 
       data-options="">
    <thead>
        <tr>
        	<th data-options="field:'ck',checkbox:true"></th>
        	<th data-options="field:'id',width:60">商品ID</th>
            <th data-options="field:'title',width:200,formatter:TAOTAO.formatText">商品标题</th>
            <th data-options="field:'cid',width:100">叶子类目</th>
            <th data-options="field:'sellPoint',width:100,formatter:TAOTAO.formatText">卖点</th>
            <th data-options="field:'price',width:70,align:'right',formatter:TAOTAO.formatPrice">价格</th>
            <th data-options="field:'num',width:70,align:'right'">库存数量</th>
            <th data-options="field:'barcode',width:100,formatter:TAOTAO.formatText">条形码</th>
            <th data-options="field:'status',width:60,align:'center',formatter:TAOTAO.formatItemStatus">状态</th>
            <th data-options="field:'created',width:130,align:'center',formatter:TAOTAO.formatDateTime">创建日期</th>
            <th data-options="field:'updated',width:130,align:'center',formatter:TAOTAO.formatDateTime">更新日期</th>
        </tr>
    </thead>
</table>
<div id="itemEditWindow" class="easyui-window" title="编辑商品" data-options="modal:true,closed:true,iconCls:'icon-save',href:'/rest/page/item-edit'" style="width:80%;height:80%;padding:10px;">
</div>
<script>

    function getSelectionsIds(){
    	var itemList = $("#itemList");
    	var sels = itemList.datagrid("getSelections");
    	var ids = [];
    	for(var i in sels){
    		ids.push(sels[i].id);
    	}
    	ids = ids.join(",");
    	return ids;
    }
    
    var toolbar = [{
        text:'新增',
        iconCls:'icon-add',
        handler:function(){
        	$(".tree-title:contains('新增商品')").parent().click();
        }
    },{
        text:'编辑',
        iconCls:'icon-edit',
        handler:function(){
        	var ids = getSelectionsIds();
        	if(ids.length == 0){
        		$.messager.alert('提示','必须选择一个商品才能编辑!');
        		return ;
        	}
        	if(ids.indexOf(',') > 0){
        		$.messager.alert('提示','只能选择一个商品!');
        		return ;
        	}
        	
        	$("#itemEditWindow").window({
        		onLoad :function(){
        			//回显数据
        			var data = $("#itemList").datagrid("getSelections")[0];
        			data.priceView = TAOTAO.formatPrice(data.price);
        			$("#itemeEditForm").form("load",data);
        			
        			// 加载商品描述
        			$.getJSON('/rest/item/desc/'+data.id,function(_data){
        				itemEditEditor.html(_data.itemDesc);
        			});
        			        			
        			TAOTAO.init({
        				"pics" : data.image,
        				"cid" : data.cid
        			});
        		}
        	}).window("open");
        }
    },{
        text:'删除',
        iconCls:'icon-cancel',
        handler:function(){
        	var ids = getSelectionsIds();
        	if(ids.length == 0){
        		$.messager.alert('提示','未选中商品!');
        		return ;
        	}
        	$.messager.confirm('确认','确定删除ID为 '+ids+' 的商品吗？',function(r){
        	    if (r){
        	    	var params = {"ids":ids};
                	$.post("/rest/item/delete",params, function(data){
            			
            				$.messager.alert('提示','删除商品成功!','info',function(){
            					$("#itemList").datagrid("reload");
            				});
            			
            		});
        	    }
        	});
        }
    },'-',{
        text:'下架',
        iconCls:'icon-remove',
        handler:function(){
        	var ids = getSelectionsIds();
        	if(ids.length == 0){
        		$.messager.alert('提示','未选中商品!');
        		return ;
        	}
        	$.messager.confirm('确认','确定下架ID为 '+ids+' 的商品吗？',function(r){
        	    if (r){
        	    	var params = {"ids":ids};
                	$.post("/rest/item/instock",params, function(data){
            			//if(data.status == 200){
            				$.messager.alert('提示','下架商品成功!','info',function(){
            					$("#itemList").datagrid("reload");
            				});
            			//}
            		});
        	    }
        	});
        }
    },{
        text:'上架',
        iconCls:'icon-remove',
        handler:function(){
        	var ids = getSelectionsIds();
        	if(ids.length == 0){
        		$.messager.alert('提示','未选中商品!');
        		return ;
        	}
        	$.messager.confirm('确认','确定上架ID为 '+ids+' 的商品吗？',function(r){
        	    if (r){
        	    	var params = {"ids":ids};
                	$.post("/rest/item/reshelf",params, function(data){
            				$.messager.alert('提示','上架商品成功!','info',function(){
            					$("#itemList").datagrid("reload");
            				});
            		});
        	    }
        	});
        }
    },{
        text:'<input type="text" id="search" name="search">'
    }];
    
    $(document).ready(function(){
    	
    	$("#itemList").datagrid({
    		singleSelect:false,
    		collapsible:true,
    		pagination:true,
    		url:'/rest/item',
    		method:'get',
    		pageSize:30,
    		toolbar:toolbar
    	});
    	
    	$("#search").searchbox({ 
    		width:300,
    		searcher:function(value,name){ 
    			$("#itemList").datagrid("load",{"title":encodeURI(value)});
    		}, 
    		prompt:'请输入商品标题' 
    	});
    	
    });
   
</script>