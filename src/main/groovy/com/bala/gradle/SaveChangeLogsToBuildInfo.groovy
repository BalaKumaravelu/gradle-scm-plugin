package com.bala.gradle

import org.gradle.api.*
import org.gradle.api.tasks.TaskAction

/**
 * Created with IntelliJ IDEA.
 * User: Bala
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
class SaveChangeLogsToBuildInfo extends  DefaultTask
{
    String buildDate = ""
    String startTime = ""
    String endTime = new Date().format("hh:mm:ss a")

    String projectName = ""
    String branch = ""
    String buildVersion = ""

    String svnUrl = ""
    int revision = -1
    String artifactDirectory =   ""

    @TaskAction
    def Save()
    {
        endTime = new Date().format("hh:mm:ss a")
        logger.quiet("Saving ChangeLogs to BuildInfoDoc")
        logger.quiet("Getting the last successful build revision")

        def artifactDir =  getArtifactDirectory()

        int lastSuccessfulBuildRevision = GradleBuildDB.GetLastSuccessfullBuildRevision(project, getProjectName(), getBranch())
        List<ChangeLog> svnLogs = ChangeLogs.GetModifications(project, getSvnUrl(),  lastSuccessfulBuildRevision, getRevision())

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
            ChangeLogs
            {
                svnLogs.each { changeLog ->
                    ChangeLog(revision:changeLog.Revision, author:changeLog.Author, dateTime:changeLog.DateTime, message:changeLog.Message)
                }
            }
        }
        new File(artifactDir).mkdirs()
        new File(artifactDir + "/BuildInfo.xml").withPrintWriter {out ->
            out.println(stringWriter)
        }
        logger.quiet(stringWriter.toString())
        logger.quiet("Successfully saved BuildInfoDoc..")
    }

}
