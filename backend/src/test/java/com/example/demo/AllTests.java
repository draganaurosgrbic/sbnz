package com.example.demo;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	com.example.demo.create.TestValidation.class, com.example.demo.create.TestNKS.class,
	com.example.demo.create.TestPoints.class, com.example.demo.create.TestEKS.class,
	com.example.demo.increase.TestValidation.class, com.example.demo.increase.TestBase.class,
	com.example.demo.increase.TestEKS.class, 
	com.example.demo.renew.TestValidation.class, com.example.demo.renew.TestEKS.class,
	com.example.demo.close.TestValidation.class,
	com.example.demo.report.TestBaseReport.class, com.example.demo.report.TestAdvancedReports.class,
	com.example.demo.events.TestEvents.class
})
public class AllTests {

}
