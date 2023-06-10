package model.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.ModelException;
import model.Reserva;
import model.User;

public class MySQLReservaDAO implements ReservaDAO {

    @Override
    public boolean save(Reserva reserva) throws ModelException {
        DBHandler db = new DBHandler();

        String sqlInsert = "INSERT INTO reservas (user_id, date, description) VALUES (?, ?, ?);";

        db.prepareStatement(sqlInsert);

        db.setInt(1, reserva.getUser().getId());
        db.setDate(2, reserva.getDate() == null ? new Date() : reserva.getDate());
        db.setString(3, reserva.getDescription());

        return db.executeUpdate() > 0;
    }

    @Override
    public boolean update(Reserva reserva) throws ModelException {
        DBHandler db = new DBHandler();

        String sqlUpdate = "UPDATE reservas "
                + "SET user_id = ?, "
                + "date = ?, "
                + "description = ? "
                + "WHERE id = ?;";

        db.prepareStatement(sqlUpdate);

        db.setInt(1, reserva.getUser().getId());
        db.setDate(2, reserva.getDate() == null ? new Date() : reserva.getDate());
        db.setString(3, reserva.getDescription());
        db.setInt(4, reserva.getId());

        return db.executeUpdate() > 0;
    }

    @Override
    public boolean delete(Reserva reserva) throws ModelException {
        DBHandler db = new DBHandler();

        String sqlDelete = "DELETE FROM reservas "
                + "WHERE id = ?;";

        db.prepareStatement(sqlDelete);
        db.setInt(1, reserva.getId());

        return db.executeUpdate() > 0;
    }

    @Override
    public List<Reserva> listAll() throws ModelException {
        DBHandler db = new DBHandler();

        List<Reserva> reservas = new ArrayList<Reserva>();

        String sqlQuery = "SELECT r.id as reserva_id, r.*, u.* "
                + "FROM reservas r "
                + "INNER JOIN users u "
                + "ON r.user_id = u.id;";

        db.createStatement();

        db.executeQuery(sqlQuery);

        while (db.next()) {
            User user = new User(db.getInt("user_id"));
            user.setName(db.getString("nome"));
            user.setGender(db.getString("sexo"));
            user.setEmail(db.getString("email"));

            Reserva reserva = new Reserva(db.getInt("reserva_id"));
            reserva.setUser(user);
            reserva.setDate(db.getDate("date")); // Substitua "date_column" pelo nome correto da coluna no banco de dados
            reserva.setDescription(db.getString("description"));

            reservas.add(reserva);
        }

        return reservas;
    }

    @Override
    public Reserva findById(int id) throws ModelException {
        DBHandler db = new DBHandler();

        String sql = "SELECT * FROM reservas WHERE id = ?;";

        db.prepareStatement(sql);
        db.setInt(1, id);
        db.executeQuery();

        Reserva r = null;
        while (db.next()) {
            r = new Reserva(id);
            r.setDate(db.getDate("date"));
            r.setDescription(db.getString("description"));

            UserDAO userDAO = DAOFactory.createDAO(UserDAO.class);
            User user = userDAO.findById(db.getInt("user_id"));
            r.setUser(user);

            break;
        }

        return r;
    }
}
