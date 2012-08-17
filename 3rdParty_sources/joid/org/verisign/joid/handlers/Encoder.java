/*
 *   Licensed to the Apache Software Foundation (ASF) under one
 *   or more contributor license agreements.  See the NOTICE file
 *   distributed with this work for additional information
 *   regarding copyright ownership.  The ASF licenses this file
 *   to you under the Apache License, Version 2.0 (the
 *   "License"); you may not use this file except in compliance
 *   with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an
 *   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *   KIND, either express or implied.  See the License for the
 *   specific language governing permissions and limitations
 *   under the License.
 *
 */
package org.verisign.joid.handlers;

import org.verisign.joid.Message;
import org.verisign.joid.OpenIdException;


/**
 * Encodes a specific OpenID {@link Message} type directly into a query string.
 *
 * @author <a href="mailto:akarasulu@apache.org">Alex Karasulu</a>
 */
public interface Encoder<E extends Message>
{
    StringBuilder encode( E message, EncodingMode mode, StringBuilder sb  ) throws OpenIdException;
}
