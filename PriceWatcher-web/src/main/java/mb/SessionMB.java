/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mb;

import usual.Routine;
import entities.Agence;
import entities.Article;
import entities.Compte;
import entities.GroupeUtilisateur;
import entities.Menu;
import entities.Multimedia;
import entities.Reglage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import static java.lang.Integer.parseInt;
import static java.lang.System.currentTimeMillis;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import static java.util.Arrays.copyOf;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import static java.util.Locale.ROOT;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.imageio.stream.FileImageOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;
import org.primefaces.component.commandlink.CommandLink;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CaptureEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.NodeUnselectEvent;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.CroppedImage;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.model.menu.MenuItem;
import org.primefaces.model.tagcloud.DefaultTagCloudModel;
import org.primefaces.model.tagcloud.TagCloudModel;
import sessions.AgenceFacadeLocal;
import sessions.ArticleFacadeLocal;
import sessions.CompteFacadeLocal;
import sessions.GroupeUtilisateurFacadeLocal;
import sessions.MenuFacadeLocal;
import sessions.MultimediaFacadeLocal;
import sessions.OperationsFacadeLocal;
import sessions.ReglageFacadeLocal;
import usual.MailTools;

/**
 *
 * @author Lucien FOTSA
 */
public class SessionMB implements Serializable {

    private static int nbreCnx = 0;
    private static int nbreVst = 0;
    public static Boolean pullInfo = true;
    private boolean esc = true;//Etat spécifique d'un composant
    private boolean egc = true;//Etat générique d'un composant
    private String style = "font-size: 10pt; font-weight: bold; font-family: arial;";

    @Resource
    private UserTransaction ut;
    @EJB
    private OperationsFacadeLocal operationsFacade;
    @EJB
    private ReglageFacadeLocal reglageFacade;
    private Reglage reglage = new Reglage();
    @EJB
    private MenuFacadeLocal menuFacade;
    private List<Menu> listModuleUser = new ArrayList<>();
    @EJB
    private AgenceFacadeLocal agenceFacade;
    private String selectedAgences;
    private Agence visitedAgence = new Agence();
    private Agence currentAgence = new Agence();
    @EJB
    private MultimediaFacadeLocal multimediaFacade;
    private List<Multimedia> listGalery = new ArrayList<>();
    //--------------------------------------------------------------------------
    private Routine routine = new Routine();
    private String locale = "en";
    @EJB
    private GroupeUtilisateurFacadeLocal groupeUtilisateurFacade;
    @EJB
    private CompteFacadeLocal compteFacade;
    private Compte currentUser = new Compte();
    //*****Essai********
    private Boolean essai = false;
    //*****Server time********
    private Date dateEssai;
    private Date dateServer = new Date(System.currentTimeMillis());
    //*****Connexion********
    private Boolean openConnection = true;
    private Integer nbreEchec = 0;
    private String droits = "";
    //--------------------------------------------------------------------
    private TagCloudModel tc = new DefaultTagCloudModel();
    private MapModel sigModel = new DefaultMapModel();
    private Marker selectedMarker;
    private Integer zoom;
    private String center;
    //*******************************************************************
    //-------------module de sécurité
    private Boolean md_sec = false;
    //-------------module veille
    private Boolean md_vei = false;
    //********************************************************************
    //-------------Paramétrage du module sécurité
    private Boolean params = false;
    //-------------Paramétrage du module veille
    private Boolean paramp = false;
    //-------------Traitement du module sécurité
    private Boolean traims = false;
    //-------------Traitement du module veille
    private Boolean traimp = false;
    //-------------Analyse du module sécurité
    private Boolean analys = false;
    //-------------Analyse du module veille
    private Boolean analyp = false;
    //********************************************************************
    //-------------Agence
    private Boolean agenc_ = false;
    private Boolean ag_cop = false;
    private Boolean ag_mod = false;
    private Boolean ag_sup = false;
    private Boolean ag_con = false;
    private Boolean ag_add = false;
    private Boolean ag_imp = false;
    private Boolean ag_res = false;
    private Boolean ag_det = false;
    //-------------Comptes
    private Boolean compte = false;
    private Boolean ct_cop = false;
    private Boolean ct_mod = false;
    private Boolean ct_sup = false;
    private Boolean ct_con = false;
    private Boolean ct_add = false;
    private Boolean ct_imp = false;
    private Boolean ct_res = false;
    private Boolean ct_det = false;
    //-------------Groupe d'utilisateur
    private Boolean grp_te = false;
    private Boolean gr_cop = false;
    private Boolean gr_mod = false;
    private Boolean gr_sup = false;
    private Boolean gr_con = false;
    private Boolean gr_add = false;
    private Boolean gr_imp = false;
    private Boolean gr_res = false;
    private Boolean gr_det = false;
    //-------------Attribution des privilèges
    private Boolean pr_cpt = false;
    private Boolean pr_grp = false;
    //-------------Mouchard
    private Boolean muchrd = false;
    //-------------Multimedia
    private Boolean multim = false;
    private Boolean mm_cop = false;
    private Boolean mm_mod = false;
    private Boolean mm_sup = false;
    private Boolean mm_con = false;
    private Boolean mm_add = false;
    private Boolean mm_imp = false;
    private Boolean mm_res = false;
    private Boolean mm_det = false;
    //-------------Article
    private Boolean articl = false;
    private Boolean at_cop = false;
    private Boolean at_mod = false;
    private Boolean at_sup = false;
    private Boolean at_con = false;
    private Boolean at_add = false;
    private Boolean at_imp = false;
    private Boolean at_res = false;
    private Boolean at_det = false;
    //-------------Analyse de stock
    private Boolean an_stk = false;
    //-------------Comptoir
    private Boolean cptoir = false;
    private Boolean kt_cop = false;
    private Boolean kt_mod = false;
    private Boolean kt_sup = false;
    private Boolean kt_con = false;
    private Boolean kt_add = false;
    private Boolean kt_imp = false;
    private Boolean kt_res = false;
    private Boolean kt_det = false;
    //-------------Notifications
    private Boolean no_sms = false;
    private Boolean ns_cop = false;
    private Boolean ns_mod = false;
    private Boolean ns_sup = false;
    private Boolean ns_con = false;
    private Boolean ns_add = false;
    private Boolean ns_imp = false;
    private Boolean ns_res = false;
    private Boolean ns_det = false;
    //-------------------------------------------------
    private Boolean pause = false;
    private String oldValue;
    private int nbreRdt = 0;
    private String index = "index.xhtml?faces-redirect=true";
    private String redirection = "/PriceWatcher-web/index.xhtml?faces-redirect=true";
    private int idleTime = 900000;
    private CroppedImage croppedImage = new CroppedImage();
    private String imageTemp;
    private String imageTempRelatif;
    private String captureMode;
    private Date today = new Date(currentTimeMillis());
    //--------------------------------------------------
    private boolean changeMe = false;
    private String oldPass;
    private String oldPassTyped;
    //--------------------------------------------------
    private TreeNode agenceTree = new DefaultTreeNode("Entreprises", null);
    private TreeNode[] selectedAgenceTree = new TreeNode[0];
    private List<Agence> selectedAgenceList = new ArrayList<>();
    private boolean hideAgencies = false;
    //--------------------------------------------------
    private boolean lock = false;
    //--------------------------------------------------
    private MailTools mailTools = new MailTools();
    //--------------------------------------------------
    @EJB
    private ArticleFacadeLocal bouquetFacade;
    private int activeIndex = 0;
    //--------------------------------------------------
    private Boolean resetCgaPassword;
    private Boolean selectAllAge = false;
    private Boolean shwoPass = false;
    //--------------------------------------------------
    @EJB
    private ArticleFacadeLocal articleFacade;
    //--------------------------------------------------
    private static FacesContext externalContext;
    private Article selectedArticle;
    //-----------------------------------------
    public static final ThreadLocal<WebRobot> webRobot = new ThreadLocal<WebRobot>() {

        @Override
        protected WebRobot initialValue() {
            return new WebRobot();
        }
    };

    public static WebRobot getWebRobot() {
        return webRobot.get();
    }
    //--------------
    private String num_dist = "";
    private String login_dist = "";
    private Date debut_rep = new Date(currentTimeMillis());
    private Date fin_rep = new Date(currentTimeMillis());
    //-----------
    private DefaultTreeNode nodeAll;

    /**
     * Creates a new instance of SessionMB
     */
    public SessionMB() {
        if (FacesContext.getCurrentInstance() != null) {
            externalContext = FacesContext.getCurrentInstance();
        }
    }

    @PostConstruct
    public void init() {
        try {
            if (visitedAgence.getIdagence() != null) {
                if (!reglageFacade.findByIdagence(visitedAgence.getIdagence()).isEmpty()) {
                    reglage = reglageFacade.findByIdagence(visitedAgence.getIdagence()).get(0);
                    reglage.setVisite(String.valueOf(Integer.valueOf(reglage.getVisite()) + 1));
                    nbreVst = Integer.valueOf(reglage.getVisite());
                    reglageFacade.edit(reglage);
                    //if (reglage.getIdagence().getDomaine() != null) {
                    //if (reglage.getIdagence().getDomaine().contains("mit2online.com")) {
                    //pullInfo = reglage.getBoole();
                    //} else {
                    //pullInfo = true;
                    //}
                    //}
                }
                loadGadgets(visitedAgence.getIdagence());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //To do code
    }

    public void clickAgency() {
        try {
            if (hideAgencies) {
                hideAgencies = false;
                ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
                ((FacesContext.getCurrentInstance()).getExternalContext()).redirect(ext.getRequestServletPath().substring(1) + "?faces-redirect=true");
            } else {
                hideAgencies = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static TreeNode[] addElement(TreeNode[] a, TreeNode e) {
        a = copyOf(a, a.length + 1);
        a[a.length - 1] = e;
        return a;
    }

    public void setNodeSelectAll(DefaultTreeNode nodeAll) {
        TreeNode selectedNode = (TreeNode) nodeAll;
        selectedNode.setExpanded(true);
        if (((Agence) selectedNode.getData()).getRaisonsociale().equals("Toutes") || ((Agence) selectedNode.getData()).getRaisonsociale().equals("All")) {
            selectedAgenceTree = new TreeNode[0];
            selectAllAge = true;
            for (TreeNode node : agenceTree.getChildren()) {
                if (!node.getData().equals("Entreprises")) {
                    node.setSelected(true);
                    node.setExpanded(true);
                    selectedAgenceTree = addElement(selectedAgenceTree, node);
                    for (TreeNode subNode : node.getChildren()) {
                        if (!subNode.getData().equals("Entreprises")) {
                            subNode.setSelected(true);
                            subNode.setExpanded(true);
                            selectedAgenceTree = addElement(selectedAgenceTree, subNode);

                        }
                    }
                }
            }
        } else {
            selectAllAge = false;
            for (TreeNode node = selectedNode.getParent(); node != null; node = node.getParent()) {
                if (!node.getData().equals("Entreprises")) {
                    //node.setSelected(true);
                    node.setExpanded(true);
                    //selectedAgenceTree = addElement(selectedAgenceTree, node);
                }
            }
        }
        loadSelectedAgence();
    }

    public void onNodeSelect(NodeSelectEvent event) {
        TreeNode selectedNode = event.getTreeNode();
        selectedNode.setExpanded(true);
        if (((Agence) selectedNode.getData()).getRaisonsociale().equals("Toutes") || ((Agence) selectedNode.getData()).getRaisonsociale().equals("All")) {
            selectedAgenceTree = new TreeNode[0];
            selectAllAge = true;
            for (TreeNode node : agenceTree.getChildren()) {
                if (!node.getData().equals("Entreprises")) {
                    node.setSelected(true);
                    node.setExpanded(true);
                    selectedAgenceTree = addElement(selectedAgenceTree, node);
                    for (TreeNode subNode : node.getChildren()) {
                        if (!subNode.getData().equals("Entreprises")) {
                            subNode.setSelected(true);
                            subNode.setExpanded(true);
                            selectedAgenceTree = addElement(selectedAgenceTree, subNode);

                        }
                    }
                }
            }
        } else {
            selectAllAge = false;
            for (TreeNode node = selectedNode.getParent(); node != null; node = node.getParent()) {
                if (!node.getData().equals("Entreprises")) {
//                    node.setSelected(true);
                    node.setExpanded(true);
//                    selectedAgenceTree = addElement(selectedAgenceTree, node);
                }
            }
        }
        loadSelectedAgence();
    }

    public void onNodeUnSelect(NodeUnselectEvent event) {
        TreeNode selectedNode = event.getTreeNode();
        selectedNode.setExpanded(true);
        selectAllAge = false;
        if (((Agence) selectedNode.getData()).getRaisonsociale().equals("Toutes") || ((Agence) selectedNode.getData()).getRaisonsociale().equals("All")) {
            selectedAgenceTree = new TreeNode[0];
        }
        loadSelectedAgence();
    }

    public void loadAgenceTree() {
        try {
            List<Agence> listAge = new ArrayList<>();
            if (currentUser.getEtat().equals("Super")) {
                listAge.addAll(agenceFacade.findAll_Siege_Etat("Actif".toUpperCase(ROOT)));
            } else {
                for (Agence age : currentUser.getAgenceCollection()) {
                    if (age.getAgeIdagence() == null) {
                        if (!listAge.contains(age)) {
                            listAge.add(age);
                        }
                    }
                }
                if (listAge.isEmpty()) {
                    listAge.addAll(currentUser.getAgenceCollection());
                }
            }
            if (!selectAllAge) {
                if (!listAge.isEmpty()) {
                    selectedAgenceTree = new TreeNode[0];
                    Collections.sort(listAge);
                    agenceTree = new DefaultTreeNode("Entreprises", null);
                    Agence ageT = new Agence(0);
                    ageT.setRaisonsociale(routine.localizeMessage("Toutes"));
                    nodeAll = new DefaultTreeNode("Tout", ageT, agenceTree);
                    nodeAll.setSelected(false);
                    //--------------------------------------
                    List<TreeNode> listSiege = new ArrayList<>();
//                List<TreeNode> listBureau = new ArrayList<>();
                    //------------------------------------------------------------Agences sièges
                    for (Agence a : listAge) {
                        DefaultTreeNode node = new DefaultTreeNode("Siege", a, agenceTree);
                        if (currentUser.getIdagence().equals(a)) {
                            node.setSelected(true);
                            node.setExpanded(true);
                            selectedAgenceTree = addElement(selectedAgenceTree, node);
                        }
                        listSiege.add(node);
                    }
                    //------------------------------------------------------------Agences bureaux
                    for (TreeNode siege : listSiege) {
                        listAge.clear();
                        if (currentUser.getEtat().equals("Super")) {
                            listAge.addAll(((Agence) siege.getData()).getAgenceCollection());
                        } else {
                            for (Agence age : currentUser.getAgenceCollection()) {
                                if (age.getAgeIdagence() != null) {
                                    if (age.getAgeIdagence().equals(((Agence) siege.getData()))) {
                                        listAge.add(age);
                                    }
                                }
                            }
                        }
                        Collections.sort(listAge);
                        for (Agence a : listAge) {
                            if (a.getEtat().equals("Actif")) {
                                DefaultTreeNode node = new DefaultTreeNode("Bureau", a, siege);
                                if (currentUser.getIdagence().equals((Agence) node.getData())) {
                                    node.setSelected(true);
                                    if (node.getParent() != null) {
                                        node.getParent().setExpanded(true);
                                    }
                                    selectedAgenceTree = addElement(selectedAgenceTree, node);
                                }
//                    listBureau.add(node);
                            }
                        }
                    }
                } else {
                    agenceTree = new DefaultTreeNode("Entreprises", null);
                }
                loadSelectedAgence();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadSelectedAgence() {
        try {
            selectedAgenceList.clear();
            selectedAgences = "";
            for (TreeNode node : selectedAgenceTree) {
                Agence a = (Agence) node.getData();
                if (a.getIdagence() != 0) {
                    if (a.getAgeIdagence() == null) {
                        if (!selectedAgenceList.contains(a)) {
                            selectedAgenceList.add(a);
                            if (selectedAgences.equals("")) {
                                visitedAgence = a;
                            }
                            selectedAgences += a.getRaisonsociale() + " (), ";
                        }
                    }
                }
            }
            for (TreeNode node : selectedAgenceTree) {
                Agence a = (Agence) node.getData();
                if (a.getIdagence() != 0) {
                    if (a.getAgeIdagence() != null) {
                        if (!selectedAgenceList.contains(a)) {
                            selectedAgenceList.add(a);
                            if (selectedAgences.equals("")) {
                                visitedAgence = a;
                                selectedAgences += a.getRaisonsociale() + ", ";
                            }
                        }
                    }
                }
            }
            for (Agence a : selectedAgenceList) {
                if (a.getAgeIdagence() != null) {
                    selectedAgences = selectedAgences.replace((a.getAgeIdagence().getRaisonsociale() + " ("), (a.getAgeIdagence().getRaisonsociale() + " (" + a.getRaisonsociale() + ", "));
                }
            }
            if (selectedAgences.equals("")) {
                loadAgenceTree();
            } else {
                selectedAgences = selectedAgences.replaceAll(" \\(\\)", "");
                selectedAgences = selectedAgences.replaceAll(", \\)", ")");
                selectedAgences = selectedAgences.substring(0, (selectedAgences.length() - 2));
            }
            if (!FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("currentUserAgence")) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("currentUserAgence", selectedAgenceList);
            } else {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("currentUserAgence");
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("currentUserAgence", selectedAgenceList);
            }
            //--------------------------------------------
            if (visitedAgence != null) {
                if (visitedAgence.getAgeIdagence() == null) {
                    detectHomePage(visitedAgence.getDomaine());
                    loadGadgets(visitedAgence.getIdagence());
                } else {
                    detectHomePage(visitedAgence.getAgeIdagence().getDomaine());
                    loadGadgets(visitedAgence.getIdagence());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadGadgets(Integer selectedAgence) {
        if (selectedAgence != null) {
            List<Agence> agenceList = new ArrayList();
            agenceList.add(new Agence(selectedAgence));
            listGalery.clear();
            listGalery.addAll(multimediaFacade.findByCategorie_Actif_Etat("%" + "Galerie / Gallery".toUpperCase(ROOT) + "%", true, "Actif".toUpperCase(ROOT), agenceList));
        }
    }

    public void onMarkerSelect(OverlaySelectEvent event) {
        selectedMarker = (Marker) event.getOverlay();
        selectedMarker.setTitle("MIT2 Sarl");
    }

    public void initMenu() {
        if (!currentUser.getEtat().equals("Super")) {
            droits += "";
            //********************************************************************
            //-------------module de sécurité
            md_sec = droits.contains("md_sec");
            //-------------module veille
            md_vei = droits.contains("md_vei");
            //********************************************************************
            //-------------Paramétrage du module sécurité
            params = droits.contains("params");
            //-------------Paramétrage du module veille
            paramp = droits.contains("paramp");
            //-------------Traitement du module sécurité
            traims = droits.contains("traims");
            //-------------Traitement du module veille
            traimp = droits.contains("traimp");
            //-------------Analyse du module sécurité
            analys = droits.contains("analys");
            //-------------Analyse du module veille
            analyp = droits.contains("analyp");
            //********************************************************************
            //-------------Agence
            agenc_ = droits.contains("agenc_");
            ag_cop = droits.contains("ag_cop");
            ag_mod = droits.contains("ag_mod");
            ag_sup = droits.contains("ag_sup");
            ag_con = droits.contains("ag_con");
            ag_add = droits.contains("ag_add");
            ag_imp = droits.contains("ag_imp");
            ag_res = droits.contains("ag_res");
            ag_det = droits.contains("ag_det");
            //-------------Comptes
            compte = droits.contains("compte");
            ct_cop = droits.contains("ct_cop");
            ct_mod = droits.contains("ct_mod");
            ct_sup = droits.contains("ct_sup");
            ct_con = droits.contains("ct_con");
            ct_add = droits.contains("ct_add");
            ct_imp = droits.contains("ct_imp");
            ct_res = droits.contains("ct_res");
            ct_det = droits.contains("ct_det");
            //-------------Groupe d'utilisateur
            grp_te = droits.contains("grp_te");
            gr_cop = droits.contains("gr_cop");
            gr_mod = droits.contains("gr_mod");
            gr_sup = droits.contains("gr_sup");
            gr_con = droits.contains("gr_con");
            gr_add = droits.contains("gr_add");
            gr_imp = droits.contains("gr_imp");
            gr_res = droits.contains("gr_res");
            gr_det = droits.contains("gr_det");
            //-------------Attribution des privilèges
            pr_cpt = droits.contains("pr_cpt");
            pr_grp = droits.contains("pr_grp");
            //-------------Mouchard
            muchrd = droits.contains("muchrd");
            //-------------Multimedia
            multim = droits.contains("multim");
            mm_cop = droits.contains("mm_cop");
            mm_mod = droits.contains("mm_mod");
            mm_sup = droits.contains("mm_sup");
            mm_con = droits.contains("mm_con");
            mm_add = droits.contains("mm_add");
            mm_imp = droits.contains("mm_imp");
            mm_res = droits.contains("mm_res");
            mm_det = droits.contains("mm_det");
            //-------------Article
            articl = droits.contains("articl");
            at_cop = droits.contains("at_cop");
            at_mod = droits.contains("at_mod");
            at_sup = droits.contains("at_sup");
            at_con = droits.contains("at_con");
            at_add = droits.contains("at_add");
            at_imp = droits.contains("at_imp");
            at_res = droits.contains("at_res");
            at_det = droits.contains("at_det");
            //-------------Analyse de stock
            an_stk = droits.contains("an_stk");
            //-------------Comptoir
            cptoir = droits.contains("cptoir");
            kt_cop = droits.contains("kt_cop");
            kt_mod = droits.contains("kt_mod");
            kt_sup = droits.contains("kt_sup");
            kt_con = droits.contains("kt_con");
            kt_add = droits.contains("kt_add");
            kt_imp = droits.contains("kt_imp");
            kt_res = droits.contains("kt_res");
            kt_det = droits.contains("kt_det");
            //-------------Notifications
            no_sms = droits.contains("no_sms");
            ns_cop = droits.contains("ns_cop");
            ns_mod = droits.contains("ns_mod");
            ns_sup = droits.contains("ns_sup");
            ns_con = droits.contains("ns_con");
            ns_add = droits.contains("ns_add");
            ns_imp = droits.contains("ns_imp");
            ns_res = droits.contains("ns_res");
            ns_det = droits.contains("ns_det");
        } else {
            //********************************************************************
            //-------------module de sécurité
            md_sec = true;
            //-------------module du veille
            md_vei = true;
            //********************************************************************
            //-------------Paramétrage du module sécurité
            params = true;
            //-------------Paramétrage du module veille
            paramp = true;
            //-------------Traitement du module sécurité
            traims = true;
            //-------------Traitement du module veille
            traimp = true;
            //-------------Analyse du module sécurité
            analys = true;
            //-------------Analyse du module personnel
            analyp = true;
            //********************************************************************
            //-------------Agence
            agenc_ = true;
            ag_cop = true;
            ag_mod = true;
            ag_sup = true;
            ag_con = true;
            ag_add = true;
            ag_imp = true;
            ag_res = true;
            ag_det = true;
            //-------------Comptes
            compte = true;
            ct_cop = true;
            ct_mod = true;
            ct_sup = true;
            ct_con = true;
            ct_add = true;
            ct_imp = true;
            ct_res = true;
            ct_det = true;
            //-------------Groupe d'utilisateur
            grp_te = true;
            gr_cop = true;
            gr_mod = true;
            gr_sup = true;
            gr_con = true;
            gr_add = true;
            gr_imp = true;
            gr_res = true;
            gr_det = true;
            //-------------Attribution des privilèges
            pr_cpt = true;
            pr_grp = true;
            //-------------Mouchard
            muchrd = true;
            //-------------Multimedia
            multim = true;
            mm_cop = true;
            mm_mod = true;
            mm_sup = true;
            mm_con = true;
            mm_add = true;
            mm_imp = true;
            mm_res = true;
            mm_det = true;
            //-------------Article
            articl = true;
            at_cop = true;
            at_mod = true;
            at_sup = true;
            at_con = true;
            at_add = true;
            at_imp = true;
            at_res = true;
            at_det = true;
            //-------------Analyse de stock
            an_stk = true;
            //-------------Comptoir
            cptoir = true;
            kt_cop = true;
            kt_mod = true;
            kt_sup = true;
            kt_con = true;
            kt_add = true;
            kt_imp = true;
            kt_res = true;
            kt_det = true;
            //-------------Notifications
            no_sms = true;
            ns_cop = true;
            ns_mod = true;
            ns_sup = true;
            ns_con = true;
            ns_add = true;
            ns_imp = true;
            ns_res = true;
            ns_det = true;
        }
    }

    public String prepareConnexion() {
        try {
            if (!FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("currentUser")) {
                currentUser = new Compte();
                return "authenticate.xhtml?faces-redirect=true";
            } else {
                if (FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().containsKey("menu_form:sreenH")) {
                    routine.setScreenH(parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("menu_form:sreenH")));
                }
                if (FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().containsKey("menu_form:sreenW")) {
                    routine.setScreenW(parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("menu_form:sreenW")));
                }
                return "portail.xhtml?faces-redirect=true";
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "authenticate.xhtml?faces-redirect=true";
        }
    }

    public void prepareActivation() {
        try {
            currentAgence = selectedAgenceList.get(0);
            if (currentAgence == null) {
                reglage = new Reglage();
                reglage.setIdagence(new Agence());
                reglage.setEssai("Stop");
                reglage.setVisite("0");
            } else {
                if (reglageFacade.findByIdagence(currentAgence.getIdagence()).isEmpty()) {
                    reglage = new Reglage();
                    reglage.setIdagence(currentAgence);
                    reglage.setEssai("Stop");
                    reglage.setVisite("0");
                } else {
                    reglage = reglageFacade.findByIdagence(currentAgence.getIdagence()).get(0);
                }
            }
            if (reglage.getEssai() != null) {
                if (reglage.getEssai().length() >= 8) {
                    dateEssai = new SimpleDateFormat("yyyy-dd-MM").parse(reglage.getEssai());
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void updateActivation() {
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
            try {
                if (reglage.getEssai().length() >= 8) {
                    if (dateEssai != null) {
                        reglage.setEssai(new SimpleDateFormat("yyyy-dd-MM").format(dateEssai));
                    } else {
                        reglage.setEssai("-");
                    }
                }
                if (reglage.getIdagence().getIdagence() != null) {
                    if (reglageFacade.findByIdagence(reglage.getIdagence().getIdagence()).isEmpty()) {
                        reglage.setIdReglage(reglageFacade.nextId());
                        reglageFacade.create(reglage);
                        libelleOperation = routine.localizeMessage("Ajouter", new Locale("fr")) + " " + routine.localizeMessage("Parametrage", new Locale("fr"));
                        libelleOperation_en = routine.localizeMessage("Ajouter", new Locale("en")) + " " + routine.localizeMessage("Parametrage", new Locale("en"));
                    } else {
                        reglageFacade.edit(reglage);
                        libelleOperation = routine.localizeMessage("Modifier", new Locale("fr")) + " " + routine.localizeMessage("Parametrage", new Locale("fr"));
                        libelleOperation_en = routine.localizeMessage("Modifier", new Locale("en")) + " " + routine.localizeMessage("Parametrage", new Locale("en"));
                    }
                    agenceFacade.edit(reglage.getIdagence());
                    routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, "idAgence = " + reglage.getIdagence().getIdagence(), currentUser, operationsFacade);
                    routine.feedBack("Information", currentUser.getLangue().equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Succes_operation"), null);
                    RequestContext.getCurrentInstance().getScriptsToExecute().add("PF('activation').hide()");
                } else {
                    routine.feedBack("Avertissement", currentUser.getLangue().equals("fr") ? libelleOperation : libelleOperation_en, "Unknown Agency !", null);
                }
            } catch (Exception e) {
                routine.feedBack("Erreur", currentUser.getLangue().equals("fr") ? libelleOperation : libelleOperation_en, null, e);
                e.printStackTrace();
            }
        }
    }

    public void loadUserModules() {
        try {
            listModuleUser.clear();
            if (!currentUser.getEtat().equals("Super")) {
                List<Menu> listM = new ArrayList<>();
                listM.addAll(menuFacade.findByCategorie_Etat("Module".toUpperCase(ROOT), "Actif".toUpperCase(ROOT)));
                listM.stream().filter((m) -> (droits.contains(m.getShortcut()))).forEach((m) -> {
                    listModuleUser.add(m);
                });
            } else {
                listModuleUser.addAll(menuFacade.findByCategorie_Etat("Module".toUpperCase(ROOT), "Actif".toUpperCase(ROOT)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean essai() {
        today = new Date(currentTimeMillis());
        try {
            if (!currentUser.getEtat().equals("Super")) {
                List<Reglage> listReg = reglageFacade.findByIdagence(currentUser.getIdagence().getAgeIdagence() != null ? currentUser.getIdagence().getAgeIdagence().getIdagence() : currentUser.getIdagence().getIdagence());
                if (!listReg.isEmpty()) {
                    switch (listReg.get(0).getEssai()) {
                        case "Stop":
                            essai = true;
                            break;
                        case "-":
                            essai = false;
                            break;
                        default:
                            Date dateExpire = new SimpleDateFormat("yyyy-dd-MM").parse(listReg.get(0).getEssai());
                            if (dateExpire.before(today)) {
                                essai = true;
                                listReg.get(0).setEssai("Stop");
                                reglageFacade.edit(listReg.get(0));
                            } else {
                                essai = false;
                            }
                            break;
                    }
                }
            } else {
                essai = false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return essai;
    }

    public String authenticationGeneral() {
        String location = authentication();
        return location;
    }

    public String authentication() {
        changeMe = currentUser.getMdp().equals("is4You!");
        //*********************************
        try {
            detectHomePage(((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURL().toString());
            List<Agence> agenceList = new ArrayList();
            agenceList.add(visitedAgence);
            agenceList.addAll(visitedAgence.getAgenceCollection());
            if (nbreCnx == 0) {
                List<Compte> listCpt = new ArrayList<>();
                listCpt.addAll(compteFacade.findByConnexion_Etat("true".toUpperCase(ROOT), "Actif".toUpperCase(ROOT), agenceList));
                listCpt.stream().map((cpt) -> {
                    cpt.setConnexion("false");
                    return cpt;
                }).forEach((cpt) -> {
                    compteFacade.edit(cpt);
                });
            }
            if (openConnection) {
                if (nbreEchec >= 3) {
                    openConnection = false;
                    Thread wait = new Thread() {
                        @Override
                        public synchronized void run() {
                            try {
                                wait(300000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            nbreEchec = 0;
                            openConnection = true;
                        }
                    };
                    wait.start();
                    routine.feedBack("Avertissement", routine.localizeMessage("Connexion"), routine.localizeMessage("Echec_connexion_suspect"), null);
                    return "authenticate.xhtml?faces-redirect=true";
                } else {
                    ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
                    if (!ext.getRequestServletPath().contains("authenticate") && !ext.getRequestServletPath().contains("index") && !pause) {
                        Compte newCpte = compteFacade.find(currentUser.getIdCompte());
                        currentUser = newCpte;
                    } else {
                        if ((currentUser.getLogin() + "").equals("super")) {
                            currentUser = compteFacade.findByLoginMdp_Etat(currentUser.getLogin().toUpperCase(ROOT), currentUser.getMdp().hashCode() + "", "%%", agenceFacade.findAll_Siege_Etat("Actif".toUpperCase(ROOT)));
                        } else {
                            currentUser = compteFacade.findByLoginMdp_Etat(currentUser.getLogin().toUpperCase(ROOT), currentUser.getMdp().hashCode() + "", "%%", agenceList);
                        }
                    }
                    if (currentUser != null) {
                        //-----------------
                        locale = currentUser.getLangue();
                        if (!currentUser.getEtat().equals("Super")) {
                            if (!currentUser.getActif() || !currentUser.getEtat().equals("Actif")) {
                                routine.feedBack("Avertissement", routine.localizeMessage("Connexion", new Locale(locale)), routine.localizeMessage("Compte_inactif", new Locale(locale)), null, new Locale(locale));
                                return "authenticate.xhtml?faces-redirect=true";
                            }
                        }
                        if (!essai()) {
                            if (!ext.getRequestServletPath().contains("authenticate") && !ext.getRequestServletPath().contains("index")) {
                                pause = false;
                                routine.feedBack("Information", routine.localizeMessage("Connexion", new Locale(locale)), routine.localizeMessage("Bienvenue", new Locale(locale)) + " " + (currentUser.getSexe().contains("Masculin") ? "Mr " : "Mme ") + currentUser.getNomPrenom(), null, new Locale(locale));
                                return ext.getRequestServletPath() + "?faces-redirect=true";
                            } else {
                                currentUser.setConnexion("true");
                                compteFacade.edit(currentUser);
                                //----------------
                                if (changeMe) {
                                    prepareUpdateProfile();
                                }
                                List<GroupeUtilisateur> listGu = new ArrayList<>();
                                listGu.addAll(currentUser.getGroupeUtilisateurCollection());
                                droits = currentUser.getDroits();
                                listGu.stream().forEach((gu) -> {
                                    droits += gu.getDroits();
                                });
                                //Activation des menus
                                initMenu();
                                loadUserModules();
                                loadAgenceTree();
                                try {
                                    setNodeSelectAll(nodeAll);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("currentUser")) {
                                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("currentUser");
                                }
                                if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("droits")) {
                                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("droits");
                                }
                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("currentUser", currentUser);
                                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("droits", droits);
                                if (FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().containsKey("menu_form:sreenH")) {
                                    routine.setScreenH(parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("menu_form:sreenH")));
                                }
                                if (FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().containsKey("menu_form:sreenW")) {
                                    routine.setScreenW(parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("menu_form:sreenW")));
                                }
                                routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteHost(), "Connexion", "Connection", currentUser.getLangue().equals("fr") ? "Système" : "System", currentUser, operationsFacade);
                                routine.feedBack("Information", routine.localizeMessage("Connexion", new Locale(locale)), routine.localizeMessage("Bienvenue", new Locale(locale)) + " " + (currentUser.getSexe().contains("Masculin") ? "Mr " : "Mme ") + currentUser.getNomPrenom(), null, new Locale(locale));
                                if (!currentUser.getEtat().equals("Super")) {
                                    nbreCnx++;
                                }
                                //--------------
                                return "portail.xhtml?faces-redirect=true";
                            }
                        } else {
                            routine.feedBack("Avertissement", routine.localizeMessage("Connexion"), routine.localizeMessage("Expiration"), null);
                            currentUser = new Compte();
                            return "authenticate.xhtml?faces-redirect=true";
                        }
                    } else {
                        if (!pause) {
                            nbreEchec++;
                        }
                        routine.feedBack("Avertissement", routine.localizeMessage("Connexion"), routine.localizeMessage("Informations_incorrectes") + ", " + (3 - nbreEchec) + " " + routine.localizeMessage("Essai_restant"), null);
                        currentUser = new Compte();
                        return "authenticate.xhtml?faces-redirect=true";
                    }
                }
            } else {
                routine.feedBack("Avertissement", routine.localizeMessage("Connexion"), routine.localizeMessage("Echec_connexion_suspect"), null);
                currentUser = new Compte();
                return "authenticate.xhtml?faces-redirect=true";
            }
        } catch (Exception e) {
            routine.feedBack("Erreur", routine.localizeMessage("Connexion"), routine.localizeMessage("Connexion"), e);
            currentUser = new Compte();
            return "authenticate.xhtml?faces-redirect=true";
        }
    }

    public String startBreak() {
        currentUser.setMdp("");
        pause = true;
        ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
        return ext.getRequestServletPath() + "?faces-redirect=true";
    }

    public void startBreakAjax() {
        try {
            if (currentUser.getConnexion() != null) {
                currentUser.setMdp("");
                pause = true;
                ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
                ((FacesContext.getCurrentInstance()).getExternalContext()).redirect(ext.getRequestServletPath().substring(1) + "?faces-redirect=true");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void meanWhile() {
        System.out.println("On break...");
    }

    public String stopBreak() {
        return authentication();
    }

    public void prepareUpdateProfile() {
        if (currentUser.getIdCompte() != null) {
            currentUser = compteFacade.find(currentUser.getIdCompte());
            oldPass = currentUser.getMdp();
            oldValue = currentUser.getLogin() + "|" + currentUser.getNomPrenom();
        }
    }

    public void updateProfile() {
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
            changeMe = currentUser.getMdp().equals("is4You!");
            String libelleOperation = "", libelleOperation_en = "";
            try {
                if (oldPass.equals(oldPassTyped.hashCode() + "")) {
                    currentUser.setMdp(currentUser.getMdp().hashCode() + "");
                    currentUser.setLogin((currentUser.getLogin() + "").trim());
                    List<Agence> agenceList = new ArrayList();
                    agenceList.add(currentUser.getIdagence());
                    if ((oldValue.equals(currentUser.getLogin() + "|" + currentUser.getNomPrenom())) || (((!(oldValue.substring(0, oldValue.lastIndexOf('|'))).equals(currentUser.getLogin()) && (oldValue.substring(oldValue.lastIndexOf('|') + 1)).equals(currentUser.getNomPrenom())) && (compteFacade.findByLogin_Etat(currentUser.getLogin().toUpperCase(ROOT), "%%", agenceList).isEmpty())) || ((!(oldValue.substring(oldValue.lastIndexOf('|') + 1)).equals(currentUser.getNomPrenom()) && (oldValue.substring(0, oldValue.lastIndexOf('|'))).equals(currentUser.getLogin()))) || ((!(oldValue.substring(oldValue.lastIndexOf('|') + 1)).equals(currentUser.getNomPrenom()) && !(oldValue.substring(0, oldValue.lastIndexOf('|'))).equals(currentUser.getLogin())) && (compteFacade.findByLogin_Etat(currentUser.getLogin().toUpperCase(ROOT), "%%", agenceList).isEmpty())))) {
                        currentUser.setDerniereModif(new Date(currentTimeMillis()));
                        compteFacade.edit(currentUser);
                        libelleOperation = routine.localizeMessage("Modifier", new Locale("fr")) + " " + routine.localizeMessage("Un", new Locale("fr")) + " " + routine.localizeMessage("Compte", new Locale("fr"));
                        libelleOperation_en = routine.localizeMessage("Modifier", new Locale("en")) + " " + routine.localizeMessage("Un", new Locale("en")) + " " + routine.localizeMessage("Compte", new Locale("en"));
                        routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, currentUser.getNomPrenom(), currentUser, operationsFacade);
                        if (FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().containsKey("currentUser")) {
                            FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().remove("currentUser");
                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("currentUser", currentUser);
                        } else {
                            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("currentUser", currentUser);
                        }
                        routine.feedBack("Information", currentUser.getLangue().equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Succes_operation"), null);
                    } else {
                        routine.feedBack("Avertissement", currentUser.getLangue().equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Doublon"), null);
                    }
                } else {
                    routine.feedBack("Avertissement", currentUser.getLangue().equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Ancien_mdp_incorrect"), null);
                }
            } catch (Exception e) {
                routine.feedBack("Erreur", currentUser.getLangue().equals("fr") ? libelleOperation : libelleOperation_en, null, e);
                e.printStackTrace();
            }
        }
    }

    public void switchLanguage() {
        locale = locale.equals("fr") ? "en" : "fr";
        if (currentUser.getConnexion() != null) {
            saveDefaultLocale();
        }
    }

    public String anglais() {
        locale = "en";
        saveDefaultLocale();
        ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
        return ext.getRequestServletPath() + "?faces-redirect=true";
    }

    public String francais() {
        locale = "fr";
        saveDefaultLocale();
        ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
        return ext.getRequestServletPath() + "?faces-redirect=true";
    }

    public String anglaisNS() {
        locale = "en";
        if (FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().containsKey("menu_form:sreenH")) {
            routine.setScreenH(parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("menu_form:sreenH")));
        }
        if (FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().containsKey("menu_form:sreenW")) {
            routine.setScreenW(parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("menu_form:sreenW")));
        }
        ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
        return ext.getRequestServletPath() + "?faces-redirect=true";
    }

    public String francaisNS() {
        locale = "fr";
        if (FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().containsKey("menu_form:sreenH")) {
            routine.setScreenH(parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("menu_form:sreenH")));
        }
        if (FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().containsKey("menu_form:sreenW")) {
            routine.setScreenW(parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("menu_form:sreenW")));
        }
        ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
        return ext.getRequestServletPath() + "?faces-redirect=true";
    }

    public void saveDefaultLocale() {
        if (currentUser.getIdCompte() != null) {
            currentUser = compteFacade.find(currentUser.getIdCompte());
            currentUser.setLangue(locale);
            compteFacade.edit(currentUser);
        }
    }

    public void detectHomePage(String url) {
        try {
            //String url = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURL().toString();
            // || url.contains("localhost") || url.contains("192.")
            index = "index.xhtml?faces-redirect=true";
            List<Agence> listAg = new ArrayList<>();
            //-----------------------------------------------------------------------------
            listAg.addAll(agenceFacade.findAll_Etat("Actif".toUpperCase(ROOT)));
            if (!listAg.isEmpty()) {
                visitedAgence = listAg.get(0);
                redirection = "/PriceWatcher-web/index.xhtml";
                index = "index.xhtml?faces-redirect=true";
                if (!FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("currentUserAgence")) {
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("currentUserAgence", listAg);
                } else {
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("currentUserAgence");
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("currentUserAgence", selectedAgenceList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirection = "/PriceWatcher-web/index.xhtml";
            index = "index.xhtml?faces-redirect=true";
            routine.feedBack("Erreur", "Home Page", null, e);
        }
    }

    public void swichWelcomePage() {
        try {
            detectHomePage(((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURL().toString());
            init();
            if (nbreRdt < 1) {
                nbreRdt++;
                ((FacesContext.getCurrentInstance()).getExternalContext()).redirect(redirection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void watcher() {
        try {
            if (!FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("currentUser") && !FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("currentClient")) {
                ((FacesContext.getCurrentInstance()).getExternalContext()).redirect("authenticate.xhtml?faces-redirect=true");
            } else {
                if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("currentUser")) {
                    Compte cpt = compteFacade.find(((Compte) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUser")).getIdCompte());
                    if (cpt.getConnexion().equals("false")) {
                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
                        if (!currentUser.getEtat().equals("Super")) {
                            nbreCnx--;
                            nbreCnx = (nbreCnx < 0) ? 0 : nbreCnx;
                        }
                        ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).invalidate();
                        ((FacesContext.getCurrentInstance()).getExternalContext()).redirect("authenticate.xhtml?faces-redirect=true");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String disconnection() {
        try {
            if (currentUser.getIdCompte() != null) {
                currentUser = compteFacade.find(currentUser.getIdCompte());
                currentUser.setConnexion("false");
                //----------------
                compteFacade.edit(currentUser);
                routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteHost(), "Déconnexion", "Disconnection", currentUser.getLangue().equals("fr") ? "Système" : "System", currentUser, operationsFacade);
                if (!currentUser.getEtat().equals("Super")) {
                    nbreCnx--;
                    nbreCnx = (nbreCnx < 0) ? 0 : nbreCnx;
                }
            }
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
            ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false)).invalidate();
            nbreRdt = 0;
            return index;
        } catch (Exception e) {
            e.printStackTrace();
            nbreRdt = 0;
            return index;
        }
    }

    public String index() {
        if (FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().containsKey("menu_form:sreenH")) {
            routine.setScreenH(parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("menu_form:sreenH")));
        }
        if (FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().containsKey("menu_form:sreenW")) {
            routine.setScreenW(parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("menu_form:sreenW")));
        }
        loadGadgets(visitedAgence.getIdagence());
        activeIndex = 0;
        return index;
    }

    public void changeModule(ActionEvent ae) {
        try {
            MenuItem mI = (MenuItem) ae.getComponent();
            ((FacesContext.getCurrentInstance()).getExternalContext()).redirect(mI.getTitle());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String moduleSecurite() {
        if (FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().containsKey("menu_form:sreenH")) {
            routine.setScreenH(parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("menu_form:sreenH")));
        }
        if (FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().containsKey("menu_form:sreenW")) {
            routine.setScreenW(parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("menu_form:sreenW")));
        }
        return "moduleSecurite.xhtml?faces-redirect=true";
    }

    public String moduleVeille() {
        if (FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().containsKey("menu_form:sreenH")) {
            routine.setScreenH(parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("menu_form:sreenH")));
        }
        if (FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().containsKey("menu_form:sreenW")) {
            routine.setScreenW(parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("menu_form:sreenW")));
        }
        return "moduleVeille.xhtml?faces-redirect=true";
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            UploadedFile file = event.getFile();
            String filePath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("resources/fichiers");
            String code = (new SimpleDateFormat("SSS")).format(new Date(System.currentTimeMillis()));
            String f_name = file.getFileName().substring(0, file.getFileName().indexOf(".")) + code + file.getFileName().substring(file.getFileName().indexOf("."));
            currentUser.setPhotoRelatif("resources/fichiers/" + f_name);
            currentUser.setPhoto(filePath + File.separator + f_name);
            byte[] bytes = file.getContents();
            try (FileOutputStream out = new FileOutputStream(currentUser.getPhoto(), true)) {
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
                String fileName = (currentUser.getNomPrenom() != null ? currentUser.getNomPrenom().replaceAll(" ", "_") + "_" + compteFacade.nextId() : compteFacade.nextId()) + code + ".png";
                currentUser.setPhotoRelatif("resources/fichiers/" + fileName);
                currentUser.setPhoto(filePath + File.separator + fileName);
                imageOutput = new FileImageOutputStream(new File(currentUser.getPhoto()));
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

    public MultimediaFacadeLocal getMultimediaFacade() {
        return multimediaFacade;
    }

    public void setMultimediaFacade(MultimediaFacadeLocal multimediaFacade) {
        this.multimediaFacade = multimediaFacade;
    }

    public List<Multimedia> getListGalery() {
        return listGalery;
    }

    public void setListGalery(List<Multimedia> listGalery) {
        this.listGalery = listGalery;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public Compte getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Compte currentUser) {
        this.currentUser = currentUser;
    }

    public Routine getRoutine() {
        return routine;
    }

    public void setRoutine(Routine routine) {
        this.routine = routine;
    }

    public Boolean getMd_sec() {
        return md_sec;
    }

    public void setMd_sec(Boolean md_sec) {
        this.md_sec = md_sec;
    }

    public Boolean getParams() {
        return params;
    }

    public void setParams(Boolean params) {
        this.params = params;
    }

    public Boolean getTraims() {
        return traims;
    }

    public void setTraims(Boolean traims) {
        this.traims = traims;
    }

    public Boolean getAnalys() {
        return analys;
    }

    public void setAnalys(Boolean analys) {
        this.analys = analys;
    }

    public Boolean getAgenc_() {
        return agenc_;
    }

    public void setAgenc_(Boolean agenc_) {
        this.agenc_ = agenc_;
    }

    public Boolean getAg_cop() {
        return ag_cop;
    }

    public void setAg_cop(Boolean ag_cop) {
        this.ag_cop = ag_cop;
    }

    public Boolean getAg_mod() {
        return ag_mod;
    }

    public void setAg_mod(Boolean ag_mod) {
        this.ag_mod = ag_mod;
    }

    public Boolean getAg_sup() {
        return ag_sup;
    }

    public void setAg_sup(Boolean ag_sup) {
        this.ag_sup = ag_sup;
    }

    public Boolean getAg_con() {
        return ag_con;
    }

    public void setAg_con(Boolean ag_con) {
        this.ag_con = ag_con;
    }

    public Boolean getAg_add() {
        return ag_add;
    }

    public void setAg_add(Boolean ag_add) {
        this.ag_add = ag_add;
    }

    public Boolean getAg_imp() {
        return ag_imp;
    }

    public void setAg_imp(Boolean ag_imp) {
        this.ag_imp = ag_imp;
    }

    public Boolean getCompte() {
        return compte;
    }

    public void setCompte(Boolean compte) {
        this.compte = compte;
    }

    public Boolean getCt_cop() {
        return ct_cop;
    }

    public void setCt_cop(Boolean ct_cop) {
        this.ct_cop = ct_cop;
    }

    public Boolean getCt_mod() {
        return ct_mod;
    }

    public void setCt_mod(Boolean ct_mod) {
        this.ct_mod = ct_mod;
    }

    public Boolean getCt_sup() {
        return ct_sup;
    }

    public void setCt_sup(Boolean ct_sup) {
        this.ct_sup = ct_sup;
    }

    public Boolean getCt_con() {
        return ct_con;
    }

    public void setCt_con(Boolean ct_con) {
        this.ct_con = ct_con;
    }

    public Boolean getCt_add() {
        return ct_add;
    }

    public void setCt_add(Boolean ct_add) {
        this.ct_add = ct_add;
    }

    public Boolean getCt_imp() {
        return ct_imp;
    }

    public void setCt_imp(Boolean ct_imp) {
        this.ct_imp = ct_imp;
    }

    public Boolean getGrp_te() {
        return grp_te;
    }

    public void setGrp_te(Boolean grp_te) {
        this.grp_te = grp_te;
    }

    public Boolean getGr_cop() {
        return gr_cop;
    }

    public void setGr_cop(Boolean gr_cop) {
        this.gr_cop = gr_cop;
    }

    public Boolean getGr_mod() {
        return gr_mod;
    }

    public void setGr_mod(Boolean gr_mod) {
        this.gr_mod = gr_mod;
    }

    public Boolean getGr_sup() {
        return gr_sup;
    }

    public void setGr_sup(Boolean gr_sup) {
        this.gr_sup = gr_sup;
    }

    public Boolean getGr_con() {
        return gr_con;
    }

    public void setGr_con(Boolean gr_con) {
        this.gr_con = gr_con;
    }

    public Boolean getGr_add() {
        return gr_add;
    }

    public void setGr_add(Boolean gr_add) {
        this.gr_add = gr_add;
    }

    public Boolean getGr_imp() {
        return gr_imp;
    }

    public void setGr_imp(Boolean gr_imp) {
        this.gr_imp = gr_imp;
    }

    public Boolean getPr_cpt() {
        return pr_cpt;
    }

    public void setPr_cpt(Boolean pr_cpt) {
        this.pr_cpt = pr_cpt;
    }

    public Boolean getPr_grp() {
        return pr_grp;
    }

    public void setPr_grp(Boolean pr_grp) {
        this.pr_grp = pr_grp;
    }

    public Boolean getMuchrd() {
        return muchrd;
    }

    public void setMuchrd(Boolean muchrd) {
        this.muchrd = muchrd;
    }

    public UserTransaction getUt() {
        return ut;
    }

    public void setUt(UserTransaction ut) {
        this.ut = ut;
    }

    public OperationsFacadeLocal getOperationsFacade() {
        return operationsFacade;
    }

    public void setOperationsFacade(OperationsFacadeLocal operationsFacade) {
        this.operationsFacade = operationsFacade;
    }

    public ReglageFacadeLocal getReglageFacade() {
        return reglageFacade;
    }

    public void setReglageFacade(ReglageFacadeLocal reglageFacade) {
        this.reglageFacade = reglageFacade;
    }

    public MenuFacadeLocal getMenuFacade() {
        return menuFacade;
    }

    public void setMenuFacade(MenuFacadeLocal menuFacade) {
        this.menuFacade = menuFacade;
    }

    public List<Menu> getListModuleUser() {
        return listModuleUser;
    }

    public void setListModuleUser(List<Menu> listModuleUser) {
        this.listModuleUser = listModuleUser;
    }

    public CompteFacadeLocal getCompteFacade() {
        return compteFacade;
    }

    public void setCompteFacade(CompteFacadeLocal compteFacade) {
        this.compteFacade = compteFacade;
    }

    public Boolean getEssai() {
        return essai;
    }

    public void setEssai(Boolean essai) {
        this.essai = essai;
    }

    public Date getDateEssai() {
        return dateEssai;
    }

    public void setDateEssai(Date dateEssai) {
        this.dateEssai = dateEssai;
    }

    public Boolean getOpenConnection() {
        return openConnection;
    }

    public void setOpenConnection(Boolean openConnection) {
        this.openConnection = openConnection;
    }

    public Integer getNbreEchec() {
        return nbreEchec;
    }

    public void setNbreEchec(Integer nbreEchec) {
        this.nbreEchec = nbreEchec;
    }

    public int getNbreCnx() {
        return nbreCnx;
    }

    public void setNbreCnx(int nbreCnx) {
        SessionMB.nbreCnx = nbreCnx;
    }

    public String getDroits() {
        return droits;
    }

    public void setDroits(String droits) {
        this.droits = droits;
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

    public Boolean getAg_res() {
        return ag_res;
    }

    public void setAg_res(Boolean ag_res) {
        this.ag_res = ag_res;
    }

    public Boolean getAg_det() {
        return ag_det;
    }

    public void setAg_det(Boolean ag_det) {
        this.ag_det = ag_det;
    }

    public Boolean getCt_res() {
        return ct_res;
    }

    public void setCt_res(Boolean ct_res) {
        this.ct_res = ct_res;
    }

    public Boolean getCt_det() {
        return ct_det;
    }

    public void setCt_det(Boolean ct_det) {
        this.ct_det = ct_det;
    }

    public Boolean getGr_res() {
        return gr_res;
    }

    public void setGr_res(Boolean gr_res) {
        this.gr_res = gr_res;
    }

    public Boolean getGr_det() {
        return gr_det;
    }

    public void setGr_det(Boolean gr_det) {
        this.gr_det = gr_det;
    }

    public Reglage getReglage() {
        return reglage;
    }

    public void setReglage(Reglage reglage) {
        this.reglage = reglage;
    }

    public Boolean getMultim() {
        return multim;
    }

    public void setMultim(Boolean multim) {
        this.multim = multim;
    }

    public Boolean getMm_cop() {
        return mm_cop;
    }

    public void setMm_cop(Boolean mm_cop) {
        this.mm_cop = mm_cop;
    }

    public Boolean getMm_mod() {
        return mm_mod;
    }

    public void setMm_mod(Boolean mm_mod) {
        this.mm_mod = mm_mod;
    }

    public Boolean getMm_sup() {
        return mm_sup;
    }

    public void setMm_sup(Boolean mm_sup) {
        this.mm_sup = mm_sup;
    }

    public Boolean getMm_con() {
        return mm_con;
    }

    public void setMm_con(Boolean mm_con) {
        this.mm_con = mm_con;
    }

    public Boolean getMm_add() {
        return mm_add;
    }

    public void setMm_add(Boolean mm_add) {
        this.mm_add = mm_add;
    }

    public Boolean getMm_imp() {
        return mm_imp;
    }

    public void setMm_imp(Boolean mm_imp) {
        this.mm_imp = mm_imp;
    }

    public Boolean getMm_res() {
        return mm_res;
    }

    public void setMm_res(Boolean mm_res) {
        this.mm_res = mm_res;
    }

    public Boolean getMm_det() {
        return mm_det;
    }

    public void setMm_det(Boolean mm_det) {
        this.mm_det = mm_det;
    }

    public Boolean getMd_vei() {
        return md_vei;
    }

    public void setMd_vei(Boolean md_vei) {
        this.md_vei = md_vei;
    }

    public Boolean getParamp() {
        return paramp;
    }

    public void setParamp(Boolean paramp) {
        this.paramp = paramp;
    }

    public Boolean getTraimp() {
        return traimp;
    }

    public void setTraimp(Boolean traimp) {
        this.traimp = traimp;
    }

    public Boolean getAnalyp() {
        return analyp;
    }

    public void setAnalyp(Boolean analyp) {
        this.analyp = analyp;
    }

    public Boolean getArticl() {
        return articl;
    }

    public void setArticl(Boolean articl) {
        this.articl = articl;
    }

    public Boolean getAt_cop() {
        return at_cop;
    }

    public void setAt_cop(Boolean at_cop) {
        this.at_cop = at_cop;
    }

    public Boolean getAt_mod() {
        return at_mod;
    }

    public void setAt_mod(Boolean at_mod) {
        this.at_mod = at_mod;
    }

    public Boolean getAt_sup() {
        return at_sup;
    }

    public void setAt_sup(Boolean at_sup) {
        this.at_sup = at_sup;
    }

    public Boolean getAt_con() {
        return at_con;
    }

    public void setAt_con(Boolean at_con) {
        this.at_con = at_con;
    }

    public Boolean getAt_add() {
        return at_add;
    }

    public void setAt_add(Boolean at_add) {
        this.at_add = at_add;
    }

    public Boolean getAt_imp() {
        return at_imp;
    }

    public void setAt_imp(Boolean at_imp) {
        this.at_imp = at_imp;
    }

    public Boolean getAt_res() {
        return at_res;
    }

    public void setAt_res(Boolean at_res) {
        this.at_res = at_res;
    }

    public Boolean getAt_det() {
        return at_det;
    }

    public void setAt_det(Boolean at_det) {
        this.at_det = at_det;
    }

    public Boolean getAn_stk() {
        return an_stk;
    }

    public void setAn_stk(Boolean an_stk) {
        this.an_stk = an_stk;
    }

    public int getNbreVst() {
        return nbreVst;
    }

    public void setNbreVst(int nbreVst) {
        SessionMB.nbreVst = nbreVst;
    }

    public Date getDateServer() {
        return dateServer;
    }

    public void setDateServer(Date dateServer) {
        this.dateServer = dateServer;
    }

    public TagCloudModel getTc() {
        return tc;
    }

    public void setTc(TagCloudModel tc) {
        this.tc = tc;
    }

    public MapModel getSigModel() {
        return sigModel;
    }

    public void setSigModel(MapModel sigModel) {
        this.sigModel = sigModel;
    }

    public Marker getSelectedMarker() {
        return selectedMarker;
    }

    public void setSelectedMarker(Marker selectedMarker) {
        this.selectedMarker = selectedMarker;
    }

    public Integer getZoom() {
        return zoom;
    }

    public void setZoom(Integer zoom) {
        this.zoom = zoom;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public int getNbreRdt() {
        return nbreRdt;
    }

    public void setNbreRdt(int nbreRdt) {
        this.nbreRdt = nbreRdt;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public Boolean getPause() {
        return pause;
    }

    public void setPause(Boolean pause) {
        this.pause = pause;
    }

    public String getRedirection() {
        return redirection;
    }

    public void setRedirection(String redirection) {
        this.redirection = redirection;
    }

    public int getIdleTime() {
        return idleTime;
    }

    public void setIdleTime(int idleTime) {
        this.idleTime = idleTime;
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

    public Date getToday() {
        return today;
    }

    public void setToday(Date today) {
        this.today = today;
    }

    public boolean isChangeMe() {
        return changeMe;
    }

    public void setChangeMe(boolean changeMe) {
        this.changeMe = changeMe;
    }

    public TreeNode getAgenceTree() {
        return agenceTree;
    }

    public void setAgenceTree(TreeNode agenceTree) {
        this.agenceTree = agenceTree;
    }

    public TreeNode[] getSelectedAgenceTree() {
        return selectedAgenceTree;
    }

    public void setSelectedAgenceTree(TreeNode[] selectedAgenceTree) {
        this.selectedAgenceTree = selectedAgenceTree;
    }

    public Agence getVisitedAgence() {
        return visitedAgence;
    }

    public void setVisitedAgence(Agence visitedAgence) {
        this.visitedAgence = visitedAgence;
    }

    public String getSelectedAgences() {
        return selectedAgences;
    }

    public void setSelectedAgences(String selectedAgences) {
        this.selectedAgences = selectedAgences;
    }

    public List<Agence> getSelectedAgenceList() {
        return selectedAgenceList;
    }

    public void setSelectedAgenceList(List<Agence> selectedAgenceList) {
        this.selectedAgenceList = selectedAgenceList;
    }

    public boolean isHideAgencies() {
        return hideAgencies;
    }

    public void setHideAgencies(boolean hideAgencies) {
        this.hideAgencies = hideAgencies;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public GroupeUtilisateurFacadeLocal getGroupeUtilisateurFacade() {
        return groupeUtilisateurFacade;
    }

    public void setGroupeUtilisateurFacade(GroupeUtilisateurFacadeLocal groupeUtilisateurFacade) {
        this.groupeUtilisateurFacade = groupeUtilisateurFacade;
    }

    public ArticleFacadeLocal getBouquetFacade() {
        return bouquetFacade;
    }

    public void setBouquetFacade(ArticleFacadeLocal bouquetFacade) {
        this.bouquetFacade = bouquetFacade;
    }

    public int getActiveIndex() {
        return activeIndex;
    }

    public void setActiveIndex(int activeIndex) {
        this.activeIndex = activeIndex;
    }

    public Boolean getResetCgaPassword() {
        return resetCgaPassword;
    }

    public void setResetCgaPassword(Boolean resetCgaPassword) {
        this.resetCgaPassword = resetCgaPassword;
    }

    public Boolean getCptoir() {
        return cptoir;
    }

    public void setCptoir(Boolean cptoir) {
        this.cptoir = cptoir;
    }

    public Boolean getKt_cop() {
        return kt_cop;
    }

    public void setKt_cop(Boolean kt_cop) {
        this.kt_cop = kt_cop;
    }

    public Boolean getKt_mod() {
        return kt_mod;
    }

    public void setKt_mod(Boolean kt_mod) {
        this.kt_mod = kt_mod;
    }

    public Boolean getKt_sup() {
        return kt_sup;
    }

    public void setKt_sup(Boolean kt_sup) {
        this.kt_sup = kt_sup;
    }

    public Boolean getKt_con() {
        return kt_con;
    }

    public void setKt_con(Boolean kt_con) {
        this.kt_con = kt_con;
    }

    public Boolean getKt_add() {
        return kt_add;
    }

    public void setKt_add(Boolean kt_add) {
        this.kt_add = kt_add;
    }

    public Boolean getKt_imp() {
        return kt_imp;
    }

    public void setKt_imp(Boolean kt_imp) {
        this.kt_imp = kt_imp;
    }

    public Boolean getKt_res() {
        return kt_res;
    }

    public void setKt_res(Boolean kt_res) {
        this.kt_res = kt_res;
    }

    public Boolean getKt_det() {
        return kt_det;
    }

    public void setKt_det(Boolean kt_det) {
        this.kt_det = kt_det;
    }

    public Agence getCurrentAgence() {
        return currentAgence;
    }

    public void setCurrentAgence(Agence currentAgence) {
        this.currentAgence = currentAgence;
    }

    public static FacesContext getExternalContext() {
        return externalContext;
    }

    public static void setExternalContext(FacesContext externalContext) {
        SessionMB.externalContext = externalContext;
    }

    public Boolean getSelectAllAge() {
        return selectAllAge;
    }

    public void setSelectAllAge(Boolean selectAllAge) {
        this.selectAllAge = selectAllAge;
    }

    public String getOldPass() {
        return oldPass;
    }

    public void setOldPass(String oldPass) {
        this.oldPass = oldPass;
    }

    public String getOldPassTyped() {
        return oldPassTyped;
    }

    public void setOldPassTyped(String oldPassTyped) {
        this.oldPassTyped = oldPassTyped;
    }

    public Boolean getShwoPass() {
        return shwoPass;
    }

    public void setShwoPass(Boolean shwoPass) {
        this.shwoPass = shwoPass;
    }

    public MailTools getMailTools() {
        return mailTools;
    }

    public void setMailTools(MailTools mailTools) {
        this.mailTools = mailTools;
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

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getNum_dist() {
        return num_dist;
    }

    public void setNum_dist(String num_dist) {
        this.num_dist = num_dist;
    }

    public String getLogin_dist() {
        return login_dist;
    }

    public void setLogin_dist(String login_dist) {
        this.login_dist = login_dist;
    }

    public Date getDebut_rep() {
        return debut_rep;
    }

    public void setDebut_rep(Date debut_rep) {
        this.debut_rep = debut_rep;
    }

    public Date getFin_rep() {
        return fin_rep;
    }

    public void setFin_rep(Date fin_rep) {
        this.fin_rep = fin_rep;
    }

    public static Boolean getPullInfo() {
        return pullInfo;
    }

    public static void setPullInfo(Boolean pullInfo) {
        SessionMB.pullInfo = pullInfo;
    }

    public ArticleFacadeLocal getArticleFacade() {
        return articleFacade;
    }

    public void setArticleFacade(ArticleFacadeLocal articleFacade) {
        this.articleFacade = articleFacade;
    }

    public Article getSelectedArticle() {
        return selectedArticle;
    }

    public void setSelectedArticle(Article selectedArticle) {
        this.selectedArticle = selectedArticle;
    }

    public DefaultTreeNode getNodeAll() {
        return nodeAll;
    }

    public void setNodeAll(DefaultTreeNode nodeAll) {
        this.nodeAll = nodeAll;
    }

    public Boolean getNo_sms() {
        return no_sms;
    }

    public void setNo_sms(Boolean no_sms) {
        this.no_sms = no_sms;
    }

    public Boolean getNs_cop() {
        return ns_cop;
    }

    public void setNs_cop(Boolean ns_cop) {
        this.ns_cop = ns_cop;
    }

    public Boolean getNs_mod() {
        return ns_mod;
    }

    public void setNs_mod(Boolean ns_mod) {
        this.ns_mod = ns_mod;
    }

    public Boolean getNs_sup() {
        return ns_sup;
    }

    public void setNs_sup(Boolean ns_sup) {
        this.ns_sup = ns_sup;
    }

    public Boolean getNs_con() {
        return ns_con;
    }

    public void setNs_con(Boolean ns_con) {
        this.ns_con = ns_con;
    }

    public Boolean getNs_add() {
        return ns_add;
    }

    public void setNs_add(Boolean ns_add) {
        this.ns_add = ns_add;
    }

    public Boolean getNs_imp() {
        return ns_imp;
    }

    public void setNs_imp(Boolean ns_imp) {
        this.ns_imp = ns_imp;
    }

    public Boolean getNs_res() {
        return ns_res;
    }

    public void setNs_res(Boolean ns_res) {
        this.ns_res = ns_res;
    }

    public Boolean getNs_det() {
        return ns_det;
    }

    public void setNs_det(Boolean ns_det) {
        this.ns_det = ns_det;
    }

}
