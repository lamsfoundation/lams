package org.lamsfoundation.lams.tool.peerreview.dto;


public class PeerreviewStatisticsDTO {
    
    // use Number to be more flexible with what Hibernate will return, as it will build this DTO for us.
    private Number sessionId;
    private String sessionName;
    private Number numLearnersInSession;
    private Number numLearnersComplete;

//    PeerreviewStatisticsDTO(Number sessionId, String sessionName, Number numLearnersInSession, Number numLearnersComplete) {
//	this.sessionId = sessionId;
//	this.sessionName = sessionName;
//	this.numLearnersInSession = numLearnersInSession;
//	this.numLearnersComplete = numLearnersComplete;
//    }
    
    public Number getSessionId() {
        return sessionId;
    }
    public void setSessionId(Number sessionId) {
        this.sessionId = sessionId;
    }
    public String getSessionName() {
        return sessionName;
    }
    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }
    public Number getNumLearnersInSession() {
        return numLearnersInSession;
    }
    public void setNumLearnersInSession(Number numLearnersInSession) {
        this.numLearnersInSession = numLearnersInSession;
    }
    public Number getNumLearnersComplete() {
        return numLearnersComplete;
    }
    public void setNumLearnersComplete(Number numLearnersComplete) {
        this.numLearnersComplete = numLearnersComplete;
    }

    public String toString() {
	return sessionName;
    }
}
