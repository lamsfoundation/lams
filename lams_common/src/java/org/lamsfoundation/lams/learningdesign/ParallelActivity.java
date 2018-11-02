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

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.learningdesign.strategy.ParallelActivityStrategy;

/**
 * @author Manpreet Minhas
 *
 */
@Entity
@DiscriminatorValue("6")
public class ParallelActivity extends ComplexActivity implements Serializable {
    private static final long serialVersionUID = 5910140223400659619L;

    /** default constructor */
    public ParallelActivity() {
	super.activityStrategy = new ParallelActivityStrategy(this);
    }

    /**
     * Makes a copy of the ParallelActivity for authoring, preview and monitoring enviornment
     *
     * @return ParallelActivity Returns a deep-copy of the originalActivity
     */
    @Override
    public Activity createCopy(int uiidOffset) {

	ParallelActivity newParallelActivity = new ParallelActivity();
	copyToNewComplexActivity(newParallelActivity, uiidOffset);
	return newParallelActivity;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("activityId", getActivityId()).toString();
    }

    @Override
    public boolean isNull() {
	return false;
    }
}