/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.tool.scratchie.dto;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.lamsfoundation.lams.util.NumberUtil;

public class LeaderResultsDTO {

    private Long contentId;
    private int count;
    private Double min;
    private Double max;
    private Double average;
    private Double median;
    private Collection<Double> modes;

    public LeaderResultsDTO(Long contentId, List<Double> grades) {
	this.contentId = contentId;

	if (grades == null || grades.isEmpty()) {
	    return;
	}

	double sum = 0;
	min = Double.MAX_VALUE;
	max = Double.MIN_VALUE;
	for (Double grade : grades) {
	    if (grade < min) {
		min = grade;
	    }
	    if (grade > max) {
		max = grade;
	    }
	    sum += grade;
	}

	count = grades.size();
	average = sum / count;

	Collections.sort(grades);
	median = grades.get(count / 2).doubleValue();
	if (count % 2 == 0) {
	    median = (median + grades.get(count / 2 - 1)) / 2;
	}

	modes = grades.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting())) // grade -> its count
		.entrySet().stream().collect(Collectors.groupingBy(Map.Entry::getValue, // convert to count -> grades that have this count (can be many)
			TreeMap::new, Collectors.mapping(Map.Entry::getKey, Collectors.toCollection(TreeSet::new))))
		.lastEntry().getValue(); // get top count
    }

    public Long getContentId() {
	return contentId;
    }

    public int getCount() {
	return count;
    }

    public Double getMin() {
	return min;
    }

    public String getMinString() {
	return LeaderResultsDTO.format(min);
    }

    public Double getMax() {
	return max;
    }

    public String getMaxString() {
	return LeaderResultsDTO.format(max);
    }

    public Double getAverage() {
	return average;
    }

    public String getAverageString() {
	return LeaderResultsDTO.format(average);
    }

    public Double getMedian() {
	return median;
    }

    public String getMedianString() {
	return LeaderResultsDTO.format(median);
    }

    public Collection<Double> getModes() {
	return modes;
    }

    public String getModesString() {
	if (modes == null || modes.isEmpty()) {
	    return "-";
	}

	Iterator<Double> modeIterator = modes.iterator();
	StringBuilder result = new StringBuilder(LeaderResultsDTO.format(modeIterator.next()));
	while (modeIterator.hasNext()) {
	    result.append(", ").append(LeaderResultsDTO.format(modeIterator.next()));
	}
	return result.toString();
    }

    public static String format(Number input) {
	return input == null ? "-" : NumberUtil.formatLocalisedNumber(input, (Locale) null, 2);
    }
}