package com.webtools.cyberscraper;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public enum LineFormatType {
	SRC("SRC"), 
	WGET("WGET"), 
	HTML_LI_A("HTML_LI_A"), 
	HTML_LI_CAPTION_IMAGE_NAME("HTML_LI_CAPTION_IMAGE_NAME"), 
	HTML_LI_SRC("HTML_LI_SRC"),
	HTML_LI("HTML_LI"),
	HTML_LI_CAPTION_DIR("HTML_LI_CAPTION_DIR");

	 
	private final String name;
	 
	private static final Map<String, LineFormatType> map =
	                             new HashMap<String, LineFormatType>();
	 
	static {
	   for (LineFormatType type : LineFormatType.values()) {
	     map.put(type.name, type);
	   }
	}
	 
	private LineFormatType(String name) {
	   this.name = name;
	}
	 
	public String getName() {
	   return name;
	}
	 
	public static LineFormatType fromString(String name) {
	   if (map.containsKey(name)) {
	     return map.get(name);
	   }
	   throw new NoSuchElementException(name + "not found");
	}

}
