package edu.uoc.lti.ags;

/**
 * @author Created by xaracil@uoc.edu
 */
public interface ScoreServiceClient {
	/**
	 * Publishes a score update. Tool platform may decide to change the result value based on the updated score.
	 * @param lineItemId Id of the line item
	 * @param score the score to update
	 * @return true if the score was updated. false otherwise
	 */
	boolean score(String lineItemId, Score score);
}
