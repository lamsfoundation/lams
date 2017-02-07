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

/**
 * @version
 *
 *          <p>
 *          <a href="IndexLinkBean.java.html"><i>View Source</i></a>
 *          </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 *
 *         Created at 10:12:09 on 14/06/2006
 */
public class IndexLinkBean {

    private String name;
    private String url;
    private String style;
    private String tooltip;
    private String id;

    public IndexLinkBean(String name, String url, String style, String tooltip) {
	super();
	this.name = name;
	this.url = url;
	this.style = style;
	this.tooltip = tooltip;
	this.id = name != null ? name.replace('.', '-') : null;
    }

    public IndexLinkBean(String name, String url) {
	this.name = name;
	this.url = url;
	this.id = name != null ? name.replace('.', '-') : null;
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
	this.id = name != null ? name.replace('.', '-') : null;
    }

    /**
     * @return Returns the url.
     */
    public String getUrl() {
	return url;
    }

    /**
     * @param url
     *            The url to set.
     */
    public void setUrl(String url) {
	this.url = url;
    }

    public String getStyle() {
	return style;
    }

    public void setStyle(String style) {
	this.style = style;
    }

    public String getTooltip() {
	return tooltip;
    }

    public void setTooltip(String tooltip) {
	this.tooltip = tooltip;
    }

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

}
