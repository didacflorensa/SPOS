package com.udl.tfg.sposapp.controllers;

import com.udl.tfg.sposapp.models.Session;
import com.udl.tfg.sposapp.repositories.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.rest.webmvc.convert.StringToDistanceConverter;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RepositoryRestController
public class SessionController {

    @Autowired
    private SessionRepository repository;

    @RequestMapping(value = "/session/{id}", method = RequestMethod.GET)
    public @ResponseBody Session getSession(@PathVariable String id, @RequestParam(value = "key", required = false) String key) throws Exception {
        Session session = repository.findOne(Long.parseLong(id));
        if (session == null)
            throw new NullPointerException();

        if (session.getKey().equals(key)) {
            return session;
        } else {
            throw new InvalidKeyException();
        }
    }

    @RequestMapping(value = "/session", method = RequestMethod.POST)
    @ResponseBody
    public HttpEntity<HashMap<String, String>> returnKey(HttpServletRequest request, @Valid @RequestBody Session session) throws Exception {
        if (session != null){
            session.generateKey();
            repository.save(session);
            HashMap<String, String> response = new HashMap<>();
            response.put("id", String.valueOf(session.getId()));
            response.put("key", session.getKey());
            return ResponseEntity.created(URI.create(request.getRequestURL() + "/" + session.getId())).body(response);
        } else {
            throw new NullPointerException();
        }
    }

    @RequestMapping(value = "/session/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public HttpEntity<Session> updateSession(@PathVariable String id, @Valid @RequestBody Session session, @RequestParam(value = "key", required = false) String key) throws  Exception{
        Session oldSession = repository.findOne(Long.parseLong(id));
        if (oldSession != null){
            if (!oldSession.getKey().equals(key))
                throw new InvalidKeyException();
            oldSession.setEmail(session.getEmail());
            oldSession.setInfo(session.getInfo());
            oldSession.setMaximumDuration(session.getMaximumDuration());
            oldSession.setSessionResults(session.getSessionResults());
            oldSession.setType(session.getType());
            oldSession.setVmConfig(session.getVmConfig());
            repository.save(oldSession);
            return ResponseEntity.ok().body(oldSession);
        } else {
            throw new NullPointerException();
        }
    }

    @RequestMapping(value = "/session/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpEntity<Void> deleteSession(@PathVariable String id, @RequestParam(value = "key", required = false) String key) throws  Exception{
        Session session = repository.findOne(Long.parseLong(id));
        if (session != null){
            if (!session.getKey().equals(key))
                throw new InvalidKeyException();
            repository.delete(Long.parseLong(id));
            return ResponseEntity.ok().build();
        } else {
            throw new NullPointerException();
        }
    }

    @RequestMapping(value = "/session", method = RequestMethod.GET)
    public @ResponseBody HttpEntity<List<Session>> getSessions(HttpServletRequest request, @RequestParam(value = "key", required = false) String key) throws Exception {
        Iterable<Session> sessions = repository.findAll();
        if (sessions == null)
            throw new NullPointerException();

        List<Session> sessionsList = new ArrayList<>();
        for (Session actualSession : sessions) {
            if (actualSession.getKey().equals(key))
                sessionsList.add(actualSession);
        }

        if (sessionsList.size() == 0){
            throw new InvalidKeyException();
        }

        return ResponseEntity.ok().body(sessionsList);
    }
}