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
@Table(name = "menu")

@NamedQueries({
    @NamedQuery(name = "Menu.nextId", query = "SELECT MAX(m.idMenu) FROM Menu m"),
    //-------------------------------------------------------------------------------------------------------
    @NamedQuery(name = "Menu.findAll", query = "SELECT m FROM Menu m ORDER BY m.libelle"),
    @NamedQuery(name = "Menu.findByIdMenu", query = "SELECT m FROM Menu m WHERE m.idMenu = :idMenu"),
    //-------------------------------------------------------------------------------------------------------
    @NamedQuery(name = "Menu.findAll_Etat", query = "SELECT m FROM Menu m WHERE UPPER(m.etat) LIKE(:etat) ORDER BY m.libelle"),
    @NamedQuery(name = "Menu.findByIdMenu_Etat", query = "SELECT m FROM Menu m WHERE UPPER(m.etat) LIKE(:etat) AND m.idMenu = :idMenu"),
    @NamedQuery(name = "Menu.findByIdMenuParent_Etat", query = "SELECT m FROM Menu m WHERE UPPER(m.etat) LIKE(:etat) AND m.menIdMenu.idMenu = :idMenu ORDER BY m.libelle"),
    @NamedQuery(name = "Menu.findByIdMenuParentCategorie_Etat", query = "SELECT m FROM Menu m WHERE UPPER(m.etat) LIKE(:etat) AND UPPER(m.categorie) LIKE(:categorie) AND m.menIdMenu.idMenu = :idMenu ORDER BY m.libelle"),
    @NamedQuery(name = "Menu.findByLabel_Etat", query = "SELECT m FROM Menu m WHERE UPPER(m.etat) LIKE(:etat) AND UPPER(m.label) LIKE(:label) ORDER BY m.libelle"),
    @NamedQuery(name = "Menu.findByLibelle_Etat", query = "SELECT m FROM Menu m WHERE UPPER(m.etat) LIKE(:etat) AND UPPER(m.libelle) LIKE(:libelle) ORDER BY m.libelle"),
    @NamedQuery(name = "Menu.findByShortcut_Etat", query = "SELECT m FROM Menu m WHERE UPPER(m.etat) LIKE(:etat) AND UPPER(m.shortcut) LIKE(:shortcut) ORDER BY m.libelle"),
    @NamedQuery(name = "Menu.findByCategorie_Etat", query = "SELECT m FROM Menu m WHERE UPPER(m.etat) LIKE(:etat) AND UPPER(m.categorie) LIKE(:categorie) ORDER BY m.libelle"),
    @NamedQuery(name = "Menu.findByDateEnregistre_Etat", query = "SELECT m FROM Menu m WHERE UPPER(m.etat) LIKE(:etat) AND m.dateEnregistre BETWEEN :dateDebut AND :dateFin ORDER BY m.libelle"),
    @NamedQuery(name = "Menu.findByDerniereModif_Etat", query = "SELECT m FROM Menu m WHERE UPPER(m.etat) LIKE(:etat) AND m.derniereModif BETWEEN :dateDebut AND :dateFin ORDER BY m.libelle")
})
public class Menu implements Serializable, Comparator<Menu> {

    @Transient
    private String sortField;
    @Transient
    private Integer sortOrder;
    @Transient
    private String libelleLabel;
    @Transient
    private Integer totalSousmenu;

    @Column(name = "accueil")
    private Boolean accueil;
    @Size(max = 1024)
    @Column(name = "rubrique")
    private String rubrique;

    private static final long serialVersionUID = 1L;
    @Id

    @Basic(optional = false)
    @Column(name = "id_menu")
    private Integer idMenu;
    @Size(max = 1024)
    @Column(name = "label")
    private String label;
    @Size(max = 1024)
    @Column(name = "libelle")
    private String libelle;
    @Size(max = 1024)
    @Column(name = "shortcut")
    private String shortcut;
    @Size(max = 1024)
    @Column(name = "categorie")
    private String categorie;
    @Size(max = 50)
    @Column(name = "etat")
    private String etat;
    @Column(name = "date_enregistre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEnregistre;
    @Column(name = "derniere_modif")
    @Temporal(TemporalType.TIMESTAMP)
    private Date derniereModif;
    @OneToMany(mappedBy = "menIdMenu")
    private Collection<Menu> menuCollection;
    @JoinColumn(name = "men_id_menu", referencedColumnName = "id_menu")
    @ManyToOne
    private Menu menIdMenu;
    @Size(max = 1024)
    @Column(name = "lien")
    private String lien;

    public Menu() {
    }

    public Menu(Integer idMenu) {
        this.idMenu = idMenu;
    }

    public Menu(Integer idMenu, String libelle) {
        this.idMenu = idMenu;
        this.libelle = libelle;
    }

    public Menu(String sortField, Integer sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }

    public Integer getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(Integer idMenu) {
        this.idMenu = idMenu;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getShortcut() {
        return shortcut;
    }

    public void setShortcut(String shortcut) {
        this.shortcut = shortcut;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
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
    public Collection<Menu> getMenuCollection() {
        return menuCollection;
    }

    public void setMenuCollection(Collection<Menu> menuCollection) {
        this.menuCollection = menuCollection;
    }

    public Menu getMenIdMenu() {
        return menIdMenu;
    }

    public void setMenIdMenu(Menu menIdMenu) {
        this.menIdMenu = menIdMenu;
    }

    public String getLien() {
        return lien;
    }

    public void setLien(String lien) {
        this.lien = lien;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMenu != null ? idMenu.hashCode() : 0);
        return hash;
    }

    @Override
    public int compare(Menu mnu1, Menu mnu2) {
        try {
            Object value1 = Menu.class.getMethod("get" + this.sortField.substring(0, 1).toUpperCase(Locale.ROOT) + this.sortField.substring(1)).invoke(mnu1);
            Object value2 = Menu.class.getMethod("get" + this.sortField.substring(0, 1).toUpperCase(Locale.ROOT) + this.sortField.substring(1)).invoke(mnu2);
            int value = (value1 != null ? ((Comparable) value1).compareTo(value2) : -1);
            return sortOrder * value;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Menu)) {
            return false;
        }
        Menu other = (Menu) object;
        return !((this.idMenu == null && other.idMenu != null) || (this.idMenu != null && !this.idMenu.equals(other.idMenu)));
    }

    @Override
    public String toString() {
        return "entities.Menu[ idMenu=" + idMenu + " ]";
    }

    public Boolean getAccueil() {
        return accueil;
    }

    public void setAccueil(Boolean accueil) {
        this.accueil = accueil;
    }

    public String getRubrique() {
        return rubrique;
    }

    public void setRubrique(String rubrique) {
        this.rubrique = rubrique;
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

    public Integer getTotalSousmenu() {
        return menuCollection.size();
    }

    public void setTotalSousmenu(Integer totalSousmenu) {
        this.totalSousmenu = totalSousmenu;
    }

}
