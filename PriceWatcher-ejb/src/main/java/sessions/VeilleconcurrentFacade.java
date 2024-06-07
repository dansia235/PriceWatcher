/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessions;

import entities.Agence;
import entities.Concurrent;
import entities.Veilleconcurrent;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author LucienFOTSA
 */
@Stateless
public class VeilleconcurrentFacade extends AbstractFacade<Veilleconcurrent> implements VeilleconcurrentFacadeLocal {

    @PersistenceContext(unitName = "PriceWatcherPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public VeilleconcurrentFacade() {
        super(Veilleconcurrent.class);
    }

    @Override
    public List<Veilleconcurrent> findAll_Etat(String etat) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Veilleconcurrent.findAll_Etat");
        query.setParameter("etat", etat);
        return query.getResultList();
    }

    @Override
    public List<Veilleconcurrent> findByIdarticleIdconcurrentDateEnregistre_Etat(Integer idarticle, Integer idconcurrent, Date dateDebut, Date dateFin, String etat) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Veilleconcurrent.findByIdarticleIdconcurrentDateEnregistre_Etat");
        query.setParameter("idarticle", idarticle).setParameter("idconcurrent", idconcurrent).setParameter("dateDebut", dateDebut).setParameter("dateFin", dateFin).setParameter("etat", etat);
        return query.getResultList();
    }

    @Override
    public List<Veilleconcurrent> findByDateEnregistre_Etat(Date dateDebut, Date dateFin, String etat) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Veilleconcurrent.findByDateEnregistre_Etat");
        query.setParameter("dateDebut", dateDebut).setParameter("dateFin", dateFin).setParameter("etat", etat);
        return query.getResultList();
    }

    @Override
    public Integer deleteByIdconcurrent(Integer idconcurrent) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Veilleconcurrent.deleteByIdconcurrent");
        query.setParameter("idconcurrent", idconcurrent);
        return query.executeUpdate();
    }

    @Override
    public List<Concurrent> findIdconcurrentByArticle_Etat(Integer idarticle, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Veilleconcurrent.findIdconcurrentByArticle_Etat");
        query.setParameter("idarticle", idarticle).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Veilleconcurrent> findIdconcurrent_Etat(Integer idconcurrent, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Veilleconcurrent.findIdconcurrent_Etat");
        query.setParameter("idconcurrent", idconcurrent).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Veilleconcurrent> findIdconcurrent_Available_Etat(Integer idconcurrent, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Veilleconcurrent.findIdconcurrent_Available_Etat");
        query.setParameter("idconcurrent", idconcurrent).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }
}
