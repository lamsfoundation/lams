package org.lamsfoundation.lams.usermanagement;

import java.util.List;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

/**
 * Interface defining User DAO methods.
 */
public interface IUserDAO extends IBaseDAO {

    /**
     * Get all users (paged), except for disabled users.
     *
     * @param page
     * @param size
     * @param sortBy
     * @param sortOrder
     * @param searchString
     *            filters results by course name. It can be null and then doesn't affect results
     * @return paged list of users
     */
    List<UserDTO> getAllUsersPaged(int page, int size, String sortBy, String sortOrder, String searchString);

    /**
     * Count total number of users excluding disabled ones and applying searchString filter.
     *
     * @param searchString
     * @return
     */
    int getCountUsers(String searchString);
}
