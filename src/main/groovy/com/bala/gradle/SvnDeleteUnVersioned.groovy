package com.bala.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Created with IntelliJ IDEA.
 * User: Bala
 * Time: 1:44 PM
 * To change this template use File | Settings | File Templates.
 */
class SvnDeleteUnVersioned extends DefaultTask
{
    String userName = ""
    String userPassword = ""
    String dir =  ""

    @TaskAction
    def DeleteUnVersioned()
    {
        logger.quiet("Deleting UnVersioned files and directories at : ${getDir()}\n")

        if(GradleUtility.DirectoryExists(getDir()))
        {
            logger.error("The workspace directory exists")
        }
        else
        {
            logger.error("The workspace directory is absent, Skipping cleanup")
            return
        }

        logger.quiet("Going to delete UnVersioned at the work space at : ${getDir()}")

        String command =String.format("svn status --non-interactive --trust-server-cert --username %s --password %s \"%s\"",
                                      getUserName(),
                                      getUserPassword(),
                                      getDir());

        Process svnProcess = Runtime.getRuntime().exec(command);

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(svnProcess.getInputStream()));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(svnProcess.getErrorStream()));

        String statusOutputLine =""
        while ((statusOutputLine = stdInput.readLine()) != null)
        {
            java.util.regex.Pattern p = java.util.regex.Pattern.compile("([?!|M]+)\\s+(.*)");
            java.util.regex.Matcher m = p.matcher(statusOutputLine);

            if (m.matches())
            {
                if(m.group(1).equalsIgnoreCase("X") || m.group(1).equalsIgnoreCase("?"))
                {
                    logger.quiet("\t" + m.group(2));
                    if(GradleUtility.IsValidFileOrDirectory(m.group(2)))
                    {
                        if(GradleUtility.IsGradleDirectory(m.group(2)))
                        {
                            logger.quiet("\t\tIts gradle directory, skipping it")
                        }
                        else
                        {
                            logger.quiet("\t\tIts valid UnVersioned file or Directory, going to delete it")
                            File file = new File(m.group(2))
                            if(file.isDirectory())
                            {
                                logger.quiet("\t\tDeleting directory " + file.path)
                                file.deleteDir()
                            }
                            else
                            {
                                logger.quiet("\t\tDeleting "+file.path)
                                file.delete()
                            }
                        }
                    }
                    else
                    {
                        logger.error("The unversioned file or directory is not valid and cannot delete it : ${m.group(2)}")
                        throw new Exception ("The unversioned file or directory is not valid and cannot delete it : ${m.group(2)}")
                    }
                }
                else
                {
                    logger.error("The Svn revert didnt do its job, check the following file/dir: ${m.group(2)}")
                    throw new Exception ("The Svn revert didnt do its job, check the following file/dir: ${m.group(2)}")
                }
            }
        }

        while (( statusOutputLine = stdError.readLine()) != null)
        {
            if(statusOutputLine.contains("is not a working copy"))
            {
                logger.quiet(statusOutputLine)
                throw new Exception ("The destination directory is not a working copy : ${getDir}")
            }
            else
            {
                logger.error(statusOutputLine);
            }
        }

        logger.quiet("Successfully deleted UnVersioned files and directories at : \${getDir}")
    }
}