package org.lamsfoundation.lams.web;

import java.io.File;
import java.io.InputStream;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.questions.Question;
import org.lamsfoundation.lams.questions.QuestionParser;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * Runs extraction of chosen IMS QTI zip file and prepares form for user to manually choose interesting question.
 */
@Controller
public class QuestionsController {

    @Autowired
    @Qualifier("centralMessageService")
    MessageService messageService;

    @RequestMapping("/questions")
    public String execute(@RequestParam(name = "file", required = false) MultipartFile file,
	    @RequestParam String returnURL, @RequestParam("limitType") String limitTypeParam,  @RequestParam String callerID,
	    HttpServletRequest request) throws Exception {

	String tempDirName = Configuration.get(ConfigurationKeys.LAMS_TEMP_DIR);
	File tempDir = new File(tempDirName);
	if (!tempDir.exists()) {
	    tempDir.mkdirs();
	}

	InputStream uploadedFileStream = null;
	String packageName = null;
	if (file != null) {
	    packageName = file.getOriginalFilename().toLowerCase();
	    uploadedFileStream = file.getInputStream();
	}

	// this parameter is not really used at the moment
	request.setAttribute("returnURL", returnURL);

	// this parameter is used by the authoring templates. TBL uses QTI import for both the Questions and Assessments tab
	request.setAttribute("callerID", callerID);
	
	
	// show only chosen types of questions
	request.setAttribute("limitType", limitTypeParam);

	// user did not choose a file
	if ((uploadedFileStream == null) || !(packageName.endsWith(".zip") || packageName.endsWith(".xml"))) {
	    MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();
	    errorMap.add("GLOBAL", messageService.getMessage("label.questions.file.missing"));
	    request.setAttribute("errorMap", errorMap);
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