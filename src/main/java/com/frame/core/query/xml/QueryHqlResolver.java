package com.frame.core.query.xml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.util.StringUtils;

import com.frame.core.query.xml.defination.ColumnDefination;
import com.frame.core.query.xml.defination.JoinEntry;
import com.frame.core.query.xml.defination.MappedClassEntry;
import com.frame.core.query.xml.defination.QueryDefination;

public class QueryHqlResolver {
	public static String generateSelect(QueryDefination defination,List<QueryCondition> conditions){
		StringBuilder sb=new StringBuilder();
		appendSelectFields(sb, defination, conditions);
		appendFrom(sb, defination, conditions);
		appendWhere(sb, defination, conditions);
		appendSort(sb, defination, conditions);
		return sb.toString();
	}
	public static String generateCount(QueryDefination defination,List<QueryCondition> conditions){
		StringBuilder sb=new StringBuilder();
		appendCount(sb);
		appendFrom(sb, defination, conditions);
		appendWhere(sb, defination, conditions);
		return sb.toString();
	}
	public static void appendCount(StringBuilder sb){
		sb.append("select count(0) ");
	}
	public static void appendSelectFields(StringBuilder sb,QueryDefination defination,List<QueryCondition> conditions){
		sb.append(" select ");
		List<ColumnDefination> columns=defination.getColumns();
		if (columns==null||columns.size()==0) throw new NullPointerException("pageDefinetion->queryDefination->columns 中的列定义不能为空");
		int index=0;
		for (Iterator<ColumnDefination> iterator = columns.iterator(); iterator.hasNext();index++) {
			ColumnDefination column = iterator.next();
			if (column.getStaticColumnData()!=null) continue;
			if (!StringUtils.isEmpty(column.getFromAlias())) sb.append(column.getFromAlias()).append('.');
			sb.append(column.getField());
			sb.append(" as col_"+index);
			sb.append(", ");
		}
		sb.delete(sb.length()-2, sb.length());
	}
	public static void appendFrom(StringBuilder sb,QueryDefination defination,List<QueryCondition> conditions){
		sb.append(" from ");// MenuEntity p join p.children as child 
		List<MappedClassEntry> mappedClasses=defination.getMappedClass();
		for (Iterator<MappedClassEntry> iterator = mappedClasses.iterator(); iterator.hasNext();) {
			MappedClassEntry e = iterator.next();
			sb.append(e.getMappedClass().getName()).append(" ");
			if (!StringUtils.isEmpty(e.getAlias())) sb.append(e.getAlias()).append(" ");
			List<JoinEntry> joins=e.getJoin();
			if (joins!=null&&joins.size()!=0) for (JoinEntry joinEntry : joins) {
				if (!StringUtils.isEmpty(joinEntry.getType())) sb.append(joinEntry.getType()).append(" ");
				sb.append(" join ");
				if (!StringUtils.isEmpty(joinEntry.getFromAlias())) sb.append(joinEntry.getFromAlias()).append(".");
				sb.append(joinEntry.getField()).append(" ");
				if (!StringUtils.isEmpty(joinEntry.getAs())) sb.append(" as ").append(joinEntry.getAs()).append(" ");
			}
			if (iterator.hasNext()) sb.append(",");
		}
	}
	public static  void appendWhere(StringBuilder sb,QueryDefination defination,List<QueryCondition> conditions){
		if (conditions==null||conditions.size()==0) return;
		sb.append(" where 1=1 ");
		if (!StringUtils.isEmpty(defination.getWhere())) sb.append("and ").append(defination.getWhere()).append(" ");
		for (QueryCondition condition : conditions) {
			sb.append("and ");
			if (!StringUtils.isEmpty(condition.getAlias())) sb.append(condition.getAlias()).append(".");
			sb.append(condition.getField()).append(" ");
			if (condition.getOperator()==null){
				Class<?> type=condition.getType();
				if (String.class.isAssignableFrom(type)){
					sb.append("like");
				}else{
					sb.append("=");
				} 
			}else{
				sb.append(condition.getOperator());
			}
			sb.append(" ? ");
		}
	}
	public static void appendSort(StringBuilder sb,QueryDefination defination,List<QueryCondition> conditions){
		//TODO
	}
	public static void main(String[] args) {
		System.out.println(List.class.isAssignableFrom(ArrayList.class));
	}
}
