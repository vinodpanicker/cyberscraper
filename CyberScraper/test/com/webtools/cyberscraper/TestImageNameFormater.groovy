package com.webtools.cyberscraper;

import static org.junit.Assert.*;
import org.junit.Test;

import com.webtools.cyberscraper.AFArtifactListFileGenerator;

class TestImageNameFormater {

	@Test
	public void testImageNameFormater()
	{
		
		String imagename="http://10.0.0.7/csroot/imagerepopath/somefolder/2.jpg"
		
		assert imagename.split("/")[-2]==""
		
		
		
	}
	
	@Test
	public void testRegularExpression()
	{
		
		String imagename="http://10.0.0.7/csroot/imagerepopath/somefolder/2.jpg"
		
		String regexp='1'
		
		assert imagename =~ regexp
		
	}
	
	@Test
	public void testGenerate()
	{
		AFArtifactListFileGenerator afg= new AFArtifactListFileGenerator()
		def path="/media/csroot/imagerepopath/" 
		def imageFileName="cc2n.txt"
		def absoluteFilePathPrefix="/media/csroot" 
		def replacementWebServerPathPrefix="http://10.0.0.7/csroot" 
		def selector="2.jpg" 
		def regexp="2"
		def formatType="SRC"
		
		assert afg.generateSelectImagesFile(path, imageFileName, absoluteFilePathPrefix, replacementWebServerPathPrefix,selector,regexp)==639
		assert afg.generateBasedOnFormatType(path, imageFileName, absoluteFilePathPrefix, replacementWebServerPathPrefix,selector,regexp,formatType)==639
	}
	
	
}
