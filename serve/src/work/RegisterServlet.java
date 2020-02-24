package work;

import java.io.UnsupportedEncodingException;

import core.Request;
import core.Response;
import core.Utils;
import core.servlet;

public class RegisterServlet implements servlet {

	@Override
	public void serve(Request request, Response response) {
		if(request.getUrl().equals("r")) {
			String name = request.findParameter("uname");
			name = Utils.decode(name,"GBK");
			String[] favs = request.findParameterList("fav");
			
			response.println("<html>")
					.println("<head>")
					.println("<meta charset = \"GBK\">")
					.println("<title>聪明的王二狗的首页</title>")
					.println("</head>")
					.println("<body>")
					.print("<h1>喜欢吃");
			for(int i = 0;i < favs.length;i++) {
				response.print(Utils.decode(favs[i],"GBK"));
				if(i != favs.length - 1) response.print(",");
			}
			response.print("的" + name + "," + "欢迎来到聪明的王二狗的首页~~</h1>")
					.print("</body>")
					.print("</html>");


		}else {
			response.print(Utils.getHtml("html/reg.html"));
		}
	}
	
}
