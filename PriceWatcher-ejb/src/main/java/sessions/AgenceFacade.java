/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessions;

import entities.Agence;
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
public class AgenceFacade extends AbstractFacade<Agence> implements AgenceFacadeLocal {

    @PersistenceContext(unitName = "PriceWatcherPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AgenceFacade() {
        super(Agence.class);
    }

    public List<Integer> idAgenceList(List<Agence> agenceList) {
        List<Integer> idAgenceList = new ArrayList();
        agenceList.forEach((a) -> {
            idAgenceList.add(a.getIdagence());
        });
        return idAgenceList;
    }

    @Override
    public Integer nextId() {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Agence.nextId");
        List<Integer> list = new ArrayList<>();
        list.addAll(query.getResultList());
        return list.get(0) == null ? 1 : list.get(0) + 1;
    }

    @Override
    public Agence findByIdagence_Etat(Integer idagence, String etat) {
        try {
            em.getEntityManagerFactory().getCache().evictAll();
            Query query = em.createNamedQuery("Agence.findByIdagence_Etat");
            query.setParameter("idagence", idagence).setParameter("etat", etat);
            List<Agence> list = new ArrayList<>();
            list.addAll(query.getResultList());
            if (list.get(0) != null) {
                em.refresh(list.get(0));
            }
            return list.get(0);
        } catch (Exception e) {
            return null;
        }
    }

    //----------------------------------------------------------------------------
    @Override
    public List<Agence> findAll_Siege_Etat(String etat) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Agence.findAll_Siege_Etat");
        query.setParameter("etat", etat);
        return query.getResultList();
    }

    @Override
    public List<Agence> findAll_Branche_Etat(String etat) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Agence.findAll_Branche_Etat");
        query.setParameter("etat", etat);
        return query.getResultList();
    }

    @Override
    public List<Agence> findAll_Etat(String etat) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Agence.findAll_Etat");
        query.setParameter("etat", etat);
        return query.getResultList();
    }

    //----------------------------------------------------------------------------
    @Override
    public List<Agence> findAll_Etat_In(String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Agence.findAll_Etat_In");
        query.setParameter("etat", etat).setParameter("agenceList", idAgenceList(agenceList));
        return query.getResultList();
    }

    @Override
    public List<Agence> findByCode_Etat(String code, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Agence.findByCode_Etat");
        query.setParameter("code", code).setParameter("etat", etat).setParameter("agenceList", idAgenceList(agenceList));
        return query.getResultList();
    }

    @Override
    public List<Agence> findByContact_Etat(String contact, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Agence.findByContact_Etat");
        query.setParameter("contact", contact).setParameter("etat", etat).setParameter("agenceList", idAgenceList(agenceList));
        return query.getResultList();
    }

    @Override
    public List<Agence> findByFax_Etat(String fax, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Agence.findByFax_Etat");
        query.setParameter("fax", fax).setParameter("etat", etat).setParameter("agenceList", idAgenceList(agenceList));
        return query.getResultList();
    }

    @Override
    public List<Agence> findByBp_Etat(String bp, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Agence.findByBp_Etat");
        query.setParameter("bp", bp).setParameter("etat", etat).setParameter("agenceList", idAgenceList(agenceList));
        return query.getResultList();
    }

    @Override
    public List<Agence> findByEmail_Etat(String email, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Agence.findByEmail_Etat");
        query.setParameter("email", email).setParameter("etat", etat).setParameter("agenceList", idAgenceList(agenceList));
        return query.getResultList();
    }

    @Override
    public List<Agence> findBySiteWeb_Etat(String siteWeb, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Agence.findBySiteWeb_Etat");
        query.setParameter("siteWeb", siteWeb).setParameter("etat", etat).setParameter("agenceList", idAgenceList(agenceList));
        return query.getResultList();
    }

    @Override
    public List<Agence> findByDomaineOnly_Etat(String domaine, String etat) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Agence.findByDomaineOnly_Etat");
        query.setParameter("domaine", domaine).setParameter("etat", etat);
        return query.getResultList();
    }

    @Override
    public List<Agence> findByDomaine_Etat(String domaine, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Agence.findByDomaine_Etat");
        query.setParameter("domaine", domaine).setParameter("etat", etat).setParameter("agenceList", idAgenceList(agenceList));
        return query.getResultList();
    }

    @Override
    public List<Agence> findByLibelle_Etat(String libelle, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Agence.findByLibelle_Etat");
        query.setParameter("libelle", libelle).setParameter("etat", etat).setParameter("agenceList", idAgenceList(agenceList));
        return query.getResultList();
    }

    @Override
    public List<Agence> findByLabel_Etat(String label, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Agence.findByLabel_Etat");
        query.setParameter("label", label).setParameter("etat", etat).setParameter("agenceList", idAgenceList(agenceList));
        return query.getResultList();
    }

    @Override
    public List<Agence> findByRaisonsociale_Etat(String raisonsociale, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Agence.findByRaisonsociale_Etat");
        query.setParameter("raisonsociale", raisonsociale).setParameter("etat", etat).setParameter("agenceList", idAgenceList(agenceList));
        return query.getResultList();
    }

    @Override
    public List<Agence> findByNumcobtribuable_Etat(String numcobtribuable, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Agence.findByNumcobtribuable_Etat");
        query.setParameter("numcobtribuable", numcobtribuable).setParameter("etat", etat).setParameter("agenceList", idAgenceList(agenceList));
        return query.getResultList();
    }

    @Override
    public List<Agence> findByCapitalsocial_Etat(Float min, Float max, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Agence.findByCapitalsocial_Etat");
        query.setParameter("min", min).setParameter("max", min).setParameter("etat", etat).setParameter("agenceList", idAgenceList(agenceList));
        return query.getResultList();
    }

    @Override
    public List<Agence> findByDateCreation_Etat(Date dateDebut, Date dateFin, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Agence.findByDateCreation_Etat");
        query.setParameter("dateDebut", dateDebut).setParameter("dateFin", dateFin).setParameter("etat", etat).setParameter("agenceList", idAgenceList(agenceList));
        return query.getResultList();
    }

    @Override
    public List<Agence> findByStatut_Etat(String statut, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Agence.findByStatut_Etat");
        query.setParameter("statut", statut).setParameter("etat", etat).setParameter("agenceList", idAgenceList(agenceList));
        return query.getResultList();
    }

    @Override
    public List<Agence> findByDomaine_Statut_Etat(String domaine, String statut, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Agence.findByDomaine_Statut_Etat");
        query.setParameter("domaine", domaine).setParameter("statut", statut).setParameter("etat", etat).setParameter("agenceList", idAgenceList(agenceList));
        return query.getResultList();
    }

    @Override
    public List<Agence> findBySlogan_Etat(String slogan, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Agence.findBySlogan_Etat");
        query.setParameter("slogan", slogan).setParameter("etat", etat).setParameter("agenceList", idAgenceList(agenceList));
        return query.getResultList();
    }

    @Override
    public List<Agence> findBySloganeng_Etat(String sloganeng, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Agence.findBySloganeng_Etat");
        query.setParameter("sloganeng", sloganeng).setParameter("etat", etat).setParameter("agenceList", idAgenceList(agenceList));
        return query.getResultList();
    }

    @Override
    public List<Agence> findByResponsable_Etat(String responsable, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Agence.findByResponsable_Etat");
        query.setParameter("responsable", responsable).setParameter("etat", etat).setParameter("agenceList", idAgenceList(agenceList));
        return query.getResultList();
    }

    @Override
    public List<Agence> findByDateEnregistre_Etat(Date dateDebut, Date dateFin, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Agence.findByDateEnregistre_Etat");
        query.setParameter("dateDebut", dateDebut).setParameter("dateFin", dateFin).setParameter("etat", etat).setParameter("agenceList", idAgenceList(agenceList));
        return query.getResultList();
    }

    @Override
    public List<Agence> findByDerniereModif_Etat(Date dateDebut, Date dateFin, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Agence.findByDerniereModif_Etat");
        query.setParameter("dateDebut", dateDebut).setParameter("dateFin", dateFin).setParameter("etat", etat).setParameter("agenceList", idAgenceList(agenceList));
        return query.getResultList();
    }

}
