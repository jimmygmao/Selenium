import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;


public class Test7z {

	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.driver", "C:/Program Files (x86)/Google/Chrome/Application/chromedriver.exe");
		WebDriver driver=new ChromeDriver();
		driver.get("https://www.wxw7z.com/");
		driver.findElement(By.linkText("��Ʒ����")).click();
		driver.findElement(By.className("goodsCat_btn")).click();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		driver.findElement(By.className("i_ct_choice")).click();
		List<WebElement> classs=driver.findElements(By.xpath("//b[@class='i_mo_wei']"));
		for(WebElement cl:classs){
			cl.click();
		}
		driver.findElement(By.className("i_ct_sure")).click();
	}
}
