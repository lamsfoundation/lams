package org.lamsfoundation.lams.tool.sbmt;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 * @hibernate.class table="tl_lasbmt11_report"
*/
public class SubmitFilesReport implements Serializable {

    /** identifier field */
    private Long reportID;

    /** persistent field */
    private String comments;

    /** nullable persistent field */
    private Long marks;

    /** nullable persistent field */
    private Date dateMarksReleased;
    
    /** persistent field */
    private SubmissionDetails submissionDetails;    

    /** full constructor */
    public SubmitFilesReport(String comments, Long marks, Date dateMarksReleased, SubmissionDetails submissionDetails) {
        this.comments = comments;
        this.marks = marks;
        this.dateMarksReleased = dateMarksReleased;        
        this.submissionDetails = submissionDetails;
    }

    /** default constructor */
    public SubmitFilesReport() {
    }

    /** minimal constructor */
    public SubmitFilesReport(SubmissionDetails submissionDetails) {        
        this.submissionDetails = submissionDetails;
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

    /** 
     * @hibernate.many-to-one not-null="true" 
     * @hibernate.column name="submission_id"
     */
    public SubmissionDetails getSubmissionDetails() {
        return this.submissionDetails;
    }

    public void setSubmissionDetails(SubmissionDetails submissionDetails) {
        this.submissionDetails = submissionDetails;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("reportID", getReportID())
            .append("comments", getComments())
            .append("marks", getMarks())
            .append("dateMarksReleased", getDateMarksReleased())   
            .toString();
    }    
}
