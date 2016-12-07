package com.frame.core.query.xml;

import java.util.List;

import org.springframework.beans.BeanUtils;

import com.frame.core.components.GsonFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class QueryConditions {
	private String paramString;
	private static final Gson gson=GsonFactory.buildDefaultGson();
	private List<QueryCondition> conditions;
	private int page=1;
	private int pageSize=10;
	
	public QueryConditions parseFromParamString(){
		if (paramString!=null){ 
			QueryConditions qcp=gson.fromJson(paramString, new TypeToken<QueryConditions>(){}.getType());
			BeanUtils.copyProperties(qcp, this);
		}
		return this;
	}
	public List<QueryCondition> getConditions() {
		return conditions;
	}
	public void setConditions(List<QueryCondition> conditions) {
		this.conditions = conditions;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public String getParamString() {
		return paramString;
	}
	public void setParamString(String paramString) {
		this.paramString = paramString;
	}
}
