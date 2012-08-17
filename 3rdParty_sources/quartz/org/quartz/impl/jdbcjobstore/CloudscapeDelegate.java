/* 
 * Copyright 2004-2005 OpenSymphony 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy 
 * of the License at 
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0 
 *   
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations 
 * under the License.
 * 
 */

/*
 * Previously Copyright (c) 2001-2004 James House
 */

package org.quartz.impl.jdbcjobstore;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;

/**
 * <p>
 * This is a driver delegate for the Cloudscape database, not surprisingly, 
 * it is known to work with Derby as well.
 * </p>
 * 
 * @author James House
 * @author Sridhar Jawaharlal, Srinivas Venkatarangaiah
 */
public class CloudscapeDelegate extends StdJDBCDelegate {
    /**
     * <p>
     * Create new CloudscapeDelegate instance.
     * </p>
     * 
     * @param logger
     *          the logger to use during execution
     * @param tablePrefix
     *          the prefix of all table names
     */
    public CloudscapeDelegate(Log log, String tablePrefix, String instanceId) {
        super(log, tablePrefix, instanceId);
    }

    /**
     * <p>
     * Create new CloudscapeDelegate instance.
     * </p>
     * 
     * @param logger
     *          the logger to use during execution
     * @param tablePrefix
     *          the prefix of all table names
     * @param useProperties
     *          useProperties flag
     */
    public CloudscapeDelegate(Log log, String tablePrefix, String instanceId,
            Boolean useProperties) {
        super(log, tablePrefix, instanceId, useProperties);
    }

    //---------------------------------------------------------------------------
    // protected methods that can be overridden by subclasses
    //---------------------------------------------------------------------------

    /**
     * <p>
     * This method should be overridden by any delegate subclasses that need
     * special handling for BLOBs. The default implementation uses standard
     * JDBC <code>java.sql.Blob</code> operations.
     * </p>
     * 
     * @param rs
     *          the result set, already queued to the correct row
     * @param colName
     *          the column name for the BLOB
     * @return the deserialized Object from the ResultSet BLOB
     * @throws ClassNotFoundException
     *           if a class found during deserialization cannot be found
     * @throws IOException
     *           if deserialization causes an error
     */
    protected Object getObjectFromBlob(ResultSet rs, String colName)
    throws ClassNotFoundException, IOException, SQLException
    {
        Object obj = null;

        byte[] inputBytes = rs.getBytes(colName);

        if (null != inputBytes) {
            ByteArrayInputStream bais = new
            ByteArrayInputStream(inputBytes); 

            ObjectInputStream in = new ObjectInputStream(bais);
            obj = in.readObject();
            in.close();
        }

        return obj;
    }    
}

// EOF
