package org.lamsfoundation.lams.tool.notebook.core;


import java.lang.reflect.Method;
import java.util.Properties;

import com.thoughtworks.selenium.HttpCommandProcessor;
import com.thoughtworks.selenium.SeleneseTestCase;


public class SeleniumFlexTestCase extends SeleneseTestCase {



	private Properties testProperties;

	private String testServer;
	private int listnerPort;
	private String browser;
	private String host;
	private String baseURL;
	

	public void setUpTest()
	{
		testServer = testProperties.getProperty("seleniumServer.location");
		listnerPort = Integer.parseInt(testProperties.getProperty("seleniumServer.listnerport"));
		browser = "*" + testProperties.getProperty("seleniumServer.browser");
		host = testProperties.getProperty("environment.webapp.host");
		baseURL = host + testProperties.getProperty("environment.webapp.dir");
	}
	
	protected String getData(String property)
	{
		return testProperties.getProperty(property);
	}
	
	protected void setDataSource(Properties testProperties)
	{
		testProperties = this.testProperties;
	}

	
	protected DefaultSeleniumFlex getDefaultSeleniumFlex()
	{
		HttpCommandProcessor proc = new HttpCommandProcessor(testServer, listnerPort, browser, baseURL);
		DefaultSeleniumFlex selenium = new DefaultSeleniumFlex(proc);
		
		return selenium;
	}
	
	protected void waitForFlexExists(String objectID, int timeout, DefaultSeleniumFlex selenium) throws Exception
	{
		while(timeout > 0 && ! selenium.getFlexExists(objectID).equals("true")) {
			Thread.sleep(1000);
			timeout--;
		}
		if(timeout == 0){
			throw new Exception("waitForFlexExists flex object:" + objectID + " Timed Out"); 
		}
	}
	
	protected void waitForFlexVisible(String objectID, int timeout, DefaultSeleniumFlex selenium) throws Exception
	{
		while(timeout > 0 && ! selenium.getFlexVisible(objectID).equals("true")) {
			Thread.sleep(1000);
			timeout--;
		}
		if(timeout == 0){
			throw new Exception("waitForFlexVisible flex object:" + objectID + " Timed Out"); 
		}
	}
	
	protected void openModule(String module, DefaultSeleniumFlex selenium) throws Exception
	{
		// open a module and wait for it to complete loading
		String coreBlank = testProperties.getProperty("environment.webapp.host") + "selenium-server/core/Blank.html";
		if(selenium.getLocation().equals(coreBlank))
		{
			selenium.open(testProperties.getProperty("environment.webapp.page"));
			// just buffer with a little time to stop the test from stepping on its own feet
			Thread.sleep(3000);
			waitForFlexExists("loadModCombo", 20, selenium);
			selenium.flexSelectIndex("loadModCombo", module);
		}
		else 
		{
			selenium.refresh();
			Thread.sleep(3000);
			waitForFlexExists("loadModCombo", 20, selenium);
			selenium.flexSelectIndex("loadModCombo", module);
		}
	}
	
	protected void completeCreditCard(DefaultSeleniumFlex selenium, String ancestor) throws Exception
	{
		// just fills out a credit card component
		selenium.flexSelect(ancestor + "creditCardTypeCombo", "Visa");
		selenium.flexType(ancestor + "creditCardNameInput", "Fergal Test");
		selenium.flexType(ancestor + "creditCardNumberInput", "4111 1111 1111 1111");
		selenium.flexSelect(ancestor + "creditCardExpiryMonthCombo", "02");
		selenium.flexSelect(ancestor + "creditCardExpiryYearCombo", "2012");
		selenium.flexType(ancestor + "creditCardSecurityNumberInput", "123");
	}
	
	protected void doLogin(DefaultSeleniumFlex selenium, String loginId, String pin) throws Exception
	{
		// do a login on the authenticated module
		openModule(testProperties.getProperty("module.login"), selenium);
		waitForFlexExists("loginIDInput", 20, selenium);
		selenium.flexType("loginIDInput", loginId);
		selenium.flexType("pinInput", pin);
		assertTrue(selenium.getFlexEnabled("loginBtn") == "true");
		selenium.flexClick("loginBtn");
		waitForFlexVisible("_AuthenticatedModule_Button3", 20, selenium);
	}
	
	protected void waitForOneFailForTheOther(DefaultSeleniumFlex selenium,
			String methodWait, String targetWait, String valueWait,
			String methodFail, String targetFail, String valueFail,
			int timeOut) throws Exception
	{
		Method methWait = selenium.getClass().getMethod(methodWait);
		Method methFail = selenium.getClass().getMethod(methodFail);
		
		while(methWait.invoke(selenium, targetWait).equals(valueWait) && 
				methFail.invoke(selenium, targetFail).equals(valueFail) && 
				timeOut > 0)
		{
			Thread.sleep(1000);
			timeOut--;
		}
		
		if(methFail.invoke(selenium, targetFail).equals(valueFail))
		{
			throw new Exception("waitForOneFailForTheOther: " + methodWait + 
					" target: " + targetFail + " - value: " + valueFail);
		}
		
		if(timeOut == 0)
		{
			throw new Exception("waitForOneFailForTheOther: Timed Out");
		}
	}
	
	protected void continueFailExists(DefaultSeleniumFlex selenium, String continueIf, String failIf) throws Exception
	{
		// pass and continue if one element exists, fail and stop if another elements exists 
		int timeOut = 60;
		
		while(selenium.getFlexExists(continueIf).equals("false") && 
				selenium.getFlexExists(failIf).equals("false") && 
				timeOut > 0)
		{
			Thread.sleep(1000);
			timeOut--;
		}
		assertTrue(selenium.getFlexExists(continueIf) == "true");
		assertTrue(selenium.getFlexExists(failIf) == "false");
	}
}

