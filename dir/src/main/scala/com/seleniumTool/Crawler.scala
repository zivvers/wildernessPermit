package seleniumTool

import org.openqa.selenium.support.ui.WebDriverWait;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;



/* should we make this a trait given the lack 
   of parameter variables ? 

   May need to parameterize remote-webdriver address */
class Crawler()  {

   private val remoteURL : URL = new URL( "http://remote-webdriver:4444/wd/hub" );
   
   protected val driver : RemoteWebDriver = new RemoteWebDriver( remoteURL , DesiredCapabilities.firefox() );

   /* hmm should this b a class variable?? */
   protected val waitForLoad = new WebDriverWait(driver, 20, 250);


}