/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessions;

import entities.Agence;
import entities.Concurrent;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author LucienFOTSA
 */
@Local
public interface ConcurrentFacadeLocal {

    void create(Concurrent concurrent);

    void edit(Concurrent concurrent);

    void remove(Concurrent concurrent);

    Concurrent find(Object id);

    List<Concurrent> findAll();

    List<Concurrent> findRange(int[] range);

    int count();

    Integer nextId();

    Concurrent findByIdconcurrent_Etat(Integer idconcurrent, String etat, List<Agence> agenceList);

    List<Concurrent> findAll_Etat(String etat, List<Agence> agenceList);

    List<Concurrent> findByLien_Etat(String lien, String etat, List<Agence> agenceList);

    List<Concurrent> findByLibelle_Etat(String libelle, String etat, List<Agence> agenceList);

    List<Concurrent> findByLabel_Etat(String label, String etat, List<Agence> agenceList);

    List<Concurrent> findByDescription_Etat(String description, String etat, List<Agence> agenceList);

    List<Concurrent> findByDescriptionen_Etat(String descriptionen, String etat, List<Agence> agenceList);

    List<Concurrent> findByDefaut_Etat(Boolean defaut, String etat, List<Agence> agenceList);

    List<Concurrent> findByDateEnregistre_Etat(Date dateDebut, Date dateFin, String etat, List<Agence> agenceList);

    List<Concurrent> findByDerniereModif_Etat(Date dateDebut, Date dateFin, String etat, List<Agence> agenceList);
}
