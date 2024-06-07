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
import javax.persistence.OneToMany;
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
@Table(name = "compte")

@NamedQueries({
    @NamedQuery(name = "Compte.nextId", query = "SELECT MAX(c.idCompte) FROM Compte c"),
    //-----------------------------------------------------------
    @NamedQuery(name = "Compte.findAll", query = "SELECT c FROM Compte c ORDER BY c.nom, c.prenom"),
    @NamedQuery(name = "Compte.findByIdCompte", query = "SELECT c FROM Compte c WHERE c.idCompte = :idCompte"),
    //------------------------------
    @NamedQuery(name = "Compte.findAll_Plus_Utilise", query = "SELECT c, (SELECT COUNT(o) FROM Operations o WHERE o.idCompte = c) AS nbreOp FROM Compte c WHERE c.idagence IN :agenceList AND c.etat = 'Actif' GROUP BY c ORDER BY nbreOp DESC"),
    @NamedQuery(name = "Compte.findAll_Etat", query = "SELECT c FROM Compte c WHERE c.idagence IN :agenceList AND UPPER(c.etat) LIKE(:etat) ORDER BY c.nom, c.prenom"),
    @NamedQuery(name = "Compte.findByIdCompte_Etat", query = "SELECT c FROM Compte c WHERE c.idagence IN :agenceList AND UPPER(c.etat) LIKE(:etat) AND c.idCompte = :idCompte"),
    @NamedQuery(name = "Compte.findByContact_Etat", query = "SELECT c FROM Compte c WHERE c.idagence IN :agenceList AND UPPER(c.etat) LIKE(:etat) AND UPPER(c.contact) LIKE(:contact) ORDER BY c.nom, c.prenom"),
    @NamedQuery(name = "Compte.findByFax_Etat", query = "SELECT c FROM Compte c WHERE c.idagence IN :agenceList AND UPPER(c.etat) LIKE(:etat) AND UPPER(c.fax) LIKE(:fax) ORDER BY c.nom, c.prenom"),
    @NamedQuery(name = "Compte.findByBp_Etat", query = "SELECT c FROM Compte c WHERE c.idagence IN :agenceList AND UPPER(c.etat) LIKE(:etat) AND UPPER(c.bp) LIKE(:bp) ORDER BY c.nom, c.prenom"),
    @NamedQuery(name = "Compte.findByEmail_Etat", query = "SELECT c FROM Compte c WHERE c.idagence IN :agenceList AND UPPER(c.etat) LIKE(:etat) AND UPPER(c.email) LIKE(:email) ORDER BY c.nom, c.prenom"),
    @NamedQuery(name = "Compte.findBySiteWeb_Etat", query = "SELECT c FROM Compte c WHERE c.idagence IN :agenceList AND UPPER(c.etat) LIKE(:etat) AND UPPER(c.siteWeb) LIKE(:siteWeb) ORDER BY c.nom, c.prenom"),
    @NamedQuery(name = "Compte.findByDomaine_Etat", query = "SELECT c FROM Compte c WHERE c.idagence IN :agenceList AND UPPER(c.etat) LIKE(:etat) AND UPPER(c.domaine) LIKE(:domaine) ORDER BY c.nom, c.prenom"),
    @NamedQuery(name = "Compte.findByNom_Etat", query = "SELECT c FROM Compte c WHERE c.idagence IN :agenceList AND UPPER(c.etat) LIKE(:etat) AND UPPER(c.nom) LIKE(:nom) ORDER BY c.nom, c.prenom"),
    @NamedQuery(name = "Compte.findByPrenom_Etat", query = "SELECT c FROM Compte c WHERE c.idagence IN :agenceList AND UPPER(c.etat) LIKE(:etat) AND UPPER(c.prenom) LIKE(:prenom) ORDER BY c.nom, c.prenom"),
    @NamedQuery(name = "Compte.findByDateNaissance_Etat", query = "SELECT c FROM Compte c WHERE c.idagence IN :agenceList AND UPPER(c.etat) LIKE(:etat) AND c.dateNaissance BETWEEN :dateDebut AND :dateFin ORDER BY c.nom, c.prenom"),
    @NamedQuery(name = "Compte.findBySexe_Etat", query = "SELECT c FROM Compte c WHERE c.idagence IN :agenceList AND UPPER(c.etat) LIKE(:etat) AND UPPER(c.sexe) LIKE(:sexe) ORDER BY c.nom, c.prenom"),
    @NamedQuery(name = "Compte.findByCni_Etat", query = "SELECT c FROM Compte c WHERE c.idagence IN :agenceList AND UPPER(c.etat) LIKE(:etat) AND UPPER(c.cni) LIKE(:cni) ORDER BY c.nom, c.prenom"),
    @NamedQuery(name = "Compte.findByActif_Etat", query = "SELECT c FROM Compte c WHERE c.idagence IN :agenceList AND UPPER(c.etat) LIKE(:etat) AND c.actif = :actif ORDER BY c.nom, c.prenom"),
    @NamedQuery(name = "Compte.findByLogin_Etat", query = "SELECT c FROM Compte c WHERE c.idagence IN :agenceList AND UPPER(c.etat) LIKE(:etat) AND UPPER(c.login) LIKE(:login) ORDER BY c.nom, c.prenom"),
    @NamedQuery(name = "Compte.findByNomPrenom_Etat", query = "SELECT c FROM Compte c WHERE c.idagence IN :agenceList AND UPPER(c.etat) LIKE(:etat) AND UPPER(c.nom) LIKE(:nom) AND UPPER(c.prenom) LIKE(:prenom) ORDER BY c.nom, c.prenom"),
    @NamedQuery(name = "Compte.findByLoginMdp_Etat", query = "SELECT c FROM Compte c WHERE c.idagence IN :agenceList AND UPPER(c.etat) LIKE(:etat) AND UPPER(c.login) LIKE(:login) AND c.mdp = :mdp ORDER BY c.nom, c.prenom"),
    @NamedQuery(name = "Compte.findByConnexion_Etat", query = "SELECT c FROM Compte c WHERE c.idagence IN :agenceList AND UPPER(c.etat) LIKE(:etat) AND UPPER(c.connexion) LIKE(:connexion) ORDER BY c.nom, c.prenom"),
    @NamedQuery(name = "Compte.findByLangue_Etat", query = "SELECT c FROM Compte c WHERE c.idagence IN :agenceList AND UPPER(c.etat) LIKE(:etat) AND UPPER(c.langue) LIKE(:langue) ORDER BY c.nom, c.prenom"),
    @NamedQuery(name = "Compte.findByDateEnregistre_Etat", query = "SELECT c FROM Compte c WHERE c.idagence IN :agenceList AND UPPER(c.etat) LIKE(:etat) AND c.dateEnregistre BETWEEN :dateDebut AND :dateFin ORDER BY c.nom, c.prenom"),
    @NamedQuery(name = "Compte.findByDerniereModif_Etat", query = "SELECT c FROM Compte c WHERE c.idagence IN :agenceList AND UPPER(c.etat) LIKE(:etat) AND c.derniereModif BETWEEN :dateDebut AND :dateFin ORDER BY c.nom, c.prenom")})
public class Compte implements Serializable, Comparable<Compte>, Comparator<Compte> {

    @Size(max = 1024)
    @Column(name = "contact")
    private String contact;

    @Size(max = 1024)
    @Column(name = "fax")
    private String fax;
    @Size(max = 1024)
    @Column(name = "bp")
    private String bp;
    @Size(max = 1024)
    @Column(name = "email")
    private String email;
    @Size(max = 1024)
    @Column(name = "site_web")
    private String siteWeb;
    @Size(max = 1024)
    @Column(name = "domaine")
    private String domaine;
    @Size(max = 1024)
    @Column(name = "nom")
    private String nom;
    @Size(max = 1024)
    @Column(name = "prenom")
    private String prenom;
    @Size(max = 1024)
    @Column(name = "lieunaissance")
    private String lieunaissance;
    @Size(max = 50)
    @Column(name = "sexe")
    private String sexe;
    @Size(max = 1024)
    @Column(name = "cni")
    private String cni;
    @Size(max = 1024)
    @Column(name = "photo")
    private String photo;
    @Size(max = 1024)
    @Column(name = "photo_relatif")
    private String photoRelatif;
    @Size(max = 50)
    @Column(name = "etat")
    private String etat;
    @Size(max = 1024)
    @Column(name = "login")
    private String login;
    @Size(max = 1024)
    @Column(name = "mdp")
    private String mdp;
    @Size(max = 7777)
    @Column(name = "droits")
    private String droits;
    @Size(max = 50)
    @Column(name = "connexion")
    private String connexion;
    @Size(max = 50)
    @Column(name = "langue")
    private String langue;
    @Size(max = 50)
    @Column(name = "note")
    private String note;
    @OneToMany(mappedBy = "idCompte")
    private Collection<Notifications> notificationsCollection;

    @Transient
    private String sortField;
    @Transient
    private Integer sortOrder;
    @Transient
    private Integer totalAgence;

    @Transient
    private Integer nonLu = 0;

    @Column(name = "nevers")
    private Boolean nevers;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_compte")
    private Integer idCompte;
    @Column(name = "date_naissance")
    @Temporal(TemporalType.DATE)
    private Date dateNaissance;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "date_enregistre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEnregistre;
    @Column(name = "derniere_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date derniereModif;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "geolong")
    private Float geolong;
    @Column(name = "geolat")
    private Float geolat;
    @ManyToMany(mappedBy = "compteCollection")
    private Collection<GroupeUtilisateur> groupeUtilisateurCollection;
    @JoinTable(name = "compteagence", joinColumns = {
        @JoinColumn(name = "id_compte", referencedColumnName = "id_compte")}, inverseJoinColumns = {
        @JoinColumn(name = "idagence", referencedColumnName = "idagence")})
    @ManyToMany
    private Collection<Agence> agenceCollection;
    @OneToMany(mappedBy = "idCompte")
    private Collection<Operations> operationsCollection;
    @JoinColumn(name = "idagence", referencedColumnName = "idagence")
    @ManyToOne
    private Agence idagence;
    @Transient
    private String cfm_mdp;
    @Transient
    private String nomPrenom = "";

    public Compte() {
    }

    public Compte(Integer idCompte) {
        this.idCompte = idCompte;
    }

    public Compte(String sortField, Integer sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }

    public Integer getIdCompte() {
        return idCompte;
    }

    public void setIdCompte(Integer idCompte) {
        this.idCompte = idCompte;
    }

    public String getSiteWeb() {
        return siteWeb;
    }

    public void setSiteWeb(String siteWeb) {
        this.siteWeb = siteWeb;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getPhotoRelatif() {
        return photoRelatif;
    }

    public void setPhotoRelatif(String photoRelatif) {
        this.photoRelatif = photoRelatif;
    }

    public Date getDerniereModif() {
        return derniereModif;
    }

    public void setDerniereModif(Date derniereModif) {
        this.derniereModif = derniereModif;
    }

    public Date getDateEnregistre() {
        return dateEnregistre;
    }

    public void setDateEnregistre(Date dateEnregistre) {
        this.dateEnregistre = dateEnregistre;
    }

    @JsonbTransient
    public Collection<GroupeUtilisateur> getGroupeUtilisateurCollection() {
        return groupeUtilisateurCollection;
    }

    public void setGroupeUtilisateurCollection(Collection<GroupeUtilisateur> groupeUtilisateurCollection) {
        this.groupeUtilisateurCollection = groupeUtilisateurCollection;
    }

    @JsonbTransient
    public Collection<Agence> getAgenceCollection() {
        return agenceCollection;
    }

    public void setAgenceCollection(Collection<Agence> agenceCollection) {
        this.agenceCollection = agenceCollection;
    }

    @JsonbTransient
    public Collection<Operations> getOperationsCollection() {
        return operationsCollection;
    }

    public void setOperationsCollection(Collection<Operations> operationsCollection) {
        this.operationsCollection = operationsCollection;
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
        hash += (idCompte != null ? idCompte.hashCode() : 0);
        return hash;
    }

    @Override
    public int compareTo(Compte c) {
        return this.nomPrenom.compareTo(c.nom + " " + c.prenom);
    }

    @Override
    public int compare(Compte compte1, Compte compte2) {
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
                    //-------------------
                    break;
                default:
                    value1 = Compte.class.getMethod(method).invoke(compte1);
                    value2 = Compte.class.getMethod(method).invoke(compte2);
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
        if (!(object instanceof Compte)) {
            return false;
        }
        Compte other = (Compte) object;
        return !((this.idCompte == null && other.idCompte != null) || (this.idCompte != null && !this.idCompte.equals(other.idCompte)));
    }

    @Override
    public String toString() {
        return "entities.Compte[ idCompte=" + idCompte + " ]";
    }

    public String getCfm_mdp() {
        return cfm_mdp;
    }

    public void setCfm_mdp(String cfm_mdp) {
        this.cfm_mdp = cfm_mdp;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public String getNomPrenom() {
        return ((this.nom != null ? this.nom : "") + " " + (this.prenom != null ? this.prenom : "")).trim();
    }

    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
    }

    public Float getGeolong() {
        return geolong;
    }

    public void setGeolong(Float geolong) {
        this.geolong = geolong;
    }

    public Float getGeolat() {
        return geolat;
    }

    public void setGeolat(Float geolat) {
        this.geolat = geolat;
    }

    public Boolean getNevers() {
        return nevers;
    }

    public void setNevers(Boolean nevers) {
        this.nevers = nevers;
    }

    public Integer getNonLu() {
        return nonLu;
    }

    public void setNonLu(Integer nonLu) {
        this.nonLu = nonLu;
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

    public Integer getTotalAgence() {
        return agenceCollection.size();
    }

    public void setTotalAgence(Integer totalAgence) {
        this.totalAgence = totalAgence;
    }

    @JsonbTransient
    public Collection<Notifications> getNotificationsCollection() {
        return notificationsCollection;
    }

    public void setNotificationsCollection(Collection<Notifications> notificationsCollection) {
        this.notificationsCollection = notificationsCollection;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getBp() {
        return bp;
    }

    public void setBp(String bp) {
        this.bp = bp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDomaine() {
        return domaine;
    }

    public void setDomaine(String domaine) {
        this.domaine = domaine;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getLieunaissance() {
        return lieunaissance;
    }

    public void setLieunaissance(String lieunaissance) {
        this.lieunaissance = lieunaissance;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getCni() {
        return cni;
    }

    public void setCni(String cni) {
        this.cni = cni;
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String getDroits() {
        return droits;
    }

    public void setDroits(String droits) {
        this.droits = droits;
    }

    public String getConnexion() {
        return connexion;
    }

    public void setConnexion(String connexion) {
        this.connexion = connexion;
    }

    public String getLangue() {
        return langue;
    }

    public void setLangue(String langue) {
        this.langue = langue;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}
