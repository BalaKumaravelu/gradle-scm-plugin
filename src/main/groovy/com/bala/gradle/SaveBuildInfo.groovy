package com.bala.gradle

import org.gradle.api.*
import org.gradle.api.plugins.*
import org.gradle.api.tasks.TaskAction

/**
 * Created with IntelliJ IDEA.
 * User: Bala
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
class SaveBuildInfo extends  DefaultTask
{
    String projectName = ""
    String branch = ""
    String svnUrl = ""

    String buildDate = ""
    String startTime = ""
    String endTime = new Date().format("hh:mm:ss a")

    String buildVersion = ""
    int revision = -1
    String artifactDirectory = ""


    @TaskAction
    def Save()
    {
        logger.quiet("Saving BuildInfoDoc")
        def artifactDir =  getArtifactDirectory()
        def stringWriter = new StringWriter()
        def buildInfoDocXml = new groovy.xml.MarkupBuilder(stringWriter)

        buildInfoDocXml.BuildInfoDoc
        {
            BuildAttributes
            {
                BuildAttribute(name:"Project", value:"${getProjectName()}")
                BuildAttribute(name:"Branch", value:"${getBranch()}")
                BuildAttribute(name:"Build Version", value:"${getBuildVersion()}")

                BuildAttribute(name:"Build Date", value:"${getBuildDate()}")
                BuildAttribute(name:"Start Time", value:"${getStartTime()}")
                BuildAttribute(name:"End Time", value:"${endTime}")

                BuildAttribute(name:"Svn Revision", value:"${getRevision()}")
                BuildAttribute(name:"Svn Url", value:"${getSvnUrl()}")
                BuildAttribute(name:"Artifact Directory", value:"${getArtifactDirectory()}")
            }
        }
        new File(artifactDir).mkdirs()
        new File(artifactDir + "/BuildInfo.xml").withPrintWriter {out ->
            out.println(stringWriter)
        }
        logger.quiet("Successfully saved BuildInfoDoc..")
        logger.quiet(stringWriter.toString())
    }
}
