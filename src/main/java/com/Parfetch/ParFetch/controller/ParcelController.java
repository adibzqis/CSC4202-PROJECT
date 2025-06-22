package com.Parfetch.ParFetch.controller;

import com.Parfetch.ParFetch.model.Parcel;
import com.Parfetch.ParFetch.model.Sender;
import com.Parfetch.ParFetch.service.ParcelService;
import com.Parfetch.ParFetch.service.SenderService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/parcels")
public class ParcelController {
    private final ParcelService parcelService;
    private final SenderService senderService;

    public ParcelController(ParcelService parcelService, SenderService senderService) {
        this.parcelService = parcelService;
        this.senderService = senderService;
    }

    @GetMapping("/list")
    public String showAllParcels(Model model, Authentication auth) {
        List<Parcel> parcels;
        String homeLink = "/";
        String username = auth.getName();

        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_SENDER"))) {
            Sender sender = senderService.findByPhone(username);
            parcels = parcelService.getParcelsBySender(sender);
            homeLink = "/student-home";
            username = sender.getName();
        } else {
            parcels = parcelService.getAllParcels();
            homeLink = "/index";
        }

        model.addAttribute("parcels", parcels);
        model.addAttribute("homeLink", homeLink);
        model.addAttribute("username", username);
        return "list-parcel";
    }

    @GetMapping("/signup")
    public String showAddParcelForm(Model model) {
        model.addAttribute("parcel", new Parcel());
        return "add-parcel";
    }

    @PostMapping("/save")
    public String saveParcel(@ModelAttribute Parcel parcel) {
        parcelService.save(parcel);
        return "redirect:/parcels/list";
    }

    @GetMapping("/update")
    public String showUpdateSelectionForm(Model model) {
        model.addAttribute("parcels", parcelService.getAllParcels());
        return "choose-parcel-to-update";
    }

    @GetMapping("/edit/{trackingNumber}")
    public String showUpdateForm(@PathVariable("trackingNumber") int trackingNumber, Model model) {
        Parcel parcel = parcelService.getAllParcels().stream()
                .filter(p -> p.getTrackingNumber() == trackingNumber)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid parcel trackingNumber: " + trackingNumber));
        model.addAttribute("parcel", parcel);
        return "update-parcel";
    }

    @PostMapping("/edit/{trackingNumber}")
    public String updateParcel(@PathVariable("trackingNumber") int trackingNumber,
                               @Valid @ModelAttribute("parcel") Parcel parcel,
                               BindingResult result,
                               Model model) {
        if (result.hasErrors()) {
            parcel.setTrackingNumber(trackingNumber);
            return "update-parcel";
        }
        parcelService.save(parcel);
        return "redirect:/parcels/list";
    }

    @GetMapping("/delete")
    public String showDeleteSelectionForm(Model model) {
        model.addAttribute("parcels", parcelService.getAllParcels());
        return "choose-parcel-to-delete";
    }

    @GetMapping("/delete/{trackingNumber}")
    public String deleteParcel(@PathVariable("trackingNumber") int trackingNumber) {
        Parcel parcel = parcelService.getAllParcels().stream()
                .filter(p -> p.getTrackingNumber() == trackingNumber)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid parcel trackingNumber: " + trackingNumber));
        parcelService.deleteParcel(parcel);
        return "redirect:/parcels/list";
    }
}
