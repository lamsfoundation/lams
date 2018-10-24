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



package org.lamsfoundation.lams.tool.mindmap.util.xmlmodel;

/**
 * XML Model Class for Notify Response (responses from Server to Flash) in Mindmap
 *
 * @author Ruslan Kazakov
 */
public class NotifyResponseModel {
    private int ok; // 0 - false, 1 - true
    private Long id; // Request ID (Action ID)
    private Long data; // Node ID

    /** Default Constructor */
    public NotifyResponseModel() {
    }

    /** Full Constructor */
    public NotifyResponseModel(int ok, Long id, Long data) {
	setOk(ok);
	setId(id);
	setData(data);
    }

    /**
     * Sets the status of the Response
     * 
     * @param ok
     */
    public void setOk(int ok) {
	this.ok = ok;
    }

    /**
     * Returns status of the Response
     * 
     * @return ok
     */
    public int getOk() {
	return ok;
    }

    /**
     * Sets the id of the Request
     * 
     * @param id
     */
    public void setId(Long id) {
	this.id = id;
    }

    /**
     * Returns the id of the Request
     * 
     * @return id
     */
    public Long getId() {
	return id;
    }

    /**
     * Sets the id of the node
     * 
     * @param data
     *            the data to set
     */
    public void setData(Long data) {
	this.data = data;
    }

    /**
     * Returns the id of the Node
     * 
     * @return data
     */
    public Long getData() {
	return data;
    }
}
