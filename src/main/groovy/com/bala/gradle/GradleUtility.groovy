package com.bala.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
/**
 * Created with IntelliJ IDEA.
 * User: Bala
 * Time: 11:34 AM
 * To change this template use File | Settings | File Templates.
 */
class GradleUtility
{
    def static Init(Project project)
    {
        project.ant.taskdef(resource: 'org/tigris/subversion/svnant/svnantlib.xml') {
            classpath {
                fileset(dir: System.getenv("ANT_HOME") + '/lib', includes: '*.jar')
            }
        }

        project.ant.svnSetting(id: 'svn.settings', javahl: 'false')
    }

    def static DirectoryExists(String dir)
    {
        File file = new File(dir)
        return file.exists();
    }

    def static IsValidFileOrDirectory(String file)
    {
        if(!file)
        {
            return false
        }

        if(file.length() < 10)
        {
            return false
        }

        return true
    }

    def static IsGradleDirectory(String file)
    {
        if(file.contains(".gradle"))
        {
            return true
        }

        return false
    }

    def static IsValidTagUrl(String url)
    {
        if(!url.contains('/tags/'))
        {
            return false
        }
        //ToDo Check there is a /tag/ location in the url and the last one is a valid build number
        return true
    }
}
