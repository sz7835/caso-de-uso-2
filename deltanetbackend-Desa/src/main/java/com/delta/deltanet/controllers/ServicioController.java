package com.delta.deltanet.controllers;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.poi.util.Units;
import org.apache.poi.wp.usermodel.HeaderFooterType;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.poi.xwpf.usermodel.XWPFTable.XWPFBorderType;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STFldCharType;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGrid;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridCol;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.delta.deltanet.models.entity.PersonaJuridica;
import com.delta.deltanet.models.entity.ServicioActividades;
import com.delta.deltanet.models.entity.ServicioForma;
import com.delta.deltanet.models.entity.ServicioInforme;
import com.delta.deltanet.models.entity.ServicioTipo;
import com.delta.deltanet.models.entity.SrvEstado;
import com.delta.deltanet.models.service.IAlertaUsuarioService;
import com.delta.deltanet.models.service.IAutenticacionUsuarioService;
import com.delta.deltanet.models.service.IPersonaJuridicaService;
import com.delta.deltanet.models.service.IPersonaService2;
import com.delta.deltanet.models.service.ISrvEstadoService;
import com.delta.deltanet.models.service.SrvActivServiceImpl;
import com.delta.deltanet.models.service.SrvActivServiceImpl.lstActi;
import com.delta.deltanet.models.service.SrvFormaServiceImpl;
import com.delta.deltanet.models.service.SrvInformeServiceImpl;
import com.delta.deltanet.models.service.SrvTipoServiceImpl;

import com.sun.tools.javac.Main;

@CrossOrigin(origins= {"*"})
@RestController
@RequestMapping("/api/servicio")
public class ServicioController {

	@Autowired
	private SrvActivServiceImpl activService;

	@Autowired
	private SrvInformeServiceImpl informeService;

	@Autowired
	private SrvTipoServiceImpl tipoService;

	@Autowired
	private SrvFormaServiceImpl formaService;

	@Autowired
	private ISrvEstadoService estadoService;

	@Autowired
	private IAutenticacionUsuarioService autenticacionUsuarioService;

	@Autowired
	private IPersonaJuridicaService personaJuridicaService;

    @Autowired
    private IPersonaService2 personaService;

	@Autowired
	private IAlertaUsuarioService alertaService;

    @Value("${spring.mail.host}")
	private String envHost;
	@Value("${spring.mail.port}")
	private String envPort;
	@Value("${spring.mail.username}")
	private String envUsername;
	@Value("${spring.mail.password}")
	private String envPassword;
	@Value("${spring.mail.properties.mail.smtp.auth}")
	private String envAuth;
	@Value("${spring.mail.properties.mail.smtp.starttls.enable}")
	private String envStartTls;

	@GetMapping("/index")
	public ResponseEntity<?> listaRegistros(@RequestParam(required = false) Long idCliente,
			                                @RequestParam(required = false) Long idTipo,
			                                @RequestParam(required = false) Long idForma,
			                                @RequestParam(required = false) Long estado,
			                                @RequestParam(required = false) Long idResolutor,
			                                @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecIni,
			                                @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecFin,
			                                @RequestParam(required = false) Integer facturable,
			                                @RequestParam(required = false) String periodoFac,
			                                @RequestParam(value = "paginado", required = false, defaultValue = "0") Integer swPag, Pageable pageable) {
		Map<String, Object> response = new HashMap<>();
		List<Long> lstTickets = null;
		String mmEje = null;
		String yyEje = null;
		if (periodoFac != null && !periodoFac.trim().isEmpty() && periodoFac.contains("-")) {
            String[] t = periodoFac.split("-");
            if (t.length == 2 && !t[0].isEmpty() && !t[1].isEmpty()) {
                yyEje = t[0];
                mmEje = t[1];
            }
        }
		if (swPag == 0) {
			List<lstActi> regActividades = activService.lstFiltrado(idCliente,idTipo,idForma,estado,idResolutor,fecIni,fecFin,facturable,mmEje,yyEje,lstTickets);
			response.put("data",regActividades);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			Page<lstActi> regActividades = activService.lstFiltrado(idCliente,idTipo,idForma,estado,idResolutor,fecIni,fecFin,facturable,mmEje,yyEje,pageable);
			response.put("datos", regActividades.getContent());
			response.put("totRegs", regActividades.getTotalElements());
			response.put("totPags", regActividades.getTotalPages());
			return ResponseEntity.ok(response);
		}
	}

	@GetMapping("/create")
	public ResponseEntity<?> listaTablas(){
		Map<String, Object> response = new HashMap<>();
		try {
			//List<ServicioProyecto> lstProyectos = proyService.listaProyectos();
			List<Object> lstClientes = personaService.listadoClientes();
			List<ServicioTipo> lstTipos = tipoService.listaTipos();
			List<ServicioForma> lstFormas = formaService.listaFormas();
			List<SrvEstado> lstEstados = estadoService.findAll();
			List<Object> lstUsuarios = autenticacionUsuarioService.listaRecursos();
			List<String> lstPeriodos = activService.listaPeriodos();
			
			lstCombo opcion;
			/*List<lstCombo> cboProyectos = new ArrayList<>();
			for(ServicioProyecto reg : lstProyectos) {
				opcion = new lstCombo();
				opcion.setId(reg.getId());
				opcion.setDesItem1(reg.getDescrip());
				cboProyectos.add(opcion);
			}*/
			
			List<lstCombo> cboTipos = new ArrayList<>();
			for(ServicioTipo reg : lstTipos) {
				opcion = new lstCombo();
				opcion.setId(reg.getId());
				opcion.setDesItem1(reg.getNombre());
				cboTipos.add(opcion);
			}
			
			List<lstCombo> cboFormas = new ArrayList<>();
			for(ServicioForma reg : lstFormas) {
				opcion = new lstCombo();
				opcion.setId(reg.getId());
				opcion.setDesItem1(reg.getNombre());
				cboFormas.add(opcion);
			}
			
			List<lstCombo> cboEstados= new ArrayList<>();
			for(SrvEstado reg : lstEstados) {
				opcion = new lstCombo();
				opcion.setId(reg.getId());
				opcion.setDesItem1(reg.getNombre());
				cboEstados.add(opcion);
			}
			
			List<lstCombo> cboUsuarios= new ArrayList<>();
			Iterator<Object> it = lstUsuarios.iterator();
			while (it.hasNext()){
				Object[] row = (Object[]) it.next();
				opcion = new lstCombo();
				opcion.setId(Long.valueOf(String.valueOf(row[0])));
				opcion.setDesItem1(String.valueOf(row[1]));
				opcion.setDesItem2(String.valueOf(row[2]));
				cboUsuarios.add(opcion);
			}
			
			response.put("clientes",lstClientes);
			response.put("tipos",cboTipos);
			response.put("formasPago",cboFormas);
			response.put("estados",cboEstados);
			response.put("usuarios",cboUsuarios);
			response.put("periodos", lstPeriodos);
			
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.put("message", "Error inesperado en la funcion create de actividades");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}
	
	@PostMapping("/store")
	public ResponseEntity<?> creaRegistro(@RequestParam(required = false) Long idActi,
			                              @RequestParam Long idCli,
			                              @RequestParam Long idTipo,
			                              @RequestParam Long idForma,
			                              @RequestParam(name = "idEstado") Long estado,
			                              @RequestParam Long idResolutor,
			                              @RequestParam Long numero,
			                              @RequestParam String descrip,
			                              @RequestParam double nroHoras,
			                              @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecIni,
			                              @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecFin,
			                              @RequestParam String mesEje,
			                              @RequestParam String yearEje,
										  @RequestParam String facturable,
			                              @RequestParam(name = "observacion", required = false) String observ,
			                              @RequestParam String username) {
		Map<String, Object> response = new HashMap<>();
		if(observ==null) observ="";
		try {
			ServicioActividades registro;
			if(idActi==null) {
				registro = new ServicioActividades();
				registro.setCreateDate(new Date());
				registro.setCreateUser(username);
			} else {
				registro = activService.buscaById(idActi);
				if (registro==null) {
					response.put("mensaje", "Id de la actividad no existe.");
					return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
				}
				registro.setUpdateDate(new Date());
				registro.setUpdateUser(username);
			}

			registro.setCliente(personaJuridicaService.findById(idCli).orElse(null));
			registro.setTipo(tipoService.buscaById(idTipo));
			registro.setForma(formaService.buscaById(idForma));
			registro.setEstado(estadoService.findByPk(estado));
			registro.setResolutor(autenticacionUsuarioService.buscaById(idResolutor));
			registro.setNumero(numero);
			registro.setDescrip(descrip);
			registro.setHoras(nroHoras);
			registro.setFechaIni(fecIni);
			registro.setFechaFin(fecFin);
			registro.setMesEje(mesEje);
			registro.setYearEje(yearEje);
			registro.setObs(observ);
			registro.setFacturable(facturable.equals("1") ? 1 : 0);

			activService.save(registro);

			response.put("data",registro);
            response.put("message","Registro exitoso");
            return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.put("mensaje", "Error inesperado en la funcion store de actividades");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}
	
	@GetMapping("/informes")
	public ResponseEntity<?> getInformes(){
		Map<String, Object> response = new HashMap<>();
		
		List<ServicioInforme> informes = informeService.findAll();
		response.put("informes", informes);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/send-informe") // BATCH-04
	public ResponseEntity<?> sendInformes(@RequestParam Long idInforme){
		Map<String, Object> response = new HashMap<>();

		ServicioInforme informe = informeService.findById(idInforme);
		List<Object> usuarios = alertaService.listaEmails("BATCH-04");
		if(usuarios == null || usuarios.isEmpty()) {
			response.put("mensaje", "No existen usuarios para enviar el informe.");
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		if(informe == null) {
			response.put("mensaje", "No existen el informe.");
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}

		Properties props = System.getProperties();
		props.put("mail.smtp.host", envHost);
		props.put("mail.smtp.user", envUsername);
		props.put("mail.smtp.password", envPassword);
		props.put("mail.smtp.starttls.enable", envStartTls);
		props.put("mail.smtp.port", envPort); // 587 is the port number of yahoo mail
		props.put("mail.smtp.auth", envAuth);
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(envUsername, envPassword);
			}
		});
		try {
			String informeFileName = informe.getCliente().getRazonSocial() + " - REPORTE MENSUAL DE HORAS DE SERVICIO - " + informe.getYearFac() + " - " + informe.getMesFac() + " - v1.0";
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(envUsername, "Deltanet"));

			for (Object obj : usuarios) {
				Object[] fila = (Object[]) obj;
				String correoDelta = (String) fila[0];
				msg.addRecipient(Message.RecipientType.TO, new InternetAddress(correoDelta));
			}
			msg.setSubject(informeFileName);
			Multipart mp = new MimeMultipart();
			MimeBodyPart htmlPart = new MimeBodyPart();
			StringBuilder cuerpo = new StringBuilder("<html><body><p>Se adjunta reporte mensual de horas servicio.</p></body></html>\n");
			htmlPart.setContent(cuerpo.toString(), "text/html;charset=utf-8");
			mp.addBodyPart(htmlPart);

			byte[] informeGeneradoBytes = generarInforme(informe);
			MimeBodyPart attachmentPart = new MimeBodyPart();
			DataSource dataSource = new ByteArrayDataSource(informeGeneradoBytes, "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
			attachmentPart.setDataHandler(new DataHandler(dataSource));
			attachmentPart.setFileName(informeFileName + ".docx");
			mp.addBodyPart(attachmentPart);
			
			msg.setContent(mp);
			Transport transport=session.getTransport();
			transport.connect();
			Transport.send(msg);
			transport.close();
		} catch (Exception e) {
			response.put("mensaje", "Error el enviar correo");
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "Informe con id: " + informe.getId() + " enviado con éxito");
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	@PostMapping("/facturar")
	public ResponseEntity<?> facturarRegistros(@RequestParam Long idCliente,
												@RequestParam String lstTickets,
												@RequestParam String fechaFac,
												@RequestParam String username){
		Map<String, Object> response = new HashMap<>();
		
		PersonaJuridica cliente = personaJuridicaService.findById(idCliente).orElse(null);
		
		if(cliente == null) {
			response.put("mensaje", "Cliente con id " + idCliente + " no existe.");
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		
		if(esFechaFutura(fechaFac)) {
			response.put("mensaje", "Fecha inválida. La fecha de facturación no puede ser futura.");
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		
		String anioFac = fechaFac.split("-")[0];
		String mesFac = fechaFac.split("-")[1];

		if(informeService.existsByClienteAndMesFac(idCliente, mesFac)) {
			String nombreMes = Month.of(Integer.valueOf(mesFac)).getDisplayName(TextStyle.FULL, new Locale("es","ES"));
			response.put("mensaje", "El cliente con id " + idCliente + " ya tiene un reporte facturado en el mes de " + nombreMes + ".");
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		
		List<Long> lstIds = Arrays.stream(lstTickets.split(","))
                .map(String::trim)
                .map(Long::valueOf)
                .collect(Collectors.toList());

		List<ServicioActividades> regActividades = activService.lstFiltrado(idCliente, lstIds, 1);
		
		double totalHoras = regActividades.stream()
				.mapToDouble(ServicioActividades::getHoras)
				.sum();

		int tamRegs = regActividades.size();
		if(tamRegs < 1) {
			response.put("mensaje", "No existen registros con los parametros proporcionados.");
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}

		for(ServicioActividades item: regActividades) {
			if (item.getEstado().getId() != 5L) {
				response.put("mensaje", "Todos los registros deben tener estado cerrado. El registro con numero " + item.getNumero() + " no esta cerrado");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			
			item.setUpdateUser(username);
			item.setUpdateDate(new Date());
			item.setMesFac(mesFac);
			item.setYearFac(anioFac);
			item.setEstado(estadoService.findByPk(1L));
		}
		
		regActividades = activService.saveAll(regActividades);
		
		// crear informe en nueva tabla
		ServicioInforme informe = new ServicioInforme();
		informe.setCliente(cliente);
		informe.setNumero(informeService.findMaxNumeroByCliente(idCliente)+1);
		informe.setMesFac(mesFac);
		informe.setYearFac(anioFac);
		informe.setListTickets(lstTickets);
		informe.setTotalHoras(totalHoras);
		informe.setCreateUser(username);
		informe.setCreateDate(new Date());
		
		informe = informeService.save(informe);
		
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDispositionFormData("attachment", "informe.docx");
			
			byte[] informeBytes = generarInforme(informe);
			
			return ResponseEntity.ok()
					.headers(headers)
					.body(informeBytes);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
	}
	
	@PostMapping("/delete")
	public ResponseEntity<?> eliminaRegistro(@RequestParam Long idActi,
			                                 @RequestParam String username){
		Map<String, Object> response = new HashMap<>();
		try {
			ServicioActividades registro = activService.buscaById(idActi);
			if (registro==null) {
				response.put("message", "Id de la actividad no existe.");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
			activService.delete(registro);
            response.put("message","Transacción realizada exitosamente.");

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.put("message", "Error inesperado en la función delete de actividades");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@GetMapping("/read")
	public ResponseEntity<?> leeRegistro(@RequestParam Long idActi){
		Map<String, Object> response = new HashMap<>();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		try {
			ServicioActividades registro = activService.buscaById(idActi);
			if (registro==null) {
				response.put("message", "Id de la actividad no existe.");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
            // Validación: no permitir si el estado es 1
            if (registro.getEstado() != null && registro.getEstado().getId() == 1L) {
                response.put("message", "No se permite consultar actividades con estado FACTURADO.");
                return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
            }
			regActi reg = new regActi();
			reg.setId(registro.getId());
			reg.setIdCliente(registro.getCliente().getIdPerJur());
			reg.setIdTipo(registro.getTipo().getId());
			reg.setIdforma(registro.getForma().getId());
			reg.setIdEstado(registro.getEstado().getId());
			reg.setIdResolutor(registro.getResolutor().getId());
			reg.setNumero(registro.getNumero());
			reg.setDescrip(registro.getDescrip());
			reg.setNroHoras(registro.getHoras());
			reg.setFecIni(formatter.format(registro.getFechaIni()));
			reg.setFecFin(formatter.format(registro.getFechaFin()));
			reg.setMesEje(registro.getMesEje());
			reg.setYyEje(registro.getYearEje());
			reg.setMmFac(registro.getMesFac());
			reg.setYyFac(registro.getYearFac());
			reg.setObservacion(registro.getObs());
			reg.setFacturable(registro.getFacturable() == 1 ? "1" : "0");

			response.put("data",reg);
            response.put("message","Transacción realizada exitosamente.");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.put("message", "Error inesperado en la función delete de actividades");
            response.put("debug", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@GetMapping("/word")
	public ResponseEntity<?> creaWord(@RequestParam Long idCliente,
            @RequestParam(required = false) Long idTipo,
            @RequestParam(required = false) Long idForma,
            @RequestParam(required = false, defaultValue = "0") Long estado,
            @RequestParam(required = false) Long idResolutor,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecIni,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fecFin,
            @RequestParam(required = false) Integer facturable,
            @RequestParam(required = false) List<Long> lstTickets,
            @RequestParam(required = false) String periodoFac,
            @RequestParam(required = false) String fechaFac,
            @RequestParam(value = "paginado", required = false, defaultValue = "0") Integer swPag, Pageable pageable){
		Map<String, Object> response = new HashMap<>();
		String mmEje = null;
		String yyEje = null;
		if (periodoFac!=null) {
			String[] t = periodoFac.split("-");
			yyEje = t[0];
			mmEje = t[1];
		}

		if(esFechaFutura(fechaFac)) {
			response.put("message", "Fecha inválida. La fecha de facturación no puede ser futura.");
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}
		
		Integer anioFac = Integer.valueOf(fechaFac.split("-")[0]);
		Integer mesFac = Integer.valueOf(fechaFac.split("-")[1]);
		
		//Obtener datos constantes de la data proporcionada y lista de actividades
		List<lstActi> regActividades = activService.lstFiltrado(idCliente,idTipo,idForma,estado,idResolutor,fecIni,fecFin,facturable,mmEje,yyEje, lstTickets);
		int tamRegs = regActividades.size();
		if(tamRegs < 1) {
			response.put("message", "No existen registros con los parametros proporcionados.");
			return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
		}

		ServicioActividades registro = activService.buscaById(regActividades.get(0).getId());
		String rz = registro.getCliente().getRazonSocial();
		Integer numMes = registro.getMesFac() != null ? Integer.valueOf(registro.getMesFac()) : mesFac;
		Integer numYear = registro.getYearFac() != null ? Integer.valueOf(registro.getYearFac()) : anioFac;
		String nMes = Month.of(numMes).getDisplayName(TextStyle.FULL, new Locale("es","ES"));
		String ultimoDia = obtenerUltimoDia(numYear, numMes);
		int ultRep = 1;
		String strNumRep = "N° " + String.valueOf(ultRep);
		
		//Creacion de documento Word
		try (XWPFDocument documento = new XWPFDocument()) {
			CTSectPr sectPr = documento.getDocument().getBody().addNewSectPr();
			
			//Establecer margen izquierdo y derecho
			CTPageMar pageMar = sectPr.addNewPgMar();
	        pageMar.setLeft(BigInteger.valueOf(1135)); 
	        pageMar.setRight(BigInteger.valueOf(1135));
	        
			//Agregar encabezado
			XWPFHeader encabezado = documento.createHeader(HeaderFooterType.DEFAULT);
			
			XWPFTable tabla = encabezado.createTable(3, 3);
			configuraTabla(tabla,XWPFBorderType.SINGLE,true);
			
			CTTblGrid tblGrid = tabla.getCTTbl().addNewTblGrid();
			
			int[] anchoColumnas = {1595,4650,1800};
			for(int ancho: anchoColumnas) {
				CTTblGridCol gridCol = tblGrid.addNewGridCol();
				gridCol.setW(BigInteger.valueOf(ancho));
			}
			
			for(XWPFTableRow fila : tabla.getRows()) {
				for(int i = 0; i < fila.getTableCells().size(); i++) {
					XWPFTableCell celda = fila.getTableCells().get(i);
					CTTc cttc = celda.getCTTc();
					CTTcPr tcPr = cttc.isSetTcPr() ? cttc.getTcPr() : cttc.addNewTcPr();
					
					CTTblWidth tcWidth = tcPr.isSetTcW() ? tcPr.getTcW() : tcPr.addNewTcW();
					tcWidth.setW(BigInteger.valueOf(anchoColumnas[i]));
					tcWidth.setType(STTblWidth.DXA);
				}
			}
			
			// Cambiar la altura de la primera fila y establecerla como fija
			ajustaAlturaCeldas(tabla,3,300);
	        
			combinarCeldasVerticalmente(tabla,0,0,2,0);
			combinarCeldasVerticalmente(tabla,0,1,2,1);
			
			//Agregar logo Delta
			XWPFTableCell celdaConImagen = tabla.getRow(0).getCell(0);
			celdaConImagen.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.BOTTOM);
			XWPFParagraph parrafoCelda = celdaConImagen.addParagraph();
			parrafoCelda.setAlignment(ParagraphAlignment.CENTER);
			XWPFRun runCelda = parrafoCelda.createRun();
			
			//Carga la imagen desde resource
			String archivo = "logoDelta.png";
			
			try (InputStream inputStream= Main.class.getClassLoader().getResourceAsStream(archivo)) {
				if (inputStream == null) {
					response.put("respuesta", "no se encuentra el archivo");
					return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
				}
				
				runCelda.addPicture(inputStream, XWPFDocument.PICTURE_TYPE_PNG, archivo, Units.toEMU(82), Units.toEMU(42));
			} catch (Exception e) {
				e.printStackTrace();
				return ResponseEntity.internalServerError().build();
			}
			
			CTDrawing drawing = runCelda.getCTR().getDrawingArray(0);
			CTInline inline = drawing.getInlineArray(0);
			CTPositiveSize2D extents = inline.getExtent();
			extents.setCx(Units.toEMU(82));
			extents.setCy(Units.toEMU(42));
			
			setTextoCelda(tabla, 0, 1, "REPORTE MENSUAL DE HORAS DE SERVICIO",ParagraphAlignment.CENTER,10,"FFFFFF","000000",true);
	        setTextoCelda(tabla, 0, 2, "Código: FO-GCA-16",ParagraphAlignment.LEFT,10,"FFFFFF","000000",true);
	        setTextoCelda(tabla, 1, 2, "Versión: 1.3",ParagraphAlignment.LEFT,10,"FFFFFF","000000",true);
	        setTextoCelda(tabla, 2, 2, "Fecha: 20/04/2022",ParagraphAlignment.LEFT,10,"FFFFFF","000000",true);
	        
	        // Crear un pie de página
	        XWPFFooter footer = documento.createFooter(HeaderFooterType.DEFAULT);

	        // Agregar un párrafo al pie de página
	        XWPFParagraph paragraph = footer.createParagraph();
	        paragraph.setAlignment(ParagraphAlignment.CENTER); // Centrar el texto

	        // Agregar texto con formato
	        XWPFRun run = paragraph.createRun();
	        run.setText("Página ");
	        addPageNumberField(paragraph);
	        run = paragraph.createRun();
	        run.setText(" de ");
	        addTotalPagesField(paragraph);

	        XWPFParagraph parrafoCuerpo = documento.createParagraph();
			XWPFRun runCuerpo = parrafoCuerpo.createRun();
			runCuerpo.setText(" ");
			
			XWPFTable tablaBody = documento.createTable(4, 6);
			configuraTabla(tablaBody,XWPFBorderType.SINGLE,false);
			
			int[] tams = {1340,1340,1340,1340,1340,1340};
			ajustaAnchoCeldas(tablaBody, tams);
			ajustaAlturaCeldas(tablaBody,4,400);
			
			mergeCellsHorizontally(tablaBody, 0, 1, 3); // Combinar las celdas de la fila 0, desde la columna 0 hasta la 3
			mergeCellsHorizontally(tablaBody, 1, 3, 5);
			
			combinarCeldasVerticalmente(tablaBody,2,0,3,0);
			mergeCellsHorizontally(tablaBody, 2, 1, 2);
			mergeCellsHorizontally(tablaBody, 2, 3, 5);
			
			mergeCellsHorizontally(tablaBody, 3, 1, 2);
			mergeCellsHorizontally(tablaBody, 3, 3, 5);
			
			setTextoCelda(tablaBody, 0, 0, "Asunto:",ParagraphAlignment.LEFT,10,"BFBFBF","000000",true);
			setTextoCelda(tablaBody, 0, 1, "REPORTE DE SERVICIO DE " + nMes.toUpperCase(),ParagraphAlignment.LEFT,10,"BFBFBF","000000",true);
			setTextoCelda(tablaBody, 0, 4, "REPORTE",ParagraphAlignment.LEFT,10,"BFBFBF","000000",true);
			setTextoCelda(tablaBody, 0, 5, strNumRep,ParagraphAlignment.CENTER,16,"FFFFFF","000000",true);
			
			setTextoCelda(tablaBody, 1, 0, "Fecha Corte:",ParagraphAlignment.LEFT,10,"FFFFFF","000000",true);
			setTextoCelda(tablaBody, 1, 1, ultimoDia,ParagraphAlignment.CENTER,10,"FFFFFF","000000",false);
			setTextoCelda(tablaBody, 1, 2, "Cliente:",ParagraphAlignment.LEFT,10,"FFFFFF","000000",true);
			setTextoCelda(tablaBody, 1, 3, rz,ParagraphAlignment.CENTER,10,"FFFFFF","000000",false);
			
			setTextoCelda(tablaBody, 2, 0, "Responsable:",ParagraphAlignment.LEFT,10,"FFFFFF","000000",true);
			setTextoCelda(tablaBody, 2, 1, "Nombre y Apellido",ParagraphAlignment.CENTER,10,"FFFFFF","000000",true);
			setTextoCelda(tablaBody, 2, 3, "Cargo",ParagraphAlignment.CENTER,10,"FFFFFF","000000",true);
			
			setTextoCelda(tablaBody, 3, 1, "Fernando Gomero",ParagraphAlignment.CENTER,10,"FFFFFF","000000",false);
			setTextoCelda(tablaBody, 3, 3, "Jefe de Desarrollo",ParagraphAlignment.CENTER,10,"FFFFFF","000000",false);
			
			//---------------------------------------------------------------//
			parrafoCuerpo = documento.createParagraph();
			runCuerpo = parrafoCuerpo.createRun();
			parrafoCuerpo.setSpacingAfter(0);
			runCuerpo.setText(" ");
			//---------------------------------------------------------------//
			tablaBody = documento.createTable(2, 10);
			configuraTabla(tablaBody,XWPFBorderType.SINGLE,false);
			
			int[] tams2 = {200,104,900,104,200,104,600,104,600,604};
			ajustaAnchoCeldas(tablaBody, tams2);
			ajustaAlturaCeldas(tablaBody,2,400);
			
			mergeCellsHorizontally(tablaBody, 0, 0, 9);
			
			setTextoCelda(tablaBody, 0, 0, "CLASIFICACIÓN",ParagraphAlignment.CENTER,10,"BFBFBF","000000",true);
			setTextoCelda(tablaBody, 1, 0, "Informativa",ParagraphAlignment.CENTER,10,"BFBFBF","000000",true);
			setTextoCelda(tablaBody, 1, 1, "X",ParagraphAlignment.CENTER,10,"FFFFFF","000000",true);
			setTextoCelda(tablaBody, 1, 2, "Control Avance",ParagraphAlignment.CENTER,10,"BFBFBF","000000",true);
			setTextoCelda(tablaBody, 1, 4, "Coordinación",ParagraphAlignment.CENTER,10,"BFBFBF","000000",true);
			setTextoCelda(tablaBody, 1, 6, "Decisión",ParagraphAlignment.CENTER,10,"BFBFBF","000000",true);
			setTextoCelda(tablaBody, 1, 8, "Otros",ParagraphAlignment.CENTER,10,"BFBFBF","000000",true);
			
			//---------------------------------------------------------------//
			parrafoCuerpo = documento.createParagraph();
			runCuerpo = parrafoCuerpo.createRun();
			parrafoCuerpo.setSpacingAfter(0);
			runCuerpo.setText(" ");
			//---------------------------------------------------------------//
			tablaBody = documento.createTable(3, 1);
			configuraTabla(tablaBody,XWPFBorderType.SINGLE,false);
			
			int[] tams3 = {8040};
			ajustaAnchoCeldas(tablaBody, tams3);
			ajustaAlturaCeldas(tablaBody,2,400);
			
			combinarCeldasVerticalmente(tablaBody,1,0,2,0);
			
			setTextoCelda(tablaBody, 0, 0, "OBJETIVOS DEL INFORME",ParagraphAlignment.CENTER,10,"BFBFBF","000000",true);
			setTextoCelda(tablaBody, 1, 0, "Brindar detalle de horas consumidas de soporte a servicios realizados a través de la mesa de ayuda de Informática Delta correspondiente al mes indicado en el asunto.",ParagraphAlignment.CENTER,10,"FFFFFF","000000",false);
			
			//---------------------------------------------------------------//
			parrafoCuerpo = documento.createParagraph();
			runCuerpo = parrafoCuerpo.createRun();
			parrafoCuerpo.setSpacingAfter(0);
			runCuerpo.setText(" ");
			//---------------------------------------------------------------//
			
			tablaBody = documento.createTable(2 + tamRegs, 7);
			configuraTabla(tablaBody,XWPFBorderType.SINGLE,false);
			
			int[] tams4 = {900,2640,900,900,900,900,900};
			ajustaAnchoCeldas(tablaBody, tams4);
			ajustaAlturaCeldas(tablaBody,2+tamRegs,300);
			
			setTextoCelda(tablaBody, 0, 0, "Ticket",ParagraphAlignment.CENTER,6,"3378CB","FFFFFF",true);
			setTextoCelda(tablaBody, 0, 1, "Descripción",ParagraphAlignment.CENTER,6,"3378CB","FFFFFF",true);
			setTextoCelda(tablaBody, 0, 2, "Horas Utilizadas",ParagraphAlignment.CENTER,6,"3378CB","FFFFFF",true);
			setTextoCelda(tablaBody, 0, 3, "Mes de Ejecución",ParagraphAlignment.CENTER,6,"3378CB","FFFFFF",true);
			setTextoCelda(tablaBody, 0, 4, "Mes de Facturación",ParagraphAlignment.CENTER,6,"3378CB","FFFFFF",true);
			setTextoCelda(tablaBody, 0, 5, "Fecha Finalización",ParagraphAlignment.CENTER,6,"3378CB","FFFFFF",true);
			setTextoCelda(tablaBody, 0, 6, "Estado",ParagraphAlignment.CENTER,6,"3378CB","FFFFFF",true);
			
			int item = 0;
			double total = 0;
			for(lstActi r : regActividades  ) {
				item++;
				setTextoCelda(tablaBody, item, 0, r.getNumero().toString(),ParagraphAlignment.CENTER,6,"FFFFFF","000000",false);
				setTextoCelda(tablaBody, item, 1, r.getDescrip(),ParagraphAlignment.LEFT,6,"FFFFFF","000000",false, 100, 0);
				setTextoCelda(tablaBody, item, 2, String.valueOf(r.getNroHoras()) ,ParagraphAlignment.CENTER,6,"FFFFFF","000000",false);
				setTextoCelda(tablaBody, item, 3, r.getPeriodoEje().split("-")[0] ,ParagraphAlignment.CENTER,6,"FFFFFF","000000",false);
				setTextoCelda(tablaBody, item, 4, nMes.substring(0, 1).toUpperCase() + nMes.substring(1).toLowerCase() ,ParagraphAlignment.CENTER,6,"FFFFFF","000000",false);
				setTextoCelda(tablaBody, item, 5, getFormatoFecha(r.getFecFin()) ,ParagraphAlignment.CENTER,6,"FFFFFF","000000",false);
				setTextoCelda(tablaBody, item, 6, "CERRADO" ,ParagraphAlignment.CENTER,6,"FFFFFF","000000",false);
				total += r.getNroHoras();
				
			}
			
			mergeCellsHorizontally(tablaBody, item+1, 0, 1);
			mergeCellsHorizontally(tablaBody, item+1, 3, 6);
			setTextoCelda(tablaBody, item + 1, 0, "TOTAL DE HORAS",ParagraphAlignment.RIGHT,6,"FFFFFF","000000",false, 0, 100);
			setTextoCelda(tablaBody, item + 1, 2, String.valueOf(total),ParagraphAlignment.CENTER,6,"FFFFFF","000000",true);
			
			//Graba documento
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			documento.write(outputStream);
			outputStream.close();
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDispositionFormData("attachment", "informe.docx");
			
			return ResponseEntity.ok()
					.headers(headers)
					.body(outputStream.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
	}
	
	private static void addPageNumberField(XWPFParagraph paragraph) {
        // Obtener el objeto CTP (párrafo) subyacente
        CTP ctp = paragraph.getCTP();

        // Crear un campo de número de página
        CTR ctr = ctp.addNewR();
        ctr.addNewFldChar().setFldCharType(STFldCharType.BEGIN);

        CTR ctr2 = ctp.addNewR();
        ctr2.addNewInstrText().setStringValue("PAGE");

        CTR ctr3 = ctp.addNewR();
        ctr3.addNewFldChar().setFldCharType(STFldCharType.END);
    }
	
	private static void addTotalPagesField(XWPFParagraph paragraph) {
        CTP ctp = paragraph.getCTP();
        CTR ctr = ctp.addNewR();
        ctr.addNewFldChar().setFldCharType(STFldCharType.BEGIN);

        CTR ctr2 = ctp.addNewR();
        ctr2.addNewInstrText().setStringValue("NUMPAGES");

        CTR ctr3 = ctp.addNewR();
        ctr3.addNewFldChar().setFldCharType(STFldCharType.END);
    }
	
	public static void ajustaAlturaCeldas(XWPFTable table, int nroFilas, int tam) {
		// Cambiar la altura de la primera fila y establecerla como fija
		XWPFTableRow fila;
		for(int i=0;i<nroFilas;i++) {
			 fila = table.getRow(i);
			 fila.setHeight(tam); // Altura en twips (1 twip = 1/1440 de pulgada)
			 fila.setHeightRule(TableRowHeightRule.EXACT); // Altura fija
		}
	}
	
	public boolean esFechaFutura(String fechaYYYYMM) {
	    if (fechaYYYYMM == null || !fechaYYYYMM.matches("\\d{4}-\\d{2}")) {
	        return false;
	    }

	    LocalDate fechaIngresada = LocalDate.parse(fechaYYYYMM + "-01");
	    LocalDate primerDiaDelMesActual = LocalDate.now().withDayOfMonth(1);

	    return fechaIngresada.isAfter(primerDiaDelMesActual);
	}
	
	public static String obtenerUltimoDia(int numYear, int numMes) {
		YearMonth yearMonth = YearMonth.of(numYear, numMes);
		int ultDia = yearMonth.atEndOfMonth().getDayOfMonth();
		return String.valueOf(ultDia)+"/"+String.format("%02d", numMes)+"/"+String.valueOf(numYear);
	}
	
	public static void ajustaAnchoCeldas(XWPFTable table, int[] sizes) {
		CTTblGrid tblGrid = table.getCTTbl().addNewTblGrid();
		
		for(int ancho: sizes) {
			CTTblGridCol gridCol = tblGrid.addNewGridCol();
			gridCol.setW(BigInteger.valueOf(ancho));
		}
		
		for(XWPFTableRow fila : table.getRows()) {
			for(int i = 0; i < fila.getTableCells().size(); i++) {
				XWPFTableCell celda = fila.getTableCells().get(i);
				CTTc cttc = celda.getCTTc();
				CTTcPr tcPr = cttc.isSetTcPr() ? cttc.getTcPr() : cttc.addNewTcPr();
				
				CTTblWidth tcWidth = tcPr.isSetTcW() ? tcPr.getTcW() : tcPr.addNewTcW();
				tcWidth.setW(BigInteger.valueOf(sizes[i]));
				tcWidth.setType(STTblWidth.DXA);
			}
		}
	}
	
	/**
     * Merges cells horizontally in a table.
     *
     * @param table       The table to modify.
     * @param row         The row index where merging should occur.
     * @param startCol    The starting column index.
     * @param endCol      The ending column index.
     */
	public static void mergeCellsHorizontally(XWPFTable table, int row, int startCol, int endCol) {
		if (row >= table.getNumberOfRows() || startCol >= table.getRow(row).getTableCells().size() || endCol >= table.getRow(row).getTableCells().size()) {
            throw new IllegalArgumentException("Invalid row or column indices.");
        }
		
		// Get the starting cell
        XWPFTableCell startCell = table.getRow(row).getCell(startCol);
        
        // Create a merge object for horizontal merging
        CTHMerge hMerge = CTHMerge.Factory.newInstance();
        hMerge.setVal(STMerge.RESTART); // Start the merge
        CTTcPr tcPr = startCell.getCTTc().getTcPr();
        if (tcPr == null) {
            tcPr = startCell.getCTTc().addNewTcPr();
        }
        tcPr.setHMerge(hMerge);
        
        // Merge the remaining cells
        for (int col = startCol + 1; col <= endCol; col++) {
            XWPFTableCell cell = table.getRow(row).getCell(col);
            CTTcPr cellTcPr = cell.getCTTc().getTcPr();
            if (cellTcPr == null) {
                cellTcPr = cell.getCTTc().addNewTcPr();
            }
            CTHMerge contMerge = CTHMerge.Factory.newInstance();
            contMerge.setVal(STMerge.CONTINUE); // Continue the merge
            cellTcPr.setHMerge(contMerge);
        }
	}
	
	public static void configuraTabla(XWPFTable table, XWPFBorderType tipoBorde, Boolean limpiar) {
		table.setWidth("100%");
		table.setBottomBorder(tipoBorde, 0, 0, null);
		table.setTopBorder(tipoBorde, 0, 0, null);
		table.setLeftBorder(tipoBorde, 0, 0, null);
		table.setRightBorder(tipoBorde, 0, 0, null);
		table.setInsideHBorder(tipoBorde, 0, 0, null);
		table.setInsideVBorder(tipoBorde, 0, 0, null);
		
		if (limpiar) {
			for (XWPFTableRow fila : table.getRows()) {
				for (XWPFTableCell celda : fila.getTableCells()) {
					for (int i = celda.getParagraphs().size() - 1; i >= 0; i--) {
						celda.removeParagraph(i);
					}
				}
			}
		}
	}
	
	public String getFormatoFecha(String fechaSinFormato) {
		DateTimeFormatter formatoEntrada = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        LocalDate fecha = LocalDate.parse(fechaSinFormato, formatoEntrada);
        DateTimeFormatter formatoSalida = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return fecha.format(formatoSalida);
	}
	
	public static void setTextoCelda(XWPFTable table, int fila, int col, 
            String texto, ParagraphAlignment alignment, 
            int tamLetra, String hexColor, String colorLetra, 
            Boolean negrita) {
		setTextoCelda(table, fila, col, texto, alignment, tamLetra, hexColor, colorLetra, negrita, 0, 0);
	}
	
	public static void setTextoCelda(XWPFTable table, int fila, int col, 
			                         String texto, ParagraphAlignment alignment, 
			                         int tamLetra, String hexColor, String colorLetra, 
			                         Boolean negrita, int indentLeft, int indentRight) {
		XWPFTableCell celda = table.getRow(fila).getCell(col);
		setCellBackgroundColor(celda, hexColor);
		
		for (int i = celda.getParagraphs().size() - 1; i >= 0; i--) {
			celda.removeParagraph(i);
		}
		
		celda.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
		XWPFParagraph parrafoCelda = celda.addParagraph();
		parrafoCelda.setAlignment(alignment);
		parrafoCelda.setIndentationLeft(indentLeft);
		parrafoCelda.setIndentationRight(indentRight);
		XWPFRun runCelda = parrafoCelda.createRun();
		runCelda.setColor(colorLetra);
		runCelda.setFontFamily("Arial");
		runCelda.setText(texto);
		runCelda.setBold(negrita);
		runCelda.setFontSize(tamLetra);
		
		// Configurar el espaciado del párrafo
        CTPPr pPr = parrafoCelda.getCTP().getPPr();
        if (pPr == null) {
            pPr = parrafoCelda.getCTP().addNewPPr();
        }
        CTSpacing spacing = pPr.isSetSpacing() ? pPr.getSpacing() : pPr.addNewSpacing();
        //spacing.setLine(BigInteger.valueOf(240)); // Espaciado entre líneas (240 twips = 1 línea)
        //spacing.setLineRule(STLineSpacingRule.AUTO); // Espaciado automático
    	spacing.setBefore(BigInteger.valueOf(0)); // Sin espaciado antes del párrafo
    	spacing.setAfter(BigInteger.valueOf(0)); // Sin espaciado después del párrafo

	}
	
	public static void setCellBackgroundColor(XWPFTableCell cell, String hexColor) {
        // Obtener las propiedades de la celda
        CTTcPr tcPr = cell.getCTTc().getTcPr();
        if (tcPr == null) {
            tcPr = cell.getCTTc().addNewTcPr();
        }

        // Crear un objeto CTShd (sombreado) para definir el color de fondo
        CTShd shd = tcPr.isSetShd() ? tcPr.getShd() : tcPr.addNewShd();
        shd.setFill(hexColor); // Establecer el color de fondo en formato HEX
    }
	
	public static void combinarCeldasVerticalmente(XWPFTable tabla, int filaInicio, int columna, int filaFin, int columnaFin) {
        for (int i = filaInicio; i <= filaFin; i++) {
            XWPFTableCell celda = tabla.getRow(i).getCell(columna);
            CTTcPr tcPr = celda.getCTTc().getTcPr();
            if (tcPr == null) {
                tcPr = celda.getCTTc().addNewTcPr();
            }

            // Configurar la fusión vertical
            CTVMerge vMerge = tcPr.isSetVMerge() ? tcPr.getVMerge() : tcPr.addNewVMerge();
            if (i == filaInicio) {
                vMerge.setVal(STMerge.RESTART); // Iniciar la fusión vertical
            } else {
                vMerge.setVal(STMerge.CONTINUE); // Continuar la fusión vertical
            }
        }
    }
	
	public static void limpiarCelda(XWPFTableCell celda) {
		for (int i = celda.getParagraphs().size() - 1; i >= 0; i--) {
			celda.removeParagraph(i);
		}
	}
	
	public static void limpiarCelda(XWPFTable table, int f, int c) {
		for(int fil=0; fil<f; fil++) {
			for(int col=0;col<c;col++) {
				XWPFTableCell celda = table.getRow(fil).getCell(col);
				for (int i = celda.getParagraphs().size() - 1; i >= 0; i--) {
					celda.removeParagraph(i);
				}
			}
		}
	}
	
	public byte[] generarInforme(ServicioInforme informe) throws Exception {
		String nombreMes = Month.of(Integer.valueOf(informe.getMesFac())).getDisplayName(TextStyle.FULL, new Locale("es","ES"));
		String ultimoDia = obtenerUltimoDia(Integer.valueOf(informe.getYearFac()), Integer.valueOf(informe.getMesFac()));
		List<Long> lstIds = Arrays.stream(informe.getListTickets().split(","))
                .map(String::trim)
                .map(Long::valueOf)
                .collect(Collectors.toList());

		List<ServicioActividades> regActividades = activService.lstFiltrado(informe.getCliente().getIdPerJur(), lstIds, 1);
		int tamRegs = regActividades.size();
		
		// crear word en base al informe guardado y retornar blob descargable
		try (XWPFDocument documento = new XWPFDocument()) {
			CTSectPr sectPr = documento.getDocument().getBody().addNewSectPr();
			
			//Establecer margen izquierdo y derecho
			CTPageMar pageMar = sectPr.addNewPgMar();
	        pageMar.setLeft(BigInteger.valueOf(1135)); 
	        pageMar.setRight(BigInteger.valueOf(1135));
	        
			//Agregar encabezado
			XWPFHeader encabezado = documento.createHeader(HeaderFooterType.DEFAULT);
			
			XWPFTable tabla = encabezado.createTable(3, 3);
			configuraTabla(tabla,XWPFBorderType.SINGLE,true);
			
			CTTblGrid tblGrid = tabla.getCTTbl().addNewTblGrid();
			
			int[] anchoColumnas = {1595,4650,1800};
			for(int ancho: anchoColumnas) {
				CTTblGridCol gridCol = tblGrid.addNewGridCol();
				gridCol.setW(BigInteger.valueOf(ancho));
			}
			
			for(XWPFTableRow fila : tabla.getRows()) {
				for(int i = 0; i < fila.getTableCells().size(); i++) {
					XWPFTableCell celda = fila.getTableCells().get(i);
					CTTc cttc = celda.getCTTc();
					CTTcPr tcPr = cttc.isSetTcPr() ? cttc.getTcPr() : cttc.addNewTcPr();
					
					CTTblWidth tcWidth = tcPr.isSetTcW() ? tcPr.getTcW() : tcPr.addNewTcW();
					tcWidth.setW(BigInteger.valueOf(anchoColumnas[i]));
					tcWidth.setType(STTblWidth.DXA);
				}
			}
			
			// Cambiar la altura de la primera fila y establecerla como fija
			ajustaAlturaCeldas(tabla,3,300);
	        
			combinarCeldasVerticalmente(tabla,0,0,2,0);
			combinarCeldasVerticalmente(tabla,0,1,2,1);
			
			//Agregar logo Delta
			XWPFTableCell celdaConImagen = tabla.getRow(0).getCell(0);
			celdaConImagen.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.BOTTOM);
			XWPFParagraph parrafoCelda = celdaConImagen.addParagraph();
			parrafoCelda.setAlignment(ParagraphAlignment.CENTER);
			XWPFRun runCelda = parrafoCelda.createRun();
			
			//Carga la imagen desde resource
			String archivo = "logoDelta.png";
			
			try (InputStream inputStream= Main.class.getClassLoader().getResourceAsStream(archivo)) {				
				runCelda.addPicture(inputStream, XWPFDocument.PICTURE_TYPE_PNG, archivo, Units.toEMU(82), Units.toEMU(42));
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
			
			CTDrawing drawing = runCelda.getCTR().getDrawingArray(0);
			CTInline inline = drawing.getInlineArray(0);
			CTPositiveSize2D extents = inline.getExtent();
			extents.setCx(Units.toEMU(82));
			extents.setCy(Units.toEMU(42));
			
			setTextoCelda(tabla, 0, 1, "REPORTE MENSUAL DE HORAS DE SERVICIO",ParagraphAlignment.CENTER,10,"FFFFFF","000000",true);
	        setTextoCelda(tabla, 0, 2, "Código: FO-GCA-16",ParagraphAlignment.LEFT,10,"FFFFFF","000000",true);
	        setTextoCelda(tabla, 1, 2, "Versión: 1.3",ParagraphAlignment.LEFT,10,"FFFFFF","000000",true);
	        setTextoCelda(tabla, 2, 2, "Fecha: 20/04/2022",ParagraphAlignment.LEFT,10,"FFFFFF","000000",true);
	        
	        // Crear un pie de página
	        XWPFFooter footer = documento.createFooter(HeaderFooterType.DEFAULT);

	        // Agregar un párrafo al pie de página
	        XWPFParagraph paragraph = footer.createParagraph();
	        paragraph.setAlignment(ParagraphAlignment.CENTER); // Centrar el texto

	        // Agregar texto con formato
	        XWPFRun run = paragraph.createRun();
	        run.setText("Página ");
	        addPageNumberField(paragraph);
	        run = paragraph.createRun();
	        run.setText(" de ");
	        addTotalPagesField(paragraph);

	        XWPFParagraph parrafoCuerpo = documento.createParagraph();
			XWPFRun runCuerpo = parrafoCuerpo.createRun();
			runCuerpo.setText(" ");
			
			XWPFTable tablaBody = documento.createTable(4, 6);
			configuraTabla(tablaBody,XWPFBorderType.SINGLE,false);
			
			int[] tams = {1340,1340,1340,1340,1340,1340};
			ajustaAnchoCeldas(tablaBody, tams);
			ajustaAlturaCeldas(tablaBody,4,400);
			
			mergeCellsHorizontally(tablaBody, 0, 1, 3); // Combinar las celdas de la fila 0, desde la columna 0 hasta la 3
			mergeCellsHorizontally(tablaBody, 1, 3, 5);
			
			combinarCeldasVerticalmente(tablaBody,2,0,3,0);
			mergeCellsHorizontally(tablaBody, 2, 1, 2);
			mergeCellsHorizontally(tablaBody, 2, 3, 5);
			
			mergeCellsHorizontally(tablaBody, 3, 1, 2);
			mergeCellsHorizontally(tablaBody, 3, 3, 5);
			
			setTextoCelda(tablaBody, 0, 0, "Asunto:",ParagraphAlignment.LEFT,10,"BFBFBF","000000",true);
			setTextoCelda(tablaBody, 0, 1, "REPORTE DE SERVICIO DE " + nombreMes.toUpperCase(),ParagraphAlignment.LEFT,10,"BFBFBF","000000",true);
			setTextoCelda(tablaBody, 0, 4, "REPORTE",ParagraphAlignment.LEFT,10,"BFBFBF","000000",true);
			setTextoCelda(tablaBody, 0, 5, "N° " + String.valueOf(informe.getNumero()),ParagraphAlignment.CENTER,16,"FFFFFF","000000",true);
			
			setTextoCelda(tablaBody, 1, 0, "Fecha Corte:",ParagraphAlignment.LEFT,10,"FFFFFF","000000",true);
			setTextoCelda(tablaBody, 1, 1, ultimoDia,ParagraphAlignment.CENTER,10,"FFFFFF","000000",false);
			setTextoCelda(tablaBody, 1, 2, "Cliente:",ParagraphAlignment.LEFT,10,"FFFFFF","000000",true);
			setTextoCelda(tablaBody, 1, 3, informe.getCliente().getRazonSocial(),ParagraphAlignment.CENTER,10,"FFFFFF","000000",false);
			
			setTextoCelda(tablaBody, 2, 0, "Responsable:",ParagraphAlignment.LEFT,10,"FFFFFF","000000",true);
			setTextoCelda(tablaBody, 2, 1, "Nombre y Apellido",ParagraphAlignment.CENTER,10,"FFFFFF","000000",true);
			setTextoCelda(tablaBody, 2, 3, "Cargo",ParagraphAlignment.CENTER,10,"FFFFFF","000000",true);
			
			setTextoCelda(tablaBody, 3, 1, "Fernando Gomero",ParagraphAlignment.CENTER,10,"FFFFFF","000000",false);
			setTextoCelda(tablaBody, 3, 3, "Jefe de Desarrollo",ParagraphAlignment.CENTER,10,"FFFFFF","000000",false);
			
			/*---------------------------------------------------------------*/
			parrafoCuerpo = documento.createParagraph();
			runCuerpo = parrafoCuerpo.createRun();
			parrafoCuerpo.setSpacingAfter(0);
			runCuerpo.setText(" ");
			/*---------------------------------------------------------------*/
			tablaBody = documento.createTable(2, 10);
			configuraTabla(tablaBody,XWPFBorderType.SINGLE,false);
			
			int[] tams2 = {200,104,900,104,200,104,600,104,600,604};
			ajustaAnchoCeldas(tablaBody, tams2);
			ajustaAlturaCeldas(tablaBody,2,400);
			
			mergeCellsHorizontally(tablaBody, 0, 0, 9);
			
			setTextoCelda(tablaBody, 0, 0, "CLASIFICACIÓN",ParagraphAlignment.CENTER,10,"BFBFBF","000000",true);
			setTextoCelda(tablaBody, 1, 0, "Informativa",ParagraphAlignment.CENTER,10,"BFBFBF","000000",true);
			setTextoCelda(tablaBody, 1, 1, "X",ParagraphAlignment.CENTER,10,"FFFFFF","000000",true);
			setTextoCelda(tablaBody, 1, 2, "Control Avance",ParagraphAlignment.CENTER,10,"BFBFBF","000000",true);
			setTextoCelda(tablaBody, 1, 4, "Coordinación",ParagraphAlignment.CENTER,10,"BFBFBF","000000",true);
			setTextoCelda(tablaBody, 1, 6, "Decisión",ParagraphAlignment.CENTER,10,"BFBFBF","000000",true);
			setTextoCelda(tablaBody, 1, 8, "Otros",ParagraphAlignment.CENTER,10,"BFBFBF","000000",true);
			
			/*---------------------------------------------------------------*/
			parrafoCuerpo = documento.createParagraph();
			runCuerpo = parrafoCuerpo.createRun();
			parrafoCuerpo.setSpacingAfter(0);
			runCuerpo.setText(" ");
			/*---------------------------------------------------------------*/
			tablaBody = documento.createTable(3, 1);
			configuraTabla(tablaBody,XWPFBorderType.SINGLE,false);
			
			int[] tams3 = {8040};
			ajustaAnchoCeldas(tablaBody, tams3);
			ajustaAlturaCeldas(tablaBody,2,400);
			
			combinarCeldasVerticalmente(tablaBody,1,0,2,0);
			
			setTextoCelda(tablaBody, 0, 0, "OBJETIVOS DEL INFORME",ParagraphAlignment.CENTER,10,"BFBFBF","000000",true);
			setTextoCelda(tablaBody, 1, 0, "Brindar detalle de horas consumidas de soporte a servicios realizados a través de la mesa de ayuda de Informática Delta correspondiente al mes indicado en el asunto.",ParagraphAlignment.CENTER,10,"FFFFFF","000000",false);
			
			/*---------------------------------------------------------------*/
			parrafoCuerpo = documento.createParagraph();
			runCuerpo = parrafoCuerpo.createRun();
			parrafoCuerpo.setSpacingAfter(0);
			runCuerpo.setText(" ");
			/*---------------------------------------------------------------*/
			
			tablaBody = documento.createTable(2 + tamRegs, 7);
			configuraTabla(tablaBody,XWPFBorderType.SINGLE,false);
			
			int[] tams4 = {900,2640,900,900,900,900,900};
			ajustaAnchoCeldas(tablaBody, tams4);
			ajustaAlturaCeldas(tablaBody,2+tamRegs,300);
			
			setTextoCelda(tablaBody, 0, 0, "Ticket",ParagraphAlignment.CENTER,6,"3378CB","FFFFFF",true);
			setTextoCelda(tablaBody, 0, 1, "Descripción",ParagraphAlignment.CENTER,6,"3378CB","FFFFFF",true);
			setTextoCelda(tablaBody, 0, 2, "Horas Utilizadas",ParagraphAlignment.CENTER,6,"3378CB","FFFFFF",true);
			setTextoCelda(tablaBody, 0, 3, "Mes de Ejecución",ParagraphAlignment.CENTER,6,"3378CB","FFFFFF",true);
			setTextoCelda(tablaBody, 0, 4, "Mes de Facturación",ParagraphAlignment.CENTER,6,"3378CB","FFFFFF",true);
			setTextoCelda(tablaBody, 0, 5, "Fecha Finalización",ParagraphAlignment.CENTER,6,"3378CB","FFFFFF",true);
			setTextoCelda(tablaBody, 0, 6, "Estado",ParagraphAlignment.CENTER,6,"3378CB","FFFFFF",true);
			
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			
			int item = 0;
			double total = 0;
			for(ServicioActividades r : regActividades  ) {
				item++;
				setTextoCelda(tablaBody, item, 0, r.getNumero().toString(),ParagraphAlignment.CENTER,6,"FFFFFF","000000",false);
				setTextoCelda(tablaBody, item, 1, r.getDescrip(),ParagraphAlignment.LEFT,6,"FFFFFF","000000",false, 100, 0);
				setTextoCelda(tablaBody, item, 2, String.valueOf(r.getHoras()) ,ParagraphAlignment.CENTER,6,"FFFFFF","000000",false);
				setTextoCelda(tablaBody, item, 3, r.getMesEje() ,ParagraphAlignment.CENTER,6,"FFFFFF","000000",false);
				setTextoCelda(tablaBody, item, 4, nombreMes.substring(0, 1).toUpperCase() + nombreMes.substring(1).toLowerCase() ,ParagraphAlignment.CENTER,6,"FFFFFF","000000",false);
				setTextoCelda(tablaBody, item, 5, formatter.format(r.getFechaFin()) ,ParagraphAlignment.CENTER,6,"FFFFFF","000000",false);
				setTextoCelda(tablaBody, item, 6, "CERRADO" ,ParagraphAlignment.CENTER,6,"FFFFFF","000000",false);
				total += r.getHoras();
				
			}
			
			mergeCellsHorizontally(tablaBody, item+1, 0, 1);
			mergeCellsHorizontally(tablaBody, item+1, 3, 6);
			setTextoCelda(tablaBody, item + 1, 0, "TOTAL DE HORAS  ",ParagraphAlignment.RIGHT,6,"FFFFFF","000000",false,0,100);
			setTextoCelda(tablaBody, item + 1, 2, String.valueOf(total),ParagraphAlignment.CENTER,6,"FFFFFF","000000",true);
			
			//Graba documento
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			documento.write(outputStream);
			outputStream.close();
			
			return outputStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public class lstCombo {
		public Long id;
		public String desItem1;
		public String desItem2;
		
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getDesItem1() {
			return desItem1;
		}
		public void setDesItem1(String desItem1) {
			this.desItem1 = desItem1;
		}
		public String getDesItem2() {
			return desItem2;
		}
		public void setDesItem2(String desItem2) {
			this.desItem2 = desItem2;
		}
	}
	
	public class regActi {
		public Long id;
		public Long idCliente;
		public Long idTipo;
		public Long idforma;
		public Long idEstado;
		public Long idResolutor;
		public Long numero;
		public String descrip;
		public double nroHoras;
		public String fecIni;
		public String fecFin;
		public String mesEje;
		public String yyEje;
		public String mmFac;
		public String yyFac;
		public String observacion;
		public String facturable;

		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		
		public Long getIdCliente() {
			return idCliente;
		}
		public void setIdCliente(Long idCliente) {
			this.idCliente = idCliente;
		}
		public Long getIdTipo() {
			return idTipo;
		}
		public void setIdTipo(Long idTipo) {
			this.idTipo = idTipo;
		}
		public Long getIdforma() {
			return idforma;
		}
		public void setIdforma(Long idforma) {
			this.idforma = idforma;
		}
		public Long getIdEstado() {
			return idEstado;
		}
		public void setIdEstado(Long idEstado) {
			this.idEstado = idEstado;
		}
		public Long getIdResolutor() {
			return idResolutor;
		}
		public void setIdResolutor(Long idResolutor) {
			this.idResolutor = idResolutor;
		}
		public Long getNumero() {
			return numero;
		}
		public void setNumero(Long numero) {
			this.numero = numero;
		}
		public String getDescrip() {
			return descrip;
		}
		public void setDescrip(String descrip) {
			this.descrip = descrip;
		}
		public double getNroHoras() {
			return nroHoras;
		}
		public void setNroHoras(double nroHoras) {
			this.nroHoras = nroHoras;
		}
		public String getFecIni() {
			return fecIni;
		}
		public void setFecIni(String fecIni) {
			this.fecIni = fecIni;
		}
		public String getFecFin() {
			return fecFin;
		}
		public void setFecFin(String fecFin) {
			this.fecFin = fecFin;
		}
		public String getMesEje() {
			return mesEje;
		}
		public void setMesEje(String mesEje) {
			this.mesEje = mesEje;
		}
		public String getYyEje() {
			return yyEje;
		}
		public void setYyEje(String yyEje) {
			this.yyEje = yyEje;
		}
		public String getMmFac() {
			return mmFac;
		}
		public void setMmFac(String mmFac) {
			this.mmFac = mmFac;
		}
		public String getYyFac() {
			return yyFac;
		}
		public void setYyFac(String yyFac) {
			this.yyFac = yyFac;
		}
		public String getObservacion() {
			return observacion;
		}
		public void setObservacion(String observacion) {
			this.observacion = observacion;
		}
		public String getFacturable() {
			return facturable;
		}
		public void setFacturable(String facturable) {
			this.facturable = facturable;
		}
	}
}

