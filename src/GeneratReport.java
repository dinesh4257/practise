import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class GeneratReport {

	static WebDriver driver;

	static void selectElementByValue(String webele, String value) {
		Select sl = new Select(driver.findElement(By.xpath(webele)));
		sl.selectByValue(value);
	}

	static void deselectAllEle(String webele) {
		Select sl = new Select(driver.findElement(By.xpath(webele)));
		sl.deselectAll();
	}

	public static void main(String[] args) {
		// Browser Initialization
		System.setProperty("webdriver.chrome.driver", "C:\\chromedriver_win32\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();

		// Go to Url
		driver.get(" https://wanikirtesh.github.io/");
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

		// Select Filters Department & Region
		selectElementByValue("//select[@id='selDetails']", "department");
		selectElementByValue("//select[@id='selDetails']", "region");

		WebDriverWait wbwait = new WebDriverWait(driver, 20);
		wbwait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//label[@for='fltdepartment']"))));

		// Select 'Marketing' For Department and 'UAE' in Region
		deselectAllEle("//select[@id='fltdepartment']");
		driver.findElement(By.xpath("//select[@id='fltdepartment']/option[3]")).click();
		deselectAllEle("//select[@id='fltregion']");
		driver.findElement(By.xpath("//select[@id='fltregion']/option[4]")).click();
		// Click on Generate Report button
		driver.findElement(By.xpath("//button[contains(text(),'Report')]")).click();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		String parentWin = driver.getWindowHandle();
		Set<String> windows = driver.getWindowHandles();
		for (String subWin : windows) {
			driver.switchTo().window(subWin);
		}
		driver.manage().window().maximize();
		// Verify that, there are 9 records displayed in report
		Assert.assertEquals("Showing 1 to 9 of 9 entries",
				driver.findElement(By.xpath("//div[@role][contains(text(),'Showing 1 to 9 of 9 entries')]")).getText());
		String fourthRow;
		int count = 0;

		List<WebElement> webEles = driver.findElements(By.xpath("//table[@id='rpt']//td/input"));
		for (WebElement Element : webEles) {
			if (Element.isSelected()) {
				count++;
				// Also validate 4th Row record matches as below
				if (driver.findElement(By.xpath("//tr[4]")).getText().contains("Chetan")) {
					fourthRow = driver.findElement(By.xpath("//tr[4]")).getText() + " checked";
					Assert.assertEquals("4 Chetan Patel Marketing UAE band 2 5 checked", fourthRow);
					System.out.println("4th Row Verified & actual value " + fourthRow);
				}
			}
		}
		// and 4 of them are checked (Last column)
		Assert.assertEquals(4, count);
		System.out.println("Test Passed");
		driver.close();
		driver.quit();
	}

}
