package com.frame.core.query.xml;

import java.util.Date;

import com.frame.core.components.GsonFactory;

public class DefaultDataFilter implements DataFilter{

	@Override
	public String filt(Object v) {
		if (v==null) return "";
		if (v instanceof Date) return GsonFactory.getDateFormart2().format(v);
		return v.toString();
	}

}
