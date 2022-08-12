package com.bala.gradle

import org.gradle.api.*

/**
 * Created with IntelliJ IDEA.
 * User: Bala
 * Time: 4:15 PM
 * To change this template use File | Settings | File Templates.
 */
class ChangeLogs
{
    def static List<ChangeLog> GetModifications(Project project, String svnUrl, int fromRevision, int toRevision)
    {
        List<ChangeLog> SvnLogs = new ArrayList<ChangeLog>();
        project.logger.quiet("Getting the ChangeLogs")
        project.logger.quiet("SvnUrl        :  ${svnUrl}")
        project.logger.quiet("From Revision :  ${fromRevision}")
        project.logger.quiet("To Revision   :  ${toRevision}\n")

        def revisionRange = toRevision - fromRevision
        if((revisionRange == 0) || (toRevision == -1) || (fromRevision ==-1))
        {
            project.logger.quiet("Revision range is not valid, so skipping getting ChangeLogs")
        }
        else
        {
            try
            {
                String command = "svn log -l ${revisionRange}  \"${svnUrl}\"";
                Process svnProcess = Runtime.getRuntime().exec(command);

                BufferedReader stdInput = new BufferedReader(new InputStreamReader(svnProcess.getInputStream()));
                BufferedReader stdError = new BufferedReader(new InputStreamReader(svnProcess.getErrorStream()));

                def revisionRegex = '^r(\\d+) \\| (\\S+) \\| (\\d+-\\d+-\\d+) (\\d+:\\d+:\\d+)(.*)$'
                def logSeparatorRegex = '^-+$'
                def blankLineRegex = '^\\s*$'

                String logOutputLine =""
                ChangeLog changeLog
                Boolean hasData = true

                while(hasData)
                {
                    logOutputLine = stdInput.readLine()

                    if(logOutputLine == null)
                    {
                        hasData=false
                    }

                    def logSeparatorLineMatcher = logOutputLine =~ logSeparatorRegex
                    if(logSeparatorLineMatcher.matches())
                    {
                        if(changeLog != null)
                        {
                            if(changeLog.Revision.toInteger() >= fromRevision )
                            {
                                SvnLogs.add(changeLog)
                            }
                            else
                            {
                                project.logger.quiet("Skipping including Revision : "+changeLog.Revision + " as its out of the range")
                            }
                        }
                    }
                    else if(hasData)
                    {
                        def revisionLogLineMatcher = logOutputLine =~ revisionRegex

                        if(revisionLogLineMatcher.matches())
                        {
                            changeLog = new ChangeLog(Revision: revisionLogLineMatcher[0][1], Author: revisionLogLineMatcher[0][2], DateTime: revisionLogLineMatcher[0][3]+" " + revisionLogLineMatcher[0][4] )
                        }
                        else if(!(logOutputLine =~ blankLineRegex).matches())
                        {
                            changeLog.AppendMessage(logOutputLine)
                        }
                    }
                }

                while (( logOutputLine = stdError.readLine()) != null)
                {
                    project.logger.error(logOutputLine)
                    //ToDo should we throw exception, wait and see what causes this
                }

                project.logger.quiet("Successfully extracted the ChangeLogs")
            }
            catch (Exception e)
            {
                project.logger.error("Cannot extract ChangeLogs, due to exception : " + e.message)
            }
        }
        project.logger.quiet("Total Extracted ChangeLogs : "+SvnLogs.size())
        project.logger.quiet("------------------------------------------------------" )
        project.logger.quiet("Revision  Author       DateTime                Message" )
        project.logger.quiet("------------------------------------------------------" )
        SvnLogs.each { changeLog ->
            project.logger.quiet(String.format("%-6s %-15s %-23s %s", changeLog.Revision,  changeLog.Author, changeLog.DateTime, changeLog.Message ))
        }

        return SvnLogs
    }


}
