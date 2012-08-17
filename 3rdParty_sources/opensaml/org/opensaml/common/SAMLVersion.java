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

package org.opensaml.common;

/**
 * A type safe SAML version enumeration.
 */
public final class SAMLVersion {

    /** SAML version 1.0. */
    public static final SAMLVersion VERSION_10 = new SAMLVersion(1, 0);

    /** SAML Version 1.1. */
    public static final SAMLVersion VERSION_11 = new SAMLVersion(1, 1);

    /** SAML Version 2.0. */
    public static final SAMLVersion VERSION_20 = new SAMLVersion(2, 0);

    /** Major version number. */
    private int majorVersion;

    /** Minor version number. */
    private int minorVersion;

    /** String representation of the version. */
    private String versionString;

    /**
     * Constructor.
     * 
     * @param major SAML major version number
     * @param minor SAML minor version number
     */
    private SAMLVersion(int major, int minor) {
        majorVersion = major;
        minorVersion = minor;

        versionString = majorVersion + "." + minorVersion;
    }

    /**
     * Gets the SAMLVersion given the major and minor version number.
     * 
     * @param majorVersion major version number
     * @param minorVersion minor version number
     * 
     * @return the SAMLVersion
     */
    public static final SAMLVersion valueOf(int majorVersion, int minorVersion) {
        if (majorVersion == 1) {
            if (minorVersion == 0) {
                return SAMLVersion.VERSION_10;
            } else if (minorVersion == 1) {
                return SAMLVersion.VERSION_11;
            }
        } else if (majorVersion == 2) {
            if (minorVersion == 0) {
                return SAMLVersion.VERSION_20;
            }
        }

        return new SAMLVersion(majorVersion, minorVersion);
    }

    /**
     * Gets the SAMLVersion for a given version string, such as "2.0".
     * 
     * @param version SAML version string
     * 
     * @return SAMLVersion for the given string
     */
    public static final SAMLVersion valueOf(String version) {
        String[] components = version.split("\\.");
        return valueOf(Integer.valueOf(components[0]), Integer.valueOf(components[1]));
    }

    /**
     * Gets the major version of the SAML version.
     * 
     * @return the major version of the SAML version
     */
    public int getMajorVersion() {
        return majorVersion;
    }

    /**
     * Gets the minor version of the SAML version.
     * 
     * @return the minor version of the SAML version
     */
    public int getMinorVersion() {
        return minorVersion;
    }

    /** {@inheritDoc} */
    public String toString() {
        return versionString;
    }
}
