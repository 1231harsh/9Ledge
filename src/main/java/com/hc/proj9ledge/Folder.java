package com.hc.proj9ledge;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Folder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int folderId;

    private String folderName;

    private int parentFolderId;

    public Folder() {
    }

    public Folder(String folderName, int parentFolderId) {
        this.folderName = folderName;
        this.parentFolderId = parentFolderId;
    }

    public int getFolderId() {
        return folderId;
    }

    public void setFolderId(int folderId) {
        this.folderId = folderId;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public int getParentFolderId() {
        return parentFolderId;
    }

    public void setParentFolderId(int parentFolderId) {
        this.parentFolderId = parentFolderId;
    }
    @Override
    public String toString() {
        return folderName;
    }
}