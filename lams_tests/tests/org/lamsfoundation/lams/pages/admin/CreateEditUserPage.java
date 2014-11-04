/****************************************************************
 * Copyright (C) 2014 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */ 

package org.lamsfoundation.lams.pages.admin;

import org.lamsfoundation.lams.pages.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

/**
 * Create users page
 * 
 * @author Ernie Ghiglione (ernieg@lamsfoundation.org)
 *
 */
public class CreateEditUserPage extends AbstractPage {

	/** 
	 * Page fields 
	 * 
	 */
	
	@FindBy(name = "login")
	private WebElement login;

	@FindBy(name = "password")
	private WebElement password;

	@FindBy(name = "password2")
	private WebElement confirmPassword;

	@FindBy(name = "authenticationMethodId")
	private WebElement authenticationMethodId;

	@FindBy(name = "title")
	private WebElement title;

	@FindBy(name = "firstName")
	private WebElement firstName;

	@FindBy(name = "lastName")
	private WebElement lastName;

	@FindBy(name = "email")
	private WebElement email;

	@FindBy(name = "addressLine1")
	private WebElement addressLine1;

	@FindBy(name = "addressLine2")
	private WebElement addressLine2;

	@FindBy(name = "addressLine3")
	private WebElement addressLine3;

	@FindBy(name = "city")
	private WebElement city;
	
	@FindBy(name = "postcode")
	private WebElement postcode;
	
	@FindBy(name = "state")
	private WebElement state;
	
	@FindBy(name = "country")
	private WebElement country;
	
	@FindBy(name = "dayPhone")
	private WebElement dayPhone;
	
	@FindBy(name = "eveningPhone")
	private WebElement eveningPhone;
	
	@FindBy(name = "mobilePhone")
	private WebElement mobilePhone;
	
	@FindBy(name = "fax")
	private WebElement fax;
	
	@FindBy(name = "localeId")
	private WebElement localeId;
	
	@FindBy(name = "timeZone")
	private WebElement timeZone;
	
	@FindBy(name = "userCSSTheme")
	private WebElement userCSSTheme;
	
	@FindBy(name = "userFlashTheme")
	private WebElement userFlashTheme;
	
	@FindBy(id = "saveButton")
	private WebElement saveButton;

	@FindBy(id = "cancelButton")
	private WebElement cancelButton;
	
	@FindBy(className = "warning")
	private WebElement errorMessages;
	
	
	public CreateEditUserPage(WebDriver driver) {
		super(driver);
		
	}
	
	public CreateEditUserPage setLogin(String userName) {
		
		login.click();
		login.clear();
		login.sendKeys(userName);
		
		return PageFactory.initElements(driver, CreateEditUserPage.class);
	}
	
	public String getLogin() {
		return login.getAttribute("value");
	}
	
	public CreateEditUserPage setPassword(String passwordTxt) {
		
		password.click();
		password.clear();
		password.sendKeys(passwordTxt);
		
		return PageFactory.initElements(driver, CreateEditUserPage.class);
	}
	
	public String getPassword() {
		return password.getAttribute("value");
	}
	
	public CreateEditUserPage setPasswordConfirmation(String passwordTxt) {
		
		confirmPassword.click();
		confirmPassword.clear();
		confirmPassword.sendKeys(passwordTxt);
		
		return PageFactory.initElements(driver, CreateEditUserPage.class);
	}
	
	public String getPasswordConfirmation() {
		return confirmPassword.getAttribute("value");
	}
	
	public CreateEditUserPage setAuthenticationMethod(String authenticationType) {
		
		Select inputSelector = new Select(authenticationMethodId);
		
		inputSelector.selectByVisibleText(authenticationType);
		
		return PageFactory.initElements(driver, CreateEditUserPage.class);
	}
	
	public String getAuthenticationMethod() {
		
		Select inputSelector = new Select(authenticationMethodId);
		
		return inputSelector.getFirstSelectedOption().getText();
	}
	
	
	public CreateEditUserPage setTitle(String titletxt) {
		
		title.click();
		title.clear();
		title.sendKeys(titletxt);
		
		return PageFactory.initElements(driver, CreateEditUserPage.class);
	}
	
	public String getTitle() {
		return title.getAttribute("value");
	}
	
	public CreateEditUserPage setFirstName(String firstNameTxt) {
		
		firstName.click();
		firstName.clear();
		firstName.sendKeys(firstNameTxt);
		
		return PageFactory.initElements(driver, CreateEditUserPage.class);
	}
	
	public String getFirstName() {
		return firstName.getAttribute("value");
	}
	
	
	public CreateEditUserPage setLastName(String lastNameTxt) {
		
		lastName.click();
		lastName.clear();
		lastName.sendKeys(lastNameTxt);
		
		return PageFactory.initElements(driver, CreateEditUserPage.class);
	}
	
	public String getLastName() {
		return lastName.getAttribute("value");
	}
	
	
	public CreateEditUserPage setEmail(String emailTxt) {
		
		email.click();
		email.clear();
		email.sendKeys(emailTxt);
		
		return PageFactory.initElements(driver, CreateEditUserPage.class);
	}
	
	public String getEmail() {
		return email.getAttribute("value");
	}
	
	public CreateEditUserPage setAddressLine1(String address1) {
		
		addressLine1.click();
		addressLine1.clear();
		addressLine1.sendKeys(address1);
		
		return PageFactory.initElements(driver, CreateEditUserPage.class);
	}
	
	public String getAddressLine1() {
		return addressLine1.getAttribute("value");
	}
	
	public CreateEditUserPage setAddressLine2(String address2) {
		
		addressLine2.click();
		addressLine2.clear();
		addressLine2.sendKeys(address2);
		
		return PageFactory.initElements(driver, CreateEditUserPage.class);
	}
	
	public String getAddressLine2() {
		return addressLine2.getAttribute("value");
	}
	
	public CreateEditUserPage setAddressLine3(String address3) {
		
		addressLine3.click();
		addressLine3.clear();
		addressLine3.sendKeys(address3);
		
		return PageFactory.initElements(driver, CreateEditUserPage.class);
	}
	
	public String getAddressLine3() {
		return addressLine3.getAttribute("value");
	}
	
	
	public CreateEditUserPage setCity(String cityTxt) {
		
		city.click();
		city.clear();
		city.sendKeys(cityTxt);
		
		return PageFactory.initElements(driver, CreateEditUserPage.class);
	}
	
	public String getCity() {
		return city.getAttribute("value");
	}
	
	public CreateEditUserPage setPostcode(String postcodeTxt) {
		
		postcode.click();
		postcode.clear();
		postcode.sendKeys(postcodeTxt);
		
		return PageFactory.initElements(driver, CreateEditUserPage.class);
	}
	
	public String getPostcode() {
		return postcode.getAttribute("value");
	}
	
	
	public CreateEditUserPage setState(String stateTxt) {
		
		state.click();
		state.clear();
		state.sendKeys(stateTxt);
		
		return PageFactory.initElements(driver, CreateEditUserPage.class);
	}
	
	public String getState() {
		return state.getAttribute("value");
	}

	
	public CreateEditUserPage setCountry(String countrytxt) {
		
		country.click();
		country.clear();
		country.sendKeys(countrytxt);
		
		return PageFactory.initElements(driver, CreateEditUserPage.class);
	}
	
	public String getCountry() {
		return country.getAttribute("value");
	}
	
	
	public CreateEditUserPage setDayPhone(String dayPhoneTxt) {
		
		dayPhone.click();
		dayPhone.clear();
		dayPhone.sendKeys(dayPhoneTxt);
		
		return PageFactory.initElements(driver, CreateEditUserPage.class);
	}
	
	public String getDayPhone() {
		return dayPhone.getAttribute("value");
	}
	
	
	public CreateEditUserPage setEveningPhone(String eveningPhoneTxt) {
		
		eveningPhone.click();
		eveningPhone.clear();
		eveningPhone.sendKeys(eveningPhoneTxt);
		
		return PageFactory.initElements(driver, CreateEditUserPage.class);
	}
	
	public String getEveningPhone() {
		return eveningPhone.getAttribute("value");
	}
	
	
	public CreateEditUserPage setMobilePhone(String mobilePhoneTxt) {
		
		mobilePhone.click();
		mobilePhone.clear();
		mobilePhone.sendKeys(mobilePhoneTxt);
		
		return PageFactory.initElements(driver, CreateEditUserPage.class);
	}
	
	public String getMobilePhone() {
		return mobilePhone.getAttribute("value");
	}
	
	
	public CreateEditUserPage setFax(String faxTxt) {
		
		fax.click();
		fax.clear();
		fax.sendKeys(faxTxt);
		
		return PageFactory.initElements(driver, CreateEditUserPage.class);
	}
	
	public String getFax() {
		return fax.getAttribute("value");
	}
	
	
	public CreateEditUserPage setLocale(String localTxt) {
		
		Select inputSelector = new Select(localeId);
		
		inputSelector.selectByVisibleText(localTxt);

		
		return PageFactory.initElements(driver, CreateEditUserPage.class);
	}
	
	public String getLocale() {
		
		Select inputSelector = new Select(localeId);
		
		return inputSelector.getFirstSelectedOption().getText();
		
	}
	
	
	public CreateEditUserPage setTimeZone(String timeZoneTxt) {
		
		Select inputSelector = new Select(timeZone);
		
		inputSelector.selectByVisibleText(timeZoneTxt);

		
		return PageFactory.initElements(driver, CreateEditUserPage.class);
	}
	
	public String getTimeZone() {
		
		Select inputSelector = new Select(timeZone);
		
		return inputSelector.getFirstSelectedOption().getText();
		
	}
	
	
	public CreateEditUserPage setHtmlTheme(String htmlThemeTxt) {
		
		Select inputSelector = new Select(userCSSTheme);
		
		inputSelector.selectByVisibleText(htmlThemeTxt);

		
		return PageFactory.initElements(driver, CreateEditUserPage.class);
	}
	
	public String getHtmlTheme() {
		
		Select inputSelector = new Select(userCSSTheme);
		
		return inputSelector.getFirstSelectedOption().getText();
		
	}
	
	
	public CreateEditUserPage setFlashTheme(String flashThemeTxt) {
		
		Select inputSelector = new Select(userFlashTheme);
		
		inputSelector.selectByVisibleText(flashThemeTxt);

		
		return PageFactory.initElements(driver, CreateEditUserPage.class);
	}
	
	public String getFlashTheme() {
		
		Select inputSelector = new Select(userFlashTheme);
		
		return inputSelector.getFirstSelectedOption().getText();
		
	}

	public CreateEditUserPage saveTest() {
		
		saveButton.click();
		
		return PageFactory.initElements(driver, CreateEditUserPage.class);
	}
	
	
	public FindUsersPage save() {
		
		saveButton.click();
		
		return PageFactory.initElements(driver, FindUsersPage.class);
	}
	
	
}
