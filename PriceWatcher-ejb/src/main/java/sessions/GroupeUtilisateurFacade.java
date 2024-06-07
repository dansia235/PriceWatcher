/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessions;

import entities.Agence;
import entities.GroupeUtilisateur;
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
public class GroupeUtilisateurFacade extends AbstractFacade<GroupeUtilisateur> implements GroupeUtilisateurFacadeLocal {

    @PersistenceContext(unitName = "PriceWatcherPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GroupeUtilisateurFacade() {
        super(GroupeUtilisateur.class);
    }

    @Override
    public Integer nextId() {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("GroupeUtilisateur.nextId");
        List<Integer> list = new ArrayList<>();
        list.addAll(query.getResultList());
        return list.get(0) == null ? 1 : list.get(0) + 1;
    }

    @Override
    public List<GroupeUtilisateur> findAll_Etat(String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("GroupeUtilisateur.findAll_Etat");
        query.setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public GroupeUtilisateur findByIdGroupeUtilisateur_Etat(Integer idGroupeUtilisateur, String etat, List<Agence> agenceList) {
        try {
            em.getEntityManagerFactory().getCache().evictAll();
            Query query = em.createNamedQuery("GroupeUtilisateur.findByIdGroupeUtilisateur_Etat");
            query.setParameter("idGroupeUtilisateur", idGroupeUtilisateur).setParameter("etat", etat).setParameter("agenceList", agenceList);
            List<GroupeUtilisateur> list = new ArrayList<>();
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
    public List<GroupeUtilisateur> findByLibelle_Etat(String libelle, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("GroupeUtilisateur.findByLibelle_Etat").setParameter("agenceList", agenceList);
        query.setParameter("libelle", libelle).setParameter("etat", etat);
        return query.getResultList();
    }

    @Override
    public List<GroupeUtilisateur> findByLabel_Etat(String label, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("GroupeUtilisateur.findByLabel_Etat").setParameter("agenceList", agenceList);
        query.setParameter("label", label).setParameter("etat", etat);
        return query.getResultList();
    }

    @Override
    public List<GroupeUtilisateur> findByDateEnregistre_Etat(Date dateDebut, Date dateFin, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("GroupeUtilisateur.findByDateEnregistre_Etat");
        query.setParameter("dateDebut", dateDebut).setParameter("dateFin", dateFin).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<GroupeUtilisateur> findByDerniereModif_Etat(Date dateDebut, Date dateFin, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("GroupeUtilisateur.findByDerniereModif_Etat");
        query.setParameter("dateDebut", dateDebut).setParameter("dateFin", dateFin).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

}
