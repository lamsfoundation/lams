package org.lamsfoundation.lams.policies.dao.hibernate;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.policies.Policy;
import org.lamsfoundation.lams.policies.PolicyConsent;
import org.lamsfoundation.lams.policies.PolicyDTO;
import org.lamsfoundation.lams.policies.dao.IPolicyDAO;
import org.springframework.stereotype.Repository;

import com.sun.webkit.PolicyClient;

@Repository
public class PolicyDAO extends LAMSBaseDAO implements IPolicyDAO {

    @Override
    public Policy getPolicyByUid(Long uid) {
	return (Policy) super.find(Policy.class, uid);
    }

    @Override
    public List<Policy> getAllPoliciesWithUserConsentsCount() {
	final String LOAD_POLICIES_WITH_CONSENTS_COUNT = "SELECT policy.*, COUNT(policyConsent.uid) AS userConsentsCount "
		+ "FROM lams_policy AS policy "
		+ "LEFT JOIN lams_policy_consent AS policyConsent ON policyConsent.policy_uid = policy.uid "
		+ "GROUP BY policy.uid ORDER BY policy.last_modified ASC";
	
	SQLQuery query = getSession().createSQLQuery(LOAD_POLICIES_WITH_CONSENTS_COUNT);
	query.addEntity(Policy.class);
	query.addScalar("userConsentsCount");
	List<Object[]> resultQuery = query.list();
	
	// this map keeps the insertion order
	LinkedList<Policy> policies = new LinkedList<Policy>();
	// make the result easier to process
	for (Object[] entry : resultQuery) {
	    Policy policy = (Policy) entry[0];
	    int userConsentsCount = ((Number) entry[1]).intValue();
	    policy.setUserConsentsCount(userConsentsCount);
	    
	    policies.add(policy);
	}
	return policies;
    }
    
    @Override
    public List<Policy> getPreviousVersionsPolicies(Long policyId) {
	String query = "from Policy p where p.policyId=? ORDER BY p.lastModified ASC";
	return (List<Policy>) doFind(query, policyId);
    }
    
    @Override
    public boolean isPolicyConsentRequiredForUser(Integer userId) {
	String SQL = "SELECT count(*) FROM lams_policy as policy" + 
		" LEFT JOIN lams_policy_consent as policyConsent" + 
		" ON policy.uid = policyConsent.policy_uid AND policyConsent.user_id = :userId" + 
		" WHERE policyConsent.uid IS NULL AND policy.policy_state_id=1";
	
	Query query = getSession().createSQLQuery(SQL);
	query.setInteger("userId", userId);
	Object value = query.uniqueResult();
	int result = ((Number) value).intValue();
	
	return result > 0;
    }
    
    @Override
    public List<PolicyDTO> getPolicyDtosByUser(Integer userId) {
	final String LOAD_POLICIES_WITH_USER_CONSENTS = "SELECT policy.*, policyConsent.uid IS NOT NULL as isConsentedByUser, policyConsent.date_agreed_on as dateAgreedOn "
		+ "FROM lams_policy AS policy "
		+ "LEFT JOIN lams_policy_consent AS policyConsent ON policyConsent.policy_uid = policy.uid AND policyConsent.user_id = :userId "
		+ "WHERE policy.policy_state_id=1";
	
	SQLQuery query = getSession().createSQLQuery(LOAD_POLICIES_WITH_USER_CONSENTS);
	query.addEntity(Policy.class);
	query.addScalar("isConsentedByUser");
	query.addScalar("dateAgreedOn");
	query.setInteger("userId", userId);
	List<Object[]> resultQuery = query.list();

	// this map keeps the insertion order
	LinkedList<PolicyDTO> policyDtos = new LinkedList<PolicyDTO>();
	// make the result easier to process
	for (Object[] entry : resultQuery) {
	    Policy policy = (Policy) entry[0];
	    
	    PolicyDTO policyDto = new PolicyDTO(policy);
	    
	    boolean isConsentedByUser = ((Number) entry[1]).intValue() == 1;
	    policyDto.setConsentedByUser(isConsentedByUser);
	    
	    Date dateAgreedOn = (Date) entry[2];
	    policyDto.setDateAgreedOn(dateAgreedOn);
	    
	    policyDtos.add(policyDto);
	}
	return policyDtos;
    }
    
    @Override
    public List<PolicyConsent> getConsentsByUserId(Integer userId) {
	String query = "from PolicyConsent consent where consent.user.userId=? ORDER BY consent.dateAgreedOn ASC";
	return (List<PolicyConsent>) doFind(query, userId);
    }
    
}
