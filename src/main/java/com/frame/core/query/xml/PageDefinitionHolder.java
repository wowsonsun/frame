package com.frame.core.query.xml;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.frame.webapp.controller.menu.MenuController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.frame.core.query.xml.definition.PageDefinition;

public class PageDefinitionHolder {
	private static final Logger LOGGER=LoggerFactory.getLogger(PageDefinitionHolder.class);
	public static class PageDefinitionLoadException extends RuntimeException{
		private static final long serialVersionUID = 4989620748698016255L;
		public PageDefinitionLoadException(Throwable t){super(t);}
	}
	private PageDefinition page;
	private String fileName;
	private Class<?> loader;
	private long lastModified=0L;
	private JAXBContext context;
	private Unmarshaller unmarshaller;
	public PageDefinitionHolder(String fileName, Class<?> loader){
		this.fileName=fileName;
		this.loader=loader;
		try {
			context = JAXBContext.newInstance(PageDefinition.class);
			unmarshaller = context.createUnmarshaller();
		} catch (JAXBException e) {
			throw new PageDefinitionLoadException(e);
		}
		refresh();
	}
	public void refresh(){
		try {
			LOGGER.info("Load pageDefinition: "+fileName+",use class "+loader);
			Resource resource= new ClassPathResource(fileName, loader);
			if ("file".equals(resource.getURL().getProtocol())) lastModified = resource.getFile().lastModified();
			page = (PageDefinition) unmarshaller.unmarshal(resource.getInputStream());
		} catch (JAXBException | IOException e) {
			throw new PageDefinitionLoadException(e);
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
			LOGGER.info("pageDefinition out of date reload pageDefinition: "+fileName+",use class "+loader);
			page = (PageDefinition) unmarshaller.unmarshal(resource.getInputStream());
		} catch (JAXBException | IOException e) {
			throw new PageDefinitionLoadException(e);
		}
	}
	public PageDefinition getPageDefinition(){
		return page;
	}

	public static void main(String[] args) throws  Exception{
		Resource resource= new ClassPathResource("pageDefinition.xml", MenuController.class);
		JAXBContext context = JAXBContext.newInstance(PageDefinition.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		PageDefinition page = (PageDefinition) unmarshaller.unmarshal(resource.getInputStream());
		System.out.println();
	}
}
