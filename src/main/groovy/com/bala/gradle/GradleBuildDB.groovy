package com.bala.gradle

import org.gradle.api.*
import org.gradle.api.plugins.*
import org.gradle.api.tasks.TaskAction
import groovy.sql.Sql

/**
 * Created with IntelliJ IDEA.
 * User: Bala
 * Time: 4:22 PM
 * To change this template use File | Settings | File Templates.
 */
class GradleBuildDB
{
    def static GetLastSuccessfullBuildRevision(Project project, String projectName, String branch)
    {
        int lastSuccessfullBuildRevision = 0

        project.logger.quiet("Getting the last successful Build Revision for  [Project=${projectName}] - [Branch=${branch}]")

        def buildsDb = Sql.newInstance(project.properties['mysqlConnectionString'], project.properties['mysqlUserName'],  project.properties['mysqlUserPassword'], "com.mysql.jdbc.Driver" )

        buildsDb.eachRow("""SELECT revision FROM gradlebuilds.build_history
                             WHERE project=${projectName} AND branch=${branch} AND status=0
                             ORDER BY pkey DESC LIMIT 1;""")
        {
            row ->
            lastSuccessfullBuildRevision = row.revision
        }

        project.logger.quiet("Successfully got the last successfull Build revision : ${lastSuccessfullBuildRevision}")
        return lastSuccessfullBuildRevision
    }

    def static UpdateBuildInfo(Project project, String projectName, String branch, String buildVersion, int status)
    {
        String   endTime =   new Date().format("yyyy-MM-dd HH:mm:ss")

        def gradleBuildTable = Sql.newInstance(project.properties['mysqlConnectionString'], project.properties['mysqlUserName'],  project.properties['mysqlUserPassword'], "com.mysql.jdbc.Driver" )

        gradleBuildTable.executeUpdate("""UPDATE gradlebuilds.build_history
                             SET endtime=${endTime}, status=${status}
                             WHERE project=${projectName} AND branch=${branch} AND buildversion=${buildVersion}
                             ORDER BY pkey DESC LIMIT 1;""")
    }

    def static InsertBuildInfo(Project project, String projectName, String branch, String buildVersion, String svnUrl, int revision, String artifactDirectory, int status)
    {
        def gradleBuildTable = Sql.newInstance(project.properties['mysqlConnectionString'], project.properties['mysqlUserName'],  project.properties['mysqlUserPassword'], "com.mysql.jdbc.Driver" )
        gradleBuildTable.executeInsert("""INSERT INTO gradlebuilds.build_history
                             (project, branch, svnurl, buildversion, revision, artifactDir, status)
                             VALUES (${projectName}, ${branch}, ${svnUrl}, ${buildVersion}, ${revision}, ${artifactDirectory}, ${status} );""")
    }
}
