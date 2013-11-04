package com.webtools.cyberscraper;

public class DefaultSimpleArtifactFilter extends ArtifactFilter{
	
	String selector;

	public DefaultSimpleArtifactFilter(String selector)
	{
		this.selector=selector;
		println """Initializing DefaultSimpleArtifactFilter!! with => $selector"""
	}
	
	@Override
	public boolean check(String artifact) {
		boolean check=artifact.contains(this.selector)
		//println """checked $artifact with [${this.selector}]=> Filter result $check"""
		return check
	}

}
