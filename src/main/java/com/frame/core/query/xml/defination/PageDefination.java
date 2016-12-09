package com.frame.core.query.xml.defination;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.frame.entity.MenuEntity;
import com.google.gson.Gson;
@XmlRootElement(namespace = "http://dc.com/pageDefination")
public class PageDefination {
	public static class SaveOrUpdate{
		
	}
	public static class Delete{
		
	}
	private QueryDefination queryDefination;
	private SaveOrUpdate saveOrUpdate;
	private Delete delete;
//	public static void main(String[] args) throws JAXBException {
//		JAXBContext context=JAXBContext.newInstance(Defination.class);
//		Defination d=new Defination();
//		d.setQueryDefination(new QueryDefination());
//		d.getQueryDefination().setMappedClass(new ArrayList<MappedClassEntry>());
//		d.getQueryDefination().getMappedClass().add(new MappedClassEntry(MenuEntity.class, "m1"));
//		d.getQueryDefination().getMappedClass().add(new MappedClassEntry(MenuEntity.class, "m2"));
//		MappedClassEntry e=new MappedClassEntry(MenuEntity.class, "m3");
//		e.setJoin(new ArrayList<JoinEntry>());
//		e.getJoin().add(new JoinEntry("parent", "p"));
//		d.getQueryDefination().getMappedClass().add(e);
//		Marshaller marshaller = context.createMarshaller(); 
//		 marshaller.marshal(d, System.out);  
//	}
	public static void main(String[] args) throws JAXBException {
		JAXBContext context=JAXBContext.newInstance(PageDefination.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();  
		PageDefination d= (PageDefination) unmarshaller.unmarshal(PageDefination.class.getResource("../example.xml"));
		System.out.println(d);
	}
	/*public void beanToXML() {  
        Classroom classroom = new Classroom(1, "软件工程", 4);  
        Student student = new Student(101, "张三", 22, classroom);  
  
        try {  
            JAXBContext context = JAXBContext.newInstance(Student.class);  
            Marshaller marshaller = context.createMarshaller();  
            marshaller.marshal(student, System.out);  
        } catch (JAXBException e) {  
            e.printStackTrace();  
        }  
  
    } */
	public QueryDefination getQueryDefination() {
		return queryDefination;
	}
	public void setQueryDefination(QueryDefination querydefination) {
		this.queryDefination = querydefination;
	}
}
