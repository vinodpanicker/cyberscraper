package com.webtools.cyberscraper

class AFArtifactListFileGenerator {


	int batch=10
	def images=[]
	int maxfetch=0

	public AFArtifactListFileGenerator() {
	}

	public AFArtifactListFileGenerator(String imageFileName,int batch) {
		this.batch=batch
		File imageFile=new File(imageFileName)
		imageFile.eachLine{

			if(it) {
				images<< it
			}
		}

		println "Initialized Images=>"+images.size
		this.maxfetch=images.size/batch
		println "Max Fetch =>"+maxfetch
	}

	def fetch(int fetchnumber) {
		String imagehtml=""
		int start=fetchnumber*batch
		int end= start+batch

		if(end>images.size) {
			println ""
			end=images.size-1
			start= end -batch
		}

		(start..end).each { i ->
			println images[i]
			imagehtml=imagehtml+"\n"+images[i]
		}

		return imagehtml
	}

	def fetch(String path) {
		def files=[]
		new File(path).eachFileRecurse{ files << it.name }

		return files
	}


	def getDirFile(String path) {

		def sout = new StringBuffer(), serr = new StringBuffer()

		String dircmd="/bin/ls -l "

		String outfileName="out.txt"
		File outfile=new File(outfileName).createNewFile()

		dircmd=dircmd + path +" > "+outfile.absoluteFile
		println dircmd
		def proc =dircmd.execute()
		proc.consumeProcessOutput(sout, serr)
		proc.waitForOrKill(1000)
		println "out> $sout err> $serr"

		return outfile.absoluteFile
	}

	public generateImagesFile(String path, String imageFileName, String absoluteFilePathPrefix, String replacementWebServerPathPrefix) {
		return generateOutputFile(path,imageFileName,absoluteFilePathPrefix,replacementWebServerPathPrefix,new JPGFilter(),new DefaultOutputLineFormatter(LineFormatType.HTML_LI_CAPTION_DIR))
	}

	public generateSelectImagesFile(String path, String imageFileName, String absoluteFilePathPrefix, String replacementWebServerPathPrefix,String selector) {
		return generateOutputFile(path,imageFileName,absoluteFilePathPrefix,replacementWebServerPathPrefix,new SingleSelectorArtifactFilter(".jpg",selector),new DefaultOutputLineFormatter(LineFormatType.HTML_LI_CAPTION_DIR))
	}

	public generateSelectImagesFile(String path, String imageFileName, String absoluteFilePathPrefix, String replacementWebServerPathPrefix,String selector,String regexp) {
		return generateOutputFile(path,imageFileName,absoluteFilePathPrefix,replacementWebServerPathPrefix,new DefaultArtifactFilter(".jpg",selector,regexp),new DefaultOutputLineFormatter(LineFormatType.HTML_LI_CAPTION_DIR))
	}

	public generateBasedOnFormatType(String path, String imageFileName, String absoluteFilePathPrefix, String replacementWebServerPathPrefix,String selector,String regexp,def formatType) {
		def lineFormatType=LineFormatType.fromString(formatType)?:LineFormatType.SRC
		
		return generateOutputFile(path,imageFileName,absoluteFilePathPrefix,replacementWebServerPathPrefix,new DefaultArtifactFilter(".jpg",selector,regexp),new DefaultOutputLineFormatter(lineFormatType))
	}

	public generateOutputFile(String path, String imageFileName, String absoluteFilePathPrefix, String replacementWebServerPathPrefix,ArtifactFilter artifactFilter,LineFormatter outputLineFormatter) {
		def folders=[]
		def images=[]
		new File(path).eachFileRecurse{
			String file= it
			if(artifactFilter.check(file)) {
				images << file
			}
			else {
				folders<< it
			}
		}

		File imagefiles=new File(imageFileName)
		println "images are =>"
		int i=0
		images.each{

			println it
			String imagefile= it

			imagefile=imagefile.replace(absoluteFilePathPrefix,replacementWebServerPathPrefix)
			imagefiles << "\n"
			i++;
			def line = outputLineFormatter.format(imagefile, i)
			imagefiles << line
		}
		return images.size
	}

	/**
	 * TODO need to parse and generate album from registry
	 * @param path
	 * @param imageFileName
	 * @param absoluteFilePathPrefix
	 * @param replacementWebServerPathPrefix
	 * @param artifactFilter
	 * @param outputLineFormatter
	 * @return
	 */
	public generateAblums(String path, String imageFileName, String absoluteFilePathPrefix, String replacementWebServerPathPrefix,ArtifactFilter artifactFilter,LineFormatter outputLineFormatter) {
		def folders=[]
		def currentFolderImages=[]
		def folderRegistry=[:]
		
		def currentFolder=path
		
		folderRegistry << [path:currentFolderImages]
		
		new File(path).eachFileRecurse{
			String file= it
			if(artifactFilter.check(file)) {
				currentFolderImages << file
			}
			else {
				def existingImages=[]
				
				if(folderRegistry.containsKey(currentFolder))
				{
				existingImages=folderRegistry.get(currentFolder)
				
				currentFolderImages=currentFolderImages+existingImages
				
				folderRegistry << [currentFolder:currentFolderImages]
				}
				
				
				def images=[]
				folders<< it
				currentFolder=it
				currentFolderImages=images
			}
		}

		//TODO Traverse folder hierarchy and for each hierarchy get images
		//generateImageFile(imageFileName, images, absoluteFilePathPrefix, replacementWebServerPathPrefix, outputLineFormatter)
	}

	private generateImageFile(String imageFileName, List images, String absoluteFilePathPrefix, String replacementWebServerPathPrefix, LineFormatter outputLineFormatter) {
		File imagefiles=new File(imageFileName)
		
		println "images are =>"
		int i=0
		images.each{

			println it
			String imagefile= it

			imagefile=imagefile.replace(absoluteFilePathPrefix,replacementWebServerPathPrefix)
			imagefiles << "\n"
			i++;
			def line = outputLineFormatter.format(imagefile, i)
			imagefiles << line
		}
		return images.size
	}


	/**
	 * Used as the generator
	 * @param imagefile
	 * @param i
	 * @param formatType
	 * @return
	 */
	private String lineFormatter(String imagefile, int i,LineFormatType formatType) {
		def line=''
		switch(formatType){

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

			case LineFormatType.FILE:
				line=imagefile
				break;
		}
		return line
	}

	
	

	static main(args) {
		AFArtifactListFileGenerator afg= new AFArtifactListFileGenerator()
		int length=args.length
		String path=args[0] //"/media/pfaimages/pfa"
		String imageFileName = args[1] //"Imagespfa.txt"
		println "Has "+length +" args!!"
		def absoluteFilePathPrefix = args[2] // "/media/pfaimages"
		def replacementWebServerPathPrefix = args[3] //"http://localhost/vimages2"
		def selector=""
		def regexp=""
		def formatType="1"
		println "About to Generate Image File "+ imageFileName + " for images in =>"+path
		println "Will re-baseline all ["+ absoluteFilePathPrefix+"] links to =>["+replacementWebServerPathPrefix+"]"

		def totalimages=0
		switch(length)
		{
			case 5:
				selector = args[4] //"http://localhost/vimages2"
				println "Selector given as =>"+selector
				totalimages=afg.generateSelectImagesFile(path, imageFileName, absoluteFilePathPrefix, replacementWebServerPathPrefix,selector)
				break;

			case 6:

				regexp = args[5] //"1.jpg"
				selector = args[4] //"1.jpg"
				totalimages=afg.generateSelectImagesFile(path, imageFileName, absoluteFilePathPrefix, replacementWebServerPathPrefix,selector,regexp)
				println "regexp given as =>"+regexp
				break;

			case 7:
                formatType=args[6] // SRC,HTML_LI_A,HTML_LI_CAPTION_IMAGE_NAME,HTML_LI_SRC,HTML_LI,HTML_LI_CAPTION_DIR
				regexp = args[5] //"1.jpg"
				selector = args[4] //"1.jpg"
				totalimages=afg.generateBasedOnFormatType(path, imageFileName, absoluteFilePathPrefix, replacementWebServerPathPrefix,selector,regexp,formatType)
				println "regexp given as =>"+regexp
				break;
			default:

				totalimages=afg.generateImagesFile(path, imageFileName, absoluteFilePathPrefix, replacementWebServerPathPrefix)
				break;
		}
		println "Finished !! Generation =>  "+totalimages+" images"
	}

}
