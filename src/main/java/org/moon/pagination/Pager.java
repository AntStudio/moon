package org.moon.pagination;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.reeham.component.ddd.pagination.PagedList;

/**
 * the pager util
 * 分页工具类
 * @author Gavin
 * @version 1.0
 * @date 2012-12-5
 */
public class Pager implements PagedList<Object>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 总记录数
	 */
	private int totalItemsCount;

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
	private List<Object> items = new ArrayList<Object>();

	
	public Pager(int totalItemsCount,List<Object> items,int pageSize,int currentPageIndex){
		this.totalItemsCount = totalItemsCount;
		this.items = items;
		this.pageSize = pageSize;
		this.currentPageIndex = currentPageIndex;
	}
	
	
	@Override
	public List<Object> getItems() {
		return items;
	}


	@Override
	public int getTotalPageCount() {
		 return  (int) ((totalItemsCount / pageSize)
					+ (totalItemsCount % pageSize == 0 ? 0 : 1));
	}


	@Override
	public int getTotalItemCount() {
		return totalItemsCount;
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
