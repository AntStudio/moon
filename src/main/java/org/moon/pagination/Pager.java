package org.moon.pagination;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.moon.utils.ParamUtils;

import com.reeham.component.ddd.pagination.PagedList;

/**
 * the pager util
 * 分页工具类
 * @author Gavin
 * @version 1.0
 * @date 2012-12-5
 */
public class Pager implements PagedList<Map<String,Object>>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 总记录数
	 */
	private Long totalItemsCount;

	/**
	 * 分页大小
	 */
	private int pageSize;

	/**
	 * 总页数
	 */
	private int totalPagesCount;

	/**
	 * 当前页数
	 */
	private int currentPageIndex;

	/**
	 * 当前分页数据
	 */
	private List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();

	
	
	/**
	 * 参数map 如页码参数(当前页,页面大小,查询参数等等)
	 */
	private Map<String,Object> paramsMap = ParamUtils.getDefaultParamMap();
	
	public Pager(Long totalItemsCount,List<Map<String,Object>> items,Map<String,Object> paramsMap){
		this.totalItemsCount = totalItemsCount;
		this.items = items;
		if(paramsMap!=null){
			this.paramsMap = paramsMap;
		}
		this.pageSize = Integer.parseInt(this.paramsMap.get("ps").toString());
		this.currentPageIndex = Integer.parseInt(this.paramsMap .get("cp").toString());
	}
	
	
	@Override
	public List<Map<String,Object>> getItems() {
		return items;
	}


	@Override
	public int getTotalPageCount() {
		 return  (int) ((totalItemsCount / pageSize)
					+ (totalItemsCount % pageSize == 0 ? 0 : 1));
	}


	@Override
	public int getTotalItemCount() {
		if(totalPagesCount<=0)
		return 0;
		else
			return totalPagesCount;
	}


	@Override
	public int getCurrentPageIndex() {
		 if(currentPageIndex<=0)
			 return 0;
		return currentPageIndex;
	}


	@Override
	public int getPageSize() {
		if(pageSize<=0)
			return 1;
		return pageSize;
	}


	@Override
	public int getNextPageIndex() {
		return currentPageIndex+1;
	}


	@Override
	public int getPreviousPageIndex() {
		if(currentPageIndex<=2)
		return 1;
		else
			return currentPageIndex-1;
	}

 
   public Map<String,Object> toMap(){
	   Map<String,Object> m = new HashMap<String,Object>();
	   m.put("rows", this.items);
	   m.put("page", this.currentPageIndex);
	   m.put("total", this.totalItemsCount);
	   return m;
   }
	
	
}
