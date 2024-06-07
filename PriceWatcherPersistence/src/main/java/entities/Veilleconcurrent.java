/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

/**
 *
 * @author LucienFOTSA
 */
@Entity
@Table(name = "veilleconcurrent")

@NamedQueries({
    @NamedQuery(name = "Veilleconcurrent.deleteByIdconcurrent", query = "DELETE FROM Veilleconcurrent s WHERE s.veilleconcurrentPK.idconcurrent = :idconcurrent"),
    @NamedQuery(name = "Veilleconcurrent.findIdconcurrentByArticle_Etat", query = "SELECT s.concurrent FROM Veilleconcurrent s WHERE s.concurrent.idagence IN :agenceList AND s.veilleconcurrentPK.idarticle = :idarticle AND UPPER(s.concurrent.etat) LIKE(:etat) ORDER BY s.concurrent.idagence, s.concurrent.libelle, s.concurrent.label"),
    @NamedQuery(name = "Veilleconcurrent.findIdconcurrent_Etat", query = "SELECT s FROM Veilleconcurrent s WHERE s.concurrent.idagence IN :agenceList AND s.veilleconcurrentPK.idconcurrent = :idconcurrent AND UPPER(s.article.etat) LIKE(:etat) ORDER BY s.article.libelle, s.article.label"),
    //------------------------------------------------------------------------------------------------------------
    @NamedQuery(name = "Veilleconcurrent.findAll", query = "SELECT s FROM Veilleconcurrent s"),
    @NamedQuery(name = "Veilleconcurrent.findAll_Etat", query = "SELECT s FROM Veilleconcurrent s WHERE UPPER(s.etat) LIKE(:etat) ORDER BY s.veilleconcurrentPK.idarticle"),
    @NamedQuery(name = "Veilleconcurrent.findByIdconcurrent", query = "SELECT s FROM Veilleconcurrent s WHERE s.veilleconcurrentPK.idconcurrent = :idconcurrent"),
    @NamedQuery(name = "Veilleconcurrent.findByIdconcurrent", query = "SELECT s FROM Veilleconcurrent s WHERE s.veilleconcurrentPK.idconcurrent = :idconcurrent"),
    @NamedQuery(name = "Veilleconcurrent.findByIdarticle", query = "SELECT s FROM Veilleconcurrent s WHERE s.veilleconcurrentPK.idarticle = :idarticle"),
    @NamedQuery(name = "Veilleconcurrent.findByCoutachatttc", query = "SELECT s FROM Veilleconcurrent s WHERE s.coutachatttc = :coutachatttc"),
    @NamedQuery(name = "Veilleconcurrent.findByPrixunit", query = "SELECT s FROM Veilleconcurrent s WHERE s.prixunit = :prixunit"),
    @NamedQuery(name = "Veilleconcurrent.findByEtat", query = "SELECT s FROM Veilleconcurrent s WHERE s.etat = :etat"),
    @NamedQuery(name = "Veilleconcurrent.findByDateEnregistre", query = "SELECT s FROM Veilleconcurrent s WHERE s.dateEnregistre = :dateEnregistre"),
    @NamedQuery(name = "Veilleconcurrent.findByIdarticleIdconcurrentDateEnregistre_Etat", query = "SELECT s FROM Veilleconcurrent s WHERE s.veilleconcurrentPK.idarticle = :idarticle AND s.veilleconcurrentPK.idconcurrent = :idconcurrent AND UPPER(s.etat) LIKE(:etat) AND s.dateEnregistre BETWEEN :dateDebut AND :dateFin"),
    @NamedQuery(name = "Veilleconcurrent.findByDateEnregistre_Etat", query = "SELECT s FROM Veilleconcurrent s WHERE UPPER(s.etat) LIKE(:etat) AND s.dateEnregistre BETWEEN :dateDebut AND :dateFin ORDER BY s.veilleconcurrentPK.idarticle"),
    @NamedQuery(name = "Veilleconcurrent.findByDerniereModif", query = "SELECT s FROM Veilleconcurrent s WHERE s.derniereModif = :derniereModif")})
public class Veilleconcurrent implements Serializable, Comparator<Veilleconcurrent> {

    @Size(max = 50)
    @Column(name = "etat")
    private String etat;

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected VeilleconcurrentPK veilleconcurrentPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "coutachatttc")
    private Float coutachatttc;
    @Column(name = "prixunit")
    private Float prixunit;
    @Column(name = "date_enregistre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEnregistre;
    @Column(name = "derniere_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date derniereModif;
    @JoinColumn(name = "idarticle", referencedColumnName = "idarticle", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Article article;
    @JoinColumn(name = "idconcurrent", referencedColumnName = "idconcurrent", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Concurrent concurrent;
    @Transient
    private String sortField;
    @Transient
    private Integer sortOrder;

    public Veilleconcurrent() {
    }

    public Veilleconcurrent(String sortField, Integer sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }

    public Veilleconcurrent(VeilleconcurrentPK veilleconcurrentPK) {
        this.veilleconcurrentPK = veilleconcurrentPK;
    }

    public Veilleconcurrent(int idconcurrent, int idarticle) {
        this.veilleconcurrentPK = new VeilleconcurrentPK(idconcurrent, idarticle);
    }

    public VeilleconcurrentPK getVeilleconcurrentPK() {
        return veilleconcurrentPK;
    }

    public void setVeilleconcurrentPK(VeilleconcurrentPK veilleconcurrentPK) {
        this.veilleconcurrentPK = veilleconcurrentPK;
    }

    public Float getCoutachatttc() {
        return coutachatttc;
    }

    public void setCoutachatttc(Float coutachatttc) {
        this.coutachatttc = coutachatttc;
    }

    public Float getPrixunit() {
        return prixunit;
    }

    public void setPrixunit(Float prixunit) {
        this.prixunit = prixunit;
    }

    public Date getDateEnregistre() {
        return dateEnregistre;
    }

    public void setDateEnregistre(Date dateEnregistre) {
        this.dateEnregistre = dateEnregistre;
    }

    public Date getDerniereModif() {
        return derniereModif;
    }

    public void setDerniereModif(Date derniereModif) {
        this.derniereModif = derniereModif;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Concurrent getConcurrent() {
        return concurrent;
    }

    public void setConcurrent(Concurrent concurrent) {
        this.concurrent = concurrent;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (veilleconcurrentPK != null ? veilleconcurrentPK.hashCode() : 0);
        return hash;
    }

    @Override
    public int compare(Veilleconcurrent cptoir1, Veilleconcurrent cptoir2) {
        try {
            Object value1 = Concurrent.class.getMethod("get" + this.sortField.substring(0, 1).toUpperCase(Locale.ROOT) + this.sortField.substring(1)).invoke(cptoir1);
            Object value2 = Concurrent.class.getMethod("get" + this.sortField.substring(0, 1).toUpperCase(Locale.ROOT) + this.sortField.substring(1)).invoke(cptoir2);
            int value = (value1 != null ? ((Comparable) value1).compareTo(value2) : -1);
            return sortOrder * value;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Veilleconcurrent)) {
            return false;
        }
        Veilleconcurrent other = (Veilleconcurrent) object;
        if ((this.veilleconcurrentPK == null && other.veilleconcurrentPK != null) || (this.veilleconcurrentPK != null && !this.veilleconcurrentPK.equals(other.veilleconcurrentPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Veilleconcurrent[ veilleconcurrentPK=" + veilleconcurrentPK + " ]";
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

}
