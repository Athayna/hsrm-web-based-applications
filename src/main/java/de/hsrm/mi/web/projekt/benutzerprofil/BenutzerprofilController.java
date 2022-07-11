package de.hsrm.mi.web.projekt.benutzerprofil;

import java.security.Principal;
import java.time.LocalDate;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import de.hsrm.mi.web.projekt.angebot.Angebot;
import de.hsrm.mi.web.projekt.angebot.AngebotRepository;
import de.hsrm.mi.web.projekt.messaging.BackendInfoService;
import de.hsrm.mi.web.projekt.messaging.BackendOperation;
import de.hsrm.mi.web.projekt.projektuser.ProjektUserService;
import de.hsrm.mi.web.projekt.projektuser.ProjektUserServiceException;

@Controller
@SessionAttributes(names = {"profil"})
public class BenutzerprofilController {
    @Autowired
    BenutzerprofilService bService;
    @Autowired
    BenutzerprofilRepository benRep;
    @Autowired
    AngebotRepository anRep;
    @Autowired
    BackendInfoService backendInfoService;
    @Autowired
    private ProjektUserService pService;

    Logger logger = LoggerFactory.getLogger(BenutzerprofilController.class);

    @ModelAttribute("profil")
    public void initProfil(Model m, Principal p) {
        BenutzerProfil profil = new BenutzerProfil();
        if(p != null){
            try {
                profil = pService.findeBenutzer(p.getName()).getBenutzerProfil();
            } catch (ProjektUserServiceException e) {
                logger.error("BenutzerprofilController: initProfil: Benutzer konnte nicht gefunden werden");
            }
        }
        /*profil.setName("Alexandra Müller");
        profil.setGeburtsdatum(LocalDate.of(1994, 12, 6));
        profil.setAdresse("Unter den Eichen 5, 65195 Wiesbaden");
        profil.setEmail("alexandra.mueller_1@student.hs-rm.de");
        profil.setLieblingsfarbe("#c79098");
        profil.setInteressen("Coding,  Computer Games , Petting Cats  ");*/
        m.addAttribute("profil", profil);
    }

    @GetMapping("/benutzerprofil")
    public String benutzerprofil(Model m) {
        return "/benutzerprofil/profilansicht";
    }

    @GetMapping("/benutzerprofil/bearbeiten")
    public String bearbeiten(Model m) {
        return "/benutzerprofil/profileditor";
    }

    @GetMapping("/benutzerprofil/liste")
    public String liste(Model m, @RequestParam(required = false) String op, @RequestParam(required = false) Long id) {
        if(op != null) {
            if (op.equals("loeschen")) {
                bService.loescheBenutzerProfilMitId(id);
                m.addAttribute("profilliste", bService.alleBenutzerProfile());
                return "redirect:/benutzerprofil/liste";
            } else if (op.equals("bearbeiten")) {
                m.addAttribute("profil", bService.holeBenutzerProfilMitId(id).get());
                return "redirect:/benutzerprofil/bearbeiten";
            }
        }
        m.addAttribute("profilliste", bService.alleBenutzerProfile());
        return "/benutzerprofil/profilliste";
    }

    @GetMapping("/benutzerprofil/angebot")
    public String angebot(Model m) {
        m.addAttribute("angebot", new Angebot());
        return "benutzerprofil/angebotsformular";
    }

    @GetMapping("/benutzerprofil/angebot/{id}/del")
    public String angebotLoeschen(Model m, @PathVariable Long id) {
        long bpId = anRep.findById(id).get().getAnbieter().getId();
        bService.loescheAngebot(id);
        m.addAttribute("profil", bService.holeBenutzerProfilMitId(bpId).get());
        backendInfoService.sendInfo("angebot", BackendOperation.DELETE, id);
        return "redirect:/benutzerprofil";
    }

    @PostMapping("/benutzerprofil/bearbeiten")
    public String bearbeitet(Model m, @Valid @ModelAttribute("profil") BenutzerProfil profil, BindingResult result) {
        if (result.hasErrors())
            return "benutzerprofil/profileditor";
        BenutzerProfil sProfil = bService.speichereBenutzerProfil(profil);
        m.addAttribute("profil", sProfil);
        return "redirect:/benutzerprofil";
    }

    @PostMapping("/benutzerprofil/angebot")
    public String angebotsformular(Model m, @Valid @ModelAttribute("angebot") Angebot angebot, BindingResult result) {
        if (result.hasErrors())
            return "benutzerprofil/angebotsformular";
        long bId = ((BenutzerProfil)m.getAttribute("profil")).getId();
        bService.fuegeAngebotHinzu(bId, angebot);
        m.addAttribute("profil", bService.holeBenutzerProfilMitId(bId).get());
        backendInfoService.sendInfo("angebot", BackendOperation.CREATE, angebot.getId());
        return "redirect:/benutzerprofil";
    }
}