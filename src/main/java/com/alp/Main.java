package com.alp;

import com.alp.entities.ProvinciaJPA;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        getProvincias();
    }

    public static void getProvincias() {
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("UnidadPersistencia");
        EntityManager em = emf.createEntityManager();
        //em.getTransaction().begin();
        String jpql = "SELECT p FROM ProvinciaJPA p ";
        List<ProvinciaJPA> propiedades = em.createQuery(jpql,
                ProvinciaJPA.class).getResultList();
        em.close();
        propiedades.forEach(provinciaJPA -> {
            System.out.println(provinciaJPA.getNombre());
        });
    }
}