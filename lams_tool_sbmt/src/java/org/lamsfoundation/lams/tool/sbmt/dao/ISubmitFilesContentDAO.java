/*
 * Created on May 30, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.sbmt.dao;

import org.lamsfoundation.lams.dao.IBaseDAO;
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

	/**
	 * Save the given content. If the content existed, then update the old
	 * content by new given content.
	 *  
	 * @param content
	 */
	public void saveOrUpdate(SubmitFilesContent content);

	public void deleteInstructionFile(Long contentID, Long uuid, Long versionID, String type);	
	
}
