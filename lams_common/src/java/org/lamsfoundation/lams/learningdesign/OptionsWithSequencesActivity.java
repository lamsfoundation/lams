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


package org.lamsfoundation.lams.learningdesign;

public class OptionsWithSequencesActivity extends OptionsActivity {
    private Integer startXcoord;
    private Integer startYcoord;
    private Integer endXcoord;
    private Integer endYcoord;

    @Override
    public Activity createCopy(int uiidOffset) {
	OptionsWithSequencesActivity newOptionsActivity = new OptionsWithSequencesActivity();
	copyToNewComplexActivity(newOptionsActivity, uiidOffset);

	newOptionsActivity.setMaxNumberOfOptions(this.getMaxNumberOfOptions());
	newOptionsActivity.setMinNumberOfOptions(this.getMinNumberOfOptions());
	newOptionsActivity.setOptionsInstructions(this.getOptionsInstructions());
	newOptionsActivity.startXcoord = this.startXcoord;
	newOptionsActivity.startYcoord = this.startYcoord;
	newOptionsActivity.endXcoord = this.endXcoord;
	newOptionsActivity.endYcoord = this.endYcoord;

	return newOptionsActivity;
    }

    public Integer getEndXcoord() {
	return endXcoord;
    }

    public void setEndXcoord(Integer endXcoord) {
	this.endXcoord = endXcoord;
    }

    public Integer getEndYcoord() {
	return endYcoord;
    }

    public void setEndYcoord(Integer endYcoord) {
	this.endYcoord = endYcoord;
    }

    public Integer getStartXcoord() {
	return startXcoord;
    }

    public void setStartXcoord(Integer startXcoord) {
	this.startXcoord = startXcoord;
    }

    public Integer getStartYcoord() {
	return startYcoord;
    }

    public void setStartYcoord(Integer startYcoord) {
	this.startYcoord = startYcoord;
    }
}
