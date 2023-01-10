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

package org.lamsfoundation.lams.learningdesign.dto;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Display bean for an activity. Holds the URL used to display the activity along with title, description and a
 * completion flag.
 *
 * If used for the learner progress screen, the defaultURL will always be false.
 *
 * @author daveg
 *
 */
public class ActivityURL {
    private String type;
    private Long activityId;
    private String url;
    private String iconURL;
    private String title;
    private String description;
    private boolean complete;
    private boolean floating;
    private byte status;
    private boolean defaultURL;
    private Long duration;
    private List<ActivityURL> childActivities;

    public ActivityURL() {
    }

    public ActivityURL(Long activityId, String url) {
	this.activityId = activityId;
	this.url = url;
	this.floating = false;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public boolean isComplete() {
	return complete;
    }

    public void setComplete(boolean complete) {
	this.complete = complete;
    }

    public boolean isFloating() {
	return floating;
    }

    public boolean getFloating() {
	return floating;
    }

    public void setFloating(boolean floating) {
	this.floating = floating;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public Long getActivityId() {
	return activityId;
    }

    public void setActivityId(Long activityId) {
	this.activityId = activityId;
    }

    public String getUrl() {
	return url;
    }

    public void setUrl(String url) {
	this.url = url;
    }

    public String getIconURL() {
	return iconURL;
    }

    public void setIconURL(String iconURL) {
	this.iconURL = iconURL;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public boolean isDefaultURL() {
	return defaultURL;
    }

    public void setDefaultURL(boolean defaultURL) {
	this.defaultURL = defaultURL;
    }

    public byte getStatus() {
	return status;
    }

    public void setStatus(byte status) {
	this.status = status;
    }

    public Long getDuration() {
	return duration;
    }

    public void setDuration(Long duration) {
	this.duration = duration;
    }

    /** Get a list of the urls for the child activities. Only used on the jsp progress display page */
    public List<ActivityURL> getChildActivities() {
	return childActivities;
    }

    public void setChildActivities(List<ActivityURL> childActivities) {
	this.childActivities = childActivities;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("activityId", activityId).append("title", title).append("url", url)
		.toString();
    }
}