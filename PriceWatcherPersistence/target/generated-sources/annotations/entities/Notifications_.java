package entities;

import entities.Agence;
import entities.Compte;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-06-05T14:14:22")
@StaticMetamodel(Notifications.class)
public class Notifications_ { 

    public static volatile SingularAttribute<Notifications, Boolean> delivre;
    public static volatile SingularAttribute<Notifications, Agence> idagence;
    public static volatile SingularAttribute<Notifications, Short> nbrevu;
    public static volatile SingularAttribute<Notifications, String> message;
    public static volatile SingularAttribute<Notifications, String> etat;
    public static volatile SingularAttribute<Notifications, Date> dateEnregistre;
    public static volatile SingularAttribute<Notifications, Compte> idCompte;
    public static volatile SingularAttribute<Notifications, String> campagne;
    public static volatile SingularAttribute<Notifications, String> contact;
    public static volatile SingularAttribute<Notifications, Date> derniereModif;
    public static volatile SingularAttribute<Notifications, Long> idnotificationsms;
    public static volatile SingularAttribute<Notifications, String> messageen;
    public static volatile SingularAttribute<Notifications, Boolean> libre;
    public static volatile SingularAttribute<Notifications, String> email;
    public static volatile SingularAttribute<Notifications, Short> nbrerepeter;
    public static volatile SingularAttribute<Notifications, Boolean> vu;
    public static volatile SingularAttribute<Notifications, Date> dernierevu;

}