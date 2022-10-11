package org.lamsfoundation.lams.statistics.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.statistics.dto.GroupStatisticsDTO;
import org.lamsfoundation.lams.statistics.dto.StatisticsDTO;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;

public class StatisticsService implements IStatisticsService {

    private IBaseDAO baseDAO;
    private IUserManagementService userService;

    /*
     * (non-Javadoc)
     *
     * @see org.lamsfoundation.lams.statistics.service.IStatisticsService#getOverallStatistics()
     */
    @Override
    public StatisticsDTO getOverallStatistics() {

	StatisticsDTO statisticsDTO = new StatisticsDTO();

	// Counting the organisations
	HashMap<String, Object> groupMap = new HashMap<>();
	groupMap.put("organisationType.organisationTypeId", OrganisationType.COURSE_TYPE);
	statisticsDTO.setGroups(baseDAO.countByProperties(Organisation.class, groupMap));

	// Counting the sub-organisations
	HashMap<String, Object> subGroupMap = new HashMap<>();
	subGroupMap.put("organisationType.organisationTypeId", OrganisationType.CLASS_TYPE);
	statisticsDTO.setSubGroups(baseDAO.countByProperties(Organisation.class, subGroupMap));

	// Getting the rest of the counts
	statisticsDTO.setActivities(baseDAO.countAll(Activity.class));
	List<Number> completedActivitiesCount = baseDAO.findNative("SELECT COUNT(*) FROM lams_progress_completed");
	if (!completedActivitiesCount.isEmpty()) {
	    statisticsDTO.setCompletedActivities(completedActivitiesCount.get(0).longValue());
	}
	statisticsDTO.setLessons(baseDAO.countAll(Lesson.class));
	statisticsDTO.setSequences(baseDAO.countAll(LearningDesign.class));
	statisticsDTO.setUsers(baseDAO.countAll(User.class));
	return statisticsDTO;

    }

    /*
     * (non-Javadoc)
     *
     * @see org.lamsfoundation.lams.statistics.service.IStatisticsService#getGroupStatisticsDTO(java.lang.Integer)
     */
    @Override
    public GroupStatisticsDTO getGroupStatisticsDTO(Integer orgId) throws Exception {

	Organisation group = baseDAO.find(Organisation.class, orgId);
	GroupStatisticsDTO groupStats = new GroupStatisticsDTO();
	if (group != null) {

	    groupStats.setName(group.getName());
	    groupStats.setLessons(group.getLessons().size());
	    groupStats.setTotalUsers(userService.getAllUsers(group.getOrganisationId()).size());
	    groupStats.setAuthors(
		    userService.getUsersFromOrganisationByRole(group.getOrganisationId(), Role.AUTHOR, false).size());
	    groupStats.setMonitors(
		    userService.getUsersFromOrganisationByRole(group.getOrganisationId(), Role.MONITOR, false).size());
	    groupStats.setLearners(
		    userService.getUsersFromOrganisationByRole(group.getOrganisationId(), Role.LEARNER, false).size());

	    Set<Organisation> subGroups = group.getChildOrganisations();

	    ArrayList<GroupStatisticsDTO> subGroupStatsList = new ArrayList<>();
	    if (subGroups != null) {
		for (Organisation subGroup : subGroups) {
		    GroupStatisticsDTO subGroupStats = new GroupStatisticsDTO();
		    subGroupStats.setName(subGroup.getName());
		    subGroupStats.setLessons(subGroup.getLessons().size());
		    subGroupStats.setTotalUsers(userService.getAllUsers(subGroup.getOrganisationId()).size());
		    subGroupStats.setAuthors(userService
			    .getUsersFromOrganisationByRole(subGroup.getOrganisationId(), Role.AUTHOR, false).size());
		    subGroupStats.setMonitors(userService
			    .getUsersFromOrganisationByRole(subGroup.getOrganisationId(), Role.MONITOR, false).size());
		    subGroupStats.setLearners(userService
			    .getUsersFromOrganisationByRole(subGroup.getOrganisationId(), Role.LEARNER, false).size());
		    subGroupStatsList.add(subGroupStats);
		}
	    }
	    groupStats.setSubGroups(subGroupStatsList);

	} else {
	    throw new Exception("Tried to fetch data for null group with id: " + orgId);
	}

	return groupStats;
    }

    @Override
    public Map<String, Integer> getGroupMap() {
	Map groupMap = new HashMap<String, Integer>();

	List<Organisation> groups = userService.findByProperty(Organisation.class,
		"organisationType.organisationTypeId", OrganisationType.COURSE_TYPE);
	if (groups != null) {
	    for (Organisation group : groups) {
		groupMap.put(group.getName(), group.getOrganisationId());
	    }
	}
	return groupMap;
    }

    public void setBaseDAO(IBaseDAO baseDAO) {
	this.baseDAO = baseDAO;
    }

    public void setUserService(IUserManagementService userService) {
	this.userService = userService;
    }
}
