package com.frame.core.query.xml.defination;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

public class ColumnDefination {
	private String header;
	private String field;
	private String fromAlias;
	private String width;
	private String sortable;
	private boolean hidden=false;
	private String staticColumnData;
	private Class<?> filter;
	private String callback;
	//private String queryComparator;
	@XmlAttribute
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	@XmlAttribute
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	@XmlAttribute
	public String getFromAlias() {
		return fromAlias;
	}
	public void setFromAlias(String fromAlias) {
		this.fromAlias = fromAlias;
	}
	@XmlAttribute
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	@XmlAttribute
	public String getSortable() {
		return sortable;
	}
	public void setSortable(String sortable) {
		this.sortable = sortable;
	}
	@XmlAttribute
	public boolean getHidden() {
		return hidden;
	}
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	@XmlAttribute
	public String getCallback() {
		return callback;
	}
	public void setCallback(String callback) {
		this.callback = callback;
	}
	@XmlAttribute
	@XmlJavaTypeAdapter(value=MappedClassEntry.class)
	public Class<?> getFilter() {
		return filter;
	}
	public void setFilter(Class<?> filter) {
		this.filter = filter;
	}
	@XmlAttribute
	public String getStaticColumnData() {
		return staticColumnData;
	}
	public void setStaticColumnData(String staticColumnData) {
		this.staticColumnData = staticColumnData;
	}
}
