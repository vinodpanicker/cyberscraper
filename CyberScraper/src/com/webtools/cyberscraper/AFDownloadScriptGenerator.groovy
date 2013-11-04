package com.webtools.cyberscraper

import javax.imageio.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.apache.commons.cli.Option;

import javax.swing.ImageIcon;

class AFDownloadScriptGenerator {
	
	AFConstants afConstants
	AFScanner afScanner
	
	public AFDownloadScriptGenerator()
	{
		this.afConstants= new AFConstants()
		this.afScanner= new AFScanner(afConstants)
	    println "Initializing Script downloader...!!" 
		println "using scanners"
		this.afConstants.listScanners()
	}
	
	/**
	 * Using a custom config file to generate
	 * @param configFileName
	 */
	public AFDownloadScriptGenerator(String configFileName)
	{
		this.afConstants= new AFConstants(configFileName)
		this.afScanner= new AFScanner(afConstants)
	
	}
	
	/**
	 * Using a AFConstants object To call a Download Script Generator
	 * @param afConstants
	 */
	public AFDownloadScriptGenerator(AFConstants afConstants)
	{
		this.afConstants= afConstants
		this.afScanner= new AFScanner(afConstants)
	
	}
	
	/**
	 * 
	 * Method used to work with a baseline AF file 
	 * 
	 * {site-id}{pagenumber}AFBaselineDownloadScript.sh
	 * found in the AF HomeDir
	 * 
	 */
	
	public List getArtifactInDownloadScript(String downloadFileName) {
		File baseDownloadScript=new File(downloadFileName)
		//Magic line to get all models in a download script, TODO move the DS to DSLatest after download and History
		def listOfArtifactsinDownloadScript=baseDownloadScript.text.findAll(~/mkdir*.*/){it->it.drop(6)}
		println listOfArtifactsinDownloadScript
		return listOfArtifactsinDownloadScript
	}

	/**
	 * Generates the Download script for PFA when the siteid and pagenumber is provided.
	 * DownloadScript can be saved as {site-id}{pagenumber}AFBaselineDownloadScript.sh
	 * The baseline script will be used for further manipulatations and history operations
	 * @param siteid
	 * @param pagenumber
	 * @return
	 */
	public generateDownloadScript(String siteid, String pagenumber) {
		String afIndexPageUrl = afScanner.fetchIndexPage(siteid, pagenumber);
		def artifactsOnPage=afScanner.getArtifacts(afIndexPageUrl,siteid)
		
		if(!artifactsOnPage)
		{
			println "No Artifacts founds!! for => "+ afIndexPageUrl
			return ""
		}
		
		def images=[]
		File downloadScript=new File("af"+siteid+pagenumber+new Date().time+".sh")
		def artifacts=[:]
		artifactsOnPage.each{ artifactsOwnPage->
			def artifactAssets=[]
			artifactAssets=afScanner.getArtifactAssets(artifactsOwnPage,siteid)
			String artifactName=afScanner.getArtifactName(artifactsOwnPage,siteid)
			//artifacts.put(artifactName,artifactAssets)

			downloadScript << "mkdir "+ artifactName
			downloadScript << "\n"
			downloadScript << "cd "+ artifactName
			downloadScript << "\n"
			artifactAssets.each{

				println "writing=>" +it
				downloadScript << it
				downloadScript << "\n"
			}
			downloadScript << "cd .. "
			downloadScript << "\n"
		}
		
		return downloadScript.absoluteFile
	}

	
	/**
	 * Generates the Download script for PFA when the siteid and pagenumber is provided.
	 * DownloadScript can be saved as {site-id}{pagenumber}AFBaselineDownloadScript.sh
	 * The baseline script will be used for further manipulatations and history operations
	 * @param siteid
	 * @param pagenumber
	 * @param level  => specify 0 for no level ,1 for 1 level, start with the max depth
	 * @return
	 */
	public generateDownloadScript(String siteid, String pagenumber,int level) {
		String afIndexPageUrl = afScanner.fetchIndexPage(siteid, pagenumber);
		
		def pagesWithArtifacts=[]
		
		while(level>0)
		{
			
		   def pagesWithIntermediateArtifacts=[]
		   	
		   pagesWithIntermediateArtifacts=afScanner.getArtifacts(afIndexPageUrl,siteid,level)
		   if(!pagesWithIntermediateArtifacts)
		   {
			   println "No Intermediate Artifacts got for =>"+afIndexPageUrl
			   break
		   }
		   println "Intermediate Artifacts got => "+pagesWithIntermediateArtifacts.size
		   level--
		   pagesWithIntermediateArtifacts.each{ afIntermidiateIndexPageUrl->
			           println "Fetching pages with artifact @ "+level+ " for => "+afIntermidiateIndexPageUrl
			   		   pagesWithArtifacts=pagesWithArtifacts+afScanner.getArtifacts(afIntermidiateIndexPageUrl,siteid,level)
					  
		   }
		
		}
		
		
		if(!pagesWithArtifacts)
		{
			println "No Artifacts founds!! for => "+ afIndexPageUrl
			return ""
		}
		
		def images=[]
		File downloadScript=new File("af"+siteid+pagenumber+new Date().time+".sh")
		def artifacts=[:]
		def currentArtifactName=""
		pagesWithArtifacts.each{ artifactsOwnPage->
			
			def artifactAssets=[]
			artifactAssets=afScanner.getArtifactAssets(artifactsOwnPage,siteid)
			//for same group of artifacts the name must not change
			String artifactName=afScanner.getArtifactName(artifactsOwnPage,siteid,afConstants.ARTIFACT_NAME_SEGMENT)
			
			//if name changes new group of artifacts found
			if(artifactName!=currentArtifactName&&currentArtifactName!="")
			{
				downloadScript << "cd .. "
				downloadScript << "\n"
				
			}
             if(artifactName!=currentArtifactName)
			{ 
			println "Name change !!=>"+ artifactName
			downloadScript << "mkdir "+ artifactName
			downloadScript << "\n"
			downloadScript << "cd "+ artifactName
			downloadScript << "\n"
			//setting new current artifact name
			currentArtifactName=artifactName
			}
			artifactAssets.each{

				println "writing=>" +it
				downloadScript << it
				downloadScript << "\n"
			}
			
		}
		
		return downloadScript.absoluteFile
	}
	
	/**
	 * Generates the Download script from indexPage url is provided.
	 * DownloadScript can be saved as {sitename}AFBaselineDownloadScript.sh
	 * The baseline script will be used for further manipulatations and history operations
	 * @param siteid
	 * @param pagenumber
	 * @param level  => specify 0 for no level ,1 for 1 level, start with the max depth
	 * @return
	 */
	public generateDownloadScriptFromIndexPage(String afIndexPageUrl,String sitename,int level) {
		
		def siteid=''
		def pagenumber=''
		def pagesWithArtifacts=[]
		
		while(level>0)
		{
			
		   def pagesWithIntermediateArtifacts=[]
			   
		   pagesWithIntermediateArtifacts=afScanner.getArtifacts(afIndexPageUrl,siteid,level)
		   if(!pagesWithIntermediateArtifacts)
		   {
			   println "No Intermediate Artifacts got for =>"+afIndexPageUrl
			   break
		   }
		   println "Intermediate Artifacts got => "+pagesWithIntermediateArtifacts.size
		   level--
		   pagesWithIntermediateArtifacts.each{ afIntermidiateIndexPageUrl->
					   println "Fetching pages with artifact @ "+level+ " for => "+afIntermidiateIndexPageUrl
						  pagesWithArtifacts=pagesWithArtifacts+afScanner.getArtifacts(afIntermidiateIndexPageUrl,siteid,level)
					  
		   }
		
		}
		
		
		if(!pagesWithArtifacts)
		{
			println "No Artifacts founds!! for => "+ afIndexPageUrl
			return ""
		}
		
		def images=[]
		File downloadScript=new File("af"+sitename+new Date().time+".sh")
		def artifacts=[:]
		def currentArtifactName=""
		pagesWithArtifacts.each{ artifactsOwnPage->
			
			def artifactAssets=[]
			artifactAssets=afScanner.getArtifactAssets(artifactsOwnPage,siteid)
			//for same group of artifacts the name must not change
			String artifactName=afScanner.getArtifactName(artifactsOwnPage,siteid,afConstants.ARTIFACT_NAME_SEGMENT)
			
			//if name changes new group of artifacts found
			if(artifactName!=currentArtifactName&&currentArtifactName!="")
			{
				downloadScript << "cd .. "
				downloadScript << "\n"
				
			}
			 if(artifactName!=currentArtifactName)
			{
			println "Name change !!=>"+ artifactName
			downloadScript << "mkdir "+ artifactName
			downloadScript << "\n"
			downloadScript << "cd "+ artifactName
			downloadScript << "\n"
			//setting new current artifact name
			currentArtifactName=artifactName
			}
			artifactAssets.each{

				println "writing=>" +it
				downloadScript << it
				downloadScript << "\n"
			}
			
		}
		
		return downloadScript.absoluteFile
	}
	
	
	
}
