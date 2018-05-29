package com.novamaday.afilehost.utils;

import com.novamaday.afilehost.objects.SiteSettings;
import xyz.capybara.clamav.ClamavClient;
import xyz.capybara.clamav.commands.scan.result.ScanResult;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VirusScanner {
    private static ClamavClient client;

    public static void init() {
        client = new ClamavClient(SiteSettings.CLAM_HOST.get(), Integer.parseInt(SiteSettings.CLAM_PORT.get()));
    }

    public static List<String> scan(File file) {
        //CLAM NO SCAN IS ONLY ON IN THE DEV/DEBUG STAGE
        if (!Boolean.valueOf(SiteSettings.CLAM_NO_SCAN.get())) {
            ScanResult result = client.scan(file.toPath());
            if (result instanceof ScanResult.OK) {
                // OK - Return empty list.
                return new ArrayList<>();
            } else if (result instanceof ScanResult.VirusFound) {
                Map<String, Collection<String>> viruses = ((ScanResult.VirusFound) result).getFoundViruses();
                return viruses.entrySet().stream().flatMap(a -> a.getValue().stream()).distinct().sorted().collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }
}