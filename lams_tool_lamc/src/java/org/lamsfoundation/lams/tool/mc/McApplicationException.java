/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.mc;


/**
 * <p>This exception wraps all basic exception occured in the mcq tool. It is 
 * not suppose to be try and catched in any level. The struts should be taking
 * care of handling this exception.</p>
 * 
* @author Ozgur Demirtas
 * 
 */
public class McApplicationException extends RuntimeException
{
    /**
     * Default Constructor
     */
    public McApplicationException()
    {
        super();
    }

    /**
     * Constructor for customized error message
     * @param message
     */
    public McApplicationException(String message)
    {
        super(message);
    }

    /**
     * Constructor for wrapping the throwable object
     * @param cause
     */
    public McApplicationException(Throwable cause)
    {
        super(cause);
    }

    /**
     * Constructor for wrapping both the customized error message and 
     * throwable exception object.
     * @param message
     * @param cause
     */
    public McApplicationException(String message, Throwable cause)
    {
        super(message, cause);
    }

}
