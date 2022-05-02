package de.hsrm.mi.web.projekt.benutzerprofil;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class BenutzerprofilController {
    @GetMapping("/benutzerprofil")
    public String benutzerprofil(@ModelAttribute("profil") BenutzerProfil profil, Model m) {
        profil.setName("Alexandra Müller");

        return "/benutzerprofil/profilansicht";
    }
}
