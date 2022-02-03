package edu.uoc.elc.lti.platform.ags;

import edu.uoc.elc.lti.exception.UnauthorizedAgsCallException;
import edu.uoc.lti.ags.Score;
import edu.uoc.lti.ags.ScoreServiceClient;
import lombok.RequiredArgsConstructor;

/**
 * @author xaracil@uoc.edu
 */
@RequiredArgsConstructor
public class GenericScoreServiceClient {
	private final boolean canScore;
	private final ScoreServiceClient scoreServiceClient;

	public boolean score(String lineItemId, Score score) {
		if (!canScore) {
			throw new UnauthorizedAgsCallException("score");
		}
		return scoreServiceClient.score(lineItemId, score);
	}
}
