package com.bala.gradle

/**
 * Created with IntelliJ IDEA.
 * User: Bala
 * Time: 4:39 PM
 * To change this template use File | Settings | File Templates.
 */


import org.gradle.api.*
import org.gradle.api.plugins.*
import org.gradle.api.tasks.TaskAction

import java.io.*;
import java.util.Properties;
import java.util.Date;

import javax.mail.*;
import javax.activation.*;
import javax.mail.internet.*;
import javax.mail.util.*;


class SendBuildMail extends  DefaultTask
{
    String mailHost = "mail-host.bala.com"
    String mailPort = "25"
    String artifactDirectory =""
    String buildInfoXslFilePath = "buildinfo.xsl"
    String mailRecipients = "Bala@bala.com, Bala@bala.com"
    String senderEmail = "builder@bala.com"

    String projectName = ""
    String branch = ""
    String buildVersion = ""
    Boolean isSuccessful = false
    Boolean buildIsSuccessful = false


    @TaskAction
    def SendBuilEmail()
    {
        logger.quiet("Sending Build email")
        buildIsSuccessful = (project.extensions['ext'].properties['buildIsSuccessful']).toBoolean()

        def buildInfoFilePath = getArtifactDirectory()+"/BuildInfo.xml"
        def buildEmailHtmlFilePath  = getArtifactDirectory()+"/BuildEmail.html"
        logger.quiet("Transforming for Build Email content")
        logger.quiet("buildIsSuccessful=${buildIsSuccessful}")
        logger.quiet("buildInfoFilePath=${buildInfoFilePath}")
        logger.quiet("buildInfoXslFilePath=${getBuildInfoXslFilePath()}")
        logger.quiet("buildEmailHtmlFilePath=${buildEmailHtmlFilePath}")

        project.ant.xslt(in:buildInfoFilePath, out:buildEmailHtmlFilePath, extension:".html", style:getBuildInfoXslFilePath())
        logger.quiet("Successfully Transformed and created Build Email content")

        logger.quiet("Sending Build Email")
        Properties mailProperties = new Properties();
        mailProperties.setProperty("mail.transport.protocol", "smtp");
        mailProperties.setProperty("mail.host", getMailHost());
        mailProperties.setProperty("mail.smtp.port", getMailPort());

        Session session = Session.getDefaultInstance(mailProperties,null);
        MimeMessage message = new MimeMessage( session);

        message.setFrom(new InternetAddress(senderEmail));

        message.setRecipients(MimeMessage.RecipientType.TO, getMailRecipients())

        String subject = "Failed : ${getProjectName()} ${getBranch()} ${getBuildVersion()}"
        if(buildIsSuccessful)
        {
            subject = "Successful : ${getProjectName()} ${getBranch()} ${getBuildVersion()}"
        }
        message.setSubject(subject);

        message.setHeader("X-Mailer", "sendhtml");

        String buildMailContent = new File(buildEmailHtmlFilePath).text
        message.setContent(buildMailContent, "text/html" )

        Transport transporter =  session.getTransport("smtp");
        transporter.connect();
        transporter.send(message);

        logger.quiet("Successfully sent Build Email..")
    }
}
