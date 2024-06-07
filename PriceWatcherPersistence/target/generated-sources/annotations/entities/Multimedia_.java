package entities;

import entities.Agence;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-06-05T14:14:22")
@StaticMetamodel(Multimedia.class)
public class Multimedia_ { 

    public static volatile SingularAttribute<Multimedia, String> categorie;
    public static volatile SingularAttribute<Multimedia, String> titre;
    public static volatile SingularAttribute<Multimedia, Agence> idagence;
    public static volatile SingularAttribute<Multimedia, Integer> idmultimedia;
    public static volatile SingularAttribute<Multimedia, String> chemin;
    public static volatile SingularAttribute<Multimedia, Boolean> actif;
    public static volatile SingularAttribute<Multimedia, String> type;
    public static volatile SingularAttribute<Multimedia, String> titreen;
    public static volatile SingularAttribute<Multimedia, String> commentaireen;
    public static volatile SingularAttribute<Multimedia, String> etat;
    public static volatile SingularAttribute<Multimedia, Date> dateEnregistre;
    public static volatile SingularAttribute<Multimedia, Float> taille;
    public static volatile SingularAttribute<Multimedia, String> lien;
    public static volatile SingularAttribute<Multimedia, Date> derniereModif;
    public static volatile SingularAttribute<Multimedia, String> commentaire;
    public static volatile SingularAttribute<Multimedia, String> cheminrelatif;

}