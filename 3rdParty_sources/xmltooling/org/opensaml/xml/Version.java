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

package org.opensaml.xml;

/** Class for printing the version of this library. */
public final class Version {

    /** Name of the library. */
    private static final String NAME;

    /** Library version. */
    private static final String VERSION;

    /** Library major version number. */
    private static final int MAJOR_VERSION;

    /** Library minor version number. */
    private static final int MINOR_VERSION;

    /** Library micro version number. */
    private static final int MICRO_VERSION;

    /** Constructor. */
    private Version() {
    }

    /**
     * Main entry point to program.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        System.out.println(NAME + " version " + VERSION);
    }

    /**
     * Gets the name of the library.
     * 
     * @return name of the library
     */
    public static String getName() {
        return NAME;
    }

    /**
     * Gets the version of the library.
     * 
     * @return version of the library
     */
    public static String getVersion() {
        return VERSION;
    }

    /**
     * Gets the major version number of the library.
     * 
     * @return major version number of the library
     */
    public static int getMajorVersion() {
        return MAJOR_VERSION;
    }

    /**
     * Gets the minor version number of the library.
     * 
     * @return minor version number of the library
     */
    public static int getMinorVersion() {
        return MINOR_VERSION;
    }

    /**
     * Gets the micro version number of the library.
     * 
     * @return micro version number of the library
     */
    public static int getMicroVersion() {
        return MICRO_VERSION;
    }

    static {
        Package pkg = Version.class.getPackage();
        NAME = pkg.getImplementationTitle().intern();
        VERSION = pkg.getImplementationVersion().intern();
        String[] versionParts = VERSION.split("\\.");
        MAJOR_VERSION = Integer.parseInt(versionParts[0]);
        MINOR_VERSION = Integer.parseInt(versionParts[1]);
        MICRO_VERSION = Integer.parseInt(versionParts[2]);
    }
}