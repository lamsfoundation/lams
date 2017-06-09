package org.lamsfoundation.lams.tool.mc.dto;

public class SessionDTO {

    private Long sessionId;
    private String sessionName;

    // Used for statistics wanted calls
    private int numberLearners;
    private String minMark;
    private String maxMark;
    private String avgMark; 

    public SessionDTO() {
    }

    public Long getSessionId() {
	return sessionId;
    }

    public void setSessionId(Long sessionId) {
	this.sessionId = sessionId;
    }

    public String getSessionName() {
	return sessionName;
    }

    public void setSessionName(String sessionName) {
	this.sessionName = sessionName;
    }

    public int getNumberLearners() {
        return numberLearners;
    }

    public void setNumberLearners(int numberLearners) {
        this.numberLearners = numberLearners;
    }

    public String getMinMark() {
        return minMark;
    }

    public void setMinMark(String minMark) {
        this.minMark = minMark;
    }

    public String getMaxMark() {
        return maxMark;
    }

    public void setMaxMark(String maxMark) {
        this.maxMark = maxMark;
    }

    public String getAvgMark() {
        return avgMark;
    }

    public void setAvgMark(String avgMark) {
        this.avgMark = avgMark;
    }
}
