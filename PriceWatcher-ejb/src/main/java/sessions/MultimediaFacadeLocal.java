/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessions;

import entities.Agence;
import entities.Multimedia;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Lucien FOTSA
 */
@Local
public interface MultimediaFacadeLocal {

    void create(Multimedia multimedia);

    void edit(Multimedia multimedia);

    void remove(Multimedia multimedia);

    Multimedia find(Object id);

    List<Multimedia> findAll();

    List<Multimedia> findRange(int[] range);

    int count();

    Integer nextId();

    List<Multimedia> findAll_Etat(String etat, List<Agence> agenceList);

    Multimedia findByIdmultimedia_Etat(Integer idmultimedia, String etat, List<Agence> agenceList);

    List<Multimedia> findByType_Etat(String type, String etat, List<Agence> agenceList);

    List<Multimedia> findByCategorie_Etat(String categorie, String etat, List<Agence> agenceList);

    List<Multimedia> findByCategorie_Actif_Etat(String categorie, Boolean actif, String etat, List<Agence> agenceList);

    List<Multimedia> findByLien_Etat(String lien, String etat, List<Agence> agenceList);

    List<Multimedia> findByTitre_Etat(String titre, String etat, List<Agence> agenceList);

    List<Multimedia> findByCommentaire_Etat(String commentaire, String etat, List<Agence> agenceList);

    List<Multimedia> findByTitreen_Etat(String titreen, String etat, List<Agence> agenceList);

    List<Multimedia> findByCommentaireen_Etat(String commentaireen, String etat, List<Agence> agenceList);

    List<Multimedia> findByTaille_Etat(Float min, Float max, String etat, List<Agence> agenceList);

    List<Multimedia> findByActif_Etat(Boolean actif, String etat, List<Agence> agenceList);

    List<Multimedia> findByDateEnregistre_Etat(Date dateDebut, Date dateFin, String etat, List<Agence> agenceList);

    List<Multimedia> findByDerniereModif_Etat(Date dateDebut, Date dateFin, String etat, List<Agence> agenceList);
}
