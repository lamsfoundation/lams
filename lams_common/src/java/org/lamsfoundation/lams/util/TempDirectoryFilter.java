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

package org.lamsfoundation.lams.util;

import java.io.File;
import java.io.FileFilter;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.util.zipfile.ZipFileUtil;

/**
 * Filename / date filter used by FileUtil, to find old expanded
 * directories. Used by FileUtil.cleanupOldFiles()
 */
public class TempDirectoryFilter implements FileFilter {

    private long newestDateToKeep = 0;
    public static final String zip_prefix = ZipFileUtil.prefix;
    public static final String tmp_prefix = FileUtil.prefix;
    private Logger log = null;

    public TempDirectoryFilter(long newestDateToKeep, Logger log) {
	this.newestDateToKeep = newestDateToKeep;
	this.log = log;
    }

    /*
     * Should this file be returned as part of the list?
     */
    @Override
    public boolean accept(File candidate) {
	if (log != null && log.isDebugEnabled()) {
	    log.debug("Checking file " + candidate + " date modified " + candidate.lastModified());
	}
	if (candidate.getName().startsWith(zip_prefix) || candidate.getName().startsWith(tmp_prefix)) {
	    if (!candidate.isDirectory()) {
		log.debug("Candidate file is not is named as we expected, but it is not a directory. Skipping file.");
		return false;
	    } else {
		return (candidate.lastModified() < newestDateToKeep);
	    }
	} else {
	    return false;
	}
    }

}