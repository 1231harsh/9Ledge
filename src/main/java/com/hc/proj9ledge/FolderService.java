package com.hc.proj9ledge;

import java.io.File;
import java.util.Stack;

public class FolderService {
    private static final String ROOT_DIRECTORY = "D:\\COding\\My Projects\\9ledge";

    public static void createFolder(Folder folder) {
        FolderDAO.addFolder(folder);

        String parentFolderPath = ROOT_DIRECTORY;

        if (folder.getParentFolderId() != 5) {
            Folder parentFolder = FolderDAO.getFolderById(folder.getParentFolderId());
            if (parentFolder != null) {
                parentFolderPath = buildFolderPath(parentFolder);
                System.out.println("Parent folder path: " + parentFolderPath);
            }
        }

        File folderDir = new File(parentFolderPath + File.separator + folder.getFolderName());
        System.out.println(parentFolderPath + File.separator + folder.getFolderName());
        if (!folderDir.exists()) {
            if (folderDir.mkdirs()) {
                System.out.println("Folder created: " + folder.getFolderName());
            } else {
                System.out.println("Failed to create folder.");
            }
        }
    }

    private static String buildFolderPath(Folder parentFolder) {
        Stack<String> pathStack = new Stack<>();
        while (parentFolder != null) {
            pathStack.push(parentFolder.getFolderName());
            parentFolder = FolderDAO.getFolderById(parentFolder.getParentFolderId());
        }

        StringBuilder fullPath = new StringBuilder(ROOT_DIRECTORY);
        while (!pathStack.isEmpty()) {
            fullPath.append(File.separator).append(pathStack.pop());
        }
        System.out.println("Full path: " + fullPath.toString());

        return fullPath.toString();
    }
}
