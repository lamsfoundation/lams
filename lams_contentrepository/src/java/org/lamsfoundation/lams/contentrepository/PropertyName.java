/*
 * Created on Jan 5, 2005
 */
package org.lamsfoundation.lams.contentrepository;

/**
 * @author Fiona Malikoff
 *
 * Names of known properties, such as MIMETYPE, INITIALPATH
 */
public final class PropertyName {

	/** VERSIONDESC is text description relating to a version
	 * It should contain text meaningful to the user. Applicable to all node types. */
	public static final String VERSIONDESC = "VERSIONDESC";

	/** MIMETYPE is required for a file node - it is set by the call to add the file stream.*/
	public static final String MIMETYPE = "MIMETYPE";
	/** FILENAME is required for a file node  - it is set by the call to add the file stream. */
	public static final String FILENAME = "FILENAME";
	
	/** INITIALPATH is required for a package node */
	public static final String INITIALPATH = "INITIALPATH";

}
