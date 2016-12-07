package com.frame.core.query.xml;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.frame.core.query.xml.defination.PageDefination;

public class PageDefinationHolder {
	private static final Logger LOGGER=LoggerFactory.getLogger(PageDefinationHolder.class);
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
			LOGGER.info("Load pageDefination: "+fileName+",use class "+loader);
			Resource resource= new ClassPathResource(fileName, loader);
			if ("file".equals(resource.getURL().getProtocol())) lastModified = resource.getFile().lastModified();
			context = JAXBContext.newInstance(PageDefination.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();  
			page = (PageDefination) unmarshaller.unmarshal(resource.getInputStream());
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
			Resource resource= new ClassPathResource(fileName, loader);
			if ("file".equals(resource.getURL().getProtocol())){
				long lastModified = resource.getFile().lastModified();
				if (this.lastModified>=lastModified) return;
				this.lastModified=lastModified;
			}else{
				return;
			}
			LOGGER.info("pageDefination out of date reload pageDefination: "+fileName+",use class "+loader);
			context = JAXBContext.newInstance(PageDefination.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();  
			page = (PageDefination) unmarshaller.unmarshal(resource.getInputStream());
		} catch (JAXBException | IOException e) {
			throw new PageDefinationLoadException(e);
		}
	}
	public PageDefination getPageDefination(){
		return page;
	}
}
