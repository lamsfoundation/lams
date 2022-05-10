package edu.uoc.lti.ags;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * @author Created by xaracil@uoc.edu
 */
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Score {
	/**
	 * Recipient of the score, usually a student. Must be present when publishing a score update through Scores
	 */
	@Getter
	private String userId;

	/**
	 * Current score received in the tool for this line item and user, in scale with scoreMaximum
	 */
	@Getter
	private double scoreGiven;

	/**
	 * Maximum possible score for this result; It must be present if scoreGiven is present.
	 */
	@Getter
	private double scoreMaximum;

	/**
	 * Comment visible to the student about this score.
	 */
	@Getter
	private String comment;

	/**
	 * Date and time when the score was modified in the tool. Should use subsecond precision.
	 */
	private Instant timeStamp;

	public String getTimeStamp() {
		return timeStamp != null ? timeStamp.toString() : null;
	}

	/**
	 * Indicate to the tool platform the status of the user towards the activity's completion.
	 * [ Initialized, Started, InProgress, Submitted, Completed ]
	 */
	private ActivityProgressEnum activityProgress;

	public String getActivityProgress() {
		return activityProgress.getValue();
	}

	/**
	 * Indicate to the platform the status of the grading process, including allowing to inform when human intervention is needed. A value other than FullyGraded may cause the tool platform to ignore the scoreGiven value if present.
	 * [ NotReady, Failed, Pending, PendingManual, FullyGraded ]
	 */
	private GradingProgressEnum gradingProgress;

	public String getGradingProgress() {
		return gradingProgress.getValue();
	}
}
