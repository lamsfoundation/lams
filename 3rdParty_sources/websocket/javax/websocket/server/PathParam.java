/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012-2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package javax.websocket.server;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation may be used to annotate method parameters on server endpoints
 * where a URI-template has been used in the path-mapping of the {@link ServerEndpoint}
 * annotation. The method parameter may be of type String, any Java primitive
 * type or any boxed version thereof. If a client URI matches the URI-template,
 * but the requested path parameter cannot be decoded, then the websocket's error
 * handler will be called.
 *
 * <p>For example:-
 * <pre><code>
 * &#64;ServerEndpoint("/bookings/{guest-id}")
 * public class BookingServer {
 * 
 *     &#64;OnMessage
 *     public void processBookingRequest(@PathParam("guest-id") String guestID, String message, Session session) {
 *         // process booking from the given guest here
 *     }
 * }
 * </code></pre>
 * 
 * <p>For example:-
 * <pre><code>
 * &#64;ServerEndpoint("/rewards/{vip-level}")
 * public class RewardServer {
 * 
 *     &#64;OnMessage
 *     public void processReward(@PathParam("vip-level") Integer vipLevel, String message, Session session) {
 *         // process reward here
 *     }
 * }
 * </code></pre>
 *
 * @author dannycoward
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface PathParam {

    /**
     * The name of the variable used in the URI-template. If the name does
     * not match a path variable in the URI-template, the value of the method parameter
     * this annotation annotates is {@code null}.
     *
     * @return the name of the variable used in the URI-template.
     */
    public String value();
}
