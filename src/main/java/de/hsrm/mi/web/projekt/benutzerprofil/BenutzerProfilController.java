package de.hsrm.mi.web.projekt.benutzerprofil;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes(names = {"profil"})
public class BenutzerprofilController {
    @ModelAttribute("profil")
    public void initProfil(Model m) {
        BenutzerProfil p = new BenutzerProfil();
        p.setName("Alexandra Müller");
        p.setGeburtsdatum(LocalDate.of(1994, 12, 6));
        p.setAdresse("Unter den Eichen 5, 65195 Wiesbaden");
        p.setEmail("alexandra.mueller_1@student.hs-rm.de");
        p.setLieblingsfarbe("#c79098");
        p.setInteressen("Coding,  Computer Games , Petting Cats  ");
        m.addAttribute("profil", p);
    }

    @GetMapping("/benutzerprofil")
    public String benutzerprofil(Model m) {
        return "/benutzerprofil/profilansicht";
    }

    @GetMapping("/benutzerprofil/bearbeiten")
    public String bearbeiten(Model m) {
        return "/benutzerprofil/profileditor";
    }
}