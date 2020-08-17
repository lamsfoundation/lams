package org.lamsfoundation.lams.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.qb.service.IQbService;
import org.lamsfoundation.lams.questions.Question;
import org.lamsfoundation.lams.questions.QuestionParser;
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
 * Runs extraction of chosen IMS QTI zip file and prepares form for user to manually choose interesting question.
 */
@Controller
public class QuestionsController {

    @Autowired
    @Qualifier("centralMessageService")
    private MessageService messageService;

    @Autowired
    private IQbService qbService;

    @RequestMapping("/questions")
    public String execute(@RequestParam String tmpFileUploadId, @RequestParam String returnURL,
	    @RequestParam("limitType") String limitTypeParam, @RequestParam String callerID,
	    @RequestParam(required = false) Boolean collectionChoice, HttpServletRequest request) throws Exception {

	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();
	String packageName = null;

	File file = null;
	File uploadDir = FileUtil.getTmpFileUploadDir(tmpFileUploadId);
	if (uploadDir.canRead()) {
	    File[] files = uploadDir.listFiles();
	    if (files.length > 1) {
		errorMap.add("GLOBAL", "Uploaded more than 1 file");
	    } else if (files.length == 1) {
		file = files[0];
		packageName = file.getName().toLowerCase();
		;
	    }
	}

	// this parameter is not really used at the moment
	request.setAttribute("returnURL", returnURL);

	// this parameter is used by the authoring templates. TBL uses QTI import for both the Questions and Assessments tab
	request.setAttribute("callerID", callerID);

	// show only chosen types of questions
	request.setAttribute("limitType", limitTypeParam);

	if (collectionChoice != null && collectionChoice) {
	    // in the view a drop down with collections will be displayed
	    request.setAttribute("collections", qbService.getUserCollections(QuestionsController.getUserId()));
	}

	// user did not choose a file
	if (file == null || !(packageName.endsWith(".zip") || packageName.endsWith(".xml"))) {
	    errorMap.add("GLOBAL", messageService.getMessage("label.questions.file.missing"));
	}

	if (!errorMap.isEmpty()) {
	    request.setAttribute("tmpFileUploadId", tmpFileUploadId);
	    request.setAttribute("errorMap", errorMap);
	    return "questions/questionFile";
	}

	String tempDirName = Configuration.get(ConfigurationKeys.LAMS_TEMP_DIR);
	File tempDir = new File(tempDirName);
	if (!tempDir.exists()) {
	    tempDir.mkdirs();
	}

	Set<String> limitType = null;
	if (!StringUtils.isBlank(limitTypeParam)) {
	    limitType = new TreeSet<>();
	    // comma delimited acceptable question types, for example "mc,fb"
	    Collections.addAll(limitType, limitTypeParam.split(","));
	}

	InputStream uploadedFileStream = new FileInputStream(file);

	Question[] questions = packageName.endsWith(".xml")
		? QuestionParser.parseQTIFile(uploadedFileStream, null, limitType)
		: QuestionParser.parseQTIPackage(uploadedFileStream, limitType);
	request.setAttribute("questions", questions);

	return "questions/questionChoice";
    }

    private static Integer getUserId() {
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	return user != null ? user.getUserID() : null;
    }
}