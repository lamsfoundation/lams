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

package org.lamsfoundation.lams.contentrepository;

/**
 * Main exception thrown by content repository classes. All exceptions thrown by the content 
 * repository (except RepositoryRuntimeException) are based on this class, so calling code 
 * can catch this exception and catch all of the repository exceptions, if it doesn't want to 
 * catch particular exceptions.
 * 
 * @see RepositoryRuntimeException
 */
public class RepositoryCheckedException extends Exception {

    /**
	* Constructs a new instance of this class.
	*/
    public RepositoryCheckedException() {
    	this("Content Repository Error.");
	}
	
    /**
	* Constructs a new instance of this class given a message describing the
	* failure cause.
	*
	* @param s description
	*/
	public RepositoryCheckedException(String s) {
		super(s);
	}
	
   /**
	* Constructs a new instance of this class given a message describing the
	* failure and a root throwable.
	*
	* @param s description
	* @param cause root throwable cause
	*/
   public RepositoryCheckedException(String s, Throwable cause) {
   		super(s,cause);
	
   }
	
   /**
	* Constructs a new instance of this class given a root throwable.
	*
	* @param cause root failure cause
	*/
   public RepositoryCheckedException(Throwable cause) {
		this("Content Repository Error.", cause);
	   }
	
	
    public String getMessage() {
		
		String s1 = super.getMessage();
		if ( s1 == null) {
			s1 = "";
		}

		Throwable cause = getCause();
	    String s2 = cause != null ? cause.getMessage() : null ;
		return s2 != null ? s1+":"+s2 : s1;

    }

}
