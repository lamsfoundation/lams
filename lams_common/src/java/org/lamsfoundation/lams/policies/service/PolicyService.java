package org.lamsfoundation.lams.policies.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.policies.Policy;
import org.lamsfoundation.lams.policies.PolicyConsent;
import org.lamsfoundation.lams.policies.PolicyDTO;
import org.lamsfoundation.lams.policies.dao.IPolicyDAO;
import org.lamsfoundation.lams.usermanagement.OrganisationState;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;

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
	policy.setLastModified(new Date());
	userManagementService.save(policy);
	
//	//remove according user consents
//	if (Policy.STATUS_INACTIVE.equals(newPolicyStatus) || Policy.STATUS_DRAFT.equals(newPolicyStatus)) { 
//	    HashMap<String, Object> properties = new HashMap<String, Object>();
//	    properties.put("policy.uid", policyUid);
//	    List<PolicyConsent> consents = userManagementService.findByProperties(PolicyConsent.class, properties);
//
//	    Iterator<PolicyConsent> iter = consents.iterator();
//	    while (iter.hasNext()) {
//		PolicyConsent consent = iter.next();
//		userManagementService.delete(consent);
//		iter.remove();
//	    }
//	}
    }

    @Override
    public List<Policy> getAllPoliciesWithUserConsentsCount() {
	return policyDAO.getAllPoliciesWithUserConsentsCount();
    }
    
    @Override
    public List<Policy> getPreviousVersionsPolicies(Long policyId) {
	return policyDAO.getPreviousVersionsPolicies(policyId);
    }
    
    @Override
    public boolean isPolicyConsentRequiredForUser(Integer userId) {
	return policyDAO.isPolicyConsentRequiredForUser(userId);
    }
    
    @Override
    public List<PolicyDTO> getPolicyDtosByUser(Integer userId) {
	return policyDAO.getPolicyDtosByUser(userId);
    }
    
    @Override
    public List<PolicyConsent> getConsentsByUserId(Integer userId) {
	return policyDAO.getConsentsByUserId(userId);
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
