package com.meticulus.codinalteparts.app;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by meticulus on 4/7/14.
 */
public class CommandUtility {

    static int BUFF_LEN = 1024 * 100;

    public static void ExecuteNoReturn(String command, Boolean useroot) throws Exception {
        Process process;
                if(useroot)
                    process = Runtime.getRuntime().exec(new String[]{"su"});
                else
                    process = Runtime.getRuntime().exec(new String[]{"/system/bin/sh" ,"-c"});
        DataOutputStream os = new DataOutputStream(process.getOutputStream());

        os.writeBytes(command+"\n");
        os.writeBytes("exit\n");
        os.flush();
        os.close();

        process.waitFor();
    }

    public static Process ExecuteReturnProcess(String command) throws Exception {
        Process process = Runtime.getRuntime().exec("su");
        DataOutputStream os = new DataOutputStream(process.getOutputStream());
        os.writeBytes(command + "\n");
        return process;
    }

    public static byte[] ExecuteCommand(String command, Boolean useroot) throws IOException
    {
        Process p;
                if(useroot)
                    p = Runtime.getRuntime().exec(new String[]{"su", "-c", "/system/bin/sh"});
                else
                    p = Runtime.getRuntime().exec(new String[]{"/system/bin/sh" ,"-c"});

        DataOutputStream stdin = new DataOutputStream(p.getOutputStream());
        //from here all commands are executed with su permissions
        stdin.writeBytes(command + "\n"); // \n executes the command
        InputStream stdout = p.getInputStream();
        byte[] buffer = new byte[BUFF_LEN];
        int read;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //read method will wait forever if there is nothing in the stream
        //so we need to read it in another way than while((read=stdout.read(buffer))>0)
        while(true){
            read = stdout.read(buffer);
            baos.write(buffer, 0, read);
            if(read<BUFF_LEN){
                //we have read everything
                break;
            }
        }
        return baos.toByteArray();
    }
    public static String ExecuteShellCommand(String command, Boolean useroot) throws IOException
    {
        byte[] bytes = ExecuteCommand(command, useroot);
        return new String(bytes);
    }
}
