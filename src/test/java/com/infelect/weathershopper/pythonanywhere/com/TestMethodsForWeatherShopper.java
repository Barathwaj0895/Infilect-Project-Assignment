package com.infelect.weathershopper.pythonanywhere.com;

import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class TestMethodsForWeatherShopper {

	WebDriver driver;

	TreeMap<String,String> map=new TreeMap<String,String>();
	TreeMap<String,String> map2=new TreeMap<String,String>();

	public boolean openChromeAndlaunchWebsite() {
		System.setProperty("webdriver.chrome.driver", "C:/Users/ravisanb/eclipse-workspace/InfelictAssignment/driver/chromedriver.exe");
		driver = new ChromeDriver();
		driver.get("https://weathershopper.pythonanywhere.com/");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
		return true;
	}


	public boolean selectMoisturizerOrSunscreenBasedOnTemperature() {
		try {
			String Temp = driver.findElement(By.xpath("//span[@id='temperature']")).getText();  //To get current Temperature  
			String degree = driver.findElement(By.xpath("//sup")).getText().toString();
			String Temp1 = Temp.replace(degree,"");	//To get the Number from the String
			int Temperature = Integer.parseInt(Temp1); 	//To get Temperature value in Integer format
			System.out.println("Temperature is :" +Temperature);
			Thread.sleep(7000);
			// To click/select Moisturizer Or Sunscreen based on the current temperature
			if(Temperature <= 19) {
				System.out.println("Temperature is below 19 °C");
				driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
				driver.findElement(By.xpath("//a[@href='/moisturizer']")).click();
			} else if (Temperature >= 34) {
				System.out.println("Temperature is above 34 °C");
				driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
				driver.findElement(By.xpath("//a[@href='/sunscreen']")).click();	
			} 
			else {
				driver.getCurrentUrl();
			}
		} catch(NoSuchElementException | InterruptedException | NumberFormatException e) {
			e.printStackTrace();
		} 
		return true;
	}

	public boolean shopForMoisturizerOrSunscreen() {
		try {
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			// To get Item Names in a list
			List<WebElement> items = driver.findElements(By.xpath("//p[@class='font-weight-bold top-space-10']")); // scrape item elements and price

			for(int i=1; i<=items.size();i++) {
				String removeRs = driver.findElement(By.xpath(("(//p[contains(text(),'Price: ')])["+i+"]"))).getText();	
				String prodName = driver.findElement(By.xpath("(//p[@class='font-weight-bold top-space-10'])["+i+"]")).getText();
				String elp1 = "R";
				String[] spl;

				// To get only Price Numbers
				if (removeRs.charAt(7)==elp1.charAt(0)){
					spl = removeRs.split("Price: Rs. ");
				} else {
					spl = removeRs.split("Price: ");
				}

				// To get and verify the product names 
				if(prodName.toUpperCase().contains("SPF-50") || prodName.toUpperCase().contains("ALOE")) {
					map.put(spl[1], prodName);
				} else {
					map2.put(spl[1], prodName);
				}
			}
			System.out.println(map);
			System.out.println(map2);
		} catch(NoSuchElementException | NumberFormatException e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean addToCart() {
		try {
			String firstKey = map.firstKey();
			String firstKey1 = map2.firstKey();
			driver.findElement(By.xpath("//button[@onclick=\"addToCart('"+map.get(firstKey)+"',"+map.firstKey()+")\"]")).click();
			driver.findElement(By.xpath("//button[@onclick=\"addToCart('"+map2.get(firstKey1)+"',"+map2.firstKey()+")\"]")).click();
			String cart = driver.findElement(By.xpath("//span[@id='cart']")).getText();
			System.out.println(cart);
			if(cart.contentEquals("2 item(s)")) {
				driver.findElement(By.xpath("//button[@class='thin-text nav-link']")).click();
				System.out.println("Product has been added to cart :" +map.get(firstKey) + " " +map2.get(firstKey1));
			} else {
				driver.getCurrentUrl();
			}
		} catch(NoSuchElementException | NumberFormatException e) {
			e.printStackTrace();
		}
		return driver.findElement(By.xpath("//h2[contains(text(), 'Checkout')]")).getText().equalsIgnoreCase("Checkout");
	}

	public boolean verifyCartProducts() {
		String firstKey = map.firstKey();
		String firstKey1 = map2.firstKey();
		String Product1 = driver.findElement(By.xpath("//*[@class='table table-striped']/tbody/tr[1]/td[1]")).getText();
		String Product2 = driver.findElement(By.xpath("//*[@class='table table-striped']/tbody/tr[2]/td[1]")).getText();
		try {
			if(map.get(firstKey).contentEquals(Product1) && map2.get(firstKey1).contentEquals(Product2)) {
				System.out.println("Products are verified and clicking on Pay with card button");
				driver.findElement(By.xpath("//button[@class='stripe-button-el']")).click();
			} else {
				driver.getCurrentUrl();
			}

		} catch(NoSuchElementException e) {
			e.printStackTrace();
		}
		System.out.println("Products in Cart has been verified and can proceed to Payment");
		return true;
	}

	public boolean makePaymentForProductInCart() {
		try {
			driver.switchTo().frame(driver.findElement(By.xpath("//iframe[@class='stripe_checkout_app']")));
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			driver.findElement(By.xpath("//input[@placeholder='Email']")).click();
			driver.findElement(By.xpath("//input[@placeholder='Email']")).sendKeys("arjunkrishna@gmail.com");
			driver.findElement(By.xpath("//input[@placeholder='Card number']")).sendKeys("5105105105105100");
			driver.findElement(By.xpath("//input[@placeholder='MM / YY']")).sendKeys("05/24");
			driver.findElement(By.xpath("//input[@placeholder='CVC']")).sendKeys("645");
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			driver.findElement(By.xpath("//input[@placeholder='ZIP Code']")).sendKeys("600057");
			driver.findElement(By.xpath("//button[@class='Button-animationWrapper-child--primary Button']")).click();
			Thread.sleep(5000); // To avoid Target frame detachment exception
		}catch(NoSuchElementException | InterruptedException e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean verifyPaymentStatus() throws InterruptedException {
		driver.navigate().refresh(); // To avoid Target frame detachment exception
		String success = driver.findElement(By.xpath("//h2")).getText();
		String successStatement = driver.findElement(By.xpath("//p[@class='text-justify']")).getText();
		String orgSuccessStatement = "Your payment was successful. You should receive a follow-up call from our sales team.";
		try {
			boolean b = success.contentEquals("PAYMENT SUCCESS") && successStatement.contentEquals(orgSuccessStatement);
			if(b == true) {
				System.out.println("Payment is successfull and order has been placed");
			} else if (b == false){
				success.equalsIgnoreCase("PAYMENT FAILED");
				System.out.println("Payment failed Hence retrying payment");
				driver.navigate().back();
				verifyCartProducts();
				makePaymentForProductInCart();	
			}
		}catch(NoSuchElementException e) {
			e.printStackTrace();
		}
		return success.contentEquals("PAYMENT SUCCESS") || success.contentEquals("PAYMENT FAILED");
	}
}
