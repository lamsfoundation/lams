package org.lamsfoundation.lams.tool.peerreview.util;

import java.util.Comparator;

public class SessionNameComparator implements Comparator<String> {

    @Override
    public int compare(String sessionName1, String sessionName2) {
	if (sessionName1 == null && sessionName2 == null) {
	    return 0;
	} else if (sessionName1 == null) {
	    return -1;
	} else if (sessionName2 == null) {
	    return 1;
	}
	String name1 = sessionName1.toLowerCase();
	String name2 = sessionName2.toLowerCase();
	String nameWithoutNumbers1 = name1.replaceAll("\\d+", "");
	String nameWithoutNumbers2 = name2.replaceAll("\\d+", "");
	if (!nameWithoutNumbers1.equals(nameWithoutNumbers2)) {
	    return name1.compareTo(name2);
	}
	String numbers1 = name1.replaceAll("\\D+", "");
	String numbers2 = name2.replaceAll("\\D+", "");
	if (numbers1.length() == 0 || numbers2.length() == 0) {
	    return name1.compareTo(name2);
	}
	try {
	    Long num1 = Long.parseLong(numbers1);
	    Long num2 = Long.parseLong(numbers2);
	    return num1.compareTo(num2);
	} catch (Exception e) {
	    return name1.compareTo(name2);
	}
    }
}