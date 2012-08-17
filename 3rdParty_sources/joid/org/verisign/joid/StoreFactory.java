//
// (C) Copyright 2007 VeriSign, Inc.  All Rights Reserved.
//
// VeriSign, Inc. shall have no responsibility, financial or
// otherwise, for any consequences arising out of the use of
// this material. The program material is provided on an "AS IS"
// BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
// express or implied.
//
// Distributed under an Apache License
// http://www.apache.org/licenses/LICENSE-2.0
//

package org.verisign.joid;


import org.verisign.joid.util.DependencyUtils;


/**
 * Creates stores. JOID comes with the DbStore.
 */
public class StoreFactory
{
    private StoreFactory()
    {
    }


    /**
     * Returns whether the store type is implemented. 
     *
     * @param storeType the type to check.
     * @return true if the store type is implemented; false otherwise.
     */
    public static boolean hasType( String storeType )
    {
        return "db".equals( storeType );
    }


    /**
     * Gets a store implementation. 
     *
     * @param className the class name of the store to instantiate.
     * @return the store.
     * @throws IllegalArgumentException if the class doesn't exist or is
     *  not a store type.
     */
    public static IStore getInstance( String className )
    {
        return ( IStore ) DependencyUtils.newInstance( className );
    }

    /**
     * Gets a database store implementation of type 
     * {@link org.verisign.joid.db.DbStore}.
     *
     * @return the database store.
     */
    /*public static Store getDbInstance()
    {
    return DbStore.getInstance();
    }*/
}
