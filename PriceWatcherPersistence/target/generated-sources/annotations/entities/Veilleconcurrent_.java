package entities;

import entities.Article;
import entities.Concurrent;
import entities.VeilleconcurrentPK;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-06-05T14:14:22")
@StaticMetamodel(Veilleconcurrent.class)
public class Veilleconcurrent_ { 

    public static volatile SingularAttribute<Veilleconcurrent, Date> dateEnregistre;
    public static volatile SingularAttribute<Veilleconcurrent, Date> derniereModif;
    public static volatile SingularAttribute<Veilleconcurrent, Concurrent> concurrent;
    public static volatile SingularAttribute<Veilleconcurrent, VeilleconcurrentPK> veilleconcurrentPK;
    public static volatile SingularAttribute<Veilleconcurrent, Float> coutachatttc;
    public static volatile SingularAttribute<Veilleconcurrent, String> etat;
    public static volatile SingularAttribute<Veilleconcurrent, Float> prixunit;
    public static volatile SingularAttribute<Veilleconcurrent, Article> article;

}