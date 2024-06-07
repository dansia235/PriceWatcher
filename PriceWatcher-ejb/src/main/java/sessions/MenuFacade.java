/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessions;

import entities.Menu;
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
public class MenuFacade extends AbstractFacade<Menu> implements MenuFacadeLocal {

    @PersistenceContext(unitName = "PriceWatcherPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MenuFacade() {
        super(Menu.class);
    }

    @Override
    public Integer nextId() {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Menu.nextId");
        List<Integer> list = new ArrayList<>();
        list.addAll(query.getResultList());
        return list.get(0) == null ? 1 : list.get(0) + 1;
    }

    @Override
    public Menu findByIdMenu_Etat(Integer idMenu, String etat) {
        try{
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Menu.findByIdMenu_Etat");
        query.setParameter("idMenu", idMenu).setParameter("etat", etat);
        List<Menu> list = new ArrayList<>();
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
    public List<Menu> findAll_Etat(String etat) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Menu.findAll_Etat");
        query.setParameter("etat", etat);
        return query.getResultList();
    }

    @Override
    public List<Menu> findByLibelle_Etat(String libelle, String etat) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Menu.findByLibelle_Etat");
        query.setParameter("libelle", libelle).setParameter("etat", etat);
        return query.getResultList();
    }

    @Override
    public List<Menu> findByCategorie_Etat(String categorie, String etat) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Menu.findByCategorie_Etat");
        query.setParameter("categorie", categorie).setParameter("etat", etat);
        return query.getResultList();
    }

    @Override
    public List<Menu> findByIdMenuParent_Etat(Integer idMenu, String etat) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Menu.findByIdMenuParent_Etat");
        query.setParameter("idMenu", idMenu).setParameter("etat", etat);
        return query.getResultList();
    }

    @Override
    public List<Menu> findByIdMenuParentCategorie_Etat(Integer idMenu, String categorie, String etat) {
        em.getEntityManagerFactory().getCache().evictAll();
        Query query = em.createNamedQuery("Menu.findByIdMenuParentCategorie_Etat");
        query.setParameter("idMenu", idMenu).setParameter("categorie", categorie).setParameter("etat", etat);
        return query.getResultList();
    }

    //-------------------------------------------------------------------------------------------------------
}
