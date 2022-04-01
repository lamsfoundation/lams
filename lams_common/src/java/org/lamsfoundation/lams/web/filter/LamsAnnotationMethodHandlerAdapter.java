package org.lamsfoundation.lams.web.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

/**
 * Addresses Spring vulnerability CVE-2022-22965
 * https://spring.io/blog/2022/03/31/spring-framework-rce-early-announcement
 */
public class LamsAnnotationMethodHandlerAdapter extends AnnotationMethodHandlerAdapter {

    @Override
    protected ServletRequestDataBinder createBinder(HttpServletRequest request, Object target, String objectName)
	    throws Exception {
	ServletRequestDataBinder binder = super.createBinder(request, target, objectName);
	String[] fields = binder.getDisallowedFields();
	List<String> fieldList = new ArrayList<>(fields != null ? Arrays.asList(fields) : Collections.emptyList());
	fieldList.addAll(Arrays.asList("class.*", "Class.*", "*.class.*", "*.Class.*"));
	binder.setDisallowedFields(fieldList.toArray(new String[] {}));
	return binder;
    }
}