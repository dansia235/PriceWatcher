/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author LucienFOTSA
 */
@Embeddable
public class VeilleconcurrentPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "idconcurrent")
    private int idconcurrent;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idarticle")
    private int idarticle;

    public VeilleconcurrentPK() {
    }

    public VeilleconcurrentPK(int idconcurrent, int idarticle) {
        this.idconcurrent = idconcurrent;
        this.idarticle = idarticle;
    }

    public int getIdconcurrent() {
        return idconcurrent;
    }

    public void setIdconcurrent(int idconcurrent) {
        this.idconcurrent = idconcurrent;
    }

    public int getIdarticle() {
        return idarticle;
    }

    public void setIdarticle(int idarticle) {
        this.idarticle = idarticle;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idconcurrent;
        hash += (int) idarticle;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VeilleconcurrentPK)) {
            return false;
        }
        VeilleconcurrentPK other = (VeilleconcurrentPK) object;
        if (this.idconcurrent != other.idconcurrent) {
            return false;
        }
        if (this.idarticle != other.idarticle) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.VeilleconcurrentPK[ idconcurrent=" + idconcurrent + ", idarticle=" + idarticle + " ]";
    }

}
