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

import java.util.Date;

/**
 * Describes the version of a node. Objects that meet this interface
 * have a natural ordering so they can be placed straight into
 * a sorted set, with the need for a special comparator. The natural
 * ordering should be such that null is sorted (ie after) non-null.
 * 
 * @author Fiona Malikoff
 */
public interface IVersionDetail extends Comparable {

	/** Get the version id. This will be a number greater than 0. 
	 * @return version id
	 */
	public Long getVersionId();

	/** Get the date/time of when this version was created. 
	 * @return date/time stamp of creation
	 */
	public Date getCreatedDateTime();

	/** Get the general text string describing the version. 
	 * @return version description
	 */
	public String getDescription();
}
