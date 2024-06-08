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
import entities.Veilleconcurrent;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import static java.util.Locale.ROOT;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.UserTransaction;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import sessions.AgenceFacadeLocal;
import sessions.ArticleFacadeLocal;
import sessions.OperationsFacadeLocal;
import sessions.ConcurrentFacadeLocal;
import sessions.NotificationsFacadeLocal;
import sessions.VeilleconcurrentFacadeLocal;

/**
 *
 * @author Lucien FOTSA
 */
public class AnalysePrixMB implements Serializable {

    @Resource
    private UserTransaction ut;
    private Compte connectedUser;
    private Routine routine = new Routine();
    private String operation = "add";
    private boolean esc = true;//Etat spécifique d'un composant
    private boolean egc = true;//Etat générique d'un composant
    private String style = "font-size: 10pt; font-weight: bold; font-family: arial;";
    //--------------------------------------------
    @EJB
    private OperationsFacadeLocal operationsFacade;
    //--------------------------------------------------------------------------
    @EJB
    private AgenceFacadeLocal agenceFacade;
    private List<Agence> listAgence = new ArrayList<>();
    private Integer selectedAgence;
    //--------------------------------------------
    @EJB
    private ArticleFacadeLocal articleFacade;
    //--------------------------------------------
    @EJB
    private VeilleconcurrentFacadeLocal veilleconcurrentFacade;
    private List<Veilleconcurrent> listVeilleconcurrent = new ArrayList<>();
    //--------------------------------------------
    @EJB
    private ConcurrentFacadeLocal concurrentFacade;
    //--------------------------------------------
    @EJB
    private NotificationsFacadeLocal notificationsFacade;
    //--------------------------------------------------------------------------
    private List<CritereFiltre> listCritereFiltre = new ArrayList<>();
    private CritereFiltre selectedCritere = new CritereFiltre("Periode", routine.localizeMessage("Periode"), "Date", "Actif");
    //--------------------------------------------------
    private int expiry = 0;
    private int article = 0;
    private int cptoir = 0;
    //---------------------------------------------------------
    private List<Agence> currentUserAgence = new ArrayList<>();
    private LazyDataModel<Veilleconcurrent> lazylistVeilleconcurrentModel;
    private List<Veilleconcurrent> lazyListVeilleconcurrent = new ArrayList<>();
    private boolean lock = false;

    /**
     * Creates a new instance of AnalyseStockMB
     */
    public AnalysePrixMB() {
    }

    @PostConstruct
    private void init() {
        connectedUser = (Compte) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUser");
        currentUserAgence = (List<Agence>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUserAgence");
        if (((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURL().toString().contains("analysePrix")) {
            loadCritere();
            selectCritere();
        }
        if (((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURL().toString().contains("moduleVeille")) {
            loadModuleStockDashboard();
        }
    }

    public void loadModuleStockDashboard() {
        try {
            expiry = 0;
            article = 0;
            cptoir = 0;
            expiry = notificationsFacade.findByVu_Etat(false, "Actif".toUpperCase(ROOT), currentUserAgence).size();
            article = articleFacade.findAll_Etat("Actif".toUpperCase(ROOT), currentUserAgence).size();
            cptoir = concurrentFacade.findAll_Etat("Actif".toUpperCase(ROOT), currentUserAgence).size();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void switchRubrique() {
        loadCritere();
        selectedCritere = new CritereFiltre("Tous", routine.localizeMessage("Tous"), "Tous", "Actif");
        selectCritere();
        rechercheVeilleconcurrent();
    }

    //--------------------------------------------------
    public void loadCritere() {
        listCritereFiltre.clear();
        listCritereFiltre.add(new CritereFiltre("Tous", routine.localizeMessage("Tous"), "Tous", "Actif"));
        listCritereFiltre.add(new CritereFiltre("Periode", routine.localizeMessage("Periode"), "Date", "Actif"));
    }

    public void selectCritere() {
        selectedCritere.setType(listCritereFiltre.get(listCritereFiltre.indexOf(selectedCritere)).getType());
        selectedCritere.setLibelle(listCritereFiltre.get(listCritereFiltre.indexOf(selectedCritere)).getLibelle());
    }

    public void rechercheVeilleconcurrent() {
        listVeilleconcurrent.clear();
        switch (selectedCritere.getAttribut()) {
            case "Tous":
                listVeilleconcurrent.addAll(veilleconcurrentFacade.findAll_Etat("Actif".toUpperCase(ROOT)));
                selectedCritere.setUsedValue(routine.localizeMessage("Tous"));
                break;
            case "Periode":
                selectedCritere.setUsedValue(new SimpleDateFormat("dd/MM/yyyy").format(selectedCritere.getValDate1()) + " - " + new SimpleDateFormat("dd/MM/yyyy").format(selectedCritere.getValDate2()));
                listVeilleconcurrent.addAll(veilleconcurrentFacade.findByDateEnregistre_Etat(selectedCritere.getValDate1(), selectedCritere.getValDate2(), "Actif".toUpperCase(ROOT)));
                selectedCritere.setUsedValue(routine.localizeMessage("Tous"));
                break;
        }
        routine.setImp(!listVeilleconcurrent.isEmpty());
        //**********************************************************************************************************

        lazylistVeilleconcurrentModel = new LazyDataModel<Veilleconcurrent>() {
            @Override
            public Veilleconcurrent getRowData(String rowKey) {
                for (Veilleconcurrent ctn : lazyListVeilleconcurrent) {
                    if (String.valueOf(ctn.getVeilleconcurrentPK().getIdarticle()).equals(rowKey)) {
                        return ctn;
                    }
                }
                return null;
            }

            @Override
            public Object getRowKey(Veilleconcurrent ctn) {
                return ctn.getVeilleconcurrentPK().getIdarticle();
            }

            @Override
            public List<Veilleconcurrent> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, Object> filters) {
                List<Veilleconcurrent> data = new ArrayList<>();
                //filter
                for (Veilleconcurrent ctn : listVeilleconcurrent) {
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
                        Collections.sort(data, new Veilleconcurrent(sm.getSortField(), (SortOrder.ASCENDING.equals(sm.getSortOrder()) ? 1 : -1)));
                    }
                }

                //rowCount
                int dataSize = data.size();
                this.setRowCount(dataSize);

                //paginate
                lazyListVeilleconcurrent.clear();
                if (dataSize > pageSize) {
                    try {
                        lazyListVeilleconcurrent.addAll(data.subList(first, first + pageSize));
                        return lazyListVeilleconcurrent;
                    } catch (IndexOutOfBoundsException e) {
                        lazyListVeilleconcurrent.addAll(data.subList(first, first + (dataSize % pageSize)));
                        return lazyListVeilleconcurrent;
                    }

                } else {
                    lazyListVeilleconcurrent.addAll(data);
                    return lazyListVeilleconcurrent;
                }
            }
        };
        //-----------------------------
    }

    public void loadAgence() {
        listAgence.clear();
        listAgence.addAll(currentUserAgence);
    }
    //--------------------------------------------------

    public String analysePrix() {
        return "analysePrix.xhtml?faces-redirect=true";
    }

    public String closePage() {
        return "moduleVeille.xhtml?faces-redirect=true";
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

    public Compte getConnectedUser() {
        return connectedUser;
    }

    public void setConnectedUser(Compte connectedUser) {
        this.connectedUser = connectedUser;
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

    public OperationsFacadeLocal getOperationsFacade() {
        return operationsFacade;
    }

    public void setOperationsFacade(OperationsFacadeLocal operationsFacade) {
        this.operationsFacade = operationsFacade;
    }

    public ArticleFacadeLocal getArticleFacade() {
        return articleFacade;
    }

    public void setArticleFacade(ArticleFacadeLocal articleFacade) {
        this.articleFacade = articleFacade;
    }

    public int getExpiry() {
        return expiry;
    }

    public int getArticle() {
        return article;
    }

    public void setArticle(int article) {
        this.article = article;
    }

    public List<Agence> getCurrentUserAgence() {
        return currentUserAgence;
    }

    public void setCurrentUserAgence(List<Agence> currentUserAgence) {
        this.currentUserAgence = currentUserAgence;
    }

    public LazyDataModel<Veilleconcurrent> getLazylistVeilleconcurrentModel() {
        return lazylistVeilleconcurrentModel;
    }

    public void setLazylistVeilleconcurrentModel(LazyDataModel<Veilleconcurrent> lazylistVeilleconcurrentModel) {
        this.lazylistVeilleconcurrentModel = lazylistVeilleconcurrentModel;
    }

    public List<Veilleconcurrent> getLazyListVeilleconcurrent() {
        return lazyListVeilleconcurrent;
    }

    public void setLazyListVeilleconcurrent(List<Veilleconcurrent> lazyListVeilleconcurrent) {
        this.lazyListVeilleconcurrent = lazyListVeilleconcurrent;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public int getCptoir() {
        return cptoir;
    }

    public void setCptoir(int cptoir) {
        this.cptoir = cptoir;
    }

    public List<Veilleconcurrent> getListVeilleconcurrent() {
        return listVeilleconcurrent;
    }

    public void setListVeilleconcurrent(List<Veilleconcurrent> listVeilleconcurrent) {
        this.listVeilleconcurrent = listVeilleconcurrent;
    }

}
