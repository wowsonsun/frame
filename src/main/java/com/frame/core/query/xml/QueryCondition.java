package com.frame.core.query.xml;

import com.frame.core.components.GsonFactory;
import com.google.gson.Gson;

public class QueryCondition{
	private static final Gson gson=GsonFactory.buildDefaultGson();
	private String field;
	private String alias;
	private Class<?> type;
	private Object value;
	private String operator;
	private int extra;
	private boolean isParesed;
	public QueryCondition parseValue(){
		isParesed=true;
		this.value=gson.fromJson("'"+value+"'", type);
		return this;
	}
	public static void main(String[] args) {
		String json="{field:'field',alias:'alias',type:'java.util.Date',value:'2013-2-2 12:12:12',operator:'LIKE'}";
		QueryCondition q=GsonFactory.buildDefaultGson().fromJson(json, QueryCondition.class);
		System.out.println(q.parseValue().getValue());
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public Class<?> getType() {
		return type;
	}
	public void setType(Class<?> type) {
		this.type = type;
	}
	public int getExtra() {
		return extra;
	}
	public void setExtra(int extra) {
		this.extra = extra;
	}
	public Object getValue() {
		if(!isParesed) this.parseValue();
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	
}
