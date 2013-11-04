package com.webtools.cyberscraper

import java.util.ArrayList;
import java.util.List;

import java.awt.image.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

class AFScanner {
	
	AFConstants afConstants
	
	public AFScanner(AFConstants afConstants)
	{
		this.afConstants=afConstants
	}

	/**
	 * Method where the rendering of the scanner happens before the call scanAssetURL
	 * Retuns a list of AssetURI's
	 *  Uses ASSET_SCANNER_0 and replaces the PATHSEGMENT_0 with userprovided pathsegment 
	 * @param artifactsOwnPage
	 * @param userProvidedPathSegment
	 * @return
	 */
	public List getArtifactAssets(String artifactsOwnPage,String userProvidedPathSegment) {
		try {
			def scanner = afConstants.ASSET_SCANNER_0.replace(afConstants.PATHSEGMENT_0,userProvidedPathSegment)
			return fetchAssetURLs(artifactsOwnPage,scanner);
		} catch (FileNotFoundException fnfe) {
			println(fnfe);
		} catch (IOException e) {
			println(e);
		}
		finally {
		}
	}

	public String getArtifactName(String artifactUrl,String userProvidedPathSegment) {
		
		try {
			artifactUrl= artifactUrl.replace(afConstants.HOST_SITE_BASE_URL+'/'+userProvidedPathSegment+'/',"")
			artifactUrl= artifactUrl.replace("/","-")
			artifactUrl= artifactUrl.substring(0,artifactUrl.length()-1)
			return artifactUrl
		} catch (FileNotFoundException fnfe) {
			println(fnfe);
		} catch (IOException e) {
			println(e);
		}
		finally {
		}
	}

	public static String getArtifactName(String artifactUrl,String userProvidedPathSegment,int nameSegmentNo) {
		
		try {
			def artifactFrags= artifactUrl.split("/")
			def artifactName= artifactFrags[nameSegmentNo]
			return artifactName
		} catch (FileNotFoundException fnfe) {
			println(fnfe);
		} catch (IOException e) {
			println(e);
		}
		finally {
		}
	}

	/**
	 * 
	 * Uses the ARTIFACT_SCANNER_0 and replaces the PATHSEGMENT_0 with user provided PathSegment
	 * @param surl
	 * @param userProvidedPathSegment
	 * @return
	 */
	public List getArtifacts(String artifactPageURL,String userProvidedPathSegment) {
		
		try {
			//TODO siteid must be embedded in ARTIFACT SCANNER at runtime
			
			def scanner = afConstants.ARTIFACT_SCANNER_0.replace(afConstants.PATHSEGMENT_0,userProvidedPathSegment)
			return fetchArtifactURLs(artifactPageURL,scanner);
			
		} catch (FileNotFoundException fnfe) {
			println(fnfe);
		} catch (IOException e) {
			println(e);
		}
		finally {
		}
	}

	/**
	 * Multilevel scanner for Artifacts Level 0 is the level just before artifact extraction
	 * Each level requires an extractor
	 * @param surl
	 * @param siteid
	 * @param level
	 * @return
	 */
	public List getArtifacts(String artifactPageURL,String userProvidedPathSegment,int level) {
		
		try {
			//TODO siteid must be embedded in ARTIFACT SCANNER at runtime
			def scanner
			
			def scannerAsString = """afConstants.ARTIFACT_SCANNER_${level}"""
            
            scanner = '${scannerAsString}'
			
			println "SCANNER selected=>"+scanner + " for Level :"+level
			
			scanner = scanner.replace(afConstants.PATHSEGMENT_0,userProvidedPathSegment)
			//TODO : using getfileURLs will levels to override matchers, matcher should be configured at levels later
			
			println """SCANNER rendered with user provided Path Segment $userProvidedPathSegment => [$scanner]  @Level :$level"""
			
			return fetchArtifactURLs(artifactPageURL,scanner,level);
			
		} catch (FileNotFoundException fnfe) {
			println(fnfe);
		} catch (IOException e) {
			println(e);
		}
		finally {
		}
	}

	/**
	 * Function gets the indexpage by replacing the ARTIFACT_INDEX_PAGE with userprovided PATHSEGMENT_0
	 * @param userProvidedPathSegment0
	 * @param userProvidedPathSegment1
	 * @return
	 */
	public String fetchIndexPage(String userProvidedPathSegment0, String userProvidedPathSegment1) {
		
		//TODO replace siteid with static path, replace _ with /, -_will be an escape for _ 
		
		//String afUrl=AFConstants.HOST_SITE_BASE_URL+'/'+siteid+'/'+AFConstants.PATHSEGMENT_1+'/'+pagenumber+'/'
		
		String indexPageURL=afConstants.ARTIFACT_INDEX_PAGE.replace(afConstants.PATHSEGMENT_0,userProvidedPathSegment0)
		indexPageURL=indexPageURL.replace(afConstants.PATHSEGMENT_1,userProvidedPathSegment1)	
		println "Using Index Page=>"+indexPageURL
		return indexPageURL
	}

	public ArrayList fetchAssetURLs(String assetPageURL, String regex)
	throws Exception {
	
	    println "In Fetching AssetURL with=>"+ regex
		URL pfa = new URL(assetPageURL);
		println "Reading!"+assetPageURL
		BufferedReader input = new BufferedReader(new InputStreamReader(pfa.openStream()));
	
		String inputLine;
		String outputLine = ""; //$NON-NLS-1$
	
		while ((inputLine = input.readLine()) != null) {
			//println inputLine;
			outputLine = outputLine + inputLine;
		}
		
		if(afConstants.PRINT_ARTIFACT_PAGE)
		{
			println "Asset Page=>"+outputLine;
	
		}
		input.close();
	
		Pattern p;
	
		try {
			p = Pattern.compile(regex);
		} catch (PatternSyntaxException e) {
			System.err.println("Regex syntax error: " + e.getMessage()); //$NON-NLS-1$
			System.err.println("Error description: " + e.getDescription()); //$NON-NLS-1$
			System.err.println("Error index: " + e.getIndex()); //$NON-NLS-1$
			System.err.println("Erroneous pattern: " + e.getPattern()); //$NON-NLS-1$
			return null;
		}
	
		String s = AFUtil.cvtLineTerminators(outputLine);
		Matcher m = p.matcher(s);
	
	
	
		def arrayFileURLs = new ArrayList();
		while (m.find()) {
			
			String strName = outputLine.substring(m.start() , m.end()-1 );
			strName=strName.replace(afConstants.ASSET_REF,afConstants.WGET_CMD)
			println strName
	
			arrayFileURLs.add(strName); //$NON-NLS-1$ //$NON-NLS-2$
	
		}
	
	
		return arrayFileURLs;
	}

	public ArrayList fetchArtifactURLs(String artifactPageURL, String regex,int level)
	throws Exception {
	
		def mStartAdjustFactor = afConstants.MATCHER_STARTING_ADJUSTMENT_FACTOR
		def mEndAdjustFactor = afConstants.MATCHER_ENDING_ADJUSTMENT_FACTOR
		
		if(level!=afConstants.MAX_SCANNER_LEVEL)
		{
			//TODO externalise and add for other levels also
			println "Adjusting Matchers To 0,1 running @ level =>"+level
			mStartAdjustFactor = 0
			mEndAdjustFactor = 1  // 1 to trim tailing "
			 
		}
		else
		{
			println "Matchers Adjustments are => "+mStartAdjustFactor +" and "+mEndAdjustFactor
		}
	
		URL af = new URL(artifactPageURL);
		println "Reading!"
		BufferedReader input = new BufferedReader(new InputStreamReader(af.openStream()));
	
		String inputLine;
		String outputLine = ""; //$NON-NLS-1$
	
		while ((inputLine = input.readLine()) != null) {
			//println inputLine;
			outputLine = outputLine + inputLine;
		}
		//println outputLine;
	
		input.close();
	
		Pattern p;
	
		try {
			p = Pattern.compile(regex);
		} catch (PatternSyntaxException e) {
			System.err.println("Regex syntax error: " + e.getMessage()); //$NON-NLS-1$
			System.err.println("Error description: " + e.getDescription()); //$NON-NLS-1$
			System.err.println("Error index: " + e.getIndex()); //$NON-NLS-1$
			System.err.println("Erroneous pattern: " + e.getPattern()); //$NON-NLS-1$
			return null;
		}
	
		String s = AFUtil.cvtLineTerminators(outputLine);
		Matcher m = p.matcher(s);
	
		def arrayFileURLs = new ArrayList();
		while (m.find()) {
			
			String strName = outputLine.substring(m.start()-mStartAdjustFactor , m.end()-mEndAdjustFactor );
			strName=strName.replace("href=\"",afConstants.HOST_SITE_BASE_URL)
			println strName
			arrayFileURLs.add(strName); //$NON-NLS-1$ //$NON-NLS-2$
	
		}
	
		return arrayFileURLs;
	}

	public ArrayList fetchArtifactURLs(String artifactPageURL, String regex)
	throws Exception {
	
	
		URL af = new URL(artifactPageURL);
		println "Reading!"
		BufferedReader input = new BufferedReader(new InputStreamReader(af.openStream()));
	
		String inputLine;
		String outputLine = ""; //$NON-NLS-1$
	
		while ((inputLine = input.readLine()) != null) {
			//println inputLine;
			outputLine = outputLine + inputLine;
		}
		//println outputLine;
	
		input.close();
	
		Pattern p;
	
		try {
			p = Pattern.compile(regex);
		} catch (PatternSyntaxException e) {
			System.err.println("Regex syntax error: " + e.getMessage()); //$NON-NLS-1$
			System.err.println("Error description: " + e.getDescription()); //$NON-NLS-1$
			System.err.println("Error index: " + e.getIndex()); //$NON-NLS-1$
			System.err.println("Erroneous pattern: " + e.getPattern()); //$NON-NLS-1$
			return null;
		}
	
		String s = AFUtil.cvtLineTerminators(outputLine);
		Matcher m = p.matcher(s);
	
		def arrayFileURLs = new ArrayList();
		while (m.find()) {
			String strName = outputLine.substring(m.start()-afConstants.MATCHER_STARTING_ADJUSTMENT_FACTOR , m.end()-afConstants.MATCHER_ENDING_ADJUSTMENT_FACTOR );
			strName=strName.replace("href=\"",afConstants.HOST_SITE_BASE_URL)
			println strName
			arrayFileURLs.add(strName); //$NON-NLS-1$ //$NON-NLS-2$
		}
	
		return arrayFileURLs;
	}

}
