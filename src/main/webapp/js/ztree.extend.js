/**
 * 复写zTree的getZtreeObj,由于zTree的zTreeTools为私有变量
 * 因此不能直接为其添加新的行为.然后在getZtreeObj的时候将自定义
 * 行为添加进去.getZTreeObj中的data变量由新加方法:
 *  getData:function(){
 *			return data;
 *		}
 * 返回,此方法在原版本中没有,需要将此方法添加到$.fn.zTree的属性里.
 * 此处主要是为了实现根据路径实现节点选择.具体见$.fn.zTree.selectNodebyTreepath
 */
$.fn.zTree.getZTreeObj = function(treeId) {
	var o = $.fn.zTree.getData().getZTreeTools(treeId);
	if(o){
		o.selectNodebyTreepath = $.fn.zTree.selectNodebyTreepath;
		o.asyncOrExpandNode = asyncOrExpandNode;
	}
	return o ? o : null;
	
};

/**
 * 根据路径选中节点.根据路径依次自动加载或展开父节点数据(已加载),最后选中节点数据.
 * 如传入路径为[1,2,3,4],那么就会依次加载或展开identifier为1,2,3的节点,最后选中
 * identifier为4的节点.注意需保证加载或展开某个节点的时候,该节点需已经存在.路径中的
 * 数据应该为父节点--子节点的关系.
 * @param identifier node标示名字 此在dataFilter中进行设置
 * @param treePath 路径
 * @param currentIndex 当前节点标示在路径中的下标.
 */
$.fn.zTree.selectNodebyTreepath = function(identifier,treePath,currentIndex){
	if(treePath.length==0)
		return 1;
	if(!currentIndex||currentIndex<1)
		currentIndex = 1;//将此值为1是因为第一个由其他程序处理,所以才发触发onExpand或onAsyncSuccess事件
	var $tree = this;
	if(treePath.length>0){
	if(currentIndex<treePath.length-1)
		{
		$tree.asyncOrExpandNode($tree.getNodeByParam(identifier,treePath[currentIndex]),true);
		currentIndex++;
		}
	else
		{
		$tree.selectNode(ztree.getNodeByParam(identifier,treePath[currentIndex]));
		currentIndex = 1;//选中后将currentIndex复位
		}
	}
	
	return currentIndex;
};

/**
 * 展开或加载子节点
 * @param node 需要展开或加载的节点
 * @param expandCallBack 是否触发onExpand回调方法
 */
var asyncOrExpandNode = function(node,expandCallBack){
	if(node.children)
		this.expandNode(node,true,false,false,expandCallBack);
	else
		this.reAsyncChildNodes(node);//节点下的数据未加载,进行加载,并回调onAsyncSuccess事件
};