/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessions;

import entities.Agence;
import entities.Operations;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Lucien FOTSA
 */
@Local
public interface OperationsFacadeLocal {

    void create(Operations operations);

    void edit(Operations operations);

    void remove(Operations operations);

    Operations find(Object id);

    List<Operations> findAll();

    List<Operations> findRange(int[] range);

    int count();

    Long nextIdRaw();

    Long nextId();

    List<Operations> findAll_Etat(String etat, List<Agence> agenceList);

    Operations findByIdOperations_Etat(Long idOperations, String etat, List<Agence> agenceList);

    List<Operations> findByIdCompte_Etat(Integer idCompte, String etat, List<Agence> agenceList);

    List<Operations> findByLibelle_Etat(String libelle, String etat, List<Agence> agenceList);

    List<Operations> findByLabel_Etat(String label, String etat, List<Agence> agenceList);

    List<Operations> findByCible_Etat(String cible, String etat, List<Agence> agenceList);

    List<Operations> findByAdresseIp_Etat(String adresseIp, String etat, List<Agence> agenceList);

    List<Operations> findByAdresseMac_Etat(String adresseMac, String etat, List<Agence> agenceList);

    List<Operations> findByAdressenom_Etat(String adressenom, String etat, List<Agence> agenceList);

    List<Operations> findByDateOpration_Etat(Date dateDebut, Date dateFin, String etat, List<Agence> agenceList);

}
