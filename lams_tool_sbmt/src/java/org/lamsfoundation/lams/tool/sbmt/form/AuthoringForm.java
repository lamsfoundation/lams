package org.lamsfoundation.lams.tool.sbmt.form;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.ValidatorForm;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.sbmt.InstructionFiles;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesContent;

/**
 * 
 * @author Dapeng.Ni
 *	@struts.form name="SbmtAuthoringForm"
 */
public class AuthoringForm extends ValidatorForm {

	private Long toolContentID;
	
	private String contentFolderID;
	
	//control fields
	private String sessionMapID;
	private String currentTab;
	
	//basic input fields
    private String title;
    private String instructions;
    private String offlineInstruction; 
    private String onlineInstruction;
    private boolean lockOnFinished;
	
    //file and display fields
    private FormFile offlineFile;
    private FormFile onlineFile;
    private List onlineFileList;
    private List offlineFileList;
    
    private boolean limitUpload;
    private int limitUploadNumber;
    
    private boolean reflectOnActivity;
    private String reflectInstructions;

    public void reset(ActionMapping mapping, HttpServletRequest request){
    	lockOnFinished = false;
    	limitUpload= false;
    	reflectOnActivity = false;
    	
    }
	public void initContentValue(SubmitFilesContent content){
		if(content == null)
			return;
		
		//copy attribute
		this.toolContentID= content.getContentID();
		this.title = content.getTitle();
		this.instructions = content.getInstruction();
		this.offlineInstruction = content.getOfflineInstruction();
		this.onlineInstruction = content.getOnlineInstruction();
		this.lockOnFinished = content.isLockOnFinished();
		
		this.limitUpload = content.isLimitUpload();
		this.limitUploadNumber = content.getLimitUploadNumber();
		
		this.reflectOnActivity = content.isReflectOnActivity();
		this.reflectInstructions = content.getReflectInstructions();
		
		onlineFileList = new ArrayList();
		offlineFileList = new ArrayList();
		Set fileSet = content.getInstructionFiles();
		if(fileSet != null){
			Iterator iter = fileSet.iterator();
			while(iter.hasNext()){
				InstructionFiles file = (InstructionFiles) iter.next();
				if(StringUtils.equalsIgnoreCase(file.getType(),IToolContentHandler.TYPE_OFFLINE))
					offlineFileList.add(file);
				else
					onlineFileList.add(file);
			}
		}
		
	}
    //**************************************************
    // Get / Set method
    //**************************************************
	public String getCurrentTab() {
		return currentTab;
	}

	public void setCurrentTab(String currentTab) {
		this.currentTab = currentTab;
	}

	public FormFile getOfflineFile() {
		return offlineFile;
	}

	public void setOfflineFile(FormFile offlineFile) {
		this.offlineFile = offlineFile;
	}

	public List getOfflineFileList() {
		return offlineFileList;
	}

	public void setOfflineFileList(List offlineFileList) {
		this.offlineFileList = offlineFileList;
	}

	public FormFile getOnlineFile() {
		return onlineFile;
	}

	public void setOnlineFile(FormFile onlineFile) {
		this.onlineFile = onlineFile;
	}

	public List getOnlineFileList() {
		return onlineFileList;
	}

	public void setOnlineFileList(List onlineFileList) {
		this.onlineFileList = onlineFileList;
	}

	public Long getToolContentID() {
		return toolContentID;
	}

	public void setToolContentID(Long toolContentID) {
		this.toolContentID = toolContentID;
	}

	public String getSessionMapID() {
		return sessionMapID;
	}

	public void setSessionMapID(String sessionMapID) {
		this.sessionMapID = sessionMapID;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public boolean isLockOnFinished() {
		return lockOnFinished;
	}

	public void setLockOnFinished(boolean lockOnFinished) {
		this.lockOnFinished = lockOnFinished;
	}

	public String getOfflineInstruction() {
		return offlineInstruction;
	}

	public void setOfflineInstruction(String offlineInstruction) {
		this.offlineInstruction = offlineInstruction;
	}

	public String getOnlineInstruction() {
		return onlineInstruction;
	}

	public void setOnlineInstruction(String onlineInstruction) {
		this.onlineInstruction = onlineInstruction;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	public String getReflectInstructions() {
		return reflectInstructions;
	}
	public void setReflectInstructions(String reflectInstructions) {
		this.reflectInstructions = reflectInstructions;
	}
	public boolean isReflectOnActivity() {
		return reflectOnActivity;
	}
	public void setReflectOnActivity(boolean reflectOnActivity) {
		this.reflectOnActivity = reflectOnActivity;
	}
	public String getContentFolderID() {
		return contentFolderID;
	}
	public void setContentFolderID(String contentFolderID) {
		this.contentFolderID = contentFolderID;
	}
	public boolean isLimitUpload() {
		return limitUpload;
	}
	public void setLimitUpload(boolean limitUpload) {
		this.limitUpload = limitUpload;
	}
	public int getLimitUploadNumber() {
		return limitUploadNumber;
	}
	public void setLimitUploadNumber(int limitUploadNumber) {
		this.limitUploadNumber = limitUploadNumber;
	}
}
