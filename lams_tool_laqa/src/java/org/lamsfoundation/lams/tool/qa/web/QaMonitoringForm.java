/*
 * ozgurd
 * Created on 26/04/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.qa.web;

/**
 * ActionForm for the Monitoring environment
 */
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;

/**
 * @author ozgurd
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class QaMonitoringForm extends ActionForm implements QaAppConstants {
	static Logger logger = Logger.getLogger(QaMonitoringForm.class.getName());

	/**
	 * these buttons  are not used in the deployment
	 */
	protected String startLesson;
	protected String deleteLesson;
	protected String forceComplete;
	
	/**
	 * buttons
	 */
	protected String editReport;
	protected String updateReport;
	protected String hideReport;
	protected String unhideReport;
	protected String submitMonitoringInstructions;
	
	protected String summary;
	protected String instructions;
	protected String editActivity;
	protected String stats;
	
	protected String responseId;
	protected String hiddenResponseId;
	protected String unHiddenResponseId;
	protected String updatedResponse;
	
	protected String offlineInstructions;
	protected String onlineInstructions;
	
	public void resetUserAction()
    {
		this.startLesson=null;
		this.deleteLesson=null;
		this.forceComplete=null;
		
		this.summary=null;
		this.instructions=null;
		this.editActivity=null;
		this.stats=null;
		
		this.editReport=null;
		this.updateReport=null;
		this.hideReport=null;
		this.unhideReport=null;
		
		this.offlineInstructions=null;
		this.onlineInstructions=null;
	}
	/**
	 * @return Returns the deleteLesson.
	 */
	public String getDeleteLesson() {
		return deleteLesson;
	}
	/**
	 * @param deleteLesson The deleteLesson to set.
	 */
	public void setDeleteLesson(String deleteLesson) {
		this.deleteLesson = deleteLesson;
	}
	/**
	 * @return Returns the startLesson.
	 */
	public String getStartLesson() {
		return startLesson;
	}
	/**
	 * @param startLesson The startLesson to set.
	 */
	public void setStartLesson(String startLesson) {
		this.startLesson = startLesson;
	}
	/**
	 * @return Returns the forceComplete.
	 */
	public String getForceComplete() {
		return forceComplete;
	}
	/**
	 * @param forceComplete The forceComplete to set.
	 */
	public void setForceComplete(String forceComplete) {
		this.forceComplete = forceComplete;
	}
	/**
	 * @return Returns the summary.
	 */
	public String getSummary() {
		return summary;
	}
	/**
	 * @param summary The summary to set.
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}
	/**
	 * @return Returns the instructions.
	 */
	public String getInstructions() {
		return instructions;
	}
	/**
	 * @param instructions The instructions to set.
	 */
	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}
	/**
	 * @return Returns the editActivity.
	 */
	public String getEditActivity() {
		return editActivity;
	}
	/**
	 * @param editActivity The editActivity to set.
	 */
	public void setEditActivity(String editActivity) {
		this.editActivity = editActivity;
	}
	/**
	 * @return Returns the stats.
	 */
	public String getStats() {
		return stats;
	}
	/**
	 * @param stats The stats to set.
	 */
	public void setStats(String stats) {
		this.stats = stats;
	}
	/**
	 * @return Returns the editReport.
	 */
	public String getEditReport() {
		return editReport;
	}
	/**
	 * @param editReport The editReport to set.
	 */
	public void setEditReport(String editReport) {
		this.editReport = editReport;
	}
	/**
	 * @return Returns the responseId.
	 */
	public String getResponseId() {
		return responseId;
	}
	/**
	 * @param responseId The responseId to set.
	 */
	public void setResponseId(String responseId) {
		this.responseId = responseId;
	}
	/**
	 * @return Returns the updateReport.
	 */
	public String getUpdateReport() {
		return updateReport;
	}
	/**
	 * @param updateReport The updateReport to set.
	 */
	public void setUpdateReport(String updateReport) {
		this.updateReport = updateReport;
	}
	/**
	 * @return Returns the updatedResponse.
	 */
	public String getUpdatedResponse() {
		return updatedResponse;
	}
	/**
	 * @param updatedResponse The updatedResponse to set.
	 */
	public void setUpdatedResponse(String updatedResponse) {
		this.updatedResponse = updatedResponse;
	}
	/**
	 * @return Returns the hideReport.
	 */
	public String getHideReport() {
		return hideReport;
	}
	/**
	 * @param hideReport The hideReport to set.
	 */
	public void setHideReport(String hideReport) {
		this.hideReport = hideReport;
	}
	/**
	 * @return Returns the hiddenResponseId.
	 */
	public String getHiddenResponseId() {
		return hiddenResponseId;
	}
	/**
	 * @param hiddenResponseId The hiddenResponseId to set.
	 */
	public void setHiddenResponseId(String hiddenResponseId) {
		this.hiddenResponseId = hiddenResponseId;
	}
	/**
	 * @return Returns the unhideReport.
	 */
	public String getUnhideReport() {
		return unhideReport;
	}
	/**
	 * @param unhideReport The unhideReport to set.
	 */
	public void setUnhideReport(String unhideReport) {
		this.unhideReport = unhideReport;
	}
	/**
	 * @return Returns the unHiddenResponseId.
	 */
	public String getUnHiddenResponseId() {
		return unHiddenResponseId;
	}
	/**
	 * @param unHiddenResponseId The unHiddenResponseId to set.
	 */
	public void setUnHiddenResponseId(String unHiddenResponseId) {
		this.unHiddenResponseId = unHiddenResponseId;
	}
	/**
	 * @return Returns the offlineInstructions.
	 */
	public String getOfflineInstructions() {
		return offlineInstructions;
	}
	/**
	 * @param offlineInstructions The offlineInstructions to set.
	 */
	public void setOfflineInstructions(String offlineInstructions) {
		this.offlineInstructions = offlineInstructions;
	}
	/**
	 * @return Returns the onlineInstructions.
	 */
	public String getOnlineInstructions() {
		return onlineInstructions;
	}
	/**
	 * @param onlineInstructions The onlineInstructions to set.
	 */
	public void setOnlineInstructions(String onlineInstructions) {
		this.onlineInstructions = onlineInstructions;
	}
	/**
	 * @return Returns the submitMonitoringInstructions.
	 */
	public String getSubmitMonitoringInstructions() {
		return submitMonitoringInstructions;
	}
	/**
	 * @param submitMonitoringInstructions The submitMonitoringInstructions to set.
	 */
	public void setSubmitMonitoringInstructions(
			String submitMonitoringInstructions) {
		this.submitMonitoringInstructions = submitMonitoringInstructions;
	}
}
