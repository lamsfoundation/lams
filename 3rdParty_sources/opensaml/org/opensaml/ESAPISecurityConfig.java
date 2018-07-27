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

package org.opensaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Pattern;

import org.owasp.esapi.SecurityConfiguration;

/**
 * Minimal implementation of OWASP ESAPI {@link SecurityConfiguration}, providing the support used within OpenSAML.
 */
public class ESAPISecurityConfig implements SecurityConfiguration {
    
    /** The singleton instance of this class. */
    private static volatile SecurityConfiguration instance;

    /** Constructor. */
    public ESAPISecurityConfig() {
    }
    
    /**
     * Factory method which implements the singleton pattern per the ESAPI reference impl.
     * 
     * @return the singleton instance of this class
     */
    public static SecurityConfiguration getInstance() {
        synchronized (ESAPISecurityConfig.class) {
            if ( instance == null ) {
                instance = new ESAPISecurityConfig();
            }
        }
        return instance;
    }

    /** {@inheritDoc} */
    public String getAccessControlImplementation() {
        return null;
    }

    /** {@inheritDoc} */
    public List getAdditionalAllowedCipherModes() {
        return null;
    }

    /** {@inheritDoc} */
    public List getAllowedExecutables() {
        return null;
    }

    /** {@inheritDoc} */
    public List getAllowedFileExtensions() {
        return null;
    }

    /** {@inheritDoc} */
    public int getAllowedFileUploadSize() {
        return 0;
    }

    /** {@inheritDoc} */
    public int getAllowedLoginAttempts() {
        return 0;
    }

    /** {@inheritDoc} */
    public boolean getAllowMixedEncoding() {
        return false;
    }

    /** {@inheritDoc} */
    public boolean getAllowMultipleEncoding() {
        return false;
    }

    /** {@inheritDoc} */
    public String getApplicationName() {
        return null;
    }

    /** {@inheritDoc} */
    public String getAuthenticationImplementation() {
        return null;
    }

    /** {@inheritDoc} */
    public String getCharacterEncoding() {
        return "UTF-8";
    }

    /** {@inheritDoc} */
    public String getCipherTransformation() {
        return null;
    }

    /** {@inheritDoc} */
    public List getCombinedCipherModes() {
        return null;
    }

    /** {@inheritDoc} */
    public List getDefaultCanonicalizationCodecs() {
        return null;
    }

    /** {@inheritDoc} */
    public String getDigitalSignatureAlgorithm() {
        return null;
    }

    /** {@inheritDoc} */
    public int getDigitalSignatureKeyLength() {
        return 0;
    }

    /** {@inheritDoc} */
    public boolean getDisableIntrusionDetection() {
        return false;
    }

    /** {@inheritDoc} */
    public String getEncoderImplementation() {
        return "org.owasp.esapi.reference.DefaultEncoder";
    }

    /** {@inheritDoc} */
    public String getEncryptionAlgorithm() {
        return null;
    }

    /** {@inheritDoc} */
    public String getEncryptionImplementation() {
        return null;
    }

    /** {@inheritDoc} */
    public int getEncryptionKeyLength() {
        return 0;
    }

    /** {@inheritDoc} */
    public String getExecutorImplementation() {
        return null;
    }

    /** {@inheritDoc} */
    public String getFixedIV() {
        return null;
    }

    /** {@inheritDoc} */
    public boolean getForceHttpOnlyCookies() {
        return false;
    }

    /** {@inheritDoc} */
    public boolean getForceHttpOnlySession() {
        return false;
    }

    /** {@inheritDoc} */
    public boolean getForceSecureCookies() {
        return false;
    }

    /** {@inheritDoc} */
    public boolean getForceSecureSession() {
        return false;
    }

    /** {@inheritDoc} */
    public String getHashAlgorithm() {
        return null;
    }

    /** {@inheritDoc} */
    public int getHashIterations() {
        return 0;
    }

    /** {@inheritDoc} */
    public String getHttpSessionIdName() {
        return null;
    }

    /** {@inheritDoc} */
    public String getHTTPUtilitiesImplementation() {
        return null;
    }

    /** {@inheritDoc} */
    public String getIntrusionDetectionImplementation() {
        return null;
    }

    /** {@inheritDoc} */
    public String getIVType() {
        return null;
    }

    /** {@inheritDoc} */
    public String getKDFPseudoRandomFunction() {
        return null;
    }

    /** {@inheritDoc} */
    public boolean getLenientDatesAccepted() {
        return false;
    }

    /** {@inheritDoc} */
    public boolean getLogApplicationName() {
        return false;
    }

    /** {@inheritDoc} */
    public boolean getLogEncodingRequired() {
        return false;
    }

    /** {@inheritDoc} */
    public String getLogFileName() {
        return null;
    }

    /** {@inheritDoc} */
    public String getLogImplementation() {
        return "org.owasp.esapi.reference.JavaLogFactory";
    }

    /** {@inheritDoc} */
    public int getLogLevel() {
        return 0;
    }

    /** {@inheritDoc} */
    public boolean getLogServerIP() {
        return false;
    }

    /** {@inheritDoc} */
    public byte[] getMasterKey() {
        return null;
    }

    /** {@inheritDoc} */
    public byte[] getMasterSalt() {
        return null;
    }

    /** {@inheritDoc} */
    public int getMaxHttpHeaderSize() {
        return 0;
    }

    /** {@inheritDoc} */
    public int getMaxLogFileSize() {
        return 0;
    }

    /** {@inheritDoc} */
    public int getMaxOldPasswordHashes() {
        return 0;
    }

    /** {@inheritDoc} */
    public String getPasswordParameterName() {
        return null;
    }

    /** {@inheritDoc} */
    public String getPreferredJCEProvider() {
        return null;
    }

    /** {@inheritDoc} */
    public Threshold getQuota(String eventName) {
        return null;
    }

    /** {@inheritDoc} */
    public String getRandomAlgorithm() {
        return null;
    }

    /** {@inheritDoc} */
    public String getRandomizerImplementation() {
        return null;
    }

    /** {@inheritDoc} */
    public long getRememberTokenDuration() {
        return 0;
    }

    /** {@inheritDoc} */
    public File getResourceFile(String filename) {
        return null;
    }

    /** {@inheritDoc} */
    public InputStream getResourceStream(String filename) throws IOException {
        return null;
    }

    /** {@inheritDoc} */
    public String getResponseContentType() {
        return null;
    }

    /** {@inheritDoc} */
    public int getSessionAbsoluteTimeoutLength() {
        return 0;
    }

    /** {@inheritDoc} */
    public int getSessionIdleTimeoutLength() {
        return 0;
    }

    /** {@inheritDoc} */
    public File getUploadDirectory() {
        return null;
    }

    /** {@inheritDoc} */
    public File getUploadTempDirectory() {
        return null;
    }

    /** {@inheritDoc} */
    public String getUsernameParameterName() {
        return null;
    }

    /** {@inheritDoc} */
    public String getValidationImplementation() {
        return null;
    }

    /** {@inheritDoc} */
    public Pattern getValidationPattern(String typeName) {
        return null;
    }

    /** {@inheritDoc} */
    public File getWorkingDirectory() {
        return null;
    }

    /** {@inheritDoc} */
    public boolean overwritePlainText() {
        return false;
    }

    /** {@inheritDoc} */
    public String setCipherTransformation(String cipherXform) {
        return null;
    }

    /** {@inheritDoc} */
    public void setResourceDirectory(String dir) {
    }

    /** {@inheritDoc} */
    public boolean useMACforCipherText() {
        return false;
    }

}