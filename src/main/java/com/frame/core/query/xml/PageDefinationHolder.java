package com.frame.core.query.xml;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.frame.core.query.xml.defination.PageDefination;

public class PageDefinationHolder {
	public static class PageDefinationLoadException extends RuntimeException{
		private static final long serialVersionUID = 4989620748698016255L;
		public PageDefinationLoadException(Throwable t){super(t);}
		
	}
	PageDefination page;
	String fileName;
	Class<?> loader;
	long lastModified=0L;
	
	public PageDefinationHolder(String fileName,Class<?> loader){
		this.fileName=fileName;
		this.loader=loader;
		refresh();
	}
	public void refresh(){
		JAXBContext context;
		try {
			URL url= loader.getResource(fileName);
			if ("file".equals(url.getProtocol())) lastModified = new File(url.getFile()).lastModified();
			context = JAXBContext.newInstance(PageDefination.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();  
			page = (PageDefination) unmarshaller.unmarshal(url.openStream());
		} catch (JAXBException | IOException e) {
			throw new PageDefinationLoadException(e);
		}
	}
	/**
	 * 如果不是文件永远不过期
	 * @return
	 */
	public boolean isOutOfDate(){
		if (this.lastModified==0L) return false;
		URL url= loader.getResource(fileName);
		long lastModified = new File(url.getFile()).lastModified();
		return this.lastModified<lastModified;
	}
	public void refreshIfOutOfDate(){
		JAXBContext context;
		try {
			URL url= loader.getResource(fileName);
			if ("file".equals(url.getProtocol())){
				long lastModified = new File(url.getFile()).lastModified();
				if (this.lastModified>=lastModified) return;
				this.lastModified=lastModified;
			}else{
				return;
			}
			context = JAXBContext.newInstance(PageDefination.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();  
			page = (PageDefination) unmarshaller.unmarshal(url.openStream());
		} catch (JAXBException | IOException e) {
			throw new PageDefinationLoadException(e);
		}
	}
	public PageDefination getPageDefination(){
		return page;
	}
}
