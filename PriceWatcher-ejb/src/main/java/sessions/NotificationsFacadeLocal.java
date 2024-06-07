/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessions;

import entities.Agence;
import entities.Notifications;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author LucienFOTSA
 */
@Local
public interface NotificationsFacadeLocal {

    void create(Notifications notificationsms);

    void edit(Notifications notificationsms);

    void remove(Notifications notificationsms);

    Notifications find(Object id);

    List<Notifications> findAll();

    List<Notifications> findRange(int[] range);

    int count();

    Long nextId();
    
    Long count_Etat(String code, List<Agence> agenceList);

    List<Notifications> findAll_Etat(String etat, List<Agence> agenceList);

    List<Agence> findAll_Plus_Utilise(List<Agence> agenceList);

    Notifications findByIdnotificationsms_Etat(Long idnotificationsms, String etat, List<Agence> agenceList);

    List<Notifications> findByIdCompte_Etat(Integer idCompte, String etat, List<Agence> agenceList);

    List<Notifications> findByIdCompteCampagne_Etat(Integer idCompte, String campagne, String etat, List<Agence> agenceList);
    
    List<Notifications> findByIdCompteCampagne_DateEnregistre_Etat(Integer idCompte, String campagne, Date dateDebut, Date dateFin, String etat, List<Agence> agenceList);
    
    List<Notifications> findByIdCompte_DateEnregistre_Etat(Integer idCompte, Date dateDebut, Date dateFin, String etat, List<Agence> agenceList);

    List<Notifications> findByIdCompteNbrevuDernierevu_Etat(Integer idCompte, Date today, String etat, List<Agence> agenceList);

    //------------------------------
    List<Notifications> findByMessage_Etat(String message, String etat, List<Agence> agenceList);

    List<Notifications> findByMessageen_Etat(String messageen, String etat, List<Agence> agenceList);

    List<Notifications> findByContact_Etat(String contact, String etat, List<Agence> agenceList);

    List<Notifications> findByEmail_Etat(String email, String etat, List<Agence> agenceList);
    
    List<Notifications> findByCampagne_Vu_Etat(String campagne, Boolean vu, String etat, List<Agence> agenceList);

    List<Notifications> findByCampagne_Etat(String campagne, String etat, List<Agence> agenceList);

    List<Notifications> findByVu_Etat(Boolean vu, String etat, List<Agence> agenceList);

    List<Notifications> findByDernierevu_Etat(Date dateDebut, Date dateFin, String etat, List<Agence> agenceList);

    List<Notifications> findByDelivre_Etat(Boolean delivre, String etat, List<Agence> agenceList);

    List<Notifications> findByLibre_Etat(Boolean libre, String etat, List<Agence> agenceList);

    List<Notifications> findByDateEnregistre_Etat(Date dateDebut, Date dateFin, String etat, List<Agence> agenceList);

    List<Notifications> findByDerniereModif_Etat(Date dateDebut, Date dateFin, String etat, List<Agence> agenceList);

}
