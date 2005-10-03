
package org.lamsfoundation.lams.tool.mc;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import junit.framework.TestCase;


/**
 * @author ozgurd
 *
 */
public abstract class AbstractMcTestCase extends TestCase
{
    protected ApplicationContext context;
    protected final long TEST_LESSON_ID=1;
    /**
     * @param name
     */
    public AbstractMcTestCase(String name)
    {
        super(name);
    }

    /**
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        context = new ClassPathXmlApplicationContext(getContextConfigLocation());
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
