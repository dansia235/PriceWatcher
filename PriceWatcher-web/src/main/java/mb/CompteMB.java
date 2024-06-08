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
import entities.Notifications;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import static java.lang.Integer.valueOf;
import static java.lang.System.currentTimeMillis;
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
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.imageio.stream.FileImageOutputStream;
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
import org.primefaces.component.commandlink.CommandLink;
import org.primefaces.component.selectbooleancheckbox.SelectBooleanCheckbox;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CaptureEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.CroppedImage;
import org.primefaces.model.DualListModel;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.primefaces.model.UploadedFile;
import sessions.AgenceFacadeLocal;
import sessions.CompteFacadeLocal;
import sessions.OperationsFacadeLocal;
import sessions.NotificationsFacadeLocal;
import sessions.ConcurrentFacadeLocal;

/**
 *
 * @author Lucien FOTSA
 */
public class CompteMB implements Serializable, Converter {

    @Resource
    private UserTransaction ut;
    @EJB
    private OperationsFacadeLocal operationsFacade;
    //--------------------------------------------------------------------------
    @EJB
    private ConcurrentFacadeLocal comptoirFacade;
    //--------------------------------------------------------------------------
    @EJB
    private CompteFacadeLocal compteFacade;
    private List<Compte> listCompte = new ArrayList<>();
    private List<Compte> selectedListCompte = new ArrayList<>();
    private Compte selectedCompte = new Compte();
    private String oldValue;
    //--------------------------------------------------------------------------
    private Compte connectedUser;
    private Routine routine = new Routine();
    private String operation = "add";
    private boolean esc = true;//Etat spécifique d'un composant
    private boolean egc = true;//Etat générique d'un composant
    private String style = "font-size: 10pt; font-weight: bold; font-family: arial;";
    //--------------------------------------------------------------------------
    @EJB
    private AgenceFacadeLocal agenceFacade;
    private List<Agence> listAgence = new ArrayList<>();
    private Integer selectedAgence;
    private DualListModel<Agence> listAgenceOthers = new DualListModel<>(new ArrayList<>(), new ArrayList<>());
    //--------------------------------------------
    private List<CritereFiltre> listCritereFiltre = new ArrayList<>();
    private CritereFiltre selectedCritere = new CritereFiltre("Tous", routine.localizeMessage("Tous"), "Tous", "Actif");
    private Boolean setPwd = false;
    private CroppedImage croppedImage = new CroppedImage();
    private String imageTemp;
    private String imageTempRelatif;
    private String captureMode;
    //---------------------------------------------------------
    private List<Agence> currentUserAgence = new ArrayList<>();
    private LazyDataModel<Compte> lazylistCompteModel;
    private List<Compte> lazyListCompte = new ArrayList<>();
    private boolean lock = false;
    //--------------------------------------------------------------------------
    @EJB
    private NotificationsFacadeLocal notificationsmsFacade;
    private Notifications selectedNotificationsms = new Notifications();
    private boolean modeleSms = false;
    private boolean showDialog = false;
    private boolean canSave = false;
    private String notifTitle = "Communication";
    private volatile int success;
    private Date today = new Date(System.currentTimeMillis());

    /**
     * Creates a new instance of CompteMB
     */
    public CompteMB() {
    }

    @PostConstruct
    private void init() {
        connectedUser = (Compte) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUser");
        currentUserAgence = (List<Agence>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUserAgence");
        loadCritere();
        selectCritere();
//        rechercheCompte();
    }

    //--------------------------------------------------
    public void loadCritere() {
        listCritereFiltre.clear();
        listCritereFiltre.add(new CritereFiltre("Tous", routine.localizeMessage("Tous"), "Tous", "Actif"));
        listCritereFiltre.add(new CritereFiltre("login", routine.localizeMessage("Login"), "String", "Actif"));
        listCritereFiltre.add(new CritereFiltre("nom", routine.localizeMessage("Nom"), "String", "Actif"));
        listCritereFiltre.add(new CritereFiltre("prenom", routine.localizeMessage("Prenom"), "String", "Actif"));
        listCritereFiltre.add(new CritereFiltre("login_etat", routine.localizeMessage("Login") + " & " + routine.localizeMessage("Etat"), "StringLogin", "Actif"));
        listCritereFiltre.add(new CritereFiltre("nom_etat", routine.localizeMessage("Nom") + " & " + routine.localizeMessage("Etat"), "StringNom", "Actif"));
        listCritereFiltre.add(new CritereFiltre("prenom_etat", routine.localizeMessage("Prenom") + " & " + routine.localizeMessage("Etat"), "StringPrenom", "Actif"));
        listCritereFiltre.add(new CritereFiltre("dateNaissance", routine.localizeMessage("Date") + " " + routine.localizeMessage("De_p") + " " + routine.localizeMessage("Naissance"), "Date", "Actif"));
        listCritereFiltre.add(new CritereFiltre("sexe", routine.localizeMessage("Sexe"), "Select2", "Actif"));
        listCritereFiltre.add(new CritereFiltre("contact", routine.localizeMessage("Contact"), "String", "Actif"));
        listCritereFiltre.add(new CritereFiltre("actif", routine.localizeMessage("Actif"), "Boolean", "Actif"));
        listCritereFiltre.add(new CritereFiltre("connexion", routine.localizeMessage("Connecte"), "Select7", "Actif"));
        listCritereFiltre.add(new CritereFiltre("dateEnregistre", routine.localizeMessage("DateEnregistre"), "Date", "Actif"));
        listCritereFiltre.add(new CritereFiltre("derniereModif", routine.localizeMessage("DerniereModif"), "Date", "Actif"));
    }

    public void selectCritere() {
        selectedCritere.setType(listCritereFiltre.get(listCritereFiltre.indexOf(selectedCritere)).getType());
        selectedCritere.setLibelle(listCritereFiltre.get(listCritereFiltre.indexOf(selectedCritere)).getLibelle());
        if (selectedCritere.getType().equals("String")) {
            selectedCritere.setValString("");
        }
    }

    public void rechercheCompte() {
        listCompte.clear();
        switch (selectedCritere.getAttribut()) {
            case "Tous":
                listCompte.addAll(compteFacade.findAll_Etat(selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!compteFacade.findAll_Etat("Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty()) : true);
                selectedCritere.setUsedValue(routine.localizeMessage("Tous"));
                break;
            case "login":
                listCompte.addAll(compteFacade.findByLogin_Etat(selectedCritere.getValString().toUpperCase(ROOT), selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!compteFacade.findByLogin_Etat(selectedCritere.getValString().toUpperCase(ROOT), "Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty()) : true);
                selectedCritere.setUsedValue(selectedCritere.getValString());
                break;
            case "nom":
                listCompte.addAll(compteFacade.findByNom_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!compteFacade.findByNom_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", "Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty()) : true);
                selectedCritere.setUsedValue(selectedCritere.getValString());
                break;
            case "prenom":
                listCompte.addAll(compteFacade.findByPrenom_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!compteFacade.findByPrenom_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", "Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty()) : true);
                selectedCritere.setUsedValue(selectedCritere.getValString());
                break;
            case "login_etat":
                listCompte.addAll(compteFacade.findByLogin_Etat(selectedCritere.getValString().toUpperCase(ROOT), selectedCritere.getValString2().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!compteFacade.findByLogin_Etat(selectedCritere.getValString().toUpperCase(ROOT), "Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty()) : true);
                selectedCritere.setUsedValue(selectedCritere.getValString());
                break;
            case "nom_etat":
                listCompte.addAll(compteFacade.findByNom_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", selectedCritere.getValString2().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!compteFacade.findByNom_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", "Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty()) : true);
                selectedCritere.setUsedValue(selectedCritere.getValString());
                break;
            case "prenom_etat":
                listCompte.addAll(compteFacade.findByPrenom_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", selectedCritere.getValString2().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!compteFacade.findByPrenom_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", "Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty()) : true);
                selectedCritere.setUsedValue(selectedCritere.getValString());
                break;
            case "dateNaissance":
                routine.correctDateTime(selectedCritere.getValDate1(), new Date(selectedCritere.getValDate2().getTime() + 59999L), null, null);
                listCompte.addAll(compteFacade.findByDateNaissance_Etat(selectedCritere.getValDate1(), new Date(selectedCritere.getValDate2().getTime() + 59999L), selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!compteFacade.findByDateNaissance_Etat(selectedCritere.getValDate1(), new Date(selectedCritere.getValDate2().getTime() + 59999L), "Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty()) : true);
                selectedCritere.setUsedValue(new SimpleDateFormat("dd/MM/yyyy").format(selectedCritere.getValDate1()) + " - " + new SimpleDateFormat("dd/MM/yyyy").format(selectedCritere.getValDate2()));
                break;
            case "sexe":
                listCompte.addAll(compteFacade.findBySexe_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!compteFacade.findBySexe_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", "Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty()) : true);
                selectedCritere.setUsedValue(selectedCritere.getValString());
                break;
            case "contact":
                listCompte.addAll(compteFacade.findByContact_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!compteFacade.findByContact_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", "Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty()) : true);
                selectedCritere.setUsedValue(selectedCritere.getValString());
                break;
            case "actif":
                listCompte.addAll(compteFacade.findByActif_Etat(selectedCritere.getValBool(), selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!compteFacade.findByActif_Etat(selectedCritere.getValBool(), "Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty()) : true);
                selectedCritere.setUsedValue(selectedCritere.getValBool().toString());
                break;
            case "connexion":
                listCompte.addAll(compteFacade.findByConnexion_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!compteFacade.findByConnexion_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", "Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty()) : true);
                selectedCritere.setUsedValue(selectedCritere.getValString());
                break;
            case "dateEnregistre":
                routine.correctDateTime(selectedCritere.getValDate1(), new Date(selectedCritere.getValDate2().getTime() + 59999L), null, null);
                listCompte.addAll(compteFacade.findByDateEnregistre_Etat(selectedCritere.getValDate1(), new Date(selectedCritere.getValDate2().getTime() + 59999L), selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!compteFacade.findByDateEnregistre_Etat(selectedCritere.getValDate1(), new Date(selectedCritere.getValDate2().getTime() + 59999L), "Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty()) : true);
                selectedCritere.setUsedValue(new SimpleDateFormat("dd/MM/yyyy").format(selectedCritere.getValDate1()) + " - " + new SimpleDateFormat("dd/MM/yyyy").format(selectedCritere.getValDate2()));
                break;
            case "derniereModif":
                routine.correctDateTime(selectedCritere.getValDate1(), new Date(selectedCritere.getValDate2().getTime() + 59999L), null, null);
                listCompte.addAll(compteFacade.findByDerniereModif_Etat(selectedCritere.getValDate1(), new Date(selectedCritere.getValDate2().getTime() + 59999L), selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!compteFacade.findByDerniereModif_Etat(selectedCritere.getValDate1(), new Date(selectedCritere.getValDate2().getTime() + 59999L), "Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty()) : true);
                selectedCritere.setUsedValue(new SimpleDateFormat("dd/MM/yyyy").format(selectedCritere.getValDate1()) + " - " + new SimpleDateFormat("dd/MM/yyyy").format(selectedCritere.getValDate2()));
                break;
        }
        //**********************************************************************************************************

        lazylistCompteModel = new LazyDataModel<Compte>() {
            @Override
            public Compte getRowData(String rowKey) {
                for (Compte ctn : lazyListCompte) {
                    if (ctn.getIdCompte().toString().equals(rowKey)) {
                        return ctn;
                    }
                }
                return null;
            }

            @Override
            public Object getRowKey(Compte ctn) {
                return ctn.getIdCompte();
            }

            @Override
            public List<Compte> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, Object> filters) {
                List<Compte> data = new ArrayList<>();
                //filter
                for (Compte ctn : listCompte) {
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
                        Collections.sort(data, new Compte(sm.getSortField(), (SortOrder.ASCENDING.equals(sm.getSortOrder()) ? 1 : -1)));
                    }
                }

                //rowCount
                int dataSize = data.size();
                this.setRowCount(dataSize);

                //paginate
                lazyListCompte.clear();
                if (dataSize > pageSize) {
                    try {
                        lazyListCompte.addAll(data.subList(first, first + pageSize));
                        return data.subList(first, first + pageSize);
                    } catch (IndexOutOfBoundsException e) {
                        lazyListCompte.addAll(data.subList(first, first + (dataSize % pageSize)));
                        return data.subList(first, first + (dataSize % pageSize));
                    }

                } else {
                    lazyListCompte.addAll(data);
                    return data;
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
        if (!listCompte.isEmpty()) {
            routine.setImp(true);
        }
        if (selectedListCompte.size() > 1) {
            if (selectedCritere.getEtat().equals("Actif")) {
                routine.setSup(true);
            } else {
                routine.setRes(true);
                routine.setDet(true);
            }
        } else if (selectedListCompte.size() == 1) {
            selectedCompte = compteFacade.find(selectedListCompte.get(0).getIdCompte());
            if (!selectedCompte.getEtat().contains("Actif")) {
                routine.setRes(true);
            }
            oldValue = selectedCompte.getLogin() + "|" + selectedCompte.getNomPrenom();
            //-------------------------------------------------------------------------------------------------------
            selectedAgence = selectedCompte.getIdagence() != null ? selectedCompte.getIdagence().getIdagence() : null;
            loadAgencePicklistSource();
            listAgenceOthers.setTarget((List<Agence>) selectedCompte.getAgenceCollection());
            listAgenceOthers.getSource().removeAll(listAgenceOthers.getTarget());
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
        setPwd = false;
        style = "font-size: 10pt; font-weight: bold; font-family: arial;";
        CommandButton btn = (CommandButton) e.getComponent();
        operation = btn.getWidgetVar();
        loadAgence();
        switch (operation) {
            case "add":
                selectedCompte = new Compte();
                selectedCompte.setActif(true);
                //-------------------------------------------------------------------------------------------------------
                String filePath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("resources/fichiers");
                selectedCompte.setPhotoRelatif("resources/fichiers/default.png");
                selectedCompte.setPhoto(filePath + "\\default.png");
                loadAgencePicklistSource();
                listAgenceOthers.getTarget().clear();
                selectedAgence = currentUserAgence.get(0).getIdagence();
                //-------------------------------------------------------------------------------------------------------
                tableReset();
                break;
            case "con":
                style = "font-size: 10pt; font-weight: bold; font-family: arial; color: blue;";
                esc = false;
                egc = false;
                break;
            case "mod":
                break;
            case "cop":
                break;
            case "cor":
                if (selectedCritere.getEtat().equals("Actif")) {
                    selectedCritere.setEtat("Supprime");
                    routine.setAdd(false);
                } else {
                    selectedCritere.setEtat("Actif");
                    routine.setAdd(true);
                }
                rechercheCompte();
                break;
        }
    }

    public void activer(AjaxBehaviorEvent e) {
        oldValue = "";
        String libelleOperation = "", libelleOperation_en = "";
        String langue = FacesContext.getCurrentInstance().getViewRoot().getLocale().getLanguage();
        try {
            libelleOperation = routine.localizeMessage("Modifier", new Locale("fr")) + " " + routine.localizeMessage("Un", new Locale("fr")) + " " + routine.localizeMessage("Compte", new Locale("fr")) + " (" + routine.localizeMessage("Activer", new Locale("fr")) + ")";
            libelleOperation_en = routine.localizeMessage("Modifier", new Locale("en")) + " " + routine.localizeMessage("Un", new Locale("en")) + " " + routine.localizeMessage("Compte", new Locale("en")) + " (" + routine.localizeMessage("Activer", new Locale("en")) + ")";
            SelectBooleanCheckbox sbc = (SelectBooleanCheckbox) e.getComponent();
            selectedCompte = compteFacade.find(Integer.parseInt(sbc.getLabel()));
            selectedCompte.setActif(!selectedCompte.getActif());
            compteFacade.edit(selectedCompte);
            routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, selectedCompte.getNomPrenom(), connectedUser, operationsFacade);
            routine.feedBack("Information", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Succes_operation"), null);
            rechercheCompte();
        } catch (Exception ex) {
            routine.feedBack("Erreur", langue.equals("fr") ? libelleOperation : libelleOperation_en, null, ex);
            ex.printStackTrace();
        }
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
                List<Agence> agenceList = new ArrayList<>();
                agenceList.add(new Agence(selectedAgence));
                ut.begin();
                selectedCompte.setLogin(selectedCompte.getLogin() != null ? selectedCompte.getLogin().trim() : "");
                selectedCompte.setNom(selectedCompte.getNom() != null ? selectedCompte.getNom().trim() : "");
                selectedCompte.setPrenom(selectedCompte.getPrenom() != null ? selectedCompte.getPrenom().trim() : "");
                switch (operation) {
                    case "add":
                        libelleOperation = routine.localizeMessage("Ajouter", new Locale("fr")) + " " + routine.localizeMessage("Un", new Locale("fr")) + " " + routine.localizeMessage("Compte", new Locale("fr"));
                        libelleOperation_en = routine.localizeMessage("Ajouter", new Locale("en")) + " " + routine.localizeMessage("Un", new Locale("en")) + " " + routine.localizeMessage("Compte", new Locale("en"));
                        selectedCompte.setFax(selectedCompte.getMdp());
                        if (setPwd) {
                            selectedCompte.setMdp(selectedCompte.getMdp().hashCode() + "");
                        }
                        if ((setPwd ? (compteFacade.findByLogin_Etat(selectedCompte.getLogin().toUpperCase(ROOT), "%%", agenceList).isEmpty()) : true) && compteFacade.findByNomPrenom_Etat(selectedCompte.getNom().toUpperCase(ROOT), selectedCompte.getPrenom().toUpperCase(ROOT), "Actif".toUpperCase(ROOT), agenceList).isEmpty()) {
                            selectedCompte.setConnexion("false");
                            selectedCompte.setLangue("fr");
                            selectedCompte.setEtat("Actif");
                            selectedCompte.setDateEnregistre(new Date(currentTimeMillis()));
                            selectedCompte.setDerniereModif(new Date(currentTimeMillis()));
                            //-------------------------------------------------------------------------------------------------------
                            selectedCompte.setIdagence(selectedAgence != null ? new Agence(selectedAgence) : null);
                            selectedCompte.setAgenceCollection(listAgenceOthers.getTarget());
                            //-------------------------------------------------------------------------------------------------------
                            selectedCompte.setIdCompte(compteFacade.nextId());
                            compteFacade.create(selectedCompte);
                            routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, selectedCompte.getNomPrenom(), connectedUser, operationsFacade);
                            routine.feedBack("Information", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Succes_operation"), null);
                        } else {
                            routine.feedBack("Avertissement", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Doublon"), null);
                        }
                        break;
                    case "cop":
                        libelleOperation = routine.localizeMessage("Copier", new Locale("fr")) + " " + routine.localizeMessage("Un", new Locale("fr")) + " " + routine.localizeMessage("Compte", new Locale("fr"));
                        libelleOperation_en = routine.localizeMessage("Copier", new Locale("en")) + " " + routine.localizeMessage("Un", new Locale("en")) + " " + routine.localizeMessage("Compte", new Locale("en"));
                        selectedCompte.setFax(selectedCompte.getMdp());
                        if (setPwd) {
                            selectedCompte.setMdp(selectedCompte.getMdp().hashCode() + "");
                        }
                        if ((setPwd ? (compteFacade.findByLogin_Etat(selectedCompte.getLogin().toUpperCase(ROOT), "%%", agenceList).isEmpty()) : true) && compteFacade.findByNomPrenom_Etat(selectedCompte.getNom().toUpperCase(ROOT), selectedCompte.getPrenom().toUpperCase(ROOT), "Actif".toUpperCase(ROOT), agenceList).isEmpty()) {
                            selectedCompte.setDateEnregistre(new Date(currentTimeMillis()));
                            selectedCompte.setDerniereModif(new Date(currentTimeMillis()));
                            //-------------------------------------------------------------------------------------------------------
                            selectedCompte.setIdagence(selectedAgence != null ? new Agence(selectedAgence) : null);
                            selectedCompte.setAgenceCollection(listAgenceOthers.getTarget());
                            //-------------------------------------------------------------------------------------------------------
                            selectedCompte.setIdCompte(compteFacade.nextId());
                            compteFacade.create(selectedCompte);
                            routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, selectedCompte.getNomPrenom(), connectedUser, operationsFacade);
                            routine.feedBack("Information", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Succes_operation"), null);
                        } else {
                            routine.feedBack("Avertissement", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Doublon"), null);
                        }
                        break;
                    case "mod":
                        libelleOperation = routine.localizeMessage("Modifier", new Locale("fr")) + " " + routine.localizeMessage("Un", new Locale("fr")) + " " + routine.localizeMessage("Compte", new Locale("fr"));
                        libelleOperation_en = routine.localizeMessage("Modifier", new Locale("en")) + " " + routine.localizeMessage("Un", new Locale("en")) + " " + routine.localizeMessage("Compte", new Locale("en"));
                        selectedCompte.setFax(selectedCompte.getMdp());
                        if (setPwd) {
                            selectedCompte.setMdp(selectedCompte.getMdp().hashCode() + "");
                        }
                        if ((oldValue.equals(selectedCompte.getLogin() + "|" + selectedCompte.getNomPrenom())) || (((!(oldValue.substring(0, oldValue.lastIndexOf('|'))).equals(selectedCompte.getLogin()) && (oldValue.substring(oldValue.lastIndexOf('|') + 1)).equals(selectedCompte.getNomPrenom())) && (setPwd ? (compteFacade.findByLogin_Etat(selectedCompte.getLogin().toUpperCase(ROOT), "%%", agenceList).isEmpty()) : true)) || ((!(oldValue.substring(oldValue.lastIndexOf('|') + 1)).equals(selectedCompte.getNomPrenom()) && (oldValue.substring(0, oldValue.lastIndexOf('|'))).equals(selectedCompte.getLogin())) && compteFacade.findByNomPrenom_Etat(selectedCompte.getNom().toUpperCase(ROOT), selectedCompte.getPrenom().toUpperCase(ROOT), "Actif".toUpperCase(ROOT), agenceList).isEmpty()) || ((!(oldValue.substring(oldValue.lastIndexOf('|') + 1)).equals(selectedCompte.getNomPrenom()) && !(oldValue.substring(0, oldValue.lastIndexOf('|'))).equals(selectedCompte.getLogin())) && compteFacade.findByNomPrenom_Etat(selectedCompte.getNom().toUpperCase(ROOT), selectedCompte.getPrenom().toUpperCase(ROOT), "Actif".toUpperCase(ROOT), agenceList).isEmpty() && (setPwd ? (compteFacade.findByLogin_Etat(selectedCompte.getLogin().toUpperCase(ROOT), "%%", agenceList).isEmpty()) : true)))) {
                            selectedCompte.setDerniereModif(new Date(currentTimeMillis()));
                            //-------------------------------------------------------------------------------------------------------
                            selectedCompte.setIdagence(selectedAgence != null ? new Agence(selectedAgence) : null);
                            selectedCompte.setAgenceCollection(listAgenceOthers.getTarget());
                            //-------------------------------------------------------------------------------------------------------
                            compteFacade.edit(selectedCompte);
                            //-------------------------------------------------------------------------------------------------------
                            //-------------------------------------------------------------------------------------------------------
                            routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, selectedCompte.getNomPrenom(), connectedUser, operationsFacade);
                            routine.feedBack("Information", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Succes_operation"), null);
                        } else {
                            routine.feedBack("Avertissement", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Doublon"), null);
                        }
                        break;
                    case "sup":
                        int not = 0;
                        libelleOperation = routine.localizeMessage("Supprimer", new Locale("fr")) + " " + routine.localizeMessage("Un", new Locale("fr")) + " " + routine.localizeMessage("Compte", new Locale("fr"));
                        libelleOperation_en = routine.localizeMessage("Supprimer", new Locale("en")) + " " + routine.localizeMessage("Un", new Locale("en")) + " " + routine.localizeMessage("Compte", new Locale("en"));
                        for (Compte compte_ : selectedListCompte) {
                            if (compte_.getOperationsCollection().isEmpty() && compte_.getGroupeUtilisateurCollection().isEmpty()) {
                                compte_.setDerniereModif(new Date(currentTimeMillis()));
                                compte_.setEtat("Supprime");
                                compteFacade.edit(compte_);
                                routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, compte_.getNomPrenom(), connectedUser, operationsFacade);
                            } else {
                                not++;
                            }
                        }
                        if (not > 0) {
                            routine.feedBack("Avertissement", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Suppression_impossible") + " " + not + " " + routine.localizeMessage("Reference_impossible") + " (" + routine.localizeMessage("Operation") + " / " + routine.localizeMessage("Message") + " / " + routine.localizeMessage("GroupeUtilisateur") + ")", null);
                        } else {
                            routine.feedBack("Information", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Succes_operation"), null);
                        }
                        break;
                    case "res":
                        libelleOperation = routine.localizeMessage("Restaurer", new Locale("fr")) + " " + routine.localizeMessage("Un", new Locale("fr")) + " " + routine.localizeMessage("Compte", new Locale("fr"));
                        libelleOperation_en = routine.localizeMessage("Restaurer", new Locale("en")) + " " + routine.localizeMessage("Un", new Locale("en")) + " " + routine.localizeMessage("Compte", new Locale("en"));
                        for (Compte compte_ : selectedListCompte) {
                            if (compteFacade.findByLogin_Etat(compte_.getLogin().toUpperCase(ROOT), "Actif".toUpperCase(ROOT), agenceList).isEmpty()) {
                                compte_.setDerniereModif(new Date(currentTimeMillis()));
                                compte_.setEtat("Actif");
                                compteFacade.edit(compte_);
                                routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, compte_.getNomPrenom(), connectedUser, operationsFacade);
                            } else {
                                //libelleOperation = routine.localizeMessage("Detruire", new Locale("fr")) + " " + routine.localizeMessage("Un", new Locale("fr")) + " " + routine.localizeMessage("Compte", new Locale("fr")) + " [2]";
                                //libelleOperation_en = routine.localizeMessage("Detruire", new Locale("en")) + " " + routine.localizeMessage("Un", new Locale("en")) + " " + routine.localizeMessage("Compte", new Locale("en")) + " [2]";
                                //compteFacade.remove(compte_);
                                //routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, compte_.getNomPrenom(), connectedUser, operationsFacade);
                                routine.feedBack("Erreur", langue.equals("fr") ? libelleOperation : libelleOperation_en, null, new Exception("Login already existing !"));
                            }
                        }
                        routine.feedBack("Information", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Succes_operation"), null);
                        break;
                    case "det":
                        libelleOperation = routine.localizeMessage("Detruire", new Locale("fr")) + " " + routine.localizeMessage("Un", new Locale("fr")) + " " + routine.localizeMessage("Compte", new Locale("fr"));
                        libelleOperation_en = routine.localizeMessage("Detruire", new Locale("en")) + " " + routine.localizeMessage("Un", new Locale("en")) + " " + routine.localizeMessage("Compte", new Locale("en"));
                        for (Compte compte_ : selectedListCompte) {
                            compteFacade.remove(compte_);
                            routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, compte_.getNomPrenom(), connectedUser, operationsFacade);
                        }
                        routine.feedBack("Information", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Succes_operation"), null);
                        break;
                }
                ut.commit();
            } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException e) {
                routine.feedBack("Erreur", langue.equals("fr") ? libelleOperation : libelleOperation_en, null, e);
                e.printStackTrace();
            }
            rechercheCompte();
        }
    }

    public String imprimer() {
        try {
            JRBeanCollectionDataSource beanCollectionDataSource;
            if (selectedListCompte.isEmpty()) {
                beanCollectionDataSource = new JRBeanCollectionDataSource(lazyListCompte);
            } else {
                beanCollectionDataSource = new JRBeanCollectionDataSource(selectedListCompte);
            }
            String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("resources/etats/compte.jasper");
            Map parameters = new HashMap();
            selectedCritere.setAgence(currentUserAgence.get(0));
            parameters.put("REPORT_FILTER", selectedCritere);
            parameters.put("USER", connectedUser);
            parameters.put("REPORT_LOCALE", FacesContext.getCurrentInstance().getViewRoot().getLocale());
            JasperPrint jasperPrint = fillReport(reportPath, parameters, beanCollectionDataSource);
            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            httpServletResponse.addHeader("Content-disposition", "attachment; filename=ListeCompte.pdf");
            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
            exportReportToPdfStream(jasperPrint, servletOutputStream);
            servletOutputStream.close();
            FacesContext.getCurrentInstance().responseComplete();
            //----------------------------------------------
            String libelleOperation = routine.localizeMessage("Impression", new Locale("fr")) + " (" + routine.localizeMessage("Compte", new Locale("fr")) + ")";
            String libelleOperation_en = routine.localizeMessage("Impression", new Locale("en")) + " (" + routine.localizeMessage("Compte", new Locale("en")) + ")";
            routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, routine.localizeMessage("Compte"), connectedUser, operationsFacade);
            //----------------------------------------------
        } catch (JRException | IOException e) {
            e.printStackTrace();
        }
        ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
        return ext.getRequestServletPath() + "?faces-redirect=true";
    }

    public void loadAgence() {
        listAgence.clear();
        listAgence.addAll(currentUserAgence);
    }

    public void loadAgencePicklistSource() {
        listAgenceOthers.getSource().clear();
        listAgenceOthers.getSource().addAll(currentUserAgence);
    }
    //--------------------------------------------------

    public String compte() {
        return "compte.xhtml?faces-redirect=true";
    }

    public void tableReset() {
        selectedListCompte.clear();
        rowClick();
    }

    public void closeDialog() {
        showDialog = false;
    }

    public void prepareSendMessage() {
        selectedNotificationsms = new Notifications();
        selectedNotificationsms.setDelivre(true);
        selectedNotificationsms.setLibre(false);
        selectedNotificationsms.setEtat("Actif");
        selectedNotificationsms.setDateEnregistre(new Date(currentTimeMillis()));
        selectedNotificationsms.setDerniereModif(new Date(currentTimeMillis()));
        selectedNotificationsms.setIdagence(connectedUser.getIdagence());
        showDialog = true;
    }

    public void selectAgence() {
        loadAgencePicklistSource();
        if (selectedAgence != null) {
            listAgenceOthers.getTarget().clear();
            listAgenceOthers.getTarget().add(agenceFacade.find(selectedAgence));
            listAgenceOthers.getSource().removeAll(listAgenceOthers.getTarget());
        }
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return agenceFacade.find(valueOf(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return ((Agence) value).getIdagence().toString();
    }

    public String closePage() {
        return "moduleSecurite.xhtml?faces-redirect=true";
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            UploadedFile file = event.getFile();
            String filePath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("resources/fichiers");
            String code = (new SimpleDateFormat("SSS")).format(new Date(System.currentTimeMillis()));
            String f_name = file.getFileName().substring(0, file.getFileName().indexOf(".")) + code + file.getFileName().substring(file.getFileName().indexOf("."));
            selectedCompte.setPhotoRelatif("resources/fichiers/" + f_name);
            selectedCompte.setPhoto(filePath + File.separator + f_name);
            byte[] bytes = file.getContents();
            try (FileOutputStream out = new FileOutputStream(selectedCompte.getPhoto(), true)) {
                out.write(bytes);
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void prepareCapture(ActionEvent ae) {
        CommandLink cl = (CommandLink) ae.getComponent();
        captureMode = cl.getId();
    }

    public void onCapture(CaptureEvent captureEvent) {
        byte[] data = captureEvent.getData();
        String filePath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("resources/fichiers");
        FileImageOutputStream imageOutput;
        try {
            String fileName = (new SimpleDateFormat("dd_MM_yy_HH_mm_ss").format(new Date(System.currentTimeMillis()))) + ".png";
            imageTempRelatif = "resources/fichiers/" + fileName;
            imageTemp = filePath + File.separator + fileName;
            imageOutput = new FileImageOutputStream(new File(imageTemp));
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
            RequestContext.getCurrentInstance().getScriptsToExecute().add("PF('widget_cameraphoto').hide()");
            RequestContext.getCurrentInstance().getScriptsToExecute().add("PF('widget_photocopper').show()");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String crop() {
        String filePath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("resources/fichiers");
        FileImageOutputStream imageOutput;
        try {
            if (captureMode.equals("picture")) {
                String code = (new SimpleDateFormat("SSS")).format(new Date(System.currentTimeMillis()));
                String fileName = (selectedCompte.getNomPrenom() != null ? selectedCompte.getNomPrenom().replaceAll(" ", "_") + "_" + compteFacade.nextId() : compteFacade.nextId()) + code + ".png";
                selectedCompte.setPhotoRelatif("resources/fichiers/" + fileName);
                selectedCompte.setPhoto(filePath + File.separator + fileName);
                imageOutput = new FileImageOutputStream(new File(selectedCompte.getPhoto()));
                imageOutput.write(croppedImage.getBytes(), 0, croppedImage.getBytes().length);
                imageOutput.close();
            } else {
                imageOutput = new FileImageOutputStream(new File(imageTemp));
                imageOutput.write(croppedImage.getBytes(), 0, croppedImage.getBytes().length);
                imageOutput.close();
//                TessBaseAPI api = new TessBaseAPI();
//                // Initialize tesseract-ocr with English, without specifying tessdata path
//                if (api.Init(null, "eng") != 0) {
//                    System.err.println("Could not initialize tesseract.");
//                    System.exit(1);
//                }
//                // Open input image with leptonica library
//                PIX image = pixRead("/" + imageTempRelatif);
//                api.SetImage(image);
//                // Get OCR result
//                BytePointer outText = api.GetUTF8Text();
//                System.out.println("OCR output:\n" + outText.getString());
//                // Destroy used object and release memory
//                api.End();
//                outText.deallocate();
//                pixDestroy(image);
            }
            deleteTempImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestContext.getCurrentInstance().getScriptsToExecute().add("PF('widget_photocopper').hide()");
        return null;
    }

    public void deleteTempImage() {
        File f = new File(imageTemp);
        f.delete();
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

    public Compte getSelectedCompte() {
        return selectedCompte;
    }

    public void setSelectedCompte(Compte selectedCompte) {
        this.selectedCompte = selectedCompte;
    }

    public List<Compte> getSelectedListCompte() {
        return selectedListCompte;
    }

    public void setSelectedListCompte(List<Compte> selectedListCompte) {
        this.selectedListCompte = selectedListCompte;
    }

    public Compte getConnectedUser() {
        return connectedUser;
    }

    public void setConnectedUser(Compte connectedUser) {
        this.connectedUser = connectedUser;
    }

    public OperationsFacadeLocal getOperationsFacade() {
        return operationsFacade;
    }

    public void setOperationsFacade(OperationsFacadeLocal operationsFacade) {
        this.operationsFacade = operationsFacade;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
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

    public DualListModel<Agence> getListAgenceOthers() {
        return listAgenceOthers;
    }

    public void setListAgenceOthers(DualListModel<Agence> listAgenceOthers) {
        this.listAgenceOthers = listAgenceOthers;
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

    public Boolean getSetPwd() {
        return setPwd;
    }

    public void setSetPwd(Boolean setPwd) {
        this.setPwd = setPwd;
    }

    public CroppedImage getCroppedImage() {
        return croppedImage;
    }

    public void setCroppedImage(CroppedImage croppedImage) {
        this.croppedImage = croppedImage;
    }

    public String getImageTemp() {
        return imageTemp;
    }

    public void setImageTemp(String imageTemp) {
        this.imageTemp = imageTemp;
    }

    public String getImageTempRelatif() {
        return imageTempRelatif;
    }

    public void setImageTempRelatif(String imageTempRelatif) {
        this.imageTempRelatif = imageTempRelatif;
    }

    public String getCaptureMode() {
        return captureMode;
    }

    public void setCaptureMode(String captureMode) {
        this.captureMode = captureMode;
    }

    public List<Agence> getCurrentUserAgence() {
        return currentUserAgence;
    }

    public void setCurrentUserAgence(List<Agence> currentUserAgence) {
        this.currentUserAgence = currentUserAgence;
    }

    public LazyDataModel<Compte> getLazylistCompteModel() {
        return lazylistCompteModel;
    }

    public void setLazylistCompteModel(LazyDataModel<Compte> lazylistCompteModel) {
        this.lazylistCompteModel = lazylistCompteModel;
    }

    public List<Compte> getLazyListCompte() {
        return lazyListCompte;
    }

    public void setLazyListCompte(List<Compte> lazyListCompte) {
        this.lazyListCompte = lazyListCompte;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public ConcurrentFacadeLocal getComptoirFacade() {
        return comptoirFacade;
    }

    public void setComptoirFacade(ConcurrentFacadeLocal comptoirFacade) {
        this.comptoirFacade = comptoirFacade;
    }

    public NotificationsFacadeLocal getNotificationsmsFacade() {
        return notificationsmsFacade;
    }

    public void setNotificationsmsFacade(NotificationsFacadeLocal notificationsmsFacade) {
        this.notificationsmsFacade = notificationsmsFacade;
    }

    public Notifications getSelectedNotificationsms() {
        return selectedNotificationsms;
    }

    public void setSelectedNotificationsms(Notifications selectedNotificationsms) {
        this.selectedNotificationsms = selectedNotificationsms;
    }

    public boolean isModeleSms() {
        return modeleSms;
    }

    public void setModeleSms(boolean modeleSms) {
        this.modeleSms = modeleSms;
    }

    public boolean isShowDialog() {
        return showDialog;
    }

    public void setShowDialog(boolean showDialog) {
        this.showDialog = showDialog;
    }

    public boolean isCanSave() {
        return canSave;
    }

    public void setCanSave(boolean canSave) {
        this.canSave = canSave;
    }

    public String getNotifTitle() {
        return notifTitle;
    }

    public void setNotifTitle(String notifTitle) {
        this.notifTitle = notifTitle;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public Date getToday() {
        return today;
    }

    public void setToday(Date today) {
        this.today = today;
    }

}
