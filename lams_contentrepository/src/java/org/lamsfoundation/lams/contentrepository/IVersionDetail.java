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
