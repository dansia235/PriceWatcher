/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessions;

import entities.Agence;
import entities.Article;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author FOTSA Lucien
 */
@Stateless
public class ArticleFacade extends AbstractFacade<Article> implements ArticleFacadeLocal {

    @PersistenceContext(unitName = "PriceWatcherPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ArticleFacade() {
        super(Article.class);
    }

    @Override
    public Integer nextId() {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Article.nextId");
        List<Integer> list = new ArrayList<>();
        list.addAll(query.getResultList());
        return list.get(0) == null ? 1 : list.get(0) + 1;
    }

    @Override
    public Article findByIdarticle_Etat(Integer idarticle, String etat, List<Agence> agenceList) {
        try {
            em.getEntityManagerFactory().getCache().evictAll();
            Query query = em.createNamedQuery("Article.findByIdarticle_Etat");
            query.setParameter("idarticle", idarticle).setParameter("etat", etat).setParameter("agenceList", agenceList);
            List<Article> list = new ArrayList<>();
            list.addAll(query.getResultList());
            if (list.get(0) != null) {
                em.refresh(list.get(0));
            }
            return list.get(0);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Article> findAll_Etat(String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Article.findAll_Etat");
        query.setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Article> findByCode_Etat(String code, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Article.findByCode_Etat");
        query.setParameter("code", code).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Article> findByCode_Exclude_Etat(String code, Integer idarticle, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Article.findByCode_Exclude_Etat");
        query.setParameter("code", code).setParameter("idarticle", idarticle).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Article> findByLibelle_Etat(String libelle, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Article.findByLibelle_Etat");
        query.setParameter("libelle", libelle).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Article> findByLabel_Etat(String label, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Article.findByLabel_Etat");
        query.setParameter("label", label).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Article> findByDescription_Etat(String description, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Article.findByDescription_Etat");
        query.setParameter("description", description).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Article> findByDescriptionen_Etat(String descriptionen, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Article.findByDescriptionen_Etat");
        query.setParameter("descriptionen", descriptionen).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Article> findByPoids_Etat(Float min, Float max, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Article.findByPoids_Etat");
        query.setParameter("min", min).setParameter("max", max).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Article> findByUnitepoids_Etat(String unitepoids, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Article.findByUnitepoids_Etat");
        query.setParameter("unitepoids", unitepoids).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Article> findByCoutachatttc_Etat(Float min, Float max, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Article.findByCoutachatttc_Etat");
        query.setParameter("min", min).setParameter("max", max).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Article> findByPrixunit_Etat(Float min, Float max, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Article.findByPrixunit_Etat");
        query.setParameter("min", min).setParameter("max", max).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Article> findByTva_Etat(Float min, Float max, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Article.findByTva_Etat");
        query.setParameter("min", min).setParameter("max", max).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Article> findByPrixunitttc_Etat(Float min, Float max, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Article.findByPrixunitttc_Etat");
        query.setParameter("min", min).setParameter("max", max).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Article> findByDatefabrication_Etat(Date dateDebut, Date dateFin, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Article.findByDatefabrication_Etat");
        query.setParameter("dateDebut", dateDebut).setParameter("dateFin", dateFin).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Article> findByDateperemption_Etat(Date dateDebut, Date dateFin, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Article.findByDateperemption_Etat");
        query.setParameter("dateDebut", dateDebut).setParameter("dateFin", dateFin).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Article> findByQuantitestock_Etat(Float min, Float max, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Article.findByQuantitestock_Etat");
        query.setParameter("min", min).setParameter("max", max).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Article> findByQuantitemin_Etat(Float min, Float max, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Article.findByQuantitemin_Etat");
        query.setParameter("min", min).setParameter("max", max).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Article> findByQuantitealerte_Etat(Float min, Float max, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Article.findByQuantitealerte_Etat");
        query.setParameter("min", min).setParameter("max", max).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Article> findByEnvente_Etat(Boolean envente, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Article.findByEnvente_Etat");
        query.setParameter("envente", envente).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Article> findByPeremption_Etat(Boolean peremption, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Article.findByPeremption_Etat");
        query.setParameter("peremption", peremption).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Article> findByDateEnregistre_Etat(Date dateDebut, Date dateFin, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Article.findByDateEnregistre_Etat");
        query.setParameter("dateDebut", dateDebut).setParameter("dateFin", dateFin).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Article> findByDerniereModif_Etat(Date dateDebut, Date dateFin, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Article.findByDerniereModif_Etat");
        query.setParameter("dateDebut", dateDebut).setParameter("dateFin", dateFin).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    //*******************************************************************************************************
    @Override
    public List<Article> findAll_Stock_Alerte(String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Article.findAll_Stock_Alerte");
        query.setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Article> findAll_Peremption(Date today, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Article.findAll_Peremption");
        query.setParameter("today", today).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

}
