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
 * Defines the types of nodes. If you add a new type to this file, 
 * make sure you add it to the isValidNodeType method.
 * 
 * @author Fiona Malikoff
 */
public final class NodeType {

	/** Node that has properties but no file. Not used at present */ 
	public static final String DATANODE = "DATANODE";

	/** Node has a file attached. Only nodes of type FILENODE can have
	 * files attached! */ 
	public static final String FILENODE = "FILENODE";

	/** Node represents a package of other nodes */
	public static final String PACKAGENODE = "PACKAGENODE";

	/** Does this string represent a known node type. */
	public static boolean isValidNodeType(String type) {
		if ( type != null  && 
				( type.equals(DATANODE) ||
				  type.equals(FILENODE) || type.equals(PACKAGENODE) ) ) {
			return true;
		}
		return false;
	}
}
