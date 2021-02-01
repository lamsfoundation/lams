package org.lamsfoundation.lams.tool.assessment.dto;

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

import org.lamsfoundation.lams.tool.assessment.model.AssessmentResult;
import org.lamsfoundation.lams.util.NumberUtil;

public class GradeStatsDTO {
    private Long sessionId;
    private String sessionName;

    private int numberOfLearners;
    private boolean leaderFinished;

    private int count;
    private Float min;
    private Float max;
    private Float average;
    private Float median;
    private Collection<Float> modes;

    //used for export purposes only
    private List<AssessmentResult> assessmentResults;

    public GradeStatsDTO(Long sessionId, String sessionName) {
	this.sessionId = sessionId;
	this.sessionName = sessionName;
    }

    public GradeStatsDTO(Long sessionId, String sessionName, List<Float> grades) {
	this(grades);
	this.sessionId = sessionId;
	this.sessionName = sessionName;
    }

    public GradeStatsDTO(List<Float> grades) {
	if (grades == null || grades.isEmpty()) {
	    return;
	}

	float sum = 0;
	min = Float.MAX_VALUE;
	max = Float.MIN_VALUE;
	for (Float grade : grades) {
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
	median = grades.get(count / 2);
	if (count % 2 == 0) {
	    median = (median + grades.get(count / 2 - 1)) / 2;
	}

	modes = grades.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting())) // grade -> its count
		.entrySet().stream().collect(Collectors.groupingBy(Map.Entry::getValue, // convert to count -> grades that have this count (can be many)
			TreeMap::new, Collectors.mapping(Map.Entry::getKey, Collectors.toCollection(TreeSet::new))))
		.lastEntry().getValue(); // get top count
    }

    public Long getSessionId() {
	return sessionId;
    }

    public String getSessionName() {
	return sessionName;
    }

    public int getNumberOfLearners() {
	return numberOfLearners;
    }

    public void setNumberOfLearners(int numberOfLearners) {
	this.numberOfLearners = numberOfLearners;
    }

    public boolean isLeaderFinished() {
	return leaderFinished;
    }

    public void setLeaderFinished(boolean leaderFinished) {
	this.leaderFinished = leaderFinished;
    }

    public List<AssessmentResult> getAssessmentResults() {
	return assessmentResults;
    }

    public void setAssessmentResults(List<AssessmentResult> assessmentResults) {
	this.assessmentResults = assessmentResults;
    }

    public int getCount() {
	return count;
    }

    public Float getMin() {
	return min;
    }

    public String getMinString() {
	return GradeStatsDTO.format(min);
    }

    public Float getMax() {
	return max;
    }

    public String getMaxString() {
	return GradeStatsDTO.format(max);
    }

    public Float getAverage() {
	return average;
    }

    public String getAverageString() {
	return GradeStatsDTO.format(average);
    }

    public Float getMedian() {
	return median;

    }

    public String getMedianString() {
	return GradeStatsDTO.format(median);
    }

    public Collection<Float> getModes() {
	return modes;
    }

    public String getModesString() {
	if (modes == null || modes.isEmpty()) {
	    return "-";
	}

	Iterator<Float> modeIterator = modes.iterator();
	StringBuilder result = new StringBuilder(GradeStatsDTO.format(modeIterator.next()));
	while (modeIterator.hasNext()) {
	    result.append(", ").append(GradeStatsDTO.format(modeIterator.next()));
	}
	return result.toString();
    }

    private static String format(Float input) {
	return input == null ? "-" : NumberUtil.formatLocalisedNumber(input, (Locale) null, 2);
    }
}