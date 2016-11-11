package org.lamsfoundation.lams.usermanagement.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.usermanagement.IUserDAO;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

/**
 * Hibernate implementation of IUserDAO
 */
public class UserDAO extends LAMSBaseDAO implements IUserDAO {

    @SuppressWarnings("unchecked")
    @Override
    public List<UserDTO> getAllUsersPaged(int page, int size, String sortBy, String sortOrder, String searchPhrase) {

	switch (sortBy) {
	    case "userId":
		sortBy = "user.userId + 0 ";
		break;
	    case "login":
		sortBy = "user.login ";
		break;
	    case "firstName":
		sortBy = "user.firstName ";
		break;
	    case "lastName":
		sortBy = "user.lastName ";
		break;
	    case "email":
		sortBy = "user.email ";
		break;
	}

	StringBuilder queryBuilder = new StringBuilder(
		"SELECT user.userId, user.login, user.firstName, user.lastName, user.email FROM User user WHERE user.disabledFlag=0 ");
	// support for custom search from a toolbar
	UserDAO.addNameSearch(queryBuilder, "user", searchPhrase);
	//order by
	queryBuilder.append(" ORDER BY ").append(sortBy).append(sortOrder);

	Query query = getSession().createQuery(queryBuilder.toString());
	query.setFirstResult(page * size);
	query.setMaxResults(size);
	List<Object[]> list = query.list();

	//group by userId as long as it returns all completed visitLogs for each user
	List<UserDTO> userDtos = new ArrayList<UserDTO>();
	for (Object[] element : list) {
	    Integer userId = ((Number) element[0]).intValue();
	    String login = (String) element[1];
	    String firstName = (String) element[2];
	    String lastName = (String) element[3];
	    String email = (String) element[4];

	    UserDTO userDto = new UserDTO(userId, firstName, lastName, login, null, null, null, email, null, null, null,
		    null, null, null, true, null, false, null);

	    userDtos.add(userDto);
	}

	return userDtos;
    }

    @Override
    public int getCountUsers(String searchPhrase) {
	StringBuilder queryBuilder = new StringBuilder("SELECT count(*) FROM User user WHERE user.disabledFlag=0 ");
	// support for custom search from a toolbar
	UserDAO.addNameSearch(queryBuilder, "user", searchPhrase);

	Query query = getSession().createQuery(queryBuilder.toString());

	Number count = (Number) query.uniqueResult();
	return count == null ? 0 : count.intValue();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> findUsers(String searchPhrase) {
	StringBuilder queryBuilder = new StringBuilder("SELECT u FROM User u WHERE u.disabledFlag=0 ");
	UserDAO.addNameSearch(queryBuilder, "u", searchPhrase);
	queryBuilder.append(" ORDER BY u.login");

	return find(queryBuilder.toString());
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> findUsers(String searchPhrase, Integer filteredOrgId) {
	StringBuilder queryBuilder = new StringBuilder(
		"SELECT u FROM User u WHERE u.disabledFlag=0 AND u.userId NOT IN (SELECT uo.user.userId FROM UserOrganisation uo WHERE "
			+ "uo.organisation.organisationId=").append(filteredOrgId).append(")");
	UserDAO.addNameSearch(queryBuilder, "u", searchPhrase);
	queryBuilder.append(" ORDER BY u.login");

	return find(queryBuilder.toString());
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> findUsers(String searchPhrase, Integer orgId, Integer filteredOrgId) {
	StringBuilder queryBuilder = new StringBuilder(
		"SELECT uo.user FROM UserOrganisation uo WHERE uo.user.disabledFlag=0 AND uo.organisation.organisationId=")
			.append(orgId)
			.append(" AND uo.user.userId NOT IN (SELECT uo.user.userId FROM UserOrganisation uo WHERE uo.organisation.organisationId=")
			.append(filteredOrgId).append(")");
	UserDAO.addNameSearch(queryBuilder, "uo.user", searchPhrase);
	queryBuilder.append(" ORDER BY uo.user.login");

	return find(queryBuilder.toString());
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> findUsers(String searchPhrase, Integer orgId, boolean includeChildOrgs) {
	StringBuilder queryBuilder = new StringBuilder(
		"SELECT u FROM User u WHERE u.disabledFlag=0 AND u.userId IN (SELECT uo.user.userId FROM UserOrganisation uo"
			+ " WHERE uo.organisation.organisationId=").append(orgId);

	if (includeChildOrgs) {
	    queryBuilder.append(" OR uo.organisation.parentOrganisation.organisationId=").append(orgId);
	}
	queryBuilder.append(")");
	UserDAO.addNameSearch(queryBuilder, "u", searchPhrase);
	queryBuilder.append(" ORDER BY u.login");

	return find(queryBuilder.toString());
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> getAllUsers() {
	return find("FROM User u WHERE u.disabledFlag=0 ORDER BY u.login");
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> findUsers(Integer filteredOrgId) {
	return find(
		"FROM User u WHERE u.disabledFlag=0 AND u.userId NOT IN (SELECT uo.user.userId FROM UserOrganisation uo WHERE uo.organisation.organisationId="
			+ filteredOrgId + ") ORDER BY u.login");
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> findUsersWithEmail(String email) {
	return find("FROM User u WHERE u.email=\'" + StringEscapeUtils.escapeSql(email) + "\' ORDER BY u.login");
    }

    private static void addNameSearch(StringBuilder queryBuilder, String entityName, String searchPhrase) {
	if (!StringUtils.isBlank(searchPhrase)) {
	    String[] tokens = searchPhrase.trim().split("\\s+");
	    for (String token : tokens) {
		String escToken = StringEscapeUtils.escapeSql(token).replace("\\", "\\\\");
		queryBuilder.append(" AND (").append(entityName).append(".firstName LIKE '%").append(escToken)
			.append("%' OR ").append(entityName).append(".lastName LIKE '%").append(escToken)
			.append("%' OR ").append(entityName).append(".login LIKE '%").append(escToken).append("%' OR ")
			.append(entityName).append(".email LIKE '%").append(escToken).append("%')");
	    }
	}
    }
}