package com.webtools.cyberscraper;

import static org.junit.Assert.*;

import org.junit.Test;

import com.webtools.cyberscraper.DefaultArtifactFilter;
import com.webtools.cyberscraper.DefaultOutputLineFormatter;
import com.webtools.cyberscraper.JPGFilter;
import com.webtools.cyberscraper.LineFormatType;
import com.webtools.cyberscraper.SingleSelectorArtifactFilter;

public class TestGenerator {
	String imagename="http://10.0.0.7/csroot/imagerepopath/somefolder/2.jpg";

    @Test
	public void testAFLineFormatter() {
    	
    	assertEquals(LineFormatType.SRC,LineFormatType.fromString("SRC"));
       
    	assertEquals("http://10.0.0.7/csroot/imagerepopath/somefolder/2.jpg", new DefaultOutputLineFormatter(LineFormatType.SRC).format(imagename, 1));
    	assertEquals("<li><img src=\"http://10.0.0.7/csroot/imagerepopath/somefolder/2.jpg\" alt=\"Image 1>somefolder\" />", new DefaultOutputLineFormatter(LineFormatType.HTML_LI).format(imagename, 1));
    	assertEquals("<li><a href=\"http://10.0.0.7/csroot/imagerepopath/somefolder/2.jpg\">Image 1 Caption</a></li>", new DefaultOutputLineFormatter(LineFormatType.HTML_LI_A).format(imagename, 1));
    	assertEquals("<li><img src=\"http://10.0.0.7/csroot/imagerepopath/somefolder/2.jpg\" alt=\"Image 1>somefolder\" />", new DefaultOutputLineFormatter(LineFormatType.HTML_LI_CAPTION_DIR).format(imagename, 1));
    	assertEquals("<li><img src=\"http://10.0.0.7/csroot/imagerepopath/somefolder/2.jpg\" alt=\"Image 1>2.jpg\" />", new DefaultOutputLineFormatter(LineFormatType.HTML_LI_CAPTION_IMAGE_NAME).format(imagename, 1));
	}

    @Test
	public void testAFArtifactFilter() {
 
    	String extension=".jpg";
		String selector="2.jpg";
		String regexp="2";
		assertTrue(new DefaultArtifactFilter(extension,selector,regexp).check(imagename));
		assertTrue(new SingleSelectorArtifactFilter(extension,selector).check(imagename));
		assertTrue(new JPGFilter().check(imagename));
		
	}
    
    


}
