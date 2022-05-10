package org.lamsfoundation.lams.policies.dao.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.policies.Policy;
import org.lamsfoundation.lams.policies.PolicyConsent;
import org.lamsfoundation.lams.policies.PolicyDTO;
import org.lamsfoundation.lams.policies.dao.IPolicyDAO;
import org.lamsfoundation.lams.policies.dto.UserPolicyConsentDTO;
import org.springframework.stereotype.Repository;

@Repository
public class PolicyDAO extends LAMSBaseDAO implements IPolicyDAO {

    @Override
    public Policy getPolicyByUid(Long uid) {
	return super.find(Policy.class, uid);
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
	LinkedList<Policy> policies = new LinkedList<>();
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
	return doFindCacheable(query, policyId);
    }

    @Override
    public boolean isPolicyConsentRequiredForUser(Integer userId) {
	String SQL = "SELECT count(*) FROM lams_policy as policy" + " LEFT JOIN lams_policy_consent as policyConsent"
		+ " ON policy.uid = policyConsent.policy_uid AND policyConsent.user_id = :userId"
		+ " WHERE policyConsent.uid IS NULL AND policy.policy_state_id=1";

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
	LinkedList<PolicyDTO> policyDtos = new LinkedList<>();
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
	return doFind(query, userId);
    }

    @Override
    public List<UserPolicyConsentDTO> getConsentDtosByPolicy(Long policyUid, int page, int size, String sortBy,
	    String sortOrder, String searchString) {

	String queryText = "SELECT user.user_id, user.login, user.first_name, user.last_name, policyConsent.uid IS NOT NULL as consented, policyConsent.date_agreed_on "
		+ " FROM lams_user AS user"
		+ " LEFT JOIN lams_policy_consent AS policyConsent ON policyConsent.user_id = user.user_id AND policyConsent.policy_uid = :policyUid "
		+ " WHERE user.disabled_flag=0";

	switch (sortBy) {
	    case "userId":
		sortBy = "user.userId + 0 ";
		break;
	    case "login":
		sortBy = "user.login ";
		break;
	    case "fullName":
		sortBy = "(CONCAT(user.last_name, ' ', user.first_name)) ";
		break;
	    case "consented":
		sortBy = "consented ";
		break;
	    case "consentedOn":
		sortBy = "policyConsent.date_agreed_on ";
		break;
	}

	StringBuilder queryBuilder = new StringBuilder(queryText);

	// support for custom search from a toolbar
	if (StringUtils.isNotBlank(searchString)) {
	    String[] tokens = searchString.trim().split("\\s+");
	    for (String token : tokens) {
		String escToken = StringEscapeUtils.escapeSql(token).replace("\\", "\\\\");
		queryBuilder.append(" AND (user.first_name LIKE '%").append(escToken)
			.append("%' OR user.last_name LIKE '%").append(escToken).append("%' OR user.login LIKE '%")
			.append(escToken).append("%')");
	    }
	}

	//order by
	queryBuilder.append(" ORDER BY ").append(sortBy).append(sortOrder);

	Query query = getSession().createSQLQuery(queryBuilder.toString());
	query.setLong("policyUid", policyUid);
	query.setMaxResults(size);
	query.setFirstResult(page * size);
	List<Object[]> list = query.list();

	//group by userId as long as it returns all completed visitLogs for each user
	List<UserPolicyConsentDTO> policyConsentDtos = new ArrayList<>();
	for (Object[] element : list) {
	    Integer userId = ((Number) element[0]).intValue();
	    String login = (String) element[1];
	    String firstName = (String) element[2];
	    String lastName = (String) element[3];

	    UserPolicyConsentDTO policyConsentDto = new UserPolicyConsentDTO(userId, firstName, lastName, login);

	    boolean isConsentGivenByUser = ((Number) element[4]).intValue() == 1;
	    policyConsentDto.setConsentGivenByUser(isConsentGivenByUser);

	    Date dateAgreedOn = (Date) element[5];
	    policyConsentDto.setDateAgreedOn(dateAgreedOn);

	    policyConsentDtos.add(policyConsentDto);
	}

	return policyConsentDtos;
    }

}
