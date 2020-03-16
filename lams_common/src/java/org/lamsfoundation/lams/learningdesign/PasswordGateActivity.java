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

package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.Vector;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.learningdesign.dto.ValidationErrorDTO;
import org.lamsfoundation.lams.learningdesign.strategy.PasswordGateActivityStrategy;
import org.lamsfoundation.lams.util.MessageService;

/**
 * Only learners who enter correct password may pass this gate.
 * The password is set up in authoring.
 *
 * @author Marcin Cieslak
 */
@Entity
@DiscriminatorValue("16")
public class PasswordGateActivity extends GateActivity implements Serializable {

    private static final long serialVersionUID = -2043599655692543024L;

    @Column(name = "gate_password")
    private String gatePassword;

    public PasswordGateActivity() {
	super.simpleActivityStrategy = new PasswordGateActivityStrategy(this);
    }

    /**
     * Makes a copy of the SynchGateActivity for authoring, preview and monitoring enviornment
     *
     * @return SynchGateActivity Returns a deep-copy of the originalActivity
     */
    @Override
    public Activity createCopy(int uiidOffset) {
	PasswordGateActivity newPasswordGateActivity = new PasswordGateActivity();
	copyToNewActivity(newPasswordGateActivity, uiidOffset);
	newPasswordGateActivity.setGateActivityLevelId(this.getGateActivityLevelId());
	newPasswordGateActivity.setGateOpen(false);
	newPasswordGateActivity.setGatePassword(this.getGatePassword());
	return newPasswordGateActivity;
    }

    /**
     * Validate schedule gate activity (offset conditions)
     *
     * @return error message key
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Vector<ValidationErrorDTO> validateActivity(MessageService messageService) {
	Vector<ValidationErrorDTO> listOfValidationErrors = new Vector();
	if (StringUtils.isBlank(getGatePassword())) {
	    listOfValidationErrors.add(new ValidationErrorDTO(ValidationErrorDTO.SCHEDULE_GATE_ERROR_CODE,
		    messageService.getMessage(ValidationErrorDTO.PASSWORD_GATE_BLANK_ERROR_KEY),
		    this.getActivityUIID()));
	}
	return listOfValidationErrors;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("activityId", getActivityId()).toString();
    }

    /**
     * @see org.lamsfoundation.lams.util.Nullable#isNull()
     */
    @Override
    public boolean isNull() {
	return false;
    }

    public String getGatePassword() {
	return gatePassword;
    }

    public void setGatePassword(String gatePassword) {
	this.gatePassword = gatePassword;
    }
}