package com.webtools.cyberscraper;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestTrainer {

	@Test
	public void testCSTraining() {
		
		// the Training routine 
		
		CSTrainer cstrainer= new CSTrainer();
		CSScanSimulator csScanSimulator= new CSScanSimulator();
		
	    // 1) create a trainee constants file

		AFConstants afConstants=cstrainer.prepareForTraining();
		
		//Set indexPage
		assertEquals(afConstants.getARTIFACT_INDEX_PAGE(),new AFConstants().getARTIFACT_INDEX_PAGE());
		String newIndexPage="https://github.com/groovy";
		afConstants.setARTIFACT_INDEX_PAGE(newIndexPage);
		assertEquals(newIndexPage,afConstants.getARTIFACT_INDEX_PAGE());
		
		//set Artifact Scanner 0
		assertEquals("href=\"[A-Za-z0-9-/]{1,}/index.html\"",new AFConstants().getARTIFACT_SCANNER_0());
		String newArtifactScanner_0="<a href=\"/groovy/[A-Za-z0-9-/]{1,}\">[A-Za-z0-9-/]{1,}</a>;";
		afConstants.setARTIFACT_SCANNER_0(newArtifactScanner_0);
		assertEquals(newArtifactScanner_0,afConstants.getARTIFACT_SCANNER_0());
		
		//set Artifact Scanner 1
		assertEquals("href=\"[A-Za-z0-9-/]{1,}/index.html\"",new AFConstants().getARTIFACT_SCANNER_1());
		String newArtifactScanner_1="<a href=\"/groovy/[A-Za-z0-9-/]{1,}/tree/master/[A-Za-z0-9-/]{1,}";
		afConstants.setARTIFACT_SCANNER_1(newArtifactScanner_1);
		assertEquals(newArtifactScanner_1,afConstants.getARTIFACT_SCANNER_1());
		
		//Set Asset Scanner 0
		assertEquals("src=\"original/[A-Za-z0-9-_]{1,}.jpg\"",new AFConstants().getASSET_SCANNER_0());
		String newAssetScanner_0="<a href=\"/groovy/[A-Za-z0-9-_]{1,}/blob/master/[A-Za-z0-9-_]{1,}";
		afConstants.setASSET_SCANNER_0(newAssetScanner_0);
		
		assertEquals(newAssetScanner_0,afConstants.getASSET_SCANNER_0());
		
		    
		// 2) Run the CSScanSimulator using the trainer generated afConstants
		
		
		csScanSimulator=csScanSimulator.scanWith(afConstants).forPathSegment("groovy").once();
		
		//csScanSimulator.forPathSegment("groovy","0");
		
		csScanSimulator=csScanSimulator.scanWith(afConstants).times(10);
		
		csScanSimulator=csScanSimulator.scanWith(afConstants).tillNoneLeft();
		
		// 3) Generate the CSScanSimulator outputs , forwardSearchURIs , assetURIs
		
		
		// 4) Feed assetURIs to the Assetizer i.e downloadscriptGenerator
		// 5) Feed the forwardSearchURIs back to the CSScanSimulator
		
		// 6) Use the Trainer to generate the afconstants file  for the AFScanner . This is the last successful simulation of the CSScanSimulator
		
		
	}

}
