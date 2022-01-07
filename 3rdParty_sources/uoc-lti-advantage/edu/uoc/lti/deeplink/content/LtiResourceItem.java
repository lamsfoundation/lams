package edu.uoc.lti.deeplink.content;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Created by xaracil@uoc.edu
 */
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@ToString(callSuper = true)
public class LtiResourceItem extends Item {
	private static String TYPE = "ltiResourceLink";

	/**
	 * String, plain text to use as the title or heading for content.
	 */
	private String title;

	/**
	 * Fully qualified url of the resource. If absent, the base LTI URL of the tool must be used for launch.
	 */
	private String url;

	private Presentation presentation;

	/**
	 * Fully qualified URL of an icon image to be placed with the file. A platform may not support the display of icons,
	 * but where it does, it may choose to use a local copy of the icon rather than linking to the URL provided (which
	 * would also allow it to resize the image to suit its needs).
	 */
	private Image icon;

	/**
	 * Fully qualified URL of a thumbnail image to be made a hyperlink. This allows the hyperlink to be opened within
	 * the platform from text or an image, or from both.
	 */
	private Image thumbnail;

	/**
	 * A map of key/value custom parameters. Those parameters must be included in the LtiResourceLinkRequest payload.
	 * Value may include substitution parameters as defined in the LTI Core Specification [LTI-13].
	 */
	private Map<String, Object> custom = new HashMap<>();

	private Window window;

	/**
	 * The iframe property indicates the resource can be embedded using an IFrame
	 */
	private IFrame iFrame;

	/**
	 * A lineItem object that indicates this activity is expected to receive scores; the platform may automatically
	 * create a corresponding line item when the resource link is created, using the maximum score as the default maximum points.
	 */
	private LineItem lineItem;


	/**
	 * Indicates the initial start and end time this activity should be made available to learners. A platform may choose
	 * to make an item not accessible by hiding it, or by disabling the link, or some other method which prevents the link
	 * from being opened by a learner. The initial value may subsequently be changed within the platform and the tool may use
	 * the ResourceLink.available.startDateTime and ResourceLink.available.endDateTime substitution parameters defined in
	 * LTI Core specification [LTI-13] within custom parameters to get the actual values at launch time.
	 */
	private Duration available;

	/**
	 * Indicates the initial start and end time submissions for this activity can be made by learners. The initial value
	 * may subsequently be changed within the platform and the tool may use the ResourceLink.submission.startDateTime and
	 * ResourceLink.submission.endDateTime substitution parameters defined in LTI Core specification [LTI-13] within
	 * custom parameters to get the actual values at launch time.
	 */
	private Duration submission;

	public LtiResourceItem() {
		super(TYPE);
	}

	@Builder
	public LtiResourceItem(String title, String url, Presentation presentation, Image icon, Image thumbnail, Window window, IFrame iFrame, Duration available, Duration submission) {
		this();
		this.title = title;
		this.url = url;
		this.presentation = presentation;
		this.icon = icon;
		this.thumbnail = thumbnail;
		this.window = window;
		this.iFrame = iFrame;
		this.available = available;
		this.submission = submission;
	}

	public void setCustom(String key, Object value) {
		this.custom.put(key, value);
	}

	public Object getCustom(String key) {
		return this.custom.get(key);
	}
}
