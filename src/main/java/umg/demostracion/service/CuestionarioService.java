//package umg.demostracion.service;
//
//
//import umg.demostracion.dao.CuestionarioDao;
//import umg.demostracion.db.DatabaseConnection;
//import umg.demostracion.db.TransactionManager;
//import umg.demostracion.model.Cuestionario;
//
//import java.sql.Connection;
//import java.sql.SQLException;
//
//public class CuestionarioService {
//    private CuestionarioDao cuestionarioDao = new CuestionarioDao();
//
//    public void crearUsuario(Cuestionario cuestionario) throws SQLException {
//        try (Connection connection = DatabaseConnection.getConnection()) {
//            TransactionManager tm = new TransactionManager(connection);
//            tm.beginTransaction();
//            try {
//                cuestionarioDao.insertUser(cuestionario);
//                tm.commit();
//            } catch (SQLException e) {
//                tm.rollback();
//                throw e;
//            }
//        }
//    }
//}
