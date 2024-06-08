/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mb;

import usual.Routine;
import entities.Agence;
import entities.Compte;
import entities.Article;
import entities.Concurrent;
import entities.CritereFiltre;
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
import java.util.Set;
import java.util.TreeMap;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import static javax.faces.context.FacesContext.getCurrentInstance;
import javax.faces.event.ActionEvent;
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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.commandlink.CommandLink;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CaptureEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.CroppedImage;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.primefaces.model.UploadedFile;
import sessions.AgenceFacadeLocal;
import sessions.OperationsFacadeLocal;
import sessions.ArticleFacadeLocal;
import sessions.VeilleconcurrentFacadeLocal;
import sessions.ConcurrentFacadeLocal;

/**
 *
 * @author Lucien FOTSA
 */
public class ArticleMB implements Serializable {

    @Resource
    private UserTransaction ut;
    @EJB
    private OperationsFacadeLocal operationsFacade;
    //--------------------------------------------------------------------------
    @EJB
    private ArticleFacadeLocal articleFacade;
    private List<Article> listArticle = new ArrayList<>();
    private List<Article> selectedListArticle = new ArrayList<>();
    private Article selectedArticle = new Article();
    private String oldValue;
    private String oldValue_en;
    private String oldCodes = "";
    //--------------------------------------------------------------------------
    private Compte connectedUser;
    private Routine routine = new Routine();
    private String operation = "add";
    private boolean esc = true;//Etat spécifique d'un composant
    private boolean egc = true;//Etat générique d'un composant
    private String style = "font-size: 10pt; font-weight: bold; font-family: arial;";
    //--------------------------------------------
    @EJB
    private VeilleconcurrentFacadeLocal veilleconcurrentFacade;
    //--------------------------------------------
    @EJB
    private AgenceFacadeLocal agenceFacade;
    private List<Agence> listAgence = new ArrayList<>();
    private Integer selectedAgence;
    //--------------------------------------------
    @EJB
    private ConcurrentFacadeLocal concurrentFacade;
    private List<Concurrent> listConcurrent = new ArrayList<>();
    //--------------------------------------------
    private List<CritereFiltre> listCritereFiltre = new ArrayList<>();
    private CritereFiltre selectedCritere = new CritereFiltre("Tous", routine.localizeMessage("Tous"), "Tous", "Actif");
    private String xlsFile;
    private CroppedImage croppedImage = new CroppedImage();
    private String imageTemp;
    private String imageTempRelatif;
    private String captureMode;
    //---------------------------------------------------------
    private List<Agence> currentUserAgence = new ArrayList<>();
    private LazyDataModel<Article> lazylistArticleModel;
    private List<Article> lazyListArticle = new ArrayList<>();
    private boolean lock = false;
    private String fileName;

    /**
     * Creates a new instance of ArticleMB
     */
    public ArticleMB() {
    }

    @PostConstruct
    private void init() {
        connectedUser = (Compte) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUser");
        currentUserAgence = (List<Agence>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUserAgence");
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("filter")) {
            rechercheArticle();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("filter");
        }
        loadCritere();
        selectCritere();
//        rechercheArticle();
    }

    //--------------------------------------------------
    public void loadCritere() {
        listCritereFiltre.clear();
        listCritereFiltre.add(new CritereFiltre("Tous", routine.localizeMessage("Tous"), "Tous", "Actif"));
        listCritereFiltre.add(new CritereFiltre("code", routine.localizeMessage("Code"), "String", "Actif"));
        listCritereFiltre.add(new CritereFiltre("libelle", routine.localizeMessage("Libelle"), "String", "Actif"));
        listCritereFiltre.add(new CritereFiltre("coutachatttc", routine.localizeMessage("Achat"), "Numeric", "Actif"));
        listCritereFiltre.add(new CritereFiltre("prixunitttc", routine.localizeMessage("Vente"), "Numeric", "Actif"));
        listCritereFiltre.add(new CritereFiltre("dateEnregistre", routine.localizeMessage("DateEnregistre"), "Date", "Actif"));
        listCritereFiltre.add(new CritereFiltre("derniereModif", routine.localizeMessage("DerniereModif"), "Date", "Actif"));
    }

    public void selectCritere() {
        selectedCritere.setType(listCritereFiltre.get(listCritereFiltre.indexOf(selectedCritere)).getType());
        selectedCritere.setLibelle(listCritereFiltre.get(listCritereFiltre.indexOf(selectedCritere)).getLibelle());
    }

    public void rechercheArticle() {
        listArticle.clear();
        switch (selectedCritere.getAttribut()) {
            case "Tous":
                listArticle.addAll(articleFacade.findAll_Etat(selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!articleFacade.findAll_Etat("Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty()) : true);
                selectedCritere.setUsedValue(routine.localizeMessage("Tous"));
                break;
            case "code":
                listArticle.addAll(articleFacade.findByCode_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!articleFacade.findByCode_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", "Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty()) : true);
                selectedCritere.setUsedValue(selectedCritere.getValString());
                break;
            case "libelle":
                listArticle.addAll(FacesContext.getCurrentInstance().getViewRoot().getLocale().getLanguage().equals(new Locale("fr").getLanguage()) ? articleFacade.findByLibelle_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence) : articleFacade.findByLabel_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (FacesContext.getCurrentInstance().getViewRoot().getLocale().getLanguage().equals(new Locale("fr").getLanguage()) ? (!articleFacade.findByLibelle_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence).isEmpty()) : (!articleFacade.findByLabel_Etat("%" + selectedCritere.getValString().toUpperCase(ROOT) + "%", selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence).isEmpty())) : true);
                selectedCritere.setUsedValue(selectedCritere.getValString());
                break;
            case "coutachatttc":
                listArticle.addAll(articleFacade.findByCoutachatttc_Etat(selectedCritere.getValNum1(), selectedCritere.getValNum2(), selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!articleFacade.findByCoutachatttc_Etat(selectedCritere.getValNum1(), selectedCritere.getValNum2(), "Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty()) : true);
                selectedCritere.setUsedValue(selectedCritere.getValNum1() + " - " + selectedCritere.getValNum2());
                break;
            case "prixunitttc":
                listArticle.addAll(articleFacade.findByPrixunitttc_Etat(selectedCritere.getValNum1(), selectedCritere.getValNum2(), selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!articleFacade.findByPrixunitttc_Etat(selectedCritere.getValNum1(), selectedCritere.getValNum2(), "Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty()) : true);
                selectedCritere.setUsedValue(selectedCritere.getValNum1() + " - " + selectedCritere.getValNum2());
                break;
            case "dateEnregistre":
                routine.correctDateTime(selectedCritere.getValDate1(), new Date(selectedCritere.getValDate2().getTime() + 59999L), null, null);
                listArticle.addAll(articleFacade.findByDateEnregistre_Etat(selectedCritere.getValDate1(), new Date(selectedCritere.getValDate2().getTime() + 59999L), selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!articleFacade.findByDateEnregistre_Etat(selectedCritere.getValDate1(), new Date(selectedCritere.getValDate2().getTime() + 59999L), "Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty()) : true);
                selectedCritere.setUsedValue(new SimpleDateFormat("dd/MM/yyyy").format(selectedCritere.getValDate1()) + " - " + new SimpleDateFormat("dd/MM/yyyy").format(selectedCritere.getValDate2()));
                break;
            case "derniereModif":
                routine.correctDateTime(selectedCritere.getValDate1(), new Date(selectedCritere.getValDate2().getTime() + 59999L), null, null);
                listArticle.addAll(articleFacade.findByDerniereModif_Etat(selectedCritere.getValDate1(), new Date(selectedCritere.getValDate2().getTime() + 59999L), selectedCritere.getEtat().toUpperCase(ROOT), currentUserAgence));
                routine.setCor(selectedCritere.getEtat().equals("Actif") ? (!articleFacade.findByDerniereModif_Etat(selectedCritere.getValDate1(), new Date(selectedCritere.getValDate2().getTime() + 59999L), "Supprime".toUpperCase(ROOT), currentUserAgence).isEmpty()) : true);
                selectedCritere.setUsedValue(new SimpleDateFormat("dd/MM/yyyy").format(selectedCritere.getValDate1()) + " - " + new SimpleDateFormat("dd/MM/yyyy").format(selectedCritere.getValDate2()));
                break;
        }
        //**********************************************************************************************************

        lazylistArticleModel = new LazyDataModel<Article>() {
            @Override
            public Article getRowData(String rowKey) {
                for (Article ctn : lazyListArticle) {
                    if (ctn.getIdarticle().toString().equals(rowKey)) {
                        return ctn;
                    }
                }
                return null;
            }

            @Override
            public Object getRowKey(Article ctn) {
                return ctn.getIdarticle();
            }

            @Override
            public List<Article> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, Object> filters) {
                List<Article> data = new ArrayList<>();
                //filter
                for (Article ctn : listArticle) {
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
                        Collections.sort(data, new Article(sm.getSortField(), (SortOrder.ASCENDING.equals(sm.getSortOrder()) ? 1 : -1)));
                    }
                }

                //rowCount
                int dataSize = data.size();
                this.setRowCount(dataSize);

                //paginate
                lazyListArticle.clear();
                if (dataSize > pageSize) {
                    try {
                        lazyListArticle.addAll(data.subList(first, first + pageSize));
                        return lazyListArticle;
                    } catch (IndexOutOfBoundsException e) {
                        lazyListArticle.addAll(data.subList(first, first + (dataSize % pageSize)));
                        return lazyListArticle;
                    }
                } else {
                    lazyListArticle.addAll(data);
                    return lazyListArticle;
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
        if (!listArticle.isEmpty()) {
            routine.setImp(true);
        }
        if (selectedListArticle.size() > 1) {
            if (selectedCritere.getEtat().equals("Actif")) {
                routine.setSup(true);
            } else {
                routine.setRes(true);
                routine.setDet(true);
            }
        } else if (selectedListArticle.size() == 1) {
            selectedArticle = articleFacade.find(selectedListArticle.get(0).getIdarticle());
            oldValue = selectedArticle.getLibelle();
            oldValue_en = selectedArticle.getLabel();
            oldCodes = selectedArticle.getCode();
            //-------------------------------------------------------------------------------------------------------
            selectedAgence = selectedArticle.getIdagence() != null ? selectedArticle.getIdagence().getIdagence() : null;
            //------------------------------------------
            if (selectedCritere.getEtat().equals("Actif")) {
                routine.setMof(true);
                if (!selectedArticle.getLibelle().equals("Access") && !selectedArticle.getLibelle().equals("Access+") && !selectedArticle.getLibelle().equals("Essentiel+") && !selectedArticle.getLibelle().equals("Evasion") && !selectedArticle.getLibelle().equals("Evasion+") && !selectedArticle.getLibelle().equals("Iroko+") && !selectedArticle.getLibelle().equals("Prestige") && !selectedArticle.getLibelle().equals("Terminal Z4 [AV]") && !selectedArticle.getLibelle().equals("Terminal Z4 de migration") && !selectedArticle.getLibelle().equals("Terminal Z4 de migration S10/S11") && !selectedArticle.getLibelle().equals("Tout CANAL+")) {
                    routine.setSup(true);
                }
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
                oldCodes = "";
                selectedArticle = new Article();
                selectedArticle.setCode("");
                selectedArticle.setQuantitestock(0F);
                selectedArticle.setQuantiteavarie(0F);
                selectedArticle.setQuantitealerte(2F);
                selectedArticle.setQuantitemin(1F);
                selectedArticle.setCoutachatttc(0F);
                selectedArticle.setPrixunitttc(0F);
                selectedArticle.setEnvente(true);
                selectedArticle.setPeremption(false);
                selectedArticle.setDateperemption(new Date(currentTimeMillis() + 2592000000L));
                //String filePath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("resources/fichiers");
                //selectedArticle.setPhotoRelatif("resources/fichiers/vin.png");
                //selectedArticle.setPhoto(filePath + "\\vin.png");
                //-------------------------------------------------------------------------------------------------------
                selectedAgence = currentUserAgence.get(0).getIdagence();
                //-------------------------------------------------------------------------------------------------------
                tableReset();
                break;
            case "cop":
                selectedArticle.getListReferenceArticle().clear();
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
                rechercheArticle();
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
                        selectedArticle.setLibelle((selectedArticle.getLibelle() + "").trim());
                        selectedArticle.setLabel((selectedArticle.getLabel() + "").trim());
                        libelleOperation = routine.localizeMessage("Ajouter", new Locale("fr")) + " " + routine.localizeMessage("Unc", new Locale("fr")) + " " + routine.localizeMessage("Article", new Locale("fr"));
                        libelleOperation_en = routine.localizeMessage("Ajouter", new Locale("en")) + " " + routine.localizeMessage("Unc", new Locale("en")) + " " + routine.localizeMessage("Article", new Locale("en"));
                        if (articleFacade.findByLibelle_Etat(selectedArticle.getLibelle().toUpperCase(ROOT), "Actif".toUpperCase(ROOT), agenceList).isEmpty() || articleFacade.findByLabel_Etat(selectedArticle.getLabel().toUpperCase(ROOT), "Actif".toUpperCase(ROOT), agenceList).isEmpty()) {
                            selectedArticle.setEtat("Actif");
                            selectedArticle.setDateEnregistre(new Date(currentTimeMillis()));
                            selectedArticle.setDerniereModif(new Date(currentTimeMillis()));
                            //--------------------------------------------------------------
                            selectedArticle.setTva(0F);
                            selectedArticle.setPrixunit(selectedArticle.getPrixunitttc());
                            //-------------------------------------------------------------------------------------------------------
                            selectedArticle.setIdagence(selectedAgence != null ? new Agence(selectedAgence) : null);
                            //-------------------------------------------------------------------------------------------------------
                            selectedArticle.setIdarticle(articleFacade.nextId());
                            articleFacade.create(selectedArticle);
                            routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, langue.equals("fr") ? selectedArticle.getLibelle() : selectedArticle.getLabel(), connectedUser, operationsFacade);
                            routine.feedBack("Information", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Succes_operation"), null);
                        } else {
                            routine.feedBack("Avertissement", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Doublon"), null);
                        }
                        break;
                    case "cop":
                        selectedArticle.setLibelle((selectedArticle.getLibelle() + "").trim());
                        selectedArticle.setLabel((selectedArticle.getLabel() + "").trim());
                        libelleOperation = routine.localizeMessage("Copier", new Locale("fr")) + " " + routine.localizeMessage("Unc", new Locale("fr")) + " " + routine.localizeMessage("Article", new Locale("fr"));
                        libelleOperation_en = routine.localizeMessage("Copier", new Locale("en")) + " " + routine.localizeMessage("Unc", new Locale("en")) + " " + routine.localizeMessage("Article", new Locale("en"));
                        if (articleFacade.findByLibelle_Etat(selectedArticle.getLibelle().toUpperCase(ROOT), "Actif".toUpperCase(ROOT), agenceList).isEmpty() || articleFacade.findByLabel_Etat(selectedArticle.getLabel().toUpperCase(ROOT), "Actif".toUpperCase(ROOT), agenceList).isEmpty()) {
                            selectedArticle.setDateEnregistre(new Date(currentTimeMillis()));
                            selectedArticle.setDerniereModif(new Date(currentTimeMillis()));
                            selectedArticle.setPrixunit(selectedArticle.getPrixunitttc());
                            //-------------------------------------------------------------------------------------------------------
                            selectedArticle.setIdagence(selectedAgence != null ? new Agence(selectedAgence) : null);
                            //-------------------------------------------------------------------------------------------------------
                            selectedArticle.setIdarticle(articleFacade.nextId());
                            articleFacade.create(selectedArticle);
                            routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, langue.equals("fr") ? selectedArticle.getLibelle() : selectedArticle.getLabel(), connectedUser, operationsFacade);
                            routine.feedBack("Information", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Succes_operation"), null);
                        } else {
                            routine.feedBack("Avertissement", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Doublon"), null);
                        }
                        break;
                    case "mod":
                        selectedArticle.setLibelle((selectedArticle.getLibelle() + "").trim());
                        selectedArticle.setLabel((selectedArticle.getLabel() + "").trim());
                        libelleOperation = routine.localizeMessage("Modifier", new Locale("fr")) + " " + routine.localizeMessage("Unc", new Locale("fr")) + " " + routine.localizeMessage("Article", new Locale("fr"));
                        libelleOperation_en = routine.localizeMessage("Modifier", new Locale("en")) + " " + routine.localizeMessage("Unc", new Locale("en")) + " " + routine.localizeMessage("Article", new Locale("en"));
                        if (((!selectedArticle.getLibelle().toUpperCase(ROOT).equals(oldValue.toUpperCase(ROOT)) && articleFacade.findByLibelle_Etat(selectedArticle.getLibelle().toUpperCase(ROOT), "Actif".toUpperCase(ROOT), agenceList).isEmpty()) || (selectedArticle.getLibelle().toUpperCase(ROOT).equals(oldValue.toUpperCase(ROOT)))) || ((!selectedArticle.getLabel().toUpperCase(ROOT).equals(oldValue_en.toUpperCase(ROOT)) && articleFacade.findByLabel_Etat(selectedArticle.getLabel().toUpperCase(ROOT), "Actif".toUpperCase(ROOT), agenceList).isEmpty()) || (selectedArticle.getLabel().toUpperCase(ROOT).equals(oldValue_en.toUpperCase(ROOT))))) {
                            selectedArticle.setDerniereModif(new Date(currentTimeMillis()));
                            selectedArticle.setPrixunit(selectedArticle.getPrixunitttc());
                            //-------------------------------------------------------------------------------------------------------
                            selectedArticle.setIdagence(selectedAgence != null ? new Agence(selectedAgence) : null);
                            //-------------------------------------------------------------------------------------------------------
                            articleFacade.edit(selectedArticle);
                            routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, langue.equals("fr") ? selectedArticle.getLibelle() : selectedArticle.getLabel(), connectedUser, operationsFacade);
                            routine.feedBack("Information", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Succes_operation"), null);
                        } else {
                            routine.feedBack("Avertissement", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Doublon"), null);
                        }
                        break;
                    case "sup":
                        int not = 0;
                        libelleOperation = routine.localizeMessage("Supprimer", new Locale("fr")) + " " + routine.localizeMessage("Unc", new Locale("fr")) + " " + routine.localizeMessage("Article", new Locale("fr"));
                        libelleOperation_en = routine.localizeMessage("Supprimer", new Locale("en")) + " " + routine.localizeMessage("Unc", new Locale("en")) + " " + routine.localizeMessage("Article", new Locale("en"));
                        for (Article article_ : selectedListArticle) {
                            if (true) {
                                article_.setDerniereModif(new Date(currentTimeMillis()));
                                article_.setEtat("Supprime");
                                articleFacade.edit(article_);
                                routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, langue.equals("fr") ? article_.getLibelle() : article_.getLabel(), connectedUser, operationsFacade);
                            } else {
                                not++;
                            }
                        }
                        if (not > 0) {
                            routine.feedBack("Avertissement", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Suppression_impossible") + " " + not + " " + routine.localizeMessage("Reference_impossible") + " (" + routine.localizeMessage("Mouvement") + " / " + routine.localizeMessage("Inventaire") + " / " + routine.localizeMessage("Vente") + ")", null);
                        } else {
                            routine.feedBack("Information", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Succes_operation"), null);
                        }
                        break;
                    case "res":
                        libelleOperation = routine.localizeMessage("Restaurer", new Locale("fr")) + " " + routine.localizeMessage("Unc", new Locale("fr")) + " " + routine.localizeMessage("Article", new Locale("fr"));
                        libelleOperation_en = routine.localizeMessage("Restaurer", new Locale("en")) + " " + routine.localizeMessage("Unc", new Locale("en")) + " " + routine.localizeMessage("Article", new Locale("en"));
                        for (Article article_ : selectedListArticle) {
                            if (articleFacade.findByLibelle_Etat(article_.getLibelle().toUpperCase(ROOT), "Actif".toUpperCase(ROOT), agenceList).isEmpty() || articleFacade.findByLabel_Etat(article_.getLabel().toUpperCase(ROOT), "Actif".toUpperCase(ROOT), agenceList).isEmpty()) {
                                article_.setDerniereModif(new Date(currentTimeMillis()));
                                article_.setEtat("Actif");
                                articleFacade.edit(article_);
                                routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, langue.equals("fr") ? article_.getLibelle() : article_.getLabel(), connectedUser, operationsFacade);
                            } else {
                                libelleOperation = routine.localizeMessage("Detruire", new Locale("fr")) + " " + routine.localizeMessage("Unc", new Locale("fr")) + " " + routine.localizeMessage("Article", new Locale("fr")) + " [2]";
                                libelleOperation_en = routine.localizeMessage("Detruire", new Locale("en")) + " " + routine.localizeMessage("Unc", new Locale("en")) + " " + routine.localizeMessage("Article", new Locale("en")) + " [2]";
                                articleFacade.remove(article_);
                                routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, langue.equals("fr") ? article_.getLibelle() : article_.getLabel(), connectedUser, operationsFacade);
                            }
                        }
                        routine.feedBack("Information", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Succes_operation"), null);
                        break;
                    case "det":
                        libelleOperation = routine.localizeMessage("Detruire", new Locale("fr")) + " " + routine.localizeMessage("Unc", new Locale("fr")) + " " + routine.localizeMessage("Article", new Locale("fr"));
                        libelleOperation_en = routine.localizeMessage("Detruire", new Locale("en")) + " " + routine.localizeMessage("Unc", new Locale("en")) + " " + routine.localizeMessage("Article", new Locale("en"));
                        for (Article article_ : selectedListArticle) {
                            articleFacade.remove(article_);
                            routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, langue.equals("fr") ? article_.getLibelle() : article_.getLabel(), connectedUser, operationsFacade);
                        }
                        routine.feedBack("Information", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Succes_operation"), null);
                        break;
                }
                ut.commit();
            } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException e) {
                routine.feedBack("Erreur", langue.equals("fr") ? libelleOperation : libelleOperation_en, null, e);
                e.printStackTrace();
            }
            rechercheArticle();
        }
    }

    public String imprimer() {
        try {
            JRBeanCollectionDataSource beanCollectionDataSource;
            if (selectedListArticle.isEmpty()) {
                beanCollectionDataSource = new JRBeanCollectionDataSource(lazyListArticle);
            } else {
                beanCollectionDataSource = new JRBeanCollectionDataSource(selectedListArticle);
            }
            String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("resources/etats/article.jasper");
            Map parameters = new HashMap();
            selectedCritere.setAgence(currentUserAgence.get(0));
            parameters.put("REPORT_FILTER", selectedCritere);
            parameters.put("USER", connectedUser);
            parameters.put("REPORT_LOCALE", FacesContext.getCurrentInstance().getViewRoot().getLocale());
            JasperPrint jasperPrint = fillReport(reportPath, parameters, beanCollectionDataSource);
            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            httpServletResponse.addHeader("Content-disposition", "attachment; filename=ListeArticle.pdf");
            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
            exportReportToPdfStream(jasperPrint, servletOutputStream);
            servletOutputStream.close();
            FacesContext.getCurrentInstance().responseComplete();
            //----------------------------------------------
            String libelleOperation = routine.localizeMessage("Impression", new Locale("fr")) + " (" + routine.localizeMessage("Article", new Locale("fr")) + ")";
            String libelleOperation_en = routine.localizeMessage("Impression", new Locale("en")) + " (" + routine.localizeMessage("Article", new Locale("en")) + ")";
            routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, routine.localizeMessage("Article"), connectedUser, operationsFacade);
            //----------------------------------------------
        } catch (JRException | IOException e) {
            e.printStackTrace();
        }
        ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
        return ext.getRequestServletPath() + "?faces-redirect=true";
    }
    //--------------------------------------------------

    public void loadAgence() {
        listAgence.clear();
        listAgence.addAll(currentUserAgence);
    }

    public String article() {
        return "article.xhtml?faces-redirect=true";
    }

    public void tableReset() {
        selectedListArticle.clear();
        rowClick();
    }

    public String closePage() {
        return "moduleVeille.xhtml?faces-redirect=true";
    }

    public void prepareImport() {
        fileName = "";
    }

    public String exportXlsFile() {
        try {
            String langue = FacesContext.getCurrentInstance().getViewRoot().getLocale().getLanguage();
            //Blank workbook
            XSSFWorkbook workbook = new XSSFWorkbook();
            //Create cell style
            XSSFCellStyle cs = workbook.createCellStyle();
            XSSFFont f = workbook.createFont();
            f.setBold(true);
            f.setColor(IndexedColors.WHITE.getIndex());
            f.setFontHeightInPoints(Short.valueOf("13"));
            cs.setAlignment(HorizontalAlignment.CENTER);
            cs.setFont(f);
            cs.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cs.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            cs.setWrapText(true);
            //Create a blank sheet
            XSSFSheet sheet = workbook.createSheet(routine.localizeMessage("Article") + "s");
            //This data needs to be written (Object[])
            Map<String, Object[]> data = new TreeMap<>();
            data.put("0", new Object[]{routine.localizeMessage("Agence"), routine.localizeMessage("Code"), routine.localizeMessage("Num_lot"), (routine.localizeMessage("Libelle") + " (" + routine.localizeMessage("Francais") + ")"), (routine.localizeMessage("Libelle") + " (" + routine.localizeMessage("Anglais") + ")"), (routine.localizeMessage("Description") + " (" + routine.localizeMessage("Francais") + ")"), (routine.localizeMessage("Description") + " (" + routine.localizeMessage("Anglais") + ")"), routine.localizeMessage("Achat") + " (XAF)", routine.localizeMessage("Vente") + " (XAF)", routine.localizeMessage("Date_peremption") + " (dd/MM/yyyy)", routine.localizeMessage("Stock")});
            for (int i = 0; i < listArticle.size(); i++) {
                Article art = listArticle.get(i);
                data.put(i + 1 + "", new Object[]{(art.getIdagence() != null ? art.getIdagence().getRaisonsociale() : ""), art.getCode(), art.getNumlot(), art.getLibelle(), art.getLabel(), art.getDescription(), art.getDescriptionen(), art.getCoutachatttc(), art.getPrixunitttc(), (art.getDateperemption() != null ? new SimpleDateFormat("dd/MM/yyyy").format(art.getDateperemption()) : ""), art.getQuantitestock()});
            }
            //Iterate over data and write to sheet
            Set<String> keyset = data.keySet();
            int rownum = 0;
            for (String key : keyset) {
                //create a row of excelsheet
                Row row = sheet.createRow(rownum++);
                if (rownum == 1) {
                    row.setHeightInPoints(30);
                }
                //get object array of prerticuler key
                Object[] objArr = data.get(key);
                int cellnum = 0;
                for (Object obj : objArr) {
                    Cell cell = row.createCell(cellnum++);
                    if (rownum == 1) {
                        cell.setCellStyle(cs);
                    }
                    if (obj instanceof String) {
                        cell.setCellValue((String) obj);
                    } else if (obj instanceof Integer) {
                        cell.setCellValue((Integer) obj);
                    } else if (obj instanceof Long) {
                        cell.setCellValue((Long) obj);
                    } else if (obj instanceof Float) {
                        cell.setCellValue((Float) obj);
                    } else if (obj instanceof Date) {
                        cell.setCellValue((Date) obj);
                    }
                }
            }
            //Write the workbook in file system
            HttpServletResponse httpServletResponse = (HttpServletResponse) getCurrentInstance().getExternalContext().getResponse();
            httpServletResponse.addHeader("Content-disposition", "attachment; filename=ListeArticleExport.xlsx");
            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
            workbook.write(servletOutputStream);
            servletOutputStream.close();
            getCurrentInstance().responseComplete();
            String libelleOperation = routine.localizeMessage("Exporter", new Locale("fr")) + " (" + routine.localizeMessage("Article", new Locale("fr")) + ")";
            String libelleOperation_en = routine.localizeMessage("Exporter", new Locale("en")) + " (" + routine.localizeMessage("Article", new Locale("en")) + ")";
            ut.begin();
            routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, routine.localizeMessage("Article"), connectedUser, operationsFacade);
            ut.commit();
            rechercheArticle();
        } catch (IOException | NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException e) {
            e.printStackTrace();
        }
        ExternalContext ext = getCurrentInstance().getExternalContext();
        return ext.getRequestServletPath() + "?faces-redirect=true";
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            UploadedFile file = event.getFile();
            fileName = file.getFileName();
            String filePath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("resources/fichiers");
            String f_name = file.getFileName().substring(0, file.getFileName().indexOf(".")) + file.getFileName().substring(file.getFileName().indexOf("."));
            selectedArticle.setPhotoRelatif("resources/fichiers/" + f_name);
            selectedArticle.setPhoto(filePath + File.separator + f_name);
            byte[] bytes = file.getContents();
            try (FileOutputStream out = new FileOutputStream(selectedArticle.getPhoto(), true)) {
                out.write(bytes);
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            fileName = "";
        }
    }

    public void handleFileUploadExcel(FileUploadEvent event) {
        try {
            UploadedFile file = event.getFile();
            fileName = file.getFileName();
            String filePath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("resources/fichiers");
            xlsFile = filePath + File.separator + file.getFileName();
            byte[] bytes = file.getContents();
            try (FileOutputStream out = new FileOutputStream(xlsFile, true)) {
                out.write(bytes);
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            fileName = "";
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
            fileName = (new SimpleDateFormat("dd_MM_yy_HH_mm_ss").format(new Date(System.currentTimeMillis()))) + ".png";
            imageTempRelatif = "resources/fichiers/" + fileName;
            imageTemp = filePath + File.separator + fileName;
            imageOutput = new FileImageOutputStream(new File(imageTemp));
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
            RequestContext.getCurrentInstance().getScriptsToExecute().add("PF('widget_cameraphoto').hide()");
            RequestContext.getCurrentInstance().getScriptsToExecute().add("PF('widget_photocopper').show()");
        } catch (IOException e) {
            e.printStackTrace();
            fileName = "";
        }
    }

    public String crop() {
        String langue = FacesContext.getCurrentInstance().getViewRoot().getLocale().getLanguage();
        String filePath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("resources/fichiers");
        FileImageOutputStream imageOutput;
        try {
            if (captureMode.equals("picture")) {
                String code = (new SimpleDateFormat("SSS")).format(new Date(System.currentTimeMillis()));
                fileName = (langue.equals("fr") ? (selectedArticle.getLibelle() != null ? (selectedArticle.getLibelle().replaceAll(" ", "_") + "_" + articleFacade.nextId()) : articleFacade.nextId()) : (selectedArticle.getLabel() != null ? (selectedArticle.getLabel().replaceAll(" ", "_") + "_" + articleFacade.nextId()) : articleFacade.nextId())) + code + ".png";
                selectedArticle.setPhotoRelatif("resources/fichiers/" + fileName);
                selectedArticle.setPhoto(filePath + File.separator + fileName);
                imageOutput = new FileImageOutputStream(new File(selectedArticle.getPhoto()));
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
            fileName = "";
        }
        RequestContext.getCurrentInstance().getScriptsToExecute().add("PF('widget_photocopper').hide()");
        return null;
    }

    public void deleteTempImage() {
        File f = new File(imageTemp);
        f.delete();
    }

    public ArticleFacadeLocal getArticleFacade() {
        return articleFacade;
    }

    public void setArticleFacade(ArticleFacadeLocal articleFacade) {
        this.articleFacade = articleFacade;
    }

    public List<Article> getListArticle() {
        return listArticle;
    }

    public void setListArticle(List<Article> listArticle) {
        this.listArticle = listArticle;
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

    public Article getSelectedArticle() {
        return selectedArticle;
    }

    public void setSelectedArticle(Article selectedArticle) {
        this.selectedArticle = selectedArticle;
    }

    public List<Article> getSelectedListArticle() {
        return selectedListArticle;
    }

    public void setSelectedListArticle(List<Article> selectedListArticle) {
        this.selectedListArticle = selectedListArticle;
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

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getOldValue_en() {
        return oldValue_en;
    }

    public void setOldValue_en(String oldValue_en) {
        this.oldValue_en = oldValue_en;
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

    public String getXlsFile() {
        return xlsFile;
    }

    public void setXlsFile(String xlsFile) {
        this.xlsFile = xlsFile;
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

    public LazyDataModel<Article> getLazylistArticleModel() {
        return lazylistArticleModel;
    }

    public void setLazylistArticleModel(LazyDataModel<Article> lazylistArticleModel) {
        this.lazylistArticleModel = lazylistArticleModel;
    }

    public List<Article> getLazyListArticle() {
        return lazyListArticle;
    }

    public void setLazyListArticle(List<Article> lazyListArticle) {
        this.lazyListArticle = lazyListArticle;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public VeilleconcurrentFacadeLocal getVeilleconcurrentFacade() {
        return veilleconcurrentFacade;
    }

    public void setVeilleconcurrentFacade(VeilleconcurrentFacadeLocal veilleconcurrentFacade) {
        this.veilleconcurrentFacade = veilleconcurrentFacade;
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

}
