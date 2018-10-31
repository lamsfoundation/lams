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


package org.lamsfoundation.lams.tool.forum.util;

/**
 * User: conradb
 * Date: 14/06/2005
 * Time: 12:33:12
 */
public class ForumException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -7408922611270822369L;

    public ForumException(String message) {
	super(message);
    }

    public ForumException(String message, Throwable cause) {
	super(message, cause);
    }

    public ForumException() {
	super();

    }

    public ForumException(Throwable cause) {
	super(cause);

    }

}
