package org.lamsfoundation.lams.statistics.service;

import java.util.Map;

import org.lamsfoundation.lams.statistics.dto.GroupStatisticsDTO;
import org.lamsfoundation.lams.statistics.dto.StatisticsDTO;

public interface IStatisticsService {

    /**
     * Get the overall statistics for the server
     * 
     * @return
     */
    public StatisticsDTO getOverallStatistics();

    /**
     * Get the statistics for the specified group
     * 
     * @param orgId
     * @return
     * @throws Exception
     */
    public GroupStatisticsDTO getGroupStatisticsDTO(Integer orgId) throws Exception;

    /**
     * Get a map of orgname,orgid for the drop down menu
     * 
     * @return
     */
    public Map<String, Integer> getGroupMap();
}
