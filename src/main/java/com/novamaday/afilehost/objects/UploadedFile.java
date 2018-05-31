package com.novamaday.afilehost.objects;

public class UploadedFile {
    private final String hash;
    private final String url;
    private final long size;

    public UploadedFile(String _hash, String _url, long _size) {
        hash = _hash;
        url = _url;
        size = _size;
    }

    public String getHash() {
        return hash;
    }

    public String getUrl() {
        return url;
    }

    public long getSize() {
        return size;
    }
}