/*
 * Created on May 30, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.sbmt.dao.hibernate;

import org.lamsfoundation.lams.learningdesign.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesContent;
import org.lamsfoundation.lams.tool.sbmt.dao.ISubmitFilesContentDAO;

/**
 * @author Manpreet Minhas
 */
public class SubmitFilesContentDAO extends BaseDAO implements ISubmitFilesContentDAO {

	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.sbmt.dao.ISubmitFilesContentDAO#getContentByID(java.lang.Long)
	 */
	public SubmitFilesContent getContentByID(Long contentID) {
		return (SubmitFilesContent) super.find(SubmitFilesContent.class,contentID);
	}
}
