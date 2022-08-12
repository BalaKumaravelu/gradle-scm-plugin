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
class UpdateBuildInfoInDB extends  DefaultTask
{
    String   projectName = ""
    String   branch = "trunk"
    String   endTime =   new Date().format("yyyy-MM-dd HH:mm:ss")
    String   buildVersion =   ""
    int status = -1
    //ToDo Add saving good and bad build status

    @TaskAction
    def Save()
    {
        logger.quiet("Saving BuildEndInfo to DB")

        if((project.extensions['ext'].properties['buildIsSuccessful']).toBoolean())
        {
            logger.quiet("The build was successful, setting the build status to success")
            status = 0
        }
        else
        {
            logger.quiet("The build failed, setting the build status to Failed")
        }

        GradleBuildDB.UpdateBuildInfo(project, getProjectName(), getBranch(), getBuildVersion(), status)

        logger.quiet("Successfully saved BuildEndInfo to DB.")
    }
}