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


package org.lamsfoundation.lams.contentrepository.dao;

import java.io.InputStream;

import org.lamsfoundation.lams.contentrepository.exception.FileException;

/**
 * Manages the reading and writing of files to the repository directories.
 *
 * @author Fiona Malikoff
 */
public interface IFileDAO {

    /**
     * Write out a file to the repository. Closes the stream. Overwrites any existing files.
     * 
     * @return the path to which the file was written
     */
    public String writeFile(Long uuid, Long versionId, InputStream is) throws FileException;

    /**
     * Gets a file from the repository.
     */
    public InputStream getFile(Long uuid, Long versionId) throws FileException;

    /**
     * Delete a file from the repository. If this makes the directory
     * empty, then the directory should be deleted.
     * 
     * @return 1 if deleted okay, 0 if file not found, -1 if file found but a delete error occured.
     */
    public int delete(Long uuid, Long versionId) throws FileException;

    /**
     * Get the actual path of the file ie the path on disk
     */
    public String getFilePath(Long uuid, Long versionId) throws FileException;

    /**
     * Is there a file on disk?
     * <p>
     * Used to validate file nodes.
     */
    public boolean fileExists(Long uuid, Long versionId) throws FileException;
}