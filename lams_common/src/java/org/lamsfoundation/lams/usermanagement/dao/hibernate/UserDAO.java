package org.lamsfoundation.lams.usermanagement.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.usermanagement.IUserDAO;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

/**
 * Hibernate implementation of IUserDAO
 */
public class UserDAO extends BaseDAO implements IUserDAO {

    @Override
    public List<UserDTO> getAllUsersPaged(int page, int size, String sortBy, String sortOrder, String searchString) {

	String GET_USERS = "SELECT user.userId, user.login, user.firstName, user.lastName, user.email" + " FROM "
		+ User.class.getName() + " user " + " WHERE user.disabledFlag=0 ";
	// support for custom search from a toolbar
	GET_USERS = addNameSearch(GET_USERS, searchString);
	//order by
	if ("userId".equals(sortBy)) {
	    sortBy = "user.userId + 0";

	} else if ("login".equals(sortBy)) {
	    sortBy = "user.login";

	} else if ("firstName".equals(sortBy)) {
	    sortBy = "user.firstName";

	} else if ("lastName".equals(sortBy)) {
	    sortBy = "user.lastName";

	} else if ("email".equals(sortBy)) {
	    sortBy = "user.email";

	}
	GET_USERS += " ORDER BY " + sortBy + " " + sortOrder;

	Query query = getSession().createQuery(GET_USERS);
	query.setFirstResult(page * size);
	query.setMaxResults(size);
	List<Object[]> list = query.list();

	//group by userId as long as it returns all completed visitLogs for each user
	List<UserDTO> userDtos = new ArrayList<UserDTO>();
	if (list != null && list.size() > 0) {
	    for (Object[] element : list) {

		Integer userId = ((Number) element[0]).intValue();
		String login = (String) element[1];
		String firstName = (String) element[2];
		String lastName = (String) element[3];
		String email = (String) element[4];

		UserDTO userDto = new UserDTO(userId, firstName, lastName, login, null, null, null, email, null, null,
			null, null, null, null, null, true, null, false);

		userDtos.add(userDto);
	    }
	}

	return userDtos;
    }

    @Override
    public int getCountUsers(String searchString) {
	String GET_USERS = "SELECT count(*) FROM " + User.class.getName() + " user " + " WHERE user.disabledFlag=0 ";
	// support for custom search from a toolbar
	GET_USERS = addNameSearch(GET_USERS, searchString);

	Query query = getSession().createQuery(GET_USERS);

	List list = query.list();
	if (list == null || list.size() == 0) {
	    return 0;
	} else {
	    return ((Number) list.get(0)).intValue();
	}
    }

    private String addNameSearch(String query, String searchString) {
	StringBuilder queryWithSearch = new StringBuilder(query);

	if (!StringUtils.isBlank(searchString)) {
	    String[] tokens = searchString.trim().split("\\s+");
	    for (String token : tokens) {
		String escToken = StringEscapeUtils.escapeSql(token).replace("\\", "\\\\");
		queryWithSearch.append(" AND (user.firstName LIKE '%").append(escToken)
			.append("%' OR user.lastName LIKE '%").append(escToken).append("%' OR user.login LIKE '%")
			.append(escToken).append("%' OR user.email LIKE '%").append(escToken).append("%')");
	    }
	}

	return queryWithSearch.toString();
    }

}
