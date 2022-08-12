package com.bala.gradle

import org.gradle.api.*
import org.gradle.api.plugins.*
import org.gradle.api.tasks.TaskAction
import groovy.sql.Sql

/**
 * Created with IntelliJ IDEA.
 * User: Bala
 * Time: 4:44 PM
 * To change this template use File | Settings | File Templates.
 */
class InsertBuildInfoToDB extends  DefaultTask
{
    String   projectName = ""
    String   branch = ""
    String   svnUrl = ""

    String   buildVersion =   ""
    int   revision =   -1
    String   artifactDirectory = ""
    int status = 5


    @TaskAction
    def Save()
    {
        logger.quiet("Saving BuildInfo to DB")

        GradleBuildDB.InsertBuildInfo(project, getProjectName(), getBranch(), getBuildVersion(), getSvnUrl(), getRevision(), getArtifactDirectory(), status)

        logger.quiet("Successfully saved BuildInfo to DB.")
    }
}