/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.integration;

import org.lamsfoundation.lams.tool.Tool;

/**
 * Maps a tool adapter to multiple integrated server instances
 * 
 *
 */
public class ExtServerToolAdapterMap {

    private Long uid;
    private Tool tool;
    private ExtServerOrgMap extServer;

    public ExtServerToolAdapterMap() {
    }
    
    public ExtServerToolAdapterMap(Tool tool, ExtServerOrgMap extServer) {
	this.tool = tool;
	this.extServer = extServer;
    }

    /**
     *
     * 
     */
    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     *
     *
     * 
     */
    public Tool getTool() {
	return tool;
    }
    public void setTool(Tool tool) {
	this.tool = tool;
    }

    /**
     *
     *
     * 
     */
    public ExtServerOrgMap getExtServer() {
	return extServer;
    }

    public void setExtServer(ExtServerOrgMap extServer) {
	this.extServer = extServer;
    }
}
