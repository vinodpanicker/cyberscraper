package com.webtools.cyberscraper

import java.util.ArrayList;
import javax.imageio.*;
import javax.swing.ImageIcon;

import java.awt.image.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

class AFUtil {

	private static String cvtLineTerminators(String s) {
		StringBuffer sb = new StringBuffer(80);

		int oldindex = 0, newindex;
		while ((newindex = s.indexOf("\\n", oldindex)) != -1) {
			//$NON-NLS-1$
			sb.append(s.substring(oldindex, newindex));
			oldindex = newindex + 2;
			sb.append('\n');
		}
		sb.append(s.substring(oldindex));

		s = sb.toString();

		sb = new StringBuffer(80);

		oldindex = 0;
		while ((newindex = s.indexOf("\\r", oldindex)) != -1) {
			//$NON-NLS-1$
			sb.append(s.substring(oldindex, newindex));
			oldindex = newindex + 2;
			sb.append('\r');
		}
		sb.append(s.substring(oldindex));

		return sb.toString();
	}
	
/**
 * 
 * @param surl
 * @param fullfilename
 * @param prefix
 * @param counter
 * @return
 */
	public static boolean saveImageToFile(String surl, String fullfilename, String prefix, int counter) {
		try {
			URL url =new URL(surl);
			fullfilename=fullfilename+prefix+counter+".jpg";
			ImageIO.write((RenderedImage) ImageIO.read(url), "jpg", new File(fullfilename));
			System.out.println(" Saved : filename:"+fullfilename)
			return true;
		} catch (FileNotFoundException fnfe) {
			println(fnfe);
			return false
		} catch (IOException e) {
			println(e);
			return false
		}
		finally {
		}
	}
	
	public static boolean saveImageToFile(String surl, String fullfilename) {
		try {
			URL url =new URL(surl);
			ImageIO.write((RenderedImage) ImageIO.read(url), "jpg", new File(fullfilename));
			System.out.println(" Saved : filename:"+fullfilename)
			return true;
		} catch (FileNotFoundException fnfe) {
			println(fnfe);
			return false
		} catch (IOException e) {
			println(e);
			return false
		}
		finally {
		}
	}

	public static String thumbUrl(String surl) {
		
				int position=surl.lastIndexOf("/");
				//println position
				String url=surl.substring(0,position+1);
				//println url
				String prefix ="tn_";
				String filename=surl.substring(position+1,surl.size());
				//println filename
				String newurl=url+prefix+filename;
		
				return newurl;
			}
	
	
	public static String padNumber(int number, int width) {
		String prefix="";
		String zeroToken ="0";
		String strNumber=number.toString();
		int numberWidth=strNumber.size();

		int diff=width-numberWidth;
		//System.out.println("diff:"+diff);
		if(diff>0)
		{
			int i=diff;
			//System.out.println("i:"+i);
			while(i>0)
			{
				prefix=zeroToken+prefix;
				i--;
			}
		}

		String paddedValue=prefix+number;
		//System.out.println("paddedValue:"+paddedValue);
		return paddedValue;
	}

	
	public static String fileis(String surl) {
		int position=surl.lastIndexOf("/");
		String filename=surl.substring(position+1,surl.size());
		return filename;
	}

	public static BufferedImage previewImage(String surl) {
	
		println "Starting with :" +surl;
		String newUrl=surl+1+"x"+AFUtil.padNumber(1,3)+".jpg";
		URL url =new URL(AFUtil.thumbUrl(newUrl));
		return ImageIO.read(url);
	}

	public static ImageIcon getIconImage(String modelname, String scmainurl) {
	
		ImageIcon imageicon=null;
	
		if(modelname!=null)
		{
			String pre=modelname.substring(0,2)
			String surl=scmainurl.substring(0, scmainurl.size()-17)+'/'+modelname+'/'+pre;
	
			String newUrl=surl+1+"x"+AFUtil.padNumber(1,3)+".jpg";
			URL url =new URL(AFUtil.thumbUrl(newUrl));
			println "Getting preview of:" +modelname +':'+url;
			imageicon=new ImageIcon(url);
		}
	
		return imageicon;
	
	}
	
	
}
