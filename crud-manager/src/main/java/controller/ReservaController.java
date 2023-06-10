package controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Reserva;
import model.User;
import model.ModelException;
import model.dao.DAOFactory;
import model.dao.ReservaDAO;

@WebServlet(urlPatterns = {"/reservas", "/reserva/form", "/reserva/insert", "/reserva/delete", "/reserva/update"})
public class ReservaController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getRequestURI();

        switch (action) {
            case "/crud-manager/reserva/form": {
                req.setAttribute("action", "insert");
                ControllerUtil.forward(req, resp, "/form-reserva.jsp");
                break;
            }
            case "/crud-manager/reserva/update": {
                String idStr = req.getParameter("reservaId");
                int idReserva = Integer.parseInt(idStr);

                ReservaDAO dao = DAOFactory.createDAO(ReservaDAO.class);
                Reserva reserva = null;

                try {
                    reserva = dao.findById(idReserva);
                } catch (ModelException e) {
                    e.printStackTrace();
                }

                req.setAttribute("action", "update");
                req.setAttribute("reserva", reserva);

                ControllerUtil.forward(req, resp, "/form-reserva.jsp");

                break;
            }
            default:
                listReservas(req);

                ControllerUtil.transferSessionMessagesToRequest(req);

                ControllerUtil.forward(req, resp, "/reservas.jsp");
        }
    }

    private void listReservas(HttpServletRequest req) {
        ReservaDAO dao = DAOFactory.createDAO(ReservaDAO.class);

        List<Reserva> reservas = null;
        try {
            reservas = dao.listAll();
        } catch (ModelException e) {
            e.printStackTrace();
        }

        if (reservas != null)
            req.setAttribute("reservas", reservas);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getRequestURI();

        switch (action) {
            case "/crud-manager/reserva/delete":
                deleteReserva(req, resp);
                break;
            case "/crud-manager/reserva/insert":
                insertReserva(req, resp);
                break;
            case "/crud-manager/reserva/update":
                updateReserva(req, resp);
                break;
            default:
                System.out.println("URL inválida " + action);
        }

        ControllerUtil.redirect(resp, req.getContextPath() + "/reservas");
    }

    private void updateReserva(HttpServletRequest req, HttpServletResponse resp) {
        String reservaIdStr = req.getParameter("reservaId");
        int reservaId = Integer.parseInt(reservaIdStr);

        ReservaDAO dao = DAOFactory.createDAO(ReservaDAO.class);

        try {
            Reserva reserva = dao.findById(reservaId);

            if (reserva != null) {
                // Preencher os campos da reserva com os dados do formulário
                // Exemplo:
                // reserva.setCampo1(req.getParameter("campo1"));
                // reserva.setCampo2(req.getParameter("campo2"));

                if (dao.update(reserva)) {
                    ControllerUtil.sucessMessage(req, "Reserva atualizada com sucesso.");
                } else {
                    ControllerUtil.errorMessage(req, "Não foi possível atualizar a reserva.");
                }
            } else {
                ControllerUtil.errorMessage(req, "Reserva não encontrada.");
            }
        } catch (ModelException e) {
            e.printStackTrace();
            ControllerUtil.errorMessage(req, e.getMessage());
        }
    }

    private void deleteReserva(HttpServletRequest req, HttpServletResponse resp) {
        String reservaIdStr = req.getParameter("reservaId");
        int reservaId = Integer.parseInt(reservaIdStr);

        ReservaDAO dao = DAOFactory.createDAO(ReservaDAO.class);

        try {
            Reserva reserva = dao.findById(reservaId);

            if (reserva != null) {
                if (dao.delete(reserva)) {
                    ControllerUtil.sucessMessage(req, "Reserva excluída com sucesso.");
                } else {
                    ControllerUtil.errorMessage(req, "Não foi possível excluir a reserva.");
                }
            } else {
                ControllerUtil.errorMessage(req, "Reserva não encontrada.");
            }
        } catch (ModelException e) {
            e.printStackTrace();
            ControllerUtil.errorMessage(req, e.getMessage());
        }
    }

    private void insertReserva(HttpServletRequest req, HttpServletResponse resp) {
        ReservaDAO dao = DAOFactory.createDAO(ReservaDAO.class);

        try {
            Reserva reserva = new Reserva(0);

            // Preencha os campos restantes da reserva com os dados do formulário
            int pessoa = Integer.parseInt(req.getParameter("pessoa"));
            int userId = Integer.parseInt(req.getParameter("userId"));
            String description = req.getParameter("description");
            Date date = ControllerUtil.formatDate(req.getParameter("date"));

           
            reserva.setUser(new User(userId));
            reserva.setDate(date);
            reserva.setDescription(description);

            if (dao.save(reserva)) {
                ControllerUtil.sucessMessage(req, "Reserva criada com sucesso.");
            } else {
                ControllerUtil.errorMessage(req, "Não foi possível criar a reserva.");
            }
        } catch (ModelException e) {
            e.printStackTrace();
            ControllerUtil.errorMessage(req, e.getMessage());
        }
    }
}







