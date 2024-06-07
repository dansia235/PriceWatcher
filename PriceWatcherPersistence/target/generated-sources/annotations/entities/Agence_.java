package entities;

import entities.Agence;
import entities.Article;
import entities.Compte;
import entities.Concurrent;
import entities.GroupeUtilisateur;
import entities.Multimedia;
import entities.Notifications;
import entities.Reglage;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-06-05T14:14:22")
@StaticMetamodel(Agence.class)
public class Agence_ { 

    public static volatile SingularAttribute<Agence, String> code;
    public static volatile SingularAttribute<Agence, String> libelle;
    public static volatile SingularAttribute<Agence, Float> geolong;
    public static volatile SingularAttribute<Agence, String> etat;
    public static volatile SingularAttribute<Agence, String> bp;
    public static volatile CollectionAttribute<Agence, Reglage> reglageCollection;
    public static volatile CollectionAttribute<Agence, Multimedia> multimediaCollection;
    public static volatile SingularAttribute<Agence, String> emails;
    public static volatile SingularAttribute<Agence, String> numcobtribuable;
    public static volatile CollectionAttribute<Agence, Notifications> notificationsCollection;
    public static volatile SingularAttribute<Agence, Agence> ageIdagence;
    public static volatile SingularAttribute<Agence, String> numregcom;
    public static volatile SingularAttribute<Agence, String> contact;
    public static volatile SingularAttribute<Agence, Date> derniereModif;
    public static volatile SingularAttribute<Agence, String> logo;
    public static volatile SingularAttribute<Agence, Date> datecreation;
    public static volatile SingularAttribute<Agence, String> fax;
    public static volatile CollectionAttribute<Agence, Concurrent> concurrentCollection;
    public static volatile SingularAttribute<Agence, String> sloganeng;
    public static volatile SingularAttribute<Agence, String> email;
    public static volatile SingularAttribute<Agence, String> logorelatif;
    public static volatile CollectionAttribute<Agence, Agence> agenceCollection;
    public static volatile CollectionAttribute<Agence, Article> articleCollection;
    public static volatile SingularAttribute<Agence, String> responsable;
    public static volatile SingularAttribute<Agence, String> siteWeb;
    public static volatile SingularAttribute<Agence, String> domaine;
    public static volatile SingularAttribute<Agence, Integer> idagence;
    public static volatile SingularAttribute<Agence, Float> geolat;
    public static volatile SingularAttribute<Agence, String> label;
    public static volatile SingularAttribute<Agence, String> quality;
    public static volatile CollectionAttribute<Agence, Compte> compteCollection;
    public static volatile CollectionAttribute<Agence, Compte> compteCollection1;
    public static volatile SingularAttribute<Agence, Date> dateEnregistre;
    public static volatile SingularAttribute<Agence, Float> capitalsocial;
    public static volatile SingularAttribute<Agence, String> qualite;
    public static volatile SingularAttribute<Agence, String> raisonsociale;
    public static volatile SingularAttribute<Agence, String> slogan;
    public static volatile SingularAttribute<Agence, String> contacts;
    public static volatile SingularAttribute<Agence, String> statut;
    public static volatile CollectionAttribute<Agence, GroupeUtilisateur> groupeUtilisateurCollection;

}