package com.lambda.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;

@RunWith(Parallelized.class)
public class JUnitConcurrentTest {
	String username = "sanjaywaware04";
	String accesskey = "i6I2yqGQJTSjxYMwkhvy3LHTBHin6XJmTAOpAa729s18AqC5U3";
	String hub = "@hub.lambdatest.com/wd/hub";

	public String platform;
	public String browserName;
	public String browserVersion;
	public RemoteWebDriver driver = null;
	public String status = "Passed";

	@Parameterized.Parameters
	public static LinkedList<String[]> getEnvironments() throws Exception {
		LinkedList<String[]> env = new LinkedList<String[]>();
		env.add(new String[] { "Windows 10", "Chrome", "88.0" });
		env.add(new String[] { "macOS Sierra", "Edge", "87.0" });
		env.add(new String[] { "Windows 7", "Firefox", "82.0" });
		env.add(new String[] { "Windows 10", "Internet Explorer", "11.0" });
		return env;
	}

	public JUnitConcurrentTest(String platform, String browserName, String browserVersion) {
		this.platform = platform;
		this.browserName = browserName;
		this.browserVersion = browserVersion;
	}

	@Before
	public void setUp() throws Exception {
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setCapability("browserName", browserName);
		caps.setCapability("version", browserVersion);
		caps.setCapability("platform", platform); // If this cap isn't specified, it will just get the any available one
		caps.setCapability("build", "JUnitParallelSample");
		caps.setCapability("name", "JUnitParallelSampleTest");
		caps.setCapability("network", true); // To enable network logs
		caps.setCapability("visual", true); // To enable step by step screenshot
		caps.setCapability("video", true); // To enable video recording
		caps.setCapability("console", true); // To capture console logs
		try {
			driver = new RemoteWebDriver(new URL("https://" + username + ":" + accesskey + hub), caps);
		} catch (MalformedURLException e) {
			System.out.println("Invalid grid URL");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		try {
			driver.get("https://www.lambdatest.com/selenium-playground/");
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
			driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void HandlingRadioButton() throws Exception {
		try {
			// Click “Radio Buttons Demo” under “Input Forms”.
			driver.findElement(By.linkText("Radio Buttons Demo")).click();

			// Under the “Radio Button Demo” section, select “Female”.

			WebElement femaleButton = driver.findElement(By.xpath("//input[@value=\"Female\"]"));
			femaleButton.click();

			// Then click the “Get Checked Value” button.

			driver.findElement(By.cssSelector("#buttoncheck")).click();
			String actual = driver.findElement(By.className("text-gray-900 text-size-15 my-10 text-black radiobutton"))
					.getText();

			String expected = "Radio button 'Female' is checked";
			assertEquals(expected, actual);
		} catch (Exception e) {
			
			e.printStackTrace();
		}

	}

	@Test
	public void BrowserWindowHandling() throws Exception {
		try {
			// Click “Window Popup Modal” under the “Alerts & Modals” section.
			driver.findElement(By.xpath("//a[contains(text(), 'Window Popup')]")).click();

			// Click the “Follow On Twitter” button.
			driver.findElement(By.xpath("//a[@title='Follow @Lambdatesting on Twitter']")).click();
			Set<String> windowIds = driver.getWindowHandles();
			
			ArrayList<String> al = new ArrayList<String>(windowIds);
			System.out.println("Id of main window is :" + al.get(0));
			System.out.println("Id of main window is :" + al.get(1));
			
			// Validate that a new window is opened using Window Handle id.
			driver.switchTo().window(al.get(1));
			String childWindowPageTitle = driver.getTitle();
			System.out.println(childWindowPageTitle);

			assertTrue(driver.getPageSource().contains("Twitter"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@After
	public void tearDown() throws Exception {
		if (driver != null) {
			driver.executeScript("lambda-status=" + status);
			driver.quit();
		}
	}
}
