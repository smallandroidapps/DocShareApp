package com.docshare.docshare.data.home;

import com.docshare.docshare.ui.home.model.DocumentItem;
import com.docshare.docshare.ui.home.model.TagItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HomeRepository {

    public List<DocumentItem> getRecentDocuments() {
        List<DocumentItem> list = new ArrayList<>();
        long now = System.currentTimeMillis();
        list.add(new DocumentItem("1", "Invoice_Jan", "pdf", 240_000, now - TimeUnit.HOURS.toMillis(3), "", "Docs"));
        list.add(new DocumentItem("2", "Family_Photo", "jpg", 1_240_000, now - TimeUnit.DAYS.toMillis(1), "", "Images"));
        list.add(new DocumentItem("3", "Project_Intro", "mp4", 12_240_000, now - TimeUnit.DAYS.toMillis(2), "", "Videos"));
        list.add(new DocumentItem("4", "Resume_Ganesh", "docx", 540_000, now - TimeUnit.MINUTES.toMillis(45), "", "Docs"));
        return list;
    }

    public List<TagItem> getTags() {
        List<TagItem> tags = new ArrayList<>();
        tags.add(new TagItem("images", "Images", false));
        tags.add(new TagItem("videos", "Videos", false));
        tags.add(new TagItem("docs", "Docs", true));
        tags.add(new TagItem("pdf", "PDF", false));
        tags.add(new TagItem("favorites", "Favorites", false));
        tags.add(new TagItem("recent", "Recently Added", false));
        return tags;
    }

    public List<DocumentItem> getRecommendedDocuments() {
        List<DocumentItem> items = new ArrayList<>();
        long base = System.currentTimeMillis();
        for (int i = 0; i < 8; i++) {
            String id = String.valueOf(100 + i);
            String category = (i % 2 == 0) ? "Docs" : (i % 3 == 0 ? "Images" : "Videos");
            String ext = category.equals("Docs") ? (i % 4 == 0 ? "pdf" : "docx") : (category.equals("Images") ? "jpg" : "mp4");
            items.add(new DocumentItem(id, "Sample_" + i, ext, 250_000 + i * 1000L, base - i * TimeUnit.HOURS.toMillis(5), "", category));
        }
        return items;
    }

    public List<DocumentItem> filterByTag(List<DocumentItem> source, String tag) {
        if (tag == null || tag.isEmpty()) return source;
        List<DocumentItem> out = new ArrayList<>();
        for (DocumentItem d : source) {
            boolean match = false;
            switch (tag) {
                case "Images": match = d.extension.equalsIgnoreCase("jpg") || d.category.equalsIgnoreCase("Images"); break;
                case "Videos": match = d.extension.equalsIgnoreCase("mp4") || d.category.equalsIgnoreCase("Videos"); break;
                case "Docs": match = d.extension.equalsIgnoreCase("docx") || d.category.equalsIgnoreCase("Docs"); break;
                case "PDF": match = d.extension.equalsIgnoreCase("pdf"); break;
                case "Recently Added": match = System.currentTimeMillis() - d.lastOpened < TimeUnit.DAYS.toMillis(3); break;
                case "Favorites": match = d.name.toLowerCase().contains("resume") || d.name.toLowerCase().contains("invoice"); break;
            }
            if (match) out.add(d);
        }
        return out;
    }
}

