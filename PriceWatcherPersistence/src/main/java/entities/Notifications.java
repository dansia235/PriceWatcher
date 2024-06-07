/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.text.SimpleDateFormat;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author LucienFOTSA
 */
@Entity
@Table(name = "notifications")

@NamedQueries({
    @NamedQuery(name = "Notifications.nextId", query = "SELECT MAX(n.idnotificationsms) FROM Notifications n"),
    @NamedQuery(name = "Notifications.count_Etat", query = "SELECT COUNT(n.idnotificationsms) FROM Notifications n WHERE n.idagence IN :agenceList AND UPPER(n.etat) LIKE(:etat)"),
    //-----------------------------------------------------------
    @NamedQuery(name = "Notifications.findAll", query = "SELECT n FROM Notifications n ORDER BY n.derniereModif DESC"),
    @NamedQuery(name = "Notifications.findByIdnotificationsms", query = "SELECT n FROM Notifications n WHERE n.idnotificationsms = :idnotificationsms"),
    //-----------------------------------------------------------
    @NamedQuery(name = "Notifications.findAll_Plus_Utilise", query = "SELECT n.idagence, (SELECT COUNT(n1) FROM Notifications n1 WHERE n1.idagence = n.idagence) AS nbreNotif FROM Notifications n WHERE n.idagence IN :agenceList AND n.idagence.etat = 'Actif' GROUP BY n.idagence ORDER BY nbreNotif DESC"),
    @NamedQuery(name = "Notifications.findAll_Etat", query = "SELECT n FROM Notifications n WHERE n.idagence IN :agenceList AND UPPER(n.etat) LIKE(:etat) ORDER BY n.derniereModif DESC"),
    @NamedQuery(name = "Notifications.findByIdnotificationsms_Etat", query = "SELECT n FROM Notifications n WHERE n.idagence IN :agenceList AND UPPER(n.etat) LIKE(:etat) AND n.idnotificationsms = :idnotificationsms"),
    @NamedQuery(name = "Notifications.findByIdCompte_Etat", query = "SELECT n FROM Notifications n WHERE n.idagence IN :agenceList AND UPPER(n.etat) LIKE(:etat) AND n.idCompte.idCompte = :idCompte ORDER BY n.derniereModif DESC"),
    @NamedQuery(name = "Notifications.findByIdCompteCampagne_Etat", query = "SELECT n FROM Notifications n WHERE n.idagence IN :agenceList AND UPPER(n.etat) LIKE(:etat) AND n.idCompte.idCompte = :idCompte AND UPPER(n.campagne) LIKE(:campagne) ORDER BY n.derniereModif DESC"),
    @NamedQuery(name = "Notifications.findByIdCompteNbrevuDernierevu_Etat", query = "SELECT n FROM Notifications n WHERE n.idagence IN :agenceList AND UPPER(n.etat) LIKE(:etat) AND n.idCompte.idCompte = :idCompte AND n.nbrevu < n.nbrerepeter AND n.dernierevu < :today ORDER BY n.derniereModif DESC"),
    @NamedQuery(name = "Notifications.findByIdCompteCampagne_DateEnregistre_Etat", query = "SELECT n FROM Notifications n WHERE n.idagence IN :agenceList AND UPPER(n.etat) LIKE(:etat) AND n.idCompte.idCompte = :idCompte AND UPPER(n.campagne) LIKE(:campagne) AND n.dateEnregistre BETWEEN :dateDebut AND :dateFin ORDER BY n.derniereModif DESC"),
    @NamedQuery(name = "Notifications.findByIdCompte_DateEnregistre_Etat", query = "SELECT n FROM Notifications n WHERE n.idagence IN :agenceList AND UPPER(n.etat) LIKE(:etat) AND n.idCompte.idCompte = :idCompte AND n.dateEnregistre BETWEEN :dateDebut AND :dateFin ORDER BY n.derniereModif DESC"),
    //-------------------------
    @NamedQuery(name = "Notifications.findByCampagne_Vu_Etat", query = "SELECT n FROM Notifications n WHERE n.idagence IN :agenceList AND UPPER(n.etat) LIKE(:etat) AND UPPER(n.campagne) LIKE(:campagne) AND n.vu = :vu ORDER BY n.derniereModif DESC"),
    @NamedQuery(name = "Notifications.findByCampagne_Etat", query = "SELECT n FROM Notifications n WHERE n.idagence IN :agenceList AND UPPER(n.etat) LIKE(:etat) AND UPPER(n.campagne) LIKE(:campagne) ORDER BY n.derniereModif DESC"),
    @NamedQuery(name = "Notifications.findByVu_Etat", query = "SELECT n FROM Notifications n WHERE n.idagence IN :agenceList AND UPPER(n.etat) LIKE(:etat) AND n.vu = :vu ORDER BY n.derniereModif DESC"),
    @NamedQuery(name = "Notifications.findByDernierevu_Etat", query = "SELECT n FROM Notifications n WHERE n.idagence IN :agenceList AND UPPER(n.etat) LIKE(:etat) AND n.dernierevu BETWEEN :dateDebut AND :dateFin ORDER BY n.derniereModif DESC"),
    @NamedQuery(name = "Notifications.findByMessage_Etat", query = "SELECT n FROM Notifications n WHERE n.idagence IN :agenceList AND UPPER(n.etat) LIKE(:etat) AND UPPER(n.message) LIKE(:message) ORDER BY n.derniereModif DESC"),
    @NamedQuery(name = "Notifications.findByMessageen_Etat", query = "SELECT n FROM Notifications n WHERE n.idagence IN :agenceList AND UPPER(n.etat) LIKE(:etat) AND UPPER(n.messageen) LIKE(:messageen) ORDER BY n.derniereModif DESC"),
    @NamedQuery(name = "Notifications.findByDelivre_Etat", query = "SELECT n FROM Notifications n WHERE n.idagence IN :agenceList AND UPPER(n.etat) LIKE(:etat) AND n.delivre = :delivre ORDER BY n.derniereModif DESC"),
    @NamedQuery(name = "Notifications.findByLibre_Etat", query = "SELECT n FROM Notifications n WHERE n.idagence IN :agenceList AND UPPER(n.etat) LIKE(:etat) AND n.libre = :libre ORDER BY n.derniereModif DESC"),
    @NamedQuery(name = "Notifications.findByContact_Etat", query = "SELECT n FROM Notifications n WHERE n.idagence IN :agenceList AND UPPER(n.etat) LIKE(:etat) AND UPPER(n.contact) LIKE(:contact) ORDER BY n.derniereModif DESC"),
    @NamedQuery(name = "Notifications.findByEmail_Etat", query = "SELECT n FROM Notifications n WHERE n.idagence IN :agenceList AND UPPER(n.etat) LIKE(:etat) AND UPPER(n.email) LIKE(:email) ORDER BY n.derniereModif DESC"),
    @NamedQuery(name = "Notifications.findByDateEnregistre_Etat", query = "SELECT n FROM Notifications n WHERE n.idagence IN :agenceList AND UPPER(n.etat) LIKE(:etat) AND n.dateEnregistre BETWEEN :dateDebut AND :dateFin ORDER BY n.derniereModif DESC"),
    @NamedQuery(name = "Notifications.findByDerniereModif_Etat", query = "SELECT n FROM Notifications n WHERE n.idagence IN :agenceList AND UPPER(n.etat) LIKE(:etat) AND n.derniereModif BETWEEN :dateDebut AND :dateFin ORDER BY n.derniereModif DESC")})
public class Notifications implements Serializable, Comparator<Notifications> {

    @Size(max = 1024)
    @Column(name = "message")
    private String message;
    @Size(max = 1024)
    @Column(name = "messageen")
    private String messageen;
    @Size(max = 1024)
    @Column(name = "contact")
    private String contact;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 1024)
    @Column(name = "email")
    private String email;
    @Size(max = 50)
    @Column(name = "etat")
    private String etat;
    @Column(name = "vu")
    private Boolean vu;
    @Column(name = "nbrevu")
    private Short nbrevu;
    @Column(name = "dernierevu")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dernierevu;
    @Size(max = 1024)
    @Column(name = "campagne")
    private String campagne;
    @Column(name = "nbrerepeter")
    private Short nbrerepeter;

    @Transient
    private String sortField;
    @Transient
    private Integer sortOrder;
    @Transient
    private String libelleLabel;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idnotificationsms")
    private Long idnotificationsms;
    @Column(name = "delivre")
    private Boolean delivre;
    @Column(name = "libre")
    private Boolean libre;
    @Column(name = "date_enregistre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEnregistre;
    @Column(name = "derniere_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date derniereModif;
    @JoinColumn(name = "idagence", referencedColumnName = "idagence")
    @ManyToOne
    private Agence idagence;
    @JoinColumn(name = "id_compte", referencedColumnName = "id_compte")
    @ManyToOne
    private Compte idCompte;

    public Notifications() {
        this.vu = false;
        this.nbrevu = 0;
    }

    public Notifications(Long idnotificationsms) {
        this.idnotificationsms = idnotificationsms;
    }

    public Notifications(String sortField, Integer sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }

    public Long getIdnotificationsms() {
        return idnotificationsms;
    }

    public void setIdnotificationsms(Long idnotificationsms) {
        this.idnotificationsms = idnotificationsms;
    }

    public Boolean getDelivre() {
        return delivre;
    }

    public void setDelivre(Boolean delivre) {
        this.delivre = delivre;
    }

    public Boolean getLibre() {
        return libre;
    }

    public void setLibre(Boolean libre) {
        this.libre = libre;
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

    public Compte getIdCompte() {
        return idCompte;
    }

    public void setIdCompte(Compte idCompte) {
        this.idCompte = idCompte;
    }

    @Override
    public int compare(Notifications notif1, Notifications notif2) {
        try {
            Object value1 = Notifications.class.getMethod("get" + this.sortField.substring(0, 1).toUpperCase(Locale.ROOT) + this.sortField.substring(1)).invoke(notif1);
            Object value2 = Notifications.class.getMethod("get" + this.sortField.substring(0, 1).toUpperCase(Locale.ROOT) + this.sortField.substring(1)).invoke(notif2);
            int value = (value1 != null ? ((Comparable) value1).compareTo(value2) : -1);
            return sortOrder * value;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idnotificationsms != null ? idnotificationsms.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Notifications)) {
            return false;
        }
        Notifications other = (Notifications) object;
        if ((this.idnotificationsms == null && other.idnotificationsms != null) || (this.idnotificationsms != null && !this.idnotificationsms.equals(other.idnotificationsms))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Notifications[ idnotifications=" + idnotificationsms + " ]";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageen() {
        return messageen;
    }

    public void setMessageen(String messageen) {
        this.messageen = messageen;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public Boolean getVu() {
        return vu;
    }

    public void setVu(Boolean vu) {
        this.vu = vu;
    }

    public Short getNbrevu() {
        return nbrevu;
    }

    public void setNbrevu(Short nbrevu) {
        this.nbrevu = nbrevu;
    }

    public Date getDernierevu() {
        return dernierevu;
    }

    public void setDernierevu(Date dernierevu) {
        if (dernierevu != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss SSS");
            try {
                this.dernierevu = sdf.parse(new SimpleDateFormat("dd/MM/yyyy").format(dernierevu) + " 23:59:59 999");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            this.dernierevu = dernierevu;
        }
    }

    public String getCampagne() {
        return campagne;
    }

    public void setCampagne(String campagne) {
        this.campagne = campagne;
    }

    public Short getNbrerepeter() {
        return nbrerepeter;
    }

    public void setNbrerepeter(Short nbrerepeter) {
        this.nbrerepeter = nbrerepeter;
    }

}
