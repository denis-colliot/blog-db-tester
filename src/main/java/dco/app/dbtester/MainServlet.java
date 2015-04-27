package dco.app.dbtester;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

/**
 * Created on 27/04/15.
 *
 * @author Denis Colliot (denis.colliot@zenika.com)
 */
public class MainServlet extends HttpServlet {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // --
        // Driver loading.
        // --

        System.out.println("Loading MySQL driver...");

        try {

            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("MySQL driver loaded.");

        } catch (final Exception e) {
            e.printStackTrace(System.err);
            resp.sendError(500, "Error while loading MySQL driver class.");
        }

        // --
        // Connection initialization.
        // --

        System.out.println("Initializing MySQL connection.");

        final String host = System.getenv().get("CU_DATABASE_DNS_1");
        final String username = System.getenv().get("CU_DATABASE_USER_1");
        final String password = System.getenv().get("CU_DATABASE_PASSWORD_1");
        final String dbName = System.getenv().get("CU_DATABASE_NAME");

        final String url = "jdbc:mysql://" + host + ":3306/" + dbName;

        System.out.println("MySQL connection URL: '" + url + "'.");

        try (final Connection connection = DriverManager.getConnection(url, username, password)) {

            final ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM t_post_po");
            while (resultSet.next()) {
                final Long id = resultSet.getLong("po_id");
                final String subject = resultSet.getString("po_subject");
                final String content = resultSet.getString("po_content");
                System.out.println("Post item: { id: " + id + " ; subject: '" + subject + "' ; content: '" + content + "' }");
            }

        } catch (final Exception e) {
            e.printStackTrace(System.err);
            resp.sendError(500, "Error while initializing connection.");
        }
    }

}
