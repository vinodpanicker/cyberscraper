package com.webtools.cyberscraper;

public class JPGFilter extends ArtifactFilter{
	
	
	String extension=".jpg";
	

	public JPGFilter()
	{
		
		this.extension=extension;
		println "Initializing JPGFilter!!"
		
	}
	
	@Override
	public boolean check(String artifact) {
		return artifact.contains(this.extension)
	}

}
