package com.bala.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
/**
 * Created with IntelliJ IDEA.
 * User: Bala
 * Time: 1:58 PM
 * To change this template use File | Settings | File Templates.
 */
class SvnTag extends DefaultTask
{
    String userName = ""
    String userPassword = ""
    String srcUrl =  ''
    String destUrl =  ''
    int revision = -1
    String message =  ""

    @TaskAction
    def Tag()
    {
        logger.quiet("\nTagging the build source")
        logger.quiet("Src Url  :  ${getSrcUrl()}")
        logger.quiet("Dest Url :  ${getDestUrl()}")
        logger.quiet("Message  :  ${getMessage()}")
        logger.quiet("Revision :  ${getRevision()}")

        if(IsValidParameters())
        {
            logger.quiet("Tagging the build source")
            String command = String.format("svn copy --non-interactive --trust-server-cert --username %s --password %s -r %d -m \"%s\"  \"%s\" \"%s\"",
                                            getUserName(),
                                            getUserPassword(),
                                            getRevision(),
                                            getMessage(),
                                            getSrcUrl(),
                                            getDestUrl())

            Process svnProcess = Runtime.getRuntime().exec(command);

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(svnProcess.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(svnProcess.getErrorStream()));

            String statusOutputLine =""
            while ((statusOutputLine = stdInput.readLine()) != null)
            {
                logger.quiet(" " + statusOutputLine);
            }

            while (( statusOutputLine = stdError.readLine()) != null)
            {
                logger.error(statusOutputLine)
                throw new Exception(statusOutputLine)
            }

            logger.quiet("Successfully tagged the build source")
        }
    }

    def Boolean IsValidParameters()
    {
        logger.quiet("\nChecking the parameters passed for tagging task")

        if((!getSrcUrl().isEmpty()) && (getSrcUrl().length() > 30))
        {
            logger.quiet("The source url is valid")
        }
        else
        {
            throw new Exception("The source url is not valid, please check it. [SourceUrl=${getSrcUrl()}]")
        }

        if(GradleUtility.IsValidTagUrl(getDestUrl()) && (getDestUrl().length() > 30))
        {
            logger.quiet("The destination url is valid")
        }
        else
        {
            throw new Exception("The destination url is not valid, please check it. [DestUrl=${getDestUrl()}]")
        }

        if(getMessage().isEmpty())
        {
            throw new Exception("The message is empty, please check it")
        }
        else
        {
            logger.quiet("The message is valid.")
        }

        if(getRevision() <=0)
        {
            throw new Exception("The Revision value is not valid")
        }
        else
        {
            logger.quiet("The Revision is valid.")
        }

        return true;
    }
}