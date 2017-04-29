package controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by pic on 29/04/2017.
 */
@Controller
public class MainController {

    @RequestMapping({"/","/home"})
    public String goIndex(Model model,
                          HttpSession session,
                          HttpServletRequest request,
                          HttpServletResponse response)
    {
        if(null!=session.getAttribute("testDada"))
        {
            String val = session.getAttribute("testDada").toString();
            if(null == val)
                val = "0";
            session.setAttribute("testDada", Integer.parseInt(val)+1 );

        }
        else
        {
            session.setAttribute("testDada", 1 );
        }
        model.addAttribute("test", session.getAttribute("testDada"));
        return "home";
    }
}
