package com.bala.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Created with IntelliJ IDEA.
 * User: Bala
 * Time: 1:23 PM
 * To change this template use File | Settings | File Templates.
 */
class SvnRevert extends DefaultTask
{
    String userName = ""
    String userPassword = ""
    String dir =  ""

    @TaskAction
    def Revert()
    {
        logger.quiet("Reverting the work space at ${getDir()}")

        if(getDir().isEmpty())
        {
             throw new Exception ("The passed path is not valid, its empty")
        }

        if(!GradleUtility.DirectoryExists(getDir()))
        {
            logger.quiet("The workspace directory is absent, Skipping revert")
            return
        }

        String command =String.format("svn revert --non-interactive --trust-server-cert --username %s --password %s -R \"%s\"",
                                      getUserName(),
                                      getUserPassword(),
                                      getDir());

        Process svnProcess = Runtime.getRuntime().exec(command);

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(svnProcess.getInputStream()));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(svnProcess.getErrorStream()));

        String statusOutputLine =""
        while ((statusOutputLine = stdInput.readLine()) != null)
        {
            logger.quiet(statusOutputLine);
        }

        while (( statusOutputLine = stdError.readLine()) != null)
        {
            //ToDo should we throw exception, wait and see what causes this
            logger.error(statusOutputLine)
            if(statusOutputLine.contains("is not a working copy"))
            {
                throw new Exception(statusOutputLine)
            }
        }
        logger.quiet("Successfully reverted the work space")
    }

}