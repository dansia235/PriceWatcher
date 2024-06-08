/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mb;

import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.ConfirmHandler;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.PromptHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.WebWindowEvent;
import com.gargoylesoftware.htmlunit.WebWindowListener;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import entities.Agence;
import entities.Article;
import entities.Compte;
import entities.Concurrent;
import entities.Notifications;
import entities.Veilleconcurrent;
import entities.VeilleconcurrentPK;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import static java.util.Locale.ROOT;
import sessions.ArticleFacadeLocal;
import sessions.NotificationsFacadeLocal;
import sessions.VeilleconcurrentFacadeLocal;

/**
 *
 * @author LucienFOTSA
 */
public class WebRobot implements Serializable {

    private ArticleFacadeLocal articleFacade;
    private VeilleconcurrentFacadeLocal veilleconcurrentFacade;
    private NotificationsFacadeLocal notificationsFacade;
    //------------------------
    private String msg = "";
    //-----------------------------------------
    private WebClient webClient;
    private LinkedList<WebWindow> windows = new LinkedList<>();

    /**
     * Creates a new instance of WebRobot
     */
    public WebRobot() {
        //To do code
    }

    public WebRobot(VeilleconcurrentFacadeLocal veilleconcurrentFacade, ArticleFacadeLocal articleFacade, NotificationsFacadeLocal notificationsFacade) {
        this.veilleconcurrentFacade = veilleconcurrentFacade;
        this.articleFacade = articleFacade;
        this.notificationsFacade = notificationsFacade;
    }

    public String watcher(Compte compte, Concurrent concurrent, Article article) {
        String mesg = "";
        try {
            webClient = new WebClient();
            webClient.getOptions().setUseInsecureSSL(true);
            webClient.getOptions().setCssEnabled(true);
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            webClient.getOptions().setPrintContentOnFailingStatusCode(false);
            webClient.getOptions().setPopupBlockerEnabled(false);
            webClient.getOptions().setRedirectEnabled(true);
            webClient.getOptions().setGeolocationEnabled(false);
            webClient.getOptions().setDoNotTrackEnabled(true);
            webClient.getOptions().setTimeout(60000);
            webClient.getOptions().setDownloadImages(false);
            webClient.getCookieManager().setCookiesEnabled(true);
            webClient.setJavaScriptTimeout(60000);
            webClient.waitForBackgroundJavaScript(60000);
            CollectingAlertHandler alertHandler = new CollectingAlertHandler();
            webClient.setAlertHandler(alertHandler);
            //-----------------------------------------------------
            webClient.addWebWindowListener(new WebWindowListener() {
                @Override
                public void webWindowOpened(WebWindowEvent event) {
                    windows.clear();
                    windows.add(event.getWebWindow());
                }

                @Override
                public void webWindowClosed(WebWindowEvent event) {
                }

                @Override
                public void webWindowContentChanged(WebWindowEvent event) {
                }
            });
            //-----------------------------------------------------
            webClient.setPromptHandler(new PromptHandler() {
                @Override
                public String handlePrompt(Page currentPage, String promptText, String defaultValue) {
                    return ("698742341");
                }
            });
            //-----------------------------------------------------
            webClient.setConfirmHandler(new ConfirmHandler() {
                @Override
                public boolean handleConfirm(Page page, String message) {
                    return true;
                }
            });
            //-----------------------------------------------------
            HtmlPage homePage = webClient.getPage(concurrent.getLien());
            Integer trials = 2;
            while (trials > 0 && (homePage.getTitleText().toUpperCase(ROOT).contains("ERROR") || homePage.getTitleText().toUpperCase(ROOT).contains("ERREUR"))) {
                trials--;
                synchronized (trials) {
                    trials.wait(7000);
                }
                homePage = webClient.getPage(concurrent.getLien());
            }
            //-------------------------
            if (homePage.getTitleText().toUpperCase(ROOT).contains("ERROR") || homePage.getTitleText().toUpperCase(ROOT).contains("ERREUR")) {
                mesg = "Echec concurrentLink Unavailable !";
                return mesg;
            }
            Integer tefls = 7;
            while (tefls > 0) {
                tefls--;
                synchronized (tefls) {
                    tefls.wait(1000);
                }
            }
            HtmlTextInput champRecherche = null;
            try {
                champRecherche = homePage.getHtmlElementById("twotabsearchtextbox");
                try {
                    HtmlSubmitInput btnRecherche = homePage.getHtmlElementById("nav-search-submit-button");
                    champRecherche.setAttribute("value", "");
                    champRecherche.type(article.getLibelle());
                    HtmlPage resultPage = btnRecherche.click(false, false, false, true, true, false);
                    Integer trdfls = 5;
                    while (trdfls > 0) {
                        trdfls--;
                        synchronized (trdfls) {
                            trdfls.wait(1000);
                        }
                    }
                    HtmlSpan spanPrice = null;
                    List<HtmlElement> listDe = resultPage.getElementById("search").getElementsByTagName("span");
                    List<HtmlElement> listDeNext = new ArrayList<>();
                    int i = 0;
                    while (i < listDe.size()) {
                        HtmlElement de = listDe.get(i);
                        listDeNext.clear();
                        de.getElementsByTagName("span").forEach((he) -> {
                            listDeNext.add(he);
                        });
                        if (de.getAttribute("class").equals("a-offscreen")) {
                            spanPrice = (HtmlSpan) de;
                            String tempPrix = spanPrice.getTextContent().replace(",", ".").replace("XAF", "").replace("XOF", "").replace("€", "").replace("EUR", "").replace("EURO", "").replace("$", "").replace("USD", "").replace("\n", "").trim();
                            String prix = "";
                            for (int k = 0; k < tempPrix.length(); k++) {
                                if (tempPrix.charAt(k) == '0' || tempPrix.charAt(k) == '1' || tempPrix.charAt(k) == '2' || tempPrix.charAt(k) == '3' || tempPrix.charAt(k) == '4' || tempPrix.charAt(k) == '5' || tempPrix.charAt(k) == '6' || tempPrix.charAt(k) == '7' || tempPrix.charAt(k) == '8' || tempPrix.charAt(k) == '9' || tempPrix.charAt(k) == '.') {
                                    prix += tempPrix.charAt(k);
                                }
                                prix = prix.replace(",", ".");
                            }
                            Float prixConcurrent = (!tempPrix.contains("XAF") ? (Float.parseFloat(prix) * 650) : Float.parseFloat(prix));
                            if (!prixConcurrent.equals(article.getPrixunit())) {
                                String td = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
                                Date debutToday = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss SSS").parse(td + " 00:00:00 000");
                                Date finToday = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss SSS").parse(td + " 23:59:59 999");
                                if (veilleconcurrentFacade.findByIdarticleIdconcurrentDateEnregistre_Etat(article.getIdarticle(), concurrent.getIdconcurrent(), debutToday, finToday, "Actif".toUpperCase(ROOT)).isEmpty()) {
                                    Veilleconcurrent vc = new Veilleconcurrent();
                                    vc.setVeilleconcurrentPK(new VeilleconcurrentPK(concurrent.getIdconcurrent(), article.getIdarticle()));
                                    vc.setArticle(article);
                                    vc.setConcurrent(concurrent);
                                    vc.setPrixunit(prixConcurrent);
                                    vc.setEtat("Actif");
                                    vc.setDateEnregistre(new Date());
                                    vc.setDerniereModif(new Date());
                                    veilleconcurrentFacade.create(vc);
                                    //---------------------
                                    List<Agence> ListAge = new ArrayList<>();
                                    ListAge.add(article.getIdagence());
                                    if (notificationsFacade.findByDateEnregistre_Etat(debutToday, finToday, "Actif".toUpperCase(ROOT), ListAge).isEmpty()) {
                                        String msg = (compte.getLangue().equals("fr") ? ("Hello " + compte.getNomPrenom() + ", Veuillez Réviser vos prix par rapport à ceux de vos concurrent pour un meilleur positionnement. Merci") : ("Hello " + compte.getNomPrenom() + ", Please Review your prices compared to those of your competitors for better positioning. Thanks"));
                                        Notifications notifications = new Notifications();
                                        notifications.setIdCompte(compte);
                                        notifications.setCampagne("PriceWatcher Alert");
                                        notifications.setDernierevu(new Date(System.currentTimeMillis() - 86400000L));
                                        notifications.setContact(compte.getContact());
                                        notifications.setEmail(compte.getEmail());
                                        notifications.setNbrerepeter((short) 1);
                                        notifications.setNbrevu((short) 0);
                                        notifications.setVu(false);
                                        notifications.setEtat("Actif");
                                        notifications.setDateEnregistre(new Date());
                                        notifications.setDerniereModif(new Date());
                                        notifications.setIdagence(article.getIdagence());
                                        notifications.setMessage(msg.replace("\"", ""));
                                        notifications.setMessageen(notifications.getMessage());
                                        notifications.setDelivre(true);
                                        notifications.setLibre(false);
                                        notifications.setIdnotificationsms(notificationsFacade.nextId());
                                        notificationsFacade.create(notifications);
                                    }
                                }
                            }
                            //----------------
                            mesg = "Succès!";
                            break;
                        } else {
                            if (i == (listDe.size() - 1)) {
                                listDe.clear();
                                i = 0;
                                listDe.addAll(listDeNext);
                            } else {
                                i++;
                            }
                        }
                    }
                    if (spanPrice == null) {
                        System.out.println("spanPrice not found !!!");
                    }
                } catch (Exception e) {
                    mesg = "Erreur : " + e.getMessage();
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    champRecherche = homePage.getHtmlElementById("gh-ac");
                    HtmlSubmitInput btnRecherche = homePage.getHtmlElementById("gh-btn");
                    champRecherche.setAttribute("value", "");
                    champRecherche.type(article.getLibelle());
                    HtmlPage resultPage = btnRecherche.click(false, false, false, true, true, false);
                    Integer trdfls = 5;
                    while (trdfls > 0) {
                        trdfls--;
                        synchronized (trdfls) {
                            trdfls.wait(1000);
                        }
                    }
                    HtmlSpan spanPrice = null;
                    List<HtmlElement> listDe = resultPage.getElementById("srp-river-results").getElementsByTagName("span");
                    List<HtmlElement> listDeNext = new ArrayList<>();
                    int i = 0;
                    while (i < listDe.size()) {
                        HtmlElement de = listDe.get(i);
                        listDeNext.clear();
                        de.getElementsByTagName("span").forEach((he) -> {
                            listDeNext.add(he);
                        });
                        if (de.getAttribute("class").equals("s-item__price")) {
                            spanPrice = (HtmlSpan) de;
                            String tempPrix = spanPrice.getTextContent().replace(",", ".").replace("XAF", "").replace("XOF", "").replace("€", "").replace("EUR", "").replace("EURO", "").replace("$", "").replace("USD", "").replace("\n", "").trim();
                            String prix = "";
                            for (int k = 0; k < tempPrix.length(); k++) {
                                if (tempPrix.charAt(k) == '0' || tempPrix.charAt(k) == '1' || tempPrix.charAt(k) == '2' || tempPrix.charAt(k) == '3' || tempPrix.charAt(k) == '4' || tempPrix.charAt(k) == '5' || tempPrix.charAt(k) == '6' || tempPrix.charAt(k) == '7' || tempPrix.charAt(k) == '8' || tempPrix.charAt(k) == '9' || tempPrix.charAt(k) == '.') {
                                    prix += tempPrix.charAt(k);
                                }
                                prix = prix.replace(",", ".");
                            }
                            Float prixConcurrent = (!tempPrix.contains("XAF") ? (Float.parseFloat(prix) * 650) : Float.parseFloat(prix));
                            if (!prixConcurrent.equals(article.getPrixunit())) {
                                String td = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
                                Date debutToday = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss SSS").parse(td + " 00:00:00 000");
                                Date finToday = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss SSS").parse(td + " 23:59:59 999");
                                if (veilleconcurrentFacade.findByIdarticleIdconcurrentDateEnregistre_Etat(article.getIdarticle(), concurrent.getIdconcurrent(), debutToday, finToday, "Actif".toUpperCase(ROOT)).isEmpty()) {
                                    Veilleconcurrent vc = new Veilleconcurrent();
                                    vc.setVeilleconcurrentPK(new VeilleconcurrentPK(concurrent.getIdconcurrent(), article.getIdarticle()));
                                    vc.setArticle(article);
                                    vc.setConcurrent(concurrent);
                                    vc.setPrixunit(prixConcurrent);
                                    vc.setEtat("Actif");
                                    vc.setDateEnregistre(new Date());
                                    vc.setDerniereModif(new Date());
                                    veilleconcurrentFacade.create(vc);
                                    //---------------------
                                    List<Agence> ListAge = new ArrayList<>();
                                    ListAge.add(article.getIdagence());
                                    if (notificationsFacade.findByDateEnregistre_Etat(debutToday, finToday, "Actif".toUpperCase(ROOT), ListAge).isEmpty()) {
                                        String msg = (compte.getLangue().equals("fr") ? ("Hello " + compte.getNomPrenom() + ", Veuillez Réviser vos prix par rapport à ceux de vos concurrent pour un meilleur positionnement. Merci") : ("Hello " + compte.getNomPrenom() + ", Please Review your prices compared to those of your competitors for better positioning. Thanks"));
                                        Notifications notifications = new Notifications();
                                        notifications.setIdCompte(compte);
                                        notifications.setCampagne("PriceWatcher Alert");
                                        notifications.setDernierevu(new Date(System.currentTimeMillis() - 86400000L));
                                        notifications.setContact(compte.getContact());
                                        notifications.setEmail(compte.getEmail());
                                        notifications.setNbrerepeter((short) 1);
                                        notifications.setNbrevu((short) 0);
                                        notifications.setVu(false);
                                        notifications.setEtat("Actif");
                                        notifications.setDateEnregistre(new Date());
                                        notifications.setDerniereModif(new Date());
                                        notifications.setIdagence(article.getIdagence());
                                        notifications.setMessage(msg.replace("\"", ""));
                                        notifications.setMessageen(notifications.getMessage());
                                        notifications.setDelivre(true);
                                        notifications.setLibre(false);
                                        notifications.setIdnotificationsms(notificationsFacade.nextId());
                                        notificationsFacade.create(notifications);
                                    }
                                }
                            }
                            //----------------
                            mesg = "Succès!";
                            break;
                        } else {
                            if (i == (listDe.size() - 1)) {
                                listDe.clear();
                                i = 0;
                                listDe.addAll(listDeNext);
                            } else {
                                i++;
                            }
                        }
                    }
                    mesg = "Price not found !!!";
                } catch (Exception ex) {
                    mesg = "Erreur : " + ex.getMessage();
                    ex.printStackTrace();
                }
            }
        } catch (Exception e) {
            mesg = "Erreur : " + e.getMessage();
            e.printStackTrace();
        }
        return mesg;
    }

    public ArticleFacadeLocal getArticleFacade() {
        return articleFacade;
    }

    public void setArticleFacade(ArticleFacadeLocal articleFacade) {
        this.articleFacade = articleFacade;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
