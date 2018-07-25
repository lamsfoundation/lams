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
package javax.websocket;

/**
 * A general exception that occurs when trying to encode a custom object to a string or binary message.
 *
 * @author dannycoward
 */
public class EncodeException extends Exception {
    private final Object object;
    private static final long serialVersionUID = 006;

    /**
     * Constructor with the object being encoded, and the reason why it failed to be.
     *
     * @param object  the object that could not be encoded.
     * @param message the reason for the failure.
     */
    public EncodeException(Object object, String message) {
        super(message);
        this.object = object;
    }


    /**
     * Constructor with the object being encoded, and the reason why it failed to be, and the cause.
     *
     * @param object  the object that could not be encoded.
     * @param message the reason for the failure.
     * @param cause   the cause of the problem.
     */
    public EncodeException(Object object, String message, Throwable cause) {
        super(message, cause);
        this.object = object;
    }


    /**
     * Return the Object that could not be encoded.
     *
     * @return the object.
     */
    public Object getObject() {
        return this.object;
    }
}
