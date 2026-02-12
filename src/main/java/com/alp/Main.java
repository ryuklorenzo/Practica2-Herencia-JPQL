package com.alp;

import com.alp.entities.*;
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
        //solucionarMergeYBorrar();
        //demoProblemaNMas1();
        //solucionJoinFetch();
        //solucionEntityGraph();
        //consultarPropiedadesPolimorfismo();
        ejecutarConsultasNombradas();

    }

    public static void ejecutarConsultasNombradas() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("UnidadPersistencia");
        EntityManager em = emf.createEntityManager();
        System.out.println("--- EJECUTANDO NAMED QUERIES ---");
        List<PropiedadesJPA> resultados = em.createNamedQuery("Propiedad.completaPorEstado", PropiedadesJPA.class)
                .setParameter("estado", EstadoPropiedad.EN_VENTA) // Pasa el ENUM directamente
                .getResultList();
        for (PropiedadesJPA p : resultados) {
            System.out.println("Propiedad: " + p.getReferencia() +
                    " | Ubicación: " + p.getLocalidad().getNombre() +
                    " (" + p.getLocalidad().getProvincia().getNombre() + ")" +
                    " | Fotos: " + p.getMultimedia().size());
        }
        Double precioMedio = em.createNamedQuery("Propiedad.precioMedio", Double.class)
                .getSingleResult();
        System.out.println("El precio medio global es: " + precioMedio + " €");
        em.close();
    }

    public static void consultarPropiedadesPolimorfismo() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("UnidadPersistencia");
        EntityManager em = emf.createEntityManager();
        System.out.println("--- CONSULTA POLIMÓRFICA (Ejercicio 5.3) ---");
        String jpql = "SELECT p FROM PropiedadesJPA p";
        java.util.List<com.alp.entities.PropiedadesJPA> lista = em.createQuery(jpql, com.alp.entities.PropiedadesJPA.class).getResultList();

        for (com.alp.entities.PropiedadesJPA p : lista) {
            System.out.print("Ref: " + p.getReferencia() +
                    " | Precio: " + p.getPrecio() + " €" +
                    " | m2: " + p.getMetrosCuadrados());
            if (p instanceof com.alp.entities.ViviendaJPA v) {
                System.out.println(" -> [VIVIENDA] " +
                        "Habitables: " + v.getMetrosHabitables() + "m2, " +
                        "Habs: " + v.getHabitaciones() + ", " +
                        "Aseos: " + v.getAseos() + ", " +
                        "Aire: " + (v.getAireAcondicionado() ? "Sí" : "No"));
            }
            else if (p instanceof com.alp.entities.TerrenoJPA t) {
                System.out.println(" -> [TERRENO] " +
                        "Edificabilidad: " + t.getEdificabilidad() + ", " +
                        "Urbanizable: " + (t.getUrbanizable() ? "Sí" : "No"));
            }
            else if (p instanceof com.alp.entities.LocalesJPA l) {
                System.out.println(" -> [LOCAL] " +
                        "Escaparate: " + (l.getEscaparate() ? "Sí" : "No") + ", " +
                        "Salida Humos: " + (l.getSalidaHumos() ? "Sí" : "No"));
            }
            else {
                System.out.println("PROPIEDAD GENÉRICA");
            }
        }
        em.close();
    }
    public static void solucionEntityGraph() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("UnidadPersistencia");
        EntityManager em = emf.createEntityManager();
        System.out.println("--- SOLUCIÓN CON ENTITY GRAPH ---");
        jakarta.persistence.EntityGraph<ProvinciaJPA> graph = em.createEntityGraph(ProvinciaJPA.class);

        jakarta.persistence.Subgraph<com.alp.entities.LocalidadesJPA> localidadesGraph =
                graph.addSubgraph("localidades");
        jakarta.persistence.Subgraph<com.alp.entities.PropiedadesJPA> propiedadesGraph =
                localidadesGraph.addSubgraph("propiedades");

        //.addAttributeNodes porque ya no necesitamos mas
        propiedadesGraph.addAttributeNodes("multimedia");
        List<ProvinciaJPA> provincias = em.createQuery("SELECT p FROM ProvinciaJPA p", ProvinciaJPA.class)
                .setHint("jakarta.persistence.fetchgraph", graph)
                .getResultList();
        List<ProvinciaJPA> provinciasUnicas = new java.util.ArrayList<>(
                new java.util.LinkedHashSet<>(provincias)
        );
        for (ProvinciaJPA p : provinciasUnicas) {
            System.out.println("Provincia: " + p.getNombre());
            for (com.alp.entities.LocalidadesJPA l : p.getLocalidades()) {
                System.out.println("\tLocalidad: " + l.getNombre());
                for (com.alp.entities.PropiedadesJPA prop : l.getPropiedades()) {
                    System.out.println("\t\tPropiedad: " + prop.getReferencia() +
                            " (Media: " + prop.getMultimedia().size() + ")");
                }
            }
        }
        em.close();
    }
    public static void solucionJoinFetch() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("UnidadPersistencia");
        EntityManager em = emf.createEntityManager();

        System.out.println("--- SOLUCIÓN JOIN FETCH (Sin DISTINCT en SQL) ---");

        //podriamos usar DISTINC para los duplicados pero da un error asi que usaremos otra funcion para quitarlos abajo
        String jpql = "SELECT p FROM ProvinciaJPA p " +
                "JOIN FETCH p.localidades l " +
                "JOIN FETCH l.propiedades prop " +
                "LEFT JOIN FETCH prop.multimedia";

        List<ProvinciaJPA> provinciasConDuplicados = em.createQuery(jpql, ProvinciaJPA.class).getResultList();

        List<ProvinciaJPA> provincias = new java.util.ArrayList<>(
                new java.util.LinkedHashSet<>(provinciasConDuplicados)
        ); // eliminar dupes
        for (ProvinciaJPA p : provincias) {
            System.out.println("Provincia: " + p.getNombre());
            for (com.alp.entities.LocalidadesJPA l : p.getLocalidades()) {
                System.out.println("\tLocalidad: " + l.getNombre());
                for (com.alp.entities.PropiedadesJPA prop : l.getPropiedades()) {
                    System.out.println("\t\tPropiedad: " + prop.getReferencia() +
                            " (Media: " + prop.getMultimedia().size() + ")");
                }
            }
        }

        em.close();
    }
    public static void demoProblemaNMas1() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("UnidadPersistencia");
        EntityManager em = emf.createEntityManager();
        System.out.println("Inicio de la consulta larga");
        List<ProvinciaJPA> provincias = em.createQuery("SELECT p FROM ProvinciaJPA p", ProvinciaJPA.class).getResultList();

        for (ProvinciaJPA p : provincias) {
            System.out.println("Provincia: " + p.getNombre());

            for (com.alp.entities.LocalidadesJPA l : p.getLocalidades()) {
                System.out.println("\tLocalidad: " + l.getNombre());

                for (com.alp.entities.PropiedadesJPA prop : l.getPropiedades()) {
                    System.out.println("\t\tPropiedad: " + prop.getReferencia());

                    System.out.println("\t\t\tMedia: " + prop.getMultimedia().size() + " archivos.");
                }
            }
        }
        System.out.println("--- FIN CONSULTA ---");
        em.close();
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