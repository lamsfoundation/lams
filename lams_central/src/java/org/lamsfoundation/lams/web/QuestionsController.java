package org.lamsfoundation.lams.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.qb.model.QbCollection;
import org.lamsfoundation.lams.qb.service.IQbService;
import org.lamsfoundation.lams.questions.Question;
import org.lamsfoundation.lams.questions.QuestionParser;
import org.lamsfoundation.lams.questions.QuestionWordParser;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Runs extraction of the chosen IMS QTI zip file or .docx file. Prepares form for user to manually choose interesting
 * question.
 */
@Controller
public class QuestionsController {

    @Autowired
    @Qualifier("centralMessageService")
    private MessageService messageService;

    @Autowired
    private IQbService qbService;

    @RequestMapping("/questions")
    public String execute(@RequestParam(required = false) String tmpFileUploadId,
	    @RequestParam(required = false) String returnURL, @RequestParam("limitType") String limitTypeParam,
	    @RequestParam(required = false) String importType, @RequestParam(required = false) String callerID,
	    @RequestParam(required = false) Boolean collectionChoice, HttpServletRequest request) throws Exception {

	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();
	boolean isTextBasedInput = "openAi".equals(importType);
	String packageName = null;
	File file = null;

	// this parameter is not really used at the moment
	request.setAttribute("returnURL", returnURL);

	// this parameter is used by the authoring templates. TBL uses QTI import for both the Questions and Assessments tab
	request.setAttribute("callerID", callerID);

	// show only chosen types of questions
	request.setAttribute("limitType", limitTypeParam);

	if (!isTextBasedInput) {
	    File uploadDir = FileUtil.getTmpFileUploadDir(tmpFileUploadId);
	    if (uploadDir.canRead()) {
		File[] files = uploadDir.listFiles();
		if (files.length > 1) {
		    errorMap.add("GLOBAL", "Uploaded more than 1 file");
		} else if (files.length == 1) {
		    file = files[0];
		    packageName = file.getName().toLowerCase();
		}
	    }
	}

	boolean isWordInput = "word".equals(importType);
	request.setAttribute("importType", importType);

	if (collectionChoice != null && collectionChoice) {
	    // in the view a drop down with collections will be displayed
	    List<QbCollection> collections = qbService.getUserCollections(QuestionsController.getUserId());
	    request.setAttribute("collections", collections);
	}
	// user did not choose a file
	if (!isTextBasedInput && (file == null || (isWordInput ? !packageName.endsWith(".docx")
		: !(packageName.endsWith(".zip") || packageName.endsWith(".xml"))))) {
	    errorMap.add("GLOBAL", messageService.getMessage("label.questions.file.missing"));
	}

	if (!errorMap.isEmpty()) {
	    request.setAttribute("tmpFileUploadId", tmpFileUploadId);
	    request.setAttribute("errorMap", errorMap);
	    return "questions/questionFile";
	}

	Question[] questions = null;
	if (isTextBasedInput) {
	    try {
		Class clazz = Class.forName(Configuration.AI_MODULE_CLASS, false, Configuration.class.getClassLoader());
		if (clazz != null) {
		    Method method = clazz.getMethod("parseResponse", String.class);
		    questions = (Question[]) method.invoke(null, request.getParameter("textInput"));
		}
	    } catch (Exception e) {
		errorMap.add("GLOBAL", "Error while parsing text input: " + e.getMessage());
	    }
	} else {
	    Set<String> limitType = null;
	    if (!StringUtils.isBlank(limitTypeParam)) {
		limitType = new TreeSet<>();
		// comma delimited acceptable question types, for example "mc,fb"
		Collections.addAll(limitType, limitTypeParam.split(","));
	    }

	    String tempDirName = Configuration.get(ConfigurationKeys.LAMS_TEMP_DIR);
	    File tempDir = new File(tempDirName);
	    if (!tempDir.exists()) {
		tempDir.mkdirs();
	    }

	    InputStream uploadedFileStream = new FileInputStream(file);

	    if (packageName.endsWith(".xml")) {
		questions = QuestionParser.parseQTIFile(uploadedFileStream, null, limitType);

	    } else if (packageName.endsWith(".docx")) {
		questions = QuestionWordParser.parseWordFile(uploadedFileStream, packageName, limitType);

	    } else {
		questions = QuestionParser.parseQTIPackage(uploadedFileStream, limitType);
	    }

	    FileUtil.deleteTmpFileUploadDir(tmpFileUploadId);
	}

	request.setAttribute("questions", questions);

	return "questions/questionChoice";
    }

    private static Integer getUserId() {
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	return user != null ? user.getUserID() : null;
    }
}