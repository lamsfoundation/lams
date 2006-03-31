/* 
 Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 USA

 http://www.gnu.org/licenses/gpl.txt 
 */
package org.lamsfoundation.lams.tool.rsrc.ims;

/**
 * A ContentPackageConverter converts an IMS Content Package into the
 * data needed for our tool. The only implementation initially done was
 * SimpleContentPackageCoverter.
 * 
 * The creation method of the converter (which is unique to each converter)
 * will need to parse the package and have the data ready for the "get"
 * calls.
 *
 * Note: Classes that implement this interface should be normal POJOS,
 * not singletons. This allows them to have instance data.
 */
public interface IContentPackageConverter {
    
    /**
     * @return Returns the defaultItem.
     */
    public abstract String getDefaultItem();

    /**
     * @return Returns the description.
     */
    public abstract String getDescription();

    /**
     * @return Returns the organzationXML.
     */
    public abstract String getOrganzationXML();

    /**
     * @return Returns the schema.
     */
    public abstract String getSchema();

    /**
     * @return Returns the title.
     */
    public abstract String getTitle();
}