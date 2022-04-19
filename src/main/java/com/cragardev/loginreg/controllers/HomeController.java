package com.cragardev.loginreg.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cragardev.loginreg.models.LoginUser;
import com.cragardev.loginreg.models.User;
import com.cragardev.loginreg.services.UserService;



@Controller
public class HomeController {

	//
	// Inject the Services
	//
	private final UserService userService;
	
	//
	// Services Constructors
	//
	
	
	public HomeController(UserService userService) {
		super();
		this.userService = userService;
	}
	

	//
    // ========= /, Landing, home, index page 1 ===========
    //
	@GetMapping("/")
    public String index() {
//		System.out.println("****************************");

        return "index.jsp";
    }
	


	//
	// ========= /, Landing, home, index page 1 ===========
	//
	@GetMapping("/home")
	public String home() {
		
		return "index.jsp";
	}
	
	
	//
	// ========= /, Dashboard page ===========
	//
	@GetMapping("/dashboard")
	public String dashboard(
			Model model,
			HttpSession session,
			RedirectAttributes redirectAttributes) {
		
		// check to see if user is logged in

		if (session.getAttribute("user_id") == null) {
			// if not in session, redirect to login
			return "redirect:/createError";
		}

		// Get users info to show them logged in
		model.addAttribute("user", userService.findUser((Long)session.getAttribute("user_id")));

		
		return "dashboard.jsp";
	}
	
	

	
	//
	// ========= One Details page ===========
	//
	@GetMapping("/oneDetails")
	public String oneDetails() {
		
		return "oneDetails.jsp";
	}
	
	
	// ---------------------------------------- CREATE NEW -----------
	//
	// ========= Create One page ===========
	//
	@GetMapping("/createOne")
	public String createOne() {
		
		return "createOne.jsp";
	}
	
    //
    // ========= Create New PROCESS ===========
    //

	
	
	
	
	// ---------------------------------------- UPDATE  -----------
	//
	// ========= Update One page ===========
	//
	@GetMapping("/updateOne")
	public String updateOne() {
		
		return "updateOne.jsp";
	}

	
	
	//
	// ========= Update PROCESS ===========
	//
	
	
	
	
    
    //
    //================== ERRORS ==========================
    //
    @RequestMapping("/createError")
    public String flashMessages(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "Please log in or Register!");
        return "redirect:/login";
    }
    
    
	
    // ---------------------------------------- DELETE ----------
    //
    // ========= Delete  PROCESS ===========
    //
    @GetMapping("/delete/{id}") // add an Id
    public String deleteCandy(@PathVariable("id") Long id) {
    	
//    	candyService.deleteCandy(id);

        return "redirect:/dashboard";
    }
    
	// **************************************************************************************************************
    //
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^  LOGIN REGISTRATION  ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    //
    // **************************************************************************************************************
    

	@GetMapping("/login")
	public String login(Model model, HttpSession session) {

		// Bind empty User and LoginUser objects to the JSP
		// to capture the form input
		session.invalidate();
		model.addAttribute("newUser", new User());
		model.addAttribute("newLogin", new LoginUser());
		return "login.jsp";
	}

	@PostMapping("/login")
	public String loginProcess(
			@Valid @ModelAttribute("newLogin") LoginUser newLogin,
			BindingResult result,
			Model model,
			HttpSession session) {

		// Add once service is implemented:
		// User user = userServ.login(newLogin, result);
		User user = userService.login(newLogin, result);

		if (result.hasErrors()) {
			model.addAttribute("newUser", new User());
			return "login.jsp";
		}

		// No errors!
		// TO-DO Later: Store their ID from the DB in session,
		// in other words, log them in.
		session.setAttribute("user_id", user.getId());

		return "redirect:/dashboard";
	}

	@PostMapping("/register")
	public String registerProcess(
			@Valid @ModelAttribute("newUser") User newUser,
			BindingResult result,
			Model model,
			HttpSession session) {

		// TO-DO Later -- call a register method in the service
		// to do some extra validations and create a new user!
		userService.register(newUser, result);

		if (result.hasErrors()) {
			// Be sure to send in the empty LoginUser before
			// re-rendering the page.
			model.addAttribute("newLogin", new LoginUser());
			return "login.jsp";
		}

		// No errors!
		// TO-DO Later: Store their ID from the DB in session,
		// in other words, log them in.

		session.setAttribute("user_id", newUser.getId());

		return "redirect:/dashboard";
	}

	// Log out user
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/login";

	}
    
    
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

	
}
