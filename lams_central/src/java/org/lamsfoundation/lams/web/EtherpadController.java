package org.lamsfoundation.lams.web;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.etherpad.EtherpadException;
import org.lamsfoundation.lams.etherpad.service.IEtherpadService;
import org.lamsfoundation.lams.etherpad.util.EtherpadUtil;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/etherpad")
public class EtherpadController {

    @Autowired
    IEtherpadService etherpadService;

    /**
     * Creates or fetches an existing Etherpad pad using API client, sets a cookie.
     *
     * @param groupId
     *            LAMS-specific group ID (just any identifier); do not confuse with Etherpad groupId, readOnlyId or
     *            padId
     * @param content
     *            initial content of Etherpad; ignored if null or Etherpad already exists
     * @return etherpad pad ID understood by etherpad API
     */
    @RequestMapping(path = "/getPad", produces = "application/json;charset=utf-8")
    @ResponseBody
    private String getPad(@RequestParam String groupId, @RequestParam(required = false) String content,
	    HttpServletResponse response) throws EtherpadException {
	String preparedContent = EtherpadUtil.preparePadContent(content);
	// try to create an Etherpad pad
	Map<String, Object> padData = etherpadService.createPad(groupId, preparedContent);

	String etherpadGroupId = (String) padData.get("groupId");
	String etherpadReadOnlyId = (String) padData.get("readOnlyId");
	//don't allow sessions that has had problems with pad initialisation
	if (StringUtils.isEmpty(etherpadGroupId) || StringUtils.isEmpty(etherpadReadOnlyId)) {
	    throw new EtherpadException(
		    "Etherpad has had problems with initialization. Please seek help from your teacher.");
	}

	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	String userName = user.getFirstName() + " " + user.getLastName();
	String authorId = etherpadService.createAuthor(user.getUserID(), userName);

	// create cookie with ALL valid user session IDs so there can be multiple pads on a single page
	Cookie cookie = etherpadService.createCookie(authorId, etherpadGroupId, true);
	response.addCookie(cookie);

	return (String) padData.get("padId");
    }
}