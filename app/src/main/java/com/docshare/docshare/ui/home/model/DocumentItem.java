package com.docshare.docshare.ui.home.model;

public class DocumentItem {
    public final String id;
    public final String name;
    public final String extension;
    public final long fileSize;
    public final long lastOpened;
    public final String thumbnailUrl;
    public final String category;

    public DocumentItem(String id, String name, String extension, long fileSize,
                        long lastOpened, String thumbnailUrl, String category) {
        this.id = id;
        this.name = name;
        this.extension = extension;
        this.fileSize = fileSize;
        this.lastOpened = lastOpened;
        this.thumbnailUrl = thumbnailUrl;
        this.category = category;
    }
}

