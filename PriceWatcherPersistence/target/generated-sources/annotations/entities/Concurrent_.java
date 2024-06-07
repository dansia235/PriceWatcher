package entities;

import entities.Agence;
import entities.Veilleconcurrent;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-06-05T14:14:22")
@StaticMetamodel(Concurrent.class)
public class Concurrent_ { 

    public static volatile SingularAttribute<Concurrent, Integer> idconcurrent;
    public static volatile SingularAttribute<Concurrent, String> descriptionen;
    public static volatile SingularAttribute<Concurrent, Date> dateEnregistre;
    public static volatile SingularAttribute<Concurrent, String> lien;
    public static volatile SingularAttribute<Concurrent, String> libelle;
    public static volatile SingularAttribute<Concurrent, Agence> idagence;
    public static volatile SingularAttribute<Concurrent, Date> derniereModif;
    public static volatile CollectionAttribute<Concurrent, Veilleconcurrent> veilleconcurrentCollection;
    public static volatile SingularAttribute<Concurrent, Boolean> defaut;
    public static volatile SingularAttribute<Concurrent, String> description;
    public static volatile SingularAttribute<Concurrent, String> label;
    public static volatile SingularAttribute<Concurrent, String> etat;

}