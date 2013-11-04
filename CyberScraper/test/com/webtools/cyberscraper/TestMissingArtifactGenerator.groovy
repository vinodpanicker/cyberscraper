package com.webtools.cyberscraper;

import static org.junit.Assert.*;

import org.junit.Test;

import com.webtools.cyberscraper.AFDownloadScriptGenerator;
import com.webtools.cyberscraper.AFMissingArtifactGenerator;

class TestMissingArtifactGenerator {

	@Test
	public void testMissingFileGenerator() {
		def indexPageUrl="http://localhost/images/"
		def generatedDownloadScript=new AFDownloadScriptGenerator().generateDownloadScriptFromIndexPage(indexPageUrl,"localsite",1)
		def artifactRepoRootDir="testRepo"
		def existingDownloadScript=generatedDownloadScript

		def missingFilesDownloadScript=new AFMissingArtifactGenerator(".jpg").generateDownloadScript(artifactRepoRootDir,existingDownloadScript)
	}
	
	@Test
	public void testMissingFileForPFAGenerator() {

		def artifactRepoRootDir="/media/artifactrepo"
		def existingDownloadScript="downloadscript.sh"

		assert new AFMissingArtifactGenerator(".jpg").generateDownloadScript(artifactRepoRootDir,existingDownloadScript)
	}
}
