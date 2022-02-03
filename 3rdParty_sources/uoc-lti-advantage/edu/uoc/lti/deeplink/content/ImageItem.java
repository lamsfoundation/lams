package edu.uoc.lti.deeplink.content;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Created by xaracil@uoc.edu
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@ToString(callSuper = true)
public class ImageItem extends Item {
	private static String TYPE = "image";

	/**
	 * Fully qualified URL of the image.
	 */
	private final String url;

	/**
	 * String, plain text to use as the title or heading for content.
	 */
	private String title;

	/**
	 * String, plain text description of the content item intended to be displayed to all users who can access the item.
	 */
	private String text;

	/**
	 * Fully qualified URL of an icon image to be placed with the file. A platform may not support the display of icons,
	 * but where it does, it may choose to use a local copy of the icon rather than linking to the URL provided
	 * (which would also allow it to resize the image to suit its needs).
	 */
	private Image icon;

	/**
	 * Fully qualified URL of a thumbnail image to be made a hyperlink. This allows the hyperlink to be opened within the
	 * platform from text or an image, or from both.
	 */
	private Image thumbnail;

	/**
	 * Integer representing the width in pixels of the image.
	 */
	private int width;

	/**
	 * Integer representing the height in pixels of the image.
	 */
	private int height;


	public ImageItem(String url) {
		super(TYPE);
		this.url = url;
	}

	@Builder
	public ImageItem(String url, String title, String text, Image icon, Image thumbnail, int width, int height) {
		this(url);
		this.title = title;
		this.text = text;
		this.icon = icon;
		this.thumbnail = thumbnail;
		this.width = width;
		this.height = height;
	}
}
