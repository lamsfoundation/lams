package org.lamsfoundation.lams.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.policies.Policy;
import org.lamsfoundation.lams.policies.PolicyConsent;
import org.lamsfoundation.lams.policies.PolicyDTO;
import org.lamsfoundation.lams.policies.service.IPolicyService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Shows policies user has to consent to. Stores PolicyConsents once user agrees to them.
 *
 * @author Andrey Balan
 */
@Controller
@RequestMapping("policyConsents")
public class PolicyConsentsController {
    @Autowired
    private IPolicyService policyService;
    @Autowired
    private IUserManagementService userManagementService;

    @RequestMapping("")
    public String execute(HttpServletRequest request) throws Exception {
	Integer loggedUserId = ((UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER)).getUserID();

	List<PolicyDTO> policyDtos = policyService.getPolicyDtosByUser(loggedUserId);
	request.setAttribute("policies", policyDtos);

	return "policyConsents";
    }

    @RequestMapping("consent")
    public String consent(HttpServletRequest request) {

	UserDTO userDto = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
	User user = userManagementService.getUserByLogin(userDto.getLogin());

	List<PolicyDTO> policyDtos = policyService.getPolicyDtosByUser(user.getUserId());
	//create all missing policy consents
	for (PolicyDTO policyDto : policyDtos) {
	    if (!policyDto.isConsentedByUser()) {
		PolicyConsent consent = new PolicyConsent();
		Policy policy = policyService.getPolicyByUid(policyDto.getUid());
		consent.setPolicy(policy);
		consent.setUser(user);
		userManagementService.save(consent);
	    }
	}
	return "redirect:/";
    }
}
