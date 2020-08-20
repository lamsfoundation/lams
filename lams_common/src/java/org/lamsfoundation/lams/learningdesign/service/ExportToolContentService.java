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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.learningdesign.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.contentrepository.exception.InvalidParameterException;
import org.lamsfoundation.lams.contentrepository.exception.ItemNotFoundException;
import org.lamsfoundation.lams.contentrepository.exception.RepositoryCheckedException;
import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ActivityEvaluation;
import org.lamsfoundation.lams.learningdesign.ActivityOrderComparator;
import org.lamsfoundation.lams.learningdesign.BranchActivityEntry;
import org.lamsfoundation.lams.learningdesign.BranchCondition;
import org.lamsfoundation.lams.learningdesign.BranchingActivity;
import org.lamsfoundation.lams.learningdesign.ChosenGrouping;
import org.lamsfoundation.lams.learningdesign.Competence;
import org.lamsfoundation.lams.learningdesign.CompetenceMapping;
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.learningdesign.ConditionGateActivity;
import org.lamsfoundation.lams.learningdesign.DataFlowObject;
import org.lamsfoundation.lams.learningdesign.DataTransition;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.learningdesign.LearnerChoiceGrouping;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.LearningDesignAnnotation;
import org.lamsfoundation.lams.learningdesign.License;
import org.lamsfoundation.lams.learningdesign.McqImportContentVersionFilter;
import org.lamsfoundation.lams.learningdesign.OptionsActivity;
import org.lamsfoundation.lams.learningdesign.OptionsWithSequencesActivity;
import org.lamsfoundation.lams.learningdesign.PasswordGateActivity;
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
import org.lamsfoundation.lams.learningdesign.dao.ILearningLibraryDAO;
import org.lamsfoundation.lams.learningdesign.dao.ILicenseDAO;
import org.lamsfoundation.lams.learningdesign.dao.ITransitionDAO;
import org.lamsfoundation.lams.learningdesign.dto.AuthoringActivityDTO;
import org.lamsfoundation.lams.learningdesign.dto.BranchActivityEntryDTO;
import org.lamsfoundation.lams.learningdesign.dto.BranchConditionDTO;
import org.lamsfoundation.lams.learningdesign.dto.CompetenceDTO;
import org.lamsfoundation.lams.learningdesign.dto.DataFlowObjectDTO;
import org.lamsfoundation.lams.learningdesign.dto.GroupDTO;
import org.lamsfoundation.lams.learningdesign.dto.GroupingDTO;
import org.lamsfoundation.lams.learningdesign.dto.LearningDesignDTO;
import org.lamsfoundation.lams.learningdesign.dto.ToolOutputBranchActivityEntryDTO;
import org.lamsfoundation.lams.learningdesign.dto.ToolOutputGateActivityEntryDTO;
import org.lamsfoundation.lams.learningdesign.dto.TransitionDTO;
import org.lamsfoundation.lams.lesson.LessonClass;
import org.lamsfoundation.lams.planner.PedagogicalPlannerActivityMetadata;
import org.lamsfoundation.lams.qb.dao.IQbDAO;
import org.lamsfoundation.lams.qb.model.QbQuestion;
import org.lamsfoundation.lams.tool.SystemTool;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.ToolAdapterContentManager;
import org.lamsfoundation.lams.tool.ToolContent;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.dao.ISystemToolDAO;
import org.lamsfoundation.lams.tool.dao.IToolContentDAO;
import org.lamsfoundation.lams.tool.dao.IToolDAO;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.util.CommonConstants;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.FileUtilException;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.VersionUtil;
import org.lamsfoundation.lams.util.zipfile.ZipFileUtil;
import org.lamsfoundation.lams.util.zipfile.ZipFileUtilException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.security.AnyTypePermission;

/**
 * Export tool content service bean.
 *
 * @author Steve.Ni
 *
 * @version $Revision$
 */
public class ExportToolContentService implements IExportToolContentService, ApplicationContextAware {
    private Logger log = Logger.getLogger(ExportToolContentService.class);

    // export tool content zip file prefix
    public static final String EXPORT_TOOLCONTNET_ZIP_PREFIX = "lams_toolcontent_";

    public static final String EXPORT_CONTENT_ZIP_PREFIX = "lams_ldcontent_";

    public static final String EXPORT_TOOLCONTNET_FOLDER_SUFFIX = "export_toolcontent";

    public static final String EXPORT_TOOLCONTNET_ZIP_SUFFIX = ".zip";

    public static final String EXPORT_CONTENT_ZIP_SUFFIX = ".zip";

    public static final String LEARNING_DESIGN_FILE_NAME = "learning_design.xml";

    public static final String TOOL_FILE_NAME = "tool.xml";

    public static final String TOOL_FAILED_FILE_NAME = "export_failed.xml";

    public static final String SVG_IMAGE_FILE_NAME = "learning_design.svg";

    private static final String ERROR_TOOL_NOT_FOUND = "error.import.matching.tool.not.found";

    private static final String ERROR_SERVICE_ERROR = "error.import.tool.service.fail";

    private static final String ERROR_NO_VALID_TOOL = "error.no.valid.tool";

    private static final String ERROR_INCOMPATIBLE_VERSION = "error.possibly.incompatible.version";

    private static final String FILTER_METHOD_PREFIX_DOWN = "down";

    private static final String FILTER_METHOD_PREFIX_UP = "up";

    private static final String FILTER_METHOD_MIDDLE = "To";

    // LAMS export format tag names
    private static final String LAMS_VERSION = "version";

    private static final String LAMS_TITLE = "title";

    private static final String DIR_CONTENT = "content";

    // message keys
    private static final String KEY_MSG_IMPORT_FILE_FORMAT = "msg.import.file.format";

    private MessageService messageService;
    private ILearningDesignService learningDesignService;

    private ApplicationContext applicationContext;

    // save list of all tool file node class information. One tool may have
    // over one file node, such as
    // in share resource tool, it has contnent attachment and shared
    // resource item attachement.
    private List<NameInfo> fileHandleClassList;

    private Class filterClass;

    // spring injection properties
    private IActivityDAO activityDAO;

    private IToolDAO toolDAO;

    private IToolContentDAO toolContentDAO;

    private ISystemToolDAO systemToolDAO;

    private IBaseDAO baseDAO;

    private ILicenseDAO licenseDAO;

    private IGroupingDAO groupingDAO;

    private ITransitionDAO transitionDAO;

    private ILearningDesignDAO learningDesignDAO;

    private ILearningLibraryDAO learningLibraryDAO;

    private IQbDAO qbDAO;

    /**
     * Class of tool attachment file handler class and relative fields information container.
     */
    private class NameInfo {

	// the Class instance according to className.
	public String className;

	public String uuidFieldName;

	public String versionFieldName;

	public String fileNameFieldName;

	public String mimeTypeFieldName;

	public String initalItemFieldName;

	public NameInfo(String className, String uuidFieldName, String versionFieldName) {
	    this.className = className;
	    this.uuidFieldName = uuidFieldName;
	    this.versionFieldName = versionFieldName;
	}

	public NameInfo(String className, String uuidFieldName, String versionFieldName, String fileNameFieldName,
		String mimeTypeFieldName, String initalItemFieldName) {
	    this.className = className;
	    this.uuidFieldName = uuidFieldName;
	    this.versionFieldName = versionFieldName;
	    this.fileNameFieldName = fileNameFieldName;
	    this.mimeTypeFieldName = mimeTypeFieldName;
	    this.initalItemFieldName = initalItemFieldName;
	}
    }

    /**
     * File node value information container.
     */
    private class ValueInfo {
	// for unmarshal
	public NameInfo name;

	public Object instance;

	// for marshal
	public Long fileUuid;

	public Long fileVersionId;

	public ValueInfo(NameInfo name, Object instance) {
	    this.name = name;
	    this.instance = instance;
	}

	public ValueInfo(Long uuid, Long versionId) {
	    fileUuid = uuid;
	    fileVersionId = versionId;
	}
    }

    private class FileConverter implements Converter {
	private Converter defaultConverter;
	private List<ValueInfo> fileNodes = new ArrayList<>();

	public FileConverter(XStream xstream) {
	    this.defaultConverter = new ReflectionConverter(xstream.getMapper(), xstream.getReflectionProvider());
	}

	public List<ValueInfo> getFileNodes() {
	    return fileNodes;
	}

	@Override
	public boolean canConvert(Class type) {
	    for (NameInfo info : fileHandleClassList) {
		if (info.className.equals(type.getName())) {
		    log.debug("XStream will handle [" + info.className + "] as file node class.");
		    return true;
		}
	    }
	    return false;
	}

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
	    if (source != null) {
		String className = source.getClass().getName();
		try {
		    for (NameInfo name : fileHandleClassList) {
			if (name.className.equals(className)) {
			    Long uuid = NumberUtils.createLong(BeanUtils.getProperty(source, name.uuidFieldName));
			    if (uuid != null) {
				// version id is optional
				Long version = null;
				if (name.versionFieldName != null) {
				    version = NumberUtils
					    .createLong(BeanUtils.getProperty(source, name.versionFieldName));
				}
				log.debug("XStream get file node [" + uuid + "," + version + "].");
				fileNodes.add(ExportToolContentService.this.new ValueInfo(uuid, version));
			    }
			}
		    }
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
		    log.error("Error while marshalling a file", e);
		}
	    }

	    defaultConverter.marshal(source, writer, context);
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
	    Object result = defaultConverter.unmarshal(reader, context);
	    if (result != null) {
		String className = result.getClass().getName();
		// During deserialize XML file into object, it will save file node into fileNodes
		for (NameInfo name : fileHandleClassList) {
		    if (name.className.equals(className)) {
			fileNodes.add(ExportToolContentService.this.new ValueInfo(name, result));
			break;
		    }
		}
	    }

	    return result;
	}
    }

    /**
     * This class is just for later system extent tool compaiblity strategy use. Currently, it just simple to get tool
     * by same signature.
     *
     * @author Steve.Ni
     *
     * @version $Revision$
     */
    public class ToolCompatibleStrategy {
	public Tool getTool(String toolSignature) {
	    return toolDAO.getToolBySignature(toolSignature);
	}
    }

    /**
     * Default contructor method.
     */
    public ExportToolContentService() {
	fileHandleClassList = new ArrayList<>();
    }

    /**
     * @see org.lamsfoundation.lams.authoring.service.IExportToolContentService.exportLearningDesign(Long)
     */
    @Override
    public String exportLearningDesign(Long learningDesignId, List<String> toolsErrorMsgs)
	    throws ExportToolContentException {
	try {
	    // root temp directory, put target zip file
	    String targetZipFileName = null;
	    String rootDir = FileUtil.createTempDirectory(ExportToolContentService.EXPORT_TOOLCONTNET_FOLDER_SUFFIX);
	    String contentDir = FileUtil.getFullPath(rootDir, ExportToolContentService.DIR_CONTENT);
	    FileUtil.createDirectory(contentDir);

	    String ldFileName = FileUtil.getFullPath(contentDir, ExportToolContentService.LEARNING_DESIGN_FILE_NAME);
	    Writer ldFile = new OutputStreamWriter(new FileOutputStream(ldFileName), "UTF-8");

	    // get learning desing and serialize it to XML file. Update the
	    // version to reflect the version now, rather than the version when it was saved.
	    LearningDesignDTO ldDto = learningDesignService.getLearningDesignDTO(learningDesignId, "");
	    ldDto.setVersion(Configuration.get(ConfigurationKeys.SERVER_VERSION_NUMBER));

	    /*
	     * learning design DTO is ready to be written into XML, but we need to find out which groupings and
	     * branching mappings are not supposed to be included into the structure (LDEV-1825)
	     */
	    Set<Long> groupingsToSkip = new TreeSet<>();

	    // iterator all activities in this learning design and export
	    // their content to given folder.
	    // The content will contain tool.xml and attachment files of
	    // tools from LAMS repository.
	    List<AuthoringActivityDTO> activities = ldDto.getActivities();

	    Set<String> exportedContentFolders = new HashSet<>();

	    // iterator all activities and export tool.xml and its
	    // attachments
	    for (AuthoringActivityDTO activity : activities) {

		int activityTypeID = activity.getActivityTypeID().intValue();
		// for teacher chosen and tool based branching activities there
		// should be no groupings saved to XML
		if ((activityTypeID == Activity.CHOSEN_BRANCHING_ACTIVITY_TYPE)
			|| (activityTypeID == Activity.TOOL_BRANCHING_ACTIVITY_TYPE)) {
		    Long groupingID = activity.getGroupingID();
		    if (groupingID != null) {
			groupingsToSkip.add(groupingID);
		    }
		    activity.setApplyGrouping(false);
		    activity.setGroupingID(null);
		    activity.setGroupingUIID(null);
		    continue;
		}
		// skip non-tool activities
		if (activityTypeID != Activity.TOOL_ACTIVITY_TYPE) {
		    continue;
		}

		// find out current acitivites toolContentMananger and export
		// tool content.
		ToolContentManager contentManager = (ToolContentManager) findToolService(
			toolDAO.getToolByID(activity.getToolID()));
		log.debug("Tool export content : " + activity.getActivityTitle() + " by contentID :"
			+ activity.getToolContentID());
		try {
		    contentManager.exportToolContent(activity.getToolContentID(), contentDir);
		} catch (Exception e) {
		    String msg = activity.getToolDisplayName() + " export tool content failed:" + e.toString();
		    log.error(msg, e);
		    // Try to delete tool.xml. This makes export_failed and
		    // tool.xml does not exist simultaneously.
		    String toolPath = FileUtil.getFullPath(contentDir, activity.getToolContentID().toString());
		    String toolFileName = FileUtil.getFullPath(toolPath, ExportToolContentService.TOOL_FILE_NAME);
		    File toolFile = new File(toolFileName);
		    if (toolFile.exists()) {
			toolFile.delete();
		    }
		    writeErrorToToolFile(contentDir, activity.getToolContentID(), msg);
		    toolsErrorMsgs.add(msg);
		}

		// export content folders for all QB questions which are used by LD's activities
		List<QbQuestion> activityQbQuestions = qbDAO.getQuestionsByToolContentId(activity.getToolContentID());
		for (QbQuestion qbQuestion : activityQbQuestions) {
		    String contentFolderID = qbQuestion.getContentFolderId();
		    // Do not export the same folder twice
		    // Some QB question share the same content folder ID
		    if (!exportedContentFolders.contains(contentFolderID)) {
			exportContentFolder(contentFolderID, contentDir);
			exportedContentFolders.add(contentFolderID);
		    }
		}
	    } // end all activities export

	    // skipping unwanted elements; learning design DTO is altered
	    // but not persisted;
	    Iterator<GroupingDTO> groupingIter = ldDto.getGroupings().iterator();
	    while (groupingIter.hasNext()) {
		if (groupingsToSkip.contains(groupingIter.next().getGroupingID())) {
		    groupingIter.remove();
		}
	    }

	    // exporting XML
	    XStream designXml = new XStream(new StaxDriver());
	    designXml.addPermission(AnyTypePermission.ANY);
	    designXml.toXML(ldDto, ldFile);
	    ldFile.close();

	    //generate SVG image
	    String destinationPath = FileUtil.getFullPath(contentDir, ExportToolContentService.SVG_IMAGE_FILE_NAME);
	    String svgPath = LearningDesignService.getLearningDesignSVGPath(learningDesignId);
	    File svgFile = new File(svgPath);
	    if (svgFile.canRead()) {
		FileUtils.copyFile(svgFile, new File(destinationPath));
	    }

	    log.debug("Learning design xml export success");

	    // export the LDs content folder, if it was not already exported for one of the questions
	    String contentFolderID = ldDto.getContentFolderID();
	    if (!exportedContentFolders.contains(contentFolderID)) {
		exportContentFolder(contentFolderID, contentDir);
	    }

	    // zip file name with full path
	    targetZipFileName = ldDto.getTitle() + ExportToolContentService.EXPORT_TOOLCONTNET_ZIP_SUFFIX;

	    log.debug("Create export content target zip file. File name is " + targetZipFileName);
	    // create zip file and return zip full file name
	    return ZipFileUtil.createZipFile(targetZipFileName, contentDir, rootDir);
	} catch (FileUtilException e) {
	    log.error("FileUtilException:", e);
	    throw new ExportToolContentException(e);
	} catch (ZipFileUtilException e) {
	    log.error("ZipFileUtilException:", e);
	    throw new ExportToolContentException(e);
	} catch (IOException e) {
	    log.error("IOException:", e);
	    throw new ExportToolContentException(e);
	}
    }

    private void exportContentFolder(String contentFolderID, String targetDir) throws ExportToolContentException {
	try {
	    // create zip file for unique content folder
	    String zipFileName = ExportToolContentService.EXPORT_CONTENT_ZIP_PREFIX + contentFolderID
		    + ExportToolContentService.EXPORT_CONTENT_ZIP_SUFFIX;
	    String secureDir = Configuration.get(ConfigurationKeys.LAMS_EAR_DIR) + File.separator
		    + FileUtil.LAMS_WWW_DIR + File.separator + FileUtil.LAMS_WWW_SECURE_DIR;

	    String contentFolder = ExportToolContentService.getContentDirPath(contentFolderID, true);
	    contentFolder = FileUtil.getFullPath(secureDir, contentFolder);

	    if (!FileUtil.isEmptyDirectory(contentFolder, true)) {
		log.debug("Create export content folder target zip file. File name is " + zipFileName);
		ZipFileUtil.createZipFile(zipFileName, contentFolder, targetDir);
	    } else {
		log.debug("No such directory (or empty directory):" + contentFolder);
	    }

	} catch (Exception e) {
	    log.error("Error thrown while a ZIP for content folder " + contentFolderID, e);
	    throw new ExportToolContentException(e);
	}
    }

    /**
     * @throws ExportToolContentException
     *
     */
    @Override
    public void exportToolContent(Long toolContentId, Object toolContentObj, IToolContentHandler toolContentHandler,
	    String rootPath) throws ExportToolContentException {
	try {
	    // create tool's save path
	    String toolPath = FileUtil.getFullPath(rootPath, toolContentId.toString());
	    FileUtil.createDirectory(toolPath);

	    // create tool xml file name : tool.xml
	    String toolFileName = FileUtil.getFullPath(toolPath, ExportToolContentService.TOOL_FILE_NAME);
	    Writer toolFile = new OutputStreamWriter(new FileOutputStream(toolFileName), "UTF-8");

	    // serialize tool xml into local file.
	    XStream toolXml = new XStream(new StaxDriver());
	    toolXml.addPermission(AnyTypePermission.ANY);
	    FileConverter fileConverter = null;
	    if (!fileHandleClassList.isEmpty()) {
		fileConverter = new FileConverter(toolXml);
		toolXml.registerConverter(fileConverter);
	    }
	    // XML to Object
	    toolXml.toXML(toolContentObj, toolFile);
	    toolFile.flush();
	    toolFile.close();

	    if (fileConverter != null) {
		for (ValueInfo fileNode : fileConverter.getFileNodes()) {
		    log.debug("Tool attachement file is going to save : " + fileNode.fileUuid);
		    toolContentHandler.saveFile(fileNode.fileUuid,
			    FileUtil.getFullPath(toolPath, fileNode.fileUuid.toString()));
		}
	    }
	} catch (ItemNotFoundException e) {
	    throw new ExportToolContentException(e);
	} catch (RepositoryCheckedException e) {
	    throw new ExportToolContentException(e);
	} catch (IOException e) {
	    throw new ExportToolContentException(e);
	} catch (FileUtilException e) {
	    throw new ExportToolContentException(e);
	} finally {
	    if (fileHandleClassList != null) {
		fileHandleClassList.clear();
	    }
	}

    }

    /**
     * @see org.lamsfoundation.lams.authoring.service.IExportToolContentService.registerFileHandleClass(String,String,
     *      String )
     */
    @Override
    public void registerFileClassForExport(String fileNodeClassName, String fileUuidFieldName,
	    String fileVersionFieldName) {
	fileHandleClassList.add(this.new NameInfo(fileNodeClassName, fileUuidFieldName, fileVersionFieldName));
    }

    @Override
    public void registerFileClassForImport(String fileNodeClassName, String fileUuidFieldName,
	    String fileVersionFieldName, String fileNameFieldName, String mimeTypeFieldName,
	    String initialItemFieldName) {
	fileHandleClassList.add(this.new NameInfo(fileNodeClassName, fileUuidFieldName, fileVersionFieldName,
		fileNameFieldName, mimeTypeFieldName, initialItemFieldName));
    }

    @Override
    public void registerImportVersionFilterClass(Class filterClass) {
	this.filterClass = filterClass;
    }

    /**
     * Import the learning design from the given path. Set the importer as the creator. If the workspaceFolderUid is
     * null then saves the design in the user's own workspace folder.
     *
     * @param designFile
     * @param importer
     * @param workspaceFolderUid
     * @return An object array where:
     *         <ul>
     *         <li>Object[0] = ldID (Long)</li>
     *         <li>Object[1] = ldErrors when importing (List<String>)</li>
     *         <li>Object[2] = toolErrors when importing (List<String>)</li>
     *         </ul>
     *
     * @throws ImportToolContentException
     */
    @Override
    public Object[] importLearningDesign(File designFile, User importer, Integer workspaceFolderUid,
	    List<String> toolsErrorMsgs, String customCSV) throws ImportToolContentException {

	Object[] ldResults = new Object[3];
	Long ldId = null;
	List<String> ldErrorMsgs = new ArrayList<>();
	String filename = designFile.getName();
	String extension = (filename != null) && (filename.length() >= 4) ? filename.substring(filename.length() - 4)
		: "";

	try {
	    if (extension.equalsIgnoreCase(".zip")) {
		if (!FileUtil.isVirusFree(new FileInputStream(designFile))) {
		    throw new ImportToolContentException("Virus found in imported design file");
		}

		String ldPath = ZipFileUtil.expandZip(new FileInputStream(designFile), filename);

		File fullFilePath = new File(
			FileUtil.getFullPath(ldPath, ExportToolContentService.LEARNING_DESIGN_FILE_NAME));
		if (fullFilePath.canRead()) {
		    ldId = importLearningDesign(ldPath, importer, workspaceFolderUid, toolsErrorMsgs, customCSV);
		} else {
		    badFileType(ldErrorMsgs, filename, "Learning design file not found.");
		}
	    } else {
		badFileType(ldErrorMsgs, filename, "Unexpected extension");
	    }

	} catch (Exception e) {
	    throw new ImportToolContentException("Error while importing a sequence", e);
	}

	ldResults[0] = ldId;
	ldResults[1] = ldErrorMsgs;
	ldResults[2] = toolsErrorMsgs;
	return ldResults;
    }

    protected String getPacket(InputStream sis) throws IOException {
	BufferedReader buff = new BufferedReader(new InputStreamReader(sis));

	StringBuffer tempStrBuf = new StringBuffer(200);
	String tempStr;
	tempStr = buff.readLine();
	while (tempStr != null) {
	    tempStrBuf.append(tempStr);
	    tempStr = buff.readLine();
	}

	return tempStrBuf.toString();
    }

    private Long importLearningDesign(String learningDesignPath, User importer, Integer workspaceFolderUid,
	    List<String> toolsErrorMsgs, String customCSV) throws ImportToolContentException {

	try {
	    // import learning design
	    String fullFilePath = FileUtil.getFullPath(learningDesignPath,
		    ExportToolContentService.LEARNING_DESIGN_FILE_NAME);
	    String importedFileVersion = checkImportVersion(fullFilePath, toolsErrorMsgs);
	    filterCoreVersion(fullFilePath, importedFileVersion);

	    LearningDesignDTO ldDto = (LearningDesignDTO) FileUtil.getObjectFromXML(null, fullFilePath);
	    log.debug("Learning design xml deserialize to LearingDesignDTO success.");

	    // begin tool import
	    Map<Long, ToolContent> toolMapper = new HashMap<>();
	    Map<Long, AuthoringActivityDTO> removedActMap = new HashMap<>();
	    List<AuthoringActivityDTO> activities = ldDto.getActivities();
	    // LDs with version 3.0.2 have already correct paths
	    boolean rewriteResourcePaths = !VersionUtil.isSameOrLaterVersion("3.0.2", importedFileVersion);
	    // for rewriting in tool content paths like secure/4028813915760eb301157a04abe7005a/
	    // into new format of paths like secure/40/28/81/39/15/76/
	    String oldResourcePath = FileUtil.LAMS_WWW_SECURE_DIR + '/' + ldDto.getContentFolderID() + '/';
	    String newResourcePath = FileUtil.LAMS_WWW_SECURE_DIR + '/'
		    + ExportToolContentService.getContentDirPath(ldDto.getContentFolderID(), false);
	    if (rewriteResourcePaths && ldDto.getDescription() != null) {
		ldDto.setDescription(ldDto.getDescription().replaceAll(oldResourcePath, newResourcePath));
	    }
	    Set<String> importedContentFolder = new HashSet<>();
	    for (AuthoringActivityDTO activity : activities) {
		learningDesignService.fillLearningLibraryID(activity, activities);
		
		// skip non-tool activities
		if (!activity.getActivityTypeID().equals(Activity.TOOL_ACTIVITY_TYPE)) {
		    continue;
		}

		String toolPath = FileUtil.getFullPath(learningDesignPath, activity.getToolContentID().toString());
		String fromVersion = activity.getToolVersion();

		//transform MCQ tool to Assessment
		if ("lamc11".equals(activity.getToolSignature())) {
		    activity.setToolSignature(CommonConstants.TOOL_SIGNATURE_ASSESSMENT);
		    
		    // run all appropriate methods from McqImportContentVersionFilter, transforming MCQ object to its latest version and then to Assessment object
		    filterClass = McqImportContentVersionFilter.class;
		    final String LAST_MCQ_VERSION = "20200120";
		    String toolFilePath = FileUtil.getFullPath(toolPath, ExportToolContentService.TOOL_FILE_NAME);
		    filterVersion(toolFilePath, fromVersion, LAST_MCQ_VERSION);
		    //set fromVersion as the date when MCQ tool was moved to Assessment, so Assessment can run all subsequent version filters
		    fromVersion = LAST_MCQ_VERSION;
		    // clear and ensure next activity can get correct filter thru registerImportVersionFilterClass()
		    filterClass = null;
		}
		
		// get tool by signature
		Tool tool = new ToolCompatibleStrategy().getTool(activity.getToolSignature());
		// can not find a matching tool
		if (tool == null) {
		    log.warn("An activity can not found matching tool [" + activity.getToolSignature() + "].");
		    toolsErrorMsgs.add(messageService.getMessage(ExportToolContentService.ERROR_TOOL_NOT_FOUND,
			    new Object[] { activity.getToolSignature() }));

		    // remove this activity from LD
		    removedActMap.put(activity.getActivityID(), activity);
		    continue;
		}

		// imported Learning Library ID and one stored in current DB may not match, so fix it here
		activity.setLearningLibraryID(tool.getLearningLibraryId());

		// create a new toolContent according to imported tool signature name
		ToolContent newContent = new ToolContent(tool);
		toolContentDAO.saveToolContent(newContent);

		// store mapping of original activity id to the new toolContent 
		toolMapper.put(activity.getActivityID(), newContent);

		// Invoke tool's importToolContent() method
		try {
		    // begin to import
		    log.debug("Tool begin to import content : " + activity.getActivityTitle() + " by contentID :"
			    + activity.getToolContentID());

		    if (rewriteResourcePaths) {
			ExportToolContentService.rewriteToolResourcePaths(toolPath, oldResourcePath, newResourcePath);
		    }

		    String toVersion = tool.getToolVersion();
		    ToolContentManager contentManager = (ToolContentManager) findToolService(tool);

		    // If this is a tool adapter tool, pass the customCSV to
		    // the special importToolContent method
		    // Otherwise invoke the normal tool
		    // importToolContentMethod
		    if (contentManager instanceof ToolAdapterContentManager) {
			ToolAdapterContentManager toolAdapterContentManager = (ToolAdapterContentManager) contentManager;
			toolAdapterContentManager.importToolContent(newContent.getToolContentId(), importer.getUserId(),
				toolPath, fromVersion, toVersion, customCSV);
		    } else {
			contentManager.importToolContent(newContent.getToolContentId(), importer.getUserId(), toolPath,
				fromVersion, toVersion);
		    }

		    // import content folder for each QB question that LD's activities use
		    List<QbQuestion> activityQbQuestions = qbDAO
			    .getQuestionsByToolContentId(newContent.getToolContentId());
		    for (QbQuestion qbQuestion : activityQbQuestions) {
			String contentFolderID = qbQuestion.getContentFolderId();
			// Do not import twice
			// Some activities share the same content folder ID
			if (!importedContentFolder.contains(contentFolderID)) {
			    importContentFolder(contentFolderID, learningDesignPath);
			    importedContentFolder.add(contentFolderID);
			}
		    }

		    log.debug("Tool content import success.");
		} catch (Exception e) {
		    String error = messageService.getMessage(ExportToolContentService.ERROR_SERVICE_ERROR,
			    new Object[] { tool.getToolDisplayName(), e.toString() });
		    log.error(error, e);
		    toolsErrorMsgs.add(error);
		    // remove any unsucessed activities from new Learning
		    // design.
		    removedActMap.put(activity.getActivityID(), activity);
		}
	    } // end all activities import

	    // all activities can not imported, ignore this LD
	    if (removedActMap.size() == activities.size()) {
		toolsErrorMsgs.add(messageService.getMessage(ExportToolContentService.ERROR_NO_VALID_TOOL));
		return -1L;
	    }

	    // import the LD's content folder, if it was not already imported for one of the QB questions
	    if (!importedContentFolder.contains(ldDto.getContentFolderID())) {
		importContentFolder(ldDto.getContentFolderID(), learningDesignPath);
	    }

	    // if the design was read only (e.g. exported a runtime
	    // sequence), clear the read only flag
	    ldDto.setDateReadOnly(null);
	    ldDto.setReadOnly(false);

	    // save learning design
	    WorkspaceFolder folder = getWorkspaceFolderForDesign(importer, workspaceFolderUid);
	    return saveLearningDesign(ldDto, importer, folder, toolMapper, removedActMap);

	} catch (Exception e) {
	    log.error("Exception occured during import.", e);
	    throw new ImportToolContentException(e);
	}
    }

    private void importContentFolder(String contentFolderID, String sourceDir) throws ImportToolContentException {
	try {
	    String contentZipFileName = ExportToolContentService.EXPORT_CONTENT_ZIP_PREFIX + contentFolderID
		    + ExportToolContentService.EXPORT_CONTENT_ZIP_SUFFIX;
	    String secureDir = Configuration.get(ConfigurationKeys.LAMS_EAR_DIR) + File.separator
		    + FileUtil.LAMS_WWW_DIR + File.separator + FileUtil.LAMS_WWW_SECURE_DIR + File.separator
		    + ExportToolContentService.getContentDirPath(contentFolderID, true);
	    File contentZipFile = new File(FileUtil.getFullPath(sourceDir, contentZipFileName));

	    // unzip file to target secure dir if exists
	    if (contentZipFile.exists()) {
		InputStream is = new FileInputStream(contentZipFile);
		ZipFileUtil.expandZipToFolder(is, secureDir);
	    }

	} catch (Exception e) {
	    throw new ImportToolContentException(e);
	}
    }

    /**
     * @param fullFilePath
     * @param toolsErrorMsgs
     * @return version of the server that exported this file
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    private String checkImportVersion(String fullFilePath, List<String> toolsErrorMsgs)
	    throws IOException, ParserConfigurationException, SAXException {
	DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	Document doc = docBuilder.parse(new FileInputStream(fullFilePath));
	Element root = doc.getDocumentElement();
	String title = root.getElementsByTagName(ExportToolContentService.LAMS_TITLE).item(0).getTextContent().trim();
	String versionString = root.getElementsByTagName(ExportToolContentService.LAMS_VERSION).item(0).getTextContent()
		.trim();

	String currentVersionString = Configuration.get(ConfigurationKeys.SERVER_VERSION_NUMBER);
	try {
	    boolean isLaterVersion = !VersionUtil.isSameOrLaterVersionAsServer(versionString);
	    if (isLaterVersion) {
		log.warn(
			"Importing a design from a later version of LAMS. There may be parts of the design that will fail to import. Design name \'"
				+ title + "\'. Version in import file " + versionString);
		toolsErrorMsgs.add(messageService.getMessage(ExportToolContentService.ERROR_INCOMPATIBLE_VERSION,
			new Object[] { versionString, currentVersionString }));
	    }
	} catch (Exception e) {
	    log.warn("Unable to properly determine current version from an import file. Design name \'" + title
		    + "\'. Version in import file " + versionString);
	    toolsErrorMsgs.add(messageService.getMessage(ExportToolContentService.ERROR_INCOMPATIBLE_VERSION,
		    new Object[] { versionString, currentVersionString }));
	}

	return versionString;
    }

    /**
     * Transform main XML file to correct version format.
     *
     * @param fullFilePath
     * @param versionString
     * @return
     * @throws Exception
     */
    private void filterCoreVersion(String fullFilePath, String versionString) throws Exception {

	ToolContentVersionFilter contentFilter = new ToolContentVersionFilter();

	// all exported files from server version prior to 2.4.2 will require deletion of defineLater and runOffline
	// flags
	boolean isEarlierVersionThan242 = !VersionUtil.isSameOrLaterVersion("2.4.2", versionString);
	if (isEarlierVersionThan242) {
	    Class problemClass = Class.forName(AuthoringActivityDTO.class.getName());
	    contentFilter.removeField(problemClass, "defineLater");
	    contentFilter.removeField(problemClass, "runOffline");
	    contentFilter.transformXML(fullFilePath);
	}

	boolean isEarlierVersionThan301 = !VersionUtil.isSameOrLaterVersion("3.0.1", versionString);
	if (isEarlierVersionThan301) {
	    Class problemClass = Class.forName(AuthoringActivityDTO.class.getName());
	    contentFilter.renameField(problemClass, "activityEvaluations", "evaluation");
	    contentFilter.transformXML(fullFilePath);
	}

	boolean isEarlierVersionThan32 = !VersionUtil.isSameOrLaterVersion("3.2", versionString);
	if (isEarlierVersionThan32) {
	    contentFilter.renameClass("org.lamsfoundation.lams.tool.qa.QaContent",
		    "org.lamsfoundation.lams.tool.qa.model.QaContent");
	    contentFilter.renameClass("org.lamsfoundation.lams.tool.qa.QaCondition",
		    "org.lamsfoundation.lams.tool.qa.model.QaCondition");
	    contentFilter.renameClass("org.lamsfoundation.lams.tool.qa.QaQueContent",
		    "org.lamsfoundation.lams.tool.qa.model.QaQueContent");
	    contentFilter.renameClass("org.lamsfoundation.lams.tool.qa.QaQueUsr",
		    "org.lamsfoundation.lams.tool.qa.model.QaQueUsr");
	    contentFilter.renameClass("org.lamsfoundation.lams.tool.qa.QaSession",
		    "org.lamsfoundation.lams.tool.qa.model.QaSession");
	    contentFilter.renameClass("org.lamsfoundation.lams.tool.qa.QaUsrResp",
		    "org.lamsfoundation.lams.tool.qa.model.QaUsrResp");
	    contentFilter.transformXML(fullFilePath);
	}
    }

    private WorkspaceFolder getWorkspaceFolderForDesign(User importer, Integer workspaceFolderUid)
	    throws ImportToolContentException {
	// if workspaceFolderUid == null use the user's default folder
	WorkspaceFolder folder = null;
	if (workspaceFolderUid != null) {
	    folder = baseDAO.find(WorkspaceFolder.class, workspaceFolderUid);
	}
	if (folder == null && importer.getWorkspaceFolder() != null) {
	    folder = importer.getWorkspaceFolder();
	}
	if (folder == null) {
	    String error = "Unable to save design in a folder - folder not found. Input folder uid "
		    + workspaceFolderUid + ", user  " + importer.getUserId();
	    log.error(error);
	    throw new ImportToolContentException(error);
	}
	return folder;
    }

    private void badFileType(List<String> ldErrorMsgs, String filename, String errDescription) {
	log.error("Uploaded file not an expected type. Filename was " + filename + " " + errDescription);
	String msg = messageService.getMessage(ExportToolContentService.KEY_MSG_IMPORT_FILE_FORMAT);
	ldErrorMsgs.add(msg != null ? msg : "Uploaded file not an expected type.");
    }

    /**
     * Import tool content
     */
    @Override
    public Object importToolContent(String toolContentPath, IToolContentHandler toolContentHandler, String fromVersion,
	    String toVersion) throws ImportToolContentException {
	Object toolPOJO = null;
	// change xml to Tool POJO
	XStream toolXml = new XStream(new StaxDriver());
	toolXml.addPermission(AnyTypePermission.ANY);
	FileConverter fileConverter = null;
	if (!fileHandleClassList.isEmpty()) {
	    fileConverter = new FileConverter(toolXml);
	    toolXml.registerConverter(fileConverter);
	}

	try {
	    // tool.xml full path
	    String toolFilePath = FileUtil.getFullPath(toolContentPath, ExportToolContentService.TOOL_FILE_NAME);
	    if ((filterClass != null) && !StringUtils.equals(fromVersion, toVersion)) {
		filterVersion(toolFilePath, fromVersion, toVersion);
	    }
	    // clear and ensure next activity can get correct filter thru
	    // registerImportVersionFilterClass().
	    filterClass = null;

	    // read tool file after transform.
	    toolPOJO = FileUtil.getObjectFromXML(toolXml, toolFilePath);

	    if (fileConverter != null) {
		// upload file node if has
		for (ValueInfo fileNode : fileConverter.getFileNodes()) {
		    Long uuid = NumberUtils
			    .createLong(BeanUtils.getProperty(fileNode.instance, fileNode.name.uuidFieldName));
		    // For instance, item class in share resource tool may
		    // be url or single file. If it is URL, then the
		    // file uuid will be null. Ignore it!
		    if (uuid == null) {
			continue;
		    }

		    // according to upload rule: the file name will be uuid.
		    String realFileName = uuid.toString();
		    String fullFileName = FileUtil.getFullPath(toolContentPath, realFileName);
		    log.debug("Tool attachement files/packages are going to upload to repository " + fullFileName);

		    // to check if the file is package (with extension name
		    // ".zip") or single file (no extension name).
		    File file = new File(fullFileName);
		    boolean isPackage = false;
		    if (!file.exists()) {
			file = new File(fullFileName + ExportToolContentService.EXPORT_TOOLCONTNET_ZIP_SUFFIX);
			realFileName = realFileName + ExportToolContentService.EXPORT_TOOLCONTNET_ZIP_SUFFIX;
			isPackage = true;
			// if this file is norpackage neither single file, throw
			// exception.
			if (!file.exists()) {
			    throw new ImportToolContentException(
				    "Content attached file/package can not be found: " + fullFileName + "(.zip)");
			}
		    }

		    // more fields values for Repository upload use
		    String fileName = BeanUtils.getProperty(fileNode.instance, fileNode.name.fileNameFieldName);
		    // optional fields
		    String mimeType = null;
		    try {
			mimeType = BeanUtils.getProperty(fileNode.instance, fileNode.name.mimeTypeFieldName);
		    } catch (Exception e) {
			log.debug("No method for mimeType:" + fileNode.instance);
		    }
		    String initalItem = null;
		    try {
			initalItem = BeanUtils.getProperty(fileNode.instance, fileNode.name.initalItemFieldName);
		    } catch (Exception e) {
			log.debug("No method for initial item:" + fileNode.instance);
		    }

		    InputStream is = new FileInputStream(file);
		    // upload to repository: file or pacakge
		    NodeKey key;
		    if (!isPackage) {
			key = toolContentHandler.uploadFile(is, fileName, mimeType);
		    } else {
			String packageDirectory = ZipFileUtil.expandZip(is, realFileName);
			key = toolContentHandler.uploadPackage(packageDirectory, initalItem);
		    }

		    // refresh file node Uuid and Version value to latest.
		    BeanUtils.setProperty(fileNode.instance, fileNode.name.uuidFieldName, key.getUuid());
		    // version id is optional
		    if (fileNode.name.versionFieldName != null) {
			BeanUtils.setProperty(fileNode.instance, fileNode.name.versionFieldName, key.getVersion());
		    }
		}
	    }
	} catch (Exception e) {
	    throw new ImportToolContentException(e);
	} finally {
	    if (fileHandleClassList != null) {
		fileHandleClassList.clear();
	    }
	}

	return toolPOJO;
    }

    /**
     * Rewrites each line of tool.xml and converts old resource paths into new format.
     */
    private static void rewriteToolResourcePaths(String toolContentPath, String oldPath, String newPath)
	    throws IOException {
	String toolFilePath = FileUtil.getFullPath(toolContentPath, ExportToolContentService.TOOL_FILE_NAME);
	File toolFile = new File(toolFilePath);
	Path path = toolFile.toPath();
	List<String> oldLines = Files.readAllLines(path);
	List<String> newLines = new LinkedList<>();
	for (String oldLine : oldLines) {
	    String newLine = oldLine.replaceAll(oldPath, newPath);
	    newLines.add(newLine);
	}
	Files.write(path, newLines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    // ******************************************************************
    // ApplicationContextAware method implementation
    // ******************************************************************
    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
	applicationContext = context;
    }

    // ******************************************************************
    // Private methods
    // ******************************************************************

    /**
     * Transform tool XML file to correct version format.
     *
     * @param toVersion
     * @param fromVersion
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @SuppressWarnings("unchecked")
    private void filterVersion(String toolFilePath, String fromVersion, String toVersion) throws Exception {
	float from = 0;
	try {
	    from = NumberUtils.createFloat(fromVersion);
	} catch (Exception e) {
	}
	float to = 0;
	try {
	    to = NumberUtils.createFloat(toVersion);
	} catch (Exception e) {
	}

	String filterMethodPrefix;
	if (from > to) {
	    filterMethodPrefix = ExportToolContentService.FILTER_METHOD_PREFIX_DOWN;
	} else {
	    filterMethodPrefix = ExportToolContentService.FILTER_METHOD_PREFIX_UP;
	}

	log.debug("Version filter class will filter from version " + from + " to " + to);

	ToolContentVersionFilter filter = (ToolContentVersionFilter) filterClass.getConstructor(new Class[0])
		.newInstance(new Object[0]);
	Method[] methods = filterClass.getDeclaredMethods();
	Map<Float, Method> methodNeeds = new TreeMap<>();
	for (Method method : methods) {
	    String name = method.getName();
	    if (name.startsWith(filterMethodPrefix)) {
		String[] ver = name.split(filterMethodPrefix + "|" + ExportToolContentService.FILTER_METHOD_MIDDLE);
		Float mf = 0f;
		Float mt = 0f;
		for (int idx = 0; idx < ver.length; idx++) {
		    if (StringUtils.isBlank(ver[idx])) {
			continue;
		    }
		    mf = NumberUtils.createFloat(ver[idx]);
		    if (ver.length > idx) {
			mt = NumberUtils.createFloat(ver[++idx]);
		    }
		    break;
		}
		if ((mf >= from) && (mt <= to)) {
		    methodNeeds.put(mf, method);
		}
	    }
	}

	Method transform = filterClass.getMethod("transformXML", new Class[] { String.class });
	for (Entry<Float, Method> methodEntry : methodNeeds.entrySet()) {
	    Method method = methodEntry.getValue();
	    Class<?>[] parameterTypes = method.getParameterTypes();
	    if (parameterTypes.length == 0) {
		method.invoke(filter, new Object[0]);
		log.debug("Version filter class method " + method.getName() + " is executed.");
	    } else if (parameterTypes.length == 1 && parameterTypes[0].isAssignableFrom(String.class)) {
		// We encountered a method which transforms tool XML
		// First we need to flush existing stacked up changes (add/remove/rename field etc.)
		Float currentVersionStep = methodEntry.getKey();
		log.debug("Flushing changes up to version " + currentVersionStep);
		transform.invoke(filter, new Object[] { toolFilePath });
		// then call the method that transforms XML
		log.debug("Transforming XML For version " + currentVersionStep);
		method.invoke(filter, new Object[] { toolFilePath });
		// then clear flushed changes so they are not called again after iteration
		filter.clearChanges();
	    } else {
		throw new InvalidParameterException("Can not run version filter method " + method.getName()
			+ ". Unsupported number or type of parameters.");
	    }
	}
	// perform final XML transformation
	transform.invoke(filter, new Object[] { toolFilePath });
    }

    /**
     * If there are any errors happen during tool exporting content. Writing failed message to file.
     */
    private void writeErrorToToolFile(String rootPath, Long toolContentId, String msg) {
	// create tool's save path
	try {
	    String toolPath = FileUtil.getFullPath(rootPath, toolContentId.toString());
	    FileUtil.createDirectory(toolPath);

	    // create tool xml file name : tool.xml
	    String toolFileName = FileUtil.getFullPath(toolPath, ExportToolContentService.TOOL_FAILED_FILE_NAME);
	    Writer toolFile = new FileWriter(new File(toolFileName));
	    toolFile.write(msg);
	    toolFile.flush();
	    toolFile.close();
	} catch (FileUtilException e) {
	    log.warn("Export error file write error:", e);
	} catch (IOException e) {
	    log.warn("Export error file write error:", e);
	}
    }

    private Object findToolService(Tool tool) throws NoSuchBeanDefinitionException {
	return applicationContext.getBean(tool.getServiceName());
    }

    public Long saveLearningDesign(LearningDesignDTO dto, User importer, WorkspaceFolder folder,
	    Map<Long, ToolContent> toolMapper, Map<Long, AuthoringActivityDTO> removedActMap)
	    throws ImportToolContentException {

	// grouping object list
	List<GroupingDTO> groupingDtoList = dto.getGroupings();
	Map<Long, Grouping> groupingMapper = new HashMap<>();
	Map<Integer, Group> groupByUIIDMapper = new HashMap<>();
	for (GroupingDTO groupingDto : groupingDtoList) {
	    Grouping grouping = getGrouping(groupingDto, groupByUIIDMapper);
	    groupingMapper.put(grouping.getGroupingId(), grouping);

	    // persist
	    grouping.setGroupingId(null);
	    groupingDAO.insert(grouping);
	}

	// ================== Start handle activities ======================
	// activity object list
	// sort them to ensure parent before its children.
	List<AuthoringActivityDTO> actDtoList = getSortedParentList(dto.getActivities());

	if (log.isDebugEnabled()) {
	    int idx = 0;
	    for (AuthoringActivityDTO activityDTO : actDtoList) {
		log.debug(idx + ": ActivityID is [" + activityDTO.getActivityID() + "], parent ID is ["
			+ activityDTO.getParentActivityID() + "]");
		idx++;
	    }
	}
	Set<Activity> actList = new TreeSet<>(new ActivityOrderComparator());
	Map<Long, Activity> activityMapper = new HashMap<>();
	Map<Integer, Activity> activityByUIIDMapper = new HashMap<>();

	// as we create the activities, we need to record any "default
	// activities" for the sequence activity
	// and branching activities to process later - we can't process them now
	// as the children won't have
	// been created yet and if we leave it till later and then find all the
	// activities we are
	// going through the activity set over and over again for no reason.
	Map<Integer, ComplexActivity> defaultActivityToParentActivityMapping = new HashMap<>();

	for (AuthoringActivityDTO actDto : actDtoList) {
	    //skip removed activities
	    if (removedActMap.containsKey(actDto.getActivityID())) {
		continue;
	    }
	    
	    Activity act = getActivity(actDto, groupingMapper, toolMapper, defaultActivityToParentActivityMapping);
	    // so far, the activitiy ID is still old one, so setup the
	    // mapping relation between old ID and new activity.
	    activityMapper.put(act.getActivityId(), act);
	    activityByUIIDMapper.put(act.getActivityUIID(), act);
	    actList.add(act);
	}
	// rescan the activity list and refresh their parent activity and input
	// activities
	for (AuthoringActivityDTO actDto : actDtoList) {
	    Activity act = activityMapper.get(actDto.getActivityID());
	    if (removedActMap.containsKey(actDto.getActivityID())) {
		continue;
	    }

	    if (actDto.getParentActivityID() != null) {
		Activity parent = activityMapper.get(actDto.getParentActivityID());
		// reset children's parent as null if parent already removed
		if (removedActMap.containsKey(parent.getActivityId())) {
		    act.setParentActivity(null);
		    act.setParentUIID(null);
		} else {
		    act.setParentActivity(parent);
		    // also add child as Complex activity: It is useless for
		    // persist data, but helpful for validate in
		    // learning design!
		    if (parent.isComplexActivity()) {
			Set<Activity> set = ((ComplexActivity) parent).getActivities();
			if (set == null) {
			    set = new TreeSet<>(new ActivityOrderComparator());
			    ((ComplexActivity) parent).setActivities(set);
			}
			if (!removedActMap.containsKey(actDto.getActivityID())) {
			    set.add(act);
			}
		    }
		}
	    }

	    if (actDto.getInputActivities() != null) {
		act.setInputActivities(new HashSet<Activity>());
		for (Integer inputActivityUIID : actDto.getInputActivities()) {
		    Activity inputAct = activityByUIIDMapper.get(inputActivityUIID);
		    if (inputAct == null) {
			log.error("Unable to find input activity with UIID " + inputActivityUIID + " for activity "
				+ act);
		    } else {
			act.getInputActivities().add(inputAct);
		    }
		}
	    }

	    // persist
	    act.setActivityId(null);
	    activityDAO.insert(act);

	    List<String> eval = actDto.getEvaluation();
	    // Once the activity is saved, we can import the ActivityEvaluations
	    if (eval != null && eval.size() > 0) {
		ActivityEvaluation activityEvaluation = new ActivityEvaluation();
		activityEvaluation.setToolOutputDefinition(eval.get(0));
		if (eval.size() > 1) {
		    activityEvaluation.setWeight(Integer.valueOf(eval.get(1)));
		}
		activityEvaluation.setActivity((ToolActivity) act);
		((ToolActivity) act).setEvaluation(activityEvaluation);
		activityDAO.update(act);
	    }
	}

	// Process the "first child" for any sequence activities and the
	// "default branch" for branching activities.
	// If the child has been removed then leave it as null as the progress
	// engine will cope (it will pick a
	// new one based on the lack of an input transition) and in authoring
	// the author will just have to set
	// up a new first activity. If the default branch is missing and other
	// details are missing (e.g. missing
	// conditions)
	// from the design then it may have to be fixed in authoring before it
	// will run, so the default branch missing
	// case needs to be picked up by the validation (done later).
	if (defaultActivityToParentActivityMapping.size() > 0) {
	    for (Integer childUIID : defaultActivityToParentActivityMapping.keySet()) {
		ComplexActivity complex = defaultActivityToParentActivityMapping.get(childUIID);
		Activity childActivity = activityByUIIDMapper.get(childUIID);
		if (childActivity == null) {
		    log.error("Unable to find the default child activity (" + childUIID + ") for the activity ("
			    + complex.getTitle() + "). The activity " + complex.getTitle()
			    + " will need to be fixed in authoring "
			    + "otherwise the progress engine will just do the best it can.");
		} else {
		    complex.setDefaultActivity(childActivity);
		}
	    }
	}

	// reset first activity UUID for LD if old first removed
	// set first as remove activity's next existed one
	Integer actUiid = dto.getFirstActivityUIID();
	if (actUiid != null) {
	    for (AuthoringActivityDTO actDto : actDtoList) {
		if (actUiid.equals(actDto.getActivityUIID())) {
		    // if first activity is removed
		    if (removedActMap.containsKey(actDto.getActivityID())) {
			List<TransitionDTO> transDtoList = dto.getTransitions();
			Long existFirstAct = null;
			Long nextActId = actDto.getActivityID();
			boolean found = false;
			// try to find next available activity
			// 1000 is failure tolerance: to avoid dead loop.
			for (int idx = 0; idx < 1000; idx++) {
			    if ((transDtoList == null) || transDtoList.isEmpty()) {
				break;
			    }
			    boolean transitionBreak = true;
			    for (TransitionDTO transDto : transDtoList) {
				// we deal with progress transitions only
				if ((transDto.getTransitionType() == null)
					|| transDto.getTransitionType().equals(Transition.PROGRESS_TRANSITION_TYPE)) {
				    // find out the transition of current first
				    // activity
				    if (nextActId.equals(transDto.getFromActivityID())) {
					transitionBreak = false;
					nextActId = transDto.getToActivityID();
					if ((nextActId != null) && !removedActMap.containsKey(nextActId)) {
					    existFirstAct = nextActId;
					    found = true;
					    break;
					} else if (nextActId == null) {
					    // no more activity
					    found = true;
					    break;
					}
					// already found the desire transition
					break;
					// if found flag is false yet, then it
					// means the 2nd node remove as well,
					// continue try 3rd...
				    }
				}
				// This activity also removed!!! then retrieve
				// again
				// If found is false, then the nextAct is still
				// not available, then continue find.
				// tranisitionBreak mean the activity is removed
				// but it can not find its transition to
				// decide next available activity.
				if (found || transitionBreak) {
				    break;
				}
			    }
			}
			Activity next = activityMapper.get(existFirstAct);
			dto.setFirstActivityUIID(next == null ? null : next.getActivityUIID());
		    }
		    // find out the first activity, then break;
		    break;
		}
	    }
	}
	// ================== END handle activities ======================

	// transition object list
	List<TransitionDTO> transDtoList = dto.getTransitions();
	Set<Transition> transList = new HashSet<>();
	for (TransitionDTO transDto : transDtoList) {
	    // Any transitions relating with this tool will be removed!
	    Long fromId = transDto.getFromActivityID();
	    Long toId = transDto.getToActivityID();
	    if ((fromId != null) && removedActMap.containsKey(fromId)) {
		continue;
	    }
	    if ((toId != null) && removedActMap.containsKey(toId)) {
		continue;
	    }
	    Transition trans = getTransition(transDto, activityMapper);
	    transList.add(trans);

	    trans.setTransitionId(null);
	}

	// Once the learning design is saved, we can import the competences
	Set<Competence> competenceList = new HashSet<>();
	if (dto.getCompetences() != null) {
	    for (CompetenceDTO competenceDTO : dto.getCompetences()) {
		Competence competence = new Competence();
		competence.setDescription(competenceDTO.getDescription());
		competence.setTitle(competenceDTO.getTitle());
		competenceList.add(competence);
	    }
	}

	// branch mappings - maps groups to branches, map conditions to branches
	List<BranchActivityEntryDTO> entryDtoList = dto.getBranchMappings();
	if (entryDtoList != null) {
	    Set<BranchActivityEntry> entryList = new HashSet<>();
	    for (BranchActivityEntryDTO entryDto : entryDtoList) {
		BranchActivityEntry entry = getBranchActivityEntry(entryDto, groupByUIIDMapper, activityByUIIDMapper);
		entryList.add(entry);
	    }
	}

	LearningDesign ld = getLearningDesign(dto, importer, folder, actList, transList, activityMapper,
		competenceList);

	// validate learning design
	Vector listOfValidationErrorDTOs = learningDesignService.validateLearningDesign(ld);
	if (listOfValidationErrorDTOs.size() > 0) {
	    ld.setValidDesign(false);
	    log.error(listOfValidationErrorDTOs);
	    // throw new ImportToolContentException("Learning design
	    // validate error.");
	} else {
	    ld.setValidDesign(true);
	}
	// Generation of title triggers Hibernate flush. After changes done to transition<->activity associations, there
	// were errors if we tried to read LD data before saving the current one. So the workaround is to first save,
	// then read and update the title, then save again.
	learningDesignDAO.insert(ld);

	// add suffix if configuration is not set or is set to true
	String addSuffix = Configuration.get(ConfigurationKeys.SUFFIX_IMPORTED_LD);
	if ((addSuffix == null) || Boolean.valueOf(addSuffix)) {
	    String title = ld.getTitle();
	    if (title == null || title.trim().length() == 0) {
		title = "unknown";
	    }
	    ld.setTitle(
		    learningDesignService.getUniqueNameForLearningDesign(ld.getTitle(), folder.getWorkspaceFolderId()));
	    learningDesignDAO.update(ld);
	    // persist
	}

	// Once we have the competences saved, we can save the competence mappings
	Set<CompetenceMapping> allCompetenceMappings = new HashSet<>();
	for (AuthoringActivityDTO actDto : actDtoList) {
	    if (removedActMap.containsKey(actDto.getActivityID())) {
		continue;
	    }
	    if (actDto.getActivityTypeID().intValue() == Activity.TOOL_ACTIVITY_TYPE) {
		for (Activity act : actList) {
		    for (Competence competence : competenceList) {
			for (String comptenceMappingStr : actDto.getCompetenceMappingTitles()) {
			    if (competence.getTitle() == comptenceMappingStr) {
				if (activityMapper.get(actDto.getActivityID()).getActivityId() == act.getActivityId()) {
				    CompetenceMapping competenceMapping = new CompetenceMapping();
				    competenceMapping.setToolActivity((ToolActivity) act);
				    competenceMapping.setCompetence(competence);
				    allCompetenceMappings.add(competenceMapping);
				    break;
				}
			    }
			}
		    }
		}
	    }
	}
	baseDAO.insertOrUpdateAll(allCompetenceMappings);

	// Process annotations (regions and labels)
	if (dto.getAnnotations() != null) {
	    for (LearningDesignAnnotation annotation : dto.getAnnotations()) {
		annotation.setUid(null);
		annotation.setLearningDesignId(ld.getLearningDesignId());
		baseDAO.insert(annotation);
	    }
	}

	return ld.getLearningDesignId();
    }

    /**
     * Method to sort activity DTO according to the rule: Parents is before their children.
     *
     * @param activities
     * @return
     */
    private List<AuthoringActivityDTO> getSortedParentList(List<AuthoringActivityDTO> activities) {
	List<AuthoringActivityDTO> result = new ArrayList<>();
	List<Long> actIdList = new ArrayList<>();

	// NOTICE: this code can not handle all nodes have their parents, ie,
	// there is at least one node parent is
	// null(root).
	int failureToleranceCount = 5000;
	while (!activities.isEmpty() && (failureToleranceCount > 0)) {
	    Iterator<AuthoringActivityDTO> iter = activities.iterator();
	    while (iter.hasNext()) {
		AuthoringActivityDTO actDto = iter.next();
		if (actDto.getParentActivityID() == null) {
		    result.add(actDto);
		    actIdList.add(actDto.getActivityID());
		    iter.remove();
		} else if (actIdList.contains(actDto.getParentActivityID())) {
		    result.add(actDto);
		    actIdList.add(actDto.getActivityID());
		    iter.remove();
		}
	    }
	    failureToleranceCount--;
	}
	if (!activities.isEmpty()) {
	    log.warn("Some activities cannot found their parent actitivy.");
	    // just append these activies into result list
	    for (AuthoringActivityDTO actDto : activities) {
		log.warn("Activity ID[" + actDto.getActivityID() + "] cannot found parent ["
			+ actDto.getParentActivityID() + "]");
	    }
	    result.addAll(activities);
	}
	return result;
    }

    /**
     * Get learning design object from this Learning design DTO object. It also following our import rules:
     * <li>lams_license - Assume same in all lams system. Import same ID</li>
     * <li>lams_copy_type - Set to 1.This indicates it is "normal" design.</li>
     * <li>lams_workspace_folder - An input parameters to let user choose import workspace</li>
     * <li>User - The person who execute import action</li>
     * <li>OriginalLearningDesign - set to null</li>
     *
     * @param activityMapper
     *
     * @return
     * @throws ImportToolContentException
     */
    private LearningDesign getLearningDesign(LearningDesignDTO dto, User importer, WorkspaceFolder folder,
	    Set<Activity> actList, Set<Transition> transList, Map<Long, Activity> activityMapper,
	    Set<Competence> competenceList) throws ImportToolContentException {
	LearningDesign ld = new LearningDesign();

	if (dto == null) {
	    return ld;
	}

	ld.setLearningDesignId(dto.getLearningDesignID());
	ld.setLearningDesignUIID(dto.getLearningDesignUIID());
	ld.setDescription(dto.getDescription());
	ld.setTitle(dto.getTitle());

	Integer actUiid = dto.getFirstActivityUIID();
	if (actUiid != null) {
	    for (Activity act : activityMapper.values()) {
		if (actUiid.equals(act.getActivityUIID())) {
		    ld.setFirstActivity(act);
		    break;
		}
	    }
	}

	ld.setMaxID(dto.getMaxID());
	ld.setValidDesign(dto.getValidDesign());
	ld.setReadOnly(dto.getReadOnly());
	ld.setDateReadOnly(dto.getDateReadOnly());

	ld.setHelpText(dto.getHelpText());
	// set to 1
	ld.setCopyTypeID(1);
	ld.setCreateDateTime(dto.getCreateDateTime());
	ld.setVersion(dto.getVersion());

	if (folder != null) {
	    ld.setWorkspaceFolder(folder);
	}

	ld.setDuration(dto.getDuration());
	ld.setLicenseText(dto.getLicenseText());

	Long licenseId = dto.getLicenseID();
	if (licenseId != null) {
	    License license = licenseDAO.getLicenseByID(licenseId);
	    if (license == null) {
		throw new ImportToolContentException(
			"Import failed: License [" + dto.getLicenseText() + "] does not exist in target database");
	    }
	    ld.setLicense(licenseDAO.getLicenseByID(licenseId));
	    ld.setLicenseText(dto.getLicenseText());
	}

	ld.setLastModifiedDateTime(dto.getLastModifiedDateTime());
	ld.setContentFolderID(dto.getContentFolderID());

	ld.setDesignType(dto.getDesignType());

	// set learning design to transition.
	for (Transition trans : transList) {
	    trans.setLearningDesign(ld);
	}
	ld.setTransitions(transList);

	// set learning design competences
	for (Competence competence : competenceList) {
	    competence.setLearningDesign(ld);
	}
	ld.setCompetences(competenceList);

	for (Activity act : actList) {
	    act.setLearningDesign(ld);
	}
	ld.setActivities(actList);

	ld.setCreateDateTime(new Date());
	ld.setLastModifiedDateTime(new Date());
	ld.setUser(importer);
	ld.setOriginalUser(importer);
	return ld;
    }

    /**
     * Return Grouping object from given GroupingDTO.
     *
     * @param groupingDto
     * @return
     */
    private Grouping getGrouping(GroupingDTO groupingDto, Map<Integer, Group> groupByUIIDMapper) {
	Grouping grouping = null;
	if (groupingDto == null) {
	    return grouping;
	}

	Integer type = groupingDto.getGroupingTypeID();

	// grouping.setActivities();
	if (Grouping.CHOSEN_GROUPING_TYPE.equals(type)) {
	    grouping = new ChosenGrouping();
	} else if (Grouping.RANDOM_GROUPING_TYPE.equals(type)) {
	    grouping = new RandomGrouping();
	    ((RandomGrouping) grouping).setLearnersPerGroup(groupingDto.getLearnersPerGroup());
	    ((RandomGrouping) grouping).setNumberOfGroups(groupingDto.getNumberOfGroups());
	} else if (Grouping.CLASS_GROUPING_TYPE.equals(type)) {
	    grouping = new LessonClass();
	} else if (Grouping.LEARNER_CHOICE_GROUPING_TYPE.equals(type)) {
	    grouping = new LearnerChoiceGrouping();
	    ((LearnerChoiceGrouping) grouping).setLearnersPerGroup(groupingDto.getLearnersPerGroup());
	    ((LearnerChoiceGrouping) grouping).setNumberOfGroups(groupingDto.getNumberOfGroups());
	    ((LearnerChoiceGrouping) grouping)
		    .setEqualNumberOfLearnersPerGroup(groupingDto.getEqualNumberOfLearnersPerGroup());
	    ((LearnerChoiceGrouping) grouping)
		    .setViewStudentsBeforeSelection(groupingDto.getViewStudentsBeforeSelection());
	} else {
	    log.error("Unable to determine the grouping type. Creating a random grouping. GroupingDTO was "
		    + groupingDto);
	}

	// common fields
	grouping.setGroupingId(groupingDto.getGroupingID());
	grouping.setGroupingUIID(groupingDto.getGroupingUIID());
	grouping.setMaxNumberOfGroups(groupingDto.getMaxNumberOfGroups());

	// process any groups that the design might have
	if (groupingDto.getGroups() != null) {
	    Iterator iter = groupingDto.getGroups().iterator();
	    while (iter.hasNext()) {
		GroupDTO groupDto = (GroupDTO) iter.next();
		Group group = getGroup(groupDto, grouping);
		grouping.getGroups().add(group);
		groupByUIIDMapper.put(group.getGroupUIID(), group);
	    }
	}
	return grouping;
    }

    private Group getGroup(GroupDTO groupDto, Grouping grouping) {
	Group group = new Group();
	group.setBranchActivities(null);
	group.setGrouping(grouping);
	group.setGroupName(groupDto.getGroupName());
	group.setGroupUIID(groupDto.getGroupUIID());
	group.setOrderId(groupDto.getOrderID());
	return group;
    }

    /**
     * Creates the map entry between a branch sequence activity and a group. We need the group maps and the activity
     * maps so that we can update the ids to the groups and the activities. Therefore this method must be done after all
     * the groups are imported and the activities are imported.
     *
     * Note: there isn't an set in the learning design for the branch mappings. The group objects actually contain the
     * link to the mappings, so this method updates the group objects.
     */
    private BranchActivityEntry getBranchActivityEntry(BranchActivityEntryDTO entryDto,
	    Map<Integer, Group> groupByUIIDMapper, Map<Integer, Activity> activityByUIIDMapper) {

	SequenceActivity branch = (SequenceActivity) activityByUIIDMapper.get(entryDto.getSequenceActivityUIID());

	Activity branchingActivity = activityByUIIDMapper.get(entryDto.getBranchingActivityUIID());
	if (branchingActivity == null) {
	    log.error("Unable to find matching branching activity for group to branch mapping " + entryDto
		    + " Skipping entry");
	    return null;
	}

	Group group = groupByUIIDMapper.get(entryDto.getGroupUIID());

	BranchCondition condition = null;
	Boolean gateOpenWhenConditionMet = null;
	if (entryDto instanceof ToolOutputBranchActivityEntryDTO) {
	    BranchConditionDTO dto = ((ToolOutputBranchActivityEntryDTO) entryDto).getCondition();
	    if (dto != null) {
		condition = dto.getCondition();
		condition.setConditionId(null);
	    }
	    if (entryDto instanceof ToolOutputGateActivityEntryDTO) {
		gateOpenWhenConditionMet = ((ToolOutputGateActivityEntryDTO) entryDto).getGateOpenWhenConditionMet();
	    }
	}

	BranchActivityEntry entry = null;
	if (condition != null) {
	    entry = condition.allocateBranchToCondition(entryDto.getEntryUIID(), branch, branchingActivity,
		    gateOpenWhenConditionMet);
	} else if (group != null) {
	    entry = group.allocateBranchToGroup(entryDto.getEntryUIID(), branch, (BranchingActivity) branchingActivity);
	}

	if (entry != null) {
	    if (branch == null) {
		if (branchingActivity.getBranchActivityEntries() == null) {
		    branchingActivity.setBranchActivityEntries(new HashSet());
		}
		branchingActivity.getBranchActivityEntries().add(entry);
	    } else {
		if (branch.getBranchEntries() == null) {
		    branch.setBranchEntries(new HashSet());
		}
		branch.getBranchEntries().add(entry);
	    }
	    return entry;
	} else {
	    log.error("Unable to find group or condition for branch mapping " + entryDto + " Skipping entry");
	    return null;
	}
    }

    private Transition getTransition(TransitionDTO transDto, Map<Long, Activity> activityMapper) {

	Transition trans = null;
	if (transDto == null) {
	    return trans;
	}
	if ((transDto.getTransitionType() != null)
		&& transDto.getTransitionType().equals(Transition.DATA_TRANSITION_TYPE)) {
	    trans = new DataTransition();
	} else {
	    trans = new Transition();
	}

	trans.setDescription(transDto.getDescription());
	Activity fromAct = activityMapper.get(transDto.getFromActivityID());
	trans.setFromActivity(fromAct);
	trans.setFromUIID(fromAct.getActivityUIID());
	// also set transition to activity: It is nonsense for persisit data,
	// but it is help this learning design
	// validated
	if (trans.isProgressTransition()) {
	    fromAct.setTransitionFrom(trans);
	}
	// set to null
	// trans.setLearningDesign();
	trans.setTitle(transDto.getTitle());

	Activity toAct = activityMapper.get(transDto.getToActivityID());
	trans.setToActivity(toAct);
	trans.setToUIID(toAct.getActivityUIID());
	// also set transition to activity: It is nonsense for persisit data,
	// but it is help this learning design
	// validated
	if (trans.isProgressTransition()) {
	    toAct.setTransitionTo(trans);
	}

	trans.setTransitionId(transDto.getTransitionID());
	trans.setTransitionUIID(transDto.getTransitionUIID());

	// reset value
	trans.setCreateDateTime(new Date());

	// copy data flow objects
	if (trans.isDataTransition()) {
	    DataTransition dataTransition = (DataTransition) trans;
	    for (DataFlowObjectDTO dataFlowObjectDto : transDto.getDataFlowObjects()) {
		DataFlowObject dataFlowObject = new DataFlowObject();
		dataFlowObject.setDataTransition(dataTransition);
		dataFlowObject.setName(dataFlowObjectDto.getName());
		dataFlowObject.setDisplayName(dataFlowObjectDto.getDisplayName());
		dataFlowObject.setOrderId(dataFlowObjectDto.getOrderId());
		Integer toolAssigmentId = StringUtils.isBlank(dataFlowObjectDto.getToolAssigmentId()) ? null
			: new Integer(dataFlowObjectDto.getToolAssigmentId());
		dataFlowObject.setToolAssigmentId(toolAssigmentId);
		dataTransition.getDataFlowObjects().add(dataFlowObject);
	    }
	}

	return trans;
    }

    /**
     *
     * @param actDto
     * @param activityMapper
     * @param groupingList
     * @param toolMapper
     * @return
     */
    private Activity getActivity(AuthoringActivityDTO actDto, Map<Long, Grouping> groupingList,
	    Map<Long, ToolContent> toolMapper, Map<Integer, ComplexActivity> defaultActivityToParentActivityMapping) {

	if (actDto == null) {
	    return null;
	}
	int type = actDto.getActivityTypeID().intValue();
	Grouping newGrouping;

	Activity act = Activity.getActivityInstance(type);
	switch (act.getActivityTypeId()) {
	    case Activity.TOOL_ACTIVITY_TYPE:
		// get back the toolContent in new system by activityID in
		// old system.
		ToolContent content = toolMapper.get(actDto.getActivityID());
		// if activity can not find matching tool, the content should be
		// null.
		if (content != null) {
		    ((ToolActivity) act).setTool(content.getTool());
		    ((ToolActivity) act).setToolContentId(content.getToolContentId());
		    ((ToolActivity) act).setToolSessions(null);
		}
		if (actDto.getPlannerMetadataDTO() != null) {
		    PedagogicalPlannerActivityMetadata plannerMetadata = actDto.getPlannerMetadataDTO()
			    .toPlannerMetadata();
		    plannerMetadata.setActivity(((ToolActivity) act));
		    ((ToolActivity) act).setPlannerMetadata(plannerMetadata);
		}

		act.setLearningLibrary(learningLibraryDAO.getLearningLibraryById(actDto.getLearningLibraryID()));
		break;
	    case Activity.GROUPING_ACTIVITY_TYPE:
		newGrouping = groupingList.get(actDto.getCreateGroupingID());
		((GroupingActivity) act).setCreateGrouping(newGrouping);
		((GroupingActivity) act).setCreateGroupingUIID(newGrouping.getGroupingUIID());
		((GroupingActivity) act).setSystemTool(systemToolDAO.getSystemToolByID(SystemTool.GROUPING));
		break;
	    case Activity.SYNCH_GATE_ACTIVITY_TYPE:
		((SynchGateActivity) act).setGateActivityLevelId(actDto.getGateActivityLevelID());
		// always set false
		((SynchGateActivity) act).setGateOpen(false);
		((SynchGateActivity) act).setSystemTool(systemToolDAO.getSystemToolByID(SystemTool.SYNC_GATE));
		break;
	    case Activity.SCHEDULE_GATE_ACTIVITY_TYPE:
		((ScheduleGateActivity) act).setGateActivityLevelId(actDto.getGateActivityLevelID());
		// always set false
		((ScheduleGateActivity) act).setGateOpen(false);

		((ScheduleGateActivity) act).setGateStartTimeOffset(actDto.getGateStartTimeOffset());
		((ScheduleGateActivity) act).setGateEndTimeOffset(actDto.getGateEndTimeOffset());
		((ScheduleGateActivity) act).setGateActivityCompletionBased(actDto.getGateActivityCompletionBased());
		((ScheduleGateActivity) act).setSystemTool(systemToolDAO.getSystemToolByID(SystemTool.SCHEDULE_GATE));
		break;
	    case Activity.PERMISSION_GATE_ACTIVITY_TYPE:
		((PermissionGateActivity) act).setGateActivityLevelId(actDto.getGateActivityLevelID());
		((PermissionGateActivity) act).setGateOpen(false);
		((PermissionGateActivity) act)
			.setSystemTool(systemToolDAO.getSystemToolByID(SystemTool.PERMISSION_GATE));
		break;
	    case Activity.PASSWORD_GATE_ACTIVITY_TYPE:
		((PasswordGateActivity) act).setGateActivityLevelId(actDto.getGateActivityLevelID());
		((PasswordGateActivity) act).setGateOpen(false);

		((PasswordGateActivity) act).setGatePassword(actDto.getGatePassword());
		((PasswordGateActivity) act).setSystemTool(systemToolDAO.getSystemToolByID(SystemTool.PASSWORD_GATE));
		break;
	    case Activity.CONDITION_GATE_ACTIVITY_TYPE:
		((ConditionGateActivity) act).setGateActivityLevelId(actDto.getGateActivityLevelID());
		((ConditionGateActivity) act).setGateOpen(false);
		((ConditionGateActivity) act)
			.setSystemTool(systemToolDAO.getSystemToolByID(SystemTool.PERMISSION_GATE));
		break;
	    case Activity.PARALLEL_ACTIVITY_TYPE:
		act.setLearningLibrary(learningLibraryDAO.getLearningLibraryById(actDto.getLearningLibraryID()));
		break;
	    case Activity.OPTIONS_ACTIVITY_TYPE:
		OptionsActivity optionsActivity = (OptionsActivity) act;
		optionsActivity.setMaxNumberOfOptions(actDto.getMaxOptions());
		optionsActivity.setMinNumberOfOptions(actDto.getMinOptions());
		optionsActivity.setOptionsInstructions(actDto.getOptionsInstructions());
		break;
	    case Activity.OPTIONS_WITH_SEQUENCES_TYPE:
		OptionsWithSequencesActivity optionsWithSequencesActivity = (OptionsWithSequencesActivity) act;
		optionsWithSequencesActivity.setMaxNumberOfOptions(actDto.getMaxOptions());
		optionsWithSequencesActivity.setMinNumberOfOptions(actDto.getMinOptions());
		optionsWithSequencesActivity.setOptionsInstructions(actDto.getOptionsInstructions());
		optionsWithSequencesActivity.setStartXcoord(actDto.getStartXCoord());
		optionsWithSequencesActivity.setEndXcoord(actDto.getEndXCoord());
		optionsWithSequencesActivity.setStartYcoord(actDto.getStartYCoord());
		optionsWithSequencesActivity.setEndYcoord(actDto.getEndYCoord());
		break;
	    case Activity.SEQUENCE_ACTIVITY_TYPE:
		break;
	    case Activity.CHOSEN_BRANCHING_ACTIVITY_TYPE:
		((BranchingActivity) act)
			.setSystemTool(systemToolDAO.getSystemToolByID(SystemTool.TEACHER_CHOSEN_BRANCHING));
		processBranchingFields((BranchingActivity) act, actDto);
		break;
	    case Activity.GROUP_BRANCHING_ACTIVITY_TYPE:
		((BranchingActivity) act)
			.setSystemTool(systemToolDAO.getSystemToolByID(SystemTool.GROUP_BASED_BRANCHING));
		processBranchingFields((BranchingActivity) act, actDto);
		break;
	    case Activity.TOOL_BRANCHING_ACTIVITY_TYPE:
		((BranchingActivity) act)
			.setSystemTool(systemToolDAO.getSystemToolByID(SystemTool.TOOL_BASED_BRANCHING));
		processBranchingFields((BranchingActivity) act, actDto);
		break;
	}

	if (act.isComplexActivity() && (actDto.getDefaultActivityUIID() != null)) {
	    defaultActivityToParentActivityMapping.put(actDto.getDefaultActivityUIID(), (ComplexActivity) act);
	}

	act.setGroupingSupportType(actDto.getGroupingSupportType());
	act.setActivityUIID(actDto.getActivityUIID());
	act.setActivityId(actDto.getActivityID());
	act.setActivityTypeId(actDto.getActivityTypeID());
	act.setApplyGrouping(actDto.getApplyGrouping());
	act.setDescription(actDto.getDescription());
	act.setLanguageFile(actDto.getLanguageFile());

	// added in 2.1 - will be missing from earlier import files.
	if (actDto.getStopAfterActivity() != null) {
	    act.setStopAfterActivity(actDto.getStopAfterActivity());
	}

	act.setLibraryActivityUiImage(actDto.getLibraryActivityUIImage());
	act.setOrderId(actDto.getOrderID());

	// temporarily set as to null, after scan all activities, then set it to
	// valid value.
	act.setParentActivity(null);

	act.setParentUIID(actDto.getParentUIID());
	act.setTitle(actDto.getActivityTitle());

	// relation will be decide in Transition object.
	// act.setTransitionFrom();
	// act.setTransitionTo();

	act.setXcoord(actDto.getxCoord());
	act.setYcoord(actDto.getyCoord());

	// tranfer old grouping to latest
	newGrouping = groupingList.get(actDto.getGroupingID());
	act.setGrouping(newGrouping);
	if (newGrouping != null) {
	    act.setGroupingUIID(newGrouping.getGroupingUIID());
	}

	act.setCreateDateTime(new Date());
	return act;
    }

    private void processBranchingFields(BranchingActivity act, AuthoringActivityDTO actDto) {
	act.setStartXcoord(actDto.getStartXCoord());
	act.setEndXcoord(actDto.getEndXCoord());
	act.setStartYcoord(actDto.getStartYCoord());
	act.setEndYcoord(actDto.getEndYCoord());
    }

    /**
     * Convert content folder ID to real path inside secure dir or on server
     */
    private static String getContentDirPath(String contentFolderID, boolean isFileSystemPath) {
	String contentFolderIDClean = contentFolderID.replaceAll("-", "");
	String contentDir = "";
	for (int charIndex = 0; charIndex < 6; charIndex++) {
	    contentDir += contentFolderIDClean.substring(charIndex * 2, charIndex * 2 + 2)
		    + (isFileSystemPath ? File.separator : "/");
	}
	return contentDir;
    }

    // ******************************************************************
    // Spring injection properties set/get
    // ******************************************************************
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

    public void setQbDAO(IQbDAO qbDAO) {
	this.qbDAO = qbDAO;
    }

    public ILicenseDAO getLicenseDAO() {
	return licenseDAO;
    }

    public void setBaseDAO(IBaseDAO baseDAO) {
	this.baseDAO = baseDAO;
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

    public void setLearningLibraryDAO(ILearningLibraryDAO learningLibraryDAO) {
	this.learningLibraryDAO = learningLibraryDAO;
    }

    public void setSystemToolDAO(ISystemToolDAO systemToolDAO) {
	this.systemToolDAO = systemToolDAO;
    }

    public MessageService getMessageService() {
	return messageService;
    }

    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }

    public ILearningDesignService getLearningDesignService() {
	return learningDesignService;
    }

    public void setLearningDesignService(ILearningDesignService learningDesignService) {
	this.learningDesignService = learningDesignService;
    }
}