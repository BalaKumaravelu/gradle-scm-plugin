package com.bala.gradle

import org.gradle.api.*
import org.gradle.api.Plugin.*

class GradleScmPlugin implements Plugin<Project> {
    void apply(Project project)
    {
        project.extensions.create("GradleScmPlugin",GradleScmPluginExtension)

        project.task('Checkout', type: SvnCheckout){
            conventionMapping.userName = { project.GradleScmPlugin.svnUserName }
            conventionMapping.userPassword = { project.GradleScmPlugin.svnUserPassword }
            conventionMapping.srcUrl =  { project.GradleScmPlugin.projectUrl }
            conventionMapping.dir = { project.GradleScmPlugin.projectBasePath }
            conventionMapping.revision = { project.GradleScmPlugin.svnRevision.toInteger() }
        }

        project.task('Revert', type: SvnRevert){
            conventionMapping.userName = { project.GradleScmPlugin.svnUserName }
            conventionMapping.userPassword = { project.GradleScmPlugin.svnUserPassword }
            conventionMapping.dir = { project.GradleScmPlugin.projectBasePath }
        }

        project.task('DeleteUnVersioned', type: SvnDeleteUnVersioned){
            conventionMapping.userName = { project.GradleScmPlugin.svnUserName }
            conventionMapping.userPassword = { project.GradleScmPlugin.svnUserPassword }
            conventionMapping.dir = { project.GradleScmPlugin.projectBasePath }
        }

        project.task('Tag', type: SvnTag){
            conventionMapping.userName = { project.GradleScmPlugin.svnUserName }
            conventionMapping.userPassword = { project.GradleScmPlugin.svnUserPassword }
            conventionMapping.srcUrl = { project.GradleScmPlugin.projectUrl }
            conventionMapping.destUrl = { project.GradleScmPlugin.tagDestinationUrl }
            conventionMapping.revision = { project.GradleScmPlugin.svnRevision.toInteger() }
            conventionMapping.message = { project.GradleScmPlugin.tagMessage }
        }

        project.task('InitBuildInfo', type: SaveBuildInfo){
            conventionMapping.buildDate = { project.GradleScmPlugin.buildDate }
            conventionMapping.startTime = { project.GradleScmPlugin.startTime }

            conventionMapping.projectName = { project.GradleScmPlugin.projectName }
            conventionMapping.branch = { project.GradleScmPlugin.branch }
            conventionMapping.buildVersion = { project.GradleScmPlugin.buildVersion }
            conventionMapping.svnUrl = { project.GradleScmPlugin.projectUrl }
            conventionMapping.revision = { project.GradleScmPlugin.svnRevision.toInteger() }
            conventionMapping.artifactDirectory = { project.GradleScmPlugin.artifactDirectory }
        }

        project.task('FinalizeBuildInfo', type: SaveChangeLogsToBuildInfo){
            conventionMapping.buildDate = { project.GradleScmPlugin.buildDate }
            conventionMapping.startTime = { project.GradleScmPlugin.startTime }

            conventionMapping.projectName = { project.GradleScmPlugin.projectName }
            conventionMapping.branch = { project.GradleScmPlugin.branch }
            conventionMapping.buildVersion = { project.GradleScmPlugin.buildVersion }
            conventionMapping.svnUrl = { project.GradleScmPlugin.projectUrl }
            conventionMapping.revision = { project.GradleScmPlugin.svnRevision.toInteger() }
            conventionMapping.artifactDirectory = { project.GradleScmPlugin.artifactDirectory }
        }

        project.task('InitBuildInfoToDB', type: InsertBuildInfoToDB){
            conventionMapping.projectName = { project.GradleScmPlugin.projectName }
            conventionMapping.branch = { project.GradleScmPlugin.branch }
            conventionMapping.buildVersion = { project.GradleScmPlugin.buildVersion }
            conventionMapping.svnUrl = { project.GradleScmPlugin.projectUrl }
            conventionMapping.revision = { project.GradleScmPlugin.svnRevision.toInteger() }
            conventionMapping.artifactDirectory = { project.GradleScmPlugin.artifactDirectory }
        }

        project.task('FinalizeBuildInfoInDB', type: UpdateBuildInfoInDB){
            conventionMapping.projectName = { project.GradleScmPlugin.projectName }
            conventionMapping.branch = { project.GradleScmPlugin.branch }
            conventionMapping.buildVersion = { project.GradleScmPlugin.buildVersion }
        }

        project.task('SendBuildMail', type: SendBuildMail){
            conventionMapping.projectName = { project.GradleScmPlugin.projectName }
            conventionMapping.branch = { project.GradleScmPlugin.branch }
            conventionMapping.buildVersion = { project.GradleScmPlugin.buildVersion }

            conventionMapping.artifactDirectory = { project.GradleScmPlugin.artifactDirectory }

            conventionMapping.mailHost = { project.GradleScmPlugin.mailHost }
            conventionMapping.mailPort = { project.GradleScmPlugin.mailPort }

            conventionMapping.buildInfoXslFilePath = { project.GradleScmPlugin.buildInfoXslFilePath }
            conventionMapping.mailRecipients = { project.GradleScmPlugin.mailRecipients }
            conventionMapping.senderEmail = { project.GradleScmPlugin.senderEmail }

            conventionMapping.isSuccessful = { project.GradleScmPlugin.isSuccessful.toBoolean() }
        }


    }

}

class GradleScmPluginExtension {
    String buildDate =""
    String startTime=""
    String endTime =""

    String projectName = ""
    String branch = ""
    String buildVersion = ""

    String   artifactDirectory = ""
    int status = 2

    String svnUserName = ""
    String svnUserPassword = ""
    String projectUrl = ""
    String projectBasePath = ""
    int svnRevision = -1
    String tagDestinationUrl = ""
    String tagMessage=""

    String mySqlUserName = ""
    String mySqlUserPassword = ""
    String mySqlConnectionString = ""

    String mailHost = "mail-host.bala.com"
    String mailPort = "25"
    String buildInfoXslFilePath = ""
    String mailRecipients = "Bala@bala.com"
    String senderEmail = "builder@bala.com"
    Boolean isSuccessful = true
}
