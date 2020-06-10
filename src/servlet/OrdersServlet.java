package servlet;
import util.AddCartUtil;
import util.LoginUtil;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/order",loadOnStartup = 1)
public class OrdersServlet extends HttpServlet{
    public AddCartUtil addCartUtil = new AddCartUtil();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String order_id = request.getParameter("order_id");
        String order_time= request.getParameter("order_time");
        String order_detail = request.getParameter("order_detail ");
        String order_subtotal=request.getParameter("order_subtotal");
        response.setContentType("text/json;charset=UTF-8");

        addCartUtil.order(order_id,order_time,order_detail,order_subtotal);
//        response.getWriter().write(data);

    }
}
