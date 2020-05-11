/*
 * Copyright 2007 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.oauth;

import java.io.Serializable;

/**
 * Properties of an OAuth Service Provider.
 * 
 * @author John Kristian
 */
public class OAuthServiceProvider implements Serializable {

    private static final long serialVersionUID = 3306534392621038574L;

    public final String requestTokenURL;
    public final String userAuthorizationURL;
    public final String accessTokenURL;

    public OAuthServiceProvider(String requestTokenURL,
            String userAuthorizationURL, String accessTokenURL) {
        this.requestTokenURL = requestTokenURL;
        this.userAuthorizationURL = userAuthorizationURL;
        this.accessTokenURL = accessTokenURL;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result
		+ ((accessTokenURL == null) ? 0 : accessTokenURL.hashCode());
	result = prime * result
		+ ((requestTokenURL == null) ? 0 : requestTokenURL.hashCode());
	result = prime * result
		+ ((userAuthorizationURL == null) ? 0 : userAuthorizationURL.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	OAuthServiceProvider other = (OAuthServiceProvider) obj;
	if (accessTokenURL == null) {
	    if (other.accessTokenURL != null)
		return false;
	} else if (!accessTokenURL.equals(other.accessTokenURL))
	    return false;
	if (requestTokenURL == null) {
	    if (other.requestTokenURL != null)
		return false;
	} else if (!requestTokenURL.equals(other.requestTokenURL))
	    return false;
	if (userAuthorizationURL == null) {
	    if (other.userAuthorizationURL != null)
		return false;
	} else if (!userAuthorizationURL.equals(other.userAuthorizationURL))
	    return false;
	return true;
    }

}
