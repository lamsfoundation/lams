/* 
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
USA

http://www.gnu.org/licenses/gpl.txt 
*/
/* $$Id$$ */
package org.lamsfoundation.lams.tool.deploy;

/**
 * Exception thrown by deployment process.
 * @author chris
 */
public class DeployException extends java.lang.RuntimeException
{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 4637002190190219303L;

	/**
     * Creates a new instance of <code>DeployException</code> without detail message.
     */
    public DeployException()
    {
    }
    
    
    /**
     * Constructs an instance of <code>DeployException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public DeployException(String msg)
    {
        super(msg);
    }
    
    public DeployException(String msg, Throwable cause)
    {
        super(msg, cause);
    }
    
    public DeployException(Throwable cause)
    {
        super(cause);
    }
}
