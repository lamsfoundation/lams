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

package org.lamsfoundation.lams.admin.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * <a href="ISpreadsheetService.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 * @author <a href="mailto:jliew@melcoe.mq.edu.au">Jun-Dir Liew</a>
 */
public interface IImportService {

    public static final String SEPARATOR = "|";
    public static final String IMPORT_HELP_PAGE = "Import+Users";
    public static final String IMPORT_GROUPS_HELP_PAGE = "Import+Groups";
    public static final String STATUS_IMPORT_TOTAL = "importTotal";
    public static final String STATUS_IMPORTED = "imported";
    public static final String STATUS_SUCCESSFUL = "successful";
    public static final String IMPORT_FILE = "file";
    public static final String IMPORT_RESULTS = "results";
    public static final int THRESHOLD = 500;

    /**
     * Returns true if spreadsheet contains user data.
     */
    public boolean isUserSpreadsheet(File fileItem) throws IOException;

    /**
     * Returns true if spreadsheet contains userorgrole data.
     */
    public boolean isRolesSpreadsheet(File fileItem) throws IOException;

    /**
     * Checks first row of spreadsheet and determines whether to parse as
     * a user or orgrole spreadsheet.
     */
    public List parseSpreadsheet(File fileItem, String sessionId) throws IOException;

    public List parseGroupSpreadsheet(File fileItem, String sessionId) throws IOException;

    /**
     * Returns number of rows found in spreadsheet.
     */
    public int getNumRows(File fileItem) throws IOException;

    /**
     * Returns message results from parsing list of users in spreadsheet.
     */
    public List parseUserSpreadsheet(File file, String sessionId) throws IOException;

    /**
     * Returns message results from parsing list of organisation memberships.
     */
    public List parseRolesSpreadsheet(File file, String sessionId) throws IOException;
}
