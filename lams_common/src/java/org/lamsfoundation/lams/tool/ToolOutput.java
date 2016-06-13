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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.tool;

/**
 * An output for a tool.
 *
 */
public class ToolOutput {

    private String name;
    private String description;
    private ToolOutputValue value;
    
    /**
     * User Id. Used when tool returns multiple tool outputs for different users.
     */
    private Integer userId;

    /**
     * Create a ToolOutput based on a Boolean. This will create a value of type OUTPUT_BOOLEAN
     * 
     * @param name
     *            mandatory
     * @param description
     *            mandatory
     * @param val
     *            mandatory
     */
    public ToolOutput(String name, String description, Boolean val) {
	this.name = name;
	this.description = description;
	this.value = new ToolOutputValue(val);
    }

    /**
     * Create a ToolOutput based on a Long. This will create a value of type OUTPUT_LONG.
     * 
     * @param name
     *            mandatory
     * @param description
     *            mandatory
     * @param val
     *            mandatory
     */
    public ToolOutput(String name, String description, Long val) {
	this.name = name;
	this.description = description;
	this.value = new ToolOutputValue(val);
    }

    /**
     * Create a ToolOutput based on a Integer. This will create a value of type OUTPUT_LONG.
     * 
     * @param name
     *            mandatory
     * @param description
     *            mandatory
     * @param val
     *            mandatory
     */
    public ToolOutput(String name, String description, Integer val) {
	this.name = name;
	this.description = description;
	this.value = new ToolOutputValue(val);
    }

    /**
     * Create a ToolOutput based on a Double. This will create a value of type OUTPUT_DOUBLE.
     * 
     * @param name
     *            mandatory
     * @param description
     *            mandatory
     * @param val
     *            mandatory
     */
    public ToolOutput(String name, String description, Double val) {
	this.name = name;
	this.description = description;
	this.value = new ToolOutputValue(val);
    }

    /**
     * Create a ToolOutput based on a Integer. This will create a value of type OUTPUT_DOUBLE.
     * 
     * @param name
     *            mandatory
     * @param description
     *            mandatory
     * @param val
     *            mandatory
     */
    public ToolOutput(String name, String description, Float val) {
	this.name = name;
	this.description = description;
	this.value = new ToolOutputValue(val);
    }

    /**
     * Create a ToolOutput based on a String. This will create a value of type OUTPUT_STRING.
     * 
     * @param name
     *            mandatory
     * @param description
     *            mandatory
     * @param val
     *            mandatory
     */
    public ToolOutput(String name, String description, String val) {
	this.name = name;
	this.description = description;
	this.value = new ToolOutputValue(val);
    }

    /**
     * Create a ToolOutput based on a String. It tries to convert it to the type defined by the type.
     * 
     * @throws ToolOutputFormatException
     *             If unable to convert the value to the requested type.
     * @param name
     *            mandatory
     * @param description
     *            mandatory
     * @param val
     *            mandatory
     */
    public ToolOutput(String name, String description, String val, OutputType type) {
	this.name = name;
	this.description = description;
	this.value = new ToolOutputValue(val, type);
    }

    /**
     * Create a Complex ToolOutput based on an Object. This will create a value of type OUTPUT_COMPLEX. The
     * junk parameter is to ensure this method isn't "accidently" in place of one of the other constructors. ie to stop
     * accidental bugs!
     * 
     * @param name
     *            mandatory
     * @param description
     *            mandatory
     * @param val
     *            mandatory
     * @param junk
     *            ignored
     */
    public ToolOutput(String name, String description, Object val, Boolean junk) {
	this.name = name;
	this.description = description;
	this.value = new ToolOutputValue(val, junk);
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public ToolOutputValue getValue() {
	return value;
    }

    public void setValue(ToolOutputValue value) {
	this.value = value;
    }
    
    public Integer getUserId() {
	return userId;
    }

    public void setUserId(Integer userId) {
	this.userId = userId;
    }
}
