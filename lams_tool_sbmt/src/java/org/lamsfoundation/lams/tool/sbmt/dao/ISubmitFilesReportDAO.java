/*
 * Created on May 30, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.sbmt.dao;


import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesReport;

/**
 * @author Manpreet Minhas
 */
public interface ISubmitFilesReportDAO extends IBaseDAO {
	
	/**
	 * Returns the report specific to the given 
	 * <code>reportID</code>
	 * 
	 * @param reportID The reportID to be looked up
	 * @return SubmitFilesReport The required populated object
	 */
	public SubmitFilesReport getReportByID(Long reportID);
		
	/**
	 * Returns the record corresponding to the given 
	 * <code>submissionID</code>
	 * 
	 * @param submissionID
	 * @return SubmitFilesReport The required object
	 */
	public SubmitFilesReport getReportBySubmissionID(Long submissionID);

	/**
	 * @param report
	 */
	public void updateReport(SubmitFilesReport report);

}
