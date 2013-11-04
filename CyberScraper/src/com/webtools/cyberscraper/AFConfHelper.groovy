package com.webtools.cyberscraper;
import groovy.util.logging.Log ;
@Log
public class AFConfHelper {

	def config
	def defaultConfigFileName="afconfig.groovy"
	public static final SLASH="/"
	String userHome = System.getProperty( "user.home" )

	  public AFConfHelper()
	  {
		  def configFileURL = new File(userHome+SLASH+this.defaultConfigFileName).toURI().toURL()
		  log.info "Loading Configuration :"+configFileURL
		  config =new ConfigSlurper().parse(configFileURL)
	  }
	  
	  public AFConfHelper(String configFileName)
	  {
		  this.defaultConfigFileName=configFileName
		  def configFileURL = new File(userHome+SLASH+configFileName).toURI().toURL()
		  this.config =new ConfigSlurper().parse(configFileURL)
		  
	  }
	  
	  public String get(String group, String parameter) {
		  log.info """Fetching $parameter for $group"""
		def parameterValue=config."$group"."$parameter"
		log.info """Got: $parameterValue"""
		  return validate(parameterValue)
	  }
	  
	  /**
	   * @param parameter
	   * @return
	   */
	  public String getAfParam(String parameter) {
		  log.info """Fetching $parameter for AF"""
		def parameterValue=config.af."$parameter"
		log.info """Got: $parameterValue"""
		  validate(parameterValue)
	  }
	  
	  /**
	   * Method returns "" if parameter is missing
	   * @param parameterValue
	   * @return
	   */
	  private validate(def parameterValue) {
		  if(checkMissing(parameterValue))
		  {
			  return ""
			  }
		  else
		  {
			  return parameterValue
		  }
	  }
	  
	  /**
	   * Need to check if this is the only missing scenario, may have to add checkValid in future -Vinod
	   * return true if missing
	   * @param parameterValue
	   * @return
	   */
	  private boolean checkMissing(def parameterValue) {
		  log.info "Checking.. "+parameterValue
		  def isMissing=false
		  
		  if(parameterValue instanceof Integer )
		  {
			 return isMissing 
		  }
		  
		  if(parameterValue instanceof ConfigObject )
		  {
			  isMissing = parameterValue.isEmpty()
		  }
		 else
		  {
		  isMissing = ((parameterValue.trim()=="{}")||(parameterValue.trim()==""))
		  }
		  log.info "Parameter isMissing :"+isMissing
		  return isMissing
	  }
}
