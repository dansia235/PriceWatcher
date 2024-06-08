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
import java.io.IOException;
import java.io.Serializable;
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
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
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
import org.primefaces.component.selectbooleancheckbox.SelectBooleanCheckbox;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import sessions.AgenceFacadeLocal;
import sessions.CompteFacadeLocal;
import sessions.OperationsFacadeLocal;
import usual.MailTools;
import sessions.NotificationsFacadeLocal;

/**
 *
 * @author Lucien FOTSA
 */
public class NotificationsMB implements Serializable {

    private MailTools mailTools = new MailTools();
    //********************************************
    @Resource
    private UserTransaction ut;
    @EJB
    private OperationsFacadeLocal operationsFacade;
    //--------------------------------------------------------------------------
    @EJB
    private NotificationsFacadeLocal notificationsFacade;
    private List<Notifications> listNotifications = new ArrayList<>();
    private List<Notifications> selectedListNotifications = new ArrayList<>();
    private Notifications selectedNotifications = new Notifications();
    private String oldValue;
    //------------------------------------------------
    private Compte connectedUser;
    private Routine routine = new Routine();
    private String operation = "add";
    private boolean esc = true;//Etat spécifique d'un composant
    private boolean egc = true;//Etat générique d'un composant
    private String style = "font-size: 10pt; font-weight: bold; font-family: arial;";
    //---------------------------------------------------
    @EJB
    private CompteFacadeLocal compteFacade;
    private List<Compte> listCompte = new ArrayList<>();
    //---------------------------------------------------
    @EJB
    private AgenceFacadeLocal agenceFacade;
    private List<Agence> listAgence = new ArrayList<>();
    private Integer selectedAgence;
    //--------------------------------------------
    private List<CritereFiltre> listCritereFiltre = new ArrayList<>();
    private CritereFiltre selectedCritere = new CritereFiltre("Tous", routine.localizeMessage("Tous"), "Tous", "Actif");
    private Boolean codeAuto = true;
    private volatile int count;
    //---------------------------------------------------------
    private List<Agence> currentUserAgence = new ArrayList<>();
    //--------------------------------------------
    private LazyDataModel<Notifications> lazylistNotificationsModel;
    private List<Notifications> lazyListNotifications = new ArrayList<>();
    private boolean lock = false;
    private boolean showDialog = false;
    private String notifTitle = "Communication";

    /**
     * Creates a new instance of NotificationsMB
     */
    public NotificationsMB() {
    }

    @PostConstruct
    private void init() {
        connectedUser = (Compte) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUser");
        currentUserAgence = (List<Agence>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUserAgence");
        loadCritere();
        selectCritere();
//        rechercheNotifications();
    }

    //--------------------------------------------------
    public void loadCritere() {
        listCritereFiltre.clear();
        listCritereFiltre.add(new CritereFiltre("Tous", routine.localizeMessage("Tous"), "Tous", "Actif"));
        listCritereFiltre.add(new CritereFiltre("idagence", routine.localizeMessage("Agence"), "Select", "Actif"));
        listCritereFiltre.add(new CritereFiltre("idCompte", routine.localizeMessage("Compte"), "Select4", "Actif"));
        listCritereFiltre.add(new CritereFiltre("msg", routine.localizeMessage("Notification"), "String", "Actif"));
        listCritereFiltre.add(new CritereFiltre("campagnevu", routine.localizeMessage("Campagne") + " & " + routine.localizeMessage("Vu"), "StringBoolean", "Actif"));
        listCritereFiltre.add(new CritereFiltre("campagne", routine.localizeMessage("Campagne"), "String", "Actif"));
        listCritereFiltre.add(new CritereFiltre("vu", routine.localizeMessage("Vu"), "Boolean", "Actif"));
        listCritereFiltre.add(new CritereFiltre("dernierevu", routine.localizeMessage("Derniere") + " " + routine.localizeMessage("Vu"), "Date", "Actif"));
        listCritereFiltre.add(new CritereFiltre("delivre", routine.localizeMessage("Delivre"), "Boolean", "Actif"));
        listCritereFiltre.add(new CritereFiltre("dateEnregistre", routine.localizeMessage("DateEnregistre"), "Date", "Actif"));
        listCritereFiltre.add(new CritereFiltre("derniereModif", routine.localizeMessage("DerniereModif"), "Date", "Actif"));
    }

    public void selectCritere() {
        selectedCritere.setType(listCritereFiltre.get(listCritereFiltre.indexOf(selectedCritere)).getType());
        selectedCritere.setLibelle(listCritereFiltre.get(listCritereFiltre.indexOf(selectedCritere)).getLibelle());
        switch (selectedCritere.getAttribut()) {
            case "idagence":
                loadAgence();
                break;
            case "idCompte":
                loadCompte();
                break;
        }
    }

    public void switchCategorie() {
        loadCritere();
        selectedCritere = new CritereFiltre("Tous", routine.localizeMessage("Tous"), "Tous", "Actif");
        selectCritere();
        rechercheNotifications();
    }

    public void rechercheNotifications() {
        listNotifications.clear();
        switch (selectedCritere.getAttribut()) {
            case "Tous":
                listNotifications.addAll(notificationsFacade.findAll_Etat(selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!notificationsFacade.findAll_Etat("Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty() ? true : routine.isCor()) : true);
                selectedCritere.setUsedValue(routine.localizeMessage("Tous"));
                break;
            case "msg":
                listNotifications.addAll(notificationsFacade.findByMessage_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!notificationsFacade.findByMessage_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", "Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty() ? true : routine.isCor()) : true);
                selectedCritere.setUsedValue(selectedCritere.getValString());
                break;
            case "campagnevu":
                listNotifications.addAll(notificationsFacade.findByCampagne_Vu_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", selectedCritere.getValBool(), selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!notificationsFacade.findByCampagne_Vu_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", selectedCritere.getValBool(), "Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty() ? true : routine.isCor()) : true);
                selectedCritere.setUsedValue(selectedCritere.getValString());
                break;
            case "campagne":
                listNotifications.addAll(notificationsFacade.findByCampagne_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!notificationsFacade.findByCampagne_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", "Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty() ? true : routine.isCor()) : true);
                selectedCritere.setUsedValue(selectedCritere.getValString());
                break;
            case "vu":
                listNotifications.addAll(notificationsFacade.findByVu_Etat(selectedCritere.getValBool(), selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!notificationsFacade.findByVu_Etat(selectedCritere.getValBool(), "Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty() ? true : routine.isCor()) : true);
                selectedCritere.setUsedValue(selectedCritere.getValBool().toString());
                break;
            case "dernierevu":
                routine.correctDateTime(selectedCritere.getValDate1(), new Date(selectedCritere.getValDate2().getTime() + 59999L), null, null);
                listNotifications.addAll(notificationsFacade.findByDernierevu_Etat(selectedCritere.getValDate1(), new Date(selectedCritere.getValDate2().getTime() + 59999L), selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!notificationsFacade.findByDernierevu_Etat(selectedCritere.getValDate1(), new Date(selectedCritere.getValDate2().getTime() + 59999L), "Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty() ? true : routine.isCor()) : true);
                selectedCritere.setUsedValue(new SimpleDateFormat("dd/MM/yyyy").format(selectedCritere.getValDate1()) + " - " + new SimpleDateFormat("dd/MM/yyyy").format(selectedCritere.getValDate2()));
                break;
            case "delivre":
                listNotifications.addAll(notificationsFacade.findByDelivre_Etat(selectedCritere.getValBool(), selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!notificationsFacade.findByDelivre_Etat(selectedCritere.getValBool(), "Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty() ? true : routine.isCor()) : true);
                selectedCritere.setUsedValue(selectedCritere.getValBool().toString());
                break;
            case "idagence":
                List<Agence> agenceList = new ArrayList();
                agenceList.add(new Agence(selectedCritere.getValSelect()));
                listNotifications.addAll(notificationsFacade.findAll_Etat(selectedCritere.getEtat().toUpperCase(ROOT), agenceList));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!notificationsFacade.findAll_Etat("Supprime".toUpperCase(ROOT), agenceList).isEmpty() ? true : routine.isCor()) : true);
                selectedCritere.setUsedValue(agenceFacade.find(selectedCritere.getValSelect()).getRaisonsociale());
                break;
            case "idCompte":
                listNotifications.addAll(notificationsFacade.findByIdCompte_Etat(selectedCritere.getValSelect(), selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!notificationsFacade.findByIdCompte_Etat(selectedCritere.getValSelect(), "Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty() ? true : routine.isCor()) : true);
                selectedCritere.setUsedValue(compteFacade.find(selectedCritere.getValSelect()).getLogin() + " [" + compteFacade.find(selectedCritere.getValSelectL()).getNomPrenom() + "]");
                break;
            case "dateEnregistre":
                routine.correctDateTime(selectedCritere.getValDate1(), new Date(selectedCritere.getValDate2().getTime() + 59999L), null, null);
                listNotifications.addAll(notificationsFacade.findByDateEnregistre_Etat(selectedCritere.getValDate1(), new Date(selectedCritere.getValDate2().getTime() + 59999L), selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!notificationsFacade.findByDateEnregistre_Etat(selectedCritere.getValDate1(), new Date(selectedCritere.getValDate2().getTime() + 59999L), "Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty() ? true : routine.isCor()) : true);
                selectedCritere.setUsedValue(new SimpleDateFormat("dd/MM/yyyy").format(selectedCritere.getValDate1()) + " - " + new SimpleDateFormat("dd/MM/yyyy").format(selectedCritere.getValDate2()));
                break;
            case "derniereModif":
                routine.correctDateTime(selectedCritere.getValDate1(), new Date(selectedCritere.getValDate2().getTime() + 59999L), null, null);
                listNotifications.addAll(notificationsFacade.findByDerniereModif_Etat(selectedCritere.getValDate1(), new Date(selectedCritere.getValDate2().getTime() + 59999L), selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!notificationsFacade.findByDerniereModif_Etat(selectedCritere.getValDate1(), new Date(selectedCritere.getValDate2().getTime() + 59999L), "Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty() ? true : routine.isCor()) : true);
                selectedCritere.setUsedValue(new SimpleDateFormat("dd/MM/yyyy").format(selectedCritere.getValDate1()) + " - " + new SimpleDateFormat("dd/MM/yyyy").format(selectedCritere.getValDate2()));
                break;
        }
        //**********************************************************************************************************

        lazylistNotificationsModel = new LazyDataModel<Notifications>() {
            @Override
            public Notifications getRowData(String rowKey) {
                for (Notifications ctn : lazyListNotifications) {
                    if (ctn.getIdnotificationsms().toString().equals(rowKey)) {
                        return ctn;
                    }
                }
                return null;
            }

            @Override
            public Object getRowKey(Notifications ctn) {
                return ctn.getIdnotificationsms();
            }

            @Override
            public List<Notifications> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, Object> filters) {
                List<Notifications> data = new ArrayList<>();
                //filter
                for (Notifications ctn : listNotifications) {
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
                        Collections.sort(data, new Notifications(sm.getSortField(), (SortOrder.ASCENDING.equals(sm.getSortOrder()) ? 1 : -1)));
                    }
                }

                //rowCount
                int dataSize = data.size();
                this.setRowCount(dataSize);

                //paginate
                lazyListNotifications.clear();
                if (dataSize > pageSize) {
                    try {
                        lazyListNotifications.addAll(data.subList(first, first + pageSize));
                        for (Notifications notif : lazyListNotifications) {
                            if (!notif.getVu()) {
                                notif.setVu(true);
                                notif.setDernierevu(new Date());
                                try {
                                    notif.setNbrevu((short) (notif.getNbrevu() + 1));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                notificationsFacade.edit(notif);
                            }
                        }
                        return lazyListNotifications;
                    } catch (IndexOutOfBoundsException e) {
                        lazyListNotifications.addAll(data.subList(first, first + (dataSize % pageSize)));
                        for (Notifications notif : lazyListNotifications) {
                            if (!notif.getVu()) {
                                notif.setVu(true);
                                notif.setDernierevu(new Date());
                                try {
                                    notif.setNbrevu((short) (notif.getNbrevu() + 1));
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                notificationsFacade.edit(notif);
                            }
                        }
                        return lazyListNotifications;
                    }

                } else {
                    lazyListNotifications.addAll(data);
                    for (Notifications notif : lazyListNotifications) {
                        if (!notif.getVu()) {
                            notif.setVu(true);
                            notif.setDernierevu(new Date());
                            try {
                                notif.setNbrevu((short) (notif.getNbrevu() + 1));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            notificationsFacade.edit(notif);
                        }
                    }
                    return lazyListNotifications;
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
        if (!listNotifications.isEmpty()) {
            routine.setImp(true);
        }
        if (selectedListNotifications.size() > 1) {
            if (selectedCritere.getEtat().equals("Actif")) {
                routine.setSup(true);
            } else {
                routine.setRes(true);
                routine.setDet(true);
            }
        } else if (selectedListNotifications.size() == 1) {
            selectedNotifications = notificationsFacade.find(selectedListNotifications.get(0).getIdnotificationsms());
            selectedAgence = selectedNotifications.getIdagence() != null ? selectedNotifications.getIdagence().getIdagence() : null;
            oldValue = "[" + selectedNotifications.getIdnotificationsms() + "]";
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

    public void closeDialog() {
        showDialog = false;
    }

    public void btnClick(ActionEvent e) {
        esc = true;
        egc = true;
        style = "font-size: 10pt; font-weight: bold; font-family: arial;";
        CommandButton btn = (CommandButton) e.getComponent();
        operation = btn.getWidgetVar();
        loadAgence();
        switch (operation) {
            case "add":
                showDialog = true;
                //--------
                selectedNotifications = new Notifications();
                selectedNotifications.setDelivre(false);
                selectedNotifications.setLibre(false);
                selectedNotifications.setMessage("");
                selectedNotifications.setMessageen("");
                //-------------------------------------------------------------------------------------------------------
                tableReset();
                break;
            case "con":
                showDialog = true;
                //--------
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
                rechercheNotifications();
                break;
            case "mod":
                showDialog = true;
                break;
        }
    }

    public void delivrer(AjaxBehaviorEvent e) {
        String libelleOperation = "", libelleOperation_en = "";
        String langue = FacesContext.getCurrentInstance().getViewRoot().getLocale().getLanguage();
        try {
            SelectBooleanCheckbox sbc = (SelectBooleanCheckbox) e.getComponent();
            selectedNotifications = notificationsFacade.find(Long.parseLong(sbc.getLabel()));
            libelleOperation = routine.localizeMessage("Delivre", new Locale("fr")) + " " + routine.localizeMessage("Unec", new Locale("fr")) + " " + routine.localizeMessage("Notification", new Locale("fr"));
            libelleOperation_en = routine.localizeMessage("Delivre", new Locale("en")) + " " + routine.localizeMessage("Unec", new Locale("en")) + " " + routine.localizeMessage("Notification", new Locale("en"));
            ut.begin();
            if (selectedNotifications.getLibre()) {
                mailTools.sendEmail((langue.equals("fr") ? selectedNotifications.getMessage() : selectedNotifications.getMessageen()), notifTitle, connectedUser.getIdagence().getEmail(), selectedNotifications.getEmail(), false, "");
                selectedNotifications.setDelivre(true);
            } else {
                mailTools.sendEmail((langue.equals("fr") ? selectedNotifications.getMessage() : selectedNotifications.getMessageen()), notifTitle, connectedUser.getIdagence().getEmail(), selectedNotifications.getEmail(), true, "");
                selectedNotifications.setDelivre(true);
            }
            selectedNotifications.setDerniereModif(new Date(currentTimeMillis()));
            notificationsFacade.edit(selectedNotifications);
            routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, "Id = " + selectedNotifications.getIdnotificationsms(), connectedUser, operationsFacade);
            routine.feedBack("Information", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Succes_operation"), null);
            ut.commit();
            rechercheNotifications();
        } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
            routine.feedBack("Erreur", langue.equals("fr") ? libelleOperation : libelleOperation_en, null, ex);
            ex.printStackTrace();
        }
    }

    public void persist() {
        String libelleOperation = "", libelleOperation_en = "";
        String langue = FacesContext.getCurrentInstance().getViewRoot().getLocale().getLanguage();
        try {
            ut.begin();
            switch (operation) {
                case "add":
                    libelleOperation = routine.localizeMessage("Ajouter", new Locale("fr")) + " " + routine.localizeMessage("Unec", new Locale("fr")) + " " + routine.localizeMessage("Notification", new Locale("fr"));
                    libelleOperation_en = routine.localizeMessage("Ajouter", new Locale("en")) + " " + routine.localizeMessage("Unec", new Locale("en")) + " " + routine.localizeMessage("Notification", new Locale("en"));
//                    if (isEmpty()) {
                    //-------------------------------------------------------------------------------------------------------
                    selectedNotifications.setEtat("Actif");
                    selectedNotifications.setDateEnregistre(new Date(currentTimeMillis()));
                    selectedNotifications.setDerniereModif(new Date(currentTimeMillis()));
                    selectedNotifications.setIdagence(connectedUser.getIdagence());
                    selectedNotifications.setMessage(selectedNotifications.getMessage().replace("\"", ""));
                    selectedNotifications.setMessageen(selectedNotifications.getMessage());
                    //-------------------------------------------------------------------------------------------------------
                    if (selectedNotifications.getLibre()) {
                        mailTools.sendEmail((langue.equals("fr") ? selectedNotifications.getMessage() : selectedNotifications.getMessageen()), notifTitle, connectedUser.getIdagence().getEmail(), selectedNotifications.getEmail(), false, "");
                        selectedNotifications.setDelivre(true);
                    } else {
                        mailTools.sendEmail((langue.equals("fr") ? selectedNotifications.getMessage() : selectedNotifications.getMessageen()), notifTitle, connectedUser.getIdagence().getEmail(), selectedNotifications.getEmail(), true, "");
                        selectedNotifications.setDelivre(true);
                    }
                    selectedNotifications.setIdnotificationsms(notificationsFacade.nextId());
                    notificationsFacade.create(selectedNotifications);
                    routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, routine.localizeMessage("Utilisateur"), connectedUser, operationsFacade);
                    routine.feedBack("Information", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Succes_operation"), null);
//                    } else {
//                        routine.feedBack("Avertissement", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Doublon"), null);
//                    }
                    break;
                case "sup":
                    int not = 0;
                    libelleOperation = routine.localizeMessage("Supprimer", new Locale("fr")) + " " + routine.localizeMessage("Unec", new Locale("fr")) + " " + routine.localizeMessage("Notification", new Locale("fr"));
                    libelleOperation_en = routine.localizeMessage("Supprimer", new Locale("en")) + " " + routine.localizeMessage("Unec", new Locale("en")) + " " + routine.localizeMessage("Notification", new Locale("en"));
                    for (Notifications notifications_ : selectedListNotifications) {
                        if (true) {
                            notifications_.setDerniereModif(new Date(currentTimeMillis()));
                            notifications_.setEtat("Supprime");
                            notificationsFacade.edit(notifications_);
                            routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, "[Id = " + notifications_.getIdnotificationsms() + "]", connectedUser, operationsFacade);
                        } else {
                            not++;
                        }
                    }
                    if (not > 0) {
                        routine.feedBack("Avertissement", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Suppression_impossible") + " " + not + " " + routine.localizeMessage("Reference_impossible") + " (" + routine.localizeMessage("Notification") + ")", null);
                    } else {
                        routine.feedBack("Information", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Succes_operation"), null);
                    }
                    break;
                case "res":
                    libelleOperation = routine.localizeMessage("Restaurer", new Locale("fr")) + " " + routine.localizeMessage("Unec", new Locale("fr")) + " " + routine.localizeMessage("Notification", new Locale("fr"));
                    libelleOperation_en = routine.localizeMessage("Restaurer", new Locale("en")) + " " + routine.localizeMessage("Unec", new Locale("en")) + " " + routine.localizeMessage("Notification", new Locale("en"));
                    for (Notifications notifications_ : selectedListNotifications) {
//                        if (isEmpty()) {
                        notifications_.setDerniereModif(new Date(currentTimeMillis()));
                        notifications_.setEtat("Actif");
                        notificationsFacade.edit(notifications_);
                        routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, "[Id = " + notifications_.getIdnotificationsms() + "]", connectedUser, operationsFacade);
//                        } else {
//                            libelleOperation = routine.localizeMessage("Detruire", new Locale("fr")) + " " + routine.localizeMessage("Unec", new Locale("fr")) + " " + routine.localizeMessage("Notification", new Locale("fr")) + " [2]";
//                            libelleOperation_en = routine.localizeMessage("Detruire", new Locale("en")) + " " + routine.localizeMessage("Unec", new Locale("en")) + " " + routine.localizeMessage("Notification", new Locale("en")) + " [2]";
//                            notificationsFacade.remove(notifications_);
//                            routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, "[" + notifications_.getIdnotifications() + "]", connectedUser, operationsFacade);
//                        }
                    }
                    routine.feedBack("Information", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Succes_operation"), null);
                    break;
                case "det":
                    libelleOperation = routine.localizeMessage("Detruire", new Locale("fr")) + " " + routine.localizeMessage("Unec", new Locale("fr")) + " " + routine.localizeMessage("Notification", new Locale("fr"));
                    libelleOperation_en = routine.localizeMessage("Detruire", new Locale("en")) + " " + routine.localizeMessage("Unec", new Locale("en")) + " " + routine.localizeMessage("Notification", new Locale("en"));
                    for (Notifications notifications_ : selectedListNotifications) {
                        notificationsFacade.remove(notifications_);
                        routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, "[Id = " + notifications_.getIdnotificationsms() + "]", connectedUser, operationsFacade);
                    }
                    routine.feedBack("Information", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Succes_operation"), null);
                    break;
            }
            closeDialog();
            ut.commit();
        } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException e) {
            routine.feedBack("Erreur", langue.equals("fr") ? libelleOperation : libelleOperation_en, null, e);
            e.printStackTrace();
        }
        rechercheNotifications();
    }

    public String imprimer() {
        try {
            JRBeanCollectionDataSource beanCollectionDataSource;
            if (selectedListNotifications.isEmpty()) {
                beanCollectionDataSource = new JRBeanCollectionDataSource(lazyListNotifications);
            } else {
                beanCollectionDataSource = new JRBeanCollectionDataSource(selectedListNotifications);
            }
            String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("resources/etats/notifications.jasper");
            Map parameters = new HashMap();
            selectedCritere.setAgence(currentUserAgence.get(0));
            parameters.put("REPORT_FILTER", selectedCritere);
            parameters.put("USER", connectedUser);
            parameters.put("REPORT_LOCALE", FacesContext.getCurrentInstance().getViewRoot().getLocale());
            JasperPrint jasperPrint = fillReport(reportPath, parameters, beanCollectionDataSource);
            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            httpServletResponse.addHeader("Content-disposition", "attachment; filename=ListeNotifications.pdf");
            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
            exportReportToPdfStream(jasperPrint, servletOutputStream);
            servletOutputStream.close();
            FacesContext.getCurrentInstance().responseComplete();
            //----------------------------------------------
            String libelleOperation = routine.localizeMessage("Impression", new Locale("fr")) + " (" + routine.localizeMessage("Notification", new Locale("fr")) + ")";
            String libelleOperation_en = routine.localizeMessage("Impression", new Locale("en")) + " (" + routine.localizeMessage("Notification", new Locale("en")) + ")";
            routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, routine.localizeMessage("Notification"), connectedUser, operationsFacade);
            //----------------------------------------------
        } catch (JRException | IOException e) {
            e.printStackTrace();
        }
        ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
        return ext.getRequestServletPath() + "?faces-redirect=true";
    }
    //--------------------------------------------------

    public String notifications() {
        return "notifications.xhtml?faces-redirect=true";
    }

    public void tableReset() {
        selectedListNotifications.clear();
        rowClick();
    }

    public void loadAgence() {
        listAgence.clear();
        listAgence.addAll(agenceFacade.findAll_Etat("Actif".toUpperCase(ROOT)));
    }

    public void loadCompte() {
        listCompte.clear();
        listCompte.addAll(compteFacade.findAll_Etat("Actif".toUpperCase(ROOT), currentUserAgence));
    }

    public String closePage() {
        return "moduleVeille.xhtml?faces-redirect=true";
    }

    public NotificationsFacadeLocal getNotificationsFacade() {
        return notificationsFacade;
    }

    public void setNotificationsFacade(NotificationsFacadeLocal notificationsFacade) {
        this.notificationsFacade = notificationsFacade;
    }

    public List<Notifications> getListNotifications() {
        return listNotifications;
    }

    public void setListNotifications(List<Notifications> listNotifications) {
        this.listNotifications = listNotifications;
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

    public Notifications getSelectedNotifications() {
        return selectedNotifications;
    }

    public void setSelectedNotifications(Notifications selectedNotifications) {
        this.selectedNotifications = selectedNotifications;
    }

    public List<Notifications> getSelectedListNotifications() {
        return selectedListNotifications;
    }

    public void setSelectedListNotifications(List<Notifications> selectedListNotifications) {
        this.selectedListNotifications = selectedListNotifications;
    }

    public OperationsFacadeLocal getOperationsFacade() {
        return operationsFacade;
    }

    public void setOperationsFacade(OperationsFacadeLocal operationsFacade) {
        this.operationsFacade = operationsFacade;
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

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public Boolean getCodeAuto() {
        return codeAuto;
    }

    public void setCodeAuto(Boolean codeAuto) {
        this.codeAuto = codeAuto;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Compte getConnectedUser() {
        return connectedUser;
    }

    public void setConnectedUser(Compte connectedUser) {
        this.connectedUser = connectedUser;
    }

    public List<Agence> getCurrentUserAgence() {
        return currentUserAgence;
    }

    public void setCurrentUserAgence(List<Agence> currentUserAgence) {
        this.currentUserAgence = currentUserAgence;
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

    public LazyDataModel<Notifications> getLazylistNotificationsModel() {
        return lazylistNotificationsModel;
    }

    public void setLazylistNotificationsModel(LazyDataModel<Notifications> lazylistNotificationsModel) {
        this.lazylistNotificationsModel = lazylistNotificationsModel;
    }

    public List<Notifications> getLazyListNotifications() {
        return lazyListNotifications;
    }

    public void setLazyListNotifications(List<Notifications> lazyListNotifications) {
        this.lazyListNotifications = lazyListNotifications;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public Integer getSelectedAgence() {
        return selectedAgence;
    }

    public void setSelectedAgence(Integer selectedAgence) {
        this.selectedAgence = selectedAgence;
    }

    public boolean isShowDialog() {
        return showDialog;
    }

    public void setShowDialog(boolean showDialog) {
        this.showDialog = showDialog;
    }

    public MailTools getMailTools() {
        return mailTools;
    }

    public void setMailTools(MailTools mailTools) {
        this.mailTools = mailTools;
    }

    public String getNotifTitle() {
        return notifTitle;
    }

    public void setNotifTitle(String notifTitle) {
        this.notifTitle = notifTitle;
    }

}
