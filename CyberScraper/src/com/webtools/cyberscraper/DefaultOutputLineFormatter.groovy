package com.webtools.cyberscraper;

public class DefaultOutputLineFormatter extends LineFormatter {

	LineFormatType lineFormatType;
	
	public DefaultOutputLineFormatter(LineFormatType lineFormatType)
	{
		this.lineFormatType=lineFormatType
	}
	
	@Override
	public String format(String imagefile, int i) {
	def line=''
		switch(lineFormatType){
		
			case LineFormatType.HTML_LI:
			line="<li><img src=\""+imagefile+"\" alt=\"Image "+ i +">"+imagefile.split('/')[-2]+"\" />"
			break;
			
			case LineFormatType.HTML_LI_CAPTION_DIR:
				line="<li><img src=\""+imagefile+"\" alt=\"Image "+ i +">"+imagefile.split('/')[-2]+"\" />"
				break;
				
			case LineFormatType.HTML_LI_A:
				line="<li><a href=\""+imagefile+"\">Image "+ i +" Caption</a></li>"

				break;
			case LineFormatType.HTML_LI_SRC:
				line="<li><img src=\""+imagefile+"\" alt=\"Image "+ i +" Caption\" />"
				break;

			case LineFormatType.HTML_LI_CAPTION_IMAGE_NAME:
				line="<li><img src=\""+imagefile+"\" alt=\"Image "+ i +">"+imagefile.split('/')[-1]+"\" />"
				break;

			case LineFormatType.SRC:
				line=imagefile
				break;
				
			case LineFormatType.WGET:
				line="wget '"+imagefile+"'"
				break;
			
		}
		return line
	}

}
