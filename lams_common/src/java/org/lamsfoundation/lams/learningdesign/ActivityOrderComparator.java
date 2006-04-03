/***************************************************************************
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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ***********************************************************************/
/* $$Id$$ */
package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.Comparator;

/**
 * The activity order comparator used for sorted set. Order id is used as
 * the primary comparing criteria as it is unique within a complex activity.
 * If they are the same, activity id are used for comparison to ensure the
 * sorted set won't treat two activities with the same order id as the 
 * same activity.
 * 
 * @author dgarth, Jacky Fang
 */
public class ActivityOrderComparator implements Comparator, Serializable {

    /**
     * Compare the order.
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Object o1, Object o2) {
        Activity activity1 = (Activity)o1;
        Activity activity2 = (Activity)o2;
        
        if(activity1.getOrderId()==null||activity2.getOrderId()==null)
            return activity1.getActivityId().compareTo(activity2.getActivityId());
        
        int orderDiff = activity1.getOrderId().compareTo(activity2.getOrderId());
        //return order id compare result if they are not the same
        if(orderDiff!=0)
            return orderDiff;
        //if order id are the same, compare activity id.
        else
            return activity1.getActivityId().compareTo(activity2.getActivityId());
    }
    
}
