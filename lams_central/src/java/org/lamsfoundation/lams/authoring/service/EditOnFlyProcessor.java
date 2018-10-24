/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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


package org.lamsfoundation.lams.authoring.service;

import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ComplexActivity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.LearningDesignProcessor;
import org.lamsfoundation.lams.learningdesign.SimpleActivity;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
import org.lamsfoundation.lams.learningdesign.exception.LearningDesignProcessorException;

public class EditOnFlyProcessor extends LearningDesignProcessor {
    public Activity lastReadOnlyActivity = null;
    // were there any activities added after the Gate?
    public Activity firstAddedActivity = null;

    public EditOnFlyProcessor(LearningDesign design, IActivityDAO activityDAO) {
	super(design, activityDAO);
    }

    @Override
    public boolean startComplexActivity(ComplexActivity activity) throws LearningDesignProcessorException {
	return true;
    }

    @Override
    public void endComplexActivity(ComplexActivity activity) throws LearningDesignProcessorException {
	if (!activity.isFloatingActivity()) {
	    if (activity.isActivityReadOnly()) {
		lastReadOnlyActivity = activity;
	    } else if (firstAddedActivity == null) {
		firstAddedActivity = activity;
	    }
	}
    }

    @Override
    public void startSimpleActivity(SimpleActivity activity) throws LearningDesignProcessorException {
	// everything done by endSimpleActivity
    }

    @Override
    public void endSimpleActivity(SimpleActivity activity) throws LearningDesignProcessorException {
	if (!activity.isFloating()) {
	    if (activity.isActivityReadOnly()) {
		lastReadOnlyActivity = activity;
	    } else if (firstAddedActivity == null) {
		firstAddedActivity = activity;
	    }
	}
    }
}