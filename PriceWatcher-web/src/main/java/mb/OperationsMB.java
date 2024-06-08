/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mb;

import usual.Routine;
import entities.Agence;
import entities.Compte;
import entities.CritereFiltre;
import entities.Operations;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import static java.util.Locale.ROOT;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import net.sf.jasperreports.engine.JRException;
import static net.sf.jasperreports.engine.JasperExportManager.exportReportToPdfStream;
import static net.sf.jasperreports.engine.JasperFillManager.fillReport;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.PieChartModel;
import org.primefaces.model.menu.MenuItem;
import sessions.AgenceFacadeLocal;
import sessions.CompteFacadeLocal;
import sessions.GroupeUtilisateurFacadeLocal;
import sessions.MultimediaFacadeLocal;
import sessions.OperationsFacadeLocal;

/**
 *
 * @author Lucien FOTSA
 */
public class OperationsMB implements Serializable {

    @Resource
    private UserTransaction ut;
    @EJB
    private OperationsFacadeLocal operationsFacade;
    private List<Operations> listOperations = new ArrayList<>();
    private List<Operations> selectedListOperations = new ArrayList<>();
    private Operations selectedOperations = new Operations();
    //--------------------------------------------------------------------------
    private Compte connectedUser;
    private Routine routine = new Routine();
    private String operation = "add";
    private boolean esc = true;//Etat spécifique d'un composant
    private boolean egc = true;//Etat générique d'un composant
    private String style = "font-size: 10pt; font-weight: bold; font-family: arial;";
    //--------------------------------------------------------------------------
    @EJB
    private CompteFacadeLocal compteFacade;
    private List<Compte> listCompte = new ArrayList<>();
    private Integer selectedCompte;
    //--------------------------------------------
    @EJB
    private MultimediaFacadeLocal multimediaFacade;
    //--------------------------------------------
    @EJB
    private GroupeUtilisateurFacadeLocal groupeUtilisateurFacade;
    //--------------------------------------------
    @EJB
    private AgenceFacadeLocal agenceFacade;
    private List<Agence> listAgence = new ArrayList<>();
    private Integer selectedAgence;
    //---------------------------------------------
    private List<CritereFiltre> listCritereFiltre = new ArrayList<>();
    private CritereFiltre selectedCritere = new CritereFiltre("dateOperation", routine.localizeMessage("Date") + " " + routine.localizeMessage("Del_ap") + routine.localizeMessage("Operation"), "Date", "Actif");
    private String mode = "Grille";
    private Integer categorie = 1;
    private int jan = 0;
    private int fev = 0;
    private int mar = 0;
    private int avr = 0;
    private int mai = 0;
    private int jui = 0;
    private int jul = 0;
    private int aou = 0;
    private int sep = 0;
    private int oct = 0;
    private int nov = 0;
    private int dec = 0;
    //----------------------------
    private int janTotal = 0;
    private int fevTotal = 0;
    private int marTotal = 0;
    private int avrTotal = 0;
    private int maiTotal = 0;
    private int juiTotal = 0;
    private int julTotal = 0;
    private int aouTotal = 0;
    private int sepTotal = 0;
    private int octTotal = 0;
    private int novTotal = 0;
    private int decTotal = 0;
    //----------------------------
    private int max = 0;
    private PieChartModel pcmOperation = new PieChartModel();
    private BarChartModel bcmOperation = new BarChartModel();
    private PieChartModel pcmOperationFill = new PieChartModel();
    private LineChartModel lcmOperation = new LineChartModel();
    //--------------------------------------------------
    private DashboardModel moduleSecuriteDashboard;
    private int agences = 0;
    private int bureau = 0;
    private int user = 0;
    private int client = 0;
    private int etudiant = 0;
    private int user_online = 0;
    private int client_online = 0;
    private int etudiant_online = 0;
    private int user_most = 0;
    private int client_most = 0;
    private int etudiant_most = 0;
    private int groupe = 0;
    private int galeri = 0;
    private int partners = 0;
    private int pub = 0;
    private int mobile = 0;
    private List<Compte> listCompteMost = new ArrayList<>();
    //---------------------------------------------------------
    private List<Agence> currentUserAgence = new ArrayList<>();
    private LazyDataModel<Operations> lazylistOperationsModel;
    private List<Operations> lazyListOperations = new ArrayList<>();
    private boolean lock = false;

    /**
     * Creates a new instance of OperationsMB
     */
    public OperationsMB() {
    }

    @PostConstruct
    private void init() {
        connectedUser = (Compte) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUser");
        currentUserAgence = (List<Agence>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUserAgence");
        if (((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURL().toString().contains("operations")) {
            loadCritere();
            selectCritere();
//            rechercheOperations();
        }
        if (((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURL().toString().contains("moduleSecurite")) {
            loadModuleSecuriteDashboard();
        }
    }

    public void loadModuleSecuriteDashboard() {
        try {
            moduleSecuriteDashboard = new DefaultDashboardModel();
            DashboardColumn column1 = new DefaultDashboardColumn();
            DashboardColumn column2 = new DefaultDashboardColumn();
            DashboardColumn column3 = new DefaultDashboardColumn();
            column1.addWidget("agence");
            column2.addWidget("groupe");
            column3.addWidget("users");
            column1.addWidget("connected");
            column2.addWidget("usage");
            column3.addWidget("multimedia");
            moduleSecuriteDashboard.addColumn(column1);
            moduleSecuriteDashboard.addColumn(column2);
            moduleSecuriteDashboard.addColumn(column3);
            listCompteMost.clear();
            agences = 0;
            bureau = 0;
            user = 0;
            client = 0;
            user_online = 0;
            client_online = 0;
            user_most = 0;
            client_most = 0;
            groupe = 0;
            galeri = 0;
            partners = 0;
            pub = 0;
            //----------------------------------------------
            for (Agence a : currentUserAgence) {
                if (a.getAgeIdagence() == null) {
                    agences++;
                } else {
                    bureau++;
                }
            }
            user = compteFacade.findAll_Etat("Actif".toUpperCase(ROOT), currentUserAgence).size();
            user_online = compteFacade.findByConnexion_Etat("true".toUpperCase(ROOT), "Actif".toUpperCase(ROOT), currentUserAgence).size();
            listCompteMost.addAll(compteFacade.findAll_Plus_Utilise(currentUserAgence));
            user_most = listCompteMost.size();
            groupe = groupeUtilisateurFacade.findAll_Etat("Actif".toUpperCase(ROOT), currentUserAgence).size();
            galeri = multimediaFacade.findByCategorie_Etat("%" + "Galerie / Gallery".toUpperCase(ROOT) + "%", "Actif".toUpperCase(ROOT), currentUserAgence).size();
            partners = multimediaFacade.findByCategorie_Etat("%" + "Partenaires / Partners".toUpperCase(ROOT) + "%", "Actif".toUpperCase(ROOT), currentUserAgence).size();
            pub = multimediaFacade.findByCategorie_Etat("%" + "Publicité / Advertising".toUpperCase(ROOT) + "%", "Actif".toUpperCase(ROOT), currentUserAgence).size();
            mobile = multimediaFacade.findByCategorie_Etat("%" + "Mobile / Mobile".toUpperCase(ROOT) + "%", "Actif".toUpperCase(ROOT), currentUserAgence).size();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void switchMode() {
        mode = mode.equals("Grille") ? "Chart" : "Grille";
        rechercheOperations();
    }

    public void switchCategorie() {
        loadCritere();
        selectedCritere = new CritereFiltre("Tous", routine.localizeMessage("Tous"), "Tous", "Actif");
        selectCritere();
        rechercheOperations();
    }

    public void generateCharts() {
        Map<String, Number> op_user = new HashMap<>(), op_user_sort = new HashMap<>();
        Map<String, Number> op_agence = new HashMap<>(), op_agence_sort = new HashMap<>();
        lazyListOperations.forEach((op) -> {
            switch (categorie) {
                case 1:
                    if (op_user.containsKey(op.getIdCompte().getNomPrenom().replace("'", "*"))) {
                        op_user.replace(op.getIdCompte().getNomPrenom().replace("'", "*"), (op_user.get(op.getIdCompte().getNomPrenom().replace("'", "*")).intValue() + 1));
                    } else {
                        op_user.put(op.getIdCompte().getNomPrenom().replace("'", "*"), 1);
                    }
                    if (op_agence.containsKey(op.getIdCompte().getIdagence().getRaisonsociale().replace("'", "*"))) {
                        op_agence.replace(op.getIdCompte().getIdagence().getRaisonsociale().replace("'", "*"), (op_agence.get(op.getIdCompte().getIdagence().getRaisonsociale().replace("'", "*")).intValue() + 1));
                    } else {
                        op_agence.put(op.getIdCompte().getIdagence().getRaisonsociale().replace("'", "*"), 1);
                    }
                    break;
            }
        });
        //----------------------------------------
        ArrayList<Entry<String, Number>> array_op_user = new ArrayList<>(op_user.entrySet());
        for (int i = 0; i <= (array_op_user.size() - 1); i++) {
            int nextBigest = i;
            for (int j = (i + 1); j < array_op_user.size(); j++) {
                if (array_op_user.get(nextBigest).getValue().intValue() < array_op_user.get(j).getValue().intValue()) {
                    nextBigest = j;
                }
            }
            if (nextBigest != i) {
                array_op_user.set(i, array_op_user.set(nextBigest, array_op_user.get(i)));
            }
            if (array_op_user.get(i).getValue().intValue() < 1) {
                break;
            }
            if (i < 3) {
                op_user_sort.put(array_op_user.get(i).getKey(), array_op_user.get(i).getValue());
            }
        }
        //*******************************************
        ArrayList<Entry<String, Number>> array_op_agence = new ArrayList<>(op_agence.entrySet());
        for (int i = 0; i <= (array_op_agence.size() - 1); i++) {
            int nextBigest = i;
            for (int j = (i + 1); j < array_op_agence.size(); j++) {
                if (array_op_agence.get(nextBigest).getValue().intValue() < array_op_agence.get(j).getValue().intValue()) {
                    nextBigest = j;
                }
            }
            if (nextBigest != i) {
                array_op_agence.set(i, array_op_agence.set(nextBigest, array_op_agence.get(i)));
            }
            if (array_op_agence.get(i).getValue().intValue() < 1) {
                break;
            }
            if (i < 3) {
                op_agence_sort.put(array_op_agence.get(i).getKey(), array_op_agence.get(i).getValue());
            }
        }
        //----------------------------------------
        pcmOperation = new PieChartModel(op_user_sort);
        pcmOperation.setTitle(routine.localizeMessage("Nombre") + " " + routine.localizeMessage("D_ap") + routine.localizeMessage("Operations") + " " + routine.localizeMessage("Par") + " " + routine.localizeMessage("Utilisateur"));
        pcmOperation.setLegendPosition("ne");
        pcmOperation.setLegendRows(5);
        pcmOperation.setShowDataLabels(true);
        pcmOperation.setFill(true);
        pcmOperation.setSeriesColors("33ccff,ffcc66,ff99ff,ccff33,cc99ff,ccffff,ffcccc,cccccc");
        //............................................................
        pcmOperationFill = new PieChartModel(op_agence_sort);
        pcmOperationFill.setTitle(routine.localizeMessage("Nombre") + " " + routine.localizeMessage("D_ap") + routine.localizeMessage("Operations") + " " + routine.localizeMessage("Par") + " " + routine.localizeMessage("Agence"));
        pcmOperationFill.setLegendPosition("ne");
        pcmOperationFill.setLegendRows(5);
        pcmOperationFill.setShowDataLabels(true);
        pcmOperationFill.setFill(false);
        pcmOperationFill.setSeriesColors("33ccff,ffcc66,ff99ff,ccff33,cc99ff,ccffff,ffcccc,cccccc");
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        bcmOperation = new BarChartModel();
        //----------------------------------------
        janTotal = 0;
        fevTotal = 0;
        marTotal = 0;
        avrTotal = 0;
        maiTotal = 0;
        juiTotal = 0;
        julTotal = 0;
        aouTotal = 0;
        sepTotal = 0;
        octTotal = 0;
        novTotal = 0;
        decTotal = 0;
        max = 0;
        //----------------------------------------
        op_user_sort.entrySet().forEach((u) -> {
            jan = 0;
            fev = 0;
            mar = 0;
            avr = 0;
            mai = 0;
            jui = 0;
            jul = 0;
            aou = 0;
            sep = 0;
            oct = 0;
            nov = 0;
            dec = 0;
            ChartSeries cs = new ChartSeries(u.getKey().replace("'", "*"));
            listOperations.forEach((op) -> {
                String op_author = "";
                switch (categorie) {
                    case 1:
                        op_author = op.getIdCompte().getNomPrenom();
                        break;
                }
                if (op_author.equals(u.getKey().replace("'", "*"))) {
                    switch ((new SimpleDateFormat("MM")).format(op.getDateOpration())) {
                        case "01":
                            jan++;
                            break;
                        case "02":
                            fev++;
                            break;
                        case "03":
                            mar++;
                            break;
                        case "04":
                            avr++;
                            break;
                        case "05":
                            mai++;
                            break;
                        case "06":
                            jui++;
                            break;
                        case "07":
                            jul++;
                            break;
                        case "08":
                            aou++;
                            break;
                        case "09":
                            sep++;
                            break;
                        case "10":
                            oct++;
                            break;
                        case "11":
                            nov++;
                            break;
                        case "12":
                            dec++;
                            break;
                    }
                }
            });
            cs.set(routine.localizeMessage("Janvier_abr"), jan);
            cs.set(routine.localizeMessage("Fevrier_abr"), fev);
            cs.set(routine.localizeMessage("Mars_abr"), mar);
            cs.set(routine.localizeMessage("Avril_abr"), avr);
            cs.set(routine.localizeMessage("Mai_abr"), mai);
            cs.set(routine.localizeMessage("Juin_abr"), jui);
            cs.set(routine.localizeMessage("Juillet_abr"), jul);
            cs.set(routine.localizeMessage("Aout_abr"), aou);
            cs.set(routine.localizeMessage("Septembre_abr"), sep);
            cs.set(routine.localizeMessage("Octobre_abr"), oct);
            cs.set(routine.localizeMessage("Novembre_abr"), nov);
            cs.set(routine.localizeMessage("Decembre_abr"), dec);
            bcmOperation.addSeries(cs);
            //--------------------------------------------------------------
            janTotal += jan;
            fevTotal += fev;
            marTotal += mar;
            avrTotal += avr;
            maiTotal += mai;
            juiTotal += jui;
            julTotal += jul;
            aouTotal += aou;
            sepTotal += sep;
            octTotal += oct;
            novTotal += nov;
            decTotal += dec;
        });
        //-----------------------------------------------------------
        max = max < janTotal ? janTotal : max;
        max = max < fevTotal ? fevTotal : max;
        max = max < marTotal ? marTotal : max;
        max = max < avrTotal ? avrTotal : max;
        max = max < maiTotal ? maiTotal : max;
        max = max < juiTotal ? juiTotal : max;
        max = max < julTotal ? julTotal : max;
        max = max < aouTotal ? aouTotal : max;
        max = max < sepTotal ? sepTotal : max;
        max = max < octTotal ? octTotal : max;
        max = max < novTotal ? novTotal : max;
        max = max < decTotal ? decTotal : max;
        //-----------------------------------------------------------
        bcmOperation.setTitle(routine.localizeMessage("Nombre") + " " + routine.localizeMessage("D_ap") + routine.localizeMessage("Operations") + " " + routine.localizeMessage("Par") + " " + routine.localizeMessage("Utilisateur") + " " + routine.localizeMessage("Et") + " " + routine.localizeMessage("Par") + " " + routine.localizeMessage("Mois"));
        bcmOperation.setLegendPosition("ne");
        bcmOperation.setLegendRows(5);
        bcmOperation.setAnimate(true);
        bcmOperation.setStacked(true);
        bcmOperation.getAxis(AxisType.X).setLabel(routine.localizeMessage("Mois"));
        bcmOperation.getAxis(AxisType.Y).setLabel(routine.localizeMessage("Nombre") + " " + routine.localizeMessage("D_ap") + routine.localizeMessage("Operations"));
        bcmOperation.getAxis(AxisType.Y).setMin(0);
        bcmOperation.getAxis(AxisType.Y).setMax(max);
        bcmOperation.setSeriesColors("33ccff,ffcc66,ff99ff,ccff33,cc99ff,ccffff,ffcccc,cccccc");
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        lcmOperation = new LineChartModel();
        //----------------------------------------
        janTotal = 0;
        fevTotal = 0;
        marTotal = 0;
        avrTotal = 0;
        maiTotal = 0;
        juiTotal = 0;
        julTotal = 0;
        aouTotal = 0;
        sepTotal = 0;
        octTotal = 0;
        novTotal = 0;
        decTotal = 0;
        max = 0;
        //----------------------------------------
        op_agence_sort.entrySet().forEach((c) -> {
            jan = 0;
            fev = 0;
            mar = 0;
            avr = 0;
            mai = 0;
            jui = 0;
            jul = 0;
            aou = 0;
            sep = 0;
            oct = 0;
            nov = 0;
            dec = 0;
            LineChartSeries cs = new LineChartSeries(c.getKey().replace("'", "*"));
            lazyListOperations.forEach((op) -> {
                Agence op_author = new Agence();
                switch (categorie) {
                    case 1:
                        op_author = op.getIdCompte().getIdagence();
                        break;
                }
                if (op_author.getRaisonsociale().replace("'", "*").equals(c.getKey().replace("'", "*"))) {
                    switch ((new SimpleDateFormat("MM")).format(op.getDateOpration())) {
                        case "01":
                            jan++;
                            break;
                        case "02":
                            fev++;
                            break;
                        case "03":
                            mar++;
                            break;
                        case "04":
                            avr++;
                            break;
                        case "05":
                            mai++;
                            break;
                        case "06":
                            jui++;
                            break;
                        case "07":
                            jul++;
                            break;
                        case "08":
                            aou++;
                            break;
                        case "09":
                            sep++;
                            break;
                        case "10":
                            oct++;
                            break;
                        case "11":
                            nov++;
                            break;
                        case "12":
                            dec++;
                            break;
                    }
                }
            });
            cs.set(1, jan);
            cs.set(2, fev);
            cs.set(3, mar);
            cs.set(4, avr);
            cs.set(5, mai);
            cs.set(6, jui);
            cs.set(7, jul);
            cs.set(8, aou);
            cs.set(9, sep);
            cs.set(10, oct);
            cs.set(11, nov);
            cs.set(12, dec);
            lcmOperation.addSeries(cs);
            //--------------------------------------------------------------
            janTotal += jan;
            fevTotal += fev;
            marTotal += mar;
            avrTotal += avr;
            maiTotal += mai;
            juiTotal += jui;
            julTotal += jul;
            aouTotal += aou;
            sepTotal += sep;
            octTotal += oct;
            novTotal += nov;
            decTotal += dec;
        });
        //-----------------------------------------------------------
        max = max < janTotal ? janTotal : max;
        max = max < fevTotal ? fevTotal : max;
        max = max < marTotal ? marTotal : max;
        max = max < avrTotal ? avrTotal : max;
        max = max < maiTotal ? maiTotal : max;
        max = max < juiTotal ? juiTotal : max;
        max = max < julTotal ? julTotal : max;
        max = max < aouTotal ? aouTotal : max;
        max = max < sepTotal ? sepTotal : max;
        max = max < octTotal ? octTotal : max;
        max = max < novTotal ? novTotal : max;
        max = max < decTotal ? decTotal : max;
        //-----------------------------------------------------------
        lcmOperation.setTitle(routine.localizeMessage("Nombre") + " " + routine.localizeMessage("D_ap") + routine.localizeMessage("Operations") + " " + routine.localizeMessage("Par") + " " + routine.localizeMessage("Agence") + " " + routine.localizeMessage("Et") + " " + routine.localizeMessage("Par") + " " + routine.localizeMessage("Mois"));
        lcmOperation.setLegendPosition("ne");
        lcmOperation.setLegendRows(5);
        lcmOperation.setAnimate(true);
//        lcmOperation.setStacked(true);
        lcmOperation.getAxis(AxisType.X).setLabel(routine.localizeMessage("Mois"));
        lcmOperation.getAxis(AxisType.Y).setLabel(routine.localizeMessage("Nombre") + " " + routine.localizeMessage("D_ap") + routine.localizeMessage("Operations"));
        lcmOperation.getAxis(AxisType.Y).setMin(0);
        lcmOperation.getAxis(AxisType.Y).setMax(max);
        lcmOperation.getAxis(AxisType.X).setMin(1);
        lcmOperation.getAxis(AxisType.X).setMax(12);
        lcmOperation.setSeriesColors("33ccff,ffcc66,ff99ff,ccff33,cc99ff,ccffff,ffcccc,cccccc");
    }

    //--------------------------------------------------
    public void loadCritere() {
        listCritereFiltre.clear();
        listCritereFiltre.add(new CritereFiltre("Tous", routine.localizeMessage("Tous"), "Tous", "Actif"));
        switch (categorie) {
            case 1:
                listCritereFiltre.add(new CritereFiltre("idagence_idCompte", routine.localizeMessage("Agence") + " / " + routine.localizeMessage("Compte"), "Select", "Actif"));
                break;
            case 2:
                listCritereFiltre.add(new CritereFiltre("idagence_idclient", routine.localizeMessage("Agence") + " / " + routine.localizeMessage("Client"), "Select2", "Actif"));
                break;
        }
        listCritereFiltre.add(new CritereFiltre("libelle", routine.localizeMessage("Libelle"), "String", "Actif"));
        listCritereFiltre.add(new CritereFiltre("cible", routine.localizeMessage("Cible"), "String", "Actif"));
        listCritereFiltre.add(new CritereFiltre("adresseIp", routine.localizeMessage("Adresse") + " (IP)", "String", "Actif"));
        listCritereFiltre.add(new CritereFiltre("dateOperation", routine.localizeMessage("Date") + " " + routine.localizeMessage("Del_ap") + routine.localizeMessage("Operation"), "Date", "Actif"));
    }

    public void selectCritere() {
        selectedCritere.setType(listCritereFiltre.get(listCritereFiltre.indexOf(selectedCritere)).getType());
        selectedCritere.setLibelle(listCritereFiltre.get(listCritereFiltre.indexOf(selectedCritere)).getLibelle());
        switch (selectedCritere.getAttribut()) {
            case "idagence_idCompte":
                loadAgence();
                selectedCritere.setValSelect(currentUserAgence.get(0).getIdagence());
                filtreCompte();
                break;
        }
        if (selectedCritere.getType().equals("String")) {
            selectedCritere.setValString("");
        }
    }

    public void rechercheOperations() {
        listOperations.clear();
        switch (selectedCritere.getAttribut()) {
            case "Tous":
                listOperations.addAll(categorie == 1 ? operationsFacade.findAll_Etat(selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence) : operationsFacade.findAll_Etat(selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (categorie == 1 ? !operationsFacade.findAll_Etat("Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty() : !operationsFacade.findAll_Etat("Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty()) : true);
                selectedCritere.setUsedValue(routine.localizeMessage("Tous"));
                break;
            case "idagence_idCompte":
                List<Agence> agenceList = new ArrayList<>();
                agenceList.add(new Agence(selectedCritere.getValSelect()));
                if (selectedCritere.getValSelect2() == null) {
                    listOperations.addAll(operationsFacade.findAll_Etat(selectedCritere.getEtat().toUpperCase(ROOT), agenceList));
                    routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!operationsFacade.findAll_Etat("Supprime".toUpperCase(ROOT), agenceList).isEmpty()) : true);
                    selectedCritere.setUsedValue(agenceFacade.find(selectedCritere.getValSelect()).getRaisonsociale());
                } else {
                    listOperations.addAll(operationsFacade.findByIdCompte_Etat(selectedCritere.getValSelect2(), selectedCritere.getEtat().toUpperCase(ROOT), agenceList));
                    routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!operationsFacade.findByIdCompte_Etat(selectedCritere.getValSelect2(), "Supprime".toUpperCase(ROOT), agenceList).isEmpty()) : true);
                    selectedCritere.setUsedValue(compteFacade.find(selectedCritere.getValSelect2()).getNomPrenom() + " [" + agenceFacade.find(selectedCritere.getValSelect()).getLibelle() + "]");
                }
                break;
            case "libelle":
                listOperations.addAll(FacesContext.getCurrentInstance().getViewRoot().getLocale().getLanguage().equals(new Locale("fr").getLanguage()) ? (categorie == 1 ? operationsFacade.findByLibelle_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence) : operationsFacade.findByLibelle_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence)) : (categorie == 1 ? operationsFacade.findByLabel_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence) : operationsFacade.findByLabel_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence)));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (FacesContext.getCurrentInstance().getViewRoot().getLocale().getLanguage().equals(new Locale("fr").getLanguage()) ? (categorie == 1 ? !operationsFacade.findByLibelle_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence).isEmpty() : !operationsFacade.findByLibelle_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence).isEmpty()) : (categorie == 1 ? !operationsFacade.findByLabel_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence).isEmpty() : !operationsFacade.findByLabel_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence).isEmpty())) : true);
                selectedCritere.setUsedValue(selectedCritere.getValString());
                break;
            case "cible":
                listOperations.addAll(categorie == 1 ? operationsFacade.findByCible_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence) : operationsFacade.findByCible_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (categorie == 1 ? !operationsFacade.findByCible_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", "Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty() : !operationsFacade.findByCible_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", "Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty()) : true);
                selectedCritere.setUsedValue(selectedCritere.getValString());
                break;
            case "adresseIp":
                listOperations.addAll(categorie == 1 ? operationsFacade.findByAdresseIp_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence) : operationsFacade.findByAdresseIp_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (categorie == 1 ? !operationsFacade.findByAdresseIp_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", "Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty() : !operationsFacade.findByAdresseIp_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", "Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty()) : true);
                selectedCritere.setUsedValue(selectedCritere.getValString());
                break;
            case "dateOperation":
                routine.correctDateTime(selectedCritere.getValDate1(), new Date(selectedCritere.getValDate2().getTime() + 59999L), null, null);
                listOperations.addAll(categorie == 1 ? operationsFacade.findByDateOpration_Etat(selectedCritere.getValDate1(), new Date(selectedCritere.getValDate2().getTime() + 59999L), selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence) : operationsFacade.findByDateOpration_Etat(selectedCritere.getValDate1(), new Date(selectedCritere.getValDate2().getTime() + 59999L), selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (categorie == 1 ? !operationsFacade.findByDateOpration_Etat(selectedCritere.getValDate1(), new Date(selectedCritere.getValDate2().getTime() + 59999L), "Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty() : !operationsFacade.findByDateOpration_Etat(selectedCritere.getValDate1(), new Date(selectedCritere.getValDate2().getTime() + 59999L), "Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty()) : true);
                selectedCritere.setUsedValue(new SimpleDateFormat("dd/MM/yyyy").format(selectedCritere.getValDate1()) + " - " + new SimpleDateFormat("dd/MM/yyyy").format(selectedCritere.getValDate2()));
                break;
        }
        //**********************************************************************************************************

        lazylistOperationsModel = new LazyDataModel<Operations>() {
            @Override
            public Operations getRowData(String rowKey) {
                for (Operations ctn : lazyListOperations) {
                    if (ctn.getIdOperations().toString().equals(rowKey)) {
                        return ctn;
                    }
                }
                return null;
            }

            @Override
            public Object getRowKey(Operations ctn) {
                return ctn.getIdOperations();
            }

            @Override
            public List<Operations> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, Object> filters) {
                List<Operations> data = new ArrayList<>();
                //filter
                for (Operations ctn : listOperations) {
                    boolean match = true;
                    if (filters != null) {
                        for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
                            try {
                                String filterProperty = it.next();
                                Object filterValue = filters.get(filterProperty);
                                String fieldValue = String.valueOf(ctn.getClass().getField(filterProperty).get(ctn));
                                if (filterValue == null || fieldValue.startsWith(filterValue.toString())) {
                                    match = true;
                                } else {
                                    match = false;
                                    break;
                                }
                            } catch (Exception e) {
                                match = false;
                            }
                        }
                    }
                    if (match) {
                        data.add(ctn);
                    }
                }

                //sort
                if (multiSortMeta != null) {
                    for (SortMeta sm : multiSortMeta) {
                        Collections.sort(data, new Operations(sm.getSortField(), (SortOrder.ASCENDING.equals(sm.getSortOrder()) ? 1 : -1)));
                    }
                }

                //rowCount
                int dataSize = data.size();
                this.setRowCount(dataSize);

                //paginate
                lazyListOperations.clear();
                if (dataSize > pageSize) {
                    try {
                        lazyListOperations.addAll(data.subList(first, first + pageSize));
                        generateCharts();
                        return lazyListOperations;
                    } catch (IndexOutOfBoundsException e) {
                        lazyListOperations.addAll(data.subList(first, first + (dataSize % pageSize)));
                        generateCharts();
                        return lazyListOperations;
                    }

                } else {
                    lazyListOperations.addAll(data);
                    generateCharts();
                    return lazyListOperations;
                }
            }
        };
        //-----------------------------
        tableReset();
    }

    public void rowClick() {
        routine.setImp(false);
        routine.setMof(false);
        routine.setSup(false);
        routine.setCon(false);
        routine.setCop(false);
        routine.setRes(false);
        routine.setDet(false);
        if (!listOperations.isEmpty()) {
            routine.setImp(true);
        }
        if (selectedListOperations.size() > 1) {
            if (selectedCritere.getEtat().equals("Actif")) {
                routine.setSup(true);
            } else {
                routine.setRes(true);
                routine.setDet(true);
            }
        } else if (selectedListOperations.size() == 1) {
            selectedOperations = operationsFacade.find(selectedListOperations.get(0).getIdOperations());
            //-------------------------------------------------------------------------------------------------------
            selectedCompte = selectedOperations.getIdCompte() != null ? selectedOperations.getIdCompte().getIdCompte() : null;
            //-------------------------------------------------------------------------------------------------------
            if (selectedCritere.getEtat().equals("Actif")) {
                routine.setMof(true);
                routine.setSup(true);
                routine.setCon(true);
                routine.setCop(true);
            } else {
                routine.setRes(true);
                routine.setDet(true);
            }
        }
    }

    public void btnClick(ActionEvent e) {
        esc = true;
        egc = true;
        style = "font-size: 10pt; font-weight: bold; font-family: arial;";
        CommandButton btn = (CommandButton) e.getComponent();
        operation = btn.getWidgetVar();
        loadAgence();
        loadCompte();
        switch (operation) {
            case "add":
                selectedOperations = new Operations();
                //-------------------------------------------------------------------------------------------------------
                selectedCompte = null;
                //-------------------------------------------------------------------------------------------------------
                tableReset();
                break;
            case "con":
                style = "font-size: 10pt; font-weight: bold; font-family: arial; color: blue;";
                esc = false;
                egc = false;
                break;
            case "cor":
                if (selectedCritere.getEtat().equals("Actif")) {
                    selectedCritere.setEtat("Supprime");
                    routine.setAdd(false);
                } else {
                    selectedCritere.setEtat("Actif");
                    routine.setAdd(true);
                }
                rechercheOperations();
                break;
        }
    }

    public void delete(ActionEvent e) {
        MenuItem btn = (MenuItem) e.getComponent();
        operation = btn.getId();
    }

    public void restaure(ActionEvent e) {
        MenuItem btn = (MenuItem) e.getComponent();
        operation = btn.getId();
    }

    public void destroy(ActionEvent e) {
        MenuItem btn = (MenuItem) e.getComponent();
        operation = btn.getId();
    }

    public void persist() {
        if (!lock) {
            lock = true;
            Thread locker = new Thread() {
                @Override
                public synchronized void run() {
                    try {

                        wait(3000);
                        lock = false;
                    } catch (Exception e) {
                        lock = false;
                        e.printStackTrace();
                    }
                }
            };
            locker.start();
            //----------------------------------------------------
            String libelleOperation = "", libelleOperation_en = "";
            String langue = FacesContext.getCurrentInstance().getViewRoot().getLocale().getLanguage();
            try {
                ut.begin();
                switch (operation) {
                    case "sup":
                        libelleOperation = routine.localizeMessage("Supprimer", new Locale("fr")) + " " + routine.localizeMessage("Des", new Locale("fr")) + " " + routine.localizeMessage("Operation" + "s", new Locale("fr"));
                        libelleOperation_en = routine.localizeMessage("Supprimer", new Locale("en")) + " " + routine.localizeMessage("Des", new Locale("en")) + " " + routine.localizeMessage("Operation" + "s", new Locale("en"));
                        selectedListOperations.stream().map((operations_) -> {
                            operations_.setEtat("Supprime");
                            return operations_;
                        }).forEachOrdered((operations_) -> {
                            operationsFacade.edit(operations_);
                        });
                        routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, routine.localizeMessage("Operation"), connectedUser, operationsFacade);
                        routine.feedBack("Information", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Succes_operation"), null);
                        break;
                    case "res":
                        libelleOperation = routine.localizeMessage("Restaurer", new Locale("fr")) + " " + routine.localizeMessage("Des", new Locale("fr")) + " " + routine.localizeMessage("Operation" + "s", new Locale("fr"));
                        libelleOperation_en = routine.localizeMessage("Restaurer", new Locale("en")) + " " + routine.localizeMessage("Des", new Locale("en")) + " " + routine.localizeMessage("Operation" + "s", new Locale("en"));
                        selectedListOperations.stream().map((operations_) -> {
                            operations_.setEtat("Actif");
                            return operations_;
                        }).forEachOrdered((operations_) -> {
                            operationsFacade.edit(operations_);
                        });
                        routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, routine.localizeMessage("Operation"), connectedUser, operationsFacade);
                        routine.feedBack("Information", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Succes_operation"), null);
                        break;
                    case "det":
                        libelleOperation = routine.localizeMessage("Detruire", new Locale("fr")) + " " + routine.localizeMessage("Des", new Locale("fr")) + " " + routine.localizeMessage("Operation" + "s", new Locale("fr"));
                        libelleOperation_en = routine.localizeMessage("Detruire", new Locale("en")) + " " + routine.localizeMessage("Des", new Locale("en")) + " " + routine.localizeMessage("Operation" + "s", new Locale("en"));
                        selectedListOperations.forEach((operations_) -> {
                            operationsFacade.remove(operations_);
                        });
                        routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, routine.localizeMessage("Operation"), connectedUser, operationsFacade);
                        routine.feedBack("Information", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Succes_operation"), null);
                        break;
                    case "sup_all":
                        libelleOperation = routine.localizeMessage("Supprimer", new Locale("fr")) + " " + routine.localizeMessage("Des", new Locale("fr")) + " " + routine.localizeMessage("Operation" + "s", new Locale("fr"));
                        libelleOperation_en = routine.localizeMessage("Supprimer", new Locale("en")) + " " + routine.localizeMessage("Des", new Locale("en")) + " " + routine.localizeMessage("Operation" + "s", new Locale("en"));
                        lazyListOperations.stream().map((operations_) -> {
                            operations_.setEtat("Supprime");
                            return operations_;
                        }).forEachOrdered((operations_) -> {
                            operationsFacade.edit(operations_);
                        });
                        routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, routine.localizeMessage("Operation"), connectedUser, operationsFacade);
                        routine.feedBack("Information", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Succes_operation"), null);
                        break;
                    case "res_all":
                        libelleOperation = routine.localizeMessage("Restaurer", new Locale("fr")) + " " + routine.localizeMessage("Des", new Locale("fr")) + " " + routine.localizeMessage("Operation" + "s", new Locale("fr"));
                        libelleOperation_en = routine.localizeMessage("Restaurer", new Locale("en")) + " " + routine.localizeMessage("Des", new Locale("en")) + " " + routine.localizeMessage("Operation" + "s", new Locale("en"));
                        lazyListOperations.stream().map((operations_) -> {
                            operations_.setEtat("Actif");
                            return operations_;
                        }).forEachOrdered((operations_) -> {
                            operationsFacade.edit(operations_);
                        });
                        routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, routine.localizeMessage("Operation"), connectedUser, operationsFacade);
                        routine.feedBack("Information", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Succes_operation"), null);
                        break;
                    case "det_all":
                        libelleOperation = routine.localizeMessage("Detruire", new Locale("fr")) + " " + routine.localizeMessage("Des", new Locale("fr")) + " " + routine.localizeMessage("Operation" + "s", new Locale("fr"));
                        libelleOperation_en = routine.localizeMessage("Detruire", new Locale("en")) + " " + routine.localizeMessage("Des", new Locale("en")) + " " + routine.localizeMessage("Operation" + "s", new Locale("en"));
                        lazyListOperations.forEach((operations_) -> {
                            operationsFacade.remove(operations_);
                        });
                        routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, routine.localizeMessage("Operation"), connectedUser, operationsFacade);
                        routine.feedBack("Information", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Succes_operation"), null);
                        break;
                }
                ut.commit();
            } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException e) {
                routine.feedBack("Erreur", libelleOperation, null, e);
                e.printStackTrace();
            }
            rechercheOperations();
        }
    }

    public String imprimer() {
        try {
            JRBeanCollectionDataSource beanCollectionDataSource;
            if (selectedListOperations.isEmpty()) {
                beanCollectionDataSource = new JRBeanCollectionDataSource(lazyListOperations);
            } else {
                beanCollectionDataSource = new JRBeanCollectionDataSource(selectedListOperations);
            }
            String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("resources/etats/operations.jasper");
            Map parameters = new HashMap();
            selectedCritere.setAgence(currentUserAgence.get(0));
            parameters.put("REPORT_FILTER", selectedCritere);
            parameters.put("USER", connectedUser);
            parameters.put("REPORT_LOCALE", FacesContext.getCurrentInstance().getViewRoot().getLocale());
            parameters.put("CATEGORIE", categorie);
            JasperPrint jasperPrint = fillReport(reportPath, parameters, beanCollectionDataSource);
            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            httpServletResponse.addHeader("Content-disposition", "attachment; filename=ListeOperations.pdf");
            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
            exportReportToPdfStream(jasperPrint, servletOutputStream);
            servletOutputStream.close();
            FacesContext.getCurrentInstance().responseComplete();
            //----------------------------------------------
            String libelleOperation = routine.localizeMessage("Impression", new Locale("fr")) + " (" + routine.localizeMessage("Operation", new Locale("fr")) + ")";
            String libelleOperation_en = routine.localizeMessage("Impression", new Locale("en")) + " (" + routine.localizeMessage("Operation", new Locale("en")) + ")";
            routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, routine.localizeMessage("Operation"), connectedUser, operationsFacade);
            //----------------------------------------------
        } catch (JRException | IOException e) {
            e.printStackTrace();
        }
        ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
        return ext.getRequestServletPath() + "?faces-redirect=true";
    }

    public void fitreGlobal() {
        filtreCompte();
    }

    public void filtreCompte() {
        listCompte.clear();
        if (selectedCritere.getValSelect() != null) {
            List<Agence> agenceList = new ArrayList();
            agenceList.add(new Agence(selectedCritere.getValSelect()));
            listCompte.addAll(compteFacade.findAll_Etat("Actif".toUpperCase(ROOT), agenceList));
        }
    }

    public void loadCompte() {
        listCompte.clear();
        listCompte.addAll(compteFacade.findAll_Etat("Actif".toUpperCase(ROOT), currentUserAgence));
    }

    public void loadAgence() {
        listAgence.clear();
        listAgence.addAll(currentUserAgence);
    }
    //--------------------------------------------------

    public String operations() {
        return "operations.xhtml?faces-redirect=true";
    }

    public void tableReset() {
        if (selectedListOperations != null) {
            selectedListOperations.clear();
        } else {
            selectedListOperations = new ArrayList<>();
        }
        rowClick();
    }

    public String closePage() {
        return "moduleSecurite.xhtml?faces-redirect=true";
    }

    public OperationsFacadeLocal getOperationsFacade() {
        return operationsFacade;
    }

    public void setOperationsFacade(OperationsFacadeLocal operationsFacade) {
        this.operationsFacade = operationsFacade;
    }

    public List<Operations> getListOperations() {
        return listOperations;
    }

    public void setListOperations(List<Operations> listOperations) {
        this.listOperations = listOperations;
    }

    public Routine getRoutine() {
        return routine;
    }

    public void setRoutine(Routine routine) {
        this.routine = routine;
    }

    public UserTransaction getUt() {
        return ut;
    }

    public void setUt(UserTransaction ut) {
        this.ut = ut;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public boolean isEsc() {
        return esc;
    }

    public void setEsc(boolean esc) {
        this.esc = esc;
    }

    public boolean isEgc() {
        return egc;
    }

    public void setEgc(boolean egc) {
        this.egc = egc;
    }

    public Operations getSelectedOperations() {
        return selectedOperations;
    }

    public void setSelectedOperations(Operations selectedOperations) {
        this.selectedOperations = selectedOperations;
    }

    public List<Operations> getSelectedListOperations() {
        return selectedListOperations;
    }

    public void setSelectedListOperations(List<Operations> selectedListOperations) {
        this.selectedListOperations = selectedListOperations;
    }

    public Compte getConnectedUser() {
        return connectedUser;
    }

    public void setConnectedUser(Compte connectedUser) {
        this.connectedUser = connectedUser;
    }

    public CompteFacadeLocal getCompteFacade() {
        return compteFacade;
    }

    public void setCompteFacade(CompteFacadeLocal compteFacade) {
        this.compteFacade = compteFacade;
    }

    public List<Compte> getListCompte() {
        return listCompte;
    }

    public void setListCompte(List<Compte> listCompte) {
        this.listCompte = listCompte;
    }

    public Integer getSelectedCompte() {
        return selectedCompte;
    }

    public void setSelectedCompte(Integer selectedCompte) {
        this.selectedCompte = selectedCompte;
    }

    public AgenceFacadeLocal getAgenceFacade() {
        return agenceFacade;
    }

    public void setAgenceFacade(AgenceFacadeLocal agenceFacade) {
        this.agenceFacade = agenceFacade;
    }

    public List<Agence> getListAgence() {
        return listAgence;
    }

    public void setListAgence(List<Agence> listAgence) {
        this.listAgence = listAgence;
    }

    public Integer getSelectedAgence() {
        return selectedAgence;
    }

    public void setSelectedAgence(Integer selectedAgence) {
        this.selectedAgence = selectedAgence;
    }

    public List<CritereFiltre> getListCritereFiltre() {
        return listCritereFiltre;
    }

    public void setListCritereFiltre(List<CritereFiltre> listCritereFiltre) {
        this.listCritereFiltre = listCritereFiltre;
    }

    public CritereFiltre getSelectedCritere() {
        return selectedCritere;
    }

    public void setSelectedCritere(CritereFiltre selectedCritere) {
        this.selectedCritere = selectedCritere;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public PieChartModel getPcmOperation() {
        return pcmOperation;
    }

    public void setPcmOperation(PieChartModel pcmOperation) {
        this.pcmOperation = pcmOperation;
    }

    public int getJan() {
        return jan;
    }

    public void setJan(int jan) {
        this.jan = jan;
    }

    public int getFev() {
        return fev;
    }

    public void setFev(int fev) {
        this.fev = fev;
    }

    public int getMar() {
        return mar;
    }

    public void setMar(int mar) {
        this.mar = mar;
    }

    public int getAvr() {
        return avr;
    }

    public void setAvr(int avr) {
        this.avr = avr;
    }

    public int getMai() {
        return mai;
    }

    public void setMai(int mai) {
        this.mai = mai;
    }

    public int getJui() {
        return jui;
    }

    public void setJui(int jui) {
        this.jui = jui;
    }

    public int getJul() {
        return jul;
    }

    public void setJul(int jul) {
        this.jul = jul;
    }

    public int getAou() {
        return aou;
    }

    public void setAou(int aou) {
        this.aou = aou;
    }

    public int getSep() {
        return sep;
    }

    public void setSep(int sep) {
        this.sep = sep;
    }

    public int getOct() {
        return oct;
    }

    public void setOct(int oct) {
        this.oct = oct;
    }

    public int getNov() {
        return nov;
    }

    public void setNov(int nov) {
        this.nov = nov;
    }

    public int getDec() {
        return dec;
    }

    public void setDec(int dec) {
        this.dec = dec;
    }

    public BarChartModel getBcmOperation() {
        return bcmOperation;
    }

    public void setBcmOperation(BarChartModel bcmOperation) {
        this.bcmOperation = bcmOperation;
    }

    public PieChartModel getPcmOperationFill() {
        return pcmOperationFill;
    }

    public void setPcmOperationFill(PieChartModel pcmOperationFill) {
        this.pcmOperationFill = pcmOperationFill;
    }

    public LineChartModel getLcmOperation() {
        return lcmOperation;
    }

    public void setLcmOperation(LineChartModel lcmOperation) {
        this.lcmOperation = lcmOperation;
    }

    public Integer getCategorie() {
        return categorie;
    }

    public void setCategorie(Integer categorie) {
        this.categorie = categorie;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public DashboardModel getModuleSecuriteDashboard() {
        return moduleSecuriteDashboard;
    }

    public void setModuleSecuriteDashboard(DashboardModel moduleSecuriteDashboard) {
        this.moduleSecuriteDashboard = moduleSecuriteDashboard;
    }

    public int getAgences() {
        return agences;
    }

    public void setAgences(int agences) {
        this.agences = agences;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getClient() {
        return client;
    }

    public void setClient(int client) {
        this.client = client;
    }

    public int getUser_online() {
        return user_online;
    }

    public void setUser_online(int user_online) {
        this.user_online = user_online;
    }

    public int getClient_online() {
        return client_online;
    }

    public void setClient_online(int client_online) {
        this.client_online = client_online;
    }

    public int getUser_most() {
        return user_most;
    }

    public void setUser_most(int user_most) {
        this.user_most = user_most;
    }

    public int getClient_most() {
        return client_most;
    }

    public void setClient_most(int client_most) {
        this.client_most = client_most;
    }

    public int getGroupe() {
        return groupe;
    }

    public void setGroupe(int groupe) {
        this.groupe = groupe;
    }

    public int getGaleri() {
        return galeri;
    }

    public void setGaleri(int galeri) {
        this.galeri = galeri;
    }

    public int getPartners() {
        return partners;
    }

    public void setPartners(int partners) {
        this.partners = partners;
    }

    public int getPub() {
        return pub;
    }

    public void setPub(int pub) {
        this.pub = pub;
    }

    public MultimediaFacadeLocal getMultimediaFacade() {
        return multimediaFacade;
    }

    public void setMultimediaFacade(MultimediaFacadeLocal multimediaFacade) {
        this.multimediaFacade = multimediaFacade;
    }

    public GroupeUtilisateurFacadeLocal getGroupeUtilisateurFacade() {
        return groupeUtilisateurFacade;
    }

    public void setGroupeUtilisateurFacade(GroupeUtilisateurFacadeLocal groupeUtilisateurFacade) {
        this.groupeUtilisateurFacade = groupeUtilisateurFacade;
    }

    public List<Compte> getListCompteMost() {
        return listCompteMost;
    }

    public void setListCompteMost(List<Compte> listCompteMost) {
        this.listCompteMost = listCompteMost;
    }

    public int getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(int etudiant) {
        this.etudiant = etudiant;
    }

    public int getEtudiant_online() {
        return etudiant_online;
    }

    public void setEtudiant_online(int etudiant_online) {
        this.etudiant_online = etudiant_online;
    }

    public int getEtudiant_most() {
        return etudiant_most;
    }

    public void setEtudiant_most(int etudiant_most) {
        this.etudiant_most = etudiant_most;
    }

    public int getJanTotal() {
        return janTotal;
    }

    public void setJanTotal(int janTotal) {
        this.janTotal = janTotal;
    }

    public int getFevTotal() {
        return fevTotal;
    }

    public void setFevTotal(int fevTotal) {
        this.fevTotal = fevTotal;
    }

    public int getMarTotal() {
        return marTotal;
    }

    public void setMarTotal(int marTotal) {
        this.marTotal = marTotal;
    }

    public int getAvrTotal() {
        return avrTotal;
    }

    public void setAvrTotal(int avrTotal) {
        this.avrTotal = avrTotal;
    }

    public int getMaiTotal() {
        return maiTotal;
    }

    public void setMaiTotal(int maiTotal) {
        this.maiTotal = maiTotal;
    }

    public int getJuiTotal() {
        return juiTotal;
    }

    public void setJuiTotal(int juiTotal) {
        this.juiTotal = juiTotal;
    }

    public int getJulTotal() {
        return julTotal;
    }

    public void setJulTotal(int julTotal) {
        this.julTotal = julTotal;
    }

    public int getAouTotal() {
        return aouTotal;
    }

    public void setAouTotal(int aouTotal) {
        this.aouTotal = aouTotal;
    }

    public int getSepTotal() {
        return sepTotal;
    }

    public void setSepTotal(int sepTotal) {
        this.sepTotal = sepTotal;
    }

    public int getOctTotal() {
        return octTotal;
    }

    public void setOctTotal(int octTotal) {
        this.octTotal = octTotal;
    }

    public int getNovTotal() {
        return novTotal;
    }

    public void setNovTotal(int novTotal) {
        this.novTotal = novTotal;
    }

    public int getDecTotal() {
        return decTotal;
    }

    public void setDecTotal(int decTotal) {
        this.decTotal = decTotal;
    }

    public int getBureau() {
        return bureau;
    }

    public void setBureau(int bureau) {
        this.bureau = bureau;
    }

    public List<Agence> getCurrentUserAgence() {
        return currentUserAgence;
    }

    public void setCurrentUserAgence(List<Agence> currentUserAgence) {
        this.currentUserAgence = currentUserAgence;
    }

    public LazyDataModel<Operations> getLazylistOperationsModel() {
        return lazylistOperationsModel;
    }

    public void setLazylistOperationsModel(LazyDataModel<Operations> lazylistOperationsModel) {
        this.lazylistOperationsModel = lazylistOperationsModel;
    }

    public List<Operations> getLazyListOperations() {
        return lazyListOperations;
    }

    public void setLazyListOperations(List<Operations> lazyListOperations) {
        this.lazyListOperations = lazyListOperations;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public int getMobile() {
        return mobile;
    }

    public void setMobile(int mobile) {
        this.mobile = mobile;
    }

}
