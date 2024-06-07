package entities;

import entities.Compte;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-06-05T14:14:22")
@StaticMetamodel(Operations.class)
public class Operations_ { 

    public static volatile SingularAttribute<Operations, String> cible;
    public static volatile SingularAttribute<Operations, String> adressenom;
    public static volatile SingularAttribute<Operations, Long> idOperations;
    public static volatile SingularAttribute<Operations, Compte> idCompte;
    public static volatile SingularAttribute<Operations, String> libelle;
    public static volatile SingularAttribute<Operations, String> label;
    public static volatile SingularAttribute<Operations, String> adresseMac;
    public static volatile SingularAttribute<Operations, String> adresseIp;
    public static volatile SingularAttribute<Operations, String> etat;
    public static volatile SingularAttribute<Operations, Date> dateOpration;

}