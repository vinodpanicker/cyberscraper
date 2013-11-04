/**
 * 
 */
package com.webtools.cyberscraper

import javax.imageio.*;
import java.awt.image.*;
import java.io.*;
import org.cyberneko.html.parsers.*;
import groovy.swing.*;
import javax.swing.ImageIcon
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Messages{
	def guid=""
	
	public Messages(long guid)
	{
		this.guid=guid
	}
	def stack=[0]
	synchronized void leftShift(value){
		
		value=value+":"+this.guid
		stack << value
		println "push: $value"
		notifyAll()
	}
	
	synchronized Object pop(){
		
		while(stack.isEmpty())
		{
			try{wait()}
			catch(InterruptedException e){ }
		}
		def value=stack.pop()
		println "pop: $value"
		return value
	}
	
	synchronized boolean hasMessage()
	{
		return !stack.isEmpty()
		
		}
}