/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mb;

import usual.Routine;
import entities.Agence;
import entities.Compte;
import entities.Menu;
import java.io.Serializable;
import static java.lang.System.currentTimeMillis;
import java.util.ArrayList;
import static java.util.Arrays.copyOf;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import static java.util.Locale.ROOT;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.NodeUnselectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import sessions.CompteFacadeLocal;
import sessions.MenuFacadeLocal;
import sessions.OperationsFacadeLocal;

/**
 *
 * @author Lucien FOTSA
 */
public class DroitAccesCompteMB implements Serializable {

    @Resource
    private UserTransaction ut;
    @EJB
    private OperationsFacadeLocal operationsFacade;
    //--------------------------------------------------------------------------
    @EJB
    private CompteFacadeLocal compteFacade;
    private List<Compte> listCompte = new ArrayList<>();
    private Integer selectedCompte;
    private Compte user = new Compte();
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
    private MenuFacadeLocal menuFacade;
    private TreeNode menus = new DefaultTreeNode("Menus", null);
    private TreeNode[] selectedMenus;
    private String droits;
    //---------------------------------------------------------
    private List<Agence> currentUserAgence = new ArrayList<>();
    private boolean lock = false;

    /**
     * Creates a new instance of CompteMB
     */
    public DroitAccesCompteMB() {
    }

    @PostConstruct
    private void init() {
        connectedUser = (Compte) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUser");
        currentUserAgence = (List<Agence>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUserAgence");
        loadCompte();
    }

    static TreeNode[] addElement(TreeNode[] a, TreeNode e) {
        a = copyOf(a, a.length + 1);
        a[a.length - 1] = e;
        return a;
    }

    public String droitAccesCompte() {
        return "droitAccesCompte.xhtml?faces-redirect=true";
    }

    public void loadCompte() {
        listCompte.clear();
        List<Compte> listCpte = new ArrayList<>();
        listCpte.addAll(compteFacade.findAll_Etat("Actif".toUpperCase(ROOT), currentUserAgence));
        if (connectedUser.getEtat().equals("Super")) {
            listCompte.addAll(listCpte);
        } else {
            String droitsCpte = connectedUser.getDroits() != null ? connectedUser.getDroits() : "";
            droitsCpte += connectedUser.getGroupeUtilisateurCollection().stream().map((gu) -> (gu.getDroits() != null ? gu.getDroits() : "")).reduce(droitsCpte, String::concat);
            for (Compte cpte : listCpte) {
                String droitsUser = cpte.getDroits() != null ? cpte.getDroits() : "";
                droitsUser += cpte.getGroupeUtilisateurCollection().stream().map((gu) -> (gu.getDroits() != null ? gu.getDroits() : "")).reduce(droitsCpte, String::concat);
                boolean good = true;
                for (int i = 0; i < droitsUser.length(); i += 6) {
                    if (!droitsCpte.contains(droitsUser.subSequence(i, i + 6))) {
                        good = false;
                        break;
                    }
                }
                if (good) {
                    listCompte.add(cpte);
                }
            }
        }
    }

    public void loadMenu() {
        droits = "";
        if (!connectedUser.getEtat().equals("Super")) {
            droits = connectedUser.getDroits();
            connectedUser.getGroupeUtilisateurCollection().stream().forEach((gu) -> {
                droits += gu.getDroits();
            });
        } else {
            List<Menu> listMenu = new ArrayList<>();
            listMenu.addAll(menuFacade.findAll());
            listMenu.stream().forEach((menu) -> {
                droits += menu.getShortcut();
            });
        }
        if (user != null) {
            menus = new DefaultTreeNode("Menus", null);
            List<Menu> listService = new ArrayList<>();
            List<TreeNode> listModule = new ArrayList<>();
            List<TreeNode> listMenu = new ArrayList<>();
            List<TreeNode> listSousMenu = new ArrayList<>();
            List<TreeNode> listFenetre = new ArrayList<>();
//            List<TreeNode> listBouton = new ArrayList<>();
            //------------------------------------------------------------Modules
            listService.addAll(menuFacade.findByCategorie_Etat("Module".toUpperCase(ROOT), "Actif".toUpperCase(ROOT)));
            listService.stream().filter((m) -> (droits.contains(m.getShortcut()))).map((m) -> new DefaultTreeNode("Module", m, menus)).map((node) -> {
                if ((user.getDroits() + "").contains(((Menu) node.getData()).getShortcut())) {
                    node.setSelected(true);
//                    node.setExpanded(true);
                    selectedMenus = addElement(selectedMenus, node);
                }
                return node;
            }).forEach((node) -> {
                listModule.add(node);
            });
            //------------------------------------------------------------Menus
            listModule.stream().map((module) -> {
                listService.clear();
                listService.addAll(menuFacade.findByIdMenuParentCategorie_Etat(((Menu) module.getData()).getIdMenu(), "Menu".toUpperCase(ROOT), "Actif".toUpperCase(ROOT)));
                return module;
            }).forEach((module) -> {
                listService.stream().filter((m) -> (droits.contains(m.getShortcut()))).map((m) -> new DefaultTreeNode("Menu", m, module)).forEach((node) -> {
                    if ((user.getDroits() + "").contains(((Menu) node.getData()).getShortcut())) {
                        node.setSelected(true);
//                        node.setExpanded(true);
                        selectedMenus = addElement(selectedMenus, node);
                    }
                    listMenu.add(node);
                });
            });
            //------------------------------------------------------------Sous menu
            listMenu.stream().forEach((menu) -> {
                List<Menu> listService2 = new ArrayList<>();
                listService.clear();
                listService.addAll(menuFacade.findByIdMenuParentCategorie_Etat(((Menu) menu.getData()).getIdMenu(), "Sous Menu".toUpperCase(ROOT), "Actif".toUpperCase(ROOT)));
                listService2.addAll(menuFacade.findByIdMenuParentCategorie_Etat(((Menu) menu.getData()).getIdMenu(), "Fenêtre".toUpperCase(ROOT), "Actif".toUpperCase(ROOT)));
                listService.stream().filter((m) -> (droits.contains(m.getShortcut()))).map((m) -> new DefaultTreeNode("Sous Menu", m, menu)).forEach((node) -> {
                    if ((user.getDroits() + "").contains(((Menu) node.getData()).getShortcut())) {
                        node.setSelected(true);
//                        node.setExpanded(true);
                        selectedMenus = addElement(selectedMenus, node);
                    }
                    listSousMenu.add(node);
                });
                listService2.stream().filter((m) -> (droits.contains(m.getShortcut()))).map((m) -> new DefaultTreeNode("Fenêtre", m, menu)).forEach((node) -> {
                    if ((user.getDroits() + "").contains(((Menu) node.getData()).getShortcut())) {
                        node.setSelected(true);
//                        node.setExpanded(true);
                        selectedMenus = addElement(selectedMenus, node);
                    }
                    listFenetre.add(node);
                });
            });
            //------------------------------------------------------------Fenetres
            listSousMenu.stream().map((sous_menu) -> {
                listService.clear();
                listService.addAll(menuFacade.findByIdMenuParentCategorie_Etat(((Menu) sous_menu.getData()).getIdMenu(), "Fenêtre".toUpperCase(ROOT), "Actif".toUpperCase(ROOT)));
                return sous_menu;
            }).forEach((sous_menu) -> {
                listService.stream().filter((m) -> (droits.contains(m.getShortcut()))).map((m) -> new DefaultTreeNode("Fenêtre", m, sous_menu)).forEach((node) -> {
                    if ((user.getDroits() + "").contains(((Menu) node.getData()).getShortcut())) {
                        node.setSelected(true);
//                        node.setExpanded(true);
                        selectedMenus = addElement(selectedMenus, node);
                    }
                    listFenetre.add(node);
                });
            });
            //------------------------------------------------------------Boutons
            listFenetre.stream().map((fenetre) -> {
                listService.clear();
                listService.addAll(menuFacade.findByIdMenuParentCategorie_Etat(((Menu) fenetre.getData()).getIdMenu(), "Bouton".toUpperCase(ROOT), "Actif".toUpperCase(ROOT)));
                return fenetre;
            }).forEach((fenetre) -> {
                listService.stream().filter((m) -> (droits.contains(m.getShortcut()))).map((m) -> new DefaultTreeNode("Bouton", m, fenetre)).forEach((node) -> {
                    if ((user.getDroits() + "").contains(((Menu) node.getData()).getShortcut())) {
                        node.setSelected(true);
//                        node.setExpanded(true);
                        selectedMenus = addElement(selectedMenus, node);
                    }
//                    listBouton.add(node);
                });
            });
        } else {
            menus = new DefaultTreeNode("Menus", null);
        }
    }

    public void selectAccount() {
        user = compteFacade.find(selectedCompte);
        selectedMenus = new TreeNode[]{};
        loadMenu();
    }

    public void onNodeSelect(NodeSelectEvent event) {
        event.getTreeNode().setExpanded(event.getTreeNode().isExpanded());
        for (TreeNode node = (event.getTreeNode()).getParent(); node != null; node = node.getParent()) {
            if (!node.getData().equals("Menus")) {
                node.setSelected(true);
                node.setExpanded(node.isExpanded());
                selectedMenus = addElement(selectedMenus, node);
            }
        }
    }

    public void onNodeUnSelect(NodeUnselectEvent event) {
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
            try {
                ut.begin();
                String droits_ = "";
                for (TreeNode tn : selectedMenus) {
                    droits_ += ((Menu) tn.getData()).getShortcut();
                }
                user.setDroits(droits_);
                user.setDerniereModif(new Date(currentTimeMillis()));
                compteFacade.edit(user);
                String langue = FacesContext.getCurrentInstance().getViewRoot().getLocale().getLanguage();
                libelleOperation = routine.localizeMessage("Modifier", new Locale("fr")) + " " + routine.localizeMessage("Les", new Locale("fr")) + " " + routine.localizeMessage("Atribuer_droit_compte", new Locale("fr"));
                libelleOperation_en = routine.localizeMessage("Modifier", new Locale("en")) + " " + routine.localizeMessage("Les", new Locale("en")) + " " + routine.localizeMessage("Atribuer_droit_compte", new Locale("en"));
                routine.operationEffectue(((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr(), ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteUser(), libelleOperation, libelleOperation_en, user.getNomPrenom(), connectedUser, operationsFacade);
                routine.feedBack("Information", langue.equals("fr") ? libelleOperation : libelleOperation_en, routine.localizeMessage("Succes_operation"), null);
                ut.commit();
            } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException e) {
                routine.feedBack("Erreur", libelleOperation, null, e);
                e.printStackTrace();
            }
        }
    }

    public String closePage() {
        return "moduleSecurite.xhtml?faces-redirect=true";
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

    public Integer getSelectedCompte() {
        return selectedCompte;
    }

    public void setSelectedCompte(Integer selectedCompte) {
        this.selectedCompte = selectedCompte;
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

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public MenuFacadeLocal getMenuFacade() {
        return menuFacade;
    }

    public void setMenuFacade(MenuFacadeLocal menuFacade) {
        this.menuFacade = menuFacade;
    }

    public TreeNode getMenus() {
        return menus;
    }

    public void setMenus(DefaultTreeNode menus) {
        this.menus = menus;
    }

    public Compte getUser() {
        return user;
    }

    public void setUser(Compte user) {
        this.user = user;
    }

    public TreeNode[] getSelectedMenus() {
        return selectedMenus;
    }

    public void setSelectedMenus(TreeNode[] selectedMenus) {
        this.selectedMenus = selectedMenus;
    }

    public String getDroits() {
        return droits;
    }

    public void setDroits(String droits) {
        this.droits = droits;
    }

    public List<Agence> getCurrentUserAgence() {
        return currentUserAgence;
    }

    public void setCurrentUserAgence(List<Agence> currentUserAgence) {
        this.currentUserAgence = currentUserAgence;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

}
