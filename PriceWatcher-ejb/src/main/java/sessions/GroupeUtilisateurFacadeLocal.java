/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessions;

import entities.Agence;
import entities.GroupeUtilisateur;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Lucien FOTSA
 */
@Local
public interface GroupeUtilisateurFacadeLocal {

    void create(GroupeUtilisateur groupeUtilisateur);

    void edit(GroupeUtilisateur groupeUtilisateur);

    void remove(GroupeUtilisateur groupeUtilisateur);

    GroupeUtilisateur find(Object id);

    List<GroupeUtilisateur> findAll();

    List<GroupeUtilisateur> findRange(int[] range);

    int count();

    Integer nextId();

    List<GroupeUtilisateur> findAll_Etat(String etat, List<Agence> agenceList);

    GroupeUtilisateur findByIdGroupeUtilisateur_Etat(Integer idGroupeUtilisateur, String etat, List<Agence> agenceList);

    List<GroupeUtilisateur> findByLibelle_Etat(String libelle, String etat, List<Agence> agenceList);

    List<GroupeUtilisateur> findByLabel_Etat(String label, String etat, List<Agence> agenceList);

    List<GroupeUtilisateur> findByDateEnregistre_Etat(Date dateDebut, Date dateFin, String etat, List<Agence> agenceList);

    List<GroupeUtilisateur> findByDerniereModif_Etat(Date dateDebut, Date dateFin, String etat, List<Agence> agenceList);
}
