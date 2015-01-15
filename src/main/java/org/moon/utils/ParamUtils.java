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
		paramMap.put("pageIndex", Constants.DEFAULT_PAGEINDEX);
		paramMap.put("pageSize", Constants.DEFAULT_PAGESIZE);
		return paramMap;
	}

    /**
     * 获取包括分页参数在内的所有参数,默认忽略空的参数
     * @param request
     * @return
     */
    public static  Map<String,Object> getAllParamMapFromRequest(HttpServletRequest request){
        return getAllParamMapFromRequest(request,true);
    }

    /**
     * 获取包括分页参数在内的所有参数
     * @param request
     * @param ignoreEmptyParam 是否忽略空字符串或者空的参数
     * @return
     */
    public static  Map<String,Object> getAllParamMapFromRequest(HttpServletRequest request,boolean ignoreEmptyParam){
        Map<String,Object> paramMap = getParamsMapForPager(request);
        paramMap.putAll(getParamMapFromRequest(request));
        return paramMap;
    }

    /**
     * 获取除了分页参数的所有参数
     * @param request
     * @param ignoreEmptyParam 是否忽略空字符串或者空的参数
     * @return
     */
    public static  Map<String,Object> getParamMapFromRequest(HttpServletRequest request,boolean ignoreEmptyParam){
        Map<String,Object> paramMap = new HashMap<String,Object>();
        Map<String,String[]> params = request.getParameterMap();
        for(Map.Entry<String,String[]> entry:params.entrySet()){
            if(entry.getValue().length == 1){
                String val = entry.getValue()[0];
                if(ignoreEmptyParam && Strings.isNullOrEmpty(val)) continue;
                paramMap.put(entry.getKey(),val);
            }else{
                paramMap.put(entry.getKey(),entry.getValue());
            }
        }
        return paramMap;
    }

    /**
     * 获取除了分页参数的所有参数 默认忽略空的参数
     * @param request
     * @return
     */
    public static  Map<String,Object> getParamMapFromRequest(HttpServletRequest request){
        return getParamMapFromRequest(request,true);
    }
    /**
     * 获取分页参数
     * @param request
     * @return
     */
	public static Map<String,Object> getParamsMapForPager(HttpServletRequest request){
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
