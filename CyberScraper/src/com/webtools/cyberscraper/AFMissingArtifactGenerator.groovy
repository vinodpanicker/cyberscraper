package com.webtools.cyberscraper;

class AFMissingArtifactGenerator {

	/**
	 *
	 * Class used by the MissingArtifactGenerator
	 *
	 */
 class Artifact {
		String artifactUrl
		def getArtifactName() {
			def artifactFrags= artifactUrl.split("/")
			def artifactName= artifactFrags[-1]
			//println """Adding artifact => $artifactName"""
			return artifactName
		}
		
		def getArtifactDir() {
			def artifactFrags= artifactUrl.split("/")
			def artifactDir= artifactFrags[-2]
			//println """Adding artifact Dir => $artifactDir"""
			return artifactDir
		}
		
		def getArtifactDirAndName() {
			def artifactFrags= artifactUrl.split("/")
			def artifactDirAndName= artifactFrags[-2]+"/"+artifactFrags[-1]
			//println """Adding artifact Dir => $artifactDirAndName"""
			return artifactDirAndName
		}
	}

	ArtifactFilter artifactFilter

	public AFMissingArtifactGenerator(String selector) {
		this.artifactFilter=new DefaultSimpleArtifactFilter(selector)
	}
	
	public AFMissingArtifactGenerator(ArtifactFilter artifactFilter) {
		this.artifactFilter=artifactFilter
	}

	public String generateDownloadScript(String artifactRepoRootDir,String existingDownloadScript) {
		int missing=0
		String missingFileDownloadScriptName=new Date().time+"MissingPatch.sh"
		println """Searching for Missing files in $artifactRepoRootDir \n against download script $existingDownloadScript"""
		missing = generateMissingDownloadScript(artifactRepoRootDir, missingFileDownloadScriptName, existingDownloadScript)
		println """Found $missing missing files!!"""
		println """Download script is => $missingFileDownloadScriptName"""
		return missingFileDownloadScriptName
	}

	private int generateMissingDownloadScript(String artifactRepoRootDir, String missingFileDownloadScriptName, String existingDownloadScript) {
		
		int missing=0
		def artifactRegistry=[]
		def artifactNameLookup=[]
		def artifactDirLookup=[]
		def artifactDirAndNameLookup=[]
		//TODO scan through artifactRepoRootDir and populate artifactRegistry of


		new File(artifactRepoRootDir).eachFileRecurse{
			String file= it
			if(artifactFilter.check(file)) {
				//artifactRegistry << new Artifact(artifactUrl:file).artifactDirAndName
				artifactRegistry << new Artifact(artifactUrl:file)
			}
		}



		artifactNameLookup=artifactRegistry*.artifactName
		println """Total Number of Available Artifacts is ${artifactNameLookup.size()}"""
		artifactDirLookup=artifactRegistry*.artifactDir.unique()
		println """Total Number of Available ArtifactDir is ${artifactDirLookup.size()}"""
		artifactDirAndNameLookup=artifactRegistry*.artifactDirAndName.unique()
		println """Total Number of Available artifactDirAndName combinations are ${artifactDirAndNameLookup.size()}"""

		File missingFileDownloadScript=new File(missingFileDownloadScriptName)
		String currentDir=""
		new File(existingDownloadScript).eachLine {
			//TODO Check if the file name exists in the filename look
			if(checkCDCommand(it))
			{
				missingFileDownloadScript<<it
				missingFileDownloadScript<<"\n"
			}
			else if(checkMK(it))
			{
				def mkdirCommand=it
				def mkdirFrags= mkdirCommand.split(" ")
				currentDir=mkdirFrags[-1]
				
				missingFileDownloadScript<<mkdirCommand
				missingFileDownloadScript<<"\n"
				

			}
			else if(checkIfMissing(it,currentDir,artifactDirAndNameLookup))
			{
				missingFileDownloadScript<<it
				missingFileDownloadScript<<"\n"
				missing++
			}

			//TODO check if dir exist in the dir lookup
		}

		return missing
	}

	private boolean checkMKorCDCommand(String content)
	{
		boolean check=false
		
		if(content.contains("mkdir")||content.contains("cd "))
		{
			//println "Found command =>$content "
			check=true
		}
		
		
		return check
	}
	
	private boolean checkMK(String content)
	{
		boolean check=false
		
		if(content.contains("mkdir"))
		{
			//println "Found command =>$content "
			check=true
		}
		
		
		return check
	}

	private boolean checkCDCommand(String content)
	{
		boolean check=false
		
		if(content.contains("cd "))
		{
			//println "Found command =>$content "
			check=true
		}
		
		
		return check
	}
	/**
	 * Method used to check only for ArtifactName
	 * @param content
	 * @param artifactNameLookup
	 * @return
	 */
	private boolean checkIfMissing(String content,def artifactNameLookup )
	{
		boolean check=false
		if(content.contains("/"))
		{
			def contentFrags= content.split("/")
			def artifactName= contentFrags[-1]
			//println "looking up =>$artifactName "
			// true if not found.. 
			check=!artifactNameLookup.contains(artifactName)
			if(check)
			{
				//println "$artifactName Missing !!"
				
			}
			else
			{
			//println "$artifactName Found!!"
			}
		}
		return check
	}

	/*
	 * Method used to check combination of directory and artifactName
	 */
	private boolean checkIfMissing(String content,String dir,def artifactDirAndNameLookup )
	{
		boolean check=false
		if(content.contains("/"))
		{
			def contentFrags= content.split("/")
			def artifactName= contentFrags[-1]
			def currentDirAndArtifactName = dir+"/"+artifactName
			//println "looking up =>$currentDirAndArtifactName "
			// true if not found..
			check=!artifactDirAndNameLookup.contains(currentDirAndArtifactName)
			if(check)
			{
				//println "$currentDirAndArtifactName Missing !!"
				
			}
			else
			{
			//println "$currentDirAndArtifactName Found!!"
			}
		}
		return check
	}

		
public static void main(args) 
	{
		def imagesRootDir=args[0]
		def existingDownloadScript=args[1]

		def missingFilesDownloadScript=new AFMissingArtifactGenerator(".jpg").generateDownloadScript(imagesRootDir,existingDownloadScript)

		println "Download Script Generated= >"+missingFilesDownloadScript


	}

}
