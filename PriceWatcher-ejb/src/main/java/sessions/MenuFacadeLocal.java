/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessions;

import entities.Menu;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Lucien FOTSA
 */
@Local
public interface MenuFacadeLocal {

    void create(Menu menu);

    void edit(Menu menu);

    void remove(Menu menu);

    Menu find(Object id);

    List<Menu> findAll();

    List<Menu> findRange(int[] range);

    int count();

    Integer nextId();

    Menu findByIdMenu_Etat(Integer idMenu, String etat);

    List<Menu> findAll_Etat(String etat);

    List<Menu> findByLibelle_Etat(String libelle, String etat);

    List<Menu> findByCategorie_Etat(String categorie, String etat);

    List<Menu> findByIdMenuParent_Etat(Integer idMenu, String etat);

    List<Menu> findByIdMenuParentCategorie_Etat(Integer idMenu, String categorie, String etat);

    //-------------------------------------------------------------------------------------------------------
}
