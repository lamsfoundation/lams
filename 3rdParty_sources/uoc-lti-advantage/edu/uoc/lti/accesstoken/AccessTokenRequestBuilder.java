package edu.uoc.lti.accesstoken;

import java.io.IOException;

public interface AccessTokenRequestBuilder {
  /**
   * Returns the request for obtaing the access token
   * @param request request to build
   * @return string with the request to pass to the authorization server
   * @throws IOException if something went wrong getting the access token
   */
  String build(AccessTokenRequest request) throws IOException;

  /**
   * Gets the content type of the request to the authorization server
   * @return the content type of the request to the authorization server
   */
  String getContentType();
}