package org.lamsfoundation.lams.web;

import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.questions.Question;
import org.lamsfoundation.lams.questions.QuestionParser;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;

/**
 * Runs extraction of chosen IMS QTI zip file and prepares form for user to manually choose interesting question.
 * 
 * @struts.action path="/questions" validate="false"
 * @struts.action-forward name="questionChoice" path="/questionChoice.jsp"
 * @struts.action-forward name="questionFile" path="/questionFile.jsp"
 */
public class QuestionsAction extends Action {
    @SuppressWarnings("unchecked")
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	// cumberstone way of extracting POSTed form with a file
	DiskFileUpload formParser = new DiskFileUpload();
	formParser.setRepositoryPath(Configuration.get(ConfigurationKeys.LAMS_TEMP_DIR));
	List<FileItem> formFields = formParser.parseRequest(request);

	String returnURL = null;
	Boolean chooseAnswers = null;
	InputStream packageFileStream = null;
	for (FileItem formField : formFields) {
	    String fieldName = formField.getFieldName();
	    if ("returnURL".equals(fieldName)) {
		// this can be empty; if so, another method of delivering results is used
		returnURL = formField.getString();
	    } else if ("chooseAnswers".equals(fieldName)) {
		chooseAnswers = Boolean.parseBoolean(formField.getString());
	    } else if ("file".equals(fieldName) && !StringUtils.isBlank(formField.getName())) {
		packageFileStream = formField.getInputStream();
	    }
	}

	request.setAttribute("returnURL", returnURL);
	request.setAttribute("chooseAnswers", chooseAnswers);

	// user did not choose a file
	if (packageFileStream == null) {
	    ActionMessages errors = new ActionMessages();
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("label.questions.file.missing"));
	    request.setAttribute(Globals.ERROR_KEY, errors);
	    return mapping.findForward("questionFile");
	}

	Question[] questions = QuestionParser.parseQTIPackage(packageFileStream);
	request.setAttribute("questions", questions);

	return mapping.findForward("questionChoice");
    }
}