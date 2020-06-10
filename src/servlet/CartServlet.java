package servlet;
import util.AddCartUtil;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/cart",loadOnStartup = 1)
public class CartServlet extends HttpServlet{
    public AddCartUtil addCartUtil = new AddCartUtil();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/json;charset=UTF-8");
        
        String data=addCartUtil.read_cart_json();
        System.out.println("cart_json:"+data);
        response.getWriter().write(data);

    }
}
