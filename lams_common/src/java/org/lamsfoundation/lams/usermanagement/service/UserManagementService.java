/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.usermanagement.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.UUID;
import java.util.Vector;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.learningdesign.dao.IGroupDAO;
import org.lamsfoundation.lams.logevent.LogEvent;
import org.lamsfoundation.lams.logevent.service.ILogEventService;
import org.lamsfoundation.lams.themes.Theme;
import org.lamsfoundation.lams.usermanagement.FavoriteOrganisation;
import org.lamsfoundation.lams.usermanagement.ForgotPasswordRequest;
import org.lamsfoundation.lams.usermanagement.IUserDAO;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationGroup;
import org.lamsfoundation.lams.usermanagement.OrganisationGrouping;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.UserOrganisation;
import org.lamsfoundation.lams.usermanagement.UserOrganisationCollapsed;
import org.lamsfoundation.lams.usermanagement.UserOrganisationRole;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.usermanagement.dao.IFavoriteOrganisationDAO;
import org.lamsfoundation.lams.usermanagement.dao.IOrganisationDAO;
import org.lamsfoundation.lams.usermanagement.dao.IRoleDAO;
import org.lamsfoundation.lams.usermanagement.dao.IUserOrganisationDAO;
import org.lamsfoundation.lams.usermanagement.dto.OrganisationDTO;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.dto.UserManageBean;
import org.lamsfoundation.lams.util.CommonConstants;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.HashUtil;
import org.lamsfoundation.lams.util.LanguageUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.imgscalr.ResizePictureUtil;
import org.lamsfoundation.lams.web.filter.AuditLogFilter;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * <p>
 * <a href="UserManagementService.java.html"> <i>View Source </i> </a>
 * </p>
 *
 * Manually caches the user objects (by user id) in the shared cache. Whenever a user object is modified, the cached
 * version must be removed. TODO complete the caching - need to remove the user from the cache on modification of
 * user/organisation details.
 *
 * @author Fei Yang, Manpreet Minhas
 */
public class UserManagementService implements IUserManagementService, InitializingBean {

    private Logger log = Logger.getLogger(UserManagementService.class);

    private static final String SEQUENCES_FOLDER_NAME_KEY = "runsequences.folder.name";

    private static final int PASSWORD_HISTORY_DEFAULT_LIMIT = 50;

    private IBaseDAO baseDAO;

    private IGroupDAO groupDAO;

    private IRoleDAO roleDAO;

    private IOrganisationDAO organisationDAO;

    private IUserDAO userDAO;

    private IUserOrganisationDAO userOrganisationDAO;

    private IFavoriteOrganisationDAO favoriteOrganisationDAO;

    protected MessageService messageService;

    private static ILogEventService logEventService;

    private IToolContentHandler centralToolContentHandler;

    private static IUserManagementService instance;

    /*
     * Sometimes we need access to a service from within an entity.
     * For example when fetching ActivityEvaluation for ToolActivity - they should not be in OneToOne relationship
     * as it can not be cached, i.e. is always eagerly fetched.
     * This singleton-type access to service allows fetching data from DB from wherever in code.
     * It is probably a bad design, but we can not enforce lazy loading in any other reasonable way
     * and eager loading makes up a good part of queries sent to DB.
     * The service fetched this way should probably be used for read-only queries
     * as we deliver the real service object, not its transactional proxy.
     */
    @Override
    public void afterPropertiesSet() {
	instance = this;
    }

    public static IUserManagementService getInstance() {
	return instance;
    }

    // ---------------------------------------------------------------------
    // Service Methods
    // ---------------------------------------------------------------------

    @Override
    public void save(Object object) {
	baseDAO.insertOrUpdate(object);
    }

    @Override
    public User saveUser(User user) {
	if (user != null) {
	    // create user
	    if (user.getUserId() == null) {
		baseDAO.insertOrUpdate(user); // creating a workspace needs a userId
		user = createWorkspaceFolderForUser(user);
	    }
	    // LDEV-2030 update workspace name if name changed
	    WorkspaceFolder workspaceFolder = user.getWorkspaceFolder();
	    if ((workspaceFolder != null) && !StringUtils.equals(user.getFullName(), workspaceFolder.getName())) {
		workspaceFolder.setName(user.getFullName());
		save(workspaceFolder);
	    }
	    // LDEV-1356 modification date
	    user.setModifiedDate(new Date());
	    baseDAO.insertOrUpdate(user);
	}

	return user;
    }

    @Override
    public void saveOrganisationGrouping(OrganisationGrouping grouping, Collection<OrganisationGroup> newGroups) {
	if (grouping.getGroupingId() == null) {
	    grouping.setGroups(new HashSet<OrganisationGroup>());
	    baseDAO.insert(grouping);
	}

	if (newGroups != null) {
	    Set<OrganisationGroup> obsoleteGroups = new HashSet<>(grouping.getGroups());
	    for (OrganisationGroup newGroup : newGroups) {
		OrganisationGroup existingGroup = null;
		// check if group already exists
		for (OrganisationGroup existingGroupCandidate : grouping.getGroups()) {
		    if (existingGroupCandidate.equals(newGroup)) {
			existingGroup = existingGroupCandidate;
			break;
		    }
		}

		if (existingGroup == null) {
		    newGroup.setGroupingId(grouping.getGroupingId());
		    // it is a new group, so add it
		    grouping.getGroups().add(newGroup);
		    baseDAO.insert(newGroup);
		} else {
		    obsoleteGroups.remove(existingGroup);

		    existingGroup.setName(newGroup.getName());
		    existingGroup.setUsers(newGroup.getUsers());
		    baseDAO.update(existingGroup);
		}
	    }

	    // remove gropus from DB
	    for (OrganisationGroup obsoleteGroup : obsoleteGroups) {
		grouping.getGroups().remove(obsoleteGroup);
		baseDAO.delete(obsoleteGroup);
	    }
	}
    }

    @Override
    public void delete(Object object) {
	baseDAO.delete(object);
    }

    @Override
    public void deleteAll(Collection objects) {
	baseDAO.deleteAll(objects);
    }

    @Override
    public void deleteById(Class clazz, Serializable id) {
	baseDAO.deleteById(clazz, id);
    }

    @Override
    public Object findById(Class clazz, Serializable id) {
	return baseDAO.find(clazz, id);
    }

    @Override
    public List findAll(Class clazz) {
	return baseDAO.findAll(clazz);
    }

    @Override
    public List findByProperty(Class clazz, String name, Object value) {
	return baseDAO.findByProperty(clazz, name, value);
    }

    @Override
    public List findByProperty(Class clazz, String name, Object value, boolean cache) {
	return baseDAO.findByProperty(clazz, name, value, cache);
    }

    @Override
    public <T> List<T> findByPropertyValues(Class<T> clazz, String name, Collection<?> values) {
	return baseDAO.findByPropertyValues(clazz, name, values);
    }

    @Override
    public List findByProperties(Class clazz, Map<String, Object> properties) {
	return findByProperties(clazz, properties, false);
    }

    @Override
    public List findByProperties(Class clazz, Map<String, Object> properties, boolean cache) {
	return baseDAO.findByProperties(clazz, properties, cache);
    }

    @Override
    public List<User> getUsersFromOrganisation(Integer orgId) {
	String query = "select uo.user from UserOrganisation uo" + " where uo.organisation.organisationId=" + orgId
		+ " order by uo.user.login";
	return baseDAO.find(query);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Vector getUsersFromOrganisationByRole(Integer organisationID, String roleName, boolean getUser) {
	Vector users = null;
	if (getUser) {
	    users = new Vector<User>();
	} else {
	    users = new Vector<UserDTO>();
	}

	// it's ugly to put query string here, but it is a convention of this class so let's stick to it for now
	String query = "SELECT uo.user FROM UserOrganisation uo INNER JOIN uo.userOrganisationRoles r WHERE uo.organisation.organisationId="
		+ organisationID + " AND r.role.name= '" + roleName + "'";
	List<User> queryResult = baseDAO.find(query);

	for (User user : queryResult) {
	    if (getUser) {
		users.add(user);
	    } else {
		users.add(user.getUserDTO());
	    }
	}

	return users;
    }

    @Override
    public List<Organisation> getFavoriteOrganisationsByUser(Integer userId) {
	return favoriteOrganisationDAO.getFavoriteOrganisationsByUser(userId);
    }

    @Override
    public boolean isOrganisationFavorite(Integer organisationId, Integer userId) {
	return favoriteOrganisationDAO.isOrganisationFavorite(organisationId, userId);
    }

    @Override
    public void toggleOrganisationFavorite(Integer orgId, Integer userId) {
	FavoriteOrganisation favoriteOrganisation = favoriteOrganisationDAO.getFavoriteOrganisation(orgId, userId);

	//create new favoriteOrganisation if it doesn't exist
	if (favoriteOrganisation == null) {

	    User user = (User) findById(User.class, userId);
	    Organisation organisation = (Organisation) findById(Organisation.class, orgId);
	    favoriteOrganisation = new FavoriteOrganisation(user, organisation);
	    save(favoriteOrganisation);

	    //remove favoriteOrganisation if it existed
	} else {
	    delete(favoriteOrganisation);
	}
    }

    @Override
    public Organisation getRootOrganisation() {
	return baseDAO.findByProperty(Organisation.class, "organisationType.organisationTypeId",
		OrganisationType.ROOT_TYPE, true).get(0);
    }

    @Override
    public boolean isUserInRole(Integer userId, Integer orgId, String roleName) {
	Map<String, Object> properties = new HashMap<>();
	properties.put("userOrganisation.user.userId", userId);
	properties.put("userOrganisation.organisation.organisationId", orgId);
	properties.put("role.name", roleName);
	if (baseDAO.findByProperties(UserOrganisationRole.class, properties, true).size() == 0) {
	    return false;
	}
	return true;
    }

    @Override
    public Organisation getOrganisationById(Integer organisationId) {
	return (Organisation) findById(Organisation.class, organisationId);
    }

    @Override
    public List getOrganisationsByTypeAndStatus(Integer typeId, Integer stateId) {
	Map<String, Object> properties = new HashMap<>();
	properties.put("organisationType.organisationTypeId", typeId);
	properties.put("organisationState.organisationStateId", stateId);
	return baseDAO.findByProperties(Organisation.class, properties, true);
    }

    @Override
    public List<Organisation> getPagedCourses(final Integer parentOrgId, final Integer typeId, final Integer stateId,
	    int page, int size, String sortBy, String sortOrder, String searchString) {
	return organisationDAO.getPagedCourses(parentOrgId, typeId, stateId, page, size, sortBy, sortOrder,
		searchString);
    }

    @Override
    public int getCountCoursesByParentCourseAndTypeAndState(final Integer parentOrgId, final Integer typeId,
	    final Integer stateId, String searchString) {
	return organisationDAO.getCountCoursesByParentCourseAndTypeAndState(parentOrgId, typeId, stateId, searchString);
    }

    @Override
    public List<UserOrganisationRole> getUserOrganisationRoles(Integer orgId, String login) {
	Map<String, Object> properties = new HashMap<>();
	properties.put("userOrganisation.organisation.organisationId", orgId);
	properties.put("userOrganisation.user.login", login);
	return baseDAO.findByProperties(UserOrganisationRole.class, properties);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<Integer, Set<Integer>> getRolesForUser(Integer userId) {
	return ((List<UserOrganisation>) findByProperty(UserOrganisation.class, "user.userId", userId)).stream()
		.collect(Collectors.toMap(userOrganisation -> userOrganisation.getOrganisation().getOrganisationId(),
			userOrganisation -> userOrganisation.getUserOrganisationRoles().stream()
				.map(userOrganisationRole -> userOrganisationRole.getRole().getRoleId())
				.collect(Collectors.toSet())));
    }

    @Override
    public List getUserOrganisationsForUserByTypeAndStatus(String login, Integer typeId, Integer stateId) {
	Map<String, Object> properties = new HashMap<>();
	properties.put("user.login", login);
	properties.put("organisation.organisationType.organisationTypeId", typeId);
	properties.put("organisation.organisationState.organisationStateId", stateId);
	return baseDAO.findByProperties(UserOrganisation.class, properties);
    }

    @Override
    public List getUserOrganisationsForUserByTypeAndStatusAndParent(String login, Integer typeId, Integer stateId,
	    Integer parentOrgId) {
	Map<String, Object> properties = new HashMap<>();
	properties.put("user.login", login);
	properties.put("organisation.organisationType.organisationTypeId", typeId);
	properties.put("organisation.organisationState.organisationStateId", stateId);
	properties.put("organisation.parentOrganisation.organisationId", parentOrgId);
	return baseDAO.findByProperties(UserOrganisation.class, properties);
    }

    @Override
    public UserOrganisationCollapsed getUserOrganisationCollapsed(Integer userId, Integer orgId) {
	Map<String, Object> properties = new HashMap<>();
	properties.put("user.userId", userId);
	properties.put("organisation.organisationId", orgId);
	List<UserOrganisationCollapsed> results = baseDAO.findByProperties(UserOrganisationCollapsed.class, properties);
	return results.isEmpty() ? null : (UserOrganisationCollapsed) results.get(0);
    }

    @Override
    public List<UserOrganisationCollapsed> getChildOrganisationsCollapsedByUser(Integer parentOrganisationId,
	    Integer userId) {
	return organisationDAO.getChildOrganisationsCollapsedByUser(parentOrganisationId, userId);
    }

    @Override
    public User getUserByLogin(String login) {
	List results = baseDAO.findByProperty(User.class, "login", login);
	return results.isEmpty() ? null : (User) results.get(0);
    }

    @Override
    public User getUserById(Integer userId) {
	return (User) findById(User.class, userId);
    }

    @Override
    public void updatePassword(User user, String password) {
	String salt = HashUtil.salt();
	user.setSalt(salt);
	String hash = HashUtil.sha256(password, salt);
	user.setPassword(hash);
	user.setModifiedDate(new Date());
	LocalDateTime date = LocalDateTime.now();
	user.setPasswordChangeDate(date);

	// add new password to history
	SortedMap<LocalDateTime, String> history = user.getPasswordHistory();
	history.put(date, hash + "=" + salt);

	// clear old password, about the limit
	int historyLimit = Configuration.getAsInt(ConfigurationKeys.PASSWORD_HISTORY_LIMIT);
	// if no limit is set then set some high limit to keep the table tidy
	historyLimit = historyLimit <= 0 ? PASSWORD_HISTORY_DEFAULT_LIMIT : historyLimit;
	while (historyLimit < history.size()) {
	    history.remove(history.firstKey());
	}

	user.setChangePassword(false);

	baseDAO.insertOrUpdate(user);

	logPasswordChanged(user, user);
    }

    @Override
    public UserOrganisation getUserOrganisation(Integer userId, Integer orgId) {
	Map<String, Object> properties = new HashMap<>();
	properties.put("user.userId", userId);
	properties.put("organisation.organisationId", orgId);
	List results = baseDAO.findByProperties(UserOrganisation.class, properties);
	return results.isEmpty() ? null : (UserOrganisation) results.get(0);
    }

    private User createWorkspaceFolderForUser(User user) {
	WorkspaceFolder folder = new WorkspaceFolder(user.getFullName(), user.getUserId(), new Date(), new Date(),
		WorkspaceFolder.NORMAL);
	save(folder);
	user.setWorkspaceFolder(folder);
	return user;
    }

    private void createWorkspaceFoldersForOrganisation(Organisation organisation, Integer userID, Date createDateTime) {
	WorkspaceFolder workspaceFolder = new WorkspaceFolder(organisation.getName(), userID, createDateTime,
		createDateTime, WorkspaceFolder.NORMAL);
	workspaceFolder.setOrganisationID(organisation.getOrganisationId());
	save(workspaceFolder);

	String description = getRunSequencesFolderName(organisation.getName());
	WorkspaceFolder workspaceFolder2 = new WorkspaceFolder(description, userID, createDateTime, createDateTime,
		WorkspaceFolder.RUN_SEQUENCES);
	workspaceFolder2.setOrganisationID(organisation.getOrganisationId());
	workspaceFolder2.setParentWorkspaceFolder(workspaceFolder);
	save(workspaceFolder2);

	workspaceFolder.addChild(workspaceFolder2);
	save(workspaceFolder);

	Set<WorkspaceFolder> folders = new HashSet<>();
	folders.add(workspaceFolder);
	folders.add(workspaceFolder2);
	organisation.setWorkspaceFolders(folders);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Organisation saveOrganisation(Organisation organisation, Integer userID) {

	User creator = (User) findById(User.class, userID);

	if (organisation.getOrganisationId() == null) {
	    Date createDateTime = new Date();
	    organisation.setCreateDate(createDateTime);
	    organisation.setCreatedBy(creator);

	    save(organisation);

	    if (organisation.getOrganisationType().getOrganisationTypeId().equals(OrganisationType.COURSE_TYPE)) {
		createWorkspaceFoldersForOrganisation(organisation, userID, createDateTime);
	    }

	    if (organisation.getOrganisationType().getOrganisationTypeId().equals(OrganisationType.CLASS_TYPE)) {
		Organisation pOrg = organisation.getParentOrganisation();
		// set parent's child orgs
		Set children = pOrg.getChildOrganisations();
		if (children == null) {
		    children = new HashSet();
		}
		children.add(organisation);
		pOrg.setChildOrganisations(children);
		// get course managers and give them staff role in this new
		// class
		Vector<UserDTO> managers = getUsersFromOrganisationByRole(pOrg.getOrganisationId(), Role.GROUP_MANAGER,
			false);
		for (UserDTO m : managers) {
		    User user = (User) findById(User.class, m.getUserID());
		    UserOrganisation uo = new UserOrganisation(user, organisation);
		    log.debug("adding course manager: " + user.getUserId() + " as staff");
		    UserOrganisationRole uor = new UserOrganisationRole(uo,
			    (Role) findById(Role.class, Role.ROLE_MONITOR));
		    HashSet uors = new HashSet();
		    uors.add(uor);
		    uo.setUserOrganisationRoles(uors);

		    // attach new UserOrganisation to the Organisation, then
		    // save the UserOrganisation.
		    // this way the Set Organisations.userOrganisations contains
		    // persisted objects,
		    // and we can safely add new UserOrganisations if necessary
		    // (i.e. if there are
		    // several course managers).
		    Set uos = organisation.getUserOrganisations();
		    if (uos == null) {
			uos = new HashSet();
		    }
		    uos.add(uo);
		    organisation.setUserOrganisations(uos);

		    save(uo);
		}
	    }
	} else {
	    // update workspace/folder names
	    WorkspaceFolder folder = organisation.getNormalFolder();
	    if (folder != null) {
		folder.setName(organisation.getName());
	    }
	    folder = organisation.getRunSequencesFolder();
	    if (folder != null) {
		folder.setName(getRunSequencesFolderName(organisation.getName()));
	    }
	}

	return organisation;
    }

    @Override
    public void updateOrganisationAndWorkspaceFolderNames(Organisation organisation) {
	baseDAO.update(organisation);
	WorkspaceFolder folder = organisation.getNormalFolder();
	folder.setName(organisation.getName());
	baseDAO.update(folder);
	folder = organisation.getRunSequencesFolder();
	folder.setName(getRunSequencesFolderName(organisation.getName()));
	baseDAO.update(folder);
    }

    private String getRunSequencesFolderName(String workspaceName) {
	// get i18n'd message according to server locale
	String[] tokenisedLocale = LanguageUtil.getDefaultLangCountry();
	Locale serverLocale = new Locale(tokenisedLocale[0], tokenisedLocale[1]);
	String runSeqName = messageService.getMessageSource().getMessage(
		UserManagementService.SEQUENCES_FOLDER_NAME_KEY, new Object[] { workspaceName }, serverLocale);

	if ((runSeqName != null) && runSeqName.startsWith("???")) {
	    log.warn("Problem in the language file - can't find an entry for "
		    + UserManagementService.SEQUENCES_FOLDER_NAME_KEY + ". Creating folder as \"run sequences\" ");
	    runSeqName = "run sequences";
	}
	return runSeqName;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<UserManageBean> getUserManageBeans(Integer orgId) {
	String query = "select u.userId,u.login,u.title,u.firstName,u.lastName, r "
		+ "from User u left join u.userOrganisations as uo left join uo.userOrganisationRoles as uor left join uor.role as r where uo.organisation.organisationId=?";
	List list = baseDAO.find(query, orgId);
	Map<Integer, UserManageBean> beansMap = new HashMap<>();
	for (int i = 0; i < list.size(); i++) {
	    Object[] data = (Object[]) list.get(i);
	    if (beansMap.containsKey(data[0])) {
		beansMap.get(data[0]).getRoles().add((Role) data[5]);
	    } else {
		UserManageBean bean = new UserManageBean();
		bean.setUserId((Integer) data[0]);
		bean.setLogin((String) data[1]);
		bean.setTitle((String) data[2]);
		bean.setFirstName((String) data[3]);
		bean.setLastName((String) data[4]);
		bean.getRoles().add((Role) data[5]);
		beansMap.put((Integer) data[0], bean);
	    }
	}
	List<UserManageBean> userManageBeans = new ArrayList<>();
	userManageBeans.addAll(beansMap.values());
	return userManageBeans;
    }

    @Override
    public void removeUser(Integer userId) throws Exception {

	User user = (User) findById(User.class, userId);
	if (user != null) {

	    if (userHasData(user)) {
		throw new Exception("Cannot remove User ID " + userId + ". User has data.");
	    }

	    log.debug("deleting user " + user.getLogin());
	    delete(user);

	    AuditLogFilter.log(AuditLogFilter.USER_DELETE_ACTION, "user login: " + user.getLogin());
	} else {
	    log.error("Requested delete of a user who does not exist. User ID " + userId);
	}
    }

    @Override
    public Boolean userHasData(User user) {
	if (user.getLearnerProgresses() != null) {
	    if (!user.getLearnerProgresses().isEmpty()) {
		log.debug("user has data, learnerProgresses: " + user.getLearnerProgresses().size());
		return true;
	    }
	}
	if (user.getLearningDesigns() != null) {
	    if (!user.getLearningDesigns().isEmpty()) {
		log.debug("user has data, learningDesigns: " + user.getLearningDesigns().size());
		return true;
	    }
	}
	if (user.getLessons() != null) {
	    if (!user.getLessons().isEmpty()) {
		log.debug("user has data, lessons: " + user.getLessons().size());
		return true;
	    }
	}
	int numLessonGroups = groupDAO.getCountGroupsForUser(user.getUserId());
	if (numLessonGroups > 0) {
	    log.debug("user has data, userGroups: " + numLessonGroups);
	    return true;
	}
	return false;
    }

    @Override
    public void disableUser(Integer userId) {
	User user = (User) findById(User.class, userId);
	user.setDisabledFlag(true);
	log.debug("disabling user " + user.getLogin());
	saveUser(user);

	Set uos = user.getUserOrganisations();
	Iterator iter = uos.iterator();
	while (iter.hasNext()) {
	    UserOrganisation uo = (UserOrganisation) iter.next();
	    log.debug("removing membership of: " + uo.getOrganisation().getName());
	    delete(uo);
	    iter.remove();
	}

	AuditLogFilter.log(AuditLogFilter.USER_DISABLE_ACTION, "user login: " + user.getLogin());
    }

    @Override
    public void setRolesForUserOrganisation(Integer userId, Integer organisationId, Set<Integer> roleIDList) {
	User user = (User) findById(User.class, userId);
	setRolesForUserOrganisation(user, organisationId,
		roleIDList.stream().map(String::valueOf).collect(Collectors.toList()));
    }

    @Override
    public void setRolesForUserOrganisation(User user, Integer organisationId, List<String> rolesList) {

	// Don't pass in the org from the web layer. The import for roles
	// doesn't use the HIbernate open session
	// filter, so it may throw a lazy loading exception when it tried to
	// access the org.UserOrganisations set
	// if org has come from the web layer.
	Organisation org = (Organisation) findById(Organisation.class, organisationId);
	setRolesForUserOrganisation(user, org, rolesList, true);
    }

    @Override
    public void setRolesForUserOrganisation(User user, Organisation org, List<String> rolesList,
	    boolean checkGroupManagerRoles) {

	// The private version of setRolesForUserOrganisation can pass around
	// the org safely as we are within
	// our transation, so no lazy loading errors. This is more efficient for
	// recursive calls to this method.

	UserOrganisation uo = getUserOrganisation(user.getUserId(), org.getOrganisationId());
	if (uo == null) {
	    if (rolesList.isEmpty()) {
		// user has no roles and shoud have none, so nothing to do
		return;
	    }
	    uo = new UserOrganisation(user, org);
	    save(uo);
	    log.debug("added " + user.getLogin() + " to " + org.getName());
	}

	// if user is to be added to a class, make user a member of parent
	// course also if not already
	if (org.getOrganisationType().getOrganisationTypeId().equals(OrganisationType.CLASS_TYPE)
		&& (getUserOrganisation(user.getUserId(), org.getParentOrganisation().getOrganisationId()) == null)) {
	    setRolesForUserOrganisation(user, org.getParentOrganisation(), rolesList, checkGroupManagerRoles);
	}

	List<String> rolesCopy = new ArrayList<>();
	rolesCopy.addAll(rolesList);
	log.debug("rolesList.size: " + rolesList.size());
	Set<UserOrganisationRole> uors = uo.getUserOrganisationRoles();
	Set<UserOrganisationRole> uorsCopy = new HashSet<>();
	if (uors != null) {
	    uorsCopy.addAll(uors);
	    // remove the common part from the rolesList and uors
	    // to get the uors to remove and the roles to add
	    for (String roleId : rolesList) {
		for (UserOrganisationRole uor : uors) {
		    if (uor.getRole().getRoleId().toString().equals(roleId)) {
			// remove from the Copys the ones we are keeping
			rolesCopy.remove(roleId);
			uorsCopy.remove(uor);
		    }
		}
	    }
	    log.debug("removing roles: " + uorsCopy);
	    uors.removeAll(uorsCopy);
	} else {
	    uors = new HashSet<>();
	}
	for (String roleId : rolesCopy) {
	    if (roleId == null) {
		continue;
	    }
	    Role role = (Role) findById(Role.class, Integer.parseInt(roleId));
	    UserOrganisationRole uor = new UserOrganisationRole(uo, role);
	    save(uor);
	    log.debug("setting role: " + role.getName() + " in organisation: " + org.getName());
	    uors.add(uor);
	    // when a user gets these roles, they need a workspace
	    if (role.getName().equals(Role.AUTHOR) || role.getName().equals(Role.APPADMIN)) {
		if (user.getWorkspaceFolder() == null) {
		    createWorkspaceFolderForUser(user);
		}
	    }
	}
	if (uors.isEmpty()) {
	    user.getUserOrganisations().remove(uo);
	    Iterator<UserOrganisation> userOrganisationIterator = org.getUserOrganisations().iterator();
	    while (userOrganisationIterator.hasNext()) {
		if (uo.getUserOrganisationId().equals(userOrganisationIterator.next().getUserOrganisationId())) {
		    userOrganisationIterator.remove();
		    break;
		}
	    }
	    delete(uo);
	} else {
	    uo.setUserOrganisationRoles(uors);
	    save(uo);
	}

	if (checkGroupManagerRoles) {
	    // make sure group managers have monitor in each subgroup
	    checkGroupManager(user, org);
	}
    }

    private void checkGroupManager(User user, Organisation org) {
	if (org.getOrganisationType().getOrganisationTypeId().equals(OrganisationType.COURSE_TYPE)) {
	    if (hasRoleInOrganisation(user, Role.ROLE_GROUP_MANAGER, org)) {
		setRolesForGroupManager(user, org.getChildOrganisations());
	    }
	} else if (org.getOrganisationType().getOrganisationTypeId().equals(OrganisationType.CLASS_TYPE)) {
	    if (hasRoleInOrganisation(user, Role.ROLE_GROUP_MANAGER, org.getParentOrganisation())) {
		setRolesForGroupManager(user, org.getParentOrganisation().getChildOrganisations());
	    }
	}
    }

    private void setRolesForGroupManager(User user, Set childOrgs) {
	for (Object o : childOrgs) {
	    Organisation org = (Organisation) o;

	    // add user to user organisation if doesn't exist
	    UserOrganisation uo = getUserOrganisation(user.getUserId(), org.getOrganisationId());
	    if (uo == null) {
		uo = new UserOrganisation(user, org);
		save(uo);
		Set uos = org.getUserOrganisations();
		uos.add(uo);
		log.debug("added " + user.getLogin() + " to " + org.getName());
		uo = setRoleForUserOrganisation(uo, (Role) findById(Role.class, Role.ROLE_MONITOR));
		save(uo);
		continue;
	    }

	    // iterate through roles and add monitor and learner if don't
	    // already exist
	    Set<UserOrganisationRole> uors = uo.getUserOrganisationRoles();
	    if ((uors != null) && !uors.isEmpty()) {
		boolean isMonitor = false;
		for (UserOrganisationRole uor : uors) {
		    if (uor.getRole().getName().equals(Role.MONITOR)) {
			isMonitor = true;
			break;
		    }
		}
		if (!isMonitor) {
		    uo = setRoleForUserOrganisation(uo, (Role) findById(Role.class, Role.ROLE_MONITOR));
		}
		save(uo);
	    }
	}
    }

    private UserOrganisation setRoleForUserOrganisation(UserOrganisation uo, Role role) {
	UserOrganisationRole uor = new UserOrganisationRole(uo, role);
	save(uor);
	uo.addUserOrganisationRole(uor);
	log.debug("setting role: " + uor.getRole().getName() + " in organisation: "
		+ uor.getUserOrganisation().getOrganisation().getName());
	return uo;
    }

    @Override
    public List<Role> filterRoles(List<Role> rolelist, Boolean isAppadmin, OrganisationType orgType) {
	List<Role> allRoles = new ArrayList<>();
	allRoles.addAll(rolelist);
	Role role = new Role();
	if (!orgType.getOrganisationTypeId().equals(OrganisationType.ROOT_TYPE) || !isAppadmin) {
	    role.setRoleId(Role.ROLE_APPADMIN);
	    allRoles.remove(role);
	} else {
	    role.setRoleId(Role.ROLE_AUTHOR);
	    allRoles.remove(role);
	    role.setRoleId(Role.ROLE_LEARNER);
	    allRoles.remove(role);
	    role.setRoleId(Role.ROLE_MONITOR);
	    allRoles.remove(role);
	}
	if (!orgType.getOrganisationTypeId().equals(OrganisationType.COURSE_TYPE)) {
	    role.setRoleId(Role.ROLE_GROUP_MANAGER);
	    allRoles.remove(role);
	}
	return allRoles;
    }

    @Override
    public boolean hasRoleInOrganisation(User user, Integer roleId) {
	return hasRoleInOrganisation(user, roleId, getRootOrganisation());
    }

    @Override
    public boolean hasRoleInOrganisation(User user, Integer roleId, Organisation organisation) {
	if (roleDAO.getUserByOrganisationAndRole(user.getUserId(), roleId, organisation) != null) {
	    return true;
	} else {
	    return false;
	}
    }

    @Override
    public void deleteChildUserOrganisations(User user, Organisation org) {
	if (!org.getOrganisationType().getOrganisationTypeId().equals(OrganisationType.COURSE_TYPE)) {
	    return;
	}
	Set childOrgs = org.getChildOrganisations();
	Iterator iter = childOrgs.iterator();
	while (iter.hasNext()) {
	    Organisation childOrg = (Organisation) iter.next();
	    Set childOrgUos = childOrg.getUserOrganisations();
	    UserOrganisation uo = getUserOrganisation(user.getUserId(), childOrg.getOrganisationId());
	    if (uo != null) {
		// remove user's membership of this subgroup
		childOrgUos.remove(uo);
		childOrg.setUserOrganisations(childOrgUos);
		save(childOrg);
		// remove User's link to this subgroup
		Set userUos = user.getUserOrganisations();
		userUos.remove(uo);
		user.setUserOrganisations(userUos);
		log.debug("removed userId=" + user.getUserId() + " from orgId=" + childOrg.getOrganisationId());
	    }
	}
    }

    @Override
    public void deleteUserOrganisation(User user, Organisation org) {
	UserOrganisation uo = getUserOrganisation(user.getUserId(), org.getOrganisationId());
	if (uo != null) {
	    org.getUserOrganisations().remove(uo);
	    save(org);
	    user.getUserOrganisations().remove(uo);
	    log.debug("Removed user " + user.getUserId() + " from organisation " + org.getOrganisationId());
	    if (org.getOrganisationType().getOrganisationTypeId().equals(OrganisationType.COURSE_TYPE)) {
		deleteChildUserOrganisations(user, org);
	    }
	}
    }

    private Integer getRequestorId() {
	UserDTO userDTO = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
	return userDTO != null ? userDTO.getUserID() : null;
    }

    @Override
    public boolean isUserGlobalGroupManager() {
	Integer rootOrgId = getRootOrganisation().getOrganisationId();
	Integer requestorId = getRequestorId();
	return requestorId != null ? isUserInRole(requestorId, rootOrgId, Role.GROUP_MANAGER) : false;
    }

    @Override
    public boolean isUserSysAdmin() {
	return isUserSysAdmin(getRequestorId());
    }

    @Override
    public boolean isUserSysAdmin(Integer userId) {
	Integer rootOrgId = getRootOrganisation().getOrganisationId();
	return userId != null && isUserInRole(userId, rootOrgId, Role.SYSADMIN);
    }

    @Override
    public boolean isUserAppAdmin() {
	Integer rootOrgId = getRootOrganisation().getOrganisationId();
	Integer requestorId = getRequestorId();
	return requestorId != null ? isUserInRole(requestorId, rootOrgId, Role.APPADMIN) : false;
    }

    @Override
    public Integer getCountRoleForOrg(Integer orgId, Integer[] roleIds, String searchPhrase) {
	Integer count = roleDAO.getCountRoleForOrg(roleIds, orgId, searchPhrase);
	if (count != null) {
	    return count;
	} else {
	    return 0;
	}
    }

    @Override
    public Integer getCountRoleForOrg(Integer orgId, Integer roleId, String searchPhrase) {
	return getCountRoleForOrg(orgId, new Integer[] { roleId }, searchPhrase);
    }

    @Override
    public Theme getDefaultTheme() {
	String htmlName = Configuration.get(ConfigurationKeys.DEFAULT_THEME);
	List<Theme> list = findByProperty(Theme.class, "name", htmlName, true);
	return list != null ? list.get(0) : null;
    }

    @Override
    public void logPasswordChanged(User user, User modifiedBy) {
	String[] args = new String[1];
	args[0] = user.getLogin() + " (" + user.getUserId() + ")";
	String message = messageService.getMessage("audit.user.password.change", args);
	getLogEventService().logEvent(LogEvent.TYPE_PASSWORD_CHANGE, modifiedBy != null ? modifiedBy.getUserId() : null,
		user.getUserId(), null, null, message);
    }

    @Override
    public void logUserCreated(User user, User createdBy) {
	String[] args = new String[2];
	args[0] = user.getLogin() + "(" + user.getUserId() + ")";
	args[1] = user.getFullName();
	String message = messageService.getMessage("audit.user.create", args);
	getLogEventService().logEvent(LogEvent.TYPE_USER_ORG_ADMIN, createdBy != null ? createdBy.getUserId() : null,
		user.getUserId(), null, null, message);
    }

    @Override
    public void logUserCreated(User user, UserDTO createdBy) {
	String[] args = new String[2];
	args[0] = user.getLogin() + "(" + user.getUserId() + ")";
	args[1] = user.getFullName();
	String message = messageService.getMessage("audit.user.create", args);
	getLogEventService().logEvent(LogEvent.TYPE_USER_ORG_ADMIN, createdBy != null ? createdBy.getUserID() : null,
		user.getUserId(), null, null, message);
    }

    @Override
    public Integer getCountUsers() {
	String query = "SELECT count(u) FROM User u";
	return getFindIntegerResult(query);
    }

    @Override
    public int getCountUsers(String searchString) {
	return userDAO.getCountUsers(searchString);
    }

    @Override
    public Integer getCountUsers(Integer authenticationMethodId) {
	String query = "select count(u) from User u " + "where u.authenticationMethod.authenticationMethodId="
		+ authenticationMethodId;
	return getFindIntegerResult(query);
    }

    private Integer getFindIntegerResult(String query) {
	List list = baseDAO.find(query);
	if ((list != null) && (list.size() > 0)) {
	    return ((Number) list.get(0)).intValue();
	}
	return null;
    }

    @Override
    public List<OrganisationDTO> getActiveCoursesByUser(Integer userId, boolean isAppadmin, int page, int size,
	    String searchString) {
	return organisationDAO.getActiveCoursesByUser(userId, isAppadmin, page, size, searchString);
    }

    @Override
    public int getCountActiveCoursesByUser(Integer userId, boolean isAppadmin, String searchString) {
	return organisationDAO.getCountActiveCoursesByUser(userId, isAppadmin, searchString);
    }

    @Override
    public List<User> findUsers(String searchPhrase) {
	return userDAO.findUsers(searchPhrase);
    }

    @Override
    public List<User> findUsers(String searchPhrase, Integer filteredOrgId) {
	return userDAO.findUsers(searchPhrase, filteredOrgId);
    }

    @Override
    public List<User> findUsers(String searchPhrase, Integer orgId, Integer filteredOrgId) {
	return userDAO.findUsers(searchPhrase, orgId, filteredOrgId);
    }

    @Override
    public List<User> findUsers(String searchPhrase, Integer orgId, boolean includeChildOrgs) {
	return userDAO.findUsers(searchPhrase, orgId, includeChildOrgs);
    }

    @Override
    public List<User> getAllUsers() {
	return userDAO.getAllUsers();
    }

    @Override
    public List<UserDTO> getAllUsers(Integer page, Integer size, String sortBy, String sortOrder, String searchString) {
	return userDAO.getAllUsersPaged(page, size, sortBy, sortOrder, searchString);
    }

    @Override
    public List<UserDTO> getAllUsers(Integer organisationID, String[] roleNames, Integer page, Integer size,
	    String sortBy, String sortOrder, String searchString) {
	return userDAO.getAllUsersPaged(organisationID, roleNames, page, size, sortBy, sortOrder, searchString);
    }

    @Override
    public List<User> getAllUsers(Integer filteredOrgId) {
	return userDAO.findUsers(filteredOrgId);
    }

    @Override
    public List<User> getAllUsersWithEmail(String email) {
	return userDAO.findUsersWithEmail(email);
    }

    @Override
    public boolean canEditGroup(Integer userId, Integer orgId) {
	if (isUserAppAdmin() || isUserGlobalGroupManager()) {
	    return true;
	}
	Organisation org = (Organisation) findById(Organisation.class, orgId);
	if (org != null) {
	    Integer groupId = orgId;
	    if (org.getOrganisationType().getOrganisationTypeId().equals(OrganisationType.CLASS_TYPE)) {
		groupId = org.getParentOrganisation().getOrganisationId();
	    }
	    return isUserInRole(userId, groupId, Role.GROUP_MANAGER);
	}
	return false;
    }

    @Override
    public ForgotPasswordRequest getForgotPasswordRequest(String key) {
	List results = baseDAO.findByProperty(ForgotPasswordRequest.class, "requestKey", key);
	return results.isEmpty() ? null : (ForgotPasswordRequest) results.get(0);
    }

    @Override
    public int removeUserFromOtherGroups(Integer userId, Integer orgId) {
	List uos = userOrganisationDAO.userOrganisationsNotById(userId, orgId);
	deleteAll(uos);
	return uos.size();
    }

    @SuppressWarnings("unchecked")
    @Override
    public User getUserDTOByOpenidURL(String openidURL) {
	List<User> results = baseDAO.findByProperty(User.class, "openidURL", openidURL);
	return results.isEmpty() ? null : (User) results.get(0);
    }

    /**
     * Returns the SQL needed to look up portrait details for a given user. This is an efficient way to get the entries
     * at the same time as retrieving the tool data, rather than making a separate lookup.
     *
     * The return values are the entry for the select clause (will always have a leading space but no trailing comma and
     * an
     * alias of luser) and the sql join clause, which should go with any other join clauses.
     *
     * To convert the portrait id set up the sql -> java object translation using
     * addScalar("portraitId", IntegerType.INSTANCE)
     *
     * @param userIdString
     *            User identifier field string e.g. user.user_id
     * @return String[] { partial select string, join clause }
     *
     */
    @Override
    public String[] getPortraitSQL(String userIdString) {
	String[] retValue = new String[2];
	retValue[0] = ", BIN_TO_UUID(luser.portrait_uuid) portraitId ";
	retValue[1] = " JOIN lams_user luser ON luser.user_id = " + userIdString;
	return retValue;
    }

    /**
     * Looks for [login].jpg images in /tmp/portraits of user IDs within given range and starting with the given prefix
     */
    @Override
    public List<String> uploadPortraits(Integer minUserId, Integer maxUserId, String prefix) throws IOException {
	File tmpDir = new File("/tmp/portraits");
	if (!tmpDir.canRead()) {
	    throw new IOException("/tmp/portraits is not readable");
	}

	List<String> uploadedPortraits = new LinkedList<>();
	Integer prefixLength = StringUtils.isBlank(prefix) ? null : prefix.length() + 1;

	for (int userId = minUserId; userId <= maxUserId; userId++) {
	    try {
		User user = baseDAO.find(User.class, userId);
		if (user == null) {
		    if (log.isDebugEnabled()) {
			log.debug("User " + userId + " not found when batch uploading portraits, skipping");
		    }
		    continue;
		}
		String login = user.getLogin();
		if (prefixLength != null) {
		    if (!login.startsWith(prefix)) {
			if (log.isDebugEnabled()) {
			    log.debug("User " + userId + " login \"" + login
				    + "\" does not start with the required prefix \"" + prefix
				    + "\" when batch uploading portraits");
			}
			continue;
		    }
		    login = login.substring(prefixLength);
		}
		File portraitFile = new File(tmpDir, login + ".jpg");
		if (!portraitFile.canRead()) {
		    if (log.isDebugEnabled()) {
			log.debug("Portrait for user " + userId + " with login \"" + login
				+ "\" was not found or can not be read when batch uploading portraits");
			continue;
		    }
		}

		// upload to the content repository
		FileInputStream is = new FileInputStream(portraitFile);
		String fileNameWithoutExt = login;
		NodeKey originalFileNode = centralToolContentHandler.uploadFile(is,
			fileNameWithoutExt + "_original.jpg", "image/jpeg", true);
		is.close();
		log.debug("Saved original portrait with uuid: " + originalFileNode.getUuid() + " and version: "
			+ originalFileNode.getVersion());

		//resize to the large size
		is = new FileInputStream(portraitFile);
		InputStream modifiedPortraitInputStream = ResizePictureUtil.resize(is,
			CommonConstants.PORTRAIT_LARGEST_DIMENSION_LARGE);
		NodeKey node = centralToolContentHandler.updateFile(originalFileNode.getUuid(),
			modifiedPortraitInputStream, fileNameWithoutExt + "_large.jpg", "image/jpeg");
		modifiedPortraitInputStream.close();
		is.close();
		if (log.isDebugEnabled()) {
		    log.debug(
			    "Saved large portrait with uuid: " + node.getUuid() + " and version: " + node.getVersion());
		}

		//resize to the medium size
		is = new FileInputStream(portraitFile);
		modifiedPortraitInputStream = ResizePictureUtil.resize(is,
			CommonConstants.PORTRAIT_LARGEST_DIMENSION_MEDIUM);
		node = centralToolContentHandler.updateFile(node.getUuid(), modifiedPortraitInputStream,
			fileNameWithoutExt + "_medium.jpg", "image/jpeg");
		modifiedPortraitInputStream.close();
		is.close();
		if (log.isDebugEnabled()) {
		    log.debug("Saved medium portrait with uuid: " + node.getUuid() + " and version: "
			    + node.getVersion());
		}

		//resize to the small size
		is = new FileInputStream(portraitFile);
		modifiedPortraitInputStream = ResizePictureUtil.resize(is,
			CommonConstants.PORTRAIT_LARGEST_DIMENSION_SMALL);
		node = centralToolContentHandler.updateFile(node.getUuid(), modifiedPortraitInputStream,
			fileNameWithoutExt + "_small.jpg", "image/jpeg");
		modifiedPortraitInputStream.close();
		is.close();
		if (log.isDebugEnabled()) {
		    log.debug(
			    "Saved small portrait with uuid: " + node.getUuid() + " and version: " + node.getVersion());
		}
		// delete old portrait file (we only want to keep the user's current portrait)
		if (user.getPortraitUuid() != null) {
		    centralToolContentHandler.deleteFile(user.getPortraitUuid());
		}
		user.setPortraitUuid(UUID.fromString(originalFileNode.getPortraitUuid()));
		saveUser(user);

		log.info("Uploaded portrait for user " + userId + " with login \"" + login + "\"");
		uploadedPortraits.add(login);
	    } catch (Exception e) {
		log.error("Error while batch uploading portraits", e);
	    }
	}

	return uploadedPortraits;
    }

    // ---------------------------------------------------------------------
    // Inversion of Control Methods - Method injection
    // ---------------------------------------------------------------------

    /**
     * Set i18n MessageService
     */
    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }

    public void setBaseDAO(IBaseDAO baseDAO) {
	this.baseDAO = baseDAO;
    }

    public void setGroupDAO(IGroupDAO groupDAO) {
	this.groupDAO = groupDAO;
    }

    public void setRoleDAO(IRoleDAO roleDAO) {
	this.roleDAO = roleDAO;
    }

    public void setOrganisationDAO(IOrganisationDAO organisationDAO) {
	this.organisationDAO = organisationDAO;
    }

    public void setUserDAO(IUserDAO userDAO) {
	this.userDAO = userDAO;
    }

    public void setUserOrganisationDAO(IUserOrganisationDAO userOrganisationDAO) {
	this.userOrganisationDAO = userOrganisationDAO;
    }

    public void setFavoriteOrganisationDAO(IFavoriteOrganisationDAO favoriteOrganisationDAO) {
	this.favoriteOrganisationDAO = favoriteOrganisationDAO;
    }

    private ILogEventService getLogEventService() {
	if (UserManagementService.logEventService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getWebApplicationContext(SessionManager.getServletContext());
	    UserManagementService.logEventService = (ILogEventService) ctx.getBean("logEventService");
	}
	return UserManagementService.logEventService;
    }

    public void setCentralToolContentHandler(IToolContentHandler centralToolContentHandler) {
	this.centralToolContentHandler = centralToolContentHandler;
    }
}
