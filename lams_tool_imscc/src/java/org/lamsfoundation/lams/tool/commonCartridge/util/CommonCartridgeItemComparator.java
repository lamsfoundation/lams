package org.lamsfoundation.lams.tool.commonCartridge.util;

import java.util.Comparator;

import org.lamsfoundation.lams.tool.commonCartridge.model.CommonCartridgeItem;

/**
 *
 * @author Andrey Balan
 *
 */
public class CommonCartridgeItemComparator implements Comparator<CommonCartridgeItem> {

    @Override
    public int compare(CommonCartridgeItem o1, CommonCartridgeItem o2) {
	if (o1 != null && o2 != null & o1.getCreateDate() != null && o2.getCreateDate() != null) {
	    return (o1.getCreateDate().getTime() - o2.getCreateDate().getTime()) > 0 ? 1 : -1;
	} else if (o1 != null) {
	    return 1;
	} else {
	    return -1;
	}
    }

}
