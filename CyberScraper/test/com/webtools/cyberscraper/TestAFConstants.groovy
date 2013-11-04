package com.webtools.cyberscraper;

import static org.junit.Assert.*;

import org.junit.Test;

import com.webtools.cyberscraper.AFConstants;

class TestAFConstants {
	
	@Test
	public void testafConstants()
	{
		AFConstants afConstants= new AFConstants()
		
		assert afConstants.ARTIFACT_INDEX_PAGE==afConstants.HOST_SITE_BASE_URL+'/host/'+afConstants.PATHSEGMENT_0+'/'+afConstants.PATHSEGMENT_2+'/'+afConstants.PATHSEGMENT_1+'/'
	
		
		}
	
	public void generateTestAfConstants()
	{
	
		def afconstantContent="""
		af
		{
		
		   
		   HOST_SITE_BASE_URL = "https://github.com/"
		   PATHSEGMENT_1 ="*pagenumber*"
		   PATHSEGMENT_2 ="page"
		   //pathsegment 0 is groovy for github
           PATHSEGMENT_0 ="*siteid*"
              
						
		   ARTIFACT_INDEX_PAGE = "http://localhost/csroot/"
		   ARTIFACT_SCANNER_0="<a href=\"/groovy/[A-Za-z0-9-/]{1,}\">[A-Za-z0-9-/]{1,}</a>;"
		   ARTIFACT_SCANNER_1="<a href=\"/groovy/[A-Za-z0-9-/]{1,}/tree/master/[A-Za-z0-9-/]{1,}"
		   MATCHER_STARTING_ADJUSTMENT_FACTOR = 0
		   MATCHER_ENDING_ADJUSTMENT_FACTOR = 0
		   
		   ASSET_SCANNER_0="<a href=\"/groovy/[A-Za-z0-9-_]{1,}/blob/master/[A-Za-z0-9-_]{1,}"
		   WGET_CMD = "wget https://github.com/"
		
		   
		}
		"""	
		
		
		File afConstantsFile=new File('~/afconstants.groovy')
		
			afConstantsFile << afconstantContent
				
		
	}

}
