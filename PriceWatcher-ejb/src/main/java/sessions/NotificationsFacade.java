/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessions;

import entities.Agence;
import entities.Notifications;
import java.util.ArrayList;
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
public class NotificationsFacade extends AbstractFacade<Notifications> implements NotificationsFacadeLocal {

    @PersistenceContext(unitName = "PriceWatcherPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public NotificationsFacade() {
        super(Notifications.class);
    }

    @Override
    public Long nextId() {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Notifications.nextId");
        List<Long> list = new ArrayList<>();
        list.addAll(query.getResultList());
        return list.get(0) == null ? 1 : list.get(0) + 1;
    }

    @Override
    public Long count_Etat(String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Notifications.count_Etat");
        query.setParameter("etat", etat).setParameter("agenceList", agenceList);
        List<Long> list = new ArrayList<>();
        list.addAll(query.getResultList());
        Long total = list.get(0) == null ? 0 : list.get(0);
        return total;
    }

    @Override
    public List<Notifications> findAll_Etat(String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Notifications.findAll_Etat");
        query.setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Agence> findAll_Plus_Utilise(List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Notifications.findAll_Plus_Utilise");
        query.setParameter("agenceList", agenceList);
        List<Agence> listResult = new ArrayList();
        List<Object[]> results = query.getResultList();
        results.forEach((result) -> {
            listResult.add((Agence) result[0]);
        });
        return listResult;
    }

    @Override
    public Notifications findByIdnotificationsms_Etat(Long idnotificationsms, String etat, List<Agence> agenceList) {
        try {
            em.getEntityManagerFactory().getCache().evictAll();
            Query query = em.createNamedQuery("Notifications.findByIdnotificationsms_Etat");
            query.setParameter("idnotificationsms", idnotificationsms).setParameter("etat", etat).setParameter("agenceList", agenceList);
            List<Notifications> list = new ArrayList<>();
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
    public List<Notifications> findByIdCompte_Etat(Integer idCompte, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Notifications.findByIdCompte_Etat");
        query.setParameter("idCompte", idCompte).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Notifications> findByIdCompte_DateEnregistre_Etat(Integer idCompte, Date dateDebut, Date dateFin, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Notifications.findByIdCompte_DateEnregistre_Etat");
        query.setParameter("idCompte", idCompte).setParameter("dateDebut", dateDebut).setParameter("dateFin", dateFin).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Notifications> findByIdCompteCampagne_Etat(Integer idCompte, String campagne, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Notifications.findByIdCompteCampagne_Etat");
        query.setParameter("idCompte", idCompte).setParameter("campagne", campagne).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Notifications> findByIdCompteCampagne_DateEnregistre_Etat(Integer idCompte, String campagne, Date dateDebut, Date dateFin, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Notifications.findByIdCompteCampagne_DateEnregistre_Etat");
        query.setParameter("idCompte", idCompte).setParameter("campagne", campagne).setParameter("dateDebut", dateDebut).setParameter("dateFin", dateFin).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Notifications> findByIdCompteNbrevuDernierevu_Etat(Integer idCompte, Date today, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Notifications.findByIdCompteNbrevuDernierevu_Etat");
        query.setParameter("idCompte", idCompte).setParameter("today", today).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    //---------------------------------------
    @Override
    public List<Notifications> findByMessage_Etat(String message, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Notifications.findByMessage_Etat");
        query.setParameter("message", message).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Notifications> findByMessageen_Etat(String messageen, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Notifications.findByMessageen_Etat");
        query.setParameter("messageen", messageen).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Notifications> findByContact_Etat(String contact, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Notifications.findByContact_Etat");
        query.setParameter("contact", contact).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Notifications> findByEmail_Etat(String email, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Notifications.findByEmail_Etat");
        query.setParameter("email", email).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Notifications> findByCampagne_Vu_Etat(String campagne, Boolean vu, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Notifications.findByCampagne_Vu_Etat");
        query.setParameter("campagne", campagne).setParameter("vu", vu).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Notifications> findByCampagne_Etat(String campagne, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Notifications.findByCampagne_Etat");
        query.setParameter("campagne", campagne).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Notifications> findByVu_Etat(Boolean vu, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Notifications.findByVu_Etat");
        query.setParameter("vu", vu).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Notifications> findByDernierevu_Etat(Date dateDebut, Date dateFin, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Notifications.findByDernierevu_Etat");
        query.setParameter("dateDebut", dateDebut).setParameter("dateFin", dateFin).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Notifications> findByDelivre_Etat(Boolean delivre, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Notifications.findByDelivre_Etat");
        query.setParameter("delivre", delivre).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Notifications> findByLibre_Etat(Boolean libre, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Notifications.findByLibre_Etat");
        query.setParameter("libre", libre).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Notifications> findByDateEnregistre_Etat(Date dateDebut, Date dateFin, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Notifications.findByDateEnregistre_Etat");
        query.setParameter("dateDebut", dateDebut).setParameter("dateFin", dateFin).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

    @Override
    public List<Notifications> findByDerniereModif_Etat(Date dateDebut, Date dateFin, String etat, List<Agence> agenceList) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Notifications.findByDerniereModif_Etat");
        query.setParameter("dateDebut", dateDebut).setParameter("dateFin", dateFin).setParameter("etat", etat).setParameter("agenceList", agenceList);
        return query.getResultList();
    }

}
