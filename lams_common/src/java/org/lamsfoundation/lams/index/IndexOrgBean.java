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
package org.lamsfoundation.lams.index;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * @version
 *
 *          <p>
 *          <a href="OrgIndexBean.java.html"><i>View Source</i></a>
 *          </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 *
 *         Created at 10:01:23 on 14/06/2006
 */
public class IndexOrgBean implements Comparable {

    private Integer id;
    private String name;
    private Date archivedDate;
    private Integer type;
    private Boolean allowSorting = false;
    private List<IndexLinkBean> links;
    private List<IndexLinkBean> moreLinks;
    private List<IndexLessonBean> lessons;
    private List<IndexOrgBean> childIndexOrgBeans;

    public IndexOrgBean(Integer id, String name, Integer type) {
	this.id = id;
	this.name = name;
	this.type = type;
	this.links = new ArrayList<IndexLinkBean>();
	this.lessons = new ArrayList<IndexLessonBean>();
	this.childIndexOrgBeans = new ArrayList<IndexOrgBean>();
    }

    /**
     * @return Returns the childIndexOrgBeans.
     */
    public List<IndexOrgBean> getChildIndexOrgBeans() {
	return childIndexOrgBeans;
    }

    /**
     * @param childIndexOrgBeans
     *            The childIndexOrgBeans to set.
     */
    public void setChildIndexOrgBeans(List<IndexOrgBean> childIndexOrgBeans) {
	this.childIndexOrgBeans = childIndexOrgBeans;
    }

    /**
     * @return Returns the lessons.
     */
    public List<IndexLessonBean> getLessons() {
	return lessons;
    }

    /**
     * @param lessons
     *            The lessons to set.
     */
    public void setLessons(List<IndexLessonBean> lessons) {
	this.lessons = lessons;
    }

    /**
     * @return Returns the links.
     */
    public List<IndexLinkBean> getLinks() {
	return links;
    }

    /**
     * @param links
     *            The links to set.
     */
    public void setLinks(List<IndexLinkBean> links) {
	this.links = links;
    }

    /**
     * @return Returns the moreLinks
     */
    public List<IndexLinkBean> getMoreLinks() {
	return moreLinks;
    }

    /**
     *
     * @param moreLinks
     *            The moreLinks to set
     */
    public void setMoreLinks(List<IndexLinkBean> moreLinks) {
	this.moreLinks = moreLinks;
    }

    /**
     * @return Returns the id.
     */
    public Integer getId() {
	return id;
    }

    /**
     * @param id
     *            The id to set.
     */
    public void setId(Integer id) {
	this.id = id;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
	return name;
    }

    /**
     * @param name
     *            The name to set.
     */
    public void setName(String name) {
	this.name = name;
    }

    /**
     * @return Returns the type.
     */
    public Integer getType() {
	return type;
    }

    /**
     * @param type
     *            The type to set.
     */
    public void setType(Integer type) {
	this.type = type;
    }

    /**
     * @return Returns the archived date.
     */
    public Date getArchivedDate() {
	return archivedDate;
    }

    /**
     * @param type
     *            The archived date to set.
     */
    public void setArchivedDate(Date archivedDate) {
	this.archivedDate = archivedDate;
    }

    @Override
    public int compareTo(Object indexOrgBean) {
	IndexOrgBean b = (IndexOrgBean) indexOrgBean;
	return name.compareTo(b.getName());
    }

    @Override
    public boolean equals(Object other) {
	if (!(other instanceof IndexOrgBean)) {
	    return false;
	}
	IndexOrgBean castOther = (IndexOrgBean) other;
	return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }

    public void addLesson(IndexLessonBean lesson) {
	lessons.add(lesson);
    }

    public void addChildOrgBean(IndexOrgBean orgBean) {
	childIndexOrgBeans.add(orgBean);
    }

    public Boolean getAllowSorting() {
	return allowSorting;
    }

    public void setAllowSorting(Boolean allowSorting) {
	this.allowSorting = allowSorting;
    }

}
