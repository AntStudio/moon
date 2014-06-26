package org.moon.utils;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.moon.core.orm.mybatis.Criteria;
import org.moon.core.orm.mybatis.criterion.Order;

/**
 * the util for parameter
 * @author Gavin
 * @version 1.0
 * @date 2012-12-6
 */
public class ParamUtils {

	public static  Map<String,Object> getDefaultParamMap(){
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("ps", Constants.DEFAULT_PAGESIZE);
		paramMap.put("cp", 1);
		paramMap.put("curcount", 0);
		paramMap.put("deleteFlag", false);
		return paramMap;
	}
	
	public static Map<String,Object> getParamsMap(HttpServletRequest request){
		Map<String,Object> paramMap = getDefaultParamMap();
		String page =request.getParameter("pageIndex");
		if(page!=null)
		paramMap.put("cp", page);	
		
		String pageSize = request.getParameter("pageSize");
		if(pageSize!=null){
			paramMap.put("ps", pageSize);
		}
		Long curcount = 0L;
		if(pageSize!=null&&page!=null)
			curcount = (long) ((Integer.parseInt(page)-1)*Integer.parseInt(pageSize));
		paramMap.put("curcount",curcount);
		String sortName = request.getParameter("sortname");
		paramMap.put("orderBy", sortName+" "+("desc".equals(request.getParameter("sortorder"))?"desc":"asc"));
		
		
		return paramMap;
	}
 
	
	public static Criteria getParamsAsCerteria(HttpServletRequest request){
		Criteria criteria = new Criteria();
		String page =request.getParameter("pageIndex");
		if(page!=null){
			criteria.currentPage(Integer.parseInt(page));
		}
		
		String pageSize = request.getParameter("pageSize");
		if(pageSize!=null){
			criteria.limit(Integer.parseInt(pageSize));
		}
		
		
		String sortType = request.getParameter("sortType");
		if(sortType==null){
			sortType = "asc";
		}
		
		String sortName = request.getParameter("sortName");
		if(sortName!=null){
			criteria.order(new Order(sortName, "asc".equalsIgnoreCase(sortType)));
		}
		return criteria;
	}
	
	public static void main(String[] args){
		 Map<String,Object> m = new HashMap<String,Object>();
		 System.out.println(m.get("key"));
		 System.out.println(m.get("key"));
		 
	
		
	}
}
