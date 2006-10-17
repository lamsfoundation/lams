package org.lamsfoundation.lams.learningdesign.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import com.thoughtworks.xstream.XStream;

public class ToolContentVersionFilter {

	private List<RemovedField> removedFieldList;
	private List<AddedField> addedFieldList;

	public ToolContentVersionFilter(){
		removedFieldList = new ArrayList<RemovedField>();
		addedFieldList = new ArrayList<AddedField>();
	}
	//container class for removed class
	class RemovedField{
		public Class ownerClass;
		public String fieldname;
		public RemovedField(Class ownerClass, String fieldname) {
			this.ownerClass = ownerClass;
			this.fieldname = fieldname;
		}
	}
//	container class for added class
	class AddedField{
		public Class ownerClass;
		public String fieldname;
		public Object defaultValue;
		public AddedField(Class ownerClass2, String fieldname2, Object defaultValue2) {
			this.ownerClass = ownerClass2;
			this.fieldname = fieldname2;
			this.defaultValue = defaultValue2;
		}
		
	}
	public void removeField(Class ownerClass, String fieldname){
		removedFieldList.add(new RemovedField(ownerClass,fieldname));
	}
	
	public void addField(Class ownerClass, String fieldname, Object defaultValue){
		addedFieldList.add(new AddedField(ownerClass, fieldname, defaultValue));
	}

	public void transformXML(String toolFilePath) throws JDOMException, IOException{
		File toolFile = new File(toolFilePath);
		
		SAXBuilder sax = new SAXBuilder();
		Document doc = sax.build(new FileInputStream(toolFile));
		Element root = doc.getRootElement();
		retrieveXML(root);
		
		toolFile.renameTo(new File(toolFilePath +"_oldver"));
		
		File newToolFile = new File(toolFilePath);
		XMLOutputter output = new XMLOutputter();
		output.output(doc, new FileOutputStream(newToolFile));
	}
	
	private void retrieveXML(Element root) throws JDOMException{
		//collect all removed fields in this class
		List<String> clzRemoveFlds = new ArrayList<String>();
		for (RemovedField remove : removedFieldList) {
			if(StringUtils.equals(root.getName(),remove.ownerClass.getName())){
				clzRemoveFlds.add(remove.fieldname);
			}
		}
		//add all new fields for this class
		for (AddedField added : addedFieldList) {
			if(StringUtils.equals(root.getName(),added.ownerClass.getName())){
				Writer eleWriter = new StringWriter();
				XStream eleX = new XStream();
				eleX.toXML(added.defaultValue,eleWriter);
				
				SAXBuilder eleBuilder = new SAXBuilder();
				Document eledoc = eleBuilder.build(new StringReader(eleWriter.toString()));
				Element eleRoot = eledoc.getRootElement();
				eleRoot.setName(added.fieldname);
				root.addContent(eleRoot);
			}
		}
		
		List<Object> children = root.getChildren();
		for (Object child: children) {
			if(child instanceof Element){
				Element ele = (Element)child;
				if(clzRemoveFlds.contains(ele.getName())){
					continue;
				}
				retrieveXML(ele);
			}
		}
		for(String name:clzRemoveFlds)
			root.removeChild(name);

	}
}
