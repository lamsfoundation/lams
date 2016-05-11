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


package org.lamsfoundation.lams.learning.export;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Models the portfolio for any type of activity, including tools, groupings, gates, optional and parallel activities
 *
 * @author mtruong, fmalikoff
 *
 */
public class ActivityPortfolio {

    private Long activityId;
    private String activityDescription;
    private String activityName;
    private String exportUrl;
    private List<ActivityPortfolio> childPortfolios;
    /* The link to the tool page from the main export page */
    private String toolLink;
    private boolean headingNoPage;
    private boolean floating;
    private Set<String> competencesCovered = new TreeSet<String>();

    /**
     * @return Returns the toolLink.
     */
    public String getToolLink() {
	return toolLink;
    }

    /**
     * @param toolLink
     *            The toolLink to set.
     */
    public void setToolLink(String toolLink) {
	this.toolLink = toolLink;
    }

    public ActivityPortfolio() {
	activityId = null;
	activityDescription = null;
	activityName = null;
	exportUrl = null;
	toolLink = null;
	childPortfolios = null;
	headingNoPage = false;
	floating = false;
    }

    /**
     * @return Returns the activityName.
     */
    public String getActivityName() {
	return activityName;
    }

    /**
     * @param activityName
     *            The activityName to set.
     */
    public void setActivityName(String activityName) {
	this.activityName = activityName;
    }

    /**
     * @return Returns the exportUrl.
     */
    public String getExportUrl() {
	return exportUrl;
    }

    /**
     * @param exportUrl
     *            The exportUrl to set.
     */
    public void setExportUrl(String exportUrl) {
	this.exportUrl = exportUrl;
    }

    /**
     * @return Returns the activityId.
     */
    public Long getActivityId() {
	return activityId;
    }

    /**
     * @param activityId
     *            The activityId to set.
     */
    public void setActivityId(Long activityId) {
	this.activityId = activityId;
    }

    /**
     * @return Returns the activityDescription.
     */
    public String getActivityDescription() {
	return activityDescription;
    }

    /**
     * @param activityDescription
     *            The activityDescription to set.
     */
    public void setActivityDescription(String activityDescription) {
	this.activityDescription = activityDescription;
    }

    /** Get the portfolios for any sub activities. Will return null for tool portfolios */
    public List<ActivityPortfolio> getChildPortfolios() {
	return childPortfolios;
    }

    public void setChildPortfolios(List<ActivityPortfolio> childPortfolios) {
	this.childPortfolios = childPortfolios;
    }

    /**
     * Generate just a heading for this portfolio, don't try to link to a page. This is used for Parallel and Sequence
     * activities in both learner and teacher modes, and for Branching in learner mode.
     *
     * @return
     */
    public boolean isHeadingNoPage() {
	return headingNoPage;
    }

    public void setHeadingNoPage(boolean headingNoPage) {
	this.headingNoPage = headingNoPage;
    }

    /**
     * Identifies this portfolio as of a Floating Activity
     *
     * @return
     */
    public boolean isFloating() {
	return floating;
    }

    public void setFloating(boolean floating) {
	this.floating = floating;
    }

    public Set<String> getCompetencesCovered() {
	return competencesCovered;
    }

    public void setCompetencesCovered(Set<String> competencesCovered) {
	this.competencesCovered = competencesCovered;
    }
}