package org.verisign.joid.util;


/**
 * User: treeder
 * Date: Aug 9, 2007
 * Time: 2:31:00 PM
 */
public class DependencyUtils
{
    /**
     * This method will create a new instance of the class specified by className.
     * 
     * @param className
     * @return
     */
    public static Object newInstance( String className )
    {
        try
        {
            return Class.forName( className ).newInstance();
        }
        catch ( ClassNotFoundException e )
        {
            throw new IllegalArgumentException( "Not found " + className );
        }
        catch ( IllegalAccessException e )
        {
            throw new IllegalArgumentException( "No access to " + className );
        }
        catch ( InstantiationException e )
        {
            throw new IllegalArgumentException( "Cannot instantiate "
                    + className );
        }
    }
}
