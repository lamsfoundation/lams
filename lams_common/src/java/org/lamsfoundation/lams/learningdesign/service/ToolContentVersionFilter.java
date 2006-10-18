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
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import com.thoughtworks.xstream.XStream;
/**
 * Super class for all Import content Version Filter. The child class method must follow name conversion.  
 * <ol>
 * 	<li>upXXXToXXX</li>
 * 	<li>downXXXToXXX</li>
 * </ol>
 * 
 * The XXX must be integer format, which is Tool version number.
 * <p>
 * For more detail, in <a href="http://wiki.lamsfoundation.org/display/lams/How+to+implement+export+and+import+tool+content">wiki</a>.
 * @author Dapeng.Ni
 *
 */
public class ToolContentVersionFilter {

	private static final Logger log = Logger.getLogger(ToolContentVersionFilter.class);
	
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
	/**
	 * When a field is removed to tool Hibernate POJO class, this method must be call in
	 * upXXXToYYY()/downXXXToYYY() methods.
	 *  
	 * @param ownerClass
	 * @param fieldname
	 */
	public void removeField(Class ownerClass, String fieldname){
		removedFieldList.add(new RemovedField(ownerClass,fieldname));
	}
	
	/**
	 * When a field is added to tool Hibernate POJO class, this method is optional in
	 * upXXXToYYY()/downXXXToYYY() methods. It could set default value for this added fields.
	 *  
	 * @param ownerClass
	 * @param fieldname
	 */
	public void addField(Class ownerClass, String fieldname, Object defaultValue){
		addedFieldList.add(new AddedField(ownerClass, fieldname, defaultValue));
	}

	/**
	 * Call by lams import tool service core. Do not use it in tool version filter class.
	 * @param toolFilePath
	 * @throws JDOMException
	 * @throws IOException
	 */
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
				log.debug("Field "+ remove.fieldname+" in class " + remove.ownerClass.getName() + " is going to leave.");
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
				
				log.debug("Field "+ added.fieldname+" in class " + added.ownerClass.getName() + " is add by value " + added.defaultValue);
			}
		}
		
		//remove fields
		List<Object> children = root.getChildren();
		for (Object child: children) {
			if(child instanceof Element){
				Element ele = (Element)child;
				//this node already removed, no necessary retrieve its children level
				if(clzRemoveFlds.contains(ele.getName())){
					continue;
				}
				//recusive current node's children level.
				retrieveXML(ele);
			}
		}
		//retrieve all current node's children, if found some element is in removed list, then remove it.
		for(String name:clzRemoveFlds){
			if(root.removeChild(name))
				log.debug("Field "+ name + " is removed.");
			else
				log.debug("Failed remove field "+ name + ".");
		}

	}
}
