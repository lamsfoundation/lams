/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */
package org.lamsfoundation.lams.tool.sbmt;

import java.io.Serializable;

import org.apache.log4j.Logger;
/** 
 * @hibernate.class table="tl_lasbmt11_instruction_files"
 * @serial  3555065437595925246L
*/
public class InstructionFiles implements Serializable,Cloneable{

	private static final long serialVersionUID = 3555065437595925246L;
	private static Logger log = Logger.getLogger(InstructionFiles.class);
	
	private Long fileID;
	private Long uuID;
	private Long versionID;
	private String type;
	/**
     * @hibernate.id generator-class="identity" type="java.lang.Long" column="file_id"
	 * @return Returns the fileID.
	 */
	public Long getFileID() {
		return fileID;
	}
	/**
	 * @param fileID The fileID to set.
	 */
	public void setFileID(Long fileID) {
		this.fileID = fileID;
	}
	/**
	 * 
     * @hibernate.property column="uuid" length="20"
	 * @return Returns the uuID.
	 */
	public Long getUuID() {
		return uuID;
	}
	/**
	 * @param uuID The uuID to set.
	 */
	public void setUuID(Long uuID) {
		this.uuID = uuID;
	}
	/**
	 * 
     * @hibernate.property column="version_id" length="20"
	 * @return Returns the versionID.
	 */
	public Long getVersionID() {
		return versionID;
	}
	/**
	 * @param versionID The versionID to set.
	 */
	public void setVersionID(Long versionID) {
		this.versionID = versionID;
	}
    public Object clone(){
		Object obj = null;
		try {
			obj = super.clone();
		} catch (CloneNotSupportedException e) {
			log.error("When clone " + InstructionFiles.class + " failed");
		}
		
		return obj;
	}
	/**
	 * @hibernate.property column="type" length="20"
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}
}
