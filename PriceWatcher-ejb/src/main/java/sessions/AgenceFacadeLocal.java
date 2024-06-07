/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessions;

import entities.Agence;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Lucien FOTSA
 */
@Local
public interface AgenceFacadeLocal {

    void create(Agence agence);

    void edit(Agence agence);

    void remove(Agence agence);

    Agence find(Object id);

    List<Agence> findAll();

    List<Agence> findRange(int[] range);

    int count();

    Integer nextId();

    Agence findByIdagence_Etat(Integer idagence, String etat);

    //-------------------------------------------------------------------------------------
    List<Agence> findAll_Siege_Etat(String etat);

    List<Agence> findAll_Branche_Etat(String etat);

    List<Agence> findAll_Etat(String etat);

    //-------------------------------------------------------------------------------------
    List<Agence> findAll_Etat_In(String etat, List<Agence> agenceList);

    List<Agence> findByCode_Etat(String code, String etat, List<Agence> agenceList);

    List<Agence> findByContact_Etat(String contact, String etat, List<Agence> agenceList);

    List<Agence> findByFax_Etat(String fax, String etat, List<Agence> agenceList);

    List<Agence> findByBp_Etat(String bp, String etat, List<Agence> agenceList);

    List<Agence> findByEmail_Etat(String email, String etat, List<Agence> agenceList);

    List<Agence> findBySiteWeb_Etat(String siteWeb, String etat, List<Agence> agenceList);

    List<Agence> findByDomaineOnly_Etat(String domaine, String etat);

    List<Agence> findByDomaine_Etat(String domaine, String etat, List<Agence> agenceList);

    List<Agence> findByLibelle_Etat(String libelle, String etat, List<Agence> agenceList);

    List<Agence> findByLabel_Etat(String label, String etat, List<Agence> agenceList);

    List<Agence> findByRaisonsociale_Etat(String raisonsociale, String etat, List<Agence> agenceList);

    List<Agence> findByNumcobtribuable_Etat(String numcobtribuable, String etat, List<Agence> agenceList);

    List<Agence> findByCapitalsocial_Etat(Float min, Float max, String etat, List<Agence> agenceList);

    List<Agence> findByDateCreation_Etat(Date dateDebut, Date dateFin, String etat, List<Agence> agenceList);

    List<Agence> findByStatut_Etat(String statut, String etat, List<Agence> agenceList);

    List<Agence> findByDomaine_Statut_Etat(String domaine, String statut, String etat, List<Agence> agenceList);

    List<Agence> findBySlogan_Etat(String slogan, String etat, List<Agence> agenceList);

    List<Agence> findBySloganeng_Etat(String sloganeng, String etat, List<Agence> agenceList);

    List<Agence> findByResponsable_Etat(String responsable, String etat, List<Agence> agenceList);

    List<Agence> findByDateEnregistre_Etat(Date dateDebut, Date dateFin, String etat, List<Agence> agenceList);

    List<Agence> findByDerniereModif_Etat(Date dateDebut, Date dateFin, String etat, List<Agence> agenceList);

}
