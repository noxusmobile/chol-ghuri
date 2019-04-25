package com.example.cholghuri;

class Upload {

    private String uploadID;
    private String caption;
    private String uploadURI;

    public Upload() {
    }

    public Upload(String uploadURI, String caption) {

        if(caption.trim().equals("")){
            caption = "no name";
        }
        this.caption = caption;
        this.uploadURI = uploadURI;
    }

    public String getUploadID() {
        return uploadID;
    }

    public String getCaption() {
        return caption;
    }

    public String getUploadURI() {
        return uploadURI;
    }

    public void setUploadID(String uploadID) {
        this.uploadID = uploadID;
    }
}
