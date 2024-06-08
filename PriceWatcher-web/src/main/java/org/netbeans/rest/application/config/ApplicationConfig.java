/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.rest.application.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.core.Application;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

/**
 *
 * @author LucienFOTSA
 */
@javax.ws.rs.ApplicationPath("webservices")
public class ApplicationConfig extends Application {

    @Override
    public Set<Object> getSingletons() {
        Set<Object> singletons = new java.util.HashSet<>();
        singletons.add(new MultiPartFeature());
        return singletons;
    }

    @Override
    public Map<String, Object> getProperties() {
        final Map<String, Object> properties = new HashMap<>();
        properties.put("jersey.config.server.provider.packages", "webServices");
        return properties;
    }

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method. It is automatically
     * populated with all resources defined in the project. If required, comment
     * out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(webServices.AgenceFacadeREST.class);
        resources.add(webServices.CompteFacadeREST.class);
        resources.add(webServices.MultimediaFacadeREST.class);
    }

}
