/*
 * Created on 16/02/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.Comparator;

/**
 * @author dgarth
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ActivityOrderComparator implements Comparator, Serializable {

    public int compare(Object o1, Object o2) {
        Activity activity1 = (Activity)o1;
        Activity activity2 = (Activity)o2;
        return activity1.getActivityId().compareTo(activity2.getActivityId());
    }
    
}
