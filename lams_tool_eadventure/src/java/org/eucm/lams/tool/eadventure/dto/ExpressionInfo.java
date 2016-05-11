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


package org.eucm.lams.tool.eadventure.dto;

public class ExpressionInfo {

    private String name;

    private String type;

    private String valueInExpression;

    private String operator;

    private String nameSecondOp;

    private String nextOp;

    public ExpressionInfo(String name, String type, String operator, String nextOp) {
	this.name = name;
	this.type = type;
	this.operator = operator;
	this.nextOp = nextOp;

    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public String getValueInExpression() {
	return valueInExpression;
    }

    public void setValueInExpression(String valueInExpression) {
	this.valueInExpression = valueInExpression;
    }

    public String getOperator() {
	return operator;
    }

    public void setOperator(String operator) {
	this.operator = operator;
    }

    public String getNameSecondOp() {
	return nameSecondOp;
    }

    public void setNameSecondOp(String nameSecondOp) {
	this.nameSecondOp = nameSecondOp;
    }

    public String getNextOp() {
	return nextOp;
    }

    public void setNextOp(String nextOp) {
	this.nextOp = nextOp;
    }

}
