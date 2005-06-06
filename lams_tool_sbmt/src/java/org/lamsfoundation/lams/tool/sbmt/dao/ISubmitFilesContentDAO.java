/*
 * Created on May 30, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.sbmt.dao;

import org.lamsfoundation.lams.learningdesign.dao.IBaseDAO;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesContent;

/**
 * @author Manpreet Minhas
 */
public interface ISubmitFilesContentDAO extends IBaseDAO{
	
	/**
	 * Returns the content corresponding to the given
	 * <code>contentID</code>
	 * 
	 * @param contentID The contentID to be looked up
	 * @return SubmitFilesContent The required populated object
	 */
	public SubmitFilesContent getContentByID(Long contentID);	
	
}
