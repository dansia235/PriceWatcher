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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
 * @author Lucien FOTSA
 */
@Entity
@Table(name = "multimedia")

@NamedQueries({
    @NamedQuery(name = "Multimedia.nextId", query = "SELECT MAX(m.idmultimedia) FROM Multimedia m"),
    //-----------------------------------------------------------
    @NamedQuery(name = "Multimedia.findAll", query = "SELECT m FROM Multimedia m ORDER BY m.categorie, m.derniereModif DESC"),
    @NamedQuery(name = "Multimedia.findByIdmultimedia", query = "SELECT m FROM Multimedia m WHERE m.idmultimedia = :idmultimedia"),
    //-------------------------
    @NamedQuery(name = "Multimedia.findAll_Etat", query = "SELECT m FROM Multimedia m WHERE m.idagence IN :agenceList AND UPPER(m.etat) LIKE(:etat) ORDER BY m.categorie, m.derniereModif DESC"),
    @NamedQuery(name = "Multimedia.findByIdmultimedia_Etat", query = "SELECT m FROM Multimedia m WHERE m.idagence IN :agenceList AND UPPER(m.etat) LIKE(:etat) AND m.idmultimedia = :idmultimedia"),
    @NamedQuery(name = "Multimedia.findByType_Etat", query = "SELECT m FROM Multimedia m WHERE m.idagence IN :agenceList AND UPPER(m.etat) LIKE(:etat) AND UPPER(m.type) LIKE(:type) ORDER BY m.categorie, m.derniereModif DESC"),
    @NamedQuery(name = "Multimedia.findByCategorie_Etat", query = "SELECT m FROM Multimedia m WHERE m.idagence IN :agenceList AND UPPER(m.etat) LIKE(:etat) AND UPPER(m.categorie) LIKE(:categorie) ORDER BY m.categorie, m.derniereModif DESC"),
    @NamedQuery(name = "Multimedia.findByCategorie_Actif_Etat", query = "SELECT m FROM Multimedia m WHERE m.idagence IN :agenceList AND UPPER(m.etat) LIKE(:etat) AND m.actif = :actif AND UPPER(m.categorie) LIKE(:categorie) ORDER BY m.categorie, m.derniereModif DESC"),
    @NamedQuery(name = "Multimedia.findByLien_Etat", query = "SELECT m FROM Multimedia m WHERE m.idagence IN :agenceList AND UPPER(m.etat) LIKE(:etat) AND UPPER(m.lien) LIKE(:lien) ORDER BY m.categorie, m.derniereModif DESC"),
    @NamedQuery(name = "Multimedia.findByTitre_Etat", query = "SELECT m FROM Multimedia m WHERE m.idagence IN :agenceList AND UPPER(m.etat) LIKE(:etat) AND UPPER(m.titre) LIKE(:titre) ORDER BY m.categorie, m.derniereModif DESC"),
    @NamedQuery(name = "Multimedia.findByCommentaire_Etat", query = "SELECT m FROM Multimedia m WHERE m.idagence IN :agenceList AND UPPER(m.etat) LIKE(:etat) AND UPPER(m.commentaire) LIKE(:commentaire) ORDER BY m.categorie, m.derniereModif DESC"),
    @NamedQuery(name = "Multimedia.findByTitreen_Etat", query = "SELECT m FROM Multimedia m WHERE m.idagence IN :agenceList AND UPPER(m.etat) LIKE(:etat) AND UPPER(m.titreen) LIKE(:titreen) ORDER BY m.categorie, m.derniereModif DESC"),
    @NamedQuery(name = "Multimedia.findByCommentaireen_Etat", query = "SELECT m FROM Multimedia m WHERE m.idagence IN :agenceList AND UPPER(m.etat) LIKE(:etat) AND UPPER(m.commentaireen) LIKE(:commentaireen) ORDER BY m.categorie, m.derniereModif DESC"),
    @NamedQuery(name = "Multimedia.findByTaille_Etat", query = "SELECT m FROM Multimedia m WHERE m.idagence IN :agenceList AND UPPER(m.etat) LIKE(:etat) AND m.taille BETWEEN :min AND :max ORDER BY m.categorie, m.derniereModif DESC"),
    @NamedQuery(name = "Multimedia.findByActif_Etat", query = "SELECT m FROM Multimedia m WHERE m.idagence IN :agenceList AND UPPER(m.etat) LIKE(:etat) AND m.actif = :actif ORDER BY m.categorie, m.derniereModif DESC"),
    @NamedQuery(name = "Multimedia.findByDateEnregistre_Etat", query = "SELECT m FROM Multimedia m WHERE m.idagence IN :agenceList AND UPPER(m.etat) LIKE(:etat) AND m.dateEnregistre BETWEEN :dateDebut AND :dateFin ORDER BY m.categorie, m.derniereModif DESC"),
    @NamedQuery(name = "Multimedia.findByDerniereModif_Etat", query = "SELECT m FROM Multimedia m WHERE m.idagence IN :agenceList AND UPPER(m.etat) LIKE(:etat) AND m.derniereModif BETWEEN :dateDebut AND :dateFin ORDER BY m.categorie, m.derniereModif DESC")})
public class Multimedia implements Serializable, Comparator<Multimedia> {

    @Transient
    private String sortField;
    @Transient
    private Integer sortOrder;

    private static final long serialVersionUID = 1L;
    @JoinColumn(name = "idagence", referencedColumnName = "idagence")
    @ManyToOne
    private Agence idagence;
    @Id

    @Basic(optional = false)
    @Column(name = "idmultimedia")
    private Integer idmultimedia;
    @Size(max = 1024)
    @Column(name = "type")
    private String type;
    @Size(max = 1024)
    @Column(name = "categorie")
    private String categorie;
    @Size(max = 1024)
    @Column(name = "lien")
    private String lien;
    @Size(max = 1024)
    @Column(name = "titre")
    private String titre;
    @Size(max = 1024)
    @Column(name = "commentaire")
    private String commentaire;
    @Size(max = 1024)
    @Column(name = "titreen")
    private String titreen;
    @Size(max = 1024)
    @Column(name = "commentaireen")
    private String commentaireen;
    @Size(max = 1024)
    @Column(name = "chemin")
    private String chemin;
    @Size(max = 1024)
    @Column(name = "cheminrelatif")
    private String cheminrelatif;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "taille")
    private Float taille;
    @Column(name = "actif")
    private Boolean actif;
    @Size(max = 50)
    @Column(name = "etat")
    private String etat;
    @Column(name = "date_enregistre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEnregistre;
    @Column(name = "derniere_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date derniereModif;

    public Multimedia() {
    }

    public Multimedia(Integer idmultimedia) {
        this.idmultimedia = idmultimedia;
    }

    public Multimedia(String sortField, Integer sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }

    public Integer getIdmultimedia() {
        return idmultimedia;
    }

    public void setIdmultimedia(Integer idmultimedia) {
        this.idmultimedia = idmultimedia;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getLien() {
        return lien;
    }

    public void setLien(String lien) {
        this.lien = lien;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getTitreen() {
        return titreen;
    }

    public void setTitreen(String titreen) {
        this.titreen = titreen;
    }

    public String getCommentaireen() {
        return commentaireen;
    }

    public void setCommentaireen(String commentaireen) {
        this.commentaireen = commentaireen;
    }

    public String getChemin() {
        return chemin;
    }

    public void setChemin(String chemin) {
        this.chemin = chemin;
    }

    public String getCheminrelatif() {
        return cheminrelatif;
    }

    public void setCheminrelatif(String cheminrelatif) {
        this.cheminrelatif = cheminrelatif;
    }

    public Float getTaille() {
        return taille;
    }

    public void setTaille(Float taille) {
        this.taille = taille;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idmultimedia != null ? idmultimedia.hashCode() : 0);
        return hash;
    }

    @Override
    public int compare(Multimedia mtm1, Multimedia mtm2) {
        try {
            Object value1 = Multimedia.class.getMethod("get" + this.sortField.substring(0, 1).toUpperCase(Locale.ROOT) + this.sortField.substring(1)).invoke(mtm1);
            Object value2 = Multimedia.class.getMethod("get" + this.sortField.substring(0, 1).toUpperCase(Locale.ROOT) + this.sortField.substring(1)).invoke(mtm2);
            int value = (value1 != null ? ((Comparable) value1).compareTo(value2) : -1);
            return sortOrder * value;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Multimedia)) {
            return false;
        }
        Multimedia other = (Multimedia) object;
        return !((this.idmultimedia == null && other.idmultimedia != null) || (this.idmultimedia != null && !this.idmultimedia.equals(other.idmultimedia)));
    }

    @Override
    public String toString() {
        return "entities.Multimedia[ idmultimedia=" + idmultimedia + " ]";
    }

    public Agence getIdagence() {
        return idagence;
    }

    public void setIdagence(Agence idagence) {
        this.idagence = idagence;
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

}
