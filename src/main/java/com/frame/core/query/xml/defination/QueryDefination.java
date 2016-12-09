package com.frame.core.query.xml.defination;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class QueryDefination {
	private List<MappedClassEntry> mappedClass;
	private String where;
	private List<SortEntry> sortBy=new ArrayList<SortEntry>();
	private String showIndex;
	private List<ColumnDefination> columns;
	private int pageSize=10;
	public List<MappedClassEntry> getMappedClass() {
		return mappedClass;
	}
	public void setMappedClass(List<MappedClassEntry> mappedClass) {
		this.mappedClass = mappedClass;
	}
	public String getWhere() {
		return where;
	}
	public void setWhere(String where) {
		this.where = where;
	}
	@XmlElement(name="by")
	@XmlElementWrapper(name="sort")
	public List<SortEntry> getSortBy() {
		return sortBy;
	}
	public void setSortBy(List<SortEntry> sortBy) {
		this.sortBy = sortBy;
	}
	@XmlAttribute
	public String getShowIndex() {
		return showIndex;
	}
	public void setShowIndex(String showIndex) {
		this.showIndex = showIndex;
	}
	@XmlElementWrapper(name="columns")
	@XmlElement(name="column")
	public List<ColumnDefination> getColumns() {
		return columns;
	}
	public void setColumns(List<ColumnDefination> columns) {
		this.columns = columns;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	private List<String> requiredJsPath;
	private List<String> requiredCssPath;
	@XmlElement(name="path")
	@XmlElementWrapper(name = "js")
	public List<String> getRequiredJsPath() {
		return requiredJsPath;
	}
	public void setRequiredJsPath(List<String> requiredJsPath) {
		this.requiredJsPath = requiredJsPath;
	}
	@XmlElement(name="path")
	@XmlElementWrapper(name = "css")
	public List<String> getRequiredCssPath() {
		return requiredCssPath;
	}
	public void setRequiredCssPath(List<String> requiredCssPath) {
		this.requiredCssPath = requiredCssPath;
	}
}
