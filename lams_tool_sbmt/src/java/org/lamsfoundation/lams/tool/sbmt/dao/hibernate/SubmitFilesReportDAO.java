/*
 * Created on May 30, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.sbmt.dao.hibernate;

import java.util.List;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesReport;
import org.lamsfoundation.lams.tool.sbmt.dao.ISubmitFilesReportDAO;

/**
 * @author Manpreet Minhas
 */
public class SubmitFilesReportDAO extends BaseDAO implements
		ISubmitFilesReportDAO {
	
	private static final String TABLENAME ="tl_lasbmt11_report";
	private static final String FIND_BY_SUBMISSION = "from " + TABLENAME +
											   " in class " + SubmitFilesReport.class.getName() + 
											   " where submission_id=?" ;

	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.sbmt.dao.ISubmitFilesReportDAO#getReportByID(java.lang.Long)
	 */
	public SubmitFilesReport getReportByID(Long reportID) {
		return (SubmitFilesReport)super.find(SubmitFilesReport.class,reportID);
	}
	
	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.sbmt.dao.ISubmitFilesReportDAO#getReportBySubmissionID(java.lang.Long)
	 */
	public SubmitFilesReport getReportBySubmissionID(Long submissionID){
		
		List list = this.getHibernateTemplate().find(FIND_BY_SUBMISSION,submissionID);
		if(list!=null)
			return (SubmitFilesReport)list.get(0);
		else
			return null;		
	}	

}
