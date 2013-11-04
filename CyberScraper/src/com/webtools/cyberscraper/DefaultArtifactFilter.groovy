package com.webtools.cyberscraper;

public class DefaultArtifactFilter extends ArtifactFilter{
	
	String selector;
	String extension=".jpg";
	String regexp;

	public DefaultArtifactFilter(String extension,String selector,String regexp)
	{
		this.selector=selector;
		this.extension=extension;
		this.regexp=regexp;
		println """Initializing DefaultArtifactFilter!! with => $selector , $extension , $regexp"""
	}
	
	@Override
	public boolean check(String artifact) {
		boolean check=(artifact.contains(this.extension)&&artifact.contains(this.selector)&&artifact.contains(this.regexp))
		//println """checked $artifact with [${this.extension} , ${this.selector},${this.regexp} ]=> Filter result $check"""
		return check
	}

}
