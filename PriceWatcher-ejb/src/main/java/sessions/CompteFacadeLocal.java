/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessions;

import entities.Agence;
import entities.Compte;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Lucien FOTSA
 */
@Local
public interface CompteFacadeLocal {

    void create(Compte compte);

    void edit(Compte compte);

    void remove(Compte compte);

    Compte find(Object id);

    List<Compte> findAll();

    List<Compte> findRange(int[] range);

    int count();

    Integer nextId();

    List<Compte> findAll_Etat(String etat, List<Agence> agenceList);

    Compte findByIdCompte_Etat(Integer idCompte, String etat, List<Agence> agenceList);

    List<Compte> findByContact_Etat(String contact, String etat, List<Agence> agenceList);

    List<Compte> findByFax_Etat(String fax, String etat, List<Agence> agenceList);

    List<Compte> findByBp_Etat(String bp, String etat, List<Agence> agenceList);

    List<Compte> findByEmail_Etat(String email, String etat, List<Agence> agenceList);

    List<Compte> findBySiteWeb_Etat(String siteWeb, String etat, List<Agence> agenceList);

    List<Compte> findByDomaine_Etat(String domaine, String etat, List<Agence> agenceList);

    List<Compte> findByNom_Etat(String nom, String etat, List<Agence> agenceList);

    List<Compte> findByPrenom_Etat(String prenom, String etat, List<Agence> agenceList);

    List<Compte> findByDateNaissance_Etat(Date dateDebut, Date dateFin, String etat, List<Agence> agenceList);

    List<Compte> findBySexe_Etat(String sexe, String etat, List<Agence> agenceList);

    List<Compte> findByCni_Etat(String cni, String etat, List<Agence> agenceList);

    List<Compte> findByActif_Etat(Boolean actif, String etat, List<Agence> agenceList);

    List<Compte> findByLogin_Etat(String login, String etat, List<Agence> agenceList);

    List<Compte> findByNomPrenom_Etat(String nom, String prenom, String etat, List<Agence> agenceList);

    Compte findByLoginMdp_Etat(String login, String mdp, String etat, List<Agence> agenceList);

    List<Compte> findByConnexion_Etat(String connexion, String etat, List<Agence> agenceList);

    List<Compte> findByLangue_Etat(String langue, String etat, List<Agence> agenceList);

    List<Compte> findByDateEnregistre_Etat(Date dateDebut, Date dateFin, String etat, List<Agence> agenceList);

    List<Compte> findByDerniereModif_Etat(Date dateDebut, Date dateFin, String etat, List<Agence> agenceList);

    //*****************************************************************************************************
    List<Compte> findAll_Plus_Utilise(List<Agence> agenceList);
}
