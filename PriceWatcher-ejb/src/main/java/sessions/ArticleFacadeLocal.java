/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessions;

import entities.Agence;
import entities.Article;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author FOTSA Lucien
 */
@Local
public interface ArticleFacadeLocal {

    void create(Article article);

    void edit(Article article);

    void remove(Article article);

    Article find(Object id);

    List<Article> findAll();

    List<Article> findRange(int[] range);

    int count();

    Integer nextId();

    Article findByIdarticle_Etat(Integer idarticle, String etat, List<Agence> agenceList);

    List<Article> findAll_Etat(String etat, List<Agence> agenceList);

    List<Article> findByCode_Etat(String code, String etat, List<Agence> agenceList);
    
    List<Article> findByCode_Exclude_Etat(String code, Integer idarticle, String etat, List<Agence> agenceList);

    List<Article> findByLibelle_Etat(String libelle, String etat, List<Agence> agenceList);

    List<Article> findByLabel_Etat(String label, String etat, List<Agence> agenceList);

    List<Article> findByDescription_Etat(String description, String etat, List<Agence> agenceList);

    List<Article> findByDescriptionen_Etat(String descriptionen, String etat, List<Agence> agenceList);

    List<Article> findByPoids_Etat(Float min, Float max, String etat, List<Agence> agenceList);

    List<Article> findByUnitepoids_Etat(String unitepoids, String etat, List<Agence> agenceList);

    List<Article> findByCoutachatttc_Etat(Float min, Float max, String etat, List<Agence> agenceList);

    List<Article> findByPrixunit_Etat(Float min, Float max, String etat, List<Agence> agenceList);

    List<Article> findByTva_Etat(Float min, Float max, String etat, List<Agence> agenceList);

    List<Article> findByPrixunitttc_Etat(Float min, Float max, String etat, List<Agence> agenceList);

    List<Article> findByDatefabrication_Etat(Date dateDebut, Date dateFin, String etat, List<Agence> agenceList);

    List<Article> findByDateperemption_Etat(Date dateDebut, Date dateFin, String etat, List<Agence> agenceList);

    List<Article> findByQuantitestock_Etat(Float min, Float max, String etat, List<Agence> agenceList);

    List<Article> findByQuantitemin_Etat(Float min, Float max, String etat, List<Agence> agenceList);

    List<Article> findByQuantitealerte_Etat(Float min, Float max, String etat, List<Agence> agenceList);

    List<Article> findByEnvente_Etat(Boolean envente, String etat, List<Agence> agenceList);

    List<Article> findByPeremption_Etat(Boolean peremption, String etat, List<Agence> agenceList);

    List<Article> findByDateEnregistre_Etat(Date dateDebut, Date dateFin, String etat, List<Agence> agenceList);

    List<Article> findByDerniereModif_Etat(Date dateDebut, Date dateFin, String etat, List<Agence> agenceList);

    //*******************************************************************************************************
    List<Article> findAll_Stock_Alerte(String etat, List<Agence> agenceList);

    List<Article> findAll_Peremption(Date today, String etat, List<Agence> agenceList);

}
