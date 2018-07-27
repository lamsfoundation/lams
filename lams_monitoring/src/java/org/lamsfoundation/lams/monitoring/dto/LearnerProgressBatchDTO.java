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


package org.lamsfoundation.lams.monitoring.dto;

import java.util.Vector;

import org.lamsfoundation.lams.lesson.LearnerProgress;

/**
 * Return the details of the Learner Progress batching - how many learner progress entries exist for this lesson, how
 * many in a batch, how many batches available and the first batch of users.
 */
public class LearnerProgressBatchDTO {

    private Vector learnerProgressList;
    private Integer batchSize;
    private Integer numBatches;
    private Integer numAllLearnerProgress;

    public LearnerProgressBatchDTO(Vector<LearnerProgress> progress, int batchSize, int numAllLearnerProgress) {
	this.learnerProgressList = progress;
	this.batchSize = batchSize;
	this.numAllLearnerProgress = numAllLearnerProgress;

	// integer division truncates, so convert to floating point first.
	double batchSizeAsDouble = batchSize;
	double numAllLearnerProgressAsDouble = numAllLearnerProgress;
	double numBatchesDouble = numAllLearnerProgressAsDouble / batchSizeAsDouble;
	numBatchesDouble = Math.ceil(numBatchesDouble);

	numBatches = (int) numBatchesDouble;
	if (numBatches < 1) {
	    numBatches = 1;
	}
    }

    public Vector getLearnerProgressList() {
	return learnerProgressList;
    }

    public Integer getBatchSize() {
	return batchSize;
    }

    public Integer getNumBatches() {
	return numBatches;
    }

    public Integer getNumAllLearnerProgress() {
	return numAllLearnerProgress;
    }
}
