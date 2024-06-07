package entities;

import entities.Agence;
import entities.Veilleconcurrent;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-06-05T14:14:22")
@StaticMetamodel(Article.class)
public class Article_ { 

    public static volatile SingularAttribute<Article, String> descriptionen;
    public static volatile SingularAttribute<Article, Boolean> peremption;
    public static volatile SingularAttribute<Article, String> photoRelatif;
    public static volatile SingularAttribute<Article, String> code;
    public static volatile SingularAttribute<Article, Float> prixunitttc;
    public static volatile SingularAttribute<Article, String> unite;
    public static volatile SingularAttribute<Article, String> unitepoids;
    public static volatile SingularAttribute<Article, String> libelle;
    public static volatile SingularAttribute<Article, Float> poids;
    public static volatile SingularAttribute<Article, Date> datefabrication;
    public static volatile SingularAttribute<Article, String> description;
    public static volatile SingularAttribute<Article, String> etat;
    public static volatile SingularAttribute<Article, String> numlot;
    public static volatile SingularAttribute<Article, Date> dateperemption;
    public static volatile SingularAttribute<Article, Date> derniereModif;
    public static volatile SingularAttribute<Article, Float> coutachatttc;
    public static volatile SingularAttribute<Article, Float> tva;
    public static volatile SingularAttribute<Article, Date> dateavarie;
    public static volatile SingularAttribute<Article, Short> nbrejour;
    public static volatile SingularAttribute<Article, Integer> idarticle;
    public static volatile SingularAttribute<Article, Agence> idagence;
    public static volatile SingularAttribute<Article, String> photo;
    public static volatile SingularAttribute<Article, String> label;
    public static volatile SingularAttribute<Article, Float> quantitestock;
    public static volatile SingularAttribute<Article, Date> dateEnregistre;
    public static volatile CollectionAttribute<Article, Veilleconcurrent> veilleconcurrentCollection;
    public static volatile SingularAttribute<Article, Boolean> codemultiple;
    public static volatile SingularAttribute<Article, Float> quantitealerte;
    public static volatile SingularAttribute<Article, Float> quantiteavarie;
    public static volatile SingularAttribute<Article, Float> prixunit;
    public static volatile SingularAttribute<Article, Float> da;
    public static volatile SingularAttribute<Article, Float> quantitemin;
    public static volatile SingularAttribute<Article, Boolean> envente;

}