package org.generation.italy.legion.model.data.implementations;

import org.generation.italy.legion.model.data.abstractions.CourseRepository;
import org.generation.italy.legion.model.data.exceptions.DataException;
import org.generation.italy.legion.model.data.exceptions.EntityNotFoundException;
import org.generation.italy.legion.model.entities.Course;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;


/*@Repository
@Profile("memo")*/
public class InMemoryCourseRepository implements CourseRepository {
    /*
        pensalo come una arrayList(NON fanno parte della stassa famiglia) ma le posizioni vengono definite con degli id UNIVOCI
        immaginalo come 2 colonne a sinistra l'id UNIVOCO della riga e a destra un oggetto
     */
    private static Map<Long, Course> dataSource = new HashMap<>();
    private static long nextId;

    /*
        Optional lo vedo un pò come una variabile jolly in che senso:
        Viene utitlizzata soprattutto (o forse unicamente[questo ce lo dirà il tempo]) per gestire variabili/isatnze/puntaori che posso essere null, come?
        Optional non può essere null, optional sarà EMPTY se gli passiamo un valore null e PRESENT se gli passiamo qualcosa che non sia null
        Come vedi si dichiarano con le generics --> <>
        all'interno ci mettiamo il tipo di dato che devono ricevere (lo sai insomma)
        MA COSA FA?
        dice al programmatore che bisogna fare un controllo (nomeOptional.isEmpty) se non lo fa è un coglione!
        si! hai capito!! serve "solo" per ricordarci/ o a dire di controllare se un dato è vuoto(null) o meno, così da evitare cappellate logiche durante la scrittura dei codici
     */



    @Override
    public List<Course> findAll() {
        return new ArrayList<>(dataSource.values());
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
    public List<Course> findByTitleContains(String part) {
        List<Course> result = new ArrayList<>();            //un pò di polimorfismo non fa mai male
        Collection<Course> cs = dataSource.values();        //rappresenta una collezione di oggetti non ordinati(messi alla cazzo di cane) si ci possiamo ciclare sopra, guarda il for
        for (Course c : cs) {
            if (c.getTitle().contains(part)) {
                result.add(c);                              //aggiungiamo l'oggetto che abbiamo trovato nella collection alla lista
            }
        }
        return result;
    }



    @Override
    public int countActiveCourses() {
        int activeCourses = 0;
        Collection<Course> collection = dataSource.values();
        for (Course c : collection) {                          //scorro la collection per vedere quanti corsi attivi ci sono
            if (c.isActive()) {
                activeCourses++;
            }
        }
        return activeCourses;
    }

    @Override
    public boolean adjustActiveCourses(int nCoursesToDelete) throws DataException {
        int activeCourses = countActiveCourses();
        ArrayList<Course> arrayList = new ArrayList<>(dataSource.values());
        if (activeCourses <= nCoursesToDelete) {
            return false;
        } else {
            try{
                for (int i = 0; i < nCoursesToDelete; i++) {
                    LocalDate toDelete = LocalDate.now();
                    for (ListIterator<Course> it = arrayList.listIterator(); it.hasNext();) {
                        Course c1 = it.next();
                        int f = it.nextIndex();
                        if(f > arrayList.size() - 1){
                            break;
                        }
                        Course c2 = arrayList.get(f);
                        if (c1.getCreatedAt().isAfter(c2.getCreatedAt()) && c2.getCreatedAt().isBefore(toDelete)) {
                            toDelete = c2.getCreatedAt();
                        } else if (c1.getCreatedAt().isBefore(c2.getCreatedAt()) && c1.getCreatedAt().isBefore(toDelete)) {
                            toDelete = c1.getCreatedAt();
                        }
                    }
                    for (Course c : arrayList) {                              //la collections contiene quelli che devono rimanere nella mappa
                        if (c.getCreatedAt() == toDelete) {
                            c.setActive(false);
                            arrayList.remove(c);
                            break;
                        }
                    }
                }
            }catch (NoSuchElementException e){
                throw new DataException("Elementi nella lista inferiori a quelli da controllare", e);
            }
            return true;
        }
    }

    @Override
    public Iterable<Course> findByTitleActiveAndMinEditions(String part, boolean active, int minEditions) {
        return null;
    }

    @Override
    public Iterable<Course> findByTitleAndActive(String part, boolean active) {
        return null;
    }


    @Override
    public void deactivateOldest(int n) {
        dataSource.values().stream()
                .filter(Course::isActive)
                .sorted(Comparator.comparing(Course::getCreatedAt))
                .limit(n)
                .forEach(Course::deactivate);
    }
}
