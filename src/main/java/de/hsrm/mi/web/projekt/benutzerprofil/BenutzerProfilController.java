package de.hsrm.mi.web.projekt.benutzerprofil;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BenutzerProfilController {
    @GetMapping("/benutzerprofil")
    public String benutzerprofil(Model m) {
        return "/benutzerprofil/profilansicht";
    }
}
