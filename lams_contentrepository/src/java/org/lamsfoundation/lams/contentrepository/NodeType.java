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
