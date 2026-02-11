package com.alp;

import com.alp.entities.LocalidadesJPA;
import com.alp.entities.ProvinciaJPA;
import com.alp.entities.ViviendaJPA;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        //getProvincias();
        //updateProvincia();
        //altaLocalidad();
        //modificarViviendaMerge();
        solucionarMergeYBorrar();



    }

    public static void solucionarMergeYBorrar() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("UnidadPersistencia");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        java.util.UUID idVivienda = java.util.UUID.fromString("083f07e9-74c5-4eb2-b8c1-6b73ff5c42b3");

        //al cargar con find, tenemos todos los datos, ya no dara el error por los campos en null
        ViviendaJPA viviendaBD = em.find(ViviendaJPA.class, idVivienda);

        if (viviendaBD != null) {
            System.out.println("Vivienda encontrada: " + viviendaBD.getId());
            System.out.println("Habitaciones antes: " + viviendaBD.getHabitaciones());

            //modificacion
            viviendaBD.setHabitaciones(2);

            System.out.println("Habitaciones cambiadas en memoria a: " + viviendaBD.getHabitaciones());

            //hacer el delete
            System.out.println("Borrando la vivienda...");
            em.remove(viviendaBD);
        } else {
            System.out.println("No se encontró la vivienda con ese ID.");
        }
        em.getTransaction().commit();
        em.close();
        System.out.println("Operación finalizada.");
    }
    public static void modificarViviendaMerge() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("UnidadPersistencia");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        ViviendaJPA viviendaFormulario = new ViviendaJPA();
        viviendaFormulario.setId(java.util.UUID.fromString("083f07e9-74c5-4eb2-b8c1-6b73ff5c42b3"));
        viviendaFormulario.setHabitaciones(2); // 2 habit.

        try {
            em.merge(viviendaFormulario);
            em.getTransaction().commit();
            System.out.println("Merge ejecutado.");
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
    public static void altaLocalidad() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("UnidadPersistencia");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        java.util.UUID idAlicante = java.util.UUID.fromString("95ccb118-c924-4c5f-8961-3fca7c124123");
        ProvinciaJPA provincia = em.getReference(ProvinciaJPA.class, idAlicante);

        LocalidadesJPA nuevaLocalidad = new LocalidadesJPA();
        nuevaLocalidad.setId(java.util.UUID.randomUUID()); //id nuevo
        nuevaLocalidad.setNombre("Almoradí");
        nuevaLocalidad.setProvincia(provincia);

        em.persist(nuevaLocalidad);

        em.getTransaction().commit();
        em.close();

        System.out.println("Localidad de Almoradí creada sin cargar la provincia.");
    }
    public static void getProvincias() {
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("UnidadPersistencia");
        EntityManager em = emf.createEntityManager();
        //em.getTransaction().begin();
        String jpql = "SELECT p FROM ProvinciaJPA p ";
        List<ProvinciaJPA> propiedades = em.createQuery(jpql,
                ProvinciaJPA.class).getResultList();
        propiedades.forEach(provinciaJPA -> {
            System.out.println(provinciaJPA.getNombre());
            System.out.println("Localidades:");
            provinciaJPA.getLocalidades().forEach(localidadesJPA -> {
                System.out.println("\tLocalidad: " + localidadesJPA.getNombre());
            });
        });
        em.close();
    }
    public static void updateProvincia() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("UnidadPersistencia");
        EntityManager em = emf.createEntityManager();
        jakarta.persistence.EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            java.util.UUID idAlicante = java.util.UUID.fromString("95ccb118-c924-4c5f-8961-3fca7c124123");
            ProvinciaJPA alicante = em.find(ProvinciaJPA.class, idAlicante);

            if (alicante != null) {
                System.out.println("Nombre original: " + alicante.getNombre());

                alicante.setNombre("Alicante Modificado FINAL");

                //si producimos un error aqui saltaria al rollback
                // ejemplo dividir entre 0 o algo parecido

                System.out.println("Nombre cambiado en memoria: " + alicante.getNombre());
            }

            //commit si funciona correctamente
            tx.commit();
            System.out.println("Transacción completada (COMMIT).");

        } catch (Exception e) {
            //rollback en caso de error
            if (tx.isActive()) {
                tx.rollback();
            }
            System.out.println("Hubo un error, se deshicieron los cambios (ROLLBACK).");
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}