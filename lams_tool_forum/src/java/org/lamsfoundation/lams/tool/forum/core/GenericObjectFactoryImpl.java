package org.lamsfoundation.lams.tool.forum.core;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * Created by IntelliJ IDEA.
 * User: conradb
 * Date: 3/06/2005
 * Time: 15:42:12
 * To change this template use File | Settings | File Templates.
 */
public class GenericObjectFactoryImpl implements GenericObjectFactory {
   protected static ApplicationContext applicationContext;
   protected static GenericObjectFactory genericObjectFactory;
   protected static Logger log = Logger.getLogger(GenericObjectFactoryImpl.class);

    public static GenericObjectFactory getInstance() {
        if (genericObjectFactory == null) {
            genericObjectFactory = new GenericObjectFactoryImpl();
            addContext("/org/lamsfoundation/lams/applicationContext.xml");
            addContext("/org/lamsfoundation/lams/contentrepository/applicationContext.xml");
            addContext("/forumApplicationContext.xml");
        }
        return genericObjectFactory;
    }

    /** Get the Spring context with a local datasource and transaction, suitable
     * for testing. Does not use JTA so the transactions across projects could be
     * out. Do NOT use in production code - only for junit testing.
     */
    public static GenericObjectFactory getTestInstance() {
        if (genericObjectFactory == null) {
            genericObjectFactory = new GenericObjectFactoryImpl();
            addContext("/org/lamsfoundation/lams/localApplicationContext.xml");
            addContext("/org/lamsfoundation/lams/contentrepository/applicationContext.xml");
            addContext("/forumApplicationContext.xml");
        }
        return genericObjectFactory;
    }

    public static ApplicationContext addContext(String contextPath) {
            try {
                if (applicationContext == null) {
                    //There is no root context let this be the root context
                    applicationContext = new ClassPathXmlApplicationContext(contextPath);
                } else {
                    //There is a root application context merge the new context with the root and refresh the context
                    applicationContext = new ClassPathXmlApplicationContext(new String[] {contextPath}, true, applicationContext);
                }
            } catch (RuntimeException e) {
                throw e;
            }
        return applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * Lookup by name
     *
     */
    public Object lookup(String binding) throws FactoryException {
        try {
            return this.getApplicationContext().getBean(binding);
        } catch (RuntimeException e) {
            throw new FactoryException("unable to locate object in container", e);
        }
    }

    /**
     * Lookup by class type
     */
    public Object lookup(Class type) throws FactoryException {
        Object found = this.lookup(this.toFirstCharLowerCase(this.cutPackageFromName(type.getName())));
        return found;
    }

    protected String toFirstCharLowerCase(String name) {
        char[] chars = name.toCharArray();
        //make first char lowercase
        chars[0] = String.valueOf(chars[0]).toLowerCase().toCharArray()[0];
        return String.valueOf(chars);
    }

    protected String cutPackageFromName(String name) {
        if (name.indexOf(".") > -1) {
            name = name.substring(name.lastIndexOf(".") + 1);
        }
        return name;
    }
}
