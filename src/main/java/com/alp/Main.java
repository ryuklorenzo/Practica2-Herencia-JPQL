package com.alp;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
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