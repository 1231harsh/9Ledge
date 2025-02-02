package com.hc.proj9ledge;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import java.util.List;

public class FolderTreeViewService {

    public static void populateFolderTreeView(TreeView<Object> treeView) {
        Folder rootFolder = FolderDAO.getRootFolder();
        if (rootFolder == null) {
            System.out.println("No root folder found!");
            rootFolder = new Folder("Files", -1);
            FolderDAO.addFolder(rootFolder);
        }

        TreeItem<Object> rootItem = new TreeItem<>(rootFolder);
        rootItem.setExpanded(true);

        loadSubfoldersAndFiles(rootItem, rootFolder);

        treeView.setRoot(rootItem);
    }

    private static void loadSubfoldersAndFiles(TreeItem<Object> parentItem, Folder parentFolder) {
        List<Folder> subfolders = FolderDAO.getSubfolders(parentFolder.getFolderId());
        for (Folder subfolder : subfolders) {
            parentItem.getChildren().add(createFolderItem(subfolder));
        }

        List<com.hc.proj9ledge.File> files = FileDAO.getFilesByFolderId(parentFolder.getFolderId());
        for (com.hc.proj9ledge.File file : files) {
            parentItem.getChildren().add(new TreeItem<>(file));
        }
    }

    private static TreeItem<Object> createFolderItem(Folder folder) {
        System.out.println("Creating folder item: " + folder.getFolderName());

        TreeItem<Object> folderItem = new TreeItem<>(folder);

        folderItem.getChildren().add(new TreeItem<>(new Folder("Loading..." , -1)));

        folderItem.addEventHandler(TreeItem.branchExpandedEvent(), event -> {
            if (!folderItem.isLeaf() && folderItem.getChildren().size() == 1 &&
                    "Loading...".equals(((Folder) folderItem.getChildren().get(0).getValue()).getFolderName())) {

                folderItem.getChildren().clear();
                loadSubfoldersAndFiles(folderItem, folder);
            }
        });

        return folderItem;
    }
}
