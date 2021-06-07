package com.infelect.weathershopper.pythonanywhere.com;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class WeathershopperRunner extends TestMethodsForWeatherShopper {
	WebDriver driver;
	
	@BeforeMethod(alwaysRun = true)
	public void setup() {
		System.out.println("Starting to execute the test methods");
		System.setProperty("webdriver.chrome.driver", "C:/Users/ravisanb/eclipse-workspace/InfelictAssignment/driver/chromedriver.exe");
		driver = new ChromeDriver();
		driver.getTitle();
	}
	
	@Test
	public void testWeathershopper() throws InterruptedException {
		Assert.assertTrue(openChromeAndlaunchWebsite(), "Login Successful");
		Assert.assertTrue(selectMoisturizerOrSunscreenBasedOnTemperature(), "Selecting products based on condition is successful");
		Assert.assertTrue(shopForMoisturizerOrSunscreen(), "Shopping products is successful");
		Assert.assertTrue(addToCart(), "Adding the products to cart is Successful");
		Assert.assertTrue(verifyCartProducts(), "Products verification from cart is Successful");
		Assert.assertTrue(makePaymentForProductInCart() && verifyPaymentStatus(), "Payment Completed and Sucessfull");
	}
	
	@AfterMethod(alwaysRun = true)
	public void tearDown() {
		System.out.println("Test Method has been completed");
		System.setProperty("webdriver.chrome.driver", "C:/Users/ravisanb/eclipse-workspace/InfelictAssignment/driver/chromedriver.exe");
		driver.quit();
	}
	
}
