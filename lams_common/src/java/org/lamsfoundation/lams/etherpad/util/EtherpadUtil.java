package org.lamsfoundation.lams.etherpad.util;

import org.lamsfoundation.lams.etherpad.service.IEtherpadService;

public class EtherpadUtil {

    public static String getPadId(String groupId) {
	return groupId + "$" + IEtherpadService.DEFAULT_PAD_NAME;
    }
}
