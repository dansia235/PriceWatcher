/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessions;

import entities.Agence;
import entities.Concurrent;
import entities.Veilleconcurrent;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author LucienFOTSA
 */
@Local
public interface VeilleconcurrentFacadeLocal {

    void create(Veilleconcurrent stockcomptoir);

    void edit(Veilleconcurrent stockcomptoir);

    void remove(Veilleconcurrent stockcomptoir);

    Veilleconcurrent find(Object id);

    List<Veilleconcurrent> findAll();

    List<Veilleconcurrent> findRange(int[] range);

    int count();

    List<Veilleconcurrent> findAll_Etat(String etat);

    List<Veilleconcurrent> findByIdarticleIdconcurrentDateEnregistre_Etat(Integer idarticle, Integer idconcurrent, Date dateDebut, Date dateFin, String etat);

    List<Veilleconcurrent> findByDateEnregistre_Etat(Date dateDebut, Date dateFin, String etat);

    Integer deleteByIdconcurrent(Integer idconcurrent);

    List<Concurrent> findIdconcurrentByArticle_Etat(Integer idarticle, String etat, List<Agence> agenceList);

    List<Veilleconcurrent> findIdconcurrent_Etat(Integer idconcurrent, String etat, List<Agence> agenceList);

    List<Veilleconcurrent> findIdconcurrent_Available_Etat(Integer idconcurrent, String etat, List<Agence> agenceList);
}
