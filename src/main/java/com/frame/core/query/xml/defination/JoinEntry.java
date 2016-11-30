package com.frame.core.query.xml.defination;

import javax.xml.bind.annotation.XmlAttribute;

public class JoinEntry {
	private String field;
	private String as;
	public JoinEntry(){}
	
	public JoinEntry(String field, String as) {
		this.field = field;
		this.as = as;
	}

	@XmlAttribute
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	@XmlAttribute
	public String getAs() {
		return as;
	}
	public void setAs(String as) {
		this.as = as;
	}
}
