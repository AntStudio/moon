package com.greejoy.support.mybatis;
import java.util.Collection;


/**
 * 限制条件
 * @author Gavin
 * @version 1.0
 * @date 2013-3-10
 */
public class Restrictions {

	private static String generateValue(Object value){
		return "'"+value+"'";
	}
	public static Criterion eq(String name,Object value){
		return new DefaultCriterion(name, "=", generateValue(value));
	}
	public static Criterion eq(String name,Object value,boolean noWrap){
		if(noWrap)
		return new DefaultCriterion(name, "=", value.toString());
		else
			return new DefaultCriterion(name, "=", generateValue(value));
	}
	public static Criterion gt(String name,Object value){
		return new DefaultCriterion(name, ">", generateValue(value));
	}
	
	public static Criterion ge(String name,Object value){
		return new DefaultCriterion(name, ">=", generateValue(value));
	}
	
	public static Criterion lt(String name,Object value){
		return new DefaultCriterion(name, "<", generateValue(value));
	}
	
	public static Criterion le(String name,Object value){
		return new DefaultCriterion(name, "<=", generateValue(value));
	}
	
	public static Criterion notEq(String name,Object value){
		return new DefaultCriterion(name, "<>", generateValue(value));
	}
	
	public static Criterion group(String name){
		return new DefaultCriterion(" GROUP BY "+ name);
	}
	
	
	public static Criterion in(String name,Collection<Object> value){
		StringBuffer sb = new StringBuffer("(");
		if(value.size()>0){
			for(Object o:value){
				sb.append(generateValue(o));
				System.out.println(generateValue(o));
				sb.append(",");
			}
			sb.deleteCharAt(sb.length()-1);
		}
		sb.append(")");
		System.out.println(sb.toString());
		return new DefaultCriterion(name, "in", sb.toString());
	}
	
	public static Criterion like(String name,Object value){
		return new DefaultCriterion(name, "like", generateValue(value));
	}
	
	public static Criterion order(String name,String order){
		return new DefaultCriterion("ORDER BY "+name+" "+order);
	}
	
	public static Criterion limit(String start,String end){
		return new DefaultCriterion("LIMIT "+start+","+end);
	}
	
	public static Criterion sql(String sql){
		return new DefaultCriterion(sql);
	}
	
	public static Criterion column(String name){
		return new DefaultCriterion("["+name+"]");
	}
	
	public static void main(String[] args) {
		/*Criteria cri = new Criteria();
		//cri.add(gt("id", 44));
		cri.add(order("name", "desc"));
		cri.add(order("id", "asc"));
		cri.add(limit("3", "9"));
		cri.add(limit("2", "4"));
		//cri.add(like("name", "%kkk%"));
		System.out.println("toSql===="+cri.toSql());
		System.out.println("toFullSql====="+cri.toFullSql());
		System.out.println("toAppendSql==="+cri.toAppendSql());
		Map<String,String> keyMap = new HashMap<String,String>();
		keyMap.put("id", "newId");
		keyMap.put("name", "newName");
		cri.replaceKey(keyMap);
		System.out.println("toSql===="+cri.toSql());
		System.out.println("toFullSql====="+cri.toFullSql());
		System.out.println("toAppendSql==="+cri.toAppendSql());*/
		
		System.out.println("[234'%H'23]".matches("\\[.+\\]"));
	}
}
