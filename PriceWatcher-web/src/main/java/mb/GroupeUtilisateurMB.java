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
import entities.GroupeUtilisateur;
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
import org.primefaces.model.DualListModel;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import sessions.AgenceFacadeLocal;
import sessions.CompteFacadeLocal;
import sessions.GroupeUtilisateurFacadeLocal;
import sessions.OperationsFacadeLocal;

/**
 *
 * @author Lucien FOTSA
 */
public class GroupeUtilisateurMB implements Serializable, Converter {

    @Resource
    private UserTransaction ut;
    @EJB
    private OperationsFacadeLocal operationsFacade;
    //--------------------------------------------------------------------------
    @EJB
    private GroupeUtilisateurFacadeLocal groupeUtilisateurFacade;
    private List<GroupeUtilisateur> listGroupeUtilisateur = new ArrayList<>();
    private List<GroupeUtilisateur> selectedListGroupeUtilisateur = new ArrayList<>();
    private GroupeUtilisateur selectedGroupeUtilisateur = new GroupeUtilisateur();
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
    //--------------------------------------------------------------------------
    @EJB
    private CompteFacadeLocal compteFacade;
    private DualListModel<Compte> listCompte = new DualListModel<>(new ArrayList<>(), new ArrayList<>());
    //--------------------------------------------
    private List<CritereFiltre> listCritereFiltre = new ArrayList<>();
    private CritereFiltre selectedCritere = new CritereFiltre("Tous", routine.localizeMessage("Tous"), "Tous", "Actif");
    //---------------------------------------------------------
    private List<Agence> currentUserAgence = new ArrayList<>();
    private LazyDataModel<GroupeUtilisateur> lazylistGroupeUtilisateurModel;
    private List<GroupeUtilisateur> lazyListGroupeUtilisateur = new ArrayList<>();
    private boolean lock = false;

    /**
     * Creates a new instance of GroupeUtilisateurMB
     */
    public GroupeUtilisateurMB() {
    }

    @PostConstruct
    private void init() {
        connectedUser = (Compte) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUser");
        currentUserAgence = (List<Agence>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUserAgence");
        loadCritere();
        selectCritere();
//        rechercheGroupeUtilisateur();
    }

    //--------------------------------------------------
    public void loadCritere() {
        listCritereFiltre.clear();
        listCritereFiltre.add(new CritereFiltre("Tous", routine.localizeMessage("Tous"), "Tous", "Actif"));
        listCritereFiltre.add(new CritereFiltre("libelle", routine.localizeMessage("Libelle"), "String", "Actif"));
        listCritereFiltre.add(new CritereFiltre("idagence", routine.localizeMessage("Agence"), "Select", "Actif"));
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
        }
    }

    public void rechercheGroupeUtilisateur() {
        listGroupeUtilisateur.clear();
        switch (selectedCritere.getAttribut()) {
            case "Tous":
                listGroupeUtilisateur.addAll(groupeUtilisateurFacade.findAll_Etat(selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!groupeUtilisateurFacade.findAll_Etat("Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty()) : true);
                selectedCritere.setUsedValue(routine.localizeMessage("Tous"));
                break;
            case "libelle":
                listGroupeUtilisateur.addAll(FacesContext.getCurrentInstance().getViewRoot().getLocale().getLanguage().equals(new Locale("fr").getLanguage()) ? groupeUtilisateurFacade.findByLibelle_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence) : groupeUtilisateurFacade.findByLabel_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (FacesContext.getCurrentInstance().getViewRoot().getLocale().getLanguage().equals(new Locale("fr").getLanguage()) ? (!groupeUtilisateurFacade.findByLibelle_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence).isEmpty()) : (!groupeUtilisateurFacade.findByLabel_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence).isEmpty())) : true);
                selectedCritere.setUsedValue(selectedCritere.getValString());
                break;
            case "idagence":
                List<Agence> agenceList = new ArrayList<>();
                agenceList.add(new Agence(selectedCritere.getValSelect()));
                listGroupeUtilisateur.addAll(groupeUtilisateurFacade.findAll_Etat(selectedCritere.getEtat().toUpperCase(ROOT), agenceList));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!groupeUtilisateurFacade.findAll_Etat("Supprime".toUpperCase(ROOT), agenceList).isEmpty()) : true);
                selectedCritere.setUsedValue(agenceFacade.find(selectedCritere.getValSelect()).getRaisonsociale());
                break;
            case "dateEnregistre":
                routine.correctDateTime(selectedCritere.getValDate1(), new Date(selectedCritere.getValDate2().getTime() + 59999L), null, null);
                listGroupeUtilisateur.addAll(groupeUtilisateurFacade.findByDateEnregistre_Etat(selectedCritere.getValDate1(), new Date(selectedCritere.getValDate2().getTime() + 59999L), selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!groupeUtilisateurFacade.findByDateEnregistre_Etat(selectedCritere.getValDate1(), new Date(selectedCritere.getValDate2().getTime() + 59999L), "Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty()) : true);
                selectedCritere.setUsedValue(new SimpleDateFormat("dd/MM/yyyy").format(selectedCritere.getValDate1()) + " - " + new SimpleDateFormat("dd/MM/yyyy").format(selectedCritere.getValDate2()));
                break;
            case "derniereModif":
                routine.correctDateTime(selectedCritere.getValDate1(), new Date(selectedCritere.getValDate2().getTime() + 59999L), null, null);
                listGroupeUtilisateur.addAll(groupeUtilisateurFacade.findByDerniereModif_Etat(selectedCritere.getValDate1(), new Date(selectedCritere.getValDate2().getTime() + 59999L), selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!groupeUtilisateurFacade.findByDerniereModif_Etat(selectedCritere.getValDate1(), new Date(selectedCritere.getValDate2().getTime() + 59999L), "Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty()) : true);
                selectedCritere.setUsedValue(new SimpleDateFormat("dd/MM/yyyy").format(selectedCritere.getValDate1()) + " - " + new SimpleDateFormat("dd/MM/yyyy").format(selectedCritere.getValDate2()));
                break;
        }
        //**********************************************************************************************************

        lazylistGroupeUtilisateurModel = new LazyDataModel<GroupeUtilisateur>() {
            @Override
            public GroupeUtilisateur getRowData(String rowKey) {
                for (GroupeUtilisateur ctn : lazyListGroupeUtilisateur) {
                    if (ctn.getIdGroupeUtilisateur().toString().equals(rowKey)) {
                        return ctn;
                    }
                }
                return null;
            }

            @Override
            public Object getRowKey(GroupeUtilisateur ctn) {
                return ctn.getIdGroupeUtilisateur();
            }

            @Override
            public List<GroupeUtilisateur> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, Object> filters) {
                List<GroupeUtilisateur> data = new ArrayList<>();
                //filter
                for (GroupeUtilisateur ctn : listGroupeUtilisateur) {
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
                        Collections.sort(data, new GroupeUtilisateur(sm.getSortField(), (SortOrder.ASCENDING.equals(sm.getSortOrder()) ? 1 : -1)));
                    }
                }

                //rowCount
                int dataSize = data.size();
                this.setRowCount(dataSize);

                //paginate
                lazyListGroupeUtilisateur.clear();
                if (dataSize > pageSize) {
                    try {
                        lazyListGroupeUtilisateur.addAll(data.subList(first, first + pageSize));
                        return data.subList(first, first + pageSize);
                    } catch (IndexOutOfBoundsException e) {
                        lazyListGroupeUtilisateur.addAll(data.subList(first, first + (dataSize % pageSize)));
                        return data.subList(first, first + (dataSize % pageSize));
                    }

                } else {
                    lazyListGroupeUtilisateur.addAll(data);
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
        if (!listGroupeUtilisateur.isEmpty()) {
            routine.setImp(true);
        }
        if (selectedListGroupeUtilisateur.size() > 1) {
            if (selectedCritere.getEtat().equals("Actif")) {
                routine.setSup(true);
            } else {
                routine.setRes(true);
                routine.setDet(true);
            }
        } else if (selectedListGroupeUtilisateur.size() == 1) {
            selectedGroupeUtilisateur = groupeUtilisateurFacade.find(selectedListGroupeUtilisateur.get(0).getIdGroupeUtilisateur());
            oldValue = selectedGroupeUtilisateur.getLibelle();
            oldValue_en = selectedGroupeUtilisateur.getLabel();
            //-------------------------------------------------------------------------------------------------------
            selectedAgence = selectedGroupeUtilisateur.getIdagence() != null ? selectedGroupeUtilisateur.getIdagence().getIdagence() : null;
            loadCompte();
            listCompte.setTarget((List) selectedGroupeUtilisateur.getCompteCollection());
            listCompte.getSource().removeAll(listCompte.getTarget());
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
                selectedGroupeUtilisateur = new GroupeUtilisateur();
                //-------------------------------------------------------------------------------------------------------
                selectedAgence = currentUserAgence.get(0).getIdagence();
                loadCompte();
//                listCompte = new DualListModel<>(new ArrayList<>(), new ArrayList<>());
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
                rechercheGroupeUtilisateur();
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
                List<Agence> agenceList = new ArrayList<>();
                agenceList.add(new Agence(selectedAgence));
                switch (operation) {
                    case "add":
                        selectedGroupeUtilisateur.setLibelle((selectedGroupeUtilisateur.getLibelle() + "").trim());
                        selectedGroupeUtilisateur.setLabel((selectedGroupeUtilisateur.getLabel() + "").trim());
                        libelleOperation = routine.localizeMessage("Ajouter", new Locale("fr")) + " " + routine.localizeMessage("Un", new Locale("fr")) + " " + routine.localizeMessage("GroupeUtilisateur", new Locale("fr"));
                        libelleOperation_en = routine.localizeMessage("Ajouter", new Locale("en")) + " " + routine.localizeMessage("Un", new Locale("en")) + " " + routine.localizeMessage("GroupeUtilisateur", new Locale("en"));
                        if (groupeUtilisateurFacade.findByLibelle_Etat(selectedGroupeUtilisateur.getLibelle().toUpperCase(ROOT), "Actif".toUpperCase(ROOT), agenceList).isEmpty() || groupeUtilisateurFacade.findByLabel_Etat(selectedGroupeUtilisateur.getLabel().toUpperCase(ROOT), "Actif".toUpperCase(ROOT), agenceList).isEmpty()) {
                            selectedGroupeUtilisateur.setEtat("Actif");
                            selectedGroupeUtilisateur.setDateEnregistre(new Date(currentTimeMillis()));
                            selectedGroupeUtilisateur.setDerniereModif(new Date(currentTimeMillis()));
                            //-------------------------------------------------------------------------------------------------------
                            selectedGroupeUtilisateur.setIdagence(selectedAgence != null ? new Agence(selectedAgence) : null);
                            selectedGroupeUtilisateur.setCompteCollection(listCompte.getTarget());
                            //-------------------------------------------------------------------------------------------------------
                            selectedGroupeUtilisateur.setIdGroupeUtilisateur(groupeUtilisateurFacade.nextId());
                            groupeUtilisateurFacade.create(selectedGroupeUtilisateur);
                            routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, langue.equals("fr") ? selectedGroupeUtilisateur.getLibelle() : selectedGroupeUtilisateur.getLabel(), connectedUser, operationsFacade);
                            routine.feedBack("Information", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Succes_operation"), null);
                        } else {
                            routine.feedBack("Avertissement", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Doublon"), null);
                        }
                        break;
                    case "cop":
                        selectedGroupeUtilisateur.setLibelle((selectedGroupeUtilisateur.getLibelle() + "").trim());
                        selectedGroupeUtilisateur.setLabel((selectedGroupeUtilisateur.getLabel() + "").trim());
                        libelleOperation = routine.localizeMessage("Copier", new Locale("fr")) + " " + routine.localizeMessage("Un", new Locale("fr")) + " " + routine.localizeMessage("GroupeUtilisateur", new Locale("fr"));
                        libelleOperation_en = routine.localizeMessage("Copier", new Locale("en")) + " " + routine.localizeMessage("Un", new Locale("en")) + " " + routine.localizeMessage("GroupeUtilisateur", new Locale("en"));
                        if (groupeUtilisateurFacade.findByLibelle_Etat(selectedGroupeUtilisateur.getLibelle().toUpperCase(ROOT), "Actif".toUpperCase(ROOT), agenceList).isEmpty() || groupeUtilisateurFacade.findByLabel_Etat(selectedGroupeUtilisateur.getLabel().toUpperCase(ROOT), "Actif".toUpperCase(ROOT), agenceList).isEmpty()) {
                            selectedGroupeUtilisateur.setDateEnregistre(new Date(currentTimeMillis()));
                            selectedGroupeUtilisateur.setDerniereModif(new Date(currentTimeMillis()));
                            //-------------------------------------------------------------------------------------------------------
                            selectedGroupeUtilisateur.setIdagence(selectedAgence != null ? new Agence(selectedAgence) : null);
                            selectedGroupeUtilisateur.setCompteCollection(listCompte.getTarget());
                            //-------------------------------------------------------------------------------------------------------
                            selectedGroupeUtilisateur.setIdGroupeUtilisateur(groupeUtilisateurFacade.nextId());
                            groupeUtilisateurFacade.create(selectedGroupeUtilisateur);
                            routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, langue.equals("fr") ? selectedGroupeUtilisateur.getLibelle() : selectedGroupeUtilisateur.getLabel(), connectedUser, operationsFacade);
                            routine.feedBack("Information", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Succes_operation"), null);
                        } else {
                            routine.feedBack("Avertissement", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Doublon"), null);
                        }
                        break;
                    case "mod":
                        selectedGroupeUtilisateur.setLibelle((selectedGroupeUtilisateur.getLibelle() + "").trim());
                        selectedGroupeUtilisateur.setLabel((selectedGroupeUtilisateur.getLabel() + "").trim());
                        libelleOperation = routine.localizeMessage("Modifier", new Locale("fr")) + " " + routine.localizeMessage("Un", new Locale("fr")) + " " + routine.localizeMessage("GroupeUtilisateur", new Locale("fr"));
                        libelleOperation_en = routine.localizeMessage("Modifier", new Locale("en")) + " " + routine.localizeMessage("Un", new Locale("en")) + " " + routine.localizeMessage("GroupeUtilisateur", new Locale("en"));
                        if (((!selectedGroupeUtilisateur.getLibelle().toUpperCase(ROOT).equals(oldValue.toUpperCase(ROOT)) && groupeUtilisateurFacade.findByLibelle_Etat(selectedGroupeUtilisateur.getLibelle().toUpperCase(ROOT), "Actif".toUpperCase(ROOT), agenceList).isEmpty()) || (selectedGroupeUtilisateur.getLibelle().toUpperCase(ROOT).equals(oldValue.toUpperCase(ROOT)))) || ((!selectedGroupeUtilisateur.getLabel().toUpperCase(ROOT).equals(oldValue_en.toUpperCase(ROOT)) && groupeUtilisateurFacade.findByLabel_Etat(selectedGroupeUtilisateur.getLabel().toUpperCase(ROOT), "Actif".toUpperCase(ROOT), agenceList).isEmpty()) || (selectedGroupeUtilisateur.getLabel().toUpperCase(ROOT).equals(oldValue_en.toUpperCase(ROOT))))) {
                            selectedGroupeUtilisateur.setDerniereModif(new Date(currentTimeMillis()));
                            //-------------------------------------------------------------------------------------------------------
                            selectedGroupeUtilisateur.setIdagence(selectedAgence != null ? new Agence(selectedAgence) : null);
                            selectedGroupeUtilisateur.setCompteCollection(listCompte.getTarget());
                            //-------------------------------------------------------------------------------------------------------
                            groupeUtilisateurFacade.edit(selectedGroupeUtilisateur);
                            routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, langue.equals("fr") ? selectedGroupeUtilisateur.getLibelle() : selectedGroupeUtilisateur.getLabel(), connectedUser, operationsFacade);
                            routine.feedBack("Information", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Succes_operation"), null);
                        } else {
                            routine.feedBack("Avertissement", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Doublon"), null);
                        }
                        break;
                    case "sup":
                        int not = 0;
                        libelleOperation = routine.localizeMessage("Supprimer", new Locale("fr")) + " " + routine.localizeMessage("Un", new Locale("fr")) + " " + routine.localizeMessage("GroupeUtilisateur", new Locale("fr"));
                        libelleOperation_en = routine.localizeMessage("Supprimer", new Locale("en")) + " " + routine.localizeMessage("Un", new Locale("en")) + " " + routine.localizeMessage("GroupeUtilisateur", new Locale("en"));
                        for (GroupeUtilisateur groupeUtilisateur_ : selectedListGroupeUtilisateur) {
                            if (groupeUtilisateur_.getCompteCollection().isEmpty()) {
                                groupeUtilisateur_.setDerniereModif(new Date(currentTimeMillis()));
                                groupeUtilisateur_.setEtat("Supprime");
                                groupeUtilisateurFacade.edit(groupeUtilisateur_);
                                routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, langue.equals("fr") ? groupeUtilisateur_.getLibelle() : groupeUtilisateur_.getLabel(), connectedUser, operationsFacade);
                            } else {
                                not++;
                            }
                        }
                        if (not > 0) {
                            routine.feedBack("Avertissement", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Suppression_impossible") + " " + not + " " + routine.localizeMessage("Reference_impossible") + " (" + routine.localizeMessage("Compte") + ")", null);
                        } else {
                            routine.feedBack("Information", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Succes_operation"), null);
                        }
                        break;
                    case "res":
                        libelleOperation = routine.localizeMessage("Restaurer", new Locale("fr")) + " " + routine.localizeMessage("Un", new Locale("fr")) + " " + routine.localizeMessage("GroupeUtilisateur", new Locale("fr"));
                        libelleOperation_en = routine.localizeMessage("Restaurer", new Locale("en")) + " " + routine.localizeMessage("Un", new Locale("en")) + " " + routine.localizeMessage("GroupeUtilisateur", new Locale("en"));
                        for (GroupeUtilisateur groupeUtilisateur_ : selectedListGroupeUtilisateur) {
                            if (groupeUtilisateurFacade.findByLibelle_Etat(groupeUtilisateur_.getLibelle().toUpperCase(ROOT), "Actif".toUpperCase(ROOT), agenceList).isEmpty() || groupeUtilisateurFacade.findByLabel_Etat(groupeUtilisateur_.getLabel().toUpperCase(ROOT), "Actif".toUpperCase(ROOT), agenceList).isEmpty()) {
                                groupeUtilisateur_.setDerniereModif(new Date(currentTimeMillis()));
                                groupeUtilisateur_.setEtat("Actif");
                                groupeUtilisateurFacade.edit(groupeUtilisateur_);
                                routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, langue.equals("fr") ? groupeUtilisateur_.getLibelle() : groupeUtilisateur_.getLabel(), connectedUser, operationsFacade);
                            } else {
                                libelleOperation = routine.localizeMessage("Detruire", new Locale("fr")) + " " + routine.localizeMessage("Un", new Locale("fr")) + " " + routine.localizeMessage("GroupeUtilisateur", new Locale("fr")) + " [2]";
                                libelleOperation_en = routine.localizeMessage("Detruire", new Locale("en")) + " " + routine.localizeMessage("Un", new Locale("en")) + " " + routine.localizeMessage("GroupeUtilisateur", new Locale("en")) + " [2]";
                                groupeUtilisateurFacade.remove(groupeUtilisateur_);
                                routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, langue.equals("fr") ? groupeUtilisateur_.getLibelle() : groupeUtilisateur_.getLabel(), connectedUser, operationsFacade);
                            }
                        }
                        routine.feedBack("Information", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Succes_operation"), null);
                        break;
                    case "det":
                        libelleOperation = routine.localizeMessage("Detruire", new Locale("fr")) + " " + routine.localizeMessage("Un", new Locale("fr")) + " " + routine.localizeMessage("GroupeUtilisateur", new Locale("fr"));
                        libelleOperation_en = routine.localizeMessage("Detruire", new Locale("en")) + " " + routine.localizeMessage("Un", new Locale("en")) + " " + routine.localizeMessage("GroupeUtilisateur", new Locale("en"));
                        for (GroupeUtilisateur groupeUtilisateur_ : selectedListGroupeUtilisateur) {
                            groupeUtilisateurFacade.remove(groupeUtilisateur_);
                            routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, langue.equals("fr") ? groupeUtilisateur_.getLibelle() : groupeUtilisateur_.getLabel(), connectedUser, operationsFacade);
                        }
                        routine.feedBack("Information", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Succes_operation"), null);
                        break;
                }
                ut.commit();
            } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException e) {
                routine.feedBack("Erreur", langue.equals("fr") ? libelleOperation : libelleOperation_en, null, e);
                e.printStackTrace();
            }
            rechercheGroupeUtilisateur();
        }
    }

    public String imprimer() {
        try {
            JRBeanCollectionDataSource beanCollectionDataSource;
            if (selectedListGroupeUtilisateur.isEmpty()) {
                beanCollectionDataSource = new JRBeanCollectionDataSource(lazyListGroupeUtilisateur);
            } else {
                beanCollectionDataSource = new JRBeanCollectionDataSource(selectedListGroupeUtilisateur);
            }
            String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("resources/etats/groupeUtilisateur.jasper");
            Map parameters = new HashMap();
            selectedCritere.setAgence(currentUserAgence.get(0));
            parameters.put("REPORT_FILTER", selectedCritere);
            parameters.put("USER", connectedUser);
            parameters.put("REPORT_LOCALE", FacesContext.getCurrentInstance().getViewRoot().getLocale());
            JasperPrint jasperPrint = fillReport(reportPath, parameters, beanCollectionDataSource);
            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            httpServletResponse.addHeader("Content-disposition", "attachment; filename=ListeGroupeUtilisateur.pdf");
            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
            exportReportToPdfStream(jasperPrint, servletOutputStream);
            servletOutputStream.close();
            FacesContext.getCurrentInstance().responseComplete();
            //----------------------------------------------
            String libelleOperation = routine.localizeMessage("Impression", new Locale("fr")) + " (" + routine.localizeMessage("GroupeUtilisateur", new Locale("fr")) + ")";
            String libelleOperation_en = routine.localizeMessage("Impression", new Locale("en")) + " (" + routine.localizeMessage("GroupeUtilisateur", new Locale("en")) + ")";
            routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, routine.localizeMessage("GroupeUtilisateur"), connectedUser, operationsFacade);
            //----------------------------------------------
        } catch (JRException | IOException e) {
            e.printStackTrace();
        }
        ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
        return ext.getRequestServletPath() + "?faces-redirect=true";
    }
    //--------------------------------------------------

    public String groupeUtilisateur() {
        return "groupeUtilisateur.xhtml?faces-redirect=true";
    }

    public void tableReset() {
        selectedListGroupeUtilisateur.clear();
        rowClick();
    }

    public void loadAgence() {
        listAgence.clear();
        listAgence.addAll(currentUserAgence);
    }

    public void loadCompte() {
        List<Agence> agenceList = new ArrayList<>();
        agenceList.add(new Agence(selectedAgence));
        listCompte.setSource(compteFacade.findAll_Etat("Actif".toUpperCase(ROOT), agenceList));
        listCompte.getTarget().clear();
    }

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        return compteFacade.find(valueOf(value));
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {
            return String.valueOf(((Compte) object).getIdCompte());
        } else {
            return null;
        }
    }

    public String closePage() {
        return "moduleSecurite.xhtml?faces-redirect=true";
    }

    public GroupeUtilisateurFacadeLocal getGroupeUtilisateurFacade() {
        return groupeUtilisateurFacade;
    }

    public void setGroupeUtilisateurFacade(GroupeUtilisateurFacadeLocal groupeUtilisateurFacade) {
        this.groupeUtilisateurFacade = groupeUtilisateurFacade;
    }

    public List<GroupeUtilisateur> getListGroupeUtilisateur() {
        return listGroupeUtilisateur;
    }

    public void setListGroupeUtilisateur(List<GroupeUtilisateur> listGroupeUtilisateur) {
        this.listGroupeUtilisateur = listGroupeUtilisateur;
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

    public GroupeUtilisateur getSelectedGroupeUtilisateur() {
        return selectedGroupeUtilisateur;
    }

    public void setSelectedGroupeUtilisateur(GroupeUtilisateur selectedGroupeUtilisateur) {
        this.selectedGroupeUtilisateur = selectedGroupeUtilisateur;
    }

    public List<GroupeUtilisateur> getSelectedListGroupeUtilisateur() {
        return selectedListGroupeUtilisateur;
    }

    public void setSelectedListGroupeUtilisateur(List<GroupeUtilisateur> selectedListGroupeUtilisateur) {
        this.selectedListGroupeUtilisateur = selectedListGroupeUtilisateur;
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

    public CompteFacadeLocal getCompteFacade() {
        return compteFacade;
    }

    public void setCompteFacade(CompteFacadeLocal compteFacade) {
        this.compteFacade = compteFacade;
    }

    public DualListModel<Compte> getListCompte() {
        return listCompte;
    }

    public void setListCompte(DualListModel<Compte> listCompte) {
        this.listCompte = listCompte;
    }

    public String getOldValue_en() {
        return oldValue_en;
    }

    public void setOldValue_en(String oldValue_en) {
        this.oldValue_en = oldValue_en;
    }

    public List<Agence> getCurrentUserAgence() {
        return currentUserAgence;
    }

    public void setCurrentUserAgence(List<Agence> currentUserAgence) {
        this.currentUserAgence = currentUserAgence;
    }

    public LazyDataModel<GroupeUtilisateur> getLazylistGroupeUtilisateurModel() {
        return lazylistGroupeUtilisateurModel;
    }

    public void setLazylistGroupeUtilisateurModel(LazyDataModel<GroupeUtilisateur> lazylistGroupeUtilisateurModel) {
        this.lazylistGroupeUtilisateurModel = lazylistGroupeUtilisateurModel;
    }

    public List<GroupeUtilisateur> getLazyListGroupeUtilisateur() {
        return lazyListGroupeUtilisateur;
    }

    public void setLazyListGroupeUtilisateur(List<GroupeUtilisateur> lazyListGroupeUtilisateur) {
        this.lazyListGroupeUtilisateur = lazyListGroupeUtilisateur;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

}
