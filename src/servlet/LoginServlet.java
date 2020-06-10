package servlet;
import util.AddCartUtil;
import util.LoginUtil;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/login",loadOnStartup = 1)
public class LoginServlet extends HttpServlet{
    public LoginUtil loginUtil = new LoginUtil();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        response.setContentType("text/json;charset=UTF-8");

        String data=loginUtil.login(email,password);
        System.out.println("login_data:"+data);
        response.getWriter().write(data);

    }
}
