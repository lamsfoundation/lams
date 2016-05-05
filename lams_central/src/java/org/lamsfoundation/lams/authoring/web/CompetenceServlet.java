package org.lamsfoundation.lams.authoring.web;

import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.authoring.service.IAuthoringService;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.wddx.FlashMessage;
import org.lamsfoundation.lams.util.wddx.WDDXProcessor;
import org.lamsfoundation.lams.web.servlet.AbstractStoreWDDXPacketServlet;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @web:servlet name="competenceServlet"
 * @web:servlet-mapping url-pattern="/competenceServlet"
 */
public class CompetenceServlet extends AbstractStoreWDDXPacketServlet {

    protected Logger log = Logger.getLogger(CompetenceServlet.class);
    private long learningDesignID;

    @Override
    protected String process(String lessonPackage, HttpServletRequest request) throws Exception {
	//get User infomation from shared session.
	HttpSession ss = SessionManager.getSession();

	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	Integer userID = (user != null) ? user.getUserID() : null;

	if (userID == null) {
	    log.error("Can not find valid login user information");
	    FlashMessage flashMessage = new FlashMessage("createLesson", "Can not find valid login user information",
		    FlashMessage.ERROR);
	    return flashMessage.serializeMessage();
	}

	if (log.isDebugEnabled()) {
	    log.debug("InitializeLessonServlet process received packet " + lessonPackage);
	}

	try {

	    IAuthoringService authoringService = getAuthoringService();

	    // get ldid from wddx packet
	    Hashtable table = (Hashtable) WDDXProcessor.deserialize(lessonPackage);

	    // parse WDDX values
	    //String title = WDDXProcessor.convertToString("lessonName", table.get("lessonName")); // competenceTitle
	    //String desc = WDDXProcessor.convertToString("lessonDescription", table.get("lessonDescription")); // competenceDescription

	    LearningDesign ld = authoringService.getLearningDesign(learningDesignID);

	    //Set<Competence> competences = ld.getCompetences();

	    // synchronise competence list with wddx packet

	    authoringService.saveLearningDesign(ld);

	    //IMonitoringService monitoringService = getMonitoringService();
	    //return monitoringService.initializeLesson(userID, lessonPackage);
	} catch (Exception e) {
	    log.error("Exception thrown while creating lesson class.", e);
	    FlashMessage flashMessage = FlashMessage.getExceptionOccured("initializeLesson", e.getMessage());

	    return flashMessage.serializeMessage();
	}
	return "duh";
    }

    @Override
    protected String getMessageKey(String packet, HttpServletRequest request) {
	return "duh";
    }

    public IAuthoringService getAuthoringService() {
	WebApplicationContext webContext = WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServletContext());
	return (IAuthoringService) webContext.getBean("authoringService");
    }
}
