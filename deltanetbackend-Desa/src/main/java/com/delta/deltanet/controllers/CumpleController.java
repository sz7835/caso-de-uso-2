package com.delta.deltanet.controllers;

import com.delta.deltanet.models.entity.AdmAleMsjCumple;
import com.delta.deltanet.services.IAdmAleMsjCumpleService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/cumple")
public class CumpleController {

    private final IAdmAleMsjCumpleService cumpleService;

    public CumpleController(IAdmAleMsjCumpleService cumpleService) {
        this.cumpleService = cumpleService;
    }

    // Simple health check
    @GetMapping("/ping")
    public Map<String, String> ping() {
        return Map.of("status", "ok");
    }

    // READ (optional, to verify): latest by sexo
    @GetMapping("/plantilla")
    public ResponseEntity<?> getPlantillaPorSexo(@RequestParam("sexo") Integer sexo) {
        AdmAleMsjCumple res = cumpleService.findLatestBySexo(sexo);
        if (res == null) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(Map.of("data", res));
    }

    /**
     * UPSERT (partial update) via multipart/form-data.
     * Fields:
     *  - sexo (Text, required)
     *  - descripcion (Text, optional)
     *  - ccDefecto (Text, optional)
     *  - saludoBreve (Text, optional)
     *  - footer (Text, optional)
     *  - banner (File, optional)
     *  - headerImage (File, optional)
     * Optional header: X-User
     */
    @PutMapping(value = "/plantilla", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> putPlantilla(
            @RequestParam("sexo") Integer sexo,
            @RequestParam(value = "descripcion", required = false) String descripcion,
            @RequestParam(value = "ccDefecto", required = false) String ccDefecto,
            @RequestParam(value = "saludoBreve", required = false) String saludoBreve,
            @RequestParam(value = "footer", required = false) String footer,
            @RequestParam(value = "banner", required = false) MultipartFile banner,
            @RequestParam(value = "headerImage", required = false) MultipartFile headerImage,
            @RequestHeader(value = "X-User", required = false) String xUser
    ) throws IOException {

        byte[] bannerBytes = (banner != null && !banner.isEmpty()) ? banner.getBytes() : null;
        byte[] headerImageBytes = (headerImage != null && !headerImage.isEmpty()) ? headerImage.getBytes() : null;

        AdmAleMsjCumple saved = cumpleService.upsertPlantilla(
                sexo,
                descripcion,
                ccDefecto,
                saludoBreve,
                bannerBytes,
                headerImageBytes,
                footer,
                (xUser != null ? xUser : "api")
        );

        return ResponseEntity.ok(Map.of("data", saved));
    }
}
