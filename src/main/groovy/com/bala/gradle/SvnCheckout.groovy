package com.bala.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
/**
 * Created with IntelliJ IDEA.
 * User: Bala
 * Time: 11:20 AM
 * To change this template use File | Settings | File Templates.
 */

class SvnCheckout extends DefaultTask
{
    //ToDo change the task variables to be similar to plugin configuration
    String userName = ""
    String userPassword = ""
    String srcUrl =  ''
    int revision = -1
    String dir =  ""


    @TaskAction
    def Checkout()
    {
        logger.quiet("Checking out the work space")
        logger.quiet("Src Url          : "+ getSrcUrl())
        logger.quiet("Destination Path :  " + getDir())
        logger.quiet("Revision         :  "+getRevision())

        try
        {
            if(!hasValidParameters())
            {
                throw new Exception("Parameters with in-valid values were passed")
            }
            String command =String.format("svn co -r %d  --non-interactive --trust-server-cert --username %s --password %s --depth infinity \"%s\" \"%s\"",
                                          getRevision(),
                                          getUserName(),
                                          getUserPassword(),
                                          getSrcUrl(),
                                          getDir());

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
                //ToDo should we throw exception, wait and see what causes this
            }
            logger.quiet("Successfully Checked out the work space")
        }
        catch (Exception e)
        {
            logger.error("Cannot Checkout workspace, due to exception : " + e.message)
            throw e
            //ToDo check the Exception, some of it might be ok to ignore
        }
    }

    def Boolean hasValidParameters()
    {
        if( (getRevision() <=0) || (getUserName().isAllWhitespace()) || (getUserPassword().isAllWhitespace()) || (getSrcUrl().isAllWhitespace()) || (getDir().isAllWhitespace()) )
        {
            return false
        }
        return true
    }
}
