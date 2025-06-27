package runner;


import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import java.util.logging.Level;
import java.util.logging.Logger;




@RunWith(Cucumber.class)
@CucumberOptions( plugin = {"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:","rerun:target/rerun.txt"},
features = {"src/test/resources/features"
},

glue = "stepDef",	
monochrome = true,
dryRun = false
		 
)

		
public class TestRunner {	
	
	static {
        Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
        Logger.getLogger("org.apache.http").setLevel(Level.OFF);
        System.setProperty("webdriver.chrome.logfile", "NUL");
        System.setProperty("webdriver.chrome.verboseLogging", "false");
        
        
    }
	

	
}








