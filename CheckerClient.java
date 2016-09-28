/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checkerclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

public class CheckerClient {

    public static Socket socket;
    public static DataOutputStream streamOut;
    public static DataInputStream streamIn;
    public static boolean quit = false;
    public static boolean gotMSG = false;

    public static void main(String[] args) throws Exception {
        socket = new Socket("10.0.2.15", 45322);
        streamIn = new DataInputStream(socket.getInputStream());
        streamOut = new DataOutputStream(socket.getOutputStream());
        String input;
        Scanner in = new Scanner(System.in);
        input = "hello";
        
       serverConnect();
        
       
       input = "Bob456";
       System.out.println(input);
       streamOut.write(input.getBytes());
       
       serverConnect();
       
       input = "101 Bob456 Hello there <EOM>";
       streamOut.write(input.getBytes());
       
       serverConnect();

       
       serverConnect();
    }

    public static void serverConnect() {
        while (!quit && !gotMSG) {
            quit = serverMsg();
        }
        quit = false;
        gotMSG = false;
    }

    public static String[] getMessagesFromBytes(int ct, byte[] msg) {
        String[] collection;
        String text = "";
        try {
            //append bytes to string
            for (int n = 0; n < ct; n++) {
                text += (char) msg[n];
            }
            //split by message
            collection = text.split("<EOM>");
        } catch (Exception ex) {
            System.out.println(ex);
            collection = new String[0];
        }
        return collection;
    }

    public static boolean serverMsg() {
        boolean hasMsg = false;
        try {
            byte[] msg = new byte[2000];
            //read in message 
            int ct = streamIn.read(msg);
            if (!msg.equals("")) {
                String[] collection = getMessagesFromBytes(ct, msg);
                for (int i = 0; i < collection.length; i++) {
                    System.out.println("Message: " + collection[i] + "\n");
                    hasMsg = true;
                    gotMSG = true;
                }
            } else if (gotMSG) {
                quit = true;
            }

        } catch (Exception ex) {
            hasMsg = false;
        }
        return hasMsg;
    }

}
