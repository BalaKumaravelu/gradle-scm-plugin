package com.bala.gradle

/**
 * Created with IntelliJ IDEA.
 * User: Bala
 * Time: 3:21 PM
 * To change this template use File | Settings | File Templates.
 */
class ChangeLog
{
    String DateTime = ""
    String Author=""
    String Revision = "-1"
    String Message=""

    public void AppendMessage(String msg)
    {
        Message += msg
    }
}
