/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.apertum.qsky.servlets;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Query;
import org.hibernate.Session;
import ru.apertum.qsky.common.GsonPool;
import ru.apertum.qsky.common.Uses;
import ru.apertum.qsky.controller.PagerAlreadyDone;
import ru.apertum.qsky.ejb.IHibernateEJBLocal;
import ru.apertum.qsky.model.pager.PagerData;
import ru.apertum.qsky.model.pager.PagerResults;

/**
 *
 * @author Evgeniy Egorov
 */
public class GetPagerData extends HttpServlet {

    @EJB(name = "qskyapi/HibernateEJB", mappedName = "ejb/qskyapi/hibernate_session_factory")
    private IHibernateEJBLocal hib;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/json;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */

            final Gson g = GsonPool.getInstance().borrowGson();
            try {
                final Session ses = hib.cs();
                ses.beginTransaction();
                final Query query = ses.getNamedQuery("PagerData.findByActive");
                query.setBoolean("active", true);
                final List data = query.list();
                final ArrayList<PagerData> forDell = new ArrayList<>();
                for (Object o : data) {
                    final PagerData p = (PagerData) o;
                    if (p.getDataType() != 0 && PagerAlreadyDone.getInstance().check(request.getRemoteAddr(), p.getId())) {
                        forDell.add(p);
                    }
                }
                data.removeAll(forDell);
                final String s = g.toJson(new Answer(data, System.getProperty("QSKY_QSYS_VER")));
                out.print(s);
                final String black = System.getProperty("QSKY_IGNOR_ADDR", "");
                final String adr = request.getRemoteAddr();
                boolean f = true;
                for (String bb : black.split(";")) {
                    f = !adr.startsWith(bb);
                    if (!f) {
                        break;
                    }
                }
                if (f) {
                    final PagerResults pagerResults = new PagerResults(request.getRemoteAddr(), Uses.getNow(), request.getParameter("qsysver"));
                    ses.save(pagerResults);
                    hib.cs().getTransaction().commit();
                } else {
                    hib.cs().getTransaction().rollback();
                }
            } finally {
                GsonPool.getInstance().returnGson(g);
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    public static class Answer {

        public Answer(List data, String currVersion) {
            this.data = data;
            this.currVersion = currVersion;
        }
        @Expose
        @SerializedName("curr_version")
        private String currVersion;

        public String getCurrVersion() {
            return currVersion;
        }

        public void setCurrVersion(String currVersion) {
            this.currVersion = currVersion;
        }
        @Expose
        @SerializedName("data")
        private List data;

        public List getData() {
            return data;
        }

        public void setData(List data) {
            this.data = data;
        }
    }
}
