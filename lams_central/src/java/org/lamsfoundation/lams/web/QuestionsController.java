package org.lamsfoundation.lams.web;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.questions.Question;
import org.lamsfoundation.lams.questions.QuestionParser;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Runs extraction of chosen IMS QTI zip file and prepares form for user to manually choose interesting question.
 *
 *
 *
 *
 */
@Controller
public class QuestionsController {
    @SuppressWarnings("unchecked")
    @RequestMapping("/questions")
    public String execute(HttpServletRequest request) throws Exception {
	// cumberstone way of extracting POSTed form with a file
	DiskFileUpload formParser = new DiskFileUpload();
	formParser.setRepositoryPath(Configuration.get(ConfigurationKeys.LAMS_TEMP_DIR));
	List<FileItem> formFields = formParser.parseRequest(request);

	String returnURL = null;
	String limitTypeParam = null;
	InputStream uploadedFileStream = null;
	String packageName = null;
	for (FileItem formField : formFields) {
	    String fieldName = formField.getFieldName();
	    if ("returnURL".equals(fieldName)) {
		// this can be empty; if so, another method of delivering results is used
		returnURL = formField.getString();
	    } else if ("limitType".equals(fieldName)) {
		limitTypeParam = formField.getString();
	    } else if ("file".equals(fieldName) && !StringUtils.isBlank(formField.getName())) {
		packageName = formField.getName().toLowerCase();
		uploadedFileStream = formField.getInputStream();
	    }
	}

	// this parameter is not really used at the moment
	request.setAttribute("returnURL", returnURL);

	// show only chosen types of questions
	request.setAttribute("limitType", limitTypeParam);

	// user did not choose a file
	if ((uploadedFileStream == null) || !(packageName.endsWith(".zip") || packageName.endsWith(".xml"))) {
	    ActionMessages errors = new ActionMessages();
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("label.questions.file.missing"));
	    request.setAttribute(Globals.ERROR_KEY, errors);
	    return "questions/questionFile";
	}

	Set<String> limitType = null;
	if (!StringUtils.isBlank(limitTypeParam)) {
	    limitType = new TreeSet<>();
	    // comma delimited acceptable question types, for example "mc,fb"
	    Collections.addAll(limitType, limitTypeParam.split(","));
	}

	Question[] questions = packageName.endsWith(".xml")
		? QuestionParser.parseQTIFile(uploadedFileStream, null, limitType)
		: QuestionParser.parseQTIPackage(uploadedFileStream, limitType);
	request.setAttribute("questions", questions);

	return "questions/questionChoice";
    }
}