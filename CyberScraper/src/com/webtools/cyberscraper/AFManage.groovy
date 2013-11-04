package com.webtools.cyberscraper;

public class AFManage {
	def debug=false
	def command=""
	public static void main(String args)
	{
		
		
		def afManage = new AFManage()
		  
				 def cli = new CliBuilder(usage: 'java -jar afManage0.1.jar -c[dh] "command"')
				 cli.h(longOpt: 'help'  , 'usage information'   , required: false           )
				 cli.c(longOpt: 'command', 'command to Manage e.g findmissing, generatescript', required: true  , args: 1 )
				 cli.d(longOpt: 'debug' , 'enable debugging'    , required: false           )
				 OptionAccessor opt = cli.parse(args)
				 if(!opt) {
					 return
				 }
				 // print usage if -h, --help, or no argument is given
				 if(opt.h || opt.arguments().isEmpty()) {
					 cli.usage()
				 }
				 if( opt.d ) {
					 afManage.debug = true
				 }
				 if( opt.c ) {
					 afManage.command = opt.c
				 }
				
				 
				 switch(afManage.command)
				 {
					 case "findmissing":
					  break;
					  case "generatescript":
					  break;
				 }
	}

}
