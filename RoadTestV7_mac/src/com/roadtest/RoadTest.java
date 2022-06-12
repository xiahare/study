package com.roadtest;
import java.util.Date;
import java.util.Locale;
//import java.lang.System.Logger;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

class OSType 
{
    private static String OS = System.getProperty("os.name", "unknown").toLowerCase(Locale.ROOT);

    public static boolean isWindows()
    {
        return OS.contains("win");
    }

    public static boolean isMac()
    {
        return OS.contains("mac");
    }

    public static boolean isUnix()
    {
        return OS.contains("nux");
    }
}

public class RoadTest {
	
    private static long timeoutMs = 10*60*1000;
    private static long nextTimeout = System.currentTimeMillis();
    

    private static void run(String[] args) {
	  
	  nextTimeout = System.currentTimeMillis() + timeoutMs;

      String configure=args[0];
      Utils utils = new Utils(configure);
      String last_name = utils.getConfig("last_name");
      String licence_number = utils.getConfig("licence_number");
      String keyword = utils.getConfig("keyword");
      String location = utils.getConfig("location");
      String nearby_location = utils.getConfig("nearby_location");
      String expected_month = utils.getConfig("expected_month");
      String is_reschedule = utils.getConfig("is_reschedule");
      String email_or_sms = utils.getConfig("email_or_sms");
      
      boolean rescheduling = true;
      if( is_reschedule!=null && !"".equals(is_reschedule) && "false".equals(is_reschedule)) {
      	rescheduling = false;
      }

      Var var = new Var();
      if (OSType.isWindows()) {
        System.setProperty("webdriver.chrome.driver", "resources/chromedriver.exe");
      } else if (OSType.isMac()) {
        System.setProperty("webdriver.chrome.driver", "resources/chromedriver");
      } else {
         System.out.println("not supported OS");
         return ;
      }

      WebDriver driver = new ChromeDriver();
      String url = var.loginUrl;
      driver.get(url);
      WebDriverWait wait = new WebDriverWait(driver, 10);
//      String url = var.url;
//      driver.get(url);
//      driver.manage().window().maximize();
//      WebDriverWait wait = new WebDriverWait(driver, 10);
//      String mainWinHander = driver.getWindowHandle();
//      Set<String> handles = driver.getWindowHandles();
//      for(String handle : handles)
//      {
//        if(!mainWinHander.equals(handle))
//        {
//
//          WebDriver popup = driver.switchTo().window(handle);
//          popup.close();
//        }
//      }
//      wait.until(ExpectedConditions.elementToBeClickable(By.xpath(var.book_online_xpath)));
//      s(1);        
//      driver.findElement(By.xpath(var.book_online_xpath)).click();		
//      String FirstHandle = driver.getWindowHandle();    
//      for(String winHandle : driver.getWindowHandles()) {    
//        if (winHandle.equals(FirstHandle)) {				
//          continue;
//        }
//        driver.switchTo().window(winHandle);            
//        System.out.println(driver.getCurrentUrl());  
//        
//        break;  
//      } 	
//      wait.until(ExpectedConditions.elementToBeClickable(By.xpath(var.next_button_xpath)));
//      s(2);
//      driver.findElement(By.xpath(var.next_button_xpath)).click(); 	
      wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(var.last_name_xpath)));
      s(1);
      driver.findElement(By.xpath(var.last_name_xpath)).sendKeys(last_name);
      //wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(var.licence_Number_xpath)));
      //s(1);
      driver.findElement(By.xpath(var.licence_Number_xpath)).sendKeys(licence_number);
      //wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(var.keyword_xpath)));
      //s(1);
      driver.findElement(By.xpath(var.keyword_xpath)).sendKeys(keyword);		
      wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(var.agree_checkbox_xpath)));
      //s(1);
      driver.findElement(By.xpath(var.agree_checkbox_xpath)).click();			
      wait.until(ExpectedConditions.elementToBeClickable(By.xpath(var.signin_button_xpath)));
      s(1);
      driver.findElement(By.xpath(var.signin_button_xpath)).click(); 
      if(rescheduling) {
      	
      	wait.until(ExpectedConditions.elementToBeClickable(By.xpath(var.reschedule_appointment_xpath)));
      	driver.findElement(By.xpath(var.reschedule_appointment_xpath)).click(); 	
      	wait.until(ExpectedConditions.elementToBeClickable(By.xpath(var.reschedule_confirm_xpath)));
      	driver.findElement(By.xpath(var.reschedule_confirm_xpath)).click();		
      }
      s(1);
      wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(var.location_xpath)));

      boolean retryNeeded = false;
      do
      {
        try
        {
          retryNeeded = false;
          driver.findElement(By.xpath(var.location_xpath)).clear();
          driver.findElement(By.xpath(var.location_xpath)).sendKeys(location);
          driver.findElement(By.xpath(var.location_xpath)).sendKeys(Keys.SPACE);
          driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
          String address_xpath = var.address_xpath.replace("address", location);
          wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(address_xpath)));
          driver.findElement(By.xpath(address_xpath)).click();
        }
        catch (StaleElementReferenceException e)
        {
          //System.out.println("StaleElementReferenceException - Retrying");
          retryNeeded = true;
        }
        try{
          Thread.sleep(2000);
        }catch(Exception e){
          System.out.println(e);
        }
      } while (retryNeeded);		
      wait.until(ExpectedConditions.elementToBeClickable(By.xpath(var.search_xpath)));
      driver.findElement(By.xpath(var.search_xpath)).click();
      String nearby_location_xpath = var.nearby_location_xpath.replace("nearby_location", nearby_location);
      wait.until(ExpectedConditions.elementToBeClickable(By.xpath(nearby_location_xpath)));
      boolean booked = false;
      while(!booked){
        try{
        	if(timeout()) {
        		System.out.println("Re-login at :"+ new Date());
        		driver.quit();
        		run(args);
        		break;
        	}
          driver.findElement(By.xpath(nearby_location_xpath)).click();
          WebElement data_title = driver.findElement(By.xpath(var.date_title_xpath));
          if (data_title != null){
            String content = data_title.getText();
            String[] expected_months = expected_month.trim().split("[,]");
            for (String month : expected_months){
              if (content.contains(month)){
              	WebElement appointTime = driver.findElement(By.xpath(var.appoinment_xpath));
              	appointTime.click();
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath(var.review_appoinment_xpath)));
                driver.findElement(By.xpath(var.review_appoinment_xpath)).click();
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath(var.review_next_button_xpath)));
                driver.findElement(By.xpath(var.review_next_button_xpath)).click();
                if( "sms".equals(email_or_sms)) {
              	  wait.until(ExpectedConditions.elementToBeClickable(By.xpath(var.sms_verification_xpath)));
                    driver.findElement(By.xpath(var.sms_verification_xpath)).click();
                }else {
              	  wait.until(ExpectedConditions.elementToBeClickable(By.xpath(var.email_verification_xpath)));
                    driver.findElement(By.xpath(var.email_verification_xpath)).click();
                }
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath(var.send_button_xpath)));
                driver.findElement(By.xpath(var.send_button_xpath)).click();
                booked = true;
                String appointTimeContent = appointTime.getText();
                System.out.println("*************************************************************************");
                System.out.println("* The appointment is booking: " + content + " || Time: " + appointTimeContent);
                System.out.println("*************************************************************************");
              }
            }
          }					
        }catch(Exception e){
          booked = false;
        }
        try{
          Thread.sleep(1000);
        }catch(Exception e){
          System.out.println(e);
        }		
      }
  
  }

	private static boolean timeout() {
		if(System.currentTimeMillis() > nextTimeout) {
			return true;
		}
		return false;
	}

	private static void s(int second) {
		try {
			Thread.sleep(second * 1000);
		} catch (Exception ex) {

		}
	}

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("please introduce the parameter file!");
			System.exit(0);
		}

		try {
			run(args);
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}
}
