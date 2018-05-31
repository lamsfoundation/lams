package org.lamsfoundation.lams.policies.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.policies.Policy;
import org.lamsfoundation.lams.policies.dao.IPolicyDAO;
import org.springframework.stereotype.Repository;

@Repository
public class PolicyDAO extends LAMSBaseDAO implements IPolicyDAO {

    @Override
    public Policy getPolicyByUid(Long uid) {
	return (Policy) super.find(Policy.class, uid);
    }

    @Override
    public List<Policy> getPolicies() {
	return super.findAll(Policy.class);
    }
    
    @Override
    public List<Policy> getActivePolicies() {
	String query = "from Policy p where p.policyStateId=1 GROUP BY p.policyId ";
	return (List<Policy>) doFind(query);
    }
    
    @Override
    public List<Policy> getActivePoliciesWithConsents() {
	String query = "from Policy p where p.policyStateId=1 GROUP BY p.policyId ";
	return (List<Policy>) doFind(query);
    }
    
    @Override
    public List<Policy> getPreviousVersionsPolicies(Long policyId) {
	String query = "from Policy p where p.policyId=? and p.policyStateId=2";
	return (List<Policy>) doFind(query, policyId);
    }
}
