package entities;

import entities.Agence;
import entities.GroupeUtilisateur;
import entities.Notifications;
import entities.Operations;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-06-05T14:14:22")
@StaticMetamodel(Compte.class)
public class Compte_ { 

    public static volatile SingularAttribute<Compte, String> note;
    public static volatile SingularAttribute<Compte, String> photoRelatif;
    public static volatile SingularAttribute<Compte, Date> dateNaissance;
    public static volatile SingularAttribute<Compte, Float> geolong;
    public static volatile SingularAttribute<Compte, String> sexe;
    public static volatile SingularAttribute<Compte, String> connexion;
    public static volatile SingularAttribute<Compte, String> login;
    public static volatile SingularAttribute<Compte, String> nom;
    public static volatile SingularAttribute<Compte, String> etat;
    public static volatile SingularAttribute<Compte, String> bp;
    public static volatile CollectionAttribute<Compte, Notifications> notificationsCollection;
    public static volatile CollectionAttribute<Compte, Operations> operationsCollection;
    public static volatile SingularAttribute<Compte, Integer> idCompte;
    public static volatile SingularAttribute<Compte, String> contact;
    public static volatile SingularAttribute<Compte, Date> derniereModif;
    public static volatile SingularAttribute<Compte, String> fax;
    public static volatile SingularAttribute<Compte, Boolean> nevers;
    public static volatile SingularAttribute<Compte, String> prenom;
    public static volatile SingularAttribute<Compte, String> email;
    public static volatile SingularAttribute<Compte, String> cni;
    public static volatile CollectionAttribute<Compte, Agence> agenceCollection;
    public static volatile SingularAttribute<Compte, String> siteWeb;
    public static volatile SingularAttribute<Compte, String> domaine;
    public static volatile SingularAttribute<Compte, Agence> idagence;
    public static volatile SingularAttribute<Compte, Float> geolat;
    public static volatile SingularAttribute<Compte, String> photo;
    public static volatile SingularAttribute<Compte, Boolean> actif;
    public static volatile SingularAttribute<Compte, String> langue;
    public static volatile SingularAttribute<Compte, String> lieunaissance;
    public static volatile SingularAttribute<Compte, Date> dateEnregistre;
    public static volatile SingularAttribute<Compte, String> mdp;
    public static volatile SingularAttribute<Compte, String> droits;
    public static volatile CollectionAttribute<Compte, GroupeUtilisateur> groupeUtilisateurCollection;

}