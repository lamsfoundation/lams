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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * @author fmalikoff
 * @Modified by Fei Yang
 *
 *
 *
 *
 */
public class PasswordChangeActionForm extends ActionForm {

    public static final String formName = "PasswordChangeActionForm"; // must match name in @struts:action section above

    private String oldPassword;

    private String password;

    private String passwordConfirm;

    private String login;

    public PasswordChangeActionForm() {
    }

    /**
     * Returns the login.
     *
     * @return String
     */
    public String getLogin() {
	return login;
    }

    /**
     * Returns the password.
     *
     * @return String
     */
    public String getPassword() {
	return password;
    }

    /**
     * Returns the passwordConfirm.
     *
     * @return String
     */
    public String getPasswordConfirm() {
	return passwordConfirm;
    }

    /**
     * Sets the login.
     *
     * @param login
     *            The login to set
     */
    public void setLogin(String login) {
	this.login = StringUtils.trimToEmpty(login);
    }

    /**
     * Sets the password.
     *
     * @param password
     *            The password to set
     */
    public void setPassword(String password) {
	this.password = StringUtils.trimToEmpty(password);
    }

    /**
     * Sets the passwordConfirm.
     *
     * @param passwordConfirm
     *            The passwordConfirm to set
     */
    public void setPasswordConfirm(String passwordConfirm) {
	this.passwordConfirm = StringUtils.trimToEmpty(passwordConfirm);
    }

    /**
     * @return Returns the oldPassword.
     */
    public String getOldPassword() {
	return oldPassword;
    }

    /**
     * @param oldPassword
     *            The oldPassword to set.
     */
    public void setOldPassword(String oldPassword) {
	this.oldPassword = StringUtils.trimToEmpty(oldPassword);
    }

    /**
     * Reset all properties to their default values.
     *
     * @param mapping
     *            The mapping used to select this instance
     * @param request
     *            The servlet request we are processing
     */
    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
	setOldPassword(null);
	setPassword(null);
	setPasswordConfirm(null);
    }

}