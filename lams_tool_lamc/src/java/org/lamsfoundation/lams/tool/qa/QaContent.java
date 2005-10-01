package org.lamsfoundation.lams.tool.qa;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.SortedSet;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class QaContent implements Serializable {

    /** identifier field */
    private Long qaContentId;

    /** nullable persistent field */
    private String title;

    /** nullable persistent field */
    private String instructions;

    /** nullable persistent field */
    private boolean defineLater;

    /** nullable persistent field */
    private boolean runOffline;

    /** nullable persistent field */
    private String creationDate;

    /** nullable persistent field */
    private Date updateDate;

    /** nullable persistent field */
    private boolean questionsSequenced;

    /** nullable persistent field */
    private boolean usernameVisible;

    /** nullable persistent field */
    private String reportTitle;

    /** nullable persistent field */
    private String monitoringReportTitle;

    /** nullable persistent field */
    private long createdBy;

    /** nullable persistent field */
    private boolean synchInMonitor;

    /** nullable persistent field */
    private boolean contentLocked;

    /** nullable persistent field */
    private String offlineInstructions;

    /** nullable persistent field */
    private String onlineInstructions;

    /** nullable persistent field */
    private String endLearningMessage;

    /** persistent field */
    private SortedSet qaQueContents;

    /** persistent field */
    private Set qaSessions;

    /** persistent field */
    private Set qaUploadedFiles;

    /** full constructor */
    public QaContent(Long qaContentId, String title, String instructions, boolean defineLater, boolean runOffline, String creationDate, Date updateDate, boolean questionsSequenced, boolean usernameVisible, String reportTitle, String monitoringReportTitle, long createdBy, boolean synchInMonitor, boolean contentLocked, String offlineInstructions, String onlineInstructions, String endLearningMessage, SortedSet qaQueContents, Set qaSessions, Set qaUploadedFiles) {
        this.qaContentId = qaContentId;
        this.title = title;
        this.instructions = instructions;
        this.defineLater = defineLater;
        this.runOffline = runOffline;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
        this.questionsSequenced = questionsSequenced;
        this.usernameVisible = usernameVisible;
        this.reportTitle = reportTitle;
        this.monitoringReportTitle = monitoringReportTitle;
        this.createdBy = createdBy;
        this.synchInMonitor = synchInMonitor;
        this.contentLocked = contentLocked;
        this.offlineInstructions = offlineInstructions;
        this.onlineInstructions = onlineInstructions;
        this.endLearningMessage = endLearningMessage;
        this.qaQueContents = qaQueContents;
        this.qaSessions = qaSessions;
        this.qaUploadedFiles = qaUploadedFiles;
    }

    /** default constructor */
    public QaContent() {
    }

    /** minimal constructor */
    public QaContent(Long qaContentId, SortedSet qaQueContents, Set qaSessions, Set qaUploadedFiles) {
        this.qaContentId = qaContentId;
        this.qaQueContents = qaQueContents;
        this.qaSessions = qaSessions;
        this.qaUploadedFiles = qaUploadedFiles;
    }

    public Long getQaContentId() {
        return this.qaContentId;
    }

    public void setQaContentId(Long qaContentId) {
        this.qaContentId = qaContentId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInstructions() {
        return this.instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public boolean isDefineLater() {
        return this.defineLater;
    }

    public void setDefineLater(boolean defineLater) {
        this.defineLater = defineLater;
    }

    public boolean isRunOffline() {
        return this.runOffline;
    }

    public void setRunOffline(boolean runOffline) {
        this.runOffline = runOffline;
    }

    public String getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public Date getUpdateDate() {
        return this.updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public boolean isQuestionsSequenced() {
        return this.questionsSequenced;
    }

    public void setQuestionsSequenced(boolean questionsSequenced) {
        this.questionsSequenced = questionsSequenced;
    }

    public boolean isUsernameVisible() {
        return this.usernameVisible;
    }

    public void setUsernameVisible(boolean usernameVisible) {
        this.usernameVisible = usernameVisible;
    }

    public String getReportTitle() {
        return this.reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    public String getMonitoringReportTitle() {
        return this.monitoringReportTitle;
    }

    public void setMonitoringReportTitle(String monitoringReportTitle) {
        this.monitoringReportTitle = monitoringReportTitle;
    }

    public long getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }

    public boolean isSynchInMonitor() {
        return this.synchInMonitor;
    }

    public void setSynchInMonitor(boolean synchInMonitor) {
        this.synchInMonitor = synchInMonitor;
    }

    public boolean isContentLocked() {
        return this.contentLocked;
    }

    public void setContentLocked(boolean contentLocked) {
        this.contentLocked = contentLocked;
    }

    public String getOfflineInstructions() {
        return this.offlineInstructions;
    }

    public void setOfflineInstructions(String offlineInstructions) {
        this.offlineInstructions = offlineInstructions;
    }

    public String getOnlineInstructions() {
        return this.onlineInstructions;
    }

    public void setOnlineInstructions(String onlineInstructions) {
        this.onlineInstructions = onlineInstructions;
    }

    public String getEndLearningMessage() {
        return this.endLearningMessage;
    }

    public void setEndLearningMessage(String endLearningMessage) {
        this.endLearningMessage = endLearningMessage;
    }

    public SortedSet getQaQueContents() {
        return this.qaQueContents;
    }

    public void setQaQueContents(SortedSet qaQueContents) {
        this.qaQueContents = qaQueContents;
    }

    public Set getQaSessions() {
        return this.qaSessions;
    }

    public void setQaSessions(Set qaSessions) {
        this.qaSessions = qaSessions;
    }

    public Set getQaUploadedFiles() {
        return this.qaUploadedFiles;
    }

    public void setQaUploadedFiles(Set qaUploadedFiles) {
        this.qaUploadedFiles = qaUploadedFiles;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("qaContentId", getQaContentId())
            .toString();
    }

}
