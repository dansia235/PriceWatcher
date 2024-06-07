/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessions;

import entities.Reglage;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Lucien FOTSA
 */
@Local
public interface ReglageFacadeLocal {

    void create(Reglage reglage);

    void edit(Reglage reglage);

    void remove(Reglage reglage);

    Reglage find(Object id);

    List<Reglage> findAll();

    List<Reglage> findRange(int[] range);

    int count();
    
    Integer nextId();

    List<Reglage> findByIdagence(Integer idagence);
}
