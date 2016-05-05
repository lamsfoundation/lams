package org.lamsfoundation.lams.tool.scratchie.util;

import java.util.Comparator;

import org.lamsfoundation.lams.tool.scratchie.model.ScratchieItem;

public class ScratchieItemComparator implements Comparator<ScratchieItem> {

    @Override
    public int compare(ScratchieItem o1, ScratchieItem o2) {

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
