/**
 *
 */
package org.lamsfoundation.lams.tool.daco.dto;

import java.util.List;

import org.lamsfoundation.lams.tool.daco.model.DacoAnswer;

/**
 * Contains the user details that are useful for displaying summary in monitoring.
 * 
 * @author Marcin Cieslak
 *
 */
public class MonitoringSummaryUserDTO {

    private Long uid;
    private Integer userId;
    private String fullName;
    private String loginName;
    private List<List<DacoAnswer>> records;
    private Integer recordCount;

    public MonitoringSummaryUserDTO() {

    }

    public MonitoringSummaryUserDTO(Long uid, Integer userId, String fullName, String loginName) {
	this.uid = uid;
	this.userId = userId;
	this.fullName = fullName;
	this.loginName = loginName;
    }

    public String getFullName() {
	return fullName;
    }

    public void setFullName(String userFullName) {
	fullName = userFullName;
    }

    public String getLoginName() {
	return loginName;
    }

    public void setLoginName(String userLoginName) {
	loginName = userLoginName;
    }

    public List<List<DacoAnswer>> getRecords() {
	return records;
    }

    public void setRecords(List<List<DacoAnswer>> records) {
	this.records = records;
	if (records != null) {
	    recordCount = records.size();
	}
    }

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public Integer getRecordCount() {
	return recordCount;
    }

    public void setRecordCount(Integer recordCount) {
	this.recordCount = recordCount;
    }

    public Integer getUserId() {
	return userId;
    }

    public void setUserId(Integer userId) {
	this.userId = userId;
    }
}