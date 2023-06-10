<jsp:directive.page contentType="text/html; charset=UTF-8" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<%@include file="base-head.jsp"%>
</head>
<body>
	<%@include file="nav-menu.jsp"%>

	<div id="container" class="container-fluid">

		<h3 class="page-header">${not empty reserva ? 'Atualizar' : 'Adicionar'} Reserva</h3>

		<form action="${pageContext.request.contextPath}/reserva/${action}"
			method="POST">
			
			<input type="hidden" value="${reserva.getId()}" name="reservaId">
				
			<div class="row">
				<div class="form-group col-md-6">
					<label for="pessoa">Pessoa</label> 
					<input type="text"
						class="form-control" id="pessoa" name="pessoa" autofocus="autofocus"
						placeholder="Nome da pessoa" required
						oninvalid="this.setCustomValidity('Por favor, informe o nome da pessoa.')"
						oninput="setCustomValidity('')"
						value="${reserva.getPessoa()}">
				</div>

				<div class="form-group col-md-6">
					<label for="userId">Usuário</label> 
					<select id="userId"
						class="form-control selectpicker" name="userId" required
						oninvalid="this.setCustomValidity('Por favor, selecione um usuário.')"
						oninput="setCustomValidity('')">
						<option value="" ${not empty reserva ? "" : 'selected'}>Selecione um usuário
						</option>
						<c:forEach var="user" items="${users}">
							<option value="${user.getId()}" ${reserva.getUser().getId() == user.getId() ? 'selected' : ''}>
								${user.getName()}
							</option>
						</c:forEach>
					</select>
				</div>
			</div>

			<div class="row">
				<div class="form-group col-md-6">
					<label for="description">Descrição</label> 
					<input type="text"
						class="form-control" id="description" name="description" autofocus="autofocus"
						placeholder="Descrição da reserva" required
						oninvalid="this.setCustomValidity('Por favor, informe a descrição da reserva.')"
						oninput="setCustomValidity('')"
						value="${reserva.getDescription()}">
				</div>

				<div class="form-group col-md-6">
					<label for="date">Data</label> 
					<input type="date"
						class="form-control" id="date" name="date" autofocus="autofocus"
						placeholder="Data da reserva" required
						oninvalid="this.setCustomValidity('Por favor, informe a data da reserva.')"
						oninput="setCustomValidity('')"
						value="${reserva.getDate()}">
				</div>
			</div>
				
			<hr />
			<div id="actions" class="row pull-right">
				<div class="col-md-12">
					<a href="${pageContext.request.contextPath}/reservas" class="btn btn-default">Cancelar</a>
					<button type="submit" class="btn btn-primary">
						${not empty reserva ? 'Atualizar' : 'Adicionar'} Reserva
					</button>
				</div>
			</div>
		</form>
	</div>

</body>
</html>
