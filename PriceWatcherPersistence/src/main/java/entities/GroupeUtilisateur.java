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
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@Table(name = "groupeutilisateur")

@NamedQueries({
    @NamedQuery(name = "GroupeUtilisateur.nextId", query = "SELECT MAX(g.idGroupeUtilisateur) FROM GroupeUtilisateur g"),
    //-----------------------------------------------------------
    @NamedQuery(name = "GroupeUtilisateur.findAll", query = "SELECT g FROM GroupeUtilisateur g ORDER BY g.libelle, g.label"),
    @NamedQuery(name = "GroupeUtilisateur.findByIdGroupeUtilisateur", query = "SELECT g FROM GroupeUtilisateur g WHERE g.idGroupeUtilisateur = :idGroupeUtilisateur"),
    //----------------------------------
    @NamedQuery(name = "GroupeUtilisateur.findAll_Etat", query = "SELECT g FROM GroupeUtilisateur g WHERE g.idagence IN :agenceList AND UPPER(g.etat) LIKE(:etat) ORDER BY g.libelle, g.label"),
    @NamedQuery(name = "GroupeUtilisateur.findByIdGroupeUtilisateur_Etat", query = "SELECT g FROM GroupeUtilisateur g WHERE g.idagence IN :agenceList AND UPPER(g.etat) LIKE(:etat) AND g.idGroupeUtilisateur = :idGroupeUtilisateur"),
    @NamedQuery(name = "GroupeUtilisateur.findByLibelle_Etat", query = "SELECT g FROM GroupeUtilisateur g WHERE g.idagence IN :agenceList AND UPPER(g.etat) LIKE(:etat) AND UPPER(g.libelle) LIKE(:libelle) ORDER BY g.libelle, g.label"),
    @NamedQuery(name = "GroupeUtilisateur.findByLabel_Etat", query = "SELECT g FROM GroupeUtilisateur g WHERE g.idagence IN :agenceList AND UPPER(g.etat) LIKE(:etat) AND UPPER(g.label) LIKE(:label) ORDER BY g.libelle, g.label"),
    @NamedQuery(name = "GroupeUtilisateur.findByDateEnregistre_Etat", query = "SELECT g FROM GroupeUtilisateur g WHERE g.idagence IN :agenceList AND UPPER(g.etat) LIKE(:etat) AND g.dateEnregistre BETWEEN :dateDebut AND :dateFin ORDER BY g.libelle, g.label"),
    @NamedQuery(name = "GroupeUtilisateur.findByDerniereModif_Etat", query = "SELECT g FROM GroupeUtilisateur g WHERE g.idagence IN :agenceList AND UPPER(g.etat) LIKE(:etat) AND g.derniereModif BETWEEN :dateDebut AND :dateFin ORDER BY g.libelle, g.label")})
public class GroupeUtilisateur implements Serializable, Comparator<GroupeUtilisateur> {

    @Transient
    private String sortField;
    @Transient
    private Integer sortOrder;
    @Transient
    private String libelleLabel;
    @Transient
    private Integer totalCompte;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_groupe_utilisateur")
    private Integer idGroupeUtilisateur;
    @Size(max = 1024)
    @Column(name = "libelle")
    private String libelle;
    @Size(max = 1024)
    @Column(name = "label")
    private String label;
    @Size(max = 7777)
    @Column(name = "droits")
    private String droits;
    @Size(max = 50)
    @Column(name = "etat")
    private String etat;
    @Column(name = "date_enregistre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEnregistre;
    @Column(name = "derniere_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date derniereModif;
    @JoinTable(name = "cpte_grpusers", joinColumns = {
        @JoinColumn(name = "id_groupe_utilisateur", referencedColumnName = "id_groupe_utilisateur")}, inverseJoinColumns = {
        @JoinColumn(name = "id_compte", referencedColumnName = "id_compte")})
    @ManyToMany
    private Collection<Compte> compteCollection;
    @JoinColumn(name = "idagence", referencedColumnName = "idagence")
    @ManyToOne
    private Agence idagence;

    public GroupeUtilisateur() {
    }

    public GroupeUtilisateur(Integer idGroupeUtilisateur) {
        this.idGroupeUtilisateur = idGroupeUtilisateur;
    }

    public GroupeUtilisateur(String sortField, Integer sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }

    public Integer getIdGroupeUtilisateur() {
        return idGroupeUtilisateur;
    }

    public void setIdGroupeUtilisateur(Integer idGroupeUtilisateur) {
        this.idGroupeUtilisateur = idGroupeUtilisateur;
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

    @JsonbTransient
    public Collection<Compte> getCompteCollection() {
        return compteCollection;
    }

    public void setCompteCollection(Collection<Compte> compteCollection) {
        this.compteCollection = compteCollection;
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
        hash += (idGroupeUtilisateur != null ? idGroupeUtilisateur.hashCode() : 0);
        return hash;
    }

    @Override
    public int compare(GroupeUtilisateur grpuser1, GroupeUtilisateur grpuser2) {
        try {
            Object value1 = GroupeUtilisateur.class.getMethod("get" + this.sortField.substring(0, 1).toUpperCase(Locale.ROOT) + this.sortField.substring(1)).invoke(grpuser1);
            Object value2 = GroupeUtilisateur.class.getMethod("get" + this.sortField.substring(0, 1).toUpperCase(Locale.ROOT) + this.sortField.substring(1)).invoke(grpuser2);
            int value = (value1 != null ? ((Comparable) value1).compareTo(value2) : -1);
            return sortOrder * value;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GroupeUtilisateur)) {
            return false;
        }
        GroupeUtilisateur other = (GroupeUtilisateur) object;
        return !((this.idGroupeUtilisateur == null && other.idGroupeUtilisateur != null) || (this.idGroupeUtilisateur != null && !this.idGroupeUtilisateur.equals(other.idGroupeUtilisateur)));
    }

    @Override
    public String toString() {
        return "entities.GroupeUtilisateur[ idGroupeUtilisateur=" + idGroupeUtilisateur + " ]";
    }

    public String getDroits() {
        return droits;
    }

    public void setDroits(String droits) {
        this.droits = droits;
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

    public Integer getTotalCompte() {
        return compteCollection.size();
    }

    public void setTotalCompte(Integer totalCompte) {
        this.totalCompte = totalCompte;
    }

}
