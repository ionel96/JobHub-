package com.example.portal.controller;

import com.example.portal.entity.Applicants;
import jakarta.persistence.Query;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ApplicantsController {
    private static final Logger logger = LoggerFactory.getLogger(ApplicantsController.class);
    Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
    StandardServiceRegistryBuilder registry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
    ServiceRegistry serviceRegistry = registry.configure("hibernate.cfg.xml").build();
    SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);

    @GetMapping("/announcement/list/job/{id}/apply")
    public String apply() {
        return "list";
    }

    @PostMapping("/announcement/list/job/{id}/apply")
    public String saveApplications(HttpServletRequest request, Model model, @PathVariable(value = "id") int jobId) {
        String currentUser = request.getUserPrincipal().getName();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Applicants applicants = new Applicants(currentUser, jobId);
        Query query = session.createQuery("SELECT COUNT(a) FROM Applicants a WHERE a.applicants_email = :currentUser AND a.job_id = :jobId");
        query.setParameter("currentUser", currentUser);
        query.setParameter("jobId", jobId);
        PortalController p = new PortalController();
        Long count = (Long)query.getSingleResult();
        if (count > 0) {
            model.addAttribute("error", true);
            return "redirect:/announcement/list?error";

        } else {
            session.save(applicants);
        }
        transaction.commit();
        session.close();
        model.addAttribute("success", true);
        return "redirect:/announcement/list?success";
    }
}
