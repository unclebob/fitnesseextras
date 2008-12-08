package com.objectmentor.wikimail;

import fitnesse.testutil.TestSuiteMaker;
import junit.framework.Test;

public class AllTestSuite
{
	public static Test suite()
	{
		return TestSuiteMaker.makeSuite("wikimail", new Class[] {
			EmailParserTest.class,
			PostmasterTest.class
		});
	}
}
