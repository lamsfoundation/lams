/*
 * Copyright 2008 Google, Inc.
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

import java.io.IOException;
import java.net.URISyntaxException;

//TODO: move this interface into oauth-provider
/**
 * An algorithm to determine whether a message has a valid signature, a correct
 * version number, a fresh timestamp, etc.
 *
 * @author Dirk Balfanz
 * @author John Kristian
 */
public interface OAuthValidator {

    /**
     * Check that the given message from the given accessor is valid.
     * 
     * @throws OAuthException
     *             the message doesn't conform to OAuth. The exception contains
     *             information that conforms to the OAuth <a
     *             href="http://wiki.oauth.net/ProblemReporting">Problem
     *             Reporting extension</a>.
     * @throws IOException
     *             the message couldn't be read.
     * @throws URISyntaxException
     *             the message URL is invalid.
     */
    public void validateMessage(OAuthMessage message, OAuthAccessor accessor)
            throws OAuthException, IOException, URISyntaxException;

}
