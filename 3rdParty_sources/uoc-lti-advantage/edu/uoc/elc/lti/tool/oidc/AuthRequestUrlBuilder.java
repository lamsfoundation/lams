package edu.uoc.elc.lti.tool.oidc;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author xaracil@uoc.edu
 */
public class AuthRequestUrlBuilder {
	public static String build(String url, LoginResponse params) {
		StringBuilder authUrl = new StringBuilder(url + "?");
		authUrl.append("scope=" + params.getScope());
		authUrl.append("&response_type=" + params.getResponse_type());
		authUrl.append("&client_id=" + params.getClient_id());
		try {
			authUrl.append("&redirect_uri=" + URLEncoder.encode(params.getRedirect_uri(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			authUrl.append("&redirect_uri=" + params.getRedirect_uri());
		}
		authUrl.append("&login_hint=" + params.getLogin_hint());
		authUrl.append("&state=" + params.getState());
		authUrl.append("&response_mode=" + params.getResponse_mode());
		authUrl.append("&nonce=" + params.getNonce());
		authUrl.append("&prompt=" + params.getPrompt());
		if (params.getLti_message_hint() != null) {
			authUrl.append("&lti_message_hint=" + params.getLti_message_hint());
		}
		return authUrl.toString();
	}
}
