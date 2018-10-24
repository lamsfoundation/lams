/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.admin.web.form;

public class SignupManagementForm {

    private Integer signupOrganisationId; 
    
    private Integer organisationId; 
    
    private boolean addToLessons = true;
    
    private boolean addAsStaff = false;
    
    private boolean addWithAuthor = false;
    
    private boolean addWithMonitor = false;
    
    private boolean emailVerify = false;
    
    private String courseKey;
    
    private String confirmCourseKey;
    
    private String blurb;
    
    private boolean disabled = false;
    
    private boolean loginTabActive = false;
    
    private String context;

    public Integer getSignupOrganisationId() {
        return signupOrganisationId;
    }

    public void setSignupOrganisationId(Integer signupOrganisationId) {
        this.signupOrganisationId = signupOrganisationId;
    }

    public Integer getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationId(Integer organisationId) {
        this.organisationId = organisationId;
    }

    public boolean isAddToLessons() {
        return addToLessons;
    }

    public void setAddToLessons(boolean addToLessons) {
        this.addToLessons = addToLessons;
    }

    public boolean isAddAsStaff() {
        return addAsStaff;
    }

    public void setAddAsStaff(boolean addAsStaff) {
        this.addAsStaff = addAsStaff;
    }

    public boolean isAddWithAuthor() {
        return addWithAuthor;
    }

    public void setAddWithAuthor(boolean addWithAuthor) {
        this.addWithAuthor = addWithAuthor;
    }

    public boolean isAddWithMonitor() {
        return addWithMonitor;
    }

    public void setAddWithMonitor(boolean addWithMonitor) {
        this.addWithMonitor = addWithMonitor;
    }
    
    public boolean getEmailVerify() {
	return emailVerify;
    }

    public void setEmailVerify(boolean emailValidation) {
	this.emailVerify = emailValidation;
    }

    public String getCourseKey() {
        return courseKey;
    }

    public void setCourseKey(String courseKey) {
        this.courseKey = courseKey;
    }

    public String getConfirmCourseKey() {
        return confirmCourseKey;
    }

    public void setConfirmCourseKey(String confirmCourseKey) {
        this.confirmCourseKey = confirmCourseKey;
    }

    public String getBlurb() {
        return blurb;
    }

    public void setBlurb(String blurb) {
        this.blurb = blurb;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isLoginTabActive() {
        return loginTabActive;
    }

    public void setLoginTabActive(boolean loginTabActive) {
        this.loginTabActive = loginTabActive;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
    
}


