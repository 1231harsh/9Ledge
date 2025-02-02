package com.hc.proj9ledge;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class FileDAO {
    private static SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public static void addFile(File file) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            session.persist(file);

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<File> searchFiles(String searchTerm) {
        List<File> files = new ArrayList<>();

        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM File WHERE fileName LIKE :searchTerm";

            Query<File> query = session.createQuery(hql, File.class);
            query.setParameter("searchTerm", "%" + searchTerm + "%");

            files = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return files;
    }

    public static List<File> getFilesByFolderId(int folderId) {
        List<File> files = new ArrayList<>();

        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM File WHERE folderId = :folderId";

            Query<File> query = session.createQuery(hql, File.class);
            query.setParameter("folderId", folderId);

            files = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return files;
    }

}
