package edu.uoc.lti.deeplink.content;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * @author Created by xaracil@uoc.edu
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@ToString(callSuper = true)
public class LinkItem extends Item {
	private static String TYPE = "link";

	private String title;
	private String text;
	private final String url;
	private Image icon;
	private Image thumbnail;

	private Window window;
	private IFrame iframe;
	private Embed embed;

	public LinkItem(String url) {
		super(TYPE);
		this.url = url;
	}

	@Builder
	public LinkItem(String title, String url, String text, Image icon, Image thumbnail) {
		this(url);
		this.title = title;
		this.text = text;
		this.icon = icon;
		this.thumbnail = thumbnail;
	}
}
