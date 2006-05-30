/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.learningdesign.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.ItemNotFoundException;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.ActivityDAO;
import org.lamsfoundation.lams.learningdesign.dto.AuthoringActivityDTO;
import org.lamsfoundation.lams.learningdesign.dto.LearningDesignDTO;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.dao.hibernate.ToolDAO;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.FileUtilException;
import org.lamsfoundation.lams.util.zipfile.ZipFileUtil;
import org.lamsfoundation.lams.util.zipfile.ZipFileUtilException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
/**
 * Export tool content service bean.
 * @author Steve.Ni
 * 
 * @version $Revision$
 */
public class ExportToolContentService implements IExportToolContentService, ApplicationContextAware {
	public static final String LEARNING_DESIGN_SERVICE_BEAN_NAME = "learningDesignService";
	//export tool content zip file prefix
	public static final String EXPORT_TOOLCONTNET_ZIP_PREFIX = "lams_toolcontent_";
	public static final String EXPORT_TOOLCONTNET_FOLDER_SUFFIX = "export_toolcontent";
	public static final String EXPORT_TOOLCONTNET_ZIP_SUFFIX = ".zip";
	public static final String LEARNING_DESIGN_FILE_NAME = "learning_design.xml";
	private static final String TOOL_FILE_NAME = "tool.xml";
	
	private Logger log = Logger.getLogger(ExportToolContentService.class);
	
	private ApplicationContext applicationContext;
	
	//save list of all tool file node class information. One tool may have over one file node, such as 
	//in share resource tool, it has contnent attachment and shared resource item attachement. 
	private List<FileHandleClassInfo> fileHandleClassList;
	
	//spring injection properties
	private ActivityDAO activityDAO;
	private ToolDAO toolDAO;
	
	/**
	 * Class of tool attachment file handler information container.
	 */
	private class FileHandleClassInfo{
		
		//the Class instance according to className.
		public Class handlerClass;
		public String className;
		public String uuidFieldName;
		public String versionFieldName;
		
		public FileHandleClassInfo(String className, String uuidFieldName, String versionFieldName){
			this.className = className;
			this.uuidFieldName = uuidFieldName;
			this.versionFieldName = versionFieldName;
		}
	}

	
	/**
	 * File node information container. 
	 */
	private class FileNodeInfo{
		private Long fileUuid;
		private Long fileVersionId;
		
		public FileNodeInfo(Long uuid, Long versionId){
			this.fileUuid = uuid;
			this.fileVersionId = versionId;
		}
		public Long getFileUuid() {
			return fileUuid;
		}
		public void setFileUuid(Long fileUuid) {
			this.fileUuid = fileUuid;
		}
		public Long getFileVersionId() {
			return fileVersionId;
		}
		public void setFileVersionId(Long fileVersionId) {
			this.fileVersionId = fileVersionId;
		}
		
	}
	/**
	 * Proxy class for Default XStream converter.
	 *  
	 */
	private class FileInvocationHandler implements InvocationHandler{
		
		private Object obj;
		private List<FileNodeInfo> fileNodes;
		private List<FileHandleClassInfo> fileHandleClassList;
		public FileInvocationHandler(Object obj){
			this.obj = obj;
			this.fileNodes = new ArrayList<FileNodeInfo>();
		}
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			Object result;
			try {
		    	
			    if(StringUtils.equals(method.getName(),"marshal")){
			    	for(FileHandleClassInfo info:fileHandleClassList){
				    	if(args[0] != null && info.className.equals((args[0].getClass().getName()))){
							Long uuid = NumberUtils.createLong(BeanUtils.getProperty(args[0],info.uuidFieldName));
							Long version = NumberUtils.createLong(BeanUtils.getProperty(args[0],info.versionFieldName));
							log.debug("XStream get file node ["+uuid +"," + version +"].");
							fileNodes.add(ExportToolContentService.this.new FileNodeInfo(uuid,version));
				    	}
			    	}
			    }
			    if(StringUtils.equals(method.getName(),"canConvert")){
			    	boolean flag = false;
			    	for(FileHandleClassInfo info:fileHandleClassList){
				    	if(args[0] != null && info.className.equals(((Class)args[0]).getName())){
				    		log.debug("XStream will handle ["+info.className+"] as file node class.");
				    		flag = true;
				    		break;
				    	}
			    	}
			    	return flag;
			    }
			    result = method.invoke(obj, args);
	        } catch (InvocationTargetException e) {
	        	throw e.getTargetException();
	        } catch (Exception e) {
	        	throw new RuntimeException("unexpected invocation exception: " + e.getMessage());
	        }
		        
			return result;
		}

		public List<FileNodeInfo> getFileNodes() {
			return fileNodes;
		}
		public List<FileHandleClassInfo> getFileHandleClassList() {
			return fileHandleClassList;
		}
		public void setFileHandleClassList(List<FileHandleClassInfo> fileHandleClassList) {
			this.fileHandleClassList = fileHandleClassList;
			
			//initial class instance.
			for(FileHandleClassInfo info:fileHandleClassList){
				try {
					info.handlerClass = Class.forName(info.className);
				} catch (ClassNotFoundException e) {
					throw new RuntimeException("unexpected invocation exception: " + e.getMessage());
				}
			}
		}
	}
	/**
	 * Default contructor method.
	 */
	public ExportToolContentService(){
		fileHandleClassList = new ArrayList<FileHandleClassInfo>();
	}
	/**
	 * @see org.lamsfoundation.lams.authoring.service.IExportToolContentService.exportLearningDesign(Long)
	 */
	public String exportLearningDesign(Long learningDesignId) throws ExportToolContentException{
		try {
			//root temp directory, put target zip file
			String rootDir = FileUtil.createTempDirectory(EXPORT_TOOLCONTNET_FOLDER_SUFFIX);
			String contentDir = FileUtil.getFullPath(rootDir, "content");
			FileUtil.createDirectory(contentDir);
			
			//zip file name with full path
			String targetZipFileName = EXPORT_TOOLCONTNET_ZIP_PREFIX + learningDesignId + EXPORT_TOOLCONTNET_ZIP_SUFFIX;
			
			//learing design file name with full path
			String ldFileName = FileUtil.getFullPath(contentDir,LEARNING_DESIGN_FILE_NAME);
			Writer ldFile = new FileWriter(new File(ldFileName));
			
			//get learning desing and serialize it to XML file.
			ILearningDesignService service =  getLearningDesignService();
			LearningDesignDTO ldDto = service.getLearningDesignDTO(learningDesignId);
			XStream designXml = new XStream();
			designXml.toXML(ldDto,ldFile);
			log.debug("Learning design xml export success");
			
			//iterator all activities in this learning design and export their content to given folder.
			//The content will contain tool.xml and attachment files of tools from LAMS repository.
			List<AuthoringActivityDTO> activities = ldDto.getActivities();
			for(AuthoringActivityDTO activity : activities){
				ToolContentManager contentManager = (ToolContentManager) findToolService(toolDAO.getToolByID(activity.getToolID()));
				log.debug("Tool export content : " + activity.getTitle() +" by contentID :" + activity.getToolContentID());
				contentManager.exportToolContent(activity.getToolContentID(),contentDir);
			}
			
			log.debug("Create export content target zip file. File name is " + targetZipFileName);
			//create zip file and return zip full file name
			return ZipFileUtil.createZipFile(targetZipFileName, contentDir,rootDir);
		} catch (FileUtilException e) {
			log.error("FileUtilExcpetion:" + e.toString());
			throw new ExportToolContentException(e);
		} catch (ZipFileUtilException e) {
			log.error("ZipFileUtilException:" + e.toString());
			throw new ExportToolContentException(e);
		} catch (IOException e) {
			log.error("IOException:" + e.toString());
			throw new ExportToolContentException(e);
		} catch (DataMissingException e) {
			log.error("DataMissingException:" + e.toString());
			throw new ExportToolContentException(e);
		} catch (ToolException e) {
			log.error("ToolException:" + e.toString());
			throw new ExportToolContentException(e);
		}
	}
	/**
	 * @throws ExportToolContentException 
	 * 
	 */
	public void exportToolContent(Long toolContentId, Object toolContentObj, IToolContentHandler toolContentHandler, String toPath) 
				throws ExportToolContentException {
		try {
			//create tool's save path
			String toolPath = FileUtil.getFullPath(toPath,toolContentId.toString());
			FileUtil.createDirectory(toolPath);
			
			//create tool xml file name : tool.xml
			String toolFileName = FileUtil.getFullPath(toolPath,TOOL_FILE_NAME);
			Writer toolFile = new FileWriter(new File(toolFileName));
			
			//serialize tool xml into local file.
			XStream toolXml = new XStream();
			Converter c = toolXml.getConverterLookup().defaultConverter();
			FileInvocationHandler handler = new FileInvocationHandler(c);
			handler.setFileHandleClassList(fileHandleClassList);
			Converter  myc = (Converter) Proxy.newProxyInstance(c.getClass().getClassLoader(),new Class[]{Converter.class},handler);
			toolXml.registerConverter(myc);
			toolXml.toXML(toolContentObj,toolFile);
			
			//get out the fileNodes
			List<FileNodeInfo> list = handler.getFileNodes();
			for(FileNodeInfo fileNode:list){
				log.debug("Tool attachement file is going to save : " + fileNode.getFileUuid());
				toolContentHandler.saveFile(fileNode.getFileUuid(),toolPath+File.separator+fileNode.getFileUuid());
			}
			list.clear();
		} catch (ItemNotFoundException e) {
			throw new ExportToolContentException(e);
		} catch (RepositoryCheckedException e) {
			throw new ExportToolContentException(e);
		} catch (IOException e) {
			throw new ExportToolContentException(e);
		} catch (FileUtilException e) {
			throw new ExportToolContentException(e);
		} finally{
			if(fileHandleClassList != null)
				fileHandleClassList.clear();
		}
		
	}
	public void registerFileHandleClass(String fileNodeClassName,String fileUuidFieldName, String fileVersionFieldName){
		fileHandleClassList.add(this.new FileHandleClassInfo(fileNodeClassName,fileUuidFieldName,fileVersionFieldName));
		
	}
	//******************************************************************
	// ApplicationContextAware method implementation
	//******************************************************************
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.applicationContext = context;
	}
	//******************************************************************
	// Private methods
	//******************************************************************
	private ILearningDesignService getLearningDesignService(){
		return (ILearningDesignService) applicationContext.getBean(LEARNING_DESIGN_SERVICE_BEAN_NAME);		
	}
	
	private Object findToolService(Tool tool) throws NoSuchBeanDefinitionException
    {
        return applicationContext.getBean(tool.getServiceName());
    }
	//******************************************************************
	// Spring injection properties set/get
	//******************************************************************
	public ActivityDAO getActivityDAO() {
		return activityDAO;
	}
	public void setActivityDAO(ActivityDAO activityDAO) {
		this.activityDAO = activityDAO;
	}
	public ToolDAO getToolDAO() {
		return toolDAO;
	}
	public void setToolDAO(ToolDAO toolDAO) {
		this.toolDAO = toolDAO;
	}


}
