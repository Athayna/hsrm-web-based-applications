package de.hsrm.mi.web.projekt.projektuser;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProjektUserController {
    @Autowired
    ProjektUserService pService;

    @ModelAttribute("user")
    public void initProfil(Model m) {
        ProjektUser user = new ProjektUser();
        m.addAttribute("user", user);
    }

    @GetMapping("/registrieren")
    public String registration() {
        return "/projektuser/registrieren";
    }

    @PostMapping("/registrieren")
    public String anlegen(Model m, @Valid @ModelAttribute("user") ProjektUser user, BindingResult result) {
        if (result.hasErrors())
            return "/projektuser/registrieren";
        ProjektUser usr;
        try {
            usr = pService.neuenBenutzerAnlegen(user.getUsername(), user.getPassword(), user.getRole());
        } catch (ProjektUserServiceException e) {
            return "/projektuser/registrieren";
        }
        m.addAttribute("user", usr);
        return "redirect:/benutzerprofil";
    }
}
