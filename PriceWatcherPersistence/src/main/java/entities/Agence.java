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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "agence")
@NamedQueries({
    @NamedQuery(name = "Agence.nextId", query = "SELECT MAX(a.idagence) FROM Agence a"),
    //-----------------------------------------------------------
    @NamedQuery(name = "Agence.findAll", query = "SELECT a FROM Agence a ORDER BY a.raisonsociale,a.libelle,a.label"),
    @NamedQuery(name = "Agence.findByIdagence", query = "SELECT a FROM Agence a WHERE a.idagence = :idagence"),
    @NamedQuery(name = "Agence.findAll_Siege_Etat", query = "SELECT a FROM Agence a WHERE UPPER(a.etat) LIKE(:etat) AND a.ageIdagence IS NULL ORDER BY a.raisonsociale,a.libelle,a.label"),
    @NamedQuery(name = "Agence.findAll_Branche_Etat", query = "SELECT a FROM Agence a WHERE UPPER(a.etat) LIKE(:etat) AND a.ageIdagence IS NOT NULL ORDER BY a.raisonsociale,a.libelle,a.label"),
    @NamedQuery(name = "Agence.findByDomaineOnly_Etat", query = "SELECT a FROM Agence a WHERE UPPER(a.etat) LIKE(:etat) AND UPPER(a.domaine) LIKE(:domaine) ORDER BY a.idagence"),
    //---------------------------------------------------------
    @NamedQuery(name = "Agence.findAll_Etat", query = "SELECT a FROM Agence a WHERE UPPER(a.etat) LIKE(:etat) ORDER BY a.raisonsociale,a.libelle,a.label"),
    @NamedQuery(name = "Agence.findAll_Etat_In", query = "SELECT a FROM Agence a WHERE a IN :agenceList AND UPPER(a.etat) LIKE(:etat) ORDER BY a.raisonsociale,a.libelle,a.label"),
    @NamedQuery(name = "Agence.findByIdagenceOnly", query = "SELECT a FROM Agence a WHERE a.idagence = :idagence"),
    @NamedQuery(name = "Agence.findByIdagence_Etat", query = "SELECT a FROM Agence a WHERE UPPER(a.etat) LIKE(:etat) AND a.idagence = :idagence"),
    @NamedQuery(name = "Agence.findByCode_Etat", query = "SELECT a FROM Agence a WHERE a IN :agenceList AND UPPER(a.etat) LIKE(:etat) AND a.code = :code ORDER BY a.raisonsociale,a.libelle,a.label"),
    @NamedQuery(name = "Agence.findByContact_Etat", query = "SELECT a FROM Agence a WHERE a IN :agenceList AND UPPER(a.etat) LIKE(:etat) AND UPPER(a.contact) LIKE(:contact) ORDER BY a.raisonsociale,a.libelle,a.label"),
    @NamedQuery(name = "Agence.findByFax_Etat", query = "SELECT a FROM Agence a WHERE a IN :agenceList AND UPPER(a.etat) LIKE(:etat) AND UPPER(a.fax) LIKE(:fax) ORDER BY a.raisonsociale,a.libelle,a.label"),
    @NamedQuery(name = "Agence.findByBp_Etat", query = "SELECT a FROM Agence a WHERE a IN :agenceList AND UPPER(a.etat) LIKE(:etat) AND UPPER(a.bp) LIKE(:bp) ORDER BY a.raisonsociale,a.libelle,a.label"),
    @NamedQuery(name = "Agence.findByEmail_Etat", query = "SELECT a FROM Agence a WHERE a IN :agenceList AND UPPER(a.etat) LIKE(:etat) AND UPPER(a.email) LIKE(:email) ORDER BY a.raisonsociale,a.libelle,a.label"),
    @NamedQuery(name = "Agence.findBySiteWeb_Etat", query = "SELECT a FROM Agence a WHERE a IN :agenceList AND UPPER(a.etat) LIKE(:etat) AND UPPER(a.siteWeb) LIKE(:siteWeb) ORDER BY a.raisonsociale,a.libelle,a.label"),
    @NamedQuery(name = "Agence.findByDomaine_Etat", query = "SELECT a FROM Agence a WHERE a IN :agenceList AND UPPER(a.etat) LIKE(:etat) AND UPPER(a.domaine) LIKE(:domaine) ORDER BY a.raisonsociale,a.libelle,a.label"),
    @NamedQuery(name = "Agence.findByLibelle_Etat", query = "SELECT a FROM Agence a WHERE a IN :agenceList AND UPPER(a.etat) LIKE(:etat) AND UPPER(a.libelle) LIKE(:libelle) ORDER BY a.raisonsociale,a.libelle,a.label"),
    @NamedQuery(name = "Agence.findByLabel_Etat", query = "SELECT a FROM Agence a WHERE a IN :agenceList AND UPPER(a.etat) LIKE(:etat) AND UPPER(a.label) LIKE(:label) ORDER BY a.raisonsociale,a.libelle,a.label"),
    @NamedQuery(name = "Agence.findByRaisonsociale_Etat", query = "SELECT a FROM Agence a WHERE a IN :agenceList AND UPPER(a.etat) LIKE(:etat) AND UPPER(a.raisonsociale) LIKE(:raisonsociale) ORDER BY a.raisonsociale,a.libelle,a.label"),
    @NamedQuery(name = "Agence.findByNumcobtribuable_Etat", query = "SELECT a FROM Agence a WHERE a IN :agenceList AND UPPER(a.etat) LIKE(:etat) AND UPPER(a.numcobtribuable) LIKE(:numcobtribuable) ORDER BY a.raisonsociale,a.libelle,a.label"),
    @NamedQuery(name = "Agence.findByCapitalsocial_Etat", query = "SELECT a FROM Agence a WHERE a IN :agenceList AND UPPER(a.etat) LIKE(:etat) AND a.capitalsocial BETWEEN :min AND :max ORDER BY a.raisonsociale,a.libelle,a.label"),
    @NamedQuery(name = "Agence.findByDatecreation_Etat", query = "SELECT a FROM Agence a WHERE a IN :agenceList AND UPPER(a.etat) LIKE(:etat) AND a.datecreation BETWEEN :dateDebut AND :dateFin ORDER BY a.raisonsociale,a.libelle,a.label"),
    @NamedQuery(name = "Agence.findByStatut_Etat", query = "SELECT a FROM Agence a WHERE a IN :agenceList AND UPPER(a.etat) LIKE(:etat) AND UPPER(a.statut) LIKE(:statut) ORDER BY a.raisonsociale,a.libelle,a.label"),
    @NamedQuery(name = "Agence.findByDomaine_Statut_Etat", query = "SELECT a FROM Agence a WHERE a IN :agenceList AND UPPER(a.etat) LIKE(:etat) AND UPPER(a.domaine) LIKE(:domaine) AND UPPER(a.statut) LIKE(:statut) ORDER BY a.raisonsociale,a.libelle,a.label"),
    @NamedQuery(name = "Agence.findBySlogan_Etat", query = "SELECT a FROM Agence a WHERE a IN :agenceList AND UPPER(a.etat) LIKE(:etat) AND UPPER(a.slogan) LIKE(:slogan) ORDER BY a.raisonsociale,a.libelle,a.label"),
    @NamedQuery(name = "Agence.findBySloganeng_Etat", query = "SELECT a FROM Agence a WHERE a IN :agenceList AND UPPER(a.etat) LIKE(:etat) AND UPPER(a.sloganeng) LIKE(:sloganeng) ORDER BY a.raisonsociale,a.libelle,a.label"),
    @NamedQuery(name = "Agence.findByResponsable_Etat", query = "SELECT a FROM Agence a WHERE a IN :agenceList AND UPPER(a.etat) LIKE(:etat) AND UPPER(a.responsable) LIKE(:responsable) ORDER BY a.raisonsociale,a.libelle,a.label"),
    @NamedQuery(name = "Agence.findByDateEnregistre_Etat", query = "SELECT a FROM Agence a WHERE a IN :agenceList AND UPPER(a.etat) LIKE(:etat) AND a.dateEnregistre BETWEEN :dateDebut AND :dateFin ORDER BY a.raisonsociale,a.libelle,a.label"),
    @NamedQuery(name = "Agence.findByDerniereModif_Etat", query = "SELECT a FROM Agence a WHERE a IN :agenceList AND UPPER(a.etat) LIKE(:etat) AND a.derniereModif BETWEEN :dateDebut AND :dateFin ORDER BY a.raisonsociale,a.libelle,a.label")})
public class Agence implements Serializable, Comparable<Agence>, Comparator<Agence> {

    @Size(max = 1024)
    @Column(name = "label")
    private String label;

    @Size(max = 1024)
    @Column(name = "qualite")
    private String qualite;

    @Size(max = 1024)
    @Column(name = "quality")
    private String quality;
    @Size(max = 1024)
    @Column(name = "raisonsociale")
    private String raisonsociale;
    @Size(max = 1024)
    @Column(name = "emails")
    private String emails;
    @Size(max = 1024)
    @Column(name = "contacts")
    private String contacts;
    @Size(max = 1024)
    @Column(name = "numregcom")
    private String numregcom;
    @Size(max = 1024)
    @Column(name = "numcobtribuable")
    private String numcobtribuable;
    @Size(max = 1024)
    @Column(name = "statut")
    private String statut;

    @Size(max = 1024)
    @Column(name = "slogan")
    private String slogan;
    @Size(max = 1024)
    @Column(name = "sloganeng")
    private String sloganeng;

    @Size(max = 1024)
    @Column(name = "logo")
    private String logo;

    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 1024)
    @Column(name = "logorelatif")
    private String logorelatif;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 1024)
    @Column(name = "responsable")
    private String responsable;
    @Size(max = 1024)
    @Column(name = "contact")
    private String contact;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 1024)
    @Column(name = "fax")
    private String fax;
    @Size(max = 1024)
    @Column(name = "bp")
    private String bp;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
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
    @Column(name = "code")
    private String code;
    @Size(max = 1024)
    @Column(name = "libelle")
    private String libelle;
    @Size(max = 50)
    @Column(name = "etat")
    private String etat;

    @Transient
    private List<String> listReferenceArticle = new ArrayList<>();
    @OneToMany(mappedBy = "idagence")
    private Collection<Notifications> notificationsCollection;

    @OneToMany(mappedBy = "idagence")
    private Collection<Concurrent> concurrentCollection;

    @Transient
    private String sortField;
    @Transient
    private Integer sortOrder;
    @Transient
    private String libelleLabel;
    @Transient
    private Integer totalAgence;

    @OneToMany(mappedBy = "ageIdagence")
    private Collection<Agence> agenceCollection;
    @JoinColumn(name = "age_idagence", referencedColumnName = "idagence")
    @ManyToOne
    private Agence ageIdagence;

    @OneToMany(mappedBy = "idagence")
    private Collection<Article> articleCollection;

    private static final long serialVersionUID = 1L;

    @OneToMany(mappedBy = "idagence")
    private Collection<Multimedia> multimediaCollection;

    @Id
    @Basic(optional = false)
    @Column(name = "idagence")
    private Integer idagence;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "capitalsocial")
    private Float capitalsocial;
    @Column(name = "datecreation")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datecreation;
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
    @ManyToMany(mappedBy = "agenceCollection")
    private Collection<Compte> compteCollection;
    @OneToMany(mappedBy = "idagence")
    private Collection<GroupeUtilisateur> groupeUtilisateurCollection;
    @OneToMany(mappedBy = "idagence")
    private Collection<Reglage> reglageCollection;
    @OneToMany(mappedBy = "idagence")
    private Collection<Compte> compteCollection1;

    public Agence() {
    }

    public Agence(Integer idagence) {
        this.idagence = idagence;
    }

    public Agence(String sortField, Integer sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }

    public Integer getIdagence() {
        return idagence;
    }

    public void setIdagence(Integer idagence) {
        this.idagence = idagence;
    }

    public String getSiteWeb() {
        return siteWeb;
    }

    public void setSiteWeb(String siteWeb) {
        this.siteWeb = siteWeb;
    }

    public Float getCapitalsocial() {
        return capitalsocial;
    }

    public void setCapitalsocial(Float capitalsocial) {
        this.capitalsocial = capitalsocial;
    }

    public Date getDatecreation() {
        return datecreation;
    }

    public void setDatecreation(Date datecreation) {
        this.datecreation = datecreation;
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

    @JsonbTransient
    public Collection<GroupeUtilisateur> getGroupeUtilisateurCollection() {
        return groupeUtilisateurCollection;
    }

    public void setGroupeUtilisateurCollection(Collection<GroupeUtilisateur> groupeUtilisateurCollection) {
        this.groupeUtilisateurCollection = groupeUtilisateurCollection;
    }

    @JsonbTransient
    public Collection<Reglage> getReglageCollection() {
        return reglageCollection;
    }

    public void setReglageCollection(Collection<Reglage> reglageCollection) {
        this.reglageCollection = reglageCollection;
    }

    @JsonbTransient
    public Collection<Compte> getCompteCollection1() {
        return compteCollection1;
    }

    public void setCompteCollection1(Collection<Compte> compteCollection1) {
        this.compteCollection1 = compteCollection1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idagence != null ? idagence.hashCode() : 0);
        return hash;
    }

    @Override
    public int compareTo(Agence a) {
        return this.getRaisonsociale().toUpperCase(Locale.ROOT).compareTo(a.getRaisonsociale().toUpperCase(Locale.ROOT));
    }

    @Override
    public int compare(Agence agence1, Agence agence2) {
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
                    //---------
                    break;
                default:
                    value1 = Agence.class.getMethod(method).invoke(agence1);
                    value2 = Agence.class.getMethod(method).invoke(agence2);
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
        if (!(object instanceof Agence)) {
            return false;
        }
        Agence other = (Agence) object;
        return !((this.idagence == null && other.idagence != null) || (this.idagence != null && !this.idagence.equals(other.idagence)));
    }

    @Override
    public String toString() {
        return "entities.Agence[ idagence=" + idagence + " ]";
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

    @JsonbTransient
    public Collection<Multimedia> getMultimediaCollection() {
        return multimediaCollection;
    }

    public void setMultimediaCollection(Collection<Multimedia> multimediaCollection) {
        this.multimediaCollection = multimediaCollection;
    }

    @JsonbTransient
    public Collection<Article> getArticleCollection() {
        return articleCollection;
    }

    public void setArticleCollection(Collection<Article> articleCollection) {
        this.articleCollection = articleCollection;
    }

    @JsonbTransient
    public Collection<Agence> getAgenceCollection() {
        return agenceCollection;
    }

    public void setAgenceCollection(Collection<Agence> agenceCollection) {
        this.agenceCollection = agenceCollection;
    }

    public Agence getAgeIdagence() {
        return ageIdagence;
    }

    public void setAgeIdagence(Agence ageIdagence) {
        this.ageIdagence = ageIdagence;
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

    public Integer getTotalAgence() {
        return agenceCollection.size();
    }

    public void setTotalAgence(Integer totalAgence) {
        this.totalAgence = totalAgence;
    }

    @JsonbTransient
    public Collection<Concurrent> getConcurrentCollection() {
        return concurrentCollection;
    }

    public void setConcurrentCollection(Collection<Concurrent> concurrentCollection) {
        this.concurrentCollection = concurrentCollection;
    }

    @JsonbTransient
    public Collection<Notifications> getNotificationsCollection() {
        return notificationsCollection;
    }

    public void setNotificationsCollection(Collection<Notifications> notificationsCollection) {
        this.notificationsCollection = notificationsCollection;
    }

    @JsonbTransient
    public List<String> getListReferenceArticle() {
        return listReferenceArticle;
    }

    public void setListReferenceArticle(List<String> listReferenceArticle) {
        this.listReferenceArticle = listReferenceArticle;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getQualite() {
        return qualite;
    }

    public void setQualite(String qualite) {
        this.qualite = qualite;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getRaisonsociale() {
        return raisonsociale;
    }

    public void setRaisonsociale(String raisonsociale) {
        this.raisonsociale = raisonsociale;
    }

    public String getEmails() {
        return emails;
    }

    public void setEmails(String emails) {
        this.emails = emails;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getNumregcom() {
        return numregcom;
    }

    public void setNumregcom(String numregcom) {
        this.numregcom = numregcom;
    }

    public String getNumcobtribuable() {
        return numcobtribuable;
    }

    public void setNumcobtribuable(String numcobtribuable) {
        this.numcobtribuable = numcobtribuable;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getSloganeng() {
        return sloganeng;
    }

    public void setSloganeng(String sloganeng) {
        this.sloganeng = sloganeng;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLogorelatif() {
        return logorelatif;
    }

    public void setLogorelatif(String logorelatif) {
        this.logorelatif = logorelatif;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

}
