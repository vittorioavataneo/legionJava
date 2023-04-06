package org.generation.italy.legion.model.data.implementations;

import org.generation.italy.legion.model.data.abstractions.CourseRepository;
import org.generation.italy.legion.model.data.exceptions.DataException;
import org.generation.italy.legion.model.data.exceptions.EntityNotFoundException;
import org.generation.italy.legion.model.entities.Course;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

import static org.generation.italy.legion.model.data.Constants.CSV_COURSE;
import static org.generation.italy.legion.model.data.Constants.ENTITY_NOT_FOUND;


/*
@Repository
@Profile("csv")*/
public class CSVFileCourseRepository implements CourseRepository {
    private String fileName;
    public static long nextId;
    public static final String DEFAULT_FILE_NAME = "Corsi.csv";

    public CSVFileCourseRepository() {
        this.fileName = DEFAULT_FILE_NAME;
    }
    public CSVFileCourseRepository(String fileName) {
        this.fileName = fileName;
    }


    @Override
    public List<Course> findAll() {
         try {
            List<String> lines = Files.readAllLines(Paths.get(fileName));
            List<Course> courses = new ArrayList<>();
            for(String s : lines){
                courses.add(CSVToCourse(s));
            }
            return courses;
        }catch (IOException e){
             try {
                 throw new DataException("Errore nella lettura del file", e);
             } catch (DataException ex) {
                 throw new RuntimeException(ex);
             }
         }
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


    @Override
    public List<Course> findByTitleContains(String part) throws DataException {
        try {
            List<String> lines = Files.readAllLines(Paths.get(fileName));
            List<Course> courses = new ArrayList<>();
            for(String s : lines){
                String[] tokens = s.split(",");
                if(tokens[1].contains(part)){
                    Course found = CSVToCourse(s);
                    courses.add(found);
                }
            }
            return courses;
        }catch (IOException e){
            throw new DataException("Errore nella lettura del file", e);
        }
    }


    @Override
    public int countActiveCourses() {
        return 0;
    }

    @Override
    public void deactivateOldest(int n) {

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

    public String courseToCSV(Course c){                //trasforma i dati presenti dell'oggetto in una stringa(che poi scriveremo sul file)
        return String.format(Locale.US,CSV_COURSE,c.getId(),c.getTitle()
                ,c.getDescription(),c.getProgram(),c.getDuration(),c.isActive(),c.getCreatedAt());
    }

    private Course CSVToCourse(String CSVLine){
        String[] tokens = CSVLine.split(",");
        return new Course(Long.parseLong(tokens[0]), tokens[1], tokens[2],
                tokens[3], Double.parseDouble(tokens[4]),Boolean.parseBoolean(tokens[5]) ,LocalDate.parse(tokens[6]));
    }
    private void flushStringsToFile(List<String> lines) throws FileNotFoundException {
        try(PrintWriter pw = new PrintWriter(new FileOutputStream(fileName))) {
            for (String st : lines) {
                pw.println(st);
            }
        }
    }
}
