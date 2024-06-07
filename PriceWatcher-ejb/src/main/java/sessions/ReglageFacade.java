/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessions;

import entities.Reglage;
import java.util.ArrayList;
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
public class ReglageFacade extends AbstractFacade<Reglage> implements ReglageFacadeLocal {

    @PersistenceContext(unitName = "PriceWatcherPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ReglageFacade() {
        super(Reglage.class);
    }
    
    @Override
    public Integer nextId() {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Reglage.nextId");
        List<Integer> list = new ArrayList<>();
        list.addAll(query.getResultList());
        return list.get(0) == null ? 1 : list.get(0) + 1;
    }

    @Override
    public List<Reglage> findByIdagence(Integer idagence) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Reglage.findByIdagence");
        query.setParameter("idagence", idagence);
        return query.getResultList();
    }
}
