
package org.lamsfoundation.lams.lesson;

import org.lamsfoundation.lams.learningdesign.NullActivity;

/**
 * @author dgarth
 *
 */
public class ParallelWaitActivity extends NullActivity {

    public static final int PARALLEL_WAIT_ACTIVITY_TYPE = -1;

    public Integer getActivityTypeId() {
		return new Integer(PARALLEL_WAIT_ACTIVITY_TYPE);
	}
    
    
}
