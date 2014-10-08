package org.lamsfoundation.lams.admin;

import java.util.concurrent.TimeUnit;


import org.lamsfoundation.lams.admin.util.AdminUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public class NewTest {
	
	private static WebDriver driver = null;		
	
  @Test
  public void f() {


	System.out.println(AdminUtil.rolesInCourse(driver, "test1", "Playpen"));
	  
  }
	@BeforeClass
	public void beforeClass() {

		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		// Login
		AdminUtil.loginAsSysadmin(driver);
		

	}

	@AfterClass
	public void afterClass() {

		// AdminUtil.logout(driver);
		//driver.quit();

	}

}
