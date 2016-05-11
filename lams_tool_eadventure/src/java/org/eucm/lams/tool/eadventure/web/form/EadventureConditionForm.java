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


package org.eucm.lams.tool.eadventure.web.form;

import java.util.HashSet;
import java.util.Set;

import org.apache.struts.action.ActionForm;

/**
 * Form responsible for representing <code>EadventureCondition</code> objects on a view layer.
 *
 * @author Andrey Balan
 * @author Angel del Blanco
 *
 * @struts.form name="eadventureConditionForm"
 */
public class EadventureConditionForm extends ActionForm {

    //tool access mode;
    private String mode;
    private String sessionMapID;

    private String name;

    private String position;

    private Set expressionList;
    // Link the name of the variable with their UUID 
    //TODO renombrar a possibleParams
    //private LabelValueBean[] possibleVars;
    //TODO tiene que ser solo un string, ya que no permitimos seleccion multiple
    //private String[] selectedItems;

    public EadventureConditionForm() {
	super();
	expressionList = new HashSet();
    }

    /**
     * Returns TaskListCondition name.
     * 
     * @return TaskListCondition name
     */
    public String getName() {
	return name;
    }

    /**
     * Sets TaskListCondition title.
     * 
     * @param title
     *            TaskListCondition title
     */
    public void setName(String name) {
	this.name = name;
    }

    /**
     * Returns current SessionMapID.
     * 
     * @return current SessionMapID
     */
    public String getSessionMapID() {
	return sessionMapID;
    }

    /**
     * Sets current SessionMapID.
     * 
     * @param sessionMapID
     *            current SessionMapID
     */
    public void setSessionMapID(String sessionMapID) {
	this.sessionMapID = sessionMapID;
    }

    /**
     * Returns working mode.
     * 
     * @return working mode
     */
    public String getMode() {
	return mode;
    }

    /**
     * Returns working mode.
     * 
     * @param mode
     *            working mode
     */
    public void setMode(String mode) {
	this.mode = mode;
    }

    public Set getExpressionList() {
	return expressionList;
    }

    public void setExpressionList(Set expressionList) {
	this.expressionList = expressionList;
    }

    public String getPosition() {
	return position;
    }

    public void setPosition(String position) {
	this.position = position;
    }

}
