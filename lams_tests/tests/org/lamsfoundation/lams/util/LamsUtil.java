package org.lamsfoundation.lams.util;

import java.util.Random;

import org.lamsfoundation.lams.LamsConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LamsUtil {

        public static void loginAs(WebDriver driver, String username, String password) {

                // Get to login page
                driver.get(LamsConstants.TEST_SERVER_URL);
                driver.findElement(By.name("j_username")).sendKeys(username);
                driver.findElement(By.name("j_password")).sendKeys(password);
                driver.findElement(By.id("loginButton")).click();

        }

        public static void logout(WebDriver driver) {

                driver.get(LamsConstants.LOGOUT_URL);

        }
        
    	public static String randInt(int min, int max) {

    		// NOTE: Usually this should be a field rather than a method
    		// variable so that it is not re-seeded every call.
    		Random rand = new Random();

    		// nextInt is normally exclusive of the top value,
    		// so add 1 to make it inclusive
    		int randomNum = rand.nextInt((max - min) + 1) + min;

    		String randomNumString = String.valueOf(randomNum);

    		return randomNumString;
    	}
    	
}