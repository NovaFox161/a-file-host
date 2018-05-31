package com.novamaday.afilehost.logger;

import com.novamaday.afilehost.objects.SiteSettings;

import javax.annotation.Nullable;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Logger {
    private static Logger instance;
    private String exceptionsFile;
    private String apiFile;
    private String debugFile;

    private final String lineBreak = System.lineSeparator();

    private Logger() {
    } //Prevent initialization

    public static Logger getLogger() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void init() {
        //noinspection ResultOfMethodCallIgnored
        new File(SiteSettings.LOG_FOLDER.get()).mkdir();

        //Create files...
        String timestamp = new SimpleDateFormat("dd-MM-yyyy-hh.mm.ss").format(System.currentTimeMillis());

        exceptionsFile = SiteSettings.LOG_FOLDER.get() + "/" + timestamp + "-exceptions.log";
        apiFile = SiteSettings.LOG_FOLDER.get() + "/" + timestamp + "-api.log";
        debugFile = SiteSettings.LOG_FOLDER.get() + "/" + timestamp + "-debug.log";

        try {
            PrintWriter exceptions = new PrintWriter(exceptionsFile, "UTF-8");
            exceptions.println("INIT --- " + timestamp + " ---");
            exceptions.close();

            PrintWriter api = new PrintWriter(apiFile, "UTF-8");
            api.println("INIT --- " + timestamp + " ---");
            api.close();

            PrintWriter debug = new PrintWriter(debugFile, "UTF-8");
            debug.println("INIT --- " + timestamp + " ---");
            debug.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exception(@Nullable String message, Exception e, Class clazz) {
        String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String error = sw.toString(); // stack trace as a string
        pw.close();
        try {
            sw.close();
        } catch (IOException e1) {
            //Can ignore silently...
        }

        try {
            FileWriter exceptions = new FileWriter(exceptionsFile, true);
            exceptions.write("ERROR --- " + timeStamp + " ---" + lineBreak);
            if (message != null) {
                exceptions.write("message: " + message + lineBreak);
            }
            exceptions.write("class: " + clazz.getName() + lineBreak);
            exceptions.write(error + lineBreak);
            exceptions.close();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public void debug(String message, @Nullable String info, Class clazz) {
        String timeStamp = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss").format(Calendar.getInstance().getTime());

        try {
            FileWriter file = new FileWriter(debugFile, true);
            file.write("DEBUG --- " + timeStamp + " ---" + lineBreak);
            if (message != null) {
                file.write("message: " + message + lineBreak);
            }
            if (info != null) {
                file.write("info: " + info + lineBreak);
            }
            file.write("class: " + clazz.getName() + lineBreak);
            file.close();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public void debug(String message) {
        String timeStamp = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss").format(Calendar.getInstance().getTime());

        try {
            FileWriter file = new FileWriter(debugFile, true);
            file.write("DEBUG --- " + timeStamp + " ---" + lineBreak);
            if (message != null) {
                file.write("info: " + message + lineBreak);
            }
            file.close();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public void api(String message) {
        String timeStamp = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss").format(Calendar.getInstance().getTime());

        try {
            FileWriter file = new FileWriter(apiFile, true);
            file.write("API --- " + timeStamp + " ---" + lineBreak);
            file.write("info: " + message + lineBreak);
            file.close();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public void api(String message, String ip) {
        String timeStamp = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss").format(Calendar.getInstance().getTime());

        try {
            FileWriter file = new FileWriter(apiFile, true);
            file.write("API --- " + timeStamp + " ---" + lineBreak);
            file.write("info: " + message + lineBreak);
            file.write("IP: " + ip + lineBreak);
            file.close();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public void api(String message, String ip, String host, String endpoint) {
        String timeStamp = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss").format(Calendar.getInstance().getTime());

        try {
            FileWriter file = new FileWriter(apiFile, true);
            file.write("API --- " + timeStamp + " ---" + lineBreak);
            file.write("info: " + message + lineBreak);
            file.write("IP: " + ip + lineBreak);
            file.write("Host: " + host + lineBreak);
            file.write("Endpoint: " + endpoint + lineBreak);
            file.close();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}