/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */

package org.apache.poi;

/**
 * Administrative class to keep track of the version number of the 
 *  POI release.
 *
 * This class implements the upcoming standard of having 
 *  org.apache.project-name.Version.getVersion() be a standard 
 *  way to get version information.
 */
public class Version {
	private static final String VERSION_STRING = "5.2.3";
	private static final String RELEASE_DATE = "20220909";

	/**
	 * Return the basic version string, of the form
	 *  nn.nn(.nn)
	 */
	public static String getVersion() {
		return VERSION_STRING;
	}

	/**
	 * Return the date of the release / build
	 */
	public static String getReleaseDate() {
		return RELEASE_DATE;
	}

	/**
	 * Name of product: POI
	 */
	public static String getProduct() {
		return "POI";
	}
	/**
	 * Implementation Language: Java
	 */
	public static String getImplementationLanguage() {
		return "Java";
	}

	/**
	 * Prints the version to the command line
	 */
	public static void main(String[] args) {
		System.out.println(
			"Apache " + getProduct() + " " +
			getVersion() + " (" + getReleaseDate() + ")"
		);
	}
}
