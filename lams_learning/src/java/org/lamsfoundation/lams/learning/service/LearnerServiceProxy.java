package org.lamsfoundation.lams.learning.service;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


/**
 * <p>This class act as the proxy between web layer and service layer. It is
 * designed to decouple the presentation logic and business logic completely.
 * In this way, the presentation tier will no longer be aware of the changes in
 * service layer. Therefore we can feel free to switch the business logic
 * implementation.</p>
 *
 */
public class LearnerServiceProxy
{

    /**
     * Return the learner domain service object. It will delegate to the Spring
     * helper method to retrieve the proper bean from Spring bean factory.
     * @param servletContext the servletContext for current application
     * @return survey service object.
     */
    public static final ILearnerService getLearnerService(ServletContext servletContext)
    {
        return (ILearnerService)getLearnerDomainService(servletContext);
    }
    
    /**
     * Retrieve the proper Spring bean from bean factory. 
     * @param servletContext the servletContext for current application
     * @return the Spring service bean.
     */
    private static Object getLearnerDomainService(ServletContext servletContext)
    {
        WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        return wac.getBean("learnerService");
    }

    
}