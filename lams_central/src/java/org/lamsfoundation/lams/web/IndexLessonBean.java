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
package org.lamsfoundation.lams.web;

import java.util.List;

/**
 * @version
 *
 * <p>
 * <a href="IndexLessonBean.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 *
 * Created at 10:13:43 on 14/06/2006
 */
public class IndexLessonBean implements Comparable{
	private String name;
	private String description;
	private String url;
	private Integer state;
	private List<IndexLinkBean> links;

	public IndexLessonBean(String name, String url) {
		this.name = name;
		this.url = url;
	}
	
	public IndexLessonBean(String name, String description, String url, Integer state, List<IndexLinkBean> links) {
		this.name = name;
		this.description = description;
		this.url = url;
		this.state = state;
		this.links = links;
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
	public int compareTo(Object o) {
		return name.compareTo(((IndexLessonBean)o).getName());
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
}
