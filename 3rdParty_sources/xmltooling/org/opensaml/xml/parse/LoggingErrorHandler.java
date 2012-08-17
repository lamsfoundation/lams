/*
 * Licensed to the University Corporation for Advanced Internet Development, 
 * Inc. (UCAID) under one or more contributor license agreements.  See the 
 * NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The UCAID licenses this file to You under the Apache 
 * License, Version 2.0 (the "License"); you may not use this file except in 
 * compliance with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensaml.xml.parse;

import org.slf4j.Logger;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * A SAX error handler that logs errors a {@link Logger} before rethrowing them.
 */
public class LoggingErrorHandler implements ErrorHandler {

    /** Error logger. */
    private Logger log;

    /**
     * Constructor.
     * 
     * @param logger logger errors will be written to
     */
    public LoggingErrorHandler(Logger logger) {
        log = logger;
    }

    /** {@inheritDoc} */
    public void error(SAXParseException exception) throws SAXException {
        log.error("XML Parsing Error:", exception);
        throw exception;
    }

    /** {@inheritDoc} */
    public void fatalError(SAXParseException exception) throws SAXException {
        log.error("XML Parsing Error", exception);
        throw exception;
    }

    /** {@inheritDoc} */
    public void warning(SAXParseException exception) throws SAXException {
        log.warn("XML Parsing Error", exception);
        throw exception;
    }
}