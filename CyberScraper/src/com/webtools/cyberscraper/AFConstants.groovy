package com.webtools.cyberscraper;

public class AFConstants {
	
	AFConfHelper afConfHelper=new AFConfHelper("afconstants.groovy")

	String  HOST_SITE_BASE_URL = afConfHelper.getAfParam("HOST_SITE_BASE_URL");
	boolean PRINT_ARTIFACT_PAGE=false
	

	//Place holder strings to be replaced with user provided pathsegments
	String PATHSEGMENT_1 =afConfHelper.getAfParam("PATHSEGMENT_1")?:'${pagenumber}'
	String PATHSEGMENT_2 =afConfHelper.getAfParam("PATHSEGMENT_2")?:'page'
	String PATHSEGMENT_0 =afConfHelper.getAfParam("PATHSEGMENT_0")?:'${siteid}'

	// Index page 	
	String ARTIFACT_INDEX_PAGE = afConfHelper.getAfParam("ARTIFACT_INDEX_PAGE")?:HOST_SITE_BASE_URL+'/host/'+PATHSEGMENT_0+'/'+this.PATHSEGMENT_2+'/'+PATHSEGMENT_1+'/'

	static final int MAX_SCANNER_LEVEL=1

	String ARTIFACT_SCANNER_1=afConfHelper.getAfParam("ARTIFACT_SCANNER_1")?:'href=\"/'+PATHSEGMENT_0+'/[A-Za-z0-9-_]{1,}/[A-Za-z0-9]{1,}/\" title'
	
	int MATCHER_STARTING_ADJUSTMENT_FACTOR =Integer.parseInt(afConfHelper.getAfParam("MATCHER_STARTING_ADJUSTMENT_FACTOR")?:"0")
	int MATCHER_ENDING_ADJUSTMENT_FACTOR = Integer.parseInt(afConfHelper.getAfParam("MATCHER_ENDING_ADJUSTMENT_FACTOR")?:"0")


	String ARTIFACT_SCANNER_0=afConfHelper.getAfParam("ARTIFACT_SCANNER_0")?:'href=\"/'+PATHSEGMENT_0+'/[A-Za-z0-9-_]{1,}/[A-Za-z0-9-_]{1,}/[A-Za-z0-9-_]{1,}.jpg\"'

	//THIS SEGMENT Must map to the ARTIFACT NAME SEGMENT in ARTIFACT_SCANNER_0, USED TO GET THE ARTIFACT GROUP NAME, AND MKDIR CMD.
	int ARTIFACT_NAME_SEGMENT=Integer.parseInt(afConfHelper.getAfParam("ARTIFACT_NAME_SEGMENT")?:"4")
		
	//ASSET_REF is used to substitute wget cmd, this can be any command in future 
	String ASSET_SCANNER_0=afConfHelper.getAfParam("ASSET_SCANNER_0")?:'src=\"/content/'+PATHSEGMENT_0+'/galleries/[A-Za-z0-9-_]{1,}/full/[A-Za-z0-9-_]{1,}.jpg\"'
	String ASSET_REF =afConfHelper.getAfParam("ASSET_REF")?:"src=\""
	
	
	String WGET_CMD = afConfHelper.getAfParam("WGET_CMD")?:"wget "+HOST_SITE_BASE_URL
	
	static final int MAX_FAILCOUNT = 250
	static final int MAX_ALBUM_COUNT = 999
	
	def listScanners()
	{
		println "ARTIFACT_INDEX_PAGE is the index page used by the scanners, The index page can be composed of pathsegment variables also"
		println """ARTIFACT_INDEX_PAGE => ARTIFACT_INDEX_PAGE"""
		
		
		println "ARTIFACT_NAME_SEGMENT : THIS SEGMENT must map to the ARTIFACT NAME SEGMENT in ARTIFACT_SCANNER_0, USED TO GET THE ARTIFACT GROUP NAME, AND MKDIR CMD."
		println """ARTIFACT_NAME_SEGMENT => $ARTIFACT_NAME_SEGMENT"""
		println "Level 1 scanner : ARTIFACT_SCANNER_1"
		println """ARTIFACT_SCANNER_1 => $ARTIFACT_SCANNER_1"""
		
		println "Level 0 scanner : ARTIFACT_SCANNER_0"
		println """ARTIFACT_SCANNER_0 => $ARTIFACT_SCANNER_0"""
		
		println "ASSET_SCANNER_0 Scanner the actual asset which has to be saved"
		println """ASSET_SCANNER_0 => $ASSET_SCANNER_0"""
		println "ASSET_REF is used to substitute wget cmd, this can be any command in future"
		println """ASSET_REF => $ASSET_REF"""
		
		println "Place holder variable used in the scanners which are replaced with user provided pathsegments values at runtime"
		println """PATHSEGMENT_0 => $PATHSEGMENT_0"""
		println """PATHSEGMENT_1 => $PATHSEGMENT_1"""
		println """PATHSEGMENT_2 => $PATHSEGMENT_2"""
		
	}
	
}
