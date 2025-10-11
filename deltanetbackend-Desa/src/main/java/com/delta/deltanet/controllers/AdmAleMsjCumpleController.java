package com.delta.deltanet.controllers;

import com.delta.deltanet.models.entity.AdmAleMsjCumple;
import com.delta.deltanet.services.IAdmAleMsjCumpleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for Birthday Message Templates (AdmAleMsjCumple).
 * Pure MVC: delegates to the Service layer; no SQL/DB code here.
 */
@RestController
@RequestMapping("/api/cumple")
public class AdmAleMsjCumpleController {

    @Autowired
    private IAdmAleMsjCumpleService cumpleService;

    /**
     * Returns the latest active birthday template for the given sexo ID.
     * Example: GET /api/cumple/template?sexoId=1
     *
     * @param sexoId numeric ID of PerNatSexo (matches id_sexo in DB)
     * @return 200 with entity when found; 204 when none
     */
    @GetMapping("/template")
    public ResponseEntity<AdmAleMsjCumple> getLatestTemplate(@RequestParam("sexoId") Integer sexoId) {
        AdmAleMsjCumple result = cumpleService.findLatestBySexo(sexoId);
        if (result == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(result);
    }
}
