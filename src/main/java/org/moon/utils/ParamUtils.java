package org.moon.utils;

import org.moon.core.orm.mybatis.Criteria;
import org.moon.core.orm.mybatis.criterion.Order;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * the util for parameter
 * @author Gavin
 * @version 1.0
 * @date 2012-12-6
 */
public class ParamUtils {

	public static  Map<String,Object> getDefaultParamMap(){
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("pageIndex", Constants.DEFAULT_PAGESIZE);
		paramMap.put("pageSize", Constants.DEFAULT_PAGEINDEX);
		return paramMap;
	}
	
	public static Map<String,Object> getParamsMap(HttpServletRequest request){
		Map<String,Object> paramMap = getDefaultParamMap();
		String pageIndex =request.getParameter("pageIndex");
		if(Objects.nonNull(pageIndex)) {
            paramMap.put("pageIndex", pageIndex);
        }
		String pageSize = request.getParameter("pageSize");
		if(Objects.nonNull(pageSize)) {
			paramMap.put("pageSize", pageSize);
		}
        int offset = (Integer.valueOf(paramMap.get("pageIndex")+"")-1)*Integer.valueOf(paramMap.get("pageSize")+"");
        paramMap.put("offset",offset);

        String sortType = request.getParameter("sortType");
        if(sortType==null){
            sortType = "asc";
        }

        String sortName = request.getParameter("sortName");
        if(sortName!=null){
            paramMap.put("sort",new Order(sortName, "asc".equalsIgnoreCase(sortType)).toSqlString());
        }
		
		
		return paramMap;
	}

    public static int getPageSize(Map params){
        Object pageSize = params.get("pageSize");
        Assert.notNull(pageSize,"Must hold not null number value for pageIndex");
        if(pageSize instanceof Integer){
            return (Integer)pageSize;
        }else{
            return Integer.valueOf((String) pageSize);
        }
    }

    public static int getPageIndex(Map params){
        Object pageIndex = params.get("pageIndex");
        Assert.notNull(pageIndex,"Must hold not null number value for pageIndex");
        if(pageIndex instanceof Integer){
            return (Integer)pageIndex;
        }else{
            return Integer.valueOf((String) pageIndex);
        }
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
	
}
