package controller;

import model.User;
import service.ManagerService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/manager/dashboard")
public class ManagerController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ManagerService managerService;

    @Override
    public void init() {
        managerService = new ManagerService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User loggedUser = (session != null) ? (User) session.getAttribute("loggedUser") : null;

        if (loggedUser == null || !"MANAGER".equalsIgnoreCase(loggedUser.getRole())) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        try {
            req.setAttribute("pendingUsers", managerService.getPendingUsers());

            // Pass any messages from session and remove them
            String message = (String) session.getAttribute("message");
            if (message != null) {
                req.setAttribute("message", message);
                session.removeAttribute("message");
            }

            String errorMessage = (String) session.getAttribute("errorMessage");
            if (errorMessage != null) {
                req.setAttribute("errorMessage", errorMessage);
                session.removeAttribute("errorMessage");
            }

            req.getRequestDispatcher("/managerDashboard.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
