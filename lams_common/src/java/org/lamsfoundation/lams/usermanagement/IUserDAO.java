package org.lamsfoundation.lams.usermanagement;

import java.util.List;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

/**
 * Interface defining User DAO methods.
 */
public interface IUserDAO extends IBaseDAO {
    List<User> getAllUsers();

    List<User> findUsers(Integer filteredOrgId);

    /**
     * Get all users (paged), except for disabled users.
     *
     * @param page
     * @param size
     * @param sortBy
     * @param sortOrder
     * @param searchPhrase
     *            filters results by course name. It can be null and then doesn't affect results
     * @return paged list of users
     */
    List<UserDTO> getAllUsersPaged(Integer page, Integer size, String sortBy, String sortOrder, String searchPhrase);

    List<UserDTO> getAllUsersPaged(Integer organisationID, String[] roleNames, Integer page, Integer size,
	    String sortBy, String sortOrder, String searchPhrase);

    /**
     * Count total number of users excluding disabled ones and applying searchString filter.
     */
    int getCountUsers(String searchPhrase);

    List<User> findUsers(String searchPhrase);

    List<User> findUsers(String searchPhrase, Integer filteredOrgId);

    List<User> findUsers(String searchPhrase, Integer orgId, Integer filteredOrgId);

    List<User> findUsers(String searchPhrase, Integer orgId, boolean includeChildOrgs);

    List<User> findUsersWithEmail(String email);

}