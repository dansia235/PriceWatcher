/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author Lucien FOTSA
 */
@Entity
@Table(name = "reglage")

@NamedQueries({
    @NamedQuery(name = "Reglage.nextId", query = "SELECT MAX(r.idReglage) FROM Reglage r"),
    @NamedQuery(name = "Reglage.findAll", query = "SELECT r FROM Reglage r"),
    @NamedQuery(name = "Reglage.findByIdReglage", query = "SELECT r FROM Reglage r WHERE r.idReglage = :idReglage"),
    @NamedQuery(name = "Reglage.findByIdagence", query = "SELECT r FROM Reglage r WHERE r.idagence.idagence = :idagence")})
public class Reglage implements Serializable {

    @Size(max = 1024)
    @Column(name = "cmcode")
    private String cmcode;
    @Column(name = "ssl")
    private Boolean ssl;
    @Size(max = 1024)
    @Column(name = "other")
    private String other;
    @Column(name = "boole")
    private Boolean boole;
    @Size(max = 1024)
    @Column(name = "chaine")
    private String chaine;

    @Size(max = 1024)
    @Column(name = "essai")
    private String essai;

    @Size(max = 1024)
    @Column(name = "visite")
    private String visite;
    @Size(max = 1024)
    @Column(name = "activite")
    private String activite;

    @Size(max = 1024)
    @Column(name = "theme")
    private String theme;
    @Size(max = 1024)
    @Column(name = "contact")
    private String contact;
    @Size(max = 1024)
    @Column(name = "devise")
    private String devise;
    @Column(name = "durecorbeiljour")
    private Short durecorbeiljour;

    private static final long serialVersionUID = 1L;
    @Id

    @Basic(optional = false)
    @Column(name = "id_reglage")
    private Integer idReglage;
    @JoinColumn(name = "idagence", referencedColumnName = "idagence")
    @ManyToOne
    private Agence idagence;

    public Reglage() {
        this.boole = true;
        this.devise = "XAF";
        this.ssl = true;
        this.visite = "0";
    }

    public Reglage(Integer idReglage) {
        this.idReglage = idReglage;
        this.boole = true;
        this.devise = "XAF";
        this.ssl = true;
        this.visite = "0";
    }

    public Integer getIdReglage() {
        return idReglage;
    }

    public void setIdReglage(Integer idReglage) {
        this.idReglage = idReglage;
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
        hash += (idReglage != null ? idReglage.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reglage)) {
            return false;
        }
        Reglage other = (Reglage) object;
        return !((this.idReglage == null && other.idReglage != null) || (this.idReglage != null && !this.idReglage.equals(other.idReglage)));
    }

    @Override
    public String toString() {
        return "entities.Reglage[ idReglage=" + idReglage + " ]";
    }

    public Short getDurecorbeiljour() {
        return durecorbeiljour;
    }

    public void setDurecorbeiljour(Short durecorbeiljour) {
        this.durecorbeiljour = durecorbeiljour;
    }

    public String getEssai() {
        return essai;
    }

    public void setEssai(String essai) {
        this.essai = essai;
    }

    public String getVisite() {
        return visite;
    }

    public void setVisite(String visite) {
        this.visite = visite;
    }

    public String getActivite() {
        return activite;
    }

    public void setActivite(String activite) {
        this.activite = activite;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDevise() {
        return devise;
    }

    public void setDevise(String devise) {
        this.devise = devise;
    }

    public Boolean getSsl() {
        return ssl;
    }

    public void setSsl(Boolean ssl) {
        this.ssl = ssl;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public Boolean getBoole() {
        return boole;
    }

    public void setBoole(Boolean boole) {
        this.boole = boole;
    }

    public String getChaine() {
        return chaine;
    }

    public void setChaine(String chaine) {
        this.chaine = chaine;
    }

    public String getCmcode() {
        return cmcode;
    }

    public void setCmcode(String cmcode) {
        this.cmcode = cmcode;
    }

}
