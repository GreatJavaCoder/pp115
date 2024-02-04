package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        Session session = Util.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        String sql = " CREATE TABLE IF NOT EXISTS users " +
                "(id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(50) NOT NULL, lastName VARCHAR(50) NOT NULL, " +
                "age TINYINT NOT NULL); ";

        Query query = session.createNativeQuery(sql, User.class);
        try {
            query.executeUpdate();
        } catch (JDBCException e) {
            e.printStackTrace();
        }
        transaction.commit();
        session.close();
    }

    @Override
    public void dropUsersTable() {
        Session session = Util.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        String sql = " DROP TABLE IF EXISTS users; ";

        Query query = session.createNativeQuery(sql, User.class);
        try {
            query.executeUpdate();
        } catch (JDBCException e) {
            e.printStackTrace();
        }
        query.executeUpdate();
        transaction.commit();
        session.close();
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        User user = new User(name, lastName, age);
        Session session = Util.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.save(user);
        transaction.commit();
        session.close();
        System.out.println(user.toString() + " добавлен в базу данных");
    }

    @Override
    public void removeUserById(long id) {
        User user = new User();
        Session session = Util.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        user = (User) session.load(User.class, id);
        session.delete(user);
        transaction.commit();
        session.close();
    }

    @Override
    public List<User> getAllUsers() {
        String hql = "from User";
        Session session = Util.getSessionFactory().openSession();
        Query query = session.createQuery(hql);
        List<User> userList = query.list();
        session.close();
        return userList;
    }

    @Override
    public void cleanUsersTable() {
        Session session = Util.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        String sql = " DELETE from users WHERE id > 0; ";

        Query query = session.createNativeQuery(sql, User.class);
        try {
            query.executeUpdate();
        } catch (JDBCException e) {
            e.printStackTrace();
        }
        transaction.commit();
        session.close();
    }
}