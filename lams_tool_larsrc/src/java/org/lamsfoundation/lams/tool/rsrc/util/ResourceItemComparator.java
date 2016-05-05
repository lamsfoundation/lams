package org.lamsfoundation.lams.tool.rsrc.util;

import java.util.Comparator;

import org.lamsfoundation.lams.tool.rsrc.model.ResourceItem;

/**
 *
 * @author steven
 *
 */
public class ResourceItemComparator implements Comparator<ResourceItem> {

    @Override
    public int compare(ResourceItem o1, ResourceItem o2) {

	if (o1 != null && o2 != null & o1.getOrderId() != null && o2.getOrderId() != null) {
	    if (o1.getOrderId() > o2.getOrderId()) {
		return 1;
	    } else {
		return -1;
	    }
	} else if (o1 != null && o2 != null & o1.getCreateDate() != null && o2.getCreateDate() != null) {
	    return (o1.getCreateDate().getTime() - o2.getCreateDate().getTime()) > 0 ? 1 : -1;
	} else if (o1 != null) {
	    return 1;
	} else {
	    return -1;
	}
    }
}
