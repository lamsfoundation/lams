package org.lamsfoundation.lams.policies.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.policies.Policy;
import org.lamsfoundation.lams.policies.PolicyConsent;
import org.lamsfoundation.lams.policies.dao.IPolicyDAO;
import org.lamsfoundation.lams.signup.dao.ISignupDAO;
import org.lamsfoundation.lams.signup.model.SignupOrganisation;
import org.lamsfoundation.lams.usermanagement.AuthenticationMethod;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.SupportedLocale;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.LanguageUtil;

public class PolicyService implements IPolicyService {

    private IPolicyDAO policyDAO;
    private IUserManagementService userManagementService;
    private ILessonService lessonService;

    @Override
    public void storeUserConsent(String login, Long policyUid) {
	User user = userManagementService.getUserByLogin(login);
	Policy policy = policyDAO.getPolicyByUid(policyUid);
	
	PolicyConsent consent = new PolicyConsent();
	consent.setUser(user);
	consent.setPolicy(policy);
	userManagementService.save(consent);
    }
    
    @Override
    public Policy getPolicyByUid(Long uid) {
	return policyDAO.getPolicyByUid(uid);
    }
    
    @Override
    public void changePolicyStatus(Long policyUid, Integer newPolicyStatus) {
	Policy policy = policyDAO.getPolicyByUid(policyUid);
	policy.setPolicyStateId(newPolicyStatus);
	userManagementService.save(policy);
    }

    @Override
    public List<Policy> getPolicies() {
	return policyDAO.getPolicies();
    }
    
    @Override
    public List<Policy> getActivePolicies() {
	return policyDAO.getActivePolicies();
    }
    
    @Override
    public List<Policy> getPreviousVersionsPolicies(Long policyId) {
	return policyDAO.getPreviousVersionsPolicies(policyId);
    }

    public void setPolicyDAO(IPolicyDAO policyDAO) {
	this.policyDAO = policyDAO;
    }

    public void setUserManagementService(IUserManagementService userManagementService) {
	this.userManagementService = userManagementService;
    }

    public void setLessonService(ILessonService lessonService) {
	this.lessonService = lessonService;
    }
}
