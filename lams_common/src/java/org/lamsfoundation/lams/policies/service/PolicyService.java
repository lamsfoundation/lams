package org.lamsfoundation.lams.policies.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.policies.Policy;
import org.lamsfoundation.lams.policies.PolicyConsent;
import org.lamsfoundation.lams.policies.PolicyDTO;
import org.lamsfoundation.lams.policies.dao.IPolicyDAO;
import org.lamsfoundation.lams.policies.dto.UserPolicyConsentDTO;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.web.filter.AuditLogFilter;

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
    public void togglePolicyStatus(Long policyUid) {
	Policy policy = policyDAO.getPolicyByUid(policyUid);
	Integer newPolicyStatus = null;
	if (Policy.STATUS_ACTIVE.equals(policy.getPolicyStateId())) {
	    newPolicyStatus = Policy.STATUS_INACTIVE;
	    AuditLogFilter.log(AuditLogFilter.POLICY_DISABLE_ACTION, "policy name: " + policy.getPolicyName());
	} else {
	    newPolicyStatus = Policy.STATUS_ACTIVE;
	    AuditLogFilter.log(AuditLogFilter.POLICY_ENABLE_ACTION, "policy name: " + policy.getPolicyName());
	}
	policy.setPolicyStateId(newPolicyStatus);

	userManagementService.save(policy);

	//in case policy gets activated, deactivate all its other versions
	if (Policy.STATUS_ACTIVE.equals(newPolicyStatus)) {
	    Long policyId = policy.getPolicyId();
	    List<Policy> previousVersions = getPreviousVersionsPolicies(policyId);
	    for (Policy previousVersion : previousVersions) {
		if (!previousVersion.getUid().equals(policyUid)
			&& Policy.STATUS_ACTIVE.equals(previousVersion.getPolicyStateId())) {
		    previousVersion.setPolicyStateId(Policy.STATUS_INACTIVE);
		    userManagementService.save(previousVersion);
		}
	    }
	}
    }

    @Override
    public Collection<Policy> getPoliciesOfDistinctVersions() {
	List<Policy> policies = policyDAO.getAllPoliciesWithUserConsentsCount();

	//calculate which policies have previous version instances
	HashMap<Long, Integer> policyCount = new LinkedHashMap<>();
	for (Policy policy : policies) {
	    Long policyId = policy.getPolicyId();
	    int previousVersionsCount = policyCount.get(policyId) == null ? 0 : policyCount.get(policyId);
	    policyCount.put(policy.getPolicyId(), previousVersionsCount + 1);
	}

	//filter out older versioned policies
	HashMap<Long, Policy> filteredPolicies = new LinkedHashMap<>();
	for (Policy policy : policies) {
	    Long policyId = policy.getPolicyId();
	    boolean hasPreviousVersions = policyCount.get(policyId) != null && policyCount.get(policyId) > 1;
	    policy.setPreviousVersions(hasPreviousVersions);

	    if (filteredPolicies.containsKey(policyId)) {
		Policy alreadyAddedPolicy = filteredPolicies.get(policyId);
		Integer policyStateId = policy.getPolicyStateId();

		//active policy has priority
		if (Policy.STATUS_ACTIVE.equals(policyStateId)) {
		    filteredPolicies.put(policyId, policy);

		    //if neither are active - newer has priority
		} else if (!Policy.STATUS_ACTIVE.equals(alreadyAddedPolicy.getPolicyStateId())
			&& policy.getLastModified().after(alreadyAddedPolicy.getLastModified())) {
		    filteredPolicies.put(policyId, policy);
		}

	    } else {
		filteredPolicies.put(policyId, policy);
	    }

	}
	return filteredPolicies.values();
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

    @Override
    public List<UserPolicyConsentDTO> getConsentDtosByPolicy(Long policyUid, int page, int size, String sortBy,
	    String sortOrder, String searchString) {
	return policyDAO.getConsentDtosByPolicy(policyUid, page, size, sortBy, sortOrder, searchString);
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
