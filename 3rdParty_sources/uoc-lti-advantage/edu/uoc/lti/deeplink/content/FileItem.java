package edu.uoc.lti.deeplink.content;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

/**
 * @author Created by xaracil@uoc.edu
 */
@Setter
@ToString(callSuper = true)
public class FileItem extends Item {
	private static String TYPE = "file";

	/**
	 * String, plain text to use as the title or heading for content.
	 */
	@Getter
	private String title;

	/**
	 * Fully qualified URL of the resource. This link may be short-lived or expire after 1st use.
	 */
	@Getter
	private final String url;

	/**
	 * String, plain text to use as the title or heading for content.
	 */
	@Getter
	private String text;

	/**
	 * Fully qualified URL of an icon image to be placed with the file.
	 * A platform may not support the display of icons, but where it does, it may choose to use a local copy of the icon
	 * rather than linking to the URL provided (which would also allow it to resize the image to suit its needs).
	 */
	@Getter
	private Image icon;

	/**
	 * Fully qualified URL of a thumbnail image to be made a hyperlink.
	 * This allows the hyperlink to be opened within the platform from text or an image, or from both.
	 */
	@Getter
	private Image thumbnail;

	/**
	 *
	 */
	@Getter
	private String mediaType;

	/**
	 * ISO 8601 Date and time [ISO8601]. The URL will be available until this time. No guarantees after that. (e.g. 2014-03-05T12:34:56Z).
	 */
	private Instant expiresAt;

	public String getExpiresAt() {
		return expiresAt != null ? expiresAt.toString() : null;
	}

	public FileItem(String url) {
		super(TYPE);
		this.url = url;
	}

	@Builder
	public FileItem(String title, String url, String mediaType, Instant expiresAt) {
		this(url);
		this.title = title;
		this.mediaType = mediaType;
		this.expiresAt = expiresAt;
	}
}
