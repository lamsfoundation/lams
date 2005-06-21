
package org.lamsfoundation.lams.tool.qa;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import junit.framework.TestCase;


/**
 * @author Jacky Fang
 *
 */
public abstract class AbstractQaTestCase extends TestCase
{
    protected ApplicationContext context;
    protected final long TEST_LESSON_ID=1;
    /**
     * @param name
     */
    public AbstractQaTestCase(String name)
    {
        super(name);
    }

    /**
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        System.out.println("AbstractQaTestCase: done with abstract super setup");
        context = new ClassPathXmlApplicationContext(getContextConfigLocation());
        System.out.println("AbstractQaTestCase: context" + context);
    }

	protected abstract String[] getContextConfigLocation();
    /**
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

}
