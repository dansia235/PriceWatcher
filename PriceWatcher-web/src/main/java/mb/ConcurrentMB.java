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
import entities.Concurrent;
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
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import sessions.AgenceFacadeLocal;
import sessions.OperationsFacadeLocal;
import sessions.ConcurrentFacadeLocal;

/**
 *
 * @author Lucien FOTSA
 */
public class ConcurrentMB implements Serializable {

    @Resource
    private UserTransaction ut;
    @EJB
    private OperationsFacadeLocal operationsFacade;
    //--------------------------------------------------------------------------
    @EJB
    private ConcurrentFacadeLocal concurrentFacade;
    private List<Concurrent> listConcurrent = new ArrayList<>();
    private List<Concurrent> selectedListConcurrent = new ArrayList<>();
    private Concurrent selectedConcurrent = new Concurrent();
    private String oldValue;
    private String oldValue_en;
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
    //--------------------------------------------
    private List<CritereFiltre> listCritereFiltre = new ArrayList<>();
    private CritereFiltre selectedCritere = new CritereFiltre("Tous", routine.localizeMessage("Tous"), "Tous", "Actif");
    private float total = 0F;
    //---------------------------------------------------------
    private List<Agence> currentUserAgence = new ArrayList<>();
    private LazyDataModel<Concurrent> lazylistConcurrentModel;
    private List<Concurrent> lazyListConcurrent = new ArrayList<>();
    private boolean lock = false;

    /**
     * Creates a new instance of ConcurrentMB
     */
    public ConcurrentMB() {
    }

    @PostConstruct
    private void init() {
        connectedUser = (Compte) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUser");
        currentUserAgence = (List<Agence>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUserAgence");
        loadCritere();
        selectCritere();
//        rechercheConcurrent();
    }

    //--------------------------------------------------
    public void loadCritere() {
        listCritereFiltre.clear();
        listCritereFiltre.add(new CritereFiltre("Tous", routine.localizeMessage("Tous"), "Tous", "Actif"));
        listCritereFiltre.add(new CritereFiltre("lien", routine.localizeMessage("Lien"), "String", "Actif"));
        listCritereFiltre.add(new CritereFiltre("dateEnregistre", routine.localizeMessage("DateEnregistre"), "Date", "Actif"));
        listCritereFiltre.add(new CritereFiltre("derniereModif", routine.localizeMessage("DerniereModif"), "Date", "Actif"));
    }

    public void selectCritere() {
        selectedCritere.setType(listCritereFiltre.get(listCritereFiltre.indexOf(selectedCritere)).getType());
        selectedCritere.setLibelle(listCritereFiltre.get(listCritereFiltre.indexOf(selectedCritere)).getLibelle());
    }

    public void rechercheConcurrent() {
        listConcurrent.clear();
        switch (selectedCritere.getAttribut()) {
            case "Tous":
                listConcurrent.addAll(concurrentFacade.findAll_Etat(selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!concurrentFacade.findAll_Etat("Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty()) : true);
                selectedCritere.setUsedValue(routine.localizeMessage("Tous"));
                break;
            case "lien":
                listConcurrent.addAll(FacesContext.getCurrentInstance().getViewRoot().getLocale().getLanguage().equals(new Locale("fr").getLanguage()) ? concurrentFacade.findByLien_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence) : concurrentFacade.findByLien_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (FacesContext.getCurrentInstance().getViewRoot().getLocale().getLanguage().equals(new Locale("fr").getLanguage()) ? (!concurrentFacade.findByLien_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence).isEmpty()) : (!concurrentFacade.findByLien_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence).isEmpty())) : true);
                selectedCritere.setUsedValue(selectedCritere.getValString());
                break;
            case "dateEnregistre":
                routine.correctDateTime(selectedCritere.getValDate1(), new Date(selectedCritere.getValDate2().getTime() + 59999L), null, null);
                listConcurrent.addAll(concurrentFacade.findByDateEnregistre_Etat(selectedCritere.getValDate1(), new Date(selectedCritere.getValDate2().getTime() + 59999L), selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!concurrentFacade.findByDateEnregistre_Etat(selectedCritere.getValDate1(), new Date(selectedCritere.getValDate2().getTime() + 59999L), "Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty()) : true);
                selectedCritere.setUsedValue(new SimpleDateFormat("dd/MM/yyyy").format(selectedCritere.getValDate1()) + " - " + new SimpleDateFormat("dd/MM/yyyy").format(selectedCritere.getValDate2()));
                break;
            case "derniereModif":
                routine.correctDateTime(selectedCritere.getValDate1(), new Date(selectedCritere.getValDate2().getTime() + 59999L), null, null);
                listConcurrent.addAll(concurrentFacade.findByDerniereModif_Etat(selectedCritere.getValDate1(), new Date(selectedCritere.getValDate2().getTime() + 59999L), selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!concurrentFacade.findByDerniereModif_Etat(selectedCritere.getValDate1(), new Date(selectedCritere.getValDate2().getTime() + 59999L), "Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty()) : true);
                selectedCritere.setUsedValue(new SimpleDateFormat("dd/MM/yyyy").format(selectedCritere.getValDate1()) + " - " + new SimpleDateFormat("dd/MM/yyyy").format(selectedCritere.getValDate2()));
                break;
        }
        //**********************************************************************************************************

        lazylistConcurrentModel = new LazyDataModel<Concurrent>() {
            @Override
            public Concurrent getRowData(String rowKey) {
                for (Concurrent clt : lazyListConcurrent) {
                    if (clt.getIdconcurrent().toString().equals(rowKey)) {
                        return clt;
                    }
                }
                return null;
            }

            @Override
            public Object getRowKey(Concurrent clt) {
                return clt.getIdconcurrent();
            }

            @Override
            public List<Concurrent> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, Object> filters) {
                List<Concurrent> data = new ArrayList<>();
                //filter
                for (Concurrent clt : listConcurrent) {
                    boolean match = true;
                    if (filters != null) {
                        for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
                            try {
                                String filterProperty = it.next();
                                Object filterValue = filters.get(filterProperty);
                                String fieldValue = String.valueOf(clt.getClass().getField(filterProperty).get(clt));
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
                        data.add(clt);
                    }
                }

                //sort
                if (multiSortMeta != null) {
                    for (SortMeta sm : multiSortMeta) {
                        Collections.sort(data, new Concurrent(sm.getSortField(), (SortOrder.ASCENDING.equals(sm.getSortOrder()) ? 1 : -1)));
                    }
                }

                //rowCount
                int dataSize = data.size();
                this.setRowCount(dataSize);

                //paginate
                lazyListConcurrent.clear();
                total = 0F;
                if (dataSize > pageSize) {
                    try {
                        lazyListConcurrent.addAll(data.subList(first, first + pageSize));
                        return lazyListConcurrent;
                    } catch (IndexOutOfBoundsException e) {
                        lazyListConcurrent.addAll(data.subList(first, first + (dataSize % pageSize)));
                        return lazyListConcurrent;
                    }

                } else {
                    lazyListConcurrent.addAll(data);
                    return lazyListConcurrent;
                }
            }
        };
        //-----------------------------------------------------------------------------
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
        if (!listConcurrent.isEmpty()) {
            routine.setImp(true);
        }
        if (selectedListConcurrent.size() > 1) {
            if (selectedCritere.getEtat().equals("Actif")) {
                routine.setSup(true);
            } else {
                routine.setRes(true);
                routine.setDet(true);
            }
        } else if (selectedListConcurrent.size() == 1) {
            selectedConcurrent = concurrentFacade.find(selectedListConcurrent.get(0).getIdconcurrent());
            oldValue = selectedConcurrent.getLibelle();
            oldValue_en = selectedConcurrent.getLabel();
            //-------------------------------------------------------------------------------------------------------
            selectedAgence = selectedConcurrent.getIdagence() != null ? selectedConcurrent.getIdagence().getIdagence() : null;
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
        switch (operation) {
            case "add":
                selectedConcurrent = new Concurrent();
                selectedConcurrent.setDefaut(false);
                //-------------------------------------------------------------------------------------------------------
                selectedAgence = currentUserAgence.get(0).getIdagence();
                //-------------------------------------------------------------------------------------------------------
                tableReset();
                break;
            case "cop":
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
                rechercheConcurrent();
                break;
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
                ut.begin();
                List<Agence> agenceList = new ArrayList();
                agenceList.add(new Agence(selectedAgence));
                switch (operation) {
                    case "add":
                        selectedConcurrent.setLibelle((selectedConcurrent.getLibelle() + "").trim());
                        selectedConcurrent.setLabel((selectedConcurrent.getLabel() + "").trim());
                        libelleOperation = routine.localizeMessage("Ajouter", new Locale("fr")) + " " + routine.localizeMessage("Un", new Locale("fr")) + " " + routine.localizeMessage("Concurrent", new Locale("fr"));
                        libelleOperation_en = routine.localizeMessage("Ajouter", new Locale("en")) + " " + routine.localizeMessage("Un", new Locale("en")) + " " + routine.localizeMessage("Concurrent", new Locale("en"));
                        if (concurrentFacade.findByLien_Etat(selectedConcurrent.getLien().toUpperCase(ROOT), "Actif".toUpperCase(ROOT), agenceList).isEmpty()) {
                            //if (concurrentFacade.findByLibelle_Etat(selectedConcurrent.getLibelle().toUpperCase(ROOT), "Actif".toUpperCase(ROOT), agenceList).isEmpty() || concurrentFacade.findByLabel_Etat(selectedConcurrent.getLabel().toUpperCase(ROOT), "Actif".toUpperCase(ROOT), agenceList).isEmpty()) {
                            selectedConcurrent.setEtat("Actif");
                            selectedConcurrent.setDateEnregistre(new Date(currentTimeMillis()));
                            selectedConcurrent.setDerniereModif(new Date(currentTimeMillis()));
                            //-------------------------------------------------------------------------------------------------------
                            selectedConcurrent.setIdagence(selectedAgence != null ? new Agence(selectedAgence) : null);
                            //-------------------------------------------------------------------------------------------------------
                            if (selectedConcurrent.getDefaut()) {
                                if (!concurrentFacade.findByDefaut_Etat(true, "Actif".toUpperCase(ROOT), agenceList).isEmpty()) {
                                    Concurrent cptr = concurrentFacade.findByDefaut_Etat(true, "Actif".toUpperCase(ROOT), agenceList).get(0);
                                    cptr.setDefaut(false);
                                    concurrentFacade.edit(cptr);
                                }
                            }
                            //-------------------------------------------------------------------------------------------------------
                            selectedConcurrent.setIdconcurrent(concurrentFacade.nextId());
                            concurrentFacade.create(selectedConcurrent);
                            routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, langue.equals("fr") ? selectedConcurrent.getLibelle() : selectedConcurrent.getLabel(), connectedUser, operationsFacade);
                            routine.feedBack("Information", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Succes_operation"), null);
                        } else {
                            routine.feedBack("Avertissement", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Doublon"), null);
                        }
                        break;
                    case "cop":
                        selectedConcurrent.setLibelle((selectedConcurrent.getLibelle() + "").trim());
                        selectedConcurrent.setLabel((selectedConcurrent.getLabel() + "").trim());
                        libelleOperation = routine.localizeMessage("Copier", new Locale("fr")) + " " + routine.localizeMessage("Un", new Locale("fr")) + " " + routine.localizeMessage("Concurrent", new Locale("fr"));
                        libelleOperation_en = routine.localizeMessage("Copier", new Locale("en")) + " " + routine.localizeMessage("Un", new Locale("en")) + " " + routine.localizeMessage("Concurrent", new Locale("en"));
                        if (concurrentFacade.findByLien_Etat(selectedConcurrent.getLibelle().toUpperCase(ROOT), "Actif".toUpperCase(ROOT), agenceList).isEmpty()) {
                            //if (concurrentFacade.findByLibelle_Etat(selectedConcurrent.getLibelle().toUpperCase(ROOT), "Actif".toUpperCase(ROOT), agenceList).isEmpty() || concurrentFacade.findByLabel_Etat(selectedConcurrent.getLabel().toUpperCase(ROOT), "Actif".toUpperCase(ROOT), agenceList).isEmpty()) {
                            selectedConcurrent.setDateEnregistre(new Date(currentTimeMillis()));
                            selectedConcurrent.setDerniereModif(new Date(currentTimeMillis()));
                            //-------------------------------------------------------------------------------------------------------
                            selectedConcurrent.setIdagence(selectedAgence != null ? new Agence(selectedAgence) : null);
                            //-------------------------------------------------------------------------------------------------------
                            if (selectedConcurrent.getDefaut()) {
                                if (!concurrentFacade.findByDefaut_Etat(true, "Actif".toUpperCase(ROOT), agenceList).isEmpty()) {
                                    Concurrent cptr = concurrentFacade.findByDefaut_Etat(true, "Actif".toUpperCase(ROOT), agenceList).get(0);
                                    cptr.setDefaut(false);
                                    concurrentFacade.edit(cptr);
                                }
                            }
                            //-------------------------------------------------------------------------------------------------------
                            selectedConcurrent.setIdconcurrent(concurrentFacade.nextId());
                            concurrentFacade.create(selectedConcurrent);
                            routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, langue.equals("fr") ? selectedConcurrent.getLibelle() : selectedConcurrent.getLabel(), connectedUser, operationsFacade);
                            routine.feedBack("Information", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Succes_operation"), null);
                        } else {
                            routine.feedBack("Avertissement", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Doublon"), null);
                        }
                        break;
                    case "mod":
                        selectedConcurrent.setLibelle((selectedConcurrent.getLibelle() + "").trim());
                        selectedConcurrent.setLabel((selectedConcurrent.getLabel() + "").trim());
                        libelleOperation = routine.localizeMessage("Modifier", new Locale("fr")) + " " + routine.localizeMessage("Un", new Locale("fr")) + " " + routine.localizeMessage("Concurrent", new Locale("fr"));
                        libelleOperation_en = routine.localizeMessage("Modifier", new Locale("en")) + " " + routine.localizeMessage("Un", new Locale("en")) + " " + routine.localizeMessage("Concurrent", new Locale("en"));
                        if (concurrentFacade.findByLien_Etat(selectedConcurrent.getLibelle().toUpperCase(ROOT), "Actif".toUpperCase(ROOT), agenceList).isEmpty()) {
                            //if (((!selectedConcurrent.getLibelle().toUpperCase(ROOT).equals(oldValue.toUpperCase(ROOT)) && concurrentFacade.findByLibelle_Etat(selectedConcurrent.getLibelle().toUpperCase(ROOT), "Actif".toUpperCase(ROOT), agenceList).isEmpty()) || (selectedConcurrent.getLibelle().toUpperCase(ROOT).equals(oldValue.toUpperCase(ROOT)))) || ((!selectedConcurrent.getLabel().toUpperCase(ROOT).equals(oldValue_en.toUpperCase(ROOT)) && concurrentFacade.findByLabel_Etat(selectedConcurrent.getLabel().toUpperCase(ROOT), "Actif".toUpperCase(ROOT), agenceList).isEmpty()) || (selectedConcurrent.getLabel().toUpperCase(ROOT).equals(oldValue_en.toUpperCase(ROOT))))) {
                            selectedConcurrent.setDerniereModif(new Date(currentTimeMillis()));
                            //-------------------------------------------------------------------------------------------------------
                            selectedConcurrent.setIdagence(selectedAgence != null ? new Agence(selectedAgence) : null);
                            //-------------------------------------------------------------------------------------------------------
                            if (selectedConcurrent.getDefaut()) {
                                if (!concurrentFacade.findByDefaut_Etat(true, "Actif".toUpperCase(ROOT), agenceList).isEmpty()) {
                                    Concurrent cptr = concurrentFacade.findByDefaut_Etat(true, "Actif".toUpperCase(ROOT), agenceList).get(0);
                                    cptr.setDefaut(false);
                                    concurrentFacade.edit(cptr);
                                }
                            }
                            //-------------------------------------------------------------------------------------------------------
                            concurrentFacade.edit(selectedConcurrent);
                            //-------------------------------------------------------------------------------------------------------
                            routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, langue.equals("fr") ? selectedConcurrent.getLibelle() : selectedConcurrent.getLabel(), connectedUser, operationsFacade);
                            routine.feedBack("Information", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Succes_operation"), null);
                        } else {
                            routine.feedBack("Avertissement", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Doublon"), null);
                        }
                        break;
                    case "sup":
                        int not = 0;
                        libelleOperation = routine.localizeMessage("Supprimer", new Locale("fr")) + " " + routine.localizeMessage("Un", new Locale("fr")) + " " + routine.localizeMessage("Concurrent", new Locale("fr"));
                        libelleOperation_en = routine.localizeMessage("Supprimer", new Locale("en")) + " " + routine.localizeMessage("Un", new Locale("en")) + " " + routine.localizeMessage("Concurrent", new Locale("en"));
                        for (Concurrent concurrent_ : selectedListConcurrent) {
                            if (concurrent_.getVeilleconcurrentCollection().isEmpty()) {
                                concurrent_.setDerniereModif(new Date(currentTimeMillis()));
                                concurrent_.setEtat("Supprime");
                                concurrentFacade.edit(concurrent_);
                                routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, langue.equals("fr") ? concurrent_.getLien() : concurrent_.getLien(), connectedUser, operationsFacade);
                            } else {
                                not++;
                            }
                        }
                        if (not > 0) {
                            routine.feedBack("Avertissement", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Suppression_impossible") + " " + not + " " + routine.localizeMessage("Reference_impossible") + " (Veille Concurrence)", null);
                        } else {
                            routine.feedBack("Information", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Succes_operation"), null);
                        }
                        break;
                    case "res":
                        libelleOperation = routine.localizeMessage("Restaurer", new Locale("fr")) + " " + routine.localizeMessage("Un", new Locale("fr")) + " " + routine.localizeMessage("Concurrent", new Locale("fr"));
                        libelleOperation_en = routine.localizeMessage("Restaurer", new Locale("en")) + " " + routine.localizeMessage("Un", new Locale("en")) + " " + routine.localizeMessage("Concurrent", new Locale("en"));
                        for (Concurrent concurrent_ : selectedListConcurrent) {
                            if (concurrentFacade.findByLien_Etat(concurrent_.getLibelle().toUpperCase(ROOT), "Actif".toUpperCase(ROOT), agenceList).isEmpty()) {
                                //if (concurrentFacade.findByLibelle_Etat(concurrent_.getLibelle().toUpperCase(ROOT), "Actif".toUpperCase(ROOT), agenceList).isEmpty() || concurrentFacade.findByLabel_Etat(concurrent_.getLabel().toUpperCase(ROOT), "Actif".toUpperCase(ROOT), agenceList).isEmpty()) {
                                concurrent_.setDerniereModif(new Date(currentTimeMillis()));
                                concurrent_.setEtat("Actif");
                                concurrentFacade.edit(concurrent_);
                                routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, langue.equals("fr") ? concurrent_.getLien() : concurrent_.getLien(), connectedUser, operationsFacade);
                            } else {
                                libelleOperation = routine.localizeMessage("Detruire", new Locale("fr")) + " " + routine.localizeMessage("Un", new Locale("fr")) + " " + routine.localizeMessage("Concurrent", new Locale("fr")) + " [2]";
                                libelleOperation_en = routine.localizeMessage("Detruire", new Locale("en")) + " " + routine.localizeMessage("Un", new Locale("en")) + " " + routine.localizeMessage("Concurrent", new Locale("en")) + " [2]";
                                concurrentFacade.remove(concurrent_);
                                routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, langue.equals("fr") ? concurrent_.getLien() : concurrent_.getLien(), connectedUser, operationsFacade);
                            }
                        }
                        routine.feedBack("Information", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Succes_operation"), null);
                        break;
                    case "det":
                        libelleOperation = routine.localizeMessage("Detruire", new Locale("fr")) + " " + routine.localizeMessage("Un", new Locale("fr")) + " " + routine.localizeMessage("Concurrent", new Locale("fr"));
                        libelleOperation_en = routine.localizeMessage("Detruire", new Locale("en")) + " " + routine.localizeMessage("Un", new Locale("en")) + " " + routine.localizeMessage("Concurrent", new Locale("en"));
                        for (Concurrent concurrent_ : selectedListConcurrent) {
                            concurrentFacade.remove(concurrent_);
                            routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, langue.equals("fr") ? concurrent_.getLien() : concurrent_.getLien(), connectedUser, operationsFacade);
                        }
                        routine.feedBack("Information", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Succes_operation"), null);
                        break;
                }
                ut.commit();
            } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException e) {
                routine.feedBack("Erreur", langue.equals("fr") ? libelleOperation : libelleOperation_en, null, e);
                e.printStackTrace();
            }
            rechercheConcurrent();
        }
    }

    public String imprimer() {
        try {
            JRBeanCollectionDataSource beanCollectionDataSource;
            if (selectedListConcurrent.isEmpty()) {
                beanCollectionDataSource = new JRBeanCollectionDataSource(lazyListConcurrent);
            } else {
                beanCollectionDataSource = new JRBeanCollectionDataSource(selectedListConcurrent);
            }
            String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("resources/etats/concurrent.jasper");
            Map parameters = new HashMap();
            selectedCritere.setAgence(currentUserAgence.get(0));
            parameters.put("REPORT_FILTER", selectedCritere);
            parameters.put("USER", connectedUser);
            parameters.put("REPORT_LOCALE", FacesContext.getCurrentInstance().getViewRoot().getLocale());
            JasperPrint jasperPrint = fillReport(reportPath, parameters, beanCollectionDataSource);
            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            httpServletResponse.addHeader("Content-disposition", "attachment; filename=ListeConcurrent.pdf");
            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
            exportReportToPdfStream(jasperPrint, servletOutputStream);
            servletOutputStream.close();
            FacesContext.getCurrentInstance().responseComplete();
            //----------------------------------------------
            String libelleOperation = routine.localizeMessage("Impression", new Locale("fr")) + " (" + routine.localizeMessage("Concurrent", new Locale("fr")) + ")";
            String libelleOperation_en = routine.localizeMessage("Impression", new Locale("en")) + " (" + routine.localizeMessage("Concurrent", new Locale("en")) + ")";
            routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, routine.localizeMessage("Concurrent"), connectedUser, operationsFacade);
            //----------------------------------------------
        } catch (JRException | IOException e) {
            e.printStackTrace();
        }
        ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
        return ext.getRequestServletPath() + "?faces-redirect=true";
    }
    //--------------------------------------------------

    public String concurrent() {
        return "concurrent.xhtml?faces-redirect=true";
    }

    public void tableReset() {
        selectedListConcurrent.clear();
        rowClick();
    }

    public void loadAgence() {
        listAgence.clear();
        listAgence.addAll(currentUserAgence);
    }

    public String closePage() {
        return "moduleVeille.xhtml?faces-redirect=true";
    }

    public ConcurrentFacadeLocal getConcurrentFacade() {
        return concurrentFacade;
    }

    public void setConcurrentFacade(ConcurrentFacadeLocal concurrentFacade) {
        this.concurrentFacade = concurrentFacade;
    }

    public List<Concurrent> getListConcurrent() {
        return listConcurrent;
    }

    public void setListConcurrent(List<Concurrent> listConcurrent) {
        this.listConcurrent = listConcurrent;
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

    public Concurrent getSelectedConcurrent() {
        return selectedConcurrent;
    }

    public void setSelectedConcurrent(Concurrent selectedConcurrent) {
        this.selectedConcurrent = selectedConcurrent;
    }

    public List<Concurrent> getSelectedListConcurrent() {
        return selectedListConcurrent;
    }

    public void setSelectedListConcurrent(List<Concurrent> selectedListConcurrent) {
        this.selectedListConcurrent = selectedListConcurrent;
    }

    public OperationsFacadeLocal getOperationsFacade() {
        return operationsFacade;
    }

    public void setOperationsFacade(OperationsFacadeLocal operationsFacade) {
        this.operationsFacade = operationsFacade;
    }

    public Compte getConnectedUser() {
        return connectedUser;
    }

    public void setConnectedUser(Compte connectedUser) {
        this.connectedUser = connectedUser;
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

    public Integer getSelectedAgence() {
        return selectedAgence;
    }

    public void setSelectedAgence(Integer selectedAgence) {
        this.selectedAgence = selectedAgence;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public List<Agence> getCurrentUserAgence() {
        return currentUserAgence;
    }

    public void setCurrentUserAgence(List<Agence> currentUserAgence) {
        this.currentUserAgence = currentUserAgence;
    }

    public LazyDataModel<Concurrent> getLazylistConcurrentModel() {
        return lazylistConcurrentModel;
    }

    public void setLazylistConcurrentModel(LazyDataModel<Concurrent> lazylistConcurrentModel) {
        this.lazylistConcurrentModel = lazylistConcurrentModel;
    }

    public List<Concurrent> getLazyListConcurrent() {
        return lazyListConcurrent;
    }

    public void setLazyListConcurrent(List<Concurrent> lazyListConcurrent) {
        this.lazyListConcurrent = lazyListConcurrent;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

}
