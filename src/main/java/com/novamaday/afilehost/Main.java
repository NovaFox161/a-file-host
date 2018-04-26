package com.novamaday.afilehost;

import com.novamaday.afilehost.account.AccountHandler;
import com.novamaday.afilehost.database.DatabaseManager;
import com.novamaday.afilehost.logger.Logger;
import com.novamaday.afilehost.objects.SiteSettings;
import com.novamaday.afilehost.utils.EmailHandler;
import com.novamaday.afilehost.utils.SparkUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String args[]) throws IOException {
        //Load settings
        Properties p = new Properties();
        p.load(new FileReader(new File("settings.properties")));
        SiteSettings.init(p);

        Logger.getLogger().init();

        //Init database
        DatabaseManager.getManager().connectToMySQL();
        DatabaseManager.getManager().createTables();

        //Init spark
        AccountHandler.getHandler().init();
        SparkUtils.initSpark();

        //Init the rest of our services
        EmailHandler.getHandler().init();
    }
}