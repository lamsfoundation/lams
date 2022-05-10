package edu.uoc.lti.clientcredentials;

/**
 * @author Created by xaracil@uoc.edu
 */
public interface ClientCredentialsTokenBuilder {
	/**
	 * Builds a JWT token for the Client Credentials call
	 * @param request params of the Client Credentials call
	 * @return JWT token
	 */
	String build(ClientCredentialsRequest request);
}
