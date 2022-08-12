package com.bala.gradle

import org.gradle.api.*
import org.gradle.api.tasks.TaskAction
//ToDo Remove this
/**
 * Created with IntelliJ IDEA.
 * User: Bala
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
class SaveBuildEndInfo extends  DefaultTask
{
    String   projectName = ""
    String   branch = "trunk"
    String   svnUrl = ""

    String   buildDate = new Date().format("MM-dd-yy")
    String   startTime = new Date().format("hh:mm:ss a")
    String   endTime =   new Date().format("hh:mm:ss a")

    String   buildVersion =   "0.1.2.3"
    int   svnRevision =   -1
    String   artifactDirectory =   ""


    @TaskAction
    def Save()
    {
        logger.quiet("Saving BuildInfoDoc")
        def stringWriter = new StringWriter()
        def buildInfoDocXml = new groovy.xml.MarkupBuilder(stringWriter)

        buildInfoDocXml.BuildInfoDoc
                {
                    BuildAttributes
                            {
                                BuildAttribute(name:"Project", value:"${projectName}")
                                BuildAttribute(name:"Branch", value:"${branch}")
                                BuildAttribute(name:"Svn Url", value:"${svnUrl}")

                                BuildAttribute(name:"Build Date", value:"${buildDate}")
                                BuildAttribute(name:"Start Time", value:"${startTime}")
                                BuildAttribute(name:"End Time", value:"${endTime}")

                                BuildAttribute(name:"Build Version", value:"${buildVersion}")
                                BuildAttribute(name:"Svn Revision", value:"${svnRevision}")
                                BuildAttribute(name:"Artifact Directory", value:"${artifactDirectory}")
                            }
                }

        new File("BuildInfo.xml").withPrintWriter {out ->
            out.println(stringWriter)
        }
        logger.quiet("Successfully saved BuildInfoDoc..")
        logger.quiet(stringWriter.toString())
    }

    def GetModifications( )
    {
        //List
        List<ChangeLog> modifications = new ArrayList<ChangeLog>();


    }


}
