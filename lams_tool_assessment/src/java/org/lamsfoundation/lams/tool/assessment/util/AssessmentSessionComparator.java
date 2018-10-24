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


package org.lamsfoundation.lams.tool.assessment.util;

import java.util.Comparator;

import org.lamsfoundation.lams.tool.assessment.model.AssessmentSession;
import org.lamsfoundation.lams.util.AlphanumComparator;

/**
 * AssessmentSession comparator.
 *
 * @author Andrey Balan
 *
 */
public class AssessmentSessionComparator implements Comparator<AssessmentSession> {

    private static AlphanumComparator alphanumComparator = new AlphanumComparator();

    @Override
    public int compare(AssessmentSession session1, AssessmentSession session2) {

	String session1Name = session1 != null ? session1.getSessionName() : "";
	String session2Name = session2 != null ? session2.getSessionName() : "";

	return alphanumComparator.compare(session1Name, session2Name);
    }

}
