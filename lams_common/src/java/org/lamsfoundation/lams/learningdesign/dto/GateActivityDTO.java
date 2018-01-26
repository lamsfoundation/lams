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


package org.lamsfoundation.lams.learningdesign.dto;

import org.lamsfoundation.lams.learningdesign.GateActivity;

/**
 * DTO wrapping a normal Gate Activity class, with an extra "calculated" field added for the learning module's gate
 * screen.
 *
 */
public class GateActivityDTO {
    private GateActivity gate;

    private Integer expectedLearnerCount;
    private Integer waitingLearnerCount;
    private boolean allowToPass;

    public GateActivityDTO(GateActivity gate, Integer expectedLearnerCount, Integer waitingLearnerCount,
	    boolean allowToPass) {
	this.gate = gate;

	this.expectedLearnerCount = expectedLearnerCount;
	this.waitingLearnerCount = waitingLearnerCount;

	this.allowToPass = allowToPass || gate.getGateOpen();
    }

    public GateActivity getGate() {
	return gate;
    }

    public Integer getExpectedLearnerCount() {
	return expectedLearnerCount;
    }

    public Integer getWaitingLearnerCount() {
	return waitingLearnerCount;
    }

    public boolean getAllowToPass() {
	return allowToPass;
    }
}