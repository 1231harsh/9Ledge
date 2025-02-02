package com.hc.proj9ledge;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int fileId;

    private String fileName;

    private int folderId;

    private String filePath;

    public File() {
        // Default constructor
    }

    public File(int fileId, String fileName, int folderId, String filePath) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.folderId = folderId;
        this.filePath = filePath;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getFolderId() {
        return folderId;
    }

    public void setFolderId(int folderId) {
        this.folderId = folderId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return fileName;
    }
}
