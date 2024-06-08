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
import entities.Multimedia;
import java.io.File;
import java.io.FileOutputStream;
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
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.primefaces.model.UploadedFile;
import sessions.AgenceFacadeLocal;
import sessions.MultimediaFacadeLocal;
import sessions.OperationsFacadeLocal;

/**
 *
 * @author Lucien FOTSA
 */
public class MultimediaMB implements Serializable {

    @Resource
    private UserTransaction ut;
    @EJB
    private OperationsFacadeLocal operationsFacade;
    //--------------------------------------------------------------------------
    @EJB
    private MultimediaFacadeLocal multimediaFacade;
    private List<Multimedia> listMultimedia = new ArrayList<>();
    private List<Multimedia> selectedListMultimedia = new ArrayList<>();
    private Multimedia selectedMultimedia = new Multimedia();
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
    //--------------------------------------------
    private List<CritereFiltre> listCritereFiltre = new ArrayList<>();
    private CritereFiltre selectedCritere = new CritereFiltre("Tous", routine.localizeMessage("Tous"), "Tous", "Actif");
    //---------------------------------------------------------
    private List<Agence> currentUserAgence = new ArrayList<>();
    //--------------------------------------------
    private LazyDataModel<Multimedia> lazylistMultimediaModel;
    private List<Multimedia> lazyListMultimedia = new ArrayList<>();
    private boolean lock = false;

    /**
     * Creates a new instance of MultimediaMB
     */
    public MultimediaMB() {
    }

    @PostConstruct
    private void init() {
        connectedUser = (Compte) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUser");
        currentUserAgence = (List<Agence>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUserAgence");
        loadCritere();
        selectCritere();
//        rechercheMultimedia();
    }

    //--------------------------------------------------
    public void loadCritere() {
        listCritereFiltre.clear();
        listCritereFiltre.add(new CritereFiltre("Tous", routine.localizeMessage("Tous"), "Tous", "Actif"));
        listCritereFiltre.add(new CritereFiltre("type", routine.localizeMessage("Type"), "Select1", "Actif"));
        listCritereFiltre.add(new CritereFiltre("categorie", routine.localizeMessage("Categorie"), "Select2", "Actif"));
        listCritereFiltre.add(new CritereFiltre("titre", routine.localizeMessage("Titre"), "String", "Actif"));
        listCritereFiltre.add(new CritereFiltre("taille", routine.localizeMessage("Taille"), "Numeric", "Actif"));
        listCritereFiltre.add(new CritereFiltre("actif", routine.localizeMessage("Actif"), "Boolean", "Boolean"));
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
        if (selectedCritere.getType().equals("String")) {
            selectedCritere.setValString("");
        }
    }

    public void rechercheMultimedia() {
        listMultimedia.clear();
        switch (selectedCritere.getAttribut()) {
            case "Tous":
                listMultimedia.addAll(multimediaFacade.findAll_Etat(selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!multimediaFacade.findAll_Etat("Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty()) : true);
                selectedCritere.setUsedValue(routine.localizeMessage("Tous"));
                break;
            case "type":
                listMultimedia.addAll(multimediaFacade.findByType_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!multimediaFacade.findByType_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", "Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty()) : true);
                selectedCritere.setUsedValue(selectedCritere.getValString());
                break;
            case "categorie":
                listMultimedia.addAll(multimediaFacade.findByCategorie_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!multimediaFacade.findByCategorie_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", "Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty()) : true);
                selectedCritere.setUsedValue(selectedCritere.getValString());
                break;
            case "titre":
                listMultimedia.addAll(multimediaFacade.findByTitre_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!multimediaFacade.findByTitre_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", "Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty()) : true);
                selectedCritere.setUsedValue(selectedCritere.getValString());
                break;
            case "taille":
                listMultimedia.addAll(multimediaFacade.findByTaille_Etat(selectedCritere.getValNum1(), selectedCritere.getValNum2(), selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!multimediaFacade.findByTaille_Etat(selectedCritere.getValNum1(), selectedCritere.getValNum2(), "Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty()) : true);
                selectedCritere.setUsedValue("[" + selectedCritere.getValNum1() + " Ko - " + selectedCritere.getValNum2() + " Ko]");
                break;
            case "actif":
                listMultimedia.addAll(multimediaFacade.findByActif_Etat(selectedCritere.getValBool(), selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!multimediaFacade.findByActif_Etat(selectedCritere.getValBool(), "Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty()) : true);
                selectedCritere.setUsedValue(selectedCritere.getValBool().toString());
                break;
            case "idagence":
                List<Agence> agenceList = new ArrayList();
                agenceList.add(new Agence(selectedCritere.getValSelect()));
                listMultimedia.addAll(multimediaFacade.findAll_Etat(selectedCritere.getEtat().toUpperCase(ROOT), agenceList));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!multimediaFacade.findAll_Etat("Supprime".toUpperCase(ROOT), agenceList).isEmpty()) : true);
                selectedCritere.setUsedValue(agenceFacade.find(selectedCritere.getValSelect()).getRaisonsociale());
                break;
            case "dateEnregistre":
                routine.correctDateTime(selectedCritere.getValDate1(), new Date(selectedCritere.getValDate2().getTime() + 59999L), null, null);
                listMultimedia.addAll(multimediaFacade.findByDateEnregistre_Etat(selectedCritere.getValDate1(), new Date(selectedCritere.getValDate2().getTime() + 59999L), selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!multimediaFacade.findByDateEnregistre_Etat(selectedCritere.getValDate1(), new Date(selectedCritere.getValDate2().getTime() + 59999L), "Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty()) : true);
                selectedCritere.setUsedValue(new SimpleDateFormat("dd/MM/yyyy").format(selectedCritere.getValDate1()) + " - " + new SimpleDateFormat("dd/MM/yyyy").format(selectedCritere.getValDate2()));
                break;
            case "derniereModif":
                routine.correctDateTime(selectedCritere.getValDate1(), new Date(selectedCritere.getValDate2().getTime() + 59999L), null, null);
                listMultimedia.addAll(multimediaFacade.findByDerniereModif_Etat(selectedCritere.getValDate1(), new Date(selectedCritere.getValDate2().getTime() + 59999L), selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!multimediaFacade.findByDerniereModif_Etat(selectedCritere.getValDate1(), new Date(selectedCritere.getValDate2().getTime() + 59999L), "Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty()) : true);
                selectedCritere.setUsedValue(new SimpleDateFormat("dd/MM/yyyy").format(selectedCritere.getValDate1()) + " - " + new SimpleDateFormat("dd/MM/yyyy").format(selectedCritere.getValDate2()));
                break;
        }
        //**********************************************************************************************************

        lazylistMultimediaModel = new LazyDataModel<Multimedia>() {
            @Override
            public Multimedia getRowData(String rowKey) {
                for (Multimedia ctn : lazyListMultimedia) {
                    if (ctn.getIdmultimedia().toString().equals(rowKey)) {
                        return ctn;
                    }
                }
                return null;
            }

            @Override
            public Object getRowKey(Multimedia ctn) {
                return ctn.getIdmultimedia();
            }

            @Override
            public List<Multimedia> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, Object> filters) {
                List<Multimedia> data = new ArrayList<>();
                //filter
                for (Multimedia ctn : listMultimedia) {
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
                        Collections.sort(data, new Multimedia(sm.getSortField(), (SortOrder.ASCENDING.equals(sm.getSortOrder()) ? 1 : -1)));
                    }
                }

                //rowCount
                int dataSize = data.size();
                this.setRowCount(dataSize);

                //paginate
                lazyListMultimedia.clear();
                if (dataSize > pageSize) {
                    try {
                        lazyListMultimedia.addAll(data.subList(first, first + pageSize));
                        return data.subList(first, first + pageSize);
                    } catch (IndexOutOfBoundsException e) {
                        lazyListMultimedia.addAll(data.subList(first, first + (dataSize % pageSize)));
                        return data.subList(first, first + (dataSize % pageSize));
                    }

                } else {
                    lazyListMultimedia.addAll(data);
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
        if (!listMultimedia.isEmpty()) {
            routine.setImp(true);
        }
        if (selectedListMultimedia.size() > 1) {
            if (selectedCritere.getEtat().equals("Actif")) {
                routine.setSup(true);
            } else {
                routine.setRes(true);
                routine.setDet(true);
            }
        } else if (selectedListMultimedia.size() == 1) {
            selectedMultimedia = multimediaFacade.find(selectedListMultimedia.get(0).getIdmultimedia());
            oldValue = selectedMultimedia.getTitre();
            //-------------------------------------------------------------------------------------------------------
            selectedAgence = selectedMultimedia.getIdagence() != null ? selectedMultimedia.getIdagence().getIdagence() : null;
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
                selectedMultimedia = new Multimedia();
                //-------------------------------------------------------------------------------------------------------
                String filePath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("resources/fichiers");
                selectedMultimedia.setCheminrelatif("resources/fichiers/default.png");
                selectedMultimedia.setChemin(filePath + "\\default.png");
                selectedMultimedia.setActif(true);
                selectedAgence = currentUserAgence.get(0).getIdagence();
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
                rechercheMultimedia();
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
                switch (operation) {
                    case "add":
                        libelleOperation = routine.localizeMessage("Ajouter", new Locale("fr")) + " " + routine.localizeMessage("Un", new Locale("fr")) + " " + routine.localizeMessage("Multimedia", new Locale("fr"));
                        libelleOperation_en = routine.localizeMessage("Ajouter", new Locale("en")) + " " + routine.localizeMessage("Un", new Locale("en")) + " " + routine.localizeMessage("Multimedia", new Locale("en"));
                        selectedMultimedia.setTitre((selectedMultimedia.getTitre() + "").trim());
                        selectedMultimedia.setTitreen((selectedMultimedia.getTitreen() + "").trim());
                        selectedMultimedia.setEtat("Actif");
                        selectedMultimedia.setDateEnregistre(new Date(currentTimeMillis()));
                        selectedMultimedia.setDerniereModif(new Date(currentTimeMillis()));
                        //-------------------------------------------------------------------------------------------------------
                        selectedMultimedia.setIdagence(selectedAgence != null ? new Agence(selectedAgence) : null);
                        //-------------------------------------------------------------------------------------------------------
                        selectedMultimedia.setIdmultimedia(multimediaFacade.nextId());
                        multimediaFacade.create(selectedMultimedia);
                        routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, selectedMultimedia.getTitre(), connectedUser, operationsFacade);
                        routine.feedBack("Information", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Succes_operation"), null);
                        break;
                    case "cop":
                        libelleOperation = routine.localizeMessage("Copier", new Locale("fr")) + " " + routine.localizeMessage("Un", new Locale("fr")) + " " + routine.localizeMessage("Multimedia", new Locale("fr"));
                        libelleOperation_en = routine.localizeMessage("Copier", new Locale("en")) + " " + routine.localizeMessage("Un", new Locale("en")) + " " + routine.localizeMessage("Multimedia", new Locale("en"));
                        selectedMultimedia.setTitre((selectedMultimedia.getTitre() + "").trim());
                        selectedMultimedia.setTitreen((selectedMultimedia.getTitreen() + "").trim());
                        selectedMultimedia.setDateEnregistre(new Date(currentTimeMillis()));
                        selectedMultimedia.setDerniereModif(new Date(currentTimeMillis()));
                        //-------------------------------------------------------------------------------------------------------
                        selectedMultimedia.setIdagence(selectedAgence != null ? new Agence(selectedAgence) : null);
                        //-------------------------------------------------------------------------------------------------------
                        selectedMultimedia.setIdmultimedia(multimediaFacade.nextId());
                        multimediaFacade.create(selectedMultimedia);
                        routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, selectedMultimedia.getTitre(), connectedUser, operationsFacade);
                        routine.feedBack("Information", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Succes_operation"), null);
                        break;
                    case "mod":
                        libelleOperation = routine.localizeMessage("Modifier", new Locale("fr")) + " " + routine.localizeMessage("Un", new Locale("fr")) + " " + routine.localizeMessage("Multimedia", new Locale("fr"));
                        libelleOperation_en = routine.localizeMessage("Modifier", new Locale("en")) + " " + routine.localizeMessage("Un", new Locale("en")) + " " + routine.localizeMessage("Multimedia", new Locale("en"));
                        selectedMultimedia.setTitre((selectedMultimedia.getTitre() + "").trim());
                        selectedMultimedia.setTitreen((selectedMultimedia.getTitreen() + "").trim());
                        selectedMultimedia.setDerniereModif(new Date(currentTimeMillis()));
                        //-------------------------------------------------------------------------------------------------------
                        selectedMultimedia.setIdagence(selectedAgence != null ? new Agence(selectedAgence) : null);
                        //-------------------------------------------------------------------------------------------------------
                        multimediaFacade.edit(selectedMultimedia);
                        routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, selectedMultimedia.getTitre(), connectedUser, operationsFacade);
                        routine.feedBack("Information", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Succes_operation"), null);
                        break;
                    case "sup":
                        libelleOperation = routine.localizeMessage("Supprimer", new Locale("fr")) + " " + routine.localizeMessage("Un", new Locale("fr")) + " " + routine.localizeMessage("Multimedia", new Locale("fr"));
                        libelleOperation_en = routine.localizeMessage("Supprimer", new Locale("en")) + " " + routine.localizeMessage("Un", new Locale("en")) + " " + routine.localizeMessage("Multimedia", new Locale("en"));
                        for (Multimedia multimedia_ : selectedListMultimedia) {
                            multimedia_.setDerniereModif(new Date(currentTimeMillis()));
                            multimedia_.setEtat("Supprime");
                            multimediaFacade.edit(multimedia_);
                            routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, multimedia_.getTitre(), connectedUser, operationsFacade);
                        }
                        routine.feedBack("Information", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Succes_operation"), null);
                        break;
                    case "res":
                        libelleOperation = routine.localizeMessage("Restaurer", new Locale("fr")) + " " + routine.localizeMessage("Un", new Locale("fr")) + " " + routine.localizeMessage("Multimedia", new Locale("fr"));
                        libelleOperation_en = routine.localizeMessage("Restaurer", new Locale("en")) + " " + routine.localizeMessage("Un", new Locale("en")) + " " + routine.localizeMessage("Multimedia", new Locale("en"));
                        for (Multimedia multimedia_ : selectedListMultimedia) {
                            multimedia_.setDerniereModif(new Date(currentTimeMillis()));
                            multimedia_.setEtat("Actif");
                            multimediaFacade.edit(multimedia_);
                            routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, multimedia_.getTitre(), connectedUser, operationsFacade);
                        }
                        routine.feedBack("Information", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Succes_operation"), null);
                        break;
                    case "det":
                        libelleOperation = routine.localizeMessage("Detruire", new Locale("fr")) + " " + routine.localizeMessage("Un", new Locale("fr")) + " " + routine.localizeMessage("Multimedia", new Locale("fr"));
                        libelleOperation_en = routine.localizeMessage("Detruire", new Locale("en")) + " " + routine.localizeMessage("Un", new Locale("en")) + " " + routine.localizeMessage("Multimedia", new Locale("en"));
                        for (Multimedia multimedia_ : selectedListMultimedia) {
                            File directoryFile = new File(multimedia_.getChemin());
                            directoryFile.delete();
                            //----------------------------------------------------------
                            multimediaFacade.remove(multimedia_);
                            routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, multimedia_.getTitre(), connectedUser, operationsFacade);
                        }
                        routine.feedBack("Information", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Succes_operation"), null);
                        break;
                }
                ut.commit();
            } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException e) {
                routine.feedBack("Erreur", langue.equals("fr") ? libelleOperation : libelleOperation_en, null, e);
                e.printStackTrace();
            }
            rechercheMultimedia();
        }
    }

    public String imprimer() {
        try {
            JRBeanCollectionDataSource beanCollectionDataSource;
            if (selectedListMultimedia.isEmpty()) {
                beanCollectionDataSource = new JRBeanCollectionDataSource(lazyListMultimedia);
            } else {
                beanCollectionDataSource = new JRBeanCollectionDataSource(selectedListMultimedia);
            }
            String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("resources/etats/multimedia.jasper");
            Map parameters = new HashMap();
            selectedCritere.setAgence(currentUserAgence.get(0));
            parameters.put("REPORT_FILTER", selectedCritere);
            parameters.put("USER", connectedUser);
            parameters.put("REPORT_LOCALE", FacesContext.getCurrentInstance().getViewRoot().getLocale());
            JasperPrint jasperPrint = fillReport(reportPath, parameters, beanCollectionDataSource);
            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            httpServletResponse.addHeader("Content-disposition", "attachment; filename=ListeMultimedia.pdf");
            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
            exportReportToPdfStream(jasperPrint, servletOutputStream);
            servletOutputStream.close();
            FacesContext.getCurrentInstance().responseComplete();
            //----------------------------------------------
            String libelleOperation = routine.localizeMessage("Impression", new Locale("fr")) + " (" + routine.localizeMessage("Multimedia", new Locale("fr")) + ")";
            String libelleOperation_en = routine.localizeMessage("Impression", new Locale("en")) + " (" + routine.localizeMessage("Multimedia", new Locale("en")) + ")";
            routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, routine.localizeMessage("Multimedia"), connectedUser, operationsFacade);
            //----------------------------------------------
        } catch (JRException | IOException e) {
            e.printStackTrace();
        }
        ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
        return ext.getRequestServletPath() + "?faces-redirect=true";
    }

    public String multimedia() {
        return "multimedia.xhtml?faces-redirect=true";
    }

    public void tableReset() {
        selectedListMultimedia.clear();
        rowClick();
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            UploadedFile file = event.getFile();
            String filePath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("resources/fichiers");
            selectedMultimedia.setCheminrelatif("resources/fichiers/" + file.getFileName());
            selectedMultimedia.setChemin(filePath + File.separator + file.getFileName());
            selectedMultimedia.setTaille((float) file.getSize());
            byte[] bytes = file.getContents();
            try (FileOutputStream out = new FileOutputStream(selectedMultimedia.getChemin(), true)) {
                out.write(bytes);
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadAgence() {
        listAgence.clear();
        listAgence.addAll(currentUserAgence);
    }

    public String closePage() {
        return "moduleSecurite.xhtml?faces-redirect=true";
    }

    public MultimediaFacadeLocal getMultimediaFacade() {
        return multimediaFacade;
    }

    public void setMultimediaFacade(MultimediaFacadeLocal multimediaFacade) {
        this.multimediaFacade = multimediaFacade;
    }

    public List<Multimedia> getListMultimedia() {
        return listMultimedia;
    }

    public void setListMultimedia(List<Multimedia> listMultimedia) {
        this.listMultimedia = listMultimedia;
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

    public Multimedia getSelectedMultimedia() {
        return selectedMultimedia;
    }

    public void setSelectedMultimedia(Multimedia selectedMultimedia) {
        this.selectedMultimedia = selectedMultimedia;
    }

    public List<Multimedia> getSelectedListMultimedia() {
        return selectedListMultimedia;
    }

    public void setSelectedListMultimedia(List<Multimedia> selectedListMultimedia) {
        this.selectedListMultimedia = selectedListMultimedia;
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

    public Integer getSelectedAgence() {
        return selectedAgence;
    }

    public void setSelectedAgence(Integer selectedAgence) {
        this.selectedAgence = selectedAgence;
    }

    public List<Agence> getCurrentUserAgence() {
        return currentUserAgence;
    }

    public void setCurrentUserAgence(List<Agence> currentUserAgence) {
        this.currentUserAgence = currentUserAgence;
    }

    public LazyDataModel<Multimedia> getLazylistMultimediaModel() {
        return lazylistMultimediaModel;
    }

    public void setLazylistMultimediaModel(LazyDataModel<Multimedia> lazylistMultimediaModel) {
        this.lazylistMultimediaModel = lazylistMultimediaModel;
    }

    public List<Multimedia> getLazyListMultimedia() {
        return lazyListMultimedia;
    }

    public void setLazyListMultimedia(List<Multimedia> lazyListMultimedia) {
        this.lazyListMultimedia = lazyListMultimedia;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

}
