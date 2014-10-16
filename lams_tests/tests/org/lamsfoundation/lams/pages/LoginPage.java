package org.lamsfoundation.lams.pages;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class LoginPage extends AbstractPage {
	
	@FindBy(name = "j_username")
	private WebElement usernameField;
	
	@FindBy(name = "j_password")
	private WebElement passwordField;
	
	@FindBy(id = "loginButton")
	private WebElement loginButton;
	
	public LoginPage(WebDriver driver) {
		super(driver);
	}
	
	public IndexPage loginAs(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        submitLogin();
        return PageFactory.initElements(driver, IndexPage.class);
		
	}
	
	public void enterUsername (String username) {
		usernameField.clear();
		usernameField.sendKeys(username);
		
	}
	
	public void enterPassword (String password) {
		passwordField.clear();
		passwordField.sendKeys(password);
	}

	public void submitLogin() {
		loginButton.click();
	}
}
