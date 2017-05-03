package com.alia.pokemoshow;



public class GalleryItem {
    private String mId;
    private String mUrl;
    private String mName;
    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getName() {
        return mName;
    }

    public void setName(String caption) {
        mName = caption;
    }


    @Override
    public String toString() {
        return mName;
    }
}