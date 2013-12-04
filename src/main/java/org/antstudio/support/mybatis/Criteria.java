package org.antstudio.support.mybatis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Criteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5084662034070903291L;

	private Collection<Criterion> criterions = new ArrayList<Criterion>();

	private Collection<Criterion> orderCriterions = new ArrayList<Criterion>();
	
	private Collection<Criterion> groupCriterions = new ArrayList<Criterion>();
	
	private Collection<Criterion> columnCriterions = new ArrayList<Criterion>();

	private Criterion limitCriterion;

	public void add(Criterion criterion) {
		if (criterion.isLimit()) {
			limitCriterion = criterion;
		} else if (criterion.isOrder()) {
			this.orderCriterions.add(criterion);
		} else if(criterion.isGroup()){
			this.groupCriterions.add(criterion);
		}else if(criterion.isColumn()){
			this.columnCriterions.add(criterion);
		}else{
			this.criterions.add(criterion);
		}
	}

	public String toColumns(){
		return toAppendColumns().substring(1);
	}
	
	public String toAppendColumns(){
		StringBuffer sb = new StringBuffer();
		for(Criterion c:columnCriterions){
			sb.append(",");
			sb.append(c.toSqlString());
		}
		return sb.toString();
	}
	
	public String toSql() {
		return toAppendSql().replaceFirst("^\\s*((and)|(or)|(where)|(AND)|(OR)|(WHERE))\\s", " ");
	}

	public String toFullSql() {
		if(criterions.size()>0)
			return " WHERE " + toSql();
		return toSql();
	}

	public String toAppendSql() {
		StringBuffer sb = new StringBuffer();
		for (Criterion cri : criterions) {
			sb.append(" AND ");
			sb.append(cri.toSqlString());
		}
		if (groupCriterions.size() > 0) {
			sb.append(" GROUP BY ");
			for (Criterion cri : groupCriterions) {
				sb.append(cri.toSqlString());
				sb.append(",");
			}
			sb = new StringBuffer(sb.substring(0, sb.length() - 1));
		}
		
		if (orderCriterions.size() > 0) {
			sb.append(" ORDER BY ");
			for (Criterion cri : orderCriterions) {
				sb.append(cri.toSqlString());
				sb.append(",");
			}
			sb = new StringBuffer(sb.substring(0, sb.length() - 1));
		}
		if (limitCriterion != null) {
			sb.append(" LIMIT ");
			sb.append(limitCriterion.toSqlString());
		}
		return sb.toString();
	}

	public void replaceKey(Map<String, String> keyMap) {
		Pattern pattern = Pattern.compile("[\\w_-]+");
		Matcher m;
		for (Criterion criterion : criterions) {
			m = pattern.matcher(criterion.toSqlString());
			if (m.find() && keyMap.containsKey(m.group())) {
				criterion.setSql(criterion.toSqlString().replaceFirst("^[\\w_-]+\\s", keyMap.get(m.group()) + " "));
			}

		}
	}
}
