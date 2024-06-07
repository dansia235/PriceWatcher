package entities;

import entities.Menu;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-06-05T14:14:22")
@StaticMetamodel(Menu.class)
public class Menu_ { 

    public static volatile SingularAttribute<Menu, Boolean> accueil;
    public static volatile SingularAttribute<Menu, String> categorie;
    public static volatile SingularAttribute<Menu, String> libelle;
    public static volatile SingularAttribute<Menu, Integer> idMenu;
    public static volatile SingularAttribute<Menu, String> label;
    public static volatile SingularAttribute<Menu, String> etat;
    public static volatile SingularAttribute<Menu, Menu> menIdMenu;
    public static volatile SingularAttribute<Menu, Date> dateEnregistre;
    public static volatile SingularAttribute<Menu, String> shortcut;
    public static volatile SingularAttribute<Menu, String> lien;
    public static volatile SingularAttribute<Menu, Date> derniereModif;
    public static volatile SingularAttribute<Menu, String> rubrique;
    public static volatile CollectionAttribute<Menu, Menu> menuCollection;

}