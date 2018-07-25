/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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

package org.lamsfoundation.lams.tool.qa.web.form;

import org.apache.struts.upload.FormFile;

public class QaAdminForm {

    public static final long serialVersionUID = 978235712395273523L;

    public static final String TRUE = "true";
    public static final String FALSE = "false";

    private Boolean qaWizardEnabled;
    private String serialiseXML;
    private String deleteCategoriesCSV;
    private String deleteSkillsCSV;
    private String deleteQuestionsCSV;
    private FormFile importFile;

    public QaAdminForm() {
    }

    public Boolean getQaWizardEnabled() {
	return qaWizardEnabled;
    }

    public void setQaWizardEnabled(Boolean qaWizardEnabled) {
	this.qaWizardEnabled = qaWizardEnabled;
    }

    public void setQaWizardEnabled(String qaWizardEnabled) {
	this.qaWizardEnabled = qaWizardEnabled.equals(TRUE);
    }

    public String getSerialiseXML() {
	return serialiseXML;
    }

    public void setSerialiseXML(String serialiseXML) {
	this.serialiseXML = serialiseXML;
    }

    public String getDeleteCategoriesCSV() {
	return deleteCategoriesCSV;
    }

    public void setDeleteCategoriesCSV(String deleteCategoriesCSV) {
	this.deleteCategoriesCSV = deleteCategoriesCSV;
    }

    public String getDeleteSkillsCSV() {
	return deleteSkillsCSV;
    }

    public void setDeleteSkillsCSV(String deleteSkillsCSV) {
	this.deleteSkillsCSV = deleteSkillsCSV;
    }

    public String getDeleteQuestionsCSV() {
	return deleteQuestionsCSV;
    }

    public void setDeleteQuestionsCSV(String deleteQuestionsCSV) {
	this.deleteQuestionsCSV = deleteQuestionsCSV;
    }

    public FormFile getImportFile() {
	return importFile;
    }

    public void setImportFile(FormFile importFile) {
	this.importFile = importFile;
    }
}
