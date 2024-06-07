package entities;

import entities.Agence;
import entities.Compte;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-06-05T14:14:22")
@StaticMetamodel(GroupeUtilisateur.class)
public class GroupeUtilisateur_ { 

    public static volatile CollectionAttribute<GroupeUtilisateur, Compte> compteCollection;
    public static volatile SingularAttribute<GroupeUtilisateur, Date> dateEnregistre;
    public static volatile SingularAttribute<GroupeUtilisateur, String> libelle;
    public static volatile SingularAttribute<GroupeUtilisateur, Agence> idagence;
    public static volatile SingularAttribute<GroupeUtilisateur, Date> derniereModif;
    public static volatile SingularAttribute<GroupeUtilisateur, String> droits;
    public static volatile SingularAttribute<GroupeUtilisateur, Integer> idGroupeUtilisateur;
    public static volatile SingularAttribute<GroupeUtilisateur, String> label;
    public static volatile SingularAttribute<GroupeUtilisateur, String> etat;

}