package com.hc.proj9ledge;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.util.Stack;


public class FileService {
    public static void createFile(com.hc.proj9ledge.File file) {

        Folder folder = FolderDAO.getFolderById(file.getFolderId());

        if (folder == null) {
            System.out.println("Folder not found!");
            return;
        }

        System.out.println("Folder: " + folder.getFolderName());

        try {
            String rootPath = "D:\\COding\\My Projects\\9ledge\\Files";

            Stack<String> folderNames = new Stack<>();

            StringBuilder fullPath = new StringBuilder(rootPath);

            while (folder != null && !folder.getFolderName().equals("Files")) {
                folderNames.push(folder.getFolderName());
                folder = FolderDAO.getFolderById(folder.getParentFolderId());
            }

            while (!folderNames.isEmpty()) {
                fullPath.append(File.separator).append(folderNames.pop());
            }

            File folderDir = new File(fullPath.toString());

            if (!folderDir.exists()) {
                boolean created = folderDir.mkdirs();
                if (created) {
                    System.out.println("Folder created: " + folderDir.getAbsolutePath());
                } else {
                    System.out.println("Failed to create folder: " + folderDir.getAbsolutePath());
                }
            }

            String filePath = fullPath.toString() + File.separator + file.getFileName() + ".docx";

            file.setFilePath(filePath);

            FileDAO.addFile(file);

            XWPFDocument doc = new XWPFDocument();
            XWPFParagraph paragraph = doc.createParagraph();
            paragraph.createRun().setText("This is a placeholder text in the Word file.");

            try (FileOutputStream out = new FileOutputStream(filePath)) {
                doc.write(out);
            }

            System.out.println("Word file created: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
