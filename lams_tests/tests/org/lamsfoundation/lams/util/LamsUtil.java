package org.lamsfoundation.lams.util;

import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
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
    	
    	public static String randomString(int length) {
    		
    		return RandomStringUtils.randomAlphabetic(length);
    		
    	}

    	public static String randomParagraph(int words) {
    		
    		String paragraph = "";
    		
    		for (int i = 0; i < words; i++) {
    			
    			int wordLength = Integer.parseInt(randInt(1, 15));
				
    			String txtTmp = randomString(wordLength);
    			
    			paragraph += txtTmp.toLowerCase() + " ";
    						    			
			}
    		
    		paragraph = StringUtils.capitalize(paragraph);
    		paragraph = paragraph.trim() + ".";
    		
    		return paragraph;
    	}
    	
    	public static String randomParagraphCharLimit(int charLimit) {
    		
    		String paragraph = "";
    		int wordLength;
    		String txtTmp;
    		
    		for (int i = 0; i < (charLimit); i =+ paragraph.length()) {
    			wordLength = Integer.parseInt(randInt(1, 15));
				
    			txtTmp = randomString(wordLength);
    			
    			paragraph += txtTmp.toLowerCase() + " ";
    		}

    		int minusOne = charLimit - 1;
    		paragraph = paragraph.substring(0, minusOne);
    		paragraph += ".";
    		
			return paragraph;
    		
    	}

		public static String randomText(int paragraphs) {
			
			String text = "";
			
			for (int i = 0; i < paragraphs; i++) {
				
				int wordsInParagraphs = Integer.parseInt(randInt(5, 80));
				
				String paragraph = randomParagraph(wordsInParagraphs);
				
				text += paragraph + "\n\n";
				
			}
			
			return text.trim();
		}
    	
}