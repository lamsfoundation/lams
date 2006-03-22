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

package org.lamsfoundation.lams.tool.rsrc.ims;

/**
 * This exception is thrown when an error occurs that may be related to
 * the formatting of the IMS content package. e.g. the manifest file 
 * is missing.
 *  
 * @author Fiona Malikoff
 */
public class IMSManifestException extends ImscpApplicationException {

	/**
	 * 
	 */
	public IMSManifestException() {
		super();
	}

	/**
	 * @param arg0
	 */
	public IMSManifestException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public IMSManifestException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public IMSManifestException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
