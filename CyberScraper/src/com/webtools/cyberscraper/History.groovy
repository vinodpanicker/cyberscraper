package com.webtools.cyberscraper

class History {
	

	
	int page=0
	int albumCounter=0
	def msg
	File f
	File flog
	def logLines=[]


	
	public void load(String modelsHomeDirPath,Messages msg)
	{
		logLines = []
		//String logFile=fullfilename+'hist.log';

		msg=msg
		
		f=new File(modelsHomeDirPath);
		flog=new File(modelsHomeDirPath+'hist.log');
		if(!f.exists())
		{
			println f.absolutePath
			println flog.absoluteFile
			f.mkdirs();
			flog.createNewFile()
		}
		else
		{
			//	    	load setcounter and page number

			if(flog.exists())
			{
				logLines=flog.readLines();
				page=Integer.parseInt(logLines[1]?logLines[1]:'0');
				albumCounter=Integer.parseInt(logLines[0]?logLines[0]:'0');

				msg << "Hist Log has :"+logLines;
				//increment to next page
				page++;

			}
		}
		
		}
	
	
	public void saveHistory(int page, int albumCounter) {
		logLines[1]=page;
		logLines[0]=albumCounter;
		flog.write(logLines[0..1].join("\n"))
	}
}
