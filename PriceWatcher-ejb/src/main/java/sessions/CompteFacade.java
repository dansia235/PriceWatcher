/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessions;

import entities.Agence;
import entities.Compte;
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
public class CompteFacade extends AbstractFacade<Compte> implements CompteFacadeLocal {

    @PersistenceContext(unitName = "PriceWatcherPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CompteFacade() {
        super(Compte.class);
    }

    @Override
    public Integer nextId() {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Compte.nextId");
        List<Integer> list = new ArrayList<>();
        list.addAll(query.getResultList());
        return list.get(0) == null ? 1 : list.get(0) + 1;
    }

    @Override
    public List<Compte> findAll_Etat(String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Compte.findAll_Etat");
        query.setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public Compte findByIdCompte_Etat(Integer idCompte, String etat, List<Agence> agenceList) {
        try {
            em.getEntityManagerFactory().getCache().evictAll();
            Query query = em.createNamedQuery("Compte.findByIdCompte_Etat");
            query.setParameter("idCompte", idCompte).setParameter("etat", etat).setParameter("agenceList", agenceList);
            List<Compte> list = new ArrayList<>();
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
    public List<Compte> findByContact_Etat(String contact, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Compte.findByContact_Etat");
        query.setParameter("contact", contact).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Compte> findByFax_Etat(String fax, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Compte.findByFax_Etat");
        query.setParameter("fax", fax).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Compte> findByBp_Etat(String bp, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Compte.findByBp_Etat");
        query.setParameter("bp", bp).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Compte> findByEmail_Etat(String email, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Compte.findByEmail_Etat");
        query.setParameter("email", email).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Compte> findBySiteWeb_Etat(String siteWeb, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Compte.findBySiteWeb_Etat");
        query.setParameter("siteWeb", siteWeb).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Compte> findByDomaine_Etat(String domaine, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Compte.findByDomaine_Etat");
        query.setParameter("domaine", domaine).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Compte> findByNom_Etat(String nom, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Compte.findByNom_Etat");
        query.setParameter("nom", nom).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Compte> findByPrenom_Etat(String prenom, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Compte.findByPrenom_Etat");
        query.setParameter("prenom", prenom).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Compte> findByDateNaissance_Etat(Date dateDebut, Date dateFin, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Compte.findByDateNaissance_Etat");
        query.setParameter("dateDebut", dateDebut).setParameter("dateFin", dateFin).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Compte> findBySexe_Etat(String sexe, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Compte.findBySexe_Etat");
        query.setParameter("sexe", sexe).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Compte> findByCni_Etat(String cni, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Compte.findByCni_Etat");
        query.setParameter("cni", cni).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Compte> findByActif_Etat(Boolean actif, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Compte.findByActif_Etat");
        query.setParameter("actif", actif).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Compte> findByLogin_Etat(String login, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Compte.findByLogin_Etat");
        query.setParameter("login", login).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Compte> findByNomPrenom_Etat(String nom, String prenom, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Compte.findByNomPrenom_Etat");
        query.setParameter("nom", nom).setParameter("prenom", prenom).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public Compte findByLoginMdp_Etat(String login, String mdp, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Compte.findByLoginMdp_Etat");
        query.setParameter("login", login).setParameter("mdp", mdp).setParameter("etat", etat).setParameter("agenceList", agenceList);
        List<Compte> list = new ArrayList<>();
        list.addAll(query.getResultList());
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<Compte> findByConnexion_Etat(String connexion, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Compte.findByConnexion_Etat");
        query.setParameter("connexion", connexion).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Compte> findByLangue_Etat(String langue, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Compte.findByLangue_Etat");
        query.setParameter("langue", langue).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Compte> findByDateEnregistre_Etat(Date dateDebut, Date dateFin, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Compte.findByDateEnregistre_Etat");
        query.setParameter("dateDebut", dateDebut).setParameter("dateFin", dateFin).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Compte> findByDerniereModif_Etat(Date dateDebut, Date dateFin, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Compte.findByDerniereModif_Etat");
        query.setParameter("dateDebut", dateDebut).setParameter("dateFin", dateFin).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    //*****************************************************************************************************
    @Override
    public List<Compte> findAll_Plus_Utilise(List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Compte.findAll_Plus_Utilise");
        query.setParameter("agenceList", agenceList);
        query.setMaxResults(7);
        List<Compte> listResult = new ArrayList();
        List<Object[]> results = query.getResultList();
        results.forEach((result) -> {
            listResult.add((Compte) result[0]);
        });
        return listResult;
    }

}
