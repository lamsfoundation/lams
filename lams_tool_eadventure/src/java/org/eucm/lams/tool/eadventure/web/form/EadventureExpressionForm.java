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

/* $Id$ */
package org.eucm.lams.tool.eadventure.web.form;

import org.apache.struts.action.ActionForm;

/**
 * Form responsible for representing <code>EadventureExpression</code> objects on a view layer.
 *
 * @author Angel del Blanco
 *
 * @struts.form name="eadventureExpressionForm"
 */
public class EadventureExpressionForm extends ActionForm {

    private String sessionMapID;

    private String position;
    // Link the name of the variable with their UUID for the first param
    private String[] possibleVarsOp1;
    private String selectedVarOp1;
    // Select the operator
    private String[] possibleOperator;
    private String selectedOperator;
    // Link the name of the variable with their UUID for the second param
    private String[] possibleVarsOp2;
    private String selectedVarOp2;
    //TODO comprobar el tipo!!!
    private String introducedValue;
    // this var identifies if the user selects to introduce a value (false) or a variable as second op (true)
    private boolean isSecondVarSelected;

    private String nextOp;

    public EadventureExpressionForm() {
	super();
	position = "-1";
    }

    public String getSessionMapID() {
	return sessionMapID;
    }

    public void setSessionMapID(String sessionMapID) {
	this.sessionMapID = sessionMapID;
    }

    public String[] getPossibleVarsOp1() {
	return possibleVarsOp1;
    }

    public void setPossibleVarsOp1(String[] possibleVarsOp1) {
	this.possibleVarsOp1 = possibleVarsOp1;
    }

    public String getSelectedVarOp1() {
	return selectedVarOp1;
    }

    public void setSelectedVarOp1(String selectedVarOp1) {
	this.selectedVarOp1 = selectedVarOp1;
    }

    public String[] getPossibleVarsOp2() {
	return possibleVarsOp2;
    }

    public void setPossibleVarsOp2(String[] possibleVarsOp2) {
	this.possibleVarsOp2 = possibleVarsOp2;
    }

    public String getSelectedVarOp2() {
	return selectedVarOp2;
    }

    public void setSelectedVarOp2(String selectedVarOp2) {
	this.selectedVarOp2 = selectedVarOp2;
    }

    public String getIntroducedValue() {
	return introducedValue;
    }

    public void setIntroducedValue(String introducedValue) {
	this.introducedValue = introducedValue;
    }

    public String[] getPossibleOperator() {
	return possibleOperator;
    }

    public void setPossibleOperator(String[] possibleOperator) {
	this.possibleOperator = possibleOperator;
    }

    public String getSelectedOperator() {
	return selectedOperator;
    }

    public void setSelectedOperator(String selectedOperator) {
	this.selectedOperator = selectedOperator;
    }

    public String getPosition() {
	return position;
    }

    public void setPosition(String position) {
	this.position = position;
    }

    public String getNextOp() {
	return nextOp;
    }

    public void setNextOp(String nextOp) {
	this.nextOp = nextOp;
    }

    public boolean isSecondVarSelected() {
	return isSecondVarSelected;
    }

    public void setSecondVarSelected(boolean isSecondVarSelected) {
	this.isSecondVarSelected = isSecondVarSelected;
    }

}
