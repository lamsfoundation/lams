package org.lamsfoundation.lams.contentrepository.struts.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

/** 
 * MyEclipse Struts
 * Creation date: 11-30-2004
 * 
 * XDoclet definition:
 * @struts:form name="addFileContentForm"
 */
public class AddFileContentForm extends ActionForm {

	// --------------------------------------------------------- Instance Variables

	/** theFile property */
	private FormFile theFile;
	private String dirName;
	private String entryString;
	private String method;
	private String description;
	private Long uuid;
	
	// --------------------------------------------------------- Methods

	/** 
	 * Method validate
	 * @param mapping
	 * @param request
	 * @return ActionErrors
	 */
	public ActionErrors validate(
		ActionMapping mapping,
		HttpServletRequest request) {

		ActionErrors errors = super.validate(mapping,request);
		if ( errors == null ) errors = new ActionErrors();
		
		if ( isEmpty(method) ) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.mandatory", "Method"));
		}

		if ( isEmpty(description) ) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.mandatory", "Description"));
		}
		if ( "uploadFile".equals(method) && theFile==null ) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.mandatory", "File"));
		}
		
		if ( "uploadPackage".equals(method) && isEmpty(dirName) ) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.mandatory", "Directory Name"));
		}

		if ( errors.size() == 0 ) return null;
		return errors;

	}

	private boolean isEmpty(String str) {
		return str == null || str.trim().length() == 0 ;
	}
	/** 
	 * Method reset
	 * @param mapping
	 * @param request
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		theFile = null;
		dirName = null;
		entryString = "index.html";
		description = null;
		method = null;
		uuid = null;
	}

	/** 
	 * Returns the theFile.
	 * @return FormFile
	 */
	public FormFile getTheFile() {
		return theFile;
	}

	/** 
	 * Set the theFile.
	 * @param theFile The theFile to set
	 */
	public void setTheFile(FormFile theFile) {
		this.theFile = theFile;
	}

	/**
	 * @return Returns dirName
	 */
	public String getDirName() {
		return dirName;
	}
	/**
	 * @param dirName Sets dirName
	 */
	public void setDirName(String dirName) {
		this.dirName = dirName;
	}
	
	/**
	 * @return Returns the uuid.
	 */
	public Long getUuid() {
		return uuid;
	}
	/**
	 * @param uuid The uuid to set.
	 */
	public void setUuid(Long uuid) {
		this.uuid = uuid;
	}
	/**
	 * @return Returns the entryString.
	 */
	public String getEntryString() {
		return entryString;
	}
	/**
	 * @param entryString The entryString to set.
	 */
	public void setEntryString(String entryString) {
		this.entryString = entryString;
	}
	/**
	 * @return Returns the method.
	 */
	public String getMethod() {
		return method;
	}
	/**
	 * @param method The method to set.
	 */
	public void setMethod(String method) {
		this.method = method;
	}
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}