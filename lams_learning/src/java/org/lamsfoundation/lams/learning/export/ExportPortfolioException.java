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

/*
 * Created on Sep 6, 2005
 *
 */
package org.lamsfoundation.lams.learning.export;

/**
 * @author mtruong
 *
 */
public class ExportPortfolioException extends java.lang.RuntimeException {
	
	public ExportPortfolioException()
	{		
	}
	
	public ExportPortfolioException(String msg)
	{
		super(msg);
	}
	
	/**
     * Constructor for wrapping the throwable object
     * @param cause
     */
    public ExportPortfolioException(Throwable cause)
    {
        super(cause);
    }

    /**
     * Constructor for wrapping both the customized error message and 
     * throwable exception object.
     * @param message
     * @param cause
     */
    public ExportPortfolioException(String message, Throwable cause)
    {
        super(message, cause);
    }

}
