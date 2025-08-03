package listener;

import dao.DBseedDAO;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class DBInitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        DBseedDAO.initialize();
    }
}