package work;

import java.io.IOException;

import core.Request;
import core.Response;
import core.Utils;
import core.servlet;

public class LoginServlet implements servlet {
	
	@Override
	public void serve(Request request, Response response) {
		if(request.getUrl().equals("g")) {
			String name = request.findParameter("uname");
			name = Utils.decode(name, "GBK");
			response.println("<html>")
					.println("<head>")
					.println("<meta charset = \"GBK\">")
					.println("<title>聪明的王二狗的首页</title>")
					.println("</head>")
					.println("<body>")
					.print("<h1>亲爱的")
					.print("的" + name + "," + "欢迎来到聪明的王二狗的首页~~</h1>")
					.print("</body>")
					.print("</html>");
		}else {
			response.print(Utils.getHtml("html/login.html"));
		}
	}

}
