package com.hc.proj9ledge;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class FolderDAO {
    public static void addFolder(Folder folder) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(folder);
            transaction.commit();
        }
    }

    public static int getParentId() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Integer> query = session.createQuery(
                    "SELECT f.folderId FROM Folder f WHERE f.parentFolderId = -1", Integer.class);

            Integer parentId = query.uniqueResult();
            return parentId;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static Folder getRootFolder() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Folder> query = session.createQuery("from Folder where parentFolderId = -1", Folder.class);
            return query.uniqueResult();
        }
    }

    public static List<Folder> getSubfolders(int parentFolderId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Folder> query = session.createQuery("from Folder where parentFolderId = :parentFolderId", Folder.class);
            query.setParameter("parentFolderId", parentFolderId);
            return query.list();
        }
    }

    public static Folder getFolderById(int folderId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Folder.class, folderId);
        }
    }

    public static List<Folder> getAllFolders() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Folder> query = session.createQuery("from Folder", Folder.class);
            return query.list();
        }
    }

    public static List<Folder> searchFolders(String searchTerm) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Folder> query = session.createQuery("from Folder where folderName like :term", Folder.class);
            query.setParameter("term", "%" + searchTerm + "%");
            return query.list();
        }
    }

}
