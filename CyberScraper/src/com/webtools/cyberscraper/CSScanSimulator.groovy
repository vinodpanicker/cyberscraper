package com.webtools.cyberscraper;

import java.util.ArrayList;

public class CSScanSimulator {

	AFConstants afConstants
	AFScanner afScanner
	
	public ArrayList forwardSearchURIs= [];
	public ArrayList assetURIs= []; 
	def userProvidedPathSegment0="";
	def userProvidedPathSegment1="";
	private ArrayList currentSearchURIs= [];
	def simulatorOutput=[:]

	public CSScanSimulator scanWith(AFConstants afConstants) {
		// TODO do constants initialization 
		
		this.afConstants= new AFConstants()
		this.afScanner= new AFScanner(afConstants)
		println "Initializing CSScanSimulator...!!"
		println "using scanners"
		this.afConstants.listScanners()
		
		
		return this;
	}

	

	
	/**
	 * This function does the scan routine one time
	 * @return
	 */
	public CSScanSimulator once() {
		// TODO do scan routine one time and update assetURIs and forwardSearchURIs
		// scan routine will read all the currentindexpageURIs and update assetURIS and forwardSearchURIs
		this.simulatorOutput=doscan();
		return this;
	}
	

	private def doscan() {
		// need to check if pathsegments are set and are not defaults.
		if(forwardSearchURIs.isEmpty())
		{
			println "No more ForwardSearchURIs Found!!"
			return
		}
		
		// scan routine will read all the currentindexpageURIs and update assetURIS and forwardSearchURIs
		currentSearchURIs << forwardSearchURIs
		
	
			
			
			def pagenumber=''
			def pagesWithArtifacts=[]
			
			while(level>0)
			{
				
			   def pagesWithIntermediateArtifacts=[]
				   
			   pagesWithIntermediateArtifacts=afScanner.getArtifacts(afIndexPageUrl,userProvidedPathSegment0,level)
			   if(!pagesWithIntermediateArtifacts)
			   {
				   println "No Intermediate Artifacts got for =>"+afIndexPageUrl
				   break
			   }
			   println "Intermediate Artifacts got => "+pagesWithIntermediateArtifacts.size
			   level--
			   pagesWithIntermediateArtifacts.each{ afIntermidiateIndexPageUrl->
						   println "Fetching pages with artifact @ "+level+ " for => "+afIntermidiateIndexPageUrl
							  pagesWithArtifacts=pagesWithArtifacts+afScanner.getArtifacts(afIntermidiateIndexPageUrl,userProvidedPathSegment0,level)
						  
			   }
			
			}
			
			
			if(!pagesWithArtifacts)
			{
				println "No Artifacts founds!! for => "+ afIndexPageUrl
				return ;
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

	/**
	 * This function will do the scan routine ntimes
	 * @param n
	 * @return
	 */
	public CSScanSimulator times(int n) {
		
		// call 
		for(int i=0;i<=n;i++)
		{
			doscan();
		}
		
		return this;
	}

	/**
	 *  Continue to scan till no more forwardSearchURIs left
	 * @return
	 */
	public CSScanSimulator tillNoneLeft() {
		// call 
		while(!forwardSearchURIs.isEmpty())
		{
			doscan();
		}
		
		return this;
	}

	/**
	 * Path Segment is provided by the user during simulation. this function sets the first forwardSearchURI
	 * Depending on the indexpage URL a single or multiple userProvidedPathSegments can be set
	 * 
	 * generally the first user provided pathsegment one is an unique identifier and the second one could be a key to navigate to the specific page.
	 * 
	 * @param pathsegment
	 * @return
	 */
	public CSScanSimulator forPathSegment(String userProvidedPathsegment) {
		String afIndexPageUrl = afScanner.fetchIndexPage(userProvidedPathsegment, "");
		this.forwardSearchURIs<<afIndexPageUrl
		this.userProvidedPathSegment0=userProvidedPathsegment
		return this;
	}

	public CSScanSimulator forPathSegment(String userProvidedPathsegment0,String userProvidedPathsegment1) {
		String afIndexPageUrl = afScanner.fetchIndexPage(userProvidedPathsegment0, userProvidedPathsegment1);
		this.forwardSearchURIs<<afIndexPageUrl
		this.userProvidedPathSegment0=userProvidedPathsegment0
		this.userProvidedPathSegment1=userProvidedPathsegment1
		return this;
	}
}
