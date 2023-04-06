package org.generation.italy.legion.model.data.implementations;

import org.generation.italy.legion.model.data.abstractions.CourseRepository;
import org.generation.italy.legion.model.data.exceptions.DataException;
import org.generation.italy.legion.model.data.exceptions.EntityNotFoundException;
import org.generation.italy.legion.model.entities.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.generation.italy.legion.model.data.JDBCConstants.*;


/*@Repository
@Profile("jdbc")*/
public class JDBCCourseRepository implements CourseRepository {
    /*public static int askToClient;
    static{
        System.out.println("inizializzazione statica");
        Driver d=new Driver();
        try {
            DriverManager.registerDriver(d);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }*/

    private Connection con;
    @Autowired
    public JDBCCourseRepository(Connection connection) {
        this.con = connection;
    }

    @Override
    public List<Course> findByTitleContains(String part) throws DataException {
        try (
             PreparedStatement st = con.prepareStatement(FIND_BY_TITLE_CONTAINS);//factory method pattern
        ) {
            st.setString(1, "%" + part + "%");
            try (ResultSet rs = st.executeQuery()) {
                List<Course> courseList = new ArrayList<>();
                while (rs.next()) {
                    courseList.add(databaseToCourse(rs));
                }
                return courseList;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataException("errore nella lettura dei corsi da database", e);
        }
    }

    public Course create2(Course course) throws DataException {
        try (
             PreparedStatement st = con.prepareStatement(INSERT_COURSE_RETURNING_ID,Statement.RETURN_GENERATED_KEYS);//factory method pattern
        ) {
            st.setString(1, course.getTitle());
            st.setString(2, course.getDescription());
            st.setString(3, course.getProgram());
            st.setDouble(4, course.getDuration());
            st.setBoolean(5, course.isActive());
            st.setDate(6, Date.valueOf(course.getCreatedAt()));
            st.executeUpdate();
            try (ResultSet keys = st.getGeneratedKeys()){
                keys.next();
                long key = keys.getLong(1);
                course.setId(key);
                return course;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataException("errore nell'insermiento del corso", e);
        }

    }
    

    @Override
    public int countActiveCourses() throws DataException{
        try (
             Statement st = con.createStatement();//factory method pattern
             ResultSet rs = st.executeQuery(ACTIVE_COURSES)){
                 rs.next();
                 return rs.getInt(1);

        }catch (SQLException e) {
            e.printStackTrace();
            throw new DataException("errore nella lettura dei corsi da database", e);
        }

    }

    @Override
    public void deactivateOldest(int n) throws DataException {
        try(
            PreparedStatement st = con.prepareStatement(DEACTIVATE_OLDEST_N_COURSES)) {
            st.setInt(1, n);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataException("Errore nella lettura dei corsi da database", e);
        }
    }

    @Override
    public boolean adjustActiveCourses(int NumActive) throws DataException {
        return false;
    }

    @Override
    public Iterable<Course> findByTitleActiveAndMinEditions(String part, boolean active, int minEditions) {
        return null;
    }

    @Override
    public Iterable<Course> findByTitleAndActive(String part, boolean active) {
        return null;
    }


    private Course databaseToCourse(ResultSet rs) throws SQLException {
        try {
            return new Course(rs.getLong("id_course"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getString("program"),
                    rs.getDouble("duration"),
                    rs.getBoolean("is_active"),
                    rs.getDate("created_at").toLocalDate());
        } catch (SQLException e) {
            throw new SQLException("errore nella lettura dei corsi da database", e);
        }
    }

    @Override
    public List<Course> findAll() throws DataException {
        return null;
    }

    @Override
    public Optional<Course> findById(long id) throws DataException {
        return Optional.empty();
    }

    @Override
    public Course create(Course entity) throws DataException {
        return null;
    }

    @Override
    public void update(Course entity) throws EntityNotFoundException, DataException {

    }

    @Override
    public void deleteById(long id) throws EntityNotFoundException, DataException {

    }
}
