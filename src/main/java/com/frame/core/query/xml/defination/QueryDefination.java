package com.frame.core.query.xml.defination;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;

public class QueryDefination {
	private List<MappedClassEntry> mappedClass;
	private String where;
	private List<SortEntry> sortBy;
	private String showIndex;
	private List<ColumnDefination> columns;
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
	public List<ColumnDefination> getColumns() {
		return columns;
	}
	public void setColumns(List<ColumnDefination> columns) {
		this.columns = columns;
	}
}