package com.novamaday.afilehost.api.v1;

import com.novamaday.afilehost.crypto.KeyGenerator;
import com.novamaday.afilehost.logger.Logger;
import com.novamaday.afilehost.objects.SiteSettings;
import com.novamaday.afilehost.utils.VirusScanner;
import org.json.JSONObject;
import spark.Request;
import spark.Response;
import spark.utils.IOUtils;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class UploadEndpoint {
    //TODO: Add check for file size handling
    public static String simpleUpload(Request request, Response response) {
        try {
            Part filePart = request.raw().getPart("file");

            //Get all the shit we need
            String contentType = filePart.getContentType();
            long mbSize = filePart.getSize() / (1024 * 1024);

            //TODO: Handle size + file type!!


            String hash = KeyGenerator.generateFileHash();

            //Save file to tmp for scanning....
            request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement(SiteSettings.TMP_FOLDER.get()));

            InputStream inputStream = filePart.getInputStream();

            OutputStream outputStream = new FileOutputStream(SiteSettings.TMP_FOLDER.get() + hash + ".tmp");
            IOUtils.copy(inputStream, outputStream);
            outputStream.close();

            File tmpFile = new File(SiteSettings.TMP_FOLDER.get() + hash + ".tmp");

            //Scan viruses...
            List<String> viruses = VirusScanner.scan(tmpFile);

            if (viruses.size() > 0) {
                //Found viruses... delete file and handle.
                tmpFile.delete();

                //Log
                Logger.getLogger().api("[VIRUS] " + viruses.toString(), request.ip(), request.host(), request.pathInfo());

                //Send detailed response...
                JSONObject body = new JSONObject();
                body.put("Message", "Viruses Found");
                body.put("reason", "Viruses Found. Upload not allowed.");
                body.put("Viruses", viruses);
                response.status(406);
            }

            //Okay, move to upload dir and handle further stuffs...
            Files.move(tmpFile.toPath(), new File(SiteSettings.UPLOAD_FOLDER.get() + hash).toPath(), StandardCopyOption.ATOMIC_MOVE);

            //TODO: Add to database...

            //TODO: Send info to client so they can redirect to page...


        } catch (IOException | ServletException e) {
            Logger.getLogger().exception("Failed to handle simple upload", e, UploadEndpoint.class);
            e.printStackTrace();

            JSONObject body = new JSONObject();
            body.put("Message", "Failed to handle simple upload");
            body.put("reason", "Internal Error. Please try again in a little while.");
            response.status(500);
            response.body(body.toString());
        }

        return response.body();
    }
}