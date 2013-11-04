package com.webtools.cyberscraper;

public class SingleSelectorArtifactFilter extends ArtifactFilter{
	
	String selector;
	String extension=".jpg";
	

	public SingleSelectorArtifactFilter(String extension,String selector)
	{
		this.selector=selector;
		this.extension=extension;
		println """Initializing SingleSelectorArtifactFilter!! with => $selector , $extension """
	}
	
	@Override
	public boolean check(String artifact) {
		return (artifact.contains(this.extension)&&artifact.contains(this.selector))
	}

}
