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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.ItemNotFoundException;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.contentrepository.dao.hibernate.WorkspaceDAO;
import org.lamsfoundation.lams.contentrepository.service.IRepositoryService;
import org.lamsfoundation.lams.learningdesign.Activity;
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
import org.lamsfoundation.lams.learningdesign.dao.hibernate.ActivityDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.GroupingDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.LearningDesignDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.LicenseDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.TransitionDAO;
import org.lamsfoundation.lams.learningdesign.dto.AuthoringActivityDTO;
import org.lamsfoundation.lams.learningdesign.dto.GroupingDTO;
import org.lamsfoundation.lams.learningdesign.dto.LearningDesignDTO;
import org.lamsfoundation.lams.learningdesign.dto.TransitionDTO;
import org.lamsfoundation.lams.lesson.LessonClass;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.ToolContent;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.dao.hibernate.ToolDAO;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.usermanagement.User;
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
	private IWorkspaceFolderDAO workspaceFolderDAO;
	private LicenseDAO licenseDAO;
	private GroupingDAO groupingDAO;
	private TransitionDAO  transitionDAO;
	private LearningDesignDAO learningDesignDAO;
	
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
	
	/**
	 * @see org.lamsfoundation.lams.authoring.service.IExportToolContentService.registerFileHandleClass(String,String,String)
	 */
	public void registerFileHandleClass(String fileNodeClassName,String fileUuidFieldName, String fileVersionFieldName){
		fileHandleClassList.add(this.new FileHandleClassInfo(fileNodeClassName,fileUuidFieldName,fileVersionFieldName));
		
	}
	/**
	 * @throws ExportToolContentException 
	 * @see org.lamsfoundation.lams.authoring.service.IExportToolContentService.importLearningDesign(String)
	 */
	public void importLearningDesign(String learningDesignPath,User importer, Integer workspaceFolderUid) throws ImportToolContentException {
		
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
				ToolContentManager contentManager = (ToolContentManager) findToolService(toolDAO.getToolByID(activity.getToolID()));
				log.debug("Tool import content : " + activity.getTitle() +" by contentID :" + activity.getToolContentID());
				
				//change xml to Tool POJO 
				XStream toolXml = new XStream();
				Reader toolFile = new FileReader(new File(FileUtil.getFullPath(toolPath,TOOL_FILE_NAME)));;
				Object toolPOJO = toolXml.fromXML(toolFile);
				contentManager.importToolContent(toolPOJO);
			}
			
			saveLearningDesign(ldDto,importer,workspaceFolderUid,toolMapper);
		} catch (FileNotFoundException e) {
			throw new ImportToolContentException(e);
		} catch (ToolException e) {
			throw new ImportToolContentException(e);
		}
		
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
	
	private void saveLearningDesign(LearningDesignDTO dto, User importer, Integer workspaceFolderUid, Map<Long,ToolContent> toolMapper)
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
		Set<Activity> actList = new LinkedHashSet<Activity>();
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
		Set<Transition> transList = new LinkedHashSet<Transition>();
		for(TransitionDTO transDto: transDtoList){
			Transition trans = getTransition(transDto,activityMapper);
			transList.add(trans);
			
			//persist
			trans.setTransitionId(null);
			transitionDAO.insert(trans);
		}
		
		
		LearningDesign ld = getLearningDesign(dto,importer,workspaceFolderUid,actList,transList);
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
	 * 
	 * @return
	 * @throws ImportToolContentException 
	 */
	private LearningDesign getLearningDesign(LearningDesignDTO dto, User importer, Integer workspaceFolderUid,
			Set<Activity> actList, Set<Transition> transList) throws ImportToolContentException {
		LearningDesign ld = new LearningDesign();
	
		
		ld.setLearningDesignId(dto.getLearningDesignID());
		ld.setLearningDesignUIID(dto.getLearningDesignUIID());
		ld.setDescription(dto.getDescription());
		ld.setTitle(dto.getTitle());
		
		Integer actUid = dto.getFirstActivityUIID();
		if(actUid == null)
			ld.setFirstActivity(null);
		else
			ld.setFirstActivity(activityDAO.getActivityByActivityId(new Long(actUid.intValue())));
		
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
		
		if(workspaceFolderUid != null)
			ld.setWorkspaceFolder(workspaceFolderDAO.getWorkspaceFolderByID(workspaceFolderUid));
								 
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

		ld.setTransitions(transList);

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
		
		trans.setDescription(transDto.getDescription());
		
		trans.setFromActivity(activityMapper.get(transDto.getFromActivityID()));
		trans.setFromUIID(transDto.getFromUIID());
//		set to null 
//		trans.setLearningDesign();
		trans.setTitle(transDto.getTitle());
		
		trans.setToActivity(activityMapper.get(transDto.getToActivityID()));
		trans.setToUIID(transDto.getToUIID());
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
				ToolContent content = toolMapper.get(actDto.getToolID());
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
		act.setGroupingUIID(newGrouping.getGroupingUIID());

		act.setCreateDateTime(new Date());
		return act;
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
	public IWorkspaceFolderDAO getWorkspaceFolderDAO() {
		return workspaceFolderDAO;
	}
	public void setWorkspaceFolderDAO(IWorkspaceFolderDAO workspaceFolderDAO) {
		this.workspaceFolderDAO = workspaceFolderDAO;
	}
	public LicenseDAO getLicenseDAO() {
		return licenseDAO;
	}
	public void setLicenseDAO(LicenseDAO licenseDAO) {
		this.licenseDAO = licenseDAO;
	}
	public GroupingDAO getGroupingDAO() {
		return groupingDAO;
	}
	public void setGroupingDAO(GroupingDAO groupingDAO) {
		this.groupingDAO = groupingDAO;
	}
	public LearningDesignDAO getLearningDesignDAO() {
		return learningDesignDAO;
	}
	public void setLearningDesignDAO(LearningDesignDAO learningDesignDAO) {
		this.learningDesignDAO = learningDesignDAO;
	}
	public TransitionDAO getTransitionDAO() {
		return transitionDAO;
	}
	public void setTransitionDAO(TransitionDAO transitionDAO) {
		this.transitionDAO = transitionDAO;
	}


}
