/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.tool.deploy;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.io.FileUtils;

/**
 * Parsers a file and does token replacement
 * 
 * @author chris
 */
public class FileTokenReplacer {
    protected File file;
    protected Map replacementMap;

    public static final String TOKEN_PREFIX = "${";
    public static final String TOKEN_SUFFIX = "}";
    public static final String TOKEN_REGEX_PREFIX = "\\$\\{";
    public static final String TOKEN_REGEX_SUFFIX = "\\}";

    public static final Pattern TOKEN_PATTERN = Pattern.compile("\\$\\{\\w+\\}");
    //pattern for ${letters}

    protected static String makeToken(final String tokenValue) {
	StringBuffer buf = new StringBuffer(TOKEN_PREFIX);
	buf.append(tokenValue);
	buf.append(TOKEN_SUFFIX);
	return buf.toString();
    }

    protected static boolean isValidToken(String token) {
	return TOKEN_PATTERN.matcher(token).matches();
    }

    /** Creates a new instance of FileTokenReplacer */
    public FileTokenReplacer(final File file, final Map<String, String> replacementMap) {
	if (file == null) {
	    throw new IllegalArgumentException("File is null");
	} else if (replacementMap == null) {
	    throw new IllegalArgumentException("Replacement map is null");
	} else if (!file.exists()) {
	    throw new IllegalArgumentException("File does not exist");
	}

	this.file = file;
	this.replacementMap = replacementMap;
    }

    public String replace() throws DeployException {
	String fileString = readFile();
	Set keys = replacementMap.keySet();
	Iterator keyIter = keys.iterator();
	while (keyIter.hasNext()) {
	    String key = (String) keyIter.next();
	    String value = (String) replacementMap.get(key);
	    String token = FileTokenReplacer.makeToken(key);
	    if (!FileTokenReplacer.isValidToken(token)) {
		throw new DeployException(key + " does not make a valid token (" + token + ")");
	    }

	    fileString = fileString.replaceAll(makeTokenRegex(key), value);
	}

	return fileString;
    }

    protected String makeTokenRegex(String tokenValue) throws DeployException {
	StringBuffer buf = new StringBuffer(TOKEN_REGEX_PREFIX);
	buf.append(tokenValue);
	buf.append(TOKEN_REGEX_SUFFIX);
	String regex = buf.toString();
	try {
	    Pattern.compile(regex);
	} catch (PatternSyntaxException psynex) {
	    throw new DeployException(tokenValue + " does not make a valid regex", psynex);
	}
	return regex;
    }

    protected String readFile() throws DeployException {

	try {
	    return FileUtils.readFileToString(file, "UTF8");
	} catch (IOException ioex) {
	    throw new DeployException("Could not read file", ioex);
	}

    }
}
