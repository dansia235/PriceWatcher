/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

/**
 *
 * @author FOTSA Lucien
 */
@Entity
@Table(name = "article")
@NamedQueries({
    @NamedQuery(name = "Article.nextId", query = "SELECT MAX(a.idarticle) FROM Article a"),
    //-------------------------------------------------------------------------------------------------------
    @NamedQuery(name = "Article.findAll", query = "SELECT a FROM Article a ORDER BY a.libelle"),
    @NamedQuery(name = "Article.findByIdarticle", query = "SELECT a FROM Article a WHERE a.idarticle = :idarticle"),
    //-------------------------------------------------------------------------------------------------------
    @NamedQuery(name = "Article.findAll_Etat", query = "SELECT a FROM Article a WHERE a.idagence IN :agenceList AND UPPER(a.etat) LIKE(:etat) ORDER BY a.libelle"),
    @NamedQuery(name = "Article.findByIdarticle_Etat", query = "SELECT a FROM Article a WHERE a.idagence IN :agenceList AND UPPER(a.etat) LIKE(:etat) AND a.idarticle = :idarticle"),
    @NamedQuery(name = "Article.findByCode_Etat", query = "SELECT a FROM Article a WHERE a.idagence IN :agenceList AND UPPER(a.etat) LIKE(:etat) AND UPPER(a.code) LIKE(:code) ORDER BY a.libelle"),
    @NamedQuery(name = "Article.findByCode_Exclude_Etat", query = "SELECT a FROM Article a WHERE a.idagence IN :agenceList AND UPPER(a.etat) LIKE(:etat) AND UPPER(a.code) LIKE(:code) AND a.idarticle <> :idarticle ORDER BY a.libelle"),
    @NamedQuery(name = "Article.findByLibelle_Etat", query = "SELECT a FROM Article a WHERE a.idagence IN :agenceList AND UPPER(a.etat) LIKE(:etat) AND UPPER(a.libelle) LIKE(:libelle) ORDER BY a.libelle"),
    @NamedQuery(name = "Article.findByLabel_Etat", query = "SELECT a FROM Article a WHERE a.idagence IN :agenceList AND UPPER(a.etat) LIKE(:etat) AND UPPER(a.label) LIKE(:label) ORDER BY a.libelle"),
    @NamedQuery(name = "Article.findByDescription_Etat", query = "SELECT a FROM Article a WHERE a.idagence IN :agenceList AND UPPER(a.etat) LIKE(:etat) AND UPPER(a.description) LIKE(:description) ORDER BY a.libelle"),
    @NamedQuery(name = "Article.findByDescriptionen_Etat", query = "SELECT a FROM Article a WHERE a.idagence IN :agenceList AND UPPER(a.etat) LIKE(:etat) AND UPPER(a.descriptionen) LIKE(:descriptionen) ORDER BY a.libelle"),
    @NamedQuery(name = "Article.findByPoids_Etat", query = "SELECT a FROM Article a WHERE a.idagence IN :agenceList AND UPPER(a.etat) LIKE(:etat) AND a.poids BETWEEN :min AND :max ORDER BY a.libelle"),
    @NamedQuery(name = "Article.findByUnitepoids_Etat", query = "SELECT a FROM Article a WHERE a.idagence IN :agenceList AND UPPER(a.etat) LIKE(:etat) AND UPPER(a.unitepoids) LIKE(:unitepoids) ORDER BY a.libelle"),
    @NamedQuery(name = "Article.findByCoutachatttc_Etat", query = "SELECT a FROM Article a WHERE a.idagence IN :agenceList AND UPPER(a.etat) LIKE(:etat) AND a.coutachatttc BETWEEN :min AND :max ORDER BY a.libelle"),
    @NamedQuery(name = "Article.findByPrixunit_Etat", query = "SELECT a FROM Article a WHERE a.idagence IN :agenceList AND UPPER(a.etat) LIKE(:etat) AND a.prixunit BETWEEN :min AND :max ORDER BY a.libelle"),
    @NamedQuery(name = "Article.findByTva_Etat", query = "SELECT a FROM Article a WHERE a.idagence IN :agenceList AND UPPER(a.etat) LIKE(:etat) AND a.tva = :tva ORDER BY a.libelle"),
    @NamedQuery(name = "Article.findByPrixunitttc_Etat", query = "SELECT a FROM Article a WHERE a.idagence IN :agenceList AND UPPER(a.etat) LIKE(:etat) AND a.prixunitttc BETWEEN :min AND :max ORDER BY a.libelle"),
    @NamedQuery(name = "Article.findByDatefabrication_Etat", query = "SELECT a FROM Article a WHERE a.idagence IN :agenceList AND UPPER(a.etat) LIKE(:etat) AND a.datefabrication BETWEEN :dateDebut AND :dateFin ORDER BY a.libelle"),
    @NamedQuery(name = "Article.findByDateperemption_Etat", query = "SELECT a FROM Article a WHERE a.idagence IN :agenceList AND UPPER(a.etat) LIKE(:etat) AND a.dateperemption BETWEEN :dateDebut AND :dateFin ORDER BY a.libelle"),
    @NamedQuery(name = "Article.findByQuantitestock_Etat", query = "SELECT a FROM Article a WHERE a.idagence IN :agenceList AND UPPER(a.etat) LIKE(:etat) AND a.quantitestock BETWEEN :min AND :max ORDER BY a.libelle"),
    @NamedQuery(name = "Article.findByQuantitemin_Etat", query = "SELECT a FROM Article a WHERE a.idagence IN :agenceList AND UPPER(a.etat) LIKE(:etat) AND a.quantitemin BETWEEN :min AND :max ORDER BY a.libelle"),
    @NamedQuery(name = "Article.findByQuantitealerte_Etat", query = "SELECT a FROM Article a WHERE a.idagence IN :agenceList AND UPPER(a.etat) LIKE(:etat) AND a.quantitealerte BETWEEN :min AND :max ORDER BY a.libelle"),
    @NamedQuery(name = "Article.findByEnvente_Etat", query = "SELECT a FROM Article a WHERE a.idagence IN :agenceList AND UPPER(a.etat) LIKE(:etat) AND a.envente = :envente ORDER BY a.libelle"),
    @NamedQuery(name = "Article.findByPeremption_Etat", query = "SELECT a FROM Article a WHERE a.idagence IN :agenceList AND UPPER(a.etat) LIKE(:etat) AND a.peremption = :peremption ORDER BY a.libelle"),
    @NamedQuery(name = "Article.findByDateEnregistre_Etat", query = "SELECT a FROM Article a WHERE a.idagence IN :agenceList AND UPPER(a.etat) LIKE(:etat) AND a.dateEnregistre BETWEEN :dateDebut AND :dateFin ORDER BY a.libelle"),
    @NamedQuery(name = "Article.findByDerniereModif_Etat", query = "SELECT a FROM Article a WHERE a.idagence IN :agenceList AND UPPER(a.etat) LIKE(:etat) AND a.derniereModif BETWEEN :dateDebut AND :dateFin ORDER BY a.libelle"),
    //*******************************************************************************************************
    @NamedQuery(name = "Article.findAll_Stock_Alerte", query = "SELECT a FROM Article a WHERE a.idagence IN :agenceList AND UPPER(a.etat) LIKE(:etat) AND a.quantitealerte >= a.quantitestock ORDER BY a.libelle"),
    @NamedQuery(name = "Article.findAll_Peremption", query = "SELECT a FROM Article a WHERE a.idagence IN :agenceList AND UPPER(a.etat) LIKE(:etat) AND a.dateperemption < :today AND a.peremption = true ORDER BY a.libelle")})
public class Article implements Serializable, Comparable<Article>, Comparator<Article> {

    @Size(max = 2147483647)
    @Column(name = "code")
    private String code;
    @Size(max = 1024)
    @Column(name = "numlot")
    private String numlot;

    @Size(max = 1024)
    @Column(name = "libelle")
    private String libelle;

    @Size(max = 1024)
    @Column(name = "label")
    private String label;
    @Size(max = 1024)
    @Column(name = "description")
    private String description;
    @Size(max = 1024)
    @Column(name = "descriptionen")
    private String descriptionen;
    @Size(max = 1024)
    @Column(name = "unitepoids")
    private String unitepoids;
    @Size(max = 1024)
    @Column(name = "unite")
    private String unite;
    @Size(max = 1024)
    @Column(name = "photo")
    private String photo;
    @Size(max = 1024)
    @Column(name = "photo_relatif")
    private String photoRelatif;
    @Size(max = 50)
    @Column(name = "etat")
    private String etat;
    @Column(name = "da")
    private Float da;
    @Transient
    private List<String> listReferenceArticle = new ArrayList<>();
    @Column(name = "codemultiple")
    private Boolean codemultiple;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "article")
    private Collection<Veilleconcurrent> veilleconcurrentCollection;

    @Column(name = "nbrejour")
    private Short nbrejour;
    @Column(name = "dateavarie")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateavarie;
    @Column(name = "quantiteavarie")
    private Float quantiteavarie;

    @Transient
    private String sortField;
    @Transient
    private Integer sortOrder;
    @Transient
    private String libelleLabel;
    @Transient
    private Integer totalInventaire;

    @Column(name = "peremption")
    private Boolean peremption;

    @Transient
    private String nomcomplet;
    @Transient
    private Float montant = 0F;
    @Transient
    private Float cout = 0F;
    @Transient
    private Float reduction = 0F;
    @Transient
    private Float reglement = 0F;
    @Transient
    private Float reliquat = 0F;
    @Transient
    private Float benefice = 0F;
    @Transient
    private Float nombre = 0F;
    @Transient
    private Float avarie = 0F;
    @Transient
    private Float coutAvarie = 0F;
    @Transient
    private Float reste = 0F;
    @Transient
    private Float coutReste = 0F;

    @Column(name = "envente")
    private Boolean envente;

    private static final long serialVersionUID = 1L;
    @Id

    @Basic(optional = false)
    @Column(name = "idarticle")
    private Integer idarticle;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "poids")
    private Float poids;
    @Column(name = "coutachatttc")
    private Float coutachatttc;
    @Column(name = "prixunit")
    private Float prixunit;
    @Column(name = "tva")
    private Float tva;
    @Column(name = "prixunitttc")
    private Float prixunitttc;
    @Column(name = "datefabrication")
    @Temporal(TemporalType.DATE)
    private Date datefabrication;
    @Column(name = "dateperemption")
    @Temporal(TemporalType.DATE)
    private Date dateperemption;
    @Column(name = "quantitestock")
    private Float quantitestock;
    @Column(name = "quantitemin")
    private Float quantitemin;
    @Column(name = "quantitealerte")
    private Float quantitealerte;
    @Column(name = "date_enregistre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEnregistre;
    @Column(name = "derniere_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date derniereModif;
    @JoinColumn(name = "idagence", referencedColumnName = "idagence")
    @ManyToOne
    private Agence idagence;

    public Article() {
    }

    public Article(Integer idarticle) {
        this.idarticle = idarticle;
    }

    public Article(String sortField, Integer sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }

    public Integer getIdarticle() {
        return idarticle;
    }

    public void setIdarticle(Integer idarticle) {
        this.idarticle = idarticle;
    }

    public Float getPoids() {
        return poids;
    }

    public void setPoids(Float poids) {
        this.poids = poids;
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

    public Float getTva() {
        return tva;
    }

    public void setTva(Float tva) {
        this.tva = tva;
    }

    public Float getPrixunitttc() {
        return prixunitttc;
    }

    public void setPrixunitttc(Float prixunitttc) {
        this.prixunitttc = prixunitttc;
    }

    public Date getDatefabrication() {
        return datefabrication;
    }

    public void setDatefabrication(Date datefabrication) {
        this.datefabrication = datefabrication;
    }

    public Date getDateperemption() {
        return dateperemption;
    }

    public void setDateperemption(Date dateperemption) {
        this.dateperemption = dateperemption;
    }

    public Float getQuantitestock() {
        return quantitestock;
    }

    public void setQuantitestock(Float quantitestock) {
        this.quantitestock = quantitestock;
    }

    public Float getQuantitemin() {
        return quantitemin;
    }

    public void setQuantitemin(Float quantitemin) {
        this.quantitemin = quantitemin;
    }

    public Float getQuantitealerte() {
        return quantitealerte;
    }

    public void setQuantitealerte(Float quantitealerte) {
        this.quantitealerte = quantitealerte;
    }

    public String getPhotoRelatif() {
        return photoRelatif;
    }

    public void setPhotoRelatif(String photoRelatif) {
        this.photoRelatif = photoRelatif;
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

    public Agence getIdagence() {
        return idagence;
    }

    public void setIdagence(Agence idagence) {
        this.idagence = idagence;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idarticle != null ? idarticle.hashCode() : 0);
        return hash;
    }

    @Override
    public int compareTo(Article a) {
        String libelleLabelthis = ((this.getLibelle() != null ? this.getLibelle() : "") + " " + (this.getLabel() != null ? this.getLabel() : "")).toUpperCase(Locale.ROOT);
        String libelleLabelother = ((a.getLibelle() != null ? a.getLibelle() : "") + " " + (a.getLabel() != null ? a.getLabel() : "")).toUpperCase(Locale.ROOT);
        return libelleLabelthis.compareTo(libelleLabelother);
    }

    @Override
    public int compare(Article article1, Article article2) {
        try {
            int subObj = 0;
            String method = "get" + this.sortField.substring(0, 1).toUpperCase(Locale.ROOT);
            for (int i = 1; i < this.sortField.length(); i++) {
                if (this.sortField.charAt(i) != '.') {
                    method += this.sortField.charAt(i);
                } else {
                    method = "get" + this.sortField.substring(i + 1, i + 2).toUpperCase(Locale.ROOT);
                    subObj++;
                    i++;
                }
            }
            Object value1 = null;
            Object value2 = null;
            switch (subObj) {
                case 1:
                    break;
                default:
                    value1 = Article.class.getMethod(method).invoke(article1);
                    value2 = Article.class.getMethod(method).invoke(article2);
                    break;
            }
            int value = (value1 != null ? ((Comparable) value1).compareTo(value2) : -1);
            return sortOrder * value;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Article)) {
            return false;
        }
        Article other = (Article) object;
        if ((this.idarticle == null && other.idarticle != null) || (this.idarticle != null && !this.idarticle.equals(other.idarticle))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Article[ idarticle=" + idarticle + " ]";
    }

    public Boolean getEnvente() {
        return envente;
    }

    public void setEnvente(Boolean envente) {
        this.envente = envente;
    }

    public Float getCout() {
        return cout;
    }

    public void setCout(Float cout) {
        this.cout = cout;
    }

    public Float getReduction() {
        return reduction;
    }

    public void setReduction(Float reduction) {
        this.reduction = reduction;
    }

    public Float getReglement() {
        return reglement;
    }

    public void setReglement(Float reglement) {
        this.reglement = reglement;
    }

    public Float getReliquat() {
        return reliquat;
    }

    public void setReliquat(Float reliquat) {
        this.reliquat = reliquat;
    }

    public Float getBenefice() {
        return benefice;
    }

    public void setBenefice(Float benefice) {
        this.benefice = benefice;
    }

    public Float getNombre() {
        return nombre;
    }

    public void setNombre(Float nombre) {
        this.nombre = nombre;
    }

    public Float getAvarie() {
        return avarie;
    }

    public void setAvarie(Float avarie) {
        this.avarie = avarie;
    }

    public Float getCoutAvarie() {
        return coutAvarie;
    }

    public void setCoutAvarie(Float coutAvarie) {
        this.coutAvarie = coutAvarie;
    }

    public Float getReste() {
        return reste;
    }

    public void setReste(Float reste) {
        this.reste = reste;
    }

    public Float getCoutReste() {
        return coutReste;
    }

    public void setCoutReste(Float coutReste) {
        this.coutReste = coutReste;
    }

    public Float getMontant() {
        return montant;
    }

    public void setMontant(Float montant) {
        this.montant = montant;
    }

    public String getNomcomplet() {
        return nomcomplet;
    }

    public void setNomcomplet(String nomcomplet) {
        this.nomcomplet = nomcomplet;
    }

    public Boolean getPeremption() {
        return peremption;
    }

    public void setPeremption(Boolean peremption) {
        this.peremption = peremption;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getLibelleLabel() {
        return (libelle != null ? libelle : "") + (label != null ? label : "");
    }

    public void setLibelleLabel(String libelleLabel) {
        this.libelleLabel = libelleLabel;
    }

    public Integer getTotalInventaire() {
        return totalInventaire;
    }

    public void setTotalInventaire(Integer totalInventaire) {
        this.totalInventaire = totalInventaire;
    }

    public Short getNbrejour() {
        return nbrejour;
    }

    public void setNbrejour(Short nbrejour) {
        this.nbrejour = nbrejour;
    }

    public Date getDateavarie() {
        return dateavarie;
    }

    public void setDateavarie(Date dateavarie) {
        this.dateavarie = dateavarie;
    }

    public Float getQuantiteavarie() {
        return quantiteavarie;
    }

    public void setQuantiteavarie(Float quantiteavarie) {
        this.quantiteavarie = quantiteavarie;
    }

    @JsonbTransient
    public Collection<Veilleconcurrent> getVeilleconcurrentCollection() {
        return veilleconcurrentCollection;
    }

    public void setVeilleconcurrentCollection(Collection<Veilleconcurrent> veilleconcurrentCollection) {
        this.veilleconcurrentCollection = veilleconcurrentCollection;
    }

    public Boolean getCodemultiple() {
        return codemultiple;
    }

    public void setCodemultiple(Boolean codemultiple) {
        this.codemultiple = codemultiple;
    }

    public List<String> getListReferenceArticle() {
        return listReferenceArticle;
    }

    public void setListReferenceArticle(List<String> listReferenceArticle) {
        this.listReferenceArticle = listReferenceArticle;
    }

    public Float getDa() {
        return da;
    }

    public void setDa(Float da) {
        this.da = da;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNumlot() {
        return numlot;
    }

    public void setNumlot(String numlot) {
        this.numlot = numlot;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionen() {
        return descriptionen;
    }

    public void setDescriptionen(String descriptionen) {
        this.descriptionen = descriptionen;
    }

    public String getUnitepoids() {
        return unitepoids;
    }

    public void setUnitepoids(String unitepoids) {
        this.unitepoids = unitepoids;
    }

    public String getUnite() {
        return unite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

}
