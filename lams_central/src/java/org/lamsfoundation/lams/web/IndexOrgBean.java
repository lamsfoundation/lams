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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
package org.lamsfoundation.lams.web;

import java.util.ArrayList;
import java.util.List;

/**
 * @version
 *
 * <p>
 * <a href="OrgIndexBean.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 *
 * Created at 10:01:23 on 14/06/2006
 */
public class IndexOrgBean {
	
	private String name;
	private Integer type;
	private List<IndexLinkBean> links;
	private List<IndexLessonBean> lessons;
	private List<IndexOrgBean> childIndexOrgBeans;
	
	
	public IndexOrgBean(String name, Integer type) {
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
	 * @param childIndexOrgBeans The childIndexOrgBeans to set.
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
	 * @param lessons The lessons to set.
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
	 * @param links The links to set.
	 */
	public void setLinks(List<IndexLinkBean> links) {
		this.links = links;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
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
	 * @param type The type to set.
	 */
	public void setType(Integer type) {
		this.type = type;
	}

}
