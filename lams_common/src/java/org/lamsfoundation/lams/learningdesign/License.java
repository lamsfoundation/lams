/*
 * Created on Feb 15, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;

/**
 * @author Minhas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class License implements Serializable{
	
	/** persistent field */
	private Long licenseID;
	/** persistent field */
	private String name;
	/** persistent field */
	private String code;
	/** persistent field */
	private String url;
	/** persistent field */
	private Boolean defaultLicense;
	/** persistent field */
	private String pictureURL;
	
	/** default constructor*/
	public License(){
		
	}
	/** full constructor*/
	public License(Long licenseID,
				   String name,
				   String code,
				   String url,
				   Boolean defaultLicense,
				   String pictureURL){
		this.licenseID = licenseID;
		this.name = name;
		this.code = code;
		this.url = url;
		this.defaultLicense = defaultLicense;
		this.pictureURL = pictureURL;
	}
	

	public Boolean getDefaultLicense() {
		return defaultLicense;
	}
	public void setDefaultLicense(Boolean defaultLicense) {
		this.defaultLicense = defaultLicense;
	}
	public Long getLicenseID() {
		return licenseID;
	}
	public void setLicenseID(Long licenseID) {
		this.licenseID = licenseID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPictureURL() {
		return pictureURL;
	}
	public void setPictureURL(String pictureURL) {
		this.pictureURL = pictureURL;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
