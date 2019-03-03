package webapp2;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AbiturientsListWithLang
 */
@WebServlet({"/AbiturientsListWithLang"})
public class AbiturientsListWithLang extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ArrayList<Abiturient> abiturients;

    private static String JSONpath = "C:\\Users\\vinog\\IdeaProjects\\lab3\\src\\webapp2\\data.json";


    /**
     * Initialization
     * Getting data from JSON
     *
     * @throws IOException if an I/O error occurs
     */
    public AbiturientsListWithLang() throws IOException {
        this.abiturients = new ArrayList<>();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(JSONpath), "UTF8"));
        JsonObject json = new Gson().fromJson(in, JsonObject.class);

        JsonArray abiturientsJson = json.getAsJsonArray("abiturients");
        for (int i = 0; i < abiturientsJson.size(); i++) {
            Abiturient abiturient = new Gson().fromJson(abiturientsJson.get(i), Abiturient.class);
            this.abiturients.add(abiturient);
        }
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");

        String lang = request.getParameter("lang");
        if (lang == null) {
            response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, "Ожидался параметр lang");
            return;
        }
        if (!"en".equalsIgnoreCase(lang) && !"ru".equalsIgnoreCase(lang)) {
            response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, "Параметр lang может принимать значения en или ru");
            return;
        }
        response.setContentType("text/html;charset=UTF-8");

        ResourceBundle res = ResourceBundle.getBundle("webapp2/abiturients", "en".equalsIgnoreCase(lang) ? Locale.ENGLISH : Locale.getDefault());


        String filter = request.getParameter("filter");

        PrintWriter out = response.getWriter();

        try {
            out.println("<html>");
            out.println("<head><title>" + res.getString("title") + "</title></head>");
            out.println("<body>");
            out.println("<form method='GET' action=AbiturientsListWithLang>");
            out.println("<p>" + res.getString("faculty.filter") + "</p>");
            out.println("<input type='text' name='filter'/>");
            out.println("<button type='submit'>" + res.getString("submit") + "</button>");
            out.println("</form>");
            out.println("<h1>" + res.getString("table.title") + " " + (filter != null ? filter : res.getString("faculty.all")) + "</h1>");
            out.println("<table border='1'>");
            out.println("<tr><td><b>" + res.getString("fio") + "</b></td><td><b>" + res.getString("faculty") + "</b></td>");
            for (Abiturient abiturient : this.abiturients) {
                String fio = abiturient.getFio();
                String faculty = abiturient.getFaculty();
                if (filter != null && filter != "") {
                    if (filter.equals(faculty))
                        out.println("<tr><td>" + fio + "</td><td>" + faculty + "</td></tr>");
                } else out.println("<tr><td>" + fio + "</td><td>" + faculty + "</td></tr>");
            }

            out.println("</table>");
            out.println("<form method='POST' action=AbiturientsListWithLang?save=true>");
            out.println("<p>" + res.getString("abiturient.add") + "</p>");
            out.println("<span>" + res.getString("abiturient.lastname") + "</span>");
            out.println("<input type='text' name='lastName'/>");
            out.println("<span>" + res.getString("abiturient.firstname") + "</span>");
            out.println("<input type='text' name='firstName'/>");
            out.println("<span>" + res.getString("abiturient.middlename") + "</span>");
            out.println("<input type='text' name='middleName'/>");
            out.println("<span>" + res.getString("faculty") + "</span>");
            out.println("<input type='text' name='faculty'/>");
            out.println("<button type='submit'>" + res.getString("save") + "</button>");
            out.println("</form>");
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }

    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        this.processRequest(request, response);
    }


    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        if (request.getParameter("save") != null && request.getParameter("save") != "") {
            Integer newId = this.abiturients.size();
            String lastName = request.getParameter("lastName");
            String firstName = request.getParameter("firstName");
            String middleName = request.getParameter("middleName");
            String faculty = request.getParameter("faculty");

            this.abiturients.add(new Abiturient(newId, lastName, firstName, middleName, faculty));
            Writer writer = new OutputStreamWriter(new FileOutputStream(JSONpath), "UTF8");
            try {
                Gson gson = new Gson();
                JsonObject json = new JsonObject();
                json.add("abiturients", gson.toJsonTree(this.abiturients));
                System.out.println();
                gson.toJson(json, writer);
            } finally {
                writer.close();
            }
        }
        this.processRequest(request, response);
    }
}
