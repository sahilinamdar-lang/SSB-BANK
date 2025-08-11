package controller;

import model.User;
import service.ManagerService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/manager/ApproveUserServlet")
public class ApproveUserServlet extends HttpServlet {

    private ManagerService managerService;

    @Override
    public void init() {
        managerService = new ManagerService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("loggedUser") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        User manager = (User) session.getAttribute("loggedUser");

        if (!"MANAGER".equalsIgnoreCase(manager.getRole())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return;
        }

        try {
            int userId = Integer.parseInt(req.getParameter("userId"));
            String action = req.getParameter("action");
            boolean result = false;

            if ("approve".equalsIgnoreCase(action)) {
                result = managerService.approveUser(userId, manager.getUserId());
            } else if ("reject".equalsIgnoreCase(action)) {
                result = managerService.rejectUser(userId, manager.getUserId());
            }

            if (result) {
                session.setAttribute("message", "User " + action + "d successfully.");
            } else {
                session.setAttribute("errorMessage", "Failed to " + action + " user.");
            }

            resp.sendRedirect(req.getContextPath() + "/manager/dashboard");

        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Invalid user ID.");
            resp.sendRedirect(req.getContextPath() + "/manager/dashboard");
        } catch (Exception e) {
            throw new ServletException("Error while approving/rejecting user", e);
        }
    }
}
