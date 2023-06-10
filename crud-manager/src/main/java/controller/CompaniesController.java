package controller;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Company;
import model.ModelException;
import model.Post;
import model.User;
import model.dao.CompanyDAO;
import model.dao.DAOFactory;
import model.dao.PostDAO;

@WebServlet(urlPatterns = {"/companies", "/company/form", "/company/insert", "/company/delete", "/company/update"})
public class CompaniesController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String action = req.getRequestURI();
		//retorna: "crud-manager/company/form"

		switch (action) {
		case "/crud-manager/company/form": {
			//PrintWriter p = resp.getWriter();
			//p.print("mapa ok");

			CommonsController.listUsers(req);
			//
			req.setAttribute("action", "insert");
			//forward: eh interno: nao tem navegador
			ControllerUtil.forward(req, resp, "/form-company.jsp");		
			break;
		}
		case "/crud-manager/company/update": {
			
			String idStr = req.getParameter("companyId");
			int idCompany = Integer.parseInt(idStr);
			
			CompanyDAO dao = DAOFactory.createDAO(CompanyDAO.class);
			Company company = null;
			
			try {
				company = dao.findById(idCompany);
			} catch (ModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			CommonsController.listUsers(req);
			req.setAttribute("action", "update");
			req.setAttribute("company", company);
			
			ControllerUtil.forward(req, resp, "/form-company.jsp");		
			
			break;
		}
		default:
			listCompanies(req);

			ControllerUtil.transferSessionMessagesToRequest(req);

			ControllerUtil.forward(req, resp, "/companies.jsp");
		}
	}

	private void listCompanies(HttpServletRequest req) {
		// TODO Auto-generated method stub
		CompanyDAO dao = DAOFactory.createDAO(CompanyDAO.class);

		List<Company> companies = null;
		try {
			companies = dao.listAll();
		} catch (ModelException e) {
			// Log no servidor
			e.printStackTrace();
		}

		if (companies != null)
			req.setAttribute("companies", companies);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub

		String action = req.getRequestURI();


		switch (action) {
		case "/crud-manager/company/delete":

			deleteCompany(req, resp);
			break;
		case "/crud-manager/company/insert": {

			insertCompany(req, resp);
			break;
		}
		case "/crud-manager/company/update": {
			updateCompany(req, resp);
			break;
		}
		default:
			System.out.println("URL inválida " + action);
		}
		//redireciona a pagina
		ControllerUtil.redirect(resp, req.getContextPath()+"/companies");
	}

	private void updateCompany(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		
		String companyIdStr = req.getParameter("companyId");
		String companyName = req.getParameter("name");
		String role = req.getParameter("role");
		String start = req.getParameter("start");
		String end = req.getParameter("end");
		Integer userId = Integer.parseInt(req.getParameter("user"));

		Company company = new Company(Integer.parseInt(companyIdStr));
		company.setName(companyName);
		company.setRole(role);
		company.setStart(ControllerUtil.formatDate(start));
		company.setEnd(ControllerUtil.formatDate(end));
		company.setUser(new User(userId));
		
		
		CompanyDAO dao = DAOFactory.createDAO(CompanyDAO.class);
		
		try {
			if (dao.update(company)) {
				ControllerUtil.sucessMessage(req, "Empresa '" + company.getName() + "' atualizada com sucesso.");
			}
			else {
				ControllerUtil.errorMessage(req, "Empresa '" + company.getName() + "' não pode ser atualizada.");
			}				
		} catch (ModelException e) {
			// log no servidor
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
		
	}

	private void deleteCompany(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub

		String companyIdParameter = req.getParameter("id");

		int companyId = Integer.parseInt(companyIdParameter);

		CompanyDAO dao = DAOFactory.createDAO(CompanyDAO.class);
		
		try {
			Company company = dao.findById(companyId);
			
			if (company == null)
				throw new ModelException("Empresa não encontrado para deleçãa.");
			
			if (dao.delete(company)) {
				ControllerUtil.sucessMessage(req, "Empresa '" + company.getName() + "' deletado com sucesso.");
			}
			else {
				ControllerUtil.errorMessage(req, "Empresa '" + company.getName() + "' não pode ser deletada. Há dados relacionados a empresa.");
			}
		} catch (ModelException e) {
			// TODO Auto-generated catch block
			if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
				ControllerUtil.errorMessage(req, e.getMessage());
			}
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}

	}

	private void insertCompany(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		//pega dados do form
		String companyName = req.getParameter("name");
		String role = req.getParameter("role");
		String start = req.getParameter("start");
		String end = req.getParameter("end");
		//user: nome do select
		Integer userId = Integer.parseInt(req.getParameter("user"));

		Company comp = new Company();
		comp.setName(companyName);
		//comp.setRole("nos");
		comp.setRole(role);
		comp.setStart(ControllerUtil.formatDate(start));
		comp.setEnd(ControllerUtil.formatDate(end));
		comp.setUser(new User(userId));
		//persistencia
		CompanyDAO dao = DAOFactory.createDAO(CompanyDAO.class);

		try {
			if (dao.save(comp)) {
				ControllerUtil.sucessMessage(req, "Empresa '" + comp.getName() + "' salvo com sucesso.");
			}
			else {
				ControllerUtil.errorMessage(req, "Empresa '" + comp.getName() + "' não pode ser salvo.");
			}
		} catch (ModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
	}
}