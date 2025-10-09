package com.delta.deltanet.controllers;

import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/api/cumple")
public class CumpleController {

  private static final int MAX_PNG_BYTES = 10_000_000; // 10 MB
  private static final int MAX_GREETING = 250;

  private final JdbcTemplate jdbc;
  public CumpleController(DataSource ds) { this.jdbc = new JdbcTemplate(ds); }

  private ResponseEntity<Map<String,Object>> ok(Map<String,Object> data){
    return ResponseEntity.ok(Map.of("error", false, "data", data));
  }
  private ResponseStatusException bad(String msg){
    return new ResponseStatusException(HttpStatus.BAD_REQUEST, msg);
  }

  // Health check
  @GetMapping("/ping")
  public Map<String,String> ping(){ return Map.of("ok","api/cumple alive"); }

  // GET: latest active template per gender (linked to per_nat_sexo)
  @GetMapping("/plantilla")
  public ResponseEntity<Map<String,Object>> get(@RequestParam Integer sexo){
    try {
      var row = jdbc.queryForMap(
        "SELECT c.id, c.id_sexo, s.descripcion AS sexo_desc, c.descripcion, " +
        "c.lista_corr_defecto, c.msg_body, c.msg_footer, " +
        "(CASE WHEN c.msg_header_banner IS NULL THEN 0 ELSE 1 END) AS has_banner, " +
        "(CASE WHEN c.msg_header_image  IS NULL THEN 0 ELSE 1 END) AS has_image " +
        "FROM adm_ale_msj_cumple c " +
        "JOIN per_nat_sexo s ON s.id = c.id_sexo " +
        "WHERE c.id_sexo=? AND c.estado_reg=1 ORDER BY c.id DESC LIMIT 1",
        sexo
      );
      return ok(Map.of(
        "id", row.get("id"),
        "id_sexo", row.get("id_sexo"),
        "sexo_desc", row.get("sexo_desc"),
        "descripcion", row.get("descripcion"),
        "lista_corr_defecto", row.get("lista_corr_defecto"),
        "msg_body", row.get("msg_body"),
        "msg_footer", row.get("msg_footer"),
        "has_banner", row.get("has_banner"),
        "has_image", row.get("has_image")
      ));
    } catch (Exception e) { throw bad("No existe plantilla activa para sexo=" + sexo); }
  }

  // PUT: create or update template (multipart/form-data)
  @PutMapping(value="/plantilla", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Map<String,Object>> upsert(
      @RequestParam Integer sexo,
      @RequestParam String lista_corr_defecto,
      @RequestParam String msg_body,
      @RequestParam String msg_footer,
      @RequestParam(required=false) String descripcion,
      @RequestParam(value="banner_png", required=false) MultipartFile banner,
      @RequestParam(value="imagen_png", required=false) MultipartFile imagen,
      @RequestParam(value="user", defaultValue="api") String user
  ) throws Exception {

    // Validate gender exists in per_nat_sexo
    Integer count = jdbc.queryForObject("SELECT COUNT(*) FROM per_nat_sexo WHERE id=?", Integer.class, sexo);
    if (count == null || count == 0)
      throw bad("El valor de sexo no existe en la tabla per_nat_sexo.");

    // Validate message content
    if (!StringUtils.hasText(msg_body) || msg_body.length() > MAX_GREETING)
      throw bad("El mensaje es obligatorio y debe tener ≤ 250 caracteres.");
    if (!msg_body.contains("[Primer Nombre]") || !msg_body.contains("[Primer Apellido]"))
      throw bad("El mensaje debe incluir [Primer Nombre] y [Primer Apellido].");

    // Validate images
    if (banner != null && !banner.isEmpty()) {
      if (!"image/png".equalsIgnoreCase(banner.getContentType())) throw bad("banner_png debe ser PNG");
      if (banner.getSize() > MAX_PNG_BYTES) throw bad("banner_png no debe superar 10 MB");
    }
    if (imagen != null && !imagen.isEmpty()) {
      if (!"image/png".equalsIgnoreCase(imagen.getContentType())) throw bad("imagen_png debe ser PNG");
      if (imagen.getSize() > MAX_PNG_BYTES) throw bad("imagen_png no debe superar 10 MB");
    }

    // Check for existing template
    Integer existingId = null;
    try {
      existingId = jdbc.queryForObject(
        "SELECT id FROM adm_ale_msj_cumple WHERE id_sexo=? AND estado_reg=1 ORDER BY id DESC LIMIT 1",
        Integer.class, sexo
      );
    } catch (Exception ignore) {}

    if (existingId == null) {
      // Insert new
      jdbc.update(
        "INSERT INTO adm_ale_msj_cumple " +
        "(id_sexo, descripcion, lista_corr_defecto, msg_body, msg_footer, " +
        " msg_header_banner, msg_header_image, create_user, create_date, estado_reg) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW(), 1)",
        sexo, descripcion, lista_corr_defecto, msg_body, msg_footer,
        (banner!=null && !banner.isEmpty()) ? banner.getBytes() : null,
        (imagen!=null && !imagen.isEmpty()) ? imagen.getBytes() : null,
        user
      );
      Integer id = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
      return ok(Map.of("id", id, "sexo", sexo));
    } else {
      // Update existing
      jdbc.update(
        "UPDATE adm_ale_msj_cumple SET descripcion=?, lista_corr_defecto=?, msg_body=?, msg_footer=?, " +
        " update_user=?, update_date=NOW() WHERE id=?",
        descripcion, lista_corr_defecto, msg_body, msg_footer, user, existingId
      );
      if (banner!=null && !banner.isEmpty())
        jdbc.update("UPDATE adm_ale_msj_cumple SET msg_header_banner=? WHERE id=?", banner.getBytes(), existingId);
      if (imagen!=null && !imagen.isEmpty())
        jdbc.update("UPDATE adm_ale_msj_cumple SET msg_header_image=? WHERE id=?", imagen.getBytes(), existingId);
      return ok(Map.of("id", existingId, "sexo", sexo));
    }
  }

  // POST: Preview HTML with substituted variables
  @PostMapping("/preview")
  public ResponseEntity<Map<String,Object>> preview(@RequestBody Map<String,String> body){
    Integer sexo = Integer.valueOf(body.getOrDefault("sexo","1"));
    String nombre = body.getOrDefault("primerNombre","Nombre");
    String apellido = body.getOrDefault("primerApellido","Apellido");

    var row = jdbc.queryForMap(
      "SELECT c.msg_body, c.msg_footer, c.msg_header_banner, c.msg_header_image, s.descripcion AS sexo_desc " +
      "FROM adm_ale_msj_cumple c " +
      "JOIN per_nat_sexo s ON s.id = c.id_sexo " +
      "WHERE c.id_sexo=? AND c.estado_reg=1 ORDER BY c.id DESC LIMIT 1",
      sexo
    );

    String msgBody   = (String) row.getOrDefault("msg_body", "");
    String msgFooter = (String) row.getOrDefault("msg_footer", "");
    byte[] banner    = (byte[]) row.get("msg_header_banner");
    byte[] imagen    = (byte[]) row.get("msg_header_image");

    String bannerTag = (banner==null) ? "" :
      "<img alt='banner' style='max-width:100%' src='data:image/png;base64," +
      Base64.getEncoder().encodeToString(banner) + "'/>";
    String imageTag = (imagen==null) ? "" :
      "<img alt='image' style='max-width:100%' src='data:image/png;base64," +
      Base64.getEncoder().encodeToString(imagen) + "'/>";

    String saludoGenero = (sexo != null && sexo == 1) ? "¡Estimado" : "¡Estimada";
    String bodyFilled = msgBody
            .replace("[Primer Nombre]", nombre)
            .replace("[Primer Apellido]", apellido);

    String html =
        "<div style=\"font-family:sans-serif\">" +
          bannerTag +
          "<h2>" + saludoGenero + " " + nombre + " " + apellido + "!</h2>" +
          "<p>" + bodyFilled + "</p>" +
          imageTag +
          "<hr/><small>" + (msgFooter == null ? "" : msgFooter) + "</small>" +
        "</div>";

    return ok(Map.of("html", html));
  }
}
