/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessions;

import entities.Agence;
import entities.Operations;
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
public class OperationsFacade extends AbstractFacade<Operations> implements OperationsFacadeLocal {

    @PersistenceContext(unitName = "PriceWatcherPU")
    private EntityManager em;
    private static volatile Long cId;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OperationsFacade() {
        super(Operations.class);
    }

    @Override
    public Long nextIdRaw() {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Operations.nextId");
        List<Long> list = new ArrayList<>();
        list.addAll(query.getResultList());
        cId = list.get(0) == null ? 1L : list.get(0);
        return (cId += 1);
    }

    @Override
    public Long nextId() {
        if (cId == null) {
            em.getEntityManagerFactory().getCache().evictAll();
            Query query = em.createNamedQuery("Operations.nextId");
            List<Long> list = new ArrayList<>();
            list.addAll(query.getResultList());
            cId = list.get(0) == null ? 1L : list.get(0);
        }
        return (cId += 1);
    }

    @Override
    public List<Operations> findAll_Etat(String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Operations.findAll_Etat");
        query.setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public Operations findByIdOperations_Etat(Long idOperations, String etat, List<Agence> agenceList) {
        try {
            em.getEntityManagerFactory().getCache().evictAll();
            Query query = em.createNamedQuery("Operations.findByIdOperations_Etat");
            query.setParameter("idOperations", idOperations).setParameter("etat", etat).setParameter("agenceList", agenceList);
            List<Operations> list = new ArrayList<>();
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
    public List<Operations> findByIdCompte_Etat(Integer idCompte, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Operations.findByIdCompte_Etat");
        query.setParameter("idCompte", idCompte).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Operations> findByLibelle_Etat(String libelle, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Operations.findByLibelle_Etat");
        query.setParameter("libelle", libelle).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Operations> findByLabel_Etat(String label, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Operations.findByLabel_Etat");
        query.setParameter("label", label).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Operations> findByCible_Etat(String cible, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Operations.findByCible_Etat");
        query.setParameter("cible", cible).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Operations> findByAdresseIp_Etat(String adresseIp, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Operations.findByAdresseIp_Etat");
        query.setParameter("adresseIp", adresseIp).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Operations> findByAdresseMac_Etat(String adresseMac, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Operations.findByAdresseMac_Etat");
        query.setParameter("adresseMac", adresseMac).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Operations> findByAdressenom_Etat(String adressenom, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Operations.findByAdressenom_Etat");
        query.setParameter("adressenom", adressenom).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Operations> findByDateOpration_Etat(Date dateDebut, Date dateFin, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Operations.findByDateOpration_Etat");
        query.setParameter("dateDebut", dateDebut).setParameter("dateFin", dateFin).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

}
