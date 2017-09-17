<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div>
	 <ul id="contentCategory" class="easyui-tree">
    </ul>
</div>
<div id="contentCategoryMenu" class="easyui-menu" style="width:120px;" data-options="onClick:menuHandler">
    <div data-options="iconCls:'icon-add',name:'add'">添加</div>
    <div data-options="iconCls:'icon-remove',name:'rename'">重命名</div>
    <div class="menu-sep"></div>
    <div data-options="iconCls:'icon-remove',name:'delete'">删除</div>
</div>
<script type="text/javascript">
$(function(){
	$("#contentCategory").tree({
		url : '/rest/content/category',
		method : "GET",
		animate: true,
		//在右键点击节点的时候触发。
		onContextMenu: function(e,node){
			//组织默认的右击菜单
            e.preventDefault();
            $(this).tree('select',node.target);//选中当前的右击节点
            
            //选取并渲染自定义的easy ui menu菜单并显示在当前右击的鼠标的坐标
            $('#contentCategoryMenu').menu('show',{
                left: e.pageX,
                top: e.pageY
            });
        },
        //当编辑完了一个节点以后触发
        onAfterEdit : function(node){
        	var _tree = $(this);
        	if(node.id == 0){
        		// 新增节点
        		$.post("/rest/content/category/add",{parentId:node.parentId,name:node.text},function(data){
        			_tree.tree("update",{
        				target : node.target,
        				id : data.id
        			});
        		});
        	}else{//重命名
        		$.ajax({
        			   type: "POST",
        			   url: "/rest/content/category/update",
        			   data: {id:node.id,name:node.text},
        			   success: function(msg){
        				   //$.messager.alert('提示','新增商品成功!');
        			   },
        			   error: function(){
        				   $.messager.alert('提示','重命名失败!');
        			   }
        			});
        	}
        }
	});
});
function menuHandler(item){
	var tree = $("#contentCategory");//选取ul元素jQuery对象
	var node = tree.tree("getSelected");//选择当前选中的树节点
	if(item.name === "add"){//添加
		//往当前选中的节点中追加一个子节点
		tree.tree('append', {
            parent: (node?node.target:null),//新节点的父节点
            data: [{
                text: '新建分类',
                id : 0,
                parentId : node.id
            }]
        }); 
		var _node = tree.tree('find',0);//查询树中节点id为0的节点；也就是新节点
		//选中新节点，并开始编辑该节点
		tree.tree("select",_node.target).tree('beginEdit',_node.target);
	}else if(item.name === "rename"){//重命名
		tree.tree('beginEdit',node.target);
	}else if(item.name === "delete"){
		$.messager.confirm('确认','确定删除名为 '+node.text+' 的分类吗？',function(r){
			if(r){//当点击 确定 的时候，r的值为true
				$.ajax({
     			   type: "POST",
     			   url: "/rest/content/category/delete",
     			   data : {parentId:node.parentId,id:node.id},
     			   success: function(msg){
     				  tree.tree("remove",node.target);
     			   },
     			   error: function(){
     				   $.messager.alert('提示','删除失败!');
     			   }
     			});
			}
		});
	}
}
</script>