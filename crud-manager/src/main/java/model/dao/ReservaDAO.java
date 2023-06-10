package model.dao;

import java.util.List;
import model.Reserva;
import model.ModelException;

public interface ReservaDAO {
    boolean save(Reserva reserva) throws ModelException;
    boolean update(Reserva reserva) throws ModelException;
    boolean delete(Reserva reserva) throws ModelException;
    Reserva findById(int id) throws ModelException;
    List<Reserva> listAll() throws ModelException;
}
