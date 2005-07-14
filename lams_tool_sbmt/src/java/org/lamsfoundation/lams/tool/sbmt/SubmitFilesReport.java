package org.lamsfoundation.lams.tool.sbmt;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;


/** 
 * @hibernate.class table="tl_lasbmt11_report"
*/
public class SubmitFilesReport implements Serializable,Cloneable{
	private static Logger log = Logger.getLogger(SubmitFilesReport.class);
	
    /** identifier field */
    private Long reportID;

    /** persistent field */
    private String comments;

    /** nullable persistent field */
    private Long marks;

    /** nullable persistent field */
    private Date dateMarksReleased;
    
    /** full constructor */
    public SubmitFilesReport(String comments, Long marks, Date dateMarksReleased) {
        this.comments = comments;
        this.marks = marks;
        this.dateMarksReleased = dateMarksReleased;
    }

    /** default constructor */
    public SubmitFilesReport() {
	}
    /** 
     * @hibernate.id generator-class="identity" type="java.lang.Long" column="report_id"
     */
    public Long getReportID() {
        return this.reportID;
    }

    public void setReportID(Long reportID) {
        this.reportID = reportID;
    }

    /** 
     * @hibernate.property column="comments" length="250" 
     */
    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    /** 
     * @hibernate.property column="marks" length="20"
     */
    public Long getMarks() {
        return this.marks;
    }

    public void setMarks(Long marks) {
        this.marks = marks;
    }

    /** 
     * @hibernate.property column="date_marks_released" length="19" 
     */
    public Date getDateMarksReleased() {
        return this.dateMarksReleased;
    }

    public void setDateMarksReleased(Date dateMarksReleased) {
        this.dateMarksReleased = dateMarksReleased;
    }  
    public String toString() {
        return new ToStringBuilder(this)
            .append("reportID", getReportID())
            .append("comments", getComments())
            .append("marks", getMarks())
            .append("dateMarksReleased", getDateMarksReleased())   
            .toString();
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		
		Object obj = null;
		try {
			obj = super.clone();
		} catch (CloneNotSupportedException e) {
			log.error("When clone " + SubmitFilesReport.class + " failed");
		}
		return obj;
	}

	public boolean equals(Object other) {
	    if ( (this == other ) ) return true;
	    if ( !(other instanceof SubmitFilesReport) ) return false;
	    SubmitFilesReport castOther = (SubmitFilesReport) other;
	    return new EqualsBuilder()
	        .append(this.getReportID(), castOther.getReportID())
	        .append(this.getComments(), castOther.getComments())
	        .append(this.getMarks(), castOther.getMarks())
	        .append(this.getDateMarksReleased(), castOther.getDateMarksReleased())
	        .isEquals();
	}

	public int hashCode() {
	    return new HashCodeBuilder()
	        .append(getReportID())
	        .append(getComments())
	        .append(getMarks())
	        .append(getDateMarksReleased())
	        .toHashCode();
	}
}
