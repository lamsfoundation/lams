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
package org.lamsfoundation.lams.authoring.template;

/** DTO used for generating options on a JSP page */
public class Option {

    private int displayOrder;
    private boolean correct; // used for MCQ
    private String text;
    private String grade; // used for assessment

    public Option(int displayOrder, boolean correct, String text, String grade) {
	this.displayOrder = displayOrder;
	this.correct = correct;
	this.text = text;
	this.setGrade(grade);
    }

    public int getDisplayOrder() {
	return displayOrder;
    }

    public boolean isCorrect() {
	return correct;
    }

    public String getText() {
	return text;
    }

    public void setDisplayOrder(int displayOrder) {
	this.displayOrder = displayOrder;
    }

    public void setCorrect(boolean correct) {
	this.correct = correct;
    }

    public void setText(String text) {
	this.text = text;
    }

    public String getGrade() {
	return grade;
    }

    public void setGrade(String grade) {
	this.grade = grade;
    }

}
