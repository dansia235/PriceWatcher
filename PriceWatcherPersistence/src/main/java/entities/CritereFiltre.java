/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author Lucien FOTSA
 */
public class CritereFiltre implements Serializable {

    private String attribut;
    private String libelle;
    private String type;
    private String etat;
    private Agence agence;
    private String valString;
    private String valString2;
    private Float valNum1;
    private Float valNum2;
    private Date valDate1;
    private Date valDate2;
    private Date valTime1;
    private Date valTime2;
    private Boolean valBool;
    private Integer valSelect;
    private Integer valSelect2;
    private Integer valSelect3;
    private Long valSelectL;
    private Long valSelect2L;
    private Long valSelect3L;
    private String usedValue;
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public CritereFiltre() {
    }

    public CritereFiltre(String attribut, String libelle, String type, String etat) {
        try {
            this.attribut = attribut;
            this.libelle = libelle;
            this.type = type;
            this.etat = etat;
            this.valDate1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss SSS").parse("01/" + new SimpleDateFormat("MM").format(new Date(System.currentTimeMillis())) + "/" + new SimpleDateFormat("yyyy").format(new Date(System.currentTimeMillis())) + " 00:00:00 000");
            this.valDate2 = new Date(System.currentTimeMillis());
            try {
                this.valTime1 = new Time(new SimpleDateFormat("HH:mm:ss").parse("00:00:00").getTime());
                this.valTime2 = new Time(new SimpleDateFormat("HH:mm:ss").parse("23:59:59").getTime());
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.valNum1 = 0F;
            this.valNum2 = 0F;
            this.valString = "";
            this.valString2 = "";
            this.valBool = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CritereFiltre)) {
            return false;
        }
        CritereFiltre other = (CritereFiltre) object;
        return !((this.attribut == null && other.attribut != null) || (this.attribut != null && !this.attribut.equals(other.attribut)));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.attribut);
        return hash;
    }

    @Override
    public String toString() {
        return (this.libelle);
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAttribut() {
        return attribut;
    }

    public void setAttribut(String attribut) {
        this.attribut = attribut;
    }

    public Float getValNum1() {
        return valNum1;
    }

    public void setValNum1(Float valNum1) {
        this.valNum1 = valNum1;
    }

    public Float getValNum2() {
        return valNum2;
    }

    public void setValNum2(Float valNum2) {
        this.valNum2 = valNum2;
    }

    public Date getValDate1() {
        try {
            if (valTime1 != null && valDate1 != null) {
                valDate1 = dateTimeFormat.parse(dateFormat.format(valDate1) + " " + timeFormat.format(valTime1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return valDate1;
    }

    public void setValDate1(Date valDate1) {
        this.valDate1 = valDate1;
    }

    public Date getValDate2() {
        try {
            if (valTime2 != null && valDate2 != null) {
                valDate2 = dateTimeFormat.parse(dateFormat.format(valDate2) + " " + timeFormat.format(valTime2));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return valDate2;
    }

    public void setValDate2(Date valDate2) {
        this.valDate2 = valDate2;
    }

    public String getValString() {
        return valString != null ? valString : "";
    }

    public void setValString(String valString) {
        this.valString = valString;
    }

    public Boolean getValBool() {
        return valBool;
    }

    public void setValBool(Boolean valBool) {
        this.valBool = valBool;
    }

    public Integer getValSelect() {
        return valSelect;
    }

    public void setValSelect(Integer valSelect) {
        this.valSelect = valSelect;
    }

    public Date getValTime1() {
        return valTime1;
    }

    public void setValTime1(Date valTime1) {
        this.valTime1 = valTime1;
    }

    public Date getValTime2() {
        return valTime2;
    }

    public void setValTime2(Date valTime2) {
        this.valTime2 = valTime2;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public Agence getAgence() {
        return agence;
    }

    public void setAgence(Agence agence) {
        this.agence = agence;
    }

    public String getUsedValue() {
        return usedValue;
    }

    public void setUsedValue(String usedValue) {
        this.usedValue = usedValue;
    }

    public Integer getValSelect2() {
        return valSelect2;
    }

    public void setValSelect2(Integer valSelect2) {
        this.valSelect2 = valSelect2;
    }

    public Integer getValSelect3() {
        return valSelect3;
    }

    public void setValSelect3(Integer valSelect3) {
        this.valSelect3 = valSelect3;
    }

    public Long getValSelectL() {
        return valSelectL;
    }

    public void setValSelectL(Long valSelectL) {
        this.valSelectL = valSelectL;
    }

    public Long getValSelect2L() {
        return valSelect2L;
    }

    public void setValSelect2L(Long valSelect2L) {
        this.valSelect2L = valSelect2L;
    }

    public Long getValSelect3L() {
        return valSelect3L;
    }

    public void setValSelect3L(Long valSelect3L) {
        this.valSelect3L = valSelect3L;
    }

    public String getValString2() {
        return valString2;
    }

    public void setValString2(String valString2) {
        this.valString2 = valString2;
    }

}
