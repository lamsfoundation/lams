package org.lamsfoundation.lams.tool.scribe.util;

import java.util.Set;
import java.util.TreeMap;

import org.lamsfoundation.lams.tool.scribe.dto.ScribeSessionDTO;
import org.lamsfoundation.lams.tool.scribe.model.ScribeSession;

public class ScribeUtils {

    public static int calculateVotePercentage(int numberOfVotes, int numberOfLearners) {
	Float v = new Float(numberOfVotes);
	Float l = new Float(numberOfLearners);

	Float result = (v / l) * 100;

	return result.intValue();
    }

    /**
     * Create a map of the reports (in ScribeSessionDTO format) for all the other groups/sessions, where the key is the
     * group/session name. The code ensures that the session name is unique, adding the session id if necessary. It will
     * only include the finalized reports.
     */
    public static TreeMap<String, ScribeSessionDTO> getReportDTOs(ScribeSession scribeSession) {
	Set<ScribeSession> scribeSessionsForContent = scribeSession.getScribe().getScribeSessions();
	TreeMap<String, ScribeSessionDTO> otherScribeSessions = new TreeMap<String, ScribeSessionDTO>();
	for (ScribeSession session : scribeSessionsForContent) {
	    if (!session.equals(scribeSession) && session.isForceComplete()) {

		// ensure a unique group name, as we will sort on that
		String sessionKey = session.getSessionName();
		if (otherScribeSessions.containsKey(sessionKey)) {
		    sessionKey = sessionKey + "(" + session.getSessionId().toString() + ")";
		}

		ScribeSessionDTO dto = new ScribeSessionDTO(session);
		otherScribeSessions.put(sessionKey, dto);
	    }
	}
	return otherScribeSessions;
    }

}
