import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;


public class TestIframe {
	public static void main(String[] args){
		System.setProperty("webdriver.chrome.driver", "C:/Program Files (x86)/Google/Chrome/Application/chromedriver.exe");
		WebDriver driver=new ChromeDriver();
		File file=new File("F:/GIt/Selenium/src/ifame.html");
		String filePath=file.getAbsolutePath();
		driver.get(filePath);
		WebElement vf=driver.findElement(By.xpath("//iframe[@id='if']"));
		driver.switchTo().frame(vf);
		driver.findElement(By.id("kw")).sendKeys("hehe");
		//�˻���һ����
		driver.switchTo().defaultContent();
	}
}
