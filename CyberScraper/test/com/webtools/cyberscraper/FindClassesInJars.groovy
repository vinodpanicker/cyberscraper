package com.webtools.cyberscraper



@Grapes(
	@Grab(group='commons-cli', module='commons-cli', version='1.2')
)
  
/**
 * findClassesInJars.groovy
 *
 * findClassesInJars.groovy -d <<root_directories>> -s <<strings_to_search_for>>
 *
 * Script that looks for provided String in JAR files (assumed to have .jar
 * extensions) in the provided directory and all of its subdirectories.
 */

def cli = new CliBuilder(
   usage: 'findClassesInJars.groovy -d <root_directories> -s <strings_to_search_for>',
   header: '\nAvailable options (use -h for help):\n',
   footer: '\nInformation provided via above options is used to generate printed string.\n')
import org.apache.commons.cli.Option
cli.with
{
   h(longOpt: 'help', 'Help', args: 0, required: false)
   d(longOpt: 'directories', 'Two arguments, separated by a comma', args: Option.UNLIMITED_VALUES, valueSeparator: ',', required: true)
   s(longOpt: 'strings', 'Strings (class names) to search for in JARs', args: Option.UNLIMITED_VALUES, valueSeparator: ',', required: true)
}
def opt = cli.parse(args)
if (!opt) return
if (opt.h) cli.usage()

def directories = opt.ds
def stringsToSearchFor = opt.ss

import java.util.zip.ZipFile
import java.util.zip.ZipException

def matches = new TreeMap<String, Set<String>>()
directories.each
{ directory ->
   def dir = new File(directory)
   stringsToSearchFor.each
   { stringToFind ->
      dir.eachFileRecurse
      { file->
         if (file.isFile() && file.name.endsWith("jar"))
         {
            try
            {
               zip = new ZipFile(file)
               entries = zip.entries()
               entries.each
               { entry->
                  if (entry.name.contains(stringToFind))
                  {
                     def pathPlusMatch = "${file.canonicalPath} [${entry.name}]"
                     if (matches.get(stringToFind))
                     {
                        matches.get(stringToFind).add(pathPlusMatch)
                     }
                     else
                     {
                        def containingJars = new TreeSet<String>()
                        containingJars.add(pathPlusMatch)
                        matches.put(stringToFind, containingJars)
                     }
                  }
               }
            }
            catch (ZipException zipEx)
            {
               println "Unable to open file ${file.name}"
            }
         }
      }
   }
}

matches.each
{ searchString, containingJarNames ->
   println "String '${searchString}' Found:"
   containingJarNames.each
   { containingJarName ->
      println "\t${containingJarName}"
   }
}
