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
@Table(name = "operations")

@NamedQueries({
    @NamedQuery(name = "Operations.nextId", query = "SELECT MAX(o.idOperations) FROM Operations o"),
    //----------------------------------
    @NamedQuery(name = "Operations.findAll", query = "SELECT o FROM Operations o ORDER BY o.idCompte, o.dateOpration DESC"),
    @NamedQuery(name = "Operations.findByIdOperations", query = "SELECT o FROM Operations o WHERE o.idOperations = :idOperations"),
    //----------------------------------
    @NamedQuery(name = "Operations.findAll_Etat", query = "SELECT o FROM Operations o WHERE o.idCompte.idagence IN :agenceList AND UPPER(o.etat) LIKE(:etat) ORDER BY o.idCompte, o.dateOpration DESC"),
    @NamedQuery(name = "Operations.findByIdOperations_Etat", query = "SELECT o FROM Operations o WHERE o.idCompte.idagence IN :agenceList AND UPPER(o.etat) LIKE(:etat) AND o.idOperations = :idOperations"),
    @NamedQuery(name = "Operations.findByIdCompte_Etat", query = "SELECT o FROM Operations o WHERE o.idCompte.idagence IN :agenceList AND UPPER(o.etat) LIKE(:etat) AND o.idCompte.idCompte = :idCompte ORDER BY o.idCompte, o.dateOpration DESC"),
    @NamedQuery(name = "Operations.findByLibelle_Etat", query = "SELECT o FROM Operations o WHERE o.idCompte.idagence IN :agenceList AND UPPER(o.etat) LIKE(:etat) AND UPPER(o.libelle) LIKE(:libelle) ORDER BY o.idCompte, o.dateOpration DESC"),
    @NamedQuery(name = "Operations.findByLabel_Etat", query = "SELECT o FROM Operations o WHERE o.idCompte.idagence IN :agenceList AND UPPER(o.etat) LIKE(:etat) AND UPPER(o.label) LIKE(:label) ORDER BY o.idCompte, o.dateOpration DESC"),
    @NamedQuery(name = "Operations.findByCible_Etat", query = "SELECT o FROM Operations o WHERE o.idCompte.idagence IN :agenceList AND UPPER(o.etat) LIKE(:etat) AND UPPER(o.cible) LIKE(:cible) ORDER BY o.idCompte, o.dateOpration DESC"),
    @NamedQuery(name = "Operations.findByAdresseIp_Etat", query = "SELECT o FROM Operations o WHERE o.idCompte.idagence IN :agenceList AND UPPER(o.etat) LIKE(:etat) AND UPPER(o.adresseIp) LIKE(:adresseIp) ORDER BY o.idCompte, o.dateOpration DESC"),
    @NamedQuery(name = "Operations.findByAdresseMac_Etat", query = "SELECT o FROM Operations o WHERE o.idCompte.idagence IN :agenceList AND UPPER(o.etat) LIKE(:etat) AND UPPER(o.adresseMac) LIKE(:adresseMac) ORDER BY o.idCompte, o.dateOpration DESC"),
    @NamedQuery(name = "Operations.findByAdressenom_Etat", query = "SELECT o FROM Operations o WHERE o.idCompte.idagence IN :agenceList AND UPPER(o.etat) LIKE(:etat) AND UPPER(o.adressenom) LIKE(:adressenom) ORDER BY o.idCompte, o.dateOpration DESC"),
    @NamedQuery(name = "Operations.findByDateOpration_Etat", query = "SELECT o FROM Operations o WHERE o.idCompte.idagence IN :agenceList AND UPPER(o.etat) LIKE(:etat) AND o.dateOpration BETWEEN :dateDebut AND :dateFin ORDER BY o.idCompte, o.dateOpration DESC")})
public class Operations implements Serializable, Comparator<Operations> {

    @Transient
    private String sortField;
    @Transient
    private Integer sortOrder;
    @Transient
    private String libelleLabel;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_operations")
    private Long idOperations;
    @Size(max = 1024)
    @Column(name = "libelle")
    private String libelle;
    @Size(max = 1024)
    @Column(name = "label")
    private String label;
    @Size(max = 1024)
    @Column(name = "cible")
    private String cible;
    @Size(max = 1024)
    @Column(name = "adresse_ip")
    private String adresseIp;
    @Size(max = 1024)
    @Column(name = "adresse_mac")
    private String adresseMac;
    @Size(max = 1024)
    @Column(name = "adressenom")
    private String adressenom;
    @Size(max = 50)
    @Column(name = "etat")
    private String etat;
    @Column(name = "date_opration")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOpration;
    @JoinColumn(name = "id_compte", referencedColumnName = "id_compte")
    @ManyToOne(optional = false)
    private Compte idCompte;

    public Operations() {
    }

    public Operations(Long idOperations) {
        this.idOperations = idOperations;
    }

    public Operations(String sortField, Integer sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }

    public Long getIdOperations() {
        return idOperations;
    }

    public void setIdOperations(Long idOperations) {
        this.idOperations = idOperations;
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

    public String getCible() {
        return cible;
    }

    public void setCible(String cible) {
        this.cible = cible;
    }

    public String getAdresseIp() {
        return adresseIp;
    }

    public void setAdresseIp(String adresseIp) {
        this.adresseIp = adresseIp;
    }

    public String getAdresseMac() {
        return adresseMac;
    }

    public void setAdresseMac(String adresseMac) {
        this.adresseMac = adresseMac;
    }

    public String getAdressenom() {
        return adressenom;
    }

    public void setAdressenom(String adressenom) {
        this.adressenom = adressenom;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public Date getDateOpration() {
        return dateOpration;
    }

    public void setDateOpration(Date dateOpration) {
        this.dateOpration = dateOpration;
    }

    public Compte getIdCompte() {
        return idCompte;
    }

    public void setIdCompte(Compte idCompte) {
        this.idCompte = idCompte;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idOperations != null ? idOperations.hashCode() : 0);
        return hash;
    }

    @Override
    public int compare(Operations operations1, Operations operations2) {
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
                    if (this.sortField.contains("Compte")) {
                        value1 = Compte.class.getMethod(method).invoke(operations1.idCompte);
                        value2 = Compte.class.getMethod(method).invoke(operations2.idCompte);
                    }
                    break;
                default:
                    value1 = Operations.class.getMethod(method).invoke(operations1);
                    value2 = Operations.class.getMethod(method).invoke(operations2);
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
        if (!(object instanceof Operations)) {
            return false;
        }
        Operations other = (Operations) object;
        return !((this.idOperations == null && other.idOperations != null) || (this.idOperations != null && !this.idOperations.equals(other.idOperations)));
    }

    @Override
    public String toString() {
        return "entities.Operations[ idOperations=" + idOperations + " ]";
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

}
