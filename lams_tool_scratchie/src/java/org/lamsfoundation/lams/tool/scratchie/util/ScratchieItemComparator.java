package org.lamsfoundation.lams.tool.scratchie.util;

import java.util.Comparator;

import org.lamsfoundation.lams.tool.scratchie.model.ScratchieItem;

public class ScratchieItemComparator implements Comparator<ScratchieItem> {

    @Override
    public int compare(ScratchieItem o1, ScratchieItem o2) {

	if (o1 != null && o2 != null) {
	    if (o1.getDisplayOrder() > o2.getDisplayOrder()) {
		return 1;
	    } else {
		return -1;
	    }
	} else if (o1 != null) {
	    return 1;
	} else {
	    return -1;
	}
    }
}