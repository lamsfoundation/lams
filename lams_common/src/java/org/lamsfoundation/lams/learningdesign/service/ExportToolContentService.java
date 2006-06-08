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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.InvalidParameterException;
import org.lamsfoundation.lams.contentrepository.ItemNotFoundException;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ActivityOrderComparator;
import org.lamsfoundation.lams.learningdesign.ChosenGrouping;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.License;
import org.lamsfoundation.lams.learningdesign.OptionsActivity;
import org.lamsfoundation.lams.learningdesign.ParallelActivity;
import org.lamsfoundation.lams.learningdesign.PermissionGateActivity;
import org.lamsfoundation.lams.learningdesign.RandomGrouping;
import org.lamsfoundation.lams.learningdesign.ScheduleGateActivity;
import org.lamsfoundation.lams.learningdesign.SequenceActivity;
import org.lamsfoundation.lams.learningdesign.SynchGateActivity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.Transition;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
import org.lamsfoundation.lams.learningdesign.dao.IGroupingDAO;
import org.lamsfoundation.lams.learningdesign.dao.ILearningDesignDAO;
import org.lamsfoundation.lams.learningdesign.dao.ILicenseDAO;
import org.lamsfoundation.lams.learningdesign.dao.ITransitionDAO;
import org.lamsfoundation.lams.learningdesign.dto.AuthoringActivityDTO;
import org.lamsfoundation.lams.learningdesign.dto.GroupingDTO;
import org.lamsfoundation.lams.learningdesign.dto.LearningDesignDTO;
import org.lamsfoundation.lams.learningdesign.dto.TransitionDTO;
import org.lamsfoundation.lams.lesson.LessonClass;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.ToolContent;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.dao.IToolContentDAO;
import org.lamsfoundation.lams.tool.dao.IToolDAO;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.usermanagement.dao.IWorkspaceFolderDAO;
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
	private static final String USER_SERVICE_BEAN_NAME = "userManagementService";
	
	//export tool content zip file prefix
	public static final String EXPORT_TOOLCONTNET_ZIP_PREFIX = "lams_toolcontent_";
	public static final String EXPORT_TOOLCONTNET_FOLDER_SUFFIX = "export_toolcontent";
	public static final String EXPORT_TOOLCONTNET_ZIP_SUFFIX = ".zip";
	public static final String LEARNING_DESIGN_FILE_NAME = "learning_design.xml";
	public static final String TOOL_FILE_NAME = "tool.xml";
	public static final String TOOL_FAILED_FILE_NAME = "export_failed.xml";
	
	
	private Logger log = Logger.getLogger(ExportToolContentService.class);
	
	private ApplicationContext applicationContext;
	
	//save list of all tool file node class information. One tool may have over one file node, such as 
	//in share resource tool, it has contnent attachment and shared resource item attachement. 
	private List<NameInfo> fileHandleClassList;
	
	//spring injection properties
	private IActivityDAO activityDAO;
	private IToolDAO toolDAO;
	private IToolContentDAO toolContentDAO;
	private IWorkspaceFolderDAO workspaceFolderDAO;
	private ILicenseDAO licenseDAO;
	private IGroupingDAO groupingDAO;
	private ITransitionDAO  transitionDAO;
	private ILearningDesignDAO learningDesignDAO;
	/**
	 * Class of tool attachment file handler class and relative fields information container.
	 */
	private class NameInfo{
		
	
		//the Class instance according to className.
		public String className;
		public String uuidFieldName;
		public String versionFieldName;
		public String fileNameFieldName;
		public String mimeTypeFieldName;
		public String filePropertyFieldName;
		public String initalItemFieldName;
		
		public NameInfo(String className, String uuidFieldName, String versionFieldName){
			this.className = className;
			this.uuidFieldName = uuidFieldName;
			this.versionFieldName = versionFieldName;
		}
		public NameInfo(String className, String uuidFieldName, String versionFieldName,
				String fileNameFieldName, String filePropertyFieldName,String mimeTypeFieldName, 
				String initalItemFieldName) {
			this.className = className;
			this.uuidFieldName = uuidFieldName;
			this.versionFieldName = versionFieldName;
			this.fileNameFieldName = fileNameFieldName;
			this.filePropertyFieldName = filePropertyFieldName;
			this.mimeTypeFieldName = mimeTypeFieldName;
			this.initalItemFieldName = initalItemFieldName;
		}
	}
	
	/**
	 * File node value information container. 
	 */
	private class ValueInfo{
		//for unmarshal
		public NameInfo name;
		public Object instance;
		
		//for marshal
		public Long fileUuid;
		public Long fileVersionId;
		
		public ValueInfo(NameInfo name, Object instance){
			this.name = name;
			this.instance = instance;
		}
		
		public ValueInfo(Long uuid, Long versionId){
			this.fileUuid = uuid;
			this.fileVersionId = versionId;
		}
	}
	/**
	 * Proxy class for Default XStream converter.
	 *  
	 */
	private class FileInvocationHandler implements InvocationHandler{
		
		private Object obj;
		private List<ValueInfo> fileNodes;
		private List<NameInfo> fileHandleClassList;
		public FileInvocationHandler(Object obj){
			this.obj = obj;
			this.fileNodes = new ArrayList<ValueInfo>();
		}
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			Object result;
			try {
		    	
			    if(StringUtils.equals(method.getName(),"marshal")){
			    	for(NameInfo name:fileHandleClassList){
				    	if(args[0] != null && name.className.equals((args[0].getClass().getName()))){
							Long uuid = NumberUtils.createLong(BeanUtils.getProperty(args[0],name.uuidFieldName));
							//version id is optional
							Long version = null;
							if(name.versionFieldName != null)
								version = NumberUtils.createLong(BeanUtils.getProperty(args[0],name.versionFieldName));
							log.debug("XStream get file node ["+uuid +"," + version +"].");
							fileNodes.add(ExportToolContentService.this.new ValueInfo(uuid,version));
				    	}
			    	}
			    }

			    if(StringUtils.equals(method.getName(),"canConvert")){
			    	boolean canConvert = false;
			    	for(NameInfo info:fileHandleClassList){
				    	if(args[0] != null && info.className.equals(((Class)args[0]).getName())){
				    		log.debug("XStream will handle ["+info.className+"] as file node class.");
				    		canConvert = true;
				    		break;
				    	}
			    	}
			    	return canConvert;
			    }
			    
			    result = method.invoke(obj, args);
			    
			    if(StringUtils.equals(method.getName(),"unmarshal") && result != null){
			    	//During deserialize XML file into object, it will save file node into fileNodes
			    	for(NameInfo name:fileHandleClassList){
			    		if(name.className.equals(result.getClass().getName())){
			    			fileNodes.add(ExportToolContentService.this.new ValueInfo(name,result));
			    			break;
			    		}
			    	}
			    }
	        } catch (InvocationTargetException e) {
	        	throw e.getTargetException();
	        }
		        
			return result;
		}

		public List<ValueInfo> getFileNodes() {
			return fileNodes;
		}
		public List<NameInfo> getFileHandleClassList() {
			return fileHandleClassList;
		}
		public void setFileHandleClassList(List<NameInfo> fileHandleClassList) {
			this.fileHandleClassList = fileHandleClassList;
		}
	}
	/**
	 * This class is just for later system extent tool compaiblity strategy use. Currently, it just
	 * simple to get tool by same signature.
	 * 
	 * @author Steve.Ni
	 * 
	 * @version $Revision$
	 */
	public class ToolCompatibleStrategy{
		public Tool getTool(String toolSignature){
			return toolDAO.getToolBySignature(toolSignature);
		}
	}
	/**
	 * Default contructor method.
	 */
	public ExportToolContentService(){
		fileHandleClassList = new ArrayList<NameInfo>();
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
				try{
					contentManager.exportToolContent(activity.getToolContentID(),contentDir);
				}catch (Exception e) {
					String msg = activity.getToolDisplayName() + " export tool content failed:" + e.toString();
					log.error(msg);
					writeErrorToToolFile(contentDir,activity.getToolContentID(),msg);
				} 
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
		}
	}

	/**
	 * @throws ExportToolContentException 
	 * 
	 */
	public void exportToolContent(Long toolContentId, Object toolContentObj, IToolContentHandler toolContentHandler, String rootPath) 
				throws ExportToolContentException {
		try {
			//create tool's save path
			String toolPath = FileUtil.getFullPath(rootPath,toolContentId.toString());
			FileUtil.createDirectory(toolPath);
			
			//create tool xml file name : tool.xml
			String toolFileName = FileUtil.getFullPath(toolPath,TOOL_FILE_NAME);
			Writer toolFile = new FileWriter(new File(toolFileName));
			
			//serialize tool xml into local file.
			XStream toolXml = new XStream();
			//get back Xstream default convert, create proxy then register it to XStream parser.
			Converter c = toolXml.getConverterLookup().defaultConverter();
			FileInvocationHandler handler =null;
			if(!fileHandleClassList.isEmpty()){
				handler = new FileInvocationHandler(c);
				handler.setFileHandleClassList(fileHandleClassList);
				Converter  myc = (Converter) Proxy.newProxyInstance(c.getClass().getClassLoader(),new Class[]{Converter.class},handler);
				toolXml.registerConverter(myc);
			}
			
			//XML to Object
			toolXml.toXML(toolContentObj,toolFile);
			
			//get out the fileNodes
			if(handler != null){
				List<ValueInfo> list = handler.getFileNodes();
				for(ValueInfo fileNode:list){
					log.debug("Tool attachement file is going to save : " + fileNode.fileUuid);
					toolContentHandler.saveFile(fileNode.fileUuid,FileUtil.getFullPath(toolPath,fileNode.fileUuid.toString()));
				}
				list.clear();
			}
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
	
	/**
	 * @see org.lamsfoundation.lams.authoring.service.IExportToolContentService.registerFileHandleClass(String,String,String)
	 */
	public void registerFileClassForExport(String fileNodeClassName,String fileUuidFieldName, String fileVersionFieldName){
		fileHandleClassList.add(this.new NameInfo(fileNodeClassName,fileUuidFieldName,fileVersionFieldName));
	}
	public void registerFileClassForImport(String fileNodeClassName, String fileUuidFieldName,
			String fileVersionFieldName, String fileNameFieldName, String filePropertyFieldName, String mimeTypeFieldName,
			String initialItemFieldName) {
		fileHandleClassList.add(this.new NameInfo(fileNodeClassName, fileUuidFieldName, fileVersionFieldName,
				fileNameFieldName, filePropertyFieldName, mimeTypeFieldName, initialItemFieldName));
	}
	
	
	/**
	 * @throws ExportToolContentException 
	 * @see org.lamsfoundation.lams.authoring.service.IExportToolContentService.importLearningDesign(String)
	 */
	public void importLearningDesign(String learningDesignPath, User importer, Integer workspaceFolderUid) throws ImportToolContentException {
		
		try {
			//import learning design
			Reader ldFile = new FileReader(new File(FileUtil.getFullPath(learningDesignPath,LEARNING_DESIGN_FILE_NAME)));
			XStream designXml = new XStream();
			LearningDesignDTO ldDto = (LearningDesignDTO) designXml.fromXML(ldFile);
			log.debug("Learning design xml deserialize to LearingDesignDTO success.");
	
			//begin tool import
			Map<Long,ToolContent> toolMapper = new HashMap<Long,ToolContent>();
			List<AuthoringActivityDTO> activities = ldDto.getActivities();
			for(AuthoringActivityDTO activity : activities){
				String toolPath = FileUtil.getFullPath(learningDesignPath,activity.getToolContentID().toString());
				
				//To create a new toolContent according to imported tool signature name.
				//get tool by signature
				Tool newTool = new ToolCompatibleStrategy().getTool(activity.getToolSignature());
				ToolContent newContent = new ToolContent(newTool);
			    toolContentDAO.saveToolContent(newContent);
			    
				//put new toolContent and it is old toolContentID into 
				toolMapper.put(activity.getToolContentID(),newContent);
				
				//Invoke tool's importToolContent() method.
				ToolContentManager contentManager = (ToolContentManager) findToolService(newTool);
				log.debug("Tool begin to import content : " + activity.getTitle() +" by contentID :" + activity.getToolContentID());
				contentManager.importToolContent(newContent.getToolContentId(),importer.getUserId(),toolPath);
				log.debug("Tool content import success.");
			}
			
			// if workspaceFolderUid == null use the user's default folder
			WorkspaceFolder folder = null;
			if ( workspaceFolderUid != null ) {
				folder = workspaceFolderDAO.getWorkspaceFolderByID(workspaceFolderUid);
			} 				
			if ( folder == null && importer.getWorkspace() != null) {
				folder = importer.getWorkspace().getRootFolder();
			}
			if ( folder == null ) {
				String error = "Unable to save design in a folder - folder not found. Input folder uid="+workspaceFolderUid+
						" user's default folder "+importer.getWorkspace();
				log.error(error);
				throw new ImportToolContentException(error);
			}

			saveLearningDesign(ldDto,importer,folder,toolMapper);
			
		}catch (ToolException e) {
			throw new ImportToolContentException(e);
		} catch (FileNotFoundException e) {
			throw new ImportToolContentException(e);
		}
		
	}
	public Object importToolContent(String toolContentPath, IToolContentHandler toolContentHandler) throws ImportToolContentException{
		Object toolPOJO = null;
//		change xml to Tool POJO 
		XStream toolXml = new XStream();
		
		Converter c = toolXml.getConverterLookup().defaultConverter();
		FileInvocationHandler handler = null;
		if(!fileHandleClassList.isEmpty()){
			//create proxy class
			handler = new FileInvocationHandler(c);
			handler.setFileHandleClassList(fileHandleClassList);
			Converter  myc = (Converter) Proxy.newProxyInstance(c.getClass().getClassLoader(),new Class[]{Converter.class},handler);
			//registry to new proxy convert to XStream
			toolXml.registerConverter(myc);				
		}
		
		List<ValueInfo> valueList = null;
		try {
			Reader toolFile = new FileReader(new File(FileUtil.getFullPath(toolContentPath,TOOL_FILE_NAME)));
			toolPOJO = toolXml.fromXML(toolFile);
			
			//upload file node if has
			if(handler != null){
				valueList = handler.getFileNodes();
				for(ValueInfo fileNode:valueList){
					
					Long uuid = NumberUtils.createLong(BeanUtils.getProperty(fileNode.instance,fileNode.name.uuidFieldName));
					Long version = null;
					//optional
					try{
						version = NumberUtils.createLong(BeanUtils.getProperty(fileNode.instance,fileNode.name.versionFieldName));
					} catch (Exception e ) {
						log.debug("No method for version:" + fileNode.instance);
					}
					
					//according to upload rule: the file name will be uuid.
					String realFileName = uuid.toString();
					String fullFileName = FileUtil.getFullPath(toolContentPath,realFileName);
					log.debug("Tool attachement files/packages are going to upload to repository " + fullFileName);
					
					//to check if the file is package (with extension name ".zip") or single file (no extension name).
					File file = new File(fullFileName); 
					boolean isPackage = false;
					if(!file.exists()){
						file = new File(fullFileName+EXPORT_TOOLCONTNET_ZIP_SUFFIX);
						realFileName = realFileName + EXPORT_TOOLCONTNET_ZIP_SUFFIX;
						isPackage = true;
						//if this file is norpackage neither single file, throw exception.
						if(!file.exists())
							throw new ImportToolContentException("Content attached file/package can not be found: " + fullFileName + "(.zip)");
					}
					
					//more fields values for Repository upload use
					String fileName = BeanUtils.getProperty(fileNode.instance,fileNode.name.fileNameFieldName);
					String fileProperty = BeanUtils.getProperty(fileNode.instance,fileNode.name.filePropertyFieldName);
					//optional fields
					String mimeType = null;
					try {
						mimeType = BeanUtils.getProperty(fileNode.instance,fileNode.name.mimeTypeFieldName);
					} catch (Exception e ) {
						log.debug("No method for mimeType:" + fileNode.instance);
					}
					String initalItem = null;
					try {
						initalItem = BeanUtils.getProperty(fileNode.instance,fileNode.name.initalItemFieldName);
					} catch (Exception e ) {
						log.debug("No method for initial item:" + fileNode.instance);
					}
	
					InputStream is = new FileInputStream(file);
					//upload to repository: file or pacakge
					NodeKey key;
					if(!isPackage)
						key = toolContentHandler.uploadFile(is,fileName,mimeType,fileProperty);
					else{
						String packageDirectory = ZipFileUtil.expandZip(is, realFileName);
						key = toolContentHandler.uploadPackage(packageDirectory,initalItem);
					}
					
					//refresh file node Uuid and Version value to latest.
					BeanUtils.setProperty(fileNode.instance,fileNode.name.uuidFieldName,key.getUuid());
					//version id is optional
					if(fileNode.name.versionFieldName != null)
						BeanUtils.setProperty(fileNode.instance,fileNode.name.versionFieldName,key.getVersion());
				}
			}
		} catch (FileNotFoundException e) {
			throw new ImportToolContentException(e);
		} catch (InvalidParameterException e) {
			throw new ImportToolContentException(e);
		} catch (RepositoryCheckedException e) {
			throw new ImportToolContentException(e);
		} catch (ZipFileUtilException e) {
			throw new ImportToolContentException(e);
		} catch (IllegalAccessException e) {
			throw new ImportToolContentException(e);
		} catch (InvocationTargetException e) {
			throw new ImportToolContentException(e);
		} catch (NoSuchMethodException e) {
			throw new ImportToolContentException(e);
		}finally{
			if(fileHandleClassList != null)
				fileHandleClassList.clear();
			if( valueList != null)
				valueList.clear();
		}
		
		return toolPOJO;
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
	/**
	 * If there are any errors happen during tool exporting content. Writing failed message to file.
	 */
	private void writeErrorToToolFile(String rootPath,Long toolContentId, String msg) {
		//create tool's save path
		try {
			String toolPath = FileUtil.getFullPath(rootPath,toolContentId.toString());
			FileUtil.createDirectory(toolPath);
		
			//create tool xml file name : tool.xml
			String toolFileName = FileUtil.getFullPath(toolPath,TOOL_FAILED_FILE_NAME);
			Writer toolFile = new FileWriter(new File(toolFileName));
			toolFile.write(msg);
			
		} catch (FileUtilException e) {
			log.warn("Export error file write error:" + e.toString());
		} catch (IOException e) {
			log.warn("Export error file write error:" + e.toString());
		}
	}	
	private ILearningDesignService getLearningDesignService(){
		return (ILearningDesignService) applicationContext.getBean(LEARNING_DESIGN_SERVICE_BEAN_NAME);		
	}
	
	private Object findToolService(Tool tool) throws NoSuchBeanDefinitionException
    {
        return applicationContext.getBean(tool.getServiceName());
    }
	
	private void saveLearningDesign(LearningDesignDTO dto, User importer, WorkspaceFolder folder, Map<Long,ToolContent> toolMapper)
			throws ImportToolContentException {

		//grouping object list
		List<GroupingDTO> groupingDtoList = dto.getGroupings();
		Map<Long,Grouping> groupingMapper = new HashMap<Long,Grouping>();
		for(GroupingDTO groupingDto : groupingDtoList){
			Grouping grouping = getGrouping(groupingDto);
			groupingMapper.put(grouping.getGroupingId(),grouping);
			
			//persist
			grouping.setGroupingId(null);
			groupingDAO.insert(grouping);
		}
		
		//activity object list
		List<AuthoringActivityDTO> actDtoList = dto.getActivities();
		Set<Activity> actList = new TreeSet<Activity> (new ActivityOrderComparator());
		Map<Long,Activity> activityMapper = new HashMap<Long,Activity>();
		for(AuthoringActivityDTO actDto: actDtoList){
			Activity act = getActivity(actDto,groupingMapper,toolMapper);
			activityMapper.put(act.getActivityId(),act);
			actList.add(act);
			
			//persist
			act.setActivityId(null);
			activityDAO.insert(act);
		}

		//transition object list
		List<TransitionDTO> transDtoList = dto.getTransitions();
		Set<Transition> transList = new HashSet<Transition>();
		for(TransitionDTO transDto: transDtoList){
			Transition trans = getTransition(transDto,activityMapper);
			transList.add(trans);
			
			//persist
			trans.setTransitionId(null);
			//leave it to learning design to save it.
//			transitionDAO.insert(trans);
		}
		
		
		LearningDesign ld = getLearningDesign(dto,importer,folder,actList,transList,activityMapper);
//		persist
		learningDesignDAO.insert(ld);
	}
	/**
	 * Get learning design object from this Learning design DTO object. It also following our
	 * import rules:
	 * <li>lams_license - Assume same in all lams system. Import same ID</li>
	 * <li>lams_copy_type - Set to 1.This indicates it is "normal" design.</li>
	 * <li>lams_workspace_folder - An input parameters to let user choose import workspace</li>
	 * <li>User - The person who execute import action</li>
	 * <li>OriginalLearningDesign - set to null</li>
	 * @param activityMapper 
	 * 
	 * @return
	 * @throws ImportToolContentException 
	 */
	private LearningDesign getLearningDesign(LearningDesignDTO dto, User importer, WorkspaceFolder folder,
			Set<Activity> actList, Set<Transition> transList, Map<Long, Activity> activityMapper) throws ImportToolContentException {
		LearningDesign ld = new LearningDesign();
	
		if(dto == null)
			return ld;
		
		ld.setLearningDesignId(dto.getLearningDesignID());
		ld.setLearningDesignUIID(dto.getLearningDesignUIID());
		ld.setDescription(dto.getDescription());
		ld.setTitle(dto.getTitle());
		
		Integer actUiid = dto.getFirstActivityUIID();
		if(actUiid != null){
			for(Activity act : activityMapper.values()){
				if(actUiid.equals(act.getActivityUIID())){
					ld.setFirstActivity(act);
					break;
				}
			}
		}
		
		ld.setMaxID(dto.getMaxID());
		ld.setValidDesign(dto.getValidDesign());
		ld.setReadOnly(dto.getReadOnly());
		ld.setDateReadOnly(dto.getDateReadOnly());
		ld.setOfflineInstructions(dto.getOfflineInstructions());	
		ld.setOnlineInstructions(dto.getOnlineInstructions());
		
		ld.setHelpText(dto.getHelpText());
		//set to 1
		ld.setCopyTypeID(1);
		ld.setCreateDateTime(dto.getCreateDateTime());
		ld.setVersion(dto.getVersion());
		
		if(folder != null)
			ld.setWorkspaceFolder(folder);
								 
		ld.setDuration(dto.getDuration());
		ld.setLicenseText(dto.getLicenseText());
		
		Long licenseId = dto.getLicenseID();
		if(licenseId != null){
			License license = licenseDAO.getLicenseByID(licenseId);
			if(license == null)
				throw new ImportToolContentException("Import failed: License ["+dto.getLicenseText()+ "] does not exist in target database");
			ld.setLicense(licenseDAO.getLicenseByID(licenseId));
			ld.setLicenseText(dto.getLicenseText());
		}
		
		ld.setLessonOrgID(dto.getLessonOrgID());
		
		ld.setLessonOrgName(dto.getLessonOrgName());
		ld.setLessonID(dto.getLessonID());
		ld.setLessonName(dto.getLessonName());
		ld.setLessonStartDateTime(dto.getLessonStartDateTime());
		ld.setLastModifiedDateTime(dto.getLastModifiedDateTime());

		//set learning design to transition.
		for(Transition trans:transList){
			trans.setLearningDesign(ld);
		}
		ld.setTransitions(transList);

		for(Activity act :actList)
			act.setLearningDesign(ld);
		ld.setActivities(actList);
		
		ld.setCreateDateTime(new Date());
		ld.setLastModifiedDateTime(new Date());
		ld.setUser(importer);
		return ld;
	}
	/**
	 * Return Grouping object from given GroupingDTO.
	 * 
	 * @param groupingDto
	 * @return
	 */
	private Grouping getGrouping(GroupingDTO groupingDto) {
		Grouping grouping = null;
		if(groupingDto == null)
			return grouping;
		
		Integer type = groupingDto.getGroupingTypeID();
		
//		grouping.setActivities();
		if (Grouping.CHOSEN_GROUPING_TYPE.equals(type)) {
			grouping = new ChosenGrouping();
		}else if (Grouping.RANDOM_GROUPING_TYPE.equals(type)) {
			grouping = new RandomGrouping();
			((RandomGrouping)grouping).setLearnersPerGroup(groupingDto.getLearnersPerGroup());
			((RandomGrouping)grouping).setNumberOfGroups(groupingDto.getNumberOfGroups());
		}else if (Grouping.CLASS_GROUPING_TYPE.equals(type)) {
			grouping = new LessonClass();
		}
		
		//commont fields
		grouping.setGroupingId(groupingDto.getGroupingID());
		grouping.setGroupingUIID(groupingDto.getGroupingUIID());
		grouping.setMaxNumberOfGroups(groupingDto.getMaxNumberOfGroups());
		
		return grouping;
	}
	
	private Transition getTransition(TransitionDTO transDto, Map<Long, Activity> activityMapper) {
		Transition trans = new Transition();
		
		if(transDto == null)
			return trans;
		
		trans.setDescription(transDto.getDescription());
		Activity fromAct = activityMapper.get(transDto.getFromActivityID());
		trans.setFromActivity(fromAct);
		trans.setFromUIID(fromAct.getActivityUIID());
//		set to null 
//		trans.setLearningDesign();
		trans.setTitle(transDto.getTitle());
		
		Activity toAct = activityMapper.get(transDto.getToActivityID());
		trans.setToActivity(toAct);
		trans.setToUIID(toAct.getActivityUIID());
		trans.setTransitionId(transDto.getTransitionID());
		trans.setTransitionUIID(transDto.getTransitionUIID());
		
		//reste value
		trans.setCreateDateTime(new Date());
		return trans;
	}
	/**
	 * 
	 * @param actDto
	 * @param groupingList
	 * @param toolMapper
	 * @return
	 */
	private Activity getActivity(AuthoringActivityDTO actDto, Map<Long, Grouping> groupingList, Map<Long,ToolContent> toolMapper) {
		Activity act = null;
		if(actDto == null)
			return act;
		int type = actDto.getActivityTypeID().intValue();
		Grouping newGrouping;
		switch(type){
			case Activity.TOOL_ACTIVITY_TYPE:
				act = new ToolActivity();
				//get back the toolContent in new system by toolContentID in old system.
				ToolContent content = toolMapper.get(actDto.getToolContentID());
				((ToolActivity)act).setTool(content.getTool());
				((ToolActivity)act).setToolContentId(content.getToolContentId());
				((ToolActivity)act).setToolSessions(null);
				break;
			case Activity.GROUPING_ACTIVITY_TYPE:
				act = new GroupingActivity();
				newGrouping = groupingList.get(actDto.getCreateGroupingID());
				((GroupingActivity)act).setCreateGrouping(newGrouping);
				((GroupingActivity)act).setCreateGroupingUIID(newGrouping.getGroupingUIID());
				break;
			case Activity.SYNCH_GATE_ACTIVITY_TYPE:
				act = new SynchGateActivity();
				((SynchGateActivity)act).setGateActivityLevelId(actDto.getGateActivityLevelID());
				//always set false
				((SynchGateActivity)act).setGateOpen(false);
				((SynchGateActivity)act).setWaitingLearners(null);
				break;
			case Activity.SCHEDULE_GATE_ACTIVITY_TYPE:
				act = new ScheduleGateActivity();
				((ScheduleGateActivity)act).setGateActivityLevelId(actDto.getGateActivityLevelID());
				((ScheduleGateActivity)act).setWaitingLearners(null);
				//always set false
				((ScheduleGateActivity)act).setGateOpen(false);
				
				((ScheduleGateActivity)act).setGateEndDateTime(actDto.getGateEndDateTime());
				((ScheduleGateActivity)act).setGateStartDateTime(actDto.getGateStartDateTime());
				((ScheduleGateActivity)act).setGateStartTimeOffset(actDto.getGateStartTimeOffset());
				((ScheduleGateActivity)act).setGateEndTimeOffset(actDto.getGateEndTimeOffset());
				break;
			case Activity.PERMISSION_GATE_ACTIVITY_TYPE:
				act = new PermissionGateActivity();
				((PermissionGateActivity)act).setGateActivityLevelId(actDto.getGateActivityLevelID());
				((PermissionGateActivity)act).setGateOpen(false);
				((PermissionGateActivity)act).setWaitingLearners(null);
				break;
			case Activity.PARALLEL_ACTIVITY_TYPE:
				act = new ParallelActivity();
				break;
			case Activity.OPTIONS_ACTIVITY_TYPE:
				act = new OptionsActivity();
				((OptionsActivity)act).setMaxNumberOfOptions(actDto.getMaxOptions());
				((OptionsActivity)act).setMinNumberOfOptions(actDto.getMinOptions());
				((OptionsActivity)act).setOptionsInstructions(actDto.getOptionsInstructions());
				break;
			case Activity.SEQUENCE_ACTIVITY_TYPE:
				act = new SequenceActivity();
				break;
		}		
		
		act.setGroupingSupportType(actDto.getGroupingSupportType());
		act.setActivityUIID(actDto.getActivityUIID());
		act.setActivityCategoryID(actDto.getActivityCategoryID());
		act.setActivityId(actDto.getActivityID());
		act.setActivityTypeId(actDto.getActivityTypeID());
		act.setApplyGrouping(actDto.getApplyGrouping());
		act.setDefineLater(actDto.getDefineLater());
		act.setDescription(actDto.getDescription());
		act.setHelpText(actDto.getHelpText());
		act.setLanguageFile(actDto.getLanguageFile());
//		act.setLearningDesign();
		//TODO: be to decided by Fiona
//		actDto.getLearningDesignID()
//		act.setLearningLibrary();
//		actDto.getLibraryActivityID()
//		act.setLibraryActivity();
		act.setLibraryActivityUiImage(actDto.getLibraryActivityUIImage());
		act.setOrderId(actDto.getOrderID());
		
//		actDto.getParentActivityID()
//		act.setParentActivity();
		
		act.setParentUIID(actDto.getParentUIID());
		act.setRunOffline(actDto.getRunOffline());
		act.setTitle(actDto.getTitle());
		
//		act.setTransitionFrom();
//		act.setTransitionTo();
		
		act.setXcoord(actDto.getxCoord());
		act.setYcoord(actDto.getyCoord());
		
		//tranfer old grouping to latest
		newGrouping = groupingList.get(actDto.getGroupingID());
		act.setGrouping(newGrouping);
		if(newGrouping != null)
			act.setGroupingUIID(newGrouping.getGroupingUIID());

		act.setCreateDateTime(new Date());
		return act;
	}
	//******************************************************************
	// Spring injection properties set/get
	//******************************************************************
	public IActivityDAO getActivityDAO() {
		return activityDAO;
	}
	public void setActivityDAO(IActivityDAO activityDAO) {
		this.activityDAO = activityDAO;
	}
	public IGroupingDAO getGroupingDAO() {
		return groupingDAO;
	}
	public void setGroupingDAO(IGroupingDAO groupingDAO) {
		this.groupingDAO = groupingDAO;
	}
	public ILearningDesignDAO getLearningDesignDAO() {
		return learningDesignDAO;
	}
	public void setLearningDesignDAO(ILearningDesignDAO learningDesignDAO) {
		this.learningDesignDAO = learningDesignDAO;
	}
	public ILicenseDAO getLicenseDAO() {
		return licenseDAO;
	}
	public void setLicenseDAO(ILicenseDAO licenseDAO) {
		this.licenseDAO = licenseDAO;
	}
	public IToolContentDAO getToolContentDAO() {
		return toolContentDAO;
	}
	public void setToolContentDAO(IToolContentDAO toolContentDAO) {
		this.toolContentDAO = toolContentDAO;
	}
	public IToolDAO getToolDAO() {
		return toolDAO;
	}
	public void setToolDAO(IToolDAO toolDAO) {
		this.toolDAO = toolDAO;
	}
	public ITransitionDAO getTransitionDAO() {
		return transitionDAO;
	}
	public void setTransitionDAO(ITransitionDAO transitionDAO) {
		this.transitionDAO = transitionDAO;
	}
	public IWorkspaceFolderDAO getWorkspaceFolderDAO() {
		return workspaceFolderDAO;
	}
	public void setWorkspaceFolderDAO(IWorkspaceFolderDAO workspaceFolderDAO) {
		this.workspaceFolderDAO = workspaceFolderDAO;
	}



}
