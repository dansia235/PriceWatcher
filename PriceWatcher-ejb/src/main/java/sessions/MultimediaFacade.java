/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessions;

import entities.Agence;
import entities.Multimedia;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Lucien FOTSA
 */
@Stateless
public class MultimediaFacade extends AbstractFacade<Multimedia> implements MultimediaFacadeLocal {

    @PersistenceContext(unitName = "PriceWatcherPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MultimediaFacade() {
        super(Multimedia.class);
    }

    @Override
    public Integer nextId() {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Multimedia.nextId");
        List<Integer> list = new ArrayList<>();
        list.addAll(query.getResultList());
        return list.get(0) == null ? 1 : list.get(0) + 1;
    }

    @Override
    public List<Multimedia> findAll_Etat(String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Multimedia.findAll_Etat");
        query.setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public Multimedia findByIdmultimedia_Etat(Integer idmultimedia, String etat, List<Agence> agenceList) {
        try {
            em.getEntityManagerFactory().getCache().evictAll();
            Query query = em.createNamedQuery("Multimedia.findByIdmultimedia_Etat");
            query.setParameter("idmultimedia", idmultimedia).setParameter("etat", etat).setParameter("agenceList", agenceList);
            List<Multimedia> list = new ArrayList<>();
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
    public List<Multimedia> findByType_Etat(String type, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Multimedia.findByType_Etat");
        query.setParameter("type", type).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Multimedia> findByCategorie_Etat(String categorie, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Multimedia.findByCategorie_Etat");
        query.setParameter("categorie", categorie).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Multimedia> findByCategorie_Actif_Etat(String categorie, Boolean actif, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Multimedia.findByCategorie_Actif_Etat");
        query.setParameter("categorie", categorie).setParameter("actif", actif).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Multimedia> findByLien_Etat(String lien, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Multimedia.findByLien_Etat");
        query.setParameter("lien", lien).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Multimedia> findByTitre_Etat(String titre, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Multimedia.findByTitre_Etat");
        query.setParameter("titre", titre).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Multimedia> findByCommentaire_Etat(String commentaire, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Multimedia.findByCommentaire_Etat");
        query.setParameter("commentaire", commentaire).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Multimedia> findByTitreen_Etat(String titreen, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Multimedia.findByTitreen_Etat");
        query.setParameter("titreen", titreen).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Multimedia> findByCommentaireen_Etat(String commentaireen, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Multimedia.findByCommentaireen_Etat");
        query.setParameter("commentaireen", commentaireen).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Multimedia> findByTaille_Etat(Float min, Float max, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Multimedia.findByTaille_Etat");
        query.setParameter("min", min).setParameter("max", max).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Multimedia> findByActif_Etat(Boolean actif, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Multimedia.findByActif_Etat");
        query.setParameter("actif", actif).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Multimedia> findByDateEnregistre_Etat(Date dateDebut, Date dateFin, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Multimedia.findByDateEnregistre_Etat");
        query.setParameter("dateDebut", dateDebut).setParameter("dateFin", dateFin).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Multimedia> findByDerniereModif_Etat(Date dateDebut, Date dateFin, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Multimedia.findByDerniereModif_Etat");
        query.setParameter("dateDebut", dateDebut).setParameter("dateFin", dateFin).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

}
