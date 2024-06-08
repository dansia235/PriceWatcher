/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mb;

import entities.Agence;
import entities.Article;
import entities.Compte;
import entities.Concurrent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static java.util.Locale.ROOT;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import sessions.AgenceFacadeLocal;
import sessions.ArticleFacadeLocal;
import sessions.ConcurrentFacadeLocal;
import sessions.NotificationsFacadeLocal;
import sessions.VeilleconcurrentFacadeLocal;

/**
 *
 * @author LucienFOTSA
 */
public class SystemMB {

    @EJB
    private ArticleFacadeLocal articleFacade;
    @EJB
    private VeilleconcurrentFacadeLocal veilleconcurrentFacade;
    @EJB
    private ConcurrentFacadeLocal concurrentFacade;
    @EJB
    private NotificationsFacadeLocal notificationsFacade;
    @EJB
    private AgenceFacadeLocal agenceFacade;
    //-----------------------------------------
    public static volatile Boolean threadsRuning = false;
    private Thread doVeille_thread;
    private Compte connectedUser;

    /**
     * Creates a new instance of SystemMB
     */
    public SystemMB() {
    }

    @PostConstruct
    private void init() {
        connectedUser = (Compte) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUser");
    }

    public void triggerSystemMB() {
        System.out.println("triggerSystemMB : " + new Date());
        try {
            if (!threadsRuning) {
                doVeille();
                //---------
                threadsRuning = true;
            } else {
                if (!doVeille_thread.isAlive()) {
                    doVeille();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doVeille() {
        doVeille_thread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        System.out.println("Thread doVeille started!");
                        List<Agence> ListAge = new ArrayList<>();
                        ListAge.add(connectedUser.getIdagence());
                        List<Article> listArticle = articleFacade.findAll_Etat("Actif".toUpperCase(ROOT), ListAge);
                        List<Concurrent> listConcurrent = concurrentFacade.findAll_Etat("Actif".toUpperCase(ROOT), ListAge);
                        for (Concurrent c : listConcurrent) {
                            for (Article a : listArticle) {
                                try {
                                    WebRobot wr = new WebRobot(veilleconcurrentFacade, articleFacade, notificationsFacade);
                                    wr.watcher(connectedUser, c, a);
                                    //---
                                    Integer tryls = 1;
                                    while (tryls > 0) {
                                        tryls--;
                                        synchronized (tryls) {
                                            tryls.wait(300000);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    continue;
                                }
                            }
                        }
                        System.out.println("Thread doVeille stoped due to end!");
                        //---
                        Integer tryls = 1;
                        while (tryls > 0) {
                            tryls--;
                            synchronized (tryls) {
                                tryls.wait(46800000);
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Thread doVeille stoped due to exception!");
                        e.printStackTrace();
                    }
                }
            }
        };
        doVeille_thread.start();
    }

    public AgenceFacadeLocal getAgenceFacade() {
        return agenceFacade;
    }

    public void setAgenceFacade(AgenceFacadeLocal agenceFacade) {
        this.agenceFacade = agenceFacade;
    }

    public ArticleFacadeLocal getArticleFacade() {
        return articleFacade;
    }

    public void setArticleFacade(ArticleFacadeLocal articleFacade) {
        this.articleFacade = articleFacade;
    }

}
