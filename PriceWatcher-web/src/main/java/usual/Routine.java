/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package usual;

import entities.Agence;
import entities.Compte;
import entities.Operations;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import static java.lang.System.currentTimeMillis;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;
import static java.util.ResourceBundle.getBundle;
import java.util.regex.Pattern;
import javax.faces.context.FacesContext;
import net.sf.jasperreports.engine.JasperPrint;
import sessions.OperationsFacadeLocal;

/**
 *
 * @author Lucien FOTSA
 */
public class Routine implements Cloneable, Serializable {

    private Agence agence = new Agence();
    private JasperPrint jasperPrint;
    private ResourceBundle rs;
    private String titleMessage = "";
    private String contexteMessage = "";
    private String message = "";
    private String iconMessage = "";
    private boolean vanish = true;
    private boolean showInfo = false;
    private float screenH = 768;
    private float screenW = 1000;
    private String cert;
    private boolean add = true;
    private boolean mof = false;
    private boolean sup = false;
    private boolean con = false;
    private boolean cop = false;
    private boolean raf = true;
    private boolean imp = false;
    private boolean exp = false;
    private boolean res = false;
    private boolean det = false;
    private boolean cor = false;
    private boolean special = false;
    private boolean threadState = false;

    public Routine() {
        showInfo = false;
        vanish = true;
        special = false;
        threadState = false;
    }

    public void correctDateTime(Date dateDebut, Date dateFin, Date heureArrive, Date heureDepart) {
        if (dateDebut != null && dateFin != null) {
            if (dateFin.before(dateDebut)) {
                Date ech = dateDebut;
                dateDebut = dateFin;
                dateFin = ech;
            }
            if (heureDepart != null && heureArrive != null) {
                if (dateDebut.compareTo(dateFin) == 0) {
                    if (heureDepart.before(heureArrive)) {
                        Date ech = heureDepart;
                        heureDepart = heureArrive;
                        heureArrive = ech;
                    }
                }
            }
        }
    }

    public String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    public boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null) {
            return false;
        } else {
            if (email.length() == 0) {
                return false;
            }
        }
        return pat.matcher(email).matches();
    }

    public void log(String line) {
        try {
            // Create new file
            String filePath = "/opt/payara5/glassfish/domains/domain1/applications/cnmlogs/";
            File file = new File(filePath + "log.txt");
            // If file doesn't exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            // Write in file
            bw.write(line + "\n");
            // Close connection
            bw.close();
        } catch (Exception e) {
            try {
                // Create new file
                String filePath = "C:\\payara5\\glassfish\\domains\\domain1\\applications\\cnmlogs\\";
                File file = new File(filePath + "log.txt");
                // If file doesn't exists, then create it
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
                BufferedWriter bw = new BufferedWriter(fw);
                // Write in file
                bw.write(line + "\n");
                // Close connection
                bw.close();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }

    public void log(String line, String fileName) {
        try {
            // Create new file
            String filePath = "/opt/payara5/glassfish/domains/domain1/applications/cnmlogs/";
            File file = new File(filePath + fileName + ".txt");
            // If file doesn't exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            // Write in file
            bw.write(line + "\n");
            // Close connection
            bw.close();
        } catch (Exception e) {
            try {
                // Create new file
                String filePath = "C:\\payara5\\glassfish\\domains\\domain1\\applications\\cnmlogs\\";
                File file = new File(filePath + fileName + ".txt");
                // If file doesn't exists, then create it
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
                BufferedWriter bw = new BufferedWriter(fw);
                // Write in file
                bw.write(line + "\n");
                // Close connection
                bw.close();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }

    public void logBackUp(Operations op) {
        try {
            // Create new file
            String fileName = "cnmlog_" + new SimpleDateFormat("dd/MM/yyyy").format(new Date(System.currentTimeMillis())).replace("/", "-") + ".txt";
            String filePath = "/opt/payara5/glassfish/domains/domain1/applications/cnmlogs/" + fileName;
            File file = new File(filePath);
            // If file doesn't exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            // Write in file
            String line = "--> " + new SimpleDateFormat("EEE dd/MMM/yyyy HH:mm:ss").format(new Date(System.currentTimeMillis())) + " " + (op.getIdCompte() != null ? ("U-" + op.getIdCompte().getNomPrenom()) : "") + " " + op.getLibelle() + " -> " + op.getCible() + " " + op.getAdresseIp();
            bw.write(line + "\n");
            // Close connection
            bw.close();
        } catch (Exception e) {
            try {
                // Create new file
                String fileName = "cnmlog_" + new SimpleDateFormat("dd/MM/yyyy").format(new Date(System.currentTimeMillis())).replace("/", "-") + ".txt";
                String filePath = "C:\\payara5\\glassfish\\domains\\domain1\\applications\\cnmlogs\\" + fileName;
                File file = new File(filePath);
                // If file doesn't exists, then create it
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
                BufferedWriter bw = new BufferedWriter(fw);
                // Write in file
                String line = "--> " + new SimpleDateFormat("EEE dd/MMM/yyyy HH:mm:ss").format(new Date(System.currentTimeMillis())) + " " + (op.getIdCompte() != null ? ("U-" + op.getIdCompte().getNomPrenom()) : "") + " " + op.getLibelle() + " -> " + op.getCible() + " " + op.getAdresseIp();
                bw.write(line + "\n");
                // Close connection
                bw.close();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }

    public void operationEffectue(String adresseIp, String adresseNom, String libelle, String label, String cible, Compte compte, OperationsFacadeLocal operationsFacade) {
        try {
            if (!compte.getEtat().equals("Super")) {
                Operations operation = new Operations();
                operation.setIdCompte(compte);
                operation.setAdresseIp(adresseIp);
                operation.setAdressenom(adresseNom);
                operation.setLibelle(libelle);
                operation.setLabel(label);
                operation.setCible(cible);
                operation.setEtat("Actif");
                operation.setDateOpration(new Date(currentTimeMillis()));
                operation.setIdOperations(operationsFacade.nextId());
                if (operationsFacade.find(operation.getIdOperations()) != null) {
                    operation.setIdOperations(operationsFacade.nextIdRaw());
                }
                operationsFacade.create(operation);
                logBackUp(operation);
            }
        } catch (Exception e) {
        }
    }

    public String localizeMessage(String info) {
        rs = getBundle("language", FacesContext.getCurrentInstance().getViewRoot().getLocale());
        String msg = info;
        try {
            msg = rs.getString(info);
        } catch (Exception e) {
        }
        return msg;
    }

    public String localizeMessage(String info, Locale locale) {
        rs = getBundle("language", locale);
        String msg = info;
        try {
            msg = rs.getString(info);
        } catch (Exception e) {
        }
        return msg;
    }

    public String index() {
        return "index.xhtml?faces-redirect=true";
    }

    public void feedBack(String title, String contexte, String info, Exception e) {
        showInfo = true;
        if (e != null) {
            vanish = false;
            message = "";
            message += localizeMessage("Message") + "  : " + e.getMessage() + ", \n";
            message += localizeMessage("Cause") + "     : " + e.getCause() + ", \n";
            message += localizeMessage("Classe") + "   : " + e.getClass() + ", \n";
            message += localizeMessage("Description") + "   : " + Arrays.toString(e.getStackTrace()) + ", \n";
            iconMessage = "resources/tool_images/error.png";
            titleMessage = localizeMessage(title);
            contexteMessage = contexte;
        } else if (title.equals("Information")) {
            vanish = true;
            message = info;
            iconMessage = "resources/tool_images/ok.png";
            titleMessage = localizeMessage(title);
            contexteMessage = contexte;
        } else {
            vanish = false;
            message = info;
            iconMessage = "resources/tool_images/stop.png";
            titleMessage = localizeMessage(title);
            contexteMessage = contexte;
        }
    }

    public void feedBack(String title, String contexte, String info, Exception e, Locale locale) {
        showInfo = true;
        if (e != null) {
            vanish = false;
            message = "";
            message += localizeMessage("Message", locale) + "  : " + e.getMessage() + ", \n";
            message += localizeMessage("Cause", locale) + "     : " + e.getCause() + ", \n";
            message += localizeMessage("Classe", locale) + "   : " + e.getClass() + ", \n";
            message += localizeMessage("Description", locale) + "   : " + Arrays.toString(e.getStackTrace()) + ", \n";
            iconMessage = "resources/tool_images/error.png";
            titleMessage = localizeMessage(title, locale);
            contexteMessage = contexte;
        } else if (title.equals("Information")) {
            vanish = true;
            message = info;
            iconMessage = "resources/tool_images/ok.png";
            titleMessage = localizeMessage(title, locale);
            contexteMessage = contexte;
        } else {
            vanish = false;
            message = info;
            iconMessage = "resources/tool_images/stop.png";
            titleMessage = localizeMessage(title, locale);
            contexteMessage = contexte;
        }
    }

    public void stopInfo() {
        showInfo = false;
        vanish = false;
        special = false;
        threadState = false;
    }

    public String getTitleMessage() {
        return titleMessage;
    }

    public void setTitleMessage(String titleMessage) {
        this.titleMessage = titleMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIconMessage() {
        return iconMessage;
    }

    public void setIconMessage(String iconMessage) {
        this.iconMessage = iconMessage;
    }

    public JasperPrint getJasperPrint() {
        return jasperPrint;
    }

    public void setJasperPrint(JasperPrint jasperPrint) {
        this.jasperPrint = jasperPrint;
    }

    public ResourceBundle getRs() {
        return rs;
    }

    public void setRs(ResourceBundle rs) {
        this.rs = rs;
    }

    public boolean isVanish() {
        return vanish;
    }

    public void setVanish(boolean vanish) {
        this.vanish = vanish;
    }

    public float getScreenH() {
        return screenH;
    }

    public void setScreenH(float screenH) {
        this.screenH = screenH;
    }

    public float getScreenW() {
        return screenW;
    }

    public void setScreenW(float screenW) {
        this.screenW = screenW;
    }

    public boolean isAdd() {
        return add;
    }

    public void setAdd(boolean add) {
        this.add = add;
    }

    public boolean isMof() {
        return mof;
    }

    public void setMof(boolean mof) {
        this.mof = mof;
    }

    public boolean isSup() {
        return sup;
    }

    public void setSup(boolean sup) {
        this.sup = sup;
    }

    public boolean isCon() {
        return con;
    }

    public void setCon(boolean con) {
        this.con = con;
    }

    public boolean isCop() {
        return cop;
    }

    public void setCop(boolean cop) {
        this.cop = cop;
    }

    public boolean isRes() {
        return res;
    }

    public void setRes(boolean res) {
        this.res = res;
    }

    public boolean isDet() {
        return det;
    }

    public void setDet(boolean det) {
        this.det = det;
    }

    public boolean isImp() {
        return imp;
    }

    public void setImp(boolean imp) {
        this.imp = imp;
    }

    public boolean isExp() {
        return exp;
    }

    public void setExp(boolean exp) {
        this.exp = exp;
    }

    public boolean isRaf() {
        return raf;
    }

    public void setRaf(boolean raf) {
        this.raf = raf;
    }

    public String getContexteMessage() {
        return contexteMessage;
    }

    public void setContexteMessage(String contexteMessage) {
        this.contexteMessage = contexteMessage;
    }

    public boolean isShowInfo() {
        return showInfo;
    }

    public void setShowInfo(boolean showInfo) {
        this.showInfo = showInfo;
    }

    public boolean isCor() {
        return cor;
    }

    public void setCor(boolean cor) {
        this.cor = cor;
    }

    public Agence getAgence() {
        return agence;
    }

    public void setAgence(Agence agence) {
        this.agence = agence;
    }

    public boolean isSpecial() {
        return special;
    }

    public void setSpecial(boolean special) {
        this.special = special;
    }

    public boolean isThreadState() {
        return threadState;
    }

    public void setThreadState(boolean threadState) {
        this.threadState = threadState;
    }

    public String getCert() {
        return cert;
    }

    public void setCert(String cert) {
        this.cert = cert;
    }

}
