package org.lamsfoundation.lams.tool.dokumaran.util;

import java.util.Comparator;

public class DokumaranSessionNameComparator implements Comparator<String> {
    @Override
    public int compare(String name1, String name2) {
	return compareSessionNames(name1, name2);
    }

    public static int compareSessionNames(String name1, String name2) {
	name1 = name1.toLowerCase();
	name2 = name2.toLowerCase();
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