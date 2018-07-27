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


package org.lamsfoundation.lams.contentrepository;

import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

import org.lamsfoundation.lams.contentrepository.exception.AccessDeniedException;
import org.lamsfoundation.lams.contentrepository.exception.InvalidParameterException;
import org.lamsfoundation.lams.contentrepository.exception.ItemNotFoundException;
import org.lamsfoundation.lams.contentrepository.exception.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.exception.RepositoryRuntimeException;
import org.lamsfoundation.lams.contentrepository.exception.ValueFormatException;

/**
 * IVersionedNodeAdmin represents the full functionality for a versioned node.
 *
 * IVersionedNodeAdmin is a protected interface - should only be used within
 * the repository package. It exists to allow easier replacement of the
 * SimpleVersionedNode class in line with the Spring framework philosophy
 * of programming to interfaces.
 */
public interface IVersionedNodeAdmin extends IVersionedNode {
    /**
     * Sets the property to a value, based on the specified type, after converting
     * from the string.
     *
     * @param name
     *            The name of a property of this node
     * @param value
     *            The value to be assigned
     * @param type
     *            The type of the property
     * @throws ValueFormatException
     *             if the type or format of a value
     *             is incompatible with the type of the specified property or if
     *             value is incompatible with (i.e. can not be converted to) type.
     */
    public void setProperty(String name, Object value, int type) throws RepositoryCheckedException;

    /**
     * Sets the property to a STRING value.
     * To remove a property, set the value to null.
     *
     * @param name
     *            The name of a property of this node
     * @param value
     *            The value to be assigned
     */
    public void setProperty(String name, String value) throws RepositoryCheckedException;

    /**
     * Sets the property to a BOOLEAN value. Creates the property if required.
     * To remove a property, set the value to null.
     *
     * @param name
     *            The name of a property of this node
     * @param value
     *            The value to be assigned
     */
    public void setProperty(String name, boolean value) throws RepositoryCheckedException;

    /**
     * Sets the property to a DOUBLE value.
     * To remove a property, set the value to null.
     *
     * @param name
     *            The name of a property of this node
     * @param value
     *            The value to be assigned
     */
    public void setProperty(String name, double value) throws RepositoryCheckedException;;

    /**
     * Sets the property to a LONG value.
     * To remove a property, set the value to null.
     *
     * @param name
     *            The name of a property of this node
     * @param value
     *            The value to be assigned
     */
    public void setProperty(String name, long value) throws RepositoryCheckedException;

    /**
     * Sets the property a CALENDAR value.
     * To remove a property, set the value to null.
     *
     * @param name
     *            The name of a property of this node
     * @param value
     *            The value to be assigned
     */
    public void setProperty(String name, Calendar value) throws RepositoryCheckedException;

    /**
     * Set the file, passed in as an inputstream. The stream will be closed
     * when the file is saved. Only nodes of type FILENODE can have a file!
     * 
     * @param iStream
     *            mandatory
     * @param filename
     *            mandatory
     * @param mimeType
     *            optional
     */
    public abstract void setFile(InputStream iStream, String filename, String mimeType)
	    throws InvalidParameterException;

    /**
     * Delete this node and all its versions, returning a list of the files
     * that could not be deleted properly. If it is a package node, the child
     * nodes will be deleted.
     *
     * A file missing from the disk is ignored, allowing nodes with lost files
     * to be deleted.
     *
     * @throws AccessDeniedException
     *             if ticket doesn't allow this action
     * @throws ItemNotFoundException
     *             if node with uuid cannot be found
     * @throws InvalidParameterException
     *             if a required parameter is missing
     * @throws RepositoryRuntimeException
     *             if any internal errors have occured
     * @return the list of file(paths) that could not be deleted. The db entries
     *         will have been deleted but these files could not be deleted.
     */
    public List deleteNode();

    /**
     * Delete the current version of this node, returning a list of the files
     * that could not be deleted properly. If it is a package node, the child
     * nodes will be deleted.
     *
     * If after deleting the version, we find that there a no other versions
     * for a node, then delete the node.
     *
     * A file missing from the disk is ignored, allowing nodes with lost files
     * to be deleted.
     *
     * @return the list of file(paths) that could not be deleted. The db entries
     *         will have been deleted but these files could not be deleted.
     */
    public List deleteVersion();

}