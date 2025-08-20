package sr.unasat.category.api.services;

import sr.unasat.category.api.entity.Category;
import jakarta.persistence.*;
import java.util.List;

public class CategoryService {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("unasat");

    public Category save(Category category) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(category);
            tx.commit();
            return category;
        } finally {
            if (tx.isActive()) tx.rollback();
            em.close();
        }
    }

    public List<Category> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT c FROM Category c", Category.class).getResultList();
        } finally {
            em.close();
        }
    }

    public Category findById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Category.class, id);
        } finally {
            em.close();
        }
    }

    public Category update(Category category) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Category updated = em.merge(category);
            tx.commit();
            return updated;
        } finally {
            if (tx.isActive()) tx.rollback();
            em.close();
        }
    }

    public void delete(Long id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Category category = em.find(Category.class, id);
            if (category != null) {
                em.remove(category);
            }
            tx.commit();
        } finally {
            if (tx.isActive()) tx.rollback();
            em.close();
        }
    }
}