package servlet;
import util.AddCartUtil;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/test",loadOnStartup = 1)
public class TestServlet extends HttpServlet{
	public AddCartUtil addCartUtil = new AddCartUtil();
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String thumbnail = request.getParameter("thumbnail");
		String title = request.getParameter("title");
		String price = request.getParameter("price");
		String quantity=request.getParameter("quantity");
		String subtotal=request.getParameter("subtotal");
		response.setContentType("text/json;charset=UTF-8");

		System.out.println("get_urls:"+title+price+quantity+subtotal);

		boolean add=addCartUtil.add_cart(thumbnail,title,price,quantity,subtotal);
		if(add){
			response.getWriter().write("add to cart success");
			System.out.println("add to cart success");
		}
	}
}
