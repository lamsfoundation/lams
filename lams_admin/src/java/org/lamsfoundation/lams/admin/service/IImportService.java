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

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

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
    public static final String IMPORT_FILE = "file";
    public static final String IMPORT_RESULTS = "results";
    public static final int THRESHOLD = 500;

    /**
     * Returns true if spreadsheet contains user data.
     *
     * @param fileItem
     * @return
     * @throws IOException
     */
    public boolean isUserSpreadsheet(MultipartFile fileItem) throws IOException;

    /**
     * Returns true if spreadsheet contains userorgrole data.
     *
     * @param fileItem
     * @return
     * @throws IOException
     */
    public boolean isRolesSpreadsheet(MultipartFile fileItem) throws IOException;

    /**
     * Checks first row of spreadsheet and determines whether to parse as
     * a user or orgrole spreadsheet.
     *
     * @param fileItem
     * @throws IOException
     */
    public List parseSpreadsheet(MultipartFile fileItem, String sessionId) throws IOException;

    /**
     *
     * @param fileItem
     * @return
     * @throws IOException
     */
    public List parseGroupSpreadsheet(MultipartFile fileItem, String sessionId) throws IOException;

    /**
     * Returns number of rows found in spreadsheet.
     *
     * @param fileItem
     * @return
     * @throws IOException
     */
    public int getNumRows(MultipartFile fileItem) throws IOException;

    /**
     * Returns message results from parsing list of users in spreadsheet.
     *
     * @param file
     * @param sessionId
     * @return
     * @throws IOException
     */
    public List parseUserSpreadsheet(MultipartFile file, String sessionId) throws IOException;

    /**
     * Returns message results from parsing list of organisation memberships.
     *
     * @param file
     * @param sessionId
     * @return
     * @throws IOException
     */
    public List parseRolesSpreadsheet(MultipartFile file, String sessionId) throws IOException;
}
