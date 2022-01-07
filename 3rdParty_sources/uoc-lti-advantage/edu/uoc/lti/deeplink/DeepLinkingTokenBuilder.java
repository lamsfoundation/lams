package edu.uoc.lti.deeplink;

/**
 * @author Created by xaracil@uoc.edu
 */
public interface DeepLinkingTokenBuilder {
	/**
	 * Builds a JWT token for the DeepLink call
	 * @param response params of the DeepLink call
	 * @return JWT token
	 */
	String build(DeepLinkingResponse response);
}
