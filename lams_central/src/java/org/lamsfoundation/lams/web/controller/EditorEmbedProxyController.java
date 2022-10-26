package org.lamsfoundation.lams.web.controller;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Provides media embedding custom service for sites which are not supported by the default embed provider.
 */
@Controller
@RequestMapping("/embed")
public class EditorEmbedProxyController {
    private static final Logger logger = Logger.getLogger(EditorEmbedProxyController.class);

    private static final String EMBED_PROVIDER_PROPERTY_NAME = "lams_embed_provider";
    private static final String CUSTOM_EMBED_PROPERTY_NAME = "lams_embed_custom_domains";
    private static final String EMBED_PROVIDER_DEFAULT_HTTP_PROTOCOL = "http:";
    private static final Pattern PROPERTY_VALUE_EXTRACTOR = Pattern.compile("'(.*?)'");

    private final String targetEmbedUrl;
    private final Set<String> customEmbedDomains;
    private final ObjectNode customEmbedJsonTemplate;

    public EditorEmbedProxyController() {
	String targetEmbedUrl = null;
	String customEmbedDomains = null;

	try {
	    // extract properties from ckconfig_custom.js
	    List<String> ckeditorProperties = Files
		    .readAllLines(Paths.get(Configuration.get(ConfigurationKeys.LAMS_EAR_DIR),
			    FileUtil.LAMS_CENTRAL_WAR_DIR, "includes", "javascript", "ckconfig_custom.js"));
	    for (String property : ckeditorProperties) {
		if (property == null) {
		    continue;
		}
		if (property.startsWith(EMBED_PROVIDER_PROPERTY_NAME)) {
		    Matcher matcher = PROPERTY_VALUE_EXTRACTOR.matcher(property);
		    if (matcher.find()) {
			targetEmbedUrl = matcher.group(1);
		    }
		} else if (property.startsWith(CUSTOM_EMBED_PROPERTY_NAME)) {
		    Matcher matcher = PROPERTY_VALUE_EXTRACTOR.matcher(property);
		    if (matcher.find()) {
			customEmbedDomains = matcher.group(1);
		    }
		}
	    }
	} catch (Exception e) {
	    logger.error("Error while parsing CKEditor custom config file", e);
	}

	if (StringUtils.isBlank(targetEmbedUrl)) {
	    this.targetEmbedUrl = null;
	} else {
	    targetEmbedUrl = targetEmbedUrl.strip();
	    if (targetEmbedUrl.startsWith("//")) {
		targetEmbedUrl = EMBED_PROVIDER_DEFAULT_HTTP_PROTOCOL + targetEmbedUrl;
	    }
	    this.targetEmbedUrl = targetEmbedUrl;
	}

	if (StringUtils.isBlank(customEmbedDomains)) {
	    this.customEmbedDomains = null;
	    this.customEmbedJsonTemplate = null;
	} else {
	    this.customEmbedDomains = Arrays.stream(customEmbedDomains.split(",")).map(String::strip)
		    .collect(Collectors.toUnmodifiableSet());
	    this.customEmbedJsonTemplate = JsonNodeFactory.instance.objectNode();
	    // at the moment only video embeds work
	    this.customEmbedJsonTemplate.put("type", "video");
	    this.customEmbedJsonTemplate.put("version", "1.0");
	    // placeholder [URL] will be replaced for each request
	    this.customEmbedJsonTemplate.put("html",
		    "<div style='left: 0; width: 100%; height: 0; position: relative; padding-bottom: 75%;'>"
			    + "<iframe src='[URL]' style='top: 0; left: 0; width: 100%; height: 100%; "
			    + "position: absolute; border: 0;'></iframe></div>");

	}
	if (this.targetEmbedUrl == null || this.customEmbedDomains == null) {
	    logger.info("CKEditor embed custom properties are not set");
	}
    }

    @GetMapping("")
    public ResponseEntity<String> proxyEmbed(HttpServletRequest request) {
	if (targetEmbedUrl == null) {
	    return ResponseEntity.status(HttpStatus.NOT_FOUND)
		    .body(EMBED_PROVIDER_PROPERTY_NAME + " property is not set in ckeditor_custom.js");
	}
	String embeddedUrl = request.getParameter("url");
	if (StringUtils.isBlank(embeddedUrl)) {
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("\"url\" parameter not found");
	}

	for (String customEmbedDomain : customEmbedDomains) {
	    if (embeddedUrl.contains(customEmbedDomain)) {
		String callback = request.getParameter("callback");
		if (StringUtils.isBlank(callback)) {
		    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("\"callback\" parameter not found");
		}

		// copy JSON template and construct response
		ObjectNode responseJSON = this.customEmbedJsonTemplate.deepCopy();
		responseJSON.put("url", embeddedUrl);
		responseJSON.put("html",
			this.customEmbedJsonTemplate.get("html").asText().replace("[URL]", embeddedUrl));
		StringBuilder responseString = new StringBuilder(callback).append(" && ").append(callback).append("(")
			.append(responseJSON.toString()).append(");");
		return ResponseEntity.ok(responseString.toString());
	    }
	}

	// if the requested media is not in custom domain list, just redirect to the original embed provider
	String callUrl = WebUtil.appendParameterDeliminator(targetEmbedUrl) + request.getQueryString();
	return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(callUrl)).build();
    }
}