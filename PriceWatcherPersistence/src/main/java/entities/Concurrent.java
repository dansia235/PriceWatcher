/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author LucienFOTSA
 */
@Entity
@Table(name = "concurrent")

@NamedQueries({
    @NamedQuery(name = "Concurrent.nextId", query = "SELECT MAX(c.idconcurrent) FROM Concurrent c"),
    //-------------------------------------------------------------------------------------------------------
    @NamedQuery(name = "Concurrent.findAll", query = "SELECT c FROM Concurrent c"),
    @NamedQuery(name = "Concurrent.findByIdconcurrent", query = "SELECT c FROM Concurrent c WHERE c.idconcurrent = :idconcurrent"),
    //-------------------------------------------------------------------------------------------------------
    @NamedQuery(name = "Concurrent.findAll_Etat", query = "SELECT c FROM Concurrent c WHERE c.idagence IN :agenceList AND UPPER(c.etat) LIKE(:etat) ORDER BY c.idagence, c.lien, c.libelle, c.label"),
    @NamedQuery(name = "Concurrent.findByIdconcurrent_Etat", query = "SELECT c FROM Concurrent c WHERE c.idagence IN :agenceList AND UPPER(c.etat) LIKE(:etat) AND c.idconcurrent = :idconcurrent"),
    @NamedQuery(name = "Concurrent.findByLien_Etat", query = "SELECT c FROM Concurrent c WHERE c.idagence IN :agenceList AND UPPER(c.etat) LIKE(:etat) AND UPPER(c.lien) LIKE(:lien) ORDER BY c.idagence, c.lien, c.libelle, c.label"),
    @NamedQuery(name = "Concurrent.findByLibelle_Etat", query = "SELECT c FROM Concurrent c WHERE c.idagence IN :agenceList AND UPPER(c.etat) LIKE(:etat) AND UPPER(c.libelle) LIKE(:libelle) ORDER BY c.idagence, c.lien, c.libelle, c.label"),
    @NamedQuery(name = "Concurrent.findByLabel_Etat", query = "SELECT c FROM Concurrent c WHERE c.idagence IN :agenceList AND UPPER(c.etat) LIKE(:etat) AND UPPER(c.label) LIKE(:label) ORDER BY c.idagence, c.lien, c.libelle, c.label"),
    @NamedQuery(name = "Concurrent.findByDescription_Etat", query = "SELECT c FROM Concurrent c WHERE c.idagence IN :agenceList AND UPPER(c.etat) LIKE(:etat) AND UPPER(c.description) LIKE(:description) ORDER BY c.idagence, c.lien, c.libelle, c.label"),
    @NamedQuery(name = "Concurrent.findByDescriptionen_Etat", query = "SELECT c FROM Concurrent c WHERE c.idagence IN :agenceList AND UPPER(c.etat) LIKE(:etat) AND UPPER(c.descriptionen) LIKE(:descriptionen) ORDER BY c.idagence, c.lien, c.libelle, c.label"),
    @NamedQuery(name = "Concurrent.findByDefaut_Etat", query = "SELECT c FROM Concurrent c WHERE c.idagence IN :agenceList AND UPPER(c.etat) LIKE(:etat) AND c.defaut = :defaut ORDER BY c.idagence, c.lien, c.libelle, c.label"),
    @NamedQuery(name = "Concurrent.findByDateEnregistre_Etat", query = "SELECT c FROM Concurrent c WHERE c.idagence IN :agenceList AND UPPER(c.etat) LIKE(:etat) AND c.dateEnregistre BETWEEN :dateDebut AND :dateFin ORDER BY c.idagence, c.lien, c.libelle, c.label"),
    @NamedQuery(name = "Concurrent.findByDerniereModif_Etat", query = "SELECT c FROM Concurrent c WHERE c.idagence IN :agenceList AND UPPER(c.etat) LIKE(:etat) AND c.derniereModif BETWEEN :dateDebut AND :dateFin ORDER BY c.idagence, c.lien, c.libelle, c.label")})
public class Concurrent implements Serializable, Comparator<Concurrent> {

    @Size(max = 1024)
    @Column(name = "lien")
    private String lien;
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
    @Size(max = 50)
    @Column(name = "etat")
    private String etat;

    @Transient
    private String sortField;
    @Transient
    private Integer sortOrder;
    @Transient
    private String libelleLabel;
    @Transient
    private Integer totalVersement;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idconcurrent")
    private Integer idconcurrent;
    @Column(name = "defaut")
    private Boolean defaut;
    @Column(name = "date_enregistre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEnregistre;
    @Column(name = "derniere_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date derniereModif;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "concurrent")
    private Collection<Veilleconcurrent> veilleconcurrentCollection;
    @JoinColumn(name = "idagence", referencedColumnName = "idagence")
    @ManyToOne
    private Agence idagence;

    public Concurrent() {
    }

    public Concurrent(String sortField, Integer sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }

    public Concurrent(Integer idconcurrent) {
        this.idconcurrent = idconcurrent;
    }

    public Integer getIdconcurrent() {
        return idconcurrent;
    }

    public void setIdconcurrent(Integer idconcurrent) {
        this.idconcurrent = idconcurrent;
    }

    public Boolean getDefaut() {
        return defaut;
    }

    public void setDefaut(Boolean defaut) {
        this.defaut = defaut;
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

    public Collection<Veilleconcurrent> getVeilleconcurrentCollection() {
        return veilleconcurrentCollection;
    }

    public void setVeilleconcurrentCollection(Collection<Veilleconcurrent> veilleconcurrentCollection) {
        this.veilleconcurrentCollection = veilleconcurrentCollection;
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
        hash += (idconcurrent != null ? idconcurrent.hashCode() : 0);
        return hash;
    }

    @Override
    public int compare(Concurrent cptoir1, Concurrent cptoir2) {
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
        if (!(object instanceof Concurrent)) {
            return false;
        }
        Concurrent other = (Concurrent) object;
        if ((this.idconcurrent == null && other.idconcurrent != null) || (this.idconcurrent != null && !this.idconcurrent.equals(other.idconcurrent))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Concurrent[ idconcurrent=" + idconcurrent + " ]";
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
        return libelleLabel;
    }

    public void setLibelleLabel(String libelleLabel) {
        this.libelleLabel = libelleLabel;
    }

    public Integer getTotalVersement() {
        return totalVersement;
    }

    public void setTotalVersement(Integer totalVersement) {
        this.totalVersement = totalVersement;
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

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getLien() {
        return lien;
    }

    public void setLien(String lien) {
        this.lien = lien;
    }

}
